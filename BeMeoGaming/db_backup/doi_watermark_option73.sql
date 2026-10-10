-- Doi dong watermark tren vat pham: option id 73
--   Cu : 「✦DragonBallSaga.vn✦」
--   Moi: 「✦NROKuRa✦」
-- Nguon duy nhat cua dong nay la bang item_option_template (Manager load luc boot).
-- Chay:  mysql -h127.0.0.1 -P3306 -uroot --default-character-set=utf8mb4 hondaodragon < doi_watermark_option73.sql
-- Sau khi chay PHAI RESTART server de nap lai item_option_template.

-- ===== DOI =====
UPDATE item_option_template SET NAME = '「✦NROKuRa✦」' WHERE id = 73;

-- Ghi chu trong tu dien option cua panel (chi la metadata, khong anh huong hien thi)
UPDATE panel_option_dict SET cach_dung = REPLACE(cach_dung, 'DragonBallSaga.vn', 'NROKuRa') WHERE option_id = 73;

-- ===== KIEM TRA =====
SELECT id, NAME, HEX(NAME) FROM item_option_template WHERE id = 73;

-- ===== KHOI PHUC (bo comment neu can quay lai) =====
-- UPDATE item_option_template SET NAME = '「✦DragonBallSaga.vn✦」' WHERE id = 73;
-- UPDATE panel_option_dict SET cach_dung = REPLACE(cach_dung, 'NROKuRa', 'DragonBallSaga.vn') WHERE option_id = 73;
