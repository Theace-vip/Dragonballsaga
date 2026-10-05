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
        <div class="col"> <a href="home" style="color: white">Quay lại diễn đàn</a> </div>
        <div>
            <a href="/money" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Nạp</a>
            <a href="/task" class="btn btn-action m-1 text-white" style="border-radius: 10px;">Top Nhiệm Vụ</a>
        </div>
    </div>
</div>
<div class="container color-forum pt-2">
    <div class="row">
        <div class="col">
            <h6 class="text-center">BẢNG XẾP HẠNG ĐUA TOP SỨC MẠNH</h6>
            <table class="table table-borderless text-center">
                <tbody>
                    <tr>
                        <th>#</th>
                        <th>Nhân vật</th>
                        <th>Sức Mạnh</th>
                        <th>Đệ Tử</th>
                        <th>Hành Tinh</th>
                        <!--<th>Tổng</th>-->
                    </tr>
                <tbody>
                    <?php
                    $countTop = 1;
                    $data = $conn->query("SELECT name, gender, CASE
						WHEN gender = 1 THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[11]')) AS SIGNED)    
						WHEN gender = 2 THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[11]')) AS SIGNED)
						ELSE CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[11]')) AS SIGNED)  END AS second_value,
						player.pet_power AS pet_power, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[11]')) AS SIGNED) 
						FROM player JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 ORDER BY player.power DESC LIMIT 10;");

                    if ($data->num_rows > 0) {
                        while ($row = $data->fetch_assoc()) {
                            // Lấy 3 ký tự đầu tiên của name và thay phần còn lại bằng *
                            $shortName = substr($row['name'], 0, 3) . str_repeat('*', max(0, strlen($row['name']) - 3));
                    ?>
                            <tr class="top_<?php echo $countTop; ?>">
                                <td><?php echo $countTop++; ?></td>
                                <td><?php echo htmlspecialchars($shortName); ?></td>
                                <td>
                                    <?php
                                    $value = $row['second_value'];
                                    if ($value != '') {
                                        if ($value > 1000000000) {
                                            echo number_format($value / 1000000000, 1, '.', '') . ' tỷ';
                                        } elseif ($value > 1000000) {
                                            echo number_format($value / 1000000, 1, '.', '') . ' Triệu';
                                        } elseif ($value >= 1000) {
                                            echo number_format($value / 1000, 1, '.', '') . ' k';
                                        } else {
                                            echo number_format($value, 0, ',', '');
                                        }
                                    } else {
                                        echo 'Không có chỉ số sức mạnh';
                                    }
                                    ?>
                                </td>
                                <td>
                                    <?php
                                    $value = $row['pet_power'];
                                    if ($value != '') {
                                        if ($value > 1000000000) {
                                            echo number_format($value / 1000000000, 1, '.', '') . ' tỷ';
                                        } elseif ($value > 1000000) {
                                            echo number_format($value / 1000000, 1, '.', '') . ' Triệu';
                                        } elseif ($value >= 1000) {
                                            echo number_format($value / 1000, 1, '.', '') . ' k';
                                        } else {
                                            echo number_format($value, 0, ',', '');
                                        }
                                    } else {
                                        echo 'Không đệ tử';
                                    }
                                    ?>
                                </td>
                                <td>
                                    <?php
                                    if ($row['gender'] == 0) {
                                        echo "Trái đất";
                                    } elseif ($row['gender'] == 1) {
                                        echo "Namec";
                                    } elseif ($row['gender'] == 2) {
                                        echo "Xayda";
                                    }
                                    ?>
                                </td>
                            </tr>
                    <?php
                        }
                    } else {
                        echo 'Máy Chủ KOL chưa có thống kê bảng xếp hạng!';
                    }
                    ?>
                </tbody>
            </table>
            <script>
                // Cập nhật tự động sau mỗi 3 giây
                setInterval(function() {
                    $.ajax({
                        url: location.href, // URL hiện tại
                        success: function(result) {
                            var leaderboardTable = $(result).find('#leaderboard-table'); // Tìm bảng xếp hạng trong HTML mới nhận được
                            $('#leaderboard-table').html(leaderboardTable.html()); // Cập nhật HTML của bảng xếp hạng
                        }
                    });
                }, 3000);
            </script>
            <div class="text-right">
                <small>Cập nhật lúc: <?php echo date('H:i d/m/Y'); ?></small>
                </small>
            </div>
        </div>
    </div>
</div>
<?php
require('../views/layout/foot.php');
?>
</div>
</div>
</body><!-- Bootstrap core JavaScript -->

</html>