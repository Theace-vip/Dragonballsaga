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

public class SUKIEN365 extends Npc {

    public SUKIEN365(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SỰ KIỆN MUA HÈ\n"
                            + "|6|NGHỈ HÈ GIẢI KHÁT"
                            + "Up quái Nhẫu Nhiên Thu Thập Được Đá Viên + Khúc Mía\n"
                            + "|7|Thu Thập Đủ 999 Đá Viên + 500 Khúc Mía + 5000 zenni\n"
                            + "|4|Ép Mía Ngẫu Nhiên Nhận Được 1 Trong 3 Cốc Nước Mía\n"
                            + "+Cốc Nước Mía Khổng Lồ : Uống Tăng 40% ChíMạng Trong 10P\n"
                            + "+Cốc Nước Mía Thơm     : Uống Tăng 40% Né Đòn Trong 10P\n"
                            + "+Cốc Nước Mía Sầu Riêng: Uống Tăng 200% SD,HP,KI Trong 10P\n"
                            + "Yêu Cầu: Mặc Cải Trang Nữ Thần Mùa Hè Để Up Nguyên liệu\n"
                            + "|7|Điểm Sự Kiện " + Util.FormatNumber(player.sukien) + " Điểm",
                            "Ép Nước Mía",
                            "Cửa Hàng\nNguyên Liệu",
                            "Cửa Hàng\nỦng Hộ",
                            "Nhận CảiTrang\n Sự Kiện"
                           
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
                    case 5 -> {
                        if (select == 0) {

                            Item cucda = null;
                            Item khucmia = null;
                            Item zenni = null;
                            try {
                                cucda = InventoryService.gI().findItemBag(player, 1229);
                                khucmia = InventoryService.gI().findItemBag(player, 1230);
                                zenni = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (cucda == null
                                    || khucmia == null
                                    || zenni == null
                                    || cucda.quantity < 999
                                    || khucmia.quantity < 500
                                    || zenni.quantity < 5000) {
                                this.npcChat(player, "Bạn Cần x999 Cục Đá + 500 Khúc Mía + 5000 zenni");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, cucda, 999);
                                InventoryService.gI().subQuantityItemsBag(player, khucmia, 500);
                                InventoryService.gI().subQuantityItemsBag(player, zenni, 5000);

                                Item cocnuocmia = ItemService.gI().createNewItem((short) Util.nextInt(1226, 1228));
                                cocnuocmia.itemOptions.add(new ItemOption(73, 2024));
                                
                                InventoryService.gI().addItemBag(player, cocnuocmia, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + cocnuocmia.template.name);
                                
                                
                                player.sukien += 1;
                               Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
                            }
                            break;

                        }
//                        if (select == 1) {
//
//                            Item cogiaiphong = null;
//                            Item thoivang = null;
//                            try {
//                                cogiaiphong = InventoryService.gI().findItemBag(player, 1016);
//                                thoivang = InventoryService.gI().findItemBag(player, 457);
//                            } catch (Exception e) {
//                            }
//                            if (cogiaiphong == null
//                                    || thoivang == null
//                                    || cogiaiphong.quantity < 1
//                                    || thoivang.quantity < 500) {
//                                this.npcChat(player, "Bạn Cần x1 Cờ Giai Phóng + 500 zenni");
//                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
//                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
//
//                            } else {
//                                InventoryService.gI().subQuantityItemsBag(player, cogiaiphong, 1);
//                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 500);
//                                
//                                Item huyhieu = ItemService.gI().createNewItem((short) 1018);
//                                huyhieu.itemOptions.add(new ItemOption(244, 0));
//                               // huyhieu.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
//                                InventoryService.gI().addItemBag(player, huyhieu, 99999999);
//                                InventoryService.gI().sendItemBag(player);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + huyhieu.template.name);
//                                
//
//                                Item caitrang = ItemService.gI().createNewItem((short) 1014);
//                                caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
//                                caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 50)));
//                                caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 50)));
//                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
//                                caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 5)));
//                                caitrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
//                                if (Util.isTrue(99, 100)) {
//                                    caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 10)));
//                                }
//                                InventoryService.gI().addItemBag(player, caitrang, 99999999);
//                                InventoryService.gI().sendItemBag(player);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrang.template.name);
//
//                                player.sukien += 1;
//                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 1 Điểm Sự Kiện");
//                            }
//
//                            break;
//
//                        }
                        if (select == 1) {
                          ShopService.gI().opendShop(player, "NGUYENLIEU", false);

                        }
                        if (select == 2) {
                          ShopService.gI().opendShop(player, "HUYHIEU", false);
                        }
                        if (select == 3) {
                              
                            Item thoivang = null;
                            try {
                               
                                thoivang = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (thoivang == null
                                    || thoivang.quantity < 1) {
                                this.npcChat(player, "Bạn Cần x1 ZENNI");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                               
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 1);

                                Item caitrang = ItemService.gI().createNewItem((short) 1046);
                                caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 2)));
                                caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                caitrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
                                caitrang.itemOptions.add(new ItemOption(93, 7));
                             
                                InventoryService.gI().addItemBag(player, caitrang, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrang.template.name);
                            
                            }

                            break;
                        }
                        if (select == 4) {
                          Service.gI().showListTop(player, Manager.Topsk);

                        }
                    }

                   
                    }
                }
            }
        }
    }


