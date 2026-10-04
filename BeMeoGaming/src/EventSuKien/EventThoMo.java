package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.func.ChangeMapService;
import utils.Util;

public class EventThoMo extends Npc {

    public EventThoMo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 132) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|Hello Dân Thợ mỏ\n"
                        + "|6|Số Thợ mỏ :" + player.TamkjllThomo + " Người\n"
                        + "Kinh Nghiệm Đào Mỏ: " + Util.FormatNumber(player.TamkjllThomoExp) + " EXP\n"
                        + "|4|Mỗi 1 Thợ Tăng Số Lượng Up Ra Ngọc + Ruby + Vp\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n",
                        "Thông Tin\nUp",
                        "Nâng cấp\nThợ",
                        "Về Nhà");

            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 132) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|Hello Mấy Con Thợ Mở\n"
                                    + "Số Thợ Đào :" + player.TamkjllThomo + " Người\n"
                                    + "|4|Buff Vàng Nhặt: " + player.TamkjllThomo * 5000
                                    + "\nBuff Ngọc nhặt: " + player.TamkjllThomo * 10
                                    + "\nBuff Ruby nhặt:" + player.TamkjllThomo * 1
                                    + "\nBuff Đá Nội Tại nhặt:" + player.TamkjllThomo * 6
                                    + "\nBuff Cỏ 4 Lá nhặt:" + player.TamkjllThomo * 0,
                                    "Thông Tin\nBuff", "Nâng cấp", "Về Nhà");
                            break;
                        case 1:
                            long exptm = player.TamkjllThomo;
                            if (player.TamkjllThomoExp > 2000000 + exptm * 1000000) {
                                player.TamkjllThomoExp -= 2000000 + exptm * 1000000;
                                player.TamkjllThomo++;
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Nâng cấp thành công.\n Số thợ mỏ hiện tại của bạn là: "
                                        + player.TamkjllThomo + "\n",
                                         "Thông Tin\nUp", "Nâng cấp", "Về Nhà");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Số Thợ Đào :" + player.TamkjllThomo + " Người\n"
                                        + "Bạn cần: " + ((2000000 + exptm * 1000000) - player.TamkjllThomoExp)
                                        + "Exp Đào mỏ nữa.",
                                        "Thông Tin\nUp", "Nâng cấp", "Về Nhà");
                            }
                            break;
                        case 2:
                             ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 288);
                            break;
                    }
                }
            }
        }

    }
}
