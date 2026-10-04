<?php
session_start();
header('Content-Type: application/json');
require_once 'config.php';

if (!isset($_SESSION['username'])) {
    echo json_encode(['error' => 'Bạn chưa đăng nhập!']);
    exit;
}

$username = $_SESSION['username'];
$eggId = isset($_GET['egg']) ? intval($_GET['egg']) : 0;

if ($eggId < 1 || $eggId > 8) {
    echo json_encode(['error' => 'Quả trứng không hợp lệ!']);
    exit;
}

// Lấy thông tin người chơi
$stmt = $conn->prepare("SELECT vnd FROM account WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();

if (!$user) {
    echo json_encode(['error' => 'Không tìm thấy tài khoản!']);
    exit;
}

$vnd = $user['vnd'];

// Kiểm tra tiền
if ($vnd < 50000) {
    echo json_encode(['error' => 'Không đủ VND để chơi!']);
    exit;
}

// Trừ tiền trước
$new_vnd = $vnd - 50000;
$update = $conn->prepare("UPDATE account SET vnd = ? WHERE username = ?");
$update->bind_param("is", $new_vnd, $username);
$update->execute();

// Xác định VIP (random server)
$vipEgg = rand(1, 8);
if ($eggId == $vipEgg) {
    $message = "🎉 Chúc mừng! Bạn trúng trứng VIP!";
    $vip = true;
} else {
    $message = "❌ Không trúng VIP. Chúc may mắn lần sau!";
    $vip = false;
}

echo json_encode([
    'vip' => $vip,
    'message' => $message,
    'vnd' => $new_vnd
]);
