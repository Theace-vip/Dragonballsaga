# QUY TRINH LAM HAO QUANG (aura) THANH 1 ITEM

> Trich dan tu kinh nghiem thuc te (28/09/2026): item **1929 "Hao Quang Hon Don Vo Cuc"** - flag_bag **193** - icon **32339..32346**.
> File nay de lai de lam lai sau nay chi can lam theo tung buoc.

---

## 0. KET LUAN TRUOC (doc truoc khi lam - tranh lam lai con duong that bai)

Co 2 channel hien thi hao quang trong game. **Chi 1 channel dung duoc cho art moi:**

| Channel | Co the them art moi? | Ly do |
|---|---|---|
| **A. Aura id (getAura / DataEffect / msg 127 sub4 + -66)** | **KHONG** | Client chi render cac id aura no **biet san** (19/40/41/42...). Server gui id 43 -> client KHONG bao gio request ve. Da test lan dau: that bai. |
| **B. Item TYPE 11 + bang flag_bag (msg -64/-62/-63)** | **CO (100%)** | Server-driven: server gui id flag -> client hoi frame -> tai PNG tu icon_botnet. Khong whitelist, khong cache. Day la co che cua cac item "Hao quang Ruc Ro / Hao quang ss2 / Hao quang rong" dang co san trong game. |

=> **Lua chon: dung channel B.** Tat ca huong dan duoi day la channel B.

### Vi the nao la "o so 7"?

Server xep slot tren nguoi (`itemsBody`) nhu sau:

| index | TYPE item | Ten tren game |
|---|---|---|
| 0-4 | 0,1,2,3,4 | Ao, Quan, Gang, Giay, Nhan |
| 5 | 5 | Cai trang |
| 6 | 32 | Giap tap luyen / An Tran Do |
| 7 | 21 | Thu cung / Pet |
| **8** | **11** | **HAO QUANG (o ban muon dat)** |
| 9 | 23/24 | Thu cuoi |
| 10 | 72 | Linh thu |
| 11 | 77 | Sach tuyet ki |
| 12 | 82 | Chan Menh |
| 13 | 83 | Danh Hieu |

=> Item **TYPE 11** tu di vao **index 8 = o Hao quang** (o ben canh o thu cung; tren client user thay la "o so 7").
Khong can chon o - server tu xep theo TYPE (`InventoryService.putItemBody`).

---

## 1. DU TRU / KIEN THUC CAN

- **Cong thuc icon theo zoom** (giong QUY_TRINH_SET_MOI.md): `x3 = x2 x 1.5`, `x4 = x2 x 2`.
- **Noi dat file:**
  ```
  BeMeoGaming/data/icon_botnet/x2/<iconId>.png   (goc, x2)
  BeMeoGaming/data/icon_botnet/x3/<iconId>.png
  BeMeoGaming/data/icon_botnet/x4/<iconId>.png
  ```
  (khong co folder x1 cho icon_botnet - client chi xai x2/x3/x4)
- **id hien tai:** icon lon nhat = 32346 (tiep 32347+); flag_bag dang dung 0..192 (tiep 193+); item_template dang dung den 1929 (tiep 1930+).
- **Tools** (trong `BeMeoGaming/tools/`, compile: `javac -encoding UTF-8 -d /tmp/pf tools/<Ten>.java`):

| Tool | Cong dung |
|---|---|
| `GenAura.java` | Tu sinh sheet hao quang codegen (8 frame, grid 3x3) -> `Eff/effect/x{1..4}/img/ImgEffect_43.png` + `DataEffect_43`. Chi dung khi KHONG co art designer va muon test channel A/sinh frame goc. |
| `GenFlagFrames.java` | **CHINH:** cat sheet `ImgEffect_*.png` (grid 3x3, 8 frame) thanh 8 PNG rieng vao `icon_botnet/x{2,3,4}/<id>.png`. |
| `InspectImg.java` | Xem size/alpha/bbox cua anh. |
| `DataEffectCodec.java` | Parse+write format DataEffect (channel A) - chi dung neu quay lai channel A. |

---

## 2. QUY TRINH 8 BUOC

### Buoc 1. Chuan bi sheet art (neu chua co)

- **Neu designer dua PNG:** chot 1 sheet **grid 3x3, 8 frame** (frame 4 = vien trong, khong nen), hoac 1 frame duy nhat roi tu nhan len.
- **Neu muon sinh tu code:** chay `GenAura.java` -> sinh `Eff/effect/x{1..4}/img/ImgEffect_43.png` (43 = id effect muc dich, co the doi).
  - Luu y format rect DataEffect: **field la u8 (toa do <= 255)** -> sheet lon hon 255px phai pack 2D (grid 3x3 nhu GenAura da lam).

### Buoc 2. Cat frame -> icon PNG

```bash
cd BeMeoGaming
javac -encoding UTF-8 -d /tmp/pf tools/GenFlagFrames.java
java -cp /tmp/pf GenFlagFrames <iconIdDauTien>      #vd: 32347
```
- Script mac dinh doc `Eff/effect/x{2,3,4}/img/ImgEffect_43.png`, grid 3x3, lay 8 frame (chi so `first` doi duoc; doi so `cols`/`count`/`sheet id` trong file neu can).
- **Phai co du 3 zoom** x2/x3/x4.
- Verify: `java -cp /tmp/pf InspectImg data/icon_botnet/x2/<idDau>.png` (xem size, alpha=true).

### Buoc 3. Them flag_bag

```sql
INSERT INTO flag_bag (id, icon_data, NAME, gold, gem, icon_id)
VALUES (<flagId>, '<icon1>,<icon2>,...,<icon8>', '<Ten Hao Quang>', -1, -1, <iconItemCuaItem>);
```
- `id` = flag id moi (0..192 da day -> tiep 193). **Noi qua byte** -> chi dung id <= 255, va server doc byte phai `& 0xFF` (da fix case -62, xem Buoc 5).
- `icon_data` = **danh sach 8 icon id vua gen** (dung dau phay, khong khoang trang).
- `icon_id` = icon ve flag (lay icon cua item hao quang, vd 32338).
- `gold/gem = -1` = khong ban/doi (giu nguyen nhu flag mac dinh).

### Buoc 4. Tao/Sua item -> TYPE 11

```sql
UPDATE item_template
SET TYPE = 11, part = <flagId>
WHERE id = <itemId>;
-- (neu tao item moi: head/body/leg = -1, gender = 3, icon_id = icon da chuan bi)
```
- `TYPE = 11` -> tu vao o hao quang (index 8).
- `part` = **flag_bag.id** (khong phai effect id nua!).

### Buoc 5. Code (chi can 1 lan - da co trong code, kiem tra la du)

Da implement, neu lam item moi **khong can sua gi nua**. Neu code bi revert/thieu, them lai:

1. `services/InventoryService.java` - **push realtime khi mac/thao**:
   - `itemBagToBody`: sau khi equip, `if (item.template.type == 11) Service.gI().sendFlagBag(player);`
   - `itemBodyToBag`: sau khi thao, cung goi `sendFlagBag(player)`.
   - (khong co dong nay -> chi hien flag khi login/relog)
2. `server/Controller.java` case **-62**: `_msg.reader().readByte() & 0xFF` (flag id > 127 bi am neu khong & 0xFF; case -63 da co san `& 0xFF`).
3. `data/DataGame.java` **vsItem++** (16 -> 17): buoc bat buoc khi doi TYPE/template -> client xoa cache template item. **Moi lan sua item_template thi tang them 1.**
4. `Player.getAura()` - khong can hook gi cho TYPE 11 (khong lien quan).

### Buoc 6. Icon version (TU DONG - chi verify)

`DataGame.extendSmallImageVersion()` chay luc boot: quet `icon_botnet/x2` lay maxIcon, keo dai bang `smallimage_version/x{1..4}` header = maxIcon+1.
- Khong can lam gi. **Verify sau boot:** header file = 2 byte BE = maxIcon+1.
  ```bash
  od -An -tx1 -N2 data/smallimage_version/x2/smallimage_version_data   #vd: 7e 5b = 32347
  ```

### Buoc 7. Compile + deploy + restart

```bash
cd BeMeoGaming
find src -name '*.java' > /tmp/tt-sources.txt && rm -rf /tmp/tt-c
javac -encoding UTF-8 -nowarn -d /tmp/tt-c -cp "lib/*" @/tmp/tt-sources.txt   # -> COMPILE_OK

# kill server + watchdog
powershell -NoProfile -Command 'Get-CimInstance Win32_Process | Where-Object { ($_.Name -eq "java.exe" -and $_.CommandLine -like "*server.ServerManager*") -or ($_.Name -eq "cmd.exe" -and $_.CommandLine -like "*run-watchdog*") } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }'
sleep 4
cp -rf /tmp/tt-c/* build/classes/

# boot lai
unset NoDefaultCurrentDirectoryInExePath
(MSYS_NO_PATHCONV=1 cmd /c open-panel.bat > /tmp/op.log 2>&1 &)
sleep 80
```
**Verify:**
```bash
grep -n "Server initialized" server_latest.log | tail -1     # co dong cuoi = boot OK
tail -n +<dongServerInitialized> server_latest.log | grep -c Exception   # phai = 0
netstat -ano | grep "14445.*LISTENING"                        # co LISTENING
grep "loaded flag bag" server_latest.log | tail -1            # count flag_bag tang dung so
```

### Buoc 8. Test in-game

1. **Relog client** (bat buoc - vsItem + icon version moi).
2. Chat `buff` -> ten nhan vat / `<itemId>` / 1.
3. Nhap dup item -> no tu vao **o hao quang** (thay o "Bó Hoa Hong" cu).
4. Hieu ung phai hien **ngay** (push realtime, khong can relog lan 2). Nguoi khac trong khu cung thay.
5. Sau khi test xong muon tra ve: xoa/sua dong SQL da them, tang vsItem++, restart.

---

## 3. CHECKLIST TRUOC KHI BAN GIAO

- [ ] PNG da co du `x2/x3/x4`, khong nen (alpha=true), frame 4 vien trong.
- [ ] `flag_bag` insert dung `icon_data` (8 id, dau phay), `part` item = flag id.
- [ ] item `TYPE = 11`.
- [ ] `vsItem` da tang.
- [ ] Boot: 0 exception, flag_bag count dung, smallimage header = maxIcon+1.
- [ ] Relog test: hien ngay khi mac, dung vi tri, dung kich thuoc.

---

## 4. BANG SU CO THUONG GAP

| Trieu chung | Nguyen nhan | Fix |
|---|---|---|
| Mac item khong thay hieu ung, khong ai request gi | Khong push `sendFlagBag` khi equip | Them `Service.gI().sendFlagBag(player)` o `itemBagToBody`/`itemBodyToBag` |
| Flag id > 127 khong duoc hoi frame | Controller -62 doc `readByte()` am | `readByte() & 0xFF` |
| Client dung template cu (item loi ve / van TYPE cu) | Khong tang vsItem | vsItem++ roi restart |
| Icon hoi ve nhung client khong biet co icon moi (o trong hanh trang) | smallimage_version khong du | Kiem tra header = maxIcon+1 (extend chay tu dong luc boot - neu khong thi xem DataGame.extendSmallImageVersion) |
| Hien aura cu / id 41 / khong phai art moi | Dung channel A (getAura/DataEffect) | **Sai channel** - chuyen sang TYPE 11 + flag_bag (channel B) |
| Art bi wrap/doi hinh | Rect DataEffect la u8 (<=255) | Pack 2D grid 3x3 (xem GenAura) |
| Hieu ung hien nhung lech cao/thap | Frame sai offset trong sheet | Chinh lai bang chia grid/noi frame trong GenFlagFrames |

---

## 5. APPENDIX - CAC CHANNEL KHONG DUNG (de nho, tranh lam lai)

- **getAura()** (`Player.java`) + msg `127 sub4` (`RadarService.setIDAuraEff`) + config `vip_aura_0..13` (panel tab He Thong, `data/config/system_tuning.properties`): chi dua duoc **id aura** (byte, <=127). Client chi render id no biet -> **khong dung de them art moi**. Hien tai ve default 19/40/41/42.
- **DataEffect / msg -66** (`DataGame.effData`, path `Eff/effect/x{z}/data|img/DataEffect_{id}`): client whitelist id. Format da reverse (DataEffectCodec verify 103/143 file byte-identical):
  - section1: `[u8 N][N x (idx,x,y,w,h)]` - field **u8**, toa do <= 255
  - section2: `[00][H] + H frame [c][c x (dx s16be, dy s16be, spriteIdx u8)]`
  - section3: `[u16be count][count x u16be frameIdx][0x3232]`
  - Art da ghi de vao id 41/19 da duoc **restore goc** tu backup.
- Item **1929 da chuyen thanh TYPE 11** - khong con nam o slot 5/cai trang nua.

---

## 6. TINH TRANG HIEN TAI (de lam tiep)

- Item **1929** `Hao Quang Hon Don Vo Cuc`: TYPE 11, part **193**, icon **32338** (da trong DB).
- flag_bag **193**: 8 frame icon **32339..32346** (cat tu ImgEffect_43, sheet GenAura sinh).
- Code da commit vao tree: push sendFlagBag (InventoryService), fix -62 (Controller), vsItem=17.
- Server da deploy + restart, boot 0 exception, flag_bag 194 dong, icon version 32347.

---

## 7. SINH FRAME TU ANH RONG NGANG CUA USER (item 1931 - quy trinh hien tai)

Art khong ve tay nua: lay truc tiep anh rong ngang user gui (1616x526, nen checkerboard da "nung" vao anh, 0% alpha) roi xe + uon quanh nguoi choi.

### Buoc 1 - cat nen -> `tools/res/hdragon.png` (alpha that)

```
javac -encoding UTF-8 -d /tmp/tt-tools tools/CutChecker.java
java -Xmx1g -cp /tmp/tt-tools CutChecker
```

4 pass: (1) flood tu bien qua pixel xam/trang nhiet do, (2) xoa vung nen bi bao kin, (3) mo rong 3 vong vao pixel lech nen ke sat (vet soi do resize), (4) day lai lo MONG ben trong than bang mau o lan can (xoi 14 lan ma van con = lo rong, giu trong). Output: `tools/res/hdragon.png` + preview `data/anh_the/hdragon_cut.png`.

### Buoc 2 - xe mang + uon -> 42 frame

```
javac -encoding UTF-8 -d /tmp/tt-tools tools/GenStripDragon.java
java -Xmx3g -cp /tmp/tt-tools GenStripDragon   # ~4.5 phut
```

- Tam ong than = **trung vi trong luong** alpha moi cot, lam mem +/-14 cot (dung trung binh/"do dai run dai nhat" se nhay, than bi vet doc).
- Moi cot nguon = 1 mang (`STRIP_W=1`, `OVERLAP=1`) affine dan theo duong helix 2 vong (PLAYER_X=540, PLAYER_Y=620, ORBIT_X=285, Y 1010->230, path ~2951px, than 2450px, kx=1.53). Mang lon (4px) se thay vet "gach" do phap tuyen cua mang khong khop voi mang ke.
- Sap xep theo depth (sau truoc) roi ve, khong dung alpha theo depth (se sinh vet noi khi mang trung nhau).
- Output: `data/icon_botnet/x{2,3,4}/32415..32456.png` + icon `32457.png` (crop dau), contact sheet `data/anh_the/strip_contact.png`.
- **Chi chay 3 zoom x2/x3/x4** (khong co x1). Xem anh: server preview chi phuc vu `.html` (PNG 404) -> dung `tools/MkPreview.java` de nhung base64 vao `data/anh_the/dragon.html`.

### Buoc 3 - byte icon (QUAN TRONG)

`DataGame.extendSmallImageVersion` chi **BO SUNG id moi** (header < newMax moi ghi), khong doi id da co -> gen lai anh cung id thi byte `len(png)%127` trong `smallimage_version_data` van la cua ban cu, client co the khong tai lai icon. Khi gen lai cung dai id:

```
javac -encoding UTF-8 -d /tmp/tt-tools tools/RefreshIconBytes.java
java -cp /tmp/tt-tools RefreshIconBytes 32415 32457
```

Doi x2/x3/x4 (x1 khong co icon trong dai nay), giu nguyen header. Backup `.bak2`.

### Buoc 4 - kiem tra sau restart

- `netstat -ano | grep "14445.*LISTENING"`
- `tail -n +<dong "Server initialized"> server_latest.log | grep -ci exception` = **0**
- `od -An -tx1 -N2 data/smallimage_version/x2/smallimage_version_data` = `7e ca` (32458 = maxIcon+1)
- `grep -i "loaded flag bag" server_latest.log` = 196

### Trang thai item 1931 (29/09/2026)

- flag_bag **195**: `icon_data` 32415..32456 (251 ky tu), `icon_id` **32457** - da trong DB.
- item **1931** `Cửu Thiên Thanh Long`: TYPE 11, part 195, icon 32457 - da trong DB.
- `sql/cuu_thien_thanh_long.sql` da dong bo voi dai id moi (dung de import tren server moi).
- 42 frame da gen bang anh user, server da restart, boot 0 exception.
- **Dang cho user relog test.** Neu lech/to nho -> chinh GenFlagFrames/GenAura roi chay lai Buoc 2 + restart.

## 8. HAO QUANG ANH DONG (item 1932 - Goku Vo Cuc, 30/09/2026)

Anh user co alpha that (khong cat checker) - tach 3 lop roi animate rieng:

1. **`tools/SplitGokuAura.java`**: doc `tools/res/goku_src.png`, tach thanh phan ra khoi base:
   - loc theo mau: tia set = bri>=224 & sat<=90; da bay = bri<=118 & b>=g & b-g>=8 & sat<=130
   - component 4-hop, loc kich thuoc (40..9000/26000), **khong cham HEAD_RECT/BODY_RECT** (giu Goku)
   - xoa thanh phan chon khoi base + inpaint lan can TB
   - output: `tools/res/goku_base.png`, `goku_layers.png` (atlas), `goku_layers.txt`
     (dong dau `W H count`, moi dong `type x y w h atlasY`, type 0=sets, 1=da)
   - debug: `data/anh_the/dbg_{bolt,rock,base}.png` + DiffImg de xem vung da xoa
2. **`tools/GenGokuAura.java`** [firstId] [frameCount]: render 42 frame **300x300 (x2)**
   (x3=450, x4=600; khoi luong 1080x1200 bi tran man hinh - da giam, `K = W/1080` ty le hieu ung)
   - base giu nguyen; da bay = sin dx/dy/rot/scale rieng tung sprite + sort chieu sau
   - tia set = alpha strobe + jitter + dropout; phu: 6 net laze procedural, 48 sparkle,
     soi hau quang breathing, cho duoi chan (elip), 2 flash/vong (het hop sang vi bat bien)
   - gen x2/x3/x4 (ZOOM_SCALES 1.0/1.5/2.0) + icon crop (355,55)-(720,405) + goku_contact.png
3. Preview: `tools/MkAnim.java out.html scale files...` -> HTML tu dong phat (JS doi anh 110ms),
   xem qua preview `http://127.0.0.1:61793/dragon.html?v=...` (server chi serve file .html co san).
4. **`RefreshIconBytes 32458 32500`** SAU khi gen lai (extend chi bo sung id moi).
5. DB: `sql/goku_vo_cuc.sql` = flag_bag **196** (icon_data 32458..32499, icon 32500)
   + item **1932** TYPE 11 part 196. Restart -> header `7e f5` (32501), flag bag 197.

### Trang thai item 1932 (30/09/2026)

- 42 frame + icon da gen, DB da ghi, server restart, boot 0 exception, byte x2/x3/x4 dung.
- **30/09/2026: giam frame 1080x1200 -> 300x300 (x2) vi bi tran man hinh** (user chon 300x300),
  gen lai + RefreshIconBytes 32458 32500 (khong can restart - sendSmallVersion doc file/lan gui).
- **Dang cho user relog test `buff 1932/1`.**

## 9. HAO QUANG PROCEDURAL 100% (item 1933 - Vong Xoay Hu Khong, 30/09/2026)

Khac 1931/1932 (cat anh user): hoan toanh ve bang Java2D, anh tham khao
`tools/res/void_src.png` chi de doi mau/sac do.

**Spec 10 tick = 8 giai doan** (trung binh 2 tick cho dinh diem -> dip hon 100ms ma khong dung yen):

| Tick | Giai doan | Noi dung |
|---|---|---|
| 1 | Khoi dong | ho den tim mo (r=34), vai hat neon li ti |
| 2 | Xoay dan | mo rong (r=44), 1 set do chat got chan -> dau goi |
| 3 | Nut ve | r=56, 4 manh pha le, set quanh dui/eo |
| 4-7 | **Dinh diem** | r=72..74, 7 set chang chi toan than (**seed rieng moi tick -> set nhay lien tuc**), 8 manh vay, red glow toan khung |
| 8 | Ha nhiet | r=58, 3 set yeu, manh ha do cao |
| 9 | Tan bien | het set, khi neon mem, ho dang dong |
| 10 | Ve moc | = tick 1 (envelope cuoi == dau) |

- **Vong lap khong giat**: quay deu 36 do/tick (360/10), moi giai doan end == start.
- **Mau**: tím than/đen thẳm (ho), Xanh neon (xoay), Do mau (set), tim/den (pha le).
- **`tools/GenVoidVortex.java`** [firstId]: ve 10 frame 300x300 (x2), envelope mang
  HOLE_R/HOLE_A/SWIRL_A/BOLT_N/BOLT_TOP/SHARD_N/SPARK_A/RED_PEAK.
- icon = frame dinh diem (i=3).
- DB: `sql/vong_xoay_hu_khong.sql` = flag_bag **197** (10 id 32501..32510, icon 32511)
  + item **1933** TYPE 11 part 197.

### Trang thai item 1933 (30/09/2026)

- 10 frame + icon da gen (300x300), DB da ghi, server restart,
  boot 0 exception, flag bag 198, header `7f 00` (32512), byte x1..x4 dung.
- **Dang cho user relog test `buff 1933/1`.**
