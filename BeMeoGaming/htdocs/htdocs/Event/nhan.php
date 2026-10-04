<?php
header('Content-Type: application/json; charset=utf-8');
session_start();
include_once('config.php');

function send_json($status, $message) {
    echo json_encode([
        "status" => $status,
        "message" => $message
    ], JSON_UNESCAPED_UNICODE);
    exit;
}

if (!isset($_SESSION['username'])) {
    send_json("error", "Bạn chưa đăng nhập!");
}

$username = $_SESSION['username'];

if (!isset($_POST['id']) || !is_numeric($_POST['id'])) {
    send_json("error", "Thiếu ID vật phẩm");
}
$item_id = intval($_POST['id']);

// Lấy vật phẩm từ kho_web (chỉ lấy template_id)
$sql = "SELECT id, template_id, quantity 
        FROM kho_web 
        WHERE id = ? AND username = ?";
$stmt = $conn->prepare($sql);
if (!$stmt) {
    send_json("error", "Lỗi SQL: " . $conn->error);
}
$stmt->bind_param("is", $item_id, $username);
$stmt->execute();
$item = $stmt->get_result()->fetch_assoc();
$stmt->close();

if (!$item) {
    send_json("error", "Vật phẩm không tồn tại");
}
if ($item['quantity'] <= 0) {
    send_json("error", "Không còn vật phẩm trong kho");
}

// Lấy túi đồ
$sql = "SELECT p.items_bag 
        FROM player p 
        JOIN account a ON a.id = p.account_id 
        WHERE a.username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $username);
$stmt->execute();
$data = $stmt->get_result()->fetch_assoc();
$stmt->close();

$items_bag = json_decode($data['items_bag'], true);
if (!is_array($items_bag)) {
    $items_bag = [];
}

// Chuẩn hoá slot thành mảng
foreach ($items_bag as &$slot) {
    if (is_string($slot)) {
        $decoded_slot = json_decode($slot, true);
        if (is_array($decoded_slot)) {
            $slot = $decoded_slot;
        }
    }
}
unset($slot);

// Bảo đảm đủ 20 slot
for ($i = 0; $i < 20; $i++) {
    if (!isset($items_bag[$i]) || !is_array($items_bag[$i]) || count($items_bag[$i]) < 4) {
        $items_bag[$i] = [-1, 0, "[]", 0];
    }
}

// Vật phẩm mới (template_id)
$new_item = [
    intval($item['template_id']), // ID game
    1,                            // số lượng
    "[]",                         // option mặc định
    0                             // expire_time mặc định
];

// Cộng dồn nếu đã có
$found = false;
for ($i = 0; $i < count($items_bag); $i++) {
    if ($items_bag[$i][0] == $new_item[0]) {
        $items_bag[$i][1] += $new_item[1];
        $found = true;
        break;
    }
}

// Nếu chưa có thì tìm slot trống
if (!$found) {
    for ($i = 0; $i < count($items_bag); $i++) {
        if ($items_bag[$i][0] == -1) {
            $items_bag[$i] = $new_item;
            $found = true;
            break;
        }
    }
}

// Nếu full thì báo lỗi
if (!$found) {
    send_json("error", "Túi đồ đã đầy");
}

// Encode lại về định dạng cũ
foreach ($items_bag as &$slot) {
    if (is_array($slot)) {
        $slot = json_encode($slot, JSON_UNESCAPED_UNICODE);
    }
}
unset($slot);

$items_bag_json = json_encode($items_bag, JSON_UNESCAPED_UNICODE);

// Cập nhật túi đồ
$sql = "UPDATE player p 
        JOIN account a ON a.id = p.account_id 
        SET p.items_bag = ? 
        WHERE a.username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $items_bag_json, $username);
$stmt->execute();
$stmt->close();

// Giảm số lượng trong kho_web
$sql = "UPDATE kho_web 
        SET quantity = quantity - 1 
        WHERE id = ? AND username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $item_id, $username);
$stmt->execute();
$stmt->close();

send_json("success", "Nhận vật phẩm thành công!");
?>
