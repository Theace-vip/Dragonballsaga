<?php
// Sử dụng $partner_id và $partner_key từ config.php
$partner_id = $partner_id;
$partner_key = $partner_key;

// Xử lý khi người dùng submit form nạp thẻ
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit'])) {
    // Debug: In ra giá trị nhận từ form
    file_put_contents('debug_log.txt', print_r($_POST, true), FILE_APPEND);

    $card_type = strtoupper(trim($_POST['card_type'] ?? ''));
    $pin = trim($_POST['code'] ?? ''); // Lấy mã thẻ (pin)
    $serial = trim($_POST['serial'] ?? ''); // Lấy số seri
    $card_amount = intval($_POST['card_amount'] ?? 0);
    $request_id = rand(100009, 999999);

    // Debug: In ra các biến
    file_put_contents('debug_log.txt', "card_type: $card_type, pin: $pin, serial: $serial, card_amount: $card_amount\n", FILE_APPEND);

    if ($card_type && $pin && $serial && $card_amount > 0) {
        $sign = md5($partner_key . $pin . $serial);

        $url = "https://doithe1s.vn/chargingws/v2"; // URL của API Doithe1s
        $data = [
            'sign' => $sign,
            'telco' => $card_type,
            'code' => $pin,
            'serial' => $serial,
            'amount' => $card_amount,
            'request_id' => $request_id,
            'partner_id' => $partner_id,
            'command' => 'charging'
        ];

        // Debug: In ra dữ liệu gửi đi
        file_put_contents('debug_log.txt', "Data sent: " . print_r($data, true) . "\n", FILE_APPEND);

        // Gửi POST request
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data)); // Dữ liệu gửi qua POST
        $response = curl_exec($ch);
        curl_close($ch);

        // Xử lý kết quả trả về
        $result = json_decode($response, true);

        // Debug: In ra phản hồi từ API
        file_put_contents('debug_log.txt', "API Response: $response\n", FILE_APPEND);

        if ($result['status'] == 99) {
            // Lưu vào bảng nạp tiền
            $stmt = $conn->prepare("INSERT INTO napthe (username, telco, amount, serial, code, request_id, status, created_at) 
                VALUES (?, ?, ?, ?, ?, ?, 'pending', NOW())");
            $stmt->bind_param("ssssss", $username, $card_type, $card_amount, $serial, $pin, $request_id);

            $stmt->execute();

            $msg = "Nạp thẻ đang chờ xử lý!";
            $icon = "info";
            $redirect = "naptien.php";
        } else {
            $msg = "Nạp thẻ thất bại!";
            $icon = "error";
            $redirect = "naptien.php";
        }
    } else {
        $msg = "Vui lòng nhập đầy đủ thông tin thẻ!";
        $icon = "error";
        $redirect = "naptien.php";
    }

    echo "
    <script>
        Swal.fire({icon: '$icon', title: 'Thông Báo', text: '$msg', confirmButtonText: 'OK'})
        .then(function() { window.location.href = '$redirect'; });
    </script>";
}
?>

<form class="form-auth text-center" method="POST">
    <div class="form-group">
        <input type="text" name="serial" class="form-control" placeholder="Số seri thẻ..." required>
    </div>
    <div class="form-group">
        <input type="text" name="code" class="form-control" placeholder="Mã thẻ..." required>
    </div>
    <div class="form-group">
        <select name="card_type" class="form-control" required>
            <option value="" disabled selected>-- Chọn loại thẻ --</option>
            <option value="Viettel">Viettel (Chiết khấu 18%)</option>
            <option value="Mobifone">Mobifone (Chiết khấu 20%)</option>
            <option value="Vinaphone">Vinaphone (Chiết khấu 20%)</option>
            <!-- Bạn có thể thêm nhiều loại thẻ khác nếu cần -->
        </select>
    </div>
    <div class="form-group">
        <select name="card_amount" class="form-control" required>
            <option value="" disabled selected>-- Chọn mệnh giá --</option>
            <option value="10000">10,000 đ</option>
            <option value="20000">20,000 đ</option>
            <option value="50000">50,000 đ</option>
            <option value="100000">100,000 đ</option>
            <option value="200000">200,000 đ</option>
            <option value="500000">500,000 đ</option>
            <option value="1000000">1,000,000 đ</option>
        </select>
    </div>
    <button class="btn btn-lg btn-dark btn-block" style="border-radius: 10px;" type="submit" name="submit">Nạp Thẻ</button>
</form>

<div style="text-align: center;margin-top: 2rem; margin-bottom: 2rem;">
    <h4>Lịch sử thẻ nạp</h4>
    <table border="1" cellpadding="5" cellspacing="0" style="width:100%;text-align:center;">
        <thead>
            <tr>
                <th>STT</th>
                <th>Mã thẻ</th>
                <th>Serial</th>
                <th>Mệnh giá</th>
                <th>Loại thẻ</th>
                <th>Trạng thái</th>
                <th>Thời gian</th>
            </tr>
        </thead>
        <tbody>
            <?php
            $stt = 1;
            $username = isset($username) ? $username : ''; // Đảm bảo biến $username tồn tại
            $result = $conn->query("SELECT * FROM napthe WHERE username='$username' ORDER BY id DESC LIMIT 10");
            if ($result && $result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<tr>
                    <td>{$stt}</td>
                    <td>{$row['code']}</td>
                    <td>{$row['serial']}</td>
                    <td>" . number_format($row['amount']) . " đ</td>
                    <td>{$row['telco']}</td>
                    <td>{$row['status']}</td>
                    <td>{$row['created_at']}</td>
                </tr>";
                    $stt++;
                }
            } else {
                echo "<tr><td colspan='7'>Chưa có lịch sử nạp thẻ</td></tr>";
            }
            ?>
        </tbody>
    </table>
</div>