package npc.npc_manifest;

/**
 *
 */
import Mail.HomThuService;
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.LeoThapNe.LeoThapNe;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;
import shop.ShopService;

public class LeoThap extends Npc {

    public LeoThap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          
            if (player.zone.map.mapId == 0) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|˚₊· ͟͟͞͞➳❥Bí Cảnh Tháp\n"
                        + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                        + "Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                        + "Đã Leo Được: " + player.tangThap + " Tầng"
                      ,
                        "Tham Gia","Shop\nBí Cảnh","Xem\nBảng Top", "Đóng");
            } else if (player.zone.map.mapId == 145 && player.tangThap == 0) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Bí Cảnh Tháp\n"
                        + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                        + "Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                        + "Đã Leo Được: " + player.tangThap + " Tầng"
                       ,
                        "Hướng Dẫn", "Xem Top", "Đóng");

            } else if (player.zone.map.mapId == 145 && player.tangThap > 0) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Bí Cảnh Tháp\n"
                        + "|4|Điểm Hạ Boss LV: " + player.pointThap + " Điểm\n"
                        + " Level Boss: " + player.levelThap + " Đang Thách Đấu\n"
                        + " Đã Leo Được: " + player.tangThap + " Tầng"
                      ,
                        "Đổi Điểm\nBí Cảnh", "SHOP\nBí Cảnh", "Xem\nBảng Top", "Đổi Điểm\nBí Cảnh","Đóng");

            } else {
                super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU:
                    if (player.zone.map.mapId == 0) {
                        if (select == 0) {
                            LeoThapNe.joinMapLeoThap(player);
                        }
                         if (select == 1) {
//                            ShopService.gI().opendShop(player, "SHOP_LEOTHAP", true);
                        }
                          if (select == 2) {
                            Service.getInstance().showListTop(player, Manager.topLeoThap);
                        }
                           if (select == 3) {
                              if (player.pointThap < 1000) {
                                    Service.getInstance().sendThongBao(player, "Bạn cần ít nhất 10 Điểm Bí Cảnh. Để đổi Vật Phẩm");
                                    return;
                                }
                                Item manhnro = InventoryService.gI().findItemBag(player, 457);
                                if (manhnro == null) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                } else {
                                   // InventoryService.gI().subQuantityItemsBag(player, manhnro, 1);
                                    Item nrosieucap = ItemService.gI().createNewItem((short) 1884, 1);
                                    nrosieucap.itemOptions.add(new ItemOption(30, 2024));
                                    InventoryService.gI().addItemMail(player,nrosieucap);
                                    HomThuService.gI().sendListMail(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
                                    player.pointThap -= 10;
                                    Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 10 Điểm Bí Cảnh");
                                }
                                break;
                        }
                         
                    } else if (player.zone.map.mapId == 145 && player.tangThap == 0) {
                        switch (select) {
                            case 0:// cửa hàng
                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                        "|7|Chúc Bạn Năm Mới An Khangღ\n"
                                        + "|5|Đại Chiến 300 Hiệp : Tiêu diệt All Boss Bí Cảnhღ\n"
                                        + "Tiêu Diệt 99 Con Boss LV1-LV99 Hoàn Thành 1 Tầngღ\n"
                                        + "Tầng Bí Cảnh Càng Cao Nhận Càng Nhiều HP KI SD GIÁP CAOღ\n"
                                        + "YÊU CẦU THỰC LỰC TỪ VIP 6 INGAME TRỞ LÊNღ\n"
                                        + "ĐIỂM TIÊU DIỆT DÙNG MUA TRỰC TIẾP ĐỒ TỪ TẦNG THÁP 1 SẼ XUẤT HIỆNღ\n"
                                        + "\nSỐ LƯỢNG TẦNG VÔ HẠN -CỐ LÊN CỐ LÊNღ\n",
                                        "Đóng");

                                break;

                            case 1:// xem top
                                this.createOtherMenu(player, ConstNpc.MENU_LEO_THAP, "|7|Bí Cảnh Thần Thú",
                                        "Top\nTiêu Diệt", "Đóng");
                                break;
                        }
                    } else if (player.zone.map.mapId == 145 && player.tangThap > 0) {
                        switch (select) {

                            case 0:

                                if (player.pointThap < 1000) {
                                    Service.getInstance().sendThongBao(player, "Bạn cần ít nhất 10 Điểm Bí Cảnh. Để đổi Vật Phẩm");
                                    return;
                                }
                                Item manhnro = InventoryService.gI().findItemBag(player, 457);
                                if (manhnro == null) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                                } else {
                                   // InventoryService.gI().subQuantityItemsBag(player, manhnro, 1);
                                    Item nrosieucap = ItemService.gI().createNewItem((short) 1884, 1);
                                    nrosieucap.itemOptions.add(new ItemOption(30, 2024));
                                    InventoryService.gI().addItemMail(player,nrosieucap);
                                    HomThuService.gI().sendListMail(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
                                    player.pointThap -= 10;
                                    Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 10 Điểm Bí Cảnh");
                                }
                                break;
                            case 1:
                                if (player.tangThap < 0) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Cần 0 Tầng Tháp Để Mở");
                                    return;
                                }
                                ShopService.gI().opendShop(player, "SHOP_LEOTHAP", true);
                                break;
                            case 2:// xem top
                                this.createOtherMenu(player, ConstNpc.MENU_LEO_THAP, "|7|\n" + "Bí Cảnh Thần Thú",
                                        "Top\nTiêu Diệt", "Đóng");
                                break;

                        }
                    }
                    break;
                case ConstNpc.MENU_LEO_THAP:
                    switch (select) {
                        case 0: // top ngày
                            Service.getInstance().showListTop(player, Manager.topLeoThap);
                            break;

                    }
                    break;
            }
        }
    }
}
