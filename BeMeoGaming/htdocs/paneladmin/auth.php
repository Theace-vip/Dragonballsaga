<?php
// paneladmin/auth.php - bao ve trang admin, KHONG phu thuoc core/set.php
// (core/set.php chet luon neu DB dragonballsaga khong ton tai, lam panel trang trang)
// Logic: neu co session web + account la admin (is_admin=1) thi ok;
// chua login thi cho vao o che do guest de test local.
if (session_status() === PHP_SESSION_NONE) { session_start(); }
$PANEL_GUEST = false;
$__u = $_SESSION['account'] ?? null;
if ($__u !== null && function_exists('db')) {
    try {
        $st = db()->prepare("SELECT is_admin FROM account WHERE username = ? LIMIT 1");
        $st->execute([$__u]);
        $row = $st->fetch();
        if ($row && (int)$row['is_admin'] !== 1) {
            die('Ban khong co quyen admin. <a href="/">Ve trang chu</a>');
        }
        if (!$row) { $PANEL_GUEST = true; }
    } catch (Exception $e) { $PANEL_GUEST = true; }
} else {
    $PANEL_GUEST = true;
}
