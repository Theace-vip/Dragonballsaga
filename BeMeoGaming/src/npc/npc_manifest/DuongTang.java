package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.func.ChangeMapService;
import utils.Util;

public class DuongTang extends Npc {

    public DuongTang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 0 -> {
                   createOtherMenu(player, ConstNpc.BASE_MENU, "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                            + "Tham Gia Sự Kiện Ngũ Hành Sơn ?\n"
                           + "Hãy Tham Gia Nhận Cải Trang Nhé + Skill Biến Hình Free\n"
                            + "|6|Điểm Sự Kiện: " + Util.FormatNumber(player.sukien),
                            "Tham Gia", "Đóng");

                }
                case 123 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ra khỏi ngôi làng này sẽ gặp ngọn núi ngũ hành sơn\n"
                              + "Điểm Fam của bạn: " + player.diemfam ,
                            "Về\nLàng Aru","Đóng");
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
                    case 0 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 122, 1017, 408);
                        }
                    }
                    case 123 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 0, -1, 432);
                        }
                         if (select == 1) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 0, -1, 432);
                            
                            
                            
                            
                            
                        }
                    }
                }
            }
        }
    }

}
