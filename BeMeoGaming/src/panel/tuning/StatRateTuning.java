package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * StatRateTuning - cau hinh ti le quy doi chi so (hien thi / thuc te).
 * File: data/config/stat_rate.properties
 * Admin sua truc tiep tren panel, khong can vao code.
 * DISPLAY = so hien thi, REAL = so thuc te, real() = REAL * GLOBAL_FACTOR.
 */
public class StatRateTuning {
    private static final String PATH = "data/config/stat_rate.properties";

    public static volatile double GLOBAL_FACTOR = 1.0;
    public static final Map<String, Double> DISPLAY = new TreeMap<>();
    public static final Map<String, Double> REAL = new TreeMap<>();

    static { try { defaults(); load(); } catch (Exception e) { e.printStackTrace(); } }

    private static void put(String key, double v) {
        try {
            DISPLAY.put(key, v);
            REAL.put(key, v);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void putPair(String key, double d, double r) {
        try {
            DISPLAY.put(key, d);
            REAL.put(key, r);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Nap gia tri mac dinh cho toan bo key.
    private static synchronized void defaults() {
        try {
            DISPLAY.clear();
            REAL.clear();
            // Sao (star)
            put("star18", 50);
            put("star30", 100);
            put("star45", 150);
            put("star65", 250);
            put("star99", 500);
            put("star200", 1200);
            put("star300", 2500);
            put("star500", 5000);
            put("star700", 8000);
            put("star999", 20000);
            put("star1", 10);
            // Vip
            put("vip1", 50);
            put("vip2", 100);
            put("vip3", 300);
            put("vip4", 500);
            put("vip5", 1000);
            put("vip6", 2000);
            // Vip 7-13: mac dinh 0 (truoc day code khong ap quyen loi chi so cho VIP 7-13) - admin set tren panel neu muon
            put("vip7", 0);
            put("vip8", 0);
            put("vip9", 0);
            put("vip10", 0);
            put("vip11", 0);
            put("vip12", 0);
            put("vip13", 0);
            // TNSM theo cap VIP (%) - truoc day hardcode 1-6, 7-13 = 0
            put("vipTnsm1", 100);
            put("vipTnsm2", 100);
            put("vipTnsm3", 150);
            put("vipTnsm4", 250);
            put("vipTnsm5", 350);
            put("vipTnsm6", 550);
            put("vipTnsm7", 0);
            put("vipTnsm8", 0);
            put("vipTnsm9", 0);
            put("vipTnsm10", 0);
            put("vipTnsm11", 0);
            put("vipTnsm12", 0);
            put("vipTnsm13", 0);
            // TNSM bonus thap cho MOI VIP (>0)
            put("vipTnsmAll", 1);
            // Set do
            put("setgoku", 80);
            put("setsaga", 30);
            put("setvegeta", 500);
            put("setnappa", 80);
            put("setpicolo", 50);
            // Linh thu per level
            put("bachho_per_lv", 50);
            put("chutuoc_per_lv", 100);
            put("thanhlong_per_lv", 100);
            // An / su kien
            put("tinhAn", 5);
            put("nhatAn", 5);
            put("nguyetAn", 5);
            put("worldcup", 10);
            put("gogeta", 10);
            // Clan per level
            put("clan_per_lv", 30);
            // Thien dao (he so %)
            put("thiendo_dame", 1);
            put("thiendo_hp", 1);
            put("thiendo_ki", 1);
            // Chuyen sinh
            put("chuyensinh_dame", 20);
            put("chuyensinh_hp", 2);
            put("chuyensinh_ki", 10);
            // Cap Phong Ba
            put("capPb_dame", 3);
            put("capPb_hp", 20);
            put("capPb_ki", 30);
            // Da Ke Thon / Duoc Ke Thon
            put("dakethon_dame", 1000);
            put("dakethon_hp", 10000);
            put("dakethon_ki", 1000);
            put("duockethon_dame", 100);
            put("duockethon_hp", 1000);
            put("duockethon_ki", 100);
            // Pet 1-4
            put("pet1_dame", 25);
            put("pet1_hp", 25);
            put("pet1_ki", 25);
            put("pet2_dame", 30);
            put("pet2_hp", 30);
            put("pet2_ki", 30);
            put("pet3_dame", 20);
            put("pet3_hp", 20);
            put("pet3_ki", 20);
            put("pet4_dame", 40);
            put("pet4_hp", 40);
            put("pet4_ki", 40);
            // Pet 11-14
            put("pet11_dame", 1000);
            put("pet11_hp", 100);
            put("pet11_ki", 100);
            put("pet12_dame", 500);
            put("pet12_hp", 50);
            put("pet12_ki", 50);
            put("pet13_dame", 700);
            put("pet13_hp", 70);
            put("pet13_ki", 70);
            put("pet14_dame", 300);
            put("pet14_hp", 30);
            put("pet14_ki", 300);
            // Option factor: display = so hien, real = so thuc
            putPair("opt49_factor", 100, 10);
            putPair("opt50_factor", 100, 10);
            putPair("opt77_factor", 100, 10);
            putPair("opt103_factor", 100, 10);
            // Ly ruou
            put("lyruou_dame", 150);
            put("lyruou_hp", 150);
            put("lyruou_ki", 150);
            // Vat pham / cuong no / sao den / bo huyet-khi
            put("banhgaquay", 1000);
            put("cuongno_sc", 200);
            put("cuongno2", 120);
            put("saoDen1", 100);
            put("saoDen2", 100);
            put("saoDen6", 300);
            put("bohuyet_sc", 200);
            put("bokhi_sc", 200);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Gia tri thuc te = REAL * GLOBAL_FACTOR.
    public static double real(String key, double def) {
        try {
            Double v = REAL.get(key);
            double base = (v == null) ? def : v.doubleValue();
            return base * GLOBAL_FACTOR;
        } catch (Exception e) { e.printStackTrace(); return def; }
    }

    // Gia tri hien thi (khong nhan GLOBAL_FACTOR).
    public static double display(String key, double def) {
        try {
            Double v = DISPLAY.get(key);
            return (v == null) ? def : v.doubleValue();
        } catch (Exception e) { e.printStackTrace(); return def; }
    }

    public static synchronized Map<String, Double> allDisplay() {
        try {
            return new TreeMap<>(DISPLAY);
        } catch (Exception e) { e.printStackTrace(); return new TreeMap<>(); }
    }

    public static synchronized Map<String, Double> allReal() {
        try {
            return new TreeMap<>(REAL);
        } catch (Exception e) { e.printStackTrace(); return new TreeMap<>(); }
    }

    public static synchronized void setPair(String key, double d, double r) {
        try {
            if (key == null) return;
            DISPLAY.put(key, d);
            REAL.put(key, r);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void load() {
        Properties p = new Properties();
        try {
            File f = new File(PATH);
            if (!f.exists()) { f.getParentFile().mkdirs(); save(); return; }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            try { GLOBAL_FACTOR = Double.parseDouble(p.getProperty("globalFactor", "1.0")); } catch (Exception e) { GLOBAL_FACTOR = 1.0; }
            // Doc cap d_/r_ cho key da co default truoc
            defaults();
            for (String k : new TreeMap<>(DISPLAY).keySet()) {
                try {
                    String ds = p.getProperty("d_" + k);
                    if (ds != null) DISPLAY.put(k, Double.parseDouble(ds.trim()));
                } catch (Exception e) { e.printStackTrace(); }
                try {
                    String rs = p.getProperty("r_" + k);
                    if (rs != null) REAL.put(k, Double.parseDouble(rs.trim()));
                } catch (Exception e) { e.printStackTrace(); }
            }
            // Doc them key la neu file co key ngoai defaults
            for (String name : p.stringPropertyNames()) {
                try {
                    if (name.startsWith("d_")) {
                        String k = name.substring(2);
                        if (!DISPLAY.containsKey(k)) DISPLAY.put(k, Double.parseDouble(p.getProperty(name).trim()));
                    } else if (name.startsWith("r_")) {
                        String k = name.substring(2);
                        if (!REAL.containsKey(k)) REAL.put(k, Double.parseDouble(p.getProperty(name).trim()));
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        try {
            p.setProperty("globalFactor", String.valueOf(GLOBAL_FACTOR));
            java.util.Set<String> keys = new java.util.TreeSet<>();
            keys.addAll(DISPLAY.keySet());
            keys.addAll(REAL.keySet());
            for (String k : keys) {
                try {
                    Double d = DISPLAY.get(k);
                    Double r = REAL.get(k);
                    if (d != null) p.setProperty("d_" + k, String.valueOf(d));
                    if (r != null) p.setProperty("r_" + k, String.valueOf(r));
                } catch (Exception e) { e.printStackTrace(); }
            }
        } catch (Exception e) { e.printStackTrace(); }
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "StatRate - d_=hien thi, r_=thuc te, globalFactor nhan vao real()"); }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
