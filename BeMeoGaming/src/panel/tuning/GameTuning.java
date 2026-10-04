package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * GameTuning - kho cong thuc trung tam, admin sua tren panel, khong can vao code.
 * Luu tai data/config/game_tuning.properties, load luc server start.
 * Triet ly dame ao: leo tu tu, soft-cap chong dot bien len 10-30 ty.
 */
public class GameTuning {
    public static volatile double DAME_GLOBAL = 1.0;
    public static volatile double HP_GLOBAL = 1.0;
    public static volatile double MP_GLOBAL = 1.0;
    public static volatile double DEF_GLOBAL = 1.0;
    public static volatile double CRIT_ADD = 0.0;
    public static volatile double SDCM_GLOBAL = 1.0;
    public static volatile double TNSM_GLOBAL = 1.0;
    public static volatile double POWER_GLOBAL = 1.0;
    public static volatile double BOSS_DAME_SCALE = 1.0;
    public static volatile double BOSS_HP_SCALE = 1.0;
    public static volatile double DROP_RATE = 1.0;
    public static volatile double EVENT_POINT_RATE = 1.0;
    public static volatile double DAME_SOFT_CAP = 30000000000d;
    public static volatile double HP_SOFT_CAP = 30000000000000d;
    public static volatile double CRIT_CAP = 110.0;
    public static volatile double NE_DON_RATE = 1.0;
    public static volatile double SUB_SD_RATE = 1.0;
    public static volatile double HUT_HP_RATE = 1.0;
    public static volatile double EXP_RATE = 1.0;
    public static volatile double GOLD_RATE = 1.0;

    private static final String PATH = "data/config/game_tuning.properties";
    static { load(); }

    public static synchronized void load() {
        Properties p = new Properties();
        File f = new File(PATH);
        try {
            if (!f.exists()) { f.getParentFile().mkdirs(); save(); return; }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            DAME_GLOBAL = d(p,"DAME_GLOBAL",DAME_GLOBAL);
            HP_GLOBAL = d(p,"HP_GLOBAL",HP_GLOBAL);
            MP_GLOBAL = d(p,"MP_GLOBAL",MP_GLOBAL);
            DEF_GLOBAL = d(p,"DEF_GLOBAL",DEF_GLOBAL);
            CRIT_ADD = d(p,"CRIT_ADD",CRIT_ADD);
            SDCM_GLOBAL = d(p,"SDCM_GLOBAL",SDCM_GLOBAL);
            TNSM_GLOBAL = d(p,"TNSM_GLOBAL",TNSM_GLOBAL);
            POWER_GLOBAL = d(p,"POWER_GLOBAL",POWER_GLOBAL);
            BOSS_DAME_SCALE = d(p,"BOSS_DAME_SCALE",BOSS_DAME_SCALE);
            BOSS_HP_SCALE = d(p,"BOSS_HP_SCALE",BOSS_HP_SCALE);
            DROP_RATE = d(p,"DROP_RATE",DROP_RATE);
            EVENT_POINT_RATE = d(p,"EVENT_POINT_RATE",EVENT_POINT_RATE);
            DAME_SOFT_CAP = d(p,"DAME_SOFT_CAP",DAME_SOFT_CAP);
            HP_SOFT_CAP = d(p,"HP_SOFT_CAP",HP_SOFT_CAP);
            CRIT_CAP = d(p,"CRIT_CAP",CRIT_CAP);
            NE_DON_RATE = d(p,"NE_DON_RATE",NE_DON_RATE);
            SUB_SD_RATE = d(p,"SUB_SD_RATE",SUB_SD_RATE);
            HUT_HP_RATE = d(p,"HUT_HP_RATE",HUT_HP_RATE);
            EXP_RATE = d(p,"EXP_RATE",EXP_RATE);
            GOLD_RATE = d(p,"GOLD_RATE",GOLD_RATE);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        p.setProperty("DAME_GLOBAL", String.valueOf(DAME_GLOBAL));
        p.setProperty("HP_GLOBAL", String.valueOf(HP_GLOBAL));
        p.setProperty("MP_GLOBAL", String.valueOf(MP_GLOBAL));
        p.setProperty("DEF_GLOBAL", String.valueOf(DEF_GLOBAL));
        p.setProperty("CRIT_ADD", String.valueOf(CRIT_ADD));
        p.setProperty("SDCM_GLOBAL", String.valueOf(SDCM_GLOBAL));
        p.setProperty("TNSM_GLOBAL", String.valueOf(TNSM_GLOBAL));
        p.setProperty("POWER_GLOBAL", String.valueOf(POWER_GLOBAL));
        p.setProperty("BOSS_DAME_SCALE", String.valueOf(BOSS_DAME_SCALE));
        p.setProperty("BOSS_HP_SCALE", String.valueOf(BOSS_HP_SCALE));
        p.setProperty("DROP_RATE", String.valueOf(DROP_RATE));
        p.setProperty("EVENT_POINT_RATE", String.valueOf(EVENT_POINT_RATE));
        p.setProperty("DAME_SOFT_CAP", String.valueOf(DAME_SOFT_CAP));
        p.setProperty("HP_SOFT_CAP", String.valueOf(HP_SOFT_CAP));
        p.setProperty("CRIT_CAP", String.valueOf(CRIT_CAP));
        p.setProperty("NE_DON_RATE", String.valueOf(NE_DON_RATE));
        p.setProperty("SUB_SD_RATE", String.valueOf(SUB_SD_RATE));
        p.setProperty("HUT_HP_RATE", String.valueOf(HUT_HP_RATE));
        p.setProperty("EXP_RATE", String.valueOf(EXP_RATE));
        p.setProperty("GOLD_RATE", String.valueOf(GOLD_RATE));
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "GameTuning panel - admin sua tren panel"); }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static double d(Properties p, String k, double def) {
        try { String v = p.getProperty(k); if (v==null) return def; return Double.parseDouble(v.trim()); }
        catch (Exception e) { return def; }
    }

    public static double softCapDame(double dame) {
        if (dame<=0) return dame;
        double cap = DAME_SOFT_CAP;
        if (cap<=0) return dame;
        if (dame<=cap) return dame;
        return cap + (dame-cap)*0.2d;
    }

    public static double softCapHp(double hp) {
        if (hp<=0) return hp;
        double cap = HP_SOFT_CAP;
        if (cap<=0) return hp;
        if (hp<=cap) return hp;
        return cap + (hp-cap)*0.2d;
    }

    public static String progressHint(double dame) {
        if (dame < 100000) return "Tan thu (<100k)";
        if (dame < 1000000) return "So cap (100k-1tr)";
        if (dame < 10000000) return "Trung cap (1-10tr)";
        if (dame < 100000000) return "Cao cap (10-100tr)";
        if (dame < 1000000000) return "Sieu cap (0.1-1 ty)";
        if (dame < 10000000000L) return "Endgame (1-10 ty)";
        return "Max ao (10-30 ty, chi boss cuoi)";
    }
}
