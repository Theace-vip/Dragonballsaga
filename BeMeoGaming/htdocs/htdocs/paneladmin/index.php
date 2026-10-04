<?php include __DIR__ . '/includes/header.php'; include __DIR__ . '/includes/sidebar.php'; 
require_once '../core/set.php';
if ($_login === null) {
    echo '<script>window.location.href = "../Pages/login.php";</script>';
    exit;
}

// Chỉ cho phép tài khoản admin
if ($_admin != 1) {
    echo '<script>window.location.href="/"</script>';
    exit;
}
?>
  <div class="container-fluid p-4">
    <div class="row g-3">
      <div class="col-12">
        <div class="p-4 bg-white rounded-3 shadow-sm">
          <h3>Bảng điều khiển</h3>
          <p class="text-muted mb-0">Chọn một mục ở menu trái để quản lý dữ liệu.</p>
        </div>
      </div>
    </div>
  </div>
<?php include __DIR__ . '/includes/footer.php'; ?>
