package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import server.Manager;
import services.NpcService;
import services.Service;
import utils.Util;

public class NPCTanThu extends Npc {

    public NPCTanThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 0 || this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|˚₊· ͟͟͞͞➳❥Hêllooooooo\n"
                              + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|4|✎Hãy Chịu Khó Đi Up Điểm Fam\n"
                        + "✎Dùng Đổi Những Thứ Tân Thủ Bên Dưới\n"
                        + "|7|✎Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n"
                               + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        ,
                        "Nhận\nNgọc Xanh","Nhận Ruby","Nhận Vàng","Nhận\nSao Pha Lê","Nhận\n Đá Nội Tại");
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 0 || this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.NHANGEM, -1,
                                    "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                  +  "|4|Bạn Muốn Nhận Ngọc Xanh ?ღ\n"
                                    + "Chi Phí Mỗi Lần Đổi Từ 10k Điểm TrainFam\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n"
                                     + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    ,
                                    "Đổi\n100k\nGem",  "Đổi\n500k\nGem",  "Đổi\n1.000k\nGem", "Đổi\n2.000k\nGem", "Đổi\n5.000k\nGem", "Đổi\n10.000k\nGem", "Đóng"
                            );
                            break;
                        case 1:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.NHANRUBY, -1,
                                     "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    +"|4|Bạn Muốn Nhận Ngọc Ruby ?ღ\n"
                                    + "Chi Phí Mỗi Lần Đổi Từ 100k Điểm TrainFam\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n"
                                     + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    ,
                                    "Đổi\n100k\nRuby",  "Đổi\n500k\nRuby",  "Đổi\n1.000k\nRuby", "Đổi\n2.000k\nRuby", "Đổi\n5.000k\nRuby", "Đổi\n10.000k\nRuby", "Đóng"
                            );
                            break;
                        case 2:
                           NpcService.gI().createMenuConMeo(player, ConstNpc.NHANVANG, -1,
                                    "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                   + "|4|Bạn Muốn Nhận Zenni Vàng ?ღ\n"
                                    + "Chi Phí Mỗi Lần Đổi Từ 100k Điểm TrainFam\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n",
                                    "Đổi\n100k\nZeni",  "Đổi\n500k\nZeni",  "Đổi\n1.000k\nZeni", "Đổi\n2.000k\nZeni", "Đổi\n5.000k\nZeni", "Đổi\n10.000k\nZeni", "Đóng"
                            );
                            break;
                        case 3:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.NHANSAOPHA, -1,
                                      "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    +"|4|Đây Là Đổi Sao Pha Lêღ\n"
                                    + "Chi Phí 10k Điểm Fam 1 Lần\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n"
                                     + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    ,
                                    "Đổi\nSao Hút máu\nx1", "Đổi\nSao Hút Kix1", "Đổi\nSao Xuyên Giapx1",  "Đổi\nSao Xuyen Chuongx1", "Đổi\nSao TNSMx1","Đóng"
                            );
                            break;
                        case 4:
                             NpcService.gI().createMenuConMeo(player, ConstNpc.NHANDANOITAI, -1,
                                       "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                  +  "|4|Bạn Muốn Nhận Đá Mở NT ?ღ\n"
                                    + "Chi Phí Mỗi Lần Đổi Từ 100k Điểm TrainFam\n"
                                    + "|7|" + "Điểm Fam: " + Util.cap(player.diemfam) + " Điểmღ\n"
                                      + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                     ,
                                    "Đổi\n100k\nCái",  "Đổi\n500k\nCái",  "Đổi\n1.000k\nCái", "Đổi\n2.000k\nCái", "Đổi\n5.000k\nCái", "Đổi\n10.000k\nCái", "Đóng"
                            );
                            break;
                      

                    }
                }
            }
        }
    }
}
