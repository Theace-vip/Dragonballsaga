<?php

// Kết nối MySQL
$host = 'localhost';       // hoặc IP database
$dbname = 'dragonballsaga';      // tên database bạn dùng
$username = 'root';        // tài khoản MySQL (thường là root khi dùng XAMPP)
$password = '';            // mật khẩu MySQL (để trống nếu chưa đặt)

// Kết nối MySQL bằng PDO
try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    die("Lỗi kết nối CSDL: " . $e->getMessage());
}

// Cấu Hình Cơ Bản
$_domain = '14.225.213.208'; // điền domain của sự kiện giới thiệu của bạn
$_IP = $_SERVER['REMOTE_ADDR']; // IP hiển thị ở phần cuối trang
$_tenmaychu = 'Ngọc Rồng'; // Tên máy chủ hiển thị ở cuối trang
$_mienmaychu = 'Ngọc Rồng';
$_title = 'Ngọc Rồng Online - Máy Chủ Ngọc Rồng Online';

// thông tin cấu hình vps
$serverIP = "14.225.213.208"; // lấy thông tin máy chủ vps
$serverPort = "443"; // port vps

// API MBBANK GỐC
$_qrmbbank = 'img/QR.jpg'; // link ảnh qr code
$_stkmbbank = '0904769973'; // số điện thoại mbbank
$_mbbank = 'Mb Bank'; // ngân hàng quân đội mbbank
$_tenmbbank = 'NGUYEN CHI HUONG'; // tên tài khoản

// API MOMO THUÊ
$_qrmbbank = 'img/qrmomo.png'; // link ảnh qr code
$_stkmomo = '00'; // số điện thoại momo
$_momo = 'Momo'; // ngân hàng quân đội mbbank
$_tenmomo = 'LE HOANG TAN PHAT'; // tên tài khoản

// API RECAPTCHA
$w_api_recaptcha = "6Ld7cRIlAAAAAAXcz8uJJFt_YzS4HYGIL24rfzPh";
$w_api_recaptcha_private ="6Ld7cRIlAAAAAC30XJ7NQLBAII468lHgdcT11_5_";


// PHIÊN BẢN FILE GAME
$_android = '2.3.0';
$_windows = '2.2.5';
$_java = '2.2.1';
$_iphone = '2.2.2';
$userloginmbbank_config = 'Nhập TK'; // Tài khoản đăng nhập Mbbank của bạn tại https://online.mbbank.com.vn
$passmbbank_config = 'Nhập MK'; // Mật khẩu đăng nhập Mbbank của bạn tại https://online.mbbank.com.vn
$deviceIdCommon_goc_config = '6gln7bb9-mbib-0000-0000-2024050401324715'; // Tha số mà bạn lấy được từ F12 vào đâyy cái thông
$stkmbbank_config = '100882167496'; // Số tài khoản Mbbank
$mbbank_name = 'MBBANK'; // Tên Tài khoản Mbbank
$_mbbank = 'Ngân Hàng Quân Đội | MBBANK'; // Ngân hàng quân đội Mbbank
$_token = ($conn->query("SELECT token FROM cpanel")->fetchColumn()) ?? '';
function CreateToken()
{
    return md5(uniqid(rand(), true));
}
function formatMoney($number)
{
    if (!is_numeric($number) || $number === null) {
        return '0 VNĐ';
    }

    $suffix = '';
    if ($number >= 1000000000000) {
        $number /= 1000000000000;
        $suffix = ' Tỷ';
    } elseif ($number >= 1000000000) {
        $number /= 1000000000;
        $suffix = ' Tỷ';
    } elseif ($number >= 1000000) {
        $number /= 1000000;
        $suffix = ' Triệu';
    } elseif ($number >= 1000) {
        $number /= 1000;
        $suffix = ' K';
    }

    return number_format($number) . $suffix;
}

?>