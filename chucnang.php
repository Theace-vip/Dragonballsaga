<?php
include 'config.php';
require('views/layout/head.php');
require('views/layout/nav.php');

function box($title, $body)
{
    echo '<div class="alert alert-warning" style="background-color: #fdf8da;">';
    echo '<h5><b>' . htmlspecialchars($title) . '</b></h5>';
    echo '<div style="line-height:1.9;">' . $body . '</div>';
    echo '</div>';
}
?>

<div class="ibox-content m-b-sm forum-container">
    <div class="text-center mb-3">
        <h2 style="color:#c0392b;letter-spacing:2px;"><?php echo htmlspecialchars($brandName); ?></h2>
        <p><?php echo htmlspecialchars($brandSlogan); ?> - <?php echo htmlspecialchars($siteUrl); ?></p>
    </div>

    <?php
    box(
        '1. Thông tin máy chủ',
        '- Tên máy chủ trong game: <b>' . htmlspecialchars($serverName) . '</b> (hiển thị là <b>' . htmlspecialchars($brandName) . '</b>)<br>' .
            '- Website: <b>' . htmlspecialchars($siteUrl) . '</b> (trang này - dùng chung tài khoản với trong game)<br>' .
            '- Tỷ lệ kinh nghiệm / tiềm năng: <b>x' . $serverRate . '</b><br>' .
            '- Sức chứa: <b>' . number_format($serverMaxPlayer) . '</b> người chơi, tối đa <b>' . $serverMaxPerIp . '</b> kết nối cùng lúc mỗi IP<br>' .
            '- Mật khẩu tài khoản được băm <b>PBKDF2-SHA256</b> (cùng định dạng giữa web và game), admin không bao giờ hỏi mật khẩu.'
    );

    box(
        '2. Hệ thống đua top - 20 bảng xếp hạng',
        '- Toàn bộ bảng xếp hạng của server nằm ở mục <a href="/dua-top"><b>Đua Top</b></a>: Nạp, Sự Kiện, TrainFam, Phòng Tập, Vĩ Thú, ' .
            'Săn Boss, Leo Tháp, Sức Mạnh, Tiên Bang, Nhập Ma, Tiêu Diệt Bư, Bản Đồ KB, Mở Rương, Đập Đồ, Tầm Bảo, Câu Cá, Thiên Đạo, Địa Đạo, Tu Tiên, Chuyển Sinh.<br>' .
            '- Trong game, nói chuyện với NPC <b>Đua Top</b> (bên trái Kaio Shin, map Đảo Kamê) để xem cùng một dữ liệu này.<br>' .
            '- Dữ liệu trong game tự làm mới <b>mỗi 5 phút</b>, trang web đọc trực tiếp từ máy chủ nên luôn cập nhật khi tải lại trang.<br>' .
            '- Xem thêm: <a href="/power">Top Sức Mạnh</a>, <a href="/task">Top Nhiệm Vụ</a>, <a href="/money">Top Nạp</a>.'
    );

    box(
        '3. Tính năng nổi bật trong game',
        '- <b>Hệ thống sự kiện</b>: Sổ Xu Mệnh, Câu Cá, Chuyển Sinh, Kết Hôn, Tết, Trung Thu, Thợ Mổ, Bíp Kíp, Tầm Bảo, box quà sự kiện.<br>' .
            '- <b>Set 5 món</b>: 9 bộ trang bị (Thanh Long, Chu Tước, Bạch Hổ, Huyền Vũ, Kim - Mộc - Thổ - Thủy - Hoa) kích hoạt khi đủ <b>5/5</b> món, buff HP/KI/Sát thương/DEF/Chí mạng, chỉnh ngay trên Panel admin (mục SET5).<br>' .
            '- <b>Boss &amp; săn boss</b>: World Boss theo lịch, săn boss map, boss bản đồ KB, boss drop ngọc rồng.<br>' .
            '- <b>Cày cuốc</b>: Phòng Tập, TrainFam, Leo Tháp, Đập Đồ, Mở Rương Thần Bí, Câu Cá, Vĩ Thú.<br>' .
            '- <b>Phúc Lợi &amp; mốc nạp</b>: shop phúc lợi giá riêng, mốc nạp hiển thị rõ giá mua - xem <a href="/mocnap">Thông tin mốc nạp</a>.<br>' .
            '- <b>Bang hội</b>: lập bang, nhiệm vụ bang, điểm bang, top bang.<br>' .
            '- <b>Nhân vật</b>: đệ tử/pet, radar, cung mệnh, nội tại, kỹ năng dạng (Kame, Masenko, Makan...), chuyển sinh, thiên đạo / địa đạo / tu tiên.<br>' .
            '- <b>Thưởng top tự động</b>: phần thưởng gửi qua thư khi kết thúc kỳ đua top.'
    );

    box(
        '4. Hướng dẫn bắt đầu',
        '- Bước 1: <a href="/register"><b>Đăng ký tài khoản</b></a> (dùng chung cho web và trong game).<br>' .
            '- Bước 2: <a href="/huongdan"><b>Tải game</b></a> theo hướng dẫn cho từng hệ điều hành.<br>' .
            '- Bước 3: Đăng nhập, gặp NPC đầu tiên để nhận nhiệm vụ tân thủ, sau đó lên Đảo Kamê gặp NPC <b>Đua Top</b>.<br>' .
            '- Bước 4: Nạp tiền tại <a href="/naptien"><b>Nạp Game</b></a> nếu cần, xem <a href="/mocnap">mốc nạp</a> trước khi nạp.<br>' .
            '- Vấn đề phát sinh: nhấn nút <b>Báo Lỗi</b> trên menu để vào nhóm Zalo hỗ trợ.'
    );
    ?>
</div>

<?php require('views/layout/foot.php'); ?>
