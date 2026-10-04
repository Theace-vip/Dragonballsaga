package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * WorldBossTuning - cau hinh Boss The Gioi (bat tu, tinh dame top 10).
 * File: data/config/worldboss.properties
 * Admin sua truc tiep tren panel, khong can vao code.
 */
public class WorldBossTuning {
    private static final String PATH = "data/config/worldboss.properties";

    public static boolean ON = false;
    public static int MAP_ID = 5;
    public static int DURATION_MIN = 30;
    public static double HP = 9.0e14;
    public static double DAME = 1;
    public static String TOP1 = "457:1";
    public static String TOP23 = "457:1";
    public static String TOP410 = "457:1";
    public static String CONSOL = "456:5";
    public static double MIN_DAME = 1000000;
    // V2: lich goi hang ngay + xoay khu + qua JSON co option
    public static String SPAWN_TIMES = "12:00,19:00";
    public static int LIFE_MIN = 60;
    public static int ZONE_SEC = 15;
    public static int START_ZONE = -1;
    public static String TOP1_JSON = "";
    public static String TOP23_JSON = "";
    public static String TOP410_JSON = "";
    public static String CONSOL_JSON = "";

    static { load(); }

    public static synchronized void load() {
        Properties p = new Properties();
        try {
            File f = new File(PATH);
            if (!f.exists()) { f.getParentFile().mkdirs(); save(); return; }
            try (FileInputStream in = new FileInputStream(f)) { p.load(in); }
            ON = Boolean.parseBoolean(p.getProperty("on", "false"));
            MAP_ID = Integer.parseInt(p.getProperty("mapId", "5"));
            DURATION_MIN = Integer.parseInt(p.getProperty("durationMin", "30"));
            HP = Double.parseDouble(p.getProperty("hp", "9.0E14"));
            DAME = Double.parseDouble(p.getProperty("dame", "1"));
            TOP1 = p.getProperty("top1", "457:1");
            TOP23 = p.getProperty("top23", "457:1");
            TOP410 = p.getProperty("top410", "457:1");
            CONSOL = p.getProperty("consol", "456:5");
            MIN_DAME = Double.parseDouble(p.getProperty("minDame", "1000000"));
            SPAWN_TIMES = p.getProperty("spawnTimes", "12:00,19:00");
            try { LIFE_MIN = Integer.parseInt(p.getProperty("lifeMin", "60")); } catch (Exception e) { LIFE_MIN = 60; }
            if (LIFE_MIN <= 0) LIFE_MIN = 60;
            try { ZONE_SEC = Integer.parseInt(p.getProperty("zoneSec", "15")); } catch (Exception e) { ZONE_SEC = 15; }
            if (ZONE_SEC < 5) ZONE_SEC = 5;
            try { START_ZONE = Integer.parseInt(p.getProperty("startZone", "-1")); } catch (Exception e) { START_ZONE = -1; }
            TOP1_JSON = p.getProperty("top1Json", "");
            TOP23_JSON = p.getProperty("top23Json", "");
            TOP410_JSON = p.getProperty("top410Json", "");
            CONSOL_JSON = p.getProperty("consolJson", "");
            if (TOP1_JSON == null) TOP1_JSON = "";
            if (TOP23_JSON == null) TOP23_JSON = "";
            if (TOP410_JSON == null) TOP410_JSON = "";
            if (CONSOL_JSON == null) CONSOL_JSON = "";
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        p.setProperty("on", String.valueOf(ON));
        p.setProperty("mapId", String.valueOf(MAP_ID));
        p.setProperty("durationMin", String.valueOf(DURATION_MIN));
        p.setProperty("hp", String.valueOf(HP));
        p.setProperty("dame", String.valueOf(DAME));
        p.setProperty("top1", TOP1 == null ? "" : TOP1);
        p.setProperty("top23", TOP23 == null ? "" : TOP23);
        p.setProperty("top410", TOP410 == null ? "" : TOP410);
        p.setProperty("consol", CONSOL == null ? "" : CONSOL);
        p.setProperty("minDame", String.valueOf(MIN_DAME));
        p.setProperty("spawnTimes", SPAWN_TIMES == null ? "" : SPAWN_TIMES);
        p.setProperty("lifeMin", String.valueOf(LIFE_MIN));
        p.setProperty("zoneSec", String.valueOf(ZONE_SEC));
        p.setProperty("startZone", String.valueOf(START_ZONE));
        p.setProperty("top1Json", TOP1_JSON == null ? "" : TOP1_JSON);
        p.setProperty("top23Json", TOP23_JSON == null ? "" : TOP23_JSON);
        p.setProperty("top410Json", TOP410_JSON == null ? "" : TOP410_JSON);
        p.setProperty("consolJson", CONSOL_JSON == null ? "" : CONSOL_JSON);
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) { p.store(o, "WorldBoss V2 - admin sua tren panel"); }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static java.util.List<String> spawnTimeList() {
        java.util.List<String> out = new java.util.ArrayList<>();
        if (SPAWN_TIMES == null) return out;
        for (String s : SPAWN_TIMES.split("[,;\\s]+")) {
            s = s.trim();
            if (s.matches("\\d{1,2}:\\d{2}")) out.add(s.length() == 4 ? "0" + s : s);
        }
        return out;
    }
}
