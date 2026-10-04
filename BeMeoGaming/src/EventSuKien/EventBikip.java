package EventSuKien;

import consts.ConstNpc;
import item.Item;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import utils.Util;

public class EventBikip extends Npc {

    public EventBikip(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (this.mapId == 176 || this.mapId == 5) {
            if (canOpenNpc(player)) {
                Item bikip = null;
                Item co4la = null;
                Item zenni = null;
                try {
                    bikip = InventoryService.gI().findItemBag(player, 590);
                    co4la = InventoryService.gI().findItemBag(player, 1150);
                    zenni = InventoryService.gI().findItemBag(player, 457);
                } catch (Exception e) {
                }

                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {

                    // Tùy map mà đổi nút đầu tiên
                    String firstButton = (this.mapId == 176) ? "Về Đảo" : "Đến  Map\nUp";

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|BÍ KIẾP THUẬT"
                            + "\n|5|Bạn cần Thu Thập 100.000 Bí Kiếp"
                            + "\nĐiều kiện nhận Cải Trang Thỏ Buma Kháng Choáng"
                            + "\n|7|[Chỉ Số 1-1000% Chỉ Số Up Cấp Thiên Đạo]"
                            + " \n|6|Bi Kiếp: x" + (bikip == null ? 0 : bikip.quantity) + " / x100.000"
                            + "\nCỏ 4 Lá: x" + (co4la == null ? 0 : co4la.quantity) + " / x150.000"
                            + "\nZenni: x" + (zenni == null ? 0 : zenni.quantity) + " / x100.000"
                            + "\nTu tiên đạt Đại Đế: Cấp hiện tại: " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]))
                            + "\nChuyển sinh đạt x" + player.SagaChuyenSinh + " / x60"
                            + "\nĐạt InGameVIP: " + player.Saga_VIP + " / 4"
                            + "\nHồng ngọc: x" + Util.format(player.inventory.ruby) + " / x999.999"
                            + "\nNgọc Xanh: x" + Util.format(player.inventory.gem) + " / x999.999",
                            firstButton, "Đổi Thưởng"
                    );
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 176 || this.mapId == 5) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            if (player.Saga_VIP < 4) {
                                Service.gI().sendThongBaoFromAdmin(player, "Đạt VIP 4 Hẵng Tiếp Tục Vào");
                                return;
                            }
                            if (this.mapId == 176) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 480);
                            } else {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 176, -1, 480);
                            }
                            break;

                        case 1:
                            Item bikip = null;
                            Item co4la = null;
                            Item zenni = null;
                            try {
                                bikip = InventoryService.gI().findItemBag(player, 590);
                                co4la = InventoryService.gI().findItemBag(player, 1150);
                                zenni = InventoryService.gI().findItemBag(player, 457);

                            } catch (Exception e) {
                            }
                            if (bikip == null || bikip.quantity < 100000) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Bí Kíp");
                                return;
                            }
                            if (co4la == null || co4la.quantity < 150000) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Cỏ 4 Lá");
                                return;
                            }
                            if (zenni == null || zenni.quantity < 100000) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Zenni");
                                return;
                            }

                            if (player.inventory.ruby < 999_999) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Hồng ngọc");
                                return;
                            }
                            if (player.inventory.gem < 999_999) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ gem");
                                return;
                            }

                            if (player.Saga_VIP < 4) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ  VIP 4");
                                return;
                            }
                            if (player.SagaChuyenSinh < 60) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Số lẩn Chuyển sinh");
                                return;
                            }
                            if (player.SagaTuTien[1] < 171) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ Cấp Tu tiên");
                                return;
                            }

                            if (player.point_bdkb < 0) {
                                Service.gI().sendThongBao(player, "Bạn chưa đủ 10k Điểm Trong Bản đồ kho báu");
                            } else {
                                player.inventory.ruby -= 999_999;
                                player.inventory.gem -= 999_999;
                                InventoryService.gI().subQuantityItemsBag(player, bikip, 100000);
                                InventoryService.gI().subQuantityItemsBag(player, co4la, 150000);
                                InventoryService.gI().subQuantityItemsBag(player, zenni, 100000);

                                Item nhancaitrang = ItemService.gI().createNewItem((short) 464);
                                nhancaitrang.itemOptions.add(new Item.ItemOption(59, Util.nextInt(1, 2)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 1000)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 1000)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 1000)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(117, Util.nextInt(1, 100)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(107, Util.nextInt(1, 8)));
                                nhancaitrang.itemOptions.add(new Item.ItemOption(233, 1));
                                if (Util.isTrue(99, 100)) {
                                    nhancaitrang.itemOptions.add(new Item.ItemOption(93, 7));
                                InventoryService.gI().addItemBag(player, nhancaitrang, 1);
                                Service.getInstance().sendMoney(player);
                                InventoryService.gI().sendItemBag(player);
                                Service.gI().sendThongBao(player, "|5|Đã nhận được " + nhancaitrang.template.name);
                            }
                            break;

                    }
                }
            }
            }
        }
    }

}
