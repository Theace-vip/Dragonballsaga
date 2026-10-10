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
                        <a href="/" title="<?php echo htmlspecialchars($brandName); ?>" style="display:block;text-align:center;">
                            <span class="brand-logo"><?php echo htmlspecialchars($brandName); ?></span>
                            <div class="brand-slogan"><?php echo htmlspecialchars($brandSlogan); ?></div>
                        </a>
                    </div>
                    <div class="col text-center">
                        <?php if (!empty($java)): ?>
                        <a href="<?php echo htmlspecialchars($java); ?>" target="_blank" class="btn btn-download text-white" style="border-radius: 10px; width: 100px;">
                            <i class="fa fa-download"></i> JAVA
                        </a>
                        <?php endif; ?>
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
                        <a class="btn btn-action m-1 text-white" href="../dua-top" style="border-radius: 10px;"><i
                                class="fa fa-bar-chart"></i> Đua Top</a>
                        <a class="btn btn-action m-1 text-white" href="../chucnang" style="border-radius: 10px;"><i
                                class="fa fa-info-circle"></i> Giới Thiệu</a>
                        <a href="<?php echo htmlspecialchars($boxzalo); ?>" class="btn btn-action m-1 text-white"
                            style="border-radius: 10px;"> <i class="fa fa-exclamation-triangle"></i> Báo Lỗi</a>
                    <?php else: ?>

                        <a href="../change-password" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                class="fa fa-key"></i> Đổi mật khẩu</a>
                        <a href="../dua-top" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                class="fa fa-bar-chart"></i> Đua Top</a>
                        <a href="../chucnang" class="btn btn-action m-1 text-white" style="border-radius: 10px;"><i
                                class="fa fa-info-circle"></i> Giới Thiệu</a>
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
                                    <span class="brand-logo sm"><?php echo htmlspecialchars($brandName); ?></span>
                                </div>
                                <div class="modal-body">
                                    <p style="padding: 10px">
                                        <b style="color:red">THÔNG BÁO:</b> Đối với phiên bản <b>Cài đặt vào iPhone</b>
                                        có thể cài trực tiếp qua Testflight và AppCenter<br>
                                        <a class="btn btn-warning mb-2" style="border-radius: 10px;"
                                            href="/huongdan">Testflight</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="<?php echo htmlspecialchars($adr); ?>">Tải APK
                                            Android</a>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;" href="/huongdan">Hướng
                                            Dẫn</a>
                                        <br>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;" href="/lenhios">Lệnh
                                            Chat</a>
                                        <br>
                                        <b>Hướng dẫn cài đặt:</b><br>
                                        <a class="btn btn-dark mb-2" style="border-radius: 10px;"
                                            href="<?php echo htmlspecialchars($ios); ?>">File
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
                                    <span class="brand-logo sm"><?php echo htmlspecialchars($brandName); ?></span>
                                </div>
                                <div class="modal-body">
                                    <p style="padding: 10px">

                                        <b style="color:red">THÔNG BÁO:</b> Phiên Bản khi dùng phiên bản MOD <b>"Nên Cài
                                            Mật Khẩu Cấp 2"</b> .<br>
                                        <a class="btn btn-warning mb-2" style="border-radius: 10px;"
                                            href="<?php echo htmlspecialchars($pc); ?>">Phiên
                                            Bản Gốc</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="<?php echo htmlspecialchars($adr); ?>">Tải
                                            Android</a>
                                        <a class="btn btn-danger mb-2" style="border-radius: 10px;"
                                            href="<?php echo htmlspecialchars($ios); ?>">Tải
                                            iOS</a>
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