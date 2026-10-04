<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';
$msg=''; $err='';
$q = trim($_GET['q'] ?? '');
$page = max(1,(int)($_GET['page'] ?? 1)); $per=20; $off=($page-1)*$per;

if ($_SERVER['REQUEST_METHOD']==='POST') {
  $act = $_POST['action'] ?? '';
  try {
    if ($act==='update') {
      $id=(int)$_POST['id'];
      $drop=trim($_POST['dropItems'] ?? '');
      if ($drop!=='' && !is_valid_json($drop)) throw new Exception('dropItems khong phai JSON hop le');
      $map=trim($_POST['mapAppear'] ?? '');
      db()->prepare("UPDATE listbosses SET name=?,hp=?,dame=?,appearTime=?,mapAppear=?,dropItems=?,head=?,body=?,leg=? WHERE id=?")
        ->execute([$_POST['name'],$_POST['hp'],$_POST['dame'],$_POST['appearTime'],$map,$drop,(int)$_POST['head'],(int)$_POST['body'],(int)$_POST['leg'],$id]);
      $msg='Da luu boss #'.$id.'. Restart server de ap dung.'; write_log('boss','update','#'.$id.' '.$_POST['name']);
    } elseif ($act==='create') {
      $drop=trim($_POST['dropItems'] ?? '[]');
      if (!is_valid_json($drop)) throw new Exception('dropItems khong phai JSON hop le');
      $max=(int)db()->query("SELECT COALESCE(MAX(id),0) FROM listbosses")->fetchColumn();
      db()->prepare("INSERT INTO listbosses (id,name,hp,dame,appearTime,mapAppear,dropItems,head,body,leg) VALUES (?,?,?,?,?,?,?,?,?,?)")
        ->execute([$max+1,$_POST['name'],$_POST['hp'],$_POST['dame'],$_POST['appearTime'],$_POST['mapAppear'],$drop,(int)$_POST['head'],(int)$_POST['body'],(int)$_POST['leg']]);
      $msg='Da them boss #'.($max+1); write_log('boss','create','#'.($max+1).' '.$_POST['name']);
    } elseif ($act==='delete') {
      db()->prepare("DELETE FROM listbosses WHERE id=?")->execute([(int)$_POST['id']]);
      $msg='Da xoa boss #'.(int)$_POST['id']; write_log('boss','delete','#'.(int)$_POST['id']);
    }
  } catch (Exception $e) { $err=$e->getMessage(); }
}
$where=''; $params=[];
if ($q!=='') { $where='WHERE name LIKE ?'; $params[]='%'.$q.'%'; }
$st=db()->prepare("SELECT COUNT(*) FROM listbosses $where"); $st->execute($params); $total=(int)$st->fetchColumn();
$st=db()->prepare("SELECT * FROM listbosses $where ORDER BY id ASC LIMIT $per OFFSET $off"); $st->execute($params); $rows=$st->fetchAll();
$pages=max(1,ceil($total/$per));
$h=fn($v)=>htmlspecialchars((string)($v??''));
?>
<div class="container-fluid p-4">
  <h3>Quan ly Boss <small class="text-muted">(bang listbosses)</small></h3>
  <?php if($msg): ?><div class="alert alert-success"><?= $h($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= $h($err) ?></div><?php endif; ?>
  <form class="row g-2 mb-3" method="get">
    <div class="col-md-6"><input class="form-control" name="q" placeholder="Tim theo ten boss..." value="<?= $h($q) ?>"></div>
    <div class="col-md-2"><button class="btn btn-outline-primary">Tim</button></div>
    <div class="col-md-4 text-end"><button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">+ Them boss</button></div>
  </form>
  <div class="table-responsive"><table class="table table-striped table-hover align-middle">
    <thead><tr><th>ID</th><th>Ten</th><th>HP</th><th>Dame</th><th>TG hoi sinh</th><th>Map</th><th>Ngoai hinh</th><th class="text-end">Sua</th></tr></thead>
    <tbody>
    <?php foreach($rows as $r): ?>
      <tr>
        <td><?= $h($r['id']) ?></td><td><b><?= $h($r['name']) ?></b></td>
        <td><?= $h($r['hp']) ?></td><td><?= $h($r['dame']) ?></td>
        <td><?= $h($r['appearTime']) ?></td><td><?= $h($r['mapAppear']) ?></td>
        <td><?= $h($r['head']) ?>/<?= $h($r['body']) ?>/<?= $h($r['leg']) ?></td>
        <td class="text-end"><button class="btn btn-sm btn-info text-white" data-bs-toggle="modal" data-bs-target="#e<?= $r['id'] ?>">Sua</button></td>
      </tr>
      <div class="modal fade" id="e<?= $r['id'] ?>" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-lg">
        <form method="post" class="modal-content"><div class="modal-header"><h5 class="modal-title">Boss #<?= $h($r['id']) ?></h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
        <div class="modal-body"><input type="hidden" name="action" value="update"><input type="hidden" name="id" value="<?= $h($r['id']) ?>">
          <div class="row g-2">
            <div class="col-md-6"><label class="form-label">Ten</label><input class="form-control" name="name" value="<?= $h($r['name']) ?>"></div>
            <div class="col-md-6"><label class="form-label">Thoi gian hoi sinh</label><input class="form-control" name="appearTime" value="<?= $h($r['appearTime']) ?>"></div>
            <div class="col-md-6"><label class="form-label">HP (so lon, dang text)</label><input class="form-control" name="hp" value="<?= $h($r['hp']) ?>"></div>
            <div class="col-md-6"><label class="form-label">Dame</label><input class="form-control" name="dame" value="<?= $h($r['dame']) ?>"></div>
            <div class="col-md-6"><label class="form-label">Map xuat hien {id,id...}</label><input class="form-control" name="mapAppear" value="<?= $h($r['mapAppear']) ?>"></div>
            <div class="col-md-2"><label class="form-label">Head</label><input class="form-control" type="number" name="head" value="<?= $h($r['head']) ?>"></div>
            <div class="col-md-2"><label class="form-label">Body</label><input class="form-control" type="number" name="body" value="<?= $h($r['body']) ?>"></div>
            <div class="col-md-2"><label class="form-label">Leg</label><input class="form-control" type="number" name="leg" value="<?= $h($r['leg']) ?>"></div>
            <div class="col-12"><label class="form-label">Drop JSON [{id,quantity,options:[{id,param}]}]</label>
            <textarea class="form-control json" rows="6" name="dropItems"><?= $h($r['dropItems']) ?></textarea></div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-outline-danger me-auto" name="action" value="delete" onclick="return confirm('Xoa boss nay?')">Xoa</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Dong</button>
          <button type="submit" class="btn btn-primary" name="action" value="update">Luu</button>
        </div></form>
      </div></div>
    <?php endforeach; ?>
    </tbody>
  </table></div>
  <nav><ul class="pagination"><?php for($i=1;$i<=$pages;$i++): ?><li class="page-item <?= $i===$page?'active':'' ?>"><a class="page-link" href="?q=<?= urlencode($q) ?>&page=<?= $i ?>"><?= $i ?></a></li><?php endfor; ?></ul></nav>
</div>
<div class="modal fade" id="createModal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-lg">
  <form method="post" class="modal-content"><div class="modal-header"><h5 class="modal-title">Them boss</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
  <div class="modal-body"><input type="hidden" name="action" value="create">
    <div class="row g-2">
      <div class="col-md-6"><label class="form-label">Ten</label><input class="form-control" name="name" required></div>
      <div class="col-md-6"><label class="form-label">Thoi gian</label><input class="form-control" name="appearTime" value="10 giay"></div>
      <div class="col-md-6"><label class="form-label">HP</label><input class="form-control" name="hp" value="1000000"></div>
      <div class="col-md-6"><label class="form-label">Dame</label><input class="form-control" name="dame" value="10000"></div>
      <div class="col-md-6"><label class="form-label">Map</label><input class="form-control" name="mapAppear" value="{0}"></div>
      <div class="col-md-2"><label class="form-label">Head</label><input class="form-control" type="number" name="head" value="0"></div>
      <div class="col-md-2"><label class="form-label">Body</label><input class="form-control" type="number" name="body" value="0"></div>
      <div class="col-md-2"><label class="form-label">Leg</label><input class="form-control" type="number" name="leg" value="0"></div>
      <div class="col-12"><label class="form-label">Drop JSON</label><textarea class="form-control json" rows="4" name="dropItems">[]</textarea></div>
    </div>
  </div>
  <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Dong</button><button class="btn btn-primary">Tao</button></div></form>
</div></div>
<?php include __DIR__ . '/includes/footer.php'; ?>
