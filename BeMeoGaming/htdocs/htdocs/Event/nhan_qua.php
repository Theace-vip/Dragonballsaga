<?php
session_start();
require_once __DIR__ . '/config.php';
header('Content-Type: application/json; charset=utf-8');

if (!isset($_SESSION['username'])) {
    echo json_encode(["status" => "error", "message" => "Bạn chưa đăng nhập"]);
    exit;
}

$username = $_SESSION['username'];
$id = intval($_POST['id']);

// 1. Lấy item trong kho_web
$stmt = $conn->prepare("SELECT item_name, quantity FROM kho_web WHERE id = ? AND username = ?");
$stmt->bind_param("is", $id, $username);
$stmt->execute();
$result = $stmt->get_result();
$item = $result->fetch_assoc();

if (!$item) {
    echo json_encode(["status" => "error", "message" => "Không tìm thấy vật phẩm"]);
    exit;
}
if ($item['quantity'] <= 0) {
    echo json_encode(["status" => "error", "message" => "Hết vật phẩm"]);
    exit;
}

// 2. Lấy template_id từ item_template
$stmt_tpl = $conn->prepare("SELECT id FROM item_template WHERE name = ?");
$stmt_tpl->bind_param("s", $item['item_name']);
$stmt_tpl->execute();
$res_tpl = $stmt_tpl->get_result();
$template = $res_tpl->fetch_assoc();

if (!$template) {
    echo json_encode(["status" => "error", "message" => "Không tìm thấy template cho item"]);
    exit;
}
$template_id = $template['id'];

// 3. Lấy ID nhân vật từ bảng player (cột `username` là tài khoản đăng nhập)
$stmt_player = $conn->prepare("SELECT id FROM player WHERE username = ?");
$stmt_player->bind_param("s", $username);
$stmt_player->execute();
$res_player = $stmt_player->get_result();
$player = $res_player->fetch_assoc();

if (!$player) {
    echo json_encode(["status" => "error", "message" => "Không tìm thấy nhân vật"]);
    exit;
}
$owner_id = $player['id'];

// 4. Thêm vào items_bag
$stmt_add = $conn->prepare("INSERT INTO items_bag (item_template_id, quantity, owner_id) VALUES (?, 1, ?)");
$stmt_add->bind_param("ii", $template_id, $owner_id);
$stmt_add->execute();

// 5. Trừ số lượng trong kho_web
$stmt_update = $conn->prepare("UPDATE kho_web SET quantity = quantity - 1 WHERE id = ? AND username = ?");
$stmt_update->bind_param("is", $id, $username);
$stmt_update->execute();

echo json_encode(["status" => "success", "message" => "Đã nhận vật phẩm vào túi đồ"]);
?>
