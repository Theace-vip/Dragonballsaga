# Phan loai 167 bo bi danh dau (ngay 2026-10-03)

> **UPDATE 2026-10-04 — NGUYEN NHAN GOC da tim thay va FIX (viewer):**
> 164/167 bo danh dau co `size:` trong .atlas LECH kich thuoc PNG (vd baby_vegata: atlas khai 704x704 nhung PNG bi downscale 512x512; 352/431 bo toan source lech).
> Runtime spine-webgl tinh UV mesh (`MeshAttachment.updateRegion`) lay SPAN theo `image.width` (PNG thuc) nhung OFFSET lai chia `page.width` (khai bao) -> span UV mesh lon hon vung region dung 704/512 lan 512/416... -> UV tran mau sang region lan can -> **manh rua/va/thua quanh ria, thua manh o chan** — chi anh huong MESH attachment (bo giay, gang tay, ao...), RegionAttachment thi dung nen nhieu bo van tot.
> FIX: patch `spine-webgl-4.1.24.js` + `spine-webgl-4.2.11.js` — `textureWidth/Height` lay theo `page.width||image.width` (khop spine-csharp game: `textureWidth = region.width/(u2-u) = page.width`).
> VERIFY: UV mesh csharp vs webgl KHOP 100% (20/20 dong baby_vegata x 3 anim, 7/7 dong android13); giay render sach 1 manh; bo_lao/agnilasa/android13/broly_form_2/baby_vegata sach. Tab cu da reload (truoc do render VO TANH do code cu + runtime cu).
> **Can phan loai lai 167 bo sau fix** (nhom N2 'ra manh' co the het phan lon).
>
> **UPDATE 2 — 2026-10-04 (64 bo con lai sau khi user gac bo tot): "khong hien thi gi ca" da FIX.**
> Nguyen nhan: (a) loadSet chon animation DAU TIEN co timeline — nhieu bo (Full_FX, g13_*_texiao) anim[0] TRANG toan phan, noi dung o anim khac; (b) quet frame cu chi 0.1..0.9 (bo qua dau/cuoi) + khong chuyen anim nen FX bi lo; (c) camera fit theo pose cua anim trong.
> FIX trong index.html (loadSet): quet coarse 5 moc tren TAT CA anim -> chuyen sang anim peak cao nhat neu anim mac dinh qua yeu (own < max(best*0.5,60)); quet fine 11 moc (0..1) tren anim duoc chon -> rai thi GIU frame ro nhat; neu t=0 trong nhung vong lap co hinh -> bat dau tu peakT; fit camera lai theo bbox cua anim duoc chon; warning neu khong thay hinh o anim nao.
> VERIFY: 64/64 bo co hinh ngay sau load (px 38..2388, khong bo nao <25); whis (bo binh thuong) khong bi hoi quy. 4 bo yeu du lieu nhung van thay: longjuanju_texiao38, taiqingzhenren41, gonggong77, zhujiuyin82.
> Ket qua quet luu tai tools/spine-viewer/_sweep/blank_scan/ (b*.json truoc fix, fix_o*.json sau fix).

> **UPDATE 3 — 2026-10-04 (7 bo user chi tay: H23601, H27701S1, g13_zheng, g13_zaochi, g13_yuanshitianzun_texiao, g13_guai_taowu, g13_guai_hundun — "vo hoan toan" / "thieu chi tiet mat, than, chan") da FIX (3 nguyen nhan viewer).**
>
> **(A) THIEU MAT/THAN/CHAN — 4 bo g13 (+ g13_guai_zheng, g13_kuiniu): skin phu bi bo qua.**
> Data co 3 skin `default,biaoqing,kong`: mat/phan than/phan hoan (head2, shouji2, zaoche_*, daiji2...) nam trong `kong`/`biaoqing` — slot do default DE TRONG, animation attack/skill/dead goi ten attachment o skin khac. Chi dung default (nhu viewer cu) -> slot NULL: setup THIEU dau/mat, luc attack/skill anim tim khong thay -> thieu them phan. Game co code rieng (CostumeSpineActor: normalFaceSkin/hitFaceSkin, useVltFaceSkins) de SetSkin — viewer khong.
> FIX (index.html loadSet): neu data co >1 skin -> gop thanh 1 skin chung `viewer_all` (default them cuoi, chi do vao slot default de trong cho skin phu -> khong doi attachment default) roi `skeleton.setSkin()`.
> VERIFY: g13_zheng/zaochi/taowu/hundun/guai_zheng/kuiniu: setup day du dau+mat (head2=head, shouji2=shouji...), luc attack skinMiss=0, dom=1, px 4100..5900. Anh huong toan corpus: ~153/431 bo da nhieu skin (chu yeu g13_*), cac bo default-only khong doi gi (72 bo quet lai: 0 loi, 0 bo trong).
>
> **(B) VO/ NHO THOT — camera fit sai + chon anim sai (H23601, H27701S1, H27705, H27707...).**
> Nguyen nhan 1: fitCamera dung UNION bbox qua TAT CA moc quet (chua ca Frame/FX rong luc quet) -> camH 2000-4000 don vi cho noi dung ~700 -> nhan vat chi ~1-8% khung (H23601 px435, H27701S1 px543 truoc fix).
> Nguyen nhan 2: coarse scan do px TAT CA anim tren 1 camera fit theo anim DAU TIEN -> anim FX to luon thang: H27701S1 chon Aaction1 (chi 9 slot FX, khong co nhan vat) -> user thay "vo hoan toan".
> Nguyen nhan 3: geometry rong nhung FX alpha~0 khong ve ra gi (H27705: geometry 1460 don vi, pixel that ~470) -> fit theo geometry van qua xa.
> FIX: (i) coarse fit camera RIENG cho tung anim truoc khi do px, diem = px * so slot dang hien (nhan vat day du thang FX rong); (ii) fit lai theo frame BAT DAU/chinh frame se hien (khung dung xem) thay cho union; (iii) chinh tiep bang bbox PIXEL THUC TE (fitCameraByPixels) loai FX trong; (iv) dong thoi: frame() khong con tang RAF loop moi moi loi goi (truoc do moi load bo them ~15 loop ve cung 1 khung), loadSet co `loadGen` guard tranh 2 load dong thoi dap vao state (lan xuat hien loi Asset not found qua la).
> VERIFY sau fix: H23601 px435->6277 cov0.63 camH889; H27701S1 px543->4615 cov0.46 camH904, chon action3_action1 (co nhan vat); H27705 px1103->5525 cov0.08->0.41; H27707 cov0.61; quet lai 72 bo (64 danh dau + diem kiem): **0 loi, 0 bo trong (px<25), console 0 loi**; nhieu bo texiao cung len manh (fuxi88->8763, bifang489->7410, xianglui2 210->9361...). Chi con g13_dijun_texiao co `px=123` o anh quet (do dong anim luc vong lap dung o frame dau trong) — kiem thu tay luc chay giua: 1491 (dang day) -> 4591..9602, KHONG phai loi.
> So lieu: tools/spine-viewer/_sweep/bug7/after64.json; cong cu phan tich: _skin-audit.js, _skin-slots.js, _slot-map.js, _scan-skins.js, _list-anims.js, _sweep/bug7/diag.js.
>
> **(C) Con lai la DATA (khong phai viewer):** H23601 animation `action2_action1` co 1 moc ~t=0.44 tat het attachment (shown 2/107) — frame trong co y trong data; H23601 cac anim an ~20 slot (variant part) tu frame dau — csharp game cung nhu vay; g13_kun_texiao/fx_stack bat dau vong lap trong (t=0) nen luc doi vong hay thay trong giay lat; H27701S1 cac anim Aaction/Baction/Zaction chi co FX (chi `action*_action1`/`normal`/`walk`... co nhan vat) — viewer da tu chon anim co nhan vat.
>
Nhom N1 (FIX ROI): FX chi hien 1 mot khoang trong vong lap -> viewer tu giu frame ro nhat — 53 bo
 - Full_FX
 - g13_baihu_texiao
 - g13_baizhe_texiao
 - g13_bifang_texiao
 - g13_biyiniao_texiao
 - g13_cangjie_texiao
 - g13_change_texiao
 - g13_chiyou_texiao
 - g13_dijiang_texiao
 - g13_dijun_texiao
 - g13_fuxi_texiao
 - g13_gonggong_texiao
 - g13_guai_hundun_texiao
 - g13_guai_zheng_texiao
 - g13_hongjun_texiao
 - g13_houtu_texiao
 - g13_huangdi_texiao
 - g13_jiuweihu_texiao
 - g13_jumang_texiao
 - g13_kuiniu_texiao
 - g13_kun_texiao
 - g13_longjuanjufeng_texiao
 - g13_luoshen_texiao
 - g13_luozu_texiao
 - g13_luwu_texiao
 - g13_nvchou_texiao
 - g13_nvwashenhun_texiao
 - g13_nvwa_texiao
 - g13_pangu_texiao
 - g13_qinglong_texiao
 - g13_qinluan_texiao
 - g13_qiongqi_texiao
 - g13_qitiandasheng_texiao
 - g13_shenghuang_texiao
 - g13_shengqiling_texiao
 - g13_sunv_texiao
 - g13_taiqingzhenren_texiao
 - g13_taotie_texiao
 - g13_tengshe_texiao
 - g13_tiangou_texiao
 - g13_tongtianjiaozhu_texiao
 - g13_xianglui2_texiao
 - g13_xihe_texiao
 - g13_xiwangmu_texiao
 - g13_xuangui_texiao
 - g13_xuanwu_texiao
 - g13_xueyuan_texiao
 - g13_yinglong_texiao
 - g13_yuanshitianzun_texiao
 - g13_yushi_texiao
 - g13_zhuganglie_texiao
 - g13_zhujiuyin_texiao
 - g13_zhuque_texiao

Nhom N2 (manh tha/re, da chung minh viewer == runtime game csharp 4.1.43: KHAC 0/102,...): 33 bo
 - g13_baize
 - g13_cangjie
 - g13_chongmingniao_xianlv
 - g13_chutian
 - g13_dijun2
 - g13_donghuangtaiyi2
 - g13_fuxi
 - g13_jiuweihu
 - g13_jiuweihu_xianlv
 - g13_juitianxuannv
 - g13_juitianxuannv2
 - g13_luoshen
 - g13_mingren
 - g13_mingzhongzhuding_nan
 - g13_mingzhongzhuding_nv
 - g13_nvwa
 - g13_qinghuaci_nv
 - g13_qiongqi
 - g13_qiongqi_boss
 - g13_shilaimu_anyexueji
 - g13_taiqingzhenren
 - g13_taowu_nv
 - g13_xingkong_nan
 - g13_xingkong_nv
 - g13_xuangui
 - g13_yinglong
 - g13_zhaolinger_xianlv
 - g13_zhuque
 - g13_zhu_nan
 - g13_zhu_nv
 - hearts2
 - khi_oozaru
 - khi_vang

Nhom N3 (nhan vat qua nho trong khung): 6 bo
 - fieza
 - g13_qitiandasheng
 - g13_xuanwu
 - g13_zhuganglie
 - H23601
 - H27705

Nhom N4 (skel 3.8.84 dung nhầm runtime 4.1.24): 2 bo
 - g13_shengyeqiyuan_nan (ver 3.8.84)
 - g13_shengyeqiyuan_nv (ver 3.8.84)

Nhom N5 (mat khuan tot theo anh chup, khong tai hieu loi gi — can ban chi ro): 73 bo
 - baby_vegata
 - baby_vegata_ssj2
 - bo_lao
 - broly_form_2
 - broly_tocbac
 - broly_toc_den
 - buuhan
 - bu_map
 - cell_do2
 - cell_trang
 - cumber
 - cumber_tocvang
 - cumber_toc_trang
 - dabura
 - fieza3rdform
 - fire1
 - g13_baiyang_nv
 - g13_bingfeng_q_nan
 - g13_biyuexianzi_xianlv
 - g13_donghuangtaiyi
 - g13_ershu
 - g13_feixingguanggao
 - g13_guai_fuzhu
 - g13_guai_hundun
 - g13_guai_qitiandasheng
 - g13_guai_taowu
 - g13_guai_zheng
 - g13_houtu
 - g13_houtu2
 - g13_kuiniu
 - g13_long_nan
 - g13_long_nv
 - g13_luwu
 - g13_ma_nan
 - g13_ma_nv
 - g13_niu_nv
 - g13_nvwa3
 - g13_pangu
 - g13_pixiu
 - g13_pixiu_chengnian
 - g13_pixiu_qinnian
 - g13_pixiu_younian
 - g13_qinluan
 - g13_shanhailingtian
 - g13_shengqiling
 - g13_sunv
 - g13_taotie
 - g13_taotie_boss
 - g13_taowu_nan
 - g13_tengshe
 - g13_xia_nan
 - g13_xihe
 - g13_xuanwu_nan
 - g13_yaochixianhe
 - g13_yuanshitianzun2
 - g13_yuelao
 - g13_zaochi
 - g13_zheng
 - g13_zhujiuyin2
 - g13_zhulong_xianlv
 - goku_black
 - goku_ssj5
 - goku_ssj5_toc_den
 - goku_ssj5_toc_do
 - goku_ssj5_white
 - goku_trang
 - H27701S1
 - H27707
 - jiren
 - khi_trang
 - li_shenron
 - songoku_trang
 - vegeta_whis
