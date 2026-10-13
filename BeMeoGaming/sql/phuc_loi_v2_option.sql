-- ============ DA THAY THE - KHONG CHAY FILE NAY NUA ============
-- Dung sql/phuc_loi_v3_option.sql (ban chot: param option 50/77/103 = % that x 10,
--  option cu 158/75/35 da bo, moc 53/90/92/93/87/89 theo muc 5 cua ke hoach).
-- File nay giu lai chi de doi chieu lich su.
-- =====================================================================
-- PHUC LOI v2 - OPTION % DEP + SET TU TUONG/NGU HANH + DAU VINH VIEN
-- File NHAP - CHUA CHAY. Doc KE_HOACH_QUA_PHUC_LOI.md truoc khi chay.
-- DB: hondaodragon
--
-- THU TU THI HANH:
--   PHAN 1 : tao 1 option moi (254) = dau vinh vien   -> SQL, ROI KHOI DONG LAI server
--   PHAN 2 : set_config: %Suc danh cho Tu Tuong/Ngu Hanh -> SQL, Panel Reload
--   [SUA HE SO] r_opt77_factor / r_opt103_factor = 100 (Panel -> StatRate)
--   PHAN 3 : tab 1 - diem danh tuan (id 2..8)          -> SQL, Panel Reload
--   PHAN 4 : tab 2 - tich nap (id 44..53 + 90..93)     -> SQL, Panel Reload
--   PHAN 5 : tab 3 - qua Coin (id 54..62 + 87..89)     -> SQL, Panel Reload
--   PHAN 6 : kiem tra
--   (GD2 - CODE: cot dau_vinh_vien + eatPea + NPoint -> muc 3 cua ke hoach)
--
-- !!! GIOI HAN QUAN TRONG: he thong chi chiu duoc TOI DA 255 OPTION
--   (client doc so option bang 1 BYTE: DataGame/ItemData writeByte(ITEM_OPTION_TEMPLATES.size())).
--   Bang item_option_template dang co 254 dong (id 0..253) => CHI THEM DUOC 1 OPTION (id 254).
--   Vi vay 2 set moi KHONG tao marker rieng, ma dung lai marker co san:
--       TU TUONG 149 (Thanh Long) 150 (Chu Tuoc) 151 (Bach Ho) 152 (Huyen Vu)
--       NGU HANH 153 (Kim) 154 (Moc) 155 (Tho) 156 (Thuy) 157 (Hoa)
--   => bonus doc tu bang set_config (da co san 9 dong), khong can them dong moi.
--
-- QUY TAC OPTION (doi chieu code src/player/NPoint.java dong 536-755):
--   42,43,44,45,46,249,250,251,252 bi COMMENT trong code -> VO DUNG (khong dung).
--   %Sinh luc 77 (param = % thuc SAU KHI sua r_opt77_factor=100)
--   %MaNa 103 (param = % thuc SAU KHI sua r_opt103_factor=100)
--   %Suc danh 239 (% thuc, 1:1 san) | %Giap 94 | %STCM 5 | Chi mang 14
--   Hut ST->HP 95 / MP 96 | Phan ST 97 | Xuyen giap 98/99 | Hut HP,KI xq 5s 8
--   Ne don 108 | Vo hinh 105 | Dep %SD xung quanh 117 | +%SD len Boss 204
--   Khoa giao dich 30 param 1 | Full set 66 (+30% SD) | Nguyet An 35 (+5% KI)
--   254 DAU VINH VIEN (moi)
-- =====================================================================

-- ---------------------------------------------------------------------
-- PHAN 0: XEM HIEN TRANG (nen backup truoc: export 3 bang
--   phuc_loi_tab, set_config, item_option_template bang phpMyAdmin)
-- ---------------------------------------------------------------------
SELECT id, tab_id, name, max_count, active, CHAR_LENGTH(list_item) AS do_dai
FROM phuc_loi_tab WHERE tab_id IN (1, 2, 3) ORDER BY tab_id, id;
SELECT COUNT(*) AS tong_so_moc FROM phuc_loi_tab;                    -- phai < 127 (hien 78)
SELECT COUNT(*) AS so_option_hien_co FROM item_option_template;      -- phai la 254 (id 0..253)
SELECT id, name, option_id, hp_pct, ki_pct, dame_pct, def_pct, crit_add, crit_dmg_pct FROM set_config ORDER BY id;

START TRANSACTION;

-- =====================================================================
-- PHAN 1: TAO 1 OPTION MOI DUY NHAT (254)
--   id 254 = vi tri tiep theo trong mang -> dung quy tac "id = vi tri".
--   Sau khi chay: KHOI DONG LAI SERVER (hoac tao option qua Panel, Panel tu nap
--   vao bo nho ngay), roi nguoi choi DANG NHAP LAI de client nhan option moi.
-- =====================================================================
INSERT INTO item_option_template (id, NAME, type) VALUES
 (254, '「✦ĐẬU VĨNH VIỄN✦」 ăn +1% HP,SĐ,KI (vĩnh viễn)', 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), type = VALUES(type);

-- =====================================================================
-- PHAN 2A: % SUC DANH cho TU TUONG (149-152) va NGU HANH (153-157)
--   Tu Tuong dang la % (KI/HP/SD/Giap 500/500/1000/500) -> CHI THEM dame_pct.
--   Ngu Hanh dang la flat (5tr/10tr) -> CHUYEN sang % va THEM dame_pct.
--   Co hieu luc ngay sau khi Panel -> Reload (khong can build).
-- =====================================================================
-- Tu Tuong
UPDATE set_config SET dame_pct = 200                       WHERE id = 1; -- Thanh Long: +200% SD (them)
UPDATE set_config SET dame_pct = 200                       WHERE id = 2; -- Chu Tuoc  : +200% SD (them)
UPDATE set_config SET crit_add = 5                         WHERE id = 3; -- Bach Ho   : da co 1000% SD, them +5 chi mang
UPDATE set_config SET dame_pct = 200                       WHERE id = 4; -- Huyen Vu  : +200% SD (them)

-- Ngu Hanh: bo flat, dung %
UPDATE set_config SET dame_base = 0, dame_pct = 400                    WHERE id = 5; -- Kim : +400% SD
UPDATE set_config SET hp_base = 0, hp_pct = 400, dame_pct = 100        WHERE id = 6; -- Moc : +400% HP, +100% SD
UPDATE set_config SET ki_base = 0, ki_pct = 400, dame_pct = 100        WHERE id = 7; -- Tho : +400% KI, +100% SD
UPDATE set_config SET def_base = 0, def_pct = 400, dame_pct = 100      WHERE id = 8; -- Thuy: +400% Giap, +100% SD
UPDATE set_config SET crit_dmg_pct = 200, crit_add = 10, dame_pct = 150 WHERE id = 9; -- Hoa: +200% STCM, +10 chi mang, +150% SD

-- =====================================================================
-- PHAN 2B: KHONG them dong set_config moi
--   Ly do: chi con 1 slot option (da dung cho 254) nen khong the tao marker
--   rieng cho set moi. Hai set moi dung lai marker TU TUONG / NGU HANH:
--       Tier 3 (Hac Am Loan Luu)  -> 149 150 151 152          (4 bonus set)
--       Tier 4 (Hon Don Vo Cuc)   -> 149 150 151 152 153..157 (9 bonus set)
--   Neu sau nay admin muon bonus RIENG cho 2 set nay: nang gioi han option
--   (doi writeByte -> writeShort o ItemData + client) roi tao marker moi,
--   hoac doi 2 option "Khảm Đá" 42/43 dang VO DUNG trong code (LUU Y: 42/43
--   dang co tren 1 vat pham cua nhan vat admin va 1 dong tab_shop).
-- =====================================================================

-- ---------------------------------------------------------------------
-- PHAN 2C: (TUY CHON) MON CU mang option 77 / 103 se MANH LEN 10 LAN sau khi
--   doi r_opt77_factor / r_opt103_factor sang 100 (truoc day param bi chia 10).
--   DB hien chi co 2 nhan vat. Muon giu nguyen suc manh cu thi chia 10 param
--   cua option do trong du lieu nhan vat TRUOC khi doi he so. Kiem tra:
--       SELECT id, name FROM player WHERE items_body LIKE '%[77,%' OR items_bag LIKE '%[77,%';
--   (Sua JSON thu cong/script; khong co UPDATE tu dong o day de tranh pha du lieu.)
-- ---------------------------------------------------------------------

-- =====================================================================
-- PHAN 3: TAB 1 - DIEM DANH TUAN (active=0, max_count = so THU: T2=2..T7=7, CN=1)
--   Tab 1 so SANH BANG DUNG (count != max_count) -> KHONG doi max_count.
--   Dau than phuc loi (523 + option 254) cong vinh vien khi an.
-- =====================================================================
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":1000,"options":[]},{"id":523,"quantity":20,"options":[{"id":254,"param":1}]}]' WHERE id=2;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":2000,"options":[]},{"id":523,"quantity":20,"options":[{"id":254,"param":1}]},{"id":1916,"quantity":1,"options":[]}]' WHERE id=3;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":2000,"options":[]},{"id":381,"quantity":10,"options":[]},{"id":382,"quantity":10,"options":[]},{"id":383,"quantity":10,"options":[]}]' WHERE id=4;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1099,"quantity":5,"options":[]},{"id":1100,"quantity":5,"options":[]},{"id":1102,"quantity":5,"options":[]}]' WHERE id=5;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1108,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]}]' WHERE id=6;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":987,"quantity":1,"options":[]}]' WHERE id=7;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]}]' WHERE id=8;

-- =====================================================================
-- PHAN 4: TAB 2 - TICH NAP (active=0, max_count = COIN DA NAP, nhan 1 lan)
--   id 44..53 dang RONG ([]), dien lai; them 4 moc dai gia 90..93.
--   LUU Y: conChoTrong yeu cau SO ITEM < SO O TRONG (du 1 o):
--          5 mon -> can >= 6 o trong;  4 mon -> can >= 5 o trong.
-- =====================================================================
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":523,"quantity":30,"options":[{"id":254,"param":1}]},{"id":1916,"quantity":1,"options":[]}]' WHERE id=44;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=45;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]}]' WHERE id=46;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":20000,"options":[]},{"id":1270,"quantity":5,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1217,"quantity":1,"options":[]}]' WHERE id=47;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":50000,"options":[]},{"id":1270,"quantity":10,"options":[]},{"id":457,"quantity":10,"options":[]},{"id":1132,"quantity":1,"options":[]}]' WHERE id=48;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":20,"options":[]},{"id":1124,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]}]' WHERE id=49;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":30,"options":[]},{"id":454,"quantity":1,"options":[]},{"id":1741,"quantity":1,"options":[]}]' WHERE id=50;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":671,"quantity":1,"options":[]},{"id":1736,"quantity":1,"options":[]}]' WHERE id=51;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":1159,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]}]' WHERE id=52;

-- 5.000.000 (id 53) - SET HAC AM - TIER 1 (1174..1178)
--   chung: 158, 75, 66 (full set +30% SD), 35:10 (Nguyet An +5% KI), 30:1 (khoa giao dich)
UPDATE phuc_loi_tab SET list_item=
'[{"id":1174,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":94,"param":100},{"id":47,"param":100000000}]},
  {"id":1175,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":77,"param":20},{"id":6,"param":100000000}]},
  {"id":1176,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":239,"param":20},{"id":0,"param":10000000}]},
  {"id":1177,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":103,"param":20},{"id":7,"param":100000000}]},
  {"id":1178,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":5,"param":10},{"id":14,"param":20}]}]'
WHERE id=53;

-- 10.000.000 (id 90) - SET NGUYEN THUY - TIER 2 (1730..1734): so % x2.5, gang them 204 (+%SD len Boss)
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(90, 2, '10.000.000', 10000000, 0,
'[{"id":1730,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":94,"param":200},{"id":47,"param":200000000}]},
  {"id":1731,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":77,"param":50},{"id":6,"param":200000000}]},
  {"id":1732,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":239,"param":50},{"id":0,"param":20000000},{"id":204,"param":100}]},
  {"id":1733,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":103,"param":50},{"id":7,"param":200000000}]},
  {"id":1734,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":5,"param":20},{"id":14,"param":30}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 20.000.000 (id 91) - do dai gia
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(91, 2, '20.000.000', 20000000, 0,
'[{"id":1885,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1224,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1225,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1123,"quantity":1,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 50.000.000 (id 92) - SET HAC AM LOAN LUU - TIER 3 (1919..1923)
--   chung: 158, 75, 66, 35:10, 30:1 + TU TUONG 149 150 151 152
--   => mac du 5/5 kich hoat 4 bonus set trong set_config (Thanh Long/Chu Tuoc/Bach Ho/Huyen Vu)
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(92, 2, '50.000.000', 50000000, 0,
'[{"id":1919,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":94,"param":300},{"id":47,"param":500000000}]},
  {"id":1920,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":77,"param":100},{"id":6,"param":500000000}]},
  {"id":1921,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":239,"param":100},{"id":0,"param":50000000},{"id":204,"param":100}]},
  {"id":1922,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":103,"param":100},{"id":7,"param":500000000}]},
  {"id":1923,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":5,"param":30},{"id":14,"param":40},{"id":8,"param":20},{"id":97,"param":30}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 100.000.000 (id 93) - SET HON DON VO CUC - TIER 4 (1924..1928)
--   chung: 158, 75, 66, 35:10, 30:1 + TU TUONG 149-152 + NGU HANH 153-157
--   => mac du 5/5 kich hoat CA 9 bonus set trong set_config + bo option rieng manh nhat
--   (neu client cat bot dong hien thi thi bo {75} hoac bot 153-157)
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(93, 2, '100.000.000', 100000000, 0,
'[{"id":1924,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":94,"param":500},{"id":47,"param":1000000000},{"id":95,"param":20}]},
  {"id":1925,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":77,"param":200},{"id":6,"param":1000000000},{"id":96,"param":20},{"id":108,"param":20}]},
  {"id":1926,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":239,"param":200},{"id":0,"param":100000000},{"id":204,"param":100},{"id":98,"param":20}]},
  {"id":1927,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":103,"param":200},{"id":7,"param":1000000000},{"id":99,"param":20}]},
  {"id":1928,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":5,"param":50},{"id":14,"param":60},{"id":97,"param":20},{"id":105,"param":1},{"id":117,"param":50}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- =====================================================================
-- PHAN 5: TAB 3 - QUA COIN (active=1, max_count = GIA COIN, mua lai vo han)
-- =====================================================================
UPDATE phuc_loi_tab SET list_item='[{"id":523,"quantity":100,"options":[{"id":254,"param":1}]},{"id":381,"quantity":50,"options":[]},{"id":382,"quantity":50,"options":[]},{"id":383,"quantity":50,"options":[]}]' WHERE id=54;

UPDATE phuc_loi_tab SET list_item='[{"id":384,"quantity":50,"options":[]},{"id":1099,"quantity":50,"options":[]},{"id":1100,"quantity":50,"options":[]},{"id":1101,"quantity":50,"options":[]}]' WHERE id=55;

UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":1000,"options":[]},{"id":1916,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=56;

UPDATE phuc_loi_tab SET list_item='[{"id":1270,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]},{"id":1081,"quantity":5,"options":[]}]' WHERE id=57;

UPDATE phuc_loi_tab SET list_item='[{"id":1917,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1076,"quantity":5,"options":[]}]' WHERE id=58;

UPDATE phuc_loi_tab SET list_item='[{"id":1918,"quantity":1,"options":[]},{"id":1132,"quantity":1,"options":[]},{"id":1125,"quantity":1,"options":[]}]' WHERE id=59;

UPDATE phuc_loi_tab SET list_item='[{"id":1217,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]},{"id":1866,"quantity":1,"options":[]}]' WHERE id=60;

UPDATE phuc_loi_tab SET list_item='[{"id":1741,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]},{"id":671,"quantity":1,"options":[]}]' WHERE id=61;

UPDATE phuc_loi_tab SET list_item='[{"id":1745,"quantity":1,"options":[]},{"id":1123,"quantity":1,"options":[]},{"id":1855,"quantity":1,"options":[]}]' WHERE id=62;

-- 2.000.000 Coin (id 87) - SET HAC AM LOAN LUU - TIER 3 (giong moc 50.000.000 ben tab 2)
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(87, 3, '2.000.000', 2000000, 1,
'[{"id":1919,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":94,"param":300},{"id":47,"param":500000000}]},
  {"id":1920,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":77,"param":100},{"id":6,"param":500000000}]},
  {"id":1921,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":239,"param":100},{"id":0,"param":50000000},{"id":204,"param":100}]},
  {"id":1922,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":103,"param":100},{"id":7,"param":500000000}]},
  {"id":1923,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":5,"param":30},{"id":14,"param":40},{"id":8,"param":20},{"id":97,"param":30}]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 3.000.000 Coin (id 88) - do dai gia
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(88, 3, '3.000.000', 3000000, 1,
'[{"id":1405,"quantity":1,"options":[]},
  {"id":1666,"quantity":10,"options":[]},
  {"id":1667,"quantity":10,"options":[]},
  {"id":1078,"quantity":5,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

-- 5.000.000 Coin (id 89) - SET HON DON VO CUC - TIER 4 (5 mon) + SKH VIP PRO
--   LUU Y: 6 mon -> can >= 7 O TRONG trong hanh trang
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(89, 3, '5.000.000', 5000000, 1,
'[{"id":1924,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":94,"param":500},{"id":47,"param":1000000000},{"id":95,"param":20}]},
  {"id":1925,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":77,"param":200},{"id":6,"param":1000000000},{"id":96,"param":20},{"id":108,"param":20}]},
  {"id":1926,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":239,"param":200},{"id":0,"param":100000000},{"id":204,"param":100},{"id":98,"param":20}]},
  {"id":1927,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":103,"param":200},{"id":7,"param":1000000000},{"id":99,"param":20}]},
  {"id":1928,"quantity":1,"options":[{"id":158,"param":0},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":153,"param":0},{"id":154,"param":0},{"id":155,"param":0},{"id":156,"param":0},{"id":157,"param":0},{"id":5,"param":50},{"id":14,"param":60},{"id":97,"param":20},{"id":105,"param":1},{"id":117,"param":50}]},
  {"id":1856,"quantity":1,"options":[]}]')
ON DUPLICATE KEY UPDATE tab_id=VALUES(tab_id), name=VALUES(name), max_count=VALUES(max_count),
 active=VALUES(active), list_item=VALUES(list_item);

COMMIT;

-- =====================================================================
-- PHAN 6: KIEM TRA (chay sau COMMIT)
-- =====================================================================
-- 6.1 Moc rong hoac JSON sai trong 3 tab (mong doi: 0 dong):
SELECT id, tab_id, name, JSON_VALID(list_item) AS json_ok, JSON_LENGTH(list_item) AS so_mon
FROM phuc_loi_tab WHERE tab_id IN (1,2,3)
  AND (JSON_VALID(list_item) = 0 OR JSON_LENGTH(list_item) = 0) ORDER BY tab_id, id;

-- 6.2 So moc moi tab (mong doi 7 / 14 / 12) va tong so moc (phai < 127):
SELECT tab_id, COUNT(*) AS so_moc FROM phuc_loi_tab WHERE tab_id IN (1,2,3) GROUP BY tab_id;
SELECT COUNT(*) AS tong_moc FROM phuc_loi_tab;

-- 6.3 TONG SO OPTION (PHAI <= 255 - keo client doc 1 byte bi sai):
SELECT COUNT(*) AS so_option_hien_co FROM item_option_template;

-- 6.4 Option moi + set_config sau khi sua:
SELECT id, NAME FROM item_option_template WHERE id = 254;
SELECT id, name, option_id, hp_pct, ki_pct, dame_pct, def_pct, crit_add, crit_dmg_pct,
       dame_base, hp_base, ki_base, def_base FROM set_config ORDER BY id;

-- 6.5 Yeu cau o trong cua tung moc tab 2/3 (conChoTrong: so item < so o trong):
SELECT id, tab_id, name, JSON_LENGTH(list_item) AS so_item,
       CONCAT('can >= ', JSON_LENGTH(list_item) + 1, ' o trong') AS yeu_cau
FROM phuc_loi_tab WHERE tab_id IN (2,3) ORDER BY tab_id, id;

-- 6.6 Doi chieu cheo item id / option id dang dung trong 3 tab:
SELECT tab_id, id AS moc, JSON_EXTRACT(list_item,'$[*].id') AS ds_item,
       JSON_EXTRACT(list_item,'$[*].options[*].id') AS ds_option
FROM phuc_loi_tab WHERE tab_id IN (1,2,3) ORDER BY tab_id, id;
