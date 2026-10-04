<?php
// paneladmin/db.php - ket noi XAMPP dung chung
// Thu dragonballsaga truoc, fallback hondaodragon (khop connect.php va config.properties)

$PANEL_DB_HOST = getenv('DB_HOST') ?: '127.0.0.1';
$PANEL_DB_PORT = getenv('DB_PORT') ?: '3306';
$PANEL_DB_USER = getenv('DB_USER') ?: 'root';
$PANEL_DB_PASS = getenv('DB_PASS') !== false ? getenv('DB_PASS') : '';
$PANEL_DB_CANDIDATES = ['dragonballsaga', 'hondaodragon'];
if (getenv('DB_NAME')) {
    $PANEL_DB_CANDIDATES = [getenv('DB_NAME')];
}

$PANEL_DB_NAME = null;
$pdo = null;
$PANEL_DB_ERROR = null;
foreach ($PANEL_DB_CANDIDATES as $tryDb) {
    try {
        $dsn = "mysql:host={$PANEL_DB_HOST};port={$PANEL_DB_PORT};dbname={$tryDb};charset=utf8mb4";
        $pdo = new PDO($dsn, $PANEL_DB_USER, $PANEL_DB_PASS, [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        ]);
        $PANEL_DB_NAME = $tryDb;
        break;
    } catch (PDOException $e) {
        $PANEL_DB_ERROR = $e->getMessage();
    }
}
if (!$pdo) {
    die('Ket noi DB that bai (XAMPP): ' . htmlspecialchars($PANEL_DB_ERROR ?? 'unknown'));
}

function db() {
    global $pdo;
    return $pdo;
}
function panel_dbname() {
    global $PANEL_DB_NAME;
    return $PANEL_DB_NAME;
}
function safe_table($t) {
    if (!preg_match('/^[a-zA-Z0-9_]+$/', $t)) die('Ten bang khong hop le');
    return $t;
}
function is_valid_json($s) {
    if ($s === null || $s === '') return false;
    json_decode($s);
    return json_last_error() === JSON_ERROR_NONE;
}
function pretty_json($s) {
    $d = json_decode($s, true);
    if ($d === null && json_last_error() !== JSON_ERROR_NONE) return $s;
    return json_encode($d, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
}
function write_log($module, $action, $detail = '') {
    try {
        $user = $_SESSION['account'] ?? 'admin';
        $st = db()->prepare("INSERT INTO panel_change_log (admin_user, module, action, detail) VALUES (?,?,?,?)");
        $st->execute([$user, $module, $action, mb_substr($detail, 0, 2000)]);
    } catch (Exception $e) { /* bang log chua tao thi bo qua */ }
}
function ensure_panel_tables() {
    $sql = @file_get_contents(__DIR__ . '/schema_panel.sql');
    if (!$sql) return;
    foreach (explode(";\n", $sql) as $stmt) {
        $stmt = trim($stmt);
        if ($stmt === '' || strpos($stmt, '--') === 0) continue;
        try { db()->exec($stmt); } catch (Exception $e) {}
    }
}
