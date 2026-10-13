-- ============================================================
--  FIX TÊN BỊ MOJIBAKE — Đậu Vĩnh Viễn (GD2)
--  Nguyên nhân: file dau_vinh_vien.sql lần trước được chạy bằng
--  mysql client KHÔNG có --default-character-set=utf8mb4, nên các
--  ký tự tiếng Việt trong file bị đọc theo codepage cũ (CP437)
--  rồi lưu thành chữ rác vào DB. Trong game, tên món và tên option
--  hiện ra thành "─Éß║¡u thß║ºn cß║Ñp 9" / "─Éß║¼U V─¿NH VIß╗äN".
--
--  Chỉ 2 dòng bị lỗi (đã quét toàn bộ):
--    - item_template        id 1944  (comment = 'dau vinh vien')
--    - item_option_template id 254   (ĐẬU VĨNH VIỄN)
--
--  Chạy lại nhiều lần vẫn an toàn (idempotent).
--  CÁCH CHẠY ĐÚNG (bắt buộc có charset):
--    mysql --default-character-set=utf8mb4 -u root hondaodragon < fix_ten_dau_vinh_vien.sql
-- ============================================================

-- Chỉ sửa khi tên còn dính rác (HEX bắt đầu bằng E29480 = ký tự '─'),
-- KHÔNG ghi đè nếu admin đã đổi tên khác trong DB.
UPDATE item_template
   SET NAME = 'Đậu thần cấp 9'
 WHERE comment = 'dau vinh vien' AND HEX(NAME) LIKE 'E29480%';

UPDATE item_option_template
   SET NAME = 'ĐẬU VĨNH VIỄN'
 WHERE id = 254 AND HEX(NAME) LIKE 'E29480%';

-- Kiểm tra: hexname phải là UTF-8 chuẩn:
--   item  = C490E1BAAD75207468E1BAA76E2063E1BAA5702039  ("Đậu thần cấp 9")
--   option= C490E1BAAC552056C4A84E48205649E1BB844E        ("ĐẬU VĨNH VIỄN")
SELECT id, NAME, HEX(NAME) AS hexname FROM item_template WHERE comment = 'dau vinh vien';
SELECT id, NAME, HEX(NAME) AS hexname FROM item_option_template WHERE id = 254;
