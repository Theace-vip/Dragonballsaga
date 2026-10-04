# BÁO CÁO CHI TIẾT TỪNG CƠ CHẾ GAME — Server BeMeoGaming

Bản này là bản **mở rộng**, viết chi tiết từng cơ chế: điều kiện mở khóa, cách chơi từng bước, công
thức tính, chỉ số nhận được, và chỗ lưu dữ liệu. Mọi số liệu đều đọc trực tiếp từ code trong `src`,
kèm tên file/dòng để tra lại khi cần.

---

## 1. HỆ THỐNG ĐẠO LỮ VÀ EM BÉ (Pet Tamkjll)

### 1.1. Bản chất: một hệ thống, hai cái tên

Server chỉ có **một con pet duy nhất** thuộc nhóm này, nhưng nó được ba nơi gọi bằng ba tên khác
nhau: NPC Đạo Lữ gọi là "Đạo Lữ SSS", NPC Kết Hôn gọi là "Em Bé Rồng", lệnh chat gọi là "Đạo Lữ".
Lý do là tất cả chúng đọc chung một bộ trường trên nhân vật người chơi:

- `TamkjllNamePet` — tên pet do người chơi đặt.
- `EmBeLv` — cấp hiện tại (trần code là 10.000, bị clamp khi load).
- `EmBeEXP` — exp tích lũy để lên cấp kế.
- `TamkjllPetGiong` — linh căn, số 0 đến 10; giá trị **-1 nghĩa là không có pet** (chết/đói/bị xóa).
- `TamkjllPetHunger` — thanh thức ăn, tính bằng "phần trăm" nhưng thực chất là điểm từ 1 đến 500+.
- `TamkjllPetPower` — sức mạnh pet (con số lớn, hiển thị qua `Util.getFormatNumber`).
- `TamkjllPlayerAttack` — mục tiêu người chơi pet đang đánh (dùng khi ra lệnh PK).

Class pet là `player/Tamkjll_Pet.java` (325 dòng), **kế thừa thẳng từ `Player`** — tức pet là một
"nhân vật giả" có đầy đủ nPoint, skill, map, zone. Nó có `master` trỏ về chủ. Ngoại hình cố định: đầu 573,
thân 574, chân 575. Id pet là số âm bắt đầu từ -4.000.000 (để phân biệt với người chơi thật).

### 1.2. Điều kiện để pet "sống"

Cả ba nơi mở menu (NPC Đạo Lữ, lệnh `daolu`, NPC Kết Hôn) đều kiểm tra `TamkjllPetGiong != -1`.
Nếu bằng -1 thì báo "Bạn Chưa Có Đạo Lữ" hoặc "Bạn Chưa Kết Hôn Sinh Bé Có Nit Dùng A".
Có ba cách khiến pet biến mất (mất hẳn, không phải tạm thời):

1. **Chết đói**: cứ 15 phút (900.000 ms) server tự trừ 1 điểm thức ăn. Khi về 0, gán
   `TamkjllPetGiong = -1` và báo "Đạo Lữ vì quá đói nên đã bỏ nhà ra đi".
2. **Cho ăn quá no**: ăn lên trên 500 điểm cũng gán `TamkjllPetGiong = -1`, báo
   "Mày Cho ăn No Qua Em Bé Bạo Tử Mà Chết Rồi."
3. **Tạo pet mới trong khi chưa có pet hợp lệ**: không xảy ra vì đã chặn trước.

Tức là người chơi phải giữ thức ăn trong khoảng **1–500**, lý tưởng là trên 10 (dưới 10% sẽ có
thông báo cảnh báo "Thức ăn pet dưới 10%").

### 1.3. Tạo pet — `Player.CreatePet(NamePet)` (dòng 447)

Khi form nhập tên thành công, server gán ngay: thức ăn = 80, cấp = 1, exp = 500,
**linh căn gán ngẫu nhiên `Util.nextInt(0, 9)`** (lưu ý: chỉ 0–9, KHÔNG ra 10 — linh căn
"Hỗn Độn Tiên Linh Căn" chỉ có thể có được bằng cách nào khác, nếu có), và sức mạnh
`Tamkjllnext(10, 100.000.000 + (giong+1) × 100.000.000)` — nghĩa là linh căn càng cao thì mốc
sức mạnh tối đa càng lớn.

### 1.4. Bốn trạng thái AI của pet

Menu ra lệnh đổi trạng thái qua `changeStatus(byte)`:

- **FOLLOW (0) — Đi Theo**: pet bám theo chủ, giữ khoảng cách 60 đơn vị khi di chuyển
  (`followMaster(60)`), nếu đứng yên thì nhấp nháy di chuyển ngẫu nhiên cách 50–80 đơn vị mỗi
  5–8 giây (`moveIdle`).
- **ATTACK_PLAYER (1) — PK Người**: pet tự tìm mục tiêu. Logic `getPlayerAttack()` khá phức tạp:
  ưu tiên mục tiêu chủ chỉ định (`master.TamkjllPlayerAttack`); nếu không có, cứ 40–45 giây quét
  lại "người đang PK trong map" (`zone.PlayerPKinmap()`), chỉ đánh người có `typePk == PK_ALL`
  (đang mở chế độ PK toàn bộ) và đang trong quan hệ PVP hợp lệ. Mỗi lần ra đòn, pet nhích lại gần
  mục tiêu ±60 đơn vị rồi dùng skill số 1.
- **ATTACK_MOB (2) — Pk quái**: tìm con quái sống **gần nhất trong bán kính 1.500** đơn vị
  (`ARANGE_CAN_ATTACK`) và đánh liên tục bằng skill số 1. Nếu map không có quái thì chuyển sang
  trạng thái idle đứng chờ.
- **GOHOME (3) — Về Nhà**: pet bị đưa về map nhà của giới tính chủ (`gender + 21`, tức map 21/22/23
  cho Nam/Nữ/Xen), rồi đi qua lại nhịp nhàng tại tọa độ cố định (map 21: x 200–250, y 336; map 22:
  x 452–500). Ở trạng thái này **pet không buff chỉ số cho chủ nữa** — mọi điều kiện buff trong
  `NPoint` đều kèm điều kiện `getStatus() != GOHOME`.

Ngoài ra pet có "sinh hồi": nếu bị giết, sau 30 giây tự hồi đầy HP/KI (`hsChar`). Nếu chủ chết hoặc
pet đang trúng hiệu ứng khống chế thì pet ngừng hành động. Nếu lệch map với chủ thì tự
`joinMapMaster()` nhảy sang map chủ, tọa độ spawn = vị trí chủ ±10.

### 1.5. Cách cho ăn — ba đường

**a) Qua NPC (chính)**: menu "Cho Ăn 5k Ruby" (mục 0 của NPC Kết Hôn khi chọn Thông Tin Em Bé).
Code thực tế: kiểm tra `SagaTuTien[0] < 5.000.000.000` (5 tỷ Linh Khí Tu Tiên), nếu đủ thì **trừ
5 tỷ linh khí** và cộng ngẫu nhiên 1–20 điểm thức ăn. *Tên menu ghi "5k Ruby" nhưng không đụng
đến hồng ngọc — đây là lỗi đặt tên dễ gây hiểu nhầm cho người chơi.*

**b) Dùng item thức ăn** (`UseItem.java`): item **1663** (thức ăn nhỏ) +1 điểm, item **1664**
(thức ăn lớn) +10 điểm. Cả hai đều trừ 1 cái trong túi.

**c) Tự động tiêu thụ**: không có. Ngược lại mỗi chu kỳ 15 phút pet còn **cộng 50–100 sức mạnh**
nên pet "già" dần theo thời gian chơi.

**Item 1662** là item tăng thẳng 1 cấp Em Be (`EmBeLv++`).

### 1.6. Lên cấp — công thức

Mỗi lần pet ra đòn (trong `SkillService.useSkillAttack`, dòng ~558), server chạy tuần tự:

1. `master.EmBeLv += random(1, 50)` — **cấp tăng trực tiếp mỗi lần đánh, không cần exp**.
2. Nếu chủ có `SagaThienDao >= 700` thì cộng thêm `EmBeEXP += random(1, SagaThienDao / 2)`.
3. Nếu `EmBeEXP >= 3.000.000 + EmBeLv × 1.500.000` thì "thăng cấp thật": `EmBeLv++` và
   `TamkjllPetPower += Tamkjllnext(500.000.000, 500.000.000 + (giong+1) × 100.000.000)`.

Phần trăm hiển thị trong menu cũng chính là công thức này:
`EmBeEXP × 100 / (3.000.000 + EmBeLv × 1.500.000)`.

4. Nếu linh căn là 0 hoặc 10, mỗi lần đánh có **2% tỉ lệ** pet "tìm được đồ cho chủ":
   nhặt `(EmBeLv + 1) × 20` hồng ngọc **hoặc** ngọc xanh (chọn 1 trong 2).

Ngoài ra khi pet đánh quái, `SkillService` dòng 618 còn cộng linh khí Tu Tiên cho chủ theo cấp
Em Be (chi tiết hơn trong mục Tu Tiên).

### 1.7. Quyền lợi chỉ số cho chủ — bảng theo linh căn

Toàn bộ nằm trong `NPoint` (dòng 1076, 1222, 1750, 1812, 2149), luôn kèm 2 điều kiện: pet đang
`!= GOHOME` và linh căn thuộc nhóm:

| Chỉ số | Công thức | Điều kiện linh căn |
|---|---|---|
| HP tối đa | `+ (EmBeLv + 1)%` | 4, 9, 10 |
| KI tối đa | `+ (EmBeLv + 1)%` | 4, 9, 10 |
| Giáp | `× (EmBeLv + 1)%` | 4, 9, 10 |
| Sức đánh | `+ (EmBeLv + 1) × 2%` | 5, 9, 10 |
| Sát thương chí mạng | `+ (EmBeLv + 1) × 2%` | 6, 9, 10 |

Ví dụ pet cấp 1.000 mà linh căn 9 (Ma Linh Căn) thì chủ được +1.001% HP, +1.001% KI, +1.001% giáp,
+2.002% dame, +2.002% chí mạng — **quá mạnh, giải thích vì sao server yêu cầu đủ loại điều kiện
khe khắt trước khi có pet**.

### 1.8. 11 linh căn và 11 kỹ năng (đọc từ `Player.LinhCanEmBe` / `KyNangEmBe`, dòng 1589–1660)

Tên linh căn theo giá trị `TamkjllPetGiong`:

- 0 — Thiên Linh Căn · 1 — Tiên Linh Căn · 2 — Ngũ Hành Linh Căn · 3 — Đơn Linh Căn ·
  4 — Song Linh Căn · 5 — Tam Linh Căn · 6 — Ngũ Linh Căn · 7 — Hỗn Độn Linh Căn ·
  8 — Thánh Linh Căn · 9 — Ma Linh Căn · 10 — Hỗn Độn Tiên Linh Căn · còn lại — "Phế Vật Không Linh Căn".

Kỹ năng tương ứng (trong đó `Lv` = EmBeLv hiện tại):

- **0 (Thiên)**: tìm `(Lv+1) × 20` hồng ngọc + ngọc xanh cho chủ.
- **1 (Tiên)**: chuyển `(Lv+1) × 4` exp **Địa Đạo** cho chủ.
- **2 (Ngũ Hành)**: chuyển `(Lv+1) × 4` exp **Tu Tiên** cho chủ.
- **3 (Đơn)**: chuyển `(Lv+1) × 4` exp **Thiên Đạo** cho chủ.
- **4 (Song)**: cộng `(Lv+1) × 5%` HP, KI, Giáp cho chủ.
- **5 (Tam)**: cộng `(Lv+1) × 3%` Sức đánh cho chủ.
- **6 (Ngũ)**: cộng `(Lv+1) × 3%` SD chí mạng cho chủ.
- **7 (Hỗn Độn)**: tìm `(Lv+1) × 20` HP/KI/Giáp **gốc** và `(Lv+1) × 5` SD gốc cho chủ
  (tức "hồi gốc" — hiếm vì gốc thường chỉ tăng khi chuyển sinh).
- **8 (Thánh)**: chuyển `(Lv+1) × 4` exp Thiên Đạo + Địa Đạo + Tu Tiên cho chủ.
- **9 (Ma)**: cộng `(Lv+1) × 5%` HP/KI/Giáp và `(Lv+1) × 3%` SD + SD chí mạng cho chủ.
- **10 (Hỗn Độn Tiên)**: làm **đủ cả 10 thứ trên** — tìm ngọc, chuyển 3 loại exp, cộng % chỉ số,
  tìm chỉ số gốc. Kỹ năng mạnh nhất nhưng khó có được nhất (không roll ra trong `CreatePet`).

### 1.9. Ba đường mở menu Đạo Lữ

1. **NPC Đạo Lữ** — file `npc/npc_manifest/DaoLu.java` (53 dòng), tempId `ConstNpc.DAO_LU_NE = 88`,
   spawn theo tọa độ NPC trong dữ liệu map (`Map.java:176` đọc mảng `npcId[]/npcX[]/npcY[]`).
   Menu hiện đầy đủ: tên, % lên cấp, cấp, linh hồn, thức ăn, sức mạnh, kèm câu cảnh báo
   "cần 15 phút để load hoặc thoát game ra vào lại" (nghĩa là thay đổi thức ăn/sức mạnh chỉ hiển
   thị lại sau chu kỳ 15 phút hoặc relog).
2. **Lệnh chat `daolu`** — `server/Command.java:349`, nội dung menu y hệt, dùng được ở mọi map.
3. **NPC Kết Hôn → mục "Thông Tin Em Bé"** — menu dài hơn, thêm nút **"Cho Ăn 5k Ruby"**.

Cả ba đều là con menu `ConstNpc.Saga_EmBe = 251018` — một menu, nhiều lối vào.

### 1.10. Lệnh của pet trong menu (NpcFactory case `Saga_EmBe`)

- Mục 0: Cho ăn (trừ 5 tỷ linh khí, +1–20 điểm).
- Mục 1: `changeStatus(FOLLOW)`.
- Mục 2: `changeStatus(ATTACK_PLAYER)` **đồng thời xóa toàn bộ hiệu ứng bất lợi trên pet**
  (`removeSkillEffectWhenDie`) — tức mục này vừa ra lệnh PK người vừa là nút "gỡ debuff".
- Mục 3: `changeStatus(ATTACK_MOB)`.
- Mục 4: `changeStatus(GOHOME)`.
- Mục 5: mở menu con `ConstNpc.huongdan` hiển thị văn bản `KyNangEmBe(giong)` — giải thích kỹ năng.

---

## 2. KẾT HÔN (EventKetHon — NPC tempId 93)

### 2.1. Cửa ải vật liệu

NPC `EventSuKien/EventKetHon.java` (227 dòng) hiện ngay trên đầu menu bảng "điều kiện nhận Nhẫn
kết hôn" kèm số liệu thực tế trong túi người chơi:

- **Ốc Biển** (item 1857): 500 cái.
- **Cua Biển** (item 1858): 500 cái.
- **Sao Biển** (item 1859): 100 cái.
- **Sò Biển** (item 1860): 100 cái.
- **Zenni** (item 457): 99.999 cái (hiển thị trong menu là "Zenni: x… / x99.999").
- **Hồng ngọc** (`inventory.ruby`): hiển thị yêu cầu 9.999, nhưng **điều kiện code là
  `ruby < 9_999_999` (gần 10 triệu)** — menu nói 9.999, code bắt 9.999.999, hai con số lệch nhau
  1000 lần.
- **Bông hồng** (item 723): 99 cái.
- **InGameVIP** (`Saga_VIP`): ≥ 4.
- **Chuyển sinh** (`SagaChuyenSinh`): ≥ 30 lần.
- **Tu Tiên** (`SagaTuTien[1]`): cảnh giới ≥ 90 (hiển thị tên cảnh qua `TamkjllTuviTutien`).
- **Điểm Bản đồ kho báu** (`point_bdkb`): ≥ 10.000.

Thứ tự check trong code: ốc → cua → sao → sò → zenni → hồng ngọc → bông hồng → VIP → chuyển sinh
→ tu tiên → bản đồ kho báu. Check nào fail cũng báo đúng tên vật liệu thiếu rồi `return`.

### 2.2. Khi đạt yêu cầu thì được gì

Trừ toàn bộ: 500 ốc + 500 cua + 100 sao + 100 sò + 99.999 zenni + 99 bông hồng +
`point_bdkb -= 10000` + **`ruby -= 9_999`** (chỉ 9.999 hồng ngọc, bất kể check gần 10 triệu).

Sinh ra **Nhẫn kết hôn (item 1213)** với **10 option ngẫu nhiên**:

1. option 67–71 random (một trong 5 loại công/độ bền ngẫu nhiên).
2. option 59 param 1–2.
3. option 50 param 1–200.
4. option 77 param 1–200.
5. option 103 param 1–200.
6. option 60 param 1–2.
7. option 249 param 1.
8. option 61 param 1–2.
9. option 251 param 1.
10. option 231 param 0.

Tức mỗi chiếc nhẫn là một "vật phẩm rolled" — hai người cùng điều kiện nhưng nhẫn khác nhau.

### 2.3. Mục 0 "Nhận Quần Đi Biển" — check ảo, tặng free

Check duy nhất là `player.Saga_VIP < 0` — trong game `Saga_VIP` không bao giờ âm, nên **ai cũng
qua**. Trừ 1 Zenni, tặng **Nhẫn cầu hôn (item 691)** với 1 option 67. Mục này thực chất là nơi
lấy nhẫn cầu hôn, không phải nhẫn kết hôn.

### 2.4. Cách cầu hôn

Cầu hôn không đi qua NPC Kết Hôn mà qua **hệ thống menu phụ trên người chơi khác**
(`SubMenuService.controller`, case `KET_HON`): nhấp vào người chơi → chọn kết hôn. Điều kiện:

- Người cầu hôn phải **đang mang cải trang item 1046 "Nữ Thần"** ở ô trang phục thứ 5
  (`itemsBody.get(5)` + `findItemBody(1046)`). Thiếu là báo "Yêu cầu mang cải trang Nữ Thần".
- **Người được cầu không được có `duockethon >= 10`** — bị chặn ở mốc 10 lần ("đã đạt tối đa 10
  lần nhận được kết hôn").
- **Người cầu không được có `dakethon >= 20`** — bị chặn ở mốc 20 lần cầu hôn.

Nếu qua hết, mở menu xác nhận `ConstNpc.KETHON_PLAYER = 838`: "Bạn có chắc chắn muốn Kết hôn
với [tên]" → Đồng ý / Hủy. Khi xác nhận (`NpcFactory:4811`):

- Kiếm tra có **Nhẫn 1213** trong túi (chính là chiếc nhẫn làm ở mục 1).
- `player.dakethon++`, đối phương `duockethon++`.
- Tiêu thụ 1 chiếc nhẫn 1213.
- Báo riêng từng người **và broadcast toàn server**
  "Bạn [tên] đã Kết hôn Với [tên] thành công".

### 2.5. Quyền lợi kết hôn (NPoint dòng 1083–1087, 1376–1380, 1757–1761)

**Mỗi lần kết hôn (`dakethon`)**, cộng dồn vào chỉ số gốc của người cầu hôn:

- HP: `hpMax × dakethon × 10000 / 100` = **+100% HP mỗi lần kết hôn** (rate mặc định `dakethon_hp = 10000`).
- KI: rate `dakethon_ki = 1000` → **+10% KI mỗi lần**.
- Sức đánh: rate `dakethon_dame = 1000` → **+10% dame mỗi lần**.

**Mỗi lần được cầu hôn (`duockethon`)**, cộng vào người được cầu:

- HP: rate `duockethon_hp = 1000` → **+10% mỗi lần**.
- KI: rate `duockethon_ki = 100` → **+1% mỗi lần**.
- Dame: rate `duockethon_dame = 100` → **+1% mỗi lần**.

Tất cả đều đọc từ `rate(key, mặc định)` nên admin **chỉnh được trong config** mà không phải sửa code.

Trong khi đó menu "Thông tin Kết hôn" (mục 2) hiển thị **khác hẳn**: "+[10000 × dakethon]% Chỉ số
HP,KI,SD" và bảng ẩn `duockethon` 0→20 correspond 0→200%. So với code thì text menu phóng đại
đối với `dakethon` (menu nói 10.000%/lần, code tính 100%/lần) — người chơi đọc menu sẽ tưởng
mạnh hơn thực tế.

### 2.6. Giới hạn tổng

- Một người tối đa **20 lần cầu hôn** và **10 lần bị cầu hôn** → tổng cộng không ai lấy được
  lợi ích "duockethon" quá mốc 20 (menu hiển thị trần 200% HP).
- `dakethon` không bị chặn trần trong code ngoài mốc 20 cho phép cầu lần nữa (check là
  `dakethon >= 20` chặn, nghĩa là đủ 20 lần là hết cầu được).

---

## 3. SINH EM BÉ

### 3.1. Điều kiện và thực hiện

Menu "Sinh Em Bé" (mục 3 của NPC Kết Hôn) chỉ mở khi **`dakethon >= 1`** — đã cầu hôn ít nhất một
lần thành công. Nếu chưa, báo tục: "✎Mày Đã Kết Hôn Đâu -Chích Cây Chuối À".

Điều kiện mở → menu `ConstNpc.EMbe = 839` với 2 lựa chọn: "Khởi Tạo Em Bé" / "Đóng".
Chọn "Khởi Tạo" → `NpcFactory:4840 case ConstNpc.EMbe → Input.gI().TAOPET(player)`.

### 3.2. Form nhập tên (`Input.TAOPET`, dòng 842)

Form có tiêu đề "Sau khi Chịch Chịch Có Con Hãy Đặt Tên Nó Đi", một ô nhập "Tên Em Bé", type
`TAO_PET`. Xử lý ở `Input.doInput` case `TAOPET` (dòng 97), theo thứ tự:

1. **Đã có pet rồi?** → báo "Đẻ Lắm Thế!" và dừng. (Một lần một con, không nuôi song song.)
2. **Có item 457 trong túi không?** — item 457 trong hệ thống này vừa là "Zenni/thỏi vàng" dùng
   trong điều kiện kết hôn, vừa được code coi là "trứng pet" (`Item trung = findItem(..., 457)`).
   Thiếu thì báo "Cần có trứng pet".
3. **Độ dài tên 4–20 ký tự** — dưới 4 hoặc trên 20 báo lỗi; code comment "cho phép kí tự đặt biệt".
4. Gán `TamkjllPetGiong = -1` trước (phá pet cũ nếu có), dispose pet cũ nếu tồn tại, rồi gọi
   `CreatePet(NamePet)` — trả về một pet **ngẫu nhiên linh căn 0–9**.
5. **Trừ item: `subQuantityItem(bag, trung, 0)` — trừ 0 cái.** Nghĩa là chỉ cần *sở hữu* trứng
   457 trong túi là sinh được, **không mất trứng**. Đây gần như chắc chắn là bug (lẽ ra phải trừ 1).

Sinh xong báo "Bạn đã thu nhận Pet: [tên]".

### 3.3. Thông Tin Em Bé (mục 4) — khác gì Thông Tin Đạo Lữ

Menu giống hệt menu Đạo Lữ về dữ liệu, nhưng có **2 khác biệt**:

1. Thêm nút **"Cho Ăn 5k Ruby"** (nút 0), các nút lệnh shift lên là 1–5.
2. **Ô phần trăm cấp bị sai công thức**: mục này dùng `EmBeLv × 100 / (3.000.000 + EmBeLv × 1.500.000)`
   (dùng `EmBeLv` thay vì `EmBeEXP`) trong khi NPC Đạo Lữ dùng `EmBeEXP × 100 / ...` (đúng).
   Hệ quả: menu Em Bé hiển thị % cấp gần như luôn ≈ 0% (hoặc con số vô nghĩa theo cấp), còn menu
   Đạo Lữ hiển thị đúng tiến độ. **Đây là bug hiển thị có thật, nên sửa**:
   `EventKetHon.java` case 4, dòng hiển thị "LV Em Bé: (…%)".

### 3.4. Luồng chơi đầy đủ

Nhẫn 1213 (làm từ NPC Kết Hôn, tốn 10 loại điều kiện) → đeo cải trang Nữ Thần → cầu hôn qua menu
người chơi → đối phương đồng ý (tiêu nhẫn 1213, `dakethon++`/`duockethon++`) → mở menu Sinh Em Bé
→ đặt tên (cần có trứng 457 trong túi, không mất) → nuôi bằng thức ăn 1663/1664 hoặc 5 tỷ linh khí
→ pet đánh quái để lên cấp → hưởng % chỉ số theo linh căn.

---

## 4. CHUYỂN SINH

Có **hai loại chuyển sinh độc lập**: chuyển sinh nhân vật và chuyển sinh đệ tử (pet cổ điển), cùng nằm
trong NPC `ConstNpc.SagaChuyenSinh`, menu case 0 và case 1 (`NpcFactory` dòng ~860–985).

### 4.1. Chuyển sinh nhân vật (case 0)

Điều kiện, theo đúng thứ tự code kiểm tra:

1. **`SagaTuTien[1] >= cs_tutien_yeucau`** — cảnh giới Tu Tiên phải đạt (thông báo: "Đã Đạt Trúc
   Cơ Đỉnh Phong Để Mở Khóa Cs." → ngưỡng thực tế là cảnh 19 Trúc Cơ đỉnh phong, tức mốc bắt đầu
   Trúc Cơ Tầng 1 là 10, nhưng text nói Trúc Cơ Đỉnh Phong = 19).
2. Kiểm tra `session.vnd >= coin` (coin = 0 → luôn qua, code cũ còn sót).
3. **Lượng Bạc (item 1271) ≥ cs_luong_bac** — thông báo nói "ít nhất 50000 Lượng Bạc".
4. **Sức mạnh ≥ 150 nghìn tỷ** (`cs_sm_yeucau`, thông báo ghi "150k Tỷ").
5. **Vàng ≥ cs_vang**.
6. Trừ: `PlayerDAO.subcash(coin)` (0), trừ lương bạc, trừ vàng.
7. Tỉ lệ thành công `cs_tile_thanh_cong` (%).

**Thành công**: reset `nPoint.power = 0`, `tiemNang = 0`, `SagaChuyenSinh++`, cộng **gốc**:
`hpg += cs_hp_goc`, `mpg += cs_ki_goc`, `dameg += cs_sd_goc`, `defg += cs_giap_goc`.
Thông báo nói phần thưởng là: **500k HP/KI gốc, 50k dame gốc, 1000 giáp gốc, và
"+52% HP/KI/SD vào Hiệu Ứng Thiên Đạo / 1 Cấp"**.

**Thất bại**: vẫn reset sức mạnh và tiềm năng về 0 (mất sạch, không cộng gì) — cái giá rất nặng.

### 4.2. Chuyển sinh đệ tử (case 1)

Điều kiện:

1. Phải có `player.pet` (đệ cổ điển).
2. Lượng Bạc ≥ `cs_luong_bac` (50.000).
3. Tu Tiên ≥ `cspet_tutien_yeucau` — thông báo "Đã Đạt Chuẩn Đế Cảnh" (mốc trên của Trúc Cơ/Kim Đan).
4. **Điểm Fam ≥ `cspet_diem_fam` + (SagaChuyenSinh × 2)** — càng chuyển sinh nhiều càng đắt thêm.
5. **Sức mạnh ≥ `Long.MAX_VALUE`** — điều kiện bất khả thi về mặt logic:
   `player.nPoint.power < Long.MAX_VALUE`almost always true → **chặn mọi người**, thông báo
   "Bạn cần: 9 Tỷ Tỷ Sức Mạnh Để Chuyển Sinh". Đây là mốc 9 tỷ tỷ (một con số vượt kiểu long),
   nhưng vì so sánh `< Long.MAX_VALUE` nên practically không bao giờ qua unless power chạm chính
   giới hạn long. *Cần admin xác nhận lại — có thể intended là `power < 9e18`.*
6. Vàng ≥ `cspet_vang`.
7. Tỉ lệ: `cspet_tile_thanh_cong` (%) + (nếu `SagaThienDao >= 5000` thì cộng thêm
   `cspet_bonus_thiendo`%), chấm với mẫu số 50 (`Util.isTrue(tỷ lệ, 50)` — chú ý tham số 50 chứ
   không phải 100, nên tỉ lệ thực = tỷ_le/50?? — `Util.isTrue(float, int max)` thường là
   `nextInt(1, max) <= value`, nếu value tính theo % 100 mà max 50 thì tỉ lệ bị nhân ~2).

**Thành công**: `pet.nPoint.power = 0`, `tiemNang = 0`, **`pet.SagaChuyenSinh++`**, cộng gốc
`cspet_hp_goc`, `cspet_ki_goc`, `cspet_sd_goc`, `cspet_giap_goc` cho đệ. Thông báo: "50k Hp Ki gốc,
1k dame gốc, 100 giáp gốc" (đúng bằng 1/10 phần thưởng của chuyển sinh người: 50k vs 500k HP/KI, 1k vs 50k dame, 100 vs 1000 giáp).
**Thất bại**: reset **sức mạnh của người chơi** (không phải đệ) về 0.

### 4.3. Quyền lợi chuyển sinh

- Chuyển sinh cộng thẳng % trong `NPoint` dòng 1809: `def ×= (SagaChuyenSinh × 2)%` cho giáp.
- Menu hiệu ứng (case `SagaHieuUng`) hiển thị: "Chuyển sinh +HP,KI,SD,Giáp: `SagaChuyenSinh × 30`% /Cấp".
- Tích lũy qua **item**: `UseItem.java:1257` có `SagaChuyenSinh++` khi dùng item đặc biệt (nhiều
  khả năng là item event/nạp — cần check id item cụ thể nếu muốn khai thác).
- Chuyển sinh cũng là điều kiện kết hôn (≥ 30) và có TOP riêng (`TopChuyenSinh`).

### 4.4. Cách khác để lên SagaChuyenSinh

`NpcFactory:899` (chuyển sinh người), `:967` (chuyển sinh đệ), `UseItem:1257` (dùng item).
Không có cách nào khác — không rơi từ boss, không mua shop.

---

## 5. TU TIÊN

### 5.1. Ba cột dữ liệu

`SagaTuTien[0]` = **Linh Khí** (exp), `SagaTuTien[1]` = **Cảnh Giới**, `SagaTuTien[2]` = **Thiên Phú**.
Lưu ý `NDVSqlFetcher:379` parse `[0]` qua `Double` — con số linh khí rất lớn (tỷ, nghìn tỷ).

### 5.2. Nhận linh khí

- **Tự động theo thời gian**: trong vòng lặp update của nhân vật (`Player.java:416`), mỗi giây
  `SagaTuTien[0] += tutien_linh_khi_moi_giay` (config panel). Đây là nguồn AFK.
- **Đánh quái rơi vật phẩm** (`InventoryService:904`): khi nhặt item có exp, nhân thêm
  `exptt = quantity × random(100, 1000)`, cộng thêm 50% × thiên phú (tức `50 × SagaTuTien[2]/100`),
  cộng thêm 5% nếu `SagaThienDao >= 300`. **Nếu thiên phú = 0 → nhân vật bị `setDie()` và báo
  "1 Phàm Nhân Kiến Hôi… Vui lòng Tẩy Thiên Phú Để Được Tu Luyện"** — tức không tẩy thiên phú thì
  không nhặt được linh khí.
- **Hiệu ứng Tiên Bang** (`SkillService:1440`): random nhận thêm exp tu tiên/nhập ma/tiên bang.
- **Pet Em Bé** (`SkillService:618`): pet đánh quái chuyển linh khí cho chủ theo cấp Em Be.

### 5.3. Cảnh giới — 341 cấp

`TamkjllTuviTutien(int)` liệt kê tên cảnh từ 0 đến 340, chia blockSize 10:

- 0–9: **Luyện Khí** Tầng 1 → đỉnh phong
- 10–19: **Trúc Cơ** Tầng 1 → đỉnh phong
- 20–29: **Kim Đan** …
- và tiếp tục cho đến mốc 340 (mỗi đại cảnh 10 tầng nhỏ, tên đại cảnh tăng dần — xem toàn bộ
  trong `Player.java:1751+`).

Mốc quan trọng: **cảnh 10 (Trúc Cơ)** mở khóa Tẩy Luyện, **cảnh 19** đủ điều kiện chuyển sinh,
**cảnh 90** đủ điều kiện kết hôn.

### 5.4. Độ Kiếp — lên cảnh giới (NPC Tu Tiên, case 2)

- Tốn **50M linh khí** (`tutien_chi_phi_do_kiep`) mỗi lần.
- Cảnh giới tối đa: `tutien_canh_gioi_toi_da` (config).
- **Tỉ lệ gốc theo cảnh hiện tại** (`bemeocanhgioi`, `Player.java:2509`):
  - cảnh 0–4: 10% · 5–8: 7% · 9 (đỉnh Luyện Khí): 3%
  - 10–13 (Trúc Cơ): 8% · 14–18: 2% · 19 (đỉnh Trúc Cơ): 10%
  - 20–28 (Kim Đan): 15% · 29: 2% · 30–38: 14% · 39: 5% …
  - Pattern: **đỉnh mỗi đại cảnh rất khó (2–5%), tầng giữa dễ hơn (10–15%)**.
- **Thần Khí cộng tỉ lệ**: nếu trong túi có 1 trong 6 item thần khí
  `{1759, 1760, 1761, 1762, 1763, 1854}` với tỉ lệ `{3, 5, 7, 13, 15, 35}%`, lấy cái tốt nhất cộng
  vào tỉ lệ gốc (trần 100%). Thần khí 1854 mạnh nhất (+35%) và bị tiêu thụ 1 cái mỗi lần độ kiếp.
- Thành công: `SagaTuTien[1]++`, **`tho_nguyen += 259.200`** (thọ nguyên +3 ngày tính theo giây),
  broadcast toàn server.
- Thất bại: mất 50M linh khí + 1 thần khí (nếu có), giữ nguyên cảnh giới.

### 5.5. Tẩy Luyện Thiên Phú (case 0)

- Thiên phú hiện tại `SagaTuTien[2]`, hiển thị qua `player.linhcan(...)` (tên linh căn kiểu tu tiên).
- **Nếu thiên phú = 0** (hết thọ nguyên lần trước): lần tẩy **MIỄN PHÍ** và gán ngẫu nhiên
  `random(1, random(1, random(1, 2)))` — vẫn chỉ ra 1 hoặc 2, bias về 1.
- Điều kiện bình thường: linh khí ≥ `tutien_chi_phi_tay_luyen` (text nói 500M), cảnh giới ≥ 10,
  thiên phú < 29 (trần).
- Trừ linh khí, **tỉ lệ thành công chỉ 3%** (`Util.isTrue(3f, 100)`).
- Thành công: `SagaTuTien[2]++`, broadcast toàn server "Đã Tẩy Luyện Thiên Phú Từ X lên: Y".
- Thiên phú ảnh hưởng: tỉ lệ nhận linh khí khi nhặt đồ (`50 × thiên phú%`), và các tỉ lệ khác
  theo `linhcan()`.

### 5.6. Thọ Nguyên — cơ chế "chết vì hết tuổi"

- `player.tho_nguyen` (dòng 282) là mốc thời gian millis kết thúc, **giảm mỗi giây** theo config
  `tutien_tho_nguyen_moi_giay` (dòng 415).
- Khi về **0**: toàn bộ reset — `SagaTuTien[0] = [1] = [2] = 0` (mất sạch linh khí, cảnh giới,
  thiên phú), thông báo "Thọ Nguyên đã hết! Bạn Bị Phế Bỏ 100 Triệu Năm Tu Vi / Phế Bỏ Linh Căn
  Thành Phàm Nhân", và **`break` khỏi vòng loop update** — nhân vật ngừng update hẳn (chết đứng /
 disconnect cho tới lần vào lại — cần kiểm chứng hành vi thực tế).
- Code cũ (lock account, kick session) đã bị comment, bản hiện tại chỉ reset và dừng update.
- Độ kiếp cộng thọ nguyên, nên vòng chơi là: tích thọ nguyên bằng cách độ kiếp thành công.

### 5.7. Quyền lợi cảnh giới

Menu case 1 hiển thị:

- Sinh Lực/KMana/Giáp tăng: `TamkjllHpKiGiaptutien(cảnh)` = **cảnh × 356%** (dòng 2706, nhận cảnh 0–340).
- Dame tăng: `cảnh × 287%`.
- Tấn Công +: `cảnh × 1.000.000`.
- Sát Thương Liên Hoàn / Kaioken / Biến Khỉ: `lamchodep(cảnh)` = `cảnh × 1.000.000` (%) — con số
  này code ghi ×1.000.000 % là **quá lớn, có thể là bug hiển thị hoặc đơn vị lạ** (cảnh 90 →
  90.000.000% SD — cần admin kiểm chứng NPoint có dùng đúng chỗ này không).

### 5.8. Vị trí NPC

Chọn case 3 → `changeMapBySpaceShip(player, 174, -1, 408)` — bay đến map 174 (map tu tiên).

---

## 6. THIÊN ĐẠO

`SagaThienDao` (nguyên tắc: int, tăng theo cấp). Nguồn nhận:

- **Nạp tiền**: `PlayerDAO.subcash` — cứ nạp 10.000 VND → `SagaThienDao += 1`,
  đồng thời `SagaDiaDao += num/5000`, `SagaThienDao += num/10000`,
  `Saga_VIP += num/vip_nap_moi`, `point_PassFree += (num/1000) × 100`.
  Tức **đại gia nạp = nguồn nhanh nhất của cả Thiên Đạo lẫn Địa Đạo**.
- **NPC**: các mức `+99` (dòng 3760), `+250` (3792), `+600` (3824) cho Thiên Đạo.
- **Rương Ghi Danh**: rương 7–11 nhận cấp Thiên Đạo.

### Hiệu ứng theo mốc (menu case `SagaHieuUng`, NpcFactory 700–790)

- **Cơ bản (có 1 cấp)**: HP/KI/SD +`SagaThienDao`% /cấp. Chuyển sinh: +`SagaChuyenSinh × 30`%
  HP/KI/SD/Giáp mỗi cấp chuyển sinh.
- **Từ cấp 30 cộng dồn** (phân theo giới):
  - Giới 0 (Nam): +Kaioken `TD`% SD, +QCKK `TD`% tung ra, +DCTT đòn kế `TD`%, +
    thời gian choáng `TD/90` giây, +thời gian thôi miên `TD/100` giây, +khiên năng lượng `TD/50` giây.
  - Giới 1 (Nữ): +Liên hoàn `TD`% SD, +LaZe `TD×28`% SD, +SD chim `TD`%,
    +STCM liên hoàn `TD`%.
  - Giới 2 (Xen): +Khỉ `TD×30`% HP và `TD`% SD, +Nổ bom `TD×65`% ST, +thời gian Khỉ `TD/30` giây,
    +giáp `TD/30`% HP khi Khỉ, +choáng `TD/50`% khi Trói, +HP `TD/90`% khi Huýt Sáo.
- **Phụ từ cấp 30**: tăng exp Địa Đạo theo Thiên Đạo, tăng tỉ lệ chuyển sinh đệ, tăng rơi
  ngọc/vàng, **Dame chuẩn lên boss `TD`%**, tăng tiến độ Tu Tiên.
- **Trên cấp 100**: đánh boss +dame `TamkjllCapPb × 106`%, tăng tỉ lệ exp Thiên Đạo, khiên
  năng lượng `TD/90` giây, giới-specific giảm hồi chiêu (Nữ: giảm nửa hồi liên hoàn, +chí mạng
  liên hoàn `TD`%; Xen: +thời gian Khỉ `TD/85`, giáp `TD/60`% HP).
- **Trên cấp 300**: tăng tỉ lệ up Nhập Ma theo Tiên Bang, vàng/Exp Khai Thác map núi khỉ vàng,
  tăng exp Tu Tiên nhận được.
- **Trên cấp 500**: tỉ lệ đổi đệ có skill 1 là Liên Hoàn, tăng tốc độ up đệ,
  +`TD/15`% HP, `TD/10`% MP, `TD/25`% dame, `TD/15`% giáp cho đệ, +`TD/20`% dame chí mạng cho đệ,
  tăng tỉ lệ chuyển sinh đệ. (Đồng thời `NPoint`: nếu pet(`isPet`) mà master Thiên Đạo ≥
  `thiendao_moc_500` thì pet được +`TD/10`% HP, `TD/5`% MP, `TD/15`% dame, `TD/20`% dameAttack.)
- **Trên cấp 700**: giảm 90% thời gian chiêu đấm (trừ Liên hoàn), giảm `TD/130`% HP "bịp",
  tăng tốc độ trưởng thành Chiến Thần. (Mốc 700 cũng là lúc **Em Bé nhận exp** — mục 1.6.)

Nguồn khác: `NPC GhiDanh` rương, `vaymuon` (Độ Kiếp Thiên Đạo — code đang comment),
`thiendao_roi_cs_goc_moi_cap` (tỉ lệ rơi CS gốc mỗi cấp Thiên Đạo).

---

## 7. ĐỊA ĐẠO

`SagaDiaDao`. Nguồn: nạp VND (`num/5000`), NPC `+500` (3764), `+1200` (3796), `+3000` (3828),
rương Ghi Danh 1–6.

### Hiệu ứng (menu `SagaHieuUngdia`)

- **Cơ bản**: **+`SagaDiaDao × 1.000.000` Sức Đánh** (500k/cấp — hiển thị ra con số tổng),
  +`SagaDiaDao × 42.000` SD cho đệ tử, và nếu ≥1: HP/KI +`DD`%/cấp.
- **Từ cấp 30** (phân giới):
  - Giới 0: Kamejoko Bộc Phá `DD`% SD, Kamejoko Super `DD×280`% SD.
  - Giới 1: Mesenco Siêu Max `DD×80`% SD, Ma Phong Ba Pro `DD×580`% SD.
  - Giới 2: Antomic Xuyên Thấu `DD×30`% SD, Liên Hoàn Chưởng `DD×500`% SD.
- **Phụ từ cấp 30**: tăng tỉ lệ may mắn, tỉ lệ kiếm tiền, tỉ lệ win Tài Xỉu,
  "Tăng Độ Nói Phét Trong Game", "Tăng Tiến Độ Nạp Game Cho ad" (các dòng này là text vui của dev,
  không có code thật đằng sau — kiểm tra không thấy effectService tương ứng).

---

## 8. CÂY PHÉP (Đậu Thần)

File `npc/specialnpc/MagicTree.java` (312 dòng). Tương tác qua NPC `ConstNpc.DAU_THAN`,
giao tiếp bằng gói tin **-34** (không phải menu NPC thường).

### 8.1. Thông số cốt lõi

- **Cấp tối đa: 10** (`MAX_LEVEL = 10`).
- **Loại hạt (đậu) theo cấp**: item `{13, 60, 61, 62, 63, 64, 65, 352, 523, 595}` với option param
  `{100, 500, 2, 4, 8, 16, 32, 64, 128, 256}` (đậu cấp 1–2 là option 48/param 100–500 kiểu %,
  cấp ≥3 dùng option 2 param 2→256).
- **Số đậu tối đa trên cây**: `maxPea = (level − 1) × 2 + 5` → cấp 1: 5, cấp 10: 23.
- **Thời gian kết 1 hạt**: `level × 60` giây → cấp 1: 60 giây/hạt, cấp 10: 600 giây/hạt
  (cấp cao kết chậm hơn nhưng nhiều ô hơn).
- **Số ô đậu mỗi cấp** (`POS_PEAS`): 5, 7, 9, 11, 13, 15, 17, 20, 24, 27 ô.
- Icon cây theo **giới tính × cấp** (`ID_MAGIC_TREE`), vị trí cây trong nhà
  `{348, 336}` (nam/nữ) và `{372, 336}` (giới 2).

### 8.2. Vòng chơi

1. **Kết hạt tự động**: hệ thống tự cộng đậu theo thời gian (`update()`): đếm số giây trôi qua /
   `secondPerPea`, khi đủ thì `currPeas++` cho tới max. Không cần online đúng lúc — tính bù.
2. **Thu hoạch** (`harvestPea`): đổi số đậu tích trên cây thành item vào túi (nếu túi đầy thì
   đưa vào rương `addItemBox`). Thông báo "Bạn vừa thu hoạch được X hạt [tên]" nếu bị tràn.
3. **Nâng cấp** (`upgradeMagicTree`): tốn **vàng** theo bảng `PEA_UPGRADE`:

| Từ cấp | Thời gian nâng | Vàng (cấp ≤3: nghìn, >3: triệu) |
|---|---|---|
| 1→2 | 10 phút | 5k |
| 2→3 | 1 giờ 40 phút | 10k |
| 3→4 | 16 giờ 40 phút | 100k |
| 4→5 | 6 ngày 22 giờ | 1 triệu |
| 5→6 | 13 ngày 21 giờ | 10 triệu |
| 6→7 | 27 ngày 18 giờ | 20 triệu |
| 7→8 | 55 ngày 13 giờ | 50 triệu |
| 8→9 | 69 ngày 10 giờ | 100 triệu |
| 9→10 | 104 ngày 4 giờ | 300 triệu |

   (Thời gian tính bằng `days × 24h × 60m` — cấp cuối **hơn 3 tháng** nếu chờ thật.)
4. **Hủy nâng cấp** (`unupgradeMagicTree`): **hoàn nửa vàng**, cây giữ nguyên cấp cũ.
5. **Kết hạt nhanh**: 4 ngọc (`"Kết hạt nhanh 4 ngọc"` trong menu) → `currPeas = maxPea` ngay.
6. **Nâng cấp nhanh**: 9 ngọc → `level++` ngay, `isUpgrade = false`.

### 8.3. Lưu trữ

Cột `data_magic_tree` dạng `[level, currPeas, isUpgrade, lastTimeHarvest, lastTimeUpgrade]`,
nạp khi vào game (`Player.magicTree`), update mỗi frame trong `Player.update()` (dòng ~465).
Quá trình nâng cấp đang chạy mà tắt game vẫn tiếp tục đếm (lưu `lastTimeUpgrade`).

---

## 9. CUNG MỆNH

File `services/CungMenhService.java` (457 dòng), NPC `npc/npc_manifest/BoMong.java`.

### 9.1. Config mặc định (`Config`, dòng 42)

- `maxLevel = 120` (cấp tối đa).
- Bonus mỗi cấp: `hpFlat = 2000`, `kiFlat = 2000`, `dameFlat = 200` (cộng thẳng) +
  `hpPercent/kiPercent/damePercent` (mặc định 0, chỉnh qua panel).
- **Đột phá cứ `dotPhaEvery = 10` cấp/lần**.
- Vật tư nâng cấp: `manhBase = 2`, `manhStep = 2`, `manhExtra = 1` (Mảnh Tinh Tú tăng dần 2, 4, 6…).
- Vật tư đột phá: `dotPhaManhBase = 15` + `dotPhaManhStep = 5`, `dotPhaNgocBase = 1` + `dotPhaNgocStep = 1`.

Tất cả chỉnh được qua **panel admin → màn CUNGMENH** (`saveCungMenhConfig`), bậc option qua
`saveCungMenhBac`, xóa bậc qua `deleteCungMenhBac`.

### 9.2. Vật tư

- **Mảnh Tinh Tú** — item `ConstItem.MANH_TINH_TU`.
- **Ngọc Tinh Đồ** — item `ConstItem.NGOC_TINH_DO` (chỉ dùng khi đột phá).

### 9.3. Quy tắc nâng cấp

- Nâng cấp thường: trả Mảnh theo cấp sắp đạt (`manhForLevel(lv+1)`).
- **Điều kiện đột phá**: `canNangCap` = `lv % dotPhaEvery != 0 || cungMenhDotPha >= lv / every`
  — tức cứ chạm bội số 10 (10, 20, 30…) thì **bắt buộc đột phá trước** bằng Mảnh + Ngọc.
- Khi đột phá không đủ vật tư, menu hiện đúng số còn thiếu: "Cần X Mảnh + Y Ngọc Tinh Đồ".

### 9.4. Quyền lợi

`applyBonus` (dòng 220) cộng vào nPoint **mỗi lần tính lại chỉ số** (read-only, không lưu):

- `tlHp += hpPercent × lv`, `tlMp += kiPercent × lv`, `tlDame += damePercent × lv` (% cộng dồn).
- `hpAdd += hpFlat × lv`, `mpAdd += kiFlat × lv`, `dameAdd += dameFlat × lv` (trắng cộng dồn).
- **Cộng thêm option bậc** (`Bac`): mọi bậc có `level <= cungMenhLevel` được cộng
  `optionId/param` — đây là hệ "bậc" kiểu option item, admin cấu hình riêng cho từng cấp.

### 9.5. Lưu trữ

Cột `cungMenhLevel`, `cungMenhDotPha` trên `player`; bảng config + bảng `bacs` trong DB
(có `NDVSqlFetcher:495 migrateOldBonus` cho dữ liệu cũ). Đọc/ghi qua `PlayerDAO.saveCungMenh`.

---

## 10. ĐỆ TỬ (Pet cổ điển — hệ thống riêng, khác Đạo Lữ)

### 10.1. Danh sách loại đệ

File `services/PetService.java` (1.252 dòng), mỗi loại có 2 hàm `createXxxPet(player[, gender],
limitPower...)`:

- `createNormalPet` — đệ thường (dùng trong MENU_ADMIN case 1: `PetService.gI().createNormalPet(player, player.gender)`).
- `createMabuPet`, `createBeerusPet`, `createPicPet`, `createBlackPet`, `createxencon`,
  `createbroly`.
- Nhóm "đệ tử Black/Zamasu/Cucumber/Kid": `createdetublack`, `createdetublackrose`,
  `createdetuzamasu`, `createdetucumber`, `createdetukidbu`, `createdetukidfide`,
  `createdetukiduub`, `createdetukidxen`.
- Nhóm Gohan: `createdetuGohan1` … `createdetuGohan5`.

Mỗi hàm set head/body/leg, skill, option trang bị cho pet (đệ mang đồ được). Pet được tạo qua
NPC (menu admin "BUFF_PET" trong `SubMenuService` cũng có case xác nhận "phát đệ tử cho [tên]") và
qua các NPC chức năng riêng.

### 10.2. Hành vi

Đệ được lưu trong `player.pet`, update trong `Player.update()` (dòng ~485 `pet.update()`),
**có nPoint riêng** — nên chỉ số đệ độc lập với chủ. Đệ cũng xem thông tin được qua NPC
`Checkthongtin` case 1 ("Thông Tin Đệ Tử").

### 10.3. Chuyển sinh đệ

Đã mô tả ở mục 4.2 — `pet.SagaChuyenSinh++`, đệ có cột chuyển sinh riêng.

### 10.4. Phân biệt với Đạo Lữ

| | Đệ tử (Pet) | Đạo Lữ/Em Bé (Tamkjll_Pet) |
|---|---|---|
| Class | `Pet` (services/PetService) | `Tamkjll_Pet extends Player` |
| Tạo | NPC buff / menu admin | Kết hôn → sinh con |
| Trang bị | Mang đồ riêng | Không mang đồ |
| Buff chủ | Không (chỉ phụ thuộc Thiên Đạo khi pet buff) | Có, theo linh căn |
| Chuyển sinh | Có riêng | Không |
| Mất | Không mất | Mất khi đói/no |

---

## 11. TỔNG HỢP BUG / ĐIỂM BẤT THƯỜNG CẦN ADMIN QUYẾT ĐỊNH

1. **Hồng ngọc kết hôn**: menu ghi yêu cầu 9.999, code check `ruby < 9_999_999` (gần 10 triệu),
   nhưng chỉ **trừ 9.999**. Người chơi có 10 triệu ruby qua check, mất 9.999.
2. **Trứng sinh con không mất**: `subQuantityItem(..., trung, 0)` — trừ 0, chỉ cần *có* item 457.
3. **Menu "Cho Ăn 5k Ruby"** thực ra trừ **5 tỷ Linh Khí Tu Tiên**, không đụng ruby.
4. **% cấp trong menu "Thông Tin Em Bé"** dùng `EmBeLv` thay vì `EmBeEXP`
   (`EventKetHon.java` case 4) — hiển thị sai; menu NPC Đạo Lữ dùng đúng.
5. **Text menu kết hôn phóng đại**: hiển thị "+10.000% mỗi lần kết hôn" nhưng code chỉ +100% HP
   (rate 10000/100), +10% KI, +10% dame.
6. **Chuyển sinh đệ**: `player.nPoint.power < Long.MAX_VALUE` — so sánh với Long.MAX_VALUE,
   practically luôn true → chặn; thông báo nói "9 Tỷ Tỷ". Ngoài ra failure case reset sức mạnh
   **người chơi** chứ không phải đệ.
7. **Tỉ lệ chuyển sinh đệ** dùng `Util.isTrue(tỷ_le, 50)` — mẫu số 50 trong khi % thường tính
   trên 100 → tỉ lệ thật gấp đôi cấu hình (nếu `Util.isTrue` là `random(1,max) <= value`).
8. **`CreatePet` chỉ roll linh căn 0–9** — linh căn 10 (Hỗn Độn Tiên, kỹ năng mạnh nhất) không
   bao giờ ra tự nhiên.
9. **Menu "Thông tin kết hôn"** hiển thị bảng `duockethon` 11–20 đều ×10% nhưng text gộp là 200%
   — ok, nhưng cách viết (mỗi nhánh if) dễ sai nếu mở rộng quá 20.
10. **Linh khí `SagaTuTien[0]` parse qua Double** — mất precision trên ~2^53 (9 quadrillion),
    unlikely reached but worth noting for extreme farming.
11. **`lamchodep(cảnh) = cảnh × 1.000.000` (%)** hiển thị cho Sát Thương Liên Hoàn/Kaioken/Khỉ —
    số phi lý, cần đối chiếu chỗ NPoint dùng.
12. **Text hiệu ứng Địa Đạo cấp 30+** có các dòng vui ("Tăng Độ Nói Phét Trong Game",
    "Tăng Tiến Độ Nạp Game Cho ad") — không có code thật, chỉ là text.
13. **Thọ Nguyên = 0** dừng hẳn vòng update nhân vật (`break`) thay vì chỉ reset — hành vi thực
    tế cần test (nhân vật đứng im? disconnect?).

---

## 12. LƯU TRỮ DỮ LIỆU — TÓM TẮT CHO NGƯỜI SỬA CODE

| Cơ chế | Trường/Cột | Nạp | Ghi |
|---|---|---|---|
| Đạo Lữ/Em Bé | `Tamkjll_Pet` (JSON), `TamkjllNamePet`, `EmBeLv`, `EmBeEXP`, `TamkjllPetGiong`, `TamkjllPetHunger`, `TamkjllPetPower` | `NDVSqlFetcher:415-427` | `PlayerDAO:1024-1028` |
| Kết hôn | `dakethon`, `duockethon` | fetcher | update khi cầu hôn |
| Chuyển sinh | `SagaChuyenSinh` (+ `pet.SagaChuyenSinh`) | `NDVSqlFetcher:339` | khi CS thành công |
| Tu Tiên | `SagaTuTien[3]`, `tho_nguyen` | `NDVSqlFetcher:379` | mỗi giây (`updateThoNguyen`) |
| Thiên/Địa Đạo | `SagaThienDao`, `SagaDiaDao` | fetcher | nạp VND, NPC, rương |
| Cây phép | `data_magic_tree` | load khi vào game | `MagicTree.update()` |
| Cung Mệnh | `cungMenhLevel`, `cungMenhDotPha` + bảng config/bacs | `NDVSqlFetcher:495` | `PlayerDAO.saveCungMenh:1433` |
| Điểm BDKB | `point_bdkb` | fetcher | trừ khi nhận nhẫn 1213 |

---

*Phạm vi: báo cáo đọc từ code tại thời điểm hiện tại (nhánh `main`). Các con số từ
`SystemTuning` (config.properties trên panel) có thể khác với mặc định trong code — nên đối chiếu
file config thật khi cần số chính xác cho người chơi.*
