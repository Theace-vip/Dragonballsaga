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
import shop.ShopService;
import utils.Util;

public class SKMabu extends Npc {

    public SKMabu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 44 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SỰ KIỆN SĂN MABU\n"
                            + "|6|TIÊU DIỆT MABU TẠI MAP ĐẠI HỘI VÕ THUẬT\n"
                            + "|7|THU THẬP MẢNH SOCOLA MABU X999 CÁI\n"
                            + "|6|GHÉP NGẪU NHIÊN RA 4 LOẠI ĐỆ TỬ VIP\n"
                            + "KID MABU : TĂNG HỢP THỂ 100% SỨC MẠNH ĐỆ\n"
                            + "KID FIDE : TĂNG HỢP THỂ 50% SỨC MẠNH ĐỆ\n"
                            + "KID Uub : TĂNG HỢP THỂ 70% SỨC MẠNH ĐỆ\n"
                            + "KID XÊNCON : TĂNG HỢP THỂ 30% SỨC MẠNH ĐỆ\n"
                            + "|4|ĐUA TOP SỰ KIÊN NHẬN SÉT KH MABU 80% CHÍ MẠNG\n"
                            + "|6|CÓ THỂ MUA NHANH VỚI GIÁ 500k CỎ/2000 MẢNH NHẬN 100Đ SK\n",
                        //    + "|7|Điểm Sự Kiện " + Util.FormatNumber(player.sukienmabu) + " Điểm",
                            "Mua Mảnh",
                            "Cửa Hàng",
                          //  "Nhận 200k Ngọc xanh",
                            "Đến Map Mabu"
                        
                    );

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
                    case 44 -> {
                        if (select == 0) {
                            Item zenni = null;
                            try {
                             
                                zenni = InventoryService.gI().findItemBag(player, 1150);
                            } catch (Exception e) {
                            }
                            if (
                                    zenni == null
                                    || zenni.quantity < 500000) {
                                this.npcChat(player, "Bạn Cần 500k cỏ 4 lá");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                              
                                InventoryService.gI().subQuantityItemsBag(player, zenni, 500000);

                                Item cocnuocmia = ItemService.gI().createNewItem((short) 1212,2000);
                                cocnuocmia.itemOptions.add(new ItemOption(73, 2024));

                                InventoryService.gI().addItemBag(player, cocnuocmia, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + cocnuocmia.template.name);

//                                player.sukienmabu += 100;
                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 100 Điểm Sự Kiện");
                            }
                            break;

                        }

                        if (select == 1) {
                              ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);

                        }
//                        if (select == 2) {
//                               if (player.inventory.gem >= 200000) {
//                            this.npcChat(player, "Tham Lam");
//                            break;
//                        }
//                        player.inventory.gem = 200000;
//                        Service.getInstance().sendMoney(player);
//                        Service.getInstance().sendThongBao(player, "|1|Bạn vừa nhận được 200k Ngọc xanh");
//                        }
                        if (select == 2) {
                              ChangeMapService.gI().changeMap(player, 168, -1, 783, 312);

                        }
                        
                        if (select == 4) {
                            Service.gI().showListTop(player, Manager.Topsanbu);

                        }
                    }

                }
            }
        }
    }
}
