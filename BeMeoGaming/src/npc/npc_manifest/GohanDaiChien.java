package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import consts.ConstTask;
import item.Item;
import item.Item.ItemOption;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import shop.ShopService;
import utils.Util;

public class GohanDaiChien extends Npc {

    public GohanDaiChien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SỰ KIỆN\n"
                            + "GOHAN PHẢN DIỆN – BÍ ẨN THỜI KHÔNG\n"
                            + "|4|CƠ CHẾ CHƠI:  Tiêu Diệt Và Thu Thập 6 Tinh Hồn GoHan\n"
                            + "|7|Một dòng thời gian bị lỗi đã tạo ra 6 phiên bản Gohan phản diện khác nhau\n"
                            + "|6|Bạn Hãy xuyên không gian, truy lùng và thanh tẩy từng Gohan Phản Diện\n"
                            + "để đánh thức Gohan Chính Nghĩa – vị cứu tinh cuối cùng.\n"
                            + "|7|Kỹ Năng Kẻ Phản Diện Có Và Rất Mạnh:\n"
                            + "|4|Gohan 1: Kẻ Lì Đòn & Khiên Liên Tục\n"
                            + "Gohan 2: Kẻ Sát Thủ & Đấm Liên Tục\n"
                            + "Gohan 3: Kẻ Ám Tiễn & Chưởng liên tục\n"
                            + "Gohan 4: Kẻ Bỉ Ổi & Thôi Miên Liên Tục \n"
                            + "Gohan 5: Kẻ Sát Nhân & 1 Hit Lên Bảng\n"
                            + "Gohan 6: Trùm Cuối & Trâu Bò + OneHit + Hồi Máu\n"
                            + "|7|Điểm Sự Kiện " + Util.FormatNumber(player.sukien) + " Điểm",
                            "Truy Lùng",
                            "Luyện Hồn",
                            "Nhận Đệ\nGohan",
                            "Shop\nUng Hộ" 
                    );

                }

                default ->
                    super.openBaseMenu(player);
            }
        }
    }
//
//    @Override
//    public void confirmMenu(Player player, int select) {
//        if (canOpenNpc(player)) {
//            if (player.iDMark.isBaseMenu()) {
//                switch (mapId) {
//                    case 5 -> {
//                        
//                        
//                        if (select == 0) {
//                        ChangeMapService.gI().goTokhongian(player);
//                    }
//
//                        if (select == 1) {
//                            Item zenni = null;
//                            Item zenni1 = null;
//                            Item zenni2 = null;
//                            Item zenni3 = null;
//                            try {
//
//                                zenni = InventoryService.gI().findItemBag(player, 1150);
//                                 zenni1 = InventoryService.gI().findItemBag(player, 1151);
//                                 zenni2 = InventoryService.gI().findItemBag(player, 1152);
//                                 zenni3 = InventoryService.gI().findItemBag(player, 1153);
//                            } catch (Exception e) {
//                            }
//                            if (zenni == null
//                                  ||zenni == null   ||zenni == null   ||zenni == null  
//                                    
//                                    || zenni.quantity < 500000
//                                     || zenni1.quantity < 500000
//                                     || zenni2.quantity < 500000
//                                     || zenni3.quantity < 500000
//                                    
//                                    ) {
//                                this.npcChat(player, "Bạn Cần 500k cỏ 4 lá");
//                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
//                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
//
//                            } else {
//
//                                InventoryService.gI().subQuantityItemsBag(player, zenni, 500000);
//
//                                Item cocnuocmia = ItemService.gI().createNewItem((short) 1212, 2000);
//                                cocnuocmia.itemOptions.add(new ItemOption(73, 2024));
//
//                                InventoryService.gI().addItemBag(player, cocnuocmia, 99999999);
//                                InventoryService.gI().sendItemBag(player);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + cocnuocmia.template.name);
//
//                                player.sukienmabu += 100;
//                                Service.getInstance().sendThongBao(player, "Bạn Nhận Được 100 Điểm Sự Kiện");
//                            }
//                            break;
//
//                        }
//
//                        if (select == 2) {
//                            ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
//
//                        }
//                        if (select == 3) {
//                            ChangeMapService.gI().changeMap(player, 168, -1, 783, 312);
//
//                        }
//
//                        if (select == 4) {
//                            Service.gI().showListTop(player, Manager.Topsanbu);
//
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//}

 @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 ) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SUKIENXUYENKO, -1,
                                    "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                  +  "|4|Bạn Muốn Truy Lùng Luôn không ?ღ\n"
                                    + "Hãy Thu Thập Tinh Hồn Từ Boss Nhé\n"
                                            + "Xuyên Không Tầm 5s Là Tới\n"
                                    + "|7|" + "Điểm Skien: " + Util.cap(player.sukien) + " Điểmღ\n"
                                     + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    ,
                                    "Đi Luôn",  "Đóng"
                            );
                            break;
                        case 1:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.LUYENHON, -1,
                                     "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    +"|4|Bạn Muốn Luyện Hồn Gohan ?ღ\n"
                                             + "Thu Thập Đủ Mỗi Loại Dị quả 999 Viên Đến Đây\n"
                                             + "999 Fancung -999 các dị quả-50k zenni\n"
                                             + "Ramdom: 6 Đệ Tử Gohan Hợp Thể Từ 10k%-60k%\n"
                                             + "Ramdom: Nhận Rương Cải Trang Gohan Cực Vip 10k%\n"
                                             + "|7|\nLuyện Vip: Nhận Rương Cải Trang Vĩnh Viễn Ramdom 20k%\n"
                                             + "Luyện Vip: Nhận Rương Pét Gohan Phản Diện Cực Vip 20k%\n"
                                     + "|7|" + "Điểm Skien: " + Util.cap(player.sukien) + " Điểmღ\n"
                                     + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    ,
                                    "Luyện Thường", "Luyện Vip", "Đóng"
                            );
                            break;
                        case 2:
                              if (player.SagaChuyenSinh < 999) {
                             Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần 5000 Điểm Sự kiện Để Nhận!");
                                        return;
                                    }
                           NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                    "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                   + "|4|Bạn Muốn Nhận Zenni Vàng ?ღ\n"
                                    + "Chi Phí Mỗi Lần Đổi Từ 100k Điểm TrainFam\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.sukien) + " Điểmღ\n",
                                    "Đóng"
                            );
                            break;
                        case 3:
                             ShopService.gI().opendShop(player, "SUKIEN", false);
                            break;
                      
                             case 4:
                              Service.gI().showListTop(player, Manager.Topsk);
                            break;
                      

                    }
                }
            }
        }
    }
}
