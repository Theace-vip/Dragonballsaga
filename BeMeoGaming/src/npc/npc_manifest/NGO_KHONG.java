package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.ChangeMapService;
import services.func.UseItem;
import shop.ShopService;
import utils.Util;

public class NGO_KHONG extends Npc {

    public NGO_KHONG(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 122 -> {
                    if (player.Saga_VIP < 99) {
                        Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì UPDATE");
                        return;
                    }

                    createOtherMenu(player, ConstNpc.BASE_MENU, "|7|˚₊· ͟͟͞͞➳❥Lão Tôn Gãi Đầu ,Bụng Kêu Rồn Rột\n"
                            + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                            + "|4|Trời Cao Có Mắt mà cũng chẳng cho ta nổi một bữa no bụng...\n"
                            + "|6|Ngươi đấy, tiểutử! Mau đi tiêu diệt lũ yêu khỉ đang giảmạo Ta\n"
                            + "Chúng đang canh giữ những quả Đào Chín\n"
                            + " thứ duy nhất có thể khiến Lão Tôn no bụng lúc này\n"
                            + "Mang về x999 quả cho ta… đổi lại, Cho Người 1 Số Tài Nguyên.\n"
                            + "Nếu ngươi đủ bản lĩnh, ta còn truyền cho một chiêu Thần Thông\n"
                            + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                            + "Điểm Sự Kiện: " + Util.FormatNumber(player.sukien),
                            "X999\nHồng Đào", "X999\nHồng Đào\nChín", "Shop\nNgộ Không", "Đóng");
                }
                case 0 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "|7|\n\"•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                            + "Tham Gia Sự Kiện Ngũ Hành Sơn ?\n"
                            + "|6|Điểm Sự Kiện: " + Util.FormatNumber(player.diemfam),
                            "Tham Gia", "Đóng");

                }
                default ->
                    super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 122 -> {
                        if (select == 0) {
                            Item hongdao = null;
                            try {
                                hongdao = InventoryService.gI().findItemBag(player, 541);
                            } catch (Exception e) {
                            }
                            if (hongdao == null
                                    || hongdao.quantity < 999) {
                                this.npcChat(player, "Bạn Cần x999 Qủa Hồng Đào");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {

                                InventoryService.gI().subQuantityItemsBag(player, hongdao, 999);

                                int[] listitemnhs = {77, 77, 1161, 1161, 1161, 1161, 1161, 1161, 1161, 77, 77, 77, 77, 77, 77, 77};
                                int[] listitemvip = {1295, 1296, 1297, 1298, 1299};
                                int randomId = listitemnhs[Util.nextInt(listitemnhs.length)];
                                int randomIdvip = listitemvip[Util.nextInt(listitemvip.length)];

                                Item itemrac = ItemService.gI().createNewItem((short) randomId, Util.nextInt(1, 10));
                                Item itemthat = ItemService.gI().createNewItem((short) randomIdvip, Util.nextInt(1, 100));

                                // itemrac.itemOptions.add(new ItemOption(30, 0));
                                //   itemthat.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(50, 100)) {
                                    InventoryService.gI().addItemBag(player, itemrac, 999999);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + itemrac.template.name);

                                } else {
                                    InventoryService.gI().addItemBag(player, itemthat, 999999);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + itemthat.template.name);

                                }
                                player.sukien += 1;
                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
                            }
                            break;

                        }

                        if (select == 1) {
                            Item hongdaochin = null;
                            try {
                                hongdaochin = InventoryService.gI().findItemBag(player, 542);
                            } catch (Exception e) {
                            }
                            if (hongdaochin == null
                                    || hongdaochin.quantity < 999) {
                                this.npcChat(player, "Bạn Cần x999 Qủa Hồng Đào Chín");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {

                                InventoryService.gI().subQuantityItemsBag(player, hongdaochin, 999);

                                int[] listitemnhs = {457, 1270, 1271};
                                int[] listitemvip = {729, 730, 731, 732, 742, 607, 608, 609};
                                int randomId = listitemnhs[Util.nextInt(listitemnhs.length)];
                                int randomIdvip = listitemvip[Util.nextInt(listitemvip.length)];

                                Item itemrac = ItemService.gI().createNewItem((short) randomId, 30);
                                Item itemthat = ItemService.gI().createNewItem((short) randomIdvip, 1);

                                itemthat.itemOptions.add(new ItemOption(Util.nextInt(67, 68), 0));
                                itemthat.itemOptions.add(new ItemOption(59, Util.nextInt(1, 2)));
                                itemthat.itemOptions.add(new ItemOption(50, Util.nextInt(100, 2000)));
                                itemthat.itemOptions.add(new ItemOption(77, Util.nextInt(100, 2000)));
                                itemthat.itemOptions.add(new ItemOption(103, Util.nextInt(100, 2000)));
                                itemthat.itemOptions.add(new ItemOption(60, Util.nextInt(1, 2)));
                                itemthat.itemOptions.add(new ItemOption(249, 1));
                                itemthat.itemOptions.add(new ItemOption(61, Util.nextInt(1, 2)));
                                itemthat.itemOptions.add(new ItemOption(251, 1));
                                itemthat.itemOptions.add(new ItemOption(231, 0));
                                // openchinhlai
                                if (Util.isTrue(70, 100)) {
                                    InventoryService.gI().addItemBag(player, itemrac, 999999);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + itemrac.template.name);

                                } else {
                                    InventoryService.gI().addItemBag(player, itemthat, 999999);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + itemthat.template.name);

                                }
                                player.sukien += 1;
                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
                            }
                            break;

                        }

                        if (select == 2) {
                            ShopService.gI().opendShop(player, "NGOKHONGSHOP", false);
                        }

                    }

                    case 0 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 0, Util.nextInt(700, 800), 432);
                        }
                        if (select == 1) {
                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                int evPoint = player.point_event;
                                if (evPoint >= 7999) {
                                    Item HopQua = ItemService.gI().createNewItem((short) 1834, 1);
                                    HopQua.itemOptions.add(new ItemOption(239, 0));
                                    HopQua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    player.point_event -= 7999;
                                    InventoryService.gI().addItemBag(player, HopQua, 1);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được Cải Trang");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Cần 7999 điểm để đổi");
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                            }
                            break;
                        }

                    }
                }
            }
        }
    }

}
