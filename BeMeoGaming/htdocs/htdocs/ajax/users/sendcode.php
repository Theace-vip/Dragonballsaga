<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}

require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";

$email = $_POST["email"];
$current_time = time();

if ($user) {
 if(!$CVH->check_email_exist($email)){
    $rand = rand(100000, 999999);
    $timecode_expiry = $current_time + 120;

    sendMail($email, 'Mã Xác Thực Email', '<body style="word-spacing:normal;background-color:#fafafa"><div style="display:none;font-size:1px;color:#fff;line-height:1px;max-height:0;max-width:0;opacity:0;overflow:hidden">OTP Xác Thực Tài Khoản</div><div style="background-color:#fafafa" lang="und" dir="auto"><table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="width:100%"><tbody><tr><td><div style="margin:0 auto;max-width:600px"><table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="width:100%"><tbody><tr><td style="direction:ltr;font-size:0;padding:16px;text-align:center"><div style="background:#fff;background-color:#fff;margin:0 auto;border-radius:8px;max-width:568px"><table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#fff;background-color:#fff;width:100%;border-radius:8px"><tbody><tr><td style="direction:ltr;font-size:0;padding:16px;text-align:center"><div class="mj-column-per-100 mj-outlook-group-fix" style="font-size:0;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td style="vertical-align:top;padding:32px"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td align="center" style="font-size:0;padding:10px 25px;padding-bottom:16px;word-break:break-word"><table border="0" cellpadding="0" cellspacing="0" role="presentation" style="border-collapse:collapse;border-spacing:0"><tbody><tr><td align="center" style="font-size:0;padding:0;word-break:break-word"><div style="font-family:Inter,Arial;font-size:13px;line-height:1;text-align:center;color:#000"><h1 style="margin:16px 0">Mã Xác Thực Tài Khoản</h1><p>Xin chào ' . $user["username"] . ',dưới đây là mã xác thực tài khoản 6 chữ số mà hệ thống đã gửi.</p></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div><div class="mj-column-px-250 mj-outlook-group-fix" style="font-size:0;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td style="background-color:#ebe3ff;border-radius:8px;vertical-align:top;padding:16px"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td align="center" style="font-size:0;padding:0;word-break:break-word"><div style="font-family:Inter,Arial;font-size:32px;font-weight:700;letter-spacing:16px;line-height:32px;text-align:center;color:#000"><p style="font-size:32px;margin:0;margin-right:-16px;padding:0">'.$rand.'</p></div></td></tr></tbody></table></td></tr></tbody></table></div><div class="mj-column-per-100 mj-outlook-group-fix" style="font-size:0;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td style="vertical-align:top;padding-top:16px"><table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%"><tbody><tr><td align="center" style="font-size:0;padding:10px 25px;word-break:break-word"><div style="font-family:Inter,Arial;font-size:13px;line-height:1;text-align:center;color:#555"><p>Mã này có giá trị trong 2 phút, vui lòng nhập vào website để kích hoạt email.</p></div></td></tr></tbody></table></td></tr></tbody></table></div></td></tr></tbody></table></div></td></tr></tbody></table></div></td></tr></tbody></table></div></body>');

    $us = $user["username"];
    $table = "account";
    
    $data = array(
        "email" => json_encode(array(
            "email" => $email,
            "verify" => "false",
            "code" => $rand,
            "timecode" => date('Y-m-d H:i:s', $timecode_expiry)
        ))
    );

    $CVH->update($table, $data, "username = '" . $us . "'");

    $CVH->Ex(true, "Mã xác thực đã được gửi đến email của bạn!");
}else{
    $CVH->Ex(false, "Email đã tồn tại ở tài khoản khác!");
}
} else {
    $CVH->Ex(false, "Bạn chưa đăng nhập, vui lòng đăng nhập để thực hiện thao tác này!");
}
?>