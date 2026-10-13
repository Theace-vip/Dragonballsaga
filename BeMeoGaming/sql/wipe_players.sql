-- ============================================================
-- wipe_players.sql - Xoa toan bo du lieu NGUOI CHOI, giu acc admin (id=1)
-- Muc dich: mo server hoan toan moi, khong con vet tai khoan cu.
--
-- GIU LAI:
--   * account id=1 (username 'admin', is_admin=1)
--   * player cua account_id=1  (nhan vat 'admin')
--   * clan id=15 ('cocacc') - clan cua admin, tranh tham chieu treo clan_id=15
--   * kho_web / gifts cua username 'admin'
--   * toan bo bang noi dung & cau hinh: item_template, part, shop, tab_shop,
--     phuc_loi*, items_phuc_loi, tambao_items, moc_vong_quay, npc_template,
--     mob_template, map_template, listbosses, notify, power_limit, caption,
--     data_badges, skill_template, task_*_template, panel_*, settings,
--     adminpanel, event, cung_menh_*, flag_bag, head_avatar, img_by_name...
--
-- XOA:
--   * account/player/clan khac admin
--   * gifts, kho_web cua tai khoan khac
--   * web_shop_history, history_tambao (lich su vong quay), history_bank,
--     `order`, napthe, momo_trans, history_transaction, posts, shop_ky_gui
--   * worldboss_dame, worldboss_reward_log
--
-- RESET:
--   * auto_gift_top.user_receive = '[]'  (xoa danh sach nguoi nhan qua top;
--     GIU is_receive=1 de server khong trao qua lai cho top cua mua cu)
--   * web_shop.sold_quantity = 0 (bo dem "da ban" cua shop web)
--   * AUTO_INCREMENT cua cac bang vua xoa
--
-- CACH CHAY (nen STOP server truoc, va da backup bang mysqldump):
--   C:\xampp\mysql\bin\mysql.exe -uroot hondaodragon < BeMeoGaming\sql\wipe_players.sql
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;

-- FK: player.account_id -> account.id  => xoa player truoc account
DELETE FROM player        WHERE account_id <> 1;
DELETE FROM account       WHERE id <> 1;
DELETE FROM clan          WHERE id <> 15;
DELETE FROM gifts         WHERE username <> 'admin';
DELETE FROM kho_web       WHERE username <> 'admin';

DELETE FROM web_shop_history;
DELETE FROM history_tambao;
DELETE FROM history_bank;
DELETE FROM `order`;
DELETE FROM napthe;
DELETE FROM momo_trans;
DELETE FROM history_transaction;
DELETE FROM posts;
DELETE FROM shop_ky_gui;
DELETE FROM worldboss_dame;
DELETE FROM worldboss_reward_log;

UPDATE auto_gift_top SET user_receive = '[]';
UPDATE web_shop      SET sold_quantity = 0;

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE account              AUTO_INCREMENT = 2;
ALTER TABLE player               AUTO_INCREMENT = 2;
ALTER TABLE clan                 AUTO_INCREMENT = 16;
ALTER TABLE gifts                AUTO_INCREMENT = 1;
ALTER TABLE kho_web              AUTO_INCREMENT = 1;
ALTER TABLE web_shop_history     AUTO_INCREMENT = 1;
ALTER TABLE history_tambao       AUTO_INCREMENT = 1;
ALTER TABLE history_bank         AUTO_INCREMENT = 1;
ALTER TABLE `order`              AUTO_INCREMENT = 1;
ALTER TABLE napthe               AUTO_INCREMENT = 1;
ALTER TABLE momo_trans           AUTO_INCREMENT = 1;
ALTER TABLE history_transaction  AUTO_INCREMENT = 1;
ALTER TABLE posts                AUTO_INCREMENT = 1;
ALTER TABLE shop_ky_gui          AUTO_INCREMENT = 1;
