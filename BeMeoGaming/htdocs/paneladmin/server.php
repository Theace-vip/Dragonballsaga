<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';

$CFG_PATH = realpath(__DIR__ . '/../../data/config/config.properties');
if (!$CFG_PATH) $CFG_PATH = __DIR__ . '/../../data/config/config.properties';
$msg = ''; $err = '';

function read_props($path) {
  $out = [];
  if (is_file($path)) foreach (file($path, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES) as $l) {
    $l = trim($l); if ($l === '' || $l[0] === '#') continue;
    $p = strpos($l, '='); if ($p === false) continue;
    $out[trim(substr($l,0,$p))] = trim(substr($l,$p+1));
  }
  return $out;
}
function write_props($path, $arr) {
  @copy($path, $path . '.bak.' . date('YmdHis'));
  $s = "# Sua tu paneladmin " . date('Y-m-d H:i:s') . "\n";
  foreach ($arr as $k => $v) $s .= "$k=$v\n";
  return @file_put_contents($path, $s) !== false;
}

$props = read_props($CFG_PATH);
if ($_SERVER['REQUEST_METHOD'] === 'POST' && ($_POST['form'] ?? '') === 'props') {
  $allow = ['server.sv','server.name','server.port','server.waitlogin','server.maxperip','server.maxplayer','server.expserver','server.local','server.test','server.daoautoupdater','server.debug'];
  // PHP tu doi dau . thanh _ trong ten input (server.expserver -> server_expserver) nen phai doc ca 2 kieu
  foreach ($allow as $k) {
    $kU = str_replace('.', '_', $k);
    if (isset($_POST[$k])) $props[$k] = trim($_POST[$k]);
    elseif (isset($_POST[$kU])) $props[$k] = trim($_POST[$kU]);
  }
  if (write_props($CFG_PATH, $props)) { $msg = 'Da luu config.properties (co backup .bak). Restart server Java de ap dung.'; write_log('server','save_props',''); }
  else $err = 'Khong ghi duoc file config.properties. Kiem tra quyen ghi cho Apache/XAMPP.';
}
if ($_SERVER['REQUEST_METHOD'] === 'POST' && ($_POST['form'] ?? '') === 'adminpanel') {
  $f = ['domain'=>$_POST['domain']??'','logo'=>$_POST['logo']??'','trangthai'=>$_POST['trangthai']??'','android'=>$_POST['android']??'','iphone'=>$_POST['iphone']??'','windows'=>$_POST['windows']??'','java'=>$_POST['java']??''];
  try {
    $c = db()->query("SELECT COUNT(*) FROM adminpanel")->fetchColumn();
    if ($c) db()->prepare("UPDATE adminpanel SET domain=?,logo=?,trangthai=?,android=?,iphone=?,windows=?,`java`=?")->execute(array_values($f));
    else db()->prepare("INSERT INTO adminpanel (domain,logo,trangthai,android,iphone,windows,`java`) VALUES (?,?,?,?,?,?,?)")->execute(array_values($f));
    $msg = 'Da luu bang adminpanel.'; write_log('server','save_adminpanel',json_encode($f, JSON_UNESCAPED_UNICODE));
  } catch (Exception $e) { $err = 'Loi adminpanel: '.$e->getMessage(); }
}
if ($_SERVER['REQUEST_METHOD'] === 'POST' && ($_POST['form'] ?? '') === 'settings') {
  try {
    $f = ['Title'=>$_POST['Title']??'','ServerName'=>$_POST['ServerName']??'','Fanpage'=>$_POST['Fanpage']??'','Group'=>$_POST['Group']??'','Zalo'=>$_POST['Zalo']??'','SiteKey'=>$_POST['SiteKey']??'','SecretKey'=>$_POST['SecretKey']??''];
    $sets = []; foreach ($f as $k=>$v) $sets[] = "`$k`=?";
    db()->prepare("UPDATE settings SET ".implode(',',$sets))->execute(array_values($f));
    $msg = 'Da luu bang settings.'; write_log('server','save_settings',json_encode($f, JSON_UNESCAPED_UNICODE));
  } catch (Exception $e) { $err = 'Loi settings: '.$e->getMessage(); }
}
try { $ap = db()->query("SELECT * FROM adminpanel LIMIT 1")->fetch() ?: []; } catch (Exception $e) { $ap = []; }
try { $st = db()->query("SELECT * FROM settings LIMIT 1")->fetch() ?: []; } catch (Exception $e) { $st = []; }
$g = fn($a,$k,$d='') => isset($a[$k]) ? htmlspecialchars($a[$k]) : $d;
?>
<div class="container-fluid p-4">
  <h3>Thong so Server</h3>
  <?php if($msg): ?><div class="alert alert-success"><?= htmlspecialchars($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= htmlspecialchars($err) ?></div><?php endif; ?>
  <div class="card p-3 mb-3">
    <h5>1. File cau hinh Java <small class="text-muted"><?= htmlspecialchars($CFG_PATH) ?></small></h5>
    <p class="text-muted small">Exp, gioi han nguoi choi, cong... nam o day. Luu xong restart server Java.</p>
    <form method="post"><input type="hidden" name="form" value="props">
    <div class="row g-2">
      <?php $fields=['server.expserver'=>'Ty le EXP','server.maxplayer'=>'Max player','server.maxperip'=>'Max/IP','server.waitlogin'=>'Wait login (s)','server.sv'=>'Server ID','server.name'=>'Ten server','server.port'=>'Port','server.local'=>'local (true/false)','server.test'=>'test (true/false)','server.daoautoupdater'=>'daoautoupdater','server.debug'=>'debug']; ?>
      <?php foreach($fields as $k=>$lb): ?>
      <div class="col-md-3"><label class="form-label"><?= $lb ?><br><code><?= $k ?></code></label>
      <input class="form-control" name="<?= $k ?>" value="<?= $g($props,$k) ?>"></div>
      <?php endforeach; ?>
    </div>
    <button class="btn btn-primary mt-3">Luu file cau hinh</button>
    </form>
  </div>
  <div class="card p-3 mb-3">
    <h5>2. Bang adminpanel (trang thai, link tai)</h5>
    <form method="post"><input type="hidden" name="form" value="adminpanel">
    <div class="row g-2">
      <div class="col-md-3"><label class="form-label">Trang thai</label>
        <select class="form-select" name="trangthai">
          <option value="hoatdong" <?= ($ap['trangthai']??'')==='hoatdong'?'selected':'' ?>>hoatdong</option>
          <option value="baotri" <?= ($ap['trangthai']??'')==='baotri'?'selected':'' ?>>baotri</option>
        </select></div>
      <div class="col-md-9"><label class="form-label">Domain</label><input class="form-control" name="domain" value="<?= $g($ap,'domain') ?>"></div>
      <div class="col-md-6"><label class="form-label">Logo</label><input class="form-control" name="logo" value="<?= $g($ap,'logo') ?>"></div>
      <div class="col-md-6"><label class="form-label">Android</label><input class="form-control" name="android" value="<?= $g($ap,'android') ?>"></div>
      <div class="col-md-4"><label class="form-label">iPhone</label><input class="form-control" name="iphone" value="<?= $g($ap,'iphone') ?>"></div>
      <div class="col-md-4"><label class="form-label">Windows</label><input class="form-control" name="windows" value="<?= $g($ap,'windows') ?>"></div>
      <div class="col-md-4"><label class="form-label">Java</label><input class="form-control" name="java" value="<?= $g($ap,'java') ?>"></div>
    </div>
    <button class="btn btn-primary mt-3">Luu adminpanel</button>
    </form>
  </div>
  <div class="card p-3 mb-3">
    <h5>3. Bang settings (ten web, fanpage, captcha)</h5>
    <form method="post"><input type="hidden" name="form" value="settings">
    <div class="row g-2">
      <div class="col-md-6"><label class="form-label">Title</label><input class="form-control" name="Title" value="<?= $g($st,'Title') ?>"></div>
      <div class="col-md-6"><label class="form-label">ServerName</label><input class="form-control" name="ServerName" value="<?= $g($st,'ServerName') ?>"></div>
      <div class="col-md-4"><label class="form-label">Fanpage</label><input class="form-control" name="Fanpage" value="<?= $g($st,'Fanpage') ?>"></div>
      <div class="col-md-4"><label class="form-label">Group</label><input class="form-control" name="Group" value="<?= $g($st,'Group') ?>"></div>
      <div class="col-md-4"><label class="form-label">Zalo</label><input class="form-control" name="Zalo" value="<?= $g($st,'Zalo') ?>"></div>
      <div class="col-md-6"><label class="form-label">SiteKey</label><input class="form-control" name="SiteKey" value="<?= $g($st,'SiteKey') ?>"></div>
      <div class="col-md-6"><label class="form-label">SecretKey</label><input class="form-control" name="SecretKey" value="<?= $g($st,'SecretKey') ?>"></div>
    </div>
    <button class="btn btn-primary mt-3">Luu settings</button>
    </form>
  </div>
</div>
<?php include __DIR__ . '/includes/footer.php'; ?>
