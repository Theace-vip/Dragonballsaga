<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";
header('Content-Type: application/json; charset=utf-8');
if (!$user['admin'] || $user['admin'] != 1) {
    header("Location: /");
    exit;
} else {
    if ($_POST['type'] == 'Add') {
        $id = abs($_POST['id']);
        $luot = abs($_POST['number']);
        if (isset($id)) {
            $table = 'cvh_giftcode';
            $data = array(
                "luot" => $luot
            );
            $where = 'id = "' . $id . '"';
            $CVH->update($table, $data, $where);
            $CVH->Ex(true, "Cập nhật thành công!");
        } else {
            $CVH->Ex(true, "Có lỗi xảy ra!");
        }
    }
}
?>