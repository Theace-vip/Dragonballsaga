-- =====================================================================
-- QUA ONLINE (Phuc Loi tab 0 - "Qua Online"): moc tu 30 phut tro di
-- 7 moc: 30 phut / 1 tieng / 2 tieng / 5 tieng / 7 tieng / 10 tieng / 12 tieng
--        = phuc_loi_tab.id 37, 38, 39, 40, 41, 42, 43 (truoc day list_item = [] )
--
-- Moi moc nhan:
--   5 loai Phap Chi Dai De 1759/1760/1761/1762/1763 (3% / 5% / 7% / 13% / 15%)
--       - moc 30 phut: x1 moi loai, moi moc sau tang them +1 (moc cuoi x7)
--       - KHONG dung 1854 (Phap Chi Dai De 35%)
--   1000 Luong Bac (1271)  - giu nguyen moi moc
--   1    Luong Vang (1270) - giu nguyen moi moc
--   5    Co 4 La   (1150)  - giu nguyen moi moc
--
-- Kem theo: doi luong doi cua 2 mon nay
--   1 Luong Vang = 2.000 Cash      (truoc: 10.000)  -> Input.java BANSLLKnb
--   1 Luong Bac  = 10 Ngoc Xanh    (truoc: 100)     -> Input.java BANSLLKnbac
--
-- Chay 1 lan tren DB hondaodragon. Sau khi chay phai nap lai bang:
--   restart server game, hoac bam nut Reload trong Panel Admin (Phuc Loi).
-- =====================================================================

UPDATE item_template SET description='Vật Phẩm 1 Lượng Vàng Bằng 2k Cash' WHERE id=1270;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":1,"options":[]},{"id":1760,"quantity":1,"options":[]},{"id":1761,"quantity":1,"options":[]},{"id":1762,"quantity":1,"options":[]},{"id":1763,"quantity":1,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=37;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":2,"options":[]},{"id":1760,"quantity":2,"options":[]},{"id":1761,"quantity":2,"options":[]},{"id":1762,"quantity":2,"options":[]},{"id":1763,"quantity":2,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=38;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":3,"options":[]},{"id":1760,"quantity":3,"options":[]},{"id":1761,"quantity":3,"options":[]},{"id":1762,"quantity":3,"options":[]},{"id":1763,"quantity":3,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=39;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":4,"options":[]},{"id":1760,"quantity":4,"options":[]},{"id":1761,"quantity":4,"options":[]},{"id":1762,"quantity":4,"options":[]},{"id":1763,"quantity":4,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=40;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":5,"options":[]},{"id":1760,"quantity":5,"options":[]},{"id":1761,"quantity":5,"options":[]},{"id":1762,"quantity":5,"options":[]},{"id":1763,"quantity":5,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=41;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":6,"options":[]},{"id":1760,"quantity":6,"options":[]},{"id":1761,"quantity":6,"options":[]},{"id":1762,"quantity":6,"options":[]},{"id":1763,"quantity":6,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=42;

UPDATE phuc_loi_tab SET list_item='[{"id":1759,"quantity":7,"options":[]},{"id":1760,"quantity":7,"options":[]},{"id":1761,"quantity":7,"options":[]},{"id":1762,"quantity":7,"options":[]},{"id":1763,"quantity":7,"options":[]},{"id":1271,"quantity":1000,"options":[]},{"id":1270,"quantity":1,"options":[]},{"id":1150,"quantity":5,"options":[]}]' WHERE id=43;

-- Kiem tra: 7 dong, JSON hop le, moi dong 8 mon (5 phap chi + bac + vang + co)
SELECT id, name, JSON_VALID(list_item) AS json_ok, JSON_LENGTH(list_item) AS so_mon,
       list_item LIKE '%"id":1854%' AS co_1854
FROM phuc_loi_tab WHERE id BETWEEN 37 AND 43 ORDER BY id;
