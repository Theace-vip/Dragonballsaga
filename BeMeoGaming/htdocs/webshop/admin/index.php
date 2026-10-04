<?php
include '../config.php';

// Kiểm tra đăng nhập
if (!isset($_SESSION['admin_logged_in'])) {
    header("Location: login.php");
    exit();
}

// Lấy danh sách sản phẩm
$sql = "SELECT * FROM web_shop ORDER BY created_at DESC";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Quản lý sản phẩm</h2>
    <a href="add_product.php">➕ Thêm sản phẩm</a>
    <table border="1" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Tên sản phẩm</th>
            <th>Số lượng</th>
            <th>Đã bán</th>
            <th>Giá</th>
            <th>Ảnh</th>
            <th>Mô tả</th>
        </tr>
        <?php while($row = $result->fetch_assoc()): ?>
        <tr>
            <td><?php echo $row['id']; ?></td>
            <td><?php echo $row['item_name']; ?></td>
            <td><?php echo $row['quantity']; ?></td>
            <td><?php echo $row['sold_quantity']; ?></td>
            <td><?php echo number_format($row['price']); ?>đ</td>
            <td><img src="../icon/<?php echo $row['item_img']; ?>" width="50"></td>
            <td><?php echo $row['description']; ?></td>
        </tr>
        <?php endwhile; ?>
    </table>
</body>
</html>
