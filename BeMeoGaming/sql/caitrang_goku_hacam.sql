-- Them cai trang "Goku Hac Am" - anh id 32291-32323 da dat vao data/icon_botnet
-- part: 2140=dau, 2141=than, 2142=chan (frame dung chung 20/16/34)
INSERT INTO part (id, TYPE, DATA) VALUES
(2140, 0, '[[32291,0,1],[32292,-3,1],[20,0,0]]'),
(2141, 1, '[[32293,0,0],[32294,0,0],[32295,0,0],[32296,0,0],[32297,0,0],[32298,0,0],[32299,0,0],[32300,0,0],[32301,0,0],[32302,0,0],[32303,0,0],[32304,0,0],[32305,0,0],[32306,0,0],[32307,0,0],[32308,0,0],[16,0,0]]'),
(2142, 2, '[[32309,0,0],[32310,0,0],[32311,0,0],[32312,0,0],[32313,0,0],[32314,0,0],[32315,0,0],[32316,0,0],[32317,0,0],[32318,0,0],[32319,0,0],[32320,0,0],[32321,0,0],[34,0,0]]');

-- item_template: cai trang type=5, mau nhu 1573 (Gohan 1)
INSERT INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1915, 5, 3, 'Goku Hắc Ám', 'Cải Trang', 0, 32322, 0, 0, 0, 0, 0, 2140, 2141, 2142, 1, 1, NULL, 0);

-- avatar khung chat cho dau part 2140
INSERT INTO head_avatar (head_id, avatar_id) VALUES (2140, 32323);
