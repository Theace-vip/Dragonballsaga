<?php
require_once __DIR__ . '/_common.php';
// GET ?view=shops|tabs | POST action=shop_update|tab_update
try {
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $v=$_GET['view']??'shops';
    if ($v==='tabs') api_ok(db()->query("SELECT t.*, s.tag_name AS shop_name FROM tab_shop t LEFT JOIN shop s ON s.id=t.shop_id ORDER BY t.shop_id,t.tab_index")->fetchAll());
    api_ok(db()->query("SELECT * FROM shop ORDER BY id")->fetchAll());
  }
  $in=api_input(); $act=$in['action']??'';
  if ($act==='shop_update') {
    db()->prepare("UPDATE shop SET npc_id=?,tag_name=?,type_shop=? WHERE id=?")->execute([(int)$in['npc_id'],$in['tag_name'],(int)$in['type_shop'],(int)$in['id']]);
    write_log('shop','api_shop','#'.(int)$in['id']); api_ok(['saved'=>true]);
  } elseif ($act==='tab_update') {
    require_json($in['items']??'','items');
    db()->prepare("UPDATE tab_shop SET tab_name=?,tab_index=?,items=? WHERE id=?")->execute([$in['tab_name'],(int)$in['tab_index'],$in['items'],(int)$in['id']]);
    write_log('shop','api_tab','#'.(int)$in['id']); api_ok(['saved'=>true]);
  }
  api_err('action khong hop le');
} catch (Exception $e) { api_err($e->getMessage(),500); }
