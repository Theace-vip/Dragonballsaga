# QUY TRÌNH LÀM BỘ ĐỒ MỚI (set item mới)

Tham khảo từ 2 bộ đã làm: **Hắc Ám Loạn Lưu** (1919-1923) và **Hỗn Độn Vô Cực** (1924-1928).
Đọc file này trước khi làm set mới — làm theo đúng các bước thì không sót gì.

---

## Tổng quan pipeline

```
1. Chuẩn bị ảnh gốc  →  2. SQL item_template  →  3. Generate icon (3 bản zoom)
→  4. Preview so sánh  →  5. Copy icon vào icon_botnet  →  6. Restart server
→  7. Verify  →  8. Option + set bonus  →  9. Cập nhật README
```

Chỉ bước 3-7 lặp lại được nếu muốn sửa ảnh sau.

---

## Bước 1 — Ảnh gốc

- Đặt ảnh vào `data/anh_the/`, tên **theo tên vật phẩm** (`ao.png`, `quan.png`...) hoặc thứ tự.
- Nên là ảnh to (≥ 1000px) — to thì downscale nét; ảnh nhỏ sẵn cũng được nhưng không upscale lại.
- Nền: PNG trong suốt (alpha) **hoặc** nền trắng đặc (tool tự key nền trắng).
- Cập nhật mapping vào `data/anh_the/README.txt` (ảnh → icon_id → item_template.id).

## Bước 2 — SQL item_template

Template đã dùng (5 món trang bị TYPE 0-4):

```sql
INSERT INTO item_template
  (id, TYPE, gender, NAME, description, level, icon_id, part,
   is_up_to_up, power_require, gold, gem, head, body, leg,
   is_up_to_up_over_99, can_trade, comment, ruby)
VALUES
  (1929, 0, 3, 'Áo Tên Bộ Mới', '', 0, 32338, 16, 0, 0, 0, 0, -1, -1, -1, 1, 1, NULL, 0),
  ... TYPE 1=quần part 17, 2=găng part 0, 3=giày part 0, 4=nhẫn part 0;
```

Quy ước / cột quan trọng:

| Cột | Ý nghĩa |
|---|---|
| `id` | Tiếp theo MAX(id) (hiện **1929**) |
| `TYPE` | 0 Áo, 1 Quần, 2 Găng, 3 Giày, 4 Nhẫn (5 slot đầu `itemsBody`, `InventoryService.putItemBody`); 29 = thẻ tiêu thụ |
| `gender` | 3 = cả nam nữ |
| `icon_id` | Tiếp theo MAX(icon_id) (hiện **32338**) — file icon ở `icon_botnet/` |
| `part` | **Ảnh body/leg khi mặc lên người** (`Player.getBody()` trả `itemsBody.get(0).template.part`, `getLeg()` trả slot 1). Bộ hiện tại đang mượn 16/17 kiểu Hắc Ám → **đồ chưa hiện lên người, xem BUG (F)**. Muốn hiện đúng cần id ảnh thật trong data client, xem `HUONG_DAN_CAI_TRANG.md` |
| `head/body/leg` | Chỉ slot 5 (mặt nạ/trang phục) override được head/body/leg; trang bị thường để -1 |
| `can_trade` | 1 = giao dịch được |

Cách chạy SQL (tiếng Việt trong CLI MySQL Windows hỏng → dùng file):

```bash
cd /c/xampp/mysql/bin
./mysql.exe --default-character-set=utf8mb4 -h127.0.0.1 -uroot hondaodragon < /tmp/ten_file.sql
# verify:
./mysql.exe -h127.0.0.1 -uroot hondaodragon -e "SELECT id,TYPE,NAME,icon_id,part FROM item_template WHERE id BETWEEN x AND y;"
```

## Bước 3 — Generate icon (phần "cho đẹp")

Tool: **`tools/GenIcons3.java`** (không đụng khi `/tmp` bị dọn). Pipeline đã tối ưu:

```
ảnh gốc
 → keyWhite: flood-fill nền trắng từ mép (chỉ với ảnh KHÔNG có alpha; ảnh có alpha giữ nguyên)
 → bbox crop (alpha > 16, nới 2% lề)         → bỏ không gian chết, icon đầy khung
 → scaleArea: box-filter PREMULTIPLIED          → KHÔNG dùng bilinear 1 lần (gây vỡ mờ + halo)
 → grade:   gamma 0.80 + contrast 1.30 + sat 1.25  → RA QUAN TRỌNG với art tối (xem dưới)
 → sharpen: 0.70 (chỉ pixel alpha ≥ 200)
 → xuất 3 bản: x4 = cạnh dài 96px, x3 = 72px, x2 = 48px (giữ tỷ lệ)
```

**Tại sao phải có bước `grade`** — bài học từ bộ Hỗn Độn:
bộ Hắc Ám art gốc đã sáng, tương phản cao → downscale là nét.
Art Hỗn Độn rất **tối + nhiều chi tiết nhỏ** → downscale xong các chi tiết đen-đen
merge thành "mù", không đọc được. `gamma 0.80` lift vùng tối + `contrast` + `saturation`
khiến icon đọc rõ như bộ Hắc Ám.

Chạy:

```bash
cd BeMeoGaming/tools
javac -encoding UTF-8 GenIcons3.java
java -Dfile.encoding=UTF-8 GenIcons3 \
  ../data/anh_the ../data/icon_botnet/x4 \
  /tmp/icon_out/x2 /tmp/icon_out/x3 /tmp/icon_out/x4 \
  ../data/anh_the/check.html \
  ao=32338 quan=32339 gang=32340 giay=32341 nhan=32342
# in ra kích thước từng icon — PHẢI khớp cạnh dài 96/72/48
```

Nếu art **sáng sẵn** (như Hắc Ám) mà grade làm cháy sáng → giảm contrast về ~1.1 hoặc bỏ gamma.

## Bước 4 — Preview trước khi deploy

- Mở `http://127.0.0.1:59969/check.html` (server root = repo; nếu chưa chạy server thì mở preview tool).
- So 3 cột: **OLD 4x | NEW 4x | NEW 1:1**, dưới cùng có **reference bộ Hắc Ám** (32328/32329).
- NEW phải: đọc rõ ở 1:1 (kích thước game thật), không halo trắng quanh mép,
  tối mà vẫn thấy chi tiết, màu không nhạt hơn reference.
- **Ảnh PNG trong preview phải embed base64** (GenIcons3 đã làm sẵn) — path tương đối chết trong preview server.
- Chưa ưng → sửa tham số grade/sharpen ở bước 3, chạy lại.

## Bước 5 — Copy icon vào icon_botnet

```bash
cd BeMeoGaming/data/icon_botnet
for s in x2 x3 x4; do cp -f /tmp/icon_out/$s/<các_id>.png $s/; done
# verify 15/15 file khớp nguồn:
for s in x2 x3 x4; do for id in <id1> <id2> ...; do
  [ "$(md5sum $s/$id.png|cut -d' ' -f1)" = "$(md5sum /tmp/icon_out/$s/$id.png|cut -d' ' -f1)" ] || echo "MISMATCH $s/$id"
done; done
```

Kích thước chuẩn trong `icon_botnet` (đối chiếu bộ Hắc Ám): x2 ≤ 48px, x3 ≤ 72px, x4 ≤ 96px, giữ tỷ lệ gốc.

## Bước 6 — Restart server

Chỉ đổi **ảnh** → không cần compile/deploy classes, chỉ restart. Đổi **code/SQL** → làm đủ:

```bash
# compile kiểm tra:
cd BeMeoGaming && find src -name '*.java' > /tmp/tt-sources.txt && rm -rf /tmp/tt-c \
  && javac -encoding UTF-8 -nowarn -d /tmp/tt-c -cp "lib/*" @/tmp/tt-sources.txt
# (đổi code mới deploy) kill java + watchdog:
powershell -NoProfile -Command 'Get-CimInstance Win32_Process | Where-Object { ($_.Name -eq "java.exe" -and $_.CommandLine -like "*server.ServerManager*") -or ($_.Name -eq "cmd.exe" -and $_.CommandLine -like "*run-watchdog*") } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }'
sleep 4 && cp -rf /tmp/tt-c/* build/classes/
# khởi động lại:
cd BeMeoGaming && unset NoDefaultCurrentDirectoryInExePath \
  && (MSYS_NO_PATHCONV=1 cmd /c open-panel.bat > /tmp/op.log 2>&1 &)
```

## Bước 7 — Verify (bắt buộc)

```bash
# 1. boot thành công + không có exception sau boot:
grep -n "Server initialized" server_latest.log | tail -1
ln=$(grep -n "Server initialized" server_latest.log | tail -1 | cut -d: -f1)
tail -n +$ln server_latest.log | grep -c "Exception"        # phải = 0

# 2. port lắng nghe:
netstat -ano | grep "14445.*LISTENING"

# 3. version icon đã kéo dài (2 byte đầu = maxIconId+1, BE):
for f in data/smallimage_version/x2/smallimage_version_data data/smallimage_version/x4/smallimage_version_data; do
  printf "%s: " $f; head -c 2 $f | xxd -p; done
# ví dụ 7e52 = 0x7E52 = 32338 = maxIconId+1  → đúng
# Nếu version file KHÔNG dài ra → client thấy icon "ô trong" (DataGame.extendSmallImageVersion
# chạy lúc boot — Manager.java; không tự chạy khi thêm file giữa chừng).
```

**User phải relog** mới thấy icon mới (client cache theo phiên).

## Bước 8 — Option chỉ số + set bonus 5 món

- **Option từng món**: gắn lúc tạo/trao item trong code
  `item.itemOptions.add(new Item.ItemOption(optionId, param));`
  (id option xem bảng `item_option_template`). Item **không** có option cố định trong `item_template`.
- **Set bonus 5/5**: bảng **`set_config`** (option_id + hp/ki/dame/def/crit...), sửa được từ panel.
  `SetClothes.setupSKT()` (`player/SetClothes.java`) đếm option trên **5 slot đầu**
  (áo/quần/găng/giày/nhẫn, trùng option trong 1 món chỉ đếm 1 lần);
  `SetConfigService` chỉ cộng bonus khi `countSet(optionId) == 5`.
  → Muốn set hiệu lực: cả 5 món phải cùng mang option đó.
- Xem `SetClothes.setupSKT()` trước khi thêm option mới để không trùng logic set cũ.

## Bước 9 — Cập nhật README

- `data/anh_the/README.txt`: thêm dòng mapping ảnh → icon_id → item_template.id.
- Xóa `data/anh_the/check.html` khi không cần preview nữa.

---

## Checklist nhanh (tick dần)

- [ ] Ảnh gốc trong `data/anh_the/`, README mapping cập nhật
- [ ] SQL chạy xong, verify SELECT đúng 5 dòng
- [ ] Icon gen xong, cạnh dài 96/72/48 đúng
- [ ] Preview: NEW 1:1 đọc rõ, không halo, không nhạt hơn reference Hắc Ám
- [ ] 15 file (5 id × 3 cỡ) copy vào `icon_botnet`, md5 khớp nguồn
- [ ] Restart → `Server initialized`, 0 exception sau boot, port 14445 LISTENING
- [ ] `smallimage_version` 2 byte đầu = maxIconId+1
- [ ] Option từng món + `set_config` (nếu cần set 5/5)
- [ ] User relog test in-game: icon hiện, (nếu fix F) đồ hiện lên người
- [ ] Dọn check.html

## Sự cố thường gặp

| Triệu chứng | Nguyên nhân / cách sửa |
|---|---|
| Icon trong ô hành trang "màu đen/trắng" | `smallimage_version` chưa kéo dài → restart server |
| Icon mờ, vỡ khi zoom | Dùng bilinear 1 lần → phải theo pipeline box-filter của GenIcons3 |
| Icon tối, không đọc chi tiết | Thiếu bước grade (gamma/contrast/saturation) |
| Vòng halo trắng quanh mép | Scale có alpha không premultiplied → dùng scaleArea |
| Ảnh preview trắng/lỗi path | Phải embed base64, không dùng path tương đối |
| Đồ mặc không hiện trên người | Cột `part` chưa trỏ id ảnh thật → BUG (F), xem `HUONG_DAN_CAI_TRANG.md` |
| Set bonus không kích hoạt | Thiếu option trên 1 trong 5 món, hoặc chưa có dòng trong `set_config` |
