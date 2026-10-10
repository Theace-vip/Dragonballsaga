<?php
session_start();
require('../config.php');
require('tops-config.php');
require('../views/layout/head.php');
require('../views/layout/nav.php');

// Nhom cac bang theo trang (giong NPC "Dua Top" trong game)
$groups = array();
foreach ($TOPS as $key => $top) {
    $g = isset($top['group']) ? $top['group'] : 'Khác';
    $groups[$g][$key] = $top;
}
ksort($groups);
?>

<div class="container color-forum pt-2 pb-1">
    <div class="row">
        <div class="col">
            <a href="/home" style="color: white">Quay lại trang chủ</a>
        </div>
        <div>
            <a href="/power" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Sức Mạnh (chi tiết)</a>
            <a href="/task" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Nhiệm Vụ</a>
        </div>
    </div>
</div>

<div class="container color-forum pt-1 pb-3">
    <div class="manage-header">
        <h2 class="text-center">BẢNG XẾP HẠNG SERVER <?php echo htmlspecialchars($brandName); ?></h2>
        <p class="text-center" style="color:#e8f6f3;">
            <?php echo count($TOPS); ?> bảng xếp hạng - cùng dữ liệu với NPC "Đua Top" trong game.
            Dữ liệu lấy trực tiếp từ máy chủ, làm mới mỗi 5 phút trong game.
        </p>
    </div>

    <?php foreach ($groups as $gname => $items): ?>
        <h5 class="mt-3 mb-2" style="color:#ffe259;">
            <i class="fa fa-star"></i> <?php echo htmlspecialchars($gname); ?>
        </h5>
        <div class="row">
            <?php foreach ($items as $key => $top): ?>
                <div class="col-md-4 col-sm-6 mb-2">
                    <a href="/top-<?php echo urlencode($key); ?>"
                       class="btn btn-action w-100 text-white text-left"
                       style="border-radius: 10px; padding: 8px 12px;">
                        <i class="fa fa-trophy"></i> <?php echo htmlspecialchars($top['name']); ?>
                        <br><small class="text-white-50"><?php echo htmlspecialchars($top['note']); ?></small>
                    </a>
                </div>
            <?php endforeach; ?>
        </div>
    <?php endforeach; ?>
</div>

<?php require('../views/layout/foot.php'); ?>
