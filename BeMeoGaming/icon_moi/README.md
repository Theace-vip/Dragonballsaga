# Icon 2 vật phẩm Bản Nguyên Tinh Cầu — ĐÃ XONG (10/10/2026)

## Trạng thái
| | Vật phẩm nâng cấp | Vật phẩm đột phá |
|---|---|---|
| Tên | **Mảnh Vỡ Tinh Thạch** | **Hạt Giống Khởi Nguyên** |
| item_template id | **1942** | **1943** |
| icon_id | **32745** | **32746** |
| Ảnh gốc | `nang_cap.png` (nền trắng đục) | `dot_pha.png` (nền trong suốt) |
| Bản đã tách nền | `nang_cap_cut.png` (CutBackground) | — |

- Icon đã đặt ở `data/icon_botnet/x2|x3|x4/32745.png` + `32746.png` (32/48/64).
- `DataGame.vsItem` đã tăng **24 → 25** (client xoá cache template + tải lại).
- Lúc boot, server tự nối `data/smallimage_version/x1..x4` từ 32745 → 32747 (đã xác nhận trong log).
- Ảnh gốc lưu ở `icon_ban_nguyen_backup/`.

## Giá đã gắn (trong `src/services/BanNguyenTinhCauService.java`)
- Nâng Cấp độ Lõi: cấp 1 = **100** mảnh, mỗi cấp +100 → 100/200/.../1000.
- Đột phá Tiến Hóa: lần 1 = **10** hạt, mỗi lần gấp đôi → 10/20/40/80/160.
- Chỉ số mỗi cấp: **+250% HP, +250% KI, +200% Sức đánh**
  (cộng dồn theo tổng cấp lõi = tier×10 + level, tối đa 50 cấp).

## Ảnh mới sau này
Thả vào folder này rồi nhắn "đã bỏ ảnh" — quy trình: resize 3 zoom bằng
`tools/GenItemIcon.java`, tách nền bằng `tools/CutBackground.java` (nếu ảnh có nền đục),
rồi tăng `vsItem` + restart.
