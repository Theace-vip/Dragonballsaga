<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';
if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
} elseif (isset($_Admin) && $_Admin == 0) {
    // Nếu đã đăng nhập nhưng không phải là admin, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}
$output = '';
if (isset($_POST['query'])) {
    $search = $_POST['query'];

    if (empty($search)) {
        $query = "SELECT player.*, account.* 
                  FROM player 
                  INNER JOIN account ON player.account_id = account.id
                  ORDER BY account.id DESC";
        $stmt = $conn->prepare($query);
        $stmt->execute();
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
    } else {
        $columns = array('player.name', 'account.temp_vnd', 'account.username', 'account.id', 'account.ip_address');
        $search_query = "SELECT player.*, account.* 
                     FROM player 
                     INNER JOIN account ON player.account_id = account.id
                     WHERE ";
        foreach ($columns as $column) {
            $search_query .= $column . " LIKE :search OR ";
        }
        $search_query = rtrim($search_query, "OR ");
        $search_query .= " ORDER BY account.id DESC";
        $stmt = $conn->prepare($search_query);
        $stmt->bindValue(':search', '%' . $search . '%', PDO::PARAM_STR);
        $stmt->execute();
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
    if (count($result) > 0) {
        foreach ($result as $row) {
            $output .= '<tr class="search-items">
                            <td>
                                        <div class="d-flex align-items-center">
                                            <div class="w-30px h-30px">
                                                <img src="/images/avatar/anonymous.png" alt=""
                                                    class="ms-100 mh-100">
                                            </div>
                                            <div class="ms-3 flex-grow-1">
                                                <div class="fw-600 text-body">Tên:
                                                    '. $row["name"].'</div>
                                                <div class="fs-13px">TK:
                                                    '. $row["username"].'</div>
                                            </div>
                                        </div>
                                    </td>
                            <td>
                                <span class="usr-email-addr">
                                    ' . number_format($row["tongnap"]) . 'đ
                                </span>
                            </td>
                            <td>
                                <span class="usr-ph-no">
                                    ' . getActive($row["active"]) . '
                                </span>
                            </td>
                            <td>
                                <span class="usr-ph-no">
                                    ' .  '
                                </span>
                            </td>
                            <td>
                                <span class="usr-ph-no">
                                    ' . $row["created_at"] . '
                                </span>
                            </td>
                            <td>
                                <div class="action-btn">
                                    <a href="javascript:void(0)" onclick="del_(' . $row['user_id'] . ');" class="btn btn-sm btn-danger delete ms-2">
                                        <i class="fa fa-trash-alt fs-5"></i>
                                    </a>
                                    <a href="javascript:void(0)"
                                        onclick="addCoin(' . $row['user_id'] . ');"
                                        class="btn btn-sm btn-success delete ms-2">
                                        <i class="fa fa-dollar fs-5"></i>
                                        </a>
                                </div>
                            </td>
                        </tr>';
        }
    } else {
        $output .= '<tr class="text-center py-3">
        <td colspan="8">
            <img src="https://cdn-icons-png.flaticon.com/128/7466/7466139.png"
                width="50" class="img-fluid">
            <p class="pt-3"><b>Không có dữ liệu</b></p>
        </td>
    </tr>';
    }
    echo $output;
}
?>