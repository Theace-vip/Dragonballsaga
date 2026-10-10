<?php
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');
?>
<div class="ibox-content m-b-sm forum-container">
    <div class="inbox-message">

        <div>
            <center>
                <H3>HƯỚNG DẪN TẢI GAME <?php echo htmlspecialchars($brandName); ?></H3>
            </center>
            <p style="line-height:1.9;">
                Chọn phiên bản phù hợp với thiết bị của bạn, tải về rồi cài đặt là vào chơi ngay.
                Tài khoản đăng ký trên web dùng trực tiếp trong game.
            </p>

            <h5><i class="fa fa-windows" style="color:red"></i> Máy tính (PC Windows)</h5>
            <p>
                <b>Bước 1</b>: Tải file về máy<br>
                <a class="btn btn-danger mb-3" style="border-radius: 10px;"
                   href="<?php echo htmlspecialchars($pc); ?>" target="_blank">Tải bản PC</a><br>
                <b>Bước 2</b>: Giải nén file vừa tải, bấm đúp vào file chạy game để vào chơi.
            </p>
            <hr>

            <h5><i class="fa fa-android" style="color:#3ddc84"></i> Android (APK)</h5>
            <p>
                <b>Bước 1</b>: Tải file APK về điện thoại<br>
                <a class="btn btn-danger mb-3" style="border-radius: 10px;"
                   href="<?php echo htmlspecialchars($adr); ?>" target="_blank">Tải bản Android</a><br>
                <b>Bước 2</b>: Mở file APK lên và cài đặt. Nếu máy báo không rõ nguồn gốc,
                vào <b>Cài đặt - Ứng dụng - Cho phép cài từ nguồn không xác định</b> rồi cài lại.
            </p>
            <hr>

            <h5><i class="fa fa-apple" style="color:#000"></i> iPhone / iPad (iOS - IPA)</h5>
            <p>
                <b>Bước 1</b>: Tải file IPA về máy<br>
                <a class="btn btn-danger mb-3" style="border-radius: 10px;"
                   href="<?php echo htmlspecialchars($ios); ?>" target="_blank">Tải bản iOS (IPA)</a><br>
                <b>Bước 2</b>: Cài file IPA bằng <b>Scarlet / AltStore / Sideloadly</b><br>
                Sau khi cài, vào <b>Cài đặt chung - Quản lý thiết bị - Tin cậy</b> nhà phát triển rồi mới mở game.
            </p>
            <hr>

            <p style="line-height:1.9;">
                <b style="color:red">LƯU Ý:</b> Một tài khoản dùng chung cho web và trong game.
                Admin không bao giờ hỏi mật khẩu của bạn. Gặp sự cố hãy bấm nút <b>Báo Lỗi</b> trên menu để vào Box Zalo.
            </p>
        </div>
    </div>
</div>
<?php require('views/layout/foot.php'); ?>