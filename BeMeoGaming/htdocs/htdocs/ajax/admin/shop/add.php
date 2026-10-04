<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";

if ($_POST['item'] || $_POST['item'] === '0' && $_POST['price'] || $_POST['price'] === '0' && $_POST['slot']) {
    if (isset($user['admin']) || $user['admin'] != 1) {
        $itemz = ($_POST['item']);
        $price = ($_POST['price']);
        $slot = ($_POST['slot']);
        $option_id = ($_POST['option_id']);
        $param_option = ($_POST['param_option']);
        
        if (count($_POST["option_id"]) > 0) {
            $data = array();
            for ($i = 0; $i < count($_POST["option_id"]); $i++) {
                if (trim($_POST["option_id"][$i] != '')) {
                    $id = isset($_POST["option_id"][$i]) ? $_POST["option_id"][$i] : 30;
                    $param = isset($_POST["param_option"][$i]) ? $_POST["param_option"][$i] : 0;
                    $item = array(
                        "id" => $id,
                        "param" => $param
                    );
                    $data[] = $item;
                }
            }
            $option = json_encode($data);
            if ($option == "[]") {
                $dataz = array(
                    "id" => 30,
                    "param" => 0
                );
                $option = json_encode([$dataz]);
            }
        }

        $table = "cvh_sell_item";
        $data = array(
            "item" => $itemz,
            "slot" => $slot,
            "price" => $price,
            "options" => $option,
            "status" => 1,
            "users_buy" => "[]",
            "time" => time()
        );
        $CVH->insert($table, $data);
        $CVH->Ex(true, "Thêm sản phẩm thành công!");
    } else {
        $CVH->Ex(false, "Địt mẹ mày cút ngay!");
    }

} else {
    $CVH->Ex(false, "Vui lòng nhập đầy đủ thông tin!");
}
?>