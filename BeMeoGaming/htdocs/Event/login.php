<?php
session_start();

// Thông tin DB
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "dragonballsaga";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Xử lý login
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user = $_POST['username'];
    $pass = $_POST['password'];

    // Check tài khoản
    $sql = "SELECT * FROM account WHERE username = ? AND password = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ss", $user, $pass);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();

        // Tạo token mới
        $token = bin2hex(random_bytes(32));

        // Lưu vào session
        $_SESSION['username'] = $row['username'];
        $_SESSION['vnd'] = $row['vnd'];
        $_SESSION['token'] = $token;

        // Cập nhật token vào DB
        $updateToken = $conn->prepare("UPDATE account SET token = ? WHERE username = ?");
        $updateToken->bind_param("ss", $token, $row['username']);
        $updateToken->execute();

        header("Location: daptrung.php");
        exit();
    } else {
        echo "<script>alert('Sai tài khoản hoặc mật khẩu!');</script>";
    }
}
?>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đăng Nhập</title>
<style>
    /* Reset cơ bản */
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    body {
        height: 100vh;
        display: flex;
        width: 100%;
        justify-content: center;
        align-items: center;
        
        background: url('images/nen.png') no-repeat center center fixed;
    }

    .login-container {
        background: url('images/nen.png') no-repeat center center fixed;
        padding: 40px 50px;
        border-radius: 15px;
        box-shadow: 0 8px 20px rgba(0,0,0,0.2);
        width: 100%;
        max-width: 400px;
        text-align: center;
        animation: fadeIn 1s ease;
    }

    .login-container h2 {
        margin-bottom: 30px;
        color: #333;
    }

    .login-container label {
        display: block;
        text-align: left;
        margin-bottom: 5px;
        color: #555;
        font-weight: 500;
    }

    .login-container input {
        width: 100%;
        padding: 12px 15px;
        margin-bottom: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        transition: border 0.3s;
        font-size: 16px;
    }

    .login-container input:focus {
        border-color: #2575fc;
        outline: none;
        box-shadow: 0 0 5px rgba(37, 117, 252, 0.5);
    }

    .login-container button {
        width: 100%;
        padding: 12px;
        background: #2575fc;
        border: none;
        border-radius: 8px;
        color: #fff;
        font-size: 18px;
        cursor: pointer;
        transition: background 0.3s, transform 0.2s;
    }

    .login-container button:hover {
        background: #6a11cb;
        transform: translateY(-2px);
    }

    /* Animation fadeIn */
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-20px); }
        to { opacity: 1; transform: translateY(0); }
    }
</style>
</head>
<body>
    <div class="login-container">
        <h2>Đăng Nhập</h2>
        <form method="POST">
            <label for="username">Tài khoản:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Đăng nhập</button>
        </form>
    </div>
</body>
</html>

