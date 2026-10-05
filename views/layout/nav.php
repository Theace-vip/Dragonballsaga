<style>
    #snow {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        pointer-events: none;
        z-index: -70;
    }
</style>
<div id="snow"><canvas class="particles-js-canvas-el" width="1125" height="901" style="width: 100%; height: 100%;"></canvas></div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        var script = document.createElement('script');
        script.src = 'https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js';
        script.onload = function() {
            particlesJS("snow", {
                "particles": {
                    "number": {
                        "value": 75,
                        "density": {
                            "enable": true,
                            "value_area": 400
                        }
                    },
                    "color": {
                        "value": "#FFCC33"
                    },
                    "opacity": {
                        "value": 1,
                        "random": true,
                        "anim": {
                            "enable": false
                        }
                    },
                    "size": {
                        "value": 3,
                        "random": true,
                        "anim": {
                            "enable": true
                        }
                    },
                    "line_linked": {
                        "enable": true
                    },
                    "move": {
                        "enable": true,
                        "speed": 1,
                        "direction": "top",
                        "random": true,
                        "straight": false,
                        "out_mode": "out",
                        "bounce": false,
                        "attract": {
                            "enable": true,
                            "rotateX": 300,
                            "rotateY": 1200
                        }
                    }
                },
                "interactivity": {
                    "events": {
                        "onhover": {
                            "enable": false
                        },
                        "onclick": {
                            "enable": false
                        },
                        "resize": false
                    }
                },
                "retina_detect": true
            });
        }
        document.head.append(script);
    });
</script>
<div class="container" style="background: #007E70; padding-bottom: 5px;border-radius: 15px;">

    <div class="row">
        <div class="col-lg-12">
            <div class="wrapper wrapper-content animated fadeInRight">
                <div class="ibox-content m-b-sm" style="/*background: #ff5601;*/margin-top: 5px;">
                    <div style="line-height: 15px; font-size: 12px; padding: 2px 5px 8px 0;" class="text-center">
                        <img height="12" src="../assets/images/12.png" style=" vertical-align: middle; margin-right: 4px; margin-top: 5px;">
                        <span style="vertical-align: middle; color: black;">Dành cho người chơi trên 12 tuổi. Chơi quá 180 phút mỗi ngày sẽ có hại sức khỏe.</span>
                    </div>
                    <div class="p-xs mb-3">
                        <a href="/">
                            <img src="<?php echo htmlspecialchars($logo); ?>"
                                style="display: block;margin-left: auto;margin-right: auto;max-width: 300px;">
                        </a>
                    </div>
                    <div class="col text-center">
                        <a href="<?php echo htmlspecialchars($java); ?>" target="_blank" class="btn btn-download text-white" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-download"></i> JAVA
                        </a>
                        <a href="<?php echo htmlspecialchars($pc); ?>" target="_blank" class="btn btn-download text-white m-1" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-windows"></i> PC
                        </a>
                        <a href="<?php echo htmlspecialchars($adr); ?>" target="_blank" class="btn btn-download text-white" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-android"></i> APK
                        </a>
                        <a href="<?php echo htmlspecialchars($ios); ?>" target="_blank" class="btn btn-download text-white" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-apple"></i> IOS
                        </a>
                        <a href="<?php echo htmlspecialchars($zalo); ?>" target="_blank" class="btn btn-download text-white" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-group"></i> ZALO
                        </a>
                    </div>
                    <!-- Dòng hướng dẫn bên dưới -->
                    <div style="line-height: 15px;font-size: 12px;padding-right: 5px;margin-bottom: 8px;padding-top: 2px;" class="text-center">
                        <span class="text-black" style="vertical-align: middle;line-height:36px;color:black;font-size:12px;">
                            Tải phiên bản phù hợp để có trải nghiệm tốt.
                        </span>
                    </div>

                </div>
                <div class="text-center" style="padding-top: 50px; padding-bottom: 50px;">
                    <!--Login-->
                    <?php if (!isset($_SESSION['account']) || empty($_SESSION['account'])): ?>

                        <a class="btn btn-action m-1 text-white" href="../login" style="border-radius: 10px;"><i
                                class="fa fa-sign-in"></i> Đăng Nhập</a>
                        <a class="btn btn-action m-1 text-white" href="../register" style="border-radius: 10px;"><i
                                class="fa fa-user-plus"></i> Đăng Ký</a>
                        <a class="btn btn-action m-1 text-white" href="../power" style="border-radius: 10px;"><i
                                class="fa fa-bar-chart"></i> Xếp Hạng</a>
                        <a href="<?php echo htmlspecialchars($boxzalo); ?>" class="btn btn-action m-1 text-white"
                            style="border-radius: 10px;"> <i class="fa fa-exclamation-triangle"></i> Báo Lỗi</a>
                    <?php else: ?>

                        <a href="../change-password" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                class="fa fa-key"></i> Đổi mật khẩu</a>
                        <a href="../naptien.php" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                class="fa fa-money"></i> Nạp tiền</a>
                        <?php if (isset($_SESSION['is_admin']) && $_SESSION['is_admin'] == 1): ?>
                            <a href="../change-password" class="btn btn-action m-1 text-white" style="border-radius: 10px;">
                                <i class="fa fa-plus-circle"></i> Cộng tiền</a>
                            <a href="../naptien.php" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                    class="fa fa-money"></i> Buff Bẩn</a>
                        <?php endif; ?>
                        <a href="../logout"><button class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                    class="fa fa-sign-out"></i> Đăng xuất</button></a>
                    <?php endif; ?>


                    <div class="modal fade" id="download_iphone" tabindex="-1" role="dialog"
                        aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-side modal-bottom-right ">
                            <div class="modal-content">
                                <div class="modal-header"
                                    style="background-color: #2c2c2c; color: #FFF; text-align: center;">
                                    <img src="https://imgur.com/XnwPrtD.png" style="display: block;
                                                                              margin-left: auto;
                                                                              margin-right: auto;
                                                                              max-width: 250px;">
                                </div>
                                <div class="modal-body">
                                    <p style="padding: 10px">
                                        <b style="color:red">THÔNG BÁO:</b> Đối với phiên bản <b>Cài đặt vào iPhone</b>
                                        có thể cài trực tiếp qua Testflight và AppCenter<br>
                                        <a class="btn btn-warning mb-2" style="border-radius: 10px;"
                                            href="/huongdan">Testflight</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="https://install.appcenter.ms/users/nrorose/apps/nro-rose-232/distribution_groups/rose">AppCenter
                                            231</a>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;" href="/huongdan">Hướng
                                            Dẫn</a>
                                        <br>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;" href="/lenhios">Lệnh
                                            Chat</a>
                                        <br>
                                        <b>Hướng dẫn cài đặt:</b><br>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;"
                                            href="https://www.mediafire.com/file/ojdsl69xln2zmpk/DragonTeam.ipa/file">File
                                            IPA</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="https://www.youtube.com/watch?v=QwnjV3Xu_sg">Cài bằng Scarlet</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="https://www.youtube.com/watch?v=kcl9unWfw3A">Cài bằng PC</a>
                                        <br>
                                        <small>
                                            Vui lòng đọc kỷ hướng dẫn bên dưới trước khi tải (truy cập cài đặt)<br>
                                            <b>Cài đặt chung -&gt; Quản lý thiết bị -&gt; "Tin cậy"</b>
                                        </small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="modal fade" id="download_pc" tabindex="-1" role="dialog"
                        aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-side modal-bottom-right ">
                            <div class="modal-content">
                                <div class="modal-header"
                                    style="background-color: #2c2c2c; color: #FFF; text-align: center;">
                                    <img src="https://imgur.com/XnwPrtD.png" style="display: block;
                                                                              margin-left: auto;
                                                                              margin-right: auto;
                                                                              max-width: 250px;">
                                </div>
                                <div class="modal-body">
                                    <p style="padding: 10px">

                                        <b style="color:red">THÔNG BÁO:</b> Phiên Bản khi dùng phiên bản MOD <b>"Nên Cài
                                            Mật Khẩu Cấp 2"</b> .<br>
                                        <a class="btn btn-warning mb-2" style="border-radius: 10px;"
                                            href="https://www.mediafire.com/file/6ect76ykxwmb3pt/NRO_ROSE_2.2.2.rar/file">Phiên
                                            Bản Gốc</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="https://www.mediafire.com/file/7pnb5yy9nny5nch/NgocRongRose231.zip/file">MOD
                                            2.3.1</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="https://www.mediafire.com/file/cgw36hcjg60nz8n/DragonRose_Mod_225.zip/file">MOD
                                            2.2.5</a>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;" href="/lenhios">Lệnh
                                            Chat</a>
                                        <br>

                                        <small>
                                            Nếu game bị xoay tròn vui lòng xóa dữ liệu .<br>

                                        </small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>