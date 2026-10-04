-- [Sua nham anh 27/09/2026] 32326.png la anh THE GIAO DICH, khong phai the x5.
-- - Ve Tang Diem 1251 dung anh moi (icon_id 11796 -> 32326)
-- - Xoa item 1918 (The X5) vi khong co anh; code x5 (ItemTime.isFamX5...) giu nguyen
--   de sau nay co anh thi chi can INSERT lai 1918.
-- Khong co tab_shop/giftcode nao dung 1918 -> xoa an toan.
-- Da bump vsItem 14 -> 15 de client tai lai item template (icon 1251 doi).
UPDATE item_template SET icon_id = 32326 WHERE id = 1251;
DELETE FROM item_template WHERE id = 1918;
