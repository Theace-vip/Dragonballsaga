# Full spine cải trang (431 bộ) — lấy từ server Hac Tinh

Toàn bộ spine cải trang (Krew, VLT, SG441, fx, skill) của tool/game, tải trực tiếp từ
server của game rồi giải nén ra file dùng được.

## Nguồn
- Catalog: `https://anti.hactinh.online/spine/v2/android/catalog.json` (bản `windows` cũng có, nặng hơn ~350MB)
- Mỗi mục trong catalog là 1 AssetBundle có `sha256` + `size` để kiểm tra.
- Bundle tải về: `../spine_bundles/` (431 file, ~193MB) + `manifest.tsv`.

## Quy trình (chạy lại được)
```bash
bash tools/fetch-remote-spine.sh                 # tải bundle về spine_bundles/ (verify sha256)
# nạp vào AssetRipper rồi export ra ripped_spine/ (xem ghi chú bên dưới)
bash tools/organize-remote-spine.sh              # gom thành từng bộ trong spine_export_remote/
bash tools/make-spine-index.sh                   # tạo INDEX.csv
```
Ghi chú AssetRipper (bản GUI Free 2.0 có HTTP API, chạy `--headless --port 5731`):
```bash
curl -X POST http://localhost:5731/Reset
curl -X POST http://localhost:5731/LoadFolder --data-urlencode 'path=<...>\spine_bundles'
curl -X POST http://localhost:5731/Export/PrimaryContent --data-urlencode 'path=<...>\ripped_spine'
```

## Cấu trúc mỗi bộ
```
<ten>/
  <ten>.skel      # skeleton binary (Spine 4.1.24) — hoặc <ten>.json (Spine 4.2.11)
  <ten>.atlas     # atlas text, pma:true
  <trang>.png     # 1-3 trang texture, tên phải khớp dòng đầu file .atlas
```

## Tìm bộ mình cần
`INDEX.csv`: `itemId,kind,ten_hien_thi,thu_muc,resourceBase,files`

| Ví dụ | itemId | thư mục |
|---|---|---|
| Whis (Thiên Sứ Whis) | 2776 | `whis/` |
| Vegeta Whis | 2772 | `vegeta_whis/` |
| Goku SSJ5 | 2724 | `goku_ssj5/` |
| Broly Hủy Diệt | 2670 | `broly_huydiet/` |

## Mở xem
- Spine Editor 4.1 (file `.skel`) hoặc 4.2 (file `.json`) → Import Data.
- Viewer online: esotericsoftware.com/spine-viewer, toolbuddy.io/spine-viewer (chọn cả bộ 3 file).
- Import vào Unity: dùng `spine-unity` đúng phiên bản (4.1 hoặc 4.2), tạo `SkeletonDataAsset` từ skeleton + atlas + png, atlas đang bật premultiplied alpha (`pma:true`).
