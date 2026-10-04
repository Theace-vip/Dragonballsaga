package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import npc.Npc;
import player.NPoint;
import player.Player;
import power.PowerLimitManager;
import services.OpenPowerService;
import services.Service;
import utils.Util;

public class QuocVuong extends Npc {

    public QuocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                "Bản thân", "Đệ tử", "Từ chối");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0 -> {
                        if (player.nPoint.limitPower < 110) {
                            this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                    "Ta sẽ truền nănglượng giúp con mở giớihạn sức mạnh của bản thân lên \n"
                                            + "|7|Yêu Cầu: 10M Điểm Fam 1 Lần\n"
                                            + "|6|Max Sức Mạnh Là Vô Hạn + Tăng Gioi Hạn Cộng Chỉ Số\n"
                                            + "|7|Điểm Fam: " + Util.FormatNumber(player.diemfam) + " Điểm\n"
                                            + "|4|Lần Này Tăng Lên Thêm Chỉ Số Gốc",
                                   // + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                   
                                    "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER ) + " Điểm Fam", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Mày Đã Mở Max 1 Lần Rồi \nMở LắM Thế",
                                    "Đóng");
                        }
                    }
                    case 1 -> {
                        if (player.pet != null) {
                            if (player.pet.nPoint.limitPower < 110) {
                                this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                       "Ta sẽ truền nănglượng giúp con mở giớihạn sức mạnh của Đệ Tử lên \n"
                                            + "|7|Yêu Cầu 10M Điểm Fam 1 Lần\n"
                                           + "|6|Max Sức Mạnh Là Vô Hạn + Tăng Gioi Hạn Cộng Chỉ Số\n"
                                               + "|7|Điểm Fam: " + Util.FormatNumber(player.diemfam) + " Điểm\n"
                                             + "|4|Lần Này Tăng Lên Thêm 1M Chỉ Số Gốc",
                                       // + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                        "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " Điểm Fam", "Đóng");
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                         "Mày Đã Mở Max 1 Lần Rồi \nMở LắM Thế",
                                        "Đóng");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                        }
                        //giới hạn đệ tử
                    }
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                switch (select) {
//                    case 0 ->
//                       OpenPowerService.gI().openPowerBasic(player);
                    case 0 -> {
                        if (player.diemfam >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER ) {
                            if (OpenPowerService.gI().openPowerSpeed(player)) {
                                player.diemfam -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                Service.gI().sendMoney(player);
                            }
                        } else {
                            Service.gI().sendThongBao(player,
                                    "Bạn không đủ điểm fam để mở, còn thiếu "
                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.diemfam)) + " Điểm Fam");
                        }
                    }
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                if (select == 0) {
                    if (player.diemfam >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER ) {
                        if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                            player.diemfam -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER ;
                            Service.gI().sendMoney(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player,
                                "Bạn không đủ Điểm Fam để mở\ncòn thiếu "
                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.diemfam)) + " Điểm Fam");
                    }
                }
            }
        }
    }
}
