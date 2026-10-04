/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npc.npc_manifest;

import consts.ConstNpc;
import jdbc.daos.PlayerDAO;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class vaymuon extends Npc {

    public vaymuon(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|˚₊· ͟͟͞͞➳❥Chào Mừng Chủ Nhân Thiên Gioi\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|6|Chát TuTien : Di Chuyển Tới Map Tu Tiênღ\b"
                        + "Fam quái Nhận Kinh Nghiệm EXP Tu Tiên\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "Độ Kiếp Lên Cảnh giới: Yêu Cầu 500M Kinh Nghiệm\n"
                        + "luyện Hóa Thiên Phú: Càng Cao Up EXP Càng Nhanh Và Dễ Thăng Cảnh giới\n"
                        + "Luyện Hóa Thiên Phú: Yêu Cầu Mỗi Lần Trừ 10 Tỷ Kinh Nghiệm\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|7|Thiên Phú Dị Bẩm: [ " + player.SagaTuTien[2] + " ] Sao              \n"
                        + "|4|Kinh Nghiệm: [ " + Util.getFormatNumber(player.SagaTuTien[0]) + " ] EXP  \n",
                        "Thông Tin\nCảnh Gioi",
                        "Luyện Hóa\nThiên Phú",
                        "Thông Tin\nChỉ Số",
                        // "Thiên Đạo\nĐộ Kiếp",
                        "Độ Kiếp \nCảnh giới"
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
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|Chào Mừng Chủ Nhân Thiên Gioi\n"
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    + "|6|Tu Vi Cảnh giới: [ " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " ]\n"
                                    + "Tu Vi Tiếp Theo: [ " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]) + 1) + " ]\n"
                                    + "|7|Thiên Phú Dị Bẩm: [ " + player.SagaTuTien[2] + " ] Sao              \n"
                                    + "|4|Kinh Nghiệm: [ " + Util.getFormatNumber(player.SagaTuTien[0]) + " ] EXP  \n"
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n",
                                     "Thông Tin\nCảnh Gioi",
                                    "Luyện Hóa\nThiên Phú",
                                    "Thông Tin\nChỉ Số",
                                    //   "Thiên Đạo\nĐộ Kiếp",
                                    "Độ Kiếp \nCảnh giới"
                            );
                            break;

                        case 1:
                            if (player.SagaTuTien[2] < 1) {
                                if (player.SagaTuTien[0] < 5000000000l) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nChào Mừng Chủ Nhân Thiên Gioi\nYêu Cầu Chủ Nhân Cần  10 Tỷ Kinh Nghiệm Tẩy Luyện\nKiếm Từ Việc Up quái");
                                    return;
                                }
                                player.SagaTuTien[0] -= 5000000000l;
                                int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 2)));
                                player.SagaTuTien[2] = tp;
                                Service.getInstance().sendThongBaoAllPlayer(
                                        "Chúc Mừng Đạo Hữu : " + player.name + " Đã Tẩy Luyện Mở Ra Thiên Phú\n" + tp + " Sao");
                            } else {
                                if (player.SagaTuTien[1] < 30) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Muốn tẩy thiên phú Cần Đạt Cảnh giới Nguyên anh.");
                                    return;
                                }
                                if (player.SagaTuTien[1] < 30) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|8|\nChào Mừng Chủ Nhân Thiên Gioi\nYêu Cầu Chủ Nhân Cần Đạt Nguyên Anh\nĐể Tẩy Luyện Kiếm Từ Việc Up quái");
                                    return;
                                }
                                if (player.SagaTuTien[2] >= 500) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Mày Đã Là Cao Thủ Rồi");
                                    return;
                                }
                                player.SagaTuTien[0] -= 5000000000l;
                                if (Util.isTrue(10f, 100)) {
                                    player.SagaTuTien[2]++;
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|6|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Tẩy Luyện Thiên Phú Từ "
                                            + (player.SagaTuTien[2] - 1) + " sao lên: "
                                            + player.SagaTuTien[2] + " sao.");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Tẩy Luyện Thiên Phú Thất Bại.\nBạn Bị Trừ 5 Tỉ EXP");
                                }
                            }
                            break;
                        case 2:
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|Cảnh giới: "
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]))
                                    + "\n|6|Hp: "
                                    + player.TamkjllHpKiGiaptutien(
                                            Util.maxInt(player.SagaTuTien[1]))
                                    + "%\nKi: "
                                    + player.TamkjllHpKiGiaptutien(
                                            Util.maxInt(player.SagaTuTien[1]))
                                    + "%\nGiáp: "
                                    + player.TamkjllHpKiGiaptutien(
                                            Util.maxInt(player.SagaTuTien[1]))
                                    + "%\nDame: "
                                    + player.TamkjllDametutien(Util.maxInt(player.SagaTuTien[1]))
                                    + "%\nSTCM: "
//                                    + player.TamkjllDametutienSTCM(Util.maxInt(player.SagaTuTien[1]))
                                    + "%\nTiềm Năng: "
                          //          + player.TamkjllDametutientnsm(Util.maxInt(player.SagaTuTien[1]))
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•"
                                    + "%\n",
                                    "Thông Tin\nCảnh Gioi",
                                    "Luyện Hóa\nThiên Phú",
                                    "Thông Tin\nChỉ Số",
                                    //  "Thiên Đạo\nĐộ Kiếp",
                                    "Độ Kiếp \nTu Vi"
                            );
                            break;
//                        case 3:
//                            if (player.getSession().vnd < 50000) {
//                                Service.getInstance().sendThongBaoFromAdmin(player, "|6|Chào Mừng Chủ Nhân Thiên Gioi\nYêu Cầu Chủ Nhân Cần 50k vnd Để Độ Kiếp Lên 1 Cảnh Gioi\n100% Thành Công");
//                                return;
//                            }
//                            if (player.SagaTuTien[1] >= 340) {
//                                Service.getInstance().sendThongBaoFromAdmin(player, "Mày Đã Là Cao Thủ Rồi");
//                                return;
//                            }
//                            PlayerDAO.subcash(player, 50000);
//                            if (Util.isTrue(100f, 100)) {
//                                player.SagaTuTien[1]++;
//                               Service.getInstance().sendThongBao(player,
//                                        "|8|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Được Thiên Đạo Độ Kiếp\n "
//                                        + " Từ Cảnh Gioi " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]) - 1) + " lên: "
//                                        + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " !");
//                            } else {
//                                Service.getInstance().sendThongBaoFromAdmin(player, "Độ Kiếp Thất Bại");
//
//                            }
//                            break;

                        case 3:
                            if (player.SagaTuTien[0] < 500000000) {
                                Service.getInstance().sendThongBaoFromAdmin(player, "|6|Chào Mừng Chủ Nhân Thiên Gioi\nYêu Cầu Chủ Nhân Cần 500M EXP Để Độ Kiếp Lên 1 Cảnh Gioi\n10% Thành Công");
                                return;
                            }
                            if (player.SagaTuTien[1] >= 340) {
                                Service.getInstance().sendThongBaoFromAdmin(player, "Mày Đã Đỉnh Cao Của Cày Chay rồi");
                                return;
                            }
                            player.SagaTuTien[0] -= 500000000;
                            if (Util.isTrue(10f, 100)) {
                                player.SagaTuTien[1]++;
                                Service.getInstance().sendThongBaoAllPlayer(
                                        "|8|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Được Độ Kiếp\n "
                                        + " Từ Cảnh Gioi " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]) - 1) + " lên: "
                                        + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " !");
                            } else {
                                Service.getInstance().sendThongBaoFromAdmin(player, "Độ Kiếp Thất Bại \nLần Sau Cố Gắng Hơn");

                            }
                            break;
                    }

                }
            }
        }
    }
}
