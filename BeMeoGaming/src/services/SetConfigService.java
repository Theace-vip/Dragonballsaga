package services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import player.NPoint;
import player.Player;
import utils.Logger;

/**
 * Set 5 mon dau (ao/quan/gang/giay/nhan) - cau hinh tren panel (bang set_config).
 *
 * Luat: chi tinh tren 5 mon dau tien cua itemsBody (slot = type: 0 ao, 1 quan,
 * 2 gang, 3 giay, 4 nhan/rada). Mot set KICH HOAT khi ca 5 mon deu mang option
 * (option_id cua dong config), luc do moi cong bonus (hp/ki/dame/def base hoac %,
 * chi mang, STCM, sat thuong skill rieng).
 *
 * Gia tri seed hien tai = gia tri cu cua cac set khi mang du 5 mon:
 * Thanh Long +500% KI, Chu Tuoc +500% HP, Bach Ho +250% SD, Huyen Vu +500% DEF,
 * Kim +5M SD, Moc +10M HP, Tho +10M KI, Thuy +10M DEF, Hoa +100% STCM.
 * Admin sua/them tren panel -> load() lai ngay, khong can restart.
 */
public class SetConfigService {

    public static class Cfg {
        public int id;
        public String name;
        public int optionId;
        public boolean active;
        public long hpBase;
        public int hpPct;
        public long kiBase;
        public int kiPct;
        public long dameBase;
        public int damePct;
        public long defBase;
        public int defPct;
        public int critAdd;
        public int critDmgPct;
        /** Mang {skillId, pct} doc tu chuoi "1:100,4:80". */
        public List<int[]> skillDmg = new ArrayList<>();
    }

    /** CopyOnWrite de panel sua trong khi NPoint dang tinh diem o thread khac. */
    public static final List<Cfg> CONFIGS = new CopyOnWriteArrayList<>();

    /** Nap toan bo dong set_config. Goi khi boot va khi panel luu. */
    public static synchronized void load() {
        List<Cfg> tmp = new ArrayList<>();
        try (java.sql.Connection con = jdbc.DBConnecter.getConnectionServer();
                java.sql.PreparedStatement ps = con.prepareStatement("SELECT * FROM set_config ORDER BY id");
                java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cfg c = new Cfg();
                c.id = rs.getInt("id");
                c.name = rs.getString("name");
                c.optionId = rs.getInt("option_id");
                c.active = rs.getInt("active") != 0;
                c.hpBase = rs.getLong("hp_base");
                c.hpPct = rs.getInt("hp_pct");
                c.kiBase = rs.getLong("ki_base");
                c.kiPct = rs.getInt("ki_pct");
                c.dameBase = rs.getLong("dame_base");
                c.damePct = rs.getInt("dame_pct");
                c.defBase = rs.getLong("def_base");
                c.defPct = rs.getInt("def_pct");
                c.critAdd = rs.getInt("crit_add");
                c.critDmgPct = rs.getInt("crit_dmg_pct");
                c.skillDmg = parseSkillDmg(rs.getString("skill_dmg"));
                tmp.add(c);
            }
        } catch (Exception e) {
            Logger.logException(SetConfigService.class, e, "Loi load bang set_config");
        }
        CONFIGS.clear();
        CONFIGS.addAll(tmp);
        Logger.success("Load set_config thanh cong (" + CONFIGS.size() + ")");
    }

    /** Dinh dang chuoi skill: "<id skill>:<%>,<id skill>:<%>" vd "1:100,4:80". */
    public static List<int[]> parseSkillDmg(String s) {
        List<int[]> out = new ArrayList<>();
        if (s == null || s.trim().isEmpty()) {
            return out;
        }
        for (String part : s.split(",")) {
            try {
                String[] kv = part.trim().split(":");
                int skill = Integer.parseInt(kv[0].trim());
                int pct = Integer.parseInt(kv[1].trim());
                out.add(new int[]{skill, pct});
            } catch (Exception e) {
                // bo quaphan dinh dang sai, khong lam lung server
            }
        }
        return out;
    }

    /** Dem so mon trong 5 mon dau mang option cua set nay. */
    private static boolean full5(Player pl, Cfg c) {
        return pl != null && pl.setClothes != null && pl.setClothes.countSet(c.optionId) == 5;
    }

    public static void applyHp(NPoint np, Player pl) {
        for (Cfg c : CONFIGS) {
            if (!c.active || (c.hpBase == 0 && c.hpPct == 0) || !full5(pl, c)) {
                continue;
            }
            np.hpMax += c.hpBase;
            if (c.hpPct != 0) {
                np.hpMax += np.hpMax * c.hpPct / 100d;
            }
        }
    }

    public static void applyKi(NPoint np, Player pl) {
        for (Cfg c : CONFIGS) {
            if (!c.active || (c.kiBase == 0 && c.kiPct == 0) || !full5(pl, c)) {
                continue;
            }
            np.mpMax += c.kiBase;
            if (c.kiPct != 0) {
                np.mpMax += np.mpMax * c.kiPct / 100d;
            }
        }
    }

    public static void applyDame(NPoint np, Player pl) {
        for (Cfg c : CONFIGS) {
            if (!c.active || (c.dameBase == 0 && c.damePct == 0) || !full5(pl, c)) {
                continue;
            }
            np.dame += c.dameBase;
            if (c.damePct != 0) {
                np.dame += np.dame * c.damePct / 100d;
            }
        }
    }

    public static void applyDef(NPoint np, Player pl) {
        for (Cfg c : CONFIGS) {
            if (!c.active || (c.defBase == 0 && c.defPct == 0) || !full5(pl, c)) {
                continue;
            }
            np.def += c.defBase;
            if (c.defPct != 0) {
                np.def += np.def * c.defPct / 100d;
            }
        }
    }

    /** + chi mang (them truc tiep) va + STCM (theo % hien tai, giong cu). */
    public static void applyCrit(NPoint np, Player pl) {
        for (Cfg c : CONFIGS) {
            if (!c.active || (c.critAdd == 0 && c.critDmgPct == 0) || !full5(pl, c)) {
                continue;
            }
            np.crit += c.critAdd;
            np.tlSDCM += np.tlSDCM * c.critDmgPct / 100;
        }
    }

    /** Tong % sat thuong skill rieng cac set dang kich hoat cho skill nay. */
    public static int skillPct(Player pl, int skillId) {
        int pct = 0;
        for (Cfg c : CONFIGS) {
            if (!c.active || c.skillDmg.isEmpty() || !full5(pl, c)) {
                continue;
            }
            for (int[] sd : c.skillDmg) {
                if (sd[0] == skillId) {
                    pct += sd[1];
                }
            }
        }
        return pct;
    }
}
