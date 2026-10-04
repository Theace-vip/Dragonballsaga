-- Item 1929 "Hao Quang Hon Don Vo Cuc" chuyen sang TYPE 11 (he thong Hao quang co san)
--   TYPE 11 -> equip vao body index 8 (o Hao quang), getFlagBag() doc part -> message -64
--   part = flag_bag.id -> client hoi frame qua -63 -> tai PNG icon_botnet
-- flag_bag.id 193 (id 0..192 da day), icon_data = 8 frame 32339..32346 (da gen tu ImgEffect_43)

INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (193, '32339,32340,32341,32342,32343,32344,32345,32346',
        'Hao Quang Hon Don Vo Cuc', -1, -1, 32338);

UPDATE item_template
SET TYPE = 11, part = 193
WHERE id = 1929;

SELECT id, TYPE, NAME, part, icon_id FROM item_template WHERE id = 1929;
SELECT id, icon_data, NAME, icon_id FROM flag_bag WHERE id = 193;
