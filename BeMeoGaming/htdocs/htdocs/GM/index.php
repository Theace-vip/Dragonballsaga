<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>🗂️ GM Tool - Dashboard</title>
<style>
body {
    font-family: Arial, sans-serif;
    background: #f0f2f5;
    margin: 0;
    padding: 0;
}

.container {
    max-width: 600px;
    margin: 50px auto;
    background: white;
    padding: 25px;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

h2 {
    text-align: center;
    margin-bottom: 25px;
    color: #333;
}

.file-list {
    list-style: none;
    padding: 0;
}

.file-list li {
    background: #f7f7f7;
    margin: 8px 0;
    padding: 12px 15px;
    border-radius: 8px;
    border-left: 5px solid #4a90e2;
    transition: background 0.3s, transform 0.2s;
}

.file-list li:hover {
    background: #eaeaea;
    transform: translateX(5px);
}

.file-list a {
    text-decoration: none;
    color: #333;
    font-weight: bold;
    display: block;
}

.file-list a span {
    font-size: 12px;
    color: #888;
    float: right;
}
</style>
</head>
<body>
<div class="container">
    <h2>🗂️ GM Tool - Dashboard</h2>
    <ul class="file-list">
        <li><a href="changegender.php">Đổi Hành Tinh</a></li>
        <li><a href="rename.php">Đổi tên</a></li>
    </ul>
</div>
</body>
</html>
