<?php
$noidung = $noidung_bank;
?>

<div style="display: flex; flex-direction: column; justify-content: center; align-items: center; margin-top: 2rem; gap: 10px;">
    <!-- input nhập số tiền -->
    <div style="margin-bottom: 1rem; display: flex; gap: 5px;">
        <input type="number" id="amountInput" placeholder="Nhập số tiền" style="padding: 5px; border-radius: 5px; border: 1px solid #ccc;">
        <a id="generateQR" target="_blank" class="btn btn-download text-white m-1" style="border-radius: 10px; width: 100px;">Tạo mã QR</a>
    </div>

    <!-- ảnh qr -->
    <div id="qrContainer" style=" width: 260px; height: 400px; border: 1px solid #ccc; padding: 15px; border-radius: 10px; background-color: #f9f9f9; box-shadow: 0 0 10px rgba(0,0,0,0.05); display: flex; flex-direction: column; align-items: center;">
        <img id="qrImage" src="" alt="QR Code" style="max-width: 100%; max-height: 100%; display: none; margin-bottom: 10px;">

        <!-- Thông tin người nhận -->
        <div id="recipientInfo" style="text-align: center; display: none;">
            <p style="margin: 4px 0;"><strong>Ngân hàng:</strong> MB Bank</p>
            <p style="margin: 4px 0;"><strong>CTK:</strong> <?= $bank_owner ?></p>
            <p style="margin: 4px 0;"><strong>STK:</strong> <?= $bank_account ?></p>
            <p style="margin: 4px 0;"><strong>Nội dung:</strong> <?= $noidung ?><?= $_SESSION['account'] ?></p>
        </div>
    </div>

    <!-- nội dung chuyển khoản -->
    <!-- <div style="margin-top: 1rem; display: flex;">
        <span style="background: gray; padding: 5px; border-radius: 5px 0 0 5px; color: white;">Nội dung</span>
        <input type="text" readonly value="<?= $noidung ?><?= $_SESSION['account'] ?>" style="border-radius: 0 5px 5px 0; border: 1px solid #ccc; padding: 5px;">
    </div> -->
</div>

<!-- Lịch sử nạp -->
<div style="text-align: center; margin-top: 2rem; margin-bottom: 2rem;">
    <h4>Lịch sử nạp</h4>
    <table border="1" cellpadding="5" cellspacing="0" style="width:100%;text-align:center; border-collapse: collapse;">
        <thead style="background-color: #f0f0f0;">
            <tr>
                <th>STT</th>
                <th>Mã giao dịch</th>
                <th>Số tiền</th>
                <th>Thời gian</th>
            </tr>
        </thead>
        <tbody>
            <?php
            $stt = 1;
            $username = isset($username) ? $username : '';
            $result = $conn->query("SELECT * FROM history_bank WHERE username='$username' ORDER BY id DESC LIMIT 10");
            if ($result && $result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<tr>
                    <td>{$stt}</td>
                    <td>{$row['code']}</td>
                    <td>" . number_format($row['amount_vnd']) . " đ</td>
                    <td>{$row['created_at']}</td>
                </tr>";
                    $stt++;
                }
            } else {
                echo "<tr><td colspan='4'>Chưa có lịch sử nạp thẻ</td></tr>";
            }
            ?>
        </tbody>
    </table>
</div>

<script>
    const amountInput = document.getElementById('amountInput');
    const generateBtn = document.getElementById('generateQR');
    const qrImage = document.getElementById('qrImage');
    const recipientInfo = document.getElementById('recipientInfo');

    generateBtn.addEventListener('click', function() {
        const amount = amountInput.value;
        const account = "<?= $_SESSION['account'] ?>";
        const bankAccount = "<?= $bank_account ?>";
        const noidung = "<?= $noidung ?>";

        if (amount > 0) {
            if (amount <= 10000) {
                alert("Vui lòng nhập số tiền lớn hơn 10.000 VNĐ để tạo mã.");
                return;
            }
            const qrUrl = `https://qr.sepay.vn/img?bank=MBBank&acc=${bankAccount}&template=&amount=${amount}&des=${noidung}_${account}`;
            qrImage.src = qrUrl;
            qrImage.style.display = 'block';
            recipientInfo.style.display = 'block';
        } else {
            alert("Vui lòng nhập số tiền hợp lệ.");
        }
    });
</script>