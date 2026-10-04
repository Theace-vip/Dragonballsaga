-- Cuu Thien Thanh Long: 42 frame (IDs 32415..32456), icon item 32457.
-- Flag 195; item template 1931; aura khung x2 = 1080x1200 (3x item 1930).
-- [29/09/2026] Frame sinh tu anh rong ngang cua user (tools/res/hdragon_src.png)
--   -> cat nen (tools/CutChecker.java -> tools/res/hdragon.png)
--   -> xe thanh mang doc va uon quanh nguoi choi (tools/GenStripDragon.java).
-- Chi chay khi DB chua co cac dong nay (server da co thi dung SQL nay lam ghi nho).
INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (195,
  '32415,32416,32417,32418,32419,32420,32421,32422,32423,32424,32425,32426,32427,32428,32429,32430,32431,32432,32433,32434,32435,32436,32437,32438,32439,32440,32441,32442,32443,32444,32445,32446,32447,32448,32449,32450,32451,32452,32453,32454,32455,32456',
  'Cuu Thien Thanh Long', -1, -1, 32457);

-- Clone template fields from aura item 1930, replace ID/name/description/icon/flag.
INSERT INTO item_template
  (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up,
   power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
SELECT 1931, 11, 3, 'Cửu Thiên Thanh Long', 'Hào quang Cửu Thiên Thanh Long bay lượn quanh thân',
       level, 32457, 195, is_up_to_up, power_require, gold, gem,
       head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby
FROM item_template WHERE id = 1930;

SELECT id, TYPE, NAME, part, icon_id FROM item_template WHERE id = 1931;
SELECT id, NAME, LENGTH(icon_data) AS icon_data_chars, icon_id FROM flag_bag WHERE id = 195;
