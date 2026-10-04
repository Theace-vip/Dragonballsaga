<?php
// api/_common.php - dung chung cho API panel
require_once dirname(__DIR__) . '/db.php';
require_once dirname(__DIR__) . '/auth.php';
header('Content-Type: application/json; charset=utf-8');
function api_ok($data = null) { echo json_encode(['ok'=>true,'data'=>$data], JSON_UNESCAPED_UNICODE); exit; }
function api_err($msg, $code = 400) { http_response_code($code); echo json_encode(['ok'=>false,'error'=>$msg], JSON_UNESCAPED_UNICODE); exit; }
function api_input() {
  $ct = $_SERVER['CONTENT_TYPE'] ?? '';
  if (stripos($ct,'application/json')!==false) { $d=json_decode(file_get_contents('php://input'),true); return is_array($d)?$d:[]; }
  return $_POST;
}
function require_json($s, $label='JSON') { if (trim($s)===''||!is_valid_json($s)) api_err("$label khong hop le"); }
