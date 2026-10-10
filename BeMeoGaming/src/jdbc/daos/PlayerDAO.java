package jdbc.daos;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.cj.log.Log;
import jdbc.DBConnecter;
import item.Item;
import item.ItemTime;
import player.Friend;
import player.Fusion;
import player.Inventory;
import player.Player;
import skill.Skill;
import services.MapService;
import utils.Logger;
import utils.SkillUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import jdbc.NDVResultSet;

import models.Template;
import models.Template.SkillTemplate;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import services.func.ChangeMapService;
import utils.TimeUtil;
import utils.Util;

public class PlayerDAO {

    @SuppressWarnings("unchecked")
    public static boolean createNewPlayer(int userId, String name, byte gender, int hair) {
        try {
            int power = 1000;
            JSONArray dataArray = new JSONArray();

            dataArray.add(0); //vàng
            dataArray.add(100000); //ngọc xanh
            dataArray.add(100); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event

            String inventory = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(21 + gender); //map
            dataArray.add(100); //x
            dataArray.add(384); //y
            String location = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //giới hạn sức mạnh
            dataArray.add(2000000); //sức mạnh
            dataArray.add(2000000); //tiềm năng
            dataArray.add(100); //thể lực
            dataArray.add(100); //thể lực đầy
            dataArray.add(gender == 0 ? 20 : 10); //hp gốc
            dataArray.add(gender == 1 ? 20 : 10); //ki gốc
            dataArray.add(gender == 2 ? 15 : 10); //sức đánh gốc
            dataArray.add(0); //giáp gốc
            dataArray.add(0); //chí mạng gốc
            dataArray.add(0); //năng động
            dataArray.add(gender == 0 ? 200 : 100); //hp hiện tại
            dataArray.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(10); //level
            dataArray.add(0); //curent pea
            dataArray.add(0); //is upgrade
            dataArray.add(new Date().getTime()); //last time harvest
            dataArray.add(new Date().getTime()); //last time upgrade
            String magicTree = dataArray.toJSONString();
            dataArray.clear();
            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */

            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            long hp = gender == 0 ? 30 : 20;

            JSONArray item = new JSONArray();
            JSONArray options = new JSONArray();
            JSONArray opt = new JSONArray();
            for (int i = 0; i < 17; i++) {
                switch (i) {
                    case 0:
                        //áo
                        opt.add(47); //id option
                        opt.add(50000); //param option
                        item.add(idAo); //id item
                        item.add(1); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 1:
                        //quần
                        opt.add(6); //id option
                        opt.add(2000000); //param option
                        item.add(idQuan); //id item
                        item.add(1); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 5:
                        //quần
                        opt.add(101); //id option
                        opt.add(50); //param option
                        item.add(977); //id item
                        item.add(1); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 8:
                        //quần
                        opt.add(101); //id option
                        opt.add(50); //param option
                        item.add(966); //id item
                        item.add(1); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                        
                        
                       

                    default:
                        item.add(-1); //id item
                        item.add(0); //số lượng
                        break;
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBody = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                switch (i) {
                    case 0:
                        opt.add(30); //id option
                        opt.add(1); //param option
                        item.add(194); //id item
                        item.add(1); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 1:
                        opt.add(30); //id option
                        opt.add(1); //param option
                        item.add(457); //id item
                        item.add(10000); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 2:
                        opt.add(30); //id option
                        opt.add(1); //param option
                        item.add(1413); //id item
                        item.add(100); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
                    case 3:
                        opt.add(30); //id option
                        opt.add(1); //param option
                        item.add(1280); //id item
                        item.add(100); //số lượng
                        options.add(opt.toJSONString());
                        opt.clear();
                        break;
//                    case 4:
//                        opt.add(30); //id option
//                        opt.add(1); //param option
//                        item.add(890); //id item
//                        item.add(5); //số lượng
//                        options.add(opt.toJSONString());
//                        opt.clear();
//                        break;
//
//                    case 5:
//                        opt.add(30); //id option
//                        opt.add(1); //param option
//                        item.add(891); //id item
//                        item.add(5); //số lượng
//                        options.add(opt.toJSONString());
//                        opt.clear();
//                        break;
//                    case 6:
//                        opt.add(30); //id option
//                        opt.add(1); //param option
//                        item.add(465); //id item
//                        item.add(5); //số lượng
//                        options.add(opt.toJSONString());
//                        opt.clear();
//                        break;
//                    case 7:
//                        opt.add(30); //id option
//                        opt.add(1); //param option
//                        item.add(466); //id item
//                        item.add(5); //số lượng
//                        options.add(opt.toJSONString());
//                        opt.clear();
//                        break;
//                    case 8:
//                        opt.add(30); //id option
//                        opt.add(1); //param option
//                        item.add(579); //id item
//                        item.add(10); //số lượng
//                        options.add(opt.toJSONString());
//                        opt.clear();
//                        break;

                    default:
                        item.add(-1); //id item
                        item.add(0); //số lượng
                        break;
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }

            String itemsBag = dataArray.toJSONString();
            dataArray.clear();

            String homThu = dataArray.toJSONString();

            for (int i = 0; i < 110; i++) {
                if (i == 1) { //rada
                    opt.add(14); //id option
                    opt.add(1); //param option
                    item.add(12); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBoxLuckyRound = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsDaBan = dataArray.toJSONString();
            dataArray.clear();

            String friends = dataArray.toJSONString();
            String enemies = dataArray.toJSONString();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            dataArray.add(0); //chỉ số 2
            dataArray.add(0); //số lần mở
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            String intrinsic = dataArray.toJSONString();
            dataArray.clear();

            //tạo mới item time
            dataArray.add(0);//sau mà có thêm 1 item mới thì nhớ chỗ này nha thêm 1 dòng như này nữa tạo thêm 1 dòng này nữa
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này 
            dataArray.add(0);//như này 
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này 
            dataArray.add(0);//như này
            dataArray.add(0);//như này 
            dataArray.add(0);//như này 
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này 
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này
            dataArray.add(0);//như này

            String itemtimeNew = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //mở giới hạn sức mạnh
            dataArray.add(0); //máy dò
            dataArray.add(0); //thức ăn cold
            dataArray.add(0); //icon thức ăn cold
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            String itemTime = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(7); //id nhiệm vụ
            dataArray.add(0); //index nhiệm vụ con
            dataArray.add(0); //số lượng đã làm
            String task = dataArray.toJSONString();
            dataArray.clear();

            String mabuEgg = dataArray.toJSONString();

            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ
            dataArray.add(System.currentTimeMillis()); //bùa mạnh mẽ
            dataArray.add(System.currentTimeMillis()); //bùa da trâu
            dataArray.add(System.currentTimeMillis()); //bùa oai hùng
            dataArray.add(System.currentTimeMillis()); //bùa bất tử
            dataArray.add(System.currentTimeMillis()); //bùa dẻo dai
            dataArray.add(System.currentTimeMillis()); //bùa thu hút
            dataArray.add(System.currentTimeMillis()); //bùa đệ tử
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x3
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x4
            String charms = dataArray.toJSONString();
            dataArray.clear();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 19, 24, 27, 28, 29}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 19, 26, 27, 28, 29}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 19, 25, 27, 28, 29};
            //[{"temp_id":"4","point":0,"last_time_use":0},]

            JSONArray skill = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                skill.add(skillsArr[i]); //id skill
                skill.add(getMaxPointSkill(skillsArr[i])); //full cap ngay khi tao nhan vat
                skill.add(0); //thời gian sử dụng trước đó
                dataArray.add(skill.toString());
                skill.clear();
            }
            String skills = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            String skillsShortcut = dataArray.toJSONString();
            dataArray.clear();

            String petData = dataArray.toJSONString();

            JSONArray blackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackBall.add(0);
                blackBall.add(0);
                blackBall.add(0);
                dataArray.add(blackBall.toJSONString());
                blackBall.clear();
            }
            String dataBlackBall = dataArray.toString();
            dataArray.clear();

            dataArray.add(-1); //id side task
            dataArray.add(0); //thời gian nhận
            dataArray.add(0); //số lượng đã làm
            dataArray.add(0); //số lượng cần làm
            dataArray.add(20); //số nhiệm vụ còn lại có thể nhận
            dataArray.add(31); //mức độ nhiệm vụ
            String dataSideTask = dataArray.toJSONString();
            dataArray.clear();

            String dataBadges = "[]";
            String dataTaskBadges = "[]";
            String dailyGift = "[]";
            int rank = 1000;

            DBConnecter.executeUpdate("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id, "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, items_daban, friends, enemies, data_intrinsic,item_time_new, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
                    + "data_black_ball, data_side_task, dataBadges, dataTaskBadges, dailyGift,power,rank, homthu, leothap) "
                    + "values ()", userId, name, hair, gender, 0, -1, inventory, location, point, magicTree,
                    itemsBody, itemsBag, itemsBox, itemsBoxLuckyRound, itemsDaBan, friends, enemies, intrinsic,
                    itemtimeNew, itemTime, task, mabuEgg, charms, skills, skillsShortcut, petData, dataBlackBall, dataSideTask, dataBadges, dataTaskBadges, dailyGift, power, rank, homThu, "[]");
            Logger.success("Tạo player mới thành công!\n");
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi tạo player mới");
            return false;
        }
    }

    //lay cap toi da cua skill template - dung khi tao nhan vat moi (full cap ngay)
    private static int getMaxPointSkill(int tempId) {
        try {
            SkillTemplate template = SkillUtil.findSkillTemplate(tempId);
            if (template != null && template.maxPoint >= 1) {
                int max = template.maxPoint;
                if (template.skillss != null && !template.skillss.isEmpty()) {
                    max = Math.min(max, template.skillss.size());
                }
                return Math.max(1, max);
            }
        } catch (Exception e) {
        }
        return 1;
    }

    @SuppressWarnings("unchecked")
    public static void updatePlayer(Player player
    ) {
        if (player != null && player.iDMark != null && player.iDMark.isLoadedAllDataPlayer()) {
            long st = System.currentTimeMillis();
            try {
                JSONArray dataArray = new JSONArray();

                //data kim lượng
                dataArray.add(player.inventory.gold > Inventory.LIMIT_GOLD
                        + (player.SagaThienDao >= 300 ? player.SagaThienDao / 5 * 1000000000L : 0)
                                ? Inventory.LIMIT_GOLD
                                + (player.SagaThienDao >= 300 ? player.SagaThienDao / 5 * 1000000000L : 0)
                                : player.inventory.gold);
                dataArray.add(player.inventory.gem);
                dataArray.add(player.inventory.ruby);
                dataArray.add(player.inventory.coupon);
                dataArray.add(player.inventory.event);
                String inventory = dataArray.toJSONString();
                dataArray.clear();

                int mapId = player.mapIdBeforeLogout;
                int x = player.location.x;
                int y = player.location.y;
                double hp = player.nPoint.hp;
                double mp = player.nPoint.mp;
                if (player.isDie()) {
                    mapId = player.gender + 21;
                    x = 300;
                    y = 336;
                    hp = 1;
                    mp = 1;
                } else {
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId) || ChangeMapService.gI().checkMapCanJoin(player, MapService.gI().getMapCanJoin(player, mapId, 0)) == null) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                    }
                }

                //data vị trí
                dataArray.add(mapId);
                dataArray.add(x);
                dataArray.add(y);
                String location = dataArray.toJSONString();
                dataArray.clear();

                //data chỉ số
                dataArray.add(player.nPoint.limitPower);
                dataArray.add(player.nPoint.power);
                dataArray.add(player.nPoint.tiemNang);
                dataArray.add(player.nPoint.stamina);
                dataArray.add(player.nPoint.maxStamina);
                dataArray.add(player.nPoint.hpg);
                dataArray.add(player.nPoint.mpg);
                dataArray.add(player.nPoint.dameg);
                dataArray.add(player.nPoint.defg);
                dataArray.add(player.nPoint.critg);
                dataArray.add(0);
                dataArray.add(hp);
                dataArray.add(mp);
                String point = dataArray.toJSONString();
                dataArray.clear();

                //data đậu thần
                dataArray.add(player.magicTree.level);
                dataArray.add(player.magicTree.currPeas);
                dataArray.add(player.magicTree.isUpgrade ? 1 : 0);
                dataArray.add(player.magicTree.lastTimeHarvest);
                dataArray.add(player.magicTree.lastTimeUpgrade);
                String magicTree = dataArray.toJSONString();
                dataArray.clear();

                //data Homthu
                //hom thu
                Gson gson = new Gson();
                String homthu = gson.toJson(player.homThu);

                //Phuc Loi (format: [listNhan, listOnline, listDiemDanh, phutOnline, weekTimeLogin])
                JSONArray listNhanArray = new JSONArray();
                for (int i : player.listNhan) {
                    listNhanArray.add(i);
                }
                JSONArray listOnlineArray = new JSONArray();
                for (int i : player.listOnline) {
                    listOnlineArray.add(i);
                }
                JSONArray listDiemDanhArray = new JSONArray();
                for (int i : player.listDiemDanh) {
                    listDiemDanhArray.add(i);
                }
                dataArray.add(listNhanArray);
                dataArray.add(listOnlineArray);
                dataArray.add(listDiemDanhArray);
                dataArray.add(player.phutOnline);
                dataArray.add(player.weekTimeLogin.getTime());
                String phucLois = dataArray.toJSONString();
                dataArray.clear();

                //data body
                JSONArray dataItem = new JSONArray();
                for (Item item : player.inventory.itemsBody) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBody = dataArray.toJSONString();
                dataArray.clear();

                //data bag
                for (Item item : player.inventory.itemsBag) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBag = dataArray.toJSONString();
                dataArray.clear();

                //data box
                for (Item item : player.inventory.itemsBox) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBox = dataArray.toJSONString();
                dataArray.clear();

                //data box crack ball
                for (Item item : player.inventory.itemsBoxCrackBall) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBoxLuckyRound = dataArray.toJSONString();
                dataArray.clear();

                //data item da ban
                for (Item item : player.inventory.itemsDaBan) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsDaBan = dataArray.toJSONString();
                dataArray.clear();

                //data bạn bè
                JSONArray dataFE = new JSONArray();
                for (Friend f : player.friends) {
                    dataFE.add(f.id);
                    dataFE.add(f.name);
                    dataFE.add(f.head);
                    dataFE.add(f.body);
                    dataFE.add(f.leg);
                    dataFE.add(f.bag);
                    dataFE.add(f.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String friend = dataArray.toJSONString();
                dataArray.clear();

                //data kẻ thù
                for (Friend e : player.enemies) {
                    dataFE.add(e.id);
                    dataFE.add(e.name);
                    dataFE.add(e.head);
                    dataFE.add(e.body);
                    dataFE.add(e.leg);
                    dataFE.add(e.bag);
                    dataFE.add(e.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String enemy = dataArray.toJSONString();
                dataArray.clear();

                //data nội tại
                dataArray.add(player.playerIntrinsic.intrinsic.id);
                dataArray.add(player.playerIntrinsic.intrinsic.param1);
                dataArray.add(player.playerIntrinsic.intrinsic.param2);
                dataArray.add(player.playerIntrinsic.countOpen);
                dataArray.add(player.effectSkill.isIntrinsic);
                dataArray.add(player.effectSkill.skillID);
                dataArray.add(player.effectSkill.cooldown);
                dataArray.add(player.effectSkill.lastTimeUseSkill);
                String intrinsic = dataArray.toJSONString();
                dataArray.clear();

                //tạo mới itemtime
                dataArray.add((player.itemTime.isItemTest ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeItemTest)) : 0));
                dataArray.add((player.itemTime.isdanbacdau ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.tgianbacdau)) : 0));
                dataArray.add((player.itemTime.isBINHX3 ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX3)) : 0));
                dataArray.add((player.itemTime.isBINHX5 ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX5)) : 0));
                dataArray.add((player.itemTime.isBINHX7 ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX7)) : 0));

                //
                dataArray.add((player.itemTime.isLYNUOCMIATO ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeLYNUOCMIATO)) : 0));
                dataArray.add((player.itemTime.isLYNUOCMIATHOM ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeLYNUOCMIATHOM)) : 0));
                dataArray.add((player.itemTime.isLYNUOCMIASAURIENG ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastLYNUOCMIASAURIENG)) : 0));
                dataArray.add((player.itemTime.isMAYDOSKH ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoskh)) : 0));
                dataArray.add((player.itemTime.isMAYDOSKHVIP ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoskhvip)) : 0));
                dataArray.add((player.itemTime.isBINHX10 ? (ItemTime.TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX10)) : 0));

                dataArray.add((player.itemTime.iscuongnosieucap ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimecnsc)) : 0));
                dataArray.add((player.itemTime.isbohuyetsieucap ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebhsc)) : 0));
                dataArray.add((player.itemTime.isbokhisieucap ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebksc)) : 0));
                dataArray.add((player.itemTime.isgiapxensieucap ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimegxsc)) : 0));

                dataArray.add((player.itemTime.isUpTbx2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx2)) : 0));
                dataArray.add((player.itemTime.isUpTbx3 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx3)) : 0));
                dataArray.add((player.itemTime.isUpTbx4 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx4)) : 0));
                dataArray.add((player.itemTime.isUpTbx5 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx5)) : 0));
                dataArray.add((player.itemTime.isUpTbx6 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx6)) : 0));
                dataArray.add((player.itemTime.isUpTbx7 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx7)) : 0));
                dataArray.add((player.itemTime.isBanhTrungDacBiet ? (ItemTime.TIME_150p - (System.currentTimeMillis() - player.itemTime.lastTimeBanhDacBiet)) : 0));
                dataArray.add((player.itemTime.isBanhTrungThu ? (ItemTime.TIME_150p - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungthu)) : 0));
                dataArray.add((player.itemTime.isBanhTrung1Trung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanh1Trung)) : 0));
                dataArray.add((player.itemTime.isBanhTrung2Trung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanh2Trung)) : 0));
                dataArray.add((player.itemTime.isBanhgaquay ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimegaquay)) : 0));
                dataArray.add((player.itemTime.isBanhthapcam ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimethapcam)) : 0));

                // [The Diem Farm 27/09/2026] 3 slot cuoi (27,28,29) - khi doc phai size-guard
                dataArray.add((player.itemTime.isFamX2 ? (ItemTime.thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX2)) : 0));
                dataArray.add((player.itemTime.isFamX3 ? (ItemTime.thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX3)) : 0));
                dataArray.add((player.itemTime.isFamX5 ? (ItemTime.thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX5)) : 0));

                String itemTimeNew = dataArray.toJSONString();
                dataArray.clear();

                //tạo mới itemtime
                dataArray.add(player.CheckDayOnl);
                dataArray.add(player.diemdanh);
                String diemdanh = dataArray.toJSONString();
                dataArray.clear();

//                //tạo mới itemtime
//                dataArray.add(player.vip);
//                dataArray.add(player.timevip);
//                String Saga_VIP = dataArray.toJSONString();
//                dataArray.clear();
                //data item time
                dataArray.add((player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0));
                dataArray.add((player.itemTime.isUseBoHuyet2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2)) : 0));
                dataArray.add((player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0));
                dataArray.add((player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0));
                dataArray.add((player.itemTime.isUseMayDo2 ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) : 0));
                dataArray.add((player.itemTime.isDuoiKhiTT ? (panel.tuning.TrungThuTuning.getLong("drop.tailSeconds", 900) * 1000L - (System.currentTimeMillis() - player.itemTime.lastTimeDuoiKhiTT)) : 0));
                dataArray.add((player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0));
                dataArray.add(player.itemTime.iconMeal);
                dataArray.add((player.itemTime.isUseTDLT ? ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000) : 0));
                dataArray.add((player.itemTime.isUseCMS ? (ItemTime.TIME_CMS - (System.currentTimeMillis() - player.itemTime.lastTimeUseCMS)) : 0));
                dataArray.add((player.itemTime.isUseGTPT ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGTPT)) : 0));
                dataArray.add((player.itemTime.isUseDK ? (ItemTime.TIME_DK - (System.currentTimeMillis() - player.itemTime.lastTimeUseDK)) : 0));
                dataArray.add((player.itemTime.isUseRX ? ((player.itemTime.timeRX - (System.currentTimeMillis() - player.itemTime.lastTimeUseRX)) / 60 / 1000) : 0));
                dataArray.add((player.itemTime.isEatMeal2 ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal2)) : 0));
                dataArray.add(player.itemTime.iconMeal2);
                dataArray.add(0);
                dataArray.add((player.itemTime.isUseNCD ? (ItemTime.TIME_NCD - (System.currentTimeMillis() - player.itemTime.lastTimeUseNCD)) : 0));
                dataArray.add(0);
                dataArray.add(0);
                String itemTime = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ
                dataArray.add(player.playerTask.taskMain.id);
                dataArray.add(player.playerTask.taskMain.index);
                dataArray.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                dataArray.add(player.playerTask.taskMain.lastTime);
                String task = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                dataArray.add(player.playerTask.sideTask.receivedTime);
                dataArray.add(player.playerTask.sideTask.count);
                dataArray.add(player.playerTask.sideTask.maxCount);
                dataArray.add(player.playerTask.sideTask.leftTask);
                dataArray.add(player.playerTask.sideTask.level);
                String sideTask = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bư
                if (player.mabuEgg != null) {
                    dataArray.add(player.mabuEgg.lastTimeCreate);
                    dataArray.add(player.mabuEgg.timeDone);
                }
                String mabuEgg = dataArray.toJSONString();
                dataArray.clear();

                //data bùa
                dataArray.add(player.charms.tdTriTue);
                dataArray.add(player.charms.tdManhMe);
                dataArray.add(player.charms.tdDaTrau);
                dataArray.add(player.charms.tdOaiHung);
                dataArray.add(player.charms.tdBatTu);
                dataArray.add(player.charms.tdDeoDai);
                dataArray.add(player.charms.tdThuHut);
                dataArray.add(player.charms.tdDeTu);
                dataArray.add(player.charms.tdTriTue3);
                dataArray.add(player.charms.tdTriTue4);
                String charm = dataArray.toJSONString();
                dataArray.clear();

                //data skill
                JSONArray dataSkill = new JSONArray();
                for (Skill skill : player.playerSkill.skills) {
                    dataSkill.add(skill.template.id);
                    dataSkill.add(skill.point);
                    dataSkill.add(skill.lastTimeUseThisSkill);
                    dataSkill.add(skill.currLevel);
                    dataArray.add(dataSkill.toJSONString());
                    dataSkill.clear();
                }
                String skills = dataArray.toJSONString();
                dataArray.clear();
                dataArray.clear();

                //data skill shortcut
                for (int skillId : player.playerSkill.skillShortCut) {
                    dataArray.add(skillId);
                }
                String skillShortcut = dataArray.toJSONString();
                dataArray.clear();

                String pet = dataArray.toJSONString();
                String petInfo;
                String petPoint;
                String petBody;
                String petSkill;

                //data pet
                if (player.pet != null) {
                    dataArray.add(player.pet.typePet);
                    dataArray.add(player.pet.gender);
                    dataArray.add(player.pet.name);
                    dataArray.add(player.fusion.typeFusion);
                    int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                    dataArray.add(timeLeftFusion < 0 ? 0 : timeLeftFusion);
                    dataArray.add(player.pet.status);
                    petInfo = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(player.pet.nPoint.limitPower);
                    dataArray.add(player.pet.nPoint.power);
                    dataArray.add(player.pet.nPoint.tiemNang);
                    dataArray.add(player.pet.nPoint.stamina);
                    dataArray.add(player.pet.nPoint.maxStamina);
                    dataArray.add(player.pet.nPoint.hpg);
                    dataArray.add(player.pet.nPoint.mpg);
                    dataArray.add(player.pet.nPoint.dameg);
                    dataArray.add(player.pet.nPoint.defg);
                    dataArray.add(player.pet.nPoint.critg);
                    dataArray.add(player.pet.nPoint.hp);
                    dataArray.add(player.pet.nPoint.mp);
                    petPoint = dataArray.toJSONString();
                    dataArray.clear();

                    JSONArray items = new JSONArray();
                    JSONArray options = new JSONArray();
                    JSONArray opt = new JSONArray();
                    for (Item item : player.pet.inventory.itemsBody) {
                        if (item.isNotNullItem()) {
                            dataItem.add(item.template.id);
                            dataItem.add(item.quantity);
                            for (Item.ItemOption io : item.itemOptions) {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                                options.add(opt.toJSONString());
                                opt.clear();
                            }
                            dataItem.add(options.toJSONString());
                        } else {
                            dataItem.add(-1);
                            dataItem.add(0);
                            dataItem.add(options.toJSONString());
                        }

                        dataItem.add(item.createTime);

                        items.add(dataItem.toJSONString());
                        dataItem.clear();
                        options.clear();
                    }
                    petBody = items.toJSONString();

                    JSONArray petSkills = new JSONArray();
                    for (Skill s : player.pet.playerSkill.skills) {
                        JSONArray pskill = new JSONArray();
                        if (s.skillId != -1) {
                            pskill.add(s.template.id);
                            pskill.add(s.point);
                            pskill.add(s.lastTimeUseThisSkill);
                            pskill.add(s.currLevel);
                        } else {
                            pskill.add(-1);
                            pskill.add(0);
                            pskill.add(0);
                            pskill.add(0);
                        }
                        petSkills.add(pskill.toJSONString());
                    }
                    petSkill = petSkills.toJSONString();

                    dataArray.add(petInfo);
                    dataArray.add(petPoint);
                    dataArray.add(petBody);
                    dataArray.add(petSkill);

                    pet = dataArray.toJSONString();
                }
                dataArray.clear();

                dataArray.add(player.SagaTuTien[0]);
                dataArray.add(player.SagaTuTien[1]);
                dataArray.add(player.SagaTuTien[2]);
                String SagaTuTien = dataArray.toJSONString();
                dataArray.clear();
                if (player.Tamkjll_Ma_cot > 0) {
                    dataArray.add(player.Tamkjll_Tu_Ma);
                    dataArray.add(player.Tamkjll_Exp_Tu_Ma);
                    dataArray.add(player.Tamkjll_Ma_Hoa);
                    dataArray.add(player.TamkjllLasttimeMaHoa);
                    dataArray.add(player.Tamkjll_Ma_cot);
                }
                String TamkjllTuMa = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.TamkjllDauLaDaiLuc[0]);
                dataArray.add(player.TamkjllDauLaDaiLuc[1]);
                dataArray.add(player.TamkjllDauLaDaiLuc[2]);
                dataArray.add(player.TamkjllDauLaDaiLuc[3]);
                dataArray.add(player.TamkjllDauLaDaiLuc[4]);
                dataArray.add(player.TamkjllDauLaDaiLuc[5]);
                dataArray.add(player.TamkjllDauLaDaiLuc[6]);
                dataArray.add(player.TamkjllDauLaDaiLuc[7]);
                dataArray.add(player.TamkjllDauLaDaiLuc[8]);
                dataArray.add(player.TamkjllDauLaDaiLuc[9]);
                dataArray.add(player.TamkjllDauLaDaiLuc[10]);
                dataArray.add(player.TamkjllDauLaDaiLuc[11]);
                dataArray.add(player.TamkjllDauLaDaiLuc[12]);
                dataArray.add(player.TamkjllDauLaDaiLuc[13]);
                dataArray.add(player.TamkjllDauLaDaiLuc[14]);
                dataArray.add(player.TamkjllDauLaDaiLuc[15]);
                dataArray.add(player.TamkjllDauLaDaiLuc[16]);
                dataArray.add(player.TamkjllDauLaDaiLuc[17]);
                dataArray.add(player.TamkjllDauLaDaiLuc[18]);
                dataArray.add(player.TamkjllDauLaDaiLuc[19]);
                dataArray.add(player.TamkjllDauLaDaiLuc[20]);
                String TamkjllDLDL = dataArray.toJSONString();
                dataArray.clear();

                if (player.TamkjllPetGiong >= 0 && player.TamkjllPetGiong <= 10) {
                    dataArray.add(player.TamkjllPetGiong);
                    dataArray.add(player.TamkjllNamePet);
                    dataArray.add(player.TamkjllPetHunger);
                    dataArray.add(player.TamkjllPetPower);
                    dataArray.add(player.TamkjlllastTimeThucan);
                    dataArray.add(player.EmBeLv);
                    dataArray.add(player.EmBeEXP);
                } else {
                    dataArray.add(-1);
                }
                String Tamkjll_Pet = dataArray.toJSONString();
                dataArray.clear();

                //data thưởng ngọc rồng đen
                for (int i = 0; i < player.rewardBlackBall.timeOutOfDateReward.length; i++) {
                    JSONArray dataBlackBall = new JSONArray();
                    dataBlackBall.add(player.rewardBlackBall.timeOutOfDateReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.lastTimeGetReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.quantilyBlackBall[i]);
                    dataArray.add(dataBlackBall.toJSONString());
                    dataBlackBall.clear();
                }
                String dataBlackBall = dataArray.toJSONString();
                dataArray.clear();

                //Ma Bao Ve
                dataArray.add(player.mbv);
                dataArray.add(player.baovetaikhoan);
                dataArray.add(player.mbvtime);
                String dataBVTK = dataArray.toJSONString();
                dataArray.clear();

                //Card
                String dataCard = JSONValue.toJSONString(player.Cards);

                //BDKB
                dataArray.add(player.timesPerDayBDKB);
                dataArray.add(player.lastTimeJoinBDKB);
                String dataBDKB = dataArray.toJSONString();
                dataArray.clear();

                //CDRD
                dataArray.add(player.joinCDRD);
                dataArray.add(player.lastTimeJoinCDRD);
                dataArray.add(player.talkToThuongDe);
                dataArray.add(player.talkToThanMeo);
                String dataCDRD = dataArray.toJSONString();
                dataArray.clear();

                //Nhận Thỏi Vàng
                dataArray.add(player.danhanthoivang);
                dataArray.add(player.lastRewardGoldBarTime);
                String dataNhanThoiVang = dataArray.toJSONString();
                dataArray.clear();

                //Rương Gỗ
                dataArray.add(player.levelWoodChest);
                dataArray.add(player.goldChallenge);
                dataArray.add(player.rubyChallenge);
                dataArray.add(player.lastTimeRewardWoodChest);
                dataArray.add(player.lastTimePKDHVT23);
                String dataRuongGo = dataArray.toJSONString();
                dataArray.clear();

                //Siêu thần thủy
                dataArray.add(player.winSTT);
                dataArray.add(player.lastTimeWinSTT);
                dataArray.add(player.callBossPocolo);
                String dataSieuThanThuy = dataArray.toJSONString();
                dataArray.clear();

                //Võ đài sinh tử
                dataArray.add(player.haveRewardVDST);
                dataArray.add(player.thoiVangVoDaiSinhTu);
                dataArray.add(player.lastTimePKVoDaiSinhTu);
                dataArray.add(player.timePKVDST);
                String dataVoDaiSinhTu = dataArray.toJSONString();
                dataArray.clear();

                //Data item event
                dataArray.add(player.itemEvent.remainingTVGSCount);
                dataArray.add(player.itemEvent.lastTVGSTime);
                dataArray.add(player.itemEvent.remainingHHCount);
                dataArray.add(player.itemEvent.lastHHTime);
                dataArray.add(player.itemEvent.remainingBNCount);
                dataArray.add(player.itemEvent.lastBNTime);
                String dataItemEvent = dataArray.toJSONString();
                dataArray.clear();

                //Data Luyện Tập
                dataArray.add(player.levelLuyenTap);
                dataArray.add(player.dangKyTapTuDong);
                dataArray.add(player.mapIdDangTapTuDong);
                dataArray.add(player.tnsmLuyenTap);
                if (player.isOffline) {
                    dataArray.add(player.lastTimeOffline);
                } else {
                    dataArray.add(System.currentTimeMillis());
                }
                dataArray.add(player.traning.getTop());
                dataArray.add(player.traning.getTime());
                dataArray.add(player.traning.getLastTime());
                dataArray.add(player.traning.getLastTop());
                dataArray.add(player.traning.getLastRewardTime());

                String dataLuyenTap = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ bang hàng ngày
                dataArray.add(player.playerTask.clanTask.template != null ? player.playerTask.clanTask.template.id : -1);
                dataArray.add(player.playerTask.clanTask.receivedTime);
                dataArray.add(player.playerTask.clanTask.count);
                dataArray.add(player.playerTask.clanTask.maxCount);
                dataArray.add(player.playerTask.clanTask.leftTask);
                dataArray.add(player.playerTask.clanTask.level);
                String clanTask = dataArray.toJSONString();
                dataArray.clear();

                //data vip
                dataArray.add(player.timesPerDayCuuSat);
                dataArray.add(player.lastTimeCuuSat);
                dataArray.add(player.nhanDeTuNangVIP);
                dataArray.add(player.nhanVangNangVIP);
                dataArray.add(player.nhanSKHVIP);
                String dataVip = dataArray.toJSONString();
                dataArray.clear();

                //data super rank
                dataArray.add(player.superRank.lastTimePK);
                dataArray.add(player.superRank.lastTimeReward);
                dataArray.add(player.superRank.ticket);
                dataArray.add(player.superRank.win);
                dataArray.add(player.superRank.lose);
                JsonObject jsonObject = new JsonObject();
                JsonArray stringArray = new JsonArray();
                for (String str : player.superRank.history) {
                    stringArray.add(str);
                }
                JsonArray longArray = new JsonArray();
                for (Long value : player.superRank.lastTime) {
                    longArray.add(value);
                }
                jsonObject.add("history", stringArray);
                jsonObject.add("lasttime", longArray);
                String jsonString = new Gson().toJson(jsonObject);
                dataArray.add(jsonString);
                String dataSuperRank = dataArray.toJSONString();
                dataArray.clear();

                //data achievement
                if (player.achievement != null) {
                    for (Template.AchievementQuest aq : player.achievement.getAchievementList()) {
                        JSONArray a = new JSONArray();
                        a.add(aq.completed);
                        a.add(aq.isRecieve);
                        dataArray.add(a.toJSONString());
                        a.clear();
                    }
                }
                String achievement = dataArray.toJSONString();
                dataArray.clear();

                //gift code
                for (String code : player.giftCode.rewards) {
                    dataArray.add(code);
                }
                String giftCode = dataArray.toJSONString();
                dataArray.clear();

                String dataBadges = JSONValue.toJSONString(player.dataBadges);

                String dataTaskBadges = JSONValue.toJSONString(player.dataTaskBadges);

                String dataDailyGift = JSONValue.toJSONString(player.dailyGiftData);

                JSONArray dataLeoThap = new JSONArray();
                dataLeoThap.add(player.tangThap);
                dataLeoThap.add(player.levelThap);
                dataLeoThap.add(player.pointThap);
                String leoThap = dataLeoThap.toJSONString();
                dataLeoThap.clear();

                String query = "update player set head = ?, have_tennis_space_ship = ?, "
                        + "clan_id = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?, "
                        + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, items_daban = ?, friends = ?, "
                        + "enemies = ?, data_intrinsic = ?,item_time_new  = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?, pet = ?, "
                        + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?, skills_shortcut = ?, notify = ?, "
                        + "baovetaikhoan = ?, data_card = ?, lasttimepkcommeson = ?, bandokhobau = ?, doanhtrai = ?, conduongrandoc = ?, masterDoesNotAttack = ?, "
                        + "nhanthoivang = ?, ruonggo = ?, sieuthanthuy = ?, vodaisinhtu = ?, rongxuong = ?, data_item_event = ?, data_luyentap = ?, data_clan_task = ?, data_vip = ?, "
                        + "rank = ?, data_super_rank = ?, data_achievement = ?, giftcode = ?, firstTimeLogin = ?, dataBadges = ?, dataTaskBadges = ?, dailyGift = ?, power = ?, "
                        + "homthu = ?, phuc_loi = ?,diemfam =? ,sukien =?, point_phongtap =? , point_sb =?, point_vithu =?,isbienhinh=?, leoThap =?,Saga_VIP=?,diemdanh=?,"
                        + "SagaThienDao = ?, PhapTac_ThienDao= ?, TamkjllCapPb= ?,SagaChuyenSinh= ?, SagaDiaDao= ?, DLbTamkjll= ?, TamkjllThomo= ?, TamkjllThomoExp= ?,SagaTuTien= ?, TamkjllDLDL= ?, Tamkjll_Pet= ?, Tamkjll_Tu_Ma= ? ,SukienTamBao =? ,point_bdkb = ?,point_moruong =? ,point_vip =?,point_dapdo =?,"
                        + "dakethon =? ,duockethon =? ,NapDau =? ,point_cauca =?,tho_nguyen =? ,point_PassFree =? ,point_PassVIP =? ,point_MokhoaVip =?,SoXuMenhDaNhan =?,SoXuMenhDaNhanvip =? where id = ?";

                DBConnecter.executeUpdate(query,
                        player.head,
                        player.haveTennisSpaceShip,
                        (player.clan != null ? player.clan.id : -1),
                        inventory,
                        location,
                        point,
                        magicTree,
                        itemsBody,
                        itemsBag,
                        itemsBox,
                        itemsBoxLuckyRound,
                        itemsDaBan,
                        friend,
                        enemy,
                        intrinsic,
                        itemTimeNew,
                        itemTime,
                        task,
                        mabuEgg,
                        pet,
                        dataBlackBall,
                        sideTask,
                        charm,
                        skills,
                        skillShortcut,
                        player.notify,
                        dataBVTK,
                        dataCard,
                        player.lastPkCommesonTime,
                        dataBDKB,
                        player.lastTimeJoinDT,
                        dataCDRD,
                        player.doesNotAttack,
                        dataNhanThoiVang,
                        dataRuongGo,
                        dataSieuThanThuy,
                        dataVoDaiSinhTu,
                        player.lastTimeShenronAppeared,
                        dataItemEvent,
                        dataLuyenTap,
                        clanTask,
                        dataVip,
                        player.superRank.rank,
                        dataSuperRank,
                        achievement,
                        giftCode,
                        Util.toDateString(player.firstTimeLogin),
                        dataBadges,
                        dataTaskBadges,
                        dataDailyGift,
                        player.nPoint.power,
                        homthu,
                        phucLois,
                        player.diemfam,
                        player.sukien,
                        player.point_event,
                        player.point_sb,
                        player.point_vithu,
                        player.isbienhinh,
                        leoThap,
                        player.Saga_VIP,
                        diemdanh,
                        player.SagaThienDao,
                        player.PhapTac_ThienDao,
                        player.TamkjllCapPb,
                        player.SagaChuyenSinh,
                        player.SagaDiaDao,
                        player.DLbTamkjll,
                        player.TamkjllThomo,
                        player.TamkjllThomoExp,
                        SagaTuTien,
                        TamkjllDLDL,
                        Tamkjll_Pet,
                        TamkjllTuMa,
                        player.SukienTamBao,
                        player.point_bdkb,
                        player.point_moruong,
                        player.point_vip,
                        player.point_dapdo,
                        player.dakethon,
                        player.duockethon,
                        player.NapDau,
                        player.point_cauca,
                        player.tho_nguyen,
                        player.point_PassFree,
                        player.point_PassVIP,
                        player.point_MokhoaVip,
                        player.SoXuMenhDaNhan,
                        player.SoXuMenhDaNhanvip,
                        player.id);
                if (player.isOffline) {
                    Logger.log(Logger.PURPLE, TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: Player " + player.name + " updated successfully! " + (System.currentTimeMillis() - st) + "ms\n");
                    player.dispose();
                } else {
                    //Logger.success(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: Player " + player.name + " saved successfully! " + (System.currentTimeMillis() - st) + "ms\n");
                }
            } catch (Exception e) {
                Logger.logException(PlayerDAO.class, e, "Lỗi save player " + player.name);
            }
        }
    }

    public static boolean checkLogout(Connection con, Player player) {
        long lastTimeLogout = 0;
        long lastTimeLogin = 0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from account where id = ? limit 1");
            ps.setInt(1, player.getSession().userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
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
            return false;
        }
        return lastTimeLogout > lastTimeLogin;
    }

    public static void updateVND(Player player) {
        try {
            NDVResultSet rs = DBConnecter.executeQuery("SELECT temp_vnd FROM account WHERE id = ?", player.getSession().userId);
            if (rs != null && rs.next()) { // Đảm bảo rs không bị null
                int tmp = rs.getInt("temp_vnd");
                tmp = Math.max(0, tmp);
                player.getSession().vnd += tmp;
                DBConnecter.executeUpdate("UPDATE account SET temp_vnd = 0 WHERE id = ?", player.getSession().userId);
                DBConnecter.executeUpdate("UPDATE account SET vnd = ? WHERE id = ?", player.getSession().vnd, player.getSession().userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void HoanTien(Player player, int amount) {
        try {
            String query = "UPDATE account SET vnd = vnd + ? WHERE id = ?";
            try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, amount);
                ps.setInt(2, player.getSession().userId);
                ps.executeUpdate();
            }
            player.getSession().vnd += amount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void updateVND(Player player){
//        try{
//               if (player == null) {
//            System.out.println("Player is null in updateVND.");
//            return;
//        }
//            NDVResultSet rs = DBConnecter.executeQuery("SELECT temp_vnd FROM account WHERE id = ?", player.getSession().userId);
//            if (rs != null && rs.next()) { // Đảm bảo rs không bị null
//                int tmp = rs.getInt("temp_vnd");
//                tmp = Math.max(0, tmp);
//                player.mocPhucLoi[2] += tmp;
//                player.mocPhucLoi[3] += tmp;
//                player.getSession().vnd += tmp;
//                DBConnecter.executeUpdate("UPDATE account SET temp_vnd = 0 WHERE id = ?", player.getSession().userId);
//                DBConnecter.executeUpdate("UPDATE account SET vnd = ? WHERE id = ?",player.getSession().vnd, player.getSession().userId);
//            }
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    public static void updateThoNguyen(Player player) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE player SET tho_nguyen = ? WHERE id = ?")) {
            ps.setLong(1, player.tho_nguyen);
            ps.setLong(2, player.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update tho_nguyen " + player.name);
        }
    }

    /**
     * Lưu riêng trạng thái Vòng Quay (Tầm Bảo): điểm quay + JSON danh sách id mốc đã nhận.
     * Tách khỏi updatePlayer để nếu DB chưa có 2 cột này thì save nhân vật chính vẫn chạy bình thường.
     */
    public static void saveVongQuay(Player player) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE player SET diem_quay = ?, active_vong_quay = ?, reset_vong_quay = ? WHERE id = ?")) {
            JSONArray data = new JSONArray();
            if (player.listNhan_TamBao != null) {
                for (Integer idMoc : player.listNhan_TamBao) {
                    if (idMoc != null) {
                        data.add(idMoc);
                    }
                }
            }
            ps.setInt(1, player.diem_quay);
            ps.setString(2, data.toJSONString());
            ps.setInt(3, player.reset_vong_quay);
            ps.setLong(4, player.id);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi lưu Vòng Quay (Tầm Bảo) " + player.name
                    + " - kiểm tra 2 cột diem_quay / active_vong_quay trong bảng player");
        }
    }

    /** Luu cap Cung Menh (JSON) - goi khi nang cap / dot pha tai Bo Mong. */
    public static void saveCungMenh(player.Player player) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE player SET cung_menh = ? WHERE id = ?")) {
            org.json.simple.JSONObject o = new org.json.simple.JSONObject();
            o.put("level", String.valueOf(player.cungMenhLevel));
            o.put("dotPha", String.valueOf(player.cungMenhDotPha));
            o.put("fixedOld", player.cungMenhFixedOldBonus ? "1" : "0");
            ps.setString(1, o.toJSONString());
            ps.setLong(2, player.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update cung_menh " + player.name);
        }
    }

    /** Luu Ban Nguyen Tinh Cau (JSON) - goi khi nang cap / dot pha tai Bo Mong. */
    public static void saveBanNguyen(player.Player player) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE player SET ban_nguyen = ? WHERE id = ?")) {
            org.json.simple.JSONObject o = new org.json.simple.JSONObject();
            o.put("level", String.valueOf(player.banNguyenLevel));
            o.put("tier", String.valueOf(player.banNguyenTier));
            ps.setString(1, o.toJSONString());
            ps.setLong(2, player.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update ban_nguyen " + player.name);
        }
    }

    public static void lockAccount(Player p) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE account SET ban = 1 WHERE id = ?")) {
            ps.setInt(1, p.getSession().userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateActive(Player player, int num) {
        String updateQuery = "UPDATE account SET active = ? WHERE id = ?";
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                player.getSession().actived = true;
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Error updating cash for player " + player.name);
            return false;
        }
    }

    public static void saveisBienHinh(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBConnecter.getConnectionServer();) {
            ps = con.prepareStatement("update player set isbienhinh = ? where id = ?");
            ps.setInt(1, player.isbienhinh);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.logException(PlayerDAO.class, e);
            }
        }
    }

    public static boolean subVND2(Player player, int ruby) {
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement("UPDATE account SET vnd = (vnd - ?), active = ? WHERE id = ?")) {
            if (!player.getSession().actived) {
                player.getSession().actived = true;
            }
            ps.setInt(1, ruby);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= ruby;
            return true;
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
        }
        return false;
    }

    public static boolean subcash(Player player, int num) {
        String updateQuery = "UPDATE account SET vnd = vnd - ? WHERE id = ?";
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                player.getSession().vnd -= num;
                player.SagaDiaDao += (num / 5000) * 1;
                player.SagaThienDao += (num / 10000) * 1;  
                player.Saga_VIP += num / Math.max(1, panel.tuning.SystemTuning.getI("vip_nap_moi")); // moi nap X Cash +1 VIP (panel)
                player.point_PassFree += (num / 1000) * 100;

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Error updating cash for player " + player.name);
            return false;
        }
    }

    public static boolean checkVnd(Player player, int giatri) {
        if (giatri <= 0) {
            return false;
        }
        try {
            NDVResultSet rs = DBConnecter.executeQuery("select vnd from account where id = ?", player.getSession().userId);
            if (rs != null && rs.next()) {
                int vnd = rs.getInt("vnd");
                if (vnd < giatri) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi check vnd " + player.name);
            return false;
        }
    }

    public static boolean subVnd(Player player, int vnd) {
        if (vnd <= 0) {
            return false;
        }
        try {
            int check = DBConnecter.executeUpdate("UPDATE account SET vnd = vnd - ? WHERE id = ?", vnd, player.getSession().userId);
            if (check == 0) {
                return false;
            }
            player.getSession().vnd -= vnd;
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
            return false;
        }
    }

    public static boolean subGoldBar(Player player, int num) {
        String updateQuery = "UPDATE account SET thoi_vang = thoi_vang - ? WHERE id = ?";
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                player.getSession().goldBar -= num;
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Error updating cash for player " + player.name);
            return false;
        }
    }

    public static void updateLastTimeUpdatePower(Player player, long num) {
        if (player == null) {
            return;
        }
        String query = "UPDATE player SET lastTimeUpdatePower = ? WHERE id = ?";
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, num);
            ps.setLong(2, player.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Error updating lastTimeUpdatePower for player: " + player.name);
        }
    }

    public static void updateLastTimeUpdateTask(Player player, long num) {
        if (player == null) {
            return;
        }
        String query = "UPDATE player SET lastTimeUpdateTask = ? WHERE id = ?";
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, num);
            ps.setLong(2, player.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(PlayerDAO.class, e, "Error updating lastTimeUpdateTask for player: " + player.name);
        }
    }

    public static void LogAddPoint(String name, int id, int point, String type) {
        System.out.println(name + " - " + id + " - " + point + " - " + type);
//        try {
//            NDVDB.executeUpdate("INSERT INTO histotyevent(name, id_account, event_point, type) VALUES ()", "cc", "cc", "cc", "cc");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public static boolean createPlAo(int userId, String name, byte gender, int hair) {
        try {
            JSONArray dataArray = new JSONArray();

            dataArray.add(2000000000); //vàng
            dataArray.add(100000); //ngọc xanh
            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event

            String inventory = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(Util.nextInt(173)); //map
            dataArray.add(100); //x
            dataArray.add(384); //y
            String location = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //giới hạn sức mạnh
            dataArray.add(2000); //sức mạnh
            dataArray.add(2000); //tiềm năng
            dataArray.add(1000); //thể lực
            dataArray.add(1000); //thể lực đầy
            dataArray.add(gender == 0 ? 200 : 100); //hp gốc
            dataArray.add(gender == 1 ? 200 : 100); //ki gốc
            dataArray.add(gender == 2 ? 15 : 10); //sức đánh gốc
            dataArray.add(0); //giáp gốc
            dataArray.add(0); //chí mạng gốc
            dataArray.add(0); //năng động
            dataArray.add(0); //hp hiện tại
            dataArray.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //level
            dataArray.add(5); //curent pea
            dataArray.add(0); //is upgrade
            dataArray.add(new Date().getTime()); //last time harvest
            dataArray.add(new Date().getTime()); //last time upgrade
            String magicTree = dataArray.toJSONString();
            dataArray.clear();
            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */

            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            long hp = gender == 0 ? 30 : 20;

            JSONArray item = new JSONArray();
            JSONArray options = new JSONArray();
            JSONArray opt = new JSONArray();
            for (int i = 0; i < 12; i++) {
                if (i == 0) { //áo
                    opt.add(47); //id option
                    opt.add(def); //param option
                    item.add(idAo); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 1) { //quần
                    opt.add(6); //id option
                    opt.add(hp); //param option
                    item.add(idQuan); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBody = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) {
                if (i == 0) { //thỏi vàng
                    opt.add(73); //id option
                    opt.add(1); //param option
                    item.add(457); //id item
                    item.add(10); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBag = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) {
                if (i == 0) { //rada
                    opt.add(14); //id option
                    opt.add(1); //param option
                    item.add(12); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBoxLuckyRound = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsDaBan = dataArray.toJSONString();
            dataArray.clear();

            String friends = dataArray.toJSONString();
            String enemies = dataArray.toJSONString();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            dataArray.add(0); //chỉ số 2
            dataArray.add(0); //số lần mở
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            String intrinsic = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //mở giới hạn sức mạnh
            dataArray.add(0); //máy dò
            dataArray.add(0); //thức ăn cold
            dataArray.add(0); //icon thức ăn cold
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            dataArray.add(0); //
            String itemTime = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(31); //id nhiệm vụ
            dataArray.add(0); //index nhiệm vụ con
            dataArray.add(0); //số lượng đã làm
            String task = dataArray.toJSONString();
            dataArray.clear();

            String mabuEgg = dataArray.toJSONString();

            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ
            dataArray.add(System.currentTimeMillis()); //bùa mạnh mẽ
            dataArray.add(System.currentTimeMillis()); //bùa da trâu
            dataArray.add(System.currentTimeMillis()); //bùa oai hùng
            dataArray.add(System.currentTimeMillis()); //bùa bất tử
            dataArray.add(System.currentTimeMillis()); //bùa dẻo dai
            dataArray.add(System.currentTimeMillis()); //bùa thu hút
            dataArray.add(System.currentTimeMillis()); //bùa đệ tử
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x3
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x4
            String charms = dataArray.toJSONString();
            dataArray.clear();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 19, 27, 28, 29}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 19, 27, 28, 29}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 19, 27, 28, 29};
            //[{"temp_id":"4","point":0,"last_time_use":0},]

            JSONArray skill = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                skill.add(skillsArr[i]); //id skill
                if (i == 0) {
                    skill.add(1); //level skill
                } else {
                    skill.add(0); //level skill
                }
                skill.add(0); //thời gian sử dụng trước đó
                dataArray.add(skill.toString());
                skill.clear();
            }
            String skills = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            String skillsShortcut = dataArray.toJSONString();
            dataArray.clear();

            String petData = dataArray.toJSONString();

            JSONArray blackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackBall.add(0);
                blackBall.add(0);
                blackBall.add(0);
                dataArray.add(blackBall.toJSONString());
                blackBall.clear();
            }
            String dataBlackBall = dataArray.toString();
            dataArray.clear();

            dataArray.add(-1); //id side task
            dataArray.add(0); //thời gian nhận
            dataArray.add(0); //số lượng đã làm
            dataArray.add(0); //số lượng cần làm
            dataArray.add(20); //số nhiệm vụ còn lại có thể nhận
            dataArray.add(0); //mức độ nhiệm vụ
            String dataSideTask = dataArray.toJSONString();
            dataArray.clear();

            DBConnecter.executeUpdate("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id, "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, items_daban, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
                    + "data_black_ball, data_side_task) "
                    + "values ()", userId, name, hair, gender, 0, -1, inventory, location, point, magicTree,
                    itemsBody, itemsBag, itemsBox, itemsBoxLuckyRound, itemsDaBan, friends, enemies, intrinsic,
                    itemTime, task, mabuEgg, charms, skills, skillsShortcut, petData, dataBlackBall, dataSideTask);
            Logger.success("New player created successfully!\n");
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi tạo player mới");
            return false;
        }
    }

}
