-- Cai trang VLT 021 (spine g13_dijiang) - chen DB
-- part: chen truc tiep bang `mysql < build/caitrang_part.txt` (2146=head, 2147=body, 2148=leg)
-- LUU Y: dx/dy da duoc gen trong byte ±127 (han che cau truc part cua client)
INSERT IGNORE INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1936, 5, 3, 'Cải trang VLT 021', 'Hóa thân thành Đế Giang', 0, 32671, -1, 0, 0, 0, 0, 2146, 2147, 2148, 1, 1, NULL, 0);

-- anh avatar khi nang cap/nhin kho (head part -> avatar)
INSERT IGNORE INTO head_avatar (head_id, avatar_id) VALUES (2146, 32672);
