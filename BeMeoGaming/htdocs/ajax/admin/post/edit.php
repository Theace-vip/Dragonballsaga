<?php
if (empty($_SERVER['HTTP_REFERER'])) {
    header('HTTP/1.0 403 Forbidden');
    echo "Forbidden: You don't have permission to access this resource.";
    exit();
}
require_once $_SERVER['DOCUMENT_ROOT'] . "/cvhvn/autoload.php";
//isset($_SESSION['admin']) && 
if (isset($user['admin']) || $user['admin'] != 1) {
    if (isset($_POST['title']) && isset($_POST['content']) && isset($_POST['avatar']) && !empty($_POST['title']) && !empty($_POST['content']) && !empty($_POST['avatar'])) {
        if (isset($user['admin']) || $user['admin'] != 1) {
            $if_admin = abs($_POST['avatar']);
            $content = $_POST['content'];
            $title = $_POST['title'];
            $id = $_POST['id'];
            $category_id = $_POST['category_id'];

            $table = "cvh_baiviet";
            $data = array(
                "title" => $title,
                "if_admin" => getAdminPosts($if_admin),
                "content" => $content,
                "category_id" => $category_id,
            );
            $where = 'id = ' . $id;
            $CVH->update($table, $data, $where);
            $CVH->Ex(true, "Đăng bài viết thành công!");

        } else {
            $CVH->Ex(false, "Địt mẹ mày cút ngay!");
        }

    } else {
        $CVH->Ex(false, "Vui lòng nhập đầy đủ thông tin!");
    }
} else {
    $CVH->Ex(false, "Bạn chưa đăng nhập!");
}