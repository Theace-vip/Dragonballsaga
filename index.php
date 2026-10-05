<?php
session_start();
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

$login_status = isset($_SESSION['account']) && !empty($_SESSION['account']);
?>

<div class="ibox-content m-b-sm forum-container">
    <div class="text-center mb-1">
        <?php
        if ($login_status) {
            $playerInfo = getPlayerInfo($_SESSION['account']);
            if ($playerInfo) {
                switch ($playerInfo['gender']) {
                    case 0:
                        $avatarPath = "/assets/images/char/traidat.png";
                        break;
                    case 1:
                        $avatarPath = "/assets/images/char/namec.png";
                        break;
                    case 2:
                        $avatarPath = "/assets/images/char/xayda.png";
                        break;
                    default:
                        $avatarPath = "/assets/images/char/default.png";
                        break;
                }
                echo '<img src="' . $avatarPath . '" alt="Avatar" style="width: 50px; height: auto; border-radius: 50%;">';
                echo '<h3>' . htmlspecialchars($playerInfo['name']) . '</h3>';
                echo '<p>Số tiền: <span style="color:red;">' . number_format($_vnd) . ' vnd</span></p>';
            }
        }
        ?>
        <!-- <small class=""><b>TIẾP NHẬN BÁO LỖI NRO DAINO</b></small>
        <br> -->

    </div>
    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <h5>
            <b>Thông Báo</b>
        </h5>
    </div>
    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <div class="topic_name">
            <div style="width: 55px; float:left; margin-right: 10px;">
                <img class="avatar" src="/assets/images/char/quylao.png"
                    style="border-color:red; width: 50px; height: 55px;">
            </div>
            <a class="alert-link" href="/naptien" title="">
                <i class="fa fa-check-circle-o" aria-hidden="true" style="color:red"></i> Nạp Game
            </a>
            <div class="box_name_eman">bởi <b><b>
                        <font style="color:red">Quy Lão</font>
                    </b></b> - <span>Vui lòng đăng nhập trước khi ấn vào.</span></div>
        </div>
    </div>

    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <div class="topic_name">
            <div style="width: 55px; float:left; margin-right: 10px;">
                <img class="avatar" src="/assets/images/char/hit.png"
                    style="border-color:red; width: 50px; height: 55px;">
            </div>
            <a class="alert-link" href="/activated" title="">
                <i class="fa fa-user-circle" aria-hidden="true" style="color:red"></i> Hướng Dẫn Kích Hoạt Thành Viên
            </a>
            <div class="box_name_eman">bởi <b><b>
                        <font style="color:red">Hit</font>
                    </b></b> - <span>Giá chỉ 10.000</span></div>
        </div>
    </div>

    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <div class="topic_name">
            <div style="width: 55px; float:left; margin-right: 10px;">
                <img class="avatar" src="/assets/images/char/rose.png"
                    style="border-color:red; width: 50px; height: 55px;">
            </div>
            <a class="alert-link" href="/huongdan" title="">
                <i class="fa fa-mobile" aria-hidden="true" style="color:red"></i> Hướng Dẫn Cài Đặt Phiên Bản IOS
            </a>
            <div class="box_name_eman">bởi <b><b>
                        <font style="color:red">ADMIN</font>
                    </b></b> - <span>Siêu đơn giản.</span></div>
        </div>
    </div>

    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <div class="topic_name">
            <div style="width: 55px; float:left; margin-right: 10px;">
                <img class="avatar" src="/assets/images/char/8.png"
                    style="border-color:red; width: 50px; height: 55px;">
            </div>
            <a class="alert-link" href="/chucnang" title="">
                <i class="fa fa-mobile" aria-hidden="true" style="color:red"></i> Giới Thiệu Và Hướng Dẫn <?php echo $namegame ?>
            </a>
            <div class="box_name_eman">bởi <b><b>
                        <font style="color:red">ADMIN</font>
                    </b></b> - <span>Các tính năng game.</span></div>
        </div>
    </div>

    <div class="alert alert-warning" style="background-color: #fdf8da;">
        <div class="topic_name">
            <div style="width: 55px; float:left; margin-right: 10px;">
                <img class="avatar" src="/assets/images/char/bunma.png"
                    style="border-color:red; width: 50px; height: 55px;">
            </div>
            <a class="alert-link" href="/mocnap" title="">
                <i class="fa fa-mobile" aria-hidden="true" style="color:red"></i> Thông Tin Mốc Nạp <?php echo $namegame ?>
            </a>
            <div class="box_name_eman">bởi <b><b>
                        <font style="color:red">ADMIN</font>
                    </b></b> - <span>Các tính năng game.</span></div>
        </div>
    </div>
</div>
<?php require('views/layout/info.php'); ?>
<?php require('views/layout/foot.php'); ?>