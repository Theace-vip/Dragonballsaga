package npc.npc_manifest;

/**
 *
 *
 */
import clan.Clan;
import consts.ConstNpc;
import consts.ConstTask;
import item.Item;
import java.util.ArrayList;
import jdbc.DBConnecter;
import jdbc.daos.PlayerDAO;
import models.TreasureUnderSea.TreasureUnderSea;
import models.TreasureUnderSea.TreasureUnderSeaService;
import npc.Npc;
import static npc.NpcFactory.PLAYERID_OBJECT;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.RewardService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import services.func.Input;
import services.func.TopService;
import services.func.UseItem;
import shop.ShopService;
import utils.Util;

public class QuyLaoKame extends Npc {

    public QuyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        Item ruacon = InventoryService.gI().findItemBag(player, 874);
        if (canOpenNpc(player)) {
            ArrayList<String> menu = new ArrayList<>();
            menu.add("Nói\nchuyện");
            menu.add("Mở \nThành Viên Free");
            menu.add("Hỗ Trợ \nNhiệm Vụ");
            menu.add("Đổi\nLượng Vàng");
            String[] menus = menu.toArray(String[]::new);
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào ?\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|6|Hãy Mở Thành Viên Free Tại Đây Nhé\n"
                        + "Yêu Cầu: Làm Đến Nhiệm Vụ fide Nhé\n"
                        + "Đặc Biệt: Nhận Thêm 100k Ngọc Và 10k Ruby\n"
                        + "\b|7|Cash " + Util.FormatNumber(player.getSession().vnd)
                        + "\n•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•",
                        menus);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.canReward) {
                RewardService.gI().rewardLancon(player);
                return;
            }
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU -> {
                    switch (select) {
                        case 0 -> {
                            ArrayList<String> menu = new ArrayList<>();
                            menu.add("Nhiệm vụ");
                            menu.add("Học\nKỹ năng");
                            Clan clan = player.clan;
                            if (clan != null) {
                                menu.add("Về khu\nvực bang");
                                if (clan.isLeader(player)) {
                                    menu.add("Giải tán\nBang hội");
                                }
                            }
                            menu.add("Kho báu\ndưới biển");
                            String[] menus = menu.toArray(String[]::new);

                            this.createOtherMenu(player, 0,
                                    "|7|Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào ?\n"
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    + "|6|Hãy Mở Thành Viên Free Tại Đây Nhé\n"
                                    + "Yêu Cầu: Làm Đến Nhiệm Vụ fide Nhé\n"
                                    + "Đặc Biệt: Nhận Thêm 100k Ngọc Và 10k Ruby\n\n"
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•",
                                    menus);

                        }

                        case 1 -> {
                            if (player.getSession().actived == true) {
                                Service.getInstance().sendThongBao(player, "|4|Bạn đã mở thành viên rồi mà. Tiếp tục chơi game thui nào!!!!");
                                return;
                            }
                            if (player.playerTask.taskMain.id < 22 && player.getSession().actived == false) {
                                Service.getInstance().sendThongBao(player, "|3|Làm Nv Fide Đi Đã Rồi Free!!");
                                return;
                            }
                            if (player.getSession().vnd >= 0 && player.getSession().actived == false) {
                                player.inventory.gem += 100000;
                                player.inventory.ruby += 10000;
                                Service.getInstance().sendMoney(player);
                                try {
                                    player.getSession().actived = true;
                                    PlayerDAO.subcash(player, 0);
                                    DBConnecter.executeUpdate("update account set active = 1 where id = " + player.getSession().userId);
                                    Service.getInstance().sendThongBao(player, "|2|Bạn đã mở thành viên Free. Đã mở khóa chức năng Giao dịch và Chat thế giới !!");
                                    // Service.getInstance().sendThongBaoAllPlayer("|2|Bạn \n[" + player.name + "]\nđã mở thành viên Free. \nĐã mở khóa chức năng Giao dịch ALL Cải trang Pét !!");
                                } catch (Exception e) {
                                    System.out.println("Loi chuc nang mo thanh vien");
                                }
                            }
                        }

//                         case 1 -> {
//                             if (player.getSession().actived == true) {
//                                    Service.getInstance().sendThongBao(player, "|4|Bạn đã mở thành viên rồi mà. Tiếp tục chơi game thui nào!!!!");
//                                    return;
//                                }
//                                if (player.getSession().vnd < 50000 && player.getSession().actived == false) {
//                                    Service.getInstance().sendThongBaoFromAdmin(player,"|3|Tài khoản của bạn không đủ 50.000coin. Vui lòng liên hệ ADMIN !\n|4|Nhận Thêm 500k HP,KI,SD Gốc Khi Mở Thành Công nè");
//                                    return;
//                                }
//                                if (player.getSession().vnd >= 50000 && player.getSession().actived == false) {
//                                     player.nPoint.dameg += 500000;
//                                      player.nPoint.hpg += 500000;
//                                       player.nPoint.mpg += 500000;
//                                    Service.getInstance().sendMoney(player);
//                                     Service.getInstance().point(player);
//                                    try {
//                                        player.getSession().actived = true;
//                                        PlayerDAO.subcash(player, 50000);
//                                        DBConnecter.executeUpdate("update account set active = 1 where id = " + player.getSession().userId);
//                                        Service.getInstance().sendThongBao(player, "|2|Bạn đã mở thành viên 50k. Đã mở khóa chức năng Giao dịch và Chat thế giới !!");
//                                    Service.getInstance().sendThongBaoAllPlayer("|2|Bạn \n[" + player.name + "]\nđã mở thành viên VIP.\nNhận 500k CSgoc\nĐã mở khóa chức năng Giao dịch ALL Cải trang Pét !!");
//                                } catch (Exception e) {
//                                    System.out.println("Loi chuc nang mo thanh vien");
//                                }
//                            }
//                        }
                        case 2 -> {
                            switch (player.playerTask.taskMain.id) {
                                case 16 -> {
                                    switch (player.playerTask.taskMain.index) {
                                        case 0 -> {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_16_0);
                                            Service.gI().sendThongBaoOK(player, "Nhiệm vụ hiện tại của con là " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                        }

                                        default ->
                                            Service.gI().sendThongBaoOK(player, "Nhiệm vụ (" + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name + ")\nhiện tại không thuộc diện hỗ trợ");
                                    }
                                }
                                case 18 -> {
                                    if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().doneTask(player, ConstTask.TASK_18_1);
                                        Service.gI().sendThongBaoOK(player, "Nhiệm vụ hiện tại của con là " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Nhiệm vụ (" + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name + ")\nhiện tại không thuộc diện hỗ trợ");
                                    }
                                }
                                case 19 -> {
                                    switch (player.playerTask.taskMain.index) {
                                        case 1 -> {
                                            TaskService.gI().addDoneSubTask(player, player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount);
                                            Service.gI().sendThongBaoOK(player, "Nhiệm vụ hiện tại của con là " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                        }

                                        default ->
                                            Service.gI().sendThongBaoOK(player, "Nhiệm vụ (" + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name + ")\nhiện tại không thuộc diện hỗ trợ");
                                    }
                                }
                                default ->
                                    Service.gI().sendThongBaoOK(player, "Nhiệm vụ (" + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name + ")\nhiện tại không thuộc diện hỗ trợ");
                            }
                        }

//                          
                        case 3 -> {
                            Input.gI().createFormQDco4la(player);
                            break;
                        }
//                case 5 -> {
//                       if (player.getSession().tongnap < 50000 && player.getSession().actived == false) {
//                                    Service.getInstance().sendThongBao(player, "|4|Bạn Chưa Mở Thành Viên 50k Hoặc Đã Nạp Đc 50k!!!!");
//                                    return;
//                                }
//                    
//                        if (player.inventory.ruby >= 500000000) {
//                            this.npcChat(player, "Tham Lam");
//                            break;
//                        }
//                        player.inventory.ruby = 500000000;
//                        Service.getInstance().sendMoney(player);
//                        Service.getInstance().sendThongBao(player, "|1|Bạn vừa nhận được 500m Hồng Ngọc");
//                        break;
//
//                        
//                }
                    }
                }
                case 0 -> {
                    switch (select) {
                        case 0 ->
                            NpcService.gI().createTutorial(player, tempId, avartar, player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                        case 1 ->
                            Service.gI().sendThongBao(player, "Bạn đã học hết các kỹ năng");
                        case 2 -> {
                            Clan clan = player.clan;
                            if (clan != null && select == 2) {
                                ChangeMapService.gI().changeMapNonSpaceship(player, 153, Util.nextInt(100, 200), 432);
                            } else {
                                if (player.clan != null && player.clan.BanDoKhoBau != null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội con đang ở hang kho báu cấp "
                                            + player.clan.BanDoKhoBau.level + "\ncon có muốn đi cùng họ không?",
                                            "Top\nBang hội", "Thành tích\nBang", "Đồng ý", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                            "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\nỞ đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                            "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                                }
                            }
                        }
                        case 3 -> {
                            boolean clanCheck = true;
                            Clan clan = player.clan;
                            if (clan != null) {
                                clanCheck = false;
                                if (clan.isLeader(player)) {
                                    createOtherMenu(player, 3, "Con có chắc muốn giải tán bang hội không?", "Đồng ý", "Từ chối");
                                } else {
                                    clanCheck = true;
                                }
                            }
                            if (clanCheck) {
                                if (player.clan != null && player.clan.BanDoKhoBau != null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội con đang ở hang kho báu cấp "
                                            + player.clan.BanDoKhoBau.level + "\ncon có muốn đi cùng họ không?",
                                            "Top\nBang hội", "Thành tích\nBang", "Đồng ý", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                            "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\nỞ đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                            "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                                }
                            }
                        }
                        case 4 -> {
                            if (player.clan != null && player.clan.BanDoKhoBau != null) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                        "Bang hội con đang ở hang kho báu cấp "
                                        + player.clan.BanDoKhoBau.level + "\ncon có muốn đi cùng họ không?",
                                        "Top\nBang hội", "Thành tích\nBang", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                        "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\nỞ đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                        "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                            }
                        }
                    }
                }
                case 3 -> {
                    Clan clan = player.clan;
                    if (clan != null) {
                        if (clan.isLeader(player)) {
                            if (select == 0) {
                                Input.gI().createFormGiaiTanBangHoi(player);
                            }
                        }
                    }
                }
                case ConstNpc.MENU_OPENED_DBKB -> {
                    switch (select) {
                        case 2 -> {
                            if (player.clan == null) {
                                Service.gI().sendThongBao(player, "Hãy vào bang hội trước");
                                return;
                            }
                            if (player.isAdmin() || player.nPoint.power >= TreasureUnderSea.POWER_CAN_GO_TO_DBKB) {
                                ChangeMapService.gI().goToDBKB(player);
                            } else {
                                this.npcChat(player, "Yêu cầu sức mạnh lớn hơn "
                                        + Util.numberToMoney(TreasureUnderSea.POWER_CAN_GO_TO_DBKB));
                            }
                        }

                    }
                }
                case ConstNpc.MENU_OPEN_DBKB -> {
                    switch (select) {
                        case 2 -> {
                            if (player.clan == null) {
                                Service.gI().sendThongBao(player, "Hãy vào bang hội trước");
                                return;
                            }
                            if (player.isAdmin() || player.nPoint.power >= TreasureUnderSea.POWER_CAN_GO_TO_DBKB) {
                                Input.gI().createFormChooseLevelBDKB(player);
                            } else {
                                this.npcChat(player, "Yêu cầu sức mạnh lớn hơn "
                                        + Util.numberToMoney(TreasureUnderSea.POWER_CAN_GO_TO_DBKB));
                            }
                        }

                    }
                }
                case ConstNpc.MENU_ACCEPT_GO_TO_BDKB -> {
                    switch (select) {
                        case 0 ->
                            TreasureUnderSeaService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                    }
                }
            }
        }
    }
}
