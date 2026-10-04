package services.func;

import PhuBanService.CodeCauCa;
import boss.BossManager;
import consts.ConstItem;
import models.Combine.CombineService;
import models.Card.Card;
import models.Card.RadarService;
import models.Card.RadarCard;
import consts.ConstMap;
import item.Item;
import consts.ConstNpc;
import consts.ConstPlayer;
import item.Item.ItemOption;
import java.io.IOException;
import java.lang.System.Logger.Level;
import map.Zone;
import player.Inventory;
import services.*;
import player.Player;
import skill.Skill;
import network.Message;
import utils.SkillUtil;
import utils.TimeUtil;
import utils.Util;
import server.io.MySession;
import utils.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jdbc.daos.PlayerDAO;
import server.Client;
import server.Manager;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;

    private int randClothes(int level) {
        return ConstItem.LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;
        if (player == null) {
            return;
        }
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryService.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryService.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryService.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:

                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryService.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
            }
            if (player.setClothes != null) {
                player.setClothes.setup();
            }
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().sendFlagBag(player);
            Service.gI().point(player);
            Service.gI().sendSpeedPlayer(player, -1);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public Item finditem(Player player, int iditem) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == iditem) {
                return item;
            }
        }
        return null;
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg = null;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index < 0) {
                                return;
                            }
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học "
                                            + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else if (item.template.id == 570) {
                                    if (!Util.isAfterMidnight(player.lastTimeRewardWoodChest)) {
                                        Service.gI().sendThongBao(player, "Hãy chờ đến ngày mai");
                                        return;
                                    }
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc muốn mở\n"
                                            + player.inventory.itemsBag.get(index).template.name + " ?");
                                    player.sendMessage(msg);

                                } else if (item.template.type == 22) {
                                    if (player.zone.items.stream()
                                            .filter(it -> it != null && it.itemTemplate.type == 22).count() > 2) {
                                        Service.gI().sendThongBaoOK(player, "Mỗi map chỉ đặt được 3 Vệ Tinh");
                                        return;
                                    }
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc muốn dùng\n"
                                            + player.inventory.itemsBag.get(index).template.name + " ?");
                                    player.sendMessage(msg);

                                } else if (item.template.id == 401 || item.template.id == 722 || item.template.id == 1214
                                        || item.template.id == 1215 || item.template.id == 1300 || item.template.id == 1301 || item.template.id == 1302 || item.template.id == 1158
                                        || item.template.id == 1159 || item.template.id == 1160 || item.template.id == 1639 || item.template.id == 1640 || item.template.id == 1641 || item.template.id == 1642 || item.template.id == 1643 || item.template.id == 1644 || item.template.id == 1885) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Mày Lưu Ý ( Trước Khi Đổi Đệ Tử Mới Hãy Tháo Trang Bị) Ngu Thì Đừng Trách");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            int iditem = _msg.reader().readShort();
                            Item item = finditem(player, iditem);
                            UseItem.gI().useItem(player, item, index);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (index < 0) {
                            return;
                        }
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }

                        if (item.isNotNullItem() && item.template.id == 570) {
                            Service.gI().sendThongBao(player, "Không thể bỏ vật phẩm này.");
                            return;
                        }
                        if (!item.isNotNullItem()) {
                            return;
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryService.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryService.gI().sendItemBag(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item != null && item.isNotNullItem()) {
            // RUONG TU PANEL: neu item co trong panel_chest thi mo theo ty le roi ket thuc tai day
            try {
                if (panel.chest.ChestManager.gI().open(pl, item, indexBag)) return;
            } catch (Exception e) {
                Logger.logException(UseItem.class, e, "Mo ruong tu panel loi");
            }
            if (item.template.id == 570) {
                int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                if (time == 0) {
                    Service.gI().sendThongBao(pl, "Hãy chờ đến ngày mai");
                } else {
                    openRuongGo(pl, item);
                }
                return;
            }
            if (item.template.strRequire <= pl.nPoint.power) {
                switch (item.template.type) {
                    case 33: // card
                        UseCard(pl, item);
                        break;
                    case 7: // sách học, nâng skill
                        learnSkill(pl, item);
                        break;

                    case 6: // đậu thần
                        this.eatPea(pl);
                        break;
                    case 12: // ngọc rồng các loại
                        controllerCallRongThan(pl, item);
                        break;

                    case 21:
                    case 23: // thú cưỡi mới
                    case 24: // thú cưỡi cũ
                    case 77: // Sách tuyệt kĩ
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        break; // adu:))
                    case 11: // item bag
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendFlagBag(pl);
                        break;//baotri ddck
//                    case 75:
//                        InventoryService.gI().itemBagToBody(pl, indexBag);
//                        Service.gI().sendchienlinh(pl, (short) (item.template.iconID - 1));
//                        break;
                    case 72: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                        break;
                    }
                    case 98: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    }
                    case 99: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    }
                    case 82:
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.getInstance().sendFoot(pl, item.template.id);
                        break;
                    case 83:
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        Service.gI().sendEffPlayer(pl);
                        break;
                    case 84, 85, 86: {
                        InventoryService.gI().itemBagToBody(pl, indexBag);
                        break;
                    }
                    default:
                        switch (item.template.id) {

                            case 1000:
                                if (pl.diemfam >= 1000) {
                                    pl.diemfam -= 1000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(pl,
                                            "Anh Zai Không có điểm Để Mở Lần Này \nHãy Up Điểm Ngũ Hành Sơn Để Mở Cải Trang Vinh Viễn" + "\nCần " + (1000 - pl.diemfam) + " Điểm Nữa");
                                    return;
                                }
                                usecaitrangbase(pl, item);
                                break;
                            case 1001:
                                if (pl.point_event >= 2000) {
                                    pl.point_event -= 2000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(pl,
                                            "Anh Zai Không có điểm Để Mở Lần Này \nHãy Up Điểm Map Phòng Luyện Tập Thời Gian\n Để Mở Lấy 99 Nro 7s" + "\nCần " + (2000 - pl.point_event) + " Điểm Nữa");
                                    return;
                                }
                                usengocrong7sao(pl, item);
                                break;
                            case 1002:
                                hopQuaTanThu(pl, item);
                                break;
                            case 1025:
                                hopQuajren(pl, item);
                                break;

                            case 1120:
                                hopQuaTanThu1(pl, item);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 457:
                                Input.gI().createFormBanSLL(pl);
                                break;
                            case 1111:
                                Input.gI().createFormBanSLLthiendao(pl);
                                break;
                            case 1112:
                                Input.gI().createFormBanSLLdiadao(pl);
                                break;

                            case 1713, 1714, 1715, 1716:
                                // Can_cau_ca(pl, item);
                                CodeCauCa.cauCa(pl, item);
                                break;
                            case 1260:
                                useGokuDay(pl, item);
                                break;
                            case 1261:
                                useGokuDayVip(pl, item);
                                break;
                            case 1453:
                                hpqua1353(pl, item);
                                break;

                            case 992: // Nhan thoi khong
                                pl.type = 2;
                                pl.maxTime = 5;
                                Service.gI().Transport(pl);
                                break;
                            case 361:
                                pl.idGo = (short) Util.nextInt(0, 6);
                                NgocRongNamecService.gI().menuCheckTeleNamekBall(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 942:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 966, 967, 968);
                                Service.gI().point(pl);
                                break;
                            case 943:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 969, 970, 971);
                                Service.gI().point(pl);
                                break;
                            case 944:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 972, 973, 974);
                                Service.gI().point(pl);
                                break;
//                            case 1019:
//                                InventoryService.gI().itemBagToBody(pl, indexBag);
//                                PetService.Pet2(pl, 1536, 1537, 1538);
//                                Service.gI().point(pl);
//                                break;      
//                                
                            case 1735: //rương skh vip
                                RuongSkhNguyenThuy(pl, item);
                                break;

                            case 1270:
                                Input.gI().createFormBanSLLKnb(pl);
                                break;
                            case 1271:
                                Input.gI().createFormBanSLLKnbac(pl);
                                break;

                            case 967:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 1050, 1051, 1052);
                                Service.gI().point(pl);
                                break;
                            case 1107:
                                InventoryService.gI().itemBagToBody(pl, indexBag);
                                PetService.Pet2(pl, 1183, 1184, 1185);
                                Service.gI().point(pl);
                                break;

                            case 211: // nho tím
                            case 212: // nho xanh
                                eatGrapes(pl, item);
                                break;
                            case 342:
                            case 343:
                            case 344:
                            case 345:
                                if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22)
                                        .count() < 3) {
                                    Service.gI().dropSatellite(pl, item, pl.zone, pl.location.x, pl.location.y);
                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBaoOK(pl, "Mỗi map chỉ đặt được 3 Vệ Tinh");
                                }
                                break;
                            case 380: // cskb
                                openCSKB(pl, item);
                                break;
                            case 381: // cuồng nộ
                            case 382: // bổ huyết
                            case 383: // bổ khí
                            case 384: // giáp xên
                            case 385: // ẩn danh
                            case 379: // máy dò capsule
                            case 638: // commeson
                            case 2075: // rocket
                            case 2160: // Nồi cơm điện
                            case 579: // Duoi khic - buff roi vat pham SK Trung Thu
                            case 1045: // Duoi khic (id moi) - buff roi vat pham SK Trung Thu
                            case 1914: // Ruong Trung Thu (id moi theo Hop Trung Thu 1512)

                            case 663: // bánh pudding
                            case 664: // xúc xíc
                            case 665: // kem dâu
                            case 666: // mì ly
                            case 667: // sushi
                            case 1099:
                            case 1100:
                            case 1101:
                            case 1102:
                            case 1103:

                            case 472:
                            case 473:
                            case 465:
                            case 466:
                            case 890:
                            case 891:
                            case 1218:
                            case 1233:
                            case 1234:
                            case 1235:
                            case 1236:
                            case 1226:
                            case 1227:
                            case 1228:
                            case 1231:
                            case 1232:
                            case 1303:
                            case 1304:
                            case 1305:
                            case 1306:
                            case 1326:
                            case 1327:
                            case 1328:
                            case 1329:
                            case 1330:
                            case 1331:

                            case 1916: // the x2 diem farm
                            case 1917: // the x3 diem farm
                            case 1918: // the x5 diem farm
                                useItemTime(pl, item);
                                break;
                            case 880:
                            case 881:
                            case 882:
                                if (pl.itemTime.isEatMeal2) {
                                    Service.gI().sendThongBao(pl, "Chỉ được sử dụng 1 cái");
                                    break;
                                }
                                useItemTime(pl, item);
                                break;
                            case 521: // tdlt
                                useTDLT(pl, item);
                                break;
                            case 454, 921, 1668, 1669, 1670: // bông tai 1-2-s-ss-sss
                                UseItem.gI().usePorata(pl, item);
                                break;

                            case 722: //đổi đệ tử

                                changexencon(pl, item);
                                Service.getInstance().point(pl);
                                break;
                            case 1691: //đổi đệ tử

                                changekidbu(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1300: //đổi đệ tử

                                changekidfide(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1301: //đổi đệ tử

                                changekiduub(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1302: //đổi đệ tử

                                changekidxen(pl, item);

                                Service.getInstance().point(pl);
                                break;

                            case 1158: //đổi đệ tử

                                changeblack(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1135: //đổi đệ tử

                                pl.nPoint.tiemNang += 1000000000000l;
                                pl.nPoint.power += 1000000000000l;
                                Service.getInstance().point(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                //  Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Đớp 1 x3 Tỷ Power Và Tiềm Năng");
                                break;

                            case 1159: //đổi đệ tử

                                changeblackrose(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1214: //đổi đệ tử

                                changezamasu(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1639: //đổi đệ tử

                                changeGohan1(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1640: //đổi đệ tử

                                changeGohan2(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1641: //đổi đệ tử

                                changeGohan3(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1642: //đổi đệ tử

                                changeGohan4(pl, item);

                                Service.getInstance().point(pl);
                                break;
                            case 1643: //đổi đệ tử

                                changeGohan5(pl, item);

                                Service.getInstance().point(pl);
                                break;

                            case 1644: //đổi đệ tử

                                changeGohan6(pl, item);

                                Service.getInstance().point(pl);
                                break;

                            case 1659:
                                Input.gI().TAOPET(pl);
                                break;

                            case 1215: //đổi đệ tử
                                changecumber(pl, item);
                                break;

                            case 1741: //đổi đệ tử
                                hopquadanhhieu(pl, item);
                                break;

                            case 1745: //đổi đệ tử
                                hopquathucuoi(pl, item);
                                break;
                            case 1238: //đổi đệ tử
                                hopqua1238(pl, item);
                                break;

                            case 1885: //đổi đệ tử
                                changebroly(pl, item);
                                break;

                            case 1251:
                                // [Sua loi 27/09/2026] khong cat ve o day - chi mo form.
                                // Ve bi cat trong Input.case tangdiem khi chuyen diem THANH CONG
                                // (truoc do nguoi choi huy form la mat ve oan).
                                Input.gI().phieutangdiem(pl);
                                break;
                            case 1706:
                                pl.sukien += 1;
                                pl.point_vip += 50;
                                Service.getInstance().point(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1707:
                                pl.sukien += 1;
                                pl.point_vip += 20;
                                Service.getInstance().point(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1708:
                                pl.sukien += 1;
                                pl.point_vip += 80;
                                Service.getInstance().point(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;

                            case 1857: //đổi đệ tử
                                quatop1(pl, item);
                                break;
                            case 1858: //đổi đệ tử
                                quatop2(pl, item);
                                break;
                            case 1859: //đổi đệ tử
                                quatop3(pl, item);
                                break;
                            case 1860: //đổi đệ tử
                                quatop4(pl, item);
                                break;
                            case 1861: //đổi đệ tử
                                quatop5(pl, item);
                                break;
                            case 1862: //đổi đệ tử
                                quatop6(pl, item);
                                break;

                            case 193: // gói 10 viên capsule
                                openCapsuleUI(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            case 194: // capsule đặc biệt
                                openCapsuleUI(pl);
                                break;
                            case 401: // đổi đệ tử
                                changePet(pl, item);
                                break;
                            case 402: // sách nâng chiêu 1 đệ tử
                            case 403: // sách nâng chiêu 2 đệ tử
                            case 404: // sách nâng chiêu 3 đệ tử
                            case 759: // sách nâng chiêu 4 đệ tử
                                upSkillPet(pl, item);
                                break;
                            case 726:
                                UseItem.gI().ItemManhGiay(pl, item);
                                break;
                            case 1110:
                                UseItem.gI().openRandomVNDLon(pl, item);
                                break;
                            case 1754:
                                UseItem.gI().bancavip(pl, item);
                                break;
                            case 727:
                            case 728:
                                UseItem.gI().ItemSieuThanThuy(pl, item);
                                break;
                            case 648:
                            //    ItemService.gI().OpenItem648(pl, item);
                                break;
                            case 1865:
                                UseItem.gI().hopquaramdomthoivang(pl, item);
                                break;
                            case 1272:
                                UseItem.gI().hopquaramdomdns(pl, item);
                                break;
                            case 1033:
                                UseItem.gI().hopquanoitai(pl, item);
                                break;
                            case 1034:
                                UseItem.gI().hopquadangusac(pl, item);
                                break;
                            case 1035:
                                UseItem.gI().hopquadanangcap(pl, item);
                                break;
                            case 1036:
                                UseItem.gI().hopquadabaove(pl, item);
                                break;
                            case 1037:
                                UseItem.gI().hopqualinhthu(pl, item);
                                break;
                            case 1038:
                                UseItem.gI().hopquaphukien(pl, item);
                                break;
                            case 1148:
                                Input.gI().ChatAll(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1149:
                                Input.gI().Chatallplayer(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;

                            case 1108:
                                if (pl.playerTask.taskMain.id < 31) {
                                    pl.playerTask.taskMain.id++;
                                    // pl.nPoint.dame += 200000;
                                    Service.getInstance().sendMoney(pl);
                                    Service.getInstance().point(pl);
                                    TaskService.gI().sendNextTaskMain(pl);
                                    Service.gI().sendThongBaoFromAdmin(pl, "✎Mày Đã Next Nhiệm Vụ");
                                } else {
                                    Service.gI().sendThongBaoFromAdmin(pl, "✎Mày Không Thể Next quá Nv ");
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1109:
                                if (pl.playerTask.taskMain.id < 30) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "✎Chưa Đủ Điều Kiện Dùng");
                                    return;
                                }
                                if (pl.playerTask.taskMain.id == 31) {
                                    pl.playerTask.taskMain.id = 10;
//                                    pl.nPoint.hpg += 10000;
//                                    pl.nPoint.mpg += 10000;
                                    Service.getInstance().sendMoney(pl);
                                    Service.getInstance().point(pl);
                                    TaskService.gI().sendNextTaskMain(pl);
                                    Service.gI().sendThongBaoFromAdmin(pl, "✎Mày Đã Reset Nhiệm Vụ\nNhận 10k HP,KI Gốc");
                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                }
                                break;

                            case 1882:
                                UseItem.gI().goidau9(pl, item);
                                break;
                            case 1884:
                                UseItem.gI().setkichhoat(pl, item);
                                break;
                            case 1856:
                                UseItem.gI().setkichhoatPRO(pl, item);
                                break;

                            case 1554:
                                UseItem.gI().setkichhoatthanlinh(pl, item);
                                break;
                            case 1555:
                                UseItem.gI().hopquasaophale100(pl, item);
                                break;
                            case 1556:
                                UseItem.gI().hopqualinhthu50(pl, item);
                                break;
                            case 1557:
                                UseItem.gI().hopqualongden70(pl, item);
                                break;
                            case 1558:
                                UseItem.gI().hopquathiendao(pl, item);
                                break;
                            case 1559:
                                UseItem.gI().hopquadakham1000(pl, item);
                                break;

                            case 1413: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 1) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 1 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 1);
                                UseItem.gI().ruongthanbi1sao(pl, item);
                            }
                            break;

                            case 1412: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 2) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 2 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 2);
                                UseItem.gI().ruongthanbi2sao(pl, item);
                            }
                            break;
                            case 1411: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 3) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 3 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 3);
                                UseItem.gI().ruongthanbi3sao(pl, item);
                            }
                            break;
                            case 1410: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 4) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 4 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 4);
                                UseItem.gI().ruongthanbi4sao(pl, item);
                            }
                            break;
                            case 1409: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 5) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 5 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 5);
                                UseItem.gI().ruongthanbi5sao(pl, item);
                            }
                            break;
                            case 1408: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 6) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 6 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 6);
                                UseItem.gI().ruongthanbi6sao(pl, item);
                            }
                            break;
                            case 1161: {
                                final short chiakhoa = 1280;
                                Item key = InventoryService.gI().findItemBag(pl, chiakhoa);
                                if (key == null || key.quantity < 8) {
                                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn cần 8 Chìa Khóa để mở rương.\nKiếm Chìa Khóa Tại Online Ngày\nHoặc Săn Boss of Up quái");
                                    break;
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, key, 8);
                                UseItem.gI().ruongthanbi8sao(pl, item);
                            }
                            break;

                            case 1205:
                                UseItem.gI().usevithu(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;

                            case 1755, 1756, 1757, 1758:
                                UseItem.gI().ruongpokemon(pl, item);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                break;
                            case 1160:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepmanhtrung, -1,
                                        "Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Ghép Trứng Đệ\n"
                                        + "Yêu Cầu Mỗi Lần Ghép Mất 999 Mảnh\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1160).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1212:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.gheptrungmabu, -1,
                                        "Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Ghép Trứng Đệ mabu\n"
                                        + "Yêu Cầu Mỗi Lần Ghép Mất 999 Mảnh\n"
                                        + "Ramdom ra 4 loại đệ tử hợp thể 500% kid\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1212).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1169:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepsachchiendau, -1,
                                        "|7|Xin Chào Chủ Nhân\n"
                                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                        + "|8|Chức Năng Ghép Sách Chiến Đấu\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 5000 Mảnh\n"
                                        + "|4|Chỉ Số Ramdom Sách Thường 99-9999% sd\n"
                                        + "|6|Chỉ Số Ramdom Sách Cao Cấp 99-20000%\n"
                                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1169).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1189:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.ghepmatgiatoc, -1,
                                        "|7|Xin Chào Chủ Nhân\n"
                                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                        + "|8|Chức Năng Ghép Mắt gia tộc\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 5000 Mảnh\n"
                                        + "|4|Chỉ Số Ramdom Mắt Thường 99-9999% sd\n"
                                        + "|6|Chỉ Số Ramdom Mắt Cao Cấp 99-20000%\n"
                                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1189).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;

                            case 1295:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPAO, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Áo Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 200%/Món + Chỉ Số Áo 5M\n"
                                        + "|6|Sét Siêu Vip TNSM 500%/Món + Chỉ Số Áo 10M\n"
                                        + "Ngẫu Nhiên: Sở Hữu Sét Linh Khí : Tăng 5k% HP,KI,SD\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1295).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1296:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPQUAN, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Quần Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 200%/Món + Chỉ Số Quần Dưới 10M\n"
                                        + "|6|Sét Siêu Vip TNSM 500%/Món + Chỉ Số Quần Dưới 20M\n"
                                        + "Ngẫu Nhiên: Sở Hữu Sét Linh Khí : Tăng 5k% HP,KI,SD\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1296).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1297:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPGANG, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Găng Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 200%/Món + Chỉ Số Găng Dưới 100K\n"
                                        + "|6|Sét Siêu Vip TNSM 500%/Món + Chỉ Số Găng Dưới 200K\n"
                                        + "Ngẫu Nhiên: Sở Hữu Sét Linh Khí : Tăng 5k% HP,KI,SD\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1297).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;
                            case 1298:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPGIAY, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Giay Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 200%/Món + Chỉ Số Giay Dưới 10M\n"
                                        + "|6|Sét Siêu Vip TNSM 500%/Món + Chỉ Số Giay Dưới 20M\n"
                                        + "Ngẫu Nhiên: Sở Hữu Sét Linh Khí : Tăng 5k% HP,KI,SD\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1298).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1299:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPNHAN, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Nhẫn Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 200%/Món + Chỉ Số Nhẫn 10\n"
                                        + "|6|Sét Siêu Vip TNSM 500%/Món + Chỉ Số Nhẫn 20\n"
                                        + "Ngẫu Nhiên: Sở Hữu Sét Linh Khí : Tăng 5k% HP,KI,SD\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1299).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;

                            case 1066:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPAOfree, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Áo Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 8888% + Chỉ Số Áo Ramdom\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1066).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1067:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPQUANfree, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Quần Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 8888% + Chỉ Số quần Ramdom\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1067).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1070:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPGANGfree, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Găng Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 8888% + Chỉ Số găng Ramdom\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1070).quantity) + " Mảnh\n",
                                        "Ghép Nhanh"
                                );
                                break;
                            case 1068:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPGIAYfree, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Giay Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 8888% + Chỉ Số Giay Ramdom\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1068).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;
                            case 1069:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.GHEPNHANfree, -1,
                                        "|7|Xin Chào Chủ Nhân Thiên Gioi\n"
                                        + "|8|Chức Năng Ghép Nhẫn Tân Thủ\n"
                                        + "|6|Yêu Cầu Mỗi Lần Ghép Mất 9999 Mảnh\n"
                                        + "|4|Sét Thường TNSM 8888% + Chỉ Số Nhẫn Ramdom\n"
                                        + "|7|Hiện Đang Có: " + (InventoryService.gI().findItemBag(pl, (short) 1069).quantity) + " Mảnh\n",
                                        "Ghép Luôn"
                                );
                                break;

                            case 1660:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.checkthongtin, -1,
                                        "|7|Xin Chào Chủ Nhân\n"
                                        + "|8|Chức Năng Check Thông Tin Nè\n"
                                        + "|7|Số Dư VND: " + Util.FormatNumber(pl.getSession().vnd) + " đ\n"
                                        + "|7|Đã Nạp VND: " + Util.FormatNumber(pl.getSession().tongnap) + " đ\n"
                                        + "|6|Online Ngày Nay: " + pl.phutOnline + " Phút",
                                        "Bản Thân",
                                        "Đệ Tử",
                                        "Đồ Sát"
                                );
                                break;
                            case 1881:
//                                if (pl.Saga_VIP < 5000) {
//                                    Service.gI().sendThongBaoFromAdmin(pl, "✎Xem ở Phó Bản Boss Đi Cu");
//                                    return;
//                                }
                                // chi tru item khi gui duoc danh sach cho client
                                if (BossManager.gI().showListBoss(pl)) {
                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Máy dò gặp lỗi nên chưa hiện danh sách - item KHÔNG bị trừ, bạn thử lại sau nhé");
                                }
                                break;
                            case 1194:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.PKALL, -1,
                                        "Xin Chào Kiếm Sĩ Số 1 Võ Lâm\n"
                                        + "|8|Minh Chủ Lệnh\n"
                                        + "|7|Quyền Đại Khai Sát Gioi Võ Lâm\n",
                                        "Bật Tắt\n Đồ Sát"
                                );
                                break;
                            case 1866:
                                UseItem.gI().hopquaramdomngoc(pl, item);
                                break;
                            case 1867:
                                UseItem.gI().hopquaramdomcaitrangtho(pl, item);
                                break;
                            case 1131:
                                UseItem.gI().openvnd500k(pl, item);
                                break;
                            case 1133:
                                UseItem.gI().openvnd99k(pl, item);
                                break;

                            case 1645:
                                UseItem.gI().hopquaramdomcaitrangGohan(pl, item);
                                break;
                            case 1646:
                                UseItem.gI().hopquaramdomcaitrangGohanvip(pl, item);
                                break;
                            case 1698:
                                UseItem.gI().hopquaramdomcaitrangGohanvippet(pl, item);
                                break;

                            case 1224:
                                pl.SagaThienDao++;
                                //    Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng 1 Cấp Thiên Đạo lực Chiến Tăng Vọt");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1217:
                                pl.Saga_VIP++;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng 1 VIP Ingame");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1216:
                                pl.TamkjllThomo += 1;
                                //   Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 1 Người Đào Mỏ Tại Map Đào Mỏ\n Ra Đảo Kame NPC Chuyển Sinh Để Đi");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1661:
                                pl.TamkjllPetGiong++;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng 1 Cấp Em Bé");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1662:
                                pl.EmBeLv += panel.tuning.SystemTuning.getI("embe_item_1662_lv");
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng "
                                        + panel.tuning.SystemTuning.getI("embe_item_1662_lv") + " LV EmBe");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1663:
                                pl.TamkjllPetHunger += panel.tuning.SystemTuning.getI("embe_item_1663_thuc_an");
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Cho Đạo Lữ ăn "
                                        + panel.tuning.SystemTuning.getI("embe_item_1663_thuc_an")
                                        + " Lượng Thức Ăn Nhỏ");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1094:
                                pl.diemfam += 5000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 5M Điểm Fam HãY Cố Gắng Đua Top");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1095:
                                pl.diemfam += 4000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 4M Điểm Fam HãY Cố Gắng Đua Top");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 699:
                                pl.nPoint.hpg += 800000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 800k HP Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 700:
                                pl.nPoint.dameg += 500000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 500k Dame Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 701:
                                pl.nPoint.mpg += 800000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 800k Mp Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1123:
                                pl.nPoint.hpg += 1000000;
                                pl.nPoint.dameg += 1000000;
                                pl.nPoint.mpg += 1000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 1m cs Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1124:
                                pl.nPoint.hpg += 500000;
                                pl.nPoint.dameg += 500000;
                                pl.nPoint.mpg += 500000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 500k cs Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1127:
                                pl.pet.nPoint.hpg += 500000;
                                pl.pet.nPoint.dameg += 500000;
                                pl.pet.nPoint.mpg += 500000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 500k cs Gốc pet");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1125:
                                pl.nPoint.hpg += 200000;
                                pl.nPoint.dameg += 200000;
                                pl.nPoint.mpg += 200000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 200k cs Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1855:
                                pl.nPoint.hpg += 50000;
                                pl.nPoint.dameg += 10000;
                                pl.nPoint.mpg += 50000;
                                pl.SagaChuyenSinh++;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Chuyển Sinh Thành Công");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1162:
                                pl.nPoint.hpg += Util.nextInt(1, 100000);
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được: " + pl.nPoint.hpg + " HP Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1163:
                                pl.nPoint.dameg += Util.nextInt(1, 100000);
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được: " + pl.nPoint.dameg + " SD Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1164:
                                pl.nPoint.mpg += Util.nextInt(1, 100000);
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được: " + pl.nPoint.mpg + " kI Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1165:
                                pl.nPoint.defg += Util.nextInt(1, 10000);
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được: " + pl.nPoint.defg + " kI Gốc");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1096:
                                pl.diemfam += 3000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 3M Điểm Fam HãY Cố Gắng Đua Top");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1097:
                                pl.diemfam += 2000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 2M Điểm Fam HãY Cố Gắng Đua Top");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1106:
                                pl.diemfam += 1000000;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Nhận Được 1M Điểm Fam HãY Cố Gắng Đua Top");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;

                            case 1664:
                                pl.TamkjllPetHunger += panel.tuning.SystemTuning.getI("embe_item_1664_thuc_an");
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Cho Đạo Lữ ăn "
                                        + panel.tuning.SystemTuning.getI("embe_item_1664_thuc_an")
                                        + " Lượng Thức Ăn Lớn");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1665:
                                Input.gI().createFormChangeNameByItem(pl);

                                break;

                            case 1225:
                                pl.SagaDiaDao++;
                                //   Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng 1 Cấp Nhập Ma 50k Sức Đánh\n lực Chiến Tăng Vọt");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 1132:
                                pl.SagaTuTien[1]++;
                                 pl.tho_nguyen += 259200;
                                Service.gI().sendThongBaoFromAdmin(pl, "Bạn Đã Tăng 1 Cảnh Gioi Tu Tiên");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                Service.getInstance().sendMoney(pl);
                                Service.getInstance().point(pl);
                                InventoryService.gI().sendItemBag(pl);
                                break;
                            case 736:
                                ItemService.gI().OpenItem736(pl, item);
                                break;
                            case 987:
                                Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); // đá bảo vệ
                                break;

                            case 1623:
                                TaskService.gI().sendNextTaskMain(pl);
                                break;
                            case 1863:
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.HOP_QUA_THAN_LINH, -1,
                                        "Chọn hành tinh của đồ thần linh muốn nhận.",
                                        "Trái đất", "Namek", "Xayda");
                                break;
                            case 1626: {
                                int[] listItem = {856, 943, 942};
                                if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                                    Service.gI().sendThongBaoOK(pl, "Cần 1 ô hành trang để mở");
                                    return;
                                }
                                Item phuKien = ItemService.gI().createNewItem((short) listItem[Util.nextInt(2)]);
                                if (phuKien.template.id == 856) {
                                    phuKien.itemOptions.add(new Item.ItemOption(50, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(77, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(103, 10));
                                } else if (phuKien.template.id == 943) {
                                    phuKien.itemOptions.add(new Item.ItemOption(50, 10));
                                } else if (phuKien.template.id == 942) {
                                    phuKien.itemOptions.add(new Item.ItemOption(77, 10));
                                    phuKien.itemOptions.add(new Item.ItemOption(103, 10));
                                }
                                if (Util.isTrue(95, 100)) {
                                    phuKien.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
                                }
                                InventoryService.gI().addItemBag(pl, phuKien, 999999);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBag(pl);
                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + phuKien.template.name);
                            }
                            break;
                            case 1628: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                        player.pet.openSkill2();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                            case 1629: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                        player.pet.openSkill3();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                            case 1630: {
                                Player player = pl;
                                if (player.pet != null) {
                                    if (player.pet.playerSkill.skills.get(3).skillId != -1) {
                                        player.pet.openSkill4();
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");
                                        return;
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                    return;
                                }
                            }
                            break;
                        }
                        break;
                }
                TaskService.gI().checkDoneTaskUseItem(pl, item);
                InventoryService.gI().sendItemBag(pl);
            } else {
                Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
            }
        }
    }

    public void usePorata(Player player, Item item) {
        if (player.pet != null) {
            switch (player.fusion.typeFusion) {
                case ConstPlayer.LUONG_LONG_NHAT_THE -> {
                    if (player.isAdmin()) {
                        player.pet.unFusion();
                        return;
                    }
                    Service.gI().sendThongBao(player, "Chưa hết thời gian Lưỡng Long Nhất Thể");
                }
                case ConstPlayer.NON_FUSION ->
                    player.pet.fusion(switch (item.template.id) {
                        case 454 ->
                            ConstPlayer.HOP_THE_PORATA;
                        case 921 ->
                            ConstPlayer.HOP_THE_PORATA2;
                        case 1668 ->
                            ConstPlayer.HOP_THE_PORATA_S;
                        case 1669 ->
                            ConstPlayer.HOP_THE_PORATA_SS;
                        case 1670 ->
                            ConstPlayer.HOP_THE_PORATA_SSS;
                        default ->
                            ConstPlayer.LUONG_LONG_NHAT_THE;
                    });
                default ->
                    player.pet.unFusion();
            }
        } else {
            Service.gI().sendThongBao(player, "Sở hữu đệ tử để sử dụng vật phẩm này");
        }
    }

    public void openRuongGo(Player pl, Item item) {
        List<String> textRuongGo = new ArrayList<>();
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            long param = item.itemOptions.get(0).param;
            int gold = 0;
            int[] listItem = {1224};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 6) {
                itemReward = ItemService.gI().createNewItem((short) 1225);
                itemReward.quantity = Util.nextInt(1, 10);
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 7) {
                itemReward = ItemService.gI().createNewItem((short) 1224);
                itemReward.quantity = 7;
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 8) {
                itemReward = ItemService.gI().createNewItem((short) 1224);
                itemReward.quantity = 8;
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 9) {
                itemReward = ItemService.gI().createNewItem((short) 1224);
                itemReward.quantity = 9;
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 10) {
                itemReward = ItemService.gI().createNewItem((short) 1224);
                itemReward.quantity = 10;
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 1224);
                itemReward.quantity = 11;
                InventoryService.gI().addItemBag(pl, itemReward, 999999);
                textRuongGo.add(text + itemReward.info);
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                    "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + textRuongGo.size() + "]");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryService.gI().sendItemBag(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        }
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 999999);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);

            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 1216:
                pl.itemTime.lastTimeItemTest = System.currentTimeMillis();
                pl.itemTime.isItemTest = true;
                break;
            case 382: // bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: // bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: // giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: // cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: // ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: // máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.gI().point(pl);

                break;
            case 1100:// bo huyet
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;

            case 1218:// bo huyet
                pl.itemTime.tgianbacdau = System.currentTimeMillis();
                pl.itemTime.isdanbacdau = true;
                break;
            case 472:// bo huyet
                pl.itemTime.lastTimeBanhDacBiet = System.currentTimeMillis();
                pl.itemTime.isBanhTrungDacBiet = true;
                break;
            case 473:// bo huyet
                pl.itemTime.lastTimeBanhTrungthu = System.currentTimeMillis();
                pl.itemTime.isBanhTrungThu = true;
                break;
            case 465:// bo huyet
                pl.itemTime.lastTimeBanh1Trung = System.currentTimeMillis();
                pl.itemTime.isBanhTrung1Trung = true;
                break;
            case 466:// bo huyet
                pl.itemTime.lastTimeBanh2Trung = System.currentTimeMillis();
                pl.itemTime.isBanhTrung2Trung = true;
                break;
            case 890:// bo huyet
                pl.itemTime.lastTimegaquay = System.currentTimeMillis();
                pl.itemTime.isBanhgaquay = true;
                break;
            case 891:// bo huyet
                pl.itemTime.lastTimethapcam = System.currentTimeMillis();
                pl.itemTime.isBanhthapcam = true;
                break;

            case 1326:// bo huyet
                pl.itemTime.thoigianUpx2 = System.currentTimeMillis();
                pl.itemTime.isUpTbx2 = true;
                break;
            case 1327:// bo huyet
                pl.itemTime.thoigianUpx3 = System.currentTimeMillis();
                pl.itemTime.isUpTbx3 = true;
                break;
            case 1328:// bo huyet
                pl.itemTime.thoigianUpx4 = System.currentTimeMillis();
                pl.itemTime.isUpTbx4 = true;
                break;
            case 1329:// bo huyet
                pl.itemTime.thoigianUpx5 = System.currentTimeMillis();
                pl.itemTime.isUpTbx5 = true;
                break;
            case 1330:// bo huyet
                pl.itemTime.thoigianUpx6 = System.currentTimeMillis();
                pl.itemTime.isUpTbx6 = true;
                break;
            case 1331:// bo huyet
                pl.itemTime.thoigianUpx7 = System.currentTimeMillis();
                pl.itemTime.isUpTbx7 = true;
                break;

            case 1916: // the x2 diem farm
                useTheDiemFarm(pl, 2);
                break;
            case 1917: // the x3 diem farm
                useTheDiemFarm(pl, 3);
                break;
            case 1918: // the x5 diem farm
                useTheDiemFarm(pl, 5);
                break;

            case 1303:// bo huyet
                pl.itemTime.lastTimecnsc = System.currentTimeMillis();
                pl.itemTime.iscuongnosieucap = true;
                break;
            case 1304:// bo huyet
                pl.itemTime.lastTimebhsc = System.currentTimeMillis();
                pl.itemTime.isbohuyetsieucap = true;
                break;
            case 1305:// bo huyet
                pl.itemTime.lastTimebksc = System.currentTimeMillis();
                pl.itemTime.isbokhisieucap = true;
                break;
            case 1306:// bo huyet
                pl.itemTime.lastTimegxsc = System.currentTimeMillis();
                pl.itemTime.isgiapxensieucap = true;
                break;

            case 1233:// bo huyet
                pl.itemTime.lastTimeBINHX3 = System.currentTimeMillis();
                pl.itemTime.isBINHX3 = true;
                break;
            case 1234:// bo huyet
                pl.itemTime.lastTimeBINHX5 = System.currentTimeMillis();
                pl.itemTime.isBINHX5 = true;
                break;
            case 1235:// bình tnsm
                pl.itemTime.lastTimeBINHX7 = System.currentTimeMillis();
                pl.itemTime.isBINHX7 = true;
                break;

            case 1236:// bình tnsm
                pl.itemTime.lastTimeBINHX10 = System.currentTimeMillis();
                pl.itemTime.isBINHX10 = true;
                break;

            case 1226:// bo huyet
                pl.itemTime.lastTimeLYNUOCMIATO = System.currentTimeMillis();
                pl.itemTime.isLYNUOCMIATO = true;
                break;
            case 1227:// bo huyet
                pl.itemTime.lastTimeLYNUOCMIATHOM = System.currentTimeMillis();
                pl.itemTime.isLYNUOCMIATHOM = true;
                break;
            case 1228:// bình tnsm
                pl.itemTime.lastLYNUOCMIASAURIENG = System.currentTimeMillis();
                pl.itemTime.isLYNUOCMIASAURIENG = true;
                break;
            case 1231:// bo huyet
                pl.itemTime.lastTimeMaydoskh = System.currentTimeMillis();
                pl.itemTime.isMAYDOSKH = true;
                break;
            case 1232:// bình tnsm
                pl.itemTime.lastTimeMaydoskhvip = System.currentTimeMillis();
                pl.itemTime.isMAYDOSKHVIP = true;
                break;

            case 1101:// bo khi
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102:// gx
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103:// an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 638: // Commeson
                pl.itemTime.lastTimeUseCMS = System.currentTimeMillis();
                pl.itemTime.isUseCMS = true;
                break;
            case 2160: // Nồi cơm điện
                pl.itemTime.lastTimeUseNCD = System.currentTimeMillis();
                pl.itemTime.isUseNCD = true;
                break;
            case 579:
                if (pl.itemTime.isUseDK) {
                    Service.gI().sendThongBaoFromAdmin(pl, "Bạn Còn Time Huyết mạch Khỉ!");
                    return;
                }
                pl.itemTime.lastTimeUseDK = System.currentTimeMillis();
                pl.itemTime.isUseDK = true;
                EffectSkillService.gI().sendEffectMonkey1(pl);
                pl.effectSkill.isBienHinh = true;
                Message msg;
                msg = new Message(-90);
                try {
                    msg.writer().writeByte(1);// check type
                    msg.writer().writeInt((int) pl.id); //id player
                    msg.writer().writeShort(198);//set head
                    msg.writer().writeShort(193);//setbody
                    msg.writer().writeShort(194);//set leg
                    msg.writer().writeByte(pl.effectSkill.isMonkey ? 1 : 0);//set khỉ
                    Service.gI().sendMessAllPlayerInMap(pl, msg);
                    msg.cleanup();
                } catch (IOException ex) {
                    //    java.util.logging.Logger.getLogger(UseItem.class.getName()).log( null, ex);
                }

                if (!pl.isPet) {
                    PlayerService.gI().sendInfoHpMp(pl);
                }
                Service.gI().point(pl);
                Service.gI().Send_Info_NV(pl);
                Service.gI().sendInfoPlayerEatPea(pl);
                // Buff Trung Thu: +ty le roi vat pham SK trong 15 phut
                EventSuKien.TrungThuService.gI().activateTailBuff(pl);
                Service.getInstance().sendThongBaoFromAdmin(pl,
                        "Duoi khic hieu luc: +" + EventSuKien.TrungThuService.gI().tailBonusPercent()
                                + "% ty le roi vat pham Su Kien Trung Thu trong "
                                + (EventSuKien.TrungThuService.gI().tailDurationMs() / 60000) + " phut!");
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                break;

            case 1045: // Duoi khic - chi buff Trung Thu, khong bien hinh
                EventSuKien.TrungThuService.gI().activateTailBuff(pl);
                Service.getInstance().sendThongBaoFromAdmin(pl,
                        "Duoi khic hieu luc: +" + EventSuKien.TrungThuService.gI().tailBonusPercent()
                                + "% ty le roi vat pham Su Kien Trung Thu trong "
                                + (EventSuKien.TrungThuService.gI().tailDurationMs() / 60000) + " phut!");
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                break;

            case 1914: // Ruong Trung Thu (icon cua Hop Trung Thu 1512)
                EventSuKien.TrungThuService.gI().openBox(pl, item);
                break;

            case 663: // bánh pudding
            case 664: // xúc xíc
            case 665: // kem dâu
            case 666: // mì ly
            case 667: // sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 880:
            case 881:
            case 882:
                pl.itemTime.lastTimeEatMeal2 = System.currentTimeMillis();
                pl.itemTime.isEatMeal2 = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal2);
                pl.itemTime.iconMeal2 = item.template.iconID;
                break;
            case 1109: // máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
    }

    /**
     * [The Diem Farm] Buff the diem farm nhu binh tiem nang: hien icon + dem nguoc
     * tren man hinh (gui boi sendAllItemTime), KHONG gui thong bao chat.
     * Cong don thoi gian: neu cung he so con hieu luc thi them 1 luong the vao
     * thoi gian con lai; moi the rieng biet tinh thoi gian cua no.
     */
    private void useTheDiemFarm(Player pl, int heSo) {
        long now = System.currentTimeMillis();
        long thoiGian = item.ItemTime.thoiGianTheFamMs();
        long lastTime = 0;
        boolean dangBat = false;
        switch (heSo) {
            case 2:
                dangBat = pl.itemTime.isFamX2;
                lastTime = pl.itemTime.lastTimeFamX2;
                break;
            case 3:
                dangBat = pl.itemTime.isFamX3;
                lastTime = pl.itemTime.lastTimeFamX3;
                break;
            case 5:
                dangBat = pl.itemTime.isFamX5;
                lastTime = pl.itemTime.lastTimeFamX5;
                break;
        }
        if (dangBat && !Util.canDoWithTime(lastTime, thoiGian)) {
            lastTime += thoiGian; // con hieu luc -> cong them 1 luong the
        } else {
            lastTime = now; // het luc hoac moi bat -> tinh lai tu dau
        }
        switch (heSo) {
            case 2:
                pl.itemTime.isFamX2 = true;
                pl.itemTime.lastTimeFamX2 = lastTime;
                break;
            case 3:
                pl.itemTime.isFamX3 = true;
                pl.itemTime.lastTimeFamX3 = lastTime;
                break;
            case 5:
                pl.itemTime.isFamX5 = true;
                pl.itemTime.lastTimeFamX5 = lastTime;
                break;
        }
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        } else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1820, SummonDragon.DRAGON_BLACK_SHENRON);
        } else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
            switch (tempId) {
                case 925:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
                    break;

                default:
                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.id >= 1334 && item.template.id <= 1351) {
                learnSkillSuperNew(pl, item);
            } else {
                if (item.template.gender == pl.gender || item.template.gender == 3) {
                    String[] subName = item.template.name.split("");
                    byte level = Byte.parseByte(subName[subName.length - 1]);
                    Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                    if (curSkill.template.id >= 17 && pl.nPoint.power < 150_000_000) {
                        Service.gI().sendThongBao(pl, "Yêu cầu đạt 150tr sức mạnh để học " + curSkill.template.name);
                        return;
                    }
                    if (curSkill.point == 7) {
                        Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                    } else {
                        if (curSkill.point == 0) {
                            if (level == 1) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                        level);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 23);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Skill skillNeed = SkillUtil
                                        .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp "
                                        + skillNeed.point + " trước!");
                            }
                        } else {
                            if (curSkill.point + 1 == level) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                        level);
                                // System.out.println(curSkill.template.name + " - " + curSkill.point);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 62);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
                                        + (curSkill.point + 1) + " trước!");
                            }
                        }
                        InventoryService.gI().sendItemBag(pl);
                    }
                } else {
                    Service.gI().sendThongBao(pl, "Không thể thực hiện");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void learnSkillNew2(Player pl, Item item) {
//        Message msg;
//        try {
//            if (item.template.gender == pl.gender || item.template.gender == 3) {
//                byte level = SkillUtil.getLevelSkillByItemID(item.template.id);
//                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
//                if (curSkill == null) {
//                    SkillService.gI().learSkillSpecial(pl,
//                            (byte) SkillUtil.getSkillByItemID(pl, item.template.id).skillId);
//                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                    return;
//                } else {
//                    if (curSkill.point == 7) {
//                        Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
//                    } else {
//                        if (curSkill.point == 0) {
//                            if (level == 1) {
//                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
//                                        level);
//                                SkillUtil.setSkill(pl, curSkill);
//                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                msg = Service.gI().messageSubCommand((byte) 23);
//                                msg.writer().writeShort(curSkill.skillId);
//                                pl.sendMessage(msg);
//                                msg.cleanup();
//                                if (curSkill.template.id == Skill.SUPER_NAMEC
//                                        || curSkill.template.id == Skill.SUPER_SAIYAN
//                                        || curSkill.template.id == Skill.SUPER_TRAI_DAT) {
//                                    curSkill = SkillUtil.createSkill(Skill.GONG, level);
//                                    SkillUtil.setSkill(pl, curSkill);
//                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                    msg = Service.gI().messageSubCommand((byte) 23);
//                                    msg.writer().writeShort(curSkill.skillId);
//                                    pl.sendMessage(msg);
//                                    msg.cleanup();
//                                }
//                            } else {
//                                Skill skillNeed = SkillUtil
//                                        .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
//                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp "
//                                        + skillNeed.point + " trước!");
//                            }
//                        } else {
//                            if (curSkill.point + 1 == level) {
//                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
//                                        level);
//                                // System.out.println(curSkill.template.name + " - " + curSkill.point);
//                                SkillUtil.setSkill(pl, curSkill);
//                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                                msg = Service.gI().messageSubCommand((byte) 62);
//                                msg.writer().writeShort(curSkill.skillId);
//                                pl.sendMessage(msg);
//                                msg.cleanup();
//                            } else {
//                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
//                                        + (curSkill.point + 1) + " trước!");
//                            }
//                        }
//                        InventoryService.gI().sendItemBag(pl);
//                    }
//                }
//            } else {
//                Service.gI().sendThongBao(pl, "Không thể thực hiện");
//            }
//        } catch (Exception e) {
//
//        }
//    }
    private void learnSkillSuperNew(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                byte level = SkillUtil.getLevelSkillByItemID(item.template.id);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 6) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                            SkillService.gI().learSkillSpecial(pl, (byte) 30);
                        } else {
                            Skill skillNeed = SkillUtil
                                    .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            if (level > 1) {
                                Item itemNew = ItemService.gI().createNewItem((short) (item.template.id - 1));
                                String name = itemNew.template.name;
                                String desiredName = name.substring(5);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + desiredName + " trước!");
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " trước!");
                            }
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            if (level > 1) {
                                Item itemNew = ItemService.gI().createNewItem((short) (item.template.id - 1));
                                String name = itemNew.template.name;
                                String desiredName = name.substring(5);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + desiredName + " trước!");
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " trước!");
                            }
                        }
                    }
                    InventoryService.gI().sendItemBag(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changexencon(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeXencon(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changekidbu(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changekidbu(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changekidfide(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changekidfide(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changekiduub(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changekiduub(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changekidxen(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changekidxen(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changebroly(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBroly(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeblack(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeblackgoku(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeblackrose(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeblackgokurose(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changezamasu(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changezamasu(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan1(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan1(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan2(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan2(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan3(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan3(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan4(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan4(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan5(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan5(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeGohan6(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeGohan6(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changecumber(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changecumber(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void hopquaramdomcaitrangtho(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 464, 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 69), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 500000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 5000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 5000)));
            it.itemOptions.add(new ItemOption(117, Util.nextInt(1, 70)));

            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, 1));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void openvnd500k(Player player, Item item) {
        if (player != null && item != null) {

            // Cộng tiền cho người chơi
            player.getSession().vnd += 500000;
            PlayerDAO.updateVND(player);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendThongBao(player, "Bạn Nhận dc 500k VNĐ");
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void openvnd99k(Player player, Item item) {
        if (player != null && item != null) {

            // Cộng tiền cho người chơi
            player.getSession().vnd += 99999;
            PlayerDAO.updateVND(player);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendThongBao(player, "Bạn Nhận dc 99k coin");
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void hopquaramdomcaitrangGohan(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578), 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 69), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 10000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 10000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 10000)));
            it.itemOptions.add(new ItemOption(60, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 3));
            it.itemOptions.add(new ItemOption(251, 1));
            if (Util.isTrue(98, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquaramdomcaitrangGohanvip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578), 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 70), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(60, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 3));
            it.itemOptions.add(new ItemOption(251, 1));
            if (Util.isTrue(95, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquaramdomcaitrangGohanvippet(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1692, 1697), 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 70), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20000)));
            it.itemOptions.add(new ItemOption(60, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 3));
            it.itemOptions.add(new ItemOption(251, 1));
            if (Util.isTrue(95, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopqualinhthu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(1351, 1367), 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 69), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(60, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 3));
            it.itemOptions.add(new ItemOption(251, 1));
            if (Util.isTrue(98, 100)) {
                it.itemOptions.add(new ItemOption(231, 1));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquaphukien(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(814, 817), 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 69), 5));
            it.itemOptions.add(new ItemOption(59, 5));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 1000)));
            it.itemOptions.add(new ItemOption(60, Util.nextInt(1, 7)));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 3));
            it.itemOptions.add(new ItemOption(251, 1));
            if (Util.isTrue(98, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(5, 10)));
            }
            InventoryService.gI().addItemBag(pl, it, 999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    public void ghepmanhtrung(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item trungThuong = ItemService.gI().createNewItem((short) 1158, 1);
            Item trungVip = ItemService.gI().createNewItem((short) 1159, 1);
            trungThuong.itemOptions.add(new ItemOption(244, 1));
            trungVip.itemOptions.add(new ItemOption(244, 1));
            if (Util.isTrue(98, 100)) {
                InventoryService.gI().addItemBag(pl, trungThuong, 999999);

            } else {
                InventoryService.gI().addItemBag(pl, trungVip, 999999);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 999);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void goidau9(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 523, 100);
            InventoryService.gI().addItemBag(pl, it, 99999999);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void hopquaramdomthoivang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 457, Util.nextInt(1, 200));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquaramdomdns(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 674, Util.nextInt(1, 50));
            if (it.template != null) {
                //  it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquanoitai(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1825, Util.nextInt(1, 100000));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private static final java.util.concurrent.ScheduledExecutorService FISH_EXEC
            = java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "FishScheduler");
                t.setDaemon(true);
                return t;
            });
    private static final short moicau = 1717;      // mồi
    private static final short cango = 1713;      // cần gỗ
    private static final short canbac = 1714;      // cần bạc
    private static final short canvang = 1715;      // cần vàng
    private static final short cantitan = 1716;      // cần kim cương
    private static final int Tgian_cau = 30000; // 30s
    private static final short[] List_Ca = {
        1718, 1719, 1720, 1721, 1722, 1723, 1724, 1725, 1726, 1727, 1728, 1857, 1858, 1859, 1860, 1110, 457, 457, 457, 14, 15, 16, 17, 18, 19, 20, 1233, 1234, 1235, 1236, 1873, 1874,
        1886, 1887, 1888, 1889, 1890, 1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909, 1910, 1911, 457, 457, 457, 457, 457, 457, 457, 861, 861, 861, 77, 77, 77,
        77, 77, 77, 77, 861, 987, 987
    };

    private int getFishingChance(int rodId) {
        switch (rodId) {
            case cango:
                return 1;  // gỗ 1%
            case canbac:
                return 3;  // bạc 3%
            case canvang:
                return 5;  // vàng 5%
            case cantitan:
                return 7;  // kim cương 7%
            default:
                return 1;  // mặc định
        }
    }

    private void Can_cau_ca(Player pl, Item rod) {
        if (pl == null || rod == null || rod.template == null) {
            return;
        }

        if (pl.zone == null || pl.zone.map == null || pl.zone.map.mapId != 216) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Chỉ có thể câu cá tại map Câu cá.");
            return;
        }
        long now = System.currentTimeMillis();
        if (pl.useCanCau) {
            long remain = (Tgian_cau - (now - pl.lasttimeCanCau)) / 1000;
            if (remain > 0) {
                Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Đang câu rồi, còn " + remain + " giây...");
                return;
            } else {
                pl.useCanCau = false;
            }
        }
        Item bait = InventoryService.gI().findItemBag(pl, moicau);
        if (bait == null || bait.quantity <= 0) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Bạn cần có Mồi câu để thả cần.");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Hành trang đã đầy.");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, bait, 1);
        pl.useCanCau = true;
        pl.lasttimeCanCau = now;
        Service.getInstance().sendThongBaoFromAdmin(pl,
                "|7|Bạn bắt đầu thả cần...\n|6|Vui lòng đợi 30 giây.");
        FISH_EXEC.schedule(() -> {
            try {
                if (pl == null || pl.zone == null || pl.zone.map == null || pl.zone.map.mapId != 216) {
                    return;
                }
                int chance = getFishingChance(rod.template.id);
                boolean success = Util.isTrue(chance, 100);

                if (!success) {
                    Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Cá chạy mất rồi!\n|4|Tiếp tục câu nào!");
                    return;
                }
                int idx = Util.nextInt(0, List_Ca.length - 1);
                Item fish = ItemService.gI().createNewItem(List_Ca[idx]);
                if (fish == null) {
                    Service.getInstance().sendThongBaoFromAdmin(pl, "|3|Có lỗi khi tạo vật phẩm cá.");
                    return;
                }
                if (rod.template.id == cantitan) {
                    fish.quantity = 2;
                }
                if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
                    Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Hành trang đã đầy, không nhận được cá.");
                    return;
                }
                InventoryService.gI().addItemBag(pl, fish, 999999);
                InventoryService.gI().sendItemBag(pl);
                Service.getInstance().sendThongBaoFromAdmin(pl,
                        "|7|Bạn câu được \n|6|[" + fish.template.name + "] \nXin chúc mừng!");
                Service.getInstance().sendThongBaoAllPlayer(
                        "|7|Người chơi [" + pl.name + "] vừa câu được \n|6|[" + fish.template.name + "]");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pl != null) {
                    pl.useCanCau = false;
                }
            }
        }, Tgian_cau, TimeUnit.MILLISECONDS);
    }

    private void RuongSkhNguyenThuy(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1730, 1731, 1732, 1733, 1734};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(158, Util.nextInt(5, 500000))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(5, 50000))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(158, Util.nextInt(5, 8))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(5, 1500000)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(158, Util.nextInt(5, 8))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(10, 75000)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(158, Util.nextInt(5, 8))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(20, 1500000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(158, Util.nextInt(5, 8))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));

                    break;
            }
            if (Util.isTrue(90, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(75, Util.nextInt(5, 6))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(Util.nextInt(188, 190), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(60, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(249, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(61, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(251, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(75, Util.nextInt(5, 6))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(Util.nextInt(188, 190), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(60, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(249, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(61, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(251, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(75, Util.nextInt(5, 6))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(Util.nextInt(188, 190), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(60, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(249, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(61, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(251, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(75, Util.nextInt(5, 6))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(Util.nextInt(188, 190), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(60, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(249, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(61, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(251, 1)); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopquadangusac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 674, Util.nextInt(1, 50000));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquadanangcap(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), Util.nextInt(1, 50000));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquadabaove(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 987, 1);
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquasaophale100(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1191, Util.nextInt(1, 10));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(50, 1000));
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopqualinhthu50(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1561, 1);
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(50, 500000));
                it.itemOptions.add(new ItemOption(77, 500000));
                it.itemOptions.add(new ItemOption(103, 500000));
                it.itemOptions.add(new ItemOption(14, 38));
                it.itemOptions.add(new ItemOption(107, 30));
                if (Util.isTrue(80, 100)) {
                    it.itemOptions.add(new ItemOption(93, 60));
                }
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopqualongden70(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(467, 471), 1);
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(50, 700000));
                it.itemOptions.add(new ItemOption(77, 700000));
                it.itemOptions.add(new ItemOption(103, 700000));
                it.itemOptions.add(new ItemOption(5, 70));
                it.itemOptions.add(new ItemOption(107, 30));
                if (Util.isTrue(80, 100)) {
                    it.itemOptions.add(new ItemOption(93, 60));
                }
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquathiendao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1224, Util.nextInt(1, 30));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(30, 70000));
                if (Util.isTrue(100, 100)) {
                    it.itemOptions.add(new ItemOption(93, 7));
                }
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquadakham1000(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1206, Util.nextInt(1, 5));
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(50, 10000));
                if (Util.isTrue(95, 100)) {
                    it.itemOptions.add(new ItemOption(30, 7));
                }
                InventoryService.gI().addItemBag(pl, it, 99999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    public void nhancaitrangupngoc(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) 1834, 1);
            if (it.template != null) {
                it.itemOptions.add(new ItemOption(239, 1));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                InventoryService.gI().addItemBag(pl, it, 999999);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
                short icon1 = item.template != null ? item.template.iconID : -1;
                short icon2 = it.template.iconID;
                CombineService.gI().sendEffectOpenItem(pl, icon1, icon2);
            } else {
                Service.getInstance().sendThongBao(pl, "Item template is missing!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void hopquaramdomngoc(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {77, 861};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index], 5000);

            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void hopquadanhhieu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 5) {
            short[] temp = {1737, 1738, 1739, 1740, 1742, 1743, 1744, 1746, 1747, 1748, 1749};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index], 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 0));
            it.itemOptions.add(new ItemOption(59, 0));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(60, 0));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 0));
            it.itemOptions.add(new ItemOption(251, 1));
            it.itemOptions.add(new ItemOption(231, 7));

            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void hopquathucuoi(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 5) {
            short[] temp = {1750, 1751, 1752, 1753};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index], 1);
            it.itemOptions.add(new ItemOption(Util.nextInt(67, 71), 0));
            it.itemOptions.add(new ItemOption(59, 0));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 1000)));
            it.itemOptions.add(new ItemOption(60, 0));
            it.itemOptions.add(new ItemOption(249, 1));
            it.itemOptions.add(new ItemOption(61, 0));
            it.itemOptions.add(new ItemOption(251, 1));
            it.itemOptions.add(new ItemOption(231, 7));

            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void hopqua1238(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 5) {
            short[] temp = {1219, 1220, 1221, 1222, 1223};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index], 1);
            it.itemOptions.add(new ItemOption(59, 0));
            it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 9999)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 9999)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 9999)));
            it.itemOptions.add(new ItemOption(253, 7));
            it.itemOptions.add(new ItemOption(231, 7));

            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void openRandomVNDLon(Player player, Item item) {
        if (player != null && item != null) {
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;
            if (Util.isTrue(99, 100)) {
                rewardVND = Util.nextInt(500, 1500);
            } else if (Util.isTrue(10, 100)) {
                rewardVND = Util.nextInt(1500, 3000);
            } else if (Util.isTrue(5, 100)) {
                rewardVND = Util.nextInt(3000, 7000);
            } else if (Util.isTrue(1, 100)) {
                rewardVND = Util.nextInt(7000, 12000);
            } else {
                rewardVND = Util.nextInt(12000, 20000);
            }
            player.getSession().vnd += rewardVND;
            PlayerDAO.updateVND(player);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendThongBao(player, "Bạn : " + player.name + " \nđã nhận được " + rewardVND + " VND \ntừ Lixi Lớn!");
//            CombineService.gI().sendEffectOpenItem(player, item.template.iconID, (short) -1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void bancavip(Player player, Item item) {
        if (player != null && item != null) {
            int randomRate = Util.nextInt(1, 100);
            int rewardVND;
            if (Util.isTrue(99, 100)) {
                rewardVND = Util.nextInt(500, 20000);
            } else if (Util.isTrue(10, 100)) {
                rewardVND = Util.nextInt(1500, 3000);
            } else if (Util.isTrue(5, 100)) {
                rewardVND = Util.nextInt(3000, 7000);
            } else if (Util.isTrue(1, 100)) {
                rewardVND = Util.nextInt(7000, 12000);
            } else {
                rewardVND = Util.nextInt(1, 500);
            }
            player.getSession().vnd += rewardVND;
            PlayerDAO.updateVND(player);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Bán Cá VIP Nhận Ramdom Số Tiền Bán Cá : " + rewardVND + " Coin");
//            CombineService.gI().sendEffectOpenItem(player, item.template.iconID, (short) -1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể mở vật phẩm, vui lòng thử lại.");
        }
    }

    private void ruongthanbi1sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 1;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi2sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(Util.nextInt(68, 71), 1));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(Util.nextInt(68, 71), 1));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(Util.nextInt(68, 71), 1));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(Util.nextInt(68, 71), 1));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(Util.nextInt(68, 71), 1));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(500, 1000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 8;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi3sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(1000, 3500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 50;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi4sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(3000, 4500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 450;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi5sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(4000, 5500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 2500;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi6sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(103,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(77,  Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 6500)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 17000;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongthanbi8sao(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {814, 815, 816, 817, 1041, 1092 // Phụ Kiện
                ,
                 1810, 1811, 1812, 1813 // Chân Mệnh
                ,
                 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 977 // cải trang
                ,
                 1166, 1167, 1156, 1157, 1142, 1143 // pét
                ,
                 346, 347, 348, 349, 350, 351 // thú cuõi
            };

            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            //   icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 82) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,  Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 200)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 23) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }
            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(59, 0));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(5000, 10000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(231, Util.nextInt(1, 2)));
            }

            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.point_moruong += 120000;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    private void ruongpokemon(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1764, 1765, 1766, 1767, 1768, 1769, 1770, 1771, 1772, 1113, 1114};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
//            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.type == 11) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 5000)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 25)));
                if (Util.isTrue(99, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
            }

            if (it.template.type == 21) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(77,Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 5000)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 25)));
                if (Util.isTrue(99, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
            }

            if (it.template.type == 5) {
                it.itemOptions.add(new ItemOption(248, 2025));
                it.itemOptions.add(new ItemOption(50,Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(103,Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 5000)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 25)));
                if (Util.isTrue(99, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }

            }
            InventoryService.gI().addItemBag(pl, it, 999999999);
            icon[1] = it.template.iconID;
            //  InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            //       pl.point_moruong += 1;
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");

        }
    }

    public void useThoiVang(Player pl, Item it) {
        if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
            Service.gI().sendThongBao(pl, "Vàng sau khi sử dụng vượt quá giới hạn!");
            return;
        }
        pl.inventory.gold += 100000000000l;
        Service.gI().sendThongBao(pl, "Bạn nhận được 100 Tỷ vàng");
        InventoryService.gI().subQuantityItemsBag(pl, it, 100);
        InventoryService.gI().sendItemBag(pl);
        Service.gI().sendMoney(pl);
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {

        if (pl.idNRNM != -1) {
            Service.gI().sendThongBao(pl, "Không thể mang ngọc rồng này lên Phi thuyền");
            Service.gI().hideWaitDialog(pl);
            return;
        }

        int zoneId = -1;
        if (index > pl.mapCapsule.size() - 1 || index < 0) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            Service.gI().hideWaitDialog(pl);
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        // Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (index != 0 || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        pl.changeMapVIP = true;
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    public void eatPea(Player player) {
        if (!Util.canDoWithTime(player.lastTimeEatPea, 1000)) {
            return;
        }
        player.lastTimeEatPea = System.currentTimeMillis();
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            long hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cám ơn sư phụ");
            }

            InventoryService.gI().subQuantityItemsBag(player, pea, 1);
            InventoryService.gI().sendItemBag(player);
        }
    }

    private void setkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(5, 500))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(5, 15000)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(10, 750)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(20, 15000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 3)));

                    break;
            }
            if (Util.isTrue(100, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 129), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(130, 132), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(133, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(127, 135), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void setkichhoatPRO(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(59, Util.nextInt(5, 500))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(5, 50000))); // Chỉ số ngẫu nhiên
                    //  it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(59, Util.nextInt(5, 500))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(5, 500000)));
                    //  it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(59, Util.nextInt(5, 500))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(10, 5000)));
                    //   it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(59, Util.nextInt(5, 500))); // Chỉ số ngẫu nhiên
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(20, 500000)));
                    //    it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(59, Util.nextInt(5, 500)));
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));
                    //    it.itemOptions.add(new ItemOption(107, Util.nextInt(1, 18)));

                    break;
            }
            if (Util.isTrue(12, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(75, 1));
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1));
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(75, 1));
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1));
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(75, 1));
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1));
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(75, 1));
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1));
                    it.itemOptions.add(new ItemOption(30, 1)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void setkichhoatthanlinh(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);

            // Thêm chỉ số dựa trên loại item
            switch (it.template.type) {
                case 0: // Áo
                    it.itemOptions.add(new ItemOption(47, Util.nextInt(10000000, 20000000))); // Chỉ số ngẫu nhiên

                    break;
                case 1: // Quần
                    it.itemOptions.add(new ItemOption(6, Util.nextInt(100000, 100000000)));

                    break;

                case 2: // Găng
                    it.itemOptions.add(new ItemOption(0, Util.nextInt(100000, 10000000)));

                    break;
                case 3: // Giày
                    it.itemOptions.add(new ItemOption(7, Util.nextInt(100000, 100000000)));

                    break;
                case 4: // Rada
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 50)));

                    break;
            }
            if (Util.isTrue(100, 100)) {
                if (it.template.gender == 0) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(107, 30)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 1) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(107, 30)); // Chỉ số đặc biệt 
                }
                if (it.template.gender == 2) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(107, 30)); // Chỉ số đặc biệt 

                }
                if (it.template.gender == 3) {
                    it.itemOptions.add(new ItemOption(Util.nextInt(51, 56), 1)); // Chỉ số đặc biệt
                    it.itemOptions.add(new ItemOption(107, 30)); // Chỉ số đặc biệt 

                }

            }
            // Thêm vào túi đồ
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
            CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: // skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: // skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: // skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cám ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemManhGiay(Player pl, Item item) {
        if (pl.winSTT && !Util.isAfterMidnight(pl.lastTimeWinSTT)) {
            Service.gI().sendThongBao(pl, "Hãy gặp thần mèo Karin để sử dụng");
            return;
        } else if (pl.winSTT && Util.isAfterMidnight(pl.lastTimeWinSTT)) {
            pl.winSTT = false;
            pl.callBossPocolo = false;
            pl.zoneSieuThanhThuy = null;
        }
        NpcService.gI().createMenuConMeo(pl, item.template.id, 564,
                "Đây chính là dấu hiệu riêng của...\nĐại Ma Vương Pôcôlô\nĐó là một tên quỷ dữ đội lốt người, một kẻ đại gian ác\ncó sức mạnh vô địch và lòng tham không đáy...\nĐối phó với hắn không phải dễ\nCon có chắc chắn muốn tìm hắn không?",
                "Đồng ý", "Từ chối");
    }

    private void ItemSieuThanThuy(Player pl, Item item) {
        long tnsm = 5_000_000;
        int n = 0;
        switch (item.template.id) {
            case 727:
                n = 2;
                break;
            case 728:
                n = 10;
                break;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBag(pl);
        if (Util.isTrue(50, 100)) {
            Service.gI().sendThongBao(pl, "Bạn đã bị chết vì độc của thuốc tăng lực siêu thần thủy.");
            pl.setDie();
        } else {
            for (int i = 0; i < n; i++) {
                Service.gI().addSMTN(pl, (byte) 2, tnsm, true);
            }
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id)
                .findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                    .filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ "
                        + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBag(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        }
    }

    public void useGokuDay(Player pl, Item it) {
        int[] idpet1 = {1375, 1376, 1377, 1479, 1484, 1789}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(Util.nextInt(67, 68), 25));
        item1.itemOptions.add(new ItemOption(59, 25));
        item1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 999)));
        item1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 999)));
        item1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 999)));
        item1.itemOptions.add(new ItemOption(60, 25));
        item1.itemOptions.add(new ItemOption(249, 1));
        item1.itemOptions.add(new ItemOption(61, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(251, 1));
        item1.itemOptions.add(new ItemOption(231, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
        InventoryService.gI().sendItemBag(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được Pét!");
    }

    public void useGokuDayVip(Player pl, Item it) {
        int[] idpet1 = {1375, 1376, 1377, 1479, 1484, 1789}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(Util.nextInt(67, 68), 25));
        item1.itemOptions.add(new ItemOption(59, 25));
        item1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 2000)));
        item1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 2000)));
        item1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 2000)));
        item1.itemOptions.add(new ItemOption(60, 25));
        item1.itemOptions.add(new ItemOption(249, 1));
        item1.itemOptions.add(new ItemOption(61, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(251, 1));
        item1.itemOptions.add(new ItemOption(231, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
        InventoryService.gI().sendItemBag(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được Pét!");
    }

    public void usecaitrangbase(Player pl, Item item) {
        short[] listItem = new short[]{575, 576, 577, 578, 448, 449, 450, 451, 452, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(2, 40);
        it.addOptionParam(50, param);
        it.addOptionParam(77, param);
        it.addOptionParam(103, param);
        it.addOptionParam(101, Util.nextInt(1, 10));
        it.addOptionParam(210, Util.nextInt(1, 4));
        it.addOptionParam(72, Util.nextInt(1, 4));
        it.addOptionParam(106, 1);

        if (Util.isTrue(98, 100)) {
            it.addOptionParam(93, Util.nextInt(2, 7));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void usevithu(Player pl) {
        short[] listItem = new short[]{1195, 1196, 1197, 1198, 1199, 1200, 1201, 1202, 1203};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 1);

        int param = Util.nextInt(2, 99);
        it.addOptionParam(Util.nextInt(67, 71), Util.nextInt(1, 99));
        it.addOptionParam(59, Util.nextInt(1, 99));
        it.addOptionParam(50, 9999);
        it.addOptionParam(77, 9999);
        it.addOptionParam(103, 9999);
        it.addOptionParam(60, Util.nextInt(1, 99));
        it.addOptionParam(Util.nextInt(149, 152), 0);
        it.addOptionParam(61, Util.nextInt(1, 20));
        it.addOptionParam(Util.nextInt(153, 157), 0);
        if (Util.isTrue(20, 100)) {
            it.addOptionParam(107, Util.nextInt(1, 18));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void usengocrong7sao(Player pl, Item item) {
        short[] listItem = new short[]{20};
        short idItem = listItem[Util.nextInt(listItem.length)];
        Item it = ItemService.gI().createNewItem(idItem, 99);

        int param = Util.nextInt(2, 15);
//        it.addOptionParam(50, param);
//        it.addOptionParam(77, param);
//        it.addOptionParam(103, param);
//         it.addOptionParam(5, Util.nextInt(1, 10));
//        it.addOptionParam(237, Util.nextInt(1, 2));
        it.addOptionParam(72, Util.nextInt(1, 2));
        it.addOptionParam(30, 1);

        if (Util.isTrue(1, 100)) {
            it.addOptionParam(93, Util.nextInt(2, 7));
        }
        if (InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã nhận được " + it.template.name);
            //   InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBag(pl);
        } else {
            Service.gI().sendThongBao(pl, "Cần ít nhất 1 ô trống trong hành trang!");
        }
    }

    public void hopQuaTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 20) {
            int gender = pl.gender;
            int[] id = {1066, 1067, 1068, 1069, 1070, 194, 441, 442, 443, 444, 445, 446, 447};
            int[] soluong = {9999, 9999, 9999, 9999, 9999, 1, 999, 999, 999, 999, 999, 999, 999};
            int[] option = {30, 30, 30, 30, 30, 73, 95, 96, 73, 98, 99, 100, 101};
            int[] param = {0, 0, 0, 0, 0, 0, 10, 10, 10, 5, 5, 10, 10};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }
            int[] idpet = {1143};
            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, 8888));
            item.itemOptions.add(new ItemOption(77, 8888));
            item.itemOptions.add(new ItemOption(103, 8888));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 8)));
            item.itemOptions.add(new ItemOption(93, 60));
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBag(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");

        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Về Rương Nhà Lấy Ra 30 ô Hành Trang Để Mở");
        }
    }

    public void hopQuajren(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 20) {
            int gender = pl.gender;
            int[] id = {1020, 1021, 1022, 1023, 1024};
            int[] soluong = {1, 1, 1, 1, 1};
            int[] option = {101, 101, 101, 101, 101};
            int[] param = {100, 100, 100, 100, 100};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 6) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }
            int[] idpet = {1024};
            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(101, 100));
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBag(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");

        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Về Rương Nhà Lấy Ra 30 ô Hành Trang Để Mở");
        }
    }

    public void hopQuaTanThu1(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 5) {
            int[] id = {1190, 1191, 1192, 1193};
            int[] soluong = {50, 50, 50, 50};
            int[] option = {50, 77, 103, 101};
            int[] param = {5, 5, 5, 5};

            for (int i = 0; i < id.length; i++) {
                Item item = ItemService.gI().createNewItem((short) id[i]);
                item.quantity = soluong[i];
                item.itemOptions.add(new ItemOption(option[i], param[i]));
                InventoryService.gI().addItemBag(pl, item, 0);
            }
        } else {
            Service.getInstance().sendThongBaoFromAdmin(pl, "Về Rương Nhà Lấy Ra 5 ô Hành Trang Để Mở");
        }
    }

    public void quatop1(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1159}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 35));
        item1.itemOptions.add(new ItemOption(77, 35));
        item1.itemOptions.add(new ItemOption(103, 35));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void ComfirmMocSoXuMenh(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            Item itemqua4;
            Item itemqua5;
            try {
                int time = 10;
                if (player.point_PassFree >= 50000 && player.SoXuMenhDaNhan == 0) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 50K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 50000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 100));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);

                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 100000 && player.SoXuMenhDaNhan == 50000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 100K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 100000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 200));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 200000 && player.SoXuMenhDaNhan == 100000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 200K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 200000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 300));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 300000 && player.SoXuMenhDaNhan == 200000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 300K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 300000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 400));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 500000 && player.SoXuMenhDaNhan == 300000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 500K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 500000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 500));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 1000000 && player.SoXuMenhDaNhan == 500000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 1M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 1000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 700));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 2000000 && player.SoXuMenhDaNhan == 1000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 2M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 2000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 1000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 3000000 && player.SoXuMenhDaNhan == 2000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 3M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 3000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 1200));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 5000000 && player.SoXuMenhDaNhan == 3000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 5M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 5000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 1500));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 7000000 && player.SoXuMenhDaNhan == 5000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 7M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 7000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 1700));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 10000000 && player.SoXuMenhDaNhan == 7000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 10M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 10000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 2000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 12000000 && player.SoXuMenhDaNhan == 10000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 12M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 12000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 2200));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 15000000 && player.SoXuMenhDaNhan == 12000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 15M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 15000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 2500));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassFree >= 17000000 && player.SoXuMenhDaNhan == 15000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 17M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 17000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 3000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 20000000 && player.SoXuMenhDaNhan == 17000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 20M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 20000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 4000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 22000000 && player.SoXuMenhDaNhan == 20000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 22M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 22000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 5000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassFree >= 25000000 && player.SoXuMenhDaNhan == 22000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 25M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 25000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 5000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 28000000 && player.SoXuMenhDaNhan == 25000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 28M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 28000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 6000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassFree >= 30000000 && player.SoXuMenhDaNhan == 28000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 30M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 30000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 7000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassFree >= 35000000 && player.SoXuMenhDaNhan == 30000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh 35M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhan = 35000000;
                    itemqua = ItemService.gI().createNewItem((short) 1588, 10);
                    itemqua.itemOptions.add(new ItemOption(239, 10000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 457, 100000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 5);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else {
                    Service.gI().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
        }
    }

    public void ComfirmMocSoXuMenhVIP(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            Item itemqua4;
            Item itemqua5;
            try {
                int time = 10;
                if (player.point_PassVIP >= 50000 && player.SoXuMenhDaNhanvip == 0) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 50K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 50000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 1000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);

                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 100000 && player.SoXuMenhDaNhanvip == 50000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh  VIP 100K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 100000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 2000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 200000 && player.SoXuMenhDaNhanvip == 100000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 200K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 200000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 3000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 300000 && player.SoXuMenhDaNhanvip == 200000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 300K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 300000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 4000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 500000 && player.SoXuMenhDaNhanvip == 300000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 500K Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 500000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 5000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 1000000 && player.SoXuMenhDaNhanvip == 500000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 1M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 1000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 6000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 2000000 && player.SoXuMenhDaNhanvip == 1000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 2M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 2000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 7000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 3000000 && player.SoXuMenhDaNhanvip == 2000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 3M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 3000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 8000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 5000000 && player.SoXuMenhDaNhanvip == 3000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 5M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 5000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 9000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 7000000 && player.SoXuMenhDaNhanvip == 5000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 7M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 7000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 10000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 10000000 && player.SoXuMenhDaNhanvip == 7000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 10M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 10000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 11000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 12000000 && player.SoXuMenhDaNhanvip == 10000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 12M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 12000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 12000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 15000000 && player.SoXuMenhDaNhanvip == 12000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 15M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 15000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 13000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassVIP >= 17000000 && player.SoXuMenhDaNhanvip == 15000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 17M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 17000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 14000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short) 1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 20000000 && player.SoXuMenhDaNhanvip == 17000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 20M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 20000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 15000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 22000000 && player.SoXuMenhDaNhanvip == 20000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 22M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 22000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 16000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassVIP >= 25000000 && player.SoXuMenhDaNhanvip == 22000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 25M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 25000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 17000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 28000000 && player.SoXuMenhDaNhanvip == 25000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 28M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 28000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 18000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else if (player.point_PassVIP >= 30000000 && player.SoXuMenhDaNhanvip == 28000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 30M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 30000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 19000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);
                } else if (player.point_PassVIP >= 35000000 && player.SoXuMenhDaNhanvip == 30000000) {
                    Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Sổ Xứ Mệnh VIP 35M Điểm\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.gI().sendThongBao(player, "|6|Xin Chờ\nNhân Viên Giao Hàng\nTrong " + time + " giây");
                    }
                    player.SoXuMenhDaNhanvip = 35000000;
                    itemqua = ItemService.gI().createNewItem((short) 1587, 10);
                    itemqua.itemOptions.add(new ItemOption(240, 20000));
                    itemqua.itemOptions.add(new ItemOption(30, 1));

                    itemqua1 = ItemService.gI().createNewItem((short)  1271, 1000000);
                    itemqua1.itemOptions.add(new ItemOption(30, 1));

                    itemqua2 = ItemService.gI().createNewItem((short) 77, 1000000);
                    itemqua2.itemOptions.add(new ItemOption(30, 1));

                    itemqua3 = ItemService.gI().createNewItem((short) 861, 1000000);
                    itemqua3.itemOptions.add(new ItemOption(30, 500));

                    itemqua4 = ItemService.gI().createNewItem((short) 1224, 50);
                    itemqua4.itemOptions.add(new ItemOption(30, 500));

                    Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Nhận Mốc Ok");
                    InventoryService.gI().addItemBag(player, itemqua, 10);
                    InventoryService.gI().addItemBag(player, itemqua1, 99999999999l);
                    InventoryService.gI().addItemBag(player, itemqua2, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua3, 2000000000);
                    InventoryService.gI().addItemBag(player, itemqua4, 2000000000);
                    InventoryService.gI().sendItemBag(player);

                } else {
                    Service.gI().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
        }
    }

    public void quatop2(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1158}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 30));
        item1.itemOptions.add(new ItemOption(77, 30));
        item1.itemOptions.add(new ItemOption(103, 30));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop3(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1214}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(244, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1540, 1541}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 25));
        item1.itemOptions.add(new ItemOption(77, 25));
        item1.itemOptions.add(new ItemOption(103, 25));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    //1453
    public void hpqua1353(Player pl, Item it) {
        int[] idpet1 = {1617, 1618, 1638, 1040, 1107}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(Util.nextInt(67, 68), 25));
        item1.itemOptions.add(new ItemOption(59, 25));
        item1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 5000)));
        item1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5000)));
        item1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5000)));
        item1.itemOptions.add(new ItemOption(60, 25));
        item1.itemOptions.add(new ItemOption(249, 1));
        item1.itemOptions.add(new ItemOption(61, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(251, 1));
        item1.itemOptions.add(new ItemOption(231, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
        InventoryService.gI().sendItemBag(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được Pét!");
    }

    public void quatop4(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 50));
        item1.itemOptions.add(new ItemOption(77, 50));
        item1.itemOptions.add(new ItemOption(103, 50));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop5(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        item.itemOptions.add(new ItemOption(93, 90));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 40));
        item1.itemOptions.add(new ItemOption(77, 40));
        item1.itemOptions.add(new ItemOption(103, 40));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop6(Player pl, Item it) {
        // Danh sách ID pet đầu tiên (ví dụ có thể là thú cưỡi, thú cưng...)
        int[] idpet = {1883}; // thêm ID tại đây
        Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
        item.itemOptions.add(new ItemOption(240, 3));
        item.itemOptions.add(new ItemOption(93, 30));
        InventoryService.gI().addItemBag(pl, item, 0);

        // Danh sách ID pet/thú/đồ đặc biệt thứ hai
        int[] idpet1 = {1817, 1818}; // thêm ID tại đây
        Item item1 = ItemService.gI().createNewItem((short) idpet1[Util.nextInt(0, idpet1.length - 1)]);
        item1.itemOptions.add(new ItemOption(50, 30));
        item1.itemOptions.add(new ItemOption(77, 30));
        item1.itemOptions.add(new ItemOption(103, 30));
        item1.itemOptions.add(new ItemOption(72, Util.nextInt(1, 2)));
        item1.itemOptions.add(new ItemOption(107, 8));
        item1.itemOptions.add(new ItemOption(210, 3));
        InventoryService.gI().addItemBag(pl, item1, 0);

        // Trừ vật phẩm mở hộp chỉ 1 lần duy nhất
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);

        // Gửi lại túi đồ sau khi thêm item
        InventoryService.gI().sendItemBag(pl);

        // Thông báo
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng TOP!");
    }

    public void quatop7(Player pl, Item it) {
        Item item = ItemService.gI().createNewItem((short) 1150, Util.nextInt(1, 50000));
        InventoryService.gI().addItemBag(pl, item, 0);
        InventoryService.gI().subQuantityItemsBag(pl, it, 1);
        InventoryService.gI().sendItemBag(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được phần thưởng Tích Lũy Nạp!");
    }

    public void openRuongSPLVIP(Player pl, Item item) {
        int idtemp = 1263;
        if (Util.isTrue(50, 100)) {
            int[] lst = {1724, 1725, 1726, 1727, 1728, 1729, 1730, 1733, 1734, 1735, 1736, 1737, 1738, 1739};
            idtemp = lst[Util.nextInt(lst.length)];
        }
        Item it = ItemService.gI().createNewItem((short) idtemp);
        if (idtemp >= 1724 && idtemp <= 1730) {
            it.addOptionParam(idtemp - 1724 + 95, (idtemp == 1727 || idtemp == 1728) ? 3 : 5);
        } else if (idtemp >= 1733 && idtemp <= 1739) {
            it.addOptionParam(idtemp - 1733 + 95, (idtemp == 1736 || idtemp == 1737) ? 4 : 8);
        }
        it.addOptionParam(30, 1);
        if (!InventoryService.gI().addItemBag(pl, it, 999999)) {
            Service.gI().sendThongBao(pl, "Bạn không đủ ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        Service.gI().sendThongBao(pl, "Bạn nhận được " + it.template.name);
        InventoryService.gI().sendItemBag(pl);

    }
}
