-- Cai trang VLT 052 (spine g13_juitianxuannv) - chen DB
-- LUU Y: part 2143-2145 chen rieng bang `mysql < build/caitrang_part.txt` (dx/dy da duoc
--       gen ra trong byte ±127 - han che cau truc part cua client)
-- item template 1935 (max hien tai = 1934)
INSERT IGNORE INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1935, 5, 3, 'Cải trang VLT 052', 'Hóa thân thành Cửu Thiên Huyền Nữ', 0, 32635, -1, 0, 0, 0, 0, 2143, 2144, 2145, 1, 1, NULL, 0);

-- anh avatar khi nang cap/nhin kho (head part -> avatar)
INSERT IGNORE INTO head_avatar (head_id, avatar_id) VALUES (2143, 32636);

-- doi ten pet 1934 sang ten co dau (quy uoc dat ten item)
UPDATE item_template SET NAME = 'Ngọc Thố Tinh', description = 'Hiệu ứng đi theo đuôi người chơi (pet H21601)' WHERE id = 1934;
