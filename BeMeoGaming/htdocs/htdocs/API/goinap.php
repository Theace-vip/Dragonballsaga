<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';

if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang lỗi
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}
?>

<style type="text/css">
    .bg-gray {
        background: #fff;
    }
    .badge-km {
        display: inline-block;
        padding: 2px 8px;
        border-radius: 4px;
        font-size: 11px;
        font-weight: 600;
        background: #ff4d4f;
        color: #fff;
        margin-top: 4px;
    }
    .package-old {
        font-size: 11px;
        text-decoration: line-through;
        color: #888;
        margin-right: 3px;
    }
</style>

<div class="form-card">
    <div class="form-group">
        <label>Thông tin nhân vật</label>
        <div class="form-block">
            <div class="form-block-info">
                <?= $_ServerName ?> - <?= maskUsername($_SESSION['usernameshow']) ?>
            </div>
        </div>
    </div>

    <div class="form-group">
        <label>Chọn gói nạp (Đang khuyến mãi x1.5)</label>
        <div class="error d-none" id="package-error" style="color: red; margin-bottom: 10px;"></div>

       <div class="list-packages" id="__game_package" data-href="/API/CachNap">

    <!-- 20K -> 30K Cash -->
    <div class="package">
        <label data-packageid="NRO_001" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">20K Cash</span> → <strong>30K Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">20.000 đ</div>
        </label>
    </div>

    <!-- 50K -> 75K Cash -->
    <div class="package">
        <label data-packageid="NRO_002" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">50K Cash</span> → <strong>75K Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">50.000 đ</div>
        </label>
    </div>

    <!-- 100K -> 150K Cash -->
    <div class="package">
        <label data-packageid="NRO_003" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">100K Cash</span> → <strong>150K Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">100.000 đ</div>
        </label>
    </div>

    <!-- 200K -> 300K Cash -->
    <div class="package">
        <label data-packageid="NRO_004" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">200K Cash</span> → <strong>300K Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">200.000 đ</div>
        </label>
    </div>

    <!-- 500K -> 750K Cash -->
    <div class="package">
        <label data-packageid="NRO_005" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">500K Cash</span> → <strong>750K Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">500.000 đ</div>
        </label>
    </div>

    <!-- 1M -> 1.5M Cash -->
    <div class="package">
        <label data-packageid="NRO_006" onclick="select_package(this)">
            <div class="package-gold">
                <span class="package-old">1M Cash</span> → <strong>1.5M Cash</strong>
                <div class="badge-km">x1.5</div>
            </div>
            <div class="package-price">1.000.000 đ</div>
        </label>
    </div>

</div>


            <!-- Popup gói thành viên (đang ẩn) -->
            <div class="package">
                <div class="d-none" id="popup-info_NRO_007">
                    <div class="modal-title">
                        Gói: <strong>Khuyến Mãi Thành Viên <?= $_ServerName ?></strong>
                    </div>
                    <div class="package-info">
                        <p>
                            <strong id="package_desc">
                                <ul>
                                    <li><strong>Khuyến Mãi Thành Viên <?= $_ServerName ?></strong></li>
                                </ul>
                            </strong>
                        </p>
                        <p>Bạn có chắc chắn muốn mua gói nạp này?</p>
                    </div>
                </div>
                <!-- Nếu sau này muốn mở gói này thì bỏ comment -->
                <!--
                <label data-packageid="NRO_007" onclick="select_package(this, 'is_popup')">
                    <div class="package-gold">Khuyến Mãi Mở Thành Viên</div>
                    <div class="package-price">50.000 đ</div>
                </label>
                -->
            </div>
        </div>
    </div>
</div>
