package AutoGiftTop;

import item.Item;
import item.Item.ItemOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import jdbc.DBConnecter;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import utils.Logger;
import utils.TimeUtil;
import utils.Util;

/**
 *
 * @author Son
 */
public class AutoGiftTopService {

    public static final ArrayList<AutoGiftTop> listTopAuto = new ArrayList<>();
    public static boolean isRunning;
    public static StringBuilder textInfo = new StringBuilder();

    private static AutoGiftTopService I;

    public static AutoGiftTopService gI() {
        if (I == null) {
            I = new AutoGiftTopService();
            I.activeAutoGiftTop();
        }
        return I;
    }

    public AutoGiftTop getTop(int id) {
        return listTopAuto.get(id);
    }

    public static void loadListAutoTop(Connection con) {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM auto_gift_top"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AutoGiftTop autoTop = new AutoGiftTop();
                autoTop.id = rs.getInt("id_top");
                autoTop.name = rs.getString("name_top");
                autoTop.timeReward = rs.getTimestamp("date_time");
                autoTop.isReceive = Byte.parseByte(rs.getString("is_receive")) != 0;
                JSONArray players = (JSONArray) JSONValue.parse(rs.getString("user_receive"));
                if (players != null) {
                    for (int i = 0; i < players.size(); i++) {
                        String arrPlayer[] = players.get(i).toString().split("#");
                        autoTop.listReceive.put(Integer.valueOf(arrPlayer[0].replace("pId:", "")), arrPlayer[1].replace("tLog:", ""));
                    }
                    players.clear();
                }
                listTopAuto.add(autoTop);
            }
            for (int topId = 0; topId < listTopAuto.size(); topId++) {
                for (int i = 0; i < 10; i++) {
                    addItemTop(topId, i);
                }
                //print info item gift
                getInfoItemGift(topId);
            }
        } catch (SQLException e) {
            Logger.logException(AutoGiftTopService.class, e);
        }
        Logger.logln("AUTOGIFTTOP [" + listTopAuto.size() + "]");
    }

    public static void getInfoItemGift(int topId) {
        //print info item gift
        textInfo.append("|7|>>>>>>>>>>>>>>[").append(listTopAuto.get(topId).name).append("]<<<<<<<<<<<<<<<\n").append("|3|Ngày kết thúc: ").append(TimeUtil.formatTime(listTopAuto.get(topId).timeReward, "dd/MM/yyyy HH:mm:ss")).append("\n");
        for (Integer top : listTopAuto.get(topId).items.keySet()) {
            textInfo.append("|-1|TOP ").append(top + 1).append("\n|3|");
            for (int j = 0; j < listTopAuto.get(topId).items.get(top).size(); j++) {
                textInfo.append(listTopAuto.get(topId).items.get(top).get(j).template.name).append(" x").append(Util.format(listTopAuto.get(topId).items.get(top).get(j).quantity)).append("\n");
            }
        }
        textInfo.append("\n");
    }

    private void activeAutoGiftTop() {
        new Thread(() -> {
            new Timer("Auto Gift Item Top").schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean isLoadTop = false;
                    isRunning = true;
                    for (AutoGiftTop autoTop : listTopAuto) {
                        if (autoTop.isToDate() && !autoTop.isReceive && !autoTop.items.isEmpty()) {
                            if (!isLoadTop) {
                                Manager.reloadtop();
                                isLoadTop = true;
                            }
                            System.err.println("Update Auto Trao Quà " + autoTop.name + " Running.........");
                            try {
                                //top 1-10
                                for (int i = 0; i < 10; i++) {
                                    switch (autoTop.id) {
                                        case 13 -> {
                                            try {
                                                if (autoTop.isNonReceive(Manager.TopCauCa.get(i).getId_player())) {
                                                    InventoryService.gI().addItemMail(i, autoTop, Manager.TopCauCa.get(i).getId_player());
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        case 12 -> {
                                            try {
                                                if (autoTop.isNonReceive(Manager.TopTambao.get(i).getId_player())) {
                                                    InventoryService.gI().addItemMail(i, autoTop, Manager.TopTambao.get(i).getId_player());
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        case 5 -> {
                                            try {
                                                if (autoTop.isNonReceive(Manager.TopNap.get(i).getId_player())) {
                                                    InventoryService.gI().addItemMail(i, autoTop, Manager.TopNap.get(i).getId_player());
                                                }
                                            } catch (Exception e) {
                                            }
                                        }

                                    }
                                }
                                autoTop.update();
                                System.err.println("Update Auto Trao Quà " + autoTop.name + " Completed.........");
                            } catch (Exception e) {
                                Logger.logException(AutoGiftTopService.class, e);
                            }
                        }
                    }
                    isRunning = false;
                }
            }, listTopAuto.get(0).timeReward);
            System.err.println("Thread Update Auto Gift Item Top Running.........");
        }).start();
    }

    private static void addItemTop(int topId, int top) {
        ArrayList<Item> items = new ArrayList<>();
        Item item;
        //Top Nạp
        switch (topId) {
            case 13 -> {
                switch (top) {
                    case 0 -> {
                        item = ItemService.gI().createNewItem(1213, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(5, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1716, 1);
                        item.itemOptions.add(new ItemOption(236, 50));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1698, 1);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                    }
                    case 1 -> {
                        item = ItemService.gI().createNewItem(1213, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 300000));
                        item.itemOptions.add(new ItemOption(77, 300000));
                        item.itemOptions.add(new ItemOption(103, 300000));
                        item.itemOptions.add(new ItemOption(5, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1715, 1);
                        item.itemOptions.add(new ItemOption(236, 40));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1698, 1);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                    }
                    case 2 -> {
                        item = ItemService.gI().createNewItem(1213, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 200000));
                        item.itemOptions.add(new ItemOption(77, 200000));
                        item.itemOptions.add(new ItemOption(103, 200000));
                        item.itemOptions.add(new ItemOption(5, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1714, 1);
                        item.itemOptions.add(new ItemOption(236, 20));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1698, 1);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 3 -> {
                        item = ItemService.gI().createNewItem(1213, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 100000));
                        item.itemOptions.add(new ItemOption(77, 100000));
                        item.itemOptions.add(new ItemOption(103, 100000));
                        item.itemOptions.add(new ItemOption(5, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1713, 1);
                        item.itemOptions.add(new ItemOption(236, 10));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1698, 1);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 4 -> {

                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 5 -> {
                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 6 -> {

                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 7 -> {
                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 8 -> {
                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    case 9 -> {
                        item = ItemService.gI().createNewItem(457, 500000);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1855, 30);
                        item.itemOptions.add(new ItemOption(30, 100));
                        items.add(item);
                    }
                    default -> {
                    }
                }
                listTopAuto.get(topId).items.put(top, new ArrayList<>(items));
                items.clear();
            }

            case 12 -> {
                //Top Sức Mạnh
                switch (top) {
                    case 0 -> {
                        item = ItemService.gI().createNewItem(1753, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 99));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1132, 100);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1127, 20);
                        items.add(item);

                        item = ItemService.gI().createNewItem(457, 500000);
                        items.add(item);

                    }
                    case 1 -> {
                        item = ItemService.gI().createNewItem(1753, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 65));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1132, 80);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1127, 15);
                        items.add(item);

                        item = ItemService.gI().createNewItem(457, 500000);
                        items.add(item);
                    }
                    case 2 -> {
                        item = ItemService.gI().createNewItem(1753, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 45));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1132, 60);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1127, 10);
                        items.add(item);

                        item = ItemService.gI().createNewItem(457, 500000);
                        items.add(item);

                    }
                    case 3 -> {
                        item = ItemService.gI().createNewItem(1753, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 30));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1132, 30);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1127, 6);
                        items.add(item);

                        item = ItemService.gI().createNewItem(457, 500000);
                        items.add(item);

                    }
                    case 4 -> {

                        item = ItemService.gI().createNewItem(1753, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 18));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1132, 20);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1127, 2);
                        items.add(item);

                        item = ItemService.gI().createNewItem(457, 500000);
                        items.add(item);

                    }
                    case 5 -> {

                        item = ItemService.gI().createNewItem(531, 1);
                        item.itemOptions.add(new ItemOption(50, 9999));
                        item.itemOptions.add(new ItemOption(9, 999));
                        item.itemOptions.add(new ItemOption(107, 8));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);

                    }
                    case 6 -> {

                        item = ItemService.gI().createNewItem(531, 1);
                        item.itemOptions.add(new ItemOption(50, 9999));
                        item.itemOptions.add(new ItemOption(9, 999));
                        item.itemOptions.add(new ItemOption(107, 8));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);

                    }
                    case 7 -> {

                        item = ItemService.gI().createNewItem(531, 1);
                        item.itemOptions.add(new ItemOption(50, 9999));
                        item.itemOptions.add(new ItemOption(9, 999));
                        item.itemOptions.add(new ItemOption(107, 8));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);
                    }
                    case 8 -> {

                        item = ItemService.gI().createNewItem(531, 1);
                        item.itemOptions.add(new ItemOption(50, 9999));
                        item.itemOptions.add(new ItemOption(9, 999));
                        item.itemOptions.add(new ItemOption(107, 8));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);

                    }
                    case 9 -> {

                        item = ItemService.gI().createNewItem(531, 1);
                        item.itemOptions.add(new ItemOption(50, 9999));
                        item.itemOptions.add(new ItemOption(9, 999));
                        item.itemOptions.add(new ItemOption(107, 8));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);
                    }
                    default -> {
                    }
                }
                listTopAuto.get(topId).items.put(top, new ArrayList<>(items));
                items.clear();
            }
            case 5 -> {
                //Top Sức Mạnh
                switch (top) {
                    case 0 -> {
                        item = ItemService.gI().createNewItem(1188, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 500000));
                        item.itemOptions.add(new ItemOption(77, 500000));
                        item.itemOptions.add(new ItemOption(103, 500000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 99));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1224, 10000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1123, 50);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 40);
                        items.add(item);

                    }
                    case 1 -> {
                        item = ItemService.gI().createNewItem(1188, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 400000));
                        item.itemOptions.add(new ItemOption(77, 400000));
                        item.itemOptions.add(new ItemOption(103, 400000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 65));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1224, 7000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1123, 45);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 20);
                        items.add(item);
                    }
                    case 2 -> {
                        item = ItemService.gI().createNewItem(1188, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 300000));
                        item.itemOptions.add(new ItemOption(77, 300000));
                        item.itemOptions.add(new ItemOption(103, 300000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 45));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1224, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1123, 30);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 18);
                        items.add(item);

                    }
                    case 3 -> {
                        item = ItemService.gI().createNewItem(1188, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 200000));
                        item.itemOptions.add(new ItemOption(77, 200000));
                        item.itemOptions.add(new ItemOption(103, 200000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 30));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1224, 3000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1123, 20);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 10);
                        items.add(item);

                    }
                    case 4 -> {
                        item = ItemService.gI().createNewItem(1188, 1);
                        item.itemOptions.add(new ItemOption(59, 0));
                        item.itemOptions.add(new ItemOption(50, 100000));
                        item.itemOptions.add(new ItemOption(77, 100000));
                        item.itemOptions.add(new ItemOption(103, 100000));
                        item.itemOptions.add(new ItemOption(204, 100));
                        item.itemOptions.add(new ItemOption(107, 18));
                        items.add(item);

                        item = ItemService.gI().createNewItem(1224, 2000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1123, 10);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 6);
                        items.add(item);

                    }
                    case 5 -> {

                        item = ItemService.gI().createNewItem(1225, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 5);
                        items.add(item);
                    }
                    case 6 -> {
                        item = ItemService.gI().createNewItem(1225, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 5);
                        items.add(item);

                    }
                    case 7 -> {
                        item = ItemService.gI().createNewItem(1225, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 5);
                        items.add(item);

                    }
                    case 8 -> {
                        item = ItemService.gI().createNewItem(1225, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 5);
                        items.add(item);

                    }
                    case 9 -> {
                        item = ItemService.gI().createNewItem(1225, 5000);
                        items.add(item);

                        item = ItemService.gI().createNewItem(1131, 5);
                        items.add(item);

                    }
                    default -> {
                    }
                }
                listTopAuto.get(topId).items.put(top, new ArrayList<>(items));
                items.clear();
            }

        }
    }

    public static class AutoGiftTop {

        public int id;
        public String name;
        public boolean isReceive;
        public HashMap<Integer, ArrayList<Item>> items = new HashMap<>();
        public HashMap<Integer, String> listReceive = new HashMap<>();
        public Timestamp timeReward;

        public boolean isToDate() {
            return System.currentTimeMillis() > timeReward.getTime();
        }

        private boolean isNonReceive(int id) {
            return listReceive.get(id) == null;
        }

        public void addPlayerReceive(int id) {
            listReceive.put(id, TimeUtil.getTimeNow("HH:mm:ss") + " (" + TimeUtil.getTimeNow("dd-MM-yyyy") + ")");
            JSONArray dataArray = new JSONArray();
            for (Integer i : this.listReceive.keySet()) {
                dataArray.add("pId:" + i + "#tLog:" + this.listReceive.get(i));
            }
            try {
                DBConnecter.executeUpdate("UPDATE auto_gift_top SET user_receive = ? WHERE name_top = ?", dataArray.toJSONString(), name);
            } catch (Exception e) {
                Logger.logException(AutoGiftTopService.class, e);
            } finally {
                dataArray.clear();
            }
        }

        public void update() {
            this.isReceive = true;
            try {
                DBConnecter.executeUpdate("UPDATE auto_gift_top SET is_receive = 1 WHERE name_top = ?", name);
            } catch (Exception e) {
                Logger.logException(AutoGiftTopService.class, e);
            }
        }
    }
}
