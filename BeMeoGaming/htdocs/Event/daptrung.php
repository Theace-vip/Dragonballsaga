<?php
session_start();
include_once('config.php'); // hoặc đúng đường dẫn nếu khác

// Kiểm tra đăng nhập
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit;
}

$username = $_SESSION['username'];

// Lấy số tiền và điểm hiện tại
$sql = "SELECT vnd, tichdiem, token FROM account WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$vnd = $user['vnd'];
$tichdiem = $user['tichdiem'];

// Kiểm tra token để chống đăng nhập 2 nơi
if (!isset($_SESSION['token']) || $user['token'] !== $_SESSION['token']) {
    session_destroy();
    header("Location: login.php");
    exit;
}

// Khởi tạo lượt chơi
if (!isset($_SESSION['turns_left'])) {
    $_SESSION['turns_left'] = 3;
}

// ====== Xử lý AJAX đập trứng ======
if (isset($_POST['egg']) && isset($_POST['method'])) {
    header('Content-Type: application/json; charset=utf-8');

    $egg = intval($_POST['egg']);
    $method = $_POST['method'];

    // Hết lượt
    if ($_SESSION['turns_left'] <= 0) {
        echo json_encode(["success" => false, "message" => "Hết lượt, vòng mới bắt đầu!"]);
        exit;
    }

    // Đập bằng VND
    if ($method == 'vnd') {
        if ($vnd < 10000) {
            echo json_encode(["success" => false, "message" => "Không đủ VND để đập trứng!"]);
            exit;
        }
        $vnd -= 10000;
        $conn->query("UPDATE account SET vnd = vnd - 10000, daptrung_count = daptrung_count + 1 WHERE username = '$username'");

        // ====== Chỉ + counter khi đập bằng VND ======
        $counterFile = __DIR__ . '/daptrung_counter.txt';
        if (!file_exists($counterFile)) file_put_contents($counterFile, "0");
        $totalCount = (int)file_get_contents($counterFile);
        $totalCount++;
        if ($totalCount >= 100) {
            $vip = true;
            $totalCount = 0; // reset khi có VIP
        } else {
            $vip = false;
        }
        file_put_contents($counterFile, $totalCount);
    } 
    // Đập bằng điểm
    else {
        if ($tichdiem < 50) {
            echo json_encode(["success" => false, "message" => "Không đủ điểm để đập trứng!"]);
            exit;
        }
        $tichdiem -= 50;
        $conn->query("UPDATE account SET tichdiem = tichdiem - 50, daptrung_count = daptrung_count + 1 WHERE username = '$username'");
        $vip = false; // đập bằng điểm không tăng counter, không ra VIP
    }

    $_SESSION['turns_left']--;

    // Xử lý trúng VIP hoặc trúng thường
    if ($vip) {
        unset($_SESSION['vip_egg']);
        $_SESSION['turns_left'] = 3;
        $prizeName = "🎁 Hộp quà VIP";
        $prizeImg = "images/hopqua_vip.png";

        $_SESSION['real_notices'][] = "$username vừa nổ hũ VIP cực khủng!";

        // Lưu quà VIP vào kho web
        $conn->query("CREATE TABLE IF NOT EXISTS kho_web (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) NOT NULL,
            item_name VARCHAR(255) NOT NULL,
            quantity INT NOT NULL DEFAULT 0,
            UNIQUE KEY (username, item_name)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

        $stmt = $conn->prepare("INSERT INTO kho_web (username, item_name, quantity) VALUES (?, ?, ?)
                                ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)");
        if ($stmt) {
            $qty = 1;
            $stmt->bind_param("ssi", $username, $prizeName, $qty);
            $stmt->execute();
            $stmt->close();
        }
    } else {
        $prizes = [
            "images/hopqua.png" => "50k vàng",
            "images/hopqua.png" => "Thẻ nạp 100k",
            "images/hopqua.png" => "Ngọc rồng 5 sao",
            "images/hopqua.png" => "200k vàng",
            "images/hopqua.png" => "Thẻ nạp 50k",
            "images/hopqua.png" => "Ngọc rồng 3 sao",
            "images/hopqua.png" => "500k vàng"
        ];
        $randKey = array_rand($prizes);
        $prizeImg = $randKey;
        $prizeName = $prizes[$randKey];

        if ($_SESSION['turns_left'] <= 0) {
            unset($_SESSION['vip_egg']);
            $_SESSION['turns_left'] = 3;
        }
    }

    echo json_encode([
        "success" => true,
        "vip" => $vip,
        "message" => $prizeName,
        "prizeImg" => $prizeImg,
        "vnd" => $vnd,
        "tichdiem" => $tichdiem,
        "turns_left" => $_SESSION['turns_left'],
        "real_notices" => $_SESSION['real_notices'] ?? []
    ]);
    exit;
}


// Lấy BXH
$ranks = $conn->query("SELECT username, daptrung_count FROM account ORDER BY daptrung_count DESC LIMIT 10")->fetch_all(MYSQLI_ASSOC);
?>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Sự Kiện Đập Trứng</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background: url('images/nen.png') no-repeat center center fixed;
        background-size: cover;
        margin: 0;
        padding: 0;
        color: #333;
    }
    /* Thêm background layer trang trí */
body::before {
    content: "";
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    /* background: url('images/confetti.png') repeat; */
    opacity: 0.1;
    pointer-events: none; /* không cản click */
    z-index: 0;
}

/* Đổi con trỏ chuột khi hover trứng */
.egg-container img {
    cursor: url('images/bua.png'), pointer; /* búa */
    width: 110px;
    transition: transform 0.2s;
}

/* Thêm hiệu ứng hover sáng trứng */
.egg-container img:hover {
    transform: scale(1.15) rotate(-5deg);
    filter: drop-shadow(0 0 10px gold);
}

    /* Thanh thông báo chạy */
    .notice-bar {
        background: rgba(255, 250, 229, 0.95);
        color: #d35400;
        padding: 8px;
        overflow: hidden;
        white-space: nowrap;
        box-shadow: 0 2px 4px rgba(0,0,0,0.2);
        font-weight: bold;
    }
    .notice-text {
        display: inline-block;
        padding-left: 100%;
        animation: marquee 15s linear infinite;
    }
    @keyframes marquee {
        0% { transform: translate(0,0); }
        100% { transform: translate(-100%,0); }
    }

    /* Container chính */
    .main-container {
        display: flex;
        justify-content: center;
        align-items: flex-start;
        padding: 20px;
        gap: 30px;
        flex-wrap: wrap;
    }
    .game-content {
        text-align: center;
        background: rgba(255,255,255,0.9);
        padding: 20px;
        border-radius: 15px;
        box-shadow: 0 0 15px rgba(0,0,0,0.15);
    }

    /* Nút chọn phương thức */
    button {
        background: #f1c40f;
        border: none;
        padding: 8px 15px;
        margin: 5px;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: bold;
        transition: background 0.3s, transform 0.1s;
        margin-right: 10px;
    }
button.active {
    background-color: #ffc107; /* màu vàng nổi bật */
    color: #fff;
    box-shadow: 0 4px 10px rgba(0,0,0,0.2);
}

button.inactive {
    background-color: #ffe082; /* màu vàng nhạt */
    color: #333;
}

    button:hover {
        background: #e1b500;
        transform: scale(1.05);
    }
    .active {
        background: gold;
        box-shadow: 0 0 8px rgba(255,215,0,0.8);
    }

    /* Grid trứng */
    .eggs {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
        max-width: 600px;
        margin: 20px auto;
    }
    .egg-container {
        text-align: center;
    }
    .egg-container img {
        width: 110px;
        cursor: pointer;
        transition: transform 0.2s;
    }
    .egg-container img:hover {
        transform: scale(1.1);
    }
    .prize img {
        width: 35px;
        height: 35px;
        object-fit: contain;
        margin-top: 5px;
    }
.confetti-piece {
    position: fixed;
    width: 8px;
    height: 8px;
    background-color: gold;
    top: 0;
    left: 50%;
    opacity: 1;
    z-index: 9999;
    pointer-events: none;
    border-radius: 50%;
    animation: fall 2s linear forwards;
}

@keyframes fall {
    0% {
        transform: translateY(0) rotate(0deg);
        opacity: 1;
    }
    100% {
        transform: translateY(500px) rotate(360deg);
        opacity: 0;
    }
}

    /* Hiệu ứng rung */
    .shake {
        animation: shake 0.5s;
        animation-iteration-count: 3;
    }
    @keyframes shake {
        0% { transform: translate(1px, 1px) rotate(0deg); }
        25% { transform: translate(-1px, -2px) rotate(-1deg); }
        50% { transform: translate(-3px, 0px) rotate(1deg); }
        75% { transform: translate(3px, 2px) rotate(0deg); }
        100% { transform: translate(1px, -1px) rotate(1deg); }
    }

    /* Thể lệ */
    .rules {
        background: #fff8e1;
        border-radius: 10px;
        padding: 15px;
        max-width: 500px;
        margin: 20px auto;
        text-align: left;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        font-size: 14px;
    }
    .rules ul {
        margin: 0;
        padding-left: 20px;
    }

    /* Bảng xếp hạng */
    .ranking {
        background: rgba(255,255,255,0.9);
        border-radius: 10px;
        padding: 15px;
        min-width: 200px;
        box-shadow: 0 0 10px rgba(0,0,0,0.2);
    }
    .ranking table {
        border-collapse: collapse;
        width: 100%;
    }
    .ranking td {
        padding: 5px 8px;
        border-bottom: 1px solid #ddd;
        font-size: 14px;
    }
    .ranking tr:nth-child(1) td {
        font-weight: bold;
        color: #d35400;
    }

    /* Nút kho web */
    .kho-web-btn {
        background: orange;
        color: white !important;
        padding: 4px 8px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: bold;
        transition: background 0.3s;
    }
    .kho-web-btn:hover {
        background: darkorange;
    }
</style>
</head>
<body>

<audio id="bgMusic" loop>
    <source src="sounds/nhacnen.mp3" type="audio/mpeg">
</audio>

<div class="notice-bar">
    <span id="noticeText" class="notice-text">Chào mừng đến sự kiện đập trứng!</span>
</div>

<div style="text-align:center; padding:10px; background: rgba(255,255,255,0.85); margin-bottom: 15px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
    <h1>🥚 Sự Kiện Đập Trứng Zinh VIP 🥚</h1>
    <p>
        Tài khoản: <b><?php echo htmlspecialchars($username); ?></b> | 
        Số VND: <span id="vnd"><?php echo number_format($vnd); ?></span> | 
        Điểm: <span id="tichdiem"><?php echo number_format($tichdiem); ?></span> | 
        Lượt còn lại: <span id="turns"><?php echo $_SESSION['turns_left']; ?></span> | 
        <a href="logout.php">Đăng xuất</a> | 
        <a href="kho_web.php" class="kho-web-btn">📦 Kho đồ web</a>
    </p>
</div>

<div class="main-container">
    <div class="game-content">
        <button id="btnVND" onclick="setMethod('vnd')" class="active">💰 Đập bằng VND</button>
        <button id="btnPoint" onclick="setMethod('tichdiem')">⭐ Đập bằng Điểm</button>

        <div class="eggs">
            <?php for ($i=1; $i<=8; $i++): ?>
                <div class="egg-container">
                    <img id="egg<?php echo $i; ?>" src="images/trung.png" onclick="breakEgg(<?php echo $i; ?>)">
                    <div id="prize<?php echo $i; ?>" class="prize"></div>
                </div>
            <?php endfor; ?>
        </div>

        <div class="rules">
            <b>Thể lệ chơi:</b>
            <ul>
                <li>Mỗi lượt click vào 1 quả trứng sẽ trừ 10,000 VND hoặc 50 điểm.</li>
                <li>Chỉ được đập 3 quả mỗi vòng.</li>
                <li>Trong số 8 quả, có 1 quả VIP chứa phần thưởng đặc biệt.</li>
                <li>Phần thưởng sẽ hiển thị ngay sau khi bạn đập trứng.</li>
				  <li>Có Thể Chơi Free Bằng Cách She Bài Viết kiếm 50 Điểm.</li>
            </ul>
        </div>
    </div>

    <div class="ranking">
        <h3>🏆 BXH Đập Trứng</h3>
        <table>
            <?php foreach ($ranks as $index => $row): ?>
                <tr>
                    <td>#<?php echo $index+1; ?></td>
                    <td><?php echo htmlspecialchars($row['username']); ?></td>
                    <td><?php echo $row['daptrung_count']; ?> lượt</td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>
</div>
</body>
</html>
<script>
let method = 'vnd';
let fakeNames = ["NamPro", "HuyenXinh", "PhongVip", "MinhHero", "BaoCa", "LinhCute", "TuBoss", "KhangVL"];
let isBreaking = false; // ✅ chống spam click

document.addEventListener('click', function startMusic() {
    let music = document.getElementById('bgMusic');
    music.play().catch(err => console.log("Không phát được nhạc:", err));
    document.removeEventListener('click', startMusic);
});

function setMethod(m) {
    method = m;

    let btnVND = document.getElementById('btnVND');
    let btnPoint = document.getElementById('btnPoint');

    if(m === 'vnd') {
        btnVND.classList.add('active');
        btnVND.classList.remove('inactive');
        btnPoint.classList.remove('active');
        btnPoint.classList.add('inactive');
    } else {
        btnPoint.classList.add('active');
        btnPoint.classList.remove('inactive');
        btnVND.classList.remove('active');
        btnVND.classList.add('inactive');
    }
}

// Khởi tạo trạng thái ban đầu
document.getElementById('btnVND').classList.add('active');
document.getElementById('btnPoint').classList.add('inactive');

function showConfetti(count = 30) {
    for (let i = 0; i < count; i++) {
        let conf = document.createElement('div');
        conf.className = 'confetti-piece';
        conf.style.left = Math.random() * window.innerWidth + 'px';
        conf.style.backgroundColor = `hsl(${Math.random()*360}, 100%, 50%)`; // màu ngẫu nhiên
        conf.style.animationDuration = (1.5 + Math.random() * 1.5) + 's';
        conf.style.width = (5 + Math.random()*10) + 'px';
        conf.style.height = conf.style.width;
        document.body.appendChild(conf);
        setTimeout(() => conf.remove(), 2500); // xóa sau khi rơi
    }
}
if (data.vip) {
    showConfetti(50); // tạo 50 mảnh confetti
}


function updateNotice(msg) {
    let noticeText = document.getElementById("noticeText");
    noticeText.innerText = msg;
    noticeText.style.animation = "none";
    void noticeText.offsetWidth;
    noticeText.style.animation = null;
}

function fakeNotice() {
    let name = fakeNames[Math.floor(Math.random() * fakeNames.length)];
    let types = [
        `${name} vừa suýt nổ hũ VIP!`,
        `${name} vừa trúng 50k vàng!`,
        `${name} đang săn VIP cực gắt!`
    ];
    updateNotice(types[Math.floor(Math.random() * types.length)]);
}
setInterval(fakeNotice, 10000);

function breakEgg(num) {
    if (isBreaking) return; // ✅ Nếu đang đập, bỏ qua click
    isBreaking = true; // ✅ Khóa click trong 2s

    let egg = document.getElementById("egg" + num);
    let prizeDiv = document.getElementById("prize" + num);
    egg.classList.add("shake");

    fetch("", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "egg=" + num + "&method=" + method
    })
    .then(res => res.json())
    .then(data => {
        setTimeout(() => {
            egg.classList.remove("shake");
            if (data.success) {
                egg.src = data.vip ? "images/no.png" : "images/notrungvip.png";
                document.getElementById("vnd").innerText = data.vnd.toLocaleString();
                document.getElementById("tichdiem").innerText = data.tichdiem.toLocaleString();
                document.getElementById("turns").innerText = data.turns_left;
                prizeDiv.innerHTML = `<img src="${data.prizeImg}"><br>${data.message}`;

                if (data.real_notices && data.real_notices.length > 0) {
                    updateNotice(data.real_notices[data.real_notices.length - 1]);
                }

                if (data.turns_left === 3) {
                    setTimeout(() => {
                        for (let i = 1; i <= 8; i++) {
                            document.getElementById("egg" + i).src = "images/trung.png";
                            document.getElementById("prize" + i).innerHTML = "";
                        }
                    }, 2000);
                }
            } else {
                prizeDiv.innerHTML = `<span style="color:red;">${data.message}</span>`;
            }

            // ✅ Delay 2 giây rồi mới cho đập tiếp
            setTimeout(() => {
                isBreaking = false;
            }, 2000);

        }, 800);
    })
    .catch(() => {
        isBreaking = false;
    });
}
</script>

</body>
</html>
