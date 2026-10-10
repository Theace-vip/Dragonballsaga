<?php
//error_reporting(0);
// Cấu hình kết nối cơ sở dữ liệu
$db_host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "hondaodragon"; // DB cua game (BeMeoGaming) - web va game dung chung account/player

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
            WHERE a.username = ?";
	$stmt = mysqli_prepare($conn, $sql);
	mysqli_stmt_bind_param($stmt, "s", $username);
	mysqli_stmt_execute($stmt);
	$result = mysqli_stmt_get_result($stmt);
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
if (session_status() === PHP_SESSION_NONE) {
	session_start();
}
$_user = isset($_SESSION['account']) ? $_SESSION['account'] : null;
if ($_user != null) {
	$_login = "on";
	$stmt = mysqli_prepare($conn, "SELECT * FROM account WHERE username = ?");
	mysqli_stmt_bind_param($stmt, "s", $_user);
	mysqli_stmt_execute($stmt);
	$user_arr = mysqli_fetch_array(mysqli_stmt_get_result($stmt));
	if (!$user_arr) {
		header("location:/?out");
	}
	$_uid = $user_arr['id'];
	$_username = htmlspecialchars($user_arr['username']);
	// Số dư hiển thị = tiền trong game + tiền nạp đang chờ (temp_vnd, game sẽ tự cộng khi vào game)
	$_vnd = $user_arr['vnd'] + $user_arr['temp_vnd'];

	$_status = $user_arr['active'];
} else {
	$_login = null;
}

// ==== Thong tin web / server NROKuRA ====
$brandName = 'NROKuRA';                       // Ten hien thi (logo text, menu, footer)
$brandSlogan = 'Server 7 Viên Ngọc Rồng Online';
$defaultTitle = 'NROKuRA – Trò chơi 7 Viên Ngọc Rồng Online';
// Icon trinh duyet (favicon) - doi o day. File dang dat: assets/images/favicon.jpg
$favicon = '/assets/images/favicon.jpg';
$keywords = 'NROKuRA, nrokura, 7 vien ngoc rong, tro choi 7 vien ngoc rong, game ngoc rong, ngoc rong online, dragon ball online, nro';
$logo = '/assets/images/logo.gif'; // file logo chinh - thay bang logo.gif cua ban
$description = 'Website chính thức của NROKuRA – Server 7 Viên Ngọc Rồng Online nhập vai trực tuyến trên máy tính và điện thoại. Đăng ký miễn phí, cày cuốc, đua top, săn boss cùng cộng đồng NROKuRA!';
$boxzalo = 'https://zalo.me/g/pnde68ve5zmxtzxrk4vt'; // Box Zalo NROKuRA
$namegame = 'NROKuRA';

// ==== Thong tin server (doc tu BeMeoGaming/src + data/config/config.properties) ====
// LUU Y: khong hien thi IP/may chu cong ket noi ra trang cong khai.
$serverName = 'hondaodragon';   // server.name trong config.properties (chi dung noi bo)
$serverRate = 100;              // server.expserver (ty le exp/kc)
$serverMaxPlayer = 10000;       // server.maxplayer
$serverMaxPerIp  = 5;           // server.maxperip
$siteUrl = 'https://nrokura.site';

// Download game (client cua server NROKuRA)
$pc = 'https://drive.google.com/file/d/15GIQUbFldJF2OTn0yJdEE39Hos0Ovf4k/view?usp=drive_link';
$adr = 'https://drive.google.com/file/d/1inUO5XAEbRrrLp50Ti0nndOG6DubybQB/view?usp=drive_link';
$ios = 'https://drive.google.com/file/d/1j7hWvDGRqqhULlXmazGKDI775wOf8mUh/view?usp=drive_link';
$java = ''; // chua co link client Java -> de rong se an nut trong nav
$zalo = $boxzalo;
$fanpage = 'https://www.facebook.com/profile.php?id=61594641300977';

// Cấu hình Cloudflare Turnstile
define('CF_SITE_KEY', '0x4AAAAAABeVdoxrFe0VuhgH');  // Thay bằng Site Key từ Cloudflare
define('CF_SECRET_KEY', '0x4AAAAAABeVdhsIXA4fryBtnY8GQftQBto');
// Bat/Tat captcha Turnstile. false = bo qua captcha (dung de test local).
// TRUOC KHI LEN ONLINE: tao key Turnstile cua rieng minh roi dat true.
define('CF_ENABLED', false);  // Thay bằng Secret Key từ Cloudflare


// API đổi thẻ cào (Doithe1s) - ĐÃ NGỪNG DÙNG: tính năng nạp thẻ cào đã bỏ khỏi web
// (doithe1s.vn chuyển sang pay1s.com, endpoint cũ /chargingws/v2 trả 404).
// Giữ lại giá trị để tham chiếu khi cần khôi phục.
$partner_id   = '33036064376';
$partner_key  = '7c0603a3faa2aeb5342257f799967bdf';
$chietkhau_card = 0;

// Thông tin ngân hàng nhận nạp (VietQR)
$bank_account = '060308773208';       // Số tài khoản nhận tiền (Sacombank)
$bank_name    = 'Sacombank';          // Tên ngân hàng hiển thị cho người chơi
$bank_code    = 'SACOMBANK';          // Mã ngân hàng cho img.vietqr.io / api.vietqr.io
$bank_owner   = 'NGUYEN THIEU BAO';   // Chủ tài khoản (hiển thị trên trang nạp + nhúng vào QR)

// cấu hình bank
$noidung_bank = "naptien";             // Tiền tố nội dung CK: naptien + username (vd: naptientheace)
$chietkhau_bank = 0;                  // % chiết khấu khi nạp bank (0 = không trừ)
$bank_heso = 1;                       // Hệ số quy đổi VNĐ -> tiền trong game (1 = 1:1, 3 = x3)
$bank_min = 10000;                    // Số tiền tối thiểu để tạo QR

// SePay (https://my.sepay.vn) - webhook realtime + API đối soát giao dịch
// Webhook: Cấu hình công ty -> Webhook, xác thực "API Key" với giá trị bên dưới
$sepay_secret = 'vmndev712';
// API Token v2 (Cấu hình công ty -> API Access). Trống = nút "Kiểm tra nạp" báo chưa cấu hình.
$sepay_api_token = 'ETZFQWQZG5ULDRNQVMKYVC0V3BZTOGIPHVCQACXJDSYONTBCMRPUKDRE7ZYS8EAP';
$sepay_api_base  = 'https://userapi.sepay.vn/v2';
// (tùy chọn) UUID tài khoản ngân hàng trên SePay, để lọc đúng STK 060308773208
$sepay_bank_account_id = '18be61bf-c404-11f1-b21a-a6006ab65aca';
// Cron đối soát: bỏ qua giao dịch đã được SePay gửi webhook thành công (tránh cộng 2 lần)
$sepay_skip_webhook_ok = true;

// Thư mục dữ liệu nội bộ (log webhook, log cộng tiền, con trỏ đối soát).
// PHẢI nằm NGOÀI webroot để không tải được qua http(s).
$bank_data_dir = 'C:/xampp/nrokura_private';

// ==== Băm mật khẩu PBKDF2-SHA256 (cùng format với server game - utils.PasswordUtil) ====
// Định dạng: pbkdf2-sha256$<iterations>$<salt_hex>$<hash_hex>
function pw_is_hash($s)
{
	return is_string($s) && strpos($s, 'pbkdf2-sha256$') === 0;
}

function pw_hash($plain)
{
	$salt = random_bytes(16);
	$dk = hash_pbkdf2('sha256', (string) $plain, $salt, 100000, 32, true);
	return 'pbkdf2-sha256$100000$' . bin2hex($salt) . '$' . bin2hex($dk);
}

function pw_verify($stored, $plain)
{
	if (!pw_is_hash($stored)) {
		return false;
	}
	$p = explode('$', $stored);
	if (count($p) !== 4 || !ctype_digit($p[1])) {
		return false;
	}
	$salt = @hex2bin($p[2]);
	$expect = @hex2bin($p[3]);
	if ($salt === false || $expect === false) {
		return false;
	}
	$dk = hash_pbkdf2('sha256', (string) $plain, $salt, (int) $p[1], strlen($expect), true);
	return hash_equals($expect, $dk);
}

// Kiểm tra cả hash lẫn plaintext (legacy cũ chưa băm)
function pw_check($stored, $plain)
{
	if (pw_is_hash($stored)) {
		return pw_verify($stored, $plain);
	}
	return is_string($stored) && hash_equals($stored, (string) $plain);
}
