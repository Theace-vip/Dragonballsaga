# PLAN: Web tự động nạp tiền (từng phần)

## 1. Mục tiêu

- Web nạp 1 lần là **tự cộng đủ các phần**: số dư, tổng nạp, mốc nạp (theo tổng nạp), quà nạp đầu, mở thành viên…
- Admin sửa **từng phần riêng biệt** trên Panel → tab **Nạp Tiền / Buff VND**.
- Chống cộng trùng (giao dịch lặp), đồng bộ player đang online.

## 2. Bảng các "phần tính tiền riêng" (đã rà toàn bộ code)

| # | Phần | Cột DB | Nơi đọc / dùng | Khi nào sync |
|---|------|--------|-----------------|--------------|
| 1 | Số dư Cash | `account.vnd` | `MySession.vnd` → mua VIP, nạp vàng, event… | Panel: ngay. Web: lúc login |
| 2 | Số dư chờ nạp | `account.temp_vnd` | `NDVSqlFetcher` dòng 88: `session.vnd = vnd + temp_vnd` rồi `temp_vnd=0` | Tự +vnd ở **lần login kế tiếp** |
| 3 | Tổng nạp | `account.tongnap` | Quà nạp đầu 50k/200k/500k/1tr/2tr (`NpcFactory` 2896-3143), **Phúc Lợi tab 2 (mốc nạp)**, Top Nạp, Event (EventBanGoi, WMAT…), Achievement | Ngay (Panel) / login (web) |
| 4 | Đã nạp (bảng xếp hạng web) | `account.danap` | `top/top-nap.php` | Web ghi |
| 5 | Mở thành viên 20k | `account.active` → `session.actived` | NPC "Mở TV 20K", giao dịch | Web tự `active=1` khi nạp > 20k (`_AutoMember`) |
| 6 | VIP tài khoản | `account.vip` → `session.vip` | `NPoint.java:2278` (bonus khi `vip>0`) | Ngay |
| 7 | Quà nạp đầu đã nhận | `player.NapDau` | `NpcFactory` case `ConstNpc.NapDau` (0 = chưa nhận, ≥1 = đã nhận 1 trong 5) | Ngay |
| 8 | VIP in-game | `player.Saga_VIP` (byte 0..127) | prefix zone, aura, đục sao, mua VIP, `subcash` +1/5tr | Ngay |
| 9 | Điểm phụ sinh khi **chi** Cash | `SagaDiaDao`, `SagaThienDao`, `point_PassFree` | cộng trong `PlayerDAO.subcash` (khi **tiền** Cash, không phải khi nạp) | Tự khi chi |

**Quan trọng:** "mốc nạp" và "quà nạp đầu" **không có cột riêng** — chúng đều đọc từ `tongnap`.
Nên khi nạp chỉ cần cộng `tongnap` là 2 phần này tự mở khóa. Muốn cho nhận lại quà nạp đầu →
đặt `player.NapDau = 0`.

**Luồng nạp thật hiện tại của web (chỉ 2 cột):**

```
Callback.php (thẻ cào) / ajax/ipn.php / ajax/webhook.php / ajax/callback.php
  → UPDATE account SET temp_vnd = temp_vnd + X, tongnap = tongnap + X
admincp: ajax/admin/user/add-coin.php → addCointAccount() (DHKD/Session.php:209)
  → UPDATE account SET temp_vnd = temp_vnd + X, tongnap = tongnap + X
```

## 3. Đã làm ở Panel (Server Control Panel → "Nạp Tiền / Buff VND")

- **Ô Account ID + "Tải hiện trạng"**: đọc và hiển thị cả 8 phần, kèm báo `[player ONLINE/offline]`.
- **Từng phần – sửa riêng lẻ**: mỗi phần 1 dòng `giá trị hiện tại | ô nhập | +Cong | Dat`
  (vnd, temp_vnd, tongnap, danap, active, vip, napdau, sagavip).
  Ghi DB **và** đồng bộ RAM nếu player đang online + `sendMoney` về client.
- **NẠP ĐẦY ĐỦ**: 1 ô số tiền + checkbox `Tong nap / Da nap / Mo thanh vien / Dat lai qua nap dau`
  + chọn đích `vnd (ngay)` hoặc `temp_vnd (chờ)` → gọi `PanelService.napFull(...)`.
- Mọi thao tác ghi `panel_audit` (tab Audit).

Code: `src/panel/PanelService.java` (napParts / napStatus / napApply / napFull), `src/panel/ControlPanel.java` (buildDonate).

## 4. Plan để WEB tự động nạp

### Cách A – Web ghi thẳng DB (đã có sẵn, ít việc nhất)

Chuẩn hóa thành **1 hàm PHP duy nhất** trong `DHKD/Session.php`:

```php
// them 1 trans_id vao bang topup_log de chong trung
function topup($conn, $accountId, $amount, $opts = []) {
    // $opts: 'vnd_ngay' => false, 'danap' => true, 'active' => true, 'reset_napdau' => false
    // 1) chong trung: INSERT INTO topup_log(trans_id, account_id, amount) ...
    //    neu duplicate -> return false (da xu ly)
    // 2) UPDATE account SET temp_vnd = temp_vnd + ?, tongnap = tongnap + ? [, danap, active] WHERE id = ?
    // 3) neu reset_napdau: UPDATE player SET NapDau = 0 WHERE account_id = ?
}
```

- Việc còn lại game tự lo: login merge `temp_vnd → vnd`, quà nạp đầu / mốc Phúc Lợi đọc `tongnap`.
- **Hạn chế**: tiền chưa thấy khi player đang online (phải login lại), không có phản hồi real-time.

### Cách B – Game mở HTTP API cho web gọi (KHUYẾN NGHỊ)

Game server thêm 1 thread HTTP nhỏ (không cần thư viện, `ServerSocket` thường):

- Cổng: `14446` (không đụng 14445), bind `127.0.0.1` nếu web chung máy, hoặc `0.0.0.0` + firewall.
- Auth: header `X-Topup-Token` so với secret trong `data/config/system_tuning.properties`
  (`topup_token`, `topup_enable=0/1`).
- Endpoint: `POST /api/topup`

```
{ "account_id": 123, "amount": 20000, "trans_id": "NT-99123",
  "parts": { "vnd": 1, "tongnap": 1, "danap": 1, "active": 1, "reset_napdau": 0 } }
→ 200 {"ok":true,"msg":"OK - nap 20.000: ..."}
```

- Server nhận → gọi đúng `PanelService.napFull(...)` (đã sync online + audit) → chống trùng
  bằng `trans_id` trong `topup_log`.
- Web (PHP) gọi bằng `file_get_contents('http://127.0.0.1:14446/api/topup', ...)`;
  **retry 3 lần** khi lỗi mạng, `trans_id` giữ nguyên → không lo cộng trùng.
- Class gợi ý: `src/panel/TopupApi.java` (thread accept → parse request → `PanelService.napFull`
  → `audit("topup_api", ...)`), start trong `ServerManager` khi `topup_enable=1`.

### Các mốc triển khai

1. [ ] Thêm bảng `topup_log(trans_id PK, account_id, amount, parts, source, created_at)` (chống trùng).
2. [ ] Cách A: gộp 4 file callback/ipn/webhook/add-coin về 1 hàm `topup()` dùng chung.
3. [ ] Cách B: viết `TopupApi.java` + secret trong panel (tab Hệ Thống) → web chuyển sang gọi API.
4. [ ] Web ghi log từng giao dịch + hiện `tongnap`/`temp_vnd` sau mỗi lần nạp để dễ đối soát.
5. [ ] Thử nghiệm: nạp 20k khi **offline** và khi **online** → kiểm tra 8 phần ở Panel
    (Tải hiện trạng) + quà nạp đầu nhận được + mốc Phúc Lợi tab 2 sáng.

### Lợi ích của Cách B

- Nạp thấy **ngay** khi player đang online (Panel đã sync RAM + gửi `sendMoney`).
- Một chỗ ghi log/audit duy nhất, chống trùng có idempotency.
- Web không cần biết cấu trúc DB của game nữa (tách quyền).
