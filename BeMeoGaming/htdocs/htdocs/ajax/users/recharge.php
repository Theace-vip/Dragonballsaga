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
}
if ($_Id) {

    // $player = $CVH->player($user['id']);

    $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;

    $mathe = $data['code'];
    $serial = $data['serial'];
    $loaithe = $data['telco'];
    $menhgia = $data['amount'];


    if ($loaithe && $menhgia && $mathe && $serial) {

        $tranid = rand(100000, 999999);

        $huydepzaii = post_card($tranid, $loaithe, $mathe, $serial, $menhgia, $config['partner_id'], $config['partner_key']);

        if ($huydepzaii['status'] == 99 || $huydepzaii['status'] == 3) {
            $table = "cvh_recharge";
            $data = array(
                "account_id" => $_Id,
                "code" => $mathe,
                "serial" => $serial,
                "amount" => $menhgia,
                "type" => $loaithe,
                "amount_real" => '-1',
                "status" => 0,
                "tranid" => $tranid,
                "time" => date("H:i:s d/m/Y")
            );
            insert($conn,$table, $data);

            Ex(true, "Nạp thẻ thành công vui lòng chờ duyệt!");

        } else {
            Ex(false, $huydepzaii['message']);
        }

    } else {

        Ex(false, "Vui lòng nhập đầy đủ thông tin!");

    }


} else {
    Ex(false, "Bạn chưa đăng nhập vui lòng đăng nhập để thực hiện thao tác này!");
}