-- N1 DUYET: Thân ↑8, Đầu ↓9 so với A (dang in-game), Head dx +4, Chân giữ nguyên
-- Cong thuc client: top-left = (cx + CharInfo.x + dx, cy - CharInfo.y + dy), don vi x1 (x4 = 4 don vi)
--   Head: dy +9 (−37→−28, −34→−25), dx +4 (−12→−8, −11→−7) -> co dau cham vong vang co than
--   Body: dy −8 cho toan bo 16 frame (chan thò ra duoi nhu REF)
--   Leg:  khong doi
UPDATE part SET DATA='[[32291,-8,-28],[32292,-7,-25],[20,0,0]]' WHERE id=2140;
UPDATE part SET DATA='[[32293,-13,-26],[32294,-13,-22],[32295,-12,-26],[32296,-9,-19],[32297,-13,-18],[32298,-11,-20],[32299,-14,-22],[32300,-8,-28],[32301,-9,-25],[32302,-13,-20],[32303,-25,-30],[32304,-24,-30],[32305,-8,-23],[32306,-16,-26],[32307,-11,-21],[32308,-20,-28],[16,0,0]]' WHERE id=2141;
-- part 2142 (chan): giu nguyen gia tri hien tai
