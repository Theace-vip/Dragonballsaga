<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';
$msg=''; $err='';
$tab=$_GET['tab'] ?? 'caption';
if ($_SERVER['REQUEST_METHOD']==='POST') {
  try {
    $act=$_POST['action']??'';
    if ($act==='cap_update') {
      db()->prepare("UPDATE caption SET earth=?,saiya=?,namek=?,power=? WHERE id=?")->execute([$_POST['earth'],$_POST['saiya'],$_POST['namek'],$_POST['power'],(int)$_POST['id']]);
      $msg='Da luu caption #'.(int)$_POST['id']; write_log('power','cap_update','#'.(int)$_POST['id']);
    } elseif ($act==='pl_update') {
      db()->prepare("UPDATE power_limit SET power=?,hp=?,mp=?,damage=?,defense=?,critical=? WHERE id=?")
        ->execute([$_POST['power'],$_POST['hp'],$_POST['mp'],$_POST['damage'],$_POST['defense'],(int)$_POST['critical'],(int)$_POST['id']]);
      $msg='Da luu power_limit #'.(int)$_POST['id']; write_log('power','pl_update','#'.(int)$_POST['id']);
    } elseif ($act==='pl_scale') {
      $f=(float)$_POST['factor'];
      if ($f<=0||$f>1000) throw new Exception('He so khong hop le (0-1000)');
      db()->exec("UPDATE power_limit SET hp=hp*$f, mp=mp*$f, damage=damage*$f, defense=defense*$f");
      $msg="Da nhan tat ca HP/MP/Dame/Def x$f"; write_log('power','pl_scale','x'.$f);
    }
  } catch (Exception $e) { $err=$e->getMessage(); }
}
$caps=[]; $pls=[];
try { $caps=db()->query("SELECT * FROM caption ORDER BY id")->fetchAll(); } catch (Exception $e) {}
try { $pls=db()->query("SELECT * FROM power_limit ORDER BY id")->fetchAll(); } catch (Exception $e) {}
$h=fn($v)=>htmlspecialchars((string)($v??''));
?>
<div class="container-fluid p-4">
  <h3>Tiem nang &amp; Suc manh</h3>
  <?php if($msg): ?><div class="alert alert-success"><?= $h($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= $h($err) ?></div><?php endif; ?>
  <ul class="nav nav-tabs mb-3">
    <li class="nav-item"><a class="nav-link <?= $tab==='caption'?'active':'' ?>" href="?tab=caption">Caption (danh hieu)</a></li>
    <li class="nav-item"><a class="nav-link <?= $tab==='power'?'active':'' ?>" href="?tab=power">Power Limit (gioi han chi so)</a></li>
  </ul>
  <?php if($tab==='caption'): ?>
  <div class="table-responsive"><table class="table table-striped table-hover align-middle">
    <thead><tr><th>ID</th><th>Trai Dat</th><th>Xayda</th><th>Namek</th><th>Power moc</th><th class="text-end">Luu</th></tr></thead><tbody>
    <?php foreach($caps as $c): ?>
    <tr><form method="post"><input type="hidden" name="action" value="cap_update"><input type="hidden" name="id" value="<?= $h($c['id']) ?>">
      <td><?= $h($c['id']) ?></td>
      <td><input class="form-control form-control-sm" name="earth" value="<?= $h($c['earth']) ?>"></td>
      <td><input class="form-control form-control-sm" name="saiya" value="<?= $h($c['saiya']) ?>"></td>
      <td><input class="form-control form-control-sm" name="namek" value="<?= $h($c['namek']) ?>"></td>
      <td><input class="form-control form-control-sm" name="power" value="<?= $h($c['power']) ?>"></td>
      <td class="text-end"><button class="btn btn-sm btn-primary">Luu</button></td>
    </form></tr>
    <?php endforeach; ?>
    </tbody></table></div>
  <?php else: ?>
    <form method="post" class="row g-2 mb-3"><input type="hidden" name="action" value="pl_scale">
      <div class="col-md-3"><input class="form-control" type="number" step="0.1" name="factor" placeholder="He so, vd 2"></div>
      <div class="col-md-9"><button class="btn btn-warning" onclick="return confirm('Nhan toan bo HP/MP/Dame/Def?')">Nhan hang loat HP/MP/Dame/Def</button></div>
    </form>
    <div class="table-responsive"><table class="table table-striped table-hover align-middle">
    <thead><tr><th>ID</th><th>Power</th><th>HP</th><th>MP</th><th>Dame</th><th>Def</th><th>Crit</th><th class="text-end">Luu</th></tr></thead><tbody>
    <?php foreach($pls as $p): ?>
    <tr><form method="post"><input type="hidden" name="action" value="pl_update"><input type="hidden" name="id" value="<?= $h($p['id']) ?>">
      <td><?= $h($p['id']) ?></td>
      <td><input class="form-control form-control-sm" name="power" value="<?= $h($p['power']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="hp" value="<?= $h($p['hp']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="mp" value="<?= $h($p['mp']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="damage" value="<?= $h($p['damage']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="defense" value="<?= $h($p['defense']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="critical" value="<?= $h($p['critical']) ?>"></td>
      <td class="text-end"><button class="btn btn-sm btn-primary">Luu</button></td>
    </form></tr>
    <?php endforeach; ?>
    </tbody></table></div>
  <?php endif; ?>
</div>
<?php include __DIR__ . '/includes/footer.php'; ?>
