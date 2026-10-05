-- Thông Thiên Giáo Chủ (spine g13_tongtianjiaozhu4) - chen DB
-- part: chen truc tiep bang `mysql < build/caitrang_part.txt` (2152=head, 2153=body, 2154=leg)
-- bytefit: 0/34 vuot byte (dx -44..-23, dy -42..-5), spine goc 499px -> bake 150px x2
-- icon: 32709..32742 (34 part), 32743 = icon item, 32744 = avatar
INSERT IGNORE INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1938, 5, 3, 'Thông Thiên Giáo Chủ', 'Hóa thân thành Thông Thiên Giáo Chủ', 0, 32743, -1, 0, 0, 0, 0, 2152, 2153, 2154, 1, 1, NULL, 0);

-- anh avatar khi nang cap/nhin kho (head part -> avatar)
INSERT IGNORE INTO head_avatar (head_id, avatar_id) VALUES (2152, 32744);
