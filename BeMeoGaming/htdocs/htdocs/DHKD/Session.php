<?php


#Duong Huynh Khanh Dang
if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

date_default_timezone_set('Asia/Ho_Chi_Minh');

$_Login = null;
$_Users = $_SESSION['account'] ?? null;
$_Ip = $_SERVER['REMOTE_ADDR'];



function fetchUserData($conn, $username)
{
    $stmt = $conn->prepare("SELECT * FROM account WHERE username = :username");
    $stmt->bindParam(":username", $username);
    $stmt->execute();
    $user_arr = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!$user_arr) {
        return null;
    }

    $user_data = [];
    foreach ($user_arr as $key => $value) {
        $user_data[$key] = ($value !== null) ? htmlspecialchars($value) : '';
    }

    return [
        "_id" => $user_data['id'],
        "_username" => $user_data['username'],
        "_password" => $user_data['password'],
        "_gmail" => $user_data['email'] ?? "",
        "_admin" => $user_data['is_admin'],
        "_coin" => $user_data['temp_vnd'],
        "_tcoin" => $user_data['tongnap'],
        "_status" => $user_data['active'],
        // "_security" => $user_data['password2'],
        "ip" => $user_data['ip_address'],
        // "thoivang" => $user_data['thoi_vang'],
        "ban" => 0,
    ];
}

if ($_Users !== null) {
    $_Login = "on";
    $user_data = fetchUserData($conn, $_Users);
    $user_sanitized = array_map(function($value) {
        return $value !== null ? htmlspecialchars($value) : $value;
    }, $user_data);

    $_Id = $user_sanitized['_id'];
    $_Username = $user_sanitized["_username"];
    $_Password = $user_sanitized["_password"];
    $_Email = $user_sanitized["_gmail"];
    $_Admin = $user_sanitized["_admin"];
    $_Coins = $user_sanitized["_coin"];
    $_TCoins = $user_sanitized["_tcoin"];
    $_Status = $user_sanitized["_status"];
    // $_Security = $user_sanitized["_security"];
    $_Ip = $user_sanitized['ip'];
    // $_ThoiVang = $user_sanitized['thoivang'];
    $_Band = 0;
}

function formatMoney($number)
{
    if (!is_numeric($number) || $number === null) {
        return '0 VNĐ';
    }

    $suffix = '';
    if ($number >= 1000000000000) {
        $number /= 1000000000000;
        $suffix = ' Tỷ';
    } elseif ($number >= 1000000000) {
        $number /= 1000000000;
        $suffix = ' Tỷ';
    } elseif ($number >= 1000000) {
        $number /= 1000000;
        $suffix = ' Triệu';
    } elseif ($number >= 1000) {
        $number /= 1000;
        $suffix = ' K';
    }

    return number_format($number) . $suffix;
}


function isValidInput($input)
{
    return preg_match('/^[a-zA-Z0-9_]+$/', $input) && strlen($input) <= 255;
}

function validateCaptcha($input, $captchaText)
{
    return strtoupper($input) === strtoupper($captchaText);
}

function generateCaptcha($length = 6)
{
    $characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    $captcha = '';
    for ($i = 0; $i < $length; $i++) {
        $captcha .= $characters[rand(0, strlen($characters) - 1)];
    }
    return $captcha;
}

// if (!isset($_POST["captcha"])) {
//     $_SESSION['captcha'] = generateCaptcha(6);
// }

function checkExistingUsername($conn, $Username)
{
    $stmt = $conn->prepare("SELECT COUNT(*) FROM account WHERE username = :username");
    $stmt->bindValue(':username', $Username, PDO::PARAM_STR);
    $stmt->execute();
    return $stmt->fetchColumn() > 0;
}


function checkExistingEmail($conn, $Email)
{
    $stmt = $conn->prepare("SELECT COUNT(*) FROM account WHERE email = :email");
    $stmt->bindValue(':email', $Email, PDO::PARAM_STR);
    $stmt->execute();
    return $stmt->fetchColumn() > 0;
}

function insertAccount($conn, $Username, $Password)
{
    $stmt = $conn->prepare("INSERT INTO account (username, password) VALUES (:username, :password)");
    $stmt->bindValue(':username', $Username, PDO::PARAM_STR);
    $stmt->bindValue(':password', $Password, PDO::PARAM_STR);
    //$stmt->bindValue(':email', $Email, PDO::PARAM_STR);
    return $stmt->execute();
}

function insert($conn, $table, $data)
{
    $field_list = '';
    $value_list = '';
    $placeholders = [];
    
    foreach ($data as $key => $value) {
        $field_list .= ", `$key`";
        $placeholders[] = ":$key"; // Tạo placeholder cho PDO
    }
    
    $sql = 'INSERT INTO `' . $table . '` (' . trim($field_list, ',') . ') VALUES (' . implode(',', $placeholders) . ')';
    
    $stmt = $conn->prepare($sql);
    
    foreach ($data as $key => $value) {
        $stmt->bindValue(":$key", $value); // Liên kết giá trị với placeholder
    }
    
    return $stmt->execute();
}

function post_card($request_id, $telco, $pin, $serial, $amount, $partner_id, $partner_key)
    {
        $data = array(
            'telco' => $telco,
            'code' => $pin,
            'serial' => $serial,
            'amount' => $amount,
            'request_id' => $request_id,
            'partner_id' => $partner_id,
            'sign' => md5($partner_key . $pin . $serial),
            'command' => 'charging'
        );

        $curl = curl_init();

        curl_setopt_array($curl, [
            CURLOPT_URL => 'https://doithe.vn/chargingws/v2?' . http_build_query($data),
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'GET',
            CURLOPT_HTTPHEADER => [
                'Content-Type: application/json',
            ],
        ]);

        $response = curl_exec($curl);

        curl_close($curl);
        return json_decode($response, true);
    }

function updatePassword($conn, $id, $Password)
{
    $stmt = $conn->prepare("UPDATE `account` SET `password` = :password WHERE `id` = :id");
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    $stmt->bindValue(':password', $Password, PDO::PARAM_STR);
    return $stmt->execute();
}

function addCointAccount($conn, $id, $coin)
{
    $stmt = $conn->prepare("UPDATE `account` SET `temp_vnd` = temp_vnd + :temp_vnd, `tongnap` = `tongnap` + :temp_vnd WHERE `id` = :id ");
    $stmt->bindValue(':temp_vnd', $coin, PDO::PARAM_INT);
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    return $stmt->execute();
}

function deletePlayer($conn, $id)
{
    $stmt = $conn->prepare("DELETE FROM `player` WHERE `account_id`= :id");
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    return $stmt->execute();
}

function getActive($stt)
{
    switch ($stt) {
        case 1:
            return '<span class="badge bg-danger">Chưa kích hoạt</span>';
        case 0:
            return '<span class="badge bg-success">Đã kích hoạt</span>';
        default:
            return '<span class="badge bg-secondary">Chưa Xác Định</span>';
    }
}

function deleteUser($conn, $id)
{
    $stmt = $conn->prepare("DELETE FROM `account` WHERE `id` = :id");
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    return $stmt->execute();
}

function Ex($status = false, $message = '', $data = [])
{
    header('Content-Type: application/json');
    echo json_encode([
        'status' => $status,
        'message' => $message,
        'data' => $data,
    ]);
}

function maskUsername($username) {
    // Lấy chiều dài của username
    $length = strlen($username);

    // Nếu username có ít hơn 3 ký tự, không làm gì và trả về username ban đầu
    if ($length < 3) {
        return $username;
    }

    // Lấy ký tự đầu tiên
    $firstChar = substr($username, 0, 2);

    // Lấy ký tự cuối cùng
    $lastChar = substr($username, -2);

    // Thay thế các ký tự từ ký tự thứ hai đến trước ký tự cuối bằng dấu *
    $masked = $firstChar.str_repeat('*', $length - 2).$lastChar;

    return $masked;
}

function formatPrice($price) {
    // Loại bỏ các dấu chấm và dấu phẩy và chuyển đổi sang số nguyên
    return (int) str_replace(['.', ','], '', $price);
}

function compact_number($num) {
    $number = $num / 1000;
    if($number <= 0) {
        return 'Miễn Phí';
    }else {
        return $number.'k';
    }

}
 function vongquay_image($id, $type) {

    $path = $_SERVER["DOCUMENT_ROOT"]."/Assets/images/minigame/".$type;

              if ($opendirectory = opendir($path)){
                while (($file = readdir($opendirectory)) !== false){

                    if ($file != "." && $file != "..") {
                        $arr = explode(".", $file);
                        if($arr[0] == $id) {
                           return '/Assets/images/minigame/'.$type.'/'.$file;
                        }
                    }
                }
                closedir($opendirectory);
              }

}
