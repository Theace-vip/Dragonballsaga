<?php
session_start();
header('Content-Type: application/json');

$conn = new mysqli("localhost", "root", "", "hondaodragon");
if ($conn->connect_error) die(json_encode(['error' => 'Lỗi kết nối DB']));

if (!isset($_SESSION['user'])) {
    echo json_encode(['error' => 'Bạn chưa đăng nhập']);
    exit;
}

if (!isset($_POST['index'])) {
    echo json_encode(['error' => 'Thiếu dữ liệu']);
    exit;
}

$index = intval($_POST['index']);
$user = $_SESSION['user'];

// Lấy VND mới nhất từ DB
$res = $conn->query("SELECT vnd FROM account WHERE id = ".$user['id']);
$row = $res->fetch_assoc();
$soVND = $row['vnd'];

if ($soVND < 50000) {
    echo json_encode(['error' => 'Không đủ VND để chơi']);
    exit;
}

// Trừ 50k
$soVND -= 50000;
$conn->query("UPDATE account SET vnd = $soVND WHERE id = ".$user['id']);
$_SESSION['user']['vnd'] = $soVND;

// Random VIP
if (!isset($_SESSION['vip_index'])) {
    $_SESSION['vip_index'] = rand(0, 7);
}

$result = ($index == $_SESSION['vip_index']) ? "vip" : "thuong";

// Nếu trúng VIP thì reset
if ($result === "vip") {
    unset($_SESSION['vip_index']);
}

echo json_encode([
    'vnd' => number_format($soVND),
    'result' => $result
]);
