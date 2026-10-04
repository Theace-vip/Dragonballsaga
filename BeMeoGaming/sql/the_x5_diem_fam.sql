-- [The x5 diem fam 27/09/2026] co anh 32327.png -> them lai item 1918
-- Icon da scale xong: data/icon_botnet/x2|x3|x4/32327.png
-- Server tu keo dai smallimage_version khi boot (DataGame.extendSmallImageVersion)
-- va can bump vsItem de client tai lai item template.
INSERT INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part,
    is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES
 (1918, 29, 3, 'Thẻ X5 Điểm Farm',
  'Dùng để nhân x5 điểm Fam nhận được khi giết quái trong 60 phút',
  0, 32327, 0, 1, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), description = VALUES(description),
    icon_id = VALUES(icon_id), TYPE = VALUES(TYPE);
