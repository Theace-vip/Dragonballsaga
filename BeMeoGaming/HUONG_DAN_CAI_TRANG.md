# HƯỚNG DẪN THÊM 1 CẢI TRANG MỚI (khảo sát từ code + DB thật)

Đối chiếu từ các cải trang đang có: id 283 (Yarirobe), 405 (Fide c1), 421 (Sơn Tinh)...
→ **327 cải trang đầy đủ** (head+body+leg) đang trong `item_template`.

## 1. HÌNH ẢNH CẦN CHUẨN BỊ: 33 ảnh PNG

| Loại | Số lượng | Frame dùng chung (KHÔNG cần ảnh riêng) |
|---|---|---|
| Ảnh **đầu** (head) | **2** | frame 3 dùng chung id `20` |
| Ảnh **thân** (body) | **16** | frame cuối dùng chung id `16` |
| Ảnh **chân** (leg) | **13** | frame cuối dùng chung id `34` |
| **Icon item** (ô hành trang) | **1** | — |
| **Avatar** (chân dung khung chat/UI) | **1** | — |
| **TỔNG** | **33 ảnh** | |

Mỗi ảnh cần **3 bản theo zoom**: `x2`, `x3`, `x4` → 33 × 3 = **99 file**
(quy tắc: **x3 = x2 × 1.5, x4 = x2 × 2**, ví dụ đầu 50×52 → 75×78 → 100×104;
icon 32×32 → 48×48 → 64×64; avatar 188×102 → 282×153 → 376×204).
→ Nếu designer đưa 1 bộ, mình tự sinh 2 bộ còn lại bằng tool (ImageMagick/PIL).

**Nơi đặt file** (server tự phục client theo id, msg `-67`):
```
BeMeoGaming/data/icon_botnet/x2/<id>.png
BeMeoGaming/data/icon_botnet/x3/<id>.png
BeMeoGaming/data/icon_botnet/x4/<id>.png
```
- `<id>` = id ảnh mới, tự chọn, **không trùng** id đang có (hiện最大 32290 → bắt đầu từ 32291).
- Ảnh PNG nền trong suốt (RGBA).
- Mỗi frame có offset hiệu chỉnh `[id, dx, dy]` (thực tế các costume đang có dx/dy từ -4..+6);
  nếu designer không cung cấp thì để `0,0` — chỉnh sau được.

## 2. DỮ LIỆU DB CẦN THÊM

### a) `part` — 3 dòng (id tiếp tục sau MAX(id)=2139 → 2140/2141/2142)
Server **tự regenerate** `data/update_data/part` từ bảng này khi boot (Manager.java:406).
```json
head (TYPE=0): [[<id_ảnh1>,0,1],[<id_ảnh2>,-3,1],[20,0,0]]          -- 3 frame
body (TYPE=1): [[id1..id16],[16,0,0]]                                 -- 17 frame
leg  (TYPE=2): [[id1..id13],[34,0,0]]                                 -- 14 frame
```

### b) `item_template` — 1 dòng
```
id (mới), TYPE=5, gender=3, NAME='Cải trang', description='Cải trang thành ...',
icon_id=<id icon item>, head=<part head mới>, body=<part body mới>, leg=<part leg mới>,
gold/gem/ruby (giá), power_require=0, can_trade=1, level=0, part=-1, is_up_to_up=0
```

### c) `head_avatar` — 1 dòng: `<head_part_id> → <avatar_id>`
(6/327 costume đang thiếu nên không bắt buộc, nhưng nên có để avatar khung chat đúng.)

### d) `array_head_2_frames` — CHỈ cần nếu đầu phải anim 2 frame (thường không).

### e) Chỉ số (%) của cải trang: gắn khi tạo item (code NPC/shop hoặc panel), không nằm trong item_template.

## 3. CODE / VERSION (bắt buộc trước khi restart)

1. **`DataGame.vsItem` 12 → 13** — BẮT BUỘC, theo comment ngay trong code
   (`DataGame.java:40-41`): không tăng thì client dùng cache cũ, không nhận template mới → item lỗi vẽ.
2. **`DataGame.vsData` 10 → 11** — để client tải lại file `part` (phần tử mới của vData);
   server regenerate file mỗi boot nhưng client chỉ tải lại khi version đổi.
3. **`data/smallimage_version/x{2,3,4}/smallimage_version_data`** — file version icon
   (32.293 byte ≈ 1 byte/id tới id 32290): cần **nối thêm 1 byte cho mỗi id ảnh mới**
   (giá trị khác 0) để client biết fetch ảnh mới. 3 file này khác nhau theo zoom (md5 khác).
4. **KHÔNG cần sửa** `data/update_data/image` (file tĩnh từ 2023, 2.997 mục — không đánh theo id icon)
   và `data/res/*` (chứa UI, không chứa ảnh part).
5. Compile + deploy + restart (giây com việc hay làm).

## 4. QUY TRÌNH

1. Designer gửi **33 ảnh** (cỡ nào cũng được, rõ nhất là cỡ x4 hoặc x2).
2. Mình: sinh 3 zoom → đặt vào `icon_botnet/x2,x3,x4` → chèn DB (`part`, `item_template`, `head_avatar`)
   → nối `smallimage_version_data` → bump `vsItem`/`vsData` → compile + deploy.
3. Đặt vào điểm bán (shop SQL / NPC / Phúc Lợi / EventBanGoi) theo yêu cầu.
4. Test in-game: mua → mặc → kiểm tra avatar chat + icon hành trang (cần user test help vì
   browser panel không điều khiển được game client).
