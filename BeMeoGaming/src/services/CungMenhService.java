package services;

import consts.ConstItem;
import item.Item;
import jdbc.daos.PlayerDAO;
import player.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cung Menh - tinh nang nang cap tai NPC Bo Mong (Rung Karin, map 47/84).
 *
 * TOAN BO cau hinh nam trong DB (sua bang Admin Panel > tab "Cung Menh"):
 *   - cung_menh_config : bao cap (max_level), % HP/KI/SD moi cap, HP/KI/SD cong
 *                        thang moi cap, cong thuc gia Manh moi cap, cong thuc dot pha.
 *   - cung_menh_option : "moi bac tang option nao" - 1 dong = (moc cap, option, param).
 *                        Dat cap >= moc la duoc cong option do (cong don nhieu moc).
 *
 * Chi so KHONG con cong vinh vien vao hpg/mpg/dameg nua: bonus duoc tinh lai moi
 * lan calPoint() (xem CungMenhService.applyBonus duoc goi tu NPoint), nho vay admin
 * sua cau hinh la nguoi choi nhan dung ngay, khong phai nang cap lai.
 *
 * Du lieu cap/dot pha cua nguoi choi luu o cot player.cung_menh (JSON {"level":x,"dotPha":y}).
 */
public class CungMenhService {

    private static CungMenhService I;

    public static CungMenhService gI() {
        if (I == null) {
            I = new CungMenhService();
        }
        return I;
    }

    // ============ CAU HINH (nap tu DB, cache trong RAM) ============
    public static class Config {
        public int maxLevel = 120;
        public double hpPercent = 0, kiPercent = 0, damePercent = 0;   // % moi cap
        public long hpFlat = 2000, kiFlat = 2000, dameFlat = 200;      // cong thang moi cap
        public int manhBase = 2, manhStep = 2, manhExtra = 1;
        public int dotPhaEvery = 10;
        public int dotPhaManhBase = 15, dotPhaManhStep = 5;
        public int dotPhaNgocBase = 1, dotPhaNgocStep = 1;
    }

    /** 1 dong cau hinh "bac": dat cap >= level thi duoc cong option nay. */
    public static class Bac {
        public int id;
        public int level;
        public int optionId;
        public long param;
        public String note = "";

        public Bac(int id, int level, int optionId, long param, String note) {
            this.id = id;
            this.level = level;
            this.optionId = optionId;
            this.param = param;
            this.note = note == null ? "" : note;
        }
    }

    // volatile: applyBonus() duoc goi rat nhieu lan (moi lan tinh lai chi so) nen chi doc,
    // khong khoa lock; chi reload() moi ghi (co synchronized).
    private volatile Config cfg = new Config();
    private volatile List<Bac> bacs = new ArrayList<>();
    private volatile Map<Integer, List<Bac>> bacsTheoCap = new LinkedHashMap<>();
    private volatile boolean loaded = false;

    /** Cau hinh dang dung (tu nap lan dau, hoac sau khi panel luu). */
    public Config config() {
        if (!loaded) {
            reload();
        }
        return cfg;
    }

    public List<Bac> listBac() {
        if (!loaded) {
            reload();
        }
        return new ArrayList<>(bacs);
    }

    /** Nap lai cau hinh tu DB. Goi sau moi lan panel luu. */
    public synchronized void reload() {
        ensureTables();
        Config c = new Config();
        List<Bac> list = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT max_level, hp_percent, ki_percent, dame_percent, hp_flat, ki_flat, dame_flat, manh_base, manh_step, manh_extra, dot_pha_every, dot_pha_manh_base, dot_pha_manh_step, dot_pha_ngoc_base, dot_pha_ngoc_step FROM cung_menh_config WHERE id=1");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.maxLevel = Math.max(1, rs.getInt("max_level"));
                    c.hpPercent = rs.getDouble("hp_percent");
                    c.kiPercent = rs.getDouble("ki_percent");
                    c.damePercent = rs.getDouble("dame_percent");
                    c.hpFlat = rs.getLong("hp_flat");
                    c.kiFlat = rs.getLong("ki_flat");
                    c.dameFlat = rs.getLong("dame_flat");
                    c.manhBase = Math.max(1, rs.getInt("manh_base"));
                    c.manhStep = Math.max(1, rs.getInt("manh_step"));
                    c.manhExtra = rs.getInt("manh_extra");
                    c.dotPhaEvery = Math.max(1, rs.getInt("dot_pha_every"));
                    c.dotPhaManhBase = Math.max(0, rs.getInt("dot_pha_manh_base"));
                    c.dotPhaManhStep = rs.getInt("dot_pha_manh_step");
                    c.dotPhaNgocBase = Math.max(0, rs.getInt("dot_pha_ngoc_base"));
                    c.dotPhaNgocStep = rs.getInt("dot_pha_ngoc_step");
                }
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT id, level, option_id, param, note FROM cung_menh_option ORDER BY level, id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Bac(rs.getInt("id"), rs.getInt("level"), rs.getInt("option_id"),
                            rs.getLong("param"), rs.getString("note")));
                }
            }
        } catch (Exception e) {
            // giu cau hinh mac dinh neu DB loi, khong lam sap server
        }
        // sap xep cache theo cap de tra cuu nhanh
        Map<Integer, List<Bac>> map = new LinkedHashMap<>();
        for (Bac b : list) {
            map.computeIfAbsent(b.level, k -> new ArrayList<>()).add(b);
        }
        this.cfg = c;
        this.bacs = list;
        this.bacsTheoCap = map;
        this.loaded = true;
    }

    /** Tu tao bang neu DB chua co (de panel/server chay duoc ngay ca khi chua import SQL). */
    private void ensureTables() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS cung_menh_config (id TINYINT NOT NULL PRIMARY KEY, max_level INT NOT NULL DEFAULT 120, hp_percent DOUBLE NOT NULL DEFAULT 0, ki_percent DOUBLE NOT NULL DEFAULT 0, dame_percent DOUBLE NOT NULL DEFAULT 0, hp_flat BIGINT NOT NULL DEFAULT 2000, ki_flat BIGINT NOT NULL DEFAULT 2000, dame_flat BIGINT NOT NULL DEFAULT 200, manh_base INT NOT NULL DEFAULT 2, manh_step INT NOT NULL DEFAULT 2, manh_extra INT NOT NULL DEFAULT 1, dot_pha_every INT NOT NULL DEFAULT 10, dot_pha_manh_base INT NOT NULL DEFAULT 15, dot_pha_manh_step INT NOT NULL DEFAULT 5, dot_pha_ngoc_base INT NOT NULL DEFAULT 1, dot_pha_ngoc_step INT NOT NULL DEFAULT 1, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)")) {
                ps.execute();
            }
            try (PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS cung_menh_option (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, level INT NOT NULL, option_id INT NOT NULL, param BIGINT NOT NULL DEFAULT 0, note VARCHAR(255) NOT NULL DEFAULT '', KEY idx_level (level)) DEFAULT CHARSET=utf8mb4")) {
                ps.execute();
            }
            try (PreparedStatement ps = con.prepareStatement("INSERT IGNORE INTO cung_menh_config (id) VALUES (1)")) {
                ps.execute();
            }
        } catch (Exception e) {
        }
    }

    // ============ DI TRU BONUS CU ============
    /** Bonus cua ban cu (truoc 24/09/2026): cong thang vinh vien vao hpg/mpg/dameg moi cap. */
    private static final long OLD_HP_PER_LEVEL = 2000L;
    private static final long OLD_KI_PER_LEVEL = 2000L;
    private static final long OLD_DAME_PER_LEVEL = 200L;

    /**
     * Goi 1 lan ngay sau khi load nhan vat: neu nhan vat da nang cap theo cong thuc CU
     * (bonus nam san trong hpg/mpg/dameg) thi tru no di, vi tu gio chi so duoc tinh lai
     * tu bang cau hinh (neu khong tru se bi cong doi). Chi chay 1 lan / nhan vat.
     */
    public void migrateOldBonus(Player player) {
        if (player == null || player.nPoint == null || !player.isPl()) {
            return;
        }
        if (player.cungMenhFixedOldBonus || player.cungMenhLevel <= 0) {
            return;
        }
        long lv = player.cungMenhLevel;
        try {
            player.nPoint.hpg = Math.max(0, player.nPoint.hpg - OLD_HP_PER_LEVEL * lv);
            player.nPoint.mpg = Math.max(0, player.nPoint.mpg - OLD_KI_PER_LEVEL * lv);
            player.nPoint.dameg = Math.max(0, player.nPoint.dameg - OLD_DAME_PER_LEVEL * lv);
            player.cungMenhFixedOldBonus = true;
            PlayerDAO.saveCungMenh(player);
        } catch (Exception e) {
        }
    }

    // ============ CONG THUC GIA (doc tu cau hinh) ============
    /** So Manh Tinh Tu de nang len cap newLevel. */
    public int manhForLevel(int newLevel) {
        Config c = config();
        int step = Math.max(1, c.manhStep);
        int extra = Math.max(0, c.manhExtra);
        return c.manhBase + ((Math.max(1, newLevel) - 1) / step) * extra;
    }

    /** Dot pha lan thu n: so Manh can. */
    public int manhForDotPha(int lan) {
        Config c = config();
        return Math.max(0, c.dotPhaManhBase + (Math.max(1, lan) - 1) * c.dotPhaManhStep);
    }

    /** Dot pha lan thu n: so Ngoc can. */
    public int ngocForDotPha(int lan) {
        Config c = config();
        return Math.max(0, c.dotPhaNgocBase + (Math.max(1, lan) - 1) * c.dotPhaNgocStep);
    }

    /** Bao cap hien tai (admin chinh trong panel). */
    public int maxLevel() {
        return config().maxLevel;
    }

    // ============ CONG CHI SO (goi tu NPoint moi lan tinh lai chi so) ============
    /**
     * Cong bonus Cung Menh vao chi so: % HP/KI/SD moi cap, cong thang moi cap,
     * va toan bo option cua cac bac da dat.
     */
    public void applyBonus(Player player, player.NPoint np) {
        if (player == null || np == null || !player.isPl()) {
            return;
        }
        int lv = player.cungMenhLevel;
        if (lv <= 0) {
            return;
        }
        Config c = config();
        if (c.hpPercent != 0) np.tlHp.add((long) Math.round(c.hpPercent * lv));
        if (c.kiPercent != 0) np.tlMp.add((long) Math.round(c.kiPercent * lv));
        if (c.damePercent != 0) np.tlDame.add((long) Math.round(c.damePercent * lv));
        if (c.hpFlat != 0) np.hpAdd += c.hpFlat * lv;
        if (c.kiFlat != 0) np.mpAdd += c.kiFlat * lv;
        if (c.dameFlat != 0) np.dameAdd += c.dameFlat * lv;
        for (Bac b : bacTheoCap(lv)) {
            np.addCungMenhOption(b.optionId, b.param);
        }
    }

    /** Tat ca bac co moc cap <= level da dat. */
    private List<Bac> bacTheoCap(int level) {
        if (!loaded) {
            reload();
        }
        List<Bac> out = new ArrayList<>();
        for (Map.Entry<Integer, List<Bac>> e : bacsTheoCap.entrySet()) {
            if (e.getKey() <= level) {
                out.addAll(e.getValue());
            }
        }
        return out;
    }

    /** Mo ta bonus 1 cap duoi dang chuoi (dung cho menu NPC). */
    public String bonusMoiCap() {
        Config c = config();
        StringBuilder sb = new StringBuilder();
        if (c.hpFlat != 0 || c.hpPercent != 0) {
            sb.append("|5|HP ");
            if (c.hpFlat != 0) sb.append("+").append(format(c.hpFlat));
            if (c.hpPercent != 0) sb.append(c.hpFlat != 0 ? " & " : "+").append(trim(c.hpPercent)).append("%");
            sb.append("\n");
        }
        if (c.kiFlat != 0 || c.kiPercent != 0) {
            sb.append("|5|KI ");
            if (c.kiFlat != 0) sb.append("+").append(format(c.kiFlat));
            if (c.kiPercent != 0) sb.append(c.kiFlat != 0 ? " & " : "+").append(trim(c.kiPercent)).append("%");
            sb.append("\n");
        }
        if (c.dameFlat != 0 || c.damePercent != 0) {
            sb.append("|5|Sức đánh ");
            if (c.dameFlat != 0) sb.append("+").append(format(c.dameFlat));
            if (c.damePercent != 0) sb.append(c.dameFlat != 0 ? " & " : "+").append(trim(c.damePercent)).append("%");
            sb.append("\n");
        }
        return sb.toString();
    }

    // ============ STATE ============
    public int getLevel(Player player) {
        return player.cungMenhLevel;
    }

    public int getDotPha(Player player) {
        return player.cungMenhDotPha;
    }

    public int dotPhaEvery() {
        return config().dotPhaEvery;
    }

    /** Co the nang cap tiep khong (cu moi dotPhaEvery cap phai dot pha 1 lan). */
    public boolean canNangCap(Player player) {
        int lv = player.cungMenhLevel;
        int every = dotPhaEvery();
        if (lv >= maxLevel()) {
            return false;
        }
        return lv % every != 0 || player.cungMenhDotPha >= lv / every;
    }

    public int manhCanNangCapTiep(Player player) {
        return manhForLevel(player.cungMenhLevel + 1);
    }

    public int manhCanDotPha(Player player) {
        return manhForDotPha(player.cungMenhDotPha + 1);
    }

    public int ngocCanDotPha(Player player) {
        return ngocForDotPha(player.cungMenhDotPha + 1);
    }

    public long countManh(Player player) {
        Item it = InventoryService.gI().findItemBag(player, ConstItem.MANH_TINH_TU);
        return it == null ? 0 : it.quantity;
    }

    public long countNgoc(Player player) {
        Item it = InventoryService.gI().findItemBag(player, ConstItem.NGOC_TINH_DO);
        return it == null ? 0 : it.quantity;
    }

    // ============ MENU ============
    public String menuText(Player player) {
        int lv = player.cungMenhLevel;
        int dp = player.cungMenhDotPha;
        int every = dotPhaEvery();
        boolean canNang = canNangCap(player);
        StringBuilder sb = new StringBuilder();
        sb.append("|1|CUNG MỆNH\n");
        sb.append("|7|Cấp hiện tại: ").append(lv).append("/").append(maxLevel());
        if (dp > 0) {
            sb.append(" (Đột phá ").append(dp).append(")");
        }
        sb.append("\n");
        if (lv > 0) {
            String bonus = bonusMoiCap();
            if (!bonus.isEmpty()) {
                sb.append("|7|Đang có mỗi cấp:\n").append(bonus);
            }
            sb.append("|5|→ Cộng dồn cả ").append(lv).append(" cấp: ")
              .append("HP ").append(format(Math.round(config().hpFlat * lv)))
              .append(config().hpPercent != 0 ? " + " + trim(config().hpPercent * lv) + "%" : "")
              .append(" | KI ").append(format(Math.round(config().kiFlat * lv)))
              .append(config().kiPercent != 0 ? " + " + trim(config().kiPercent * lv) + "%" : "")
              .append(" | SĐ ").append(format(Math.round(config().dameFlat * lv)))
              .append(config().damePercent != 0 ? " + " + trim(config().damePercent * lv) + "%" : "")
              .append("\n");
        }
        sb.append("|7|Mảnh Tinh Tú: ").append(format(countManh(player)))
          .append(" - Ngọc Tinh Đồ: ").append(format(countNgoc(player))).append("\n");
        if (lv >= maxLevel()) {
            sb.append("|2|Con đã đạt cấp tối đa của Cung Mệnh!\n");
        } else if (!canNang) {
            sb.append("|3|Đang cần ĐỘT PHA để lên cấp tiếp (mỗi ").append(every).append(" cấp):\n");
            sb.append("|6|Cần ").append(manhCanDotPha(player)).append(" Mảnh + ")
              .append(ngocCanDotPha(player)).append(" Ngọc Tinh Đồ\n");
        } else {
            sb.append("|4|Nâng lên cấp ").append(lv + 1).append(": cần ")
              .append(manhCanNangCapTiep(player)).append(" Mảnh Tinh Tú\n");
        }
        return sb.toString();
    }

    private String format(long n) {
        return String.format("%,d", n).replace(',', '.');
    }

    private String trim(double v) {
        if (v == Math.floor(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(Math.round(v * 100.0) / 100.0);
    }

    // ============ HANH DONG ============
    public void nangCap(Player player) {
        if (player.cungMenhLevel >= maxLevel()) {
            Service.gI().sendThongBao(player, "Cấp Cung Mệnh đã đạt tối đa!");
            return;
        }
        if (!canNangCap(player)) {
            Service.gI().sendThongBao(player, "Cần ĐỘT PHA trước khi nâng cấp tiếp!");
            return;
        }
        int need = manhCanNangCapTiep(player);
        long co = countManh(player);
        if (co < need) {
            Service.gI().sendThongBao(player, "Không đủ Mảnh Tinh Tú! Cần " + need + " (con có " + co + ").");
            return;
        }
        if (!removeManh(player, need)) {
            Service.gI().sendThongBao(player, "Lỗi trừ nguyên liệu, thử lại.");
            return;
        }
        player.cungMenhLevel++;
        saveAndRefresh(player);
        Service.gI().sendThongBao(player, "Nâng cấp thành công! Cung Mệnh cấp " + player.cungMenhLevel
                + "\nChỉ số cộng theo cấu hình đã được cập nhật ngay.");
    }

    public void dotPha(Player player) {
        int lv = player.cungMenhLevel;
        int every = dotPhaEvery();
        if (lv >= maxLevel()) {
            Service.gI().sendThongBao(player, "Cấp Cung Mệnh đã đạt tối đa!");
            return;
        }
        if (lv % every != 0 || player.cungMenhDotPha >= lv / every) {
            Service.gI().sendThongBao(player, "Chỉ đột phá khi đạt cấp tròn " + every + ", " + (every * 2) + ", " + (every * 3) + "...");
            return;
        }
        int needM = manhCanDotPha(player);
        int needN = ngocCanDotPha(player);
        if (countManh(player) < needM || countNgoc(player) < needN) {
            Service.gI().sendThongBao(player, "Không đủ nguyên liệu! Cần " + needM + " Mảnh + " + needN
                    + " Ngọc (con có " + countManh(player) + " Mảnh, " + countNgoc(player) + " Ngọc).");
            return;
        }
        if (!removeManh(player, needM) || !removeNgoc(player, needN)) {
            Service.gI().sendThongBao(player, "Lỗi trừ nguyên liệu, thử lại.");
            return;
        }
        player.cungMenhDotPha++;
        saveAndRefresh(player);
        Service.gI().sendThongBao(player, "ĐỘT PHA lần " + player.cungMenhDotPha + " thành công!"
                + "\nCó thể nâng cấp tiếp đến cấp " + (player.cungMenhDotPha * every + every) + ".");
    }

    private void saveAndRefresh(Player player) {
        PlayerDAO.saveCungMenh(player);
        Service.gI().point(player);
    }

    // ============ HELPER: TRU ITEM ============
    private boolean removeManh(Player player, int qty) {
        return removeItem(player, ConstItem.MANH_TINH_TU, qty);
    }

    private boolean removeNgoc(Player player, int qty) {
        return removeItem(player, ConstItem.NGOC_TINH_DO, qty);
    }

    private boolean removeItem(Player player, int tempId, int qty) {
        long remain = qty;
        while (remain > 0) {
            Item it = InventoryService.gI().findItemBag(player, tempId);
            if (it == null) {
                return false;
            }
            if (it.quantity > remain) {
                it.quantity -= (int) remain;
                remain = 0;
            } else {
                remain -= it.quantity;
                InventoryService.gI().removeItemBag(player, it);
            }
        }
        InventoryService.gI().sendItemBag(player);
        return true;
    }
}
