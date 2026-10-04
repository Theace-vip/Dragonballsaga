package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import consts.ConstTask;
import item.Item;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import shop.ShopService;
import utils.Util;

public class EventCauCa extends Npc {

    public EventCauCa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|ĐẾN KHU VỰC CÂU CÁ"
                        + "\n|5|Khu Vực Cho Thuê Câu Cá Tư Bản\n"
                        + "Câu Những Con Cá Lớn Bán Lấy Tiền Tiêu Và 1 Số Phần quà Ngon\n"
                        + "|6|Cần Thủ: Có Thể Câu Trúng Lixi 500Tr Cash\n"
                        + "\n|7|Điểm Cần Thủ : " + Util.FormatNumber(player.point_cauca) + " Điểm"
                        + "\n|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " Cash"
                        + "\n|1|Bạn có chắc muốn đến Khu câu cá ?",
                        "Khu\nCâu cá",
                        "Cửa Hàng\nTrang Bị",
                        "Cửa Hàng\nPhụ Kiện",
                        "Cửa Hàng\nVIP"
                      //  "Nhận Đổi\nGói Tân Thủ"
                );
            } else if (this.mapId == 216) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|NÔNG DÂN"
                        + "\n\n|5|Nơi đây có bán Cần câu, Mồi cho cá và Shop đổi cá"
                        + "\n|3|Đổi Xô cá Xanh cần 4 loại Cá Thường khác nhau"
                        + "\nĐổi Xô cá Vàng cần 4 loại Cá VIP khác nhau"
                        + "\n|7|Điểm Cần Thủ : " + Util.FormatNumber(player.point_cauca) + " Điểm",
                        "Shop\nCần Câu",
                        "Shop\nQuy đổi",
                        "Đổi\nXô cá Xanh",
                        "Đổi\nXô cá Vàng",
                        "Quay về");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            if (player.isPl() && TaskService.gI().getIdTask(player) > ConstTask.TASK_9_0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 216, -1, 480);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào Hãy Làm Đến Nv Xên 8");
                            }
                            break;
                        case 1:
                            ShopService.gI().opendShop(player, "FREE", true);
                            break;

                        case 2:

                            ShopService.gI().opendShop(player, "FREE2", true);
                            break;
                              case 3:

                            ShopService.gI().opendShop(player, "VIP", true);
                            break;
                        case 4:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.DoiTanThu, -1,
                                    "|7|KHU VỰC ĐỔI THƯỞNG"
                                    + "\n|2|Các Gói Đổi Từ Điểm Fam Từ quái"
                                    + "\n|4|Hãy Fam Điểm Từ Các Loại quái Tích Lũy Mang Về Đây"
                                   + "\n|6|Các Gói Hợi Bị Xịn Đó Nha - Hỗ Trợ Tốt Cho Menber"
                                    + "\n|7|Điểm Fam :" + player.diemfam + " Điểm" ,
                                    "Nhận Gói\nĐá NC","Nhận Gói\nChân Mệnh","Nhận Gói\nSao Pha Lê","Nhận Gói\nTài Nguyên","Nhận Gói\nLinh Thú","Nhận Gói\nCải Trang");

                            break;
                        case 5:
                            if (player.Saga_VIP < 99) {
                                Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì UPDATE");
                                return;
                            }
                            NpcService.gI().createBigMessage(player, avartar, "Ấn Mở Để Xem Lịch Sử Mua Hàng\b"
                                    + "|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " Coin",
                                    (byte) 1,
                                    "Mở",
                                    "https://dragonballsaga.vn/lichsu_mua.txt");
                            break;
                    }
                }
            } else if (this.mapId == 216) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
//                            if (player.Saga_VIP < 99) {
//                                Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì UPDATE");
//                                return;
//                            }
                            ShopService.gI().opendShop(player, "CAN_CAU", true);
                            break;
                        case 1:
                            if (player.Saga_VIP < 99) {
                                Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì UPDATE");
                                return;
                            }
                            ShopService.gI().opendShop(player, "DOI_CA", true);
                            break;
                        case 2: {
                            Item cathuong = null;
                            Item cathuong1 = null;
                            Item cathuong2 = null;
                            Item cathuong3 = null;
                            try {
                                cathuong = InventoryService.gI().findItemBag(player, 13064);
                                cathuong1 = InventoryService.gI().findItemBag(player, 13605);
                                cathuong2 = InventoryService.gI().findItemBag(player, 13660);
                                cathuong3 = InventoryService.gI().findItemBag(player, 13670);
                            } catch (Exception e) {
//                                        throw new RuntimeException(e);
                            }
                            if (cathuong == null || cathuong.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá trích");
                                return;
                            }
                            if (cathuong1 == null || cathuong1.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá đường xanh");
                                return;
                            }
                            if (cathuong2 == null || cathuong2.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá Green");
                                return;
                            }
                            if (cathuong3 == null || cathuong3.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá Blue");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, cathuong, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong1, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong2, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong3, 1);
                                Service.getInstance().sendMoney(player);
                                Item xoca = ItemService.gI().createNewItem((short) 1005);
                                InventoryService.gI().addItemBag(player, xoca, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                this.npcChat(player, "|4|Bạn nhận được Xô cá Xanh");
                            }
                            break;
                        }
                        case 3: {
                            Item cathuong = null;
                            Item cathuong1 = null;
                            Item cathuong2 = null;
                            Item cathuong3 = null;
                            try {
                                cathuong = InventoryService.gI().findItemBag(player, 13068);
                                cathuong1 = InventoryService.gI().findItemBag(player, 13609);
                                cathuong2 = InventoryService.gI().findItemBag(player, 13700);
                                cathuong3 = InventoryService.gI().findItemBag(player, 13071);
                            } catch (Exception e) {
//                                        throw new RuntimeException(e);
                            }
                            if (cathuong == null || cathuong.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá Clownfish");
                                return;
                            }
                            if (cathuong1 == null || cathuong1.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá heo");
                                return;
                            }
                            if (cathuong2 == null || cathuong2.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá voi");
                                return;
                            }
                            if (cathuong3 == null || cathuong3.quantity < 1) {
                                this.npcChat(player, "Bạn chưa có Cá Moorish");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, cathuong, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong1, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong2, 1);
                                InventoryService.gI().subQuantityItemsBag(player, cathuong3, 1);
                                Service.getInstance().sendMoney(player);
                                Item xoca = ItemService.gI().createNewItem((short) 1006);
                                InventoryService.gI().addItemBag(player, xoca, 9999999);
                                InventoryService.gI().sendItemBag(player);
                                this.npcChat(player, "|4|Bạn nhận được Xô cá Vàng");
                            }
                            break;
                        }
                        case 4:
                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 455);//con đường rắn độc
                    }
                }
            }
        }

    }
}
