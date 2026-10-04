<?php
require 'config.php'; // Kết nối CSDL

// Lấy các quà chưa nhận
$query = "SELECT * FROM gifts WHERE received = 0";
$result = $conn->query($query);

if (!$result) {
    die("Lỗi truy vấn gifts: " . $conn->error);
}

while ($row = $result->fetch_assoc()) {
    $username = $row['username'];
    $item_id = $row['prize_value']; // ID vật phẩm từ item_template
    $gift_id = $row['id'];

    // Tìm ID nhân vật theo tên
    $stmt = $conn->prepare("SELECT id FROM player WHERE name = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $res_player = $stmt->get_result();
    $player = $res_player->fetch_assoc();

    if (!$player) {
        echo "Không tìm thấy nhân vật: $username<br>";
        continue;
    }

    $player_id = $player['id'];

    // Thêm vật phẩm vào bảng items_bag
    $insert = $conn->prepare("INSERT INTO items_bag (player_id, item_template_id, quantity) VALUES (?, ?, ?)");
    $quantity = 1; // Số lượng item
    $insert->bind_param("iii", $player_id, $item_id, $quantity);
    $insert->execute();

    // Đánh dấu là đã nhận
    $update = $conn->prepare("UPDATE gifts SET received = 1 WHERE id = ?");
    $update->bind_param("i", $gift_id);
    $update->execute();

    echo "✅ Đã gửi item ID [$item_id] cho nhân vật [$username] (ID $player_id)<br>";
}

echo "<hr>Đã xử lý xong!";
?>
