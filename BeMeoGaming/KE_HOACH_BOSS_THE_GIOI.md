# KẾ HOẠCH: BOSS THẾ GIỚI (skill 3 hành tinh + PvP toàn map + hồi chiêu = 0)

> Trạng thái: **KẾ HOẠCH — CHƯA CODE**. Tài liệu này để chốt thiết kế trước khi viết code,
> vì cả 3 thay đổi đều đụng vào luồng chiến đấu lõi (skill, PK, hồi chiêu) — rủi ro lỗi rất cao.

## 1. Mục tiêu

| # | Tính năng | Kết quả mong muốn |
|---|-----------|-------------------|
| F1 | Boss thế giới thi triển skill của **các hành tinh** | Boss dùng được skill Trái Đất / Namếc / Xayda (kể cả chiêu đặc biệt, biến hình), đổi bộ skill theo pha (theo % HP hoặc theo thời gian sống) |
| F2 | **PvP toàn map** khi boss đang ở map đó | Mọi khu (zone) trong map có boss: tất cả người chơi đánh nhau được, không chỉ khu có boss |
| F3 | **Hồi chiêu = 0** cho người chơi trong map đó | Bấm skill là ra liên tục, thanh hồi chiêu client về 0 |

## 2. Hiện trạng code (đã có, dùng lại được)

**Boss thế giới (đã tồn tại, không cần viết mới):**
- `boss/boss_manifest/WorldBoss/WorldBoss.java` — bất tử (HP tự hồi), xoay khu mỗi `ZONE_SEC`, sống `LIFE_MIN` phút, ghi dame top vào `WorldBossTracker`.
- `boss/WorldBossScheduler.java` — gọi theo `SPAWN_TIMES` (mặc định `12:00,19:00`), đang chạy nền 20s/tick.
- `boss/WorldBossTracker.java` — mùa (season) theo giờ, top 1/2-3/4-10 + console, ghi DB (`WorldBossDameDAO`, `WorldBossRewardDAO`), flush 30s/lần.
- `panel/tuning/WorldBossTuning.java` — file `data/config/worldboss.properties`: `on, mapId, hp, dame, lifeMin, zoneSec, startZone, spawnTimes, minDame, top1Json...` → **chỗ để thêm công tắc cho 3 tính năng này**.
- `boss/BossesData.WORLD_BOSS` (id `BossID.WORLD_BOSS = -7000`), `BossManager.createBoss` → `WorldBoss`.

**Skill của boss hiện tại:**
- `Boss.initBase()` → `Boss.initSkill()` dựng skill từ `BossData.getSkillTemp()` (`int[][] {skillId, point, coolDown?}`), dùng `SkillUtil.createSkill(skillId, point)`.
- Boss đánh bằng `Boss.attack()` → `SkillService.gI().useSkill(this, pl, null, -1, null)`; chọn target bằng `getPlayerAttack()`.

**PvP (PK) hiện tại:**
- Chế độ PK là **thuộc tính của người chơi**: `Player.typePk` (`ConstPlayer.NON_PK=0`, `PK_PVP=3`, `PK_PVP_2=4`, `PK_ALL=5`).
- Đổi chế độ: `PlayerService.gI().changeAndSendTypePK(player, type)` (+ `sendTypePk` gửi gói 35 cho client).
- Cổng chặn đánh người: `SkillService` (~dòng 1642-1680) kiểm tra `typePk` của cả 2 bên; `Player.injured` mới là nơi trừ máu.
- **Chưa có cờ PvP cấp map/zone** (không có `map.isPvp` nào trong code) → phải tự thêm.
- Tham chiếu sẵn có: `matches/PVP.java`, `matches/PVPService.java`, map Võ Đài Sinh Tử (`boss_manifest/DeathOrAliveArena`).

**Hồi chiêu hiện tại:**
- Mỗi skill giữ `Skill.coolDown` + `Skill.lastTimeUseThisSkill` (`skill/Skill.java`).
- Cổng chặn server: `SkillService.canUseSkillWithCooldown(player)` → `Util.canDoWithTime(skillSelect.lastTimeUseThisSkill, skillSelect.coolDown - 50)`.
- Đồng bộ client: gói `-94` qua `Service.sendTimeSkill(pl)` / `Service.releaseCooldownSkill(pl)` — hàm này **đã set `lastTimeUseThisSkill = now - coolDown` và đẩy hồi chiêu về 0 cho toàn bộ skill** (kèm hồi đầy KI).

## 3. Thiết kế đề xuất (từng tính năng)

### F1 — Skill 3 hành tinh cho boss thế giới

**Cách làm (ưu tiên cấu hình, không hardcode):**
1. Thêm vào `WorldBossTuning`: `skillPhase` (danh sách pha, ví dụ `50:trai_dat;25:namec;0:xayda`), `skillSetTraiDat/Namec/Xayda` (chuỗi `skillId:point:cooldown;...`), `phaseMode` (`hp` theo % HP còn lại | `time` theo phút sống), `phaseSec`.
2. Trong `WorldBoss`, override `initSkill()` (hoặc thêm `applyPhaseSkills(int phase)`) để tạo skill từ chuỗi cấu hình bằng `SkillUtil.createSkill` — dùng lại đúng API của `Boss.initSkill` để không phá animation.
3. Đổi pha: trong `autoLeaveMap()` (đã tick mỗi giây) kiểm tra điều kiện pha → gọi lại `initSkill()` + chat cảnh báo + `Service.getInstance().point(this)` để client refresh.
4. Chiêu biến hình/đặc biệt: dùng `EffectSkillService` (setMonkey, setIntrinsic...) như boss khác; **không** cho boss biến hình khi đang bất tử kiểu "không bao giờ chết" nếu animation bị kẹt — kiểm tra kỹ.

**Rủi ro chính:**
- Sai `skillId`/`point` → boss không ra chiêu hoặc crash `NullPointerException` trong `SkillUtil` (bọc try-catch từng pha, luôn giữ bộ skill cũ nếu parse lỗi).
- Skill Trái Đất/Namếc/Xayda có `coolDown` riêng: nếu để hồi chiêu dài, boss sẽ "đứng im" (không còn `active()`), nên đặt hồi chiêu ngắn trong cấu hình.
- Client chỉ vẽ đúng animation khi boss có `nPoint.mp` (đã có `mp = mpg` trong `Boss.update()`), nên nhớ set `mpg` khi đổi pha.

### F2 — PvP toàn map khi boss ở map đó

**Cách làm:**
1. Thêm cờ runtime cấp map: `Map.pvpWorldBossZone` (hoặc `Zone.pvpBoss`) bật/tắt trong `WorldBoss.joinMap()` / `adminKill()` / khi hết `LIFE_MIN`.
2. Đổi chế độ PK cho người chơi: `PlayerService.changeAndSendTypePK(player, ConstPlayer.PK_ALL)` khi họ vào map/khu đó, và **trả về `NON_PK` khi rời đi** (bắt buộc, nếu không người chơi bị PK vĩnh viễn ở mọi map).
3. Gọi đổi PK ở **tất cả** các điểm vào/ra map: `ChangeMapService.changeMap`, `changeMapWaypoint`, login vào map boss, hồi sinh trong map, boss rời map/xoay khu, server start (nếu người chơi offline đang đứng trong map đó).
4. Ghi nhớ trạng thái cũ: lưu `Player.typePkTruocBoss` để trả lại đúng chế độ trước đó (tránh hạ PK của người đang PK_ALL sẵn).

**Rủi ro chính (cao nhất trong 3 tính năng):**
- **Người chơi bị kẹt PK_ALL** sau khi boss biến mất → phải có "hàm quét dọn" chạy mỗi 5s: player nào không còn ở map boss thì trả PK về.
- Chết trong map boss: có rơi đồ/trừ điểm không? Cần chốt (đề xuất: không rơi đồ, không trừ điểm — map sự kiện).
- Đồng đội/bang: PK_ALL đánh cả đồng đội — cần chốt có miễn trừ bang/phái không.
- Đệ tử/pet: pet có đánh người không, đánh pet có tính không (`Pet.injured`).
- Hiển thị: `sendTypePk` gửi cho cả map → thêm gói tin khi vào map (tối ưu: gửi 1 lần, không gửi mỗi tick).
- Map an toàn (làng, map nhiệm vụ, làng Namek…) có bị áp PvP không → cần allowlist map an toàn.

### F3 — Hồi chiêu = 0 trong lúc có boss

**Cách làm:**
1. Thêm cờ `Zone.hoiChieuZero` (theo khu, hoặc chỉ cho người chơi đang ở map boss — đề xuất theo **map**, đúng yêu cầu "toàn bộ khu người chơi").
2. Cổng server: trong `SkillService.canUseSkillWithCooldown(player)` → nếu người chơi đang ở map/khu có cờ thì `return true` (hoặc cộng thêm: `skill.coolDownHieuLuc = 0`).
3. Client: gửi lại `Service.releaseCooldownSkill(player)` **khi vào map** (1 lần) và khi boss bắt đầu/kết thúc; khi rời map gửi `Service.sendTimeSkill(player)` để client vẽ lại hồi chiêu thật.
4. Người chơi vào map giữa lúc boss đang sống → phải gọi cùng luồng (dùng chung hàm `WorldBossPvpService.apDung(player)`).

**Rủi ro chính:**
- **Sốc gói tin**: skill spam liên tục → server/đường truyền nghẽn. Bắt buộc thêm **rate-limit** phía server (ví dụ tối đa 10-15 skill/giây/người) trước khi mở cho toàn map.
- Người chơi dùng hồi chiêu 0 ở map đó rồi **gọi skill xong lại chạy sang map khác**: cờ phải được kiểm tra theo zone hiện tại mỗi lần dùng skill, không chỉ lúc vào map.
- `coolDown` còn dùng cho cả `Skill.lastTimeUseThisSkill` khi lưu/đọc DB (`PlayerDAO` lưu mảng skill) → **không** được sửa giá trị gốc `skill.coolDown` (sẽ hỏng dữ liệu người chơi), chỉ bypass khi kiểm tra.
- Nếu tương lai bật buff "hồi chiêu" cho VIP, hai luồng sẽ chồng nhau → thống nhất 1 hàm `SkillService.hieuChieuHienTai(player, skill)`.

## 4. Lộ trình triển khai (4 giai đoạn, mỗi giai đoạn bật/tắt bằng panel)

| Giai đoạn | Nội dung | Công tắc panel | Điều kiện qua bước sau |
|-----------|----------|----------------|------------------------|
| **G0** | Refactor an toàn: tách `WorldBossPvpService` (1 nơi duy nhất lo vào/ra map, áp/trả PK, hồi chiêu) + thêm 3 cờ `pvpOn, zeroCdOn, skillPhaseOn` (mặc định **tắt**) | `worldboss.properties` | Test 2 ngày offline: không có ai bị kẹt PK, không lỗi log |
| **G1** | F3 (hồi chiêu 0) + rate-limit | `zeroCdOn=1` | Đo tải: CPU/gói tin khi 20 người spam skill; rời map hồi chiêu về đúng |
| **G2** | F1 (skill 3 hành tinh) | `skillPhaseOn=1` | Boss ra chiêu đủ 3 bộ, không kẹt animation, đổi pha theo % HP |
| **G3** | F2 (PvP toàn map) — sau cùng vì rủi ro cao nhất | `pvpOn=1` | Test 2 máy: vào/ra map giữa event, chết, đệ tử, out game rồi login lại |

**Test checklist (bắt buộc trước khi bật production):**
1. Vào map boss khi event đang chạy → PK bật, hồi chiêu 0 ngay lập tức (cả 3 hành tinh).
2. Boss rời map / hết `LIFE_MIN` / admin kill → mọi người trong map trả PK cũ + hồi chiêu trở lại bình thường.
3. Người chơi đứng ở map boss rồi **out game**, login lại khi boss đã đi → không bị PK_ALL.
4. Đổi khu (zone) trong map: vẫn PvP (đúng yêu cầu "toàn bộ khu").
5. Đánh nhau với bang/phái/đồng đội → chốt hành vi.
6. Chết trong map → có/không rơi đồ, hồi sinh về đâu.
7. Tải: 30 người spam skill + boss HP 1e18 → kiểm tra CPU server và log gói tin.
8. Restart server giữa lúc boss sống → trạng thái PK được trả về đúng khi login.

**Rollback:** tắt 3 cờ trong panel (`zeroCdOn=0, skillPhaseOn=0, pvpOn=0`) → hệ thống về đúng như hiện tại, không cần rebuild, không cần restart.

## 5. Câu hỏi cần chốt trước khi code

1. **F2 phạm vi**: PvP toàn **map** (mọi khu) hay chỉ khu có boss? PvP ở **làng/map an toàn** hay chỉ map được cấu hình?
2. **F2 luật**: có miễn trừ đồng bang/đồng đội/đệ tử không? Chết có mất gì không?
3. **F3 phạm vi**: hồi chiêu 0 cho **mọi skill** hay chỉ skill đánh (không áp cho biến hình/chiêu đặc biệt)?
4. **F1 pha**: đổi skill theo **% HP** hay theo **thời gian sống**? Có thông báo toàn server khi boss đổi pha không?
5. **Phần thưởng**: giữ nguyên top dame 10 như hiện tại, hay thêm thưởng cho người "sát thương cuối"?
6. **Chỉ số boss**: `WorldBossTuning.HP` hiện 9e14, đang bị cộng thêm 1e18 (tuning `boss_hp_them`) → muốn boss thế giới **miễn** phần +1e18 hay giữ luôn?

## 6. Ghi chú kỹ thuật đã kiểm chứng

- Đã xác nhận trong code: `Service.releaseCooldownSkill(Player)` và `Service.sendTimeSkill(Player)` tồn tại → F3 không cần viết gói tin mới.
- Đã xác nhận: **không có** cờ PvP cấp map nào trong code → F2 là phần phải viết mới nhiều nhất.
- Đã xác nhận: hồi chiêu chỉ được chặn ở `SkillService.canUseSkillWithCooldown`, dùng chung cho người chơi và pet → sửa 1 chỗ, nhưng phải rate-limit.
