package panel;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** 1 dong qua trong giftcode.detail, thay cho JSON tho. temp_id -1=Vang -2=Ngoc -3=Ngoc khoa. */
public class GiftItemModel {
    public int tempId;
    public String itemName = "";
    public int quantity = 1;
    public List<Opt> options = new ArrayList<>();

    public static class Opt {
        public int id; public long param;
        public Opt(int id, long param) { this.id = id; this.param = param; }
    }

    public GiftItemModel copy() {
        GiftItemModel c = new GiftItemModel();
        c.tempId = tempId; c.itemName = itemName; c.quantity = quantity;
        for (Opt o : options) c.options.add(new Opt(o.id, o.param));
        return c;
    }

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put("temp_id", String.valueOf(tempId));
        o.put("quantity", String.valueOf(quantity));
        JSONArray arr = new JSONArray();
        for (Opt op : options) {
            JSONObject jo = new JSONObject();
            jo.put("id", String.valueOf(op.id));
            jo.put("param", String.valueOf(op.param));
            arr.add(jo);
        }
        o.put("options", arr);
        return o;
    }

    public String optionsCsv() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(options.get(i).id).append(":").append(options.get(i).param);
        }
        return sb.toString();
    }

    public static GiftItemModel fromJson(JSONObject o) {
        GiftItemModel m = new GiftItemModel();
        try {
            m.tempId = Integer.parseInt(String.valueOf(o.get("temp_id")));
            Object q = o.get("quantity");
            try { m.quantity = Integer.parseInt(String.valueOf(q)); } catch (Exception e) { m.quantity = 1; }
            Object arr = o.get("options");
            if (arr instanceof JSONArray) {
                for (Object oo : (JSONArray) arr) {
                    if (oo instanceof JSONObject) {
                        JSONObject jo = (JSONObject) oo;
                        int id = Integer.parseInt(String.valueOf(jo.get("id")));
                        long param = Long.parseLong(String.valueOf(jo.get("param")));
                        m.options.add(new Opt(id, param));
                    }
                }
            }
        } catch (Exception e) { }
        return m;
    }

    public static String specialName(int tempId) {
        if (tempId == -1) return "Vang (tien)";
        if (tempId == -2) return "Ngoc (tien)";
        if (tempId == -3) return "Ngoc khoa (tien)";
        return null;
    }
}
