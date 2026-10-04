<?php
require_once 'core/set.php';
require_once 'core/connect.php';
require_once 'core/head.php';

if ($_login === null) {
    echo '<script>window.location.href = "../dang-nhap.php";</script>';
    exit;
}
?>

<div class="ant-col ant-col-xs-24 ant-col-sm-24 ant-col-md-24">
    <div class="page-layout-body">
        <div class="ant-row">
            <a href="/" style="color: black" class="ant-col ant-col-24 home_page_bodyTitleList__UdhN_">Quay lại diễn đàn</a>
        </div>
        <div class="ant-col ant-col-24">
            <div class="ant-list ant-list-split">
                <div class="ant-spin-nested-loading">
                    <div class="ant-spin-container">
                        <ul class="ant-list-items">
                            <div id="data_news">
                                <div class="container pt-5 pb-5">
                                    <div class="row">
                                        <div class="col-lg-6 offset-lg-3">
                                            <h4>ĐỔI MẬT KHẨU</h4>
                                            <?php if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                                                $stmt = $conn->prepare("SELECT password FROM account WHERE username=:username");
                                                $stmt->bindParam(":username", $_username);
                                                $stmt->execute();
                                                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                                                $matkhaucu = $row['password'] ?? '';

                                                $matKhauHienTai = $_POST['password'] ?? '';
                                                $matKhauMoi = $_POST['new_password'] ?? '';
                                                $xacNhanMatKhauMoi = $_POST['new_password_confirmation'] ?? '';

                                                if (!empty($matKhauHienTai) && !empty($matKhauMoi) && !empty($xacNhanMatKhauMoi)) {
                                                    if ($matKhauHienTai !== $matkhaucu) {
                                                        echo "<div class='alert alert-danger'>Sai mật khẩu hiện tại</div>";
                                                    } elseif ($matKhauMoi === $matKhauHienTai) {
                                                        echo "<div class='alert alert-danger'>Mật khẩu mới không được giống mật khẩu hiện tại</div>";
                                                    } elseif ($matKhauMoi !== $xacNhanMatKhauMoi) {
                                                        echo "<div class='alert alert-danger'>Mật khẩu mới không giống nhau</div>";
                                                    } else {
                                                        $stmt = $conn->prepare("UPDATE account SET password=:matKhauMoi WHERE username=:username");
                                                        $stmt->bindParam(":matKhauMoi", $matKhauMoi);
                                                        $stmt->bindParam(":username", $_username);

                                                        if ($stmt->execute()) {
                                                            echo "<div class='alert alert-success'>Cập nhật mật khẩu mới thành công</div>";
                                                        } else {
                                                            echo "<div class='alert alert-danger'>Lỗi khi cập nhật mật khẩu mới</div>";
                                                        }
                                                    }
                                                } else {
                                                    echo "<div class='alert alert-danger'>Vui lòng điền đầy đủ thông tin trong form</div>";
                                                }
                                            } ?>

                                            <form method="POST">
                                                <div class="mb-3">
                                                    <label class="font-weight-bold">Mật Khẩu hiện tại:</label>
                                                    <input type="password" class="form-control" name="password" placeholder="Mật khẩu hiện tại" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="font-weight-bold">Mật Khẩu Mới:</label>
                                                    <input type="password" class="form-control" name="new_password" placeholder="Mật khẩu mới" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="font-weight-bold">Xác Nhận Mật Khẩu Mới:</label>
                                                    <input type="password" class="form-control" name="new_password_confirmation" placeholder="Xác nhận mật khẩu mới" required>
                                                </div>
                                                <button class="ant-btn ant-btn-default header-menu-item header-menu-item-active w-50" type="submit">Thực hiện</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="paging" class="d-flex justify-content-end align-items-center flex-wrap"></div>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<?php include_once 'core/footer.php'; ?>
