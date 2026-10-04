# BẢNG MAP FRAME NRO ← SPINE `g13_juitianxuannv` (Cải trang VLT 052)

> Chưa bake — bảng này để **duyệt trước** (bước 1 của kế hoạch).
> Nguồn: `CharInfo[33]` trong `TuanBip/Assets/Scripts/Game1/Char.cs` (parse bằng `tools/frame-export/dumpcharinfo.js`)
> + 172 frame đã export trong `data/spine_frames/g13_juitianxuannv/` (10 anim, fps 12).

## 0. Nguyên tắc

- Client vẽ `head → leg → body`, anchor TOP_LEFT, `cdir=1` (quay phải); `cdir=-1` client tự lật bằng `trans`
  → **chỉ cần bake hướng phải**, dùng bộ anim `-1`.
- Bộ `-2` (stand-2/run-2/...) **cùng hướng với -1** (đã so MSE: mse(same) << mse(mirror)) nhưng là nét khác
  → không phải bản lật, **bỏ**, chỉ bake `-1`.
- `CharInfo[cf]` cho 3 chỉ số: `[0]=đầu, [1]=chân, [2]=thân`. Các cf **dùng chung** 1 chỉ số → 1 ảnh:
  - **đầu: 3 ảnh** (idx 0 / 1 / 2)
  - **chân: 14 ảnh** (idx 0..13)
  - **thân: 17 ảnh** (idx 0..16)
  → tổng **34 ảnh part** + 1 icon + 1 avatar = **36 ảnh mới** (id 32601..32636).
- Ảnh vệt trắng (smear) ở cf 16/25/29: spine không có vệt trắng → thay bằng pose thường gần nhất.

## 1. Bảng 33 cf ← frame spine

| cf | Ý nghĩa (từ code) | Frame nguồn | head idx | leg idx | body idx |
|---|---|---|---|---|---|
| 0 | đứng (frame A) | stand-1 f0 | 0 | 1 | 1 |
| 1 | đứng (frame B) | stand-1 f1 | 0 | 1 | 1 |
| 2 | đi bộ 1 | run-1 f0 | 1 | 2 | 2 |
| 3 | đi bộ 2 | run-1 f2 | 1 | 3 | 3 |
| 4 | đi bộ 3 | run-1 f3 | 1 | 4 | 4 |
| 5 | đi bộ 4 | run-1 f4 | 1 | 5 | 5 |
| 6 | đi bộ 5 | run-1 f6 | 1 | 6 | 6 |
| 7 | rơi/nảy (air) | run-1 f1 | 0 | 7 | 7 |
| 8 | nhảy ngang người | run-1 f5 | 0 | 0 | 7 |
| 9 | nhảy lặn xuống | attack-1 f4 | 1 | 10 | 12 |
| 10 | nhảy lặn (tay duỗi) | attack-1 f5 | 1 | 11 | 12 |
| 11 | nhảy ngang (tay ra sau) | run-1 f7 | 0 | 12 | 9 |
| 12 | charge trên không (2 tay giơ) | attack-1 f3 | 0 | 8 | 8 |
| 13 | skill đứng — start | skill-1 f0 | 1 | 9 | 10 |
| 14 | skill đứng — đánh | skill-1 f7 | 1 | 9 | 11 |
| 15 | skill đứng — đánh (thấp) | skill-1 f3 | 1 | 9 | 2 |
| 16 | skill đứng — end / bị giữ | stand-1 f0 | 1 | 9 | 13 |
| 17 | charge đứng (2 tay trước ngực) | stand-1 f3 | 0 | 9 | 7 |
| 18 | skill bay — start (2 tay giơ) | skill-1 f19 | 0 | 9 | 8 |
| 19 | skill bay — đánh | skill-1 f6 | 0 | 9 | 14 |
| 20 | skill bay — đánh 2 | skill-1 f8 | 0 | 9 | 15 |
| 21 | skill bay — end (ngồi) | skill-1 f11 | 0 | 9 | 9 |
| 22 | charge kết thúc (2 tay giơ) | attack-1 f3 | 0 | 1 | 8 |
| 23 | bị trúng đòn / đóng băng | attack-1 f7 | 0 | 7 | 0 |
| 24 | skill rơi — start | skill-1 f5 | 0 | 9 | 0 |
| 25 | dịch chuyển tức thì (vệt) | run-1 f0 | 2 | 13 | 16 |
| 26 | skill rơi — đánh (thấp) | skill-1 f9 | 1 | 8 | 10 |
| 27 | skill rơi — ngang người | skill-1 f8 | 1 | 8 | 11 |
| 28 | skill — cúi thấp | skill-1 f4 | 1 | 8 | 2 |
| 29 | skill — vệt ngang | stand-1 f0 | 1 | 8 | 13 |
| 30 | skill — ngồi tay duỗi | skill-1 f9 | 0 | 8 | 14 |
| 31 | bị giữ khi bay (2 tay gấp) | skill-1 f16 | 0 | 8 | 15 |
| 32 | charge bay (2 tay giơ trên đầu) | attack-1 f3 | 0 | 8 | 9 |

## 2. Ảnh đại diện theo nhóm (thực tế mình bake theo nhóm này)

**Đầu — 3 ảnh**

| head idx | cf dùng | Ảnh nguồn đề xuất |
|---|---|---|
| 0 | 0,1,7,8,11,12,17,18,19,20,21,22,23,24,30,31,32 | stand-1 f0 (đầu đứng thẳng) |
| 1 | 2,3,4,5,6,9,10,13,14,15,16,26,27,28,29 | run-1 f0 (đầu nghiêng — hợp đi bộ/skill) |
| 2 | 25 | run-1 f0 (dùng chung, cf25 là vệt) |

**Chân — 14 ảnh**

| leg idx | cf dùng | Ảnh nguồn đề xuất |
|---|---|---|
| 0 | 8 | run-1 f5 |
| 1 | 0,1,22 | stand-1 f0 |
| 2 | 2 | run-1 f0 |
| 3 | 3 | run-1 f2 |
| 4 | 4 | run-1 f3 |
| 5 | 5 | run-1 f4 |
| 6 | 6 | run-1 f6 |
| 7 | 7,23 | run-1 f1 |
| 8 | 12,26,27,28,29,30,31,32 | skill-1 f22 (chân stance rộng) |
| 9 | 13,14,15,16,17,18,19,20,21,24 | skill-1 f16 (chân đứng chiến đấu) |
| 10 | 9 | attack-1 f4 |
| 11 | 10 | attack-1 f5 |
| 12 | 11 | run-1 f7 |
| 13 | 25 | run-1 f0 |

**Thân — 17 ảnh**

| body idx | cf dùng | Ảnh nguồn đề xuất |
|---|---|---|
| 0 | 23,24 | attack-1 f7 |
| 1 | 0,1 | stand-1 f0 |
| 2 | 2,15,28 | skill-1 f3 |
| 3 | 3 | run-1 f2 |
| 4 | 4 | run-1 f3 |
| 5 | 5 | run-1 f4 |
| 6 | 6 | run-1 f6 |
| 7 | 7,8,17 | run-1 f1 |
| 8 | 12,18,22 | attack-1 f3 (2 tay giơ trên đầu) |
| 9 | 11,21,32 | attack-1 f3 |
| 10 | 13,26 | skill-1 f0 |
| 11 | 14,27 | skill-1 f7 (tay duỗi trước) |
| 12 | 9,10 | attack-1 f4 |
| 13 | 16,29 | stand-1 f0 (thân trung tính cho pose vệt) |
| 14 | 19,30 | skill-1 f6 (tay duỗi trước) |
| 15 | 20,31 | skill-1 f16 (2 tay gấp) |
| 16 | 25 | run-1 f0 |

## 3. ID + DB (chưa chạy)

| Mục | Giá trị |
|---|---|
| id ảnh part | 32601..32634 (đầu 3 + chân 14 + thân 17) |
| id icon item | 32635 (32×32 @x2) |
| id avatar | 32636 (188×102 @x2) |
| part head / body / leg | 2143 / 2144 / 2145 |
| item_template | **1935**, TYPE=5, gender=3, tên **"Cải trang VLT 052"** (CÓ DẤU) |
| head_avatar | 2143 → 32636 |
| bump | `DataGame.vsItem` 12→13, `DataGame.vsData` 10→11 |
| sửa kèm | item **1934** đổi tên "Ngoc Tho Tinh" → **"Ngọc Thố Tinh"** (quy ước tên item có dấu) |
