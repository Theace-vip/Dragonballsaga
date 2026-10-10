<?php
session_start();
require('../config.php');
require('tops-config.php');

// Kiem tra key TRUOC khi xuat HTML de con phai redirect duoc
$key = isset($_GET['t']) ? (string) $_GET['t'] : '';
if (!isset($TOPS[$key])) {
    header('Location: /dua-top');
    exit;
}
$top = $TOPS[$key];

require('../views/layout/head.php');
require('../views/layout/nav.php');
?>

<style>
    th,
    td {
        white-space: nowrap;
        padding: 3px 6px !important;
        font-size: 12px;
    }

    .top-tab {
        display: inline-block;
        border-radius: 10px;
        padding: 5px 10px;
        margin: 3px;
        background: #0d6efd;
        color: #fff;
        font-size: 12px;
    }

    .top-tab.active {
        background: #ffc107;
        color: #212529;
        font-weight: bold;
    }
</style>

<div class="container color-forum pt-2 pb-1">
    <div class="row">
        <div class="col">
            <a href="/dua-top" style="color: white"><i class="fa fa-arrow-left"></i> Tất cả bảng xếp hạng</a>
            <a href="/home" class="ml-2" style="color: white">Trang chủ</a>
        </div>
    </div>
    <div class="text-center mt-2">
        <?php foreach ($TOPS as $k => $t): ?>
            <a class="top-tab <?php echo $k === $key ? 'active' : ''; ?>"
               href="/top-<?php echo urlencode($k); ?>"><?php echo htmlspecialchars($t['name']); ?></a>
        <?php endforeach; ?>
    </div>
</div>

<div class="container color-forum pt-2 pb-3">
    <div class="manage-header">
        <h2 class="text-center"><?php echo htmlspecialchars($top['name']); ?></h2>
        <p class="text-center" style="color:#e8f6f3;">
            <?php echo htmlspecialchars($top['note']); ?> - cùng dữ liệu với NPC "Đua Top" trong game
        </p>
    </div>

    <table class="table table-borderless text-center">
        <thead>
            <tr>
                <th>#</th>
                <th>Nhân Vật</th>
                <th>Bang</th>
                <th>Hành Tinh</th>
                <?php foreach ($top['cols'] as $c): ?>
                    <th><?php echo htmlspecialchars($c['label']); ?></th>
                <?php endforeach; ?>
            </tr>
        </thead>
        <tbody>
            <?php
            $rows = array();
            $err = '';
            $res = mysqli_query($conn, $top['sql']);
            if ($res === false) {
                $err = mysqli_error($conn);
            } else {
                while ($row = mysqli_fetch_assoc($res)) {
                    $rows[] = $row;
                }
            }
            $stt = 1;
            if ($err !== '') {
                echo '<tr><td colspan="' . (4 + count($top['cols'])) . '">
                        <center><h6>Bảng xếp hạng đang được cập nhật, bạn thử lại sau ít phút.</h6></center>
                      </td></tr>';
            } elseif (!$rows) {
                echo '<tr><td colspan="' . (4 + count($top['cols'])) . '">
                        <center><h6>Chưa có dữ liệu bảng xếp hạng này.</h6></center>
                      </td></tr>';
            } else {
                foreach ($rows as $row) {
                    $clan = isset($row['clan_name']) && $row['clan_name'] !== '' && $row['clan_name'] !== null
                        ? htmlspecialchars($row['clan_name']) : 'Không bang';
                    echo '<tr' . ($stt <= 3 ? ' style="color:#ffe259;"' : '') . '>
                            <td>' . $stt . '</td>
                            <td><b>' . htmlspecialchars($row['name']) . '</b></td>
                            <td>' . $clan . '</td>
                            <td>' . top_gender($row['gender']) . '</td>';
                    foreach ($top['cols'] as $i => $c) {
                        $alias = 'v' . ($i + 1);
                        echo '<td>' . top_fmt(isset($row[$alias]) ? $row[$alias] : null, $c['fmt']) . '</td>';
                    }
                    echo '</tr>';
                    $stt++;
                }
            }
            ?>
        </tbody>
    </table>

    <div class="text-right">
        <small>Cập nhật lúc: <?php echo date('H:i d/m/Y'); ?> - tự làm mới khi bạn tải lại trang</small>
    </div>
</div>

<?php require('../views/layout/foot.php'); ?>
