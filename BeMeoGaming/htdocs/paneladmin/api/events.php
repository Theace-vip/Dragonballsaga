<?php
require_once __DIR__ . '/_common.php';
// GET: ty le + co | POST action=rates|flag
try {
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    ensure_panel_tables();
    $r=db()->query("SELECT `data` FROM `event` WHERE `name`='international_womens_day'")->fetch();
    $flags=db()->query("SELECT * FROM panel_event_flags ORDER BY key_name")->fetchAll();
    api_ok(['rates'=>json_decode($r['data']??'{}',true),'flags'=>$flags]);
  }
  $in=api_input(); $act=$in['action']??'';
  if ($act==='rates') {
    $d=['damePrecent'=>(int)($in['damePrecent']??0),'hpPrecent'=>(int)($in['hpPrecent']??0),'mpPrecent'=>(int)($in['mpPrecent']??0),'papPrecent'=>(int)($in['papPrecent']??0)];
    $json=json_encode($d);
    $c=db()->query("SELECT COUNT(*) FROM `event` WHERE `name`='international_womens_day'")->fetchColumn();
    if ($c) db()->prepare("UPDATE `event` SET `data`=? WHERE `name`='international_womens_day'")->execute([$json]);
    else db()->prepare("INSERT INTO `event` (id,name,`data`) VALUES (1,'international_womens_day',?)")->execute([$json]);
    write_log('event','api_rates',$json); api_ok($d);
  } elseif ($act==='flag') {
    if (!preg_match('/^[A-Z_]+$/',$in['key']??'')) api_err('key khong hop le');
    db()->prepare("UPDATE panel_event_flags SET enabled=? WHERE key_name=?")->execute([(int)($in['enabled']??0),$in['key']]);
    write_log('event','api_flag',$in['key'].'='.(int)($in['enabled']??0)); api_ok(['saved'=>true]);
  }
  api_err('action khong hop le');
} catch (Exception $e) { api_err($e->getMessage(),500); }
