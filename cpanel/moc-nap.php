<?php
session_start();
require('../config.php');
require('../views/layout/head.php');
require('../views/layout/nav.php');
require('../tuanbinh.php');
?>

<style>
    .content-wrapper {
        max-width: 960px;
        margin: 30px auto;
        padding: 20px 25px;
        border-radius: 18px;
        background: #fdf8da;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
    }

    h3 {
        font-size: 2rem;
        margin-bottom: 30px;
    }

    .moc-nap {
        margin: 16px 0;
        padding: 16px 20px;
        border-radius: 14px;
        background: #fff;
        box-shadow: 0 3px 12px rgba(0, 0, 0, 0.05);
        transition: box-shadow 0.3s ease;
    }

    .moc-nap h4 {
        font-size: 1.2rem;
        margin-bottom: 16px;
        font-weight: 700;
    }

    .phan-thuong-container {
        display: flex;
        flex-wrap: wrap;
        gap: 14px;
        justify-content: flex-start;
    }

    .qua-box {
        width: 54px;
        height: 54px;
        border-radius: 12px;
        background: #fdf8da;
        box-shadow: 0 2px 6px rgba(100, 100, 100, 0.08);
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: transform 0.25s ease, box-shadow 0.25s ease;
    }

    .qua-box:hover {
        transform: scale(1.1);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
    }

    .qua-box img {
        max-width: 40px;
        max-height: 40px;
        object-fit: contain;
        border-radius: 6px;
        pointer-events: none;
    }

    .qua-soLuong {
        position: absolute;
        bottom: -6px;
        right: 2px;
        background: rgba(231, 76, 60, 0.9);
        color: #fff;
        font-size: 11px;
        font-weight: 700;
        padding: 1px 4px;
        border-radius: 8px;
        box-shadow: 0 0 4px rgba(231, 76, 60, 0.6);
    }
</style>

<div class="content-wrapper">
    <center>
        <h3>DANH SÁCH CÁC MỐC NẠP</h3>
    </center>

    <?php for ($i = 0; $i < $soLuongMocNap; $i++): ?>
        <div class="moc-nap" role="group" aria-label="Mốc nạp số <?php echo $i + 1; ?>">
            <h4>🎁 Mốc <?php echo $i + 1; ?></h4>
            <div class="phan-thuong-container">
                <?php if (isset($phanThuong[$i])): ?>
                    <?php foreach ($phanThuong[$i] as $qua): ?>
                        <div class="qua-box" title="Số lượng: x<?php echo $qua['soLuong']; ?>">
                            <img src="<?php echo htmlspecialchars($duongDanAnh . $qua['img']); ?>.png" alt="Quà">
                            <div class="qua-soLuong">x<?php echo $qua['soLuong']; ?></div>
                        </div>
                    <?php endforeach; ?>
                <?php else: ?>
                    <p>Chưa có phần thưởng cho mốc này.</p>
                <?php endif; ?>
            </div>
        </div>
    <?php endfor; ?>
</div> <?php require('../views/layout/foot.php'); ?>