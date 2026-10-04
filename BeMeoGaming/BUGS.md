# BUGS đã phát hiện (điều tra 26/09/2026)

Liên quan: hệ thống Sổ Xứ Mệnh, nạp tiền web, cửa hàng Hiệu.
Chưa sửa gì — chỉ ghi nhận. Số liệu đối chiếu từ DB `hondaodragon`.

---

## BUG 1 — NẶNG: Web nạp tiền ghi vào cột chết (cash/danap thay vì vnd/tongnap)

- **Vị trí**: `/c/xampp/htdocs/nrokura/callback.php:48` và `callback_bank.php:60`
  ```sql
  UPDATE account SET cash = ?, danap = ? WHERE username = ?
  ```
- **Sự thật**: game đọc `vnd` (+`temp_vnd`) và `tongnap` (`NDVSqlFetcher.java:90-92`).
  grep toàn bộ Java: **0 chỗ nào đọc cột `cash` / `danap`** (2 cột này chỉ tồn tại như legacy).
- **Hệ quả**:
  - Người nạp qua web → tiền vào cột không ai đọc → cash trong game KHÔNG tăng.
  - `tongnap` KHÔNG tăng → không ai (kể cả nạp thật) qua được cửa Premium Sổ Xứ Mệnh
    (`vnd >= 500k && tongnap >= 500k`, `NpcFactory.java:5009`).
  - Web `rank/top-money.php` xếp hạng theo `tongnap` → chỉ có `admin`.
  - Bằng chứng DB: mọi account `cash=0`, `danap=0`; `tongnap>0` chỉ `admin` (2.000.000.000 — số buff tay từ panel, code không nơi nào tự cộng `tongnap` ngoài `PanelService.buffVnd`).
- **Đề xuất sửa**: callback ghi `UPDATE account SET vnd = vnd + ?, tongnap = tongnap + ?`
  (hoặc cả `danap` nếu web muốn giữ cột đó cho mục đích hiển thị).

## BUG 2 — Cửa Hàng Hiệu bypass cửa tongnap (trao điểm VIP Sổ Xứ Mệnh chỉ bằng cash)

- **Vị trí**: `NpcFactory.java` case `ConstNpc.goidapdo/goichuyensinh/goichanmenh/chualanh/goidetu/goitambao`
  (NPC "Cửa Hàng Hiệu" — `EventBanGoi.java`, map 5), ví dụ case 0 dòng ~3374:
  ```java
  if (player.getSession().vnd >= 1000000) {        // CHỈ check cash
      ... +100.000 Điểm VIP Sổ Xứ Mệnh ...         // NpcFactory.java:3453
      PlayerDAO.subcash(player, 1000000);
  }
  ```
- **Hệ quả**: người chơi có cash (kể cả cash bán Lượng Vàng) mua gói 1M–10M → có điểm VIP
  → nhận mốc VIP Sổ Xứ Mệnh (Đá Khảm, 1.000.000 Lượng Bạc/mốc — `UseItem.ComfirmMocSoXuMenhVIP`)
  mà KHÔNG cần "500k Tín Dụng Nạp". Bypass trực tiếp cửa Premium.
- **Hiện trạng**: chưa ai dùng (DB: `point_PassVIP>0` chỉ `admin`) — nhưng cửa đang mở.
- **Đề xuất sửa**: thêm `&& session.tongnap >= <ngưỡng>` vào các gói này, hoặc bỏ hẳn
  việc cộng `point_PassVIP` ở đây nếu chỉ Premium mới được cộng.

## BUG 3 — Guard "Chưa Mở Gói Premium" bị comment hết trong NPC Sự Kiện

- **Vị trí**: `EventSoXuMenh.java` dòng 57–59, 71–73, 79–81 (comment `// if (player.point_MokhoaVip < 1 ...)`)
- **Hệ quả**: menu "Mở khóa VIP / Nhận mốc Free / Nhận mốc VIP" mở cho tất cả mọi người,
  không phân biệt đã mua Premium. Hiện an toàn nhờ thiếu điểm, không nhờ guard.
- **Đề xuất sửa**: bật lại guard cho các nút VIP (giữ mở cho nút mốc Free nếu thiết kế là free).

## BUG 4 — Điểm Free ("Sổ") sinh từ việc TIÊU cash — không liên quan nạp

- **Vị trí**: `PlayerDAO.subcash` (`PlayerDAO.java:1506`):
  `point_PassFree += (num / 1000) * 100` — mỗi 1.000 cash tiêu = +100 điểm Free.
  Cộng thêm boss drop (`BLACKGOKU.java:41`, `BLACKGOKUROSE`, `Android15`).
- **Thực tế DB**: 11 player đã nhận mốc Free tới 50k–500k điểm mà KHÔNG mua gì:
  bomthue, atula (500k), bataka (300k), kandz, oxasx, brojp (200k), gianggiang, tachi (100k),
  draaa, concuu, namee (50k). Đây nhiều khả năng là "vài user mua đc sổ" mà admin thấy —
  thực chất họ **nhận mốc Free**, không mua.
- **Ghi chú design**: nếu mốc Free vốn free thì không phải bug; nhưng cân nhắc có nên
  cho "tiêu cash = tích điểm" hay không (ai tiêu nhiều cash sẽ có full mốc Free).

## BUG 5 — Top Tiên Bang / Top Nhập Ma luôn rỗng: cột không tồn tại trong DB

- **Vị trí**: `Manager.java:139-140` (`queryToptienbang` dùng `player.CapTamkjll`, `queryTopNhapMa` dùng `player.lbTamkjll`).
- **Hiện trạng DB**: `SHOW COLUMNS FROM player` **không có** 2 cột này → query ném SQLException,
  `realTop` nuốt exception (catch rỗng) → `TopTienBang`/`TopNhapMa` = 0 dòng (log: `Successfully loaded TopTienBang (0)`).
- **Hệ quả**: NPC "Đua Top" (map Đảo Kamê) 2 mục này luôn trống, và trang web `https://nrokura.site/top-tienbang`,
  `/top-nhapma` hiển thị "Bảng xếp hạng đang được cập nhật".
- **Đề xuất sửa**: thêm `CapTamkjll INT DEFAULT 0` + `lbTamkjll INT DEFAULT 0` vào `player`
  (hoặc đổi query sang cột nghi nghiện đã viết thực tế nếu 2 cột này bị đổi tên), sau đó `reloadtop`.

## BUG 6 — Máy Dò Boss (item 1881) không hiện boss nào (ĐÃ SỬA)

- **Triệu chứng**: dùng item `1881 Rada Dò Boss` → item bị trừ, không có bảng nào hiện ra.
- **Nguyên nhân chính**: `BossManager.showListBoss` ghi số dòng bằng `writeInt(size)` trong khi client đọc là **BYTE**.
  `writeInt(53)` = `00 00 00 35` → byte đầu = 0 → client hiện 0 dòng. (Chính `Service.showListTop` từng ghi lại có comment: "client doc so dong dang BYTE... ghi int -> client hien 0 dong").
- **Nỗi lỗi tương đồng**: `TopService` (7 chỗ `writeInt(Math.min(100,...))` + 2 chỗ `writeInt(tops.size())` tiêu đề "Top 100"),
  `SuperRankService` (Top 100 Cao Thủ), `Command.showListPlayer` (danh sách người online).
- **ĐÃ SỬA (26/09/2026)**:
  1. `BossManager.showListBoss` → `writeByte(min(255, n))`, log bằng `Logger.logException` (trước là `printStackTrace` → không có gì trong log), trả `boolean`.
  2. `UseItem case 1881`: chỉ trừ item khi gọi `showListBoss` thành công; nếu lỗi → báo và giữ item.
  3. **Chỉ liệt kê boss đang sống**: filter `isAliveOnMap` (zone != null, không chết, status JOIN_MAP/CHAT_S/ACTIVE/AFK) → boss chết bay khỏi danh sách ngay, spawn lại là tự động quay lại (không cần add/remove tay).
  4. Đồng thời sửa `writeInt` → `writeByte` cho TopService/SuperRank/Command.
- **Cách kiểm chứng**: vào game dùng item 1881, hoặc dùng lệnh admin `/boss` (cùng 1 hàm).

## BUG 7 — Máy Dò Boss: bấm "Dịch chuyển" không nhảy đúng boss đã chọn (ĐÃ SỬA 28/09/2026)

- **Triệu chứng**: dùng 1881 → list hiện ra → bấm 1 dòng nhưng tele về boss KHÁC (sai khu/đã chết),
  hoặc bấm mà không dịch chuyển gì cả (im lặng).
- **Nguyên nhân**:
  1. List ghi 2 int khác nhau `(vị trí dòng, boss.id)` nhưng client chỉ gửi **1 int** về
     (`Controller case -118`: `_msg.readInt()`). Server rơi vào `BossManager.getBoss(_id)` —
     hàm này match `boss.id == id || indexOf(boss) == id`, **không lọc sống/chết**, trả về bản
     **đẦu tiên** trùng id → nhiều boss cùng id (`CauThanThu ×5`, `KHIDOT ×10`, `ONG_GIA_NOEL ×30`...)
     đều nhảy về bản đầu; bản cũ chết/`zone==null` (WorldBoss đẻ bản mới không remove bản cũ)
     → `boss.zone == null` → im lặng không tele.
  2. Nếu int gửi là vị trí dòng thì `indexOf` so với **mảng gốc chưa lọc** (chứa boss chết) → lệch dòng.
  3. Server không lưu list đã gửi theo player → không map được dòng bấm về boss.
  4. `menuType=3` set khi mở máy dò **không reset** → bấm dòng list Top/online (cùng msg -96)
     vẫn rơi vào nhánh dịch chuyển boss → tele lung tung.
  5. Các nhánh chặn (`checkMapCanJoin` theo nhiệm vụ, map 122/123/124) trả null **không thông báo** → im lặng.
- **ĐÃ SỬA**:
  1. `BossManager.showListBoss`: ghi **2 int đều = vị trí dòng** (hết mơ hồ client gửi int nào),
     **lưu list boss đã lọc theo player** (`IDMark.bossTeleList` + `bossTeleListTime`, hạn 5 phút).
  2. `BossManager.getBossFromTeleList(player, index)`: lấy đúng dòng đã gửi, validate còn sống
     + còn hạn (không dùng `getBoss` nữa cho máy dò).
  3. `Controller case -118` default: chỉ tele khi `menuType==3` + list còn hạn; hết hạn/boss chết →
     báo "dùng lại máy dò"; chặn Phó Bản từ ngoài và check trước `checkMapCanJoin(Yardart)` → có thông báo.
  4. List Top/Online (`TopService` ×9 chỗ, `Service.showListTop`, `Command.showListPlayer`)
     set `menuType=9` khi gửi → click list đó không còn dính nhánh máy dò.
- **Cách kiểm chứng**: relog → dùng 1881 → bấm từng dòng phải nhảy đúng boss đúng khu;
  boss chết giữa chừng → báo dùng lại máy dò; mở Top rồi bấm dòng Top → không bị tele nhầm.

## BUG 8 — Crash client sau khi setup chỉ số + chat "acs" auto cộng điểm (ĐÃ SỬA 28/09/2026)

- **Triệu chứng**: người chơi setup sức đánh/HP/KI rồi mở chat `acs` để auto cộng → **một số máy crash game**
  (app tắt đột ngột; log server thấy 8 lần reconnect lặp trong 1 phút, không có exception phía server).
- **Lưu ý**: `acs`/`setup chỉ số` là tính năng **client** (server không có lệnh này) → nguyên nhân nằm ở
  dữ liệu server phản hồi về.
- **Nguyên nhân (2 ứng viên, cả 2 đều sửa)**:
  1. **Sai format int/double theo version**: server ghi ~15 loại tin nhắn (-42 chỉ số, máu mob, dame, skill...)
     theo `session.version == 237` (double) vs khác (int). Log có **6 phiên login version=0** (client relogin
     sau restart/không gửi lại cmd2, hoặc parse chuỗi platform lỗi bị `catch {}` nuốt im lặng) → server gửi
     **layout int cho client 237** → client đọc thiếu/dư byte → crash. `writeShort(-1)` aura (version≥214)
     cũng lệch theo version=0.
  2. **Flood phản hồi**: mỗi lần cộng điểm server gửi -42 + broadcast sub14 **cho cả map**; `acs` spam hàng
     trăm req/s → hàng nghìn tin nhắn/giây (nhân số người trong map) → máy yếu OOM/kill app. Không rate-limit,
     toast lỗi ("không đủ tiềm năng"/"đã đạt mức tối đa") cũng spam từng request.
- **ĐÃ SỬA**:
  1. `MySession.version` mặc định **237** (thay vì 0) → reconnect không gửi cmd2 vẫn dùng đúng format.
  2. `Service.setClientType`: log `[CLIENT] platform=... version=...`; parse lỗi → log + giữ 237 (không nuốt im lặng).
  3. `Service.point()` **gộp phản hồi trong cửa sổ 120ms** (flush cuối bằng ScheduledExecutor) → tối đa ~8 lần/s,
     cộng tay vẫn thấy ngay lập tức (click rải >120ms là send tức thì), lan cuối cùng luôn gửi đầy đủ.
  4. `Controller case 16`: rate-limit **100 req/s/người** (vẫn rất nhanh: 3.276.700 điểm/s), log `[ACS]`
     khi rate≥10/s, khi bị chặn (101/s) và khi request lỗi định dạng (type/point sai).
  5. `NPoint`: dedupe mọi toast lỗi cộng điểm (≤1 lần/1.5s) thay vì mỗi request 1 toast.
- **Cách kiểm chứng**: relog (không mở lại app) sau khi restart server → vào game không crash; dùng acs cộng
  hàng loạt → mượt, máy yếu không crash; log `[ACS]`/`[CLIENT]` xuất hiện khi test.

## Ghi chú thêm

- `admin.tongnap = 2.000.000.000` — nếu admin nói "không cộng tổng nạp cho ai" thì cần
  đối chiếu lại số này (chỉ `PanelService.buffVnd` mới cộng tongnap trong code).
- Mốc Sổ (Free & VIP) **không trừ điểm** sau khi nhận (không có `point_PassFree -=`) —
  chỉ bị chặn bởi `SoXuMenhDaNhan(vip)` đã nhận rồi → không farm lại được, tạm ổn.
- `napthe` chỉ có 2 record (2025) — dòng nạp thẻ tự động hiện không hoạt động/đã cũ.
