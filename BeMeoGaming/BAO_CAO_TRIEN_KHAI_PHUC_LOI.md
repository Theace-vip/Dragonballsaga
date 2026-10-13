# BÁO CÁO TRIỂN KHAI — PHÚC LỢI v3 + ĐẬU VĨNH VIỄN

**Ngày:** 13/10/2026 · **DB:** `hondaodragon` (MySQL XAMPP, 127.0.0.1:3306)
**Server:** `dist/BeMeoGaming.jar` → khởi động lại **19:37**, PID **49448**, cổng **14445** (LISTENING; kiểm lại lúc 19:39 vẫn sống, không có exception mới)
**Kế hoạch gốc:** [KE_HOACH_QUA_PHUC_LOI.md](KE_HOACH_QUA_PHUC_LOI.md) — đã chốt **giữ hệ số 10** (`param = % thật × 10`)

---

## 1. TÓM TẮT — ĐÃ LÀM GÌ

| # | Việc | Trạng thái | Bằng chứng |
|---|---|---|---|
| 1 | Backup 4 bảng (`phuc_loi_tab`, `phuc_loi`, `item_option_template`, `set_config`, `item_template`) | ✅ | [backups/backup_phuc_loi_20261013-193151.sql](backups/backup_phuc_loi_20261013-193151.sql) (228 KB) |
| 2 | Cột `player.dau_vinh_vien` + **option 254** + **item đậu vĩnh viễn 1944** + mốc 44/54 | ✅ | `so_option = 255`, cột tồn tại, item `comment='dau vinh vien'` |
| 3 | `set_config`: thêm **%SĐ** cho Tư Tượng + chuyển Ngũ Hành **flat → %** | ✅ | bảng ở mục 4 |
| 4 | Nội dung **3 tab** Phúc Lợi: 33 mốc (7/14/12), option theo **mục 5** | ✅ | `moc_loi = 0`, tổng mốc **85** |
| 5 | **Build lại** `build/classes` (663 file nguồn, `javac --release 21 --enable-preview`, exit 0) | ✅ | [log](#6-nh%E1%BB%AFng-ch%E1%BB%97-%C4%91%C3%A1ng-ch%C3%BA-%C4%91%C3%A3-ph%C3%A1t-hi%E1%BB%87n) |
| 6 | Đóng gói lại `dist/BeMeoGaming.jar` (jar cũ **thiếu code đậu vĩnh viễn**) | ✅ | `.bak-20261013-193505` giữ bản cũ |
| 7 | Tắt 2 tiến trình java cũ (PID 48700 + 37200 lỗi bind), bật lại server qua watchdog | ✅ | log: `Server initialized and listening on port 14445` |

**2 file SQL đã chạy lên DB thật (mỗi file exit 0):**
* [sql/dau_vinh_vien.sql](sql/dau_vinh_vien.sql) — cột đếm + option 254 + item đậu + mốc 44/54
* [sql/phuc_loi_v3_option.sql](sql/phuc_loi_v3_option.sql) — **file mới**: set_config + 3 tab (thay 2 file cũ)

**2 file SQL cũ đã bị đánh dấu "ĐÃ THAY THẾ — KHÔNG CHẠY NỮA":** [phuc_loi_v2_option.sql](sql/phuc_loi_v2_option.sql), [phuc_loi_3tab_qua.sql](sql/phuc_loi_3tab_qua.sql) (chúng còn option cũ `158/75/35` và param cũ, chạy đè sẽ **phá** nội dung v3).

---

## 2. LOG KHỞI ĐỘNG MỚI NHẤT (bằng chứng server đã nạp đủ)

```
Successfully loaded map item template (1945)          <- có item đậu vĩnh viễn 1944
Successfully loaded map item option template (255)    <- có option 254, CHẠM TRẦN 255
Load phuc_loi thành công (9)                          <- 9 tab
Load phuc_loi_tab thành công (85)                     <- 85 mốc, KHÔNG có dòng "sai dữ liệu"
Finish connect Server: hondaodragon
Server initialized and listening on port 14445
```
Không có `Error`/`Exception`/`Warning` nào trong lượt khởi động này.

---

## 3. NỘI DUNG ĐÃ VÀO GAME

### 3.1 Quy tắc param (quan trọng)
Hệ số giữ nguyên `r_opt49/77/103_factor = 10` (đã kiểm lại [stat_rate.properties:115-118](data/config/stat_rate.properties#L115)) ⇒ **option 50/77/103 ghi param = % thật × 10**:

| Set | Mốc | Param 50/77/103 | % thật |
|---|---|---|---|
| T1 Hắc Ám (1174–1178) | tab2: 53 (5.000.000) | 250 | 25% |
| T2 Nguyên Thủy (1730–1734) | tab2: 90 (10.000.000) | 500 | 50% |
| T3 Tư Tượng (1919–1923) | tab2: 92 (50.000.000) · tab3: 87 (2.000.000 Coin) | 900 | 90% |
| T4 Ngũ Hành (1924–1928) | tab2: 93 (100.000.000) · tab3: 89 (5.000.000 Coin + SKH 1856) | 1500 | 150% |

**Đã đối chiếu tổng % thật đọc lại từ DB sau khi chạy** (cộng param rồi chia 10):

| Mốc | %SĐ | %Sinh lực | %MaNa | Khớp kế hoạch |
|---|---|---|---|---|
| 53 | 100% | 75% | 75% | ✅ |
| 90 | 200% | 150% | 150% | ✅ |
| 92 | 360% | 270% | 270% | ✅ |
| 87 | 360% | 270% | 270% | ✅ |
| 93 | 600% | 450% | 450% | ✅ |
| 89 | 600% | 450% | 450% | ✅ |

> Nhắc lại nhược điểm đã chốt: **số hiện trên món = 10× số thật** (param 250 hiện `+250%`, thật 25%).

### 3.2 `set_config` sau khi sửa (đọc lại từ DB)

| id | Set | hp_pct | ki_pct | dame_pct | def_pct | crit_add | crit_dmg_pct | flat cũ |
|---|---|---|---|---|---|---|---|---|
| 1 | Thanh Long | 0 | 500 | **200** | 0 | 0 | 0 | — |
| 2 | Chu Tước | 500 | 0 | **200** | 0 | 0 | 0 | — |
| 3 | Bạch Hổ | 0 | 0 | 1000 | 0 | **5** | 0 | — |
| 4 | Huyền Vũ | 0 | 0 | **200** | 500 | 0 | 0 | — |
| 5 | Kim | 0 | 0 | **400** | 0 | 0 | 0 | 5.000.000 SĐ → **0** |
| 6 | Mộc | **400** | 0 | **100** | 0 | 0 | 0 | 10.000.000 HP → **0** |
| 7 | Thổ | 0 | **400** | **100** | 0 | 0 | 0 | 10.000.000 KI → **0** |
| 8 | Thủy | 0 | 0 | **100** | **400** | 0 | 0 | 10.000.000 Giáp → **0** |
| 9 | Hỏa | 0 | 0 | **150** | 0 | **10** | **200** | STCM 100 → **200** |

Tier 3 mang marker **149-152** (4 bonus Tư Tượng); Tier 4 mang marker **153-157** (5 bonus Ngũ Hành) — đúng như bảng mục 5 (không dùng cả 9 vì mỗi món sẽ thành 14-15 dòng).

### 3.3 Đậu vĩnh viễn
* **Option 254** = `ĐẬU VĨNH VIỄN` (255 option là **chạm trần** 1 byte).
* **Item 1944** = bản copy của đậu thần 523: cùng `type=6`, `icon_id=241`, **tên y hệt `Đậu thần cấp 9`**, khác duy nhất ở option 254 → người chơi nhận ra khi mở thông tin vật phẩm.
* Ăn 1 cục = `player.dauVinhVien += 1` → **+1% HP, +1% SĐ, +1% KI vĩnh viễn** (cộng vào `tlHp/tlMp/tlDame`), lưu DB ngay, sống qua relog/restart, không giới hạn.
* Nơi cấp: **tab1 mốc 2 & 3** (20 cục), **tab2 mốc 44** (30 cục), **tab3 mốc 54** (100 cục).

### 3.4 Ba tab Phúc Lợi
| Tab | Số mốc | Ghi chú |
|---|---|---|
| 1 — Điểm danh tuần | 7 (id 2–8) | T2/T3 thưởng **20 đậu vĩnh viễn** (item 1944) |
| 2 — Tích nạp | 14 (44–53 + 90–93) | 53/90/92/93 là 4 set tier 1–4; mốc 44 do file đậu điền |
| 3 — Quà Coin | 12 (54–62 + 87–89) | 87/89 bán lại vô hạn set tier 3/4; mốc 54 do file đậu điền |

**Ô trống cần thiết** (`conChoTrong` đòi dư 1 ô): mốc 4 món → ≥5 ô; mốc 5 món (full set) → **≥6 ô**; mốc 89 (5 set + SKH) → **≥7 ô**.

---

## 4. CÁCH KIỂM TRA TRONG GAME (admin làm — bắt buộc relog)

1. **Đăng nhập lại** (thoát hẳn client) để nhận danh sách 255 option.
2. Mở **bất kỳ món đồ cũ** đang có → xem tên option còn hiển thị đúng không.
   ⚠️ **Đây là điểm rủi ro cao nhất**: server gửi số option bằng **1 byte** (`writeByte(ITEMS_OPTION_TEMPLATES.size())`), 255 là giá trị lớn nhất. Nếu tên option bị sai/loạn ⇒ phải đổi `writeByte → writeShort` ở [ItemData.java:27](src/data/ItemData.java#L27) **và** phía client.
3. Mở **Phúc Lợi** → tab 2 thấy mốc **5.000.000 / 10.000.000 / 50.000.000 / 100.000.000**; tab 3 thấy **2.000.000 / 3.000.000 / 5.000.000**.
4. **Chừa ≥6 ô trống** rồi nhận mốc 53 (tab 2) → mở thông tin 5 món, kiểm: món Áo 1174 có `+250% Sức đánh` + `+250% Sinh lực` + `+30% Giáp` + `+10000 Giáp` + `Full Sét Dragon` + `Khóa` = **6 dòng**.
5. Mặc đủ **5/5** món tier 1 → mở thông tin nhân vật: HP/SĐ/KI phải nhảy **+100% SĐ, +75% HP, +75% KI** (ngoài +30% SĐ của option 66).
6. **Đậu**: nhận mốc 44 → ăn 1 cục → thấy thông báo *"Đậu vĩnh viễn: +1% HP, +1% SĐ, +1% KI (đã ăn: N cục)"* → **thoát ra vào lại**, chỉ số vẫn còn.

---

## 5. RỦI RO & CÁCH QUAY LUI

| Rủi ro | Mức | Xử lý |
|---|---|---|
| **Trần 255 option** — client có thể đọc byte 255 thành -1 | **Cao** | Test bước 2 ở mục 4. Nếu lỗi: đổi `writeByte → writeShort` ở [ItemData.java](src/data/ItemData.java#L27) + client, hoặc xoá option chết để giải phóng slot |
| **Số hiện = 10× số thật** (param 250 hiện 250%) | Trung bình | Đã chốt chấp nhận; có thể ghi % thật vào `description` của món |
| Mốc full set cần nhiều ô trống (6–7 ô) | Trung bình | Đã ghi rõ trong tên/thông báo; nếu người chơi thiếu ô sẽ báo "Hãy chừa ô trống" |
| Người chơi **chưa relog** thì chưa thấy gì | Thấp | Thông báo bảo người chơi đăng nhập lại |
| **Đừng Reload Phúc Lợi bằng jar cũ** | — | Không còn áp dụng: server đã chạy jar mới (có option 254) |

**Quay lui nhanh (nếu cần):**
1. `mysql -uroot hondaodragon < backups/backup_phuc_loi_20261013-193151.sql` (khôi phục 5 bảng)
2. Trả jar cũ: `cp dist/BeMeoGaming.jar.bak-20261013-193505 dist/BeMeoGaming.jar`
3. Tắt java đang chạy rồi bật lại (Scheduled Task `GameServer-DBS` hoặc `open.bat`).

> Database tạm `pl_v4_test` dùng để test đã **drop sạch**; DB thật chỉ bị thay đổi đúng các bảng trong backup.

---

## 6. NHỮNG CHỖ ĐÁNG CHÚ Ý ĐÃ PHÁT HIỆN

1. **`dist/BeMeoGaming.jar` cũ hơn code 5 file** (`Player.java`, `NPoint.java`, `UseItem.java`, `PlayerDAO.java`, `NDVSqlFetcher.java`) ⇒ nếu chỉ restart mà không build lại thì **đậu vĩnh viễn không hoạt động**. Đã build lại và đóng gói; bản cũ giữ ở `dist/BeMeoGaming.jar.bak-20261013-193505`.
2. **Có 2 tiến trình java chạy cùng lúc**: PID 48700 giữ cổng 14445, PID 37200 đã chết mồm với log `Error initializing server at port 14445` (khởi động lần 2 bị trùng cổng). Cả 2 đã tắt; server hiện tại chạy dưới **Scheduled Task `GameServer-DBS`** (watchdog, tự bật lại nếu java thoát, chạy từ `build/classes`).
3. **Mâu thuẫn nhỏ trong kế hoạch đã sửa**: mục 2.2 từng ghi Tier 4 mang "cả 9 bonus" (149-157) nhưng bảng mục 5 chỉ dùng 153-157 → đã sửa mục 2.2 cho khớp, và bổ sung dòng tổng 5/5 của Tier 1 (trước đó thiếu chỉ số flat và ghi sai `+1 tốc chạy`, thực tế là option 16 `+20% tốc độ di chuyển`).
4. **Không thể test tự động bước trong game**: server chỉ có 2 nhân vật và không có client tự động; mọi kiểm chứng ở trên là **DB + log server**. Bước mục 4 (2–6) cần admin làm bằng tay trong client.

---

## 7. FILE LIÊN QUAN

| File | Vai trò |
|---|---|
| [BAO_CAO_SET_SHOP.md](BAO_CAO_SET_SHOP.md) | **Đợt sau (13/10 tối)** — chỉ số 5 set bán trong shop Lượng Bạc / Lượng Vàng / Cỏ 4 Lá |
| [sql/shop_set_option.sql](sql/shop_set_option.sql) | Option cho 5 set shop (đã chạy lên DB thật, không cần build/restart) |
| [sql/phuc_loi_v3_option.sql](sql/phuc_loi_v3_option.sql) | **Bản chốt** nội dung 3 tab + set_config (đã chạy) |
| [sql/dau_vinh_vien.sql](sql/dau_vinh_vien.sql) | Đậu vĩnh viễn: cột + option 254 + item 1944 + mốc 44/54 (đã chạy) |
| [sql/fix_ten_dau_vinh_vien.sql](sql/fix_ten_dau_vinh_vien.sql) | Vá tên item 1944 + option 254 bị chữ rác (đã chạy) |
| [KE_HOACH_QUA_PHUC_LOI.md](KE_HOACH_QUA_PHUC_LOI.md) | Kế hoạch chi tiết + bảng option từng món (mục 5) |
| [src/player/NPoint.java](src/player/NPoint.java#L526) | Cộng `dauVinhVien` vào `tlHp/tlMp/tlDame` |
| [src/services/func/UseItem.java](src/services/func/UseItem.java#L3842) | Hook ăn đậu vĩnh viễn |
| [src/jdbc/daos/PlayerDAO.java](src/jdbc/daos/PlayerDAO.java#L1214) | Lưu/đọc cột `dau_vinh_vien` |
| [backups/](backups/) | Bản dump 5 bảng trước khi sửa |
| `dist/BeMeoGaming.jar.bak-20261013-193505` | Jar cũ (để quay lui) |

---

## 8. SỰ CỐ SAU TRIỂN KHAI (13/10 tối) — TÊN BỊ CHỮ RÁC — ĐÃ FIX

**Hiện tượng:** trong game, tên món Đậu vĩnh viễn và tên option hiện thành chữ rác:
`─Éß║¡u thß║ºn cß║Ñp 9` thay vì `Đậu thần cấp 9`, và `─Éß║¼U V─¿NH VIß╗äN` thay vì `ĐẬU VĨNH VIỄN`.

**Nguyên nhân:** lần chạy `sql/dau_vinh_vien.sql` đầu tiên thiếu cờ `--default-character-set=utf8mb4`.
mysql client đọc file theo codepage cũ (CP437), nên các ký tự tiếng Việt bị **mã hoá sai ngay lúc INSERT** vào DB
(không phải lỗi code client/server — item 523 gốc có HEX chuẩn nên hiển thị bình thường).

**Phạm vi:** quét HEX toàn bộ cột text/varchar trong DB `hondaodragon` → **chỉ đúng 2 dòng** bị lỗi:
`item_template` id 1944 và `item_option_template` id 254. Không dòng nào khác.

**Đã fix:**
1. Chạy [sql/fix_ten_dau_vinh_vien.sql](sql/fix_ten_dau_vinh_vien.sql) (chỉ vá khi tên còn dính rác, không ghi đè tên admin đã đổi).
   HEX sau fix = `C490E1BAAD75207468E1BAA76E2063E1BAA5702039` — **giống y hệt item 523 gốc**.
2. Rerun `sql/dau_vinh_vien.sql` kèm charset đúng (exit 0, idempotent — mốc 44/54 giữ nguyên, option vẫn 255).
3. Bổ sung **bước 3b tự vá tên** + cảnh báo charset trong header `dau_vinh_vien.sql` để lần sau chạy thiếu cờ cũng tự sửa được.
4. Restart server để nạp lại tên: PID cũ 33332 → watchdog `GameServer-DBS` tự bật lại **PID 32332**,
   log mới: `map item template (1945)`, `map item option template (255)`, **0 exception**, cổng 14445 listening.

**Cần làm ở phía admin:** **relog** (thoát ra vào lại) để client nhận tên mới — tên item/option chỉ được gửi khi đăng nhập.

**Ghi chú `HP, KI+9,999000` là đúng thiết kế:** option 2 param 9999 ⇒ mỗi lần ăn hồi 9.999.000 HP/KI
(`UseItem.eatPea`: `hpKiHoiPhuc = param * 1000`), cộng thêm +1% HP/Sức đánh/KI vĩnh viễn qua option 254.
