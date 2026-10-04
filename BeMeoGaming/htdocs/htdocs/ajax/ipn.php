<?php
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';
header("content-type: application/json; charset=UTF-8");
http_response_code(200); //200 - Everything will be 200 Oke
$rawData = file_get_contents('php://input');
$data = json_decode($rawData, true);
if (is_null($data)) {
    echo 'Invalid JSON data.';
    exit;
}
function response($data, $code = 200)
{
    http_response_code($code);
    header('Content-Type: application/json');
    echo json_encode($data);
    die();
}
$accessKey = $config['accessKey'];
$secretKey = $config['secretKey'];
$response = array();
try {
    $amount = $data['amount'];
    $extraData = isset($data['extraData']) ? $data['extraData'] : '';
    $message = $data['message'];
    $orderId = $data['orderId'];
    $orderInfo = $data['orderInfo'];
    $orderType = $data['orderType'];
    $partnerCode = $data['partnerCode'];
    $payType = $data['payType'];
    $requestId = $data['requestId'];
    $responseTime = $data['responseTime'];
    $resultCode = $data['resultCode'];
    $transId = $data['transId'];
    $m2signature = $data['m2signature'];


    $rawHash = "accessKey=$accessKey&amount=$amount&extraData=$extraData&message=$message&orderId=$orderId&orderInfo=$orderInfo&orderType=$orderType&partnerCode=$partnerCode&payType=$payType&requestId=$requestId&responseTime=$responseTime&resultCode=$resultCode&transId=$transId";

    $partnerSignature = hash_hmac("sha256", $rawHash, $secretKey);
    file_put_contents('ipn.txt', $rawHash . PHP_EOL, FILE_APPEND);


    if ($m2signature == $partnerSignature) {
        $stmt = $conn->prepare("SELECT * FROM `order` WHERE `orderId` = :orderId");
        $stmt->bindValue(":orderId", $orderId);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result['status'] == 1) {
            if ($resultCode == '0') {
                $account_id = $result['account_id'];
                $monney = intval($amount) * 1.5;//20%
                $update = $conn->prepare("UPDATE `account` SET `temp_vnd` = `temp_vnd` + :monney, `tongnap` = `tongnap` + :monney WHERE `id` = :account_id");
                $update->bindValue(":monney", $monney);
                $update->bindValue(":account_id", $account_id);
                $update->execute();

                $update2 = $conn->prepare("UPDATE `order` SET `status` = '2', `transId` = :transId WHERE `orderId` = :orderId");
                $update2->bindValue(":transId", $transId);
                $update2->bindValue(":orderId", $orderId);
                $update2->execute();

                $result = '<div class="alert alert-success">Capture Payment Success</div>';
            } else {
                $update2 = $conn->prepare("UPDATE `order` SET `status` = '0', `transId` = :transId WHERE `orderId` = :orderId");
                $update2->bindValue(":transId", $transId);
                $update2->bindValue(":orderId", $orderId);
                $update2->execute();
                $result = '<div class="alert alert-danger">' . $message . '</div>';
            }
        }
    } else {
        $result = '<div class="alert alert-danger">This transaction could be hacked, please check your signature and returned signature</div>';
    }
} catch (Exception $e) {
    echo $response['message'] = $e;
}

$debugger = array();
$debugger['rawData'] = $rawHash;
$debugger['pay2sSignature'] = $m2signature;
$debugger['partnerSignature'] = $partnerSignature;

if ($m2signature == $partnerSignature) {
    $response['success'] = true;
    $response['message'] = "Received payment result success";
} else {
    $response['success'] = false;
    $response['message'] = "ERROR! Fail checksum";
}
$response['debugger'] = $debugger;
echo json_encode($response);