<?php
/**
 * Nut "Kiem tra nap" tren trang nap tien.
 * Goi SePay API v2 de tim giao dich tien vao khop noi dung cua nguoi choi dang dang nhap,
 * roi cong tien cho nhung giao dich chua duoc xu ly.
 * Du phong cho truong hop webhook bi lo (server restart, SePay khong goi duoc...).
 */
require_once __DIR__ . '/bank_nap.php';

header('Content-Type: application/json; charset=utf-8');

function check_bank_reply($ok, $message, array $extra = [])
{
    echo json_encode(array_merge(['success' => (bool) $ok, 'message' => $message], $extra), JSON_UNESCAPED_UNICODE);
    exit;
}

if (empty($_SESSION['account'])) {
    http_response_code(401);
    check_bank_reply(false, 'Ban can dang nhap de kiem tra nap tien.');
}
if (($_SERVER['REQUEST_METHOD'] ?? '') !== 'POST') {
    http_response_code(405);
    check_bank_reply(false, 'Chi ho tro POST.');
}

$username = (string) $_SESSION['account'];

// ---- Chong spam: 1 lan / 10 giay cho moi tai khoan ----
$stateFile = __DIR__ . '/bank_check_state.json';
$state     = [];
if (is_file($stateFile)) {
    $state = json_decode((string) @file_get_contents($stateFile), true);
    if (!is_array($state)) {
        $state = [];
    }
}
$last = (int) ($state[$username] ?? 0);
if (time() - $last < 10) {
    check_bank_reply(true, 'Vua kiem tra xong, cho ' . (10 - (time() - $last)) . ' giay roi thu lai.', ['credited' => 0]);
}
$state[$username] = time();
@file_put_contents($stateFile, json_encode($state), LOCK_EX);

if (empty($sepay_api_token)) {
    check_bank_reply(false, 'Chua cau hinh SePay API token ($sepay_api_token trong config.php). Sau khi lien ket ngan hang tren my.sepay.vn, tao API Token o Cau hinh cong ty -> API Access roi dien vao config.php.');
}

// ---- Goi SePay API v2: tim giao dich tien vao co noi dung cua nguoi choi ----
$query = [
    'transfer_type' => 'in',
    'q'             => $noidung_bank . $username,
    'per_page'      => 50,
];
if (!empty($sepay_bank_account_id)) {
    $query['bank_account_id'] = $sepay_bank_account_id;
}

list($httpCode, $body, $curlErr) = sepay_api_get('/transactions', $query);

if ($httpCode !== 200) {
    bank_log('bank_nap.log', 'CHECK_API_ERROR | user=' . $username . ' | http=' . $httpCode . ' | err=' . $curlErr . ' | body=' . substr((string) $body, 0, 300));
    check_bank_reply(false, 'Khong goi duoc SePay API (HTTP ' . $httpCode . '). Kiem tra lai API token / ket noi mang.', ['http' => $httpCode, 'error' => $curlErr]);
}

$json = json_decode((string) $body, true);
$rows = (is_array($json) && isset($json['data']) && is_array($json['data'])) ? $json['data'] : [];

$credited  = 0;
$duplicate = 0;
$total     = 0;

foreach ($rows as $t) {
    if (!is_array($t)) {
        continue;
    }
    if (($t['transfer_type'] ?? 'in') !== 'in') {
        continue;
    }
    $amount = (int) ($t['amount_in'] ?? 0);
    if ($amount <= 0) {
        continue;
    }
    $content = (string) ($t['transaction_content'] ?? '');
    $total++;

    // Giao dich da duoc webhook xu ly thanh cong thi bo qua (tranh cong 2 lan)
    if ($sepay_skip_webhook_ok && isset($t['webhook_success']) && (int) $t['webhook_success'] === 1) {
        $duplicate++;
        continue;
    }

    // Chi cong cho dung nguoi dang dang nhap
    $r = bank_credit($conn, $content, $amount, sepay_tx_code($t), $username);
    if ($r['status'] === 'credited') {
        $credited++;
    } elseif ($r['status'] === 'duplicate') {
        $duplicate++;
    }
}

$state[$username] = time();
@file_put_contents($stateFile, json_encode($state), LOCK_EX);

if ($credited > 0) {
    check_bank_reply(true, 'Da cong tien cho ' . $credited . ' giao dich. Vao game de so du duoc cap nhat.', ['credited' => $credited, 'found' => $total]);
}
if ($duplicate > 0 && $total > 0) {
    check_bank_reply(true, 'Cac giao dich nay da duoc cong truoc do. Vao game kiem tra so du.', ['credited' => 0, 'found' => $total]);
}
check_bank_reply(true, 'Chua tim thay giao dich nao khop "noi dung = ' . $noidung_bank . $username . '". Neu ban vua chuyen, doi 1-2 phut roi bam lai.', ['credited' => 0, 'found' => $total]);
