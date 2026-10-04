<?php
session_start();
include_once('config.php');

if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
    die("ID sản phẩm không hợp lệ!");
}

$id = intval($_GET['id']);

// Lấy thông tin sản phẩm
$sql = "SELECT * FROM web_shop WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows == 0) {
    die("Sản phẩm không tồn tại!");
}

$product = $result->fetch_assoc();
?>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="utf-8">
<title><?php echo htmlspecialchars($product['item_name']); ?> - Chi tiết sản phẩm</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<style>
body {
    background: #f8f9fa;
}
.container {
    background: #fff;
    padding: 20px;
    border-radius: 12px;
    margin-top: 30px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.product-detail img {
    max-width: 200px;
    display: block;
    margin-bottom: 15px;
}
.price {
    color: #e63946;
    font-size: 1.5rem;
    font-weight: bold;
}
</style>
</head>
<body>
<div class="container product-detail">
    <h1><?php echo htmlspecialchars($product['item_name']); ?></h1>
    <div class="row">
        <div class="col-md-4">
            <img src="icon/<?php echo htmlspecialchars($product['item_img']); ?>.png" alt="<?php echo htmlspecialchars($product['item_name']); ?>">
        </div>
        <div class="col-md-8">
            <p class="price"><?php echo number_format($product['price']); ?> VND</p>
            <p><strong>Tồn kho:</strong> <?php echo (int)$product['quantity']; ?></p>
            <p><strong>Mô tả:</strong></p>
            <p><?php echo !empty($product['description']) ? nl2br(htmlspecialchars($product['description'])) : "Chưa có mô tả cho sản phẩm này."; ?></p>

            <a href="shop.php" class="btn btn-secondary mt-3">⬅ Quay lại cửa hàng</a>
        </div>
    </div>
</div>
</body>
</html>
