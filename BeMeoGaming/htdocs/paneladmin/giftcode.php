<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';
$msg=''; $err='';
if ($_SERVER['REQUEST_METHOD']==='POST') {
  try {
    $act=$_POST['action']??'';
    $detail=trim($_POST['detail']??'');
    if ($act!=='delete' && !is_valid_json($detail)) throw new Exception('detail khong phai JSON hop le. Mau: [{"temp_id":"14","quantity":"7","options":[{"id":"30","param":"0"}]}]');
    if ($act==='update') {
      db()->prepare("UPDATE giftcode SET code=?,count_left=?,detail=?,expired=? WHERE id=?")
        ->execute([strtolower(trim($_POST['code'])),(int)$_POST['count_left'],$detail,$_POST['expired']?:null,(int)$_POST['id']]);
      $msg='Da luu giftcode #'.(int)$_POST['id']; write_log('giftcode','update',$_POST['code']);
    } elseif ($act==='create') {
      db()->prepare("INSERT INTO giftcode (code,count_left,detail,expired) VALUES (?,?,?,?)")
        ->execute([strtolower(trim($_POST['code'])),(int)$_POST['count_left'],$detail,$_POST['expired']?:null]);
      $msg='Da tao giftcode '.$_POST['code']; write_log('giftcode','create',$_POST['code']);
    } elseif ($act==='delete') {
      db()->prepare("DELETE FROM giftcode WHERE id=?")->execute([(int)$_POST['id']]);
      $msg='Da xoa giftcode'; write_log('giftcode','delete','#'.(int)$_POST['id']);
    }
  } catch (Exception $e) { $err=$e->getMessage(); }
}
try { $rows=db()->query("SELECT * FROM giftcode ORDER BY id DESC")->fetchAll(); } catch (Exception $e) { $rows=[]; }
$h=fn($v)=>htmlspecialchars((string)($v??''));
?>
<div class="container-fluid p-4">
  <h3>Giftcode</h3>
  <p class="text-muted small">count_left = -1 la vo han (Java tu doi thanh 999999999). Code luu chu thuong.</p>
  <?php if($msg): ?><div class="alert alert-success"><?= $h($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= $h($err) ?></div><?php endif; ?>
  <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#createModal">+ Tao code</button>
  <div class="table-responsive"><table class="table table-striped table-hover align-middle">
    <thead><tr><th>ID</th><th>Code</th><th>Con lai</th><th>Het han</th><th class="text-end">Sua</th></tr></thead><tbody>
    <?php foreach($rows as $r): ?>
    <tr><td><?= $h($r['id']) ?></td><td><b><?= $h($r['code']) ?></b></td><td><?= $h($r['count_left']) ?></td><td><?= $h($r['expired']) ?></td>
    <td class="text-end"><button class="btn btn-sm btn-info text-white" data-bs-toggle="modal" data-bs-target="#e<?= $r['id'] ?>">Sua</button></td></tr>
    <div class="modal fade" id="e<?= $r['id'] ?>" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-lg">
      <form method="post" class="modal-content"><div class="modal-header"><h5 class="modal-title"><?= $h($r['code']) ?></h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
      <div class="modal-body"><input type="hidden" name="action" value="update"><input type="hidden" name="id" value="<?= $h($r['id']) ?>">
        <div class="row g-2">
          <div class="col-md-6"><label class="form-label">Code</label><input class="form-control" name="code" value="<?= $h($r['code']) ?>"></div>
          <div class="col-md-3"><label class="form-label">Con lai (-1 = vo han)</label><input class="form-control" type="number" name="count_left" value="<?= $h($r['count_left']) ?>"></div>
          <div class="col-md-3"><label class="form-label">Het han</label><input class="form-control" type="datetime-local" name="expired" value="<?= $h(str_replace(' ','T',substr($r['expired']??'',0,16))) ?>"></div>
          <div class="col-12"><label class="form-label">Detail JSON [{temp_id,quantity,options:[{id,param}]}]</label>
          <textarea class="form-control json" rows="8" name="detail"><?= $h($r['detail']) ?></textarea></div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-outline-danger me-auto" name="action" value="delete" onclick="return confirm('Xoa code nay?')">Xoa</button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Dong</button>
        <button type="submit" class="btn btn-primary" name="action" value="update">Luu</button>
      </div></form>
    </div></div>
    <?php endforeach; ?>
    </tbody></table></div>
</div>
<div class="modal fade" id="createModal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-lg">
  <form method="post" class="modal-content"><div class="modal-header"><h5 class="modal-title">Tao code</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
  <div class="modal-body"><input type="hidden" name="action" value="create">
    <div class="row g-2">
      <div class="col-md-6"><label class="form-label">Code</label><input class="form-control" name="code" required></div>
      <div class="col-md-3"><label class="form-label">Con lai</label><input class="form-control" type="number" name="count_left" value="100"></div>
      <div class="col-md-3"><label class="form-label">Het han</label><input class="form-control" type="datetime-local" name="expired"></div>
      <div class="col-12"><label class="form-label">Detail JSON</label><textarea class="form-control json" rows="6" name="detail">[{"temp_id":"14","quantity":"7","options":[{"id":"30","param":"0"}]}]</textarea></div>
    </div>
  </div>
  <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Dong</button><button class="btn btn-primary">Tao</button></div></form>
</div></div>
<?php include __DIR__ . '/includes/footer.php'; ?>
