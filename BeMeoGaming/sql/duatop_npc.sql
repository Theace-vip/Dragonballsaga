-- =====================================================================
-- NPC "Đua top" - map 5 (Đảo Kamê), ben trai NPC Kaio Shin (80, 274, 288)
-- Vi tri moi: (180, 288)
--
-- QUAN TRONG: chay file nay TRUOC khi khoi dong server voi code moi,
-- vi code goi NPC_TEMPLATES.get(111) - them row nay la chay, khong can sua code.
-- Sau nay muon doi ten/nhin/toa do = sua DB, khong dot xc code.
-- =====================================================================

-- 1) Template moi: ten "Đua top", ngoai hinh copy NPC "Bảng Xếp Hạng" (id 75: 645/646/647)
INSERT INTO `npc_template` (`id`, `NAME`, `head`, `body`, `leg`, `avatar`)
VALUES (111, 'Đua top', 645, 646, 647, 0);

-- 2) Dat NPC vao ban do map 5, truoc NPC dau tien -> hien thi ben trai NPC Kaio Shin (274, 288)
--    (REPLACE an toan, khong can ghi lai toan bo chuoi npcs)
UPDATE `map_template`
SET `npcs` = REPLACE(`npcs`, '[[13,', '[[111,180,288],[13,')
WHERE `id` = 5;

-- Kiem tra sau khi chay:
-- SELECT id, npcs FROM map_template WHERE id = 5;
-- SELECT * FROM npc_template WHERE id = 111;
