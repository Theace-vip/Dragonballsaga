package npc.npc_manifest;

import java.time.Duration;
import java.time.LocalDateTime;

import consts.ConstNpc;
import item.Item;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.TopService;
import utils.Util;

public class Berry extends Npc {

    LocalDateTime timeEnd = LocalDateTime.of(2025, 9, 6, 12, 0);
    public static long COST = 500_000_000;
    public Berry(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        Duration duration = Duration.between(LocalDateTime.now(), timeEnd);
        long day = duration.toDays();
        long h = duration.toHoursPart();
        int m = duration.toMinutesPart();
        if (canOpenNpc(player)) {
            createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Sự kiện Đại chiến thần thú!\n"
                    + "|1|Thời gian còn lại là: " + day + " ngày " + h + " giờ " + m + " phút.\n"
                    + "|6|Các thần thú đã nổi loạn cùng với vua bách thú hãy mau chóng đi thu phục chúng\n"
                    + "Tiêu diệt Thần thú và Vua Bách Thú sẽ nhận được những phần quà giá trị\n"
                    + "Dùng x99 Ngọc Thần Thú và "+Util.numberToMoney(COST)+" vàng để đổi những phần quà giá trị",
                    "Đổi quà","Top\n Sự kiện");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if(canOpenNpc(player)){
            if(player.iDMark.isBaseMenu()){
                int q = 0;
                for(Item it : player.inventory.itemsBag){
                    if(it != null && it.template != null){
                        if(it.template.id == 1015){
                            q += it.quantity;
                        }
                    }
                }
                switch (select) {
                    case 1:
                        TopService.gI().showListTopEvent(player);
                        break;
                    case 0:
                        if(q < 99){
                            Service.gI().sendThongBao(player, "Bạn còn thiếu " + (99 - q) +" Ngọc thần thú nữa");
                            return;
                        }
                        if(player.inventory.gold < 500_000_000){
                            Service.gI().sendThongBao(player, "Bạn còn thiếu " + Util.numberToMoney(COST - player.inventory.gold) +" vàng nữa");
                            return;
                        }
                        int[] list = { 1381,1351,1352,1353,1354,1355,1356,1357,1358,1359,1360,1361,1362,1363,1364,1365,1366,1367,1173,1174,1175,1176,1177,1178,1179,1180,1181,1182,1183,1184,1816,1817,1818,1819,1820,1471,1472,1480,1561,1797,1799,1804};
                        
                        Item it = ItemService.gI().createNewItem((short) list[Util.nextInt(list.length)]);
                        
                        int point = Util.nextInt(9,15);
                        int an = 1;
                        if(it.template.type == 5){
                            point = Util.nextInt(25,30);
                            an = Util.nextInt(3,5);
                        }
                        it.addOptionParam(50, point);
                        it.addOptionParam(77, point);
                        it.addOptionParam(103, point);
                        it.addOptionParam(210, an);
                        if(Util.isTrue(999, 1000)) it.addOptionParam(93, Util.nextInt(3,7));
                        q=99;
                        for(Item itz : player.inventory.itemsBag){
                            if(itz != null && itz.template != null){
                                if(q == 0) break;
                                if(itz.template.id == 1015){
                                    int tmp = Math.min(q, itz.quantity);
                                    q -= tmp;
                                    InventoryService.gI().subQuantityItemsBag(player, itz, tmp);
                                }
                            }
                        }
                        Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được "+it.template.name);
                        player.inventory.gold -= COST;
                        InventoryService.gI().addItemBag(player, it,999999);
                        Service.gI().sendMoney(player);
                        InventoryService.gI().sendItemBag(player);

                        break;
                    
                    default:
                        break;
                }
            }
        }
    }

}
