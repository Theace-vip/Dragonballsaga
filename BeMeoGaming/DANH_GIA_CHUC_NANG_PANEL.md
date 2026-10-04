# ĐÁNH GIÁ TOÀN BỘ CHỨC NĂNG → PANEL ADMIN (bản v2 — rà từng hàm)

> Ngày: 02/10/2026
> Phạm vi quét: **662 file Java / 35 package**, **68 bảng MySQL** (DB `hondaodragon`),
> panel hiện tại **24 màn sidebar** (`ControlPanel.java` 6.572 dòng + `PanelService.java` 3.458 dòng, **181 API**).
> Đối chiếu thêm với: toàn bộ **lệnh admin in-game** (`server/Command.java` → `vipchat`), **menu admin NPC** (`NpcFactory` MENU_ADMIN),
> **form nhập in-game** (`services/func/Input.java`), **lệnh terminal server** (`ServerManager.activeCommandLine`),
> 9 DAO (`jdbc/daos`), file log (`server_latest.log` 17MB, `watchdog.log`), file ghi ngoài (`htdocs/lichsu_mua.txt` 2.5MB, `vip_hoan.txt`).
> Cách làm: mỗi chức năng (kể cả hàm 1 dòng) → đối chiếu "panel đã có chưa?" → nếu chưa: **ĐƯA / CÂN NHẮC / KHÔNG ĐƯA**,
> và nếu đưa thì **chỉnh sửa được gì, quản lý được gì**.

---

## 0. KẾT LUẬN NHANH

| Phân loại | Số mục | Ghi chú |
|---|---|---|
| ✅ Đã có trên panel — đủ dùng | **24 màn** (181 API) | kể cả 3 màn P0: Nhân vật / Nhật ký / Mail |
| 🟢 **NÊN đưa — P1** (làm trước) | **12 mục** | 8 mục cũ + **4 mục mới phát hiện**: Lệnh Admin, Lịch sử nạp & giao dịch, Sửa acc nâng cao, Thông báo cuộn |
| 🟡 **NÊN đưa — P2** (làm sau) | **12 mục** | thêm mới: Web shop, Cài đặt web, Cứu trợ player kẹt, Tiện ích NV (bùa/item time/quà phúc lợi) |
| 🟠 **CÂN NHẮC — P3** (rủi ro/cần phương án) | **~14 mục** | hardcode hoặc sửa sai mất cân bằng kinh tế |
| ❌ **KHÔNG NÊN đưa** | **~16 nhóm** | không có tham số sửa, hoặc sửa sai vỡ server/client |

**Tổng kết**: 24 màn ≈ 55% chức năng quản trị → sau P1 ≈ **85%** → sau P2 ≈ **95%**.
Mọi mục mới đều làm **menu sidebar + form/table**, admin không viết một dòng SQL/JSON nào.

---

## 1. TIÊU CHÍ ĐÁNH GIÁ (dùng cho từng hàm)

1. **Có tham số dữ liệu không?** Dữ liệu nằm ở **DB / file config / bảng properties** → sửa được bằng form.
   Là **hằng `static final` hoặc logic code** → không đưa (phải sửa code + deploy).
2. **Tần suất vận hành**: sửa hàng ngày → ưu tiên cao; sửa 1 lần/năm → ưu tiên thấp.
3. **Rủi ro**: sai làm vỡ kinh tế (tỉ lệ rớt, thưởng), bảo mật (token/mật khẩu), session/server → loại hoặc P3.
4. **Thay thế hiện tại**: đang phải **SQL tay / gõ chat in-game / mở terminal** → càng nên đưa panel.

---

## 2. BẢNG TRA CỨU TOÀN BỘ CHỨC NĂNG (theo nhóm, từng hàm)

### A. Tài khoản & người chơi

| Chức năng (code/bảng) | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Dashboard CPU/RAM/thread/session/IP/delay/uptime | ✅ | — | — | — |
| Bảo trì: ngay (startImmediately), sau N phút, auto 23:59:50 | ✅ | 🟡 thêm | Giờ auto hiện là `static final` 23:59:50 — chuyển sang config để **đổi giờ/tắt** trên panel | 2 |
| Acc: tìm, ban, set admin, reset pass, buff VND/tongnap/vip, xóa acc, top nạp | ✅ | — | — | — |
| Nhân vật: chi số/tiền/túi đồ/skill, thêm/xóa item, set point skill, xóa NV | ✅ (P0) | — | — | — |
| Nhật ký admin + IP đăng nhập | ✅ (P0) | — | — | — |
| Gửi mail quà online/offline | ✅ (P0) | — | — | — |
| **Sửa acc nâng cao**: `account.role`, `thoi_vang`, `luotquay`, `vang`, `event_point`, `tichdiem`, `danap`, `cash`, `temp_vnd`, `daptrung_count`, `gioithieu`, `point_post` | ❌ | 🟢 P1 | Form sửa các cột này (cộng/trừ/set) — hiện muốn đổi phải SQL tay | 11 |
| **Xóa mã bảo vệ** `account.mabaove` + `player.baovetaikhoan`/`captcha` | ❌ | 🟡 P2 | Nút "đặt lại mã bảo vệ/captcha" cho acc kẹt (logic ở `Service.mabaove:2596`) | — |
| **Đầu & giới tính NV**: `player.head`, `gender` | ❌ | 🟢 P1 | Sửa đầu nhân vật, đổi giới tính (tương đương lệnh chat `ht <0-2>` đang có in-game) | 11 |
| **Rương phụ** `player.items_box` (ngoài túi đồ/bodies) | ❌ | 🟢 P1 | Tab "Rương đồ" trong form NV: xem/thêm/xóa item ở rương phụ (viewPlayer hiện chỉ đọc body+bag) | 11 |
| **Điểm danh** `player.diemdanh`, **nhận lại quà** `phuc_loi[5]`, **giftcode đã nhận** `player.giftcode` | ❌ | 🟢 P1 / 🟡 P2 | Reset điểm danh; reset mốc phúc lợi để nhận lại quà online; xóa player khỏi danh sách đã nhập giftcode để test | 1 |
| **Lệnh admin in-game** (`Command.vipchat`): `hp/ki/sd/giap/crit` (set chỉ số gốc), `tsm/gsm/ttn/gtn` (sức mạnh/tiềm năng), `up`, `nv_<id>` (set nhiệm vụ), `m<mapId>` (đổi map), `i`/`mail` (buff item), `ht`, `tele` (gọi cả map), `pk` (tiêu diệt server), `r` (hồi chiêu), `skillxd/skilltd/skillnm` (học tuyệt kỹ), `double`, `kick`, `shop`, `top` reload | ❌ (phải **gõ chat trong game**) | 🟢 **P1 — mục mới** | Màn **"Lệnh Admin"**: form chọn player + lệnh (dropdown/biểu mẫu) → thay gõ chat, chạy server-side, có ghi nhật ký. Giảm phụ thuộc vào client đang mở | 9 |
| Menu NPC admin (`NpcFactory.MENU_ADMIN`): buff ngọc 14–20, **tạo đệ** (`PetService.createNormalPet`), bảo trì 30', tìm NV, list boss, call Broly | ❌ | 🟢 P1 | Đa số panel đã có (bảo trì/boss/tìm NV); **"tạo đệ cho player"** → gộp mục Đệ tử | 4 |
| Form in-game `Input` (giftcode, MBV, chat all, giải tán bang, đổi tên, nạp thẻ, bán sll...) | ❌ | 🟡 | Phần lớn panel đã thay; **đổi tên NV** (`createFormChangeName`) → P3 | — |
| `account.token/xsrf_token/newpass/password` | ❌ | ❌ | Bảo mật — không đưa | — |
| Xóa vĩnh viễn NV (`deletePlayerRow`) | ✅ | — | — | — |
| Đổi tên nhân vật | ❌ | 🟠 P3 | Làm được nhưng kéo theo task/bang/top trỏ sai — cần check FK dạng text | — |
| Bạn bè / kẻ thù (`friends`, `enemies`) | ❌ | ❌ | Riêng tư người chơi | — |

### B. Vật phẩm, Shop, NPC, Rương

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Shop (tab, giá, tiền, CSV, undo, clone, lịch sử sửa) + reload | ✅ | — | — | — |
| Thư viện item: tìm, **tạo item mới**, whereUsed, tang item + option builder | ✅ | — | — | — |
| Rương quà (`panel_chest`) CRUD | ✅ | — | — | — |
| Từ điển option + gợi ý param | ✅ | — | — | — |
| NPC: danh sách, tạo NPC nháp, gán/tạo shop | ✅ | — | — | — |
| Đổ rác / ký gửi (`Consign`, `shop_ky_gui`) | ✅ | — | — | — |
| **Sửa `item_template` có sẵn** (giá/gem/type/gender/icon) + reload | ❌ (chỉ tạo item mới) | 🟡 P2 | Sửa trực tiếp template + **reload item_template**, kèm lịch sử undo như shop | — |
| **Shop web**: `web_shop` (57 sp), `web_shop_history` (407 đơn), `kho_web` (11 sp), `posts` | ❌ | 🟡 **P2 — mới** | CRUD sp web (giá/tên/mô tả/ảnh), xem đơn đã bán, cộng/trừ **kho web** của player, CRUD bài viết | — |
| **Quà web** `gifts`, **`items_box_lucky_round`** (rương quay) | ❌ | 🟡 P2 | Xem/sửa quà sự kiện web; xem rương quay | — |
| `items_daban` (100 đồ đã bán gần nhất của NV) | ❌ | 🟠 P3 | Tab read-only trong form NV | — |
| Danh mục `type_item` / `type_sell_item_shop` (nhóm loại, kiểu bán) | ❌ | 🟠 P3 | Sửa nhãn nhóm item đang dùng trong dropdown shop | — |
| `LuckyRound` (vòng quay may mắn 1458/1459) | ❌ | 🟠 P3 | Tỉ lệ hardcode trong code → chỉ đọc | — |

### C. Tiền, nạp thẻ & giao dịch  (**nhóm mới phát hiện**)

| Chức năng (code/bảng) | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Giftcode CRUD + reload | ✅ | — | — | — |
| Nạp / buff VND, DONATE, top nạp | ✅ | — | — | — |
| **Lịch sử nạp thẻ** `napthe` (telco/serial/code/amount/status) | ❌ | 🟢 **P1 — mới** | Bảng lọc theo player/ngày/trạng thái; đối soát nạp lỗi | 10 |
| **Chuyển khoản/Momo/đơn** `history_bank`, `momo_trans`, `order` | ❌ | 🟢 **P1 — mới** | Xem giao dịch tiền thật, tìm theo username/số tiền/mã giao dịch | 10 |
| **Lịch sử mua shop** `C:/xampp/htdocs/lichsu_mua.txt` (2.5MB) & **hoàn VIP** `vip_hoan.txt` | ❌ (mới chỉ xem qua link web) | 🟢 **P1 — mới** | Tail/filter file ngay trên panel: tìm theo tên player, khoảng ngày | 10 |
| **Lịch sử giao dịch P2P** `history_transaction` + nút xóa (`deleteHistory`) | ❌ | 🟢 **P1 — mới** | Xem 2 player đổi gì, thời gian; dọn bảng khi đầy | 10 |
| **Hủy giao dịch kẹt** (`TransactionService.cancelTrade`) | ❌ | 🟡 P2 | Nút "hủy trade đang treo" cho player kẹt | — |
| `account.reward/is_gift_box/gift_time` (quà tặng theo thời gian) | ❌ | 🟡 P2 | Xem/đặt quà chờ + thời hạn | — |

### D. Cấu hình & dữ liệu server

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Công thức dame (catalog) + Chỉ số Show/Real (Dame/HP/KI/Crit/Option/Output) | ✅ | — | — | — |
| GameTuning / EventTuning / TrungThu / WorldBoss / SystemTuning / CungMệnh / Set 5 món / SetConfig 9 nhóm | ✅ | — | — | — |
| MAX_PLAYER / MAX_PER_IP | ⚠️ chỉ **đặt RAM**, không ghi file | 🟢 P1 | Nối vào editor `config.properties` cho đúng bản chất | 6 |
| **`config.properties` đầy đủ**: `server.name/port/sv/expserver/waitlogin/maxplayer/maxperip/debug/daoautoupdater`, `database.*` | ❌ | 🟢 **P1** | Editor mọi key → **ghi file** → cảnh báo "cần restart"; hiện 6 file `.bak` lộn xộn do sửa tay | 6 |
| **Thông báo cuộn** bảng `notify` (`Manager.NOTIFY`) | ❌ (chỉ "TB All" gửi 1 lần) | 🟢 **P1 — mới** | CRUD danh sách TB tự động trong game (thêm/sửa/xóa/dùng lại) | 12 |
| **Cài đặt web** bảng `settings` (Title/Fanpage/Zalo/Email hỗ trợ/SiteKey/AccountBank...) | ❌ | 🟡 **P2 — mới** | Form sửa thông tin site + ngân hàng + khóa captcha | — |
| Trạng thái app web `adminpanel` (domain/logo/trạng thái/android/iphone...) | ❌ | 🟡 P2 | Xem + sửa link tải, trạng thái "hoạt động" | — |
| **Reload mở rộng**: `skill_template`, `intrinsic`, `item_template`, `npc`, `mob/map`, `task/badge`, `achievement`, `notify`, `power_limit`, `caption`, `radar` | ⚠️ mới 5 cái (giftcode/tầm bảo/shop/systemTuning/cungMệnh) | 🟡 P2 | Màn **"Tải lại dữ liệu"** với các nút — cần viết thêm method reload trong `Manager` | — |
| `power_limit` (`PowerLimitManager.load`) + `OpenPowerService` | ❌ | 🟡 P2 | Sửa giới hạn sức mạnh theo bậc; nút "mở giới hạn" cho player | — |
| Bảo trì auto 23:59:50 (`AutoMaintenance`) | ⚠️ chỉ bật/tắt | 🟡 P2 | Đổi giờ/tuần auto (hiện `final`) | — |
| `caption` (danh xưng theo sức mạnh/`CaptionManager`) | ❌ | 🟠 P3 | Sửa text danh xưng từng bậc | — |
| `flag_bag` (cờ bang) | ❌ | 🟠 P3 | Sửa danh sách cờ hiển thị khi chọn | — |
| `radar` (thẻ radar + option) | ❌ | 🟠 P3 | Sửa option thẻ radar (ảnh hưởng chỉ số) | — |
| Asset: `head_avatar`, `bg_item_template`, `img_by_name`, `part`, `array_head_2_frames`, `map_template` | ❌ | ❌ | Dữ liệu client — sửa sai vỡ giao diện | — |

### E. Boss, Map, Zone, Quái

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Sửa data boss (dame/HP/nơi spawn/rớt/ghi chú) + reset | ✅ | — | — | — |
| Boss live: kill, gọi/spawn theo zone, tìm spawn | ✅ | — | — | — |
| World Boss: cấu hình, summon, kill, cấp thưởng, reset top, lịch spawn | ✅ | — | — | — |
| Map & Mob override (thay quái/HP/drop theo map) | ✅ | — | — | — |
| **Zone live** (tab Map&Mob) | ❌ | 🟡 P2 | Xem zone nào có ai; **teleport player → map/zone**; gọi lại quái zone; clear boss trong zone | — |
| **Đồ rơi map** `ItemMapService` | ❌ | 🟡 P2 | Xem đồ đang rơi; nút "dọn đồ rơi" theo map/zone | — |
| Danh sách boss phụ bản `listbosses` (`ListBossesService`) | ❌ | 🟡 P2 | Gộp tab vào màn BOSS (boss sự kiện/phụ bản) | — |
| `TrapMap`, `MaBuHold`, `WayPoint`, `TaskDirections` (đường map) | ❌ | ❌ | Logic map hardcode — không có gì sửa | — |

### F. Sự kiện, Rồng, Phụ bản

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| 9 sự kiện lớn on/off + catalog + Trung Thu (tỉ lệ→rương→chỉ số) | ✅ | — | — | — |
| Bảng `event` (`EventDAO`, sự kiện 8/3...) | ❌ | 🟡 P2 | Thêm tab "Sự kiện theo DB" trong màn EVENTS | — |
| **Sự kiện nhỏ NPC** `EventSuKien/`: Câu Cá, Kết Hôn, Chuyển Sinh, Số Xu Mệnh, Tết, Thợ Mò, Bán Gói, Bí Kíp | ❌ | 🟡 P2 | Tab từng cái: bật/tắt NPC + tham số nếu có DB; code hardcode thì chỉ bật/tắt được | — |
| **Rồng**: `SummonDragon` (thường/đen/băng), `ShenronEvent`, `NgocRongNamecService` (7 ngọc Namek), `BlackBallWar` | ❌ | 🟡 P2 | Gọi rồng admin (chọn điều ước), reset rồng, xem 7 ngọc đang đủ, bật/tắt BlackBallWar + lịch | — |
| **Quà top tự gửi** `auto_gift_top` (`AutoGiftTopService`) | ❌ | 🟡 P2 | Sửa item quà + số lượng + lịch gửi từng loại top | — |
| Phụ bản `models/*` (MajinBuu, DestronGas, RedRibbonHQ, SnakeWay, TreasureUnderSea, LeoThapNe, SuperDivineWater, DeathOrAliveArena, WMAT, 23rd, Card, Radar) | ❌ | 🟠 P3 | Màn "trạng thái + lịch + bật/tắt" cho cái nào có DB; phần hardcode chỉ xem | — |
| **Câu cá** `PhuBanService.CodeCauCa` (mồi 1717/1728/1729, map 216, tỉ lệ) | ❌ | 🟠 P3 | Đưa mồi + tỉ lệ ra config rồi mới sửa trên panel | — |
| `Combine` tỉ lệ nâng cấp (`getTiLeNangcap*`, `dnsdapdo`...) | ❌ | 🟠 P3 | Sửa tỉ lệ = chỉnh kinh tế trực tiếp — chỉ làm khi có phương án cân bằng | — |

### G. Top & xếp hạng

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Top nạp | ✅ | — | — | — |
| **19 top** (`src/Top/`, `Manager.top*`, `TopService`) | ❌ | 🟢 **P1** | Màn "Top": xem từng loại (SM/SD/HP/KI/SK/PVP/Săn boss/Tầm bảo/Câu cá/Thiên-Địa đạo/Tu tiên/Chuyển sinh/VND/VIP...), nút **tải lại** (`reloadtop`, `updateTopNow`) + **đặt chu kỳ refresh** (`TopService.startAutoRefresh(phút)`) + `TopEventManager.load()` | 8 |
| SuperRank (`SuperRankDAO`, `player.rank`) | ❌ | 🟡 P2 | Xem/sửa rank, set mùa mới | — |

### H. Tiến trình nhân vật: Nhiệm vụ / Nội tại / Tiên trình / Đệ / Cây phép  (**P1 — trái tim của panel**)

| Chức năng (bảng/cột) | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| **Nhiệm vụ chính** `data_task` (`TaskService.sendTaskMain`) | ❌ | 🟢 P1 | Dropdown chọn task chính từ `task_main_template` + reset index con; đối chiếu lệnh `nv_<id>` in-game | 1 |
| **Job phụ** `data_side_task` (`SideTask.reset/renew`) | ❌ | 🟡→P1 | Xem tiến độ 20 job; **nhận lại job** khi kẹt | 1 |
| **Job bang** `data_clan_task` (`ClanTask`) | ❌ | 🟢 P1 | Xem/reset job bang (gộp màn Bang) | 5 |
| **Badges & điểm danh** `dataBadges`, `dataTaskBadges`, `dailyGift` | ❌ | 🟢 P1 | Xem badge đã mở; reset điểm danh/nhận lại quà ngày | 1 |
| **Nội tại** `data_intrinsic` (27 mẫu `intrinsic`) | ❌ | 🟢 P1 | Chọn nội tại từ dropdown (tên+mô tả), set param1/2, "mở lại"/gỡ — thay SQL | 2 |
| **Tiên trình**: `SagaChuyenSinh`, `SagaThienDao`, `SagaDiaDao`, `SagaTuTien[3]`, `PhapTac_ThienDao`, `Saga_VIP`, `TamkjllCapPb` | ❌ | 🟢 P1 | Form per-player: cộng/trừ/set số lần chuyển sinh, điểm Thần/Địa đạo, Tu Tiên, VIP, pháp tắc | 3 |
| **Điểm sự kiện**: `diemfam`, `sukien`, `point_phongtap`, `point_sb`, `point_vithu`, `point_bdkb`, `point_moruong`, `point_dapdo`, `point_vip`, `SukienTamBao`, `leothap`, `active_vong_quay` | ❌ | 🟢 P1 | Cộng/trừ điểm các bảng xếp hạng/sự kiện cho player (gộp tab "Tiên trình") | 3 |
| **Cây phép** `data_magic_tree` (level/hạt đậu/thời nâng) | ❌ | 🟢 P1 | Set level ≤10, hạt đậu, reset thời gian nâng | 4 |
| **Đệ tử** `pet` (`PetService`) + **Đạo Lữ** `Tamkjll_Pet`, `TamkjllDLDL[20]`, `DLbTamkjll`, `TamkjllThomo(Exp)`, `Tamkjll_Tu_Ma` | ❌ | 🟢 P1 | Xem/tạo đệ (gọi `createNormalPet`), set cấp/EXP; Đạo Lữ: cấp, EXP, thức ăn, linh hồn | 4 |
| **Luyện tập** `data_luyentap` (`TraningDAO`) | ❌ | 🟡 P2 | Xem/sửa điểm phòng tập | — |
| **Radar** `data_card` + `RadarService.RadarSetLevel/SetAmount` | ❌ | 🟠 P3 | Set cấp/số lượng thẻ radar cho player | — |
| **Thành tựu** `data_achievement` (21 mẫu) | ❌ | 🟠 P3 | Xem tiến độ + config thưởng (sửa sai mất cân bằng) | — |
| **Hẹn hôn** `dakethon` | ❌ | 🟠 P3 | Hủy kết hôn khi player kẹt | — |
| Đồ trạng thái phụ: `data_black_ball`, `data_mabu_egg`, `nhanthoivang`, `ruonggo`, `sieuthanthuy`, `vodaisinhtu`, `rongxuong`, `isbienhinh` | ❌ | 🟠 P3 | Chỉ nút "reset khi kẹt" | — |

### I. Bang hội  (**P1 — màn riêng mới**)

| Chức năng (`src/clan`, bảng `clan`) | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Danh sách 20–25 bang: id/tên/slogan/level/powerPoint/maxMember/active/capsuleClan | ❌ | 🟢 P1 | Bảng bang + **sửa slogan/level/maxMember**; nút tải lại danh sách (`reloadClanMember`) | 5 |
| Thành viên & quyền (`ClanMember`: LEADER/DEPUTY/MEMBER) | ❌ | 🟢 P1 | Bảng thành viên online/offline, **thăng/hạ quyền**, kick, **giải tán** (`deleteDB`) | 5 |
| **Phó bản bang**: `doanhTrai`, `BanDoKhoBau`, `ConDuongRanDoc`, `KhiGasHuyDiet` + `lastTimeOpen*` + `timesPerDayKGHD` | ❌ | 🟢 P1 | Mở/đóng phó bản cho bang, **reset số lượt/ngày**, xem ai đang mở | 5 |
| Chat bang (`ClanMessage`) | ❌ | 🟠 P3 | Xem log chat bang khi điều tra (xem-only) | — |
| Cờ bang `flag_bag` | ❌ | 🟠 P3 | Xem/sửa cờ bang | — |

### J. Vận hành server

| Chức năng | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| Bảo trì ngay/sau N phút, auto maintain, auto clean | ✅ | 🟡 | Đổi giờ auto (xem mục D) | — |
| **Console đọc log** `server_latest.log` (17MB), `watchdog.log`, `server_restart.log` | ❌ | 🟢 **P1** | Tail 500 dòng cuối, lọc `exception/error/NullPointer`, tìm theo tên player/IP, nút tải file, nút xem `[SK]` debug | 7 |
| **Lệnh terminal server** (`ServerManager.activeCommandLine`: `baotri`, `athread`, `nplayer`, `shop`, `item`, `a`) | ❌ | 🟡 P2 | Gộp vào Console: nút bảo trì/số thread/số người/reload shop — **bỏ nút `a` (đóng server)** hoặc xác nhận 2 lớp | — |
| **Backup/Restore DB 1 nút** (chưa có `mysqldump` ở đâu trong project) | ❌ | 🟡 P2 | Nút dump → `backup/`, danh sách file, restore (xác nhận kép); helper sẵn `AutoMaintenance.runBatchFile` | — |
| **Lệnh Admin** (mục riêng, xem mục A) | ❌ | 🟢 P1 | Form thay gõ chat in-game, có audit | 9 |
| Kick all / TB All / kick player / gửi TB player | ✅ | — | — | — |
| Chat toàn cục kênh VIP/veve (`ChatGlobalService`) | ⚠️ mới "TB All" | 🟠 P3 | Gửi chat kênh từ panel (hiện TB All đã đủ) | — |

### K. Chức năng nhỏ lẻ khác (rà từng hàm)

| Chức năng (code) | Panel | Quyết định | Nếu đưa → quản lý/sửa được gì | Ưu tiên |
|---|---|---|---|---|
| **Cứu trợ player kẹt**: `EffectSkillService.removeTroi/removeAnTroi/removeThoiMien/removeStun/removeSocola`, `Service.releaseCooldownSkill`, `PlayerService.hoiSinh/hoiSinhMaBu` | ❌ | 🟡 **P2 — mới** | 3 nút trong form NV: **gỡ trạng thái (bị trói/choáng/thôi miên)**, **hồi chiêu skill**, **hồi sinh NV đang chết/kẹt map phụ** | — |
| **Bùa** `player.data_charm` (`Charms.addTimeCharms`) | ❌ | 🟡 P2 | Xem bùa đang có (đảo, thời gian); thêm/gỡ bùa | — |
| **Item time** `data_item_time`, `item_time_new` (`ItemTimeService.sendAllItemTime`) | ❌ | 🟡 P2 | Xem hiệu lực thời gian còn lại; **xóa text time kẹt** (Doanh trai, TDLT, Khí gas...) | — |
| **Quà phúc lợi đã nhận** `player.phuc_loi` | ✅ có màn config | 🟡 P2 | Nút reset để player nhận lại mốc quà (gộp form NV) | — |
| `VatPhamDaBan` (100 đồ đã bán) | ❌ | 🟠 P3 | Tab xem-only form NV | — |
| **Đổi type PK** (`PlayerService.changeTypePK`), **gỡ effect map** (`EffectMapService`) | ❌ | 🟠 P3 | Hỗ trợ sự kiện/họp bang | — |
| **Minigame**: `DecisionMakerCost`, `LuckyNumberCost` (giá `final`, `timeGame/timeDelay` sửa được) | ❌ | 🟠 P3 | Chỉ đổi được thời gian ván; giá là `final` → phải sửa code | — |
| **Câu cá/Thợ mò/Tết/Trung Thu drops** (hardcode trong manifest) | một phần ✅ (Trung Thu) | 🟠 P3 | Khi nào đưa tỉ lệ ra file/config mới panel được | — |
| `ChisoAn` (`OPTION_CHI_SO_vip`, `OPTION_CHI_SO_HUYENAO`) — option chỉ số ẩn trang bị | ❌ | 🟠 P3 | Mảng hardcode → nếu muốn phải ra config trước | — |
| `NPoint` logic tính dame/crit/nội tại nội bộ | ❌ | ❌ | Đã có màn Công Thức + Chỉ Số — đưa code lên là trùng | — |
| `AutoGiftTop` (đã liệt kê F) | ❌ | 🟡 P2 | — | — |

---

## 3. CHI TIẾT 12 MỤC P1 (thứ tự làm)

| # | Màn hình mới | Vị trí sidebar | API cần viết (PanelService) | Quản lý/sửa được gì |
|---|---|---|---|---|
| 1 | **Nhiệm vụ & Điểm danh** (tab trong Quan Lý Nhân Vật) | QUAN LY | `viewTask/saveTask/resetSideTask/resetDaily/viewBadges` | Chọn task chính, nhận lại job phụ, reset điểm danh/badges |
| 2 | **Nội tại** (tab NV) | QUAN LY | `viewIntrinsic/setIntrinsic/removeIntrinsic` | Set id + param nội tại, mở lại/gỡ |
| 3 | **Tiên trình & Điểm sự kiện** (tab NV) | QUAN LY | `viewSaga/saveSaga` | Chuyển sinh, Thần/Địa đạo, Tu Tiên, VIP, `point_*`, `diemfam/sukien` |
| 4 | **Cây phép + Đệ/Đạo Lữ** (2 tab NV) | QUAN LY | `viewMagicTree/saveMagicTree`, `viewPet/createPet/savePet` | Level cây phép, hạt đậu; tạo đệ, cấp/EXP, Đạo Lữ |
| 5 | **Bang hội** (màn riêng) | QUAN LY | `listClans/viewClan/saveClan/kickMember/setRole/disposeClan/openPhoBan` | Slogan/level/maxMember, quyền, kick, giải tán, mở/đóng 4 phó bản bang, reset lượt |
| 6 | **Cấu hình Server** (`config.properties`) | (thay thế ô MAX_PLAYER hiện tại) | `listConfigProps/saveConfigProps` | Mọi key `server.*`/`database.*` → **ghi file** + cảnh báo restart |
| 7 | **Console log** | TONG QUAN | `tailLog(n)/filterLog(pat)/downloadLog` | Đọc log, lọc lỗi, tìm theo tên/IP, tải file |
| 8 | **Top** (màn riêng) | QUAN LY | `listTop(kind)/reloadTop(kind)/setTopRefresh(min)` | 19 loại top, tải lại, đặt chu kỳ refresh, xem thưởng top |
| 9 | **Lệnh Admin** (màn riêng) | QUAN LY | `runAdminCmd(kind, player, args)` + audit | buff chỉ số/sức mạnh/item, đổi map, học tuyệt kỹ, tele/pk, hồi chiêu — thay gõ chat |
| 10 | **Lịch sử nạp & giao dịch** (màn riêng) | GIAO DICH | `listNapThe/listBank/listMomo/listHistoryTx/tailLichSuMua` | Đối soát nạp thẻ, bank/momo, mua shop, hoàn VIP, giao dịch P2P |
| 11 | **Mở rộng form** (không cần màn mới) | — | sửa `viewPlayer/saveBasic` | acc nâng cao (role/thoi_vang/luotquay/event_point...), NV: rương box, đầu/giới tính |
| 12 | **Thông báo cuộn** (tab trong Sự kiện hoặc Hệ Thống) | — | `listNotify/saveNotify/deleteNotify` | CRUD bảng `notify` |

---

## 4. P2 (12 mục — làm sau P1)

1. Reload dữ liệu mở rộng (một màn, các nút) — cần viết method trong `Manager`.
2. Backup/Restore DB 1 nút (mysqldump → `backup/`) — chưa có sẵn gì trong project.
3. Zone live (xem ai trong map, teleport player, gọi quái, clear boss) + dọn đồ rơi map.
4. Rồng/BlackBall: gọi rồng, reset, 7 ngọc Namek, bật/tắt BlackBallWar.
5. Account an toàn: reset mã bảo vệ/captcha, danh sách NV của acc.
6. Sự kiện nhỏ `EventSuKien` + bảng `event`.
7. Quà top tự gửi (`auto_gift_top`).
8. SuperRank: xem/sửa rank, reset mùa.
9. **Web shop**: `web_shop` / `web_shop_history` / `kho_web` / `posts`.
10. **Cài đặt web**: `settings`, `adminpanel`.
11. **Cứu trợ player kẹt**: gỡ trạng thái, hồi chiêu, hồi sinh (+ hủy trade kẹt).
12. **Tiện ích NV**: bùa, item time, reset phúc lợi, giftcode đã nhận, quà chờ `reward`.

*(cùng: sửa `item_template` + reload, đổi giờ bảo trì auto, PowerLimit, lệnh terminal gộp Console)*

## 5. P3 — CÂN NHẮC (khoảng 14 mục, làm khi có nhu cầu thật)

Sửa template nhiệm vụ thưởng · thành tựu/`data_achievement` · radar (`data_card`) · Combine tỉ lệ nâng cấp · câu cá (mồi/tỉ lệ) · minigame (giá `final`) · caption danh xưng · cờ bang · `items_daban` · đổi tên NV · hủy kết hôn (`dakethon`) · DailyGift config · type_item/type_sell · chat kênh VIP từ panel · phụ bản `models/*` chỉ màn trạng thái.

---

## 6. ❌ KHÔNG NÊN ĐƯA LÊN PANEL (và lý do) — 16 nhóm

| Nhóm code/bảng | Lý do |
|---|---|
| `AntiLogin`, logic captcha/mã bảo vệ | Chống hack — chỉ cần **nút reset** cho acc kẹt |
| `network/`, session, packet, `Controller` routes | Sửa sai treo server, không thuộc quản trị |
| `account.password/token/xsrf_token`, bảng `cpanel` (token) | Bảo mật tuyệt đối |
| Asset client: `head_avatar`, `bg_item_template`, `img_by_name`, `part`, `array_head_2_frames`, `DataGame`/`ItemData` (gói data client) | Sửa sai vỡ giao diện/mất sync client |
| `map_template`/`mob_template`/`npc_template` (hằng load lúc boot) | Có màn Map&Mob override + NPC/Boss riêng |
| Logic tính dame/crit/`NPoint`/`SkillService` | Đã có màn Công Thức + Chỉ Số |
| AI boss `boss_manifest/*` (194 file) | Đã có màn sửa **data** boss; AI là code → deploy |
| `friends`/`enemies`, chat riêng tư, `homthu` nội dung | Riêng tư người chơi |
| `minigame/` logic (RockPaperScissors, LuckyNumber, DecisionMaker) | Code hardcode, chỉ 2 biến thời gian sửa được → không đáng màn |
| `TaskDirections`, `TrapMap`, `MaBuHold`, `WayPoint`, `EffectMapService` | Logic map/đường đi, không có tham số sửa |
| `Fusion`/`Clone`/`Satellite`/`EffectSkin` (biến hình, ngoại trang) | Logic client-driven |
| `matches/PVP` invite/revenge logic | Chỉ cần Top PVP (mục G) |
| `menu controller`, `MenuController`, `SubMenuService` | Giao thức menu in-game |
| File test (`data/test.java`, `minigame/Test.java`) | Nên xóa khỏi source, không lên panel |
| `utils` thuần (Util/TimeUtil/StringUtil/FileIO/PasswordUtil) | Helper, không có dữ liệu quản trị |
| Xác nhận 2 lớp cho thao tác nguy hiểm (kick all, xóa acc, restore DB) | Không phải màn hình — là **yêu cầu bắt buộc** khi làm các mục trên |

---

## 7. LỘ TRÌNH CẬP NHẬT

1. ✅ **P0 đã xong + deploy**: Quan Lý Nhân Vật (online/offline), Option builder, Nhật ký admin + IP, Gửi mail — compile 0 lỗi, boot 0 exception.
2. 🟢 **P1 (12 mục)** theo thứ tự: Nhiệm vụ → Nội tại → Tiên trình → Cây phép+Đệ → **Bang hội** → `config.properties` → **Console log** → **Top** → **Lệnh Admin** → **Lịch sử nạp & giao dịch** → **Mở rộng form acc/NV** → **Thông báo cuộn**.
3. 🟡 **P2 (12 mục)**: reload mở rộng, backup, zone live + đồ rơi, rồng, acc an toàn, sự kiện nhỏ, quà top, SuperRank, web shop, cài đặt web, cứu trợ kẹt, tiện ích NV.
4. 🟠 **P3**: mục 5 — làm khi có nhu cầu thật.

*Mọi mục đều triển khai dạng menu sidebar + form/table; thao tác nguy hiểm bắt buộc xác nhận 2 lớp và ghi vào `panel_audit`.*

---

## 8. PHỤ LỤC — Nơi code nhanh tra

| Chức năng | File |
|---|---|
| Lệnh admin in-game | `src/server/Command.java` (`vipchat`, ~:160–520) |
| Menu admin NPC | `src/npc/NpcFactory.java` :1036 (`MENU_ADMIN`) |
| Form nhập in-game | `src/services/func/Input.java` :767–949 |
| Lệnh terminal server | `src/server/ServerManager.java` :252 (`activeCommandLine`) |
| Bảng player/account | DB `hondaodragon`.`player` (99 cột), `account` (41 cột) |
| Lịch sử mua/hoàn VIP (file) | `C:/xampp/htdocs/lichsu_mua.txt`, `vip_hoan.txt` (`utils/InputHoanTien`) |
| Lịch sử giao dịch P2P | `src/jdbc/daos/HistoryTransactionDAO.java` → bảng `history_transaction` |
| Nạp thẻ/Momo/bank | bảng `napthe`, `momo_trans`, `history_bank`, `order` |
| Shop web | bảng `web_shop`, `web_shop_history`, `kho_web`, `posts` |
| Thông báo cuộn | bảng `notify` → `Manager.NOTIFY` (:705) |
| Top | `src/services/func/TopService.java`, `src/server/Manager.java` (`reloadtop`:1006) |
| Bang & phó bản bang | `src/clan/Clan.java` (:39–72) |
| Log server | `server_latest.log`, `watchdog.log` |
| Panel hiện tại | `src/panel/ControlPanel.java` (24 màn), `src/panel/PanelService.java` (181 API) |
