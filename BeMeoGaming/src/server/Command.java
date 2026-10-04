package server;


import Mail.HomThuService;
import boss.BossManager;
import boss.BrolyManager;
import consts.ConstNpc;
import dragon.SystemMetrics;
import item.Item;
import item.Item.ItemOption;
import java.io.IOException;

import java.util.List;
import map.Zone;

import network.Message;
import network.SessionManager;
import player.Pet;
import player.Player;//Do Mèo check ch kỹ ấy chứ
import services.InventoryService;
import services.ItemService;
import services.MapService;
import services.NpcService;
import services.PlayerService;
import services.Service;
import services.SkillService;
import services.TaskService;
import services.func.ChangeMapService;
import services.func.Input;
import skill.Skill;
import utils.SkillUtil;
import utils.TimeUtil;
import utils.Util;

public class Command {

    private static Command instance;

    public static Command gI() {
        if (instance == null) {
            instance = new Command();
        }
        return instance;
    }

    public void chat(Player player, String text) {
        if (!vipchat(player, text)) {
            Service.gI().chatMap(player, text);
        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list online khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("「✦Men Online✦」 : " + Client.gI().getPlayers().size() + " (" + TimeUtil.getTimeNow("HH:mm:ss") + ")");
            // client doc so dong dang BYTE -> writeInt se hien 0 dong
            int topCount = Math.min(255, Client.gI().getPlayers().size());
            msg.writer().writeByte(topCount);
            for (int i = 0; i < topCount; i++) {
                Player pl = Client.gI().getPlayers().get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "Cán Bộ" : pl.isAdmin() ? "" : "Cư Dân");
                msg.writer().writeUTF(//"ThỏiVàng: "+Util.numberToMoney(InventoryServiceNew.gI().findItemBag (pl, (short)457).quantity ) 

                        "\nSức Mạnh: " + Util.numberToMoney(pl.nPoint.power)
                        + "\nTiềm Năng: " + Util.numberToMoney(pl.nPoint.tiemNang)
                        + "\nVàng: " + Util.numberToMoney(pl.inventory.gold)
                        + "\nNgọc: " + Util.numberToMoney(pl.inventory.gem)
                        + "\nRuby: " + Util.numberToMoney(pl.inventory.ruby)
                        + "\nHP: " + Util.numberToMoney(pl.nPoint.hpMax)
                        + "\nKI: " + Util.numberToMoney(pl.nPoint.mpMax)
                        + "\nSức Đánh: " + Util.numberToMoney(pl.nPoint.dame)
                        + "\ngiáp: " + Util.numberToMoney(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + "\n[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: " + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg2;
        try {
            msg2 = new Message(-25);
            msg2.writer().writeUTF(thongBao);
            pl.sendMessage(msg2);
            msg2.cleanup();
        } catch (Exception e) {
        }
        return;
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void addSMTN(Player player, byte type, double param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, (long) param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    public boolean vipchat(Player player, String text) {
        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("logskill")) {
                Service.getInstance().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return true;
            } else if (text.startsWith("ht ")) {
                if (text.replace("ht ", "").getBytes().length > 0) {
                    byte gender = Byte.parseByte(text.replace("ht ", ""));
                    if (gender > 2) {
                        return false;
                    }
                    int[][] listSkill = {{0, 1, 6, 9, 10, 20, 22, 19, 24, 27, 28, 29}, {2, 3, 7, 11, 12, 17, 18, 19, 26, 27, 28, 29}, {4, 5, 8, 13, 14, 21, 23, 19, 25, 27, 28, 29}};
                    for (int i = 0; i < player.playerSkill.skills.size(); i++) {
                        player.playerSkill.skills.set(i, SkillUtil.createSkill(listSkill[gender][i], 1));
                    }
                    player.gender = gender;
                    Service.gI().player(player);
                    for (int i = 0; i < player.playerSkill.skills.size(); i++) {
                        player.playerSkill.skillShortCut[i] = player.playerSkill.skills.get(i).template.id;
                    }
                    player.zone.load_Another_To_Me(player);
                    player.zone.load_Me_To_Another(player);
                    player.playerSkill.sendSkillShortCut();
                    sendThongBao(player, "|7|Change Gender\n[" + Service.gI().get_HanhTinh(gender) + "]\nSuccessful!");
                    return true;
                }
            } else if (text.startsWith("i ")) {
                int itemId = Integer.parseInt(text.replace("i ", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));

                InventoryService.gI().addItemBag(player, item, 999999);
                InventoryService.gI().sendItemBag(player);
                Service.getInstance().sendThongBao(player, "GET " + item.template.name + " [" + item.template.id + "] SUCCESS !");
                return true;
            } else if (text.startsWith("mail ")) {
                try {
                    String[] item = text.replace("mail ", "").split(" ");
                    if (Short.parseShort(item[0]) <= 10000) {
                        Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                        if (it != null && item.length == 1) {
                            InventoryService.gI().addItemMail(player, it);
                            HomThuService.gI().sendListMail(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemMail(player, it);
                            HomThuService.gI().sendListMail(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                            String name = String.valueOf(item[1]);
                            InventoryService.gI().addItemMail(Client.gI().getPlayer(name), it);
                            HomThuService.gI().sendListMail(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 3 && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                            String name = String.valueOf(item[2]);
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemMail(Client.gI().getPlayer(name), it);
                            HomThuService.gI().sendListMail(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                }
                return true;
            }
//            if (text.equals("videl")) {
//                PetService.gI().changeVidelPet(player, player.gender);
//                return;
//            }
//            if (text.equals("update")) {
//                player.pet.setLever(player.pet.getLever() + 1);
//                player.zone.loadAnotherToMe(player);
//                player.zone.load_Me_To_Another(player);
//                return;
//            }
            if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return true;
            } else if (text.equals("check")) {
                BrolyManager.gI().showListBoss(player);
                return true;
           
            
          
            }
            if (text.equals("r")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                return true;
            }
//            if (text.equals("dh")) {
//                duahau.createduahau(player);
//            }
//            if (text.equals("bill")) {
//                BillEgg.createBillEgg(player);
//            }

            if (text.equals("tele")) {
                this.sendThongBao(player, "Thực thi lệnh thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        if (pl.zone != null) {
                            ChangeMapService.gI().changeMap(pl, player.zone, player.location.x, player.location.y);
                        }
                        Service.getInstance().sendThongBao(pl, "|2|Bạn đã được ADMIN gọi đến đây");
                    }
                }
                return true;
            }
            if (text.equals("pk")) {
                this.sendThongBao(player, "Xiên toàn server thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        pl.isDie();
                        pl.setDie();
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        Service.getInstance().sendThongBaoFromAdmin(pl, "|2|\nADMIN ĐÃ TÀN SÁT CẢ SERVER");
                    }
                }
                return true;
            }

            if (text.equals("ad")) {
                showListPlayer(player);
                return true;
            }
            if (text.length() > 100) {
                text = text.substring(0, 100);
            }
            Service.getInstance().chatMap(player, text);

            if (text.equals("client")) {
                Client.gI().show(player);
                return true;
            }
            if (text.equals("buff")) {
                Input.gI().createFormSenditem(player);
                return true;
            }
            if (text.equals("chiso")) {
                Input.gI().createFormSenditem1(player);
                return true;
            }

            if (text.equals("shop")) {
                Manager.reloadShop();
                this.sendThongBao(player, "Load Shop Success");
                return true;
            }
            if (text.equals("kick")) {
                Client.gI().kickAll();
                return true;
            }

            if (text.equals("top")) {
                Manager.reloadtop();
                this.sendThongBao(player, "Load top Success");
                return true;
            }

            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return true;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return true;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return true;
            }
            if (text.equals("r")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                return true;

            } else if (text.equals("daolu")) {
                if (player.TamkjllPetGiong != -1) {
                    NpcService.gI().createMenuConMeo(player, ConstNpc.Saga_EmBe, -1,
                            "|7|Đạo Lữ SSS\n"
                            + "|7|Name Đạo Lữ: " + player.TamkjllNamePet + " \n"
                            + "|7|LV Đạo Lữ: (" + (player.EmBeEXP * 100 / (3000000L + player.EmBeLv * 1500000L))
                            + "%)-LV: "
                            + player.EmBeLv + "\n"
                            + "|4|Linh Hồn: " + player.LinhCanEmBe(player.TamkjllPetGiong) + "\n"
                            + "Thức ăn: " + player.TamkjllPetHunger + "%\n"
                            + "Sức mạnh: " + Util.getFormatNumber(player.TamkjllPetPower) + "\n"
                            + "cần 15 phút để load hoặc thoát game ra vào lại\n"
                            + "Hãy Cho Đạo Lữ Ăn Liên Tục Không Sẽ Chết Đói Đó\n"
                            + "Chết Đạo Lữ Sẽ Bỏ Nhà Gia Đi\n"
                            + "|6|Chủ Nhân Hãy Ra Lệnh Cho Ta\n",
                            "Đi Theo", "PK Người", "Pk quái",
                            "Về Nhà", "Kĩ Năng\nĐạo Lữ");
                } else {
                    Service.gI().sendThongBaoOK(player, "Bạn Chưa Có Đạo Lữ Để Dùng Tính Năng Này");
                }
                return true;
            } else if (text.equals("admin")) {
                Runtime runtime = Runtime.getRuntime();
                long maxMemory = runtime.maxMemory() / (1024 * 1024);

                long totalMemory = runtime.totalMemory() / (1024 * 1024);
                long usedMemory = totalMemory - runtime.freeMemory() / (1024 * 1024);
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1,
                        "|0|Time start: " + ServerManager.timeStart + "\nClients: " + Client.gI().getPlayers().size()
                        + " người chơi\n Sessions: " + SessionManager.gI().getNumSession() + "\nThreads: "
                        + Thread.activeCount() + " luồng" + "\n" + SystemMetrics.ToString()
                        + "\nMemory JVM: " + usedMemory + "/" + maxMemory + " MB",
                        "Ngọc rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "Boss", "Call Broly", "Đóng");
                return true;

            } else if (text.equals("vt")) {
                sendThongBaoFromAdmin(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));

            } else if (text.startsWith("hp")) {
                player.nPoint.hpg = Double.valueOf(text.replace("hp", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;
                //buff ki
            } else if (text.startsWith("double")) {
                Item thoivang = ItemService.gI().createNewItem((short) 0, 1);
                thoivang.itemOptions.add(new ItemOption(101, 999999999999999999L));
                InventoryService.gI().addItemBag(player, thoivang, 999999);
                InventoryService.gI().sendItemBag(player);

                return true;
                //buff ki
            } else if (text.startsWith("ki")) {
                player.nPoint.mpg = Double.valueOf(text.replace("ki", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;
                //buff sd
            } else if (text.startsWith("sd")) {
                player.nPoint.dameg = Double.valueOf(text.replace("sd", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;
                //buff giap
            } else if (text.startsWith("giap")) {
                player.nPoint.defg = Double.valueOf(text.replace("giap", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;
                //buff chí mạng
            } else if (text.startsWith("crit")) {
                player.nPoint.critg = Integer.valueOf(text.replace("crit", ""));
                Service.getInstance().point(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;
                //buff nhiệm vụ
            } else if (text.startsWith("nv_")) {
                player.playerTask.taskMain.id = Integer.valueOf(text.replace("nv_", ""));
                player.playerTask.taskMain.index = 0;
                TaskService.gI().sendTaskMain(player);
                TaskService.gI().sendNextTaskMain(player);
                Service.getInstance().sendThongBaoOK(player, "Thành công");
                return true;

                //**************************************************************
            } else if (text.contains("tsm ")) {
                long power = Long.parseLong(text.replaceAll("tsm ", ""));
                player.nPoint.power +=  power;
                addSMTN(player, (byte) 2, power, false);
                //player.sendAddchatYellow("Bạn vừa tăng thành công " + power + " sức mạnh");
                sendThongBao(player, "Bạn vừa tăng thành công " + power + " sức mạnh");
            } else if (text.contains("gsm ")) {
                double power = Double.parseDouble(text.replaceAll("gsm ", ""));
                player.nPoint.power -= (long) power;
                addSMTN(player, (byte) 2, -power, false);
                //player.sendAddchatYellow("Bạn vừa giảm thành công " + power + " sức mạnh");
                sendThongBao(player, "Bạn vừa giảm thành công " + power + " sức mạnh");
            } else if (text.contains("ttn ")) {
                double power = Double.parseDouble(text.replaceAll("ttn ", ""));
                player.nPoint.tiemNang +=  power;
                addSMTN(player, (byte) 2, power, false);
                //player.sendAddchatYellow("Bạn vừa tăng thành công " + power + " tiềm năng");
                sendThongBao(player, "Bạn vừa tăng thành công " + power + " tiềm năng");
            } else if (text.contains("gtn ")) {
                long power = Long.parseLong(text.replaceAll("gtn ", ""));
                player.nPoint.tiemNang -= (long) power;
                addSMTN(player, (byte) 2, -power, false);
                //player.sendAddchatYellow("Bạn vừa giảm thành công " + power + " tiềm năng");
                sendThongBao(player, "Bạn vừa giảm thành công " + power + " tiềm năng");
            } else if (text.equals("buff")) {
                //  Input.gI().createFormSenditem(player);
            } else if (text.equals("chiso")) {
                //   Input.gI().createFormSenditem1(player);
            } else if (text.startsWith("i")) {
                try {
                    String[] item = text.replace("i", "").split(" ");
                    if (Short.parseShort(item[0]) <= 10000) {
                        Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                        if (it != null && item.length == 1) {
                            InventoryService.gI().addItemBag(player, it, 999999);
                            InventoryService.gI().sendItemBag(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemBag(player, it, 999999999);
                            InventoryService.gI().sendItemBag(player);
                            Service.getInstance().sendThongBao(player, "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                            String name = String.valueOf(item[1]);
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name), it, 999999999);
                            InventoryService.gI().sendItemBag(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 3 && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                            String name = String.valueOf(item[2]);
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name), it, 999999999);
                            InventoryService.gI().sendItemBag(Client.gI().getPlayer(name));
                            Service.getInstance().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " " + it.template.name + " đến player " + name);
                            Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.getInstance().sendThongBao(player, "Không tìm thấy player");
                }
                return true;

            } else if (text.startsWith("hp")) {
                player.nPoint.hpg = Double.valueOf(text.replaceAll("hp", ""));
            } else if (text.startsWith("up")) {
                try {
                    long power = Long.valueOf(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("m")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m", ""));
                    Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, mapId, 0);
                    if (zone != null) {
                        player.location.x = 500;
                        player.location.y = zone.map.yPhysicInTop(500, 100);
                        MapService.gI().goToMap(player, zone);
                        Service.getInstance().clearMap(player);
                        zone.mapInfo(player);
                        player.zone.loadAnotherToMe(player);
                        player.zone.load_Me_To_Another(player);
                    }
                    return true;
                } catch (NumberFormatException e) {

                    System.out.println("lỗi");
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else if (text.equals("tutien")) {
                if (player.nPoint.power < 500000000000l) {
                    Service.gI().sendThongBaoOK(player,
                            "Hãy cày thêm: " + Util.FormatNumber(500000000000l - player.nPoint.power) + " Sức Mạnh, mới vào đc map tu tiên");
                    return true;
                }
                ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 336);
            }
        } else {
            if (text.equals("tutien")) {
                if (player.nPoint.power < 500000000000l) {
                    Service.gI().sendThongBaoOK(player,
                            "Hãy cày thêm: " + Util.FormatNumber(500000000000l - player.nPoint.power) + " Sức Mạnh, mới vào đc map tu tiên");
                    return true;
                }
              ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 336);
            }
        }
        return false;
        
        
        
        
    }

}

//        if (text.startsWith("ten con la ")) {
//            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
//            if (player.pet != null) {
//                if (text.equals("di theo") || text.equals("follow")) {//nhìn nè tại vì nó nmawf trong đây:))
//                    player.pet.changeStatus(Pet.FOLLOW);
//                } else if (text.equals("bao ve") || text.equals("protect")) {
//                    player.pet.changeStatus(Pet.PROTECT);
//                } else if (text.equals("tan cong") || text.equals("attack")) {
//                    player.pet.changeStatus(Pet.ATTACK);
//                } else if (text.equals("ve nha") || text.equals("go home")) {
//                    player.pet.changeStatus(Pet.GOHOME);
//                } else if (text.equals("bien hinh")) {
//                    player.pet.transform();
//                }
//            }
//        }
//      if (text.length() > 10 {
//            text = text.substring(0, 100);
//        return null;
//        }
//        chatMap(player, text);
//    }
//  public boolean check(Player player, String text) {
//      if (player.isAdmin()) {
//            if (text.equals("giftcode")) {
//                GiftCodeManager.gI().checkInfomationGiftCode(player);
//                return true;
//            } else if (text.equals("a")) {
//                BossManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapbroly")) {
//                BrolyManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapboss2")) {
//                OtherBossManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapdt")) {
//                RedRibbonHQManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapbdkb")) {
//                TreasureUnderSeaManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapcdrd")) {
//                SnakeWayManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("mapkghd")) {
//                GasDestroyManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("maptrungthu")) {
//                TrungThuEventManager.gI().showListBoss(player);
//                return true;
//            } else if (text.equals("hsk")) {
//                Service.gI().releaseCooldownSkill(player);
//                return true;
//            } else if (text.startsWith("up")) {
//                try {
//                    double power = Double.parseDouble(text.replaceAll("up", ""));
//                    Service.gI().addSMTN(player, (byte) 2, power, false);
//                    return true;
//                } catch (Exception e) {
//                }
//            } else if (text.startsWith("upp")) {
//                try {
//                    if(player.pet != null){
//                        double power = Double.parseDouble(text.replaceAll("up", ""));
//                        Service.gI().addSMTN(player, (byte) 2, power, false);
//                        return true;
//                    }
//                } catch (Exception e) {
//                }
//            } else if (text.equals("battu")) {
//                if (player.isBattu) {
//                    player.isBattu = false;
//                } else {
//                    player.isBattu = true;
//                }
//                Service.gI().sendThongBao(player, "Bất tử" + (player.isBattu ? ": ON" : ": OFF"));
//                return true;
//            } else if (text.startsWith("upp")) {
//                try {
//                    double power = Double.parseDouble(text.replaceAll("upp", ""));
//                    Service.gI().addSMTN(player.pet, (byte) 2, power, false);
//                    return true;
//                } catch (Exception e) {
//                }
//            } else if (text.equals("test")) {
//                switch (player.gender) {
//                    case 0 ->
//                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME, 1);
//                    case 2 ->
//                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG, 1);
//                    default ->
//                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA, 1);
//                }
//                return true;
//            } else if (text.equals("test2")) {
//                switch (player.gender) {
//                    case 0 -> {
//                        SkillService.gI().learSkillSpecial(player, Skill.PHAN_THAN, 6);
//                    }
//                    case 2 -> {
//                        SkillService.gI().learSkillSpecial(player, Skill.PHAN_THAN, 6);
//                    }
//                    default -> {
//                        SkillService.gI().learSkillSpecial(player, Skill.PHAN_THAN, 6);
//                    }
//                }
//                return true;
//            } else if (text.equals("dragon")) {
//                ShenronEvent shenron = new ShenronEvent();
//                shenron.setPlayer(player);
//                ShenronEventManager.gI().add(shenron);
//                player.shenronEvent = shenron;
//                shenron.setZone(player.zone);
//                shenron.activeShenron(true, ShenronEvent.DRAGON_EVENT);
//                shenron.sendWhishesShenron();
//                return true;
//            } else if (text.equals("ad")) {
//                Runtime runtime = Runtime.getRuntime();
//                long maxMemory = runtime.maxMemory() / (1024 * 1024);
//
//                long totalMemory = runtime.totalMemory()/ (1024 * 1024);
//                long usedMemory = totalMemory - runtime.freeMemory()/ (1024 * 1024);
//                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1,
//                        "|0|Time start: " + ServerManager.timeStart + "\nClients: " + Client.gI().getPlayers().size()
//                                + " người chơi\n Sessions: " + SessionManager.gI().getNumSession() + "\nThreads: "
//                                + Thread.activeCount() + " luồng" + "\n" + SystemMetrics.ToString()
//                                + "\nMemory JVM: " + usedMemory + "/" + maxMemory + " MB" ,
//                        "Ngọc rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "Boss", "Call Broly", "Đóng");
//                return true;
//            } else if (text.equals("daucatmoi")) {
//                for (int i = 0; i < 10; i++) {
//                    ServerNotify.gI().notify("BOSS Nro vừa xuất hiện tại nhà anh ấy");
//                }
//                return true;
//            } else if (text.startsWith("m")) {
//                int mapId = Integer.parseInt(text.replace("m ", ""));
//                ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
//                return true;
//            }
//            if (text.startsWith("sd_")) {
//                try {
//                 player.nPoint.dameg = Double.valueOf(text.replace("sd_", ""));
//                    Service.gI().point(player);
//                      Service.gI().sendThongBaoOK(player, "Thành công");
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("hp_")) {
//                try {
//                     player.nPoint.hpg = Double.valueOf(text.replace("hp_", ""));
//                    Service.gI().point(player);
//                     Service.gI().sendThongBaoOK(player, "Thành công");
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("ki_")) {
//                try {
//                    player.nPoint.mpg = Double.valueOf(text.replace("ki_", ""));
//                    Service.gI().point(player);
//                     Service.gI().sendThongBaoOK(player, "Thành công");
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("def_")) {
//                try {
//                   player.nPoint.defg = Double.valueOf(text.replace("def_", ""));
//                    Service.gI().point(player);
//                     Service.gI().sendThongBaoOK(player, "Thành công");
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("crit_")) {
//                try {
//                  player.nPoint.critg = Integer.valueOf(text.replace("crit_", ""));
//                    Service.gI().point(player);
//                     Service.gI().sendThongBaoOK(player, "Thành công");
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("notiboss")) {
//                player.notiKillBoss = !player.notiKillBoss;
//                Service.gI().sendThongBao(player, "Thông báo Kill Boss: " + (player.notiKillBoss ? "ON" : "OFF"));
//                return true;
//            }
//            if (text.startsWith("ntask")) {
//                try {
//                    int idTask = Integer.parseInt(text.replaceAll("ntask", ""));
//                    player.playerTask.taskMain.id = idTask - 1;
//                    player.playerTask.taskMain.index = 0;
//                    TaskService.gI().sendNextTaskMain(player);
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.startsWith("badges_")) {
//                int idBadges = Integer.parseInt(text.replaceAll("badges_", ""));
//                player.badges.idBadges = idBadges;
//            }
//            if (text.startsWith("kq")) {
//                Service.gI().sendThongBao(player, "Kết quả Lucky Round tiếp theo là: " + LuckyNumber.RESULT);
//                return true;
//            }
//            if (text.startsWith("danhhieu_")) {
//                int idGender = Integer.parseInt(text.replaceAll("danhhieu_", ""));
//                BadgesData data = new BadgesData(player, idGender, 5);
//                return true;
//            }
//            if (text.startsWith("gender_")) {
//                byte idGender = Byte.parseByte(text.replaceAll("gender_", ""));
//                player.gender = idGender;
//                return true;
//            }
////            if (text.startsWith("i")) {
////                String[] parts = text.split("");
////                if (parts.length >= 3) {
////                    short id = Short.parseShort(parts[1]);
////                    int quantity = Integer.parseInt(parts[2]);
////                    Item item = ItemService.gI().createNewItem(id, quantity);
////                    List<Item.ItemOption> ops = ItemService.gI().getListOptionItemShop((short) id);
////                    if (!ops.isEmpty()) {
////                        item.itemOptions = ops;
////                    }
////                    InventoryService.gI().addItemBag(player, item,9999);
////                    InventoryService.gI().sendItemBag(player);
////                    Service.gI().sendThongBao(player,
////                            "GET " + item.template.name + " [" + item.template.id + "] SUCCESS !");
////                    return true;
////                } else {
////                    Service.gI().sendThongBao(player, "Lỗi");
////                    return true;
////                }
////            } // else if (text.startsWith("i ")) {
//              // int itemId = Integer.parseInt(text.replace("i ", ""));
//              // Item item = ItemService.gI().createNewItem(((short) itemId));
//              // List<Item.ItemOption> ops = ItemService.gI().getListOptionItemShop((short)
//              // itemId);
//              // if (!ops.isEmpty()) {
//              // item.itemOptions = ops;
//              // }
//              // InventoryService.gI().addItemBag(player, item);
//              // InventoryService.gI().sendItemBag(player);
//              // Service.gI().sendThongBao(player, "GET " + item.template.name + " [" +
//              // item.template.id + "] SUCCESS !");
//              // return true;
//              // }
//            else if (text.equals("item")) {
//                Input.gI().createFormGiveItem(player);
//                return true;
//            } else if (text.equals("getitem")) {
//                Input.gI().createFormGetItem(player);
//                return true;
//            } else if (text.equals("d")) {
//                Service.gI().setPos(player, player.location.x, player.location.y + 10);
//                return true;
//                
//               } else if (text.startsWith("i ")) {
//                int itemId = Integer.parseInt(text.replace("i ", ""));
//                Item item = ItemService.gI().createNewItem(((short) itemId));
//
//                InventoryService.gI().addItemBag(player, item,99999999);
//                InventoryService.gI().sendItemBag(player);
//              Service.getInstance().sendThongBao(player, "GET " + item.template.name + " [" + item.template.id + "] SUCCESS !");
//               return true;
//           
//                 
//                
//            }
//        }
//        if (text.startsWith("ten con la ")) {
//            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
//        }
//
//        if (player.pet != null) {
//            switch (text) {
//                case "di theo", "follow" ->
//                    player.pet.changeStatus(Pet.FOLLOW);
//                case "bao ve", "protect" ->
//                    player.pet.changeStatus(Pet.PROTECT);
//                case "tan cong", "attack" ->
//                    player.pet.changeStatus(Pet.ATTACK);
//                case "ve nha", "go home" ->
//                    player.pet.changeStatus(Pet.GOHOME);
//                case "bien hinh" ->
//                    player.pet.transform();
//            }
//        }
//        return false;
//    }
//}
