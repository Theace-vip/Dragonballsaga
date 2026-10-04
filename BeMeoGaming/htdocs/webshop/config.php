<?php
// ================== CẤU HÌNH DATABASE ==================
$host = "localhost";
$user = "root";       // Tài khoản mặc định của XAMPP
$pass = "";           // Mật khẩu mặc định (rỗng)
$db   = "dragonballsaga"; // Tên database

// ================== KẾT NỐI DATABASE ==================
$conn = new mysqli($host, $user, $pass, $db);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// ================== SESSION ==================
if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

// ================== TÀI KHOẢN ADMIN MẶC ĐỊNH ==================
$admin_username = "admin";   // đổi tên cho an toàn
$admin_password = "thao009";  // đổi mật khẩu ngay khi deploy
?>
