<?php
require __DIR__ . '/config.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';

$table = isset($_GET['table']) ? safe_table($_GET['table']) : 'account';

// Get columns metadata
$cols = $pdo->query("DESCRIBE `$table`")->fetchAll();
if (!$cols) { die("Không thể lấy cấu trúc bảng"); }

$pk = null; $auto = [];
foreach ($cols as $c) {
  if (strtolower($c['Key']) === 'pri') { $pk = $c['Field']; }
  if (strpos(strtolower($c['Extra']), 'auto_increment') !== false) { $auto[$c['Field']] = true; }
}

// Handle CREATE / UPDATE / DELETE actions
$action = $_POST['action'] ?? $_GET['action'] ?? null;

if ($action === 'create') {
    $fields = []; $place = []; $values = [];
    foreach ($cols as $c) {
        $f = $c['Field'];
        if (isset($auto[$f])) continue; // skip auto increment
        if (!isset($_POST[$f])) continue;
        $fields[] = "`$f`";
        $place[] = ":" . $f;
        $values[":" . $f] = $_POST[$f] === '' ? null : $_POST[$f];
    }
    if ($fields) {
        $sql = "INSERT INTO `$table` (" . implode(',', $fields) . ") VALUES (" . implode(',', $place) . ")";
        $stmt = $pdo->prepare($sql);
        $stmt->execute($values);
        header("Location: paneladmin/manage.php?table=$table&msg=created");
        exit;
    }
}

if ($action === 'update' && $pk && isset($_POST[$pk])) {
    $set = []; $values = [];
    foreach ($cols as $c) {
        $f = $c['Field'];
        if ($f === $pk) continue;
        if (!array_key_exists($f, $_POST)) continue;
        $set[] = "`$f` = :" . $f;
        $values[":" . $f] = $_POST[$f] === '' ? null : $_POST[$f];
    }
    $values[":pk"] = $_POST[$pk];
    if ($set) {
        $sql = "UPDATE `$table` SET " . implode(',', $set) . " WHERE `$pk` = :pk";
        $stmt = $pdo->prepare($sql);
        $stmt->execute($values);
        header("Location: paneladmin/manage.php?table=$table&msg=updated");
        exit;
    }
}

if ($action === 'delete' && $pk && isset($_GET['id'])) {
    $stmt = $pdo->prepare("DELETE FROM `$table` WHERE `$pk` = :id");
    $stmt->execute([':id' => $_GET['id']]);
    header("Location: paneladmin/manage.php?table=$table&msg=deleted");
    exit;
}

// Pagination
$per_page = isset($_GET['per_page']) ? max(1, (int)$_GET['per_page']) : 25;
$page = isset($_GET['page']) ? max(1, (int)$_GET['page']) : 1;
$offset = ($page - 1) * $per_page;

$total = $pdo->query("SELECT COUNT(*) AS c FROM `$table`")->fetch()['c'] ?? 0;
$stmt = $pdo->prepare("SELECT * FROM `$table` ORDER BY " . ($pk ? "`$pk` ASC" : "1") . " LIMIT :limit OFFSET :offset");
$stmt->bindValue(':limit', $per_page, PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute();
$rows = $stmt->fetchAll();
?>
  <div class="container-fluid p-4">
    <div class="d-flex align-items-center justify-content-between mb-3">
      <h3 class="mb-0">Quản lý bảng: <span class="text-primary"><?= htmlspecialchars($table) ?></span></h3>
      <div>
        <a href="index.php" class="btn btn-outline-secondary btn-sm">Về Dashboard</a>
      </div>
    </div>

    <?php if (isset($_GET['msg'])): ?>
      <div class="alert alert-success">Thao tác thành công: <?= htmlspecialchars($_GET['msg']) ?></div>
    <?php endif; ?>

    <div class="mb-3">
      <!-- Button trigger modal -->
      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">
        + Thêm bản ghi
      </button>
    </div>

    <div class="table-responsive">
      <table class="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <?php foreach ($cols as $c): ?>
              <th><?= htmlspecialchars($c['Field']) ?></th>
            <?php endforeach; ?>
            <th class="text-end">Hành động</th>
          </tr>
        </thead>
        <tbody>
        <?php foreach ($rows as $r): ?>
          <tr>
            <?php foreach ($cols as $c): $f = $c['Field']; ?>
              <td><?= htmlspecialchars((string)($r[$f] ?? '')) ?></td>
            <?php endforeach; ?>
            <td class="text-end action-buttons">
              <button class="btn btn-sm btn-info text-white" data-bs-toggle="modal" data-bs-target="#editModal<?= htmlspecialchars($r[$pk] ?? '') ?>">Sửa</button>
              <?php if ($pk): ?>
              <a class="btn btn-sm btn-danger" href="paneladmin/manage.php?table=<?= urlencode($table) ?>&action=delete&id=<?= urlencode($r[$pk]) ?>" onclick="return confirm('Xóa bản ghi này?')">Xóa</a>
              <?php endif; ?>
            </td>
          </tr>

          <!-- Edit Modal -->
          <div class="modal fade" id="editModal<?= htmlspecialchars($r[$pk] ?? '') ?>" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg">
              <form method="post" class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">Sửa bản ghi</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  <input type="hidden" name="action" value="update">
                  <?php if ($pk): ?><input type="hidden" name="<?= htmlspecialchars($pk) ?>" value="<?= htmlspecialchars($r[$pk]) ?>"><?php endif; ?>
                  <div class="row g-3">
                    <?php foreach ($cols as $c):
                        $f = $c['Field'];
                        $is_auto = isset($auto[$f]);
                        $value = $r[$f] ?? '';
                        $type = strtolower($c['Type']);
                        $inputType = (strpos($type, 'int') !== false) ? 'number' : ((strpos($type,'text')!==false || strpos($type,'blob')!==false) ? 'textarea' : 'text');
                        if ($f === $pk || $is_auto) { $disabled = 'disabled'; } else { $disabled = ''; }
                    ?>
                      <div class="col-md-6">
                        <label class="form-label"><?= htmlspecialchars($f) ?></label>
                        <?php if ($inputType === 'textarea'): ?>
                          <textarea class="form-control" name="<?= htmlspecialchars($f) ?>" <?= $disabled ?>><?= htmlspecialchars($value) ?></textarea>
                        <?php else: ?>
                          <input class="form-control" type="<?= $inputType ?>" name="<?= htmlspecialchars($f) ?>" value="<?= htmlspecialchars($value) ?>" <?= $disabled ?>>
                        <?php endif; ?>
                      </div>
                    <?php endforeach; ?>
                  </div>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                  <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
              </form>
            </div>
          </div>
        <?php endforeach; ?>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <nav class="mt-3" aria-label="Page nav">
      <?php $pages = max(1, ceil($total / $per_page)); ?>
      <ul class="pagination">
        <?php for ($i=1; $i <= $pages; $i++): ?>
          <li class="page-item <?= $i===$page ? 'active' : '' ?>">
            <a class="page-link" href="?table=<?= urlencode($table) ?>&page=<?= $i ?>&per_page=<?= $per_page ?>"><?= $i ?></a>
          </li>
        <?php endfor; ?>
      </ul>
    </nav>
  </div>

  <!-- Create Modal -->
  <div class="modal fade" id="createModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <form method="post" class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Thêm bản ghi</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" name="action" value="create">
          <div class="row g-3">
            <?php foreach ($cols as $c):
                $f = $c['Field'];
                if (isset($auto[$f])) continue;
                $type = strtolower($c['Type']);
                $inputType = (strpos($type, 'int') !== false) ? 'number' : ((strpos($type,'text')!==false || strpos($type,'blob')!==false) ? 'textarea' : 'text');
            ?>
              <div class="col-md-6">
                <label class="form-label"><?= htmlspecialchars($f) ?></label>
                <?php if ($inputType === 'textarea'): ?>
                  <textarea class="form-control" name="<?= htmlspecialchars($f) ?>"></textarea>
                <?php else: ?>
                  <input class="form-control" type="<?= $inputType ?>" name="<?= htmlspecialchars($f) ?>">
                <?php endif; ?>
              </div>
            <?php endforeach; ?>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
          <button type="submit" class="btn btn-primary">Tạo</button>
        </div>
      </form>
    </div>
  </div>

<?php include __DIR__ . '/includes/footer.php'; ?>
