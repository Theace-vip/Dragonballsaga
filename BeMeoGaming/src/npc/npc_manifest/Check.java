/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npc.npc_manifest;

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class Check extends Npc {

    public Check(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 14 || this.mapId == 0 || this.mapId == 7) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Chào Các Azai\n"
                        + "|6|" + "Nhớ Nạp Nhiều Nhiều cho Adminღ Nha nhé\n"
                        + "|7|" + "Nhập Ma : " + Util.cap(player.SagaDiaDao) + " Bậcღ\n"
                        + "|5|" + "EXP Nhập Ma  : " + Util.cap(player.DLbTamkjll) + " Expღ\n"
                        + "|7|" + "Tiên Bang  : " + Util.cap(player.SagaThienDao) + " Bậcღ\n"
                        + "|5|" + "Exp Tiên Bang  : " + Util.cap(player.PhapTac_ThienDao) + " Expღ\n",
                        "Hiệu ứng\nTiên Bang", "Hiệu Ứng\n Chuyển sinh", "Hiệu Ứng\nNhập Ma", "Hiệu Ứng\nTu Tiên", "Hiệu Ứng\nSố Dư", "Từ chối");

            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 14 || this.mapId == 0 || this.mapId == 7) {
                switch (select) {
                    case 0:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                "|7|Chúc Bạn Năm Mới An Khang\n"
                                + "|4|\nUP Chung Clan Để Nhận Điểm EXP Đủ 100% Sẽ +1 Cấp\n"
                                + "|4|AE ấn Xem Hiệu ứng Cơ Bản Để Xem Chỉ Số Tăng",
                                "Sát Thương\n Vừa Đấm", "Hiệu ứng \nTiên Bang",
                                "Thông Tin Exp"
                        );
                        break;
                    case 1:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.SagaChuyenSinh, -1,
                                "|4|Chuyển Sinh : " + Util.cap(player.SagaChuyenSinh) + " Cấpღ\n"
                                + "|7|Sức Mạnh Yêu Cầu : " + Util.cap(player.nPoint.power) + " /18M X3 Tỉ Sức Mạnhღ\n"
                                + "Điểm EXP Tiên Bang : " + Util.FormatNumber(player.PhapTac_ThienDao) + " /200Mღ\n"
                                  + "|5|" + "Cần Tối Thiểu 5000 Cấp Tiên Bang Để Chuyển Sinhღ\n"
                                + "ChuyểnSinh :Yêu Cầu Sức Mạnh + 200M EXP TB *2/cấp + 10k Tỉ Vàngღ\n"
                                + "+100k SDG + 500K HP KIG + 10k GIÁP + 10% HP,KI,SD /1 Cấp",
                                "Chuyển Sinh\nSư Phụ", "Chuyển Sinh\nĐệ",
                                "Đóng"
                        );
                        break;
                    case 2:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                "|5|\n" + "Nhập Ma : " + Util.cap(player.SagaDiaDao) + " Cấpღ\n"
                                + "EXP Nhập Ma : " + Util.cap(player.DLbTamkjll) + " /EXPღ\n"
                                + "|7|" + "Tiến Trìnhღ:"
                                + (player.DLbTamkjll * 100 / (3000000L + player.SagaDiaDao * 200000L))
                                + "%:  \n"
                                + "|5|" + "Chỉ Cần Di Chuyển Sẽ Được EXP Nhập Maღ\n"
                                + "Khi Đạt 100% Tiến Trình Sẽ Lên 500K SD/Cấp \n",
                                "Đóng"
                        );
                        break;
                    case 3:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                "|5|" + "Cảnh giới : " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " ღ\n"
                                + "|7|" + "EXP Tu Tiên : " + Util.getFormatNumber(player.SagaTuTien[0]) + " EXPღ\n"
                                + "|7|" + "Thiên Phúღ:" + player.SagaTuTien[2] + " Sao\n"
                                + "|5|" + "Điều Kiện Tu Tiên Cần Đẹp Traiღ\n",
                                "Đóng"
                        );
                        break;
                    case 4:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                "|5|\b" + "Số Dư  : " + Util.numberToMoney(player.getSession().vnd) + " Coin\n"
                                + "Số Dư  : " + Util.numberToMoney(player.getSession().tongnap) + " Tích Lũy\n"
                                + "ID : " + (player.getSession().userId) + " Tài Khoản\n",
                                "Đóng"
                        );
                        break;
                }
            }
        }
    }
}
