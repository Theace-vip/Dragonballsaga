<?php

require_once('config.php');

if(isset($_GET['status'])) {
	$code = $_GET['code'];
	$serial = $_GET['serial'];
	$amount = $_GET['amount'];
	$tranid = $_GET['request_id'];
	


	$callback_sign = md5($partner_key.$_GET['code'].$_GET['serial']);
    // echo $callback_sign;
	if($_GET['callback_sign'] == $callback_sign) { 
	
		if ($_GET['status'] == 1) {
			
		    // echo $tranid;
		    // Tránh SQL Injection bằng prepared statement
            $stmt = $conn->prepare("SELECT * FROM napthe WHERE request_id=? AND status='pending'");
            $stmt->bind_param("s", $tranid);
            $stmt->execute();
            $result = $stmt->get_result();
            $card = $result->fetch_assoc();
            // print_r($card);
            if ($card) {
				// Lấy thông tin user từ username trong $card
				$stmt = $conn->prepare("SELECT * FROM account WHERE username = ?");
				$stmt->bind_param("s", $card['username']);
				$stmt->execute();
				$result = $stmt->get_result();
				$user = $result->fetch_assoc();

				if ($user) {
					// Tính toán số cash sau khi trừ chiết khấu
					$chietkhau_card = isset($chietkhau_card) ? $chietkhau_card : 0;
					$handle_cash = $amount - ($amount * $chietkhau_card / 100);
					
					// Cập nhật cash mới
					$new_cash = $user['cash'] + $handle_cash;
					$new_totalCard = $user['danap'] + $handle_cash;
					//file_put_contents('debug_log.txt', "current cash: " . $user['cash'] . "\n", FILE_APPEND);
					//file_put_contents('debug_log.txt', "new cash: " . $handle_cash . "\n", FILE_APPEND);
					//file_put_contents('debug_log.txt', "new cash: " . $new_cash . "\n", FILE_APPEND);

					// Cập nhật tài khoản
					$stmt_update = $conn->prepare('UPDATE account SET cash = ?, danap = ? WHERE username = ?');
					$stmt_update->bind_param("dis", $new_cash, $new_totalCard, $user['username']);
					$success = $stmt_update->execute();

					if ($success) {
						// Đánh dấu thẻ đã nạp thành công
						$stmt_update_card = $conn->prepare('UPDATE napthe SET status = "success" WHERE request_id = ?');
						$stmt_update_card->bind_param("s", $tranid);
						$stmt_update_card->execute();

						echo "Nạp tiền thành công cho tài khoản: " . htmlspecialchars($user['username']);
					} else {
						echo "Lỗi khi cập nhật tài khoản!";
					}

				} else {
					echo "Lỗi: Không tìm thấy tài khoản!";
				}
			}

        
		} else {
            echo "Thẻ lỗi";
            $stmt_update = $conn->prepare('UPDATE napthe SET status = "error" WHERE request_id = ?');
            $stmt_update->bind_param("s" ,$tranid);
            $success = $stmt_update->execute();
			//test call back
			// $thucnhan = $thucnhan + 1000000;
			// mysqli_query($connect, "UPDATE `user` SET `tien` = tien + $thucnhan WHERE id = '$tranid'");

		}


	}

}
