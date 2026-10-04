package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * EventTuning - admin bat/tat + ti le farm/rot/diem, khong can vao code.
 * File: data/config/event_tuning.properties
 * Giu cac flag goc EventManager, them rate + mo ta farm.
 */
public class EventTuning {
    private static final String PATH = "data/config/event_tuning.properties";
    private static final Map<String, Ev> CACHE = new LinkedHashMap<>();

    public static class Ev {
        public boolean on = false;
        public double dropRate = 1.0;
        public double pointRate = 1.0;
        public String farm = "";
        public String note = "";
    }

    private static final String[] KEYS = new String[]{
        "HALLOWEEN","HUNG_VUONG","LUNNAR_NEW_YEAR","TRUNG_THU","CHRISTMAS",
        "GIAI_CUU_NHAN_GIOI","DAI_CHIEN_THAN_THU","INTERNATIONAL_WOMANS_DAY","TOP_UP"
    };

    static { load(); }

    public static synchronized void load() {
        CACHE.clear();
        for (String k : KEYS) CACHE.put(k, new Ev());
        Properties p = new Properties();
        File f = new File(PATH);
        try {
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                // nap trang thai goc tu EventManager
                try {
                    Class<?> c = Class.forName("event.EventManager");
                    for (String k : KEYS) {
                        try { CACHE.get(k).on = c.getField(k).getBoolean(null); } catch (Exception e) {}
                    }
                } catch (Exception e) {}
                save();
                return;
            }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            for (String k : KEYS) {
                Ev e = CACHE.get(k);
                e.on = "1".equals(p.getProperty(k + ".on", e.on ? "1" : "0").trim());
                try { e.dropRate = Double.parseDouble(p.getProperty(k + ".drop", "1").trim()); } catch (Exception ex) {}
                try { e.pointRate = Double.parseDouble(p.getProperty(k + ".point", "1").trim()); } catch (Exception ex) {}
                e.farm = p.getProperty(k + ".farm", "");
                e.note = p.getProperty(k + ".note", "");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        for (String k : KEYS) {
            Ev e = CACHE.get(k);
            p.setProperty(k + ".on", e.on ? "1" : "0");
            p.setProperty(k + ".drop", String.valueOf(e.dropRate));
            p.setProperty(k + ".point", String.valueOf(e.pointRate));
            p.setProperty(k + ".farm", e.farm == null ? "" : e.farm);
            p.setProperty(k + ".note", e.note == null ? "" : e.note);
        }
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "Event tuning - admin sua tren panel"); }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized Map<String, Ev> all() { return new LinkedHashMap<>(CACHE); }

    public static synchronized String set(String key, boolean on, double drop, double point, String farm, String note) {
        Ev e = CACHE.get(key);
        if (e == null) return "Khong co event " + key;
        e.on = on; e.dropRate = drop; e.pointRate = point;
        e.farm = farm == null ? "" : farm; e.note = note == null ? "" : note;
        save();
        // ap dung ngay vao EventManager (can restart de init lai boss, nhung flag bat/tat co tac dung ngay cho lan init sau)
        try {
            Class<?> c = Class.forName("event.EventManager");
            try { c.getField(key).setBoolean(null, on); } catch (NoSuchFieldException nsf) {}
        } catch (Exception ex) {}
        return "OK da luu " + key + " on=" + on;
    }
}
