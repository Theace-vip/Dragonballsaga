package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * TrungThuTuning - cau hinh su kien Trung Thu, chinh sua toan bo tu panel.
 * File: data/config/trungthu_tuning.properties
 *
 * Nhom (layer) de panel phan tang:
 *   1. Tong        - bat/tat su kien + NPC
 *   2. Do roi      - ty le vat pham roi moi lan ha quai
 *   3. Nau banh    - cong diem + nguyen lieu + phi nap
 *   4. Doi diem    - moc doi qua + phan thuong
 *   5. Ruong 1914  - noi dung ruong (qua + cai trang)
 *   6. Boss        - respawn, sat thuong co dinh
 *
 * LUU Y DON VI: "k" = x1000.  7k = 7000 (7000%), 5k = 5000 (5000%).
 * Chi so option dung so nguyen day du: 7000 chu khong phai 77.
 */
public class TrungThuTuning {

    private static final String PATH = "data/config/trungthu_tuning.properties";
    private static final Properties P = new Properties();

    /** key -> phan mo ta hien thi tren panel */
    private static final Map<String, String> NOTE = new LinkedHashMap<>();
    /** key -> nhom (layer) */
    private static final Map<String, String> LAYER = new LinkedHashMap<>();
    /** gia tri mac dinh */
    private static final Map<String, String> DEF = new LinkedHashMap<>();

    static {
        // ---- 1. Tong ----
        def("Tong", "on", "1", "Bat (1) / tat (0) su kien. Bat = hien NPC nau banh, tat = an NPC di.");
        def("Tong", "npc.map", "7", "Map lang dat NPC noi banh 66 (ngan cach bang dau phay). 0=Lang Aru, 7=Lang Mori, 14=Lang Kakarot, 16=Lang Plant, 163=Lang Plant thuy");
        def("Tong", "npc.id", "66", "ID NPC noi banh (chi dat tren map lang).");
        def("Tong", "npc.replaceId", "92", "NPC Trung Thu cu bi thay the tren map lang.");

        // ---- 2. Do roi (tong weight = 100 -> moi lan ha quai rot 1 vat) ----
        def("Do roi", "drop.0", "751|1|30", "La dong - 30% moi lan ha quai (itemId|soLuong|tyLe%)");
        def("Do roi", "drop.1", "889|1|20", "Dau xanh (ID 889) - 20%");
        def("Do roi", "drop.2", "1711|1|15", "Hat sen - 15%");
        def("Do roi", "drop.3", "1709|1|15", "Bot nep - 15%");
        def("Do roi", "drop.4", "1712|1|10", "Moi lua - 10%");
        def("Do roi", "drop.5", "1912|5|10", "10% con lai: x5 Manh Tinh Tu (1912)");
        def("Do roi", "drop.tailBonus", "10", "Duoi khic (579/1045): +10% ty le roi them vat pham SK");
        def("Do roi", "drop.tailSeconds", "900", "Thoi gian hieu luc buff Duoi khic (giay) = 15 phut");

        // ---- 3. Nau banh ----
        def("Nau banh", "cook.feeVnd", "49000", "Phi nau banh (VND nap thuc te / lan)");
        def("Nau banh", "cook.recipe.0", "20|1711:99,1709:50,1712:5,751:5", "Banh Hat sen (20 diem): diem|nguyen lieu itemId:soLuong - gom 5 La dong (751)");
        def("Nau banh", "cook.recipe.1", "50|1710:199,1709:150,1712:15,751:10,457:99999", "Banh Dau xanh (50 diem): gom 10 La dong (751)");
        def("Nau banh", "cook.recipe.2", "80|1711:499,1710:399,1709:199,1712:35,751:20,457:99999", "Banh Thap cam (80 diem): gom 20 La dong (751)");

        // ---- 4. Doi diem (tru diem truoc -> moi phat thuong) ----
        // Dinh dang: <diemCan>|<sukienCongThem>|<itemId>:<soLuong>:<optionId>:<thamSoOption>[,...]
        def("Doi diem", "exchange.0", "500000|5|1559:50:30:1", "Moc 500k diem -> 50 Hop qua Da Kham");
        def("Doi diem", "exchange.1", "100000|5|1558:100:30:1", "Moc 100k diem -> 100 Hop qua Thien Dao");
        def("Doi diem", "exchange.2", "50000|5|1557:10:30:1", "Moc 50k diem -> 10 Hop qua Long Den");
        def("Doi diem", "exchange.3", "30000|5|1556:10:30:1", "Moc 30k diem -> 10 Hop qua Linh Thu");
        def("Doi diem", "exchange.4", "20000|5|1555:10:30:1,1225:1000:30:1", "Moc 20k diem -> 10 Hop Sao Pha Le + 1000 Da Dia Dao");
        def("Doi diem", "exchange.5", "5000|5|1554:10:30:1", "Moc 5k diem = 5000 diem -> 10 Hop Than Linh");

        // ---- 5. Ruong 1914 (moi theo Hop Trung Thu 1512, icon 11717) ----
        def("Ruong 1914", "box.id", "1914", "ID vat pham ruong moi (icon lay cua 1512 = 11717)");
        def("Ruong 1914", "box.icon", "11717", "IconID dung cho ruong moi");
        def("Ruong 1914", "box.costumeRate", "5", "Ty le re cai trang khi mo ruong (%) = 5%");
        def("Ruong 1914", "box.costumeRange", "282-292", "Khoai ID cai trang (min-max)");
        def("Ruong 1914", "box.reward.0", "1912|10|50|7000", "Qua 1: itemId|soLuong|optionId|optionParam (50=Dame 7000% = 7k)");
        def("Ruong 1914", "box.reward.1", "1913|1|77|5000", "Qua 2: option 77=Sinh Luc 5000% = 5k");
        def("Ruong 1914", "box.reward.2", "1559|5|30|1", "Qua 3");

        // ---- 6. Boss ----
        def("Boss", "boss.restSeconds", "15", "Boss spawn lai sau khi chet (giay) - khong gioi han so lan");
        def("Boss", "boss.damage", "2000000", "Sat thuong co dinh boss Trung Thu gay ra, ap dung SAU moi bo dieu chinh (2m)");
        def("Boss", "boss.spawnCount", "10", "So luong moi boss khi su kien vua bat (spawn tu dong nhu boss thuong)");
        def("Boss", "boss.list", "KHIDOT,NGUYETTHAN,NHATTHAN", "Danh sach boss su kien (giu Nguyet + Nhat Than)");
        def("Boss", "bossReward.opt77", "7000", "Ruong boss Nguyet/Nhat Than: option 77 Sinh Luc +7000% (7k = x1000, KHONG phai 77)");
        def("Boss", "bossReward.opt103", "5000", "Ruong boss: option 103 MaNa +5000% (5k = 5000)");
        def("Boss", "bossReward.opt50", "5000", "Ruong boss: option 50 Dame +5000% (5k = 5000)");
    }

    private static void def(String layer, String key, String value, String note) {
        LAYER.put(key, layer);
        DEF.put(key, value);
        NOTE.put(key, note);
    }

    static {
        load();
    }

    // ======================= read =======================
    private static long lastLoadMs;
    private static long lastStoreMs;
    private static long lastCheckMs;

    /** Nap lai file neu admin sua tay ngoai panel (toi da kiem tra 1 lan/2 giay). */
    private static synchronized void touch() {
        long now = System.currentTimeMillis();
        if (now - lastCheckMs < 2000) return;
        lastCheckMs = now;
        File f = new File(PATH);
        long m;
        try {
            m = f.exists() ? f.lastModified() : 0;
        } catch (Exception e) {
            return;
        }
        if (m != 0 && m > lastLoadMs && m != lastStoreMs) {
            load();
        }
    }

    public static synchronized void load() {
        File f = new File(PATH);
        try {
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                // viet day du cac gia tri mac dinh de admin thay ngay tren panel
                P.clear();
                for (Map.Entry<String, String> e : DEF.entrySet()) {
                    P.setProperty(e.getKey(), e.getValue());
                }
                save();
                return;
            }
            Properties tmp = new Properties();
            try (FileInputStream in = new FileInputStream(f)) {
                tmp.load(in);
            }
            P.clear();
            P.putAll(tmp);
            lastLoadMs = f.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void save() {
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(f)) {
                P.store(out, "Trung Thu event tuning - admin sua tren panel (k = x1000: 7k=7000, 5k=5000)");
            }
            lastStoreMs = f.lastModified();
            lastLoadMs = lastStoreMs;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized String get(String key) {
        touch();
        String v = P.getProperty(key);
        if (v == null) {
            return DEF.getOrDefault(key, "");
        }
        return v.trim();
    }

    public static synchronized int getInt(String key, int def) {
        try {
            return (int) parseRate(get(key));
        } catch (Exception e) {
            return def;
        }
    }

    public static synchronized long getLong(String key, long def) {
        try {
            return (long) parseRate(get(key));
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Doc so tuy bien don vi: "7k" = 7000, "7k%" = 7000, "5k" = 5000, "1.5k" = 1500.
     * Nguoi ra lenh: "7k -> 7000% chu khong phai 77, o day k = 1000".
     */
    public static double parseRate(String raw) {
        if (raw == null) return 0;
        String s = raw.trim().replace("%", "").replace(" ", "").toLowerCase();
        if (s.isEmpty()) return 0;
        boolean k = s.endsWith("k");
        if (k) s = s.substring(0, s.length() - 1);
        double v;
        if (s.contains(",")) {
            s = s.replace(",", ".");
        }
        v = Double.parseDouble(s);
        if (k) v *= 1000;
        return v;
    }

    public static synchronized String set(String key, String value) {
        touch();
        if (value == null) value = "";
        try {
            // validate so truoc khi luu (ho tro ca "7k")
            if (!value.isEmpty() && DEF.containsKey(key) && isNumericKey(key)) {
                parseRate(value);
            }
        } catch (Exception e) {
            return "Gia tri so khong hop le: " + value + " (vi du dung: 7000 hoac 7k)";
        }
        P.setProperty(key, value.trim());
        save();
        return "OK da luu " + key + " = " + value.trim();
    }

    public static synchronized void setRaw(String key, String value) {
        P.setProperty(key, value == null ? "" : value);
        save();
    }

    /** Xoa toan bo khoa bat dau bang prefix (vd: "exchange.") - chua save. */
    public static synchronized void removePrefix(String prefix) {
        touch();
        List<String> kill = new ArrayList<>();
        for (Object k : P.keySet()) {
            String s = String.valueOf(k);
            if (s.startsWith(prefix)) kill.add(s);
        }
        for (String s : kill) P.remove(s);
    }

    /** Dat gia tri khong save ngay (de gop nhieu doi truoc khi save). */
    public static synchronized void putQuiet(String key, String value) {
        if (value == null) value = "";
        P.setProperty(key, value);
    }

    /** Dem so khoa co san trong mot nhom (exchange.0, exchange.1, ...). */
    public static synchronized int countPrefix(String prefix) {
        int n = 0;
        while (!get(prefix + n).isEmpty()) n++;
        return n;
    }

    private static boolean isNumericKey(String key) {
        return key.startsWith("drop.tail") || key.startsWith("cook.fee")
                || key.startsWith("box.costumeRate") || key.startsWith("boss.");
    }

    // ======================= chi so phan tram cua ruong boss =======================
    // NOTE: don vi "k" = x1000. 7k = 7000 (7000%), 5k = 5000 (5000%).
    public static int rewardOpt77() { return getInt("bossReward.opt77", 7000); }
    public static int rewardOpt103() { return getInt("bossReward.opt103", 5000); }
    public static int rewardOpt50() { return getInt("bossReward.opt50", 5000); }

    // ======================= panel rows =======================
    public static class Row {
        public String key, layer, value, note;

        public Row(String key, String layer, String value, String note) {
            this.key = key;
            this.layer = layer;
            this.value = value;
            this.note = note;
        }
    }

    /** Tat ca hang (gop default + gia tri da luu), gom theo nhom de panel phan tang. */
    public static synchronized List<Row> rows() {
        List<Row> out = new ArrayList<>();
        for (String key : DEF.keySet()) {
            out.add(new Row(key, LAYER.get(key), get(key), NOTE.get(key)));
        }
        for (Object ok : P.keySet()) {
            String key = String.valueOf(ok);
            if (!DEF.containsKey(key)) {
                out.add(new Row(key, LAYER.getOrDefault(key, "Khac"), get(key),
                        NOTE.getOrDefault(key, "Khoa tuy chinh (admin them)")));
            }
        }
        return out;
    }

    public static synchronized List<String> layers() {
        List<String> out = new ArrayList<>();
        for (String l : LAYER.values()) {
            if (!out.contains(l)) out.add(l);
        }
        for (Row r : rows()) {
            if (!out.contains(r.layer)) out.add(r.layer);
        }
        return out;
    }

    // ======================= tien ich =======================
    /** Su kien dang bat? */
    public static boolean isOn() {
        return getInt("on", 1) == 1;
    }
}
