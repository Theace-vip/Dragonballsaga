<?php
// ====== Cấu hình database ======
$db_host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "dragonballsaga";

$conn = new mysqli($db_host, $db_user, $db_pass, $db_name);
$conn->set_charset("utf8mb4");

$message = "";
$gm_code = "huyensumo9989"; // Mã GM

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $old_name = trim($_POST['old_name']);
    $new_name = trim($_POST['new_name']);

    if (empty($new_name)) {
        $message = "⚠ Vui lòng nhập tên mới!";
    } else {
        $check_stmt = $conn->prepare("
            SELECT a.id, a.vnd 
            FROM player p
            JOIN account a ON p.account_id = a.id
            WHERE p.name = ?
        ");
        $check_stmt->bind_param("s", $old_name);
        $check_stmt->execute();
        $res = $check_stmt->get_result();

        if ($res->num_rows == 0) {
            $message = "❌ Không tìm thấy nhân vật!";
        } else {
            $row = $res->fetch_assoc();
            $account_id = intval($row['id']);
            $vnd = intval($row['vnd']);

            if ($vnd < 10000) {
                $message = "⚠ Không đủ 10,000 VND để đổi tên!";
            } else {
                $update_name_stmt = $conn->prepare("UPDATE player SET name = ? WHERE name = ?");
                $update_name_stmt->bind_param("ss", $new_name, $old_name);
                $update_name_stmt->execute();

                if ($update_name_stmt->affected_rows > 0) {
                    $update_vnd_stmt = $conn->prepare("UPDATE account SET vnd = vnd - 10000 WHERE id = ?");
                    $update_vnd_stmt->bind_param("i", $account_id);
                    $update_vnd_stmt->execute();
                    $message = "✅ Đổi tên {$old_name} thành {$new_name} thành công! (-10,000 VND)";
                } else {
                    $message = "❌ Không thể đổi tên, có thể tên mới đã tồn tại!";
                }
            }
        }
    }
}
?>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GM Đổi Tên</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h2>🔄 GM Đổi Tên ✔️</h2>
	<h2>🔄 (Lưu Y Thoát Game Trước Khi Đổi Tên) ✔️</h2>
	<h2>🔄 (Có Thể Tên Ký Tự Đặc Biết Được) ✔️</h2>
    <?php if ($message): ?>
        <div class="msg <?php echo (strpos($message, '✅') !== false) ? 'success' : 'error'; ?>">
            <?php echo $message; ?>
        </div>
    <?php endif; ?>
    <form method="POST">
        <input type="text" name="old_name" placeholder="Tên cũ" required>
        <input type="text" name="new_name" placeholder="Tên mới" required>
        <button type="submit">Đổi Tên (-10,000 VND)</button>
    </form>
</div>
</body>
</html>
