<?php
session_start();
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

// Hàm xác minh captcha từ Cloudflare
function verifyCaptcha($response)
{
    $secretKey = CF_SECRET_KEY; // Lấy từ file config
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

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['login'])) {
    $user = $_POST['username'];
    $pass = $_POST['password'];
    $captchaResponse = $_POST['cf-turnstile-response'];

    // Kiểm tra captcha
    // if (!verifyCaptcha($captchaResponse)) {
    //     echo "
    //     <script>
    //         Swal.fire({icon: 'error', title: 'Thông Báo', text: 'Captcha không hợp lệ!', confirmButtonText: 'OK'})
    //         .then(function() { window.location.href = 'login.php'; });
    //     </script>";
    //     exit();
    // }

    // Kiểm tra tài khoản và mật khẩu
    $sql = "SELECT * FROM account WHERE username='$user' AND password='$pass'";
    $result = mysqli_query($conn, $sql);
    if (mysqli_num_rows($result) === 0) {
        $msg = "Sai tài khoản hoặc mật khẩu!";
        $icon = "error";
        $redirect = "login.php";
    } else {
        $account = mysqli_fetch_assoc($result);
        $account_id = $account['id'];

        // Kiểm tra xem có nhân vật (player) hay chưa
        $sql = "SELECT * FROM player WHERE account_id='$account_id'";
        $player_result = mysqli_query($conn, $sql);
        if (mysqli_num_rows($player_result) === 0) {
            $msg = "Bạn chưa tạo nhân vật. Vui lòng tạo nhân vật trước khi đăng nhập!";
            $icon = "warning";
            $redirect = "login.php";
        } else {
            // Đăng nhập thành công
            $_SESSION['account'] = $user;
            $_SESSION['is_admin'] = $account['is_admin'];
            $msg = "Đăng nhập thành công!";
            $icon = "success";
            $redirect = "index.php";
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
<!-- HTML Form Đăng Nhập -->
<form class="form-auth text-center" method="POST">
    <h1 class="h3 mb-3 font-weight-normal">Đăng Nhập</h1>
    <div class="form-group">
        <input type="text" name="username" class="form-control" placeholder="Tên tài khoản..." required>
    </div>
    <div class="form-group">
        <input type="password" name="password" class="form-control" placeholder="Mật khẩu..." required>
    </div>
    <!-- Cloudflare Turnstile Captcha -->
    <!--<div class="cf-turnstile" data-sitekey="<?php echo CF_SITE_KEY; ?>" style="margin: 10px 0;"></div>-->
    <button class="btn btn-lg btn-dark btn-block" style="border-radius: 10px;" type="submit" name="login">Đăng nhập</button>
</form>

<!-- Nhúng JavaScript của Turnstile -->
<script src="https://challenges.cloudflare.com/turnstile/v0/api.js" async defer></script>

<?php require('views/layout/foot.php'); ?>