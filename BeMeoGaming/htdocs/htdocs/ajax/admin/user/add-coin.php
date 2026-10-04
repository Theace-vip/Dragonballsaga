<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';
if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
} elseif (isset($_Admin) && $_Admin == 0) {
    // Nếu đã đăng nhập nhưng không phải là admin, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
} else {
    if ($_POST['type'] == 'ADD_COIN') {
        $id = abs($_POST['id']);
        $coin = abs(intval($_POST['temp_vnd']));
        if (isset($id) && isset($coin)) {
            addCointAccount($conn, $id, $coin);
            // mysqli_query($CVH->connect_db(), "UPDATE `users` SET `coin` = coin + $coin, `tongnap` = `tongnap` + $coin WHERE `id` = '" . $id . "' ");
            Ex(true, "Đã cộng tiền thành công!");
        } else {
            Ex(true, "Tài khoản không tồn tại!");
        }
    }
}
?>