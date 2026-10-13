# BÁO CÁO — CHỈ SỐ 5 SET ĐỒ BÁN TRONG SHOP (Lượng Bạc / Lượng Vàng / Cỏ 4 Lá)

**Ngày:** 13/10/2026 · **DB:** `hondaodragon` (MySQL/MariaDB 10.4.32, XAMPP 127.0.0.1:3306)
**File chạy:** [sql/shop_set_option.sql](sql/shop_set_option.sql) — **chỉ UPDATE `phuc_loi_tab.list_item` của 5 dòng**
**Không tạo item/option mới ⇒ KHÔNG cần build lại jar, KHÔNG cần restart server.**

---

## 1. ĐÃ LÀM GÌ

| # | Việc | Trạng thái | Bằng chứng |
|---|---|---|---|
| 1 | Backup bảng `phuc_loi_tab` (toàn bảng, 24 KB) | ✅ | [backups/backup_shop_set_20261013-204029.sql](backups/backup_shop_set_20261013-204029.sql) |
| 2 | Viết [sql/shop_set_option.sql](sql/shop_set_option.sql) — option đầy đủ cho 5 set shop | ✅ | file mới, chỉ 5 câu UPDATE có guard |
| 3 | Dry-run trên DB tạm `pl_set_test`: chạy **2 lần, exit 0 cả 2**, JSON hợp lệ, đúng chỉ số | ✅ | `json_ok=1`, `so_mon_trong=0` cho cả 5 dòng |
| 4 | Test guard: sửa tay 1 dòng (giả lập admin sửa) rồi chạy lại ⇒ **không ghi đè** | ✅ | dòng 30 giữ nguyên `SENTINEL_ADMIN_EDIT`, các dòng khác không đổi độ dài |
| 5 | Chạy lên **DB thật** `hondaodragon` (exit 0) + verify lại chỉ số đọc từ DB thật | ✅ | script node `=> TAT CA OK`, xem mục 3 |
| 6 | Dọn DB tạm `pl_set_test` | ✅ | đã DROP |

---

## 2. BẬC THANG ĐÃ CHỐT (admin xác nhận từng mức)

Đơn vị: **% thật**; option `50/77/103` ghi `param = % thật × 10` (giữ nguyên hệ số 10 như v3 —
[hệ số ở đây](data/config/stat_rate.properties#L115)). Số **hiện trên món = 10× số thật** (Thần Long hiện `+1700%`, thật 170%).

| Set | Món | Bán ở | Giá (không đổi) | %/món | **Tổng 5/5** SĐ / HP / KI | Marker 5/5 |
|---|---|---|---|---|---|---|
| **Thanh Long** | 1679–1683 | tab 4 Shop lượng bạc | 100.000 Lượng Bạc | 20% | **80% / 60% / 60%** | `66` Full Sét Dragon (+30% SĐ) |
| **Hắc Ám** | 1174–1178 | tab 5 Shop lượng vàng | 100.000 Lượng Vàng | 85% | **340% / 255% / 255%** | `149-152` |
| **Thần Long** | 1868–1872 | tab 5 Shop lượng vàng | 10.000 Lượng Vàng | 170% | **680% / 510% / 510%** | `149-152` |
| **Hắc Ám Loạn Lưu** | 1919–1923 | tab 8 Shop Cỏ 4 Lá | 5.000 Cỏ 4 Lá | 220% | **880% / 660% / 660%** | `149-152` |
| **Hỗn Độn Vô Cực** | 1924–1928 | tab 8 Shop Cỏ 4 Lá | 10.000 Cỏ 4 Lá | 300% | **1200% / 900% / 900%** | `149-152` |

So với mốc nạp / quà coin đang chạy (id 53 / 90 / 92 / 93):

| Mốc | T1 Hắc Ám 5tr | T2 Nguyên Thủy 10tr | T3 Tư Tượng 50tr | T4 Ngũ Hành 100tr |
|---|---|---|---|---|
| % thật / món | 25% | 50% | **90%** | 150% |
| Tổng 5/5 SĐ | 100% | 200% | 360% | 600% |

⇒ **Đúng yêu cầu**: Thanh Long (20%) **thấp hơn cả T1** (25%) và mọi set quà coin;
Hắc Ám shop (85%) **ít hơn 1 xíu** T3 50tr (90%);
Thần Long (170%) **mạnh nhất trong shop**, trên cả T4 100tr (150%);
2 set Cỏ 4 Lá (220% / 300%) **vượt trội toàn bộ**.

### Đầy đủ option từng món (đọc lại từ DB thật)

| Set | Món | Option (id:param) |
|---|---|---|
| Thanh Long | 1679 Áo | `50:200` `77:200` `94:20` `47:8000` `66:0` `30:1` |
| | 1680 Quần | `77:200` `103:200` `6:200000` `8:4` `66:0` `30:1` |
| | 1681 Găng | `50:200` `103:200` `0:10000` `14:2` `66:0` `30:1` |
| | 1682 Giày | `50:200` `77:200` `18:15` `16:15` `66:0` `30:1` |
| | 1683 Nhẫn | `103:200` `50:200` `5:8` `19:15` `66:0` `30:1` |
| Hắc Ám | 1174 Áo | `50:850` `77:850` `94:90` `95:15` + `149-152:0` + `30:1` |
| | 1175 Quần | `77:850` `103:850` `173:15` + marker |
| | 1176 Găng | `50:850` `103:850` `5:30` `14:8` + marker |
| | 1177 Giày | `50:850` `77:850` `108:15` + marker |
| | 1178 Nhẫn | `103:850` `50:850` `97:25` `105:1` + marker |
| Thần Long | 1868 Áo | `50:1700` `77:1700` `94:170` `95:35` `117:45` + marker |
| | 1869 Quần | `77:1700` `103:1700` `96:35` `117:45` + marker |
| | 1870 Găng | `50:1700` `103:1700` `5:70` `14:16` `117:45` + marker |
| | 1871 Giày | `50:1700` `77:1700` `97:60` `117:45` + marker |
| | 1872 Nhẫn | `103:1700` `50:1700` `105:1` `98:35` `117:45` + marker |
| Hắc Ám Loạn Lưu | 1919–1923 | như bản mốc 50tr nhưng `50/77/103:2200`, `94:220`, `95:45`, `5:80`, `14:20`, `108:40`, `97:60`, `173:45`, `117:60` |
| Hỗn Độn Vô Cực | 1924–1928 | như bản mốc 100tr nhưng `50/77/103:3000`, `94:300`, `95:60`, `96:60`, `5:120`, `14:30`, `97:100`, `98:60`, `117:80` |

Mỗi món **6 / 8–9 / 9–10 dòng** — nằm trong giới hạn 9–11 dòng đã chốt với admin; riêng Thanh Long chỉ 6 dòng (set yếu).

---

## 3. BẰNG CHỨNG VERIFY (đọc lại từ DB thật, không phải từ file SQL)

```
id  tab  json_ok  so_mon  so_mon_trong
18   8       1       5        0
19   4       1       5        0
28   8       1       5        0
30   5       1       5        0
31   5       1       5        0
```

Script [tools_tmp/check_shop_set.mjs](../tools_tmp/check_shop_set.mjs) (node, vì MariaDB 10.4 **không có `JSON_TABLE`**):

```
OK   id=18 SET Hac Am Loan Luu (co 4 la)  | 5 mon, 48 dong option, mon trong=0 | SĐ=880% HP=660% KI=660% | marker 149-152=true
OK   id=19 SET Thanh Long (luong bac)     | 5 mon, 30 dong option, mon trong=0 | SĐ=80%  HP=60%  KI=60%  | marker 66=true
OK   id=28 SET Hon Don Vo Cuc (co 4 la)   | 5 mon, 48 dong option, mon trong=0 | SĐ=1200% HP=900% KI=900% | marker 149-152=true
OK   id=30 SET Hac Am (luong vang)        | 5 mon, 43 dong option, mon trong=0 | SĐ=340% HP=255% KI=255% | marker 149-152=true
OK   id=31 SET Than Long (luong vang)     | 5 mon, 48 dong option, mon trong=0 | SĐ=680% HP=510% KI=510% | marker 149-152=true
=> TAT CA OK
```

Script còn kiểm: mọi món có `30:1` (khoá giao dịch) và marker set đúng nhóm — cả 5 set đều đạt.

---

## 4. KÍCH HOẠT — ✅ ĐÃ RESTART SERVER (13/10 20:49)

**Đã làm:** server cũ (PID 32332, boot 20:15) **giữ dữ liệu Phúc Lợi cũ trong RAM** ⇒ tooltip món trong shop hiện rỗng chỉ số.
Đã dừng task `GameServer-DBS` + kill java/watchdog rồi chạy lại task ⇒ **java mới PID 46944, boot 20:49:46**.

Bằng chứng log boot mới (đọc từ byte 61.889.861 của `server_latest.log`):

```
Successfully loaded map item template (1945)
Successfully loaded map item option template (255)
Load phuc_loi thành công (9) Load phuc_loi_tab thành công (85)   <- 85 mốc, KHÔNG có dòng "sai dữ liệu"
Server initialized and listening on port 14445                     <- PID 46944 đang LISTENING
```

⇒ cả 5 dòng shop đã parse JSON mới thành công (nằm trong 85 mốc này), 0 exception sau boot.
**Cần làm ở phía admin: đăng nhập lại client** rồi mở bảng **Phúc Lợi** để client nhận chỉ số mới.

`PhucLoi` chỉ đọc `phuc_loi_tab` khi **reload** (lúc boot, hoặc khi panel admin bấm Lưu) — nên các lần sửa SQL sau chỉ cần 1 trong 2 cách dưới đây.

**Cách 1 — panel Java tự reload (chỉ dùng khi server chạy CÙNG PHIÊN với panel):**
1. Mở panel Java (`open-panel.bat`) → mục **Phuc Loi (Qua Online)** → **Bước 1**: nhấp đúp 1 tab (vd *Shop lượng vàng*) → **Vào tab**.
   ⚠️ `open-panel.bat` chỉ ghi `panel.trigger` (không restart) khi server chạy **cùng phiên Windows**; khác phiên (vd server chạy ở Session 0 qua Scheduled Task) thì nó **kill java + watchdog rồi boot lại** ⇒ mất kết nối người chơi.
   Panel web PHP (`htdocs/paneladmin`, chạy bằng `php -S 127.0.0.1:8000`) **không** sửa được Phúc Lợi — không file PHP nào đọc/ghi bảng `phuc_loi`.
2. **Bước 2**: chọn 1 mốc bất kỳ → bấm **Lưu** (không cần sửa gì).
   `PanelService.savePhucLoiMoc()` gọi `PhucLoi.gI().reload()` ⇒ server đọc lại `list_item` từ DB.
   (Panel đọc/ghi JSON có giữ nguyên `options` nên bấm Lưu **không** làm mất chỉ số vừa thêm.)
3. Người chơi chỉ cần **mở lại bảng Phúc Lợi** là thấy chỉ số mới — **không cần relog**
   (option của mốc được gửi kèm lúc mở bảng: `PhucLoi.sendPhucLoi` → `item.getDisplayOptions()`).

**Cách 2:** restart server (Scheduled Task `GameServer-DBS` tự bật lại) — log boot sẽ in `Load phuc_loi_tab thành công (85)`.

---

## 5. HIỆU LỰC THẬT (món + bonus `set_config` khi đủ 5/5)

Chỉ số trên món chỉ là **một nửa** sức mạnh thật: đủ 5/5 món cùng marker thì `SetConfigService` cộng thêm bonus của nhóm marker.
Nhóm `149-152` (Tư Tượng) hiện cộng **+1600% SĐ** (200 + 200 + **1000** + 200), nhóm `153-157` (Ngũ Hành) cộng **+850% SĐ**.
Vì vậy 4 set cao (Hắc Ám shop, Thần Long, 2 set Cỏ) đều mang nhóm `149-152` để thứ tự đúng như yêu cầu:

| Set | SĐ trên món | Bonus `set_config` | **SĐ hiệu dụng** |
|---|---|---|---|
| Thanh Long (bạc) | 80% | `66` +30% | **110%** |
| T1 Hắc Ám (mốc 5tr) | 100% | `66` +30% | **130%** |
| T2 Nguyên Thủy (mốc 10tr) | 200% | `66` +30% | **230%** |
| T4 Ngũ Hành (mốc 100tr) | 600% | `153-157` +850% | **1450%** |
| **Hắc Ám (shop vàng)** | 340% | `149-152` +1600% | **1940%** (dưới T3 1960% = "ít hơn 1 xíu") |
| T3 Tư Tượng (mốc 50tr) | 360% | `149-152` +1600% | **1960%** |
| **Thần Long (shop vàng)** | 680% | `149-152` +1600% | **2280%** — mạnh nhất shop |
| **Hắc Ám Loạn Lưu (Cỏ 5.000)** | 880% | `149-152` +1600% | **2480%** |
| **Hỗn Độn Vô Cực (Cỏ 10.000)** | 1200% | `149-152` +1600% | **2800%** — mạnh nhất tuyệt đối |

Ngoài SĐ, nhóm `149-152` còn cho **+500% HP, +500% KI, +500% Giáp, +5 chí mạng**; nhóm `153-157` cho
+400% SĐ/HP/KI/Giáp + 200% STCM + 10 chí mạng. Muốn đổi bất kỳ con số nào ở cột bonus: panel → **Set 5 món** (`set_config`), lưu là áp dụng ngay.

---

## 6. RỦI RO & LƯU Ý

| Rủi ro | Mức | Ghi chú |
|---|---|---|
| **Cùng 1 item có 2 mức chỉ số khác nhau tuỳ nguồn** | Có chủ đích | 1174–1178 mua ở shop vàng = 85%/món, còn mốc nạp 5tr cấp cùng item = 25%/món; 1919–1928 mua ở shop Cỏ = 220%/300%, mốc 50tr/100tr cấp = 90%/150%. Đúng yêu cầu "shop mạnh hơn / chỉ ít hơn 1 xíu" nên giữ nguyên |
| 2 set Cỏ dùng nhóm marker `149-152`, không phải `153-157` của bản mốc | Có chủ đích | Nhóm `153-157` yếu hơn ⇒ nếu giữ, set Cỏ 10.000 không còn "vượt trội toàn bộ" |
| Marker `149-152` trùng tên hiển thị `「✦THANHLONG✦」…` | Thấp | Chỉ là tên option đánh dấu set (option đã chạm trần 255, **không thể tạo marker mới**); T3 đang dùng y vậy |
| Nhóm 149-152 mạnh hơn 153-157 ⇒ mốc 50tr (T3) hiệu dụng > mốc 100tr (T4) | Trung bình | Tình trạng **có sẵn từ trước**, báo cáo này không sửa; nếu muốn đường cong tăng đều thì chỉnh `set_config` (panel, không cần build) |
| Số hiện trên món = 10× số thật | Trung bình | Đã chốt từ v3 (giữ hệ số 10). Lưu ý: `50:3000` hiện `+3000%`, thật 300% |
| File SQL chạy lại | Thấp | Guard `list_item LIKE '%"options":[]%'` ⇒ chỉ điền vào mốc còn rỗng, **không ghi đè** mốc admin đã sửa tay (đã test) |

**Quay lui:** `mysql --default-character-set=utf8mb4 -u root hondaodragon < backups/backup_shop_set_20261013-204029.sql`
(khôi phục bảng `phuc_loi_tab`) → rồi reload Phúc Lợi như mục 4.

---

## 7. FILE LIÊN QUAN

| File | Vai trò |
|---|---|
| [sql/shop_set_option.sql](sql/shop_set_option.sql) | **File chốt** — 5 UPDATE chỉ số 5 set shop (đã chạy lên DB thật) |
| [backups/backup_shop_set_20261013-204029.sql](backups/backup_shop_set_20261013-204029.sql) | Backup bảng `phuc_loi_tab` trước khi sửa |
| [tools_tmp/check_shop_set.mjs](../tools_tmp/check_shop_set.mjs) | Script node verify tổng chỉ số + marker + khoá giao dịch |
| [src/services/PhucLoi.java](src/services/PhucLoi.java#L400) | `readItems()` đọc `list_item` → gắn option lên món khi cấp |
| [src/player/SetClothes.java](src/player/SetClothes.java#L100) | Đếm 5/5 món theo option (marker) |
| [src/services/SetConfigService.java](src/services/SetConfigService.java) | Bonus set 5/5 (bảng `set_config`, sửa được ở panel) |
| [BAO_CAO_TRIEN_KHAI_PHUC_LOI.md](BAO_CAO_TRIEN_KHAI_PHUC_LOI.md) | Báo cáo v3 (3 tab + đậu vĩnh viễn + mốc nạp) |
