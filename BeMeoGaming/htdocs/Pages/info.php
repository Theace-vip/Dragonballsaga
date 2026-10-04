<?php
require_once '../DHKD/Header.php';
$errorMsg = "";
$succesMsg = "";
if($_POST["old-password"] && $_POST["new-password"] && $_POST["confirm-password"]){
    $old_password = $_POST["old-password"];
    $new_password = $_POST["new-password"];
    $confirm_password = $_POST["confirm-password"];
    if($old_password == $_Password) {
        if($old_password != $new_password){
            if($new_password == $confirm_password){
                updatePassword($conn, $_Id, $new_password);
                $succesMsg = "Đổi mật khẩu thành công";
            } else
                $errorMsg = "Mật khẩu mới không được giống mật khẩu cũ";
        }
        else
            $errorMsg = "Mật khẩu mới không được giống mật khẩu cũ";
    }
    else 
        $errorMsg = "Mật khẩu cũ không đúng";

    
}
#Duong Huynh Khanh Dang
?>
<style>
    .btn-download-popup {
        background-color: #ffcc00;
        border: none;
        padding: 15px 30px;
        color: #fff;
        font-size: 24px;
        text-decoration: none;
        display: inline-block;
        border-radius: 5px;
    }
    .status-good {
        color: green;
    }

    .status-banned {
        color: red;
    }

    .status-pending {
        color: orange;
    }

    .accordion-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
    }

    .accordion {
        margin: 10px;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        min-width: 200px;
        /* Đảm bảo kích thước tối thiểu */
    }

    form {
        font-size: 1.17rem;
    }

    input[type=password] {
        width: 100%;
        max-width: 70%;
        padding: 20px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
        background-color: #fff;
    }
    .status-good {
        color: green;
        font-weight: bold;
        margin: 10px 0;
    }

    .status-banned {
        color: red;
        font-weight: bold;
        margin: 10px 0;
    }
</style>

<div class="layer">
    <div class="loading-container">
        <div class="tank-gif"></div>
    </div>
</div>
<div id="wrapper" class="en wrapper scaleDesktop scaleMobile">
    <div class="rating">
        <img src="/img.game/products/metalslug/vng-18.jpg" alt="" class="">
    </div>


    <section id="faqs" class="section faqs scrollFrame" data-block-id="faqs" style="min-height: 1400px;">
        <div class="section__background">
            <img src="/Assets/images/bg-top.png" alt="" class="bg-top pc">
            <img src="//global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/faqs/images/bg-top-mb.jpg" alt="" class="bg-top mb">
            <div class="bg-loop"></div>
            <img src="//global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/faqs/images/bg-bottom.jpg" alt="" class="bg-bottom pc">
            <img src="//global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/faqs/images/bg-bottom-mb.jpg" alt="" class="bg-bottom mb">


            <span id="faqs-scrollwatch-pin" class="scrollwatch-pin"></span>
        </div>


        <div class="section__content">
            <div class="faq-container">
                <div class="title">Thông Tin</div>
                <center>
                    <div class="accordion-container">
                        <div class="accordion">
                            <h3>ID : <?= $_Id ?></h3>
                            <h3>Tên đăng nhập : <?= $_Username ?></h3>
                            <h3>Số tiền : <?= number_format($_Coins, 0, ',', '.');  ?> VND</h3>
                        </div>
                        <div class="accordion">
                            <h3>Thành Viên</h3>
                            <td class="text-success fw-bold">
                                <?php echo $_Status == 0 ? '<h4 class="status-good">Đã kích hoạt</h4>' : '<h4 class="status-pending">Chưa kích hoạt</h4>' ?>
                            </td>
                            


                        </div>
                        <div class="accordion">
                        <?php
                            // Debug: Hiển thị giá trị và kiểu dữ liệu của $_Status                       


                            if ($_Band == 0) {
                                echo '<h3>Trạng thái: </h3><h4 class="status-good">Người chơi tốt</h4>';
                            } else {
                                echo '<h3>Trạng thái: </h3><h4 class="status-banned">Bạn đã bị cấm khỏi trò chơi</h4>';
                            }


                            ?>
                        </div>
                        


                    </div>
                    <?php if (!empty($errorMsg)): ?>
                        <div class="text-center status-banned"><?= $errorMsg; ?></div>
                    <?php endif; ?>
                    <?php if (!empty($succesMsg)): ?>
                        <div class="text-center status-good"><?= $succesMsg; ?></div>
                    <?php endif; ?>
                    <form action="/info" method="POST">
                        <div class="text-center">
                            <h3 for="old-password">Mật khẩu cũ:</h3>
                            <input type="password" id="old-password" name="old-password" required>
                        </div>
                        <div class="text-center">
                            <h3 for="new-password">Mật khẩu mới:</h3>
                            <input type="password" id="new-password" name="new-password" required>
                        </div>
                        <div class="text-center">
                            <h3 for="confirm-password">Xác nhận mật khẩu mới:</h3>
                            <input type="password" id="confirm-password" name="confirm-password" required>
                        </div>
                        <div class="text-center">
                            <button class="btn-download-popup" type="submit">
                                <i class="fa fa-download"></i>
                                Đổi Mật Khẩu
                            </button>
                        </div>
                    </form>
                </center>
            </div>

        </div>
    </section>







    <?php include "../DHKD/Footer.php"?>
</div>
<div class="floating floating-left" style="z-index: 9999;">
    <div id="float_top" class="float-scale float_top scaleMobile scaleDesktop" data-block-id="float_top">
        <div class="float__content pc">
            <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/float_top/images/bg.png" alt="" class="bg-img">
            <a href="/" class="metalslug-icon">ICON<img src="<?= $_Logo ?>" style="display: block; margin-left: auto; margin-right: auto; max-width: 200px;"></a>
            <nav class="navigation">


                <ul class="list-container">
                    <li class="list-item">
                        <a class="menu-item scrollFrameControl" title="TRANG CHỦ" onclick="window.location.href='/'">
                            TRANG CHỦ
                        </a>
                    </li>
                    <li class="list-item">
                        <a onclick="window.location.href='/#block2'" class="menu-item scrollFrameControl" title="BẢNG XẾP HẠNG">
                            BẢNG XẾP HẠNG
                        </a>
                    </li>
                    <li class="list-item">
                        <a onclick="window.location.href='/#block3'" class="menu-item scrollFrameControl" title="DIỄN ĐÀN">
                            DIỄN ĐÀN
                        </a>
                    </li>
                    <li class="list-item">
                        <a onclick="window.location.href='/nap-the'" class="menu-item scrollFrameControl" title="NẠP THẺ">
                            NẠP THẺ
                        </a>
                    </li>
                    <!-- <li class="list-item">
                        <a onclick="window.location.href='/#block5'" class="menu-item scrollFrameControl" title="MINIGAME">
                            MINIGAME
                        </a>
                    </li> -->

                    <li class="list-item"><a href="<?= $_Zalo ?>" class="menu-item">ZALO</a></li>
                </ul>


            </nav>
            <style>
                .language {
                    background-image: url('/Assets/images/dangnhap.png');
                    background-size: cover;
                    /* or 'contain' depending on your requirement */
                    background-repeat: no-repeat;
                    background-position: center;
                    width: 70px;
                    /* Set the width of the element */
                    height: 70px;
                    /* Set the height of the element */
                }
            </style>
            <div class="language">
                <span class="text__title mobile" style="display: none;">Tài Khoản</span>
                <input class="currentInput" type="checkbox" name="" data-region="vn">
                <label for="language" class="floattop__item floattop__item--language current currentLabel" style="display: none;">Vietnam</label>
                <div class="language__list choose-language"><input type="hidden" id="input-region" class="input-region" value="vn">
                    <?php if ($_Login === null) { ?>
                        <ul class="dropdown-content">
                            <li class="language__item"><a class="region" href="/dang-nhap">ĐĂNG NHẬP</a></li>
                            <li class="language__item"><a class="region" href="/dang-ky">ĐĂNG KÝ</a></li>
                        </ul>
                    <?php } else { ?>
                        <ul class="dropdown-content">
                            <li class="language__item"><a class="region" href="/info">Tài Khoản</a></li>
                            <li class="language__item"><a class="region" href="/nap-the">Nạp Tiền</a></li>
                            <!-- <li class="language__item"><a class="region" href="#">Minigame</a></li> -->
                            <li class="language__item"><a class="region" href="/dang-xuat">Đăng Xuất</a></li>
                        </ul>
                    <?php } ?>
                </div>
            </div>






        </div>
        <div class="float__content mb">
            <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/float_top/images/bg-mb.png" alt="" class="bg-img">
            <a href="/" class="game-icon">GAME ICON</a>
            <div class="btn-container">
                <a href="/nap-the" target="_blank" class="btn btn-topup"><span class="coins-icon"></span><span class="text">NẠP THẺ</span></a>
                <a href="redeem.html" target="_blank" class="btn btn-code"><span class="present-icon"></span><span class="text">NHẬP<br />CODE</span></a>
            </div>
        </div>
    </div>
</div>
<!-- <div class="floating floating-right">
    <div id="float_right" class="float-scale float_right scaleMobile scaleDesktop" data-block-id="float_right">
        <div class="float__content"><img src="./Assets/ThangHoa/qr-zalo.png" width="140" alt="" class="qr-code" />
            <div class="link-container-1"><a href="<?= $_Iphone ?>" onclick="dataLayer.push({'event':'DownloadIosVN'})" target="_blank" class="link link-ios" rel="noopener">IOS</a> <a href="<?= $_Android ?>" onclick="dataLayer.push({'event':'DownloadGGPlayVN'})" target="_blank" class="link link-android" rel="noopener">ANDROID</a> <a href="<?= $_Windows ?>" onclick="dataLayer.push({'event':'DownloadApkVN'})" target="_blank" class="link link-apk" rel="noopener">APK</a></div>
            <div class="link-container-2"><a href="/nap-the" onclick="dataLayer.push({'event':'TopupVN'})" target="_blank" class="link link-topup" rel="noopener">TOPUP</a> </div>
            <div class="link-container-3"><a href="<?= $_Fanpage ?>" target="_blank" class="link link-facebook" rel="noopener">FACEBOOK</a> <a href="<?= $_Group ?>" target="_blank" class="link link-group" rel="noopener">GROUP</a> <a href="<?= $_Tiktok ?>" target="_blank" class="link link-tiktok" rel="noopener">TIKTOK</a></div>
        </div>
    </div>
</div> -->
<div class="floating floating-bottom">
    <div id="float_bottom" class="float-scale float_bottom scaleMobile scaleDesktop mb" data-block-id="float_bottom">
        <div class="float__content"><img src="/img.game/products/metalslug/2023-global-mainsite/dist/assets/float_bottom/images/bg.png" alt="" class="bg-img"><button class="btn-menu"></button>
            <nav class="navigation">
                <ul class="list-container">
                    <li class="list-item">
                        <a href="//metalslugawk.vnggames.com/vn/index.html#block1" class="menu-item" title="TRANG CHỦ">
                            TRANG CHỦ
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="//metalslugawk.vnggames.com/vn/index.html#block2" class="menu-item" title="VỀ METAL SLUG">
                            VỀ METAL SLUG
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="//metalslugawk.vnggames.com/vn/index.html#block3" class="menu-item" title="TIN TỨC">
                            TIN TỨC
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="//metalslugawk.vnggames.com/vn/index.html#block4" class="menu-item" title="THÔNG TIN">
                            THÔNG TIN
                        </a>
                    </li>
                    <!-- <li class="list-item">
                        <a href="//metalslugawk.vnggames.com/vn/index.html#block5" class="menu-item" title="TÍNH NĂNG">
                            TÍNH NĂNG
                        </a>
                    </li>

                    <li class="list-item"><a href="//metalslugawk.vnggames.com/faqs/faqs.1.html" class="menu-item">FAQS</a></li>
                    <li class="list-item"><a href="https://metalslugawk.vnggames.net/vn/userterms.html" target="_blank" class="menu-item">DKSD</a></li> -->
                </ul>
            </nav>

            <div class="language">
                <span class="text__title mobile" style="display: none;">Language</span>
                <input class="currentInput" type="checkbox" name="" data-region="vn">
                <label for="language" class="floattop__item floattop__item--language current currentLabel" style="display: none;">Vietnam</label>
                <div class="language__list choose-language"><input type="hidden" id="input-region" class="input-region" value="vn">
                    <ul class="dropdown-content">
                        <li class="language__item" data-language="th"><a class="region" href="#" data-region="th">TH</a></li>
                        <li class="language__item" data-language="tw"><a class="region" href="#" data-region="tw">TWHK</a></li>
                        <li class="language__item" data-language="ph"><a class="region" href="#" data-region="ph">PH</a></li>
                        <li class="language__item" data-language="id"><a class="region" href="#" data-region="id">ID</a></li>
                        <li class="language__item" data-language="sg"><a class="region" href="#" data-region="sg">SGMY</a></li>
                        <li class="language__item" data-language="vn"><a class="region" href="#" data-region="vn">VN</a></li>
                    </ul>
                </div>
            </div>




        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/img.game/products/metalslug/2023-global-mainsite/dist/DHKD.js"></script>
<script src="/img.game/products/metalslug/2023-global-mainsite/language.js"></script>
</body>

</html>