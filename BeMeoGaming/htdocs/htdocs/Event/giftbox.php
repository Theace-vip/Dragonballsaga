<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

$conn = new mysqli("localhost", "root", "", "hondaodragon");
$conn->set_charset("utf8mb4");

$username = $_SESSION['username'];
$message = "";

// Khi bấm nhận quà
if (isset($_POST['claim_all'])) {
    $option = "[\\\"[30,1]\\\"]";
    $expire = 1755174142410;

    // Lấy quà chưa nhận
    $giftQuery = $conn->prepare("SELECT id, item_id, quantity FROM gifts WHERE username = ? AND status = 0");
    $giftQuery->bind_param("s", $username);
    $giftQuery->execute();
    $giftRes = $giftQuery->get_result();

    if ($giftRes->num_rows > 0) {
        // Lấy túi đồ
        $check_stmt = $conn->prepare("SELECT items_bag FROM player WHERE name = ?");
        $check_stmt->bind_param("s", $username);
        $check_stmt->execute();
        $res = $check_stmt->get_result();
        $row = $res->fetch_assoc();
        $items_bag = json_decode($row['items_bag'], true);

        while ($gift = $giftRes->fetch_assoc()) {
            $added = false;
            for ($i = 0; $i < count($items_bag); $i++) {
                if (strpos($items_bag[$i], '[-1,0,"[]') !== false) {
                    $items_bag[$i] = "[".$gift['item_id'].",".$gift['quantity'].",\"".$option."\",".$expire."]";
                    $added = true;
                    break;
                }
            }
            if ($added) {
                // Đánh dấu đã nhận
                $updateGift = $conn->prepare("UPDATE gifts SET status = 1 WHERE id = ?");
                $updateGift->bind_param("i", $gift['id']);
                $updateGift->execute();
            }
        }

        // Cập nhật túi đồ
        $new_bag = json_encode($items_bag, JSON_UNESCAPED_SLASHES);
        $update_stmt = $conn->prepare("UPDATE player SET items_bag = ? WHERE name = ?");
        $update_stmt->bind_param("ss", $new_bag, $username);
        $update_stmt->execute();

        $message = "✅ Nhận quà thành công!";
    } else {
        $message = "⚠ Không có quà để nhận!";
    }
}

// Lấy danh sách quà chưa nhận
$listQuery = $conn->prepare("SELECT * FROM gifts WHERE username = ? AND status = 0");
$listQuery->bind_param("s", $username);
$listQuery->execute();
$listRes = $listQuery->get_result();
?>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kho Quà VIP</title>
<style>
body { font-family: Arial; background: #f5f5f5; }
.container { width: 500px; margin: auto; background: white; padding: 20px; border-radius: 10px; }
h2 { text-align: center; }
table { width: 100%; border-collapse: collapse; }
table, th, td { border: 1px solid #ddd; padding: 8px; }
th { background: #eee; }
button { background: green; color: white; padding: 10px; border: none; cursor: pointer; }
button:hover { background: darkgreen; }
.msg { padding: 10px; margin-bottom: 10px; border-radius: 5px; }
.success { background: #d4edda; color: #155724; }
.error { background: #f8d7da; color: #721c24; }
</style>
</head>
<body>
<div class="container">
    <h2>🎁 Kho Quà VIP</h2>
    <?php if ($message): ?>
        <div class="msg <?php echo (strpos($message, '✅') !== false) ? 'success' : 'error'; ?>">
            <?php echo $message; ?>
        </div>
    <?php endif; ?>

    <?php if ($listRes->num_rows > 0): ?>
        <form method="POST">
            <table>
                <tr>
                    <th>ID Item</th>
                    <th>Số lượng</th>
                    <th>Ngày nhận</th>
                </tr>
                <?php while ($gift = $listRes->fetch_assoc()): ?>
                <tr>
                    <td><?= $gift['item_id'] ?></td>
                    <td><?= $gift['quantity'] ?></td>
                    <td><?= $gift['created_at'] ?></td>
                </tr>
                <?php endwhile; ?>
            </table>
            <br>
            <button type="submit" name="claim_all">Nhận Tất Cả</button>
        </form>
    <?php else: ?>
        <p>Không có quà nào trong kho.</p>
    <?php endif; ?>
</div>
</body>
</html>
