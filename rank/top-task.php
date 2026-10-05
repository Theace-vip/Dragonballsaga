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
        <div class="col d-flex justify-content-between align-items-center">
            <a href="home" style="color: white">Quay lại diễn đàn</a>
            <div>
                <a href="/power" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Sức Mạnh</a>
                <a href="/money" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Nạp</a>
            </div>
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

            $query = "SELECT 
                p.name, 
                CAST(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(p.data_task, ',', 2), ',', -1),']','') AS UNSIGNED) AS top_nv,
                CAST(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(p.data_task, ',', 3), ',', -1),']','') AS UNSIGNED) AS secondary_task,
                t.name AS task_name,
                st.name AS sub_task_name  
            FROM 
                player p
            LEFT JOIN 
                task_main_template t 
                ON t.id = CAST(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(p.data_task, ',', 2), ',', -1),']','') AS UNSIGNED)
            LEFT JOIN 
                task_sub_template st 
                ON st.task_main_id = t.id  
                AND st.task_main_id = CAST(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(p.data_task, ',', 1), ',', -1),']','') AS UNSIGNED)
            ORDER BY 
                top_nv DESC, 
                secondary_task DESC
            LIMIT 10";

            $stmt = $conn->prepare($query);
            $stmt->execute();

            // Lấy kết quả
            $result = $stmt->get_result();
            $stt = 1;
            $monthName = $vietnameseMonths[intval($currentMonth)];

            echo '<div class="manage-header">
                    <h2 class="text-center">Bảng Xếp Hạng Top Nhiệm Vụ</h2>
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
                                <th>Nhiệm Vụ</th>
                                <th>Nhiệm Vụ Nhánh</th>
                            </tr>
                        </thead>
                        <tbody>';
                while ($row = $result->fetch_assoc()) {
                    $shortName = substr($row['name'], 0, 3) . str_repeat('*', max(0, strlen($row['name']) - 3));

                    echo '<tr>
                            <td>' . $stt++ . '</td>
                            <td>' . htmlspecialchars($shortName) . '</td>
                            <td>' . htmlspecialchars($row['task_name']) . '</td>
                            <td>' . ($row['secondary_task'] ? htmlspecialchars($row['secondary_task']) : '0') . '</td>
                          </tr>';
                }
                echo '</tbody></table>';
            } else {
                echo '<center><h6>Chưa có thống kê bảng xếp hạng nhiệm vụ</h6></center>';
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