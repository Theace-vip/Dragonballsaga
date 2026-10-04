<?php
require_once __DIR__ . '/_common.php';
// GET ?id= | GET list | POST action=create|update|delete
try {
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (isset($_GET['id'])) {
      $st=db()->prepare("SELECT * FROM giftcode WHERE id=?"); $st->execute([(int)$_GET['id']]);
      api_ok($st->fetch());
    }
    api_ok(db()->query("SELECT * FROM giftcode ORDER BY id DESC")->fetchAll());
  }
  $in=api_input(); $act=$in['action']??'';
  if ($act==='delete') {
    db()->prepare("DELETE FROM giftcode WHERE id=?")->execute([(int)$in['id']]);
    write_log('giftcode','api_delete','#'.(int)$in['id']); api_ok(['deleted'=>true]);
  }
  require_json($in['detail']??'','detail');
  if ($act==='update') {
    db()->prepare("UPDATE giftcode SET code=?,count_left=?,detail=?,expired=? WHERE id=?")
      ->execute([strtolower($in['code']),(int)$in['count_left'],$in['detail'],$in['expired']??null,(int)$in['id']]);
    write_log('giftcode','api_update',$in['code']); api_ok(['saved'=>true]);
  } elseif ($act==='create') {
    db()->prepare("INSERT INTO giftcode (code,count_left,detail,expired) VALUES (?,?,?,?)")
      ->execute([strtolower($in['code']),(int)$in['count_left'],$in['detail'],$in['expired']??null]);
    write_log('giftcode','api_create',$in['code']); api_ok(['created'=>true]);
  }
  api_err('action khong hop le');
} catch (Exception $e) { api_err($e->getMessage(),500); }
