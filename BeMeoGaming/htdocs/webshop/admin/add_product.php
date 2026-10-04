<?php
include '../config.php';

// Kiểm tra đăng nhập
if (!isset($_SESSION['admin_logged_in'])) {
    header("Location: login.php");
    exit();
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $item_name = $conn->real_escape_string($_POST['item_name']);
    $quantity = intval($_POST['quantity']);
    $template_id = intval($_POST['template_id']);
    $price = intval($_POST['price']);
    $description = $conn->real_escape_string($_POST['description']);

    // Upload ảnh nếu có
    $item_img = null;
if (!empty($_FILES['item_img']['name'])) {
    $upload_dir = "../icon/";
    if (!file_exists($upload_dir)) {
        mkdir($upload_dir, 0777, true);
    }

    // Lấy tên file gốc
    $original_name = $_FILES['item_img']['name'];
    $extension = pathinfo($original_name, PATHINFO_EXTENSION);
    $filename = pathinfo($original_name, PATHINFO_FILENAME);

    // Lọc ký tự nguy hiểm khỏi tên file
    $safe_name = preg_replace('/[^a-zA-Z0-9_-]/', '', $filename);
    $final_name = $safe_name; // giữ nguyên tên không thêm time

    $target_path = $upload_dir . $final_name . "." . $extension;

    if (move_uploaded_file($_FILES['item_img']['tmp_name'], $target_path)) {
        $item_img = $final_name;
    }
}


    // Thêm sản phẩm vào database
    $sql = "INSERT INTO web_shop (item_name, quantity, template_id, price, description, item_img)
            VALUES ('$item_name', '$quantity', '$template_id', '$price', '$description', '$item_img')";

    if ($conn->query($sql) === TRUE) {
        echo "✅ Sản phẩm đã được thêm!";
        header("Location: index.php");
        exit();
    } else {
        echo "❌ Lỗi: " . $conn->error;
    }
}
?>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thêm sản phẩm</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Thêm sản phẩm mới</h2>
    <form method="POST" enctype="multipart/form-data">
        <input type="text" name="item_name" placeholder="Tên sản phẩm" required><br><br>
        <input type="number" name="quantity" placeholder="Số lượng" required><br><br>
        <input type="number" name="template_id" placeholder="Template ID" required><br><br>
        <input type="number" name="price" placeholder="Giá" required><br><br>
        <textarea name="description" placeholder="Mô tả sản phẩm"></textarea><br><br>
        <input type="file" name="item_img"><br><br>
        <button type="submit">Thêm sản phẩm</button>
    </form>
</body>
</html>
