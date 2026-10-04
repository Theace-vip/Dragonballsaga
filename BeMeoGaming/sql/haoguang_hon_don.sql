-- Item hao quang "Hao Hon Don Vo Cuc" - mac vao slot5 (cai trang) -> getAura() tra 43
-- head/body/leg = -1 -> giuyen ngoi hinh hien tai (fallback this.head/body/leg), chi bat hao quang
-- icon 32338 = vong hao quang lay tu ImgEffect_43 (x4 frame 4), da copy vao icon_botnet x2/x3/x4
INSERT INTO item_template (id, TYPE, gender, NAME, description, level, icon_id, part,
  is_up_to_up, power_require, gold, gem, head, body, leg,
  is_up_to_up_over_99, can_trade, comment, ruby)
VALUES (1929, 5, 3, 'Hào Quang Hỗn Độn Vô Cực', 'Hào quang', 0, 32338, -1,
  0, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0);
