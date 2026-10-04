<?php
require_once __DIR__ . '/_common.php';
// GET ?table=adminpanel|settings | POST table=adminpanel|settings + fields
try {
  $m = $_SERVER['REQUEST_METHOD'];
  if ($m === 'GET') {
    $t = $_GET['table'] ?? 'adminpanel';
    if (!in_array($t, ['adminpanel','settings'])) api_err('table khong hop le');
    api_ok(db()->query("SELECT * FROM `$t` LIMIT 1")->fetch());
  }
  $in = api_input(); $t = $in['table'] ?? 'adminpanel';
  if (!in_array($t, ['adminpanel','settings'])) api_err('table khong hop le');
  unset($in['table']);
  $sets = []; $vals = [];
  foreach ($in as $k=>$v) { if (!preg_match('/^[a-zA-Z0-9_]+$/',$k)) continue; $sets[]="`$k`=?"; $vals[]=$v; }
  if (!$sets) api_err('khong co field');
  $c = db()->query("SELECT COUNT(*) FROM `$t`")->fetchColumn();
  if ($c) db()->prepare("UPDATE `$t` SET ".implode(',',$sets))->execute($vals);
  else { $cols=implode(',',array_map(fn($s)=>explode('=',$s)[0],$sets)); db()->prepare("INSERT INTO `$t` ($cols) VALUES (".implode(',',array_fill(0,count($vals),'?')).")")->execute($vals); }
  write_log('server','api_save_'.$t,json_encode($in,JSON_UNESCAPED_UNICODE));
  api_ok(['saved'=>true]);
} catch (Exception $e) { api_err($e->getMessage(),500); }
