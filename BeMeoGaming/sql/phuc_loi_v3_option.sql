-- =====================================================================
--  PHÚC LỢI v3 — BẢN CHỐT (mục 4 + mục 5 của KE_HOACH_QUA_PHUC_LOI.md)
--  THAY THẾ: sql/phuc_loi_v2_option.sql  và  sql/phuc_loi_3tab_qua.sql
--  (2 file cũ KHÔNG chạy nữa — chúng còn option cũ 158/75/35 và param cũ)
--
--  QUY TẮC PARAM (admin chốt: GIỮ hệ số 10, không đổi r_opt*_factor):
--     option 50 (%SĐ) / 77 (%Sinh lực) / 103 (%MaNa) = param × 0.1
--     => MUỐN % THẬT BAO NHIÊU THÌ GHI PARAM = SỐ THẬT × 10
--        T1 = 25%  -> param 250      T3 = 90%  -> param 900
--        T2 = 50%  -> param 500      T4 = 150% -> param 1500
--     Các option 1:1 (94 %Giáp, 5 %STCM, 14 chí mạng, 117 %SĐ đẹp,
--     95/96/97/98/99, 108, 105, 173, 204, 47, 6, 7, 0, 8, 16, 18, 19)
--     ghi ĐÚNG số thật.
--
--  THỨ TỰ THI HÀNH:
--    1) sql/dau_vinh_vien.sql        (cột dau_vinh_vien + option 254 + item đậu 1944 + mốc 44/54)
--    2) FILE NÀY                     (set_config + nội dung 3 tab)
--    3) KHỞI ĐỘNG LẠI SERVER         (nạp option 254 vào bộ nhớ)
--    4) Panel -> Phúc Lợi -> Reload
--    5) Người chơi ĐĂNG NHẬP LẠI      (client nhận danh sách option mới)
--
--  AN TOÀN: chạy lại nhiều lần vẫn được (UPDATE theo id + ON DUPLICATE KEY UPDATE).
--   - Mốc 44 (tab 2) và mốc 54 (tab 3) để NGUYÊN: file dau_vinh_vien.sql đã điền
--     (guard chỉ điền mốc rỗng), file này không ghi đè.
--   - Trước khi chạy nên backup:
--     mysqldump -uroot hondaodragon phuc_loi_tab phuc_loi set_config item_option_template > backup.sql
-- =====================================================================

-- ---------------------------------------------------------------------
-- PHẦN 0: HIỆN TRẠNG (xem trước khi sửa)
-- ---------------------------------------------------------------------
SELECT id, tab_id, name, max_count, active, JSON_LENGTH(list_item) AS so_mon
FROM phuc_loi_tab WHERE tab_id IN (1,2,3) ORDER BY tab_id, id;
SELECT COUNT(*) AS tong_so_moc FROM phuc_loi_tab;                 -- phải <= 127
SELECT COUNT(*) AS so_option FROM item_option_template;           -- phải = 255 sau khi có 254

-- Đậu vĩnh viễn (item + option 254) — BẮT BUỘC đã chạy dau_vinh_vien.sql:
SET @pea_id := (SELECT id FROM item_template WHERE comment = 'dau vinh vien' LIMIT 1);
SELECT IF(@pea_id IS NULL,
          '### CẢNH BÁO: CHƯA có đậu vĩnh viễn -> chạy sql/dau_vinh_vien.sql TRƯỚC, nếu không mốc 2/3 tab 1 sẽ không được điền!',
          CONCAT('OK: item đậu vĩnh viễn id = ', @pea_id)) AS kiem_tra_dau;

START TRANSACTION;

-- =====================================================================
-- PHẦN 1: set_config — thêm %SĐ cho Tư Tượng (149-152) và Ngũ Hành (153-157)
--   Tư Tượng đã ở dạng % -> chỉ THÊM dame_pct.
--   Ngũ Hành đang ở dạng flat (5tr/10tr) -> CHUYỂN sang % + thêm dame_pct.
--   Có hiệu lực ngay sau Panel -> Reload. KHÔNG cần build lại.
-- =====================================================================
UPDATE set_config SET dame_pct = 200                        WHERE id = 1; -- Thanh Long: +500% KI, +200% SĐ
UPDATE set_config SET dame_pct = 200                        WHERE id = 2; -- Chu Tước  : +500% HP, +200% SĐ
UPDATE set_config SET crit_add = 5                          WHERE id = 3; -- Bạch Hổ   : +1000% SĐ, +5 chí mạng
UPDATE set_config SET dame_pct = 200                        WHERE id = 4; -- Huyền Vũ  : +500% Giáp, +200% SĐ
UPDATE set_config SET dame_base = 0,  dame_pct = 400        WHERE id = 5; -- Kim : +400% SĐ (bỏ 5.000.000 flat)
UPDATE set_config SET hp_base   = 0,  hp_pct   = 400, dame_pct = 100 WHERE id = 6; -- Mộc : +400% HP, +100% SĐ
UPDATE set_config SET ki_base   = 0,  ki_pct   = 400, dame_pct = 100 WHERE id = 7; -- Thổ : +400% KI, +100% SĐ
UPDATE set_config SET def_base  = 0,  def_pct  = 400, dame_pct = 100 WHERE id = 8; -- Thủy: +400% Giáp, +100% SĐ
UPDATE set_config SET crit_dmg_pct = 200, crit_add = 10, dame_pct = 150 WHERE id = 9; -- Hỏa: +200% STCM, +10 chí mạng, +150% SĐ

-- =====================================================================
-- PHẦN 2: TAB 1 — ĐIỂM DANH TUẦN (id 2..8, active=0, max_count = thứ trong tuần)
--   T2/T3 thưởng ĐẬU VĨNH VIỄN (item 1944, mang option 254 + option 2 hồi HP/KI).
--   max_count KHÔNG đổi (server so sánh BẰNG ĐÚNG số thứ).
-- =====================================================================
SET @json_2 := CONCAT('[{"id":1271,"quantity":1000,"options":[]},',
                      '{"id":', @pea_id, ',"quantity":20,"options":[{"id":254,"param":1},{"id":2,"param":9999}]}]');
UPDATE phuc_loi_tab SET list_item = @json_2 WHERE id = 2 AND @pea_id IS NOT NULL;

SET @json_3 := CONCAT('[{"id":1271,"quantity":2000,"options":[]},',
                      '{"id":', @pea_id, ',"quantity":20,"options":[{"id":254,"param":1},{"id":2,"param":9999}]},',
                      '{"id":1916,"quantity":1,"options":[]}]');
UPDATE phuc_loi_tab SET list_item = @json_3 WHERE id = 3 AND @pea_id IS NOT NULL;

-- Thứ 4 (id 4)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":2000,"options":[]},{"id":381,"quantity":10,"options":[]},{"id":382,"quantity":10,"options":[]},{"id":383,"quantity":10,"options":[]}]' WHERE id=4;

-- Thứ 5 (id 5)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1099,"quantity":5,"options":[]},{"id":1100,"quantity":5,"options":[]},{"id":1102,"quantity":5,"options":[]}]' WHERE id=5;

-- Thứ 6 (id 6)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1108,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]}]' WHERE id=6;

-- Thứ 7 (id 7)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":987,"quantity":1,"options":[]}]' WHERE id=7;

-- Chủ nhật (id 8)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]}]' WHERE id=8;

-- =====================================================================
-- PHẦN 3: TAB 2 — TÍCH NẠP (active=0, max_count = coin đã nạp, nhận 1 lần)
--   id 45..53 + 3 mốc mới 90/92/93. Mốc 44 (20.000) do dau_vinh_vien.sql điền.
--   LƯU Ý ô trống: conChoTrong cần SỐ Ô TRỐNG > SỐ ITEM (dư 1 ô)
--     4 món -> cần >= 5 ô trống ; 5 món (full set) -> cần >= 6 ô trống.
-- =====================================================================
-- 30.000 (id 45)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=45;

-- 50.000 (id 46)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]}]' WHERE id=46;

-- 100.000 (id 47)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":20000,"options":[]},{"id":1270,"quantity":5,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1217,"quantity":1,"options":[]}]' WHERE id=47;

-- 500.000 (id 48)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":50000,"options":[]},{"id":1270,"quantity":10,"options":[]},{"id":457,"quantity":10,"options":[]},{"id":1132,"quantity":1,"options":[]}]' WHERE id=48;

-- 1.000.000 (id 49)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":20,"options":[]},{"id":1124,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]}]' WHERE id=49;

-- 1.500.000 (id 50)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":30,"options":[]},{"id":454,"quantity":1,"options":[]},{"id":1741,"quantity":1,"options":[]}]' WHERE id=50;

-- 2.000.000 (id 51)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":671,"quantity":1,"options":[]},{"id":1736,"quantity":1,"options":[]}]' WHERE id=51;

-- 3.000.000 (id 52)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":1159,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]}]' WHERE id=52;

-- 5.000.000 (id 53) — SET HẮC ÁM — TIER 1 (1174..1178). MỐC FULL SET -> ĐI MỘT MÌNH (cần >= 6 ô trống).
--  Mỗi món: 2 cơ bản (50/77/103, param = % thật × 10 = 250) + 2 quan trọng + 66 (set +30% SĐ) + 30:1 (khoá)
--  Tổng 5/5: +100% SĐ · +75% Sinh lực · +75% MaNa · +30% Giáp · +300.000 HP · +15.000 SĐ · +10.000 Giáp
--            · +3% chí mạng · +10% STCM · +5% hút HP/KI xq · +20% chính xác · +20% tốc chạy · +30% SĐ (66)
UPDATE phuc_loi_tab SET list_item=
'[{"id":1174,"quantity":1,"options":[{"id":50,"param":250},{"id":77,"param":250},{"id":94,"param":30},{"id":47,"param":10000},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1175,"quantity":1,"options":[{"id":77,"param":250},{"id":103,"param":250},{"id":6,"param":300000},{"id":8,"param":5},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1176,"quantity":1,"options":[{"id":50,"param":250},{"id":103,"param":250},{"id":0,"param":15000},{"id":14,"param":3},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1177,"quantity":1,"options":[{"id":50,"param":250},{"id":77,"param":250},{"id":18,"param":20},{"id":16,"param":20},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1178,"quantity":1,"options":[{"id":103,"param":250},{"id":50,"param":250},{"id":5,"param":10},{"id":19,"param":20},{"id":66,"param":0},{"id":30,"param":1}]}]'
WHERE id=53;

-- 10.000.000 (id 90) — SET NGUYÊN THỦY — TIER 2 (1730..1734). MỐC FULL SET -> ĐI MỘT MÌNH.
--  param 50/77/103 = 500 (= 50% thật)
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(90, 2, '10.000.000', 10000000, 0,
'[{"id":1730,"quantity":1,"options":[{"id":50,"param":500},{"id":77,"param":500},{"id":94,"param":60},{"id":95,"param":10},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1731,"quantity":1,"options":[{"id":77,"param":500},{"id":103,"param":500},{"id":204,"param":50},{"id":173,"param":10},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1732,"quantity":1,"options":[{"id":50,"param":500},{"id":103,"param":500},{"id":5,"param":20},{"id":14,"param":6},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1733,"quantity":1,"options":[{"id":50,"param":500},{"id":77,"param":500},{"id":18,"param":30},{"id":108,"param":10},{"id":66,"param":0},{"id":30,"param":1}]},
  {"id":1734,"quantity":1,"options":[{"id":103,"param":500},{"id":50,"param":500},{"id":8,"param":10},{"id":97,"param":15},{"id":66,"param":0},{"id":30,"param":1}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 20.000.000 (id 91) — đồ đại gia
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(91, 2, '20.000.000', 20000000, 0,
'[{"id":1885,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1224,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1225,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1123,"quantity":1,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 50.000.000 (id 92) — SET TƯ TƯỢNG (Hắc Ám Loạn Lưu 1919..1923) — TIER 3. MỐC FULL SET -> ĐI MỘT MÌNH.
--  param 50/77/103 = 900 (= 90% thật). Marker 149-152 -> mặc 5/5 kích hoạt 4 bonus set_config.
--  Chung: 117:25 (%SĐ "đẹp") + 149..152:0 + 30:1
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(92, 2, '50.000.000', 50000000, 0,
'[{"id":1919,"quantity":1,"options":[{"id":50,"param":900},{"id":77,"param":900},{"id":94,"param":100},{"id":95,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1920,"quantity":1,"options":[{"id":77,"param":900},{"id":103,"param":900},{"id":173,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1921,"quantity":1,"options":[{"id":50,"param":900},{"id":103,"param":900},{"id":5,"param":35},{"id":14,"param":10},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1922,"quantity":1,"options":[{"id":50,"param":900},{"id":77,"param":900},{"id":108,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1923,"quantity":1,"options":[{"id":103,"param":900},{"id":50,"param":900},{"id":97,"param":30},{"id":105,"param":1},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 100.000.000 (id 93) — SET NGŨ HÀNH (Hỗn Độn Vô Cực 1924..1928) — TIER 4. MỐC FULL SET -> ĐI MỘT MÌNH.
--  param 50/77/103 = 1500 (= 150% thật). Marker 153-157 -> mặc 5/5 kích hoạt 5 bonus set_config Ngũ Hành.
--  Chung: 117:40 (%SĐ "đẹp") + 153..157:0 + 30:1
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(93, 2, '100.000.000', 100000000, 0,
'[{"id":1924,"quantity":1,"options":[{"id":50,"param":1500},{"id":77,"param":1500},{"id":94,"param":150},{"id":95,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1925,"quantity":1,"options":[{"id":77,"param":1500},{"id":103,"param":1500},{"id":96,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1926,"quantity":1,"options":[{"id":50,"param":1500},{"id":103,"param":1500},{"id":5,"param":60},{"id":14,"param":15},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1927,"quantity":1,"options":[{"id":50,"param":1500},{"id":77,"param":1500},{"id":97,"param":50},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1928,"quantity":1,"options":[{"id":103,"param":1500},{"id":50,"param":1500},{"id":105,"param":1},{"id":98,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- =====================================================================
-- PHẦN 4: TAB 3 — QUÀ COIN (active=1, max_count = GIÁ Coin, mua lại vô hạn)
--   id 55..62 + 3 mốc mới 87/88/89. Mốc 54 (50.000) do dau_vinh_vien.sql điền.
-- =====================================================================
-- 50.000 (id 55)
UPDATE phuc_loi_tab SET list_item='[{"id":384,"quantity":50,"options":[]},{"id":1099,"quantity":50,"options":[]},{"id":1100,"quantity":50,"options":[]},{"id":1101,"quantity":50,"options":[]}]' WHERE id=55;

-- 100.000 (id 56)
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":1000,"options":[]},{"id":1916,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=56;

-- 100.000 (id 57)
UPDATE phuc_loi_tab SET list_item='[{"id":1270,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]},{"id":1081,"quantity":5,"options":[]}]' WHERE id=57;

-- 150.000 (id 58)
UPDATE phuc_loi_tab SET list_item='[{"id":1917,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1076,"quantity":5,"options":[]}]' WHERE id=58;

-- 200.000 (id 59)
UPDATE phuc_loi_tab SET list_item='[{"id":1918,"quantity":1,"options":[]},{"id":1132,"quantity":1,"options":[]},{"id":1125,"quantity":1,"options":[]}]' WHERE id=59;

-- 300.000 (id 60)
UPDATE phuc_loi_tab SET list_item='[{"id":1217,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]},{"id":1866,"quantity":1,"options":[]}]' WHERE id=60;

-- 500.000 (id 61)
UPDATE phuc_loi_tab SET list_item='[{"id":1741,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]},{"id":671,"quantity":1,"options":[]}]' WHERE id=61;

-- 1.000.000 (id 62)
UPDATE phuc_loi_tab SET list_item='[{"id":1745,"quantity":1,"options":[]},{"id":1123,"quantity":1,"options":[]},{"id":1855,"quantity":1,"options":[]}]' WHERE id=62;

-- 2.000.000 Coin (id 87) — SET TƯ TƯỢNG (1919..1923) — giống mốc 50.000.000 bên tab 2.
--  MỐC FULL SET -> ĐI MỘT MÌNH (cần >= 6 ô trống).
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(87, 3, '2.000.000', 2000000, 1,
'[{"id":1919,"quantity":1,"options":[{"id":50,"param":900},{"id":77,"param":900},{"id":94,"param":100},{"id":95,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1920,"quantity":1,"options":[{"id":77,"param":900},{"id":103,"param":900},{"id":173,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1921,"quantity":1,"options":[{"id":50,"param":900},{"id":103,"param":900},{"id":5,"param":35},{"id":14,"param":10},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1922,"quantity":1,"options":[{"id":50,"param":900},{"id":77,"param":900},{"id":108,"param":20},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},
  {"id":1923,"quantity":1,"options":[{"id":103,"param":900},{"id":50,"param":900},{"id":97,"param":30},{"id":105,"param":1},{"id":117,"param":25},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 3.000.000 Coin (id 88) — đồ đại gia
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(88, 3, '3.000.000', 3000000, 1,
'[{"id":1405,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1666,"quantity":10,"options":[]},
  {"id":1667,"quantity":10,"options":[]},
  {"id":1078,"quantity":5,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 5.000.000 Coin (id 89) — SET NGŨ HÀNH (1924..1928) + SKH VIP PRO (1856).
--  6 món (5 set + 1 hào quang) -> cần >= 7 Ô TRỐNG trong hành trang.
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(89, 3, '5.000.000', 5000000, 1,
'[{"id":1924,"quantity":1,"options":[{"id":50,"param":1500},{"id":77,"param":1500},{"id":94,"param":150},{"id":95,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1925,"quantity":1,"options":[{"id":77,"param":1500},{"id":103,"param":1500},{"id":96,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1926,"quantity":1,"options":[{"id":50,"param":1500},{"id":103,"param":1500},{"id":5,"param":60},{"id":14,"param":15},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1927,"quantity":1,"options":[{"id":50,"param":1500},{"id":77,"param":1500},{"id":97,"param":50},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1928,"quantity":1,"options":[{"id":103,"param":1500},{"id":50,"param":1500},{"id":105,"param":1},{"id":98,"param":30},{"id":117,"param":40},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":30,"param":1}]},
  {"id":1856,"quantity":1,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

COMMIT;

-- =====================================================================
-- PHẦN 5: KIỂM TRA SAU KHI CHẠY
-- =====================================================================
-- 5.1 Mốc còn rỗng / JSON sai trong 3 tab (mong đợi: 0 dòng)
SELECT id, tab_id, name, JSON_VALID(list_item) AS json_ok, JSON_LENGTH(list_item) AS so_mon
FROM phuc_loi_tab WHERE tab_id IN (1,2,3)
  AND (JSON_VALID(list_item) = 0 OR JSON_LENGTH(list_item) = 0) ORDER BY tab_id, id;

-- 5.2 Số mốc mỗi tab (mong đợi 7 / 14 / 12) + tổng số mốc (phải <= 127)
SELECT tab_id, COUNT(*) AS so_moc FROM phuc_loi_tab WHERE tab_id IN (1,2,3) GROUP BY tab_id;
SELECT COUNT(*) AS tong_so_moc FROM phuc_loi_tab;

-- 5.3 Số option (phải <= 255) + đậu vĩnh viễn
SELECT COUNT(*) AS so_option FROM item_option_template;
SELECT id, NAME, TYPE FROM item_option_template WHERE id = 254;
SELECT id, NAME, TYPE, icon_id, comment FROM item_template WHERE comment = 'dau vinh vien';

-- 5.4 set_config sau khi sửa
SELECT id, name, option_id, hp_pct, ki_pct, dame_pct, def_pct, crit_add, crit_dmg_pct,
       dame_base, hp_base, ki_base, def_base FROM set_config ORDER BY id;

-- 5.5 Yêu cầu ô trống của từng mốc tab 2/3 (conChoTrong cần số ô trống > số item)
SELECT id, tab_id, name, JSON_LENGTH(list_item) AS so_item,
       CONCAT('cần >= ', JSON_LENGTH(list_item) + 1, ' ô trống') AS yeu_cau
FROM phuc_loi_tab WHERE tab_id IN (2,3) ORDER BY tab_id, id;

-- 5.6 Đối chiếu: mọi item id dùng trong 3 tab có tồn tại trong item_template?
SELECT t.id AS item_id_thieu
FROM (SELECT 381 id UNION SELECT 382 UNION SELECT 383 UNION SELECT 384 UNION SELECT 454
      UNION SELECT 457 UNION SELECT 522 UNION SELECT 523 UNION SELECT 671 UNION SELECT 674
      UNION SELECT 987 UNION SELECT 1076 UNION SELECT 1078 UNION SELECT 1081 UNION SELECT 1099
      UNION SELECT 1100 UNION SELECT 1101 UNION SELECT 1102 UNION SELECT 1108 UNION SELECT 1109
      UNION SELECT 1123 UNION SELECT 1124 UNION SELECT 1125 UNION SELECT 1132 UNION SELECT 1159
      UNION SELECT 1174 UNION SELECT 1175 UNION SELECT 1176 UNION SELECT 1177 UNION SELECT 1178
      UNION SELECT 1217 UNION SELECT 1224 UNION SELECT 1225 UNION SELECT 1270 UNION SELECT 1271
      UNION SELECT 1405 UNION SELECT 1666 UNION SELECT 1667 UNION SELECT 1730 UNION SELECT 1731
      UNION SELECT 1732 UNION SELECT 1733 UNION SELECT 1734 UNION SELECT 1736 UNION SELECT 1741
      UNION SELECT 1745 UNION SELECT 1855 UNION SELECT 1856 UNION SELECT 1866 UNION SELECT 1867
      UNION SELECT 1885 UNION SELECT 1916 UNION SELECT 1917 UNION SELECT 1918 UNION SELECT 1919
      UNION SELECT 1920 UNION SELECT 1921 UNION SELECT 1922 UNION SELECT 1923 UNION SELECT 1924
      UNION SELECT 1925 UNION SELECT 1926 UNION SELECT 1927 UNION SELECT 1928) t
LEFT JOIN item_template i ON i.id = t.id
WHERE i.id IS NULL;   -- mong đợi: 0 dòng
