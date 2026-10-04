<?php
// --- Lọc lịch sử ---
$where = [];
if (!empty($_GET['username'])) {
    $username_filter = $conn->real_escape_string($_GET['username']);
    $where[] = "username LIKE '%$username_filter%'";
}
if (!empty($_GET['item_name'])) {
    $item_filter = $conn->real_escape_string($_GET['item_name']);
    $where[] = "item_name LIKE '%$item_filter%'";
}

$where_sql = "";
if (count($where) > 0) {
    $where_sql = "WHERE " . implode(" AND ", $where);
}

// --- Query lịch sử ---
$sql_history = "SELECT username, item_name, amount, price, total_price, created_at 
                FROM web_shop_history 
                $where_sql
                ORDER BY created_at DESC 
                LIMIT 10";
$result_history = $conn->query($sql_history);
$history = $result_history->fetch_all(MYSQLI_ASSOC);
?>
<form method="get" class="row g-2 mb-3">
    <div class="col-md-3">
        <input type="text" name="username" value="<?php echo isset($_GET['username']) ? htmlspecialchars($_GET['username']) : ''; ?>" class="form-control" placeholder="Lọc theo player">
    </div>
    <div class="col-md-3">
        <input type="text" name="item_name" value="<?php echo isset($_GET['item_name']) ? htmlspecialchars($_GET['item_name']) : ''; ?>" class="form-control" placeholder="Lọc theo vật phẩm">
    </div>
    <div class="col-md-2">
        <button type="submit" class="btn btn-primary">Lọc</button>
        <a href="shop.php" class="btn btn-secondary">Reset</a>
    </div>
</form>