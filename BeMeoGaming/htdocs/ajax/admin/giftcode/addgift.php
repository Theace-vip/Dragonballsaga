<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";

if ($_POST['code']  && $_POST['item'] || $_POST['item'] === '0' && $_POST['count'] && $_POST['hsd']) {
    if (isset($user['admin']) || $user['admin'] != 1) {
        $code = ($_POST['code']);
        $itema = ($_POST['item'] ?? []);
        $soluonga = ($_POST['soluong'] ?? []);
        $count = ($_POST['count']);
        $option_id = ($_POST['option_id'] ?? []);
        $param_option = ($_POST['param_option'] ?? []);
        ksort($itema,SORT_ASC);
        ksort($soluonga,SORT_ASC);
        ksort($option_id,SORT_ASC);
        ksort($param_option,SORT_ASC);
        $hsd = ($_POST['hsd']);
        $data = array();
        if (count($option_id ?? []) > 0) {
            for ($i = 0; $i < count($option_id); $i++) {
                foreach($option_id[$i] as $j => $v) {
                    if (trim($v != '') || $v === '0' ) {
                        $id = isset($v)  ? $v : 30;
                        $param = isset($param_option[$i][$j]) ? $param_option[$i][$j] : 0;
                        $item = array(
                            "id" => $id,
                            "param" => $param,
                            "idItem" => $itema[$i]
                        );
                        $data[] = $item;
                    }
                }
            }
            // $option = json_encode($option_id);
            // if ($option == "[]") {
            //     $dataz = array(
            //         "id" => 30,
            //         "param" => 0
            //     );
            //     $option = json_encode([$dataz]);
            // }
        }
        $items = [];
        foreach($itema as $t => $it) {
            $soluong = $soluonga[$t] ?? 1;
            $itemz = array(
                "id" => $it,
                "quantity" => $soluong
            );
            $items[] = $itemz;
        }
        if ($code == 'rd') {
            $code = rand_string(6);
        }
        $option = json_encode($data);
        $itemz = json_encode($items);
        $table = "cvh_giftcode";
        $data = array(
            "code" => $code,
            "luot" => $count,
            "item" => $itemz,
            "option" => $option,
            "status" => true,
            "hsd" => $hsd,
            "time" => time()
        );
        $CVH->insert($table, $data);
        $CVH->Ex(true, "Thêm gift code " . $code . " thành công!");
    } else {
        $CVH->Ex(false, "Địt mẹ mày cút ngay!");
    }

} else {
    $CVH->Ex(false, "Vui lòng nhập đầy đủ thông tin!");
}
?>