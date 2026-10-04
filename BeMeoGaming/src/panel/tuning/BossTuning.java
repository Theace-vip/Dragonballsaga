package panel.tuning;

import item.Item.ItemOption;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdbc.DBConnecter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utils.Logger;

/**
 * BossTuning - override chi so + DROP boss theo FIELD BossesData (khong sua code).
 * Admin sua tren panel (Web PHP -> bang panel_boss_override).
 *  - dame <0 = giu goc, hp rong = giu goc, rest <0 = giu goc
 *  - drops: DANH SACH multi-item, moi entry co options + ty le % + random range [min,max]
 *
 * Backup cu: data/config/boss_override.properties (van doc neu co, de tien chuyen doi).
 */
public class BossTuning {

    private static final Map<String, BossOv> CACHE = new LinkedHashMap<>();

    public static class BossOv {
        public double dame = -1;
        public String hp = "";           // rong = giu goc, vd "1000000000,2000000000"
        public int rest = -1;
        public String maps = "";         // rong = giu goc, vd "0,1,2"
        public List<DropEntry> drops = new ArrayList<>();
        public String note = "";

        /** Backward-compat: drop dau tien (cho code cu neu can). */
        public int getDropTemp() { return drops.isEmpty() ? -1 : drops.get(0).tempId; }
        public int getDropQty() { return drops.isEmpty() ? 1 : drops.get(0).qty; }
        public int getDropRate() { return drops.isEmpty() ? 100 : drops.get(0).rate; }
    }

    static { load(); }

    public static synchronized void load() {
        CACHE.clear();
        // 1) doc tu DB panel_boss_override
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM panel_boss_override")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String field = rs.getString("field");
                        BossOv o = CACHE.computeIfAbsent(field, x -> new BossOv());
                        try { o.dame = rs.getDouble("dame"); } catch (Exception e) {}
                        o.hp = rs.getString("hp_json") == null ? "" : rs.getString("hp_json");
                        try { o.rest = rs.getInt("rest"); } catch (Exception e) {}
                        o.maps = rs.getString("maps") == null ? "" : rs.getString("maps");
                        o.note = rs.getString("note") == null ? "" : rs.getString("note");
                        String dj = rs.getString("drop_json");
                        o.drops = parseDrops(dj);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(BossTuning.class, e, "Load panel_boss_override that bai");
        }
        // 2) merge them tu file properties cu (neu co) de tien chuyen doi
        try {
            File f = new File("data/config/boss_override.properties");
            if (f.exists()) {
                java.util.Properties p = new java.util.Properties();
                try (java.io.FileInputStream in = new java.io.FileInputStream(f)) { p.load(in); }
                for (String k : p.stringPropertyNames()) {
                    int dot = k.indexOf('.');
                    if (dot < 0) continue;
                    String field = k.substring(0, dot);
                    String attr = k.substring(dot + 1);
                    BossOv o = CACHE.computeIfAbsent(field, x -> new BossOv());
                    String v = p.getProperty(k, "").trim();
                    try {
                        switch (attr) {
                            case "dame": o.dame = Double.parseDouble(v); break;
                            case "hp": o.hp = v; break;
                            case "rest": o.rest = Integer.parseInt(v); break;
                            case "maps": o.maps = v; break;
                            case "drop": if (o.drops.isEmpty()) { DropEntry d = new DropEntry(); d.tempId = Integer.parseInt(v); o.drops.add(d); } break;
                            case "dropQty": if (!o.drops.isEmpty()) o.drops.get(0).qty = Integer.parseInt(v); break;
                            case "dropRate": if (!o.drops.isEmpty()) o.drops.get(0).rate = Integer.parseInt(v); break;
                            case "note": o.note = v; break;
                        }
                    } catch (Exception e) {}
                }
            }
        } catch (Exception e) {}
    }

    @SuppressWarnings("unchecked")
    public static List<DropEntry> parseDrops(String json) {
        List<DropEntry> out = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return out;
        try {
            Object obj = JSONValue.parse(json.trim());
            if (obj instanceof JSONArray) {
                for (Object o : (JSONArray) obj) {
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject j = (JSONObject) o;
                    DropEntry d = new DropEntry();
                    try { d.tempId = Integer.parseInt(String.valueOf(j.get("temp_id"))); } catch (Exception e) { continue; }
                    try { d.qty = Integer.parseInt(String.valueOf(j.get("qty"))); } catch (Exception e) {}
                    try { d.rate = Integer.parseInt(String.valueOf(j.get("rate"))); } catch (Exception e) { d.rate = 100; }
                    try { d.minParam = Long.parseLong(String.valueOf(j.get("min"))); } catch (Exception e) { d.minParam = -1; }
                    try { d.maxParam = Long.parseLong(String.valueOf(j.get("max"))); } catch (Exception e) { d.maxParam = -1; }
                    Object opts = j.get("options");
                    if (opts instanceof JSONArray) {
                        for (Object op : (JSONArray) opts) {
                            if (!(op instanceof JSONObject)) continue;
                            JSONObject jo = (JSONObject) op;
                            try {
                                int oid = Integer.parseInt(String.valueOf(jo.get("id")));
                                long opa = Long.parseLong(String.valueOf(jo.get("param")));
                                d.options.add(new ItemOption(oid, opa));
                            } catch (Exception e) {}
                        }
                    }
                    out.add(d);
                }
            }
        } catch (Exception e) {}
        return out;
    }

    public static synchronized void save() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            // xoa het va ghi lai (don gian, an toan)
            try (PreparedStatement del = con.prepareStatement("DELETE FROM panel_boss_override")) { del.executeUpdate(); }
            for (Map.Entry<String, BossOv> e : CACHE.entrySet()) {
                String f = e.getKey(); BossOv o = e.getValue();
                String dj = toJson(o.drops);
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO panel_boss_override (field,dame,hp_json,rest,maps,drop_json,note) VALUES (?,?,?,?,?,?,?)")) {
                    ps.setString(1, f);
                    ps.setDouble(2, o.dame);
                    ps.setString(3, o.hp == null ? "" : o.hp);
                    ps.setInt(4, o.rest);
                    ps.setString(5, o.maps == null ? "" : o.maps);
                    ps.setString(6, dj);
                    ps.setString(7, o.note == null ? "" : o.note);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            Logger.logException(BossTuning.class, e, "Save panel_boss_override that bai");
        }
    }

    public static String toJson(List<DropEntry> drops) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < drops.size(); i++) {
            DropEntry d = drops.get(i);
            if (i > 0) sb.append(",");
            sb.append("{\"temp_id\":").append(d.tempId).append(",\"qty\":").append(d.qty)
              .append(",\"rate\":").append(d.rate)
              .append(",\"min\":").append(d.minParam).append(",\"max\":").append(d.maxParam)
              .append(",\"options\":[");
            for (int j = 0; j < d.options.size(); j++) {
                ItemOption op = d.options.get(j);
                if (j > 0) sb.append(",");
                sb.append("{\"id\":").append(op.optionTemplate.id).append(",\"param\":").append(op.param).append("}");
            }
            sb.append("]}");
        }
        sb.append("]");
        return sb.toString();
    }

    public static synchronized BossOv get(String field) {
        BossOv o = CACHE.get(field);
        if (o == null) { o = new BossOv(); CACHE.put(field, o); }
        return o;
    }

    public static synchronized String set(String field, double dame, String hp, int rest, String maps,
                                          String dropJson, String note) {
        BossOv o = get(field);
        o.dame = dame;
        o.hp = hp == null ? "" : hp.trim();
        o.rest = rest;
        o.maps = maps == null ? "" : maps.trim();
        o.drops = parseDrops(dropJson);
        o.note = note == null ? "" : note;
        save();
        return "OK da luu override " + field;
    }

    public static synchronized String reset(String field) {
        CACHE.remove(field);
        save();
        return "OK da reset " + field + " ve goc (can goi lai boss de ap dung)";
    }

    public static synchronized Map<String, BossOv> all() { return new LinkedHashMap<>(CACHE); }

    public static double[] parseHp(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            String[] parts = s.trim().split("[,;\\s]+");
            double[] out = new double[parts.length];
            for (int i = 0; i < parts.length; i++) out[i] = Double.parseDouble(parts[i].trim());
            return out.length == 0 ? null : out;
        } catch (Exception e) { return null; }
    }

    public static int[] parseMaps(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            String[] parts = s.trim().split("[,;\\s]+");
            int[] out = new int[parts.length];
            for (int i = 0; i < parts.length; i++) out[i] = Integer.parseInt(parts[i].trim());
            return out.length == 0 ? null : out;
        } catch (Exception e) { return null; }
    }
}
