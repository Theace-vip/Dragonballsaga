package jdbc.daos;

import item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import shop.ItemShop;
import shop.Shop;
import shop.TabShop;
import services.ItemService;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    public static List<Shop> getShops(Connection con) {
        List<Shop> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from shop order by npc_id asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Shop shop = new Shop();
                shop.id = rs.getInt("id");
                shop.npcId = rs.getByte("npc_id");
                shop.tagName = rs.getString("tag_name");
                shop.typeShop = rs.getByte("type_shop");
                loadShopTab(con, shop);
                list.add(shop);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
        return list;
    }

    private static void loadShopTab(Connection con, Shop shop) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from tab_shop where shop_id = ? order by tab_index asc");
            ps.setInt(1, shop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TabShop tab = new TabShop();
                tab.shop = shop;
                tab.id = rs.getInt("id");
                tab.name = rs.getString("tab_name").replaceAll("<>", "\n");
                tab.index = rs.getInt("tab_index");
                loadItemShop(con, tab);
                shop.tabShops.add(tab);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
    }

    private static void loadItemShop(Connection con, TabShop tabShop) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from tab_shop where tab_index = ? and shop_id = ?");
            ps.setInt(1, tabShop.index);
            ps.setInt(2, tabShop.shop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONArray dataArray;
                JSONValue jv = new JSONValue();
                JSONObject dataObject;
                dataArray = (JSONArray) jv.parse(rs.getString("items"));
                for (Object o : dataArray) {
                    Item item = null;
                    dataObject = (JSONObject) o;
                    ItemShop itemShop = new ItemShop();
                    itemShop.tabShop = tabShop;
                    itemShop.id = tabShop.itemShops.size() + 1;
                    itemShop.temp = ItemService.gI().getTemplate(Short.parseShort(String.valueOf(dataObject.get("temp_id"))));
                    itemShop.isNew = Boolean.parseBoolean(String.valueOf(dataObject.get("is_new")));
                    itemShop.cost = Integer.parseInt(String.valueOf(dataObject.get("cost")));
                    itemShop.iconSpec = Integer.parseInt(String.valueOf(dataObject.get("item_spec")));
                    itemShop.typeSell = Byte.parseByte(String.valueOf(dataObject.get("type_sell")));
                    JSONArray options = (JSONArray) dataObject.get("options");
                    for (int j = 0; j < options.size(); j++) {
                        JSONObject opt = (JSONObject) options.get(j);
                        itemShop.options.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get("id"))), Integer.parseInt(String.valueOf(opt.get("param")))));
                    }
                    boolean isSell = Boolean.parseBoolean(String.valueOf(dataObject.get("is_sell")));
                    if (isSell) {
                        tabShop.itemShops.add(itemShop);
                    }
                }
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
    }

    /**
     * Tao shop moi tu Admin Panel.
     * @param con connection
     * @param npcId NPC hien co (byte)
     * @param tagName tag dinh danh (ShopService.opendShop(tagName))
     * @param name ten shop (tab mac dinh)
     * @param typeShop loai shop (byte)
     * @param itemsJson JSON items theo format tab_shop.items
     * @return id shop moi, hoac -1 neu that bai
     */
    public static int createShopFromPanel(Connection con, int npcId, String tagName, String name, int typeShop, String itemsJson) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO shop (npc_id, tag_name, type_shop, panel_created) VALUES (?,?,?,1)",
                java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setByte(1, (byte) npcId);
            ps.setString(2, tagName);
            ps.setByte(3, (byte) typeShop);
            ps.executeUpdate();
            int shopId = -1;
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) shopId = gk.getInt(1);
            }
            ps.close();
            if (shopId > 0) {
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO tab_shop (shop_id, tab_name, tab_index, items, panel_created) VALUES (?,?,0,?,1)");
                ps2.setInt(1, shopId);
                ps2.setString(2, name == null ? tagName : name);
                ps2.setString(3, itemsJson == null || itemsJson.isEmpty() ? "[]" : itemsJson);
                ps2.executeUpdate();
                ps2.close();
            }
            return shopId;
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
            return -1;
        }
    }

}
