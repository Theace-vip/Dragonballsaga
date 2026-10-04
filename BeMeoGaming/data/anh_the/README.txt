BO ANH THE VAO DAY (dat ten theo icon_id), sau do bao agent code.

Trang thai hien tai (27/09/2026):
  32324.png -> THE X2 DIEM FARM      (item_template.id = 1916, TYPE 29)
  32325.png -> THE X3 DIEM FARM      (item_template.id = 1917, TYPE 29)
  32326.png -> THE GIAO DICH DIEM FARM -> dung cho VE TANG DIEM (item_template.id = 1251)

  32327.png -> THE X5 DIEM FARM          (item_template.id = 1918, TYPE 29)  [da lam 27/09]

  --- Bo HON DON VO CUC (5 mon trang bi, da lam 28/09) ---
  ao.png    -> 32333.png -> Áo Hỗn Độn Vô Cực      (item_template.id = 1924, TYPE 0, slot ao)
  quan.png  -> 32334.png -> Quần Hỗn Độn Vô Cực    (item_template.id = 1925, TYPE 1, slot quan)
  gang.png  -> 32335.png -> Găng Hỗn Độn Vô Cực    (item_template.id = 1926, TYPE 2, slot gang)
  giay.png  -> 32336.png -> Giày Hỗn Độn Vô Cực    (item_template.id = 1927, TYPE 3, slot giay)
  nhan.png  -> 32337.png -> Nhẫn Hỗn Độn Vô Cực    (item_template.id = 1928, TYPE 4, slot nhan/rada)

  --- HAO QUANG (28/09) ---
  haoguang.png -> 32338.png -> Hào Quang Hỗn Độn Vô Cực (item_template.id = 1929, TYPE 5 cai trang,
    head/body/leg = -1 giu ngoi hinh, mac len kich hoat hao quang Effect id 43 - file Eff/effect/x*/DataEffect_43)

QUAN TRONG - bang version icon:
  Them icon moi xong chi can RESTART server la du - server tu dong keo dai
  data/smallimage_version/x1..x4/smallimage_version_data (DataGame.extendSmallImageVersion
  goi trong Manager luc boot). Neu khong keo dai thi client khong thay icon (o trong).
  Cong thuc: short BE = maxIconId+1, moi byte = len(icon_botnet/x{z}/{id}.png) % 127 (thieu = -1).

Quy uoc:
- PNG nen trong (alpha), vuong, toi thieu 64x64 (dep nhat 128x180 hoac anh goc to).
- Khong can tao 3 ban zoom - agent se scale thanh 32x32 (x2), 48x48 (x3), 64x64 (x4)
  va tach nen caro neu anh van con nen caro.

Vi tri nhan (agent se copy vao day khi code):
  data/icon_botnet/x2/<icon_id>.png   (32x32)
  data/icon_botnet/x3/<icon_id>.png   (48x48)
  data/icon_botnet/x4/<icon_id>.png   (64x64)
