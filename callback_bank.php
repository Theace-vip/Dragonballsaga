<?php 
require_once('config.php');
file_put_contents('debug_log_bank.txt', "no bypass header: " . print_r(json_encode($data), true) . "\n", FILE_APPEND);
$headers = getallheaders();
//print_r($headers);
if (!isset($headers['authorization']) || $headers['authorization'] !== 'Apikey '.$sepay_secret) {
    http_response_code(403);
    die('Lỗi: Không có quyền truy cập ');
}
// print_r($_REQUEST);
$data = json_decode(file_get_contents('php://input'), true);
// print_r($data['id']);
file_put_contents('debug_log_bank.txt', "Data sent: " . print_r(json_encode($data), true) . "\n", FILE_APPEND);
if ($data){
    $partner_id = isset($data['id']) ? intval($data['id']) : 0;
    $noidung = strtolower(isset($data['content']) ? $data['content'] : '');
    $amount = isset($data['transferAmount']) ? floatval($data['transferAmount']) : 0;
    $comment = false;
    // print_r($noidung);
    // Tìm username trong nội dung chuyển khoản
    // if (preg_match('/'.$noidung_bank.'([a-zA-Z0-9_]+)/', $noidung, $matches)) {
    //     $comment = $matches[1];
    // }
    if (preg_match('/'.$noidung_bank.'(\d+)/', $noidung, $matches)) {
        $comment = $matches[1]; // Kết quả: '1'
    }


    if (!$comment){
        die('Lỗi: Nội dung không hợp lệ');
    }
    
    

    // Tránh SQL Injection bằng prepared statement
    $stmt = $conn->prepare("SELECT * FROM account WHERE username=?");
    $stmt->bind_param("s", $comment);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();

    if ($user){
        // Xem tồn tại giao dịch chưa
        $stmt = $conn->prepare("SELECT * FROM history_bank WHERE username=? AND code=?");
        $stmt->bind_param("ss", $user['username'], $partner_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $isPayment = $result->fetch_assoc();
        if ($isPayment){
            echo "giao dịch đã tồn tại";
            die();    
        }
        // tồn tại user
        $handle_cash = ($amount - ($amount * $chietkhau_bank / 100));
        $chietkhau_bank = isset($chietkhau_bank) ? $chietkhau_bank : 0;
        $new_cash = $user['cash'] + $handle_cash;
        $new_totalCard = $user['danap'] + $new_cash;

        // Cập nhật cash và danap cho user
        $stmt_update = $conn->prepare('UPDATE account SET cash = ?, danap = ? WHERE username = ?');
        $stmt_update->bind_param("dis", $new_cash, $new_totalCard, $user['username']);
        $success = $stmt_update->execute();

        if ($success) {
            //  echo "Nạp tiền thành công cho tài khoản: " . htmlspecialchars($user['username']);
            // Ghi vào lịch sử nạp tiền
            $stmt_log = $conn->prepare('INSERT INTO history_bank (username, amount_vnd, amount_cash,description,code, created_at) VALUES (?, ?, ?, ?, ?, NOW())');
            $stmt_log->bind_param("sddss",$user['username'], $amount, $handle_cash, $noidung, $partner_id);
            $stmt_log->execute();

            echo "Nạp tiền thành công cho tài khoản: " . htmlspecialchars($user['username']);
        }
    } else {
        echo "Lỗi: Không tìm thấy tài khoản!";
    }
}
?>