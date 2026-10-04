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

public class Naruto extends Npc {

    LocalDateTime timeEnd = LocalDateTime.of(2024, 12, 15, 12, 0);
    public static long COST = 500_000_000;

    public Naruto(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        Duration duration = Duration.between(LocalDateTime.now(), timeEnd);
        long day = duration.toDays();
        long h = duration.toHoursPart();
        int m = duration.toMinutesPart();
        if (canOpenNpc(player)) {
            createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Sự kiện Giải cứu nhẫn giới!\n"
                    + "|1|Thời gian còn lại là: " + day + " ngày " + h + " giờ " + m + " phút.\n"
                    + "|6|Obito đang thực hiện kế hoạch hủy diệt toàn bộ nhẫn giới, bạn hãy mong chóng đi ngăn chặn hắn.\n"
                    + "Tiêu diệt boss Obito sẽ nhận được những phần quà giá trị: Cửu vĩ, Cải trang Obito, Sharingan, 20 -> 100 điểm chiến thần,...\n"
                    + "Dùng x99 điểm chiến thần và " + Util.numberToMoney(COST) + " vàng để đổi những phần quà giá trị",
                     "Đổi quà");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                int q = 0;
                for (Item it : player.inventory.itemsBag) {
                    if (it != null && it.template != null) {
                        if (it.template.id == 1536) {
                            q += it.quantity;
                        }
                    }
                }
                switch (select) {
                    case 1:
                        TopService.gI().showListTopEvent(player);
                        break;
                    case 0:
                        if (q < 99) {
                            Service.gI().sendThongBao(player, "Bạn còn thiếu " + (99 - q) + " điểm chiến thần nữa");
                            return;
                        }
                        if(!player.isAdmin()) player.inventory.event += 99;
                        if (player.inventory.gold < 500_000_000) {
                            Service.gI().sendThongBao(player,
                                    "Bạn còn thiếu " + Util.numberToMoney(COST - player.inventory.gold) + " vàng nữa");
                            return;
                        }
                        int[] list = { 1461, 1688, 1689, 1686, 1687, 1690,1477,1513 };
                        Item it = ItemService.gI().createNewItem((short) list[Util.nextInt(list.length)]);
                        int point = Util.nextInt(9, 15);
                        int an = 1;
                        if (it.template.type == 5) {
                            point = Util.nextInt(20, 30);
                            an = Util.nextInt(3, 5);
                        }
                        it.addOptionParam(50, point);
                        it.addOptionParam(77, point);
                        it.addOptionParam(103, point);
                        it.addOptionParam(210, an);
                        if (Util.isTrue(97, 100))
                            it.addOptionParam(93, Util.nextInt(3, 7));
                        q = 99;
                        for (Item itz : player.inventory.itemsBag) {
                            if (itz != null && itz.template != null) {
                                if (q == 0)
                                    break;
                                if (itz.template.id == 1536) {
                                    int tmp = Math.min(q, itz.quantity);
                                    q -= tmp;
                                    InventoryService.gI().subQuantityItemsBag(player, itz, tmp);
                                }
                            }
                        }
                        Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + it.template.name);
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
