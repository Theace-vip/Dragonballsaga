<?php
session_start();
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

// Nạp thẻ cào đã bỏ - chỉ còn nạp chuyển khoản ngân hàng
if (!isset($_SESSION['account'])) {
    echo '<script>window.location.href = "/login";</script>';
    exit();
}

$msg = null;
$username = $_SESSION['account']; // Lấy tên tài khoản từ session

?>
<div style="text-align: center;">
    <h4 style="margin: 10px 0;">Nạp tiền bằng chuyển khoản ngân hàng</h4>
</div>
<?php

require_once('views/payment/bank.php');

?>

<?php require('views/layout/foot.php'); ?>