<?php
session_start();
include_once('config.php');

if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit;
}

$username = $_SESSION['username'];

// Phân trang
$limit = 8;
$page = isset($_GET['page']) ? max(1, (int)$_GET['page']) : 1;
$offset = ($page - 1) * $limit;

// Tổng sản phẩm
$total_sql = "SELECT COUNT(*) as total FROM web_shop";
$total_result = $conn->query($total_sql);
$total_items = $total_result->fetch_assoc()['total'] ?? 0;
$total_pages = ceil($total_items / $limit);

// Lấy số dư user
$sql = "SELECT vnd FROM account WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$vnd = $user['vnd'] ?? 0;

// Lấy sản phẩm
$sql_shop = "SELECT id, item_name, quantity, price, item_img FROM web_shop LIMIT $offset, $limit";
$result = $conn->query($sql_shop);
$items = $result->fetch_all(MYSQLI_ASSOC);

// Lấy tên player (làm notice)
$player_result = $conn->query("SELECT name FROM player");
$player_names = [];
if($player_result){
    while($row = $player_result->fetch_assoc()){
        $player_names[] = $row['name'];
    }
}
?>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1"> <!-- RẤT QUAN TRỌNG CHO MOBILE -->
<title>🛒 WEBSHOP SAGA</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<style>
:root{
  --brand:#28a745;
  --danger:#e63946;
  --ink:#2c3e50;
  --bg:#e9f7ef;
  --card:#ffffff;
  --muted:#6b7280;
}

/* ==== Layout chung ==== */
body{
  background: var(--bg);
  font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, "Helvetica Neue", "Noto Sans", "Liberation Sans", sans-serif;
  margin:0;
}
.wrapper{
  max-width: 1140px;
  margin-inline: auto;
  padding: clamp(12px, 3vw, 24px);
  background: #fffffff6;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,.08);
}

/* ==== Tiêu đề ==== */
h1{
  color: var(--ink);
  font-weight: 800;
  text-align: center;
  font-size: clamp(20px, 4.5vw, 28px);
  margin: 0 0 6px;
}
h2{
  color:#ef4444;
  font-size: clamp(12px, 3.2vw, 14px);
  text-align:center;
  font-weight:700;
  margin: 0 0 10px;
}
.blink{ animation: blink 1s steps(1,start) infinite; }
@keyframes blink{0%,50%,100%{opacity:1}25%,75%{opacity:0}}

/* ==== Notice chạy ngang (CSS only, mượt mobile) ==== */
.notice{
  position: relative;
  overflow: hidden;
  background:#2ecc71;
  color:#fff;
  font-weight:700;
  border-radius: 8px;
  padding: 8px 10px;
  margin-bottom: 14px;
}
.notice-track{
  display: inline-block;
  white-space: nowrap;
  will-change: transform;
  animation: marquee 12s linear infinite;
}
@keyframes marquee{
  0%{ transform: translateX(100%) }
  100%{ transform: translateX(-100%) }
}

/* ==== Thông tin user + nút nạp ==== */
.meta{
  font-size: clamp(13px, 3.4vw, 15px);
  display:flex;
  flex-wrap:wrap;
  gap:8px 12px;
  align-items:center;
  justify-content:center;
  color:#111827;
}
.meta b{ color:#111827 }
.kho-web-btn{
  background: orange;
  color:#fff !important;
  padding: 8px 12px;
  border-radius:8px;
  font-weight:700;
  text-decoration:none;
}
.kho-web-btn:hover{ background: #ff8a00 }

/* ==== Lưới sản phẩm ==== */
/* Dùng grid thuần để mượt, đồng thời vẫn đặt trong .row để giữ khoảng cách bootstrap đẹp trên desktop */
.product-grid{
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
@media (min-width: 576px){
  .product-grid{ grid-template-columns: repeat(3, 1fr); gap: 14px; }
}
@media (min-width: 992px){
  .product-grid{ grid-template-columns: repeat(4, 1fr); gap: 16px; }
}

/* ==== Card sản phẩm (bỏ width cố định, tối ưu chạm) ==== */
.shop-card{
  background: var(--card);
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 12px;
  display:flex;
  flex-direction: column;
  gap: 8px;
  min-height: 100%;
  position: relative;
  box-shadow: 0 4px 8px rgba(0,0,0,0.06);
  transition: transform .18s ease, box-shadow .18s ease;
}
.shop-card:has(button:active){ transform: scale(.995); }
@media (hover:hover){
  .shop-card:hover{ transform: translateY(-3px); box-shadow:0 8px 16px rgba(0,0,0,.12); }
}

/* Hình ảnh cân giữa, không vỡ tỉ lệ, tăng chút size trên desktop */
.image-wrapper{
  display:flex; align-items:center; justify-content:center;
  padding: 6px 0 2px;
  min-height: 96px;
}
.image-wrapper img{
  width: clamp(64px, 16vw, 92px);
  height: clamp(64px, 16vw, 92px);
  object-fit: contain;
  image-rendering: -webkit-optimize-contrast;
}

/* Text trong card */
.shop-card h5{
  font-size: clamp(14px, 3.8vw, 16px);
  font-weight: 800;
  color:#111827;
  margin: 2px 0 0;
  text-wrap: balance;
}
.shop-card p{
  font-size: clamp(12px, 3.4vw, 14px);
  color:#374151;
  margin: 0;
}

/* Badge giá luôn nổi đúng vị trí */
.price-badge{
  position:absolute;
  top: 10px; right: 10px;
  background: var(--danger);
  color:#fff;
  font-weight: 800;
  font-size: clamp(11px, 3.1vw, 13px);
  padding: 4px 8px;
  border-radius: 999px;
  z-index:1;
  box-shadow: 0 2px 6px rgba(0,0,0,.12);
}

/* Input số + nút bấm thân thiện ngón tay (44px) */
.amount-input{
  font-size: clamp(13px, 3.4vw, 14px);
  padding: 10px 12px;
  border-radius: 10px;
}
.btn-mua{
  background: var(--brand);
  color:#fff;
  font-weight:800;
  border:none;
  border-radius: 10px;
  padding: 10px 12px;
  display:flex; align-items:center; justify-content:center;
  gap:8px;
  width: 100%;
  min-height: 44px;
}
.btn-mua::before{ content:"⚡"; }
.btn-mua:hover{ background: #218838; }

/* Nút “Chi tiết” nhỏ gọn nhưng full-width trên mobile */
.btn-detail{
  font-size: clamp(12px, 3.2vw, 13px);
  width: 100%;
}

/* Pagination: tăng target area */
.pagination .page-link{
  color: var(--brand);
  min-width: 40px;
  text-align:center;
}
.pagination .active .page-link{
  background: var(--brand); border-color: var(--brand); color:#fff;
}

/* Bảng lịch sử: thu gọn trên mobile bằng cách bọc dòng dài */
.table{ font-size: clamp(12px, 3.3vw, 14px); }
.table td, .table th{ vertical-align: middle; }

/* Modal: chữ tự xuống dòng */
.modal-body p{ word-break: break-word; }

/* Message box spacing */
#message-box{ margin-top: 12px; }
</style>
</head>
<body>
  <div class="wrapper">
    <h1>🛒 Cửa Hàng Vật Phẩm</h1>
    <h2 class="blink">Thoát game trước khi mua vật phẩm để tránh lỗi cộng vật phẩm!</h2>

    <!-- Notice -->
    <div class="notice"><span id="notice-text" class="notice-track"></span></div>

    <!-- Info bar -->
    <div class="meta mb-2">
      <span>Tài khoản: <b><?php echo htmlspecialchars($username); ?></b></span>
      <span>•</span>
      <span>Số dư: <b id="vnd"><?php echo number_format($vnd); ?></b> Coin</span>
      <a class="kho-web-btn" href="../Pages/Nap.php">Nạp tiền</a>
    </div>

    <div id="message-box"></div>

    <!-- Lưới sản phẩm -->
    <?php if ($items): ?>
      <div class="product-grid mb-2">
        <?php foreach ($items as $item): ?>
          <div>
            <div class="shop-card">
              <div class="price-badge"><?php echo number_format($item['price']); ?> coin</div>

              <div class="image-wrapper">
                <img loading="lazy" src="icon/<?php echo htmlspecialchars($item['item_img']); ?>.png" alt="<?php echo htmlspecialchars($item['item_name']); ?>">
              </div>

              <h5>🎁 <?php echo htmlspecialchars($item['item_name']); ?></h5>
              <p class="text-muted">Tồn kho: <?php echo (int)$item['quantity']; ?></p>

              <input type="number"
                     class="form-control amount-input"
                     data-id="<?php echo $item['id']; ?>"
                     min="1"
                     max="<?php echo (int)$item['quantity']; ?>"
                     value="1"
                     inputmode="numeric"
                     pattern="[0-9]*">

              <button class="btn-mua"
                      data-id="<?php echo $item['id']; ?>"
                      data-name="<?php echo htmlspecialchars($item['item_name']); ?>"
                      data-price="<?php echo (int)$item['price']; ?>">
                MUA NGAY
              </button>

              <a href="detail_shop.php?id=<?php echo $item['id']; ?>"
                 class="btn btn-info btn-detail mt-2">Chi tiết</a>
            </div>
          </div>
        <?php endforeach; ?>
      </div>
    <?php else: ?>
      <div class="alert alert-warning">Không có vật phẩm nào trong cửa hàng</div>
    <?php endif; ?>

    <!-- Phân trang -->
    <?php if ($total_pages > 1): ?>
      <nav class="mt-3">
        <ul class="pagination justify-content-center flex-wrap gap-1">
          <?php for ($i = 1; $i <= $total_pages; $i++): ?>
            <li class="page-item <?php if($i==$page) echo 'active'; ?>">
              <a class="page-link" href="?page=<?php echo $i; ?>"><?php echo $i; ?></a>
            </li>
          <?php endfor; ?>
        </ul>
      </nav>
    <?php endif; ?>

    <!-- Modal Xác nhận -->
    <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Xác nhận mua hàng</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p id="confirmText" class="m-0"></p>
          </div>
          <div class="modal-footer flex-wrap gap-2">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
            <button type="button" class="btn btn-success" id="confirmBuy">Xác nhận</button>
          </div>
        </div>
      </div>
    </div>

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
    $where_sql = count($where) ? "WHERE " . implode(" AND ", $where) : "";

    // --- Query lịch sử ---
    $sql_history = "SELECT username, item_name, amount, price, total_price, created_at 
                    FROM web_shop_history 
                    $where_sql
                    ORDER BY created_at DESC 
                    LIMIT 10";
    $result_history = $conn->query($sql_history);
    $history = $result_history ? $result_history->fetch_all(MYSQLI_ASSOC) : [];
    ?>

    <!-- Bộ lọc lịch sử -->
    <form method="get" class="row g-2 mb-3 mt-4">
      <div class="col-12 col-md-4">
        <input type="text" name="username" value="<?php echo isset($_GET['username']) ? htmlspecialchars($_GET['username']) : ''; ?>" class="form-control" placeholder="Lọc theo player">
      </div>
      <div class="col-12 col-md-4">
        <input type="text" name="item_name" value="<?php echo isset($_GET['item_name']) ? htmlspecialchars($_GET['item_name']) : ''; ?>" class="form-control" placeholder="Lọc theo vật phẩm">
      </div>
      <div class="col-12 col-md-4 d-flex gap-2">
        <button type="submit" class="btn btn-primary flex-fill">Lọc</button>
        <a href="shop.php" class="btn btn-secondary flex-fill">Reset</a>
      </div>
    </form>

    <!-- Lịch sử -->
    <h3 class="mt-3 mb-2" style="font-size: clamp(16px,4.2vw,20px); font-weight:800;">📜 Lịch sử mua hàng</h3>
    <div class="table-responsive">
      <table class="table table-striped table-bordered mb-0">
        <thead>
          <tr>
            <th>Player</th>
            <th>Tên vật phẩm</th>
            <th>Số lượng</th>
            <th>Giá / 1</th>
            <th>Tổng</th>
            <th>Thời gian</th>
          </tr>
        </thead>
        <tbody>
          <?php if (!empty($history)): ?>
            <?php foreach ($history as $h): ?>
              <tr>
                <td><?php echo htmlspecialchars($h['username']); ?></td>
                <td><?php echo htmlspecialchars($h['item_name']); ?></td>
                <td><?php echo (int)$h['amount']; ?></td>
                <td><?php echo number_format((int)$h['price']); ?> coin</td>
                <td><?php echo number_format((int)$h['total_price']); ?> coin</td>
                <td><?php echo htmlspecialchars($h['created_at']); ?></td>
              </tr>
            <?php endforeach; ?>
          <?php else: ?>
            <tr><td colspan="6" class="text-center">Chưa có lịch sử mua hàng</td></tr>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div><!-- /.wrapper -->

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
$(function(){
  const playerNames = <?php echo json_encode($player_names); ?>;
  const items = <?php echo json_encode($items); ?>;

  // Notice text (đổi nội dung mỗi vòng lặp)
  function randomNotice(){
    if(playerNames.length === 0 || items.length === 0)
      return "Chào mừng bạn đến với WebShop!";
    const player = playerNames[Math.floor(Math.random()*playerNames.length)];
    const itemObj = items[Math.floor(Math.random()*items.length)];
    return `🔥 Player ${player} vừa mua ${itemObj.item_name} với giá ${Number(itemObj.price).toLocaleString()} coin`;
  }
  const span = $("#notice-text");
  span.text(randomNotice());
  // Cập nhật nội dung mỗi 12s (khớp animation CSS)
  setInterval(()=>{ span.text(randomNotice()); }, 12000);

  // Mua hàng (giữ nguyên API, chỉ đảm bảo UX mobile tốt)
  let currentId = null, currentAmount = 1;

  $(document).on("click", ".btn-mua", function(){
    currentId = $(this).data("id");
    const itemName = $(this).data("name");
    const price = Number($(this).data("price")) || 0;
    currentAmount = Number($(`.amount-input[data-id='${currentId}']`).val()) || 1;
    const total = price * currentAmount;

    $("#confirmText").text(`Bạn có chắc chắn muốn mua ${currentAmount} x ${itemName} với giá ${total.toLocaleString()} coin không?`);
    new bootstrap.Modal(document.getElementById('confirmModal')).show();
  });

  $("#confirmBuy").on("click", function(){
    $.ajax({
      url: "mua.php",
      method: "POST",
      data: { id: currentId, amount: currentAmount },
      dataType: "json"
    }).done(function(res){
      const ok = res.status === "success";
      $("#message-box").html(`<div class="alert ${ok?'alert-success':'alert-danger'} mb-2">${res.message || 'Thao tác hoàn tất'}</div>`);
      bootstrap.Modal.getInstance(document.getElementById('confirmModal')).hide();
      // Optional: cuộn lên message trên mobile
      window.scrollTo({ top: 0, behavior: "smooth" });
    }).fail(function(){
      $("#message-box").html('<div class="alert alert-danger mb-2">Có lỗi xảy ra khi gửi yêu cầu!</div>');
      bootstrap.Modal.getInstance(document.getElementById('confirmModal')).hide();
    });
  });

  // Chặn nhập ngoài min/max + làm tròn
  $(document).on("input", ".amount-input", function(){
    const max = Number($(this).attr("max")) || 999999;
    let val = Math.max(1, Math.min(max, Math.floor(Number($(this).val()) || 1)));
    $(this).val(val);
  });
});
</script>
</body>
</html>
