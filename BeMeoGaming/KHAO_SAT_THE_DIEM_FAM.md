# Khảo sát: Thẻ x2/x3/x5 Điểm Farm + Thẻ giao dịch Điểm Farm

> Trạng thái: **ĐÃ CODE + DEPLOY 27/09/2026** (server dang chay, panel mo). User tu setup shop.
> Xem muc "6. DA LAM" o cuoi file cho phan lech so voi ke hoach.
> Quyết định đã chốt: thẻ buff **60 phút** (chỉnh được trên panel), kênh giao dịch = **sửa Vé Tặng Điểm 1251**.

---

## 1. Điểm farm hiện tại (không đổi, chỉ bơm hệ số)

- Nguồn chính: `Mob.getItemMobReward` (`src/mob/Mob.java` ~dòng 1076-1095), maps 0–167, sau khi roll drop:
  - `realMin = SystemTuning("farm_min")` = 500
  - `realMax = "farm_base_max"(1500) + Saga_VIP * "farm_vip_moi"(10000)`, trần `"farm_toi_da"`(150000)
  - `realGain = Util.nextInt(realMin, realMax)` → `player.diemfam += realGain` → toast.
  - **Toàn bộ nằm trên panel**: tab *He Thong → nhóm "Diem Farm"* (`src/panel/tuning/SystemTuning.java` dòng ~106).
- Nguồn KHÁC (không áp thẻ): boss `BOSSDIEMFAM` (1–20.000), capsule `UseItem` (+1–5 triệu), event.
- Chi tiêu: NPC (Nâng cấp bông tai/chân mệnh, mở limit power...), Combine, Đua Top, Web.

## 2. Khuôn làm thẻ buff (copy nguyên từ thẻ đang chạy)

Thẻ mẫu: **Bình cần x3/x5/x7/x10** (1233–1236, TYPE=29, 60′, icon 21877–21880), **Thuỷ Dược X2..X7** (1326–1331, 10′).

Chuỗi hoạt động (5 chỗ, đều có mẫu sẵn):

| Bước | File | Việc cần làm |
|---|---|---|
| 1. Item | DB `item_template` | Thêm **1916 "Thẻ X2 Điểm Farm", 1917 X3, 1918 X5** — TYPE=29, gender=0, can_trade=1, icon_id=21330 (dùng lại icon Thuỷ Dược, **không cần bump version client**; muốn icon riêng thì sinh 3 icon + bump x1–x4 như cải trang) |
| 2. Dùng | `src/services/func/UseItem.java` | `case 1916/1917/1918` → set `itemTime.isFamX2/X3/X5 + lastTimeFamX* = now` (mẫu case 1233 dòng ~1711) |
| 3. Hết giờ | `src/item/ItemTime.java` | Thêm 6 field (3 flag + 3 timestamp), `update()` tự tắt (mẫu dòng 197-218 `Util.canDoWithTime(last, TIME)`), hằng `TIME_FAM_CARD = 3600000` hoặc đọc từ tuning |
| 4. Đếm ngược | `src/services/ItemTimeService.java` | `sendItemTime(player, icon, giayConLai)` cho 3 thẻ (mẫu dòng 74-105) |
| 5. Lưu/đọc lại | `PlayerDAO` + `NDVSqlFetcher` | `item_time_new` là **JSON positional, hiện 27 slot (index 0–26)** → append 3 slot cuối (27,28,29). **Bắt buộc size-guard khi đọc** (`if size()>29`) vì save của player cũ sẽ ngắn hơn → nếu không guard sẽ vỡ load player |
| 6. Áp hệ số | `src/mob/Mob.java` (chỗ `player.diemfam += realGain`) | `heSo = isFamX5?5 : isFamX3?3 : isFamX2?2 : 1` (**cộng dồn**: 1 thẻ thì lấy đúng hệ số thẻ, từ 2 thẻ trở lên = tổng hệ số − 1, vd X2+X3 = x4; thời gian các thẻ tính riêng, dùng trùng thẻ thì cộng thêm 1 giờ) → `realGain *= heSo` → vẫn chặn trần `farm_toi_da` sau khi nhân → toast ghi rõ "xN thẻ Farm" |

### Thêm SystemTuning (nhóm "Diem Farm"):
- `farm_card_thoi_gian` (giây, default 3600) — thời lượng buff, sửa trên panel không cần build.
- (tuỳ chọn) `farm_card_applies_boss` = 0/1 — có áp cho boss `BOSSDIEMFAM` không (mặc định 0).

## 3. Vé Tặng Điểm 1251 — hiện tại ĐANG BUG

- `UseItem case 1251` (dòng 642): mở form `Input.phieutangdiem()` **và cắt thẻ ngay** dù người chơi chưa nhập/đóng form → **mất thẻ oan**.
- `Input case tangdiem` (dòng 124-160):
  - `if (player.diemfam <= 100_000_000) break;` — bắt buộc người gửi có **>100 triệu** điểm, sai ý nghĩa thẻ.
  - Số điểm phải `100M–1B` trong khi báo lỗi ghi *"Chỉ có thể chuyển tối đa Từ 10100 Đến 10.000.000"* → thông báo ≠ code.
  - `Client.gI().getPlayer(name)` → **chỉ chuyển được cho người đang ONLINE**.
  - Không log lịch sử; dùng `sendThongBaoFromAdmin` (sai chức năng).
- TYPE=27 **giao dịch được qua cửa sổ Trade** (`Trade.isItemCannotTran` chỉ block id 590 trong type 27) → vé vẫn mua/bán tự do giữa 2 người.

### Kế hoạch sửa (chốt theo lựa chọn "Sửa Vé 1251"):
1. **Cắt thẻ đúng lúc**: chỉ `subQuantityItemsBag` khi chuyển **thành công** (trong case tangdiem), không cắt lúc mở form.
2. Bỏ bug >100M; giới hạn min/max chuyển đọc từ SystemTuning (`fam_transfer_min`, `fam_transfer_max`) — default 1.000 … 10.000.000 (theo đúng thông báo cũ).
3. **Hỗ trợ offline**: target không online → ghi thẳng `UPDATE player SET diemfam = diemfam + ? WHERE name = ?` (DB `hondaodragon`, cột `diemfam` đã lưu/đọc qua `PlayerDAO`/`NDVSqlFetcher`) — cẩn thận: nếu player đang login ở máy khác thì phải khớp lại, chỉ update khi chắc chắn offline.
4. **Ghi lịch sử**: dùng `HistoryTransactionDAO` (đang dùng cho Trade) — log người gửi/nhận/số điểm/thời gian.
5. Thông báo đúng chức năng (dùng `sendThongBao` thường).

## 4. Phân phối

- **User tự setup shop** (panel/shop NPC hoặc web_shop) — server chỉ cần 1916–1918 tồn tại + cơ chế trên hoạt động.
- Vé 1251 đã có sẵn trong game; sau khi sửa cơ chế là shop cũ bán tiếp chạy đúng.

## 5. Rủi ro / lưu ý khi làm

- **Quan trọng nhất**: slot JSON `item_time_new` positional — thêm phải kèm size-guard, sai index là load sai buff của mọi player.
- ID 1916–1918: grep trước khi chốt chắc không trùng hardcode (MAX hiện tại = 1915, item template count 1916).
- Client đã cache version: **không cần bump vsData/vsItem** nếu chỉ thêm item_template row?? → CẦN KIỂM TRA: item_template thêm row có tự reload (`vsItem`)? → khi code thật, check `DataGame` vsItem như lần cải trang (lần trước phải bump 12→13). Thêm 3 dòng item mới nhiều khả năng cũng phải bump `vsItem`.
- Toast farm hiện spam mỗi kill — khi có thẻ thì gộp dòng "xN" vào toast cũ, không thêm popup mới.

## 6. Checklist khi code (ước lượng ~6 file + 1 SQL)

- [x] SQL: insert 1916/1917 (`sql/the_diem_fam.sql`) + bump `vsItem` (`DataGame`)
- [x] [Sua nham anh] 32326.png la anh THE GIAO DICH -> `sql/the_giao_dich_1251.sql`:
      1251 icon_id 11796 -> 32326, XOA item 1918 (khong co anh x5; code x5 giu nguyen),
      `vsItem` 14 -> 15 de client tai lai template (icon 1251 doi).
- [x] `ItemTime.java`: 3 flag + 3 lastTime + update() + `thoiGianTheFamMs()` + `heSoDiemFarm()`
- [x] `UseItem.java`: case 1916/1917/1918 (ca 2 cho: dispatch + useItemTime)
- [x] `ItemTimeService.java`: 3 dong dem nguoc (icon 32324/32325/32326)
- [x] `PlayerDAO` + `NDVSqlFetcher`: append 3 slot cuoi (27,28,29) + size-guard
- [x] `Mob.java`: nhan he so + chan tran (tuy chon tren panel) + toast "The xN"
- [x] `SystemTuning.java`: 4 key trong nhom "Diem Farm" / phan "The diem farm"
- [x] `Input.java` + `UseItem case 1251`: sua 5 loi ve tang diem
- [x] Compile -> kill java/watchdog -> deploy -> restart (open-panel.bat) -> boot sach, panel mo
- [ ] TEST IN-GAME (chua chay duoc vi can client): dung the (toast xN, icon dem nguoc), kill mob
      (diem nhan), het gio tu tat, tang diem online/offline, ve khong mat khi huy form

## 6.5 BUG THAT DA TIM: bang version icon (smallimage_version)

- **Trieu chung**: them item moi thi khong thay icon (o trong hanh trang).
- **Nguyen nhan**: `data/smallimage_version/x{1..4}/smallimage_version_data` la bang version
  icon theo tung `icon_id`, gui cho client bo msg **-77 khi LOGIN**:
  `short BE = maxVersion (= maxIconId + 1)` + tung byte = `len(icon_botnet/x{z}/{id}.png) % 127`
  (thieu file = -1). Bo them icon ma khong keo dai bang -> icon_id nam ngoai bang -> client khong
  biet co icon moi -> khong yeu cau -> o trong.
- **Bang chung**: luc them 32289/32290 (24/09) bang co 32293 byte (header 32291); luc them
  32322/32323 (26/09) da regenerate -> 32326 byte (header 32324). Lan nay (32324/32325/32326)
  thi QUEN -> bi loi.
- **Da sua triet de**: `DataGame.extendSmallImageVersion()` (chi BO SUNG phan cuoi + doi header,
  giu du lieu cu) duoc goi 1 lan trong `Manager` luc boot -> tu hien tuong lai, chi can restart.
  Log: `Keo dai smallimage_version x1..x4: 32324 -> 32327`.
- **Khong lien quan**: `data/res` (chi 40 id, la res UI, khac voi icon vat pham).

## 6. DA LAM - phan lech so voi ke hoach

1. **Icon dung anh that** (khong dung lai icon 21330): user dua 3 anh 1254x1254 vao
   `data/anh_the/32324|32325|32326.png` -> script tach nen caro + scale 32/48/64
   -> `data/icon_botnet/x2|x3|x4/<icon_id>.png`. Preview: `data/anh_the/preview.html`.
   `32324` = the x2 (1916), `32325` = the x3 (1917), `32326` = the giao dich (1251).
2. **Them key `farm_card_vuot_tran`** (mac dinh 1): truoc do ke hoach re-apply `farm_toi_da`
   sau khi nhan -> neu VIP da dat tran thi the x2/x3/x5 se khong co y nghia gi. Hien tai mac dinh
   THE DUOC VUOT TRAN, admin tat tren panel neu muon chan.
3. **Lich su tang diem ghi file** `log/chuyen_diem_fam.log` thay vi `HistoryTransactionDAO`
   (DAO do chi nhan duoc 2 `Player` object -> khong dung voi truong hop tang cho nguoi OFFLINE).
4. **`phieutangdiem` chuyen o nhap so sang NUMERIC** + them cau chu thich tren form.
5. `farm_card_thoi_gian` mac dinh 3600 giay (60 phut), doc tai su dung va tai het gio ->
   sua tren panel se ap dung cho ca buff dang chay.
