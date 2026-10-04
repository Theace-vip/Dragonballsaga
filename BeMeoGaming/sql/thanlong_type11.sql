-- Hao quang "Than Long" - 24 frame bay vong quanh nguoi (cot), len va xuong
-- icon moi: 32347..32370 (24 frame 360x400 = 2.5x hao quang vong tron) + icon item 32371
-- flag_bag 194 (0..193 da day)

INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (194,
  '32347,32348,32349,32350,32351,32352,32353,32354,32355,32356,32357,32358,32359,32360,32361,32362,32363,32364,32365,32366,32367,32368,32369,32370',
  'Hao Quang Than Long', -1, -1, 32371);

-- item moi: clone tu 1929 (TYPE 11 = hao quang, part = flag_bag.id)
INSERT INTO item_template
  (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up,
   power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
SELECT 1930, 11, 3, 'Hao Quang Than Long', 'Hao quang than long bay vong quanh',
       level, 32371, 194, is_up_to_up,
       power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby
FROM item_template WHERE id = 1929;

SELECT id, TYPE, NAME, part, icon_id FROM item_template WHERE id IN (1929, 1930);
SELECT id, icon_data, NAME, icon_id FROM flag_bag WHERE id = 194;
