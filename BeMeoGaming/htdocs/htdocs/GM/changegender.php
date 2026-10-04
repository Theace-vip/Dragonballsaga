<?php
// ====== Cấu hình database ======
$db_host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "hondaodragon";

$conn = new mysqli($db_host, $db_user, $db_pass, $db_name);
$conn->set_charset("utf8mb4");

$message = "";

// Bảng skill theo hành tinh
$planet_skills = [
    0 => '["[0,1,0,0]","[1,0,0,0]","[6,0,0,0]","[9,0,0,0]","[10,0,0,0]","[20,0,0,0]","[22,0,0,0]","[19,0,0,0]","[24,0,0,0]","[27,0,0,0]","[28,0,0,0]","[29,0,0,0]"]',
    1 => '["[2,1,0,0]","[3,0,0,0]","[7,0,0,0]","[11,0,0,0]","[12,0,0,0]","[17,0,0,0]","[18,0,0,0]","[19,0,0,0]","[26,0,0,0]","[27,0,0,0]","[28,0,0,0]","[29,0,0,0]"]',
    2 => '["[4,1,0]","[5,0,0]","[8,0,0]","[13,0,0]","[14,0,0]","[21,0,0]","[23,0,0]","[19,0,0]","[25,0,0]","[27,0,0]","[28,0,0]","[29,0,0]"]'
];

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $name = trim($_POST['name']);
    $planet = intval($_POST['planet']);

    if (!isset($planet_skills[$planet])) {
        $message = "⚠ Hành tinh không hợp lệ!";
    } else {
        $check_stmt = $conn->prepare("
            SELECT a.id, a.vnd 
            FROM player p
            JOIN account a ON p.account_id = a.id
            WHERE p.name = ?
        ");
        $check_stmt->bind_param("s", $name);
        $check_stmt->execute();
        $res = $check_stmt->get_result();

        if ($res->num_rows == 0) {
            $message = "❌ Không tìm thấy nhân vật!";
        } else {
            $row = $res->fetch_assoc();
            $account_id = intval($row['id']);
            $vnd = intval($row['vnd']);

            if ($vnd < 500000) {
                $message = "⚠ Không đủ 500,000 coin để đổi!";
            } else {
                $skills_json = $planet_skills[$planet];

                $update_stmt = $conn->prepare("
                    UPDATE player 
                    SET gender = ?, skills = ? 
                    WHERE name = ?
                ");
                if (!$update_stmt) {
                    die("Lỗi prepare: " . $conn->error);
                }
                $update_stmt->bind_param("iss", $planet, $skills_json, $name);
                $update_stmt->execute();

                if ($update_stmt->affected_rows > 0) {
                    $update_vnd_stmt = $conn->prepare("UPDATE account SET vnd = vnd - 500000 WHERE id = ?");
                    $update_vnd_stmt->bind_param("i", $account_id);
                    $update_vnd_stmt->execute();
                    $message = "✅ Đổi sang hành tinh {$planet} thành công! (-500,000 coin)";
                } else {
                    $message = "❌ Không thể đổi, có thể nhân vật đã ở hành tinh này!";
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
<title>GM Đổi Hành Tinh</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h2>🌍 GM Đổi Hành Tinh ✔️</h2>
    <?php if ($message): ?>
        <div class="msg <?php echo (strpos($message, '✅') !== false) ? 'success' : 'error'; ?>">
            <?php echo $message; ?>
        </div>
    <?php endif; ?>
    <form method="POST">
        <input type="text" name="name" placeholder="Tên nhân vật" required>
        <select name="planet" required>
            <option value="">-- Chọn hành tinh --</option>
            <option value="0">0 - Trái Đất</option>
            <option value="1">1 - Namec</option>
            <option value="2">2 - Xayda</option>
        </select>
        <p><strong>Lưu ý: Nhớ thoát game trước khi đổi Hành tinh nếu không mất tiền admin không chịu trách nhiệm</strong></p>
        <button type="submit">Đổi (-500,000 coin)</button>
    </form>
</div>
</body>
</html>
