-- Vong Xoay Hu Khong - hao quang hoi den duoi chan (item 1933)
-- 10 tick / 8 giai doan: f1 khoi dong, f2 xoay dan, f3 nut ve,
--   f4a/f4b/f5a/f5b DINH DIEM (4 tick = lau hon, moi tick set seed rieng -> set nhay lien tuc),
--   f6 ha nhiet, f7 tan bien, f10 = f1 (vong lap khong giat)
-- Frame: data/icon_botnet/x{2,3,4}/32501..32510 + icon 32511 (300x300 x2)
-- Chay: cd /c/xampp/mysql/bin && ./mysql.exe --default-character-set=utf8mb4 -h127.0.0.1 -uroot hondaodragon < sql/vong_xoay_hu_khong.sql

INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (
  197,
  '32501,32502,32503,32504,32505,32506,32507,32508,32509,32510',
  'Vong Xoay Hu Khong',
  -1, -1, 32511
)
ON DUPLICATE KEY UPDATE icon_data = VALUES(icon_data), NAME = VALUES(NAME), icon_id = VALUES(icon_id);

INSERT INTO item_template
  (id, TYPE, gender, NAME, description, level, icon_id, part,
   is_up_to_up, power_require, gold, gem, head, body, leg,
   is_up_to_up_over_99, can_trade, comment, ruby)
VALUES
  (1933, 11, 3, 'Vòng Xoáy Hư Không', 'Ho den hu khong lon xoay duoi chan, set do va manh vo khong gian bay quanh', 0, 32511, 197,
   0, 0, 0, 0, -1, -1, -1,
   1, 1, NULL, 0)
ON DUPLICATE KEY UPDATE NAME = VALUES(NAME), description = VALUES(description),
  icon_id = VALUES(icon_id), part = VALUES(part), TYPE = VALUES(TYPE);
