<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';


$thongbao = isset($thongbao) ? $thongbao : '';



if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}
?>
<div class="form-card">
    <div class="form-group">
        <label>Thông tin nhân vật</label>
        <div class="form-block">
            <div class="form-block-info"><?= $_ServerName ?> - <?= maskUsername($_SESSION['usernameshow']) ?></div>
            <div class="form-block-action">
                <button type="button" class="previous btn-change" onclick="back_step_previous(1)">Change package</button>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label>Mệnh giá</label>
        <div class="form-block">
            <div class="form-block-info"><?= $_ServerName ?> - <?= $_SESSION['pricenap'] ?></div>
            <div class="form-block-action">
                <button type="button" class="previous btn-change" onclick="back_step_previous(2)">Change package</button>
            </div>
        </div>
    </div>
    <!-- New div for displaying the message -->
    <div id="role-message1" class="alert alert-info d-none"></div>
    <form method="POST">
<?php if($_POST["method"] == "card"){?>
        <label>Loại Thẻ&nbsp;</label>
        <select class="form-control form-control-alternative" name="telco" required>
            <option>Chọn loại thẻ</option>
            <option value="VIETTEL">Viettel</option>
            <option value="VINAPHONE">Vinaphone</option>
            <option value="MOBIFONE">Mobifone</option>
            <option value="VNMOBI">Vietnamobile</option>
            <option value="ZING">Zing</option>
            <option value="GATE">Gate</option>
            <option value="GARENA">Garena</option>
            <option value="VCOIN">Vcoin (VTC)</option>
            <option value="ZINGCHAM">Zing (Chậm)</option>
        </select>
        <label>Nhập Seri&nbsp;</label>
        <input type="text" class="form-control" name="serial" required />
        <label>Nhập Mã Thẻ&nbsp;</label>
        <input type="text" class="form-control" name="code" required />

        <br>
        <?php } ?>

        <button id="submitBtn" type="submit" class="btn btn-primary action-button w100" name="submit">
            <span id="btnText">NẠP NGAY</span>
            <span id="loadingIcon" class="d-none"><i class="fa fa-spinner fa-spin"></i> Đang xử lý...</span>
        </button>
    </form>

    <script>
        $(document).ready(function() {
            // Ngăn chặn hành vi mặc định của form khi nhấn nút submit
            $("form").submit(function(event) {
                event.preventDefault(); // Ngăn chặn hành vi mặc định của form khi nhấn nút submit
                var submitBtn = $("#submitBtn");
                submitBtn.prop("disabled", true); // Ngăn chặn nhấp nút lần nữa khi đã nhấn

                // Hiển thị icon loading và ẩn nội dung
                $("#btnText").addClass("d-none");
                $("#loadingIcon").removeClass("d-none");

                // Gọi hàm xử lý AJAX để gửi yêu cầu nạp thẻ
                submitForm();
            });

            function submitForm() {
                var telco = $("select[name='telco']").val();
                var serial = $("input[name='serial']").val();
                var code = $("input[name='code']").val();
                var amount = "<?php echo formatPrice($_SESSION['pricenap']); ?>";
                var username = "<?php echo $_SESSION['usernameshow']; ?>"; // Thêm biến username

                // Kiểm tra và xử lý các dữ liệu
                if (telco === '' || serial === '' || code === '') {
                    alert('Vui lòng nhập đầy đủ thông tin.');
                    // Đặt lại nút sau khi hoàn thành
                    resetButton();
                    return;
                }

                <?php if($_POST["method"] == "card"){?>

                // Gửi yêu cầu AJAX
               $.ajax({
                    type: "POST",
                    url: "/ajax/users/recharge.php", // Địa chỉ API để gửi yêu cầu
                    data: JSON.stringify({
                        telco: telco,
                        serial: serial,
                        code: code,
                        amount: amount,
                        username: username // Truyền thêm username vào yêu cầu
                    }),
                    contentType: "application/json",
                    success: function(response) {
                        // Ẩn đi thông báo cũ nếu có
                        $('#role-message1').addClass('d-none');

                        // Xử lý kết quả trả về từ server
                        if (response.status) {
                            $('#role-message1').removeClass('alert-danger').addClass('alert-success').text('Gửi thẻ thành công. Đợi duyệt.').removeClass('d-none');
                            // Cập nhật thông tin nếu cần thiết
                            // Đặt lại nút sau khi hoàn thành
                            resetButton();
                        } else {
                            $('#role-message1').removeClass('alert-success').addClass('alert-danger').text('Lỗi: ' + response.message).removeClass('d-none');
                            // Đặt lại nút sau khi hoàn thành
                            resetButton();
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#role-message1').removeClass('alert-success').addClass('alert-danger').text('Lỗi không thể gửi yêu cầu. Vui lòng thử lại sau.').removeClass('d-none');
                        console.error(xhr.responseText);
                        // Đặt lại nút sau khi hoàn thành
                        resetButton();
                    }
                });
                <?php } else { ?>
                    $.ajax({
                        type: "POST",
                        url: "/ajax/users/atm.php", // Địa chỉ API để gửi yêu cầu
                        data: {
                            amount: amount
                        },
                        success: function(response) {
                            // response = JSON.parse(response);
                            if (response.status) {
                                // Chuyển hướng đến trang thanh toán
                                // toastr.success("Đang chuyển hướng đến trang thanh toán...", 'Thông báo', {
                                //     timeOut: 5000
                                // });
                                setTimeout(function() {
                                    window.location.href = response.data.payUrl;
                                }, 3000);
                            } else {
                                alert(response.message);
                                // toastr.error(response.message, 'Thông báo', {
                                //     timeOut: 5000
                                // });
                            }
                        },
                        error: function() {
                            // toastr.error('Đã có lỗi xảy ra, vui lòng thử lại!', 'Thông báo', {
                            //     timeOut: 5000
                            // });
                        }
                    });
                <?php } ?>
            }
            // Hàm đặt lại nút sau khi hoàn thành
            function resetButton() {
                $("#btnText").removeClass("d-none"); // Hiển thị lại văn bản ban đầu của nút
                $("#loadingIcon").addClass("d-none"); // Ẩn icon loading
                $("#submitBtn").prop("disabled", false); // Cho phép nhấn lại nút submit
            }
        });
    </script>