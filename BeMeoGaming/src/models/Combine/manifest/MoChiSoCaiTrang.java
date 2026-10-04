package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class MoChiSoCaiTrang {

    public static void showCombine(Player player) {
        if (player.combine.itemsCombine.size() == 0) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "•Em Thiếu Gì ? Thế•\n"
                    + "Hãy đưa Anh Đồ chưa kích hoạt chỉ số Vào Đây!"
                    + "••\n",
                    "Đóng");
            return;
        }

        if (player.combine.itemsCombine.size() == 2) {
            if (player.combine.itemsCombine.stream().filter(
                    item -> item.isNotNullItem() && (item.template.type == 5))
                    .count() < 1) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, ""
                        + "•Em Thiếu Cải Trang À ?•\n"
                        + "Thiếu cải trang kích hoạt Kìa\nMua Trên Shop Kìa\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n",
                        "Đóng");
                return;
            }
            if (player.combine.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.template.id == 1270)
                    .count() < 1) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, ""
                        + "•Em Lại Thiếu Vàng Lượng Rồi !•\n"
                        + "Thiếu Vàng Lượng Rồi"
                        + "••\n",
                        "Đóng");
                return;
            }

            String npcSay = "Em Muốn Mở Cải Trang Ramdom Chỉ Số  Không !";

            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Nâng cấp", "Từ chối");
        } else {
            if (player.combine.itemsCombine.size() > 2) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp Kìa Em\n Cần Cải Trang Và Vàng Lượng",
                        "Đóng");
                return;
            }
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
        }
    }

    public static void MoChiSoCaiTrang(Player player) {
        if (player.combine.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu Kìa Em ?\n Cần 1 Cải Trang Và Vàng Lượng?");
            return;
        }
        if (player.combine.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && (item.template.type == 5))
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu cải trang kích hoạt Roài");
            return;
        }
        if (player.combine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1270)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Vàng Lượng");
            return;
        }
        ///

        Item caiTrang = null;
        Item longDen = null;
        int checkOption = 0;
        for (Item item : player.combine.itemsCombine) {
            if (item.template.type == 5) {
                caiTrang = item;
            } else if (item.template.id == 1270) {
                longDen = item;
            }
        }
        for (ItemOption io : caiTrang.itemOptions) {
            if (io.optionTemplate.id == 241) {
                checkOption++;
            } else if (io.optionTemplate.id == 242) {
                checkOption = 0;
            }
        }
        if (checkOption == 0) {
            Service.getInstance().sendThongBao(player, "Yêu cầu Cải Trang chưa kích hoạt ! ");
            return;
        }

        if (caiTrang != null && longDen != null && longDen.quantity >= 20) {
            InventoryService.gI().subQuantityItemsBag(player, longDen, 20);
            caiTrang.itemOptions.clear();
            caiTrang.itemOptions.add(new ItemOption(59, Util.nextInt(1, 2)));
            caiTrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100000)));
            caiTrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100000)));
            caiTrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100000)));
            caiTrang.itemOptions.add(new ItemOption(94, Util.nextInt(1, 1000)));
            caiTrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100)));
            caiTrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 25)));
            caiTrang.itemOptions.add(new ItemOption(231, 0));
            CombineService.gI().sendEffectSuccessCombine(player);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendMoney(player);
            CombineService.gI().reOpenItemCombine(player);

            ///
        }
    }
}
