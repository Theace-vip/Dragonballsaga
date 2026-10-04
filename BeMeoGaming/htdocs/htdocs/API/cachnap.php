<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';
// Hàm để xử lý yêu cầu và trả về HTML phương thức thanh toán


if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}

function getPaymentMethodsHTML($packageId)
{
    ob_start();
    global $_packagePrices, $_price, $_ServerName;
    // Kiểm tra nếu packageId tồn tại trong mảng, lấy giá tương ứng
    if (isset($_packagePrices[$packageId])) {
        $_price = $_packagePrices[$packageId];
    } else {
        $_price = 'Giá không xác định';
    }
    $_SESSION['pricenap'] = $_price;
?>


    <style type="text/css">
        .bg-gray {
            background: #fff;
        }
    </style>
    <div class="form-card">
        <div class="form-group">
            <label>Thông tin nhân vật</label>
            <div class="form-block">
                <div class="form-block-info"><?= $_ServerName ?> - <?= maskUsername($_SESSION['usernameshow']) ?></div>
                <!-- <div class="form-block-action">
                    <button type="button" class="previous btn-change" onclick="back_step_previous(2)">Change package</button>
                </div> -->
            </div>
        </div>
        <div class="form-group"><label>Mệnh giá</label>
            <div class="form-block">
                <div class="form-block-info">
                    <div class="form-block-info">
                        <?php echo $_price; ?> đ
                    </div>

                </div>
                <div class="form-block-action"><button type="button" class="previous btn-change" onclick="back_step_previous(2)">Change package</button></div>
            </div>
        </div>
        <div class="form-group" id="__method_group"><label>Chọn phương thức</label>
            <div class="error d-none" id="method-error" style="color: red; margin-bottom: 10px;"></div>
            <div class="list-methods block" id="__game_method" data-href="/API/DoNap">
                <!-- <div class="method"><input type="radio" name="payment_method" id="card"><label for="card" data-method="card" data-packageid="<?php echo $packageId; ?>" data-source-package="<?php echo $packageId; ?>" onclick="select_method(this)">
                        <div class="method-detail">
                            <div class="method-thumb"><img src="/Assets/frontend/img/logo-cards/card.png" class="img-fluid"></div>
                            <div class="method-meta">
                                <div class="method-name"><?php echo $_price; ?> VND</div>
                                <div class="method-gift no-gift">Card Tự Động</div>
                            </div> -->
                        </div>
                    </label></div>
                <!-- <div class="method"><input type="radio" name="payment_method" id="momo_ewallet"><label for="momo_ewallet" data-method="momo_ewallet" data-packageid="<?php echo $packageId; ?>" data-source-package="<?php echo $packageId; ?>" onclick="select_method(this)">
                        <div class="method-detail">
                            <div class="method-thumb"><img src="/Assets/frontend/img/logo-cards/momo_icon_square_pinkbg@3x.png" class="img-fluid"></div>
                            <div class="method-meta">
                                <div class="method-name"><?php echo $_price; ?> VND</div>
                                <div class="method-gift no-gift">MoMo</div>
                            </div>
                        </div>
                    </label></div>
                <div class="method"><input type="radio" name="payment_method" id="appota_ewallet"><label for="zalopay" data-method="zalopay" data-packageid="<?php echo $packageId; ?>" data-source-package="<?php echo $packageId; ?>" onclick="select_method(this)">
                        <div class="method-detail">
                            <div class="method-thumb"><img src="/Assets/frontend/img/logo-cards/zalo-pay.png" class="img-fluid"></div>
                            <div class="method-meta">
                                <div class="method-name"><?php echo $_price; ?> VND</div>
                                <div class="method-gift no-gift">Ví ZaloPay</div>
                            </div>
                        </div>
                    </label></div> -->
                <div class="method"><input type="radio" name="payment_method" id="bank_visa"><label for="bank_visa" data-method="bank_visa" data-packageid="<?php echo $packageId; ?>" data-source-package="<?php echo $packageId; ?>" onclick="select_method(this)">
                        <div class="method-detail">
                            <div class="method-thumb"><img src="/Assets/frontend/img/logo-cards/credit-card.jpg" class="img-fluid"></div>
                            <div class="method-meta">
                                <div class="method-name"><?php echo $_price; ?> VND</div>
                                <div class="method-gift no-gift">ATM</div>
                            </div>
                        </div>
                    </label></div>
            </div>
        </div>
    </div>

<?php
    $html = ob_get_contents();
    ob_end_clean();
    return $html;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $packageId = isset($_POST['source_package_id']) ? $_POST['source_package_id'] : '';

    if (empty($packageId)) {
        echo json_encode([
            'status' => false,
            'message' => 'Mã gói không hợp lệ.'
        ]);
        exit();
    }

    $html = getPaymentMethodsHTML($packageId);

    echo json_encode([
        'status' => true,
        'html' => $html
    ]);
    exit();
} else {
    echo json_encode([
        'status' => false,
        'message' => 'Phương thức yêu cầu không hợp lệ.'
    ]);
    exit();
}
?>