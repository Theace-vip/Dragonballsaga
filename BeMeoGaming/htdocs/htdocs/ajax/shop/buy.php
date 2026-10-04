<?php
// if (empty($_SERVER['HTTP_REFERER'])) {
//     header('HTTP/1.0 403 Forbidden');
//     echo "Forbidden: You don't have permission to access this resource.";
//     exit();
// }
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";
require_once  $_SERVER['DOCUMENT_ROOT'] . "/ajax/service.php";


$id = abs($_POST['Tempid']);

if (!empty($id)) {

    $row = $CVH->get_row("SELECT * FROM `cvh_sell_item` WHERE `id` = $id");
    if($user){
      if($row['slot'] >= 1){

        if($user['temp_vnd'] >= $row['price']){

            $CVH->Ex(true, "Mua vật phẩm thành công vui lòng vào hòm thư của game để nhận!");

            $player = $CVH->player($user['id']);

            $CVH->addBuy($id, $player['id'], time(), 0);

            $data = array(
                "slot" => $row['slot'] - 1
            ); 
            $CVH->update("cvh_sell_item", $data, 'id = '.$id.'');

            //trừ tiền thành viên
            $table = 'account';
            $where = 'username = "' . $user['username'] . '"';
            $CVH->tru($table, "temp_vnd", $row['price'], $where);
            Service::gI()->sendBuyWeb($id, $player['id']);
        } else {

            $CVH->Ex(false, "Tài khoản của bạn không đủ ".number_format($row["price"])."đ vui lòng nạp thêm tiền để thực hiện giao dịch!");

        }
      } else {

        $CVH->Ex(false, "Sản phẩm đã hết số lượt mua!");

    }
    } else {

        $CVH->Ex(false, "Bạn chưa đăng nhập!");

    }
} else {

    $CVH->Ex(false, "Dữ liệu truyền vào không hợp lệ!");

}