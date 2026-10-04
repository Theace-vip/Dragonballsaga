<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/auth.php';
include __DIR__ . '/includes/header.php';
include __DIR__ . '/includes/sidebar.php';
$msg=''; $err='';
if ($_SERVER['REQUEST_METHOD']==='POST') {
  try {
    $act=$_POST['action']??'';
    if ($act==='shop_update') {
      db()->prepare("UPDATE shop SET npc_id=?,tag_name=?,type_shop=? WHERE id=?")->execute([(int)$_POST['npc_id'],$_POST['tag_name'],(int)$_POST['type_shop'],(int)$_POST['id']]);
      $msg='Da luu shop #'.(int)$_POST['id']; write_log('shop','shop_update','#'.(int)$_POST['id']);
    } elseif ($act==='tab_update') {
      $items=trim($_POST['items']??'');
      if ($items!=='' && !is_valid_json($items)) throw new Exception('items khong phai JSON hop le');
      db()->prepare("UPDATE tab_shop SET tab_name=?,tab_index=?,items=? WHERE id=?")->execute([$_POST['tab_name'],(int)$_POST['tab_index'],$items,(int)$_POST['id']]);
      $msg='Da luu tab #'.(int)$_POST['id']; write_log('shop','tab_update','#'.(int)$_POST['id']);
    } elseif ($act==='create_shop') {
      $npc=(int)$_POST['npc_id'];
      $tag=trim($_POST['tag_name']);
      $name=trim($_POST['name']??$tag);
      $type=(int)$_POST['type_shop'];
      $items=trim($_POST['items']??'[]');
      if ($tag==='') throw new Exception('tag_name khong duoc trong');
      if ($items!=='' && !is_valid_json($items)) throw new Exception('items khong phai JSON hop le');
      $st=db()->prepare("INSERT INTO shop(npc_id,tag_name,type_shop,panel_created) VALUES(?,?,?,1)");
      $st->execute([$npc,$tag,$type]);
      $shopId=db()->lastInsertId();
      db()->prepare("INSERT INTO tab_shop(shop_id,tab_name,tab_index,items,panel_created) VALUES(?,?,0,?,1)")
        ->execute([$shopId,$name,$items]);
      $msg='Da tao shop #'.$shopId.' (npc '.$npc.', tag '.$tag.')'; write_log('shop','create_shop','#'.$shopId);
    } elseif ($act==='add_items') {
      $shopId=(int)$_POST['shop_id'];
      $idx=(int)$_POST['tab_index'];
      $add=trim($_POST['items']??'[]');
      if (!is_valid_json($add)) throw new Exception('items khong hop le');
      $cur=db()->prepare("SELECT items FROM tab_shop WHERE shop_id=? AND tab_index=?");
      $cur->execute([$shopId,$idx]);
      $row=$cur->fetch();
      $merged='[';
      if ($row && $row['items']) { $c=trim($row['items']); if (strpos($c,'[')===0) $merged.=substr($c,1); }
      $a=trim($add); if (strpos($a,'[')===0) $a=substr($a,1); if (strpos($a,']')===strlen($a)-1) $a=substr($a,0,-1);
      if (strlen($merged)>1 && $a!=='') $merged.=',';
      $merged.=$a.']';
      db()->prepare("UPDATE tab_shop SET items=? WHERE shop_id=? AND tab_index=?")->execute([$merged,$shopId,$idx]);
      $msg='Da them vat pham vao shop #'.$shopId; write_log('shop','add_items','#'.$shopId);
    }
  } catch (Exception $e) { $err=$e->getMessage(); }
}
try { $shops=db()->query("SELECT * FROM shop ORDER BY id")->fetchAll(); } catch (Exception $e) { $shops=[]; }
try { $tabs=db()->query("SELECT t.*, s.tag_name AS shop_name FROM tab_shop t LEFT JOIN shop s ON s.id=t.shop_id ORDER BY t.shop_id, t.tab_index")->fetchAll(); } catch (Exception $e) { $tabs=[]; }
$h=fn($v)=>htmlspecialchars((string)($v??''));
?>
<div class="container-fluid p-4">
  <h3>Shop</h3>
  <p class="text-muted small">Chi sua shop/tab. Khong dong vao shop_ky_gui cua nguoi choi. Luu xong restart (hoac go lenh shop trong console Java).</p>
  <?php if($msg): ?><div class="alert alert-success"><?= $h($msg) ?></div><?php endif; ?>
  <?php if($err): ?><div class="alert alert-danger"><?= $h($err) ?></div><?php endif; ?>
  <div class="card p-3 mb-3"><h5>0. Tao shop moi (tu NPC co san)</h5>
    <form method="post" class="row g-2 align-items-end">
      <input type="hidden" name="action" value="create_shop">
      <div class="col-md-1"><label class="form-label">NPC ID</label><input class="form-control form-control-sm" type="number" name="npc_id" value="0" required></div>
      <div class="col-md-2"><label class="form-label">Tag (ShopService.opendShop)</label><input class="form-control form-control-sm" name="tag_name" placeholder="SHOP_MOI_1" required></div>
      <div class="col-md-2"><label class="form-label">Ten shop</label><input class="form-control form-control-sm" name="name" placeholder="Shop VIP"></div>
      <div class="col-md-1"><label class="form-label">Type</label><input class="form-control form-control-sm" type="number" name="type_shop" value="0"></div>
      <div class="col-md-6"><label class="form-label">Items JSON [{temp_id,cost,item_spec,type_sell,is_sell,options:[{id,param}]}]</label>
        <textarea class="form-control form-control-sm" rows="2" name="items" placeholder='[{"temp_id":14,"cost":100,"item_spec":0,"type_sell":0,"is_sell":true,"options":[]}]'></textarea></div>
      <div class="col-md-12"><button class="btn btn-success btn-sm">Tao shop (tạo nút/button mới gắn NPC)</button>
        <small class="text-muted">type_sell: 0=thỏi vàng, 1=ngọc xanh, 3=rubi, 4=lượng vàng, 5=lượng bạc, 6=cỏ 4 lá.</small></div>
    </form>
  </div>

  <div class="card p-3 mb-3"><h5>0b. Tien te shop (panel_shop_currency)</h5>
    <?php
    $cur=[]; try { $cur=db()->query("SELECT * FROM panel_shop_currency ORDER BY type_sell")->fetchAll(); } catch(Exception $e){}
    $ctypes=[0=>'Thỏi vàng',1=>'Ngọc xanh',3=>'Rubi',4=>'Lượng vàng',5=>'Lượng bạc',6=>'Cỏ 4 lá'];
    ?>
    <table class="table table-sm table-bordered"><thead><tr><th>type_sell</th><th>Tên</th></tr></thead><tbody>
    <?php foreach($ctypes as $k=>$v): ?>
      <tr><td><?= $k ?></td><td><?= $h($v) ?></td></tr>
    <?php endforeach; ?>
    </tbody></table>
    <?php if(empty($cur)): ?><small class="text-muted">Bang panel_shop_currency chua duoc tao (chay panel_ext.sql).</small><?php endif; ?>
  </div>

  <div class="card p-3 mb-3"><h5>1. Gian hang (shop)</h5>
  <div class="table-responsive"><table class="table table-sm table-striped align-middle">
    <thead><tr><th>ID</th><th>NPC</th><th>Tag</th><th>Type</th><th class="text-end">Luu</th></tr></thead><tbody>
    <?php foreach($shops as $s): ?>
    <tr><form method="post"><input type="hidden" name="action" value="shop_update"><input type="hidden" name="id" value="<?= $h($s['id']) ?>">
      <td><?= $h($s['id']) ?></td>
      <td><input class="form-control form-control-sm" type="number" name="npc_id" value="<?= $h($s['npc_id']) ?>"></td>
      <td><input class="form-control form-control-sm" name="tag_name" value="<?= $h($s['tag_name']) ?>"></td>
      <td><input class="form-control form-control-sm" type="number" name="type_shop" value="<?= $h($s['type_shop']) ?>"></td>
      <td class="text-end"><button class="btn btn-sm btn-primary">Luu</button></td>
    </form></tr>
    <?php endforeach; ?>
    </tbody></table></div></div>
  <div class="card p-3 mb-3"><h5>2. Tab &amp; vat pham (tab_shop)</h5>
  <?php foreach($tabs as $t): ?>
    <form method="post" class="border rounded p-2 mb-2"><input type="hidden" name="action" value="tab_update"><input type="hidden" name="id" value="<?= $h($t['id']) ?>">
      <div class="row g-2 align-items-end">
        <div class="col-md-1"><label class="form-label">ID</label><div><b><?= $h($t['id']) ?></b></div><small class="text-muted"><?= $h($t['shop_name']) ?></small></div>
        <div class="col-md-3"><label class="form-label">Ten tab</label><input class="form-control form-control-sm" name="tab_name" value="<?= $h($t['tab_name']) ?>"></div>
        <div class="col-md-1"><label class="form-label">Index</label><input class="form-control form-control-sm" type="number" name="tab_index" value="<?= $h($t['tab_index']) ?>"></div>
        <div class="col-md-6"><label class="form-label">Items JSON [{temp_id,is_new,cost,item_spec,type_sell,is_sell,options}]</label>
        <textarea class="form-control form-control-sm json" rows="3" name="items"><?= $h($t['items']) ?></textarea></div>
        <div class="col-md-1"><button class="btn btn-sm btn-primary">Luu</button></div>
      </div>
    </form>
  <?php endforeach; ?>
  </div>
</div>
<?php include __DIR__ . '/includes/footer.php'; ?>
