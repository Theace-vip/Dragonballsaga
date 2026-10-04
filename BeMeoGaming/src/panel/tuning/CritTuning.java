package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * CritTuning - cau hinh chi mang / bien thien dame.
 * File: data/config/crit_tuning.properties
 * Admin sua truc tiep tren panel, khong can vao code.
 */
public class CritTuning {
    private static final String PATH = "data/config/crit_tuning.properties";

    public static volatile double CRIT_MULT = 2.0;
    public static volatile double SDCM_ADD = 0;
    public static volatile double VARIANCE_PCT = 5.0;
    public static volatile double CRIT_CAP = 110;
    public static volatile double CRIT_ADD = 0;
    public static volatile boolean THIENDAO70_ON = true;
    public static volatile boolean THIENDAO100_LH_ON = true;

    static { try { load(); } catch (Exception e) { e.printStackTrace(); } }

    public static synchronized void load() {
        Properties p = new Properties();
        try {
            File f = new File(PATH);
            if (!f.exists()) { f.getParentFile().mkdirs(); save(); return; }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            try { CRIT_MULT = Double.parseDouble(p.getProperty("critMult", "2.0")); } catch (Exception e) { CRIT_MULT = 2.0; }
            try { SDCM_ADD = Double.parseDouble(p.getProperty("sdcmAdd", "0")); } catch (Exception e) { SDCM_ADD = 0; }
            try { VARIANCE_PCT = Double.parseDouble(p.getProperty("variancePct", "5.0")); } catch (Exception e) { VARIANCE_PCT = 5.0; }
            try { CRIT_CAP = Double.parseDouble(p.getProperty("critCap", "110")); } catch (Exception e) { CRIT_CAP = 110; }
            try { CRIT_ADD = Double.parseDouble(p.getProperty("critAdd", "0")); } catch (Exception e) { CRIT_ADD = 0; }
            try { THIENDAO70_ON = Boolean.parseBoolean(p.getProperty("thiendao70On", "true")); } catch (Exception e) { THIENDAO70_ON = true; }
            try { THIENDAO100_LH_ON = Boolean.parseBoolean(p.getProperty("thiendao100LhOn", "true")); } catch (Exception e) { THIENDAO100_LH_ON = true; }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        try {
            p.setProperty("critMult", String.valueOf(CRIT_MULT));
            p.setProperty("sdcmAdd", String.valueOf(SDCM_ADD));
            p.setProperty("variancePct", String.valueOf(VARIANCE_PCT));
            p.setProperty("critCap", String.valueOf(CRIT_CAP));
            p.setProperty("critAdd", String.valueOf(CRIT_ADD));
            p.setProperty("thiendao70On", String.valueOf(THIENDAO70_ON));
            p.setProperty("thiendao100LhOn", String.valueOf(THIENDAO100_LH_ON));
        } catch (Exception e) { e.printStackTrace(); }
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "Crit - admin sua tren panel"); }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
