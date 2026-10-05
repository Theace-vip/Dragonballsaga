<?php
session_start();
include 'config.php'; // Sử dụng $partner_id và $partner_key từ config
require('views/layout/head.php');
require('views/layout/nav.php');
$type = "card";
if (isset($_GET['type']) && $_GET['type'] == 'bank') {
    $type = "bank";
} else {
    $type = "card";
}
if (!isset($_SESSION['account'])) {
    echo '<script>window.location.href = "/login";</script>';
    exit();
}

$msg = null;
$username = $_SESSION['account']; // Lấy tên tài khoản từ session

?>
<div style="text-align: center;">
    <a href="/naptien.php?type=card">
        <button style="cursor: pointer;outline: none;border: none;padding: 10px 10px;border-radius: 5px;" type="button">Nạp thẻ cào</button>
    </a>
    <a href="/naptien.php?type=bank">
        <button style="cursor: pointer;outline: none;border: none;padding: 10px 10px;border-radius: 5px;" type="button">Nạp chuyển khoản</button>
    </a>
</div>
<?php

if ($type == "bank") {
    require_once('views/payment/bank.php');
} else {
    require_once('views/payment/card.php');
}

?>

<?php require('views/layout/foot.php'); ?>