-- ============ DA THAY THE - KHONG CHAY FILE NAY NUA ============
-- Dung sql/phuc_loi_v3_option.sql (ban chot: param option 50/77/103 = % that x 10,
--  option cu 158/75/35 da bo, moc 53/90/92/93/87/89 theo muc 5 cua ke hoach).
-- File nay giu lai chi de doi chieu lich su.
-- =====================================================================
-- QUA CHO 3 TAB PHUC LOI: Diem danh tuan (tab 1) / Tich nap (tab 2) / Qua Coin (tab 3)
-- File NHAP - CHUA CHAY. Xem KE_HOACH_QUA_PHUC_LOI.md truoc khi chay.
--
-- DB: hondaodragon  |  Bang: phuc_loi (tab) + phuc_loi_tab (moc)
--   tab_id = 1 -> Diem danh tuan : max_count = thu trong tuan (2..7, CN = 1) - KHONG DOI
--   tab_id = 2 -> Tich nap       : max_count = moc tongnap (coin)          - KHONG DOI
--   tab_id = 3 -> Qua Coin       : max_count = GIA bang Coin, active = 1   - KHONG DOI
--   active = 0 -> moc nhan 1 lan (free); active = 1 + tab_id >= 3 -> mua bang coin, mua lai khong gioi han
--
-- QUY TAC OPTION (xem sql/cach_dung_option.sql):
--   0   cong thang suc danh            2   HP + KI (x1000)        6   cong HP (sinh luc)
--   7   cong KI (mana)                 14  % chi mang             47  cong DEF/giap
--   94  % giap (giam sat thuong)       75 + 66  co kich hoat set  158 nhan "Thuoc Tinh Thuong"
--   30  param 1 = KHOA GIAO DICH (chan ban lai) -> dung cho qua nap >= 1M
--   35  dem NGUYET AN: du 5 mon co option nay trong 5 o mac -> +5% KI (param chi de hien thi)
--   66  "Full Set Dragon" hien thi +1500% SD NHUNG server that chi cong +30% SD (key setsaga
--       trong data/config/stat_rate.properties; SetClothes.setsaga == 5 moi kich hoat)
--   34/36 dem TINH AN (+5% SD) / NHAT AN (+5% HP) - muon doi bo thi thay 35 bang 34 hoac 36
--   Pet / linh thu / thu cuoi / hon de / da / the / dau than: options = [] (khong can option)
--
-- LUU Y KY THUAT:
--   - conChoTrong(): can SO O TRONG > SO MON cua moc -> moc 5 mon (full set) can >= 6 o trong
--     => moc tang full set nen dung mot minh, khong kem mon khac.
--   - Mon co option KHONG xep chong (moi mon = 1 o).
--   - Thu tu hien thi trong tab = thu tu id tang dan => moc moi PHAI dung id > 86.
--   - Gioi han 127 moc toan bang (client doc kieu byte). Sau file nay: 78 + 7 = 85 moc.
--   - Chay xong: vao Panel -> Phuc Loi -> Reload (hoac restart server). Khong can build lai.
-- =====================================================================

-- ---------------------------------------------------------------------
-- BUOC 0: BACKUP (chay ngoai mysql: mysqldump -uroot hondaodragon phuc_loi_tab > backup_phuc_loi_tab.sql)
-- Luu lai JSON cu de doi chieu / quay lui:
SELECT id, tab_id, name, max_count, active, list_item FROM phuc_loi_tab ORDER BY id;

START TRANSACTION;

-- =====================================================================
-- BUOC 1: DIEM DANH TUAN (tab 1) - do thong dung, ung dung lon, tang dan toi Chu nhat
-- =====================================================================
-- Thu 2 (id 2): 1.000 Luong Bac + 20 Dau than cap 9
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":1000,"options":[]},{"id":523,"quantity":20,"options":[]}]' WHERE id=2;

-- Thu 3 (id 3): 2.000 Luong Bac + 20 Dau than cap 9 + 1 The X2 Diem Farm
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":2000,"options":[]},{"id":523,"quantity":20,"options":[]},{"id":1916,"quantity":1,"options":[]}]' WHERE id=3;

-- Thu 4 (id 4): 2.000 Luong Bac + 10 Cuong No + 10 Bo Huyet + 10 Bo Khi
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":2000,"options":[]},{"id":381,"quantity":10,"options":[]},{"id":382,"quantity":10,"options":[]},{"id":383,"quantity":10,"options":[]}]' WHERE id=4;

-- Thu 5 (id 5): 3.000 Luong Bac + 5 Cuong No SC + 5 Bo Huyet SC + 5 Giap Xen SC
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1099,"quantity":5,"options":[]},{"id":1100,"quantity":5,"options":[]},{"id":1102,"quantity":5,"options":[]}]' WHERE id=5;

-- Thu 6 (id 6): 3.000 Luong Bac + 1 The Next Nhiem Vu + 1 The Reset Nhiem Vu
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":3000,"options":[]},{"id":1108,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]}]' WHERE id=6;

-- Thu 7 (id 7): 5.000 Luong Bac + 1 Luong Vang + 1 The X3 Diem Farm + 1 Da Bao Ve
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":987,"quantity":1,"options":[]}]' WHERE id=7;

-- Chu nhat (id 8): 10.000 Luong Bac + 2 Luong Vang + 1 The X5 Diem Farm + 1 Bua De Tu
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]}]' WHERE id=8;

-- =====================================================================
-- BUOC 2: TICH NAP (tab 2) - dien 10 moc RONG (44..53). max_count = moc nap, KHONG DOI.
-- Tang dan: moc thap = do tieu hao + tien te, moc cao = do hiem/co option + khoa giao dich.
-- (Khong tang lai SET Than Long 1868-1872: da co o NPC Nap Dau.)
-- =====================================================================
-- 20.000 (id 44): 5.000 Luong Bac + 30 Dau than 9 + 1 The X2 Diem Farm
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":523,"quantity":30,"options":[]},{"id":1916,"quantity":1,"options":[]}]' WHERE id=44;

-- 30.000 (id 45): 5.000 Luong Bac + 1 The X3 Diem Farm + 1 The Next Nhiem Vu
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":5000,"options":[]},{"id":1917,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=45;

-- 50.000 (id 46): 10.000 Luong Bac + 2 Luong Vang + 1 The X5 Diem Farm
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":10000,"options":[]},{"id":1270,"quantity":2,"options":[]},{"id":1918,"quantity":1,"options":[]}]' WHERE id=46;

-- 100.000 (id 47): 20.000 Luong Bac + 5 Luong Vang + 1 Bua De Tu + 1 The VIP
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":20000,"options":[]},{"id":1270,"quantity":5,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1217,"quantity":1,"options":[]}]' WHERE id=47;

-- 500.000 (id 48): 50.000 Luong Bac + 10 Luong Vang + 10 Thoi Vang + 1 The Tang 1 Canh Gioi Tu Tien
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":50000,"options":[]},{"id":1270,"quantity":10,"options":[]},{"id":457,"quantity":10,"options":[]},{"id":1132,"quantity":1,"options":[]}]' WHERE id=48;

-- 1.000.000 (id 49): 100.000 Luong Bac + 20 Luong Vang + 1 Tang 500k CS Goc + 1 Da Than ngu sac
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":20,"options":[]},{"id":1124,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]}]' WHERE id=49;

-- 1.500.000 (id 50): 100.000 Luong Bac + 30 Luong Vang + 1 Bong Tai Potara Cap 1 + 1 Hop qua Danh Hieu
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":100000,"options":[]},{"id":1270,"quantity":30,"options":[]},{"id":454,"quantity":1,"options":[]},{"id":1741,"quantity":1,"options":[]}]' WHERE id=50;

-- 2.000.000 (id 51): 200.000 Luong Bac + 50 Luong Vang + 1 Bua Tri Tue x3 + 1 Linh Thu Ki Lan
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":671,"quantity":1,"options":[]},{"id":1736,"quantity":1,"options":[]}]' WHERE id=51;

-- 3.000.000 (id 52): 200.000 Luong Bac + 50 Luong Vang + 1 Trung De Black Super + 1 Hop qua Cai Trang
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":200000,"options":[]},{"id":1270,"quantity":50,"options":[]},{"id":1159,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]}]' WHERE id=52;

-- 5.000.000 (id 53): SET Hac Am 5 mon (1174-1178). MOC NAY DE MOT MINH (can >= 6 o trong).
-- BO OPTION CHUAN (giong NPC doi do bang Coin dang chay):
--   moi mon : 158:0 (nhan Thuoc Tinh Thuong) + 75:0 (Set kich hoat) + 66:0 (Full Set Dragon)
--             + 35:10 (dem NGUYET AN, du 5 mon = +5% KI) + 30:1 (KHOA giao dich)
--   Ao  1174: 47:<DEF>     Quan 1175: 6:<sinh luc>   Gang 1176: 0:<SD goc>
--   Giay 1177: 7:<mana>   Nhan 1178: 14:<% chi mang>
UPDATE phuc_loi_tab SET list_item=
'[{"id":1174,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1175,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1176,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":50000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1177,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1178,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":35},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]}]'
WHERE id=53;

-- =====================================================================
-- BUOC 3: TICH NAP - THEM 4 MOC DAI GIA (id moi > 86 nen luon nam CUOI tab 2)
-- =====================================================================
-- 10.000.000 (id 90): SET Nguyen Thuy 5 mon (1730-1734) - do cho de tu
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(90, 2, '10.000.000', 10000000, 0,
'[{"id":1730,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":800000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1731,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":800000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1732,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":80000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1733,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":800000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1734,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":50},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]}]');

-- 20.000.000 (id 91): Trung De Broly Super + Da Thien Dao + Da Dia Dao + Tang 1M CS Goc
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(91, 2, '20.000.000', 20000000, 0,
'[{"id":1885,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1224,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1225,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1123,"quantity":1,"options":[]}]');

-- 50.000.000 (id 92): SET Hac Am Loan Luu 5 mon (1919-1923) + khoa
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(92, 2, '50.000.000', 50000000, 0,
'[{"id":1919,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":1500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1920,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":1500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1921,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":150000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1922,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":1500000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1923,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":55},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]}]');

-- 100.000.000 (id 93): SET Hon Don Vo Cuc 5 mon (1924-1928) + khoa
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(93, 2, '100.000.000', 100000000, 0,
'[{"id":1924,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":3000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1925,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":3000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1926,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":300000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1927,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":3000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1928,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":70},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]}]');

-- =====================================================================
-- BUOC 4: QUA COIN (tab 3) - dien 9 moc RONG (54..62). max_count = GIA (Coin), KHONG DOI.
-- Thuong dung -> hiem, gia tang dan. Mua bang Coin, mua lai khong gioi han.
-- =====================================================================
-- 50.000 (id 54): 100 Dau than 9 + 50 Cuong No + 50 Bo Huyet + 50 Bo Khi
UPDATE phuc_loi_tab SET list_item='[{"id":523,"quantity":100,"options":[]},{"id":381,"quantity":50,"options":[]},{"id":382,"quantity":50,"options":[]},{"id":383,"quantity":50,"options":[]}]' WHERE id=54;

-- 50.000 (id 55): 50 Giap Xen bo hung + 50 Cuong No SC + 50 Bo Huyet SC + 50 Bo Khi SC
UPDATE phuc_loi_tab SET list_item='[{"id":384,"quantity":50,"options":[]},{"id":1099,"quantity":50,"options":[]},{"id":1100,"quantity":50,"options":[]},{"id":1101,"quantity":50,"options":[]}]' WHERE id=55;

-- 100.000 (id 56): 1.000 Luong Bac + 1 The X2 Diem Farm + 1 The Next Nhiem Vu
UPDATE phuc_loi_tab SET list_item='[{"id":1271,"quantity":1000,"options":[]},{"id":1916,"quantity":1,"options":[]},{"id":1108,"quantity":1,"options":[]}]' WHERE id=56;

-- 100.000 (id 57): 1 Luong Vang + 1 The Reset Nhiem Vu + 5 Da May Man cap 3
UPDATE phuc_loi_tab SET list_item='[{"id":1270,"quantity":1,"options":[]},{"id":1109,"quantity":1,"options":[]},{"id":1081,"quantity":5,"options":[]}]' WHERE id=57;

-- 150.000 (id 58): 1 The X3 Diem Farm + 1 Bua De Tu + 5 Da Nang Cap cap 3
UPDATE phuc_loi_tab SET list_item='[{"id":1917,"quantity":1,"options":[]},{"id":522,"quantity":1,"options":[]},{"id":1076,"quantity":5,"options":[]}]' WHERE id=58;

-- 200.000 (id 59): 1 The X5 Diem Farm + 1 The Tang 1 Canh Gioi Tu Tien + 1 Tang 200k CS Goc
UPDATE phuc_loi_tab SET list_item='[{"id":1918,"quantity":1,"options":[]},{"id":1132,"quantity":1,"options":[]},{"id":1125,"quantity":1,"options":[]}]' WHERE id=59;

-- 300.000 (id 60): 1 The VIP + 1 Da Than ngu sac + 1 Hop qua Ngoc
UPDATE phuc_loi_tab SET list_item='[{"id":1217,"quantity":1,"options":[]},{"id":674,"quantity":1,"options":[]},{"id":1866,"quantity":1,"options":[]}]' WHERE id=60;

-- 500.000 (id 61): 1 Hop qua Danh Hieu + 1 Hop qua Cai Trang + 1 Bua Tri Tue x3
UPDATE phuc_loi_tab SET list_item='[{"id":1741,"quantity":1,"options":[]},{"id":1867,"quantity":1,"options":[]},{"id":671,"quantity":1,"options":[]}]' WHERE id=61;

-- 1.000.000 (id 62): 1 Hop qua Thu Cuoi + 1 Tang 1M CS Goc + 1 The Chuyen Sinh
UPDATE phuc_loi_tab SET list_item='[{"id":1745,"quantity":1,"options":[]},{"id":1123,"quantity":1,"options":[]},{"id":1855,"quantity":1,"options":[]}]' WHERE id=62;

-- =====================================================================
-- BUOC 5: QUA COIN - THEM 3 MOC HIEM (id > 86 -> nam cuoi tab 3)
-- =====================================================================
-- 2.000.000 (id 87): SET Hac Am Loan Luu 5 mon (1919-1923) + khoa. Moc full set -> de mot minh.
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(87, 3, '2.000.000', 2000000, 1,
'[{"id":1919,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":1000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1920,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":1000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1921,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":100000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1922,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":1000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1923,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":45},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]}]');

-- 3.000.000 (id 88): Bong Tai Potara Cap 3 + 10 Da Than Sac + 10 Da Than quang + 5 Da Nang Cap cap 5
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(88, 3, '3.000.000', 3000000, 1,
'[{"id":1405,"quantity":1,"options":[{"id":30,"param":1}]},
  {"id":1666,"quantity":10,"options":[]},
  {"id":1667,"quantity":10,"options":[]},
  {"id":1078,"quantity":5,"options":[]}]');

-- 5.000.000 (id 89): SET Hon Don Vo Cuc 5 mon (1924-1928) + SKH VIP PRO. Moc full set + 1 mon -> can >= 7 o trong.
INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(89, 3, '5.000.000', 5000000, 1,
'[{"id":1924,"quantity":1,"options":[{"id":158,"param":0},{"id":47,"param":2000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1925,"quantity":1,"options":[{"id":158,"param":0},{"id":6,"param":2000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1926,"quantity":1,"options":[{"id":158,"param":0},{"id":0,"param":200000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1927,"quantity":1,"options":[{"id":158,"param":0},{"id":7,"param":2000000000},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1928,"quantity":1,"options":[{"id":158,"param":0},{"id":14,"param":60},{"id":75,"param":0},{"id":66,"param":0},{"id":35,"param":10},{"id":30,"param":1}]},
  {"id":1856,"quantity":1,"options":[]}]');

COMMIT;

-- =====================================================================
-- BUOC 6: KIEM TRA
-- =====================================================================
-- 6.1 Moc nao con JSON sai hoac con rong:
SELECT id, tab_id, name, max_count, active,
       JSON_VALID(list_item) AS json_ok,
       JSON_LENGTH(list_item) AS so_mon
FROM phuc_loi_tab
WHERE JSON_VALID(list_item) = 0 OR JSON_LENGTH(list_item) = 0
ORDER BY tab_id, id;

-- 6.2 Xem lai 3 tab vua sua (mong doi: tab1 = 7 moc, tab2 = 14 moc, tab3 = 12 moc):
SELECT tab_id, COUNT(*) AS so_moc, SUM(JSON_LENGTH(list_item) = 0) AS moc_rong
FROM phuc_loi_tab WHERE tab_id IN (1,2,3) GROUP BY tab_id;

-- 6.3 Tong so moc toan bang (phai <= 127):
SELECT COUNT(*) AS tong_moc FROM phuc_loi_tab;

-- 6.4 Liet ke id item dang dung trong 3 tab (de doi chieu voi item_template):
--     MariaDB cu khong co JSON_TABLE, nen kiem tra cheo bang tay / bang cau SELECT duoi day.
SELECT tab_id, JSON_EXTRACT(list_item, '$[*].id') AS ds_item_id
FROM phuc_loi_tab WHERE tab_id IN (1,2,3) ORDER BY tab_id, id;

-- 6.5 Danh sach id item + option dang dung (da kiem tra ton tai trong item_template/kg)
--     id item: 381 382 383 384 454 457 522 523 671 674 987 1076 1078 1081 1099 1100 1101 1102
--              1108 1109 1123 1124 1125 1132 1159 1174 1175 1176 1177 1178 1217 1224 1225
--              1270 1271 1405 1666 1667 1730 1731 1732 1733 1734 1736 1741 1745 1855 1856
--              1866 1867 1885 1916 1917 1918 1919 1920 1921 1922 1923 1924 1925 1926 1927 1928
--     id option: 0 6 7 14 30 35 47 66 75 158
--     (bo set: 158 nhan Thuoc Tinh Thuong - 75 Set kich hoat - 66 Full Set Dragon +1500% SD
--      - 35 dem NGUYET AN du 5/5 = +5% KI - 30:1 khoa giao dich;
--      chi so rieng: Ao 47 DEF / Quan 6 sinh luc / Gang 0 SD / Giay 7 mana / Nhan 14 % chi mang)
