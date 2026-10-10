<?php
#Duong Huynh Khanh Dang
require_once '../DHKD/Configs.php';
require_once '../DHKD/Session.php';
require_once '../DHKD/Connections.php';
define('POINTS_PER_TOPUP', 1);

/* ============================================================================
 * NAP THE CAO: DA NGUNG (2026-10-10)
 * Toan bo phan doi chieu chu ky + cong tien tu the cao da duoc comment theo yeu
 * cau - tu day callback cua nha cung cap khong cong tien cho ai nua.
 * Muon bat lai: bo comment khoi doan code duoi day (va xoa phan tra thong bao
 * "da tam ngung" o cuoi file).
 * ----------------------------------------------------------------------------
// Lấy dữ liệu từ request
$txtBody = file_get_contents('php://input');
$jsonBody = json_decode($txtBody);

// Khởi tạo biến log để ghi thông tin lỗi
$log = "";

if (isset($jsonBody->callback_sign)) {
    $callback_sign = md5($Partner_Key . $jsonBody->code . $jsonBody->serial);

    if ($jsonBody->callback_sign == $callback_sign) {
        // Display received data for debugging
        $getdata['status'] = $jsonBody->status;
        $getdata['message'] = $jsonBody->message;
        $getdata['request_id'] = $jsonBody->request_id;
        $getdata['trans_id'] = $jsonBody->trans_id;
        $getdata['declared_value'] = $jsonBody->declared_value;
        $getdata['value'] = $jsonBody->value;
        $getdata['amount'] = $jsonBody->amount;
        $getdata['code'] = $jsonBody->code;
        $getdata['serial'] = $jsonBody->serial;
        $getdata['telco'] = $jsonBody->telco;
        print_r($getdata);

        // Kiểm tra nếu đối chứng chữ ký hợp lệ
        $code = $jsonBody->code;
        $serial = $jsonBody->serial;

        // Kiểm tra xem có dữ liệu người dùng trong bảng "napthe" hay không
        $get_user_nap_sql = "SELECT * FROM napthe WHERE code = :code AND serial = :serial";
        $stmt = $conn->prepare($get_user_nap_sql);
        $stmt->bindParam(':code', $code);
        $stmt->bindParam(':serial', $serial);
        $stmt->execute();

        if ($stmt->rowCount() > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            $user_nap = $row['user_nap'];
            $price = $row['amount'] * $_GiaTri;

            // Tiến hành câu truy vấn UPDATE trong bảng "napthe" để cập nhật trạng thái (status)
            $update_status_sql = "UPDATE napthe SET status = :status WHERE code = :code AND serial = :serial";
            $stmt = $conn->prepare($update_status_sql);
            $stmt->bindParam(':status', $jsonBody->status);
            $stmt->bindParam(':code', $code);
            $stmt->bindParam(':serial', $serial);

            if ($stmt->execute()) {
                if ($jsonBody->status == 1) {
                    $updateActiveAndVnd = ($_AutoMember == 1 && $price > 20000) ? "active = CASE WHEN active = 0 THEN 1 ELSE active END, " : "";
                    $updateVnd = ($_AutoMember == 1 && $price > 20000) ? "temp_vnd = temp_vnd + :price - 20000, tongnap = tongnap + :price" : "temp_vnd = temp_vnd + :price, tongnap = tongnap + :price";
                    $Account = "UPDATE account SET {$updateActiveAndVnd}{$updateVnd} WHERE username = :user_nap";
                    $Account_Update = $conn->prepare($Account);

                    $adjustedPrice = ($price > 20000 && $_AutoMember == 1) ? $price - 20000 : $price;

                    $Account_Update->bindParam(':price', $adjustedPrice);
                    $Account_Update->bindParam(':user_nap', $user_nap);
                    if (!$Account_Update->execute()) {
                        // Ghi log khi có lỗi xảy ra trong quá trình cập nhật tài khoản
                        $log .= "Error updating account: " . implode(" ", $Account_Update->errorInfo()) . "\n";
                    }
                }
            } else {
                // Ghi log khi không thể cập nhật trạng thái
                $log .= "Error updating status for code: $code, serial: $serial\n";
            }
        } else {
            // Ghi log khi không tìm thấy dữ liệu người dùng trong bảng "napthe"
            $log .= "No user data found for code: $code, serial: $serial\n";
        }
    } else {
        // Ghi log khi chữ ký callback không hợp lệ
        $log .= "Invalid callback signature\n";
    }
} else {
    // Ghi log khi không nhận được dữ liệu callback
    $log .= "No callback data received\n";
}

// Ghi log nếu có lỗi
if (!empty($log)) {
    $logFile = fopen("error.log", "a") or die("Unable to open log file!");
    fwrite($logFile, date("Y-m-d H:i:s") . "\n" . $log . "\n");
    fclose($logFile);
}
 * ========================================================================== */

// Luong the cao da ngung: khong doi chieu chu ky, khong cong tien, khong doi status
http_response_code(200);
echo 'Nap the cao da tam ngung. Vui long nap bang chuyen khoan ngan hang.';