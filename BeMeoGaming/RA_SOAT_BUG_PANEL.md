# BÁO CÁO RÀ SOÁT PANEL + TÌM BUG EXPLOIT — Server BeMeoGaming

Ngày: 03/10/2026 · Phạm vi: toàn bộ `src` (662 file Java) + 26 màn hình panel.
Ký hiệu: **✅ = đã sửa trong lần rà soát này** · **⚠️ = phát hiện, cần quyết định của admin** · **✔ = đã kiểm tra, an toàn**.

---

## PHẦN 1 — PANEL: NGƯỜI MỚI CÓ DÙNG ĐƯỢC KHÔNG?

**Trả lời thành thật: trước lần này thì CHƯA RÕ.** Panel có 26 màn hình, mỗi màn đã có sẵn dòng tiêu
đề + dòng mô tả (wrapPage), nút bấm tiếng Việt, thao tác nguy hiểm đều có hộp xác nhận, mọi thao tác
đều ghi vào "Nhat Ky Admin". Tuy nhiên thiếu một chỗ **hướng dẫn tổng** — người mở lần đầu không biết
mỗi tab dùng để làm gì, và các thuật ngữ riêng của server (Đạo Lữ, Cung Mệnh, Tiên Bang, He Thong theo nhom...)
mình mò code mới hiểu.

**✅ Đã sửa: thêm tab "HUONG DAN SU DUNG"** ngay dưới "Bang Dieu Khien" — một trang văn bản liệt kê
từng nhóm tab, bước thao tác mẫu, và các cảnh báo quan trọng (không đưa mật khẩu, Xoa TK giữ lại
nhân vật, cột "Mac dinh" trong tab He Thong để khôi phục...).

Còn lại chấp nhận được: nhãn bảng trộn có/không dấu (theo quy ước code tiếng Việt không dấu của dự án),
một số thuật ngữ game vẫn phải hỏi người chơi/server khác.

---

## PHẦN 2 — BUG EXPLOIT ĐÃ TÌM RA VÀ ĐÃ SỬA (✅)

### ✅ BUG 1 (NẶNG) — Trade nhận "vàng âm" từ client → chôm được vàng đối phương
`Trade.addItemTrade`, nhánh `index == -1` (đặt vàng vào ô giao dịch): server nhận `quantity` từ client
**không kiểm tra gì**. Khi hoàn tất giao dịch:

- `player1.gold -= goldTrade1` — nếu goldTrade1 **âm** thì thực chất là **cộng** vàng cho P1
- `player2.gold += goldTrade1` — P2 **bị trừ** vàng

Tức một bên gửi `-1.000.000` là ăn 1 triệu của bên kia (check `FAIL_MAX_GOLD` chỉ chặn tràn phía trên).
**Đã sửa**: clamp `goldTrade = max(0, min(quantity, min(vàng hiện có, INT_MAX))` —同时 chặn cả
đưa nhiều hơn số vàng đang có (trước đây có thể tạo vàng âm cho chính mình).

### ✅ BUG 2 (NẶNG) — 1 người xác nhận 2 lần là trade chạy khi đối phương chưa đồng ý
`TransactionService` case `ACCEPT` gọi `trade.acceptTrade()` — chỉ làm `accept++`, **không phân biệt
ai gửi**. Player A gửi 2 gói ACCEPT = `accept == 2` → `startTrade()` ngay cả khi B chưa bấm đồng ý,
trong khi ô giao dịch của B vẫn "đang sửa" (server không enforce trạng thái).
**Đã sửa**: `acceptTrade(Player)` đánh dấu `accepted1/accepted2`, mỗi người chỉ tính 1 lần; đồng thời
sau khi accept, `addItemTrade` từ chính người đó bị chặn ("đã xác nhận - không thể thay đổi nữa")
— client có khóa thì server cũng khóa.

### ✅ BUG 3 — Trade nhận index âm/vượt biên từ client
`itemsBag1.get(index)` với `index` do client gửi (kiểu byte, có thể âm) → exception. Bị bắt ở tầng
ngoài nên không crash, nhưng là điểm không kiểm soát được.
**Đã sửa**: check `0 <= index < size` trước khi lấy item (cả 2 phía).

### ✅ BUG 4 — Mua lại đồ "đã bán" bị trừ tiền khi túi đầy → cháy tiền lặp lại
`ShopService.buyItemDaBan`: **trừ vàng/ngọc TRƯỚC**, mới check `getCountEmptyBag`. Túi đầy → mất tiền,
nhận "Hành trang đã đầy", **đồ vẫn nằm trong danh sách** → bấm mua lại là tiếp tục mất tiền.
**Đã sửa**: check itemNotNull + túi đầy (và `index < 0`) **trước** khi trừ tiền; các nhánh fail đều
mở lại shop đúng trạng thái.

### ✅ BUG 5 — Mua ký gửi mất tiền mất đồ khi túi đầy
`ConsignShopService.buyItem`: trừ tiền rồi `addItemBag` mà **không đọc kết quả**; nếu túi đầy thì
`addItemBag` fail, `it.isBuy = true` → **tiền mất, item gắn cờ đã bán vĩnh viễn** (người bán vẫn nhận
được tiền — unfair hoàn toàn với người mua).
**Đã sửa**: check `getCountEmptyBag == 0` ngay đầu, trước khi trừ bất kỳ đồng nào.

---

## PHẦN 3 — BUG ĐÃ TÌM, CẦN QUYẾT ĐỊNH (⚠️) — tất cả đều đã đưa lên panel để chỉnh

### ⚠️ 6. Sinh Em Bé không mất trứng (menu bảo "Cần có trứng pet" nhưng trừ 0)
`Input.TAOPET`: check có item 457 nhưng `subQuantityItem(..., 0)` — chỉ cần *giữ* trứng trong túi là sinh.
Đã có config **`embe_trung_tru`** (tab He Thong → Dao Lu/Em Be → Tao pet), mặc định **0 = giữ hành vi cũ**.
→ **Khuyến nghị: đặt 1** nếu muốn đúng như mô tả.

### ⚠️ 7–8. Nhẫn kết hôn: điều kiện và cái mất lệch nhau
- Hồng ngọc: **check `kh_ruby_check` = 9.999.999** nhưng **trừ `kh_ruby_tru` = 9.999** (menu ghi 9.999).
- Zenni: **check `kh_zenni` = 99.999** nhưng **trừ `kh_zenni_tru` = 9.999**.

Nghĩa là cửa ải "gần 10 triệu hồng ngọc" nhưng chỉ tốn 9.999. Cả 4 con số đã tách riêng trên panel
(tab He Thong → Ket Hon) — admin chọn: hạ check cho khớp, hoặc nâng tiền trừ cho khớp.

### ⚠️ 9. Giftcode race condition (nhập 2 chỗ cùng lúc có thể ăn 2 suất)
`GiftCodeManager.checkUseGiftCode`: đọc `countLeft`, rồi `countLeft -= 1` rồi ghi DB — **không atomic**.
Hai người nhập code 1-lượt trong cùng khoảng miligiây có thể cùng qua check.
→ Sửa khuyến nghị: `UPDATE giftcode SET count_left = count_left - 1 WHERE id = ? AND count_left > 0`
rồi chỉ nhận quà khi affected rows = 1. *(Chưa sửa để không đụng luồng giftcode hiện hành.)*

### ⚠️ 10. Ký gửi "nhận tiền": cộng tiền TRƯỚC khi xóa danh mục
`claimOrDel case 2`: `gold += ...` chạy trước `listItem.remove(it)`. Trong thực tế tin nhắn mỗi phiên
xử lý tuần tự nên khó khai thác, nhưng nên đảo thứ tự (remove thành công mới cộng tiền).

### ⚠️ 11. `Trade.isItemCannotTran` case 27: điều kiện bất thành
`if (id != 457 && id == 590)` — chỉ chặn đúng item 590. Có thể intended là "chặn mọi type 27 trừ 457".
→ Cần admin xác nhận hành vi mong muốn rồi sửa 1 dòng.

### ⚠️ 12. Mục "Nhận Quần Đi Biển" check `Saga_VIP < 0` — luôn luôn qua
Ai cũng lấy được nhẫn cầu hôn 691 free (chỉ trừ 1 Zenni). Có thể là chủ ý, ghi nhận.

### ⚠️ 13. Chuyển sinh đệ tử: 2 bất thường
- `power < Long.MAX_VALUE` → gần như **không ai bao giờ qua** được (thông báo bảo cần "9 Tỷ Tỷ").
- `Util.isTrue(tỷ_lệ, 50)` — mẫu số 50 thay vì 100 → tỉ lệ thật **gấp đôi** config `cspet_tile_thanh_cong`.

### ⚠️ 14. Menu "Thông Tin Em Bé" hiển thị % cấp sai công thức
Dùng `EmBeLv` thay vì `EmBeEXP` (EventKetHon case 4) → % gần như luôn vô nghĩa. Sửa 1 dòng nhưng để
admin xác nhận trước.

---

## PHẦN 4 — KHU VỰC ĐÃ KIỂM TRA VÀ AN TOÀN (✔)

- **✔ Trade (phần copy)**: làm việc trên **bản sao sâu** (`copyItemsBag` → `copyItem` từng item),
  hủy/trắng hàng chỉ hủy bản sao → **không mất đồ, không nhân bản đồ**; thành công có ghi
  `history_transaction` (bảng 0 dòng = chưa có ai trade, không phải feature hỏng).
- **✔ Hộp thư (mail)**: nhận quà gắn `isNhan`, item chỉ rời thư khi `addItemBag` trả true, túi đầy
  thì giữ lại trong thư → không double-claim theo luồng tuần tự; có cap 50 thư.
- **✔ Shop thường**: đã check túi đầy trước, trừ tiền trước khi thêm item đúng thứ tự.
- **✔ Giftcode**: chặn theo từng player (`player.giftCode`), có đếm lượt, có check hành trang trống.
- **✔ Nạp thẻ**: bảng `napthe` do web ngoài ghi, game không tự xử lý status → không exploit in-game;
  panel chỉ đọc được.
- **✔ Ký gửi (phần còn lại)**: chặn mua chính hàng của mình, phí đăng 5 ngọc, hoàn 95% khi nhận tiền.
- **✔ Panel**: mọi thao tác ghi `panel_audit`, thao tác nguy hiểm có xác nhận, Session/SQL đều qua
  PreparedStatement.

---

## PHẦN 5 — THỨ TỰ XỬ LÝ KHUYẾN NGHỊ

1. **Deploy 5 fix ✅ ngay** (bug 1–2 là exploit kinh tế thật sự — sửa trước khi(player biết).
2. Admin quyết định ⚠️ 6–8 (trứng free, lệch hồng ngọc/zenni) — chỉ cần đổi số trong tab **He Thong**.
3. Sửa nhanh ⚠️ 9, 10, 14 (giftcode atomic, thứ tự nhận tiền, % menu Em Bé) — ~20 dòng.
4. Xác nhận ⚠️ 11, 12, 13 (hành vi intended của dev trước) rồi sửa.
