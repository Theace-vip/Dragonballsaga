<?php
#Duong Huynh Khanh Dang

#Nạp
$_packagePrices = [        // Gói Nạp      Giá Gói   (Không thay đổi gói nạp!!)
    'NRO_001' => '20.000', //'NRO_001' => '20.000' 
    'NRO_002' => '50.000',
    'NRO_003' => '100.000',
    'NRO_004' => '200.000',
    'NRO_005' => '500.000',
    'NRO_006' => '1.000.000'
];

#Web by DHKD
$_Logo = ''; // Thay tên + đuôi của Logo vào đây
$_Title = 'Hòn Đảo Dragon | Máy Chủ Ngọc Rồng Online';
$_ServerName = 'Hòn Đảo Dragon';
$_Description = 'Website chính thức của Hòn Đảo Dragon – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!';

#Chức Năng Quên MK
$_ForgotEmail = 'Email'; // Gmail Chạy Quên Mật Khẩu
$_ForgotPass = 'Password'; // Mật Khẩu Gmail Chạy Quên Mật Khẩu

#Tình Trạng Web
$_GiaTri = '1'; // Nạp x1 -> x2 -> x3 (Thẻ Cào)
$_GiaTriAtm = '3'; // Chuyển Khoản x1 -> x2 -> x3
$_TrangThai = '1'; // Hoạt Động = 1, Bảo Trì = 0 (Trạng Thái Nạp Tiền)
$_AutoMember = '0'; // Auto mở khi nạp = 1, Tắt Auto mở thành viên = 0 (Áp dạng cho nạp thẻ và Atm)
$_FixWeb = '0'; // Bảo Trì = 1, Không Bảo Trì = 0
$_AuthLog = '0'; // Bảo Trì = 1, Không Bảo Trì = 0

#Hỗ Trợ
$_Fanpage = 'https://zalo.me/g/bndhbr398';
$_Zalo = 'https://zalo.me/g/bndhbr398';
$_Tiktok = 'https://zalo.me/g/bndhbr398';
$_Group = '';

#---------------#
#Downloads
$_Android = 'https://drive.google.com/file/d/15gCtFptdffCSPqty368XKrDR4tl2l5ty/view?usp=sharing'; // Downloads nơi lưu file game
$_Iphone = '';
$_Java = '/Downloads/ThangHoa.jar';
$_Windows = 'https://drive.google.com/file/d/1QRDU6jE7IYHyNzz5WjUxSFWJWWsYgbLg/view?usp=sharing';

#Card
$Partner_Key = '3c6d89ecd3109d128dc30021834a61e7';
$Partner_Id = '29371532750';
$_ApiCard = 'https://thesieutoc.net'; // Link Đại Lý Thẻ

#Atm - Mbbank
$userloginmbbank_config = 'User'; // Tài khoản đăng nhập Mbbank của bạn tại https://online.mbbank.com.vn
$passmbbank_config = 'Password'; // Mật khẩu đăng nhập Mbbank của bạn tại https://online.mbbank.com.vn
$deviceIdCommon_goc_config = 'DeviceId'; // Thay cái thông số mà bạn lấy được từ F12 vào đây
$stkmbbank_config = ''; // Số tài khoản Mbbank
$mbbank_name = ''; // Tên Tài khoản Mbbank
$_mbbank = 'Ngân Hàng Quân Đội | Mbbank'; // Ngân hàng quân đội Mbbank
//$_Token = ($conn->query("SELECT token FROM cpanel")->fetchColumn()) ?? '';

#Atm - ACB
$username = ''; // Tài khoản
$password = ''; // Mật khẩu
$account = ''; // Số tài khoản
$config = [
   'partner_id' => '50096368033',
   'partner_key' => 'e9e229f3198ee24fae44c39ae717d619',
   'accessKey' => 'd40b8ea4364f9cf12827982deb95999102d64644b068e5ab818f7e92c7854edd',
   'secretKey' => 'd27fff5963f46365a54ef2ac4ea41b9a812a236a6488afde8799fbd97c2c7e31',
   'partnerCode' => 'PAY2SEJABSQ83AU2EE17',
];
function CreateToken()
{
    return md5(uniqid(rand(), true));
}
?>