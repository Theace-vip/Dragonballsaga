<?php
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';

$accessKey = $config['accessKey'];
$secretKey = $config['secretKey'];
$response = array();
try {
    $amount = $_GET['amount'];
    $extraData = isset($_GET['extraData']) ? $_GET['extraData'] : '';
    $message = $_GET['message'];
    $orderId = $_GET['orderId'];
    $orderInfo = $_GET['orderInfo'];
    $orderType = $_GET['orderType'];
    $partnerCode = $_GET['partnerCode'];
    $payType = $_GET['payType'];
    $requestId = $_GET['requestId'];
    $responseTime = $_GET['responseTime'];
    $resultCode = $_GET['resultCode'];
    $transId = $_GET['transId'] ?? '';
    $m2signature = $_GET['m2signature'];

    $rawHash = "accessKey=$accessKey&amount=$amount&message=$message&orderId=$orderId&orderInfo=$orderInfo&orderType=$orderType&partnerCode=$partnerCode&payType=$payType&requestId=$requestId&responseTime=$responseTime&resultCode=$resultCode";

    $partnerSignature = hash_hmac("sha256", $rawHash, $secretKey);
    file_put_contents('atm_callback.txt', $rawHash . PHP_EOL, FILE_APPEND);


    if ($m2signature == $partnerSignature) {
        $stmt = $conn->prepare("SELECT * FROM `order` WHERE `orderId` = :orderId");

        $stmt->bindValue(":orderId", $orderId);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result['status'] == 1) {
            if ($resultCode == '0') {
            } else {
                $update2 = $conn->prepare("UPDATE `order` SET `status` = '0', `transId` = :transId WHERE `orderId` = :orderId");
                $update2->bindValue(":transId", $transId);
                $update2->bindValue(":orderId", $orderId);
                $update2->execute();
            }
        }
    } else {
        // $result = '<div class="alert alert-danger">This transaction could be hacked, please check your signature and returned signature</div>';
    }
} catch (Exception $e) {
}

header("Location: /");