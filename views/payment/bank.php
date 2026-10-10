<?php
// Trang nap tien chuyen khoan - Sacombank (VietQR)
$noidung = $noidung_bank;
$username = isset($_SESSION['account']) ? $_SESSION['account'] : '';
$noidungFull = $noidung . $username;
$qrBase = 'https://img.vietqr.io/image/' . rawurlencode($bank_code) . '-' . rawurlencode($bank_account) . '-compact2.png';
// Ty le nap hien tai (su kien x2/x3/.../x50) do ControlPanel dat trong panel_nap_rate
if (!function_exists('bank_nap_rate_multiplier')) {
    require_once __DIR__ . '/../../bank_nap.php';
}
$tyleNap = bank_nap_rate_multiplier($conn);
?>

<div style="display: flex; flex-direction: column; justify-content: center; align-items: center; margin-top: 2rem; gap: 10px;">
<?php if ($tyleNap > 1) { ?>
    <div style="max-width: 620px; width: 100%; text-align: center; padding: 10px 14px; border-radius: 10px; background-color: #fff7e0; border: 1px solid #f0c36d; color: #8a5a00;">
        🔥 <strong>Sự kiện nạp: x<?= (int) $tyleNap ?></strong> — nạp 20.000đ nhận
        <strong><?= number_format(20000 * (int) $tyleNap) ?>đ</strong>. Hệ thống tự cộng đúng tỷ lệ này sau khi ngân hàng báo có.
    </div>
<?php } ?>
    <div style="max-width: 620px; text-align: center; line-height: 1.6;">
        <p>Nạp tiền bằng cách <strong>chuyển khoản</strong> tới tài khoản bên dưới, ghi đúng nội dung.
           Hệ thống tự động cộng tiền sau khi ngân hàng báo có (thường 10–60 giây).</p>
    </div>

    <!-- Thông tin người nhận -->
    <div style="width: 100%; max-width: 620px; border: 1px solid #ccc; padding: 15px; border-radius: 10px; background-color: #f9f9f9; box-shadow: 0 0 10px rgba(0,0,0,0.05);">
        <table style="width:100%; border-collapse: collapse;">
            <tr>
                <td style="padding: 4px 0; width: 38%;">Ngân hàng</td>
                <td style="padding: 4px 0;"><strong><?= htmlspecialchars($bank_name) ?></strong></td>
            </tr>
            <?php if ($bank_owner !== '') { ?>
            <tr>
                <td style="padding: 4px 0;">Chủ tài khoản</td>
                <td style="padding: 4px 0;"><strong><?= htmlspecialchars($bank_owner) ?></strong></td>
            </tr>
            <?php } ?>
            <tr>
                <td style="padding: 4px 0;">Số tài khoản</td>
                <td style="padding: 4px 0;">
                    <strong id="stk"><?= htmlspecialchars($bank_account) ?></strong>
                    <button type="button" onclick="copyText('<?= htmlspecialchars($bank_account) ?>', this)" style="cursor:pointer;">Copy</button>
                </td>
            </tr>
            <tr>
                <td style="padding: 4px 0;">Nội dung</td>
                <td style="padding: 4px 0;">
                    <strong id="des" style="color: #c00;"><?= htmlspecialchars($noidungFull) ?></strong>
                    <button type="button" onclick="copyText('<?= htmlspecialchars($noidungFull) ?>', this)" style="cursor:pointer;">Copy</button>
                </td>
            </tr>
        </table>
        <p style="margin: 8px 0 0; color: #666; font-size: 13px;">
            Lưu ý: nội dung chuyển khoản phải giữ đúng chữ "<?= htmlspecialchars($noidung) ?>" + tên tài khoản
            (<em><?= htmlspecialchars($noidungFull) ?></em>), nếu sai hệ thống không tự cộng tiền được.
        </p>
    </div>

    <!-- input nhập số tiền -->
    <div style="margin-bottom: 1rem; display: flex; gap: 5px;">
        <input type="number" id="amountInput" placeholder="Nhập số tiền" style="padding: 5px; border-radius: 5px; border: 1px solid #ccc;">
        <a id="generateQR" class="btn btn-download text-white m-1" style="cursor:pointer; border-radius: 10px; width: 100px; text-align:center;">Tạo mã QR</a>
    </div>

    <!-- ảnh qr -->
    <div id="qrContainer" style="width: 260px; min-height: 260px; border: 1px solid #ccc; padding: 15px; border-radius: 10px; background-color: #f9f9f9; box-shadow: 0 0 10px rgba(0,0,0,0.05); display: flex; flex-direction: column; align-items: center; justify-content: center;">
        <img id="qrImage" src="" alt="QR Code" style="max-width: 100%; display: none;">
        <span id="qrHint" style="color:#888; font-size: 13px; text-align:center;">Nhập số tiền rồi bấm "Tạo mã QR"</span>
    </div>

    <!-- Kiểm tra nạp -->
    <div style="margin-top: 10px; text-align: center;">
        <button type="button" id="checkBank" style="cursor: pointer; padding: 8px 16px; border-radius: 5px;">Đã chuyển khoản? Kiểm tra nạp</button>
        <div id="checkResult" style="margin-top: 8px; font-size: 14px;"></div>
    </div>
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
            $stmt = $conn->prepare("SELECT * FROM history_bank WHERE username=? ORDER BY id DESC LIMIT 10");
            $stmt->bind_param("s", $username);
            $stmt->execute();
            $result = $stmt->get_result();
            if ($result && $result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<tr>
                    <td>{$stt}</td>
                    <td>" . htmlspecialchars($row['code']) . "</td>
                    <td>" . number_format($row['amount_vnd']) . " đ</td>
                    <td>" . htmlspecialchars($row['created_at']) . "</td>
                </tr>";
                    $stt++;
                }
            } else {
                echo "<tr><td colspan='4'>Chưa có lịch sử nạp chuyển khoản</td></tr>";
            }
            ?>
        </tbody>
    </table>
</div>

<script>
    const amountInput = document.getElementById('amountInput');
    const generateBtn = document.getElementById('generateQR');
    const qrImage = document.getElementById('qrImage');
    const qrHint = document.getElementById('qrHint');
    const qrBase = "<?= $qrBase ?>";
    const bankAccount = "<?= htmlspecialchars($bank_account) ?>";
    const accountName = "<?= htmlspecialchars($bank_owner) ?>";
    const des = "<?= htmlspecialchars($noidungFull) ?>";
    const bankMin = <?= (int) $bank_min ?>;

    function copyText(text, btn) {
        navigator.clipboard.writeText(text).then(function () {
            const old = btn.innerText;
            btn.innerText = 'Đã copy';
            setTimeout(function () { btn.innerText = old; }, 1500);
        });
    }

    generateBtn.addEventListener('click', function () {
        const amount = parseInt(amountInput.value, 10);
        if (!amount || amount < bankMin) {
            alert('Vui lòng nhập số tiền từ ' + bankMin.toLocaleString('vi-VN') + ' VNĐ trở lên.');
            return;
        }
        let url = qrBase + '?amount=' + amount + '&addInfo=' + encodeURIComponent(des);
        if (accountName) {
            url += '&accountName=' + encodeURIComponent(accountName);
        }
        qrImage.src = url;
        qrImage.style.display = 'block';
        qrHint.style.display = 'none';
    });

    // Nút kiểm tra nạp: gọi SePay API tìm giao dịch khớp nội dung
    document.getElementById('checkBank').addEventListener('click', function () {
        const btn = this;
        const box = document.getElementById('checkResult');
        btn.disabled = true;
        box.style.color = '#555';
        box.innerText = 'Đang kiểm tra...';
        fetch('/check-bank.php', { method: 'POST', headers: { 'Content-Type': 'application/json' } })
            .then(function (r) { return r.json(); })
            .then(function (data) {
                box.style.color = data.success ? (data.credited > 0 ? 'green' : '#c60') : 'red';
                box.innerText = data.message || '';
                if (data.credited > 0) {
                    setTimeout(function () { window.location.reload(); }, 1500);
                }
            })
            .catch(function () {
                box.style.color = 'red';
                box.innerText = 'Không kiểm tra được, thử lại sau.';
            })
            .finally(function () { btn.disabled = false; });
    });
</script>
