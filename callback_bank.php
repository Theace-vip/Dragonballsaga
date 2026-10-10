<?php
/**
 * Webhook nhan giao dich ngan hang tu SePay (Sacombank).
 * Cau hinh tai my.sepay.vn -> Cau hinh cong ty -> Webhook:
 *   URL      : https://nrokura.site/callback_bank.php
 *   Xac thuc : API Key  ->  gia tri $sepay_secret trong config.php
 *   Su kien  : "Tien vao"
 *
 * Luon tra ve {"success": true} de SePay khong gui lai (tru khi loi CSDL -> 500).
 */
require_once __DIR__ . '/bank_nap.php';

header('Content-Type: application/json; charset=utf-8');

$raw = file_get_contents('php://input');

// ---- Xac thuc: header "Authorization: Apikey <secret>" hoac chu ky HMAC-SHA256 ----
$headers = function_exists('getallheaders') ? getallheaders() : [];
if (!$headers && !empty($_SERVER['HTTP_AUTHORIZATION'])) {
    $headers['Authorization'] = $_SERVER['HTTP_AUTHORIZATION'];
}
$auth = '';
$sig  = '';
foreach ($headers as $k => $v) {
    $lk = strtolower($k);
    if ($lk === 'authorization') {
        $auth = (string) $v;
    } elseif ($lk === 'x-sepay-signature') {
        $sig = (string) $v;
    }
}

$authorized = false;
if ($auth !== '' && hash_equals('Apikey ' . $sepay_secret, $auth)) {
    $authorized = true;
} elseif ($sig !== '' && $sepay_secret !== '') {
    $authorized = hash_equals(hash_hmac('sha256', $raw, $sepay_secret), $sig);
}

if (!$authorized) {
    bank_log('bank_webhook_raw.txt', 'UNAUTHORIZED | header="' . $auth . '" | body=' . substr($raw, 0, 500));
    http_response_code(401);
    echo json_encode(['success' => false, 'message' => 'Unauthorized']);
    exit;
}

$data = json_decode($raw, true);
if (!is_array($data)) {
    bank_log('bank_webhook_raw.txt', 'INVALID_JSON | ' . substr($raw, 0, 1000));
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'JSON khong hop le']);
    exit;
}

bank_log('bank_webhook_raw.txt', substr($raw, 0, 2000));

// Ho tro 2 dang payload: 1 giao dich (SePay) va {"transactions":[...]}
$list = (isset($data['transactions']) && is_array($data['transactions'])) ? $data['transactions'] : [$data];

$credited = 0;
$skipped  = 0;
$failed   = 0;

foreach ($list as $t) {
    if (!is_array($t)) {
        continue;
    }
    // id = null => bo qua (theo tai lieu SePay)
    if (!isset($t['id']) || $t['id'] === null || $t['id'] === '') {
        $skipped++;
        continue;
    }
    $type = $t['transferType'] ?? ($t['transfer_type'] ?? 'in');
    if ($type !== 'in') {
        $skipped++;
        continue;
    }
    $amount  = $t['transferAmount'] ?? ($t['amount_in'] ?? 0);
    $content = (string) ($t['content'] ?? ($t['transaction_content'] ?? ($t['description'] ?? '')));

    $r = bank_credit($conn, $content, $amount, sepay_tx_code($t));
    if ($r['status'] === 'credited') {
        $credited++;
    } elseif ($r['status'] === 'db_error') {
        $failed++;
    } else {
        $skipped++;
    }
}

if ($failed > 0) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Co ' . $failed . ' giao dich loi CSDL, hay thu lai']);
    exit;
}

echo json_encode([
    'success'  => true,
    'credited' => $credited,
    'skipped'  => $skipped,
    'message'  => 'Da xu ly ' . count($list) . ' giao dich',
]);
