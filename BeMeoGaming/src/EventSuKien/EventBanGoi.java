package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import utils.Util;

public class EventBanGoi extends Npc {

    public EventBanGoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|CỬA HÀNG HIỆU"
                        + "\n|5|Chị Bán Các Gói VIP Nè Cưng\n"
                        + "giá Trị Gói Càng Cao - Càng Ngon Nhé\n"
                        + "|6|Hãy Ủng Hộ Nhé Cưng\b"
                        + "|6|100k ATM 1M Coin - Và Nhiều Cái Ngon\n"
                        + "\n|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " Coin"
                        + "\n|7|Đã Nạp " + Util.numberToText(player.getSession().tongnap) + " Coin",
                        "Mua Gói\nĐập Đồ",
                        "Mua Gói\nChuyển Sinh",
                        "Mua Gói\nChân Mệnh",
                        "Mua Gói\nChữa Lành",
                        "Mua Gói\nĐệ Tử",
                        "Mua Gói\nTầm Bảo"
                );

            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.goidapdo, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI ĐẬP ĐỒ 1M TRỊ GIÁ 100%ღ\n"
                                    + "1 Sét Kích Hoạt VIP 100M HP,KIღ\n"
                                    + "1 Cải Trang 50.000% HP,KI,SDღ\n"
                                    + "100 Triệu RuBy,Ngọc Xanhღ\n"
                                    + "X1000.000 Zenniღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;
                        case 1:
                             NpcService.gI().createMenuConMeo(player, ConstNpc.goichuyensinh, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI Chuyển Sinh 1M TRỊ GIÁ 100%ღ\n"
                                    + "1 Linh Thú TNSM 100K%ღ\n"
                                    + "20 Cấp Chuyển Sinh Freeღ\n"
                                    + "99 Cấp Thiên Đạo Freeღ\n"
                                    + "500 Cấp Địa Đạo Freeღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;
                        case 2:
                             NpcService.gI().createMenuConMeo(player, ConstNpc.goichanmenh, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI Chân Mệnh 1M TRỊ GIÁ 100%ღ\n"
                                    + "1 Chân Mệnh Base Freeღ\n"
                                    + "1M Đá Ngũ Sắc Freeღ\n"
                                    + "5M Điểm Fam Freeღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;
                        case 3:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.chualanh, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI Chữa Lành 1M TRỊ GIÁ 100%ღ\n"
                                    + "10M Đá Gỡ sao Pha Lêღ\n"
                                    + "100 Đá Độ Kiếp 15%ღ\n"
                                    + "10 Đá Độ Kiếp 35%ღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;
                              case 4:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.goidetu, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI Đệ Tử 1M TRỊ GIÁ 100%ღ\n"
                                    + "1 Đệ Gohan 10k% Hợp Thểღ\n"
                                    + "1 Danh Hiệu Đại Thánh 100k% HP,KI,SD VVღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;
                             case 5:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.goitambao, -1,
                                    "|7|Hello Mấy Con Lợnღ\n"
                                    + "|5|\n" + "GÓI Tầm Bảo 1M TRỊ GIÁ 100%ღ\n"
                                    + "999 key bạc Tầm Bảoღ\n"
                                    + "300 Key Vàng Tầm Bảoღ\n"
                                    + "+100.000 Điểm VIP Sổ Xứ Mệnh\n"
                                    + "|7|" + "Số Dư: " + Util.cap(player.getSession().vnd) + " Coinღ\n",
                                    "Mua\n1M Coin\nGiá Trị\n100%", "Mua\n2M Coin\nGiá Trị\n250%", "Mua\n5M Coin\nGiá Trị\n400%", "Mua\n10M Coin\nGiá Trị\n800%"
                            );
                            break;

                    }
                }

            }
        }

    }
}
