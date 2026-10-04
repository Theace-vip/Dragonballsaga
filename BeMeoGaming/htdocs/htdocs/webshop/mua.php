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

// Kiểm tra dữ liệu
if (!isset($_POST['id']) || !is_numeric($_POST['id'])) {
    send_json("error", "Thiếu ID vật phẩm");
}
if (!isset($_POST['amount']) || !is_numeric($_POST['amount']) || $_POST['amount'] <= 0) {
    send_json("error", "Số lượng mua không hợp lệ");
}

$item_id = intval($_POST['id']);
$amount  = intval($_POST['amount']);

try {
    $conn->begin_transaction();

    // Lấy thông tin item trong shop
    $sql = "SELECT id, item_name, template_id, quantity, price, sold_quantity, item_img 
            FROM web_shop WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $item_id);
    $stmt->execute();
    $item = $stmt->get_result()->fetch_assoc();
    $stmt->close();

    if (!$item) {
        throw new Exception("Vật phẩm không tồn tại trong shop");
    }
    if ($item['quantity'] < $amount) {
        throw new Exception("Shop không đủ số lượng để bán");
    }

    // Tổng giá
    $total_price = $item['price'] * $amount;

    // Lấy số tiền người chơi
    $sql = "SELECT a.vnd, p.items_bag 
            FROM account a
            JOIN player p ON a.id = p.account_id
            WHERE a.username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $player = $stmt->get_result()->fetch_assoc();
    $stmt->close();

    if (!$player) {
        throw new Exception("Không tìm thấy thông tin người chơi");
    }
    if ($player['vnd'] < $total_price) {
        throw new Exception("Bạn không đủ tiền để mua vật phẩm này");
    }

    // Giải mã túi đồ
    $items_bag = json_decode($player['items_bag'], true);
    if (!is_array($items_bag)) {
        $items_bag = [];
    }
    foreach ($items_bag as &$slot) {
        if (is_string($slot)) {
            $decoded_slot = json_decode($slot, true);
            if (is_array($decoded_slot)) {
                $slot = $decoded_slot;
            }
        }
    }
    unset($slot);
    for ($i = 0; $i < 20; $i++) {
        if (!isset($items_bag[$i]) || !is_array($items_bag[$i]) || count($items_bag[$i]) < 4) {
            $items_bag[$i] = [-1, 0, "[]", 0];
        }
    }

    // Tạo item mới
    $new_item = [
        intval($item['template_id']), // template_id game
        $amount,                      // số lượng mua
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
    if (!$found) {
        throw new Exception("Túi đồ đã đầy");
    }

    // Mã hóa lại túi đồ
    foreach ($items_bag as &$slot) {
        if (is_array($slot)) {
            $slot = json_encode($slot, JSON_UNESCAPED_UNICODE);
        }
    }
    unset($slot);
    $items_bag_json = json_encode($items_bag, JSON_UNESCAPED_UNICODE);

    // Trừ tiền, cập nhật túi đồ
    $sql = "UPDATE account a
            JOIN player p ON a.id = p.account_id
            SET a.vnd = a.vnd - ?, p.items_bag = ?
            WHERE a.username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("iss", $total_price, $items_bag_json, $username);
    $stmt->execute();
    $stmt->close();

    // Cập nhật shop: trừ số lượng, tăng sold_quantity
    $sql = "UPDATE web_shop 
            SET quantity = quantity - ?, sold_quantity = sold_quantity + ?
            WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("iii", $amount, $amount, $item_id);
    $stmt->execute();
    $stmt->close();

    // Lưu lịch sử mua hàng
    $sql = "INSERT INTO web_shop_history (username, item_id, item_name, amount, price, total_price, item_img) 
            VALUES (?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sisiiss", $username, $item_id, $item['item_name'], $amount, $item['price'], $total_price, $item['item_img']);
    $stmt->execute();
    $stmt->close();

    $conn->commit();

    send_json("success", "Mua thành công {$amount} {$item['item_name']} với giá {$total_price} Coin!");
} catch (Exception $e) {
    $conn->rollback();
    send_json("error", "Có lỗi khi mua hàng: " . $e->getMessage());
}
?>
