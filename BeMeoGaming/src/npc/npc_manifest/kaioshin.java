package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.Service;
import services.func.ChangeMapService;
import utils.Util;

public class kaioshin extends Npc {

    public kaioshin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ta Sẽ giúp Ngươi Dịch Chuyển Đến Vị Trí\nBất Cứ Nào Trên Vũ Trụ",
                            "Đến Map\nSKH", "Đến Map\nNghĩa Địa", "Đến Map\nĐá quý", "Đến Map\nNgục Tù", "Đến Map\nRừng Cây", "Đến Map\nĐịa Ngục");
                    
                    
                }
              case 49 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta Rất Thất Vọng Về Ngươi",
                            "Về Làng Aru");
             
               
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
                    case 5 -> {
                        if (select == 0) {
                            if (player.Saga_VIP < 1) {
                                Service.gI().sendThongBaoFromAdmin(player, "Map Chỉ Dành Cho Gà Mờ VIP 1\nUp Sét Kích Hoạt 100% Rơi");
                                return;
                            }
                              ChangeMapService.gI().changeMapBySpaceShip(player, 182, -1, 168);
                        }
                           if (select == 1) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 181, -1, 264); 
                         }
                           if (select == 2) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 180, -1, 384);     
                            
                         }
                           if (select == 3) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 179, -1, 288);        
                             }
                           if (select == 4) {
                           ChangeMapService.gI().changeMapBySpaceShip(player, 177, -1, 360);    
                           }
                           if (select == 5) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 408);      
                            
                            
                            
                        }
                    }
                    
                    case 49 -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, Util.nextInt(700, 800), 432);
                        }
                    }
                }
            }
        }
    }

}
