<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
ensure_panel_tables();
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';
$msg=''; $err='';
if ($_SERVER['REQUEST_METHOD']==='POST' && ($_POST['form']??'')==='rates') {
  $d=['damePrecent'=>(int)$_POST['damePrecent'],'hpPrecent'=>(int)$_POST['hpPrecent'],'mpPrecent'=>(int)$_POST['mpPrecent'],'papPrecent'=>(int)$_POST['papPrecent']];
  try {
    $json=json_encode($d);
    $c=db()->query("SELECT COUNT(*) FROM `event` WHERE `name`='international_womens_day'")->fetchColumn();
    if ($c) db()->prepare("UPDATE `event` SET `data`=? WHERE `name`='international_womens_day'")->execute([$json]);
    else db()->prepare("INSERT INTO `event` (id,name,`data`) VALUES (1,'international_womens_day',?)")->execute([$json]);
    $msg='Da luu ty le su kien. Restart server de ap dung.'; write_log('event','save_rates',$json);
  } catch (Exception $e) { $err=$e->getMessage(); }
}
if ($_SERVER['REQUEST_METHOD']==='POST' && ($_POST['form']??'')==='flags') {
  try {
    foreach ($_POST['flag'] ?? [] as $k=>$v) {
      if (!preg_match('/^[A-Z_]+$/',$k)) continue;
      db()->prepare("UPDATE panel_event_flags SET enabled=? WHERE key_name=?")->execute([$v?1:0,$k]);
    }
    // cac checkbox tat khong gui len -> set 0
    $all=db()->query("SELECT key_name FROM panel_event_flags")->fetchAll(PDO::FETCH_COLUMN);
    foreach ($all as $k) if (!isset($_POST['flag'][$k])) db()->prepare("UPDATE panel_event_flags SET enabled=0 WHERE key_name=?")->execute([$k]);
    $msg='Da luu co su kien. Luu y: Java hien hard-code trong EventManager.java, can restart (dot sau se patch doc bang nay).';
    write_log('event','save_flags',json_encode($_POST['flag']??[]));
  } catch (Exception $e) { $err=$e->getMessage(); }
}
try { $r=db()->query("SELECT `data` FROM `event` WHERE `name`='international_womens_day'")->fetch(); $ev=json_decode($r['data']??'{}',true)?:[]; } catch (Exception $e) { $ev=[]; }
try { $flags=db()->query("SELECT * FROM panel_event_flags ORDER BY key_name")->fetchAll(); } catch (Exception $e) { $flags=[]; }
$v=fn($k)=>htmlspecialchars($ev[$k]??0);
?>
<div class="container-fluid p-4">
  <h3>Su kien &amp; Ty le</h3>
  <?php if($msg): ?><div class="alert alert-success"><?= htmlspecialchars($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= htmlspecialchars($err) ?></div><?php endif; ?>
  <div class="card p-3 mb-3">
    <h5>1. Ty le su kien (bang event - international_womens_day)</h5>
    <p class="text-muted small">Khop EventDAO.java: damePrecent, hpPrecent, mpPrecent, papPrecent.</p>
    <form method="post" class="row g-2"><input type="hidden" name="form" value="rates">
      <div class="col-md-3"><label class="form-label">Dame %</label><input class="form-control" type="number" name="damePrecent" value="<?= $v('damePrecent') ?>"></div>
      <div class="col-md-3"><label class="form-label">HP %</label><input class="form-control" type="number" name="hpPrecent" value="<?= $v('hpPrecent') ?>"></div>
      <div class="col-md-3"><label class="form-label">MP %</label><input class="form-control" type="number" name="mpPrecent" value="<?= $v('mpPrecent') ?>"></div>
      <div class="col-md-3"><label class="form-label">Tiem nang &amp; SM %</label><input class="form-control" type="number" name="papPrecent" value="<?= $v('papPrecent') ?>"></div>
      <div class="col-12"><button class="btn btn-primary mt-2">Luu ty le</button></div>
    </form>
  </div>
  <div class="card p-3 mb-3">
    <h5>2. Bat / tat su kien (bang panel_event_flags)</h5>
    <form method="post"><input type="hidden" name="form" value="flags">
    <table class="table table-sm"><tbody>
      <?php foreach($flags as $f): ?>
      <tr><td><b><?= htmlspecialchars($f['key_name']) ?></b><br><small class="text-muted"><?= htmlspecialchars($f['note']??'') ?></small></td>
      <td class="text-end"><div class="form-check form-switch d-inline-block">
        <input class="form-check-input" type="checkbox" name="flag[<?= htmlspecialchars($f['key_name']) ?>]" value="1" <?= $f['enabled']?'checked':'' ?>>
      </div></td></tr>
      <?php endforeach; ?>
    </tbody></table>
    <button class="btn btn-primary">Luu co su kien</button>
    </form>
  </div>
</div>
<?php include __DIR__ . '/includes/footer.php'; ?>
