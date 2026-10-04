<?php
// ====== Cấu hình database ======
$db_host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "hondaodragon";

$conn = new mysqli($db_host, $db_user, $db_pass, $db_name);
$conn->set_charset("utf8mb4");

$message = "";
$gm_code = "huyensumo9989"; // Mã GM

// Lấy danh sách item
$item_list = [];
$item_sql = "SELECT id, name FROM item_template ORDER BY id ASC";
$item_res = $conn->query($item_sql);
while ($row = $item_res->fetch_assoc()) {
    $item_list[] = $row;
}

// Xử lý Buff
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $char_name = trim($_POST['char_name']);
    $item_id   = intval($_POST['item_id']);
    $quantity  = intval($_POST['quantity']);
    $input_gm_code = trim($_POST['gm_code']);

    if ($input_gm_code !== $gm_code) {
        $message = "❌ Sai mã GM!";
    } else {
        $option = "[\\\"[30,1]\\\"]"; 
        $expire = 1755174142410;

        $check_stmt = $conn->prepare("SELECT items_bag FROM player WHERE name = ?");
        $check_stmt->bind_param("s", $char_name);
        $check_stmt->execute();
        $res = $check_stmt->get_result();

        if ($res->num_rows == 0) {
            $message = "❌ Không tìm thấy nhân vật!";
        } else {
            $row = $res->fetch_assoc();
            $items_bag = json_decode($row['items_bag'], true);

            if (!is_array($items_bag)) {
                $message = "❌ Lỗi đọc dữ liệu items_bag!";
            } else {
                $buffed = false;
                for ($i = 0; $i < count($items_bag); $i++) {
                    if (strpos($items_bag[$i], '[-1,0,"[]') !== false) {
                        $items_bag[$i] = "[".$item_id.",".$quantity.",\"".$option."\",".$expire."]";
                        $buffed = true;
                        break;
                    }
                }

                if ($buffed) {
                    $new_bag = json_encode($items_bag, JSON_UNESCAPED_SLASHES);
                    $update_stmt = $conn->prepare("UPDATE player SET items_bag = ? WHERE name = ?");
                    $update_stmt->bind_param("ss", $new_bag, $char_name);
                    $update_stmt->execute();
                    $message = "✅ Buff thành công item ID {$item_id} số lượng {$quantity} cho {$char_name}!";
                } else {
                    $message = "⚠ Túi đồ đã đầy!";
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
<title>GM Buff Item</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h2>🆓 GM Buff Item ✔️</h2>
    <?php if ($message): ?>
        <div class="msg <?php echo (strpos($message, '✅') !== false) ? 'success' : 'error'; ?>">
            <?php echo $message; ?>
        </div>
    <?php endif; ?>
    <form method="POST">
        <input type="text" name="char_name" placeholder="Tên nhân vật" required>
        <select name="item_id" required>
            <option value="">-- Chọn Item --</option>
            <?php foreach ($item_list as $item): ?>
                <option value="<?= htmlspecialchars($item['id']) ?>">
                    <?= htmlspecialchars($item['id']) ?> - <?= htmlspecialchars($item['name']) ?>
                </option>
            <?php endforeach; ?>
        </select>
        <input type="number" name="quantity" placeholder="Số lượng" required>
        <input type="password" name="gm_code" placeholder="Nhập mã GM" required>
        <button type="submit">Buff</button>
    </form>
</div>
</body>
</html>
