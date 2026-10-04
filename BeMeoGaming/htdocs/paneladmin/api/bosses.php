<?php
require_once __DIR__ . '/_common.php';
// GET ?id= | ?q=&page= | POST action=create|update|delete
try {
  if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (isset($_GET['id'])) {
      $st=db()->prepare("SELECT * FROM listbosses WHERE id=?"); $st->execute([(int)$_GET['id']]);
      api_ok($st->fetch());
    }
    $q=trim($_GET['q']??''); $page=max(1,(int)($_GET['page']??1)); $per=20; $off=($page-1)*$per;
    $w=''; $p=[];
    if ($q!==''){ $w='WHERE name LIKE ?'; $p[]='%'.$q.'%'; }
    $st=db()->prepare("SELECT COUNT(*) FROM listbosses $w"); $st->execute($p); $total=(int)$st->fetchColumn();
    $st=db()->prepare("SELECT * FROM listbosses $w ORDER BY id LIMIT $per OFFSET $off"); $st->execute($p);
    api_ok(['total'=>$total,'page'=>$page,'rows'=>$st->fetchAll()]);
  }
  $in=api_input(); $act=$in['action']??'';
  if ($act==='update') {
    require_json($in['dropItems']??'','dropItems');
    db()->prepare("UPDATE listbosses SET name=?,hp=?,dame=?,appearTime=?,mapAppear=?,dropItems=?,head=?,body=?,leg=? WHERE id=?")
      ->execute([$in['name'],$in['hp'],$in['dame'],$in['appearTime'],$in['mapAppear'],$in['dropItems'],(int)$in['head'],(int)$in['body'],(int)$in['leg'],(int)$in['id']]);
    write_log('boss','api_update','#'.(int)$in['id']); api_ok(['saved'=>true]);
  } elseif ($act==='create') {
    require_json($in['dropItems']??'[]','dropItems');
    $max=(int)db()->query("SELECT COALESCE(MAX(id),0) FROM listbosses")->fetchColumn();
    db()->prepare("INSERT INTO listbosses (id,name,hp,dame,appearTime,mapAppear,dropItems,head,body,leg) VALUES (?,?,?,?,?,?,?,?,?,?)")
      ->execute([$max+1,$in['name'],$in['hp'],$in['dame'],$in['appearTime'],$in['mapAppear'],$in['dropItems'],(int)($in['head']??0),(int)($in['body']??0),(int)($in['leg']??0)]);
    write_log('boss','api_create','#'.($max+1)); api_ok(['id'=>$max+1]);
  } elseif ($act==='delete') {
    db()->prepare("DELETE FROM listbosses WHERE id=?")->execute([(int)$in['id']]);
    write_log('boss','api_delete','#'.(int)$in['id']); api_ok(['deleted'=>true]);
  }
  api_err('action khong hop le');
} catch (Exception $e) { api_err($e->getMessage(),500); }
