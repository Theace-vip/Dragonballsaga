-- Them cai trang "Cai trang VLT 030" (set spine g13_fuxi2) — client v0260
-- *** DA CHAY TRUC TIEP VAO DB hondaodragon (verify bang mysql, khong can chay lai) ***
--   item_template 1941 OK | part 2161/2162/2163 OK | head_avatar 2161->26547 OK
--   part_vuot_short = 0 (server loadDatabase parse Short -> id icon PHAI <= 32767)
--
-- Anh da tao (108 file MOI, khong ghi de id cu):
--   data/icon_botnet/x2|/x3|/x4/26512..26547.png   (36 id x 3)
--     26512..26514 = head (3 frame)
--     26515..26528 = leg  (14 frame)
--     26529..26545 = body (17 frame)
--     26546        = icon_id
--     26547        = avatar
--   ** iconBase = 26512 (GAP trong 26512..30017) vi 32586..32621 da co nguoi dung
--      va 32745.. vuot short -> crash server **
-- Da fix byte trung gian smallimage_version (extendSmallImageVersion chi noi duoi):
--   tools/fix_smallimage_range.js 26512 26547  -> x2/x3/x4 ghi 36 byte, x1 giu -1
-- Frame pack da len CDN: spine_export_remote/g13_fuxi2/g13_fuxi2.srf (commit ee7b94cce)
--
-- Bang can cu (max hien tai: item_template = 1941, part = 2163):
--     1939 Hau Tho (part 2155/2156/2157)
--     1940 Thieu Nu Tinh Khong (part 2158/2159/2160)
--     1941 Cai trang VLT 030 (part 2161/2162/2163)  <- id nay
--     -> id ke tiep: item 1942, part 2164..2166

-- ---------- 1) part (sinh boi tools/GenCaitrang.java, copy nguyen tu build/caitrang_part.txt) ----------
INSERT INTO part (id, TYPE, DATA) VALUES
  (2161, 0, '[[26512,-14,-41],[26513,-17,-42],[26514,-18,-39]]'),
  (2162, 1, '[[26529,-25,-26],[26530,-21,-35],[26531,-25,-36],[26532,-20,-36],[26533,-24,-34],[26534,-22,-34],[26535,-23,-35],[26536,-20,-26],[26537,-16,-22],[26538,-23,-22],[26539,-24,-32],[26540,-21,-32],[26541,-20,-25],[26542,-18,-35],[26543,-23,-33],[26544,-24,-32],[26545,-20,-26]]'),
  (2163, 2, '[[26515,-10,-9],[26516,-19,-20],[26517,-17,-20],[26518,-13,-21],[26519,-18,-20],[26520,-15,-20],[26521,-15,-21],[26522,-23,-14],[26523,-20,-17],[26524,-15,-21],[26525,-23,-5],[26526,-24,-5],[26527,-17,-10],[26528,-22,-14]]');

-- ---------- 2) item_template (dinh dang giong sql/caitrang_goku_hacam.sql) ----------
--     id=1941  TYPE=5 (cai trang)  gender=3
--     icon_id=26546 (ID_ICON = iconBase+34)   head/body/leg = 2161/2162/2163
INSERT INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part, is_up_to_up, power_require, gold, gem, head, body, leg, is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1941, 5, 3, 'Cải trang VLT 030', 'Cải Trang', 0, 26546, 0, 0, 0, 0, 0, 2161, 2162, 2163, 1, 1, NULL, 0);

-- ---------- 3) avatar khung chat cho dau part 2161 ----------
INSERT INTO head_avatar (head_id, avatar_id) VALUES (2161, 26547);
