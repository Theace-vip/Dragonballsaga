<?php
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

// Hàm xác minh captcha từ Cloudflare
function verifyCaptcha($response)
{
    $secretKey = CF_SECRET_KEY; // Lấy từ file config.php
    $url = 'https://challenges.cloudflare.com/turnstile/v0/siteverify';

    $data = http_build_query([
        'secret' => $secretKey,
        'response' => $response,
        'remoteip' => $_SERVER['REMOTE_ADDR']
    ]);

    $options = [
        'http' => [
            'header' => 'Content-type: application/x-www-form-urlencoded',
            'method' => 'POST',
            'content' => $data,
        ],
    ];
    $context = stream_context_create($options);
    $result = file_get_contents($url, false, $context);
    $verification = json_decode($result, true);

    return $verification['success'] ?? false;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['register'])) {
    $user = $_POST['username'];
    $pass = $_POST['password'];
    $captchaResponse = $_POST['cf-turnstile-response'];

    // Kiểm tra captcha
    if (!verifyCaptcha($captchaResponse)) {
        echo "
        <script>
            Swal.fire({icon: 'error', title: 'Thông Báo', text: 'Captcha không hợp lệ!', confirmButtonText: 'OK'})
            .then(function() { window.location.href = 'register.php'; });
        </script>";
        exit();
    }

    // Kiểm tra tên đăng nhập đã tồn tại
    $sql = "SELECT 1 FROM account WHERE username='$user'";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) > 0) {
        $msg = "Tên đăng nhập đã tồn tại!";
        $icon = "error";
        $redirect = "register.php";
    } elseif (!preg_match('/^[a-z0-9]{4,16}$/', $pass)) {
        $msg = "Mật khẩu không hợp lệ! Chỉ chứa a-z, 0-9 và từ 4-16 ký tự.";
        $icon = "error";
        $redirect = "register.php";
    } else {
        // Thêm tài khoản mới
        $sql = "INSERT INTO account (username, password) VALUES ('$user', '$pass')";
        if (mysqli_query($conn, $sql)) {
            $msg = "Tạo tài khoản thành công! Vui lòng đăng nhập.";
            $icon = "success";
            $redirect = "login.php";
        } else {
            $msg = "Lỗi: " . mysqli_error($conn);
            $icon = "error";
            $redirect = "register.php";
        }
    }

    echo "
    <script>
        Swal.fire({icon: '$icon', title: 'Thông Báo', text: '$msg', confirmButtonText: 'OK'})
        .then(function() { window.location.href = '$redirect'; });
    </script>";
}
?>
<div class="col"> <a href="home" style="color: white">Quay lại diễn đàn</a> </div>
<!-- Form Đăng Ký -->
<form class="form-auth text-center" method="POST">
    <h1 class="h3 mb-3 font-weight-normal text-white"><b>Đăng Ký</b></h1>
    <div class="form-group">
        <input type="text" name="username" class="form-control" placeholder="Tên tài khoản..." required>
    </div>
    <div class="form-group">
        <input type="password" name="password" class="form-control" placeholder="Mật khẩu..." required>
    </div>
    <!-- Cloudflare Turnstile Captcha -->
    <div class="cf-turnstile" data-sitekey="<?php echo CF_SITE_KEY; ?>" style="margin: 10px 0;"></div>
    <button class="btn btn-lg btn-dark btn-block" style="border-radius: 10px;" type="submit" name="register">Đăng ký</button>
</form>

<!-- Nhúng JavaScript của Turnstile -->
<script src="https://challenges.cloudflare.com/turnstile/v0/api.js" async defer></script>

<?php require('views/layout/foot.php'); ?>