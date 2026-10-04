<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";

if (!empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['repassword'])) {

    $username = $CVH->rewrite($CVH->FormatString($_POST['username']));
    $password = $CVH->rewrite($CVH->FormatString($_POST['password']));
    $repassword = $CVH->rewrite($CVH->FormatString($_POST['repassword']));

    if ($password == $repassword) {

        if ($CVH->LimitString($username, 4, 20) == false) {

            $CVH->Ex(false, "Tên tài khoản bắt buộc phải từ 4 tới 20 ký tự!");

        } else if ($CVH->LimitString($password, 4, 20) == false) {

            $CVH->Ex(false, "Mật khẩu bắt buộc phải từ 4 tới 20 ký tự!");

        } else if ($CVH->check_user_register($username) == true) {

            $CVH->Ex(false, "Tài khoản đã tồn tại trên hệ thống!");

        } else if ($CVH->check_user_register($username) == false) {

            $token = $CVH->Token($username, $password);
            $table = "account";
            $data = array(
                "username" => $username,
                "password" => $password,
                "nap" => 0,
                "napsukien" => 0,
                "diemnap" => 0,
                "napthang" => 0,
                "is_admin" => 0,
                "temp_vnd" => 0,
                "coin" => 0,
                "tongnap" => 0,
                "password2" => $password,
            );
            
            $CVH->insert($table, $data);

            $CVH->Ex(true, "Đăng ký thành công!");
        }

    } else {

        $CVH->Ex(false, "Hai mật khẩu bạn nhập chưa giống nhau!");

    }


} else {

    $CVH->Ex(false, "Vui lòng nhập đầy đủ thông tin!");

}