package services;

import consts.ConstItem;
import item.Item;
import jdbc.daos.PlayerDAO;
import player.NPoint;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.PlayerService;
import services.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BẢN NGUYÊN TINH CẦU (Planet Core)
 * ---------------------------------------------------------------
 * Moi nhan vat thuoc 1 trong 3 hanh tinh (player.gender):
 *   0 = Trái Đất, 1 = Namek, 2 = Xayda.
 *
 * - 10 Cấp độ Lõi (level 0-10) / 1 Tier; max 10 cap phai DOT PHA 1 lan de len Tier tiep theo.
 * - 5 Giai đoạn Tiến Hóa (tier 0-5).
 * - Chi so co ban: moi cap loi cong 250% HP / 250% KI / 200% Suc danh (tong ca 10 cap = 2500/2500/2000%).
 * - Chi so dot pha (dac trung toc):
 *      Trái Đất: +% Mien thuong. Tier 5: nhan sat thuong chi tu -> giu 1 HP + hoi 50% HP toi da (hoi chieu 60s).
 *      Namek   : +% Phan sat thuong. Tier 5: moi giay hoi 5% HP/KI + giam 10% sat thuong ke dich xung quanh.
 *      Xayda   : +% Sat thuong chuan (bo qua giap). Tier 5 (Zenkai): HP cang thap sat thuong cang cao,
 *                toi da +300% khi HP duoi 20%.
 *
 * Luu du lieu: cot player.ban_nguyen (JSON {"level":x,"tier":y}) - xem PlayerDAO.saveBanNguyen.
 * VAT PHAM (xem ConstItem):
 *   1942 Manh Vo Tinh Thach  - nang cap: cap 1 = 100 manh, moi cap cong them 100 (100..1000).
 *   1943 Hat Giong Khoi Nguyen - dot pha: lan 1 = 10 hat, moi lan sau gap doi (10/20/40/80/160).
 */
public class BanNguyenTinhCauService {

    private static BanNguyenTinhCauService I;

    public static BanNguyenTinhCauService gI() {
        if (I == null) {
            I = new BanNguyenTinhCauService();
        }
        return I;
    }

    // ================== CAU HINH ==================
    /** Index menu NPC (Bo Mong) cho Ban Nguyen Tinh Cau. */
    public static final int MENU_BAN_NGUYEN = 251011;

    /** So cap loi toi da moi tier truoc khi phai dot pha. */
    public static final int MAX_LEVEL = 10;
    /** So giai doan tien hoa toi da. */
    public static final int MAX_TIER = 5;

    /** Vat pham nang cap (id item_template 1942). */
    public static final int ITEM_NANG_CAP = ConstItem.MANH_VO_TINH_THACH;
    /** Vat pham dot pha (id item_template 1943). */
    public static final int ITEM_DOT_PHA = ConstItem.HAT_GIONG_KHOI_NGUYEN;

    /** Gia nang cap: cap 1 = 100 manh, moi cap cong them 100 (100, 200, ... 1000). */
    public static final int MANH_VO_TINH_THACH_MOI_CAP = 100;
    /** Gia dot pha: lan 1 = 10 hat, moi lan sau gap doi (10, 20, 40, 80, 160). */
    public static final int HAT_GIONG_KHOI_NGUYEN_CO_SO = 10;

    /** % HP/KI/SD cong them moi Cap do Loi. */
    public static final double HP_PERCENT_PER_LEVEL = 250d;
    public static final double KI_PERCENT_PER_LEVEL = 250d;
    public static final double DAME_PERCENT_PER_LEVEL = 200d;

    /** % dac trung cong them moi Tier (dot pha) theo hanh tinh. */
    public static final double TD_MIEN_THUONG_PER_TIER = 4d;
    public static final double NAMEK_PHAN_SAT_PER_TIER = 5d;
    public static final double XAYDA_SAT_THUONG_CHUAN_PER_TIER = 4d;

    /** Noi tai Tier 5. */
    public static final long TD_CHEAT_DEATH_COOLDOWN = 60_000L;
    public static final double TD_CHEAT_DEATH_HEAL_PERCENT = 50d;
    public static final double NAMEK_REGEN_PERCENT_PER_SECOND = 5d;
    public static final double NAMEK_AURA_GIAM_SAT_THUONG = 10d;
    public static final double XAYDA_ZENKAI_HP_NGUONG = 20d;
    public static final double XAYDA_ZENKAI_MAX_PERCENT = 300d;

    // hoi chieu noi tai Trai Dat (theo id nhan vat de khong phai them field vao Player)
    private final Map<Long, Long> lastCheatDeath = new ConcurrentHashMap<>();

    // ================== THONG TIN CO BAN ==================
    /** Tong so cap loi da dat = tier * 10 + level. */
    public int tongCapLoi(Player p) {
        return p.banNguyenTier * MAX_LEVEL + p.banNguyenLevel;
    }

    public boolean coNoiTaiTier5(Player p) {
        return p.banNguyenTier >= MAX_TIER;
    }

    public String tenHanhTinh(Player p) {
        switch (p.gender) {
            case 1:
                return "Namek";
            case 2:
                return "Xayda";
            default:
                return "Trái Đất";
        }
    }

    /** % chi so dac trung cua hanh tinh theo tier hien tai (mien thuong / phan sat / sat thuong chuan). */
    public double percentDacTrung(Player p) {
        int tier = Math.min(p.banNguyenTier, MAX_TIER);
        if (tier <= 0) {
            return 0;
        }
        switch (p.gender) {
            case 1:
                return NAMEK_PHAN_SAT_PER_TIER * tier;
            case 2:
                return XAYDA_SAT_THUONG_CHUAN_PER_TIER * tier;
            default:
                return TD_MIEN_THUONG_PER_TIER * tier;
        }
    }

    public String tenChiSoDacTrung(Player p) {
        switch (p.gender) {
            case 1:
                return "% Phản sát thương";
            case 2:
                return "% Sát thương chuẩn (bỏ qua giáp)";
            default:
                return "% Miễn thương";
        }
    }

    public String moTaNoiTaiTier5(Player p) {
        switch (p.gender) {
            case 1:
                return "Mỗi giây hồi " + trim(NAMEK_REGEN_PERCENT_PER_SECOND) + "% HP/KI và giảm "
                        + trim(NAMEK_AURA_GIAM_SAT_THUONG) + "% sát thương kẻ địch xung quanh";
            case 2:
                return "Zenkai: HP dưới " + trim(XAYDA_ZENKAI_HP_NGUONG) + "% càng thấp sát thương càng cao, "
                        + "tối đa +" + trim(XAYDA_ZENKAI_MAX_PERCENT) + "% sát thương";
            default:
                return "Nhận sát thương chí tử: giữ lại 1 HP và hồi ngay "
                        + trim(TD_CHEAT_DEATH_HEAL_PERCENT) + "% HP tối đa (hồi chiêu "
                        + (TD_CHEAT_DEATH_COOLDOWN / 1000) + "s)";
        }
    }

    // ================== CONG CHI SO (goi tu NPoint.calPoint) ==================
    /**
     * Cong % HP/KI/SD cua Ban Nguyen vao chi so. Goi trong calPoint() nen chi so luon dung
     * sau khi nang cap / dot pha ma khong can chinh tay.
     */
    public void applyBonus(Player player, NPoint np) {
        try {
            if (player == null || np == null || !player.isPl()) {
                return;
            }
            int tong = tongCapLoi(player);
            if (tong <= 0) {
                return;
            }
            if (HP_PERCENT_PER_LEVEL != 0) {
                np.tlHp.add(Math.round(HP_PERCENT_PER_LEVEL * tong));
            }
            if (KI_PERCENT_PER_LEVEL != 0) {
                np.tlMp.add(Math.round(KI_PERCENT_PER_LEVEL * tong));
            }
            if (DAME_PERCENT_PER_LEVEL != 0) {
                np.tlDame.add(Math.round(DAME_PERCENT_PER_LEVEL * tong));
            }
        } catch (Exception e) {
        }
    }

    /** Bonus 1 lan hoiHP mien thuong cua Mien Thuong (Trai Dat) va aura cua Namek. */
    public boolean coGiamSatThuong(Player p) {
        if (p.banNguyenTier <= 0) {
            return false;
        }
        return p.gender == 0 || (p.gender == 1 && coNoiTaiTier5(p));
    }

    // ================== HOOK: NAN NHAN NHAN SAT THUONG ==================
    /**
     * Goi trong Player.injured() truoc khi tru HP.
     * Tra ve so sat thuong thuc nhan sau khi tru % mien thuong (Trai Dat) + aura Namek,
     * va xu ly noi tai "giu 1 HP" cua Trai Dat Tier 5.
     */
    public double truSatThuongNhan(Player victim, double damage, boolean isMobAttack) {
        try {
            if (victim == null || damage <= 0 || !victim.isPl() || victim.banNguyenTier <= 0) {
                return damage;
            }
            if (victim.gender == 0) {
                damage -= damage * (TD_MIEN_THUONG_PER_TIER * victim.banNguyenTier) / 100d;
            } else if (victim.gender == 1 && coNoiTaiTier5(victim)) {
                damage -= damage * NAMEK_AURA_GIAM_SAT_THUONG / 100d;
            }
            if (damage <= 0) {
                return 0;
            }
            // Trai Dat Tier 5: chan don chi tu
            if (victim.gender == 0 && coNoiTaiTier5(victim) && damage >= victim.nPoint.hp) {
                long now = System.currentTimeMillis();
                Long last = lastCheatDeath.get(victim.id);
                if (last == null || now - last >= TD_CHEAT_DEATH_COOLDOWN) {
                    lastCheatDeath.put(victim.id, now);
                    victim.nPoint.setHp(victim.nPoint.hpMax * TD_CHEAT_DEATH_HEAL_PERCENT / 100d);
                    Service.gI().point(victim);
                    Service.gI().Send_Info_NV(victim);
                    PlayerService.gI().sendInfoHpMp(victim);
                    Service.gI().sendThongBao(victim, "|1|Nội tại Trái Đất: giữ mạng thành công! Hồi "
                            + trim(TD_CHEAT_DEATH_HEAL_PERCENT) + "% HP (hồi chiêu "
                            + (TD_CHEAT_DEATH_COOLDOWN / 1000) + "s)");
                    return 0;
                }
            }
            return damage;
        } catch (Exception e) {
            return damage;
        }
    }

    /**
     * Goi trong Player.injured() sau khi da tru HP: Namek phan sat thuong lai ke tan cong.
     * plAtt == null (don do quai / hoi sinh) thi khong phan.
     */
    public void phanSatThuong(Player victim, Player plAtt, double damageDaNhan) {
        try {
            if (victim == null || plAtt == null || plAtt.equals(victim) || !plAtt.isPl() || plAtt.isBattu) {
                return;
            }
            if (victim.gender != 1 || victim.banNguyenTier <= 0 || damageDaNhan <= 0) {
                return;
            }
            double phan = damageDaNhan * (NAMEK_PHAN_SAT_PER_TIER * victim.banNguyenTier) / 100d;
            if (phan <= 0) {
                return;
            }
            // piercing = true: phan sat thuong bo qua giap/def cua ke tan cong
            plAtt.injured(null, phan, true, false);
        } catch (Exception e) {
        }
    }

    /**
     * Goi trong Player.injured() sau khi da tru giap/def: Xayda cong phan "sat thuong chuan"
     * (ti le da bi tru giap van duoc tinh vao don danh).
     *
     * @param damageSauGiap sat thuong sau khi bi giam boi giap/def
     * @param damageTruocGiap sat thuong goc truoc khi bi giam
     */
    public double congSatThuongChuan(Player plAtt, double damageSauGiap, double damageTruocGiap) {
        try {
            if (plAtt == null || !plAtt.isPl() || plAtt.gender != 2 || plAtt.banNguyenTier <= 0) {
                return damageSauGiap;
            }
            double chuan = damageTruocGiap * (XAYDA_SAT_THUONG_CHUAN_PER_TIER * Math.min(plAtt.banNguyenTier, MAX_TIER)) / 100d;
            return damageSauGiap + chuan;
        } catch (Exception e) {
            return damageSauGiap;
        }
    }

    // ================== HOOK: SAT THUONG DAU RA (Zenkai) ==================
    /** % sat thuong cong them cua noi tai Zenkai (Xayda Tier 5). */
    public double zenkaiPercent(Player p) {
        try {
            if (p == null || !p.isPl() || p.gender != 2 || !coNoiTaiTier5(p)) {
                return 0;
            }
            double hpPercent = p.nPoint.getCurrPercentHP();
            if (hpPercent >= XAYDA_ZENKAI_HP_NGUONG) {
                return 0;
            }
            double ratio = 1d - (hpPercent / XAYDA_ZENKAI_HP_NGUONG);
            if (ratio < 0) {
                ratio = 0;
            }
            if (ratio > 1) {
                ratio = 1;
            }
            return XAYDA_ZENKAI_MAX_PERCENT * ratio;
        } catch (Exception e) {
            return 0;
        }
    }

    /** Cong % Zenkai vao sat thuong dau ra (goi o cuoi NPoint.getDameAttack). */
    public double applyZenkai(Player p, double dameAttack) {
        double zenkai = zenkaiPercent(p);
        if (zenkai <= 0) {
            return dameAttack;
        }
        return dameAttack + dameAttack * zenkai / 100d;
    }

    // ================== HOOK: TICK MOI GIAY (Namek Tier 5) ==================
    /** Goi trong Player.update() moi ~1 giay. */
    public void tick(Player p) {
        try {
            if (p == null || !p.isPl() || !coNoiTaiTier5(p) || p.gender != 1 || p.isDie()) {
                return;
            }
            double hpHoi = p.nPoint.hpMax * NAMEK_REGEN_PERCENT_PER_SECOND / 100d;
            double kiHoi = p.nPoint.mpMax * NAMEK_REGEN_PERCENT_PER_SECOND / 100d;
            if (p.nPoint.hp < p.nPoint.hpMax || p.nPoint.mp < p.nPoint.mpMax) {
                PlayerService.gI().hoiPhuc(p, hpHoi, kiHoi);
            }
        } catch (Exception e) {
        }
    }

    // ================== NANG CAP / DOT PHA ==================
    public boolean canNangCap(Player p) {
        return p.banNguyenLevel < MAX_LEVEL;
    }

    public boolean canDotPha(Player p) {
        return p.banNguyenLevel >= MAX_LEVEL && p.banNguyenTier < MAX_TIER;
    }

    // ============ GIA NANG CAP / DOT PHA (vat pham) ============
    /** So Manh Vo Tinh Thach de len Cap do Loi ke tiep (level hien tai 0..9). */
    public int manhCanNangCap(int levelHienTai) {
        int lv = Math.max(0, Math.min(levelHienTai, MAX_LEVEL - 1));
        return MANH_VO_TINH_THACH_MOI_CAP * (lv + 1);
    }

    public int manhCanNangCap(Player p) {
        return manhCanNangCap(p.banNguyenLevel);
    }

    /** So Hat Giong Khoi Nguyen cho lan dot pha ke tiep (tier hien tai 0..4; moi lan gap doi). */
    public int hatGiongCanDotPha(int tierHienTai) {
        int tier = Math.max(0, Math.min(tierHienTai, MAX_TIER - 1));
        return HAT_GIONG_KHOI_NGUYEN_CO_SO << tier;
    }

    public int hatGiongCanDotPha(Player p) {
        return hatGiongCanDotPha(p.banNguyenTier);
    }

    /** Ten vat pham theo id template (de hien trong thong bao / menu), fallback "vật phẩm #id". */
    public static String tenVatPham(int id) {
        try {
            // Danh sach template duoc load theo id (index == id) nhung van kiem tra lai cho chac.
            if (id >= 0 && id < Manager.ITEM_TEMPLATES.size()) {
                models.Template.ItemTemplate t = Manager.ITEM_TEMPLATES.get(id);
                if (t != null && t.id == id && t.name != null && !t.name.isEmpty()) {
                    return t.name;
                }
            }
            for (models.Template.ItemTemplate t : Manager.ITEM_TEMPLATES) {
                if (t != null && t.id == id && t.name != null && !t.name.isEmpty()) {
                    return t.name;
                }
            }
        } catch (Exception e) {
        }
        return "vật phẩm #" + id;
    }

    public void nangCap(Player player) {
        try {
            if (!canNangCap(player)) {
                Service.gI().sendThongBao(player, "Đã đạt " + MAX_LEVEL + " Cấp độ Lõi. Hãy ĐỘT PHA để lên Tier "
                        + (player.banNguyenTier + 1) + ".");
                return;
            }
            if (ITEM_NANG_CAP > 0) {
                int can = manhCanNangCap(player);
                Item item = InventoryService.gI().findItemBag(player, ITEM_NANG_CAP);
                if (item == null || item.quantity < can) {
                    Service.gI().sendThongBao(player, "Cần " + can + " " + tenVatPham(ITEM_NANG_CAP)
                            + " để nâng Cấp độ Lõi " + player.banNguyenLevel + " → " + (player.banNguyenLevel + 1)
                            + " (đang có " + (item == null ? 0 : item.quantity) + ").");
                    return;
                }
                InventoryService.gI().subQuantityItemsBag(player, item, can);
            }
            player.banNguyenLevel++;
            save(player);
            player.nPoint.calPoint();
            Service.gI().point(player);
            Service.gI().Send_Info_NV(player);
            PlayerService.gI().sendInfoHpMpMoney(player);
            Service.gI().sendThongBao(player, "|1|Bản Nguyên Tinh Cầu: Cấp độ Lõi " + player.banNguyenLevel + "/"
                    + MAX_LEVEL + " (Tier " + player.banNguyenTier + ")\n"
                    + "Tổng: +" + trim(HP_PERCENT_PER_LEVEL * tongCapLoi(player)) + "% HP, +"
                    + trim(KI_PERCENT_PER_LEVEL * tongCapLoi(player)) + "% KI, +"
                    + trim(DAME_PERCENT_PER_LEVEL * tongCapLoi(player)) + "% Sức đánh");
        } catch (Exception e) {
        }
    }

    public void dotPha(Player player) {
        try {
            if (player.banNguyenTier >= MAX_TIER) {
                Service.gI().sendThongBao(player, "Bản Nguyên Tinh Cầu đã đạt Tier tối đa (" + MAX_TIER + ").");
                return;
            }
            if (player.banNguyenLevel < MAX_LEVEL) {
                Service.gI().sendThongBao(player, "Phải nâng đủ " + MAX_LEVEL + " Cấp độ Lõi ("
                        + player.banNguyenLevel + "/" + MAX_LEVEL + ") mới đột phá được.");
                return;
            }
            if (ITEM_DOT_PHA > 0) {
                int can = hatGiongCanDotPha(player);
                Item item = InventoryService.gI().findItemBag(player, ITEM_DOT_PHA);
                if (item == null || item.quantity < can) {
                    Service.gI().sendThongBao(player, "Cần " + can + " " + tenVatPham(ITEM_DOT_PHA)
                            + " để đột phá Tier " + player.banNguyenTier + " → " + (player.banNguyenTier + 1)
                            + " (đang có " + (item == null ? 0 : item.quantity) + ").");
                    return;
                }
                InventoryService.gI().subQuantityItemsBag(player, item, can);
            }
            player.banNguyenTier++;
            player.banNguyenLevel = 0;
            save(player);
            player.nPoint.calPoint();
            Service.gI().point(player);
            Service.gI().Send_Info_NV(player);
            PlayerService.gI().sendInfoHpMpMoney(player);
            Service.gI().sendThongBao(player, "|1|ĐỘT PHA BẢN NGUYÊN thành công! Tier " + player.banNguyenTier + " ("
                    + tenHanhTinh(player) + ")\n" + tenChiSoDacTrung(player) + ": +"
                    + trim(percentDacTrung(player)) + "%"
                    + (coNoiTaiTier5(player) ? "\nMỞ KHÓA NỘI TẠI TIER 5: " + moTaNoiTaiTier5(player) : ""));
            if (player.banNguyenTier >= 2) {
                Service.gI().sendThongBaoBenDuoi("Chúc mừng " + player.name + " vừa đột phá Bản Nguyên Tinh Cầu "
                        + tenHanhTinh(player) + " lên Tier " + player.banNguyenTier);
            }
        } catch (Exception e) {
        }
    }

    public String menuText(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("|7|BẢN NGUYÊN TINH CẦU - ").append(tenHanhTinh(player)).append("\n");
        sb.append("|5|Cấp độ Lõi: ").append(player.banNguyenLevel).append("/").append(MAX_LEVEL)
                .append("  |5|Tier: ").append(player.banNguyenTier).append("/").append(MAX_TIER).append("\n");
        int tong = tongCapLoi(player);
        sb.append("|6|Cơ bản (mỗi cấp): +").append(trim(HP_PERCENT_PER_LEVEL)).append("% HP, +")
                .append(trim(KI_PERCENT_PER_LEVEL)).append("% KI, +").append(trim(DAME_PERCENT_PER_LEVEL))
                .append("% Sức đánh\n");
        sb.append("|6|Đang có: +").append(trim(HP_PERCENT_PER_LEVEL * tong)).append("% HP, +")
                .append(trim(KI_PERCENT_PER_LEVEL * tong)).append("% KI, +")
                .append(trim(DAME_PERCENT_PER_LEVEL * tong)).append("% Sức đánh\n");
        sb.append("|4|Đột phá (").append(tenHanhTinh(player)).append("): ").append(tenChiSoDacTrung(player))
                .append(" +").append(trim(percentDacTrung(player))).append("%\n");
        sb.append("|3|Nội tại Tier 5: ").append(moTaNoiTaiTier5(player)).append("\n");
        if (canNangCap(player)) {
            sb.append("|1|Nâng Cấp độ Lõi ").append(player.banNguyenLevel).append(" → ")
                    .append(player.banNguyenLevel + 1).append(": ").append(manhCanNangCap(player))
                    .append(" ").append(tenVatPham(ITEM_NANG_CAP)).append("\n");
        } else {
            sb.append("|2|Cấp độ Lõi đã tối đa (").append(MAX_LEVEL).append(")\n");
        }
        if (player.banNguyenLevel >= MAX_LEVEL) {
            if (player.banNguyenTier < MAX_TIER) {
                sb.append("|1|Đột phá Tier ").append(player.banNguyenTier).append(" → ")
                        .append(player.banNguyenTier + 1).append(": ").append(hatGiongCanDotPha(player))
                        .append(" ").append(tenVatPham(ITEM_DOT_PHA)).append("\n");
            } else {
                sb.append("|2|Tier đã tối đa (").append(MAX_TIER).append(")\n");
            }
        }
        return sb.toString();
    }

    public void save(Player player) {
        try {
            PlayerDAO.saveBanNguyen(player);
        } catch (Exception e) {
        }
    }

    private static String trim(double v) {
        if (v == Math.floor(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(Math.round(v * 100d) / 100d);
    }
}
