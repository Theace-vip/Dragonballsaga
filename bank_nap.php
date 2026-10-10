<?php
/**
 * Loi cong tien nap qua ngan hang (Sacombank) - dung chung cho:
 *   - callback_bank.php   : webhook realtime tu SePay
 *   - check-bank.php      : nut "Kiem tra nap" tren trang nap tien
 *   - reconcile-bank.php  : cron doi soat dinh ky (CLI)
 *
 * Nguyen tac an toan:
 *   1. Chong trung bang UNIQUE(username, code) cua bang history_bank:
 *      INSERT lich su TRUOC, chi cong tien khi INSERT thanh cong (trong 1 transaction).
 *   2. Tien nap ghi vao account.temp_vnd (so du cho) + tongnap + danap.
 *      Game se tu cong temp_vnd vao vnd khi nguoi choi vao game
 *      (BeMeoGaming/src/jdbc/daos/NDVSqlFetcher.java:88 - session.vnd = vnd + temp_vnd).
 *   3. Ty le nap (su kien x2/x3/.../x50) do ControlPanel (panel Java server game)
 *      dat trong bang panel_nap_rate -> web doc truc tiep, ap dung ngay cho
 *      chuyen khoan. Tran toi da x50, het han theo until_at thi tu tat.
 */
require_once __DIR__ . '/config.php';

/**
 * Duong dan file du lieu noi bo (log giao dich, con tro doi soat).
 * Mac dinh nam NGOAI webroot (C:/xampp/nrokura_private) nen khong the tai
 * qua http(s), du .htaccess hay cau hinh vhost co bi sai.
 * Ghi de bang $bank_data_dir trong config.php neu doi cho luu.
 */
function bank_data_path($name)
{
    global $bank_data_dir;

    $dir = (isset($bank_data_dir) && $bank_data_dir !== '')
        ? $bank_data_dir
        : dirname(__DIR__, 2) . DIRECTORY_SEPARATOR . 'nrokura_private';

    if (!is_dir($dir)) {
        @mkdir($dir, 0700, true);
    }

    return rtrim($dir, '/\\') . '/' . $name;
}

/**
 * Ty le nap hien tai (su kien x2/x3/.../x50) - doc tu bang panel_nap_rate
 * do ControlPanel (panel Java) ghi vao cung DB game. Tra ve 1.0 neu:
 * chua co bang/chua dat/khong bat/hoac da qua until_at.
 */
function bank_nap_rate_multiplier($conn)
{
    static $cache = null;
    if ($cache !== null) {
        return $cache;
    }
    $cache = 1.0;

    $res = @$conn->query("SELECT rate, enabled, until_at FROM panel_nap_rate WHERE id = 1 LIMIT 1");
    if ($res && ($row = $res->fetch_assoc())) {
        $rate  = (int) ($row['rate'] ?? 1);
        $on    = (int) ($row['enabled'] ?? 0) === 1;
        $until = trim((string) ($row['until_at'] ?? ''));

        if ($until !== '' && strtotime($until) !== false && time() > strtotime($until)) {
            $on = false; // het han su kien
        }
        if ($on && $rate > 1) {
            $cache = (float) min(50, max(1, $rate));
        }
    }

    return $cache;
}

/** Ghi log don gian (luon ghi ra ngoai webroot) */
function bank_log($file, $line)
{
    @file_put_contents(bank_data_path($file), '[' . date('Y-m-d H:i:s') . '] ' . $line . "\n", FILE_APPEND | LOCK_EX);
}

/**
 * Tach cac username ung vien tu noi dung chuyen khoan.
 * Chap nhan: "naptientheace", "naptien_theace", "naptien theace", "naptien:theace"
 */
function bank_username_candidates($content, $prefix)
{
    $content = strtolower((string) $content);
    $prefix  = strtolower((string) $prefix);
    if ($content === '' || $prefix === '') {
        return [];
    }
    $out = [];
    if (preg_match_all('/' . preg_quote($prefix, '/') . '[\s:_\-]*([a-z0-9][a-z0-9_]{2,19})/', $content, $m)) {
        foreach ($m[1] as $c) {
            $out[] = $c;
        }
    }
    return array_values(array_unique($out));
}

/**
 * Tim tai khoan tu noi dung chuyen khoan.
 * Buoc 1: cac ung vien ngay sau tien to "naptien".
 * Buoc 2 (du phong): noi dung co chua username nao dang ton tai trong DB khong
 *         (bank doi khi cat dau _ hoac chen them chu).
 */
function bank_find_user($conn, $content, $prefix)
{
    $sql = "SELECT id, username, vnd, temp_vnd, tongnap, danap FROM account WHERE LOWER(username) = ? LIMIT 1";
    foreach (bank_username_candidates($content, $prefix) as $cand) {
        $stmt = $conn->prepare($sql);
        $stmt->bind_param('s', $cand);
        $stmt->execute();
        $row = $stmt->get_result()->fetch_assoc();
        if ($row) {
            return $row;
        }
    }

    $compact = preg_replace('/[^a-z0-9_]/', '', strtolower((string) $content));
    if ($compact === '') {
        return null;
    }
    $res = $conn->query("SELECT id, username, vnd, temp_vnd, tongnap, danap FROM account WHERE CHAR_LENGTH(username) >= 4 LIMIT 20000");
    while ($res && ($row = $res->fetch_assoc())) {
        if (strpos($compact, strtolower($row['username'])) !== false) {
            return $row;
        }
    }
    return null;
}

/**
 * Cong tien cho 1 giao dich.
 *
 * @param mysqli     $conn
 * @param string     $content        noi dung chuyen khoan (dung de tim username)
 * @param int|float  $amount_vnd     so tien thuc nhan (VND)
 * @param string     $code           ma giao dich duy nhat (chong trung)
 * @param string|null $force_username chi cong cho dung user nay (dung cho nut kiem tra)
 * @return array status: credited | duplicate | no_user | bad_amount | no_code | db_error
 */
function bank_credit($conn, $content, $amount_vnd, $code, $force_username = null)
{
    global $chietkhau_bank, $bank_heso, $noidung_bank;

    $amount_vnd = (int) round((float) $amount_vnd);
    $code       = substr(trim((string) $code), 0, 100);
    $content    = (string) $content;

    if ($amount_vnd <= 0) {
        return ['status' => 'bad_amount', 'message' => 'So tien khong hop le'];
    }
    if ($code === '') {
        return ['status' => 'no_code', 'message' => 'Thieu ma giao dich'];
    }

    if ($force_username !== null && $force_username !== '') {
        $stmt = $conn->prepare("SELECT id, username, vnd, temp_vnd, tongnap, danap FROM account WHERE LOWER(username) = ? LIMIT 1");
        $u = strtolower((string) $force_username);
        $stmt->bind_param('s', $u);
        $stmt->execute();
        $user = $stmt->get_result()->fetch_assoc();
    } else {
        $user = bank_find_user($conn, $content, $noidung_bank);
    }

    if (!$user) {
        bank_log('bank_nap.log', 'NO_USER | code=' . $code . ' | amount=' . $amount_vnd . ' | content="' . $content . '"');
        return ['status' => 'no_user', 'message' => 'Noi dung khong khop tai khoan nao'];
    }

    $chietkhau = (float) (isset($chietkhau_bank) ? $chietkhau_bank : 0);
    $heso      = (float) (isset($bank_heso) ? $bank_heso : 1);
    // Ty le nap (su kien x2/x3/.../x50) - dat o ControlPanel -> bang panel_nap_rate
    $tyle      = bank_nap_rate_multiplier($conn);
    $net       = (int) round($amount_vnd * $heso * $tyle * (1 - $chietkhau / 100));
    if ($net <= 0) {
        return ['status' => 'bad_amount', 'message' => 'So tien sau chiet khau khong hop le'];
    }

    $errno = 0;
    $err   = '';
    try {
        $conn->begin_transaction();

        // 1) Ghi lich su truoc: trung code => khong cong tien
        $ins = $conn->prepare("INSERT INTO history_bank (username, amount_vnd, amount_cash, description, code, created_at) VALUES (?, ?, ?, ?, ?, NOW())");
        $ins->bind_param('sddss', $user['username'], $amount_vnd, $net, $content, $code);
        if ($ins->execute() === false) {
            $errno = $ins->errno;
            $err   = $conn->error;
            throw new RuntimeException('insert_failed');
        }

        // 2) Cong tien vao so du cho + tong nap + thong ke nap
        $upd = $conn->prepare("UPDATE account SET temp_vnd = temp_vnd + ?, tongnap = tongnap + ?, danap = danap + ? WHERE id = ?");
        $accId = (int) $user['id'];
        $upd->bind_param('iiii', $net, $net, $net, $accId);
        if ($upd->execute() === false) {
            $errno = $upd->errno;
            $err   = $conn->error;
            throw new RuntimeException('update_failed');
        }

        $conn->commit();
    } catch (Throwable $e) {
        try {
            $conn->rollback();
        } catch (Throwable $e2) {
        }
        if ($errno === 0) {
            $errno = (int) $e->getCode();
            $err   = $e->getMessage();
        }
        if ($errno === 1062 || stripos($err, 'Duplicate') !== false) {
            return ['status' => 'duplicate', 'message' => 'Giao dich da duoc cong truoc do', 'username' => $user['username']];
        }
        bank_log('bank_nap.log', 'DB_ERROR | user=' . $user['username'] . ' | code=' . $code . ' | ' . $err);
        return ['status' => 'db_error', 'message' => 'Loi ghi CSDL: ' . $err, 'username' => $user['username']];
    }

    // Lay lai so du de bao cao
    $stmt = $conn->prepare("SELECT vnd, temp_vnd, tongnap FROM account WHERE id = ?");
    $accId = (int) $user['id'];
    $stmt->bind_param('i', $accId);
    $stmt->execute();
    $after = $stmt->get_result()->fetch_assoc();

    bank_log('bank_nap.log', 'CREDITED | user=' . $user['username'] . ' | code=' . $code
        . ' | amount=' . $amount_vnd . ' | net=' . $net . ' | temp_vnd=' . (int) ($after['temp_vnd'] ?? 0)
        . ' | content="' . $content . '"');

    return [
        'status'     => 'credited',
        'message'    => 'Nap thanh cong',
        'username'   => $user['username'],
        'amount_vnd' => $amount_vnd,
        'net'        => $net,
        'temp_vnd'   => (int) ($after['temp_vnd'] ?? 0),
        'tongnap'    => (int) ($after['tongnap'] ?? 0),
    ];
}

/**
 * Goi SePay API v2 (GET).
 * @return array [http_code, body, error]
 */
function sepay_api_get($path, array $query = [])
{
    global $sepay_api_token, $sepay_api_base;

    $url = rtrim($sepay_api_base, '/') . $path;
    if ($query) {
        $url .= '?' . http_build_query($query);
    }
    $ch = curl_init($url);
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_HTTPHEADER     => [
            'Authorization: Bearer ' . $sepay_api_token,
            'Content-Type: application/json',
        ],
        CURLOPT_TIMEOUT        => 25,
        CURLOPT_CONNECTTIMEOUT => 10,
    ]);
    $body = curl_exec($ch);
    $code = (int) curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $err  = (string) curl_error($ch);
    curl_close($ch);

    return [$code, $body, $err];
}

/** Ma giao dich chong trung tu 1 ban ghi SePay (webhook hoac API) */
function sepay_tx_code(array $t)
{
    // id la khoa chong trung on dinh cua SePay (khong doi qua moi lan retry/replay).
    // Voi API v2, id la UUID; webhook gui id dang so. Du phong: ma tham chieu ngan hang.
    $id  = $t['id'] ?? '';
    $ref = $t['referenceCode'] ?? ($t['reference_number'] ?? '');
    $key = ($id !== '' && $id !== null) ? $id : $ref;
    return 'sp_' . substr((string) $key, 0, 90);
}
