-- [The Diem Farm 27/09/2026] them 3 the: x2/x3/x5 diem farm
-- icon_id 32324/32325/32326 -> data/icon_botnet/x2|x3|x4/<icon_id>.png (da tao)
-- vsItem da bump 13 -> 14 trong DataGame de client xoa cache item template
INSERT INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part,
    is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES
 (1916, 29, 3, 'Thẻ X2 Điểm Farm',
  'Dùng để nhân x2 điểm Fam nhận được khi giết quái trong 60 phút',
  0, 32324, 0, 1, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0),
 (1917, 29, 3, 'Thẻ X3 Điểm Farm',
  'Dùng để nhân x3 điểm Fam nhận được khi giết quái trong 60 phút',
  0, 32325, 0, 1, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0),
 (1918, 29, 3, 'Thẻ X5 Điểm Farm',
  'Dùng để nhân x5 điểm Fam nhận được khi giết quái trong 60 phút',
  0, 32326, 0, 1, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), description = VALUES(description), icon_id = VALUES(icon_id), TYPE = VALUES(TYPE);
