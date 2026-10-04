<?php
session_start();
include_once('config.php');

// Kiểm tra đăng nhập
if (!isset($_SESSION['username'])) {
    die("Bạn chưa đăng nhập!");
}

$username = $_SESSION['username'];

// Lấy dữ liệu kho web theo username
$sql_kho = "SELECT id, item_name, quantity FROM kho_web WHERE username = ?";
$stmt = $conn->prepare($sql_kho);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();
$items = $result->fetch_all(MYSQLI_ASSOC);
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Kho Web của <?php echo htmlspecialchars($username); ?></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="container mt-5">
    <h3>📦 Kho Web của <?php echo htmlspecialchars($username); ?></h3>
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Tên vật phẩm</th>
                <th>Số lượng</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
        <?php if ($items): ?>
            <?php foreach ($items as $item): ?>
                <tr>
                    <td>🎁 <?php echo htmlspecialchars($item['item_name']); ?></td>
                    <td><?php echo (int)$item['quantity']; ?></td>
                    <td>
                        <button class="btn btn-success btn-nhan" data-id="<?php echo $item['id']; ?>">Nhận</button>
                    </td>
                </tr>
            <?php endforeach; ?>
        <?php else: ?>
            <tr>
                <td colspan="3">Không có vật phẩm nào</td>
            </tr>
        <?php endif; ?>
        </tbody>
    </table>

<!-- Load jQuery trước -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- Script xử lý -->
<script>
$(document).ready(function(){
    console.log("JS loaded"); // Test xem script chạy chưa

    $(".btn-nhan").on("click", function(){
        console.log("Button clicked"); // Test xem click nhận chưa
        var id = $(this).data("id");

        $.ajax({
            url: "nhan.php",
            method: "POST",
            data: { id: id },
            dataType: "json",
            success: function(res) {
                console.log("Response:", res);
                alert(res.message);
                if (res.status === "success") {
                    location.reload();
                }
            },
            error: function(xhr) {
                console.log("AJAX error:", xhr.status, xhr.responseText);
                alert("Có lỗi xảy ra khi gửi yêu cầu!");
            }
        });
    });
});
</script>
</body>
</html>
