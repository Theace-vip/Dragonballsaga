# KẾ HOẠCH QUÀ PHÚC LỢI v3 — 3 cấp option, set Tư Tượng/Ngũ Hành, Đậu vĩnh viễn

> Viết lại sau khi **tra trực tiếp code + DB** (không đoán). Mọi con số đều có dòng code / kết quả SQL kèm theo.
> **Changelog v3**: thêm mục **0.5 AUDIT 3 CẤP OPTION** (id nào chạy id nào chết) và **mục 5 thiết kế lại từng set** theo 3 cấp để admin duyệt.
> File SQL: [phuc_loi_v2_option.sql](sql/phuc_loi_v2_option.sql) — đã chạy thử trên DB tạm `pl_v2_test` (exit 0, chạy 2 lần không lỗi) theo **thiết kế v2**.
> ⚠️ **File SQL v2 CHƯA khớp mục 5 (v3)** — sau khi admin DUYỆT bảng ở mục 5 mới sinh lại SQL (chạy lại sẽ `ON DUPLICATE KEY UPDATE` ghi đè đúng mốc cũ, không cần dọn tay).

---

## 0. YÊU CẦU VÀ CÁCH GIẢI QUYẾT

| # | Yêu cầu | Cách làm |
|---|---|---|
| 1 | Dùng option **+#% Sức đánh / +#% Sinh lực / +#% MaNa** có sẵn | Dùng **50** (`「✦Dame✦」+#%`), **77** (`「✦ Sinh Lực ✦」+#%`), **103** (`「✦ MaNa ✦」+#%`) — **GIỮ NGUYÊN hệ số 10, không đụng dữ liệu món cũ**; ghi **param = % thật × 10** (⚠️ số hiện trên món = 10× số thật). Quy tắc chi tiết ở **mục 5.0** |
| 2 | **+% Sức đánh** cho set **Tư Tượng** và **Ngũ Hành** | 9 câu `UPDATE set_config` (chỉ SQL + Reload, không build) |
| 3 | Option **cực ngon** cho set bậc cao nhất | 4 tier set trong Phúc Lợi + bộ option % / hút / phản / xuyên / né / vô hình… + **dùng marker Tư Tượng 149-152 và Ngũ Hành 153-157** làm "danh tính" set (mặc 5/5 là kích hoạt bonus trong `set_config`) |
| 4 | **Đậu thần Phúc Lợi**: ăn **+1% HP, +1% Sức đánh, +1% KI vĩnh viễn, không giới hạn** | **1 option mới duy nhất: 254** (marker) + cột `dau_vinh_vien` + counter trong `NPoint` (phần CODE) |

> ❗ **Ràng buộc quyết định cả thiết kế — hệ thống chỉ chịu được TỐI ĐA 255 OPTION.**
> `DataGame/ItemData` gửi số option bằng **1 byte**: `msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size())` ([ItemData.java:28](src/data/ItemData.java#L28)) và Panel cũng ghi rõ *"Tối đa 255 option (đếm 1 byte)"* ([ControlPanel.java:1654](src/panel/ControlPanel.java#L1654)).
> Bảng hiện có **254 option (id 0..253)** ⇒ **chỉ thêm được đúng 1 option**. Vì vậy:
> * option mới **254 = ĐẬU VĨNH VIỄN** (bắt buộc, vì code phải nhận diện cục đậu);
> * 2 set mới **KHÔNG** tạo marker riêng → dùng lại **149-152** (Tư Tượng) và **153-157** (Ngũ Hành) đã có bonus trong `set_config`;
> * sau khi chạy xong, số option = **255 = CHẠM TRẦN** → các option sau này phải xoá option chết hoặc đổi `writeByte` → `writeShort` ở cả server & client.

---

## 0.5. AUDIT 3 CẤP OPTION (bảng admin gửi) — CÁI NÀO CHẠY, CÁI NÀO CHẾT

Đã grep **toàn bộ `src/`** cho từng id trong 3 đường xử lý thật: `NPoint.addOption` (đường item), `SetClothes.setupSKT` (marker set) và `SetConfigService` (bonus `set_config`).

### Cấp 1 — THÔNG DỤNG
| id | Tên trong DB | Code thật | Dùng được? |
|---|---|---|---|
| 0 | `「✦Tấn Công✦」+#` | `dameAdd += param` [NPoint:537](src/player/NPoint.java#L537) | ✅ |
| 2 | `HP, KI+#000` | `hpAdd += param*1000; mpAdd += param*1000` | ✅ |
| **50** | `「✦Dame✦」+#%` | `tlDame += param × r_opt49_factor/100` ⇒ **hiện ×0.1** | ⚠️ ghi param **×10** (giữ hệ số 10) |
| **77** | `「✦ Sinh Lực ✦」+#%` | `tlHp += param × r_opt77_factor/100` ⇒ **hiện ×0.1** | ⚠️ ghi param **×10** (giữ hệ số 10) |
| 101 | `「✦TNSM✦」 +#%` | `tlTNSM += param` (+% TN/SM) | ✅ |
| **103** | `「✦ MaNa ✦」+#%` | `tlMp += param × r_opt103_factor/100` ⇒ **hiện ×0.1** | ⚠️ ghi param **×10** (giữ hệ số 10) |

### Cấp 2 — TRUNG BÌNH
| id | Tên trong DB | Code thật | Dùng được? |
|---|---|---|---|
| 5 | `「✦STCM✦」+#%` | `tlDameCrit += param; tlSDCM += param` (+% ST chí mạng) | ✅ |
| 117 | `Đẹp +#% SĐ cho mình và người xq` | `tlSexyDame = max(param)` | ✅ (chỉ lấy **max**, không cộng dồn) |
| **10** | `Chính xác: +#` | **KHÔNG có `case 10`** — `panel_option_dict` cũng ghi *“option 10 chủ yếu hiển thị, muốn cộng thật dùng 18”* | ❌ **CHẾT HẲN** → dùng **18** |
| **79** | `Đệ tử #% sức đánh` | trước đây không có code | ✅ **ĐÃ VIẾT CODE (GD4)** |
| **188/189/190** | `SET_DE +20K% ST Kame/Masenco/Antomic` | **KHÔNG cần code**: `setupSKT()` đếm **mọi** option vào `setCount` ⇒ chỉ cần **1 dòng `set_config`** (`option_id = 188/189/190`, `skill_dmg = "<id skill>:20000"`) là chạy | ✅ qua `set_config` |
| **192** | `+#% Chí mạng` | trước đây không có code | ✅ **ĐÃ VIẾT CODE (GD4)** — alias của 14 (`critAdd`) |
| **234** | `Fam Địa Đạo Luân Hồi` | **KHÔNG cần code**: như 188-190, đặt làm marker set trong `set_config` là chạy | ✅ qua `set_config` |

### Cấp 3 — NÂNG CAO
| id | Tên trong DB | Code thật | Dùng được? |
|---|---|---|---|
| **149-152** | Thanh Long / Chu Tước / Bạch Hổ / Huyền Vũ | `set_config` khi **đủ 5/5 món** [SetConfigService:100](src/services/SetConfigService.java#L100) | ✅ |
| **153-157** | Kim / Mộc / Thổ / Thủy / Hỏa | `set_config` khi **đủ 5/5 món** | ✅ |
| **141-145, 136-140, 146** | `「SET」18S…999S`, `+16 STCM` | `setkichhoat*==5` → cộng **% HP/KI/SĐ** theo `star*` [NPoint:931](src/player/NPoint.java#L931), [1277](src/player/NPoint.java#L1277), [1699](src/player/NPoint.java#L1699) | ✅ (mạnh, nhưng **chỉ 1 marker/món**) |
| 127-135 | SETCày (Kaioken/Kame/Kirin/Nappa…) | `+% ST` theo **từng kỹ năng** | ✅ nhưng hẹp theo kỹ năng |
| 116 | `Kháng TDHS` | `khangTDHS = true` (miễn kháng kỹ năng Thái Dương Hạ San) | ✅ (param bỏ qua) |
| **233** | `Fam Thiên Đạo Pháp Tắc` | **KHÔNG cần code**: dùng làm marker set trong `set_config` (như 188-190) | ✅ qua `set_config` |
| **247** | `Kích Sát Boss +#%` | trước đây không có code | ✅ **ĐÃ VIẾT CODE (GD4)** — cộng `+%` sát thương khi đánh Boss |

Ngoài 3 cấp trên, các option **thật sự chạy** mà nên dùng bổ trợ: `6/7` (HP/KI flat), `47` (DEF flat), `94` (`+#% GIÁP`), `14` (`+#% Chí Mạng`), `18` (`+#% Chính xác`), `19` (`+#% Tấn công khi đánh quái`), `204` (`+#% Tấn công lên Boss`), `8` (hút `#%` HP/KI xung quanh mỗi 5s), `95/96` (hút `#%` ST thành HP/MP), `97` (phản `#%` ST), `98/99` (xuyên giáp chưởng/cận chiến), `105` (vô hình), `108` (`#%` né), `162` (hồi `#%` KI/s), `173` (hồi `#%` HP/KI cho đồng đội), `100` (`+#%` vàng), `88` (`+#%` exp).

> **Tổng kết sau khi sửa**: trong 27 id admin đưa —
> * **1 id chết hẳn**: `10` (chỉ hiển thị, thay bằng `18`);
> * **3 id đã viết code** (GD4): `79` (+% SĐ cho đệ tử), `192` (alias chí mạng của 14), `247` (+% ST khi đánh Boss);
> * **5 id dùng được KHÔNG cần code** qua `set_config`: `188, 189, 190, 233, 234` (chỉ cần 1 dòng `set_config` là thành marker set);
> * còn lại **chạy sẵn**.
>
> 👉 Phát hiện quan trọng: `SetClothes.setupSKT()` đưa **MỌI id option** vào `setCount` ([SetClothes:96](src/player/SetClothes.java#L96)) nên **bất kỳ option nào cũng có thể làm marker set** chỉ bằng 1 dòng `set_config` — không phải sửa code.

---

## 0.6. BẰNG CHỨNG HỆ SỐ 10× LÀ LỖI (không phải chủ ý)

`r_opt49_factor = r_opt77_factor = r_opt103_factor = 10` ([stat_rate.properties:115-118](data/config/stat_rate.properties#L115)) ⇒ mọi option `%` bị chia 10.

Nhưng **dữ liệu shop đang dùng đúng thang 1:1**:

```json
// tab_shop (Linh Thạch): {"param":20,"id":77} = +20% Sinh lực ; {"param":10,"id":50} = +10% Dane
// tab_shop id 18:      {"param":23,"id":50},{"param":19,"id":103} ; {"param":35,"id":77} trên Bông Tai
// 12 dòng tab_shop chứa id 50, 12 dòng chứa id 77, 10 dòng chứa id 103
```

⇒ Người cấu hình shop đã ghi **đúng % thật**. Vậy hệ số `10` là **bug**, và sửa `10 → 100` là **sửa đúng ý định**, không phải buff.

### ⚠️ SỬA LẠI KẾT LUẬN CŨ — CÓ món đang mang option 49/50/77/103

Lượt trước tôi kiểm sai (dùng mẫu `"id":N` của `tab_shop`, trong khi dữ liệu nhân vật lưu dạng khác `[[id,param],...]`). Quét lại **đúng định dạng** trên `player.items_body / items_bag / items_box / items_daban / data_item_event / items_box_lucky_round`:

| Option:param đang có trên nhân vật | Số lần | Hiện nhận (hệ số 10) | Nếu đổi hệ số thành 100 |
|---|---|---|---|
| `[50,10000]` | **11** | +1000% SĐ | **+10000% SĐ** |
| `[77,10000]` | **11** | +1000% HP | **+10000% HP** |
| `[103,10000]` | **11** | +1000% KI | **+10000% KI** |
| `[50,110]` | 1 | +11% SĐ | +110% SĐ |
| `[77,20]` | 1 | +2% HP | +20% HP |
| `[103,260]` | 1 | +26% KI | +260% KI |

⇒ **Đổi hệ số 10 → 100 sẽ làm 11 món SET của nhân vật mạnh lên ×10.** Đây là rủi ro **thật** (không phải "không có món nào bị lệch"). **2 cách xử lý** — admin chọn:

* **(A – giữ nguyên sức mạnh cũ)** đổi hệ số `10 → 100` **và** chia 10 param của các món cũ đang mang 49/50/77/103 (`10000→1000`, `110→11`, `20→2`, `260→26`). Sau đó option 50/77/103 hiện đúng số thật.
* **(B – không đổi hệ số)** giữ hệ số 10, thế thì muốn `+20%` thật phải ghi param `200` (số hiện = 200% ≠ số thật 20%) — quay lại đúng vấn đề admin phàn nàn ban đầu.
* **(C – sạch nhất cho quà mới)** dùng `set_config` (`hp_pct`/`ki_pct`/`dame_pct`, áp **1:1**) để cho set %HP/%KI/%SĐ, còn option trên món chỉ dùng các id **đã 1:1**: `147/239/240` (%SĐ), `94` (%Giáp), `5` (STCM), `117` (đẹp %SĐ), `108`, `97`, `19`, `204`, `101`. Không cần đụng hệ số, không đụng dữ liệu cũ.

### ✅ ADMIN ĐÃ CHỐT: **GIỮ hệ số 10** (không theo khuyến nghị A cũ)

| | |
|---|---|
| **Chốt** | **B có điều chỉnh**: giữ `r_opt49/77/103_factor = 10` như đang chạy, và **param ghi trên món mới = % thật × 10** (xem **mục 5.0**) |
| **Ưu** | Món mới (Tư Tượng / Ngũ Hành) chạy **đúng** % mong muốn; **không đụng 1 dòng dữ liệu nào** của người chơi hiện có |
| **Nhược** | **Số hiện trên món = 10× số thật** (param `250` → hiện `+250%`, thật `+25%`) |
| **Đã loại A** | Đổi hệ số `10 → 100` sẽ làm **11 món SET cũ** (`[50/77/103, 10000]`) nhảy +1000% → **+10000%** — rủi ro lớn hơn cái lợi "số hiện = số thật" |
| **Đã loại C** | Dùng `set_config` cho %HP/%KI làm món **mất 2 dòng `77`/`103`** ⇒ bảng option nghèo hơn, trái yêu cầu "option cực ngon" |
| **Hệ quả cho kế hoạch** | **PHẦN 2C (chia 10 param món cũ) HUỶ** — không cần làm; mục 6 bước 2 và mục 7 đã cập nhật theo |

---

## 0.7. "XÓA OPTION CHẾT" — TRẢ LỜI THẲNG: **KHÔNG DELETE ĐƯỢC Ở GIỮA**

### Bằng chứng: client map option theo **VỊ TRÍ MẢNG**, không theo id

[ItemData.updateItemOptionItemplate()](src/data/ItemData.java#L28-L35) gửi option như sau — **không hề gửi `id`**:

```java
msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size());
for (ItemOptionTemplate io : Manager.ITEM_OPTION_TEMPLATES) {
    msg.writer().writeUTF(io.name);   // chi co name
    msg.writer().writeByte(io.type);  // va type
}
```

Và server tra ngược bằng `ITEM_OPTION_TEMPLATES.get(id)` (index = id). **⇒ Xóa 1 dòng ở giữa bảng sẽ làm MỌI option phía sau lệch 1 ⇒ tên hiển thị + hiệu ứng của mọi món/id phía sau sai hết** (món của người chơi, shop, vòng quay, quà Phúc Lợi…). Chỉ xóa an toàn khi xóa **block liền kề ở CUỐI bảng**.

### Kết quả quét (code + DB, 2026-10-13)

Quét "không được tham chiếu ở đâu": `case N:` (NPoint/SetClothes), `optionTemplate.id == N`, `haveOption(N)`, khởi tạo `new ItemOption(N, …)`, và các nơi chứa option: `player` (6 cột JSON), `tab_shop.items`, `moc_vong_quay.item_options`, `items_phuc_loi.options`, `tambao_items.item_options`, `data_badges.Options`, `shop_ky_gui.itemOption`, `radar.options`, `listbosses.dropItems`, `history_tambao.item`, `set_config.option_id`, `cung_menh_option.option_id`.

**67 id ứng viên chết:**

```
11 12 13 15 17 32 37 40 62 68 69 70 82 83 85 90 91 92 113 115 161
163 164 165 166 167 169 170 171 172 175 176 177 178 179 180 183 184 185 187
189 190 194 195 196 197 198 199 200 201 202 203 205 206 208 214 215 222
224 225 227 228 229 230 232 234 235
```

**Id chết cao nhất = 235; 236→253 đều đang dùng ⇒ KHÔNG có block đuôi ⇒ không xóa được dòng nào bằng `DELETE`.**

### ✅ CHỐT (admin đã duyệt): **TÁI SỬ DỤNG id chết** — KHÔNG DELETE

Quyết định: giữ nguyên bảng option, **không xóa dòng nào**. Khi cần option mới mà hết slot thì lấy 1 id trong 67 id ở trên, đổi tên + viết code.

Lợi ích so với tạo id mới / xóa:

Có thể lấy 1 trong 67 id trên, **đổi tên** thành option mới + viết code cho nó. Lợi hơn hẳn so với tạo id mới:

* không phá index ⇒ không ảnh hưởng món/code hiện có;
* **không tốn slot 255** (bảng option đang 254 dòng; nếu dùng id chết thì vẫn còn 1 slot trống thay vì chạm trần);
* "sạch" về mặt logic: không còn option chết nữa.

> 🔴 **42-46 và 249-253 KHÔNG PHẢI option chết** — chúng đang được code gán vào vật phẩm lúc chạy: `new Item.ItemOption(249, 1)` / `251` ([DrLychee.java:113](src/boss/boss_manifest/DestronGas/DrLychee.java#L113), [Hatchiyack](src/boss/boss_manifest/DestronGas/Hatchiyack.java#L127)), `new ItemOption(250, 0)` ([PhaLeHoaTrangBi](src/models/Combine/manifest/PhaLeHoaTrangBi.java#L250)), `new ItemOption(253, 7)` ([UseItem:3040](src/services/func/UseItem.java#L3040)). **Tuyệt đối không xóa.**

---

## 0.8. GIẢI THÍCH "HỆ SỐ 10×" BẰNG VÍ DỤ CỤ THỂ (admin hỏi)

### Chuyện gì đang xảy ra

Tên option là `「✦ Sinh Lực ✦」+#%` và trong game nó hiện đúng **con số mình ghi vào** (param). Nhưng lúc **cộng chỉ số thật**, code không dùng param trực tiếp mà nhân qua 1 con số cấu hình rồi chia 100:

```java
// NPoint.addOption
case 77: tlHp.add(io.param * rate("opt77_factor", 10) / 100.0);
```

Con số cấu hình đó nằm ở [stat_rate.properties](data/config/stat_rate.properties) (sửa được trong **Panel → Tuning → StatRate**) và **hiện đang = 10**:

```
r_opt77_factor  = 10     →  thực nhận = param × 10 / 100 = param × 0.1
r_opt103_factor = 10     →  thực nhận = param × 0.1
r_opt49_factor  = 10     →  option 49 và 50 (Dame) = param × 0.1
```

### Ví dụ trên chính đồ của bạn

| Ghi trên món | Hiện trong game | Cộng thật hiện nay | Sau khi sửa hệ số 10 → 100 |
|---|---|---|---|
| option 77, param **20** | `+20%` | **+2%** (ít hơn 10 lần) | +20% ✔ khớp |
| option 77, param **10000** (11 món SET đang có) | `+1000%` (do tên option tự hiện param/…) | **+1000%** | **+10000%** (mạnh gấp 10) |

⇒ Có 2 mặt: (1) món **mới** nếu để hệ số 10 thì **hiện 20% mà chỉ được 2%** — đúng cái admin phàn nàn; (2) nhưng nếu đổi hệ số thành 100 thì 11 món SET cũ đang `param 10000` sẽ nhảy từ +1000% lên **+10000%**.

### 3 cách xử lý (chọn 1)

| | Cách | Kết quả |
|---|---|---|
| **A** | Đổi `r_opt49/77/103_factor` = 100 **+** chia 10 param của các món cũ đang mang 49/50/77/103 (`10000→1000`, `110→11`, `20→2`, `260→26`) | Số hiện = số thật, **sức mạnh món cũ giữ nguyên** |
| **B** | Chỉ đổi hệ số = 100 | Số hiện = số thật, nhưng 11 món SET cũ **mạnh lên ×10** |
| **C** | Không đụng hệ số; %HP/%KI/%SĐ của set lấy từ `set_config` (`hp_pct`/`ki_pct`/`dame_pct` — áp **1:1**), trên món chỉ dùng option đã 1:1 (`147/239/240` %SĐ, `94` %Giáp, `5` STCM, `117` đẹp %SĐ, `108`, `97`, `19`, `204`, `101`) | Không đụng gì dữ liệu cũ, **không sửa hệ số**, nhưng bảng option trên món sẽ ít dòng hơn (không có 77/103) |

> ✅ **Đã chốt: giữ hệ số 10 + param ×10** (B) — lý do & hệ quả ở **mục 0.6**. Bảng trên giữ lại làm hồ sơ so sánh.

---

## 1. SỰ THẬT ĐÃ TRA ĐƯỢC

### 1.1 Option chỉ có tác dụng nếu được xử lý trong `NPoint.addOption` (item path, dòng 536–755)

```
0  2  3  5  6  7  8  14  16/114/148  18  19  22  23  24  25  26  27  28  33
34  35  36  47  48  49  50  77  80  81  88  94  95  96  97  98  99  100  101
103  104  105  106  108  109  110  116  117  147  159  160  162  173  204  211  153  239  240
```

**Option đã bị comment trong code ⇒ VÔ DỤNG, không dùng:** `42, 43, 44, 45, 46, 249, 250, 251, 252, 156(case đã comment nhưng 156 vẫn là marker Ngũ Hành)`.
Lưu ý: `42-46` (Khảm Đá) tuy chết trong code nhưng **đang có trên 1 vật phẩm của nhân vật admin và 1 dòng `tab_shop`**; `249-253` là **marker có chủ đích** (ẨN / Đập Xịt / VIP / Hỏng / Tu Tiên Mãi Đỉnh — ghi rõ trong `panel_option_dict`). ⇒ **Không được "mượn" các id này để làm marker mới.**

### 1.2 Vì sao số % bị lệch 10 lần

`rate()` → `StatRateTuning.real(key)` = `REAL × GLOBAL_FACTOR`; `r_` = số thật, `d_` = số hiển thị ([stat_rate.properties](data/config/stat_rate.properties)):

```
d_opt77_factor=100.0   r_opt77_factor=10.0     -> option 77  = param x 0.1
d_opt103_factor=100.0  r_opt103_factor=10.0    -> option 103 = param x 0.1
d_opt49_factor=100.0   r_opt49_factor=10.0     -> option 49/50 = param x 0.1
```

| Option | Thực nhận hiện tại | Dùng trong quà mới (**giữ hệ số 10**) |
|---|---|---|
| **77** `「 Sinh Lực 」+#%` | `param × 0.1` | ✔ **dùng** — ghi **param = % thật × 10** |
| **103** `「 MaNa 」+#%` | `param × 0.1` | ✔ **dùng** — ghi **param = % thật × 10** |
| **239** `Xạ Mệnh +#% Sức Đánh` | `param` (1:1 sẵn) | `param` ✔ (chỉ nên **đổi tên** thành `「 Sức Đánh 」 +#%`) |
| 147 `+#% sức đánh`, 240 | `param` (1:1) | dùng thay thế được |
| 94 `「 GIAP 」 +#%`, 5 `「STCM」+#%`, 14, 8, 95, 96, 97, 98, 99, 105, 108, 117 | 1:1 | dùng bình thường |
| **204** `Tấn công+#% lên Boss` | chỉ ăn với **skill** ([SkillService:1223](src/services/SkillService.java#L1223)) | thêm 1 dòng ở [Boss.injured()](src/boss/Boss.java#L800) để ăn cả đòn thường (tuỳ chọn) |
| 49/50 `Tấn công+#%`, `「Dame」+#%` | `param × 0.1` | ✔ **dùng 50 cho %SĐ** — param **×10** (49 để nguyên, không dùng) |

### 1.3 Cơ chế set (đọc [SetClothes.setupSKT()](src/player/SetClothes.java#L100-L155) + [SetConfigService](src/services/SetConfigService.java))

* Chỉ tính **5 ô đầu**: `0 áo, 1 quần, 2 găng, 3 giày, 4 nhẫn/rada`; mỗi món đếm 1 lần 1 option; **đủ 5/5 mới kích hoạt**.
* Option cứng trong code: `66` = Full Sét Dragon → **+30% SĐ** (`rate("setsaga",30)`); `35/34/36` = Nguyệt/Tinh/Nhật Ẩn → +5% KI/SĐ/HP.
* Option **149-157** (Tư Tượng + Ngũ Hành) đưa vào `setCount` rồi tra bảng **`set_config`** (`countSet(optionId) == 5`). 5 hàm áp dụng đều đã nối vào NPoint: `applyHp` [964](src/player/NPoint.java#L964), `applyKi` [1243](src/player/NPoint.java#L1243), `applyDame` [1733](src/player/NPoint.java#L1733), `applyDef` [1839](src/player/NPoint.java#L1839), `applyCrit` [1870](src/player/NPoint.java#L1870).
* Cột `set_config`: `id, name, option_id, active, hp_base, hp_pct, ki_base, ki_pct, dame_base, dame_pct, def_base, def_pct, crit_add, crit_dmg_pct, skill_dmg` (đã xác nhận bằng `SHOW COLUMNS`).
* Hiện trạng: Tư Tượng đã là **%** (`KI 500 / HP 500 / SĐ 1000 / Giáp 500`), Ngũ Hành còn **flat** (`5tr / 10tr`) — xem 1.4.

### 1.4 Bảng `set_config` hiện tại (đọc trực tiếp từ DB)

| id | name | option_id | hp_pct | ki_pct | dame_pct | def_pct | dame_base | hp_base | ki_base | def_base | crit_add | crit_dmg_pct |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | Thanh Long | 149 | 0 | 500 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 2 | Chu Tuoc | 150 | 500 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 3 | Bach Ho | 151 | 0 | 0 | 1000 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 4 | Huyen Vu | 152 | 0 | 0 | 0 | 500 | 0 | 0 | 0 | 0 | 0 | 0 |
| 5 | Kim | 153 | 0 | 0 | 0 | 0 | 5.000.000 | 0 | 0 | 0 | 0 | 0 |
| 6 | Moc | 154 | 0 | 0 | 0 | 0 | 0 | 10.000.000 | 0 | 0 | 0 | 0 |
| 7 | Tho | 155 | 0 | 0 | 0 | 0 | 0 | 0 | 10.000.000 | 0 | 0 | 0 |
| 8 | Thuy | 156 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 10.000.000 | 0 | 0 |
| 9 | Hoa | 157 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 100 |

### 1.5 Cơ chế Phúc Lợi (đọc [PhucLoi.java](src/services/PhucLoi.java))

| Sự thật | Dòng | Hệ quả |
|---|---|---|
| `active == 0 hoặc 2` → **nhận miễn phí 1 lần** | [247](src/services/PhucLoi.java#L247) | tab 1 (điểm danh), tab 2 (tích nạp) để `active=0` |
| `active == 1` + `tab_id >= 3` → **mua bằng tiền tệ, mua lại vô hạn** | [250](src/services/PhucLoi.java#L250) | tab 3 (quà Coin) để `active=1`, `max_count` = **giá** |
| `count()`: tab 0 = phút online; **tab 1 = thứ trong tuần**; **tab 2 = coin đã nạp (`tongnap`)**; tab ≥3 = `currency_item` (0 = **Coin** qua `vnd`) | [198-215](src/services/PhucLoi.java#L198) | tab 1 `max_count` phải **đúng** số thứ (T2=2…T7=7, CN=1): code so **bằng đúng** (`count != max_count`) [263](src/services/PhucLoi.java#L263) |
| `conChoTrong` = **số item của mốc < số ô trống** (phải **dư 1 ô**) | [352](src/services/PhucLoi.java#L352) | mốc 5 món cần **≥6 ô trống**; mốc 6 món cần **≥7 ô trống** |
| Gửi `writeByte(TEMPLATES.size())` | [82](src/services/PhucLoi.java#L82) | **tổng số mốc ≤ 127** (hiện 78 → sau khi làm 85) |
| `readItems` tạo option **không kiểm tra tồn tại**; `getItemOptionTemplate(id) = ITEM_OPTION_TEMPLATES.get(id)` | [PhucLoi:433](src/services/PhucLoi.java#L433), [ItemService:160](src/services/ItemService.java#L160) | **option id phải tồn tại trước** khi server đọc lại Phúc Lợi, nếu không mốc đó **bị bỏ** (có log `Mốc Phúc Lợi id=... sai dữ liệu`) |
| Panel **Tạo option** tự chèn vào bộ nhớ ngay (không cần restart) | [PanelService:1912](src/panel/PanelService.java#L1912) | có thể tạo option 254 bằng Panel, hoặc chạy SQL rồi khởi động lại server |

### 1.6 Đậu thần

[UseItem.eatPea()](src/services/func/UseItem.java#L3789): quét `itemsBag` tìm item **đầu tiên `template.type == 6`**, lấy cấp bằng `name.substring(13)`, hồi HP/KI, và **trừ 1 viên** (`subQuantityItemsBag(player, pea, 1)` [3830](src/services/func/UseItem.java#L3830)).
⇒ Item **523 = `Đậu thần cấp 9`, type 6, icon 241** dùng được ngay; tên item **phải giữ** dạng `Đậu thần cấp 9` (13 ký tự đầu) nếu không sẽ `NumberFormatException`.

### 1.7 Trạng thái DB

* `phuc_loi_tab`: tab 1 (id 2–8) thưởng ngọc rồng/đá; **tab 2 (id 44–53) và tab 3 (id 54–62) đang RỖNG (`[]`)**; id lớn nhất **86** ⇒ thêm mốc mới 87–93.
* Bảng `phuc_loi`: tab 0 Online, 1 Điểm danh, 2 Tích nạp, 3 Quà Coin, 4–8 là shop (Lương bạc/vàng/Thỏi vàng/Ngọc xanh/Cờ 4 lá).
* Tab 5 bán `SET Hắc Ám`, `SET Thần Long` (không option); tab 8 bán `SET Hắc Ám Loạn Lưu` + `SET Hỗn Độn Vô Cực` (**không option**) ⇒ bản Phúc Lợi có option chính là bản "xịn", không trùng lặp vô nghĩa.
* Các item set mới đều tồn tại và hợp lệ: `1174-1178` (Hắc Ám), `1730-1734` (Nguyên Thủy), `1919-1923` (Hắc Ám Loạn Lưu), `1924-1928` (Hỗn Độn Vô Cực) — type 0..4 đúng 5 ô đầu, `icon_id` hợp lệ, `can_trade = 1` (option `30:1` khoá giao dịch hoạt động — [Trade.java:207](src/services/func/Trade.java#L207)).
* Bảng `player` **không có** cột điểm gốc ⇒ đậu vĩnh viễn cần cột mới.

---

## 2. THIẾT KẾ

### 2.1 Bộ 4 option % "chuẩn & đẹp" (đúng bảng admin)

| Vai trò | id | Tên trong DB | Việc phải làm |
|---|---|---|---|
| **% Sức đánh** | **50** | `「✦Dame✦」+#%` | **không sửa gì** — giữ `r_opt49_factor = 10`, ghi **param = % thật × 10** |
| **% Sinh lực** | **77** | `「✦ Sinh Lực ✦」+#%` | **không sửa gì** — giữ `r_opt77_factor = 10`, param **×10** |
| **% MaNa** | **103** | `「✦ MaNa ✦」+#%` | **không sửa gì** — giữ `r_opt103_factor = 10`, param **×10** |
| **% SĐ "đẹp" (bản thân + người xung quanh)** | **117** | `Đẹp +#% SĐ cho mình và người xq` | không phải sửa (1:1), chỉ lấy **max** |

⇒ Muốn `+25%` thật thì ghi **param `250`** (món hiện `+250%`). **Không sửa gì** trong [stat_rate.properties](data/config/stat_rate.properties) — `r_` giữ nguyên `10`.

> Ghi chú: ngoài 50 còn có `147` (+#% sức đánh, 1:1 sẵn) và `239/240` (`Xứ Mệnh +#% Sức Đánh`, 1:1) — dùng làm biến thể nếu muốn tên hiển thị khác.

### 2.2 Option dùng cho set cao cấp (đã kiểm chứng có tác dụng)

`158` nhãn | `75` nhãn Set Kích Hoạt | `66` **+30% SĐ** | `35:10` **+5% KI** (Nguyệt Ẩn) | `30:1` **khoá giao dịch** |
`239` **+% SĐ** | `77` **+% Sinh lực** | `103` **+% MaNa** | `94` **+% Giáp** | `5` **+% STCM** | `14` + chí mạng |
`204` **+% SĐ lên Boss** | `8` **hút % HP,KI xung quanh mỗi 5s** | `95`/`96` **biến % tấn công thành HP / MP** | `97` **phản % ST** |
`98`/`99` xuyên giáp chưởng / cận chiến | `105` **vô hình** | `108` **% né** | `117` **đẹp +% SĐ cho mình & xung quanh**

**Marker "danh tính" set của 2 set mới = marker có sẵn:**
* Tier 3 (Hắc Ám Loạn Lưu) mang **149, 150, 151, 152** ⇒ mặc 5/5 kích hoạt 4 bonus `set_config` (Thanh Long +500% KI, Chu Tước +500% HP, Bạch Hổ +1000% SĐ, Huyền Vũ +500% Giáp).
* Tier 4 (Hỗn Độn Vô Cực) mang **153-157** ⇒ kích hoạt **5 bonus `set_config` Ngũ Hành** (Kim +400% SĐ, Mộc +400% HP, Thổ +400% KI, Thủy +400% Giáp, Hỏa +200% STCM +10 chí mạng).
  → Bản đầu tôi định cho Tier 4 mang **cả 149-157** (9 bonus) nhưng như vậy mỗi món thành **14–15 dòng** (vượt "9–11 dòng" đã chốt với admin) nên **bản chốt chỉ dùng 153-157** như bảng mục 5 và file SQL v3.

### 2.3 Tư Tượng + Ngũ Hành: thêm **+% Sức đánh** (SQL, không cần build)

| id | Set | Trước | Sau |
|---|---|---|---|
| 1 | Thanh Long | KI +500% | KI +500%, **SĐ +200%** |
| 2 | Chu Tuoc | HP +500% | HP +500%, **SĐ +200%** |
| 3 | Bach Ho | SĐ +1000% | giữ SĐ +1000%, **thêm +5 chí mạng** |
| 4 | Huyen Vu | Giáp +500% | Giáp +500%, **SĐ +200%** |
| 5 | Kim | SĐ +5.000.000 (flat) | **SĐ +400%** |
| 6 | Moc | HP +10.000.000 (flat) | **HP +400%, SĐ +100%** |
| 7 | Tho | KI +10.000.000 (flat) | **KI +400%, SĐ +100%** |
| 8 | Thuy | Giáp +10.000.000 (flat) | **Giáp +400%, SĐ +100%** |
| 9 | Hoa | STCM +100% | **STCM +200%, +10 chí mạng, SĐ +150%** |

> Ngũ Hành chuyển flat → %. Muốn giữ đúng độ mạnh tuyệt đối thì chỉnh số trong 3.2/PHẦN 2A của file SQL; không cần build lại.

### 2.4 Đậu vĩnh viễn (yêu cầu #4)

* **Option 254** (mới) gắn lên cục **đậu thần 523** ở mọi mốc Phúc Lợi (đậu vẫn giữ option 2/48 hồi HP-KI như thường).
* Khi ăn: `player.dauVinhVien += 1` → **+1% HP, +1% SĐ, +1% KI vĩnh viễn** cho mỗi cục, **không giới hạn**, cộng dồn với mọi nguồn khác, sống qua relog/restart.
* Bonus đổ vào `tlHp/tlMp/tlDame` lúc tính point nên ăn ngay vào HP/SĐ/KI.
* Chống lạm dụng: chỉ tính theo **số đã ăn** (đậu bị trừ khi ăn), không theo số đang giữ trong hành trang.

---

## 3. VIỆC CODE CẦN LÀM

### GD1 — (tuỳ chọn, 1 dòng) cho option 204 ăn với đòn đánh thường lên Boss

[src/boss/Boss.java](src/boss/Boss.java#L800), ngay sau `damage = this.nPoint.subDameInjureWithDeff(damage);`:

```java
if (plAtt != null && plAtt.nPoint != null) {
    damage += damage * plAtt.nPoint.dameBoss / 100;   // option 204: +% sát thương lên Boss
}
```

Không làm GD1 thì option 204 chỉ mạnh khi người chơi đánh bằng **skill** (vẫn có tác dụng thật, chỉ hẹp hơn).

### GD2 — ✅ ĐÃ VIẾT XONG: đậu vĩnh viễn (item riêng + cột đếm + hook)

**Cách làm đã chốt (theo yêu cầu admin)**: copy đậu thần thành **1 item riêng**, **logic ăn giữ nguyên**, chỉ khác là ăn item mới thì **+1% HP / +1% Sức đánh / +1% KI vĩnh viễn, không giới hạn**.

Đã làm:

| Việc | Chi tiết |
|---|---|
| Item mới | [sql/dau_vinh_vien.sql](sql/dau_vinh_vien.sql) copy id **523** (`Đậu thần cấp 9`) thành item mới ở **cuối bảng** (`id = MAX(id)+1` = 1944), **tên giữ y hệt `Đậu thần cấp 9`** (theo yêu cầu admin), cùng `type=6`, `icon_id=241`, giữ nguyên luồng ăn (hồi HP/KI theo option 2/48 + hồi stamina cho đệ tử). Phân biệt với đậu thường **chỉ bằng option 254** hiện trong bảng thông tin |
| Option đánh dấu | **254 = `ĐẬU VĨNH VIỄN`** gắn lên item đó — vì tên **giống y hệt** đậu thần thường, đây là **cách duy nhất** để người chơi nhận ra |
| Quà Phúc Lợi | đã điền đậu vĩnh viễn vào **2 mốc đang RỖNG**: mốc **44** (tab Tích nạp 20.000 → 5.000 Bạc + **30 đậu** + Thẻ X2) và mốc **54** (tab Quà Coin 50.000 → **100 đậu** + 50 Cuồng nộ/Bổ huyết/Bổ khí). Guard `list_item IN ('[]','')` ⇒ **chỉ điền mốc rỗng, không ghi đè** mốc admin đã điền |
| Đọc cấp đậu an toàn | [UseItem.eatPea](src/services/func/UseItem.java#L3789): ưu tiên `name.substring(13)` như cũ, lỗi thì lấy **số đầu tiên trong tên** (trước đây `Integer.parseInt` thẳng ⇒ tên lạ là **crash handler**). Cách mới giữ nguyên kết quả với đậu 1–9, và **sửa luôn quirk cũ**: "Đậu thần cấp 10" từng bị đọc thành **cấp 0** (vì `substring(13)` = `"0"`) → nay ra đúng **10** |
| Cộng vĩnh viễn | sau khi ăn: `player.dauVinhVien += 1` → tính lại point + lưu DB + thông báo |
| Cột DB | `player.dau_vinh_vien` (thêm cột có guard `information_schema`, đọc có try/catch nên server vẫn chạy dù chưa ALTER) |
| Hook điểm | [NPoint.setPointWhenWearClothes](src/player/NPoint.java#L249): `tlHp/tlMp/tlDame.add(dauVinhVien)` ⇒ **+1% HP, +1% KI, +1% SĐ mỗi cục**, cộng dồn vô hạn, sống qua relog/restart |

✔️ **Đã kiểm bằng DB tạm `pl_v3_test`** (copy 4 bảng `player`/`item_template`/`item_option_template`/`phuc_loi_tab`): chạy file SQL **2 lần, cả 2 lần exit 0**; tổng option = **255 = MAX(id)+1** (không thủng index); item mới **id 1944, type 6, icon 241, comment `dau vinh vien`**; chạy lần 2 **không tạo thêm item** và **không ghi đè mốc 44/54** (guard `NOT EXISTS` + `list_item IN ('[]','')`); cả 2 mốc qua **`JSON_VALID = 1`**; DB thật `hondaodragon` **không bị sửa** (vẫn 254 option, 1944 item, 0 item `dau vinh vien`, 59 mốc đã có quà).

⚠️ **Bản chi tiết các bước code** (để đối chiếu):

1. **SQL:** `ALTER TABLE player ADD COLUMN dau_vinh_vien INT NOT NULL DEFAULT 0;`
2. **[Player.java](src/player/Player.java)** — thêm field cạnh các `point_*`:
   ```java
   public int dauVinhVien;
   ```
3. **[PlayerDAO.java](src/jdbc/daos/PlayerDAO.java)** — thêm `dau_vinh_vien` vào **danh sách cột của câu SELECT** (vùng ~1910), vào **câu UPDATE** (vùng ~1205) + setter tương ứng, và khi load:
   ```java
   player.dauVinhVien = rs.getInt("dau_vinh_vien");   // bọc try/catch như các cột mới khác
   ```
4. **[UseItem.eatPea()](src/services/func/UseItem.java#L3789)** — ngay sau khi tìm thấy `pea`:
   ```java
   if (pea.haveOption(254)) {
       player.dauVinhVien += 1;
       Service.gI().sendThongBao(player, "|1|Đậu vĩnh viễn +1% HP - Sức đánh - KI (tổng: "
               + player.dauVinhVien + "%)");
       jdbc.daos.PlayerDAO.updatePlayer(player);
   }
   ```
5. **[NPoint.setPointWhenWearClothes()](src/player/NPoint.java#L506-L520)** — chèn **sau** vòng lặp option item, **trước** `setDameTrainArmor();`:
   ```java
   // ĐẬU VĨNH VIỄN: mỗi cục đã ăn +1% HP, +1% Sức đánh, +1% KI
   if (this.player.dauVinhVien > 0) {
       this.tlHp.add((long) this.player.dauVinhVien);
       this.tlMp.add((long) this.player.dauVinhVien);
       this.tlDame.add((long) this.player.dauVinhVien);
   }
   ```
   (Đặt trước `setBasePoint()`/`setDame()` để số % vào ngay trong lần tính đó.)

### GD3 — Tạo option mới + nội dung 3 tab (chỉ SQL)

Chạy [phuc_loi_v2_option.sql](sql/phuc_loi_v2_option.sql) → **khởi động lại server** (để nạp option 254 — hoặc tạo option qua Panel thì không cần restart) → **Panel → Reload Phúc Lợi** → người chơi **đăng nhập lại** để client nhận danh sách option mới.

### GD4 — ✅ ĐÃ VIẾT XONG: 3 option trước đây không có code

Admin yêu cầu viết code cho `192, 79, 233, 234, 247`. Kết quả điều tra lại (mục 0.5): **233/234 không cần code** — chỉ cần 1 dòng `set_config` là thành marker set; **3 option cần code thật** và đã làm:

| Option | File | Thay đổi |
|---|---|---|
| **192** `+#% Chí mạng` | [NPoint.addOption](src/player/NPoint.java#L757) | `case 192: this.critAdd += io.param;` (alias của 14) |
| **79** `Đệ tử #% sức đánh` | [NPoint](src/player/NPoint.java) | Thêm field `public short tlDamePet;` + `case 79: this.tlDamePet += io.param;` + reset trong `resetPoint()` + áp dụng ở nhánh dame của pet: *sư phụ mặc → đệ tử +`param`% sức đánh* |
| **247** `Kích Sát Boss +#%` | [NPoint](src/player/NPoint.java) + [Boss.injured](src/boss/Boss.java#L800) | Thêm field `public double dameKichSatBoss;` + `case 247` + reset + trong `Boss.injured()`: `damage += damage * dameKichSatBoss / 100` (đồng thời bổ sung luôn **option 204** `dameBoss` cho **đòn đánh thường** — trước chỉ ăn với skill) |

✔️ **Đã build kiểm chứng**: `javac --release 21 --enable-preview` toàn bộ 663 file `src/**` → **exit 0**; đã đóng lại `dist/BeMeoGaming.jar` (backup bản cũ ở `dist/BeMeoGaming.jar.bak-20261013-npoint`).

### GD5 — Gán 5 option “không code” thành marker set (chỉ SQL)

Thêm 5 dòng `set_config` (tuỳ chọn, khi nào cần set tương ứng):

| id | name | option_id | skill_dmg / bonus |
|---|---|---|---|
| 10 | SET Đệ Kame | 188 | `skill_dmg = "<id Kame>:20000"` |
| 11 | SET Đệ Masenco | 189 | `skill_dmg = "<id Masenco>:20000"` |
| 12 | SET Đệ Antomic | 190 | `skill_dmg = "<id Antomic>:20000"` |
| 13 | Fam Thiên Đạo | 233 | `hp_pct/ki_pct/dame_pct` tuỳ chỉnh |
| 14 | Fam Địa Đạo | 234 | `hp_pct/ki_pct/dame_pct` tuỳ chỉnh |

> 🚫 **Không chạy GD5 nếu chưa cần** — vì `set_config` đã có 9 dòng cho Tư Tượng/Ngũ Hành; thêm dòng mới cho 188-190/233-234 là để dành, không ảnh hưởng set nào đang chạy.

### GD6 — Không còn slot option: muốn thêm option mới thì **tái sử dụng id chết**

Bảng option hiện **254 dòng (0..253)**. Sau khi tạo **254 = ĐẬU VĨNH VIỄN** là **255 = chạm trần** (`writeByte` 1 byte). Muốn thêm option nữa thì **không tạo id mới**, mà lấy 1 id trong **67 id chết** ở mục 0.7 → đổi tên + viết code (xem quy trình ở mục 0.7). **Không `DELETE`.**

---

## 4. NỘI DUNG 3 TAB

### TAB 1 — Điểm danh tuần (id 2–8, `active=0`, `max_count` = số thứ)

| id | Thứ | Quà |
|---|---|---|
| 2 | T2 | 1.000 Lượng Bạc, 20 **Đậu vĩnh viễn** (523 + `254:1`) |
| 3 | T3 | 2.000 Lượng Bạc, 20 Đậu vĩnh viễn, Thẻ X2 điểm Farm (1916) |
| 4 | T4 | 2.000 Lượng Bạc, 10 Cường nộ (381), 10 Bổ huyết (382), 10 Bổ khí (383) |
| 5 | T5 | 3.000 Lượng Bạc, 5 Cường Nộ Siêu Cấp (1099), 5 Bổ Huyết SC (1100), 5 Giáp Xun SC (1102) |
| 6 | T6 | 3.000 Lượng Bạc, Thẻ Next Nhiệm Vụ (1108), Thẻ Reset Nhiệm Vụ (1109) |
| 7 | T7 | 5.000 Lượng Bạc, 1 Lượng Vàng (1270), Thẻ X3 điểm Farm (1917), Đá bảo vệ (987) |
| 8 | CN | 10.000 Lượng Bạc, 2 Lượng Vàng, Thẻ X5 điểm Farm (1918), Bùa Đủ Tứ (522) |

### TAB 2 — Tích nạp (điền 44–53 + mốc mới 90–93, `active=0`, `count` = coin nạp, nhận 1 lần)

| id | Mốc (coin nạp) | Quà |
|---|---|---|
| 44 | 20.000 | ✅ **ĐÃ ĐIỀN** (file `dau_vinh_vien.sql`): 5.000 Lượng Bạc (1271), **30 Đậu vĩnh viễn** (1944 + `254:1`), Thẻ X2 (1916) |
| 45 | 30.000 | 5.000 Lượng Bạc, Thẻ X3, Thẻ Next NV |
| 46 | 50.000 | 10.000 Lượng Bạc, 2 Lượng Vàng, Thẻ X5 |
| 47 | 100.000 | 20.000 Lượng Bạc, 5 Lượng Vàng, Bùa Đủ Tứ, Thẻ VIP (1217) |
| 48 | 500.000 | 50.000 Lượng Bạc, 10 Lượng Vàng, 10 Thỏi Vàng (457), Thẻ tăng 1 Cảnh Giới (1132) |
| 49 | 1.000.000 | 100.000 Lượng Bạc, 20 Lượng Vàng, 500k CS Gốc (1124), Đá Thần Ngũ Sắc (674) |
| 50 | 1.500.000 | 100.000 Lượng Bạc, 30 Lượng Vàng, Bông Tai Potara C1 (454), Hộp Quà Danh Hiệu (1741) |
| 51 | 2.000.000 | 200.000 Lượng Bạc, 50 Lượng Vàng, Bùa Trí Tuệ x3 (671), Linh Thú Kỳ Lân (1736) |
| 52 | 3.000.000 | 200.000 Lượng Bạc, 50 Lượng Vàng, Trứng Đệ Black Super (1159), Hộp Quà Cải Trang (1867) |
| 53 | **5.000.000** | **SET HẮC ÁM – TIER 1** (1174–1178) — option mục 5 |
| **90** | **10.000.000** | **SET NGUYÊN THỦY – TIER 2** (1730–1734) — option mục 5 |
| **91** | 20.000.000 | Trứng Đệ Broly Super (1885), Đá Thiên Đạo (1224), Đá Địa Đạo (1225), Tăng 1M CS Gốc (1123) |
| **92** | **50.000.000** | **SET TƯ TƯỢNG** (Hắc Ám Loạn Lưu 1919–1923) — option mục 5 |
| **93** | **100.000.000** | **SET NGŨ HÀNH** (Hỗn Độn Vô Cực 1924–1928) — option mục 5 |

### TAB 3 — Quà Coin (54–62 + mốc mới 87–89, `active=1`, `max_count` = **giá Coin**, mua lại vô hạn)

| id | Giá Coin | Quà |
|---|---|---|
| 54 | 50.000 | ✅ **ĐÃ ĐIỀN** (file `dau_vinh_vien.sql`): **100 Đậu vĩnh viễn** (1944 + `254:1`), 50 Cường nộ (381), 50 Bổ huyết (382), 50 Bổ khí (383) |
| 55 | 50.000 | 50 Giáp Xun bh (384), 50 Cường Nộ SC, 50 Bổ Huyết SC, 50 Bổ Khí SC |
| 56 | 100.000 | 1.000 Lượng Bạc, Thẻ X2, Thẻ Next NV |
| 57 | 100.000 | 1 Lượng Vàng, Thẻ Reset NV, 5 Đá may mắn c3 (1081) |
| 58 | 150.000 | Thẻ X3, Bùa Đủ Tứ, 5 Đá nâng cấp c3 (1076) |
| 59 | 200.000 | Thẻ X5, Thẻ tăng 1 Cảnh Giới, 500k CS Gốc (1125) |
| 60 | 300.000 | Thẻ VIP, Đá Thần Ngũ Sắc, Hộp Quà Ngọc (1866) |
| 61 | 500.000 | Hộp Quà Danh Hiệu, Hộp Quà Cải Trang, Bùa Trí Tuệ x3 |
| 62 | 1.000.000 | Hộp Quà Thú Cưng (1745), Tăng 1M CS Gốc, Thẻ Chuyển Sinh (1855) |
| **87** | **2.000.000** | **SET TƯ TƯỢNG** (1919–1923, mua lại vô hạn) |
| **88** | 3.000.000 | Bông Tai Potara C3 (1405), 10 Đá Thần Sắc (1666), 10 Đá Thần Quang (1667), 5 Đá nâng cấp c5 (1078) |
| **89** | **5.000.000** | **SET NGŨ HÀNH** (1924–1928) + **SKH VIP PRO** (1856) — *6 món ⇒ cần **≥7 ô trống*** |

---

## 5. NỘI DUNG OPTION TỪNG SET — BẢN CHỐT (2 cơ bản + 2–3 quan trọng)

> **Cách chia đã chốt với admin**: mỗi món chỉ **2 option cơ bản (bắt buộc)** + **2–3 option quan trọng**, tổng **4–6 dòng chỉ số/món** ⇒ client hiển thị thoải mái. Set cấp thấp chỉ dùng Cấp 1, lên cấp thì thêm tầng. Mọi con số sửa được (SQL thuần, không cần build).

### 5.0 QUY TẮC CHUNG

| Loại | Option | Ghi chú |
|---|---|---|
| **Khung (bắt buộc, mọi món)** | `30:1` = khoá giao dịch | luôn có |
| **Cơ bản (2/món, luân phiên)** | 2 trong 3: **`50`** %SĐ · **`77`** %Sinh lực · **`103`** %MaNa | cả set vẫn đủ 3 loại (xem tổng bên dưới) |
| **Quan trọng (1–3/món)** | chọn theo cấp: `94, 14, 5, 18, 8, 19, 204, 95, 96, 97, 98, 99, 105, 108, 117` | theo bảng từng tier |
| **Set bonus riêng** | T1/T2: `66:0` (+30% SĐ khi đủ 5/5). T3/T4: markers + `set_config` | |

**Quy tắc param (admin CHỐT: giữ nguyên hệ số 10, không đụng option cũ)** — với 3 option `50/77/103`, server nhân hệ số **0.1** ⇒ **param phải ghi = % thật × 10**:

| Muốn thật | Phải ghi param | Số hiện trên món |
|---|---|---|
| +25% | `250` | `+250%` ⚠️ hiện 10× thật |
| +50% | `500` | `+500%` |
| +90% | `900` | `+900%` |
| +150% | `1500` | `+1500%` |

Các option **1:1** (`147`/`239`/`240` %SĐ, `94` %Giáp, `5` STCM, `14` chí mạng, `117` đẹp %SĐ) ghi **đúng số thật** (hiện = thật).
> Nếu sau này muốn "hiện = thật" cho 50/77/103 thì phải đổi hệ số (đã bị loại vì làm 11 món SET cũ mạnh ×10) **hoặc** chuyển %HP/%KI sang `set_config`.

Thang số (số THẬT · param ghi trong ngoặc): **T1 = 25% (250) · T2 = 50% (500) · T3 = 90% (900) · T4 = 150% (1500)**.

---

### TIER 1 — SET HẮC ÁM (1174–1178) · 5.000.000 nạp / 1.000.000 Coin · **CHỈ CẤP 1**

| Món | Cơ bản | Quan trọng | Khung | Dòng |
|---|---|---|---|---|
| 1174 Áo | `50:250` `77:250` | `94:30` `47:10000` | `66:0` `30:1` | 6 |
| 1175 Quần | `77:250` `103:250` | `6:300000` `8:5` | `66:0` `30:1` | 6 |
| 1176 Găng | `50:250` `103:250` | `0:15000` `14:3` | `66:0` `30:1` | 6 |
| 1177 Giày | `50:250` `77:250` | `18:20` `16:20` | `66:0` `30:1` | 6 |
| 1178 Nhẫn | `103:250` `50:250` | `5:10` `19:20` | `66:0` `30:1` | 6 |

**Tổng 5/5**: **+100% SĐ · +75% Sinh lực · +75% MaNa** · +30% SĐ (66) · +30% Giáp · +300.000 HP · +15.000 SĐ · +10.000 Giáp · +3% chí mạng · +10% STCM · +5% hút HP/KI xq · +20% chính xác · +20% tốc độ di chuyển · +20% SĐ khi đánh quái.

---

### TIER 2 — SET NGUYÊN THỦY (1730–1734) · 10.000.000 nạp · **CẤP 1 + 2**

| Món | Cơ bản | Quan trọng | Khung | Dòng |
|---|---|---|---|---|
| 1730 Áo | `50:500` `77:500` | `94:60` `95:10` | `66:0` `30:1` | 6 |
| 1731 Quần | `77:500` `103:500` | `204:50` `173:10` | `66:0` `30:1` | 6 |
| 1732 Găng | `50:500` `103:500` | `5:20` `14:6` | `66:0` `30:1` | 6 |
| 1733 Giày | `50:500` `77:500` | `18:30` `108:10` | `66:0` `30:1` | 6 |
| 1734 Nhẫn | `103:500` `50:500` | `8:10` `97:15` | `66:0` `30:1` | 6 |

**Tổng 5/5**: **+200% SĐ · +150% Sinh lực · +150% MaNa** · +30% SĐ (66) · +60% Giáp · +20% STCM · +6% chí mạng · +50% SĐ lên Boss · +30% chính xác · +10% né · +15% phản ST.

---

### TIER 3 — SET TƯ TƯỢNG (1919–1923) · 50.000.000 nạp / 2.000.000 Coin · **+ CẤP 3**

Marker Tư Tượng **bắt buộc cả 5 món**: `149` Thanh Long · `150` Chu Tước · `151` Bạch Hổ · `152` Huyền Vũ.

| Món | Cơ bản | Quan trọng | Chung | Dòng |
|---|---|---|---|---|
| 1919 Áo | `50:900` `77:900` | `94:100` `95:20` | `117:25` `149:0` `150:0` `151:0` `152:0` `30:1` | 10 |
| 1920 Quần | `77:900` `103:900` | `173:20` | nt | 9 |
| 1921 Găng | `50:900` `103:900` | `5:35` `14:10` | nt | 10 |
| 1922 Giày | `50:900` `77:900` | `108:20` | nt | 9 |
| 1923 Nhẫn | `103:900` `50:900` | `97:30` `105:1` | nt | 10 |

**Tổng 5/5**: **+360% SĐ · +270% Sinh lực · +270% MaNa** · **+125% SĐ "đẹp"** (117 — buff cả người xung quanh) · **4 bonus `set_config` Tư Tượng** · +100% Giáp · +20% STCM · +10% chí mạng · +20% né · +30% phản ST · hút 20% ST→HP · hồi HP/KI đồng đội · vô hình.

---

### TIER 4 — SET NGŨ HÀNH (1924–1928) · 100.000.000 nạp / 5.000.000 Coin · **FULL 3 CẤP**

Marker Ngũ Hành **bắt buộc cả 5 món**: `153` Kim · `154` Mộc · `155` Thổ · `156` Thủy · `157` Hỏa.

| Món | Cơ bản | Quan trọng | Chung | Dòng |
|---|---|---|---|---|
| 1924 Áo | `50:1500` `77:1500` | `94:150` `95:30` | `117:40` `153:0` `154:0` `155:0` `156:0` `157:0` `30:1` | 11 |
| 1925 Quần | `77:1500` `103:1500` | `96:30` | nt | 10 |
| 1926 Găng | `50:1500` `103:1500` | `5:60` `14:15` | nt | 11 |
| 1927 Giày | `50:1500` `77:1500` | `97:50` | nt | 10 |
| 1928 Nhẫn | `103:1500` `50:1500` | `105:1` `98:30` | nt | 11 |

**Tổng 5/5**: **+600% SĐ · +450% Sinh lực · +450% MaNa** · **+200% SĐ "đẹp"** · **5 bonus `set_config` Ngũ Hành** (bảng 2.3) · +150% Giáp · +60% STCM · +15% chí mạng · +30% xuyên giáp · +50% phản ST · hút 30% ST→HP/MP · vô hình.

> Vì mỗi món chỉ **9–11 dòng** (thay vì 14–16 như bản trước) nên **không cần** sửa code nhóm marker nữa.

---

## 6. TRÌNH TỰ THI HÀNH & NGHIỆM THU

| Bước | Việc | Cách kiểm |
|---|---|---|
| 0 | Backup 3 bảng `phuc_loi_tab`, `set_config`, `item_option_template` | file xuất tồn tại |
| 1 | Chạy **PHẦN 1** (option 254) → **khởi động lại server** | `SELECT COUNT(*) FROM item_option_template` = **255** |
| 2 | **Kiểm (không sửa)** hệ số `opt49/77/103_factor` phải **= 10** | Panel → StatRate hiện `10`; mặc đủ 5/5 tier 1 → SĐ +100%, Sinh lực/MaNa +75% (khớp bảng mục 5) |
| 3 | Chạy **PHẦN 2A** (%SĐ tư tượng/ngũ hành) → Panel **Reload** | `SELECT id,name,dame_pct FROM set_config` khớp bảng 2.3 |
| 4 | Chạy **GD2** (cột + code) rồi **build lại jar** | `SHOW COLUMNS FROM player LIKE 'dau_vinh_vien'` |
| 5 | Chạy **PHẦN 3, 4, 5** (nội dung 3 tab) → Panel **Reload Phúc Lợi** | tab 1 = 7 mốc, tab 2 = 14, tab 3 = 12; không mốc nào rỗng/sai JSON |
| 6 | Người chơi **đăng nhập lại**, mở bảng Phúc Lợi | thấy đủ mốc, tên mốc shop có `(Giá: … Coin)` |
| 7 | Test: nhận mốc điểm danh T2, nhận mốc tích nạp, mua 2 món Coin | item vào hành trang **đúng option** (mở info thấy `+250% Sinh lực` = **25% thật**) |
| 8 | Test đậu: ăn 1 cục → thông báo +1%, chỉ số HP/SĐ/KI tăng, **relog vẫn còn** | `SELECT dau_vinh_vien FROM player` tăng |
| 9 | Test set: mặc đủ 5/5 tier 4 | chỉ số nhảy theo `set_config`; sửa số trong bảng → Reload là thấy đổi ngay |

### Kết quả đã kiểm bằng DB tạm `pl_v2_test` (chạy file SQL 2 lần, exit 0)
| Hạng mục | Kết quả |
|---|---|
| Lỗi khi chạy | **0** (chạy lại lần 2 cũng exit 0 nhờ `ON DUPLICATE KEY UPDATE`) |
| Tổng số option | **255** (đúng trần) |
| Số mốc tab 1 / 2 / 3 | **7 / 14 / 12**; tổng **85** (< 127) |
| Mốc rỗng / JSON sai | **0** |
| 64 item id dùng trong file | **tồn tại hết**, không item nào `icon_id = -1` |
| 35 option id dùng trong file | **tồn tại hết** trong `item_option_template` |
| `set_config` sau khi sửa | khớp bảng 2.3 (đã SELECT đối chiếu) |

---

## 7. RỦI RO & CÁCH XỬ LÝ

| Rủi ro | Mức | Xử lý |
|---|---|---|
| **Trần 255 option** — vượt là client đọc sai toàn bộ tên option | **Cao** | Chỉ tạo 1 option (254); sau khi làm xong là **hết chỗ**, muốn thêm phải xoá option chết (42-46) hoặc đổi `writeByte → writeShort` cả server + client |
| Quên nạp option 254 trước khi Reload Phúc Lợi ⇒ mốc chứa đậu **bị bỏ** (log `Mốc Phúc Lợi id=... sai dữ liệu`) | Trung bình | Đúng thứ tự bước 1 → 5; kiểm log `Load phuc_loi_tab thành công (85)` |
| **Số hiện trên món = 10× số thật** (hệ quả của việc giữ hệ số 10: param `250` hiện `+250%`, thật `+25%`) | Trung bình | Admin đã chốt chấp nhận; giải thích cho người chơi, và/hoặc ghi **% thật vào `description`** của món |
| `conChoTrong` cần **dư 1 ô** ⇒ mốc 5 món cần 6 ô, mốc 6 món (id 89) cần 7 ô | Trung bình | Đã tính; ghi chú cho người chơi |
| Tổng mốc > 127 ⇒ client lỗi | Thấp (85) | Câu check 6.2 |
| Client giới hạn số dòng option ⇒ tier 4 (17-19 option) bị cắt | Trung bình | Bỏ `{75}`/`153-157` hoặc tách sang món khác |
| Option 42-46 đang có trên dữ liệu; 249-253 là marker có chủ đích | — | **Không dùng lại** các id này làm marker mới |
| Quà trùng shop tab 5 / tab 8 | Thấp | Bản Phúc Lợi là bản có option (chủ ý) |
| Ngũ Hành flat → % lệch sức mạnh cũ | Trung bình | Bảng 2.3 ghi rõ trước/sau; sửa số là xong, không cần build |
