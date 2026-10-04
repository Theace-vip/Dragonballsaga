<?php
#Duong Huynh Khanh Dang
// Bật hiển thị lỗi
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';


$query = "SELECT * FROM user_history_system ORDER BY time DESC LIMIT 5";
$stmt = $conn->prepare($query);
$stmt->execute();
$data = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Chuyển đổi kết quả thành JSON và trả về
header('Content-Type: application/json');
echo json_encode($data);
