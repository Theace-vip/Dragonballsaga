package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.ChangeMapService;
import shop.ShopService;
import utils.Util;

public class Luffyshop extends Npc {

    public Luffyshop(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 135 -> {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SHOP BẢN ĐỒ KHO BÁU\n"
                                    + "|4|Chuyên Cung Cấp Nhu Yếu Phẩm Cực Vip 9999%\n"
                            + "|6|Điểm bdkb " + Util.FormatNumber(player.point_bdkb) + " Điểm",
                            "Đổi Xu Hải Tặc",
                            "Cửa Hàng\nLuffy",
                            "Nhận CảiTrang\n Sự Kiện" 
                    );

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
                    case 135 -> {
                        if (select == 0) {
                            if (player.point_bdkb < 100) {
                                Service.getInstance().sendThongBao(player, "Bạn cần ít nhất 100 Điểm Sk Để Quy Đổi Vé Mua");
                                return;
                            }
                            Item manhnro = null;
                            try {
                                manhnro = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (manhnro == null) {
                                this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, manhnro, 1);
                                Item nrosieucap = ItemService.gI().createNewItem((short) 1335, 1);
                                nrosieucap.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, nrosieucap, 9999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + nrosieucap.template.name);
                                player.point_bdkb -= 100;
                                Service.getInstance().sendThongBao(player, "Bạn Bị Trừ 100 Điểm bdkb");
                            }
                            break;

                        }

                        if (select == 1) {
                            ShopService.gI().opendShop(player, "LUFFY", false);

                        }
                       
                        if (select == 2) {

                            Item thoivang = null;
                            try {

                                thoivang = InventoryService.gI().findItemBag(player, 457);
                            } catch (Exception e) {
                            }
                            if (thoivang == null
                                    || thoivang.quantity < 1) {
                                this.npcChat(player, "Bạn Cần x1 ZENNI");
                            } else if (InventoryService.gI().getCountEmptyBag(player) == 7) {
                                this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");

                            } else {

                                InventoryService.gI().subQuantityItemsBag(player, thoivang, 1);

                                Item caitrang = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
                                caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 300)));
                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 2)));
                                caitrang.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                caitrang.itemOptions.add(new ItemOption(72, Util.nextInt(1, 10)));
                                caitrang.itemOptions.add(new ItemOption(231, 7));

                                InventoryService.gI().addItemBag(player, caitrang, 99999999);
                                InventoryService.gI().sendItemBag(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + caitrang.template.name);

                            }

                            break;
                        }
                        if (select == 3) {
                            Service.gI().showListTop(player, Manager.Topbdkb);

                        }
                    }

                }
            }
        }
    }
}
