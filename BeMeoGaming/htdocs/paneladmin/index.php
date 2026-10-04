<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
ensure_panel_tables();
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';

function panel_count($sql) { try { return (int)db()->query($sql)->fetchColumn(); } catch (Exception $e) { return 0; } }
$nBoss = panel_count("SELECT COUNT(*) FROM listbosses");
$nGift = panel_count("SELECT COUNT(*) FROM giftcode");
$nCaption = panel_count("SELECT COUNT(*) FROM caption");
$nPower = panel_count("SELECT COUNT(*) FROM power_limit");
$nFlags = 0; $flags = [];
try { $flags = db()->query("SELECT * FROM panel_event_flags ORDER BY key_name")->fetchAll(); foreach ($flags as $f) if ($f['enabled']) $nFlags++; } catch (Exception $e) {}
$evJson = '';
try { $r = db()->query("SELECT `data` FROM `event` WHERE `name`='international_womens_day'")->fetch(); $evJson = $r['data'] ?? ''; } catch (Exception $e) {}
$ev = json_decode($evJson, true) ?: [];
?>
<div class="container-fluid p-4">
  <h3 class="mb-1">Tong quan server</h3>
  <p class="text-muted">DB: <b><?= htmlspecialchars(panel_dbname()) ?></b> - Moi thay doi luu MySQL, restart server Java de ap dung.</p>
  <div class="row g-3 mb-4">
    <div class="col-md-3"><div class="card card-stat p-3"><div class="text-muted">Boss</div><h2><?= $nBoss ?></h2><a href="bosses.php">Quan ly Boss</a></div></div>
    <div class="col-md-3"><div class="card card-stat p-3"><div class="text-muted">Su kien dang bat</div><h2><?= $nFlags ?></h2><a href="events.php">Bat/tat su kien</a></div></div>
    <div class="col-md-3"><div class="card card-stat p-3"><div class="text-muted">Moc suc manh</div><h2><?= $nCaption ?> / <?= $nPower ?></h2><a href="power.php">Chinh suc manh</a></div></div>
    <div class="col-md-3"><div class="card card-stat p-3"><div class="text-muted">Giftcode</div><h2><?= $nGift ?></h2><a href="giftcode.php">Quan ly code</a></div></div>
  </div>
  <div class="row g-3">
    <div class="col-md-6"><div class="card p-3">
      <h5>Ty le su kien hien tai</h5>
      <table class="table table-sm"><tbody>
        <tr><td>Dame %</td><td><b><?= htmlspecialchars($ev['damePrecent'] ?? 0) ?></b></td></tr>
        <tr><td>HP %</td><td><b><?= htmlspecialchars($ev['hpPrecent'] ?? 0) ?></b></td></tr>
        <tr><td>MP %</td><td><b><?= htmlspecialchars($ev['mpPrecent'] ?? 0) ?></b></td></tr>
        <tr><td>Tiem nang &amp; Suc manh %</td><td><b><?= htmlspecialchars($ev['papPrecent'] ?? 0) ?></b></td></tr>
      </tbody></table>
      <a class="btn btn-sm btn-primary" href="events.php">Doi ty le</a>
    </div></div>
    <div class="col-md-6"><div class="card p-3">
      <h5>Co su kien</h5>
      <table class="table table-sm"><tbody>
      <?php foreach ($flags as $f): ?>
        <tr><td><?= htmlspecialchars($f['key_name']) ?></td>
        <td><?= $f['enabled'] ? '<span class="badge bg-success">BAT</span>' : '<span class="badge bg-secondary">TAT</span>' ?></td></tr>
      <?php endforeach; ?>
      </tbody></table>
      <div class="mt-2">
        <a class="btn btn-sm btn-outline-primary" href="server.php">Thong so server</a>
        <a class="btn btn-sm btn-outline-primary" href="shop.php">Shop</a>
      </div>
      <hr>
      <h6>API dung lai</h6>
      <code>api/server.php, api/bosses.php, api/events.php, api/power.php, api/giftcode.php, api/shop.php</code>
      <p class="text-muted small mt-1">Tra JSON {ok, data, error}. Xem mau trong thu muc api/.</p>
    </div></div>
  </div>
</div>
<?php include __DIR__ . '/includes/footer.php'; ?>
