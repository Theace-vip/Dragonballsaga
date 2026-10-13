-- ============================================================
--  ĐẬU VĨNH VIỄN  (GD2)  —  chạy trên DB hondaodragon
--  Chạy lại nhiều lần vẫn an toàn (idempotent).
--  1) cột đếm  2) option 254  3) item riêng copy từ đậu thần 523
--  4) thêm đậu vĩnh viễn vào 2 mốc Phúc Lợi ĐANG RỖNG
--
--  *** BẮT BUỘC chạy kèm --default-character-set=utf8mb4:
--      mysql --default-character-set=utf8mb4 -u root hondaodragon < dau_vinh_vien.sql
--  Thiếu cờ này, tên tiếng Việt bị lưu thành chữ rác (mojibake).
-- ============================================================

-- ---------- 1) Cột đếm số đậu vĩnh viễn đã ăn ----------
SET @has_col := (SELECT COUNT(*) FROM information_schema.COLUMNS
                 WHERE TABLE_SCHEMA = DATABASE()
                   AND TABLE_NAME = 'player'
                   AND COLUMN_NAME = 'dau_vinh_vien');
SET @sql := IF(@has_col = 0,
               'ALTER TABLE player ADD COLUMN dau_vinh_vien INT NOT NULL DEFAULT 0',
               'DO 0');
PREPARE st FROM @sql; EXECUTE st; DEALLOCATE PREPARE st;

-- ---------- 2) Option 254 = ĐẬU VĨNH VIỄN ----------
--  Bảng đang có 254 option (id 0..253) => CHỈ CÒN ĐÚNG 1 SLOT TRỐNG.
--  KHÔNG xoá option ở giữa: client map option theo VỊ TRÍ MẢNG
--  (ItemData.updateItemOptionItemplate chỉ gửi name + type, không gửi id).
INSERT INTO item_option_template (id, NAME, type)
VALUES (254, 'ĐẬU VĨNH VIỄN', 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), type = VALUES(type);

-- ---------- 3) Item riêng: copy đậu thần cấp 9 (523) ----------
--  Đặt ở CUỐI danh sách (id = MAX(id)+1) vì client map item theo vị trí mảng.
--  TÊN GIỐNG HỆT đậu thần thường ("Đậu thần cấp 9") theo yêu cầu admin;
--  phân biệt bằng option 254 hiện trong bảng thông tin vật phẩm.
--  Luồng ăn giữ nguyên (hồi HP/KI theo option 2/48 + hồi stamina đệ tử).
SET @new_item := IFNULL((SELECT MAX(id) FROM item_template), 0) + 1;

INSERT INTO item_template
    (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up,
     power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
SELECT @new_item, t.TYPE, t.gender, 'Đậu thần cấp 9', t.description, t.level,
       t.icon_id, t.part, t.is_up_to_up, t.power_require, t.gold, t.gem, t.head, t.body,
       t.leg, t.is_up_to_up_over_99, t.can_trade, 'dau vinh vien', t.ruby
FROM item_template t
WHERE t.id = 523
  AND NOT EXISTS (SELECT 1 FROM (SELECT id, comment FROM item_template) x
                  WHERE x.comment = 'dau vinh vien');

SET @pea_id := (SELECT id FROM item_template WHERE comment = 'dau vinh vien' LIMIT 1);

-- ---------- 3b) Tự vá tên nếu lần chạy trước bị mojibake ----------
--  Chỉ sửa khi tên còn dính rác (HEX bắt đầu bằng E29480 = '─').
UPDATE item_template
   SET NAME = 'Đậu thần cấp 9'
 WHERE comment = 'dau vinh vien' AND HEX(NAME) LIKE 'E29480%';
UPDATE item_option_template
   SET NAME = 'ĐẬU VĨNH VIỄN'
 WHERE id = 254 AND HEX(NAME) LIKE 'E29480%';

-- ---------- 4) Thêm đậu vĩnh viễn vào 2 mốc Phúc Lợi ĐANG RỖNG ----------
--  Guard `list_item IN ('[]','')`: CHỈ điền vào mốc còn rỗng, KHÔNG ghi đè mốc admin đã điền.
--  Mốc 44 (tab Tích nạp, 20.000 coin nạp, active=0) và mốc 54 (tab Quà Coin, giá 50.000, active=1).
SET @json_44 := CONCAT('[{"id":1271,"quantity":5000,"options":[]},',
                       '{"id":', @pea_id, ',"quantity":30,"options":[{"id":254,"param":1},{"id":2,"param":9999}]},',
                       '{"id":1916,"quantity":1,"options":[]}]');
UPDATE phuc_loi_tab SET list_item = @json_44
 WHERE id = 44 AND tab_id = 2 AND @pea_id IS NOT NULL
   AND (list_item = '[]' OR list_item IS NULL OR list_item = '');

SET @json_54 := CONCAT('[{"id":', @pea_id, ',"quantity":100,"options":[{"id":254,"param":1},{"id":2,"param":9999}]},',
                       '{"id":381,"quantity":50,"options":[]},',
                       '{"id":382,"quantity":50,"options":[]},',
                       '{"id":383,"quantity":50,"options":[]}]');
UPDATE phuc_loi_tab SET list_item = @json_54
 WHERE id = 54 AND tab_id = 3 AND @pea_id IS NOT NULL
   AND (list_item = '[]' OR list_item IS NULL OR list_item = '');

-- ---------- 5) Kiểm tra ----------
SELECT (SELECT COUNT(*) FROM item_option_template) AS tong_option,          -- phai = 255
       (SELECT MAX(id) + 1 FROM item_option_template) AS max_plus_1;        -- phai = 255
SELECT id, NAME, HEX(NAME) AS hexname_item, TYPE, icon_id, comment FROM item_template WHERE comment = 'dau vinh vien';
SELECT id, NAME, HEX(NAME) AS hexname_option FROM item_option_template WHERE id = 254;
SELECT id, tab_id, JSON_VALID(list_item) AS json_ok, list_item
  FROM phuc_loi_tab WHERE id IN (44, 54);
SELECT COUNT(*) AS co_cot_dau_vinh_vien FROM information_schema.COLUMNS
 WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'player' AND COLUMN_NAME = 'dau_vinh_vien';
