package Top;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnecter;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import player.Player;
import item.Item;
import services.ItemService;
import services.func.TopService;

/**
 *
 * @author Admin
 */
public class TopTutien {

    @Getter
    private List<Player> list = new ArrayList<>();
    private static final TopTutien INSTANCE = new TopTutien();

    public static TopTutien getInstance() {
        return INSTANCE;
    }

   public void load() {
    list.clear();
    try (Connection con = DBConnecter.getConnectionServer();
         PreparedStatement ps = con.prepareStatement(
             "SELECT id, name, head, gender, " +
             "CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(SagaTuTien, ',', 2), ',', -1) AS UNSIGNED) AS tutien2 " +
             "FROM player " +
             "ORDER BY tutien2 DESC " +
             "LIMIT 100"
         );
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Player player = processPlayerResultSet(rs);
            list.add(player);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    TopService.arrListTop.set(9, new ArrayList<>(list));
}

  private Player processPlayerResultSet(ResultSet rs) throws SQLException {
    Player player = new Player();
    player.id = rs.getInt("id");
    player.name = rs.getString("name");
    player.head = rs.getShort("head");
    player.gender = rs.getByte("gender");

    // chỉ gán biến thứ 2
    player.SagaTuTien[1] = rs.getLong("tutien2");
    

    return player;
}


    private void extractDataPoint(String dataPoint, Player player) {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = (JSONArray) jv.parse(dataPoint);
        player.nPoint.power = Double.parseDouble(dataArray.get(1).toString());
        dataArray.clear();
    }

    private void extractItemsBody(String itemsBody, Player player) {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = (JSONArray) jv.parse(itemsBody);

        for (Object itemDataObject : dataArray) {
            Item item = createItemFromDataObject(itemDataObject.toString());
            player.inventory.itemsBody.add(item);
        }

        dataArray.clear();
    }

    private Item createItemFromDataObject(String itemData) {
        JSONValue jv = new JSONValue();
        JSONArray dataObject = (JSONArray) jv.parse(itemData);
        short tempId = Short.parseShort(String.valueOf(dataObject.get(0)));
        Item item;
        if (tempId != -1) {
            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get(1))));
            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get(2)).replaceAll("\"", ""));

            for (Object option : options) {
                JSONArray opt = (JSONArray) jv.parse(String.valueOf(option));
                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                        Integer.parseInt(String.valueOf(opt.get(1)))));
            }
            item.createTime = Long.parseLong(String.valueOf(dataObject.get(3)));
            if (ItemService.gI().isOutOfDateTime(item)) {
                item = ItemService.gI().createItemNull();
            }
        } else {
            item = ItemService.gI().createItemNull();
        }

        return item;
    }
}
