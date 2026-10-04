# BÁO CÁO HỆ THỐNG SKILL — Server BeMeoGaming

> Ngày: 30/09/2026 — Nguồn: đọc toàn bộ code `src/` + data bảng `skill_template` (DB `hondaodragon`).
> Mỗi mục có trích `file:dòng` để đối chiếu.

---

## 1. Tổng quan & luồng xử lý

| Thành phần | File | Vai trò |
|---|---|---|
| Dữ liệu skill | DB `skill_template` (36 template, **240 cấp skill**) | Load 1 lần lúc boot |
| Load data | `src/server/Manager.java:449-499` | `select * from skill_template order by nclass_id, slot` → parse JSON cột `skills` |
| Model | `src/skill/Skill.java`, `PlayerSkill.java`, `NClass.java` | skillId, point, coolDown, manaUse, damage, dx/dy |
| Xử lý chính | `src/services/SkillService.java` (1660 dòng) | useSkill, attack, buff, cooldown, mana |
| Tính dame | `src/player/NPoint.java:1979 getDameAttack()`, `:1462 setDame()` | Công thức đầu ra |
| Nhận damage | `src/player/Player.java:1238 injured()`, `src/mob/Mob.java:112 injured()` | Giáp, né, khiên… |
| Hiệu ứng | `src/services/EffectSkillService.java`, `src/player/EffectSkill.java` | Choáng/trói/khiên/biến hình |
| Hằng số phụ | `src/utils/SkillUtil.java` | Thời gian & phạm vi hiệu ứng theo cấp |
| Tuyệt kỹ (type 4) | `src/player/NewSkill.java` + `SkillService.updateSkillSpecial:221` | Đã bỏ gồng — tung tick đầu (~250ms) → gây dame |
| Nội tại | bảng DB `intrinsic` (id 0–26) + `SkillService.affterUseSkill:1469` | Giảm CD, cộng %dame |

**Luồng 1 đòn đánh:**
```
Client ─ msg -45/54/-60 ─> Controller.java:540/690/706
   ─> SkillService.useSkill():51
        ├─ check hiệu ứng đang treo, PK rules, mana (canUseSkillWithMana:1433)
        ├─ check cooldown (canUseSkillWithCooldown:1464)
        ├─ type 1 → useSkillAttack():533   (đánh thường/chưởng)
        ├─ type 2 → useSkillBuffToPlayer():1011 (Trị thương)
        ├─ type 3 → useSkillAlone():820    (TDHS, khiên, tự sát, huyt sáo…)
        └─ type 4 → useNewSkillNotFocus() + updateSkillSpecial():221 (tuyệt kỹ)
   ─> playerAttackPlayer():1151 / playerAttackMob():1237
        ─> nPoint.getDameAttack()  →  target.injured()
        ─> gửi msg -60 (player) / 54 (mob): dame, crit, die
```

---

## 2. Phân loại skill (cột `TYPE` trong DB)

| TYPE | Nghĩa | Router trong `useSkill()` | Skill |
|---|---|---|---|
| **1** | Đánh (attack) | `useSkillAttack():533` | Đấm (cácloại: Dragon/Demon/Galick/Liên hoàn/Kaioken), Kame, Masenko, Antomic, QCKK, Makanko, DCTT, Thôi miên, Trói, Sôcôla |
| **2** | Buff | `useSkillBuffToPlayer():1011` | Trị thương (Namec) |
| **3** | Đơn (alone) | `useSkillAlone():820` | TDHS, Khiên, Tái tạo NL, Biến Khỉ, Đẻ trứng, Huyt sáo, Tự sát, Biến Sôcôla*, Super*, Tăng Cường, Phân Thân |
| **4** | Tuyệt kỹ (new skill) | `useNewSkillNotFocus` + `updateSkillSpecial:221` | Super Kamejoko (id24), Ma Phong Ba (id26), Cađíc Liên Hoàn Chưởng (id25) |

\* Sôcôla thực chất chạy trong `useSkillAttack` (case `SOCOLA`); Super/Tăng Cường/Phân Thân xử lý riêng (biến hình 479, tiến hóa 510, phân thân 1023).

**Cột `mana_use_type` (cách trừ MP):**

| Giá trị | Công thức | Nguồn |
|---|---|---|
| 0 | Trừ **cố định** `mana_use` MP | `SkillService:1497-1516` |
| 1 | Trừ **% MP tối đa**: `mpMax * mana_use / 100` | `SkillService:1497-1516` |
| 2 | Trừ **toàn bộ MP** (chỉ cần mp > 0) — Makankosappo | `SkillService:1497-1516` |

> ⚠️ Lưu ý: các skill manaType=1 tính theo **% mpMax**, ví dụ Trị thương cấp 1 = 40% KI tối đa, Super Kamejoko cấp 1 = 80% KI, TDHS = 45%… rồi giảm dần theo cấp.

---

## 3. Bảng đầy đủ 36 template / 240 cấp skill

Ký hiệu: `dmg` = % sức đánh (damage), `mp` = mana_use, `cd` = cooldown (ms), `pow` = power_require, `dx/dy` = phạm vi hiệu ứng (đơn vị pixel/cơ), `sk` = skillId từng cấp.

### 3.1 TRÁI ĐẤT (nclass_id = 0)

**tempId 0 — Chiêu đấm Dragon** (type1, manaType0, maxPoint7)
```
c1 sk0  dmg100 mp1    cd500 pow1000      dx32 | c2 sk1  dmg110 mp2   cd500 pow10000
c3 sk2  dmg120 mp4    cd500 pow22000     dx36 | c4 sk3  dmg130 mp8   cd500 pow66000
c5 sk4  dmg140 mp16   cd500 pow200000    dx40 | c6 sk5  dmg150 mp32  cd500 pow600000
c7 sk6  dmg160 mp70   cd500 pow1800000   dx44
```

**tempId 1 — Chiêu Kamejoko** (type1, manaType0) — chưởng
```
c1 sk7  dmg150 mp30   cd2000 pow10000     dx160 | c2 sk8  dmg200 mp60   cd2500 pow20000
c3 sk9  dmg250 mp120  cd3000 pow60000     dx180 | c4 sk10 dmg300 mp240  cd3500 pow180000
c5 sk11 dmg350 mp480  cd4000 pow540000    dx200 | c6 sk12 dmg400 mp960  cd4500 pow1600000
c7 sk13 dmg450 mp1280 cd5000 pow4800000   dx220
```

**tempId 6 — Thái Dương Hạ San** (type3, manaType1) — choáng diện rộng (case `SkillService:824`)
```
c1 sk42 dmg3000 mp45 cd60000 pow60000     dx150 | c2 sk43 dmg4000 mp40 cd55000 pow120000
c3 sk44 dmg5000 mp35 cd50000 pow360000    dx210 | c4 sk45 dmg6000 mp30 cd45000 pow1000000
c5 sk46 dmg7000 mp25 cd40000 pow3200000   dx270 | c6 sk47 dmg8000 mp20 cd35000 pow10000000
c7 sk48 dmg9000 mp15 cd30000 pow30000000  dx330
```

**tempId 9 — Kaioken** (type1, manaType0) — có điều kiện HP > 10% hpMax (`SkillService:1436`)
```
c1 sk63 dmg160 mp9000  cd500 pow150000000 | c2 sk64 dmg170 mp13000 cd500 pow200000000
c3 sk65 dmg180 mp15000 cd500 pow250000000 | c4 sk66 dmg190 mp18000 cd500 pow300000000
c5 sk67 dmg200 mp21000 cd500 pow350000000 | c6 sk68 dmg210 mp24000 cd500 pow400000000
c7 sk69 dmg220 mp27000 cd500 pow450000000   (dx32)
```

**tempId 27 — Super Goku** (type3, manaType1, maxPoint6) — biến hình
```
c1 sk186 .. c6 sk191: dmg100 mp0 cd300000 pow 250/350/450/550/650/650 tr ; dx200
```
**tempId 28 — Tăng Cường** (type3, maxPoint1) `sk192 dmg100 mp0 cd500 pow250000000`

**tempId 10 — Quả cầu kênh khi** (type1, manaType1)
```
c1 sk70 dmg500  mp50 cd360000 pow500000000   dx300 | c2 sk71 dmg600  mp55 cd350000 pow600000000 dx400
c3 sk72 dmg700  mp60 cd340000 pow700000000   dx500 | c4 sk73 dmg800  mp65 cd330000 pow800000000 dx600
c5 sk74 dmg900  mp70 cd320000 pow900000000   dx700 | c6 sk75 dmg1000 mp75 cd310000 pow1000000000 dx800
c7 sk76 dmg1100 mp80 cd300000 pow1100000000  dx900
```

**tempId 20 — Dịch chuyển tức thời** (type1, manaType0)
```
c1 sk128 dmg1000 mp5000  cd20000 pow10000000   | c2 sk129 dmg1500 mp7000  cd19000 pow25000000
c3 sk130 dmg2000 mp10000 cd18000 pow50000000   | c4 sk131 dmg2500 mp15000 cd17000 pow125000000
c5 sk132 dmg3000 mp20000 cd16000 pow625000000  | c6 sk133 dmg3500 mp25000 cd15000 pow3125000000
c7 sk134 dmg4000 mp30000 cd14000 pow15625000000  (dx5000 — tức thị)
```

**tempId 22 — Thôi miên** (type1, manaType0)
```
c1 sk142 dmg5  cd30000 | c2 dmg6 cd32000 | c3 dmg7 cd34000 | c4 dmg8 cd36000
c5 dmg9 cd38000 | c6 dmg10 cd40000 | c7 dmg11 cd42000   (mp10000 cố định, dx200)
pow: 10tr/25tr/50tr/125tr/625tr/3.125tr tỷ/15.625 tỷ
```

**tempId 24 — Super Kamejoko** (type**4**, manaType1, maxPoint**9** — nhưng có **10 cấp**)
```
c1 sk156 dmg550 mp80 cd170000 dx190 dy25 | c2 dmg600 mp75 cd160000 dx200 dy30
c3 dmg650 mp70 cd150000 dx210 dy35       | c4 dmg700 mp65 cd140000 dx230 dy40
c5 dmg750 mp60 cd130000 dx250 dy45       | c6 dmg800 mp55 cd120000 dx270 dy50
c7 dmg850 mp50 cd110000 dx290 dy55       | c8 dmg900 mp45 cd100000 dx310 dy60
c9 dmg950 mp40 cd90000  dx330 dy65       | c10 sk165 dmg1000 mp35 cd80000 dx350 dy70
(powReq = 60.000.000.000 tất cả các cấp)
```

**tempId 19 — Khiên năng lượng** (type3, manaType1) — giống nhau ở cả 3 lớp
```
c1 sk121 dmg15 mp51 cd75000  | c2 dmg20 mp48 cd80000 | c3 dmg25 mp45 cd85000
c4 dmg30 mp42 cd90000        | c5 dmg35 mp39 cd95000 | c6 dmg40 mp36 cd100000
c7 dmg45 mp33 cd105000       (dx0 — tự lên mình; pow 10tr → 15.625 tỷ)
```

**tempId 29 — Phân Thân** (type3, manaType0) — giống cả 3 lớp
```
c1 sk221 cd300000 | c2 sk222 cd600000 (!) | c3..c7 cd300000 ; dmg100 mp0 dx200
pow: 250tr/350tr/450tr/550tr/650tr/650tr/650tr
```

### 3.2 NAMKİẾC (nclass_id = 1)

**tempId 2 — Chiêu đấm Demon** (type1, manaType0)
```
c1 sk14 dmg95  mp1 cd400 pow1000   dx24 | c2 dmg105 mp2 cd400 pow10000   dx26
c3 dmg115 mp4 cd400 pow22000       dx28 | c4 dmg125 mp8 cd400 pow66000   dx30
c5 dmg135 mp16 cd400 pow200000     dx32 | c6 dmg145 mp32 cd400 pow600000 dx34
c7 dmg155 mp70 cd400 pow1800000    dx36
```

**tempId 3 — Chiêu Masenko** (type1, manaType0) — chưởng
```
c1 sk21 dmg100 mp8   cd800 pow10000    dx140 | c2 dmg110 mp16  cd790 pow20000
c3 dmg120 mp32  cd780 pow60000         dx160 | c4 dmg130 mp64  cd760 pow180000
c5 dmg140 mp128 cd740 pow540000        dx180 | c6 dmg150 mp256 cd720 pow1600000
c7 dmg160 mp512 cd700 pow4800000       dx200
```

**tempId 7 — Trị thương** (type**2** buff, manaType1)
```
c1 sk49 dmg50 mp40 cd30000 pow60000    dx100 | c2 dmg55 mp35 cd32000 pow120000  dx105
c3 dmg60 mp30 cd34000 pow360000        dx110 | c4 dmg65 mp25 cd38000 pow1000000 dx115
c5 dmg70 mp20 cd40000 pow3200000       dx120 | c6 dmg75 mp15 cd42000 pow10000000 dx125
c7 dmg80 mp10 cd44000 pow30000000      dx130
```

**tempId 11 — Makankosappo** (type1, manaType**2** = hết MP)
```
c1 sk77 dmg70  cd360000 pow150000000 | c2 dmg80 cd350000 pow200000000
c3 dmg90  cd340000 pow250000000 | c4 dmg100 cd330000 pow300000000
c5 dmg110 cd320000 pow350000000 | c6 dmg120 cd310000 pow400000000
c7 dmg130 cd300000 pow450000000   (dx20000; mp0 — nhấn full KI)
```

**tempId 27 — Super Picolo** (type3, maxPoint6): `sk193–198, dmg100 mp0 cd300000 pow250tr→650tr`
**tempId 28 — Tăng Cường**: `sk199 cd500 pow250tr`
**tempId 12 — Triệu Hồi (Đẻ trứng)** (type3, manaType1)
```
c1 sk84 dmg50 mp20 cd360000 pow500000000  | c2 dmg55 mp30 cd390000 pow600000000
c3 dmg60 mp40 cd420000 pow700000000       | c4 dmg65 mp50 cd450000 pow800000000
c5 dmg70 mp60 cd480000 pow900000000       | c6 dmg75 mp70 cd510000 pow1000000000
c7 dmg80 mp80 cd540000 pow1100000000      (dx200)
```

**tempId 17 — Liên hoàn** (type1, manaType0) — đánh nhanh
```
c1 sk107 dmg160 mp100 cd350 pow10000000   dx30 | c2 dmg165 mp200 cd345 pow25000000  dx35
c3 dmg170 mp300 cd340 pow50000000          dx40 | c4 dmg175 mp400 cd335 pow125000000 dx45
c5 dmg180 mp500 cd330 pow625000000         dx50 | c6 dmg185 mp600 cd335 pow3125000000 dx55
c7 dmg190 mp700 cd330 pow15625000000       dx60
```

**tempId 18 — Biến Sôcôla** (type1, manaType1)
```
c1 sk114 dmg15 mp22 cd30000 | c2 dmg17 mp20 cd29000 | c3 dmg19 mp18 cd28000
c4 dmg21 mp16 cd27000       | c5 dmg23 mp14 cd26000 | c6 dmg25 mp12 cd25000
c7 dmg27 mp10 cd24000       (dx500; pow 10tr → 15.625 tỷ)
```

**tempId 26 — Ma phong ba** (type**4**, manaType1, maxPoint9 — **10 cấp**)
```
c1 sk166 dmg550 mp80 cd170000 dx83  | c2 dmg600 mp75 cd160000 dx95
c3 dmg650 mp70 cd150000 dx107       | c4 dmg700 mp65 cd140000 dx119
c5 dmg750 mp60 cd130000 dx130       | c6 dmg800 mp55 cd120000 dx142
c7 dmg850 mp50 cd110000 dx154       | c8 dmg900 mp45 cd100000 dx165
c9 dmg950 mp40 cd90000  dx177       | c10 sk175 dmg1000 mp35 cd80000 dx188 (pow 60 tỷ tất cả)
```

### 3.3 XAYDA (nclass_id = 2)

**tempId 4 — Chiêu đấm Galick** (type1, manaType0)
```
c1 sk28 dmg110 mp1 cd500 pow1000    dx36 | c2 dmg120 mp2 cd500 pow10000   dx37
c3 dmg130 mp4 cd500 pow22000        dx38 | c4 dmg140 mp8 cd500 pow66000   dx39
c5 dmg150 mp16 cd500 pow200000      dx40 | c6 dmg160 mp32 cd500 pow600000 dx41
c7 dmg170 mp70 cd500 pow1800000     dx42
```

**tempId 5 — Chiêu Antomic** (type1, manaType0) — chưởng
```
c1 sk35 dmg110 mp18   cd1000 pow10000    dx150 | c2 dmg140 mp34   cd1200 pow20000
c3 dmg170 mp68   cd1400 pow60000         dx170 | c4 dmg200 mp136  cd1600 pow180000
c5 dmg230 mp258  cd1800 pow540000        dx190 | c6 dmg260 mp514  cd2000 pow1600000
c7 dmg290 mp1026 cd2200 pow4800000       dx210
```

**tempId 8 — Tái tạo năng lượng** (type3, manaType1)
```
c1 sk56 dmg4 mp0 cd55000 pow60000 | c2 dmg5 cd50000 pow120000 | c3 dmg6 cd45000 pow360000
c4 dmg7 cd40000 pow1000000        | c5 dmg8 cd35000 pow3200000
c6 dmg9 cd30000 pow10000000       | c7 dmg10 cd25000 pow30000000   (dx0)
```

**tempId 13 — Biến Khỉ** (type3, manaType1)
```
c1 sk91 cd300000 pow250tr | c2 sk92 cd310000 pow350tr | c3 cd320000 pow450tr
c4 cd330000 pow550tr      | c5 cd340000 pow650tr      | c6 cd350000 pow750tr
c7 cd360000 pow850tr      (dmg100 mp10 dx200)
```

**tempId 27 — Super Vegeta** (maxPoint6): `sk200–205, cd300000, pow250tr→650tr`
**tempId 28 — Tăng Cường**: `sk206 cd500 pow250tr`
**tempId 14 — Tự Sát** (type3, manaType1)
```
c1 sk98  dmg100 dx200 | c2 dmg105 dx300 | c3 dmg110 dx400 | c4 dmg115 dx500
c5 dmg120 dx600      | c6 dmg125 dx700 | c7 dmg130 dx900
(mp50, cd120000 cố định; pow 250tr → 550tr)
```

**tempId 21 — Gồng Sáo (Huýt sáo)** (type3, manaType0)
```
c1 sk135 dmg40 mp50 cd210000 | c2 dmg50 mp45 cd205000 | c3 dmg60 mp40 cd200000
c4 dmg70 mp35 cd195000       | c5 dmg80 mp30 cd190000 | c6 dmg90 mp25 cd185000
c7 dmg100 mp20 cd180000      (dx500; pow 10tr → 15.625 tỷ)
```

**tempId 23 — Trói Mục Tiêu** (type1, manaType0)
```
c1 sk149 dmg5  mp5000  cd15000 | c2 dmg10 mp10000 cd20000 | c3 dmg15 mp15000 cd25000
c4 dmg20 mp20000 cd30000       | c5 dmg25 mp25000 cd35000
c6 dmg30 mp30000 cd40000       | c7 dmg35 mp32000 cd45000   (dx150)
```

**tempId 25 — Cađíc Liên Hoàn Chưởng** (type**4**, manaType1, maxPoint9 — **10 cấp**)
```
c1 sk176 dmg550 mp80 cd170000 dx120 | c2 dmg600 mp75 cd160000 dx130
c3 dmg650 mp70 cd150000 dx140       | c4 dmg700 mp65 cd140000 dx150
c5 dmg750 mp60 cd130000 dx160       | c6 dmg800 mp55 cd120000 dx170
c7 dmg850 mp50 cd110000 dx180       | c8 dmg900 mp45 cd100000 dx190
c9 dmg950 mp40 cd90000  dx200       | c10 sk185 dmg1000 mp35 cd80000 dx210 (pow 60 tỷ)
```

> ⚠️ **Bất thường**: 3 tuyệt kỹ (24/25/26) khai `max_point = 9` nhưng mảng `skills` có **10 cấp** (`point` = 1…10). `SkillUtil.createSkill` cho phép tới `skills.size()` nên cấp 10 vẫn nhận được; cấp 10 chủ yếu học qua item (1417–1437) / NPC Whis (`Whis.java:133`).

---

## 4. CÔNG THỨC TÍNH DAME ĐẦU RA (chi tiết)

### 4.1 Bước 1 — Sức đánh gốc `nPoint.dame` (`NPoint.setDame():1462`)

```
dame = dameg (điểm sức đánh) + dameAdd
     + %từ option trang bị (tlDame[])            , itemTime (lý rượu +150%, bánh ga quay…)
     + pet fusion % (pet1 +25%, pet2 +30%, pet3 +20%, pet4 +40%, pet11 +1000%…)
     + set Gokux5 +80% , set Sagax5 +30% , tinh Ấn +5% , VIP +50..1000%
     + bang: clan.level × 30%                     (d_clan_per_lv=30)
     + Thần Đạo: dame × SagaThienDao × 1%         (d_thiendo_dame=1)
     + Chuyển Sinh: +SagaChuyenSinh × 20%         (d_chuyensinh_dame=20)
     + Địa Ngục: +SagaDiaDao × 1.000.000 (cộng thẳng)
     + nhiệm vụ chính: +200.000 × id task
     + các mốc "Dấu La Đại Lực" (5tr→100tr cộng thẳng theo mốc 0..6)
     + biến hình: +10/20/30/40/50/60% (isbienhinh 1..6)   | biến khỉ: +(level+3)%
     ± map lạnh: /7 (nếu không có "không lạnh")
     - Sôcôla: -50% dame khi đang bị biến sôcôla
     × DAME_GLOBAL = 1.000.000                    ← game_tuning.properties
     softCap: trên 3.0×10^10 chỉ tính 20% phần vượt (GameTuning.softCapDame:101)
```
Sau đó phía client hiển thị "sức đánh" là giá trị này.

### 4.2 Bước 2 — `getDameAttack(isAttackMob)` (`NPoint.java:1979`)

```java
setIsCrit();                                  // xác định crit trước (xem 4.5)
double dameAttack = dame;                     // dame đã qua Bước 1

// (a) % của skill — theo skillSelect.template.id
switch (skill) {
  DRAGON      : %intrinsic (id1)  ; percentDameSkill = skill.damage;
  KAMEJOKO    : %intrinsic (id2)  ; damage; set Songoku5 → +100%   (xDame)
  GALICK      : %intrinsic (id16) ; damage; set Kakarot5 → +100%
  ANTOMIC     : %intrinsic (id17) ; damage
  DEMON       : %intrinsic (id8)  ; damage
  MASENKO     : %intrinsic (id9)  ; damage
  LIEN_HOAN   : %intrinsic (id13) ; damage; set Ociteu5 → +80%
  KAIOKEN     : %intrinsic (id26) ; damage; Thần Đạo ≥30 → +SagaThienDao% ; set ThiênXinHang5 → +80%
  DCTT        : crit bắt buộc ; damage ± ngẫu nhiên 5% ; Thần Đạo ≥30 → +SagaThienDao%   (NPoint:2059-2066)
  MAKANKOSAPPO: return mpMax/100 × damage × (1 + skillPct/100)     ← KHÔNG theo dame
  QCKK        : return (ΣHP quái trong R + ΣHP người trong R)/10 + dame×10
                + (set Kirin5 → +100) ± ngẫu nhiên 5%              ← công thức riêng
  DE_TRUNG    : return dame(+100 nếu set Pikkoro5) × (1 + skillPct/100)
}
// (b) nếu intrinsic id18 + đang biến khỉ → %dame intrinsic

if (percentDameSkill != 0) dameAttack = dameAttack × percentDameSkill / 100;
dameAttack += dameAttack × percentDameIntrinsic / 100;      // nội tại
dameAttack += dameAttack × dameAfter / 100;                 // bonus "đòn kế" (DCTT/ThôiMiên/Sôcôla/Trói, intrinsic 6/7/14/22) — tự reset về 0 sau đó
if (isDameBuff) dameAttack += dameAttack × tileDameBuff/100; // buff dame đang bật
if (isAttackMob) {
    for (tl : tlDameAttMob) dameAttack += dameAttack × tl/100;   // option "SD đánh quái"
    if (pet && charm đệ tử) dameAttack ×= 2;
}

// (c) CHÍ MẠNG
if (isCrit) {
    dameAttack ×= critMult();                       // = 2.0  (crit_tuning.properties)
    dameAttack += dameAttack × (tlSDCM + sdcmAdd)/100;   // % SD chí mạng từ trang bị
    if (Thần Đạo ≥ 70 && THIENDAO70_ON)  dameAttack += dameAttack × SagaThienDao/100;
    if (liên hoàn && TD≥100 && ON)        dameAttack += dameAttack × SagaThienDao/100;
    if (pet && master TD≥500)             dameAttack += dameAttack × (TD/20)/100;
    if (em bé loại 6/9/10)                dameAttack += dameAttack × 2×(EmBeLv+1)/100;
}

// (d) % thêm từ set 5 mảnh & skillPct (SetConfigService.skillPct:164)
percentXDame += SetConfigService.skillPct(player, skillId);  // set config cộng theo skill id
dameAttack += dameAttack × percentXDame / 100;

// (e) dao động ±5% + 1  (VARIANCE_PCT = 5.0)
dameAttack += random(-1..1) × (dameAttack × 5/100) + 1;

// (f) nếu đang có hiệu ứng xChuong (skin) & là chưởng → dameAttack ×= xChuong, bật isXDame cho đòn sau
return dameAttack;
```

### 4.3 Bước 3 — Nhân hệ số output khi là người chơi

```java
// SkillService.playerAttackPlayer:1151 và playerAttackMob:1237
if (đang isXDame của skin) { dameAttack = false; if (target isBoss) dameAttack /= 3; }
if (target isBoss) dameAttack += dameAttack × nPoint.dameBoss/100;   // % "dame boss"
dameAttack = OutputTuning.applyPlayerDame(dameAttack, true);         // × playerOutRate (SkillService:1166/1265)
```
- `playerOutRate = 0.1` (`data/config/output_tuning.properties`) → **mọi dame đầu ra của người chơi bị ×0,1**.
- Tổng hệ số gần đúng: `dame gốc × 1.000.000 (DAME_GLOBAL) × 0,1 (out) = ×100.000` cộng các % ở trên.

### 4.4 Công thức riêng từng chiêu (tóm tắt)

| Skill | Công thức dame | Nguồn |
|---|---|---|
| QCKK | `(ΣHP_mob(R) + ΣHP_người(R))/10 + dame×10` ±5%, +100 nếu set Kirin5 | `NPoint:2072-2099` |
| Makankosappo | `mpMax × damage% /100` (+ skillPct) — **không theo dame** | `NPoint:2067-2071` |
| Đẻ trứng | `dame(+100 nếu set Pikkoro5)` + skillPct | `NPoint:2100-2105` |
| DCTT | crit chắc + `dame ±5%` + Thần Đạo% | `NPoint:2039-2047` |
| Tự sát | `dame = hpMax người đánh`; quái nhận đủ, **boss nhận /2 (biến khỉ /3)**; kẻ đánh chết | `SkillService:946-985` |
| Trị thương | hồi `(level+9)×5%` HP **và** MP cho mục tiêu + người trong 300px (nếu point>1), hồi sinh được | `SkillService:1014`, `SkillUtil:134` |
| Huyt sáo | hồi `(level+3)×10%` HP cho người cùng cờ (trừ Namec — Namec bị trừ), 30s | `SkillService:887-941`, `SkillUtil:130` |
| Tái tạo NL | hồi `(level+3)%` HP+MP **mỗi giây × 10 giây** (10 tick) | `NPoint:2717-2734`, `SkillUtil:138` |

### 4.5 Chí mạng (`NPoint.setIsCrit():1968`)

```
isCrit = true nếu:  intrinsic id25 (liên tục) && HP% ≤ param1
                 hoặc isCrit100 (dòng chắc crit: đang DCTT, đánh người đang bị Trói, ...)
                 hoặc random < crit%          (crit = critg + option, cap 110)
set khỉ → crit = 110 (chắc crit), set Mabu → 25, lỳ nước mía → 40, bánh trung thu → 50
critMult = 2.0 ; critCap = 110 ; variance = 5%   ← data/config/crit_tuning.properties
```

### 4.6 Bên nhận dame

**Người chơi — `Player.injured():1238`:**
```
0.đang bất tử buff? → 0 ; hồi sinh <1,5s → 0 ; cùng bang/NRNM → 0 ; PK rules
1.Map MaBu: 8 chiêu đánh bị cắt ngọn tại hpMax/20
2.chống chưởng (voHieuChuong>0) với chiêu chưởng → dame = 0, hồi MP bằng %dame
3.tlGiap -= xuyên giáp của attacker (tlxgc chưởng / tlxgcc đấm), tlNeDon -= tlchinhxac
   cap: ne don ≤ 40%, giáp ≤ 30%
4.roll né: random(tlNeDon,100) trúng → 0
5.dame -= dame × tlGiap/100
6.không piercing: dame -= def   (def = defg×4 + bonus; hạ tối thiểu 1)   ← subDameInjureWithDeff:2347
7.item Giáp Xen: /2 (hoặc ×40% nếu loại 2)
8.đang có khiên: dame = 1 (vỡ khi > hpMax; map phụ bản → 10) ; nếu có盾 sẽ phản sát thương (PST)
9.trừ HP, chết → setDie
```
- **Phản sát thương (PST)**: `phanSatThuong():1098` — % `tlPST` của dame gây ra phản lại attacker (boss chỉ bị tối đa `hpMax/100` mỗi lần).
- **Hút HP/KI**: `hutHPMP():1161` — hồi `dame × tlHutHp/100` (quái cộng thêm `tlHutHpMob`), KI tương tự.

**Quái — `Mob.injured():112`:**
```
dame bị chặn bằng hp còn lại ; nếu quái full máu mà không dieWhenHpFull → chỉ lấy hp-1
quái "elite" (lvMob>0, 10% tỉ lệ random khi sinh, tempId>18): dame bị quy đổi
   = 10% × (maxHp×10 nếu maxHp≤20tr , ngược lại 2×10^9) và quái phản đòn 50%
map Khí Gas Hủy Diệt: 4 chiêu (Liên hoàn/AT/Masenko/Kame) chỉ gây 1
boss sự kiện Trung Thu: dame = 2.000.000 cố định
HP giảm → die → task/achievement ; tiềm năng nhận = (dame/HPfull)×maxTiemNang ± chênh cấp
```

**Quái/đệ đánh lại — `MobPoint.getDameAttack():39` + `Mob.mobAttackPlayer():358`:**
```
dame quái = cố định ±1%   hoặc   hpFull × random(pDame-1..pDame+1)% ± (level×10)
giảm: bùa da trâu /2 ; đệ /2 ; satellite defend -20% ; charm CMS ×0,1
      lvMob>0 → = 10% hpMax người chơi ;charm Oai Hung (lvMob>0) → 0
      Thần Đạo ≥1 → dame / (SagaThienDao + 1)   ← dame quái vỡ dần theo Thần Đạo
```

### 4.7 Boss dùng skill

- `Boss.attack():617` — gate **100ms**, chọn **ngẫu nhiên 1 skill** trong bộ skill của boss, nếu trong phạm vi thì đánh (25% tỉ lệ xích lại gần trước).
- Phạm vi boss: chưởng 300px, đấm 100px, còn lại 500px (`getRangeCanAttackWithSkillSelect:656`).
- CD của skill boss lấy từ `BossData.skillTemp[2]` (`Boss.initSkill:230-243`) — có thể override `coolDown`.
- Phân thân của người chơi thành boss: dame ×10, HP ×10, cấp 140 (`callPhanThan:184`, `new PhanThan(...,60000):216`).

---

## 5. DELAY / COOLDOWN (mọi loại)

### 5.1 Cooldown skill của người chơi
```java
// SkillService.canUseSkillWithCooldown():1464
canUse = now - skill.lastTimeUseThisSkill >= skill.coolDown - 50     // nới lỏng 50ms
// Sau khi đánh: setLastTimeUseSkill():1518 → lastTimeUseThisSkill = now - 1
```
- **CD theo bảng §3** (vd đấm Dragon 500ms, Liên hoàn 330–350ms, Kame 2–5s, TDHS 30–60s, tuyệt kỹ 80–170s).
- **Nội tại giảm CD** (`setLastTimeUseSkill:1518-1550`): nếu có nội tại hợp lệ, server đặt `lastTimeUseSkill = now - coolDown×param1/100` rồi `EffectSkill.update():283` **giải phóng CD sớm** khi `now - lastTimeUseSkill ≥ coolDown` → **CD thực = coolDown × (100 − param1)/100**.
  - Nội tại giảm CD: Trị thương(id10), TDHS(id3), QCKK(id4), Khiên(id5/15/20), Makanko(id11), Đẻ trứng(id12), Tự sát(id19), Huýt sáo(id21).
  - ⚠️ Khi giải phóng, `Service.releaseCooldownSkill():1969` **hồi luôn MP về MAX**.
- Gửi lại CD client qua msg **-94** (`sendTimeSkill():1935`).

### 5.2 Thời gian gồng / chuẩn bị (prepare) — ⚠️ ĐÃ BỎ (30/09/2026)

> Skill **tác dụng ngay khi bấm** (1 lần bấm = ra đòn). Cờ `prepareQCKK/prepareLaze/prepareTuSat` luôn `false` — boss có check cờ này để "đứng im khi gồng" giờ luôn đánh bình thường.

| Skill | Thay đổi | Code |
|---|---|---|
| QCKK | ~~gồng 4000ms, 2 lần bấm~~ → **ném ngay 1 lần bấm** | `case QUA_CAU_KENH_KHI:682` |
| Makankosappo | ~~nạp 3000ms~~ → **bắn ngay** | `case MAKANKOSAPPO:713` |
| Tự sát | ~~gồng 2000ms + chờ ≥1500ms + `Thread.sleep(1500)`~~ → **nổ ngay khi bấm** | `case TU_SAT:946` |
| Tuyệt kỹ (type4) | ~~chờ `TIME_GONG`=2000ms~~ → **tick đầu ~250ms vào step1**, gây dame mỗi 250ms trong ~2000ms (~7–8 đợt) như cũ; `timeGong` gửi client = 250ms | `NewSkill:13`, `start(250)`, `updateSkillSpecial:221`, `SkillService:375` |
| Biến hình (Super) | CD **300.000ms (5 phút)**, hiệu ứng biến hình cũng tự hết sau 5 phút | `canUseSkillWithCooldown(skillId):479`, `EffectSkill.update():221` |
| Tiến hóa (Evolution) | CD **3.000ms**, cần biến hình ≥ cấp 2, tối đa theo `isbienhinh` | `:484`, `useSkillEvolution:510` |
| Phân thân | CD = `coolDown` của skill Phân Thân (300s, riêng cấp 2 là 600s), bản sao tồn 60s | `startSkillClone:993`, `callPhanThan` |

`timeDame` gửi client cho tuyệt kỹ: Super Kame/LHC = **3000ms**, Ma Phong Ba = **5000ms** (`NewSkill.timeEnd():153`). Ma Phong Ba nhốt mục tiêu **11000ms** (22s nếu có typeItem) (`finishUseMafuba:369-390`, `setIsBinh:375`), số mục tiêu = `point/2` (tối thiểu 1) (`SkillService:236`).

### 5.3 Delay phía quái / boss
| Đối tượng | Delay | Code |
|---|---|---|
| Quái thường | `timeAttack` = **1000ms** nếu có mục tiêu là thù địch tạm, ngược lại **2000ms** | `Mob.java:302,341,351` |
| Quái elite (lvMob>0) | **không đánh** (trừ map phụ bản) | `Mob.java:302` |
| Quái elite phản đòn khi bị đánh | tối đa mỗi **2500ms**, 50% tỉ lệ | `Mob.java:144` |
| Boss | gate **100ms**/lần chọn skill | `Boss.java:618` |

### 5.4 Tick server (ảnh hưởng delay nội bộ)
- Mỗi map chạy loop `Thread.sleep` để **chu kỳ 1000ms** (`Map.java:194-207`) → `nPoint.update()` mỗi giây:
  - Tái tạo NL: **10 tick × 1 giây** (`NPoint:2717`)
  - Hồi HP/KI thường: mỗi **60.000ms** ; hồi **stamina** mỗi **30.000ms** (`NPoint:2741-2750`)
- Timer tuyệt kỹ: **250ms**/tick (`NewSkill.start(250)`).

### 5.5 Thể lực (stamina)
- Người: cứ **500 đòn** trừ 1 thể lực (không trừ nếu có bùa đeo đai) — `SkillService:628-634`
- Đệ: 2 đòn/lần (5 nếu có charm đệ tử)
- Hết stamina → không đánh được, hồi 1/30s.

### 5.6 MP
- `canUseSkillWithMana():1433` (xem bảng manaType ở §2). Kaioken thêm điều kiện `hp > hpMax×10%`.
- Trừ MP sau khi đánh: `setMpAffterUseSkill():1497`.

---

## 6. PHẠM VI & MISS

```java
// Skill.java:4-5
RANGE_ATTACK_CHIEU_DAM  = 100;   // nhóm đấm: DRAGON, DEMON, GALICK, LIEN_HOAN, KAIOKEN
RANGE_ATTACK_CHIEU_CHUONG = 300; // nhóm chưởng: KAMEJOKO, MASENKO, ANTOMIC (client tự check)
// useSkillAttack():659-663 — vượt 100px → miss (dame = 0), TRỪ map 113 (siêu hạng)
// Boss: 100 (đấm) / 300 (chưởng) / 500 (còn lại)
```
Phạm vi hiệu ứng theo cấp (`SkillUtil.java`):
```
Choáng TDHS   : R = 120 + level×30            (:118)
Bom Tự sát    : R = 400 + level×30            (:122)
QCKK (tính dame): R = 350 + level×30          (:126)
Trói / DCTT / Sôcôla / Huyt sáo: dùng dx trong bảng §3 (150 / 5000 / 500)
```

---

## 7. HIỆU ỨNG KÈM THEO — thời gian theo cấp (`SkillUtil.java`)

| Hiệu ứng | Công thức thời gian | Dòng |
|---|---|---|
| Choáng TDHS | `(level+2) × 1000ms` ; **×2** nếu set Thiên Xìn Hạng=5 ; +`(ThầnĐạo/100)×1000ms` nếu TD>100 | `:94` + `SkillService:823-829` |
| Khiên năng lượng | `(level+2) × 5000ms` | `:102` |
| Trói | `level × 5000ms` | `:106` |
| Choáng DCTT | `(level+1) × 500ms` | `:110` |
| Thôi miên | `(level+4) × 1000ms` ; +`(ThầnĐạo/100)×1000ms` nếu TD>100 | `:114` + `SkillService:774-778` |
| Biến Sôcôla | `30.000ms` cố định (mục tiêu -50% dame) | `:98` + `SkillService:725-738` |
| Biến khỉ | `(level+5) × 10.000ms`, +(HP `(level+3)×10%`), +(dame `level+3%`), crit=110 | `:78,86,90` |
| Đẻ trứng (trứng tồn tại) | `getTimeMonkey(level) × 2`, HP đệ = `hpMaxPlayer × {30,40,50,60,70,80,90}%`, template `{8,11,32,25,43,49,50}` | `:142-160` |
| Biến hình Super (thời gian buff dame/HP/KI) | `getTimeSuper`: 60s/90s/120s/180s/240s/300s theo cấp | `:165` |
| Hồi phục Trị thương | `(level+9) × 5%` HP+MP | `:134` |
| Hồi phục Huýt sáo | `(level+3) × 10%` HP | `:130` |
| Tái tạo NL | `level+3` % mỗi giây × 10 giây | `:138` |

**Nội tại (bảng `intrinsic`, id 1–26) ảnh hưởng skill** — tóm tắt theo code (`getDameAttack`, `setLastTimeUseSkill`, `affterUseSkill:1469`):
- **Cộng %dame cho chiêu**: id1 Dragon(5–100%), id2 Kame(5–200%), id8 Demon(5–500%), id9 Masenko(2–150%), id13 Liên hoàn(5–25%), id16 Galick(5–80%), id17 Antomic(5–160%), id18 Biến khỉ(5–55%), id26 Kaioken(1–50%).
- **Giảm CD**: id3, 4, 5, 10, 11, 12, 15, 19, 20, 21 (mức `param1` %).
- **Buff "đòn kế"** (`dameAfter`): id6 (sau DCTT), id7 (sau Thôi miên), id14 (sau Sôcôla), id22 (sau Trói) — cộng `param1%` cho đòn kế tiếp, tự xoá sau 1 lần (`affterUseSkill:1472-1491`).
- **Chắc crit khi HP thấp**: id25 (20–50%).

---

## 8. CẤU HÌNH ẢNH HƯỞNG DAME (`data/config/*.properties`)

| Key | Giá trị hiện tại | Ảnh hưởng |
|---|---|---|
| `DAME_GLOBAL` | **1.000.000** | ×sức đánh gốc của mọi entity |
| `DAME_SOFT_CAP` | 3.0×10^10 | Trên cap chỉ tính 20% phần vượt |
| `BOSS_DAME_SCALE` / `BOSS_HP_SCALE` | 1.0 / 1.0 | Scale boss |
| `playerOutRate` | **0.1** | ×dame đầu ra của người chơi |
| `critMult` / `critCap` / `variancePct` | 2.0 / 110 / 5.0 | Chí mạng & dao động |
| `SDCM_GLOBAL` / `CRIT_ADD` | 1.0 / 0 | Hệ số %SD chí mạng, %crit thêm |
| `DEF_GLOBAL` | 1.0 | ×giáp |
| `d_thiendo_dame` | 1.0 | %dame mỗi cấp Thần Đạo |
| `d_chuyensinh_dame` | 20.0 | %dame mỗi lần chuyển sinh |
| `d_clan_per_lv` | 30.0 | %dame mỗi cấp bang |
| `SetConfigService.skillPct` | theo `set_config` | Cộng %dame riêng từng skill khi đủ 5 mảnh set |

---

## 9. GHI CHÚ / ĐIỂM CẦN CHÚ Ý

1. **3 tuyệt kỹ có 10 cấp nhưng `max_point=9`** (§3) — kiểm tra luồng học cấp 10 (item 1417–1437 / NPC Whis) nếu muốn chốt hành vi.
2. **Makankosappo không theo `dame`** — damage hoàn toàn từ `mpMax`; buff KI trực tiếp tăng dame chiêu này.
3. **QCKK tính dame từ HP mục tiêu trong vùng** — quái/đệm血 nhiều = dame lớn.
4. **Nội tại giảm CD giải phóng kèm hồi full MP** (`Service:1969-1981`) — nếu muốn tách thì sửa riêng nhánh này.
5. **`playerOutRate=0.1` + `DAME_GLOBAL=1e6`** là 2 nút cân bằng chính của dame người chơi — chỉnh ở panel (tab Output / Game) không cần sửa code.
6. **Đòn đánh bị "miss" hoàn toàn** nếu đứng quá 100px với nhóm đấm (trừ map 113) — check khoảng cách này khi test dame.
7. **Quái elite (lvMob>0)** không nhận dame trực tiếp mà quy đổi theo %HP — test "BOSS TEST DAME" cần lưu ý nếu con boss đó là `lvMob>0` (dame hiển thị sẽ không đúng kỳ vọng).
8. **Phản sát thương (PST)** và **hút HP/KI** tính trên `dameHit` **sau** khi qua `playerOutRate`.

---
*Phụ lục data thô: 240 dòng cấp skill đã parse bằng tool `tools/ParseSkills.java`.*
