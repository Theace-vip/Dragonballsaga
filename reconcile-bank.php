<?php
/**
 * Cron doi soat giao dich ngan hang (chay bang CLI).
 *
 * Vi du Task Scheduler (Windows) / cron (Linux) chay moi 2 phut:
 *   C:\xampp\php\php.exe C:\xampp\htdocs\nrokura\reconcile-bank.php
 *
 * Yeu cau: $sepay_api_token trong config.php (my.sepay.vn -> Cau hinh cong ty -> API Access).
 * Giao dich da duoc webhook xu ly (webhook_success = 1) se bo qua neu
 * $sepay_skip_webhook_ok = true, tranh cong trung.
 */
if (PHP_SAPI !== 'cli') {
    http_response_code(403);
    exit('CLI only');
}

require_once __DIR__ . '/bank_nap.php';

if (empty($sepay_api_token)) {
    fwrite(STDERR, "[reconcile-bank] Chua cau hinh \$sepay_api_token trong config.php\n");
    exit(1);
}

$cursorFile = bank_data_path('bank_since_id.txt');
$since      = is_file($cursorFile) ? trim((string) @file_get_contents($cursorFile)) : '';

$query = [
    'transfer_type' => 'in',
    'per_page'      => 100,
];
if ($since !== '') {
    $query['since_id'] = $since;
} else {
    // Lan dau chay: chi lay giao dich trong 24h gan nhat, tranh cong lai lich su cu
    $query['transaction_date_from'] = date('Y-m-d H:i:s', time() - 86400);
}
if (!empty($sepay_bank_account_id)) {
    $query['bank_account_id'] = $sepay_bank_account_id;
}

list($httpCode, $body, $curlErr) = sepay_api_get('/transactions', $query);
if ($httpCode !== 200) {
    bank_log('bank_nap.log', 'RECONCILE_API_ERROR | http=' . $httpCode . ' | err=' . $curlErr . ' | body=' . substr((string) $body, 0, 300));
    fwrite(STDERR, "[reconcile-bank] SePay API loi HTTP $httpCode $curlErr\n");
    exit(1);
}

$json = json_decode((string) $body, true);
$rows = (is_array($json) && isset($json['data']) && is_array($json['data'])) ? $json['data'] : [];

$credited = 0;
$skipped  = 0;
$failed   = 0;
$lastId   = $since;

foreach ($rows as $t) {
    if (!is_array($t)) {
        continue;
    }
    if (($t['transfer_type'] ?? 'in') !== 'in') {
        $skipped++;
        continue;
    }
    $amount = (int) ($t['amount_in'] ?? 0);
    if ($amount <= 0) {
        $skipped++;
        continue;
    }

    $id = (string) ($t['id'] ?? '');
    if ($id !== '') {
        $lastId = $id;
    }

    if ($sepay_skip_webhook_ok && isset($t['webhook_success']) && (int) $t['webhook_success'] === 1) {
        $skipped++;
        continue;
    }

    $r = bank_credit($conn, (string) ($t['transaction_content'] ?? ''), $amount, sepay_tx_code($t));
    if ($r['status'] === 'credited') {
        $credited++;
    } elseif ($r['status'] === 'db_error') {
        $failed++;
    } else {
        $skipped++;
    }
}

if ($lastId !== '' && $lastId !== $since) {
    @file_put_contents($cursorFile, $lastId, LOCK_EX);
}

$msg = '[reconcile-bank] ' . date('Y-m-d H:i:s') . " | doc=" . count($rows) . " | cong=$credited | bo_qua=$skipped | loi=$failed\n";
echo $msg;
bank_log('bank_nap.log', trim($msg));
exit($failed > 0 ? 1 : 0);
