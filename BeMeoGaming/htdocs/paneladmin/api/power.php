<?php
require_once __DIR__ . '/_common.php';
// GET ?table=caption|power_limit | POST action=cap_update|pl_update|pl_scale
try {
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $t=$_GET['table']??'caption';
    if (!in_array($t,['caption','power_limit'])) api_err('table khong hop le');
    api_ok(db()->query("SELECT * FROM `$t` ORDER BY id")->fetchAll());
  }
  $in=api_input(); $act=$in['action']??'';
  if ($act==='cap_update') {
    db()->prepare("UPDATE caption SET earth=?,saiya=?,namek=?,power=? WHERE id=?")->execute([$in['earth'],$in['saiya'],$in['namek'],$in['power'],(int)$in['id']]);
    write_log('power','api_cap','#'.(int)$in['id']); api_ok(['saved'=>true]);
  } elseif ($act==='pl_update') {
    db()->prepare("UPDATE power_limit SET power=?,hp=?,mp=?,damage=?,defense=?,critical=? WHERE id=?")
      ->execute([$in['power'],$in['hp'],$in['mp'],$in['damage'],$in['defense'],(int)$in['critical'],(int)$in['id']]);
    write_log('power','api_pl','#'.(int)$in['id']); api_ok(['saved'=>true]);
  } elseif ($act==='pl_scale') {
    $f=(float)($in['factor']??0);
    if ($f<=0||$f>1000) api_err('factor 0-1000');
    db()->exec("UPDATE power_limit SET hp=hp*$f, mp=mp*$f, damage=damage*$f, defense=defense*$f");
    write_log('power','api_scale','x'.$f); api_ok(['scaled'=>$f]);
  }
  api_err('action khong hop le');
} catch (Exception $e) { api_err($e->getMessage(),500); }
