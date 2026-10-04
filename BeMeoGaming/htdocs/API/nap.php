<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';

if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/error.php';</script>";
    exit;
}
?>
<?php
function CheckUser($role, $conn)
{
    try {
        $stmt = $conn->prepare("SELECT * FROM account WHERE username = :username");
        $stmt->bindParam(':username', $role, PDO::PARAM_STR);
        $stmt->execute();
        $select = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($select !== false && $select['username'] == $role) {            
            // If player exists, set session and return success message
            $account_id = $select['id'];
            $stmt_player = $conn->prepare("SELECT name FROM player WHERE account_id = :account_id");
            $stmt_player->bindParam(':account_id', $account_id, PDO::PARAM_INT);
            $stmt_player->execute();
            $player_info = $stmt_player->fetch(PDO::FETCH_ASSOC);
            if($player_info == false)
            {
                // If player does not exist, return message
            
                echo json_encode([
                    'status' => false,
                    'message' => 'Nhân vật không tồn tại.'
                ]);
                exit();
            }
            $_SESSION['playername'] = htmlspecialchars($role, ENT_QUOTES, 'UTF-8');
            if ($player_info !== false) {
                $_SESSION['usernameshow'] = htmlspecialchars($select['username'], ENT_QUOTES, 'UTF-8');
                $_SESSION['idshow'] = htmlspecialchars($account_id, ENT_QUOTES, 'UTF-8');
            } else {
                // Handle case where account username is not found (optional)
                $_SESSION['usernameshow'] = ''; // Set a default or handle as needed
            }
            $html = '
            <div class="form-group" id="find-package" data-href="/API/GoiNap">
                <label>Chọn nhân vật</label>
                <select id="role_id" name="role_id" class="form-control" onchange="findPackage(this)">
                    <option value="'. $_SESSION['idshow'] .'">'. maskUsername($_SESSION['usernameshow']) .'</option>
                </select>
                <div class="error d-none" id="role_id-error" style="color: red;"></div>
            </div>
            <script type="text/javascript">
                findPackage("auto_click", "next_pay");
            </script>';

            echo json_encode([
                'status' => true,
                'html' => $html
            ]);
            exit();
        } else {
            // If player does not exist, return message

            echo json_encode([
                'status' => false,
                'message' => 'Nhân vật không tồn tại.'
            ]);
            exit();
        }
    } catch (PDOException $e) {
        // Database error
        echo json_encode([
            'status' => false,
            'message' => 'Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại sau!'
        ]);
        exit();
    }
}

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve the 'role' parameter from the POST request
    $role = $_Username;
    // Basic validation to ensure 'role' is not empty
    if (empty($role)) {
        echo json_encode([
            'status' => false,
            'message' => 'Chưa có nhân vật.'
        ]);
        exit();
    }

    // Call function to check user in database
    CheckUser(htmlspecialchars($role, ENT_QUOTES, 'UTF-8'), $conn);
} else {
    // If the request method is not POST, return an error message
    echo json_encode([
        'status' => false,
        'message' => 'Phương thức yêu cầu không hợp lệ.'
    ]);
    exit();
}
?>