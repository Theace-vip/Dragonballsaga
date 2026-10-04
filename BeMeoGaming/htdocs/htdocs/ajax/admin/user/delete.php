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
    if ($_POST['type'] == 'Del_Mem') {
        $id = abs($_POST['id']);
        if (isset($id)) {
            deletePlayer($conn, $id);
            deleteUser($conn, $id);
            Ex(true, "Xóa tài khoản và nhân vật thành công!");
        } else {
            Ex(true, "Tài khoản không tồn tại!");
        }
    }
}
?>