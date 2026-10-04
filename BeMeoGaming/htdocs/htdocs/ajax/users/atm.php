<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';

if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}

function execPostRequest($url, $data)
{
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt(
        $ch,
        CURLOPT_HTTPHEADER,
        array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($data)
        )
    );
    curl_setopt($ch, CURLOPT_TIMEOUT, 5);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 5);
    //execute post
    $result = curl_exec($ch);
    //close connection
    curl_close($ch);
    return $result;
}


if($_Id) {
    $endpoint = 'https://payment.pay2s.vn/v1/gateway/api/create';
    $merchantName = $_ServerName;
    $accessKey = $config['accessKey'];
    $secretKey = $config['secretKey'];
    $order_desc = 'donate{{orderid}}';
    $partnerCode = $config['partnerCode'];
    $redirectUrl = 'https://hondaodragon.online/ajax/atm_callback.php';
    $ipnUrl = 'https://hondaodragon.online/ajax/ipn.php';
    $amount = '50000';
    $bank_accounts = "099964399999|mbb";
    $orderId = time() . "";
    $requestId = time() . "";
    $requestType = 'pay2s';
    
    if (!empty($_POST)) {
    
        $amount = $_POST["amount"];
        $orderInfo = str_replace('{{orderid}}', $orderId, $order_desc);
        // $bank_accounts = $_POST['bank_accounts'];
        $lines = explode("\n", $bank_accounts);
        $bankList = [];
        foreach ($lines as $line) {
    
            list($accountNumber, $bankId) = explode('|', trim($line));
            $bankList[] = [
                'account_number' => trim($accountNumber),
                'bank_id' => trim($bankId)
            ];
        }
        $requestId = time() . '';
    
        $rawHash = "accessKey=" . $accessKey . "&amount=" . $amount . "&bankAccounts=Array&ipnUrl=" . $ipnUrl . "&orderId=" . $orderId . "&orderInfo=" . $orderInfo . "&partnerCode=" . $partnerCode . "&redirectUrl=" . $redirectUrl . "&requestId=" . $requestId . "&requestType=" . $requestType;
        $signature = hash_hmac("sha256", $rawHash, $secretKey);
        $data = array(
            'accessKey' => $accessKey,
            'partnerCode' => $partnerCode,
            'partnerName' => $merchantName,
            'requestId' => $requestId,
            'amount' => $amount,
            'orderId' => $orderId,
            'orderInfo' => $orderInfo,
            'orderType' => $requestType,
            'bankAccounts' => $bankList,
            'redirectUrl' => $redirectUrl,
            'ipnUrl' => $ipnUrl,
            'requestType' => $requestType,
            'signature' => $signature,
        );
    
        $table = "order";
        $dataPost = array(
            "account_id" => $_Id,
            'requestId' => $requestId,
            'amount' => $amount,
            'orderId' => $orderId,
            'orderInfo' => $orderInfo,
            'orderType' => $requestType,
            'signature' => $signature,
            'status' => '1',
            "created_at" => date("Y-m-d H:i:s")
        );
        // $CVH->insert($table, $dataPost);
        insert($conn, $table, $dataPost);
        $result = execPostRequest($endpoint, json_encode($data));
        $jsonResult = json_decode($result, true);
        Ex(true, "Tạo link thanh toán thành công!",['payUrl' => $jsonResult['payUrl']]);
    }
} else {
    Ex(false, "Bạn chưa đăng nhập vui lòng đăng nhập để thực hiện thao tác này!");
}

?>