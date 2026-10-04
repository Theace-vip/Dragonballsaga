<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';

// Xử lý yêu cầu POST từ client
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $postData = json_decode(file_get_contents('php://input'), true);

    if (empty($postData['telco']) || empty($postData['amount']) || empty($postData['serial']) || empty($postData['code'])) {
        $response = array(
            'success' => false,
            'message' => $postData
        );
    } else {
        $telco = $postData['telco'];
        $serial = $postData['serial'];
        $code = $postData['code'];
        $amount = $postData['amount'];
        $username = $postData['username'];

        $dataPost = array(
            'request_id' => rand(100000000, 999999999),
            'code' => $code,
            'partner_id' => $Partner_Id,
            'serial' => $serial,
            'telco' => $telco,
            'amount' => $amount,
            'command' => 'charging',
            'sign' => md5($Partner_Key . $code . $serial)
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $_ApiCard . 'chargingws/v2');
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($dataPost));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = curl_exec($ch);

        if ($result === false) {
            $response = array(
                'success' => false,
                'message' => 'Lỗi khi gửi yêu cầu nạp thẻ!'
            );
        } else {
            $obj = json_decode($result);

            if ($obj->status == 99) {
                // Thẻ gửi thành công, đợi duyệt.
                $insert_query = "INSERT INTO napthe (user_nap, telco, serial, code, amount, status) VALUES (:user_nap, :telco, :serial, :code, :amount, 99)";

                $stmt = $conn->prepare($insert_query);
                $stmt->bindParam(':user_nap', $username);
                $stmt->bindParam(':telco', $telco);
                $stmt->bindParam(':serial', $serial);
                $stmt->bindParam(':code', $code);
                $stmt->bindParam(':amount', $amount);

                if ($stmt->execute()) {
                    $response = array(
                        'success' => true,
                        'message' => 'Nạp thành công, Vui lòng đợi máy chủ duyệt!'
                    );
                } else {
                    $response = array(
                        'success' => false,
                        'message' => 'Lỗi khi lưu dữ liệu vào máy chủ!'
                    );
                }
            } elseif ($obj->status == 1) {
                $response = array(
                    'success' => false,
                    'message' => $obj->message
                );
            } elseif ($obj->status == 2 || $obj->status == 3 || $obj->status == 4) {
                $response = array(
                    'success' => false,
                    'message' => $obj->message
                );
            } else {
                $response = array(
                    'success' => false,
                    'message' => $obj->message
                );
            }
        }
    }
    // Đảm bảo rằng dữ liệu được trả về là JSON
    header('Content-Type: application/json');
    echo json_encode($response);
}