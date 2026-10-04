<?php
// Kết nối database game
$host = "localhost";
$user = "root"; // user mặc định của XAMPP
$pass = ""; // mật khẩu mặc định rỗng
$db   = "hondaodragon"; // tên database game của bạn

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error]));
}

// Bật session nếu chưa bật
if (session_status() == PHP_SESSION_NONE) {
    session_start();
}
?>
 