package npc.npc_manifest;

/**
 *
 */
import consts.ConstNpc;
import models.SuperRank.SuperRankService;
import npc.Npc;
import player.Player;
import server.Manager;
import services.Service;
import services.TaskService;

public class cadich extends Npc {

    public cadich(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

   
    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                       + "Xin Chào Anh Chị Đến Với HonDaoDragon\n"
                                + "|4|Đây Là Bảng Xếp Hạng Đua Top\n"
                                + "Wesite:https://hondaodragon.online \n"
                      +  "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        ,
                        
                        "TOP\nSức Mạnh",
                        "TOP\nSăn Boss",
                        "TOP\nNạp Tiền",
                        "TOP\nRương Thần Bí",
                         "TOP\nĐập Đồ"
                        
                );
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
//                    case 0: // mã quà tặng
//                       Service.gI().showListTop(player, Manager.TopTienBang);
//                        break;
//                      case 1: // mã quà tặng
//                       Service.gI().showListTop(player, Manager.TopNhapMa);
//                        break;   
                      case 0: // mã quà tặng
                          Service.gI().showListTop(player, Manager.Topsucmanh);
                        break;   
                       case 1: // mã quà tặng
                        Service.gI().showListTop(player, Manager.Topsanboss);
                        break;    
                        case 2: // mã quà tặng
                            Service.gI().showListTop(player, Manager.TopNap);
                 //      TopService.gI().showListTopVnd(player);
                        break;
                          case 3: // mã quà tặng
                            Service.gI().showListTop(player, Manager.Topmoruong);
                        break;
                         case 4: // mã quà tặng
                            Service.gI().showListTop(player, Manager.Topdapdo);
                        break; 
                        
                        
                 
                }
            }
        }
    }
}
