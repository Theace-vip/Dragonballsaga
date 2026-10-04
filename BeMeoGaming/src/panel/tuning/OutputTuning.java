package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * OutputTuning - cau hinh ti le dame/hp/ki dau ra.
 * File: data/config/output_tuning.properties
 * Admin sua truc tiep tren panel, khong can vao code.
 */
public class OutputTuning {
    private static final String PATH = "data/config/output_tuning.properties";

    public static volatile double PLAYER_OUT_RATE = 1.0;
    public static volatile double HP_OUT_RATE = 1.0;
    public static volatile double KI_OUT_RATE = 1.0;

    static { try { load(); } catch (Exception e) { e.printStackTrace(); } }

    // Chi nhan dame khi nguoi choi la ben tan cong.
    public static double applyPlayerDame(double dameHit, boolean isAttackerPl) {
        try {
            if (!isAttackerPl) return dameHit;
            return dameHit * PLAYER_OUT_RATE;
        } catch (Exception e) { e.printStackTrace(); return dameHit; }
    }

    public static synchronized void load() {
        Properties p = new Properties();
        try {
            File f = new File(PATH);
            if (!f.exists()) { f.getParentFile().mkdirs(); save(); return; }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            try { PLAYER_OUT_RATE = Double.parseDouble(p.getProperty("playerOutRate", "1.0")); } catch (Exception e) { PLAYER_OUT_RATE = 1.0; }
            try { HP_OUT_RATE = Double.parseDouble(p.getProperty("hpOutRate", "1.0")); } catch (Exception e) { HP_OUT_RATE = 1.0; }
            try { KI_OUT_RATE = Double.parseDouble(p.getProperty("kiOutRate", "1.0")); } catch (Exception e) { KI_OUT_RATE = 1.0; }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        try {
            p.setProperty("playerOutRate", String.valueOf(PLAYER_OUT_RATE));
            p.setProperty("hpOutRate", String.valueOf(HP_OUT_RATE));
            p.setProperty("kiOutRate", String.valueOf(KI_OUT_RATE));
        } catch (Exception e) { e.printStackTrace(); }
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "Output - admin sua tren panel"); }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
