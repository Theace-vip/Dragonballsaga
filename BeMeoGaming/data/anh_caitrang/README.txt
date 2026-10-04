DAT ANH CAI TRANG MOI vao day (chi can ban x4)

Dat ten file dung quy uoc de machine nhan duoc (vi du ten bo: "sontinh"):

  BeMeoGaming/data/anh_caitrang/<ten_bo>/
    dau_1.png, dau_2.png                 -> anh DAU (head), can 2 anh
    than_1.png ... than_16.png           -> anh THAN (body), can 16 anh
    chan_1.png ... chan_13.png           -> anh CHAN (leg), can 13 anh
    icon.png                             -> icon item o hanh trang
    avatar.png                           -> anh dai dien khung chat/UI

Tong 33 anh. khong can them frame dung chung (id 20/16/34) - lay tu bo cu.

Chi can ban x4 la du - tu sinh x2/x3 bang cach downscale.
Anh PNG nen trong suot (RGBA), khong nen chua chuoi/tieu de.

Sau khi nhan duoc, may se: sinh 3 zoom -> dat vao data/icon_botnet/x2,x3,x4
theo id moi (tu 32291) -> chen DB (part/item_template/head_avatar)
-> bump vsItem/vsData -> compile + deploy.
