-- Cai trang VLT 090 (spine g13_qiongqi_nan) - chen DB
-- part: chen truc tiep bang `mysql < build/caitrang_part.txt` (2149=head, 2150=body, 2151=leg)
-- LUU Y: dx/dy da duoc gen trong byte ±127 (han che cau truc part cua client)
INSERT IGNORE INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1937, 5, 3, 'Cải trang VLT 090', 'Hóa thân thành Cùng Kỳ', 0, 32707, -1, 0, 0, 0, 0, 2149, 2150, 2151, 1, 1, NULL, 0);

-- anh avatar khi nang cap/nhin kho (head part -> avatar)
INSERT IGNORE INTO head_avatar (head_id, avatar_id) VALUES (2149, 32708);
