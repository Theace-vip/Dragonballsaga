<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';


if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}

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
                    <button type="button" class="previous btn-change" onclick="back_step_previous(1)">Change Role</button>
                </div> -->
            </div>
        </div>
        <div class="form-group">
            <label>Chọn gói nạp</label>
            <div class="error d-none" id="package-error" style="color: red; margin-bottom: 10px;"></div>
            <div class="list-packages" id="__game_package" data-href="/API/CachNap">
                <div class="package">
                    <label data-packageid="NRO_001" onclick="select_package(this)">
                        <div class="package-gold">40K Coin</div>
                        <div class="package-price">20.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <label data-packageid="NRO_002" onclick="select_package(this)">
                        <div class="package-gold">100K Coin</div>
                        <div class="package-price">50.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <label data-packageid="NRO_003" onclick="select_package(this)">
                        <div class="package-gold">200k Coin</div>
                        <div class="package-price">100.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <label data-packageid="NRO_004" onclick="select_package(this)">
                        <div class="package-gold">400k Coin</div>
                        <div class="package-price">200.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <label data-packageid="NRO_005" onclick="select_package(this)">
                        <div class="package-gold">1000k Coin</div>
                        <div class="package-price">500.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <label data-packageid="NRO_006" onclick="select_package(this)">
                        <div class="package-gold">2M Coin</div>
                        <div class="package-price">1.000.000 đ</div>
                    </label>
                </div>
                <div class="package">
                    <div class="d-none" id="popup-info_NRO_007">
                        <div class="modal-title">
                            Gói: <strong>Khuyến Mãi Thành Viên <?= $_ServerName ?></strong>
                        </div>
                        <div class="package-info">
                            <p>
                                <strong id="package_desc">
                                    <ul>
                                        <li>
                                            <strong>Khuyến Mãi Thành Viên <?= $_ServerName ?></strong>
                                        </li>
                                    </ul>
                                </strong>
                            </p>
                            <p>Bạn có chắc chắn muốn mua gói nạp này? </p>
                        </div>
                    </div>
                    <!-- <label data-packageid="NRO_007" onclick="select_package(this, 'is_popup')">
                        <div class="package-gold">Khuyến Mãi Mở Thành Viên</div>
                        <div class="package-price">50.000 đ</div>
                    </label> -->
                </div>
            </div>
        </div>
    </div>
