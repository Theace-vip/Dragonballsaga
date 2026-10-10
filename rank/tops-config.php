<?php
/**
 * Cau hinh toan bo bang xep hang cua game NROKuRA.
 * Nguon: BeMeoGaming/src/server/Manager.java (cac queryTop*) + npc/npc_manifest/DuaTop.java
 * Web dung chung DB hondaodragon voi game -> du lieu lay truc tiep tu bang player/account.
 *
 * Moi bang: name, note, sql (bat buoc chon p.id, p.name, p.gender + v1..v3), cols = cot hien thi.
 * fmt: num | money | short | sao | raw
 */

// prefix dung chung: lay ten bang (clan) + nhan vat
function top_prefix($extraSelect = '')
{
    $s = "SELECT p.id, p.name, p.gender, cl.NAME AS clan_name, " . $extraSelect;
    $s .= " FROM player p LEFT JOIN clan cl ON cl.id = p.clan_id ";
    return $s;
}

$TOPS = array(
    'nap' => array(
        'name' => 'Top Nạp',
        'note' => 'Tổng nạp (VNĐ) của tài khoản - account.tongnap',
        'group' => 'Trang 1',
        'sql' => top_prefix('a.tongnap AS v1') .
            'JOIN account a ON a.id = p.account_id ORDER BY a.tongnap DESC LIMIT 20',
        'cols' => array(array('label' => 'Tổng Nạp', 'fmt' => 'money')),
    ),
    'sukien' => array(
        'name' => 'Top Sự Kiện',
        'note' => 'Điểm sự kiện tích lũy - player.sukien',
        'group' => 'Trang 1',
        'sql' => top_prefix('p.sukien AS v1') . 'ORDER BY p.sukien DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Sự Kiện', 'fmt' => 'num')),
    ),
    'trainfam' => array(
        'name' => 'Top TrainFam',
        'note' => 'Điểm cộng đồng gia tộc/fam - player.diemfam',
        'group' => 'Trang 1',
        'sql' => top_prefix('p.diemfam AS v1') . 'ORDER BY p.diemfam DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Fam', 'fmt' => 'num')),
    ),
    'phongtap' => array(
        'name' => 'Top Phòng Tập',
        'note' => 'Điểm phòng tập - player.point_phongtap',
        'group' => 'Trang 1',
        'sql' => top_prefix('p.point_phongtap AS v1') . 'ORDER BY p.point_phongtap DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Phòng Tập', 'fmt' => 'num')),
    ),
    'vithu' => array(
        'name' => 'Top Vĩ Thú',
        'note' => 'Điểm săn Vĩ Thú - player.point_vithu',
        'group' => 'Trang 1',
        'sql' => top_prefix('p.point_vithu AS v1') . 'ORDER BY p.point_vithu DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Săn Vĩ Thú', 'fmt' => 'num')),
    ),
    'sanboss' => array(
        'name' => 'Top Săn Boss',
        'note' => 'Điểm săn boss - player.point_sb',
        'group' => 'Trang 1',
        'sql' => top_prefix('p.point_sb AS v1') . 'ORDER BY p.point_sb DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Săn Boss', 'fmt' => 'num')),
    ),
    'leothap' => array(
        'name' => 'Top Leo Tháp',
        'note' => 'Tầng / Level / Điểm leo tháp - player.leothap (loại tài khoản bị khóa, admin)',
        'group' => 'Trang 1',
        'sql' => top_prefix(
            "CAST(JSON_UNQUOTE(JSON_EXTRACT(p.leothap,'$[0]')) AS UNSIGNED) AS v1, " .
                "CAST(JSON_UNQUOTE(JSON_EXTRACT(p.leothap,'$[1]')) AS UNSIGNED) AS v2, " .
                "CAST(JSON_UNQUOTE(JSON_EXTRACT(p.leothap,'$[2]')) AS UNSIGNED) AS v3"
        ) . 'JOIN account a ON a.id = p.account_id WHERE a.is_admin = 0 AND a.ban = 0 ORDER BY v1 DESC, v2 DESC, v3 DESC LIMIT 100',
        'cols' => array(
            array('label' => 'Tầng', 'fmt' => 'num'),
            array('label' => 'Level', 'fmt' => 'num'),
            array('label' => 'Điểm', 'fmt' => 'num'),
        ),
    ),
    'sucmanh' => array(
        'name' => 'Top Sức Mạnh',
        'note' => 'Tổng sức mạnh nhân vật - player.power',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.power AS v1') . 'ORDER BY p.power DESC LIMIT 100',
        'cols' => array(array('label' => 'Sức Mạnh', 'fmt' => 'short')),
    ),
    'tienbang' => array(
        'name' => 'Top Tiên Bang',
        'note' => 'Cấp Tiên Bang - player.CapTamkjll',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.CapTamkjll AS v1') . 'ORDER BY p.CapTamkjll DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Tiên Bang', 'fmt' => 'num')),
    ),
    'nhapma' => array(
        'name' => 'Top Nhập Ma',
        'note' => 'Cấp Nhập Ma - player.lbTamkjll',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.lbTamkjll AS v1') . 'ORDER BY p.lbTamkjll DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Nhập Ma', 'fmt' => 'num')),
    ),
    'tieubu' => array(
        'name' => 'Top Tiêu Diệt Bư',
        'note' => 'Điểm tiêu diệt Bư - player.SukienTamBao',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.SukienTamBao AS v1') . 'ORDER BY p.SukienTamBao DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm Tiêu Bư', 'fmt' => 'num')),
    ),
    'bdkb' => array(
        'name' => 'Top Bản Đồ KB',
        'note' => 'Điểm bản đồ kb - player.point_bdkb',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.point_bdkb AS v1') . 'ORDER BY p.point_bdkb DESC LIMIT 20',
        'cols' => array(array('label' => 'Điểm BDkb', 'fmt' => 'num')),
    ),
    'moruong' => array(
        'name' => 'Top Mở Rương',
        'note' => 'Số rương thần bí đã mở - player.point_moruong',
        'group' => 'Trang 2',
        'sql' => top_prefix('p.point_moruong AS v1') . 'ORDER BY p.point_moruong DESC LIMIT 20',
        'cols' => array(array('label' => 'Đã Mở', 'fmt' => 'num')),
    ),
    'dapdo' => array(
        'name' => 'Top Đập Đồ',
        'note' => 'Tổng sao đã đập - player.point_dapdo',
        'group' => 'Trang 3',
        'sql' => top_prefix('p.point_dapdo AS v1') . 'ORDER BY p.point_dapdo DESC LIMIT 20',
        'cols' => array(array('label' => 'Đã Đập', 'fmt' => 'sao')),
    ),
    'tambao' => array(
        'name' => 'Top Tầm Bảo',
        'note' => 'Số lần quay Tầm Bảo - player.SukienTamBao',
        'group' => 'Trang 3',
        'sql' => top_prefix('p.SukienTamBao AS v1') . 'ORDER BY p.SukienTamBao DESC LIMIT 50',
        'cols' => array(array('label' => 'Đã Quay', 'fmt' => 'num')),
    ),
    'cauca' => array(
        'name' => 'Top Câu Cá',
        'note' => 'Số cá hiếm đã câu - player.point_cauca',
        'group' => 'Trang 3',
        'sql' => top_prefix('p.point_cauca AS v1') . 'ORDER BY p.point_cauca DESC LIMIT 20',
        'cols' => array(array('label' => 'Đã Câu', 'fmt' => 'num')),
    ),
    'thiendao' => array(
        'name' => 'Top Thiên Đạo',
        'note' => 'Cấp Thiên Đạo - player.SagaThienDao',
        'group' => 'Trang 4',
        'sql' => top_prefix('p.SagaThienDao AS v1') . 'ORDER BY p.SagaThienDao DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Thiên Đạo', 'fmt' => 'num')),
    ),
    'diadao' => array(
        'name' => 'Top Địa Đạo',
        'note' => 'Cấp Địa Đạo - player.SagaDiaDao',
        'group' => 'Trang 4',
        'sql' => top_prefix('p.SagaDiaDao AS v1') . 'ORDER BY p.SagaDiaDao DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Địa Đạo', 'fmt' => 'num')),
    ),
    'tutien' => array(
        'name' => 'Top Tu Tiên',
        'note' => 'Cấp Tu Tiên - player.SagaTuTien[1]',
        'group' => 'Trang 4',
        'sql' => top_prefix(
            "CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(p.SagaTuTien, ',', 2), ',', -1) AS UNSIGNED) AS v1"
        ) . 'ORDER BY v1 DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Tu Tiên', 'fmt' => 'num')),
    ),
    'chuyensinh' => array(
        'name' => 'Top Chuyển Sinh',
        'note' => 'Cấp Chuyển Sinh - player.SagaChuyenSinh',
        'group' => 'Trang 4',
        'sql' => top_prefix('p.SagaChuyenSinh AS v1') . 'ORDER BY p.SagaChuyenSinh DESC LIMIT 20',
        'cols' => array(array('label' => 'Cấp Chuyển Sinh', 'fmt' => 'num')),
    ),
);

/**
 * Dinh dang 1 gia tri theo kieu fmt.
 */
function top_fmt($val, $fmt)
{
    if ($val === null || $val === '') {
        return '-';
    }
    switch ($fmt) {
        case 'money':
            return number_format((float) $val, 0, ',', '.') . 'đ';
        case 'sao':
            return number_format((float) $val) . ' sao';
        case 'short':
            $v = (float) $val;
            if ($v > 1000000000) {
                return number_format($v / 1000000000, 1, '.', '') . ' tỷ';
            }
            if ($v > 1000000) {
                return number_format($v / 1000000, 1, '.', '') . ' triệu';
            }
            if ($v >= 1000) {
                return number_format($v / 1000, 1, '.', '') . ' k';
            }
            return number_format($v, 0, ',', '.');
        case 'raw':
            return (string) $val;
        case 'num':
        default:
            return number_format((float) $val, 0, ',', '.');
    }
}

/**
 * Ten hanh tinh theo gender (giong game: 0 Trai Dat, 1 Namec, 2 Xayda).
 */
function top_gender($g)
{
    switch ((int) $g) {
        case 0:
            return 'Trái Đất';
        case 1:
            return 'Namec';
        case 2:
            return 'Xayda';
        default:
            return '-';
    }
}
