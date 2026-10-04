package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import models.Combine.CombineUtil;
import player.Player;
import server.ServerNotify;
import services.InventoryService;
import services.Service;
import utils.Util;

public class NangCapVatPham {

    private static long getGold(Item item) {
        long levelItem = item.getOptionParam(72);
        int levelOption = item.template.level;
        long level = levelItem + levelOption;
        if (levelOption >= 0) {
            return 10_000_000_000l;
        } else {
            return level * 10000;
        }
    }
    
       private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 30;
            case 1:
                return 70;
            case 2:
                return 110;
            case 3:
                return 170;
            case 4:
                return 230;
            case 5:
                return 350;
            case 6:
                return 500;
            default:
                return 999;

        }

    }

    private static long getDa(Item item) {
        long levelItem = item.getOptionParam(72);
        int levelOption = item.template.level;
        long level = levelItem + levelOption;
        return level + 999;
    }

    private static int getRatio(long level) {
        if (level == 0) {
            return 80;
        } else if (level == 1) {
            return 50;
        } else if (level == 2) {
            return 20;
        } else if (level == 3) {
            return 10;
        } else if (level == 4) {
            return 7;
        } else if (level == 5) {
            return 5;
        } else if (level == 6) {
            return 3;
        } else if (level == 7) {
            return 1;
        } else {
            return 1;
        }
    }

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 2) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị và đúng loại đá nâng cấp");
            return;
        }

        Item trangBi = null;
        Item daNangCap = null;
        for (Item item : player.combine.itemsCombine) {
            if (item.template.type < 5) {
                trangBi = item;
            } else if (item.isDaNangCap()) {
                daNangCap = item;
            }
        }
        if (trangBi == null || daNangCap == null || !trangBi.canNangCapWithNDC(daNangCap)) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị và đúng loại đá nâng cấp");
            return;
        }
        Item daBaoVe = InventoryService.gI().findItemBag(player, 987);
        Item daBaoVeKhoa = InventoryService.gI().findItemBag(player, 1143);
        long level = trangBi.getOptionParam(72);
        long gold = getGold(trangBi);
        long da = getDa(trangBi);
        if (level >= CombineService.MAX_LEVEL_ITEM) {
            Service.gI().sendDialogMessage(player, "Vật phẩm đã đạt cấp độ tối đa, không thể nâng cấp nữa");
            return;
        }
        boolean canUseDBV = level == 2 || level == 4 || level == 6 || level == 7|| level == 8|| level == 9|| level == 10|| level == 11|| level == 12|| level == 13|| level == 14
                || level == 15|| level == 16;

        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append("Hiện tại ").append(trangBi.template.name);
        if (level > 0) {
            text.append(" [+").append(level).append("]\n");
        } else {
            text.append("\n");
        }
        text.append(ConstFont.BOLD_DARK).append(trangBi.getOptionInfoUpgrade()).append("\n");
        text.append(ConstFont.BOLD_BLUE).append("Sau khi nâng cấp [+").append(level + 1).append("]\n");
        text.append(ConstFont.BOLD_GREEN).append(trangBi.getOptionInfoUpgradeFinal()).append("\n");
    //    text.append(ConstFont.BOLD_BLUE).append("Tỉ lệ thành công: ").append(getRatio(level)).append("%\n");
        text.append(daNangCap.quantity >= da ? ConstFont.BOLD_BLUE : ConstFont.BOLD_RED).append("Cần ").append(da).append(" ").append(daNangCap.template.name).append("\n");
        text.append(player.inventory.gold >= gold ? ConstFont.BOLD_BLUE : ConstFont.BOLD_RED).append("Cần ").append(Util.numberToMoney(gold)).append(" vàng");
        if (canUseDBV) {
            text.append("\n");
            text.append(ConstFont.BOLD_BLUE).append("Nếu thất bại sẽ rớt xuống [+").append(level - 1).append("]\n");
            text.append(ConstFont.BOLD_RED).append("Nếu dùng đá bảo vệ sẽ không bị rớt cấp.");
        }
        if (daNangCap.quantity < da) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    text.toString(), "Còn thiếu\n" + Util.numberToMoney(da - daNangCap.quantity) + " " + daNangCap.template.name);
            return;
        }

        if (player.inventory.gold < gold) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    text.toString(), "Còn thiếu\n" + Util.numberToMoney(gold - player.inventory.gold) + " vàng");
            return;
        }

        if (canUseDBV && (daBaoVe != null || daBaoVeKhoa != null)) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    text.toString(), "Nâng cấp", "Nâng cấp\ndùng đá\nbảo vệ", "Đóng");
            return;
        }

        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                text.toString(), "Nâng cấp", "Đóng");
    }

    public static void nangCapVatPham(Player player, boolean useDBV) {
        if (player.combine.itemsCombine.size() != 2) {
            return;
        }

        Item trangBi = null;
        Item daNangCap = null;
        for (Item item : player.combine.itemsCombine) {
            if (item.template.type < 5) {
                trangBi = item;
            } else if (item.isDaNangCap()) {
                daNangCap = item;
            }
        }
        if (trangBi == null || daNangCap == null || !trangBi.canNangCapWithNDC(daNangCap)) {
            return;
        }
        Item daBaoVe = InventoryService.gI().findItemBag(player, 987);
        Item daBaoVeKhoa = InventoryService.gI().findItemBag(player, 1143);
        long level = trangBi.getOptionParam(72);
        long gold = getGold(trangBi);
        long da = getDa(trangBi);

        boolean canUseDBV = level == 2 || level == 4 || level == 6 || level == 7|| level == 8|| level == 9|| level == 10|| level == 11|| level == 12|| level == 13|| level == 14
                || level == 15|| level == 16;
        if (daNangCap.quantity < da || player.inventory.gold < gold || level >= CombineService.MAX_LEVEL_ITEM) {
            return;
        }
        if (canUseDBV && useDBV && daBaoVe == null && daBaoVeKhoa == null) {
            return;
        }
        if (Util.isTrue(getRatio(level), 100)) {
            for (Item.ItemOption io : trangBi.itemOptions) {
                if (io.isOptionCanUpgrade()) {
                    io.param += (io.param * 6 / 100);
                }
            }
            trangBi.addOptionParam(72, 1);
            if (trangBi.getOptionParam(72) == 16) {
                 trangBi.itemOptions.add(new ItemOption(75, 1));
                trangBi.itemOptions.add(new ItemOption(146, 1));
            }
            CombineService.gI().sendEffectSuccessCombine(player);
            if (level > 1) {
                Service.gI().chatJustForMe(player, player, player.name + ": Vừa nâng cấp thành công " + trangBi.template.name + " lên +" + (level + 1));
            }
            if (level > 5 && !player.isAdmin()) {
                ServerNotify.gI().notify(player.name + ": Vừa nâng cấp thành công " + trangBi.template.name + " lên +" + (level + 1));
            }
        } else {
            if (canUseDBV) {
                if (!useDBV || (daBaoVe == null && daBaoVeKhoa == null)) {
                    if (trangBi.template.type < 4) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.isOptionCanUpgrade()) {
                                io.param -= (io.param * 11 / 100);
                            }
                        }
                    }
                    trangBi.subOptionParam(72, 1);
                    trangBi.addOptionParam(209, 1);
                     trangBi.addOptionParam(146, 1);
                }
            }
            CombineService.gI().sendEffectFailCombine(player);
        }
        if (canUseDBV && useDBV) {
            if (daBaoVe != null) {
                InventoryService.gI().subQuantityItemsBag(player, daBaoVe, 1);
            } else {
                InventoryService.gI().subQuantityItemsBag(player, daBaoVeKhoa, 1);
            }
        }
        player.inventory.gold -= gold;
        InventoryService.gI().subQuantityItemsBag(player, daNangCap, (int) da);
        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
    }

}
