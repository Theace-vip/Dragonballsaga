<?php
session_start();
require('../config.php');
require('../views/layout/head.php');
require('../views/layout/nav.php');
?>

<style>
    th,
    td {
        white-space: nowrap;
        padding: 2px 4px !important;
        font-size: 11px;
    }
</style>

<div class="container color-forum pt-1 pb-1">
    <div class="row">
        <div class="col">
            <a href="home" style="color: white">Quay lại diễn đàn</a>
        </div>
        <div>
            <a href="/power" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Sức Mạnh</a>
            <a href="/task" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Nhiệm Vụ</a>
        </div>
    </div>
</div>

<div class="container color-forum pt-2">
    <div class="row">
        <div class="col">
            <?php
            $currentMonth = date('m');
            $currentYear = date('Y');

            $vietnameseMonths = array(
                1 => 'Tháng 1',
                2 => 'Tháng 2',
                3 => 'Tháng 3',
                4 => 'Tháng 4',
                5 => 'Tháng 5',
                6 => 'Tháng 6',
                7 => 'Tháng 7',
                8 => 'Tháng 8',
                9 => 'Tháng 9',
                10 => 'Tháng 10',
                11 => 'Tháng 11',
                12 => 'Tháng 12'
            );

            $query = "SELECT player.name, SUM(account.tongnap) AS tongnap, account.username 
                      FROM account 
                      JOIN player ON account.id = player.account_id 
                      WHERE account.is_admin >= 0 
                      GROUP BY player.name 
                      ORDER BY tongnap DESC 
                      LIMIT 10";

            $stmt = $conn->prepare($query);
            $stmt->execute();

            $result = $stmt->get_result();
            $stt = 1;

            echo '<div class="manage-header">
                    <h2 class="text-center">Bảng Xếp Hạng Đua Top Nạp</h2>
                  </div>
                  <div class="manage-body">
                    <div class="alert" style="display: flex;"></div>';

            if ($result->num_rows > 0) {
                echo '
                    <table class="table table-borderless text-center">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Nhân Vật</th>
                                <th>Tổng Nạp</th>
                            </tr>
                        </thead>
                        <tbody>';

                while ($row = $result->fetch_assoc()) {
                    $shortName = substr($row['name'], 0, 3) . str_repeat('*', max(0, strlen($row['name']) - 3));

                    echo '<tr>
                            <td>' . $stt++ . '</td>
                            <td>' . htmlspecialchars($shortName) . '</td>
                            <td>' . number_format($row['tongnap'], 0, ',', '.') . 'đ</td>
                          </tr>';
                }

                echo '</tbody></table>';
            } else {
                echo '<center><h6>Chưa có thống kê bảng xếp hạng top nạp!</h6></center>';
            }
            ?>

            <div class="text-right">
                <small>Cập nhật lúc: <?php echo date('H:i d/m/Y'); ?></small>
            </div>
        </div>
    </div>
</div>

<?php
require('../views/layout/foot.php');
?>
</div>
</div>
</body>

</html>