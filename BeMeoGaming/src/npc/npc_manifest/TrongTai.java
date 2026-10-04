package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import models.SuperRank.SuperRankManager;
import models.SuperRank.SuperRankService;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.func.ChangeMapService;
import utils.Util;

public class TrongTai extends Npc {

    public TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 113 -> {
                    if (SuperRankManager.gI().awaiting(player)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vui lòng chờ, số thứ tự của bạn là " + SuperRankManager.gI().ordinal(player.id), "OK", "Về\nĐại Hội\nVõ Thuật");
                        return;
                    }
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, 
                            "|7|Đại hội võ thuật Siêu Hạng\n"
                       + "|4|diễn ra 24/7 kể cả ngày lễ và chủ nhật\n"
                                    + "Hãy thi đấu ngay để khẳng định đẳng cấp của mình nhé\n"
                                    + "|7|Chi Phí Sau Vé Free Là: 20k Coin/ Thi Đấu\n"
                                    + "|4|giải Thưởng Coin Hàng Ngày Rất Ngon Đấy\n"
                                    + "|7|Bạn Đang Hạng :" + player.superRank.rank
                                  + "\n|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " Coin",
                            "Top 100\nCao Thủ", 
                            "Hướng\ndẫn\nthêm", player.superRank.ticket > 0 ? "Miễn phí\nCòn " + player.superRank.ticket + " vé" : "Thi đấu", "Nhận\nDanh Hiệu\nCao Thủ", "Về\nĐại Hội\nVõ Thuật");
                }
                default ->
                    super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 113 -> {
                        if (SuperRankManager.gI().awaiting(player)) {
                            if (select == 1) {
                                ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                            }
                            return;
                        }
                        switch (select) {
                            case 0 ->
                                SuperRankService.gI().topList(player, 0);
                            case 1 ->
                                NpcService.gI().createTutorial(player, tempId, avartar, ConstNpc.THONG_TIN_SIEU_HANG);
                            case 2 ->
                                SuperRankService.gI().topList(player, 1);
                            case 3 ->
                               ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                            case 4 ->
                                ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                            default -> {
                            }
                        }
                    }

                }
            }
        }
    }
}
