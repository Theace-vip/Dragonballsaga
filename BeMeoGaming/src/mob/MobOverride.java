package mob;

import item.Item.ItemOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnecter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utils.Logger;
import map.Zone;

/**
 * MobOverride - quan ly override / them quai tu bang panel_map_mob (DB).
 *  - Ghi de HP/SD/level/drop cho quai co san (is_new=0, khop map+mob_temp+mob_index)
 *  - Them quai moi vao map (is_new=1)
 *  - Hot-reload: goi reload() de doc lai DB ma khong can restart.
 */
public class MobOverride {

    private static MobOverride I;
    public static MobOverride gI() {
        if (I == null) I = new MobOverride();
        return I;
    }

    /** 1 dong trong panel_map_mob. */
    public static class Row {
        public int mapId;
        public int mobIndex;
        public int mobTemp;
        public int level;
        public double hp;      // 0 = giu goc
        public int sd;         // -1 = giu goc (percentDame)
        public int x;
        public int y;
        public String dropJson;
        public boolean isNew;
        public List<panel.tuning.DropEntry> drops;
    }

    private final List<Row> rows = new ArrayList<>();

    public MobOverride() { reload(); }

    public synchronized void reload() {
        rows.clear();
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM panel_map_mob")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Row r = new Row();
                        r.mapId = rs.getInt("map_id");
                        r.mobIndex = rs.getInt("mob_index");
                        r.mobTemp = rs.getInt("mob_temp");
                        r.level = rs.getInt("level");
                        r.hp = rs.getDouble("hp");
                        r.sd = rs.getInt("sd");
                        r.x = rs.getInt("x");
                        r.y = rs.getInt("y");
                        r.dropJson = rs.getString("drop_json");
                        r.isNew = rs.getInt("is_new") == 1;
                        r.drops = parseDrops(r.dropJson);
                        rows.add(r);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MobOverride.class, e, "Load panel_map_mob that bai");
        }
    }

    public static List<panel.tuning.DropEntry> parseDrops(String json) {
        List<panel.tuning.DropEntry> out = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return out;
        try {
            Object obj = JSONValue.parse(json.trim());
            if (obj instanceof JSONArray) {
                for (Object o : (JSONArray) obj) {
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject j = (JSONObject) o;
                    panel.tuning.DropEntry d = new panel.tuning.DropEntry();
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

    /** Tim override cho quai co san (map + mob_temp + index). */
    public Row findOverride(int mapId, int mobTemp, int mobIndex) {
        for (Row r : rows) {
            if (!r.isNew && r.mapId == mapId && r.mobTemp == mobTemp && r.mobIndex == mobIndex) return r;
        }
        return null;
    }

    /** Danh sach quai moi them vao 1 map. */
    public List<Row> newMobs(int mapId) {
        List<Row> out = new ArrayList<>();
        for (Row r : rows) {
            if (r.isNew && r.mapId == mapId) out.add(r);
        }
        return out;
    }

    /** Drop cho mob (map + mob_temp + index). */
    public List<panel.tuning.DropEntry> getDrops(int mapId, int mobTemp, int mobIndex) {
        Row r = findOverride(mapId, mobTemp, mobIndex);
        if (r != null && r.drops != null && !r.drops.isEmpty()) return r.drops;
        return new ArrayList<>();
    }

    /** Ap dung override HP/SD/level vao mob da tao. */
    public void apply(Mob mob, int mapId) {
        try {
            Row r = findOverride(mapId, mob.tempId, mob.id);
            if (r == null) return;
            if (r.level > 0) mob.level = (byte) r.level;
            if (r.hp > 0) {
                mob.point.setHpFull(r.hp);
                mob.point.sethp(r.hp);
                mob.setTiemNang();
            }
            if (r.sd >= 0 && r.sd <= 127) {
                mob.pDame = (byte) r.sd;
                mob.setTiemNang();
            }
        } catch (Exception e) { }
    }

    /** Tao mob moi tu Row (is_new). */
    public Mob makeNewMob(Row r, Zone zone) {
        try {
            mob.Mob mob = new mob.Mob();
            mob.id = zone.mobs.size();
            mob.tempId = r.mobTemp;
            mob.level = (byte) (r.level > 0 ? r.level : 1);
            models.Template.MobTemplate temp = server.Manager.getMobTemplateByTemp(r.mobTemp);
            if (temp != null) {
                mob.pDame = (byte) (r.sd >= 0 ? r.sd : temp.percentDame);
                mob.pTiemNang = temp.percentTiemNang;
                mob.type = temp.type;
            }
            mob.point.setHpFull(r.hp > 0 ? r.hp : 100);
            mob.point.sethp(mob.point.getHpFull());
            mob.location.x = (short) (r.x != 0 ? r.x : 300);
            mob.location.y = (short) (r.y != 0 ? r.y : 300);
            mob.setTiemNang();
            return mob;
        } catch (Exception e) {
            return null;
        }
    }
}
