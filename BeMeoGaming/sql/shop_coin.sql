-- ============================================================
-- 5 tab SHOP cho Phuc Loi (id 4-8) + 1 moc mac dinh moi tab
-- Chay tren DB hondaodragon. An toan chay lai (INSERT IGNORE / UPDATE).
--
-- currency_item = tien te tra tien khi mua:
--   0    = Coin
--   -1   = Vi Ngoc Xanh (inventory.gem), -2 = Vi Hong Ngoc (inventory.ruby)
--   1271 = Luong Bac, 1270 = Luong Vuang, 457 = Thoi Vuang, 1150 = Co 4 La
--   (tr tuong ung trong HAN TRANG)
-- Moc active=1 + tab id>=3 => mua bang tien te cua tab, mua lai KHONG gioi han.
-- ============================================================

-- CHAY 1 LAN (bi loi neu chay lai da co column):
ALTER TABLE phuc_loi ADD COLUMN currency_item INT NOT NULL DEFAULT 0;

INSERT IGNORE INTO phuc_loi (id, name, max_tab, id_tab, info_phucloi, action, tich_luy) VALUES
(4, 'Shop lượng bạc', 1, 4, 'Dùng Lượng Bạc mua vật phẩm', 1, 'Lượng Bạc'),
(5, 'Shop lượng vàng', 1, 5, 'Dùng Lượng Vàng mua vật phẩm', 1, 'Lượng Vàng'),
(6, 'Shop thỏi vàng', 1, 6, 'Dùng Thỏi Vàng mua vật phẩm', 1, 'Thỏi Vàng'),
(7, 'Shop ngọc xanh', 1, 7, 'Dùng Ngọc mua vật phẩm', 1, 'Ngọc Xanh'),
(8, 'Shop Cỏ 4 lá', 1, 8, 'Dùng Cỏ 4 Lá mua vật phẩm', 1, 'Cỏ 4 Lá');

-- Gan tien te cho 5 tab shop
UPDATE phuc_loi SET currency_item = 1271, tich_luy = 'Lượng Bạc'  WHERE id = 4;
UPDATE phuc_loi SET currency_item = 1270, tich_luy = 'Lượng Vàng' WHERE id = 5;
UPDATE phuc_loi SET currency_item = 457,  tich_luy = 'Thỏi Vàng' WHERE id = 6;
UPDATE phuc_loi SET currency_item = -1,  tich_luy = 'Ngọc Xanh'  WHERE id = 7;
UPDATE phuc_loi SET currency_item = 1150, tich_luy = 'Cỏ 4 Lá'   WHERE id = 8;

-- moc: id, tab_id, ten, gia (so tien te), active=1, qua ban
INSERT IGNORE INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES
(14, 4, 'Mua Lượng Bạc', 1000, 1, '[{"id":1271,"quantity":10,"options":[]}]'),
(15, 5, 'Mua Lượng Vàng', 1000, 1, '[{"id":1270,"quantity":10,"options":[]}]'),
(16, 6, 'Mua Thỏi Vàng', 1000, 1, '[{"id":457,"quantity":1,"options":[]}]'),
(17, 7, 'Mua Ngọc Xanh', 1000, 1, '[{"id":77,"quantity":100,"options":[]}]'),
(18, 8, 'Mua Cỏ 4 Lá', 1000, 1, '[{"id":1150,"quantity":10,"options":[]}]');
