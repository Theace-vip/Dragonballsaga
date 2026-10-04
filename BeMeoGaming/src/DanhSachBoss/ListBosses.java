/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DanhSachBoss;

import com.google.gson.Gson; 
import com.google.gson.reflect.TypeToken;
import item.Item;
import item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.util.*;
import org.json.simple.JSONObject;
import services.ItemService;
/**
 *
 * @author HairMod
 */
public class ListBosses { 
    public static List<BossStruct> listBosses = new ArrayList<>();
    public static void loadBosses(Connection connection) { 
        try {
            String sql = "SELECT * FROM listBosses";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BossStruct b = new BossStruct();
                b.id = rs.getInt("id");
                b.name = rs.getString("name"); 
                b.hp = rs.getString("hp");
                b.dame = rs.getString("dame"); 
                b.timeAppear = rs.getString("appearTime");
                String mapAppearStr = rs.getString("mapAppear");
               if (mapAppearStr != null && !mapAppearStr.isEmpty()) {
                    try { 
                        mapAppearStr = mapAppearStr.replaceAll("[\\{\\}]", "");  
                        String[] parts = mapAppearStr.split(",");
                        b.mapAppears = new int[parts.length];
                        for (int i = 0; i < parts.length; i++) {
                            b.mapAppears[i] = Integer.parseInt(parts[i].trim());  
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        b.mapAppears = new int[0];  
                    }
                } else {
                    b.mapAppears = new int[0];  
                }
                b.head = rs.getShort("head");
                b.body = rs.getShort("body");
                b.leg = rs.getShort("leg");
                b.dropItems = parseDropItems(rs);

                listBosses.add(b); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
   private static List<Item> parseDropItems(ResultSet rs) {
    List<Item> list = new ArrayList<>(); 
    try {
        String dropJson = rs.getString("dropItems");
        if (dropJson != null && !dropJson.isEmpty()) {
            Object parsed = JSONValue.parse(dropJson); 
            if (parsed instanceof JSONArray) {
                JSONArray arr = (JSONArray) parsed;
                for(int i = 0; i < arr.size(); i++){
                    JSONObject js = (JSONObject)arr.get(i);
                    Item item = parseItemFromJson(js);
                    if(item != null) {
                        list.add(item);
                    }
                }
            } else if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                for(int i = 0; i < obj.size(); i++){
                    JSONObject js = (JSONObject)obj.get(String.valueOf(i));
                    if(js != null) {
                        Item item = parseItemFromJson(js);
                        if(item != null) {
                            list.add(item);
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing drop items: " + e.getMessage());
        e.printStackTrace();
    }
    return list;
}

private static Item parseItemFromJson(JSONObject js) {
    try {
        int tempID = Integer.parseInt(js.get("id").toString());
        if(tempID != -1){
            int quantity = Integer.parseInt(js.get("quantity").toString());
            Item item = ItemService.gI().createNewItem(tempID, quantity);
            
            // Xử lý options
            Object optionsObj = js.get("options");
            if(optionsObj != null){
                Object parsedOptions = JSONValue.parse(optionsObj.toString());
                
                if(parsedOptions instanceof JSONArray) {
                    JSONArray optionsArray = (JSONArray) parsedOptions;
                    item.itemOptions = new ArrayList<>();
                    for(int j = 0; j < optionsArray.size(); j++){
                        JSONObject o = (JSONObject)optionsArray.get(j);
                        int optionID = Integer.parseInt(o.get("id").toString());
                        long param = Long.parseLong(o.get("param").toString());
                        item.itemOptions.add(new ItemOption(optionID, param));
                    }
                } else if(parsedOptions instanceof JSONObject) {
                    JSONObject optionsObj2 = (JSONObject) parsedOptions;
                    item.itemOptions = new ArrayList<>();
                    for(int j = 0; j < optionsObj2.size(); j++){
                        JSONObject o = (JSONObject)optionsObj2.get(String.valueOf(j));
                        if(o != null) {
                            int optionID = Integer.parseInt(o.get("id").toString());
                            long param = Long.parseLong(o.get("param").toString());
                            item.itemOptions.add(new ItemOption(optionID, param));
                        }
                    }
                }
            } else {
                item.itemOptions = new ArrayList<>();
            }
            return item;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
