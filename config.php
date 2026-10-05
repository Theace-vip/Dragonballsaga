<?php
//error_reporting(0);
// Cấu hình kết nối cơ sở dữ liệu
$db_host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "ngocronggoku";

$conn = mysqli_connect($db_host, $db_user, $db_pass, $db_name);

date_default_timezone_set('Asia/Ho_Chi_Minh');
if (!$conn) {
	die("KHONG THE KET NOI DEN CSDL! VUI LONG KIEM TRA LAI");
} else {
	mysqli_set_charset($conn, 'utf8');
}

function getPlayerInfo($username)
{
	global $conn;
	$sql = "SELECT p.gender, p.name, a.thoi_vang 
            FROM player p 
            JOIN account a ON p.account_id = a.id 
            WHERE a.username = '$username'";
	$result = mysqli_query($conn, $sql);
	if ($result && $player = mysqli_fetch_assoc($result)) {
		return [
			'name' => htmlspecialchars($player['name']),
			'gender' => $player['gender']
		];
	}
	return null;
}


function _query($sql)
{
	global $conn;
	return mysqli_query($conn, $sql);
}

function _fetch($sql)
{
	return mysqli_fetch_array(_query($sql));
}
$_user = isset($_SESSION['account']) ? $_SESSION['account'] : null;
if ($_user != null) {
	$_login = "on";
	$user_arr = _fetch("SELECT * FROM account Where username='$_user'");
	if (!$user_arr) {
		header("location:/?out");
	}
	$_uid = $user_arr['id'];
	$_username = htmlspecialchars($user_arr['username']);
	$_vnd = $user_arr['vnd'];

	$_status = $user_arr['active'];
} else {
	$_login = null;
}

$defaultTitle = 'Web KuRa';
$favicon = 'https://i.imgur.com/leWWY5X.png';
$keywords = 'Chú Bé Rồng Online,ngoc rong mobile, game ngoc rong, game 7 vien ngoc rong, game bay vien ngoc rong';
$logo = '../assets/images/logo.gif';
$description = '"Website chính thức của Web KuRa – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!';
$boxzalo = 'https://zalo.me/g/yluhiu944';
$namegame = 'Game Nro';
// Download game
$pc = 'https://drive.google.com/file/d/16NEijBzXUVq-J4dSIGFHy0cIY9uhKMRS/view';
$adr = 'https://drive.google.com/file/d/1E3g7DKobqSL5B6jEp57cbE01nI9T5vBt/view';
$ios = 'https://testflight.apple.com/join/n6JeuSQR';

// Cấu hình Cloudflare Turnstile
define('CF_SITE_KEY', '0x4AAAAAABeVdoxrFe0VuhgH');  // Thay bằng Site Key từ Cloudflare
define('CF_SECRET_KEY', '0x4AAAAAABeVdhsIXA4fryBtnY8GQftQBto');  // Thay bằng Secret Key từ Cloudflare


// API đổi thẻ của Doithe1s
$partner_id   = '33036064376';
$partner_key  = '7c0603a3faa2aeb5342257f799967bdf';
$chietkhau_card = 0;

// Thông tin ngân hàng (VietQR)
$bank_account = '00582626022';       // Số tài khoản nhận tiền
$bank_name    = 'MBBANK';      // Tên ngân hàng (in hoa nếu dùng VietQR)
$bank_code    = 'MB';              // Mã ngân hàng (Ví dụ VCB, MBBank, ...)
$bank_owner   = 'NGUYEN TUAN BINH';     // Chủ tài khoản

// cấu hình bank
$noidung_bank = "naptien";
$chietkhau_bank = 0;

// SePay webhook secret (nếu sử dụng xác thực qua secret/HMAC)
$sepay_secret = 'vmndev712';
