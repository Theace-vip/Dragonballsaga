<?php
session_start();
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

// Kiểm tra trạng thái đăng nhập
if (!isset($_SESSION['account'])) {
    echo "
    <script>
        Swal.fire({icon: 'error', title: 'Lỗi', text: 'Bạn cần đăng nhập để thay đổi mật khẩu!', confirmButtonText: 'OK'})
        .then(function() { window.location.href = 'login.php'; });
    </script>";
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['change_password'])) {
    $user = $_SESSION['account'];
    $old_pass = trim($_POST['old_password']);
    $new_pass = trim($_POST['new_password']);

    // Kiểm tra tài khoản và mật khẩu cũ
    $sql = "SELECT * FROM account WHERE username = ? AND password = ?";
    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "ss", $user, $old_pass);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);

    if (mysqli_num_rows($result) === 0) {
        $msg = "Mật khẩu cũ không đúng!";
        $icon = "error";
        $redirect = "change-password";
    } else {
        $row = mysqli_fetch_assoc($result);
        $account_id = $row['id'];

        // Cập nhật mật khẩu mới
        $update_sql = "UPDATE account SET password = ? WHERE id = ?";
        $update_stmt = mysqli_prepare($conn, $update_sql);
        mysqli_stmt_bind_param($update_stmt, "si", $new_pass, $account_id);
        mysqli_stmt_execute($update_stmt);

        if (mysqli_stmt_affected_rows($update_stmt) > 0) {
            $msg = "Thay đổi mật khẩu thành công!";
            $icon = "success";
            $redirect = "index.php";
        } else {
            $msg = "Không có gì thay đổi! Có thể mật khẩu mới trùng mật khẩu cũ.";
            $icon = "warning";
            $redirect = "change-password";
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
<form class="form-auth text-center" method="POST">
    <h1 class="h3 mb-3 font-weight-normal">Đổi Mật Khẩu</h1>
    <div class="form-group">
        <input type="password" name="old_password" class="form-control" placeholder="Mật khẩu cũ..." required>
    </div>
    <div class="form-group">
        <input type="password" name="new_password" class="form-control" placeholder="Mật khẩu mới..." required>
    </div>
    <button class="btn btn-lg btn-dark btn-block" style="border-radius: 10px;" type="submit" name="change_password">Đổi
        mật khẩu</button>
</form>
<?php require('views/layout/foot.php'); ?>