package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import consts.ConstTask;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import services.func.UseItem;
import shop.ShopService;
import utils.Util;

public class EventSoXuMenh extends Npc {

    public EventSoXuMenh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 14 || this.mapId == 7 || this.mapId == 0) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|SỔ XỨ MỆNH MÙA S1"
                        + "\n|5|Tích Lũy Săn Boss Kiếm Điểm\n"
                        + "Nhận Những Mốc Mùa giải Bên Dưới\n"
                        + "|6|Free Nhận Mốc: Sách Ép Từ 100%-10k%- 1 Số Vật Phẩm Khác\b"
                        + "|6|VIP Nhận Mốc: Đá Khảm Từ 1k%-100k%-1 Số Vật Phẩm Khác\n"
                                + "|2|[Đặc Biệt: VIP Nhận 1000.000 Lượng Bạc/Mốc]"
                        + "\n|7|Điểm Free : " + Util.FormatNumber(player.point_PassFree) + " Điểm"
                        + "\n|7|Điểm VIP : " + Util.FormatNumber(player.point_PassVIP) + " Điểm"
                        + "\n|1|Quy Đổi 1.000 Cash Tặng 100 Điểm Free"
                        + "\n|7|Gói VIP Mở Với giá 500k Tín Dụng Nạp",
                        "Mở Khóa\nVIP",
                        "Nhận quà\nMốc Free",
                        "Nhận quà\nMốc VIP",
                        "Mốc quà\nĐã Nhận"
                );

            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 14 || this.mapId == 7 || this.mapId == 0) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
//                              if (player.point_MokhoaVip < 100) {
//                               Service.gI().sendThongBaoFromAdmin(player, "Chưa Mở Gói Premium\n Liên Hệ admin");
//                               return;
//                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.kickVipPass, -1,
                                    "|7|MUA GÓI PREMIUM XỨ MỆNH\n"
                                    + "\n|2|Tặng Thêm 100k Điểm VIP Khi Mua\n"
                                    + "|7|Mua Thêm Điểm: 50k Coin 50k Điểm\n"
                                    + "\n|4|Mốc Free : " + Util.FormatNumber(player.SoXuMenhDaNhan) + " Điểm Đã Nhận"
                                    + "\n|7|Mốc VIP : " + Util.FormatNumber(player.SoXuMenhDaNhanvip) + " Điểm Đã Nhận",
                                    "Mua Premium", "Mua Điểm");

                            break;
                        case 1:
//                              if (player.point_MokhoaVip < 1) {
//                               Service.gI().sendThongBaoFromAdmin(player, "Chưa Mở Gói Premium\n Liên Hệ admin");
//                               return;
//                            }
                            UseItem.gI().ComfirmMocSoXuMenh(player);
                            break;

                        case 2:
//                            if (player.point_MokhoaVip < 1) {
//                               Service.gI().sendThongBaoFromAdmin(player, "Chưa Mở Gói Premium\n Liên Hệ admin");
//                               return;
//                            }

                            UseItem.gI().ComfirmMocSoXuMenhVIP(player);
                            break;
                        case 3:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                    "|7|SỔ XỮ MỆNH MỐC ĐÃ NHẬN"
                                    + "\n|2|[Các Gói Mốc Đã Nhận]"
                                    + "\n|4|Mốc Free : " + Util.FormatNumber(player.SoXuMenhDaNhan) + " Điểm Đã Nhận"
                                    + "\n|7|Mốc VIP : " + Util.FormatNumber(player.SoXuMenhDaNhanvip) + " Điểm Đã Nhận",
                                    "Đóng");

                            break;

                    }
                }

            }
        }

    }
}
