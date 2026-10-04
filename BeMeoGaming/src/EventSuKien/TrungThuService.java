package EventSuKien;

import java.util.ArrayList;
import java.util.List;

import consts.ConstNpc;
import item.Item;
import jdbc.daos.PlayerDAO;
import map.ItemMap;
import map.Zone;
import npc.Npc;
import panel.tuning.TrungThuTuning;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.ItemTimeService;
import services.Service;
import Mail.HomThuService;
import utils.Util;

/**
 * Nghiep vu su kien Trung Thu.
 *
 * Tat ca chi so doc tu panel.tuning.TrungThuTuning
 * (data/config/trungthu_tuning.properties) de admin chinh sua tren panel.
 *
 * Don vi: "k" = x1000 -> 7k = 7000 (7000%), 5k = 5000 (5000%).
 */
public class TrungThuService {

    private static final TrungThuService INSTANCE = new TrungThuService();

    public static TrungThuService gI() {
        return INSTANCE;
    }

    // =====================================================================
    // 0. TONG: bat/tat su kien + NPC
    // =====================================================================

    /** Su kien dang bat? (panel toggle: bat = hien NPC, tat = an NPC) */
    public boolean isOn() {
        return TrungThuTuning.isOn();
    }

    /** NPC nay co thuoc su kien Trung Thu khong (66 noi banh / 92 trung thu cu)? */
    public boolean isEventNpc(int tempId) {
        return tempId == ConstNpc.NOI_BANH || tempId == ConstNpc.EventTrungThu;
    }

    /**
     * NPC co hien voi player khong?
     * - Su kien tat -> an ca 66 va 92.
     * - Su kien bat -> chi con NPC 66 tren map lang (92 bi thay the).
     */
    public boolean isVisible(Npc npc) {
        if (npc == null) return false;
        if (!isEventNpc(npc.tempId)) return true;
        if (!isOn()) return false;
        if (npc.tempId == ConstNpc.EventTrungThu) {
            // NPC 92 bi thay the tren cac map lang da dat noi banh
            return !isNpcMap(npc.mapId);
        }
        // NPC noi banh 66 chi dat tren map lang
        return isNpcMap(npc.mapId);
    }

    /**
     * Map duoc phep dat NPC noi banh (66) - ho tro nhieu map, ngan cach bang dau phay.
     * Mac dinh 7 (Lang Mori) - vi chi map 7 co NPC Trung Thu (92) de thay the.
     * VD: "7" hoac "0,7,14,16,163" (cac lang).
     */
    public boolean isNpcMap(int mapId) {
        String raw = TrungThuTuning.get("npc.map");
        if (raw.isEmpty()) raw = "7";
        for (String s : raw.split(",")) {
            try {
                if (Integer.parseInt(s.trim()) == mapId) return true;
            } catch (Exception ignore) {
            }
        }
        return false;
    }

    // =====================================================================
    // 1. DO ROI (moi lan ha quai)
    // =====================================================================

    public int tailBonusPercent() {
        return TrungThuTuning.getInt("drop.tailBonus", 10);
    }

    public long tailDurationMs() {
        return TrungThuTuning.getLong("drop.tailSeconds", 900) * 1000L;
    }

    /** Buff Duoi khic con hieu luc khong? */
    public boolean hasTailBuff(Player pl) {
        if (pl == null || pl.itemTime == null) return false;
        if (!pl.itemTime.isDuoiKhiTT) return false;
        if (Util.canDoWithTime(pl.itemTime.lastTimeDuoiKhiTT, tailDurationMs())) {
            pl.itemTime.isDuoiKhiTT = false;
            return false;
        }
        return true;
    }

    /**
     * Kich hoat buff Duoi khic: +ty le roi vat pham SK Trung Thu trong 15 phut.
     * Co day du icon buff tren thanh thong bao.
     */
    public void activateTailBuff(Player pl) {
        long ms = tailDurationMs();
        pl.itemTime.isDuoiKhiTT = true;
        pl.itemTime.lastTimeDuoiKhiTT = System.currentTimeMillis();
        ItemTimeService.gI().sendItemTime(pl, 5072, (int) (ms / 1000));
    }

    /**
     * Roll do roi Trung Thu cho 1 lan ha quai.
     * Bang trong so tong = 100 -> moi lan ha quai rot dung 1 vat pham su kien.
     * Neu dang buff Duoi khic -> co +ty le% roi THEM 1 lan nua.
     */
    public void addEventDrops(Player pl, Zone zone, int x, int yEnd, List<ItemMap> out) {
        try {
            if (!isOn() || pl == null || zone == null || out == null) return;
            rollOnce(pl, zone, x, yEnd, out);
            if (hasTailBuff(pl) && Util.isTrue(tailBonusPercent(), 100)) {
                rollOnce(pl, zone, x, yEnd, out);
            }
        } catch (Exception e) {
            // khong de loi do roi lam dung chinh quai
        }
    }

    private void rollOnce(Player pl, Zone zone, int x, int yEnd, List<ItemMap> out) {
        List<int[]> table = dropTable();
        if (table.isEmpty()) return;
        int total = 0;
        for (int[] e : table) total += e[2];
        if (total <= 0) return;
        int roll = Util.nextInt(1, total);
        int cum = 0;
        for (int[] e : table) {
            cum += e[2];
            if (roll <= cum) {
                out.add(new ItemMap(zone, e[0], Math.max(1, e[1]), x, yEnd, pl.id));
                return;
            }
        }
    }

    /** Doc cac khoa drop.N = itemId|soLuong|tyLe% */
    public List<int[]> dropTable() {
        List<int[]> table = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            String raw = TrungThuTuning.get("drop." + i);
            if (raw.isEmpty()) continue;
            String[] p = raw.split("\\|");
            if (p.length < 3) continue;
            try {
                int id = Integer.parseInt(p[0].trim());
                int qty = Integer.parseInt(p[1].trim());
                int rate = (int) TrungThuTuning.parseRate(p[2]);
                if (id <= 0 || rate <= 0) continue;
                table.add(new int[]{id, qty, rate});
            } catch (Exception ignore) {
            }
        }
        return table;
    }

    // =====================================================================
    // 2. NAU BANH - tru nguyen lieu + tien nap TRUOC, roi moi cong diem sukien
    //    (khong nhan banh - diem vao sukien de dua top Trung Thu)
    // =====================================================================

    public static class Recipe {
        public int points;
        public List<int[]> mats = new ArrayList<>(); // {itemId, qty}
    }

    public Recipe getRecipe(int index) {
        String raw = TrungThuTuning.get("cook.recipe." + index);
        if (raw.isEmpty()) return null;
        Recipe r = new Recipe();
        String[] parts = raw.split("\\|");
        if (parts.length == 0) return null;
        try {
            r.points = (int) TrungThuTuning.parseRate(parts[0]);
        } catch (Exception e) {
            return null;
        }
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            for (String s : parts[1].split(",")) {
                String[] kv = s.trim().split(":");
                if (kv.length != 2) continue;
                try {
                    r.mats.add(new int[]{Integer.parseInt(kv[0].trim()), Integer.parseInt(kv[1].trim())});
                } catch (Exception ignore) {
                }
            }
        }
        if (r.mats.isEmpty()) return null;
        return r;
    }

    public int cookFeeVnd() {
        return TrungThuTuning.getInt("cook.feeVnd", 49000);
    }

    /**
     * Nau banh: kiem tra du nguyen lieu + du tien nap
     * -> TRU NGUYEN LIEU -> TRU TIEN NAP -> MOI CONG DIEM sukien.
     * Khong trao loai banh.
     */
    public void cook(Player player, int index) {
        if (player == null) return;
        if (!isOn()) {
            Service.getInstance().sendThongBaoFromAdmin(player, "Su kien Trung Thu dang tat");
            return;
        }
        Recipe r = getRecipe(index);
        if (r == null) {
            Service.getInstance().sendThongBaoFromAdmin(player, "Lua chon khong hop le!");
            return;
        }
        int fee = cookFeeVnd();

        // --- kiem tra (chua tru gi) ---
        for (int[] m : r.mats) {
            Item it = InventoryService.gI().findItemBag(player, m[0]);
            if (it == null || it.quantity < m[1]) {
                Service.getInstance().sendThongBaoFromAdmin(player, "Khong du nguyen lieu!");
                return;
            }
        }
        if (player.getSession() == null || player.getSession().vnd < fee) {
            Service.getInstance().sendThongBaoFromAdmin(player,
                    "Khong du tien nap! Can " + Util.format(fee) + " VND moi lan nau banh.");
            return;
        }

        // --- TRU NGUYEN LIEU TRUOC ---
        for (int[] m : r.mats) {
            Item it = InventoryService.gI().findItemBag(player, m[0]);
            if (it != null) {
                InventoryService.gI().subQuantityItemsBag(player, it, m[1]);
            }
        }
        // --- TRU TIEN NAP (tien nap thuc te) ---
        PlayerDAO.subcash(player, fee);
        PlayerDAO.updateVND(player);

        // --- MOI PHAT THUONG: diem sukien (dua top su kien Trung Thu) ---
        player.sukien += r.points;

        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        Service.getInstance().point(player);
        Service.getInstance().sendThongBaoFromAdmin(player,
                "Nau banh thanh cong! +" + r.points + " Diem Su Kien Trung Thu.");
    }

    // =====================================================================
    // 3. DOI DIEM - TRU DIEM TRUOC -> moi phat thuong
    // =====================================================================

    public static class Exchange {
        public int need;
        public long sukienBonus;
        /** cac mon qua: {itemId, soLuong, optionId, thamSoOption} */
        public List<int[]> rewards = new ArrayList<>();
        public String label = "";
    }

    /**
     * Doc mot moc doi diem.
     * Dinh dang moi:  <diemCan>|<sukienCongThem>|<itemId>:<sl>:<optId>:<optParam>[,...]
     * Dinh dang cu (van doc duoc): <diemCan>|<itemId>|<soLuong>|<sukienCongThem>
     */
    public Exchange getExchange(int index) {
        String raw = TrungThuTuning.get("exchange." + index);
        if (raw.isEmpty()) return null;
        String[] p = raw.split("\\|");
        if (p.length < 2) return null;
        try {
            Exchange e = new Exchange();
            boolean isNew = p.length >= 3 && p[2].contains(":");
            if (isNew) {
                e.need = (int) TrungThuTuning.parseRate(p[0]);
                e.sukienBonus = (long) TrungThuTuning.parseRate(p[1]);
                for (String s : p[2].split(",")) {
                    int[] r = parseReward(s);
                    if (r != null) e.rewards.add(r);
                }
            } else if (p.length >= 4) {
                // dinh dang cu
                e.need = (int) TrungThuTuning.parseRate(p[0]);
                e.rewards.add(new int[]{Integer.parseInt(p[1].trim()), Integer.parseInt(p[2].trim()), 30, 1});
                e.sukienBonus = (long) TrungThuTuning.parseRate(p[3]);
            } else {
                return null;
            }
            // qua doi kem (dinh dang cu exchange.N.extra)
            String extra = TrungThuTuning.get("exchange." + index + ".extra");
            if (!extra.isEmpty()) {
                int[] r = parseReward(extra.replace("|", ":"));
                if (r != null) e.rewards.add(r);
            }
            if (e.rewards.isEmpty()) return null;
            return e;
        } catch (Exception e) {
            return null;
        }
    }

    /** "1559:50:30:1" -> {1559,50,30,1}; ho tro it hon 4 phan. */
    private int[] parseReward(String s) {
        try {
            String[] a = s.trim().split(":");
            if (a.length < 2) return null;
            int id = Integer.parseInt(a[0].trim());
            int qty = Math.max(1, Integer.parseInt(a[1].trim()));
            int opt = a.length > 2 ? Integer.parseInt(a[2].trim()) : 0;
            int par = a.length > 3 ? (int) TrungThuTuning.parseRate(a[3]) : 0;
            if (id <= 0) return null;
            return new int[]{id, qty, opt, par};
        } catch (Exception e) {
            return null;
        }
    }

    /** Tat ca moc doi diem, liet ke lien tuc tu 0 (panel luu lien tuc). */
    public List<Exchange> allExchanges() {
        List<Exchange> out = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            Exchange e = getExchange(i);
            if (e == null) break;
            out.add(e);
        }
        return out;
    }

    /** Doi diem: kiem tra -> TRU point_vip TRUOC -> gui qua qua thu -> cong sukien. */
    public void exchange(Player player, int index) {
        if (player == null) return;
        if (!isOn()) {
            Service.getInstance().sendThongBaoFromAdmin(player, "Su kien Trung Thu dang tat");
            return;
        }
        List<Exchange> list = allExchanges();
        if (index < 0 || index >= list.size()) {
            Service.getInstance().sendThongBaoFromAdmin(player, "Lua chon khong hop le!");
            return;
        }
        Exchange e = list.get(index);
        if (player.point_vip < e.need) {
            Service.getInstance().sendThongBao(player,
                    "Khong du diem! Can " + Util.format(e.need) + " diem, ban co "
                            + Util.format(player.point_vip) + " diem.");
            return;
        }

        // --- TRU DIEM TRUOC ---
        player.point_vip -= e.need;

        // --- ROI MOI PHAT THUONG ---
        StringBuilder got = new StringBuilder();
        for (int[] r : e.rewards) {
            Item goiqua = ItemService.gI().createNewItem(r[0], r[1]);
            if (goiqua.template == null) continue;
            if (r[2] > 0) {
                goiqua.itemOptions.add(new item.Item.ItemOption(r[2], r[3]));
            } else {
                goiqua.itemOptions.add(new item.Item.ItemOption(30, 1)); // mac dinh: Da Khoa
            }
            InventoryService.gI().addItemMail(player, goiqua);
            if (got.length() > 0) got.append(" + ");
            got.append(goiqua.template.name).append(" x").append(r[1]);
        }
        HomThuService.gI().sendListMail(player);

        // diem dua top su kien
        player.sukien += e.sukienBonus;

        Service.getInstance().sendMoney(player);
        Service.getInstance().point(player);
        Service.getInstance().sendThongBao(player,
                "Nhan duoc: " + got + "\nCong " + e.sukienBonus + " Diem Su Kien Trung Thu");
    }

    // =====================================================================
    // 4. RUONG TRUNG THU (item moi 1914, icon cua Hop Trung Thu 1512)
    // =====================================================================

    public int boxId() {
        return TrungThuTuning.getInt("box.id", 1914);
    }

    public int costumeRate() {
        return TrungThuTuning.getInt("box.costumeRate", 5);
    }

    /** Mo ruong Trung Thu: ty le cai trang 5%, con lai la cac phan qua cau hinh. */
    public void openBox(Player pl, Item item) {
        if (pl == null || item == null) return;
        if (!isOn()) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Su kien Trung Thu dang tat");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 2) {
            Service.getInstance().sendThongBao(pl, "Hanh trang khong du trong de mo ruong.");
            return;
        }
        short icon1 = item.template != null ? item.template.iconID : -1;
        List<Item> got = new ArrayList<>();

        // --- cai trang 5% ---
        if (Util.isTrue(costumeRate(), 100)) {
            String range = TrungThuTuning.get("box.costumeRange");
            int min = 282, max = 292;
            try {
                String[] rr = range.split("-");
                min = Integer.parseInt(rr[0].trim());
                max = Integer.parseInt(rr[1].trim());
            } catch (Exception ignore) {
            }
            if (max < min) max = min;
            Item ct = ItemService.gI().createNewItem(Util.nextInt(min, max), 1);
            if (ct.template != null) {
                ct.itemOptions.add(new item.Item.ItemOption(Util.nextInt(1, 99), 1));
                ct.itemOptions.add(new item.Item.ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, ct, 99999999);
                got.add(ct);
            }
        }

        // --- cac phan qua cau hinh (option su so nguyen day du: 7000 = 7k%, 5000 = 5k%) ---
        for (int i = 0; i < 32; i++) {
            String raw = TrungThuTuning.get("box.reward." + i);
            if (raw.isEmpty()) continue;
            String[] p = raw.split("\\|");
            if (p.length < 4) continue;
            try {
                int id = Integer.parseInt(p[0].trim());
                int qty = Math.max(1, Integer.parseInt(p[1].trim()));
                int optId = Integer.parseInt(p[2].trim());
                int optParam = (int) TrungThuTuning.parseRate(p[3]);
                Item it = ItemService.gI().createNewItem(id, qty);
                if (it.template == null) continue;
                if (optId > 0) {
                    it.itemOptions.add(new item.Item.ItemOption(optId, optParam));
                } else {
                    it.itemOptions.add(new item.Item.ItemOption(30, 1)); // mac dinh: Da Khoa
                }
                InventoryService.gI().addItemBag(pl, it, 99999999);
                got.add(it);
            } catch (Exception ignore) {
            }
        }

        if (got.isEmpty()) {
            Service.getInstance().sendThongBao(pl, "Ruong rong, hay thu lai sau.");
            return;
        }

        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
        if (icon1 != -1 && !got.isEmpty()) {
            models.Combine.CombineService.gI().sendEffectOpenItem(pl, icon1, got.get(0).template.iconID);
        }
        Service.getInstance().sendThongBao(pl, "Mo ruong thanh cong: " + got.size() + " vat pham!");
    }

    // =====================================================================
    // 5. BOSS
    // =====================================================================

    /** Thoi gian spawn lai sau khi boss chet (giay), mac dinh 15s, khong gioi han. */
    public int bossRestSeconds() {
        return TrungThuTuning.getInt("boss.restSeconds", 15);
    }

    /**
     * Sat thuong co dinh boss Trung Thu gay ra - ap dung SAU KHI da tinh
     * het moi bo dieu chinh (giap, shield, crit, %...) -> 2.000.000.
     */
    public long bossFixedDamage() {
        return TrungThuTuning.getLong("boss.damage", 2000000);
    }

    public int bossSpawnCount() {
        int n = TrungThuTuning.getInt("boss.spawnCount", 10);
        return n < 1 ? 1 : n;
    }

    /** Boss co thuoc su kien Trung Thu khong (Khi Dot / Nguyet Than / Nhat Than)? */
    public boolean isEventBoss(long bossId) {
        String list = TrungThuTuning.get("boss.list").toUpperCase();
        String[] names = list.split(",");
        for (String n : names) {
            String key = n.trim();
            if (key.isEmpty()) continue;
            try {
                int id = (int) boss.BossID.class.getField(key).getInt(null);
                if (id == bossId) return true;
            } catch (Exception ignore) {
            }
        }
        return bossId == boss.BossID.KHIDOT || bossId == boss.BossID.NGUYETTHAN
                || bossId == boss.BossID.NHATTHAN;
    }

    /**
     * Kiem tra 1 player co phai boss su kien Trung Thu khong.
     * Dung khi dang o duong dan gay sat thuong (plAtt.isBoss = true).
     */
    public boolean isEventBossPlayer(Player p) {
        if (p == null || !p.isBoss || !isOn()) return false;
        if (isEventBoss(p.id)) return true;
        // fallback theo ten boss (truong hop id bi doi)
        String list = TrungThuTuning.get("boss.list").toUpperCase();
        String pname = p.name == null ? "" : p.name.trim().toUpperCase();
        if (pname.isEmpty()) return false;
        for (String n : list.split(",")) {
            String key = n.trim();
            if (key.isEmpty()) continue;
            try {
                java.lang.reflect.Field f = boss.BossesData.class.getField(key);
                Object d = f.get(null);
                String nm = (String) d.getClass().getMethod("getName").invoke(d);
                if (nm != null && nm.trim().toUpperCase().equals(pname)) return true;
            } catch (Exception ignore) {
            }
        }
        return false;
    }
}
