<?php
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';
// Lấy tiêu đề của yêu cầu để kiểm tra token Authorization
$headers = apache_request_headers();
$webhookToken = isset($headers['Authorization']) ? $headers['Authorization'] : '';
$expectedToken = 'Bearer 8a7c69401965bdae5282b519aa788c7dabe9241a0b88947576'; 

// Lấy dữ liệu JSON từ yêu cầu
$requestBody = file_get_contents('php://input');
$data = json_decode($requestBody, true);

if (json_last_error() === JSON_ERROR_NONE) {
    if (isset($data['transactions']) && is_array($data['transactions'])) {
        file_put_contents("webhook.txt", json_encode($data) . "\n", FILE_APPEND);
        $pattern = '/HDRT\d+/';
        foreach ($data['transactions'] as $transaction) {
            if ($transaction['id'] === null) {
                continue;
            }
            if (preg_match($pattern, $transaction['content'], $matches)) {
                $transaction_code = $matches[0];

                $stmt = $conn->prepare("SELECT * FROM `order` WHERE `orderInfo` = :orderInfo");
                $stmt->bindValue(":orderInfo", $transaction_code);
                $stmt->execute();
                $result = $stmt->fetch();
                if ($result['status'] == 1) {
                    $amount = $transaction['transferAmount'];  
                    $account_id = $result['account_id'];
                    $monney = intval($amount) * 1.2;//20%
                    $update = $conn->prepare("UPDATE `account` SET `temp_vnd` = `temp_vnd` + :monney, `tongnap` = `tongnap` + :monney WHERE `id` = :account_id");
                    $update->bindValue(":monney", $monney);
                    $update->bindValue(":account_id", $account_id);
                    $update->execute();

                    $update2 = $conn->prepare("UPDATE `order` SET `status` = '2' WHERE `orderInfo` = :orderInfo");
                    $update2->bindValue(":orderInfo", $transaction_code);
                    $update2->execute();
                }
            }
        }

        $response = [
            'success' => true,
            'message' => 'Giao dịch được xử lý và xác nhận thành công'
        ];
        http_response_code(200);
    } else {
        // Phản hồi lỗi nếu không có 'transactions'
        $response = [
            'success' => false,
            'message' => 'Thông tin không hợp lệ, không tìm thấy giao dịch'
        ];
        error_log("Thông tin không hợp lệ, không tìm thấy giao dịch");
        http_response_code(400);
    }
} else {
    // Phản hồi lỗi nếu JSON không hợp lệ
    $response = [
        'success' => false,
        'message' => 'JSON không hợp lệ'
    ];
    error_log("Invalid JSON: " . $requestBody);
    http_response_code(400);
}

header('Content-Type: application/json');

// Xuất phản hồi JSON
echo json_encode($response);
