-- Goku Vo Cuc - hao quang Goku Ultra Instinct (item moi 1932)
-- Frame: data/icon_botnet/x{2,3,4}/32458..32499 (42 frame) + icon 32500
-- Chay: cd /c/xampp/mysql/bin && ./mysql.exe --default-character-set=utf8mb4 -h127.0.0.1 -uroot hondaodragon < sql/goku_vo_cuc.sql

INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (
  196,
  '32458,32459,32460,32461,32462,32463,32464,32465,32466,32467,32468,32469,32470,32471,32472,32473,32474,32475,32476,32477,32478,32479,32480,32481,32482,32483,32484,32485,32486,32487,32488,32489,32490,32491,32492,32493,32494,32495,32496,32497,32498,32499',
  'Goku Vo Cuc',
  -1, -1, 32500
)
ON DUPLICATE KEY UPDATE icon_data = VALUES(icon_data), NAME = VALUES(NAME), icon_id = VALUES(icon_id);

INSERT INTO item_template
  (id, TYPE, gender, NAME, description, level, icon_id, part,
   is_up_to_up, power_require, gold, gem, head, body, leg,
   is_up_to_up_over_99, can_trade, comment, ruby)
VALUES
  (1932, 11, 3, 'Goku Vô Cực', 'Hao quang Goku Vo Cuc bao quanh than', 0, 32500, 196,
   0, 0, 0, 0, -1, -1, -1,
   1, 1, NULL, 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), description = VALUES(description),
  icon_id = VALUES(icon_id), part = VALUES(part), TYPE = VALUES(TYPE);
