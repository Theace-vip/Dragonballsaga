package panel;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** 1 dong item trong tab_shop, thay cho JSON tho. */
public class ShopItemModel {
    public int tempId;
    public String itemName = "";
    public int cost;
    public byte typeSell; // 0=Vang 1=Ngoc 3=Ruby 4=Coupon/DiemSK
    public boolean isNew;
    public boolean isSell = true;
    public int itemSpec;
    public List<Opt> options = new ArrayList<>();

    public static class Opt {
        public int id; public long param;
        public Opt(int id, long param) { this.id = id; this.param = param; }
    }

    public ShopItemModel copy() {
        ShopItemModel c = new ShopItemModel();
        c.tempId = tempId; c.itemName = itemName; c.cost = cost;
        c.typeSell = typeSell; c.isNew = isNew; c.isSell = isSell; c.itemSpec = itemSpec;
        for (Opt o : options) c.options.add(new Opt(o.id, o.param));
        return c;
    }

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        o.put("cost", cost);
        o.put("type_sell", (int) typeSell);
        o.put("is_new", isNew);
        o.put("temp_id", tempId);
        o.put("item_spec", itemSpec);
        JSONArray arr = new JSONArray();
        for (Opt op : options) {
            JSONObject jo = new JSONObject();
            jo.put("param", op.param);
            jo.put("id", op.id);
            arr.add(jo);
        }
        o.put("options", arr);
        o.put("is_sell", isSell);
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

    public static ShopItemModel fromJson(JSONObject o) {
        ShopItemModel m = new ShopItemModel();
        try {
            m.cost = Integer.parseInt(String.valueOf(o.get("cost")));
            m.typeSell = Byte.parseByte(String.valueOf(o.get("type_sell")));
            Object inew = o.get("is_new");
            m.isNew = inew instanceof Boolean ? (Boolean) inew : Boolean.parseBoolean(String.valueOf(inew));
            m.tempId = Integer.parseInt(String.valueOf(o.get("temp_id")));
            Object spec = o.get("item_spec");
            try { m.itemSpec = Integer.parseInt(String.valueOf(spec)); } catch (Exception e) { m.itemSpec = 0; }
            Object sell = o.get("is_sell");
            m.isSell = sell == null ? true : (sell instanceof Boolean ? (Boolean) sell : Boolean.parseBoolean(String.valueOf(sell)));
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
}
