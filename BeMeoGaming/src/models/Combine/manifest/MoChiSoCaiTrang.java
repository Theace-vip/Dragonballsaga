package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.Service;
import utils.Util;

/**
 * Mo chi so cai trang - 3 muc chi phi (chon khi bam Nang cap):
 *   0 = 20 Luong Vang  -> chi so x1 (nhu cu)
 *   1 = 40 Luong Vang  -> chi so x2
 *   2 = 100 Luong Vang -> chi so x6
 *
 * @author Administrator
 */
public class MoChiSoCaiTrang {

    private static final long[] COST = {20L, 40L, 100L};
    private static final long[] MULT = {1L, 2L, 6L};

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

            String npcSay = "Chọn mức Mở Chỉ Số cho cải trang:\n"
                    + "• 20 Lượng: chỉ số x1 (giữ như hiện tại)\n"
                    + "• 40 Lượng: chỉ số random x2\n"
                    + "• 100 Lượng: chỉ số random x6\n"
                    + "(Chi phí trừ từ Lượng Vàng trong túi)";

            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay,
                    "Mở 20 Lượng\n(chỉ số x1)",
                    "Mở 40 Lượng\n(chỉ số x2)",
                    "Mở 100 Lượng\n(chỉ số x6)",
                    "Từ chối");
        } else {
            if (player.combine.itemsCombine.size() > 2) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp Kìa Em\n Cần Cải Trang Và Vàng Lượng",
                        "Đóng");
                return;
            }
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
        }
    }

    public static void MoChiSoCaiTrang(Player player) {
        MoChiSoCaiTrang(player, 0);
    }

    /**
     * @param level 0 = 20 Luong (x1), 1 = 40 Luong (x2), 2 = 100 Luong (x6)
     */
    public static void MoChiSoCaiTrang(Player player, int level) {
        if (level < 0 || level > 2) {
            level = 0;
        }
        long cost = COST[level];
        long mult = MULT[level];

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

        if (caiTrang == null || longDen == null) {
            return;
        }
        if (longDen.quantity < cost) {
            Service.getInstance().sendThongBao(player, "Không đủ Lượng Vàng! Mức này cần " + cost + " Lượng");
            return;
        }

        InventoryService.gI().subQuantityItemsBag(player, longDen, (int) cost);
        // chi so random: nhan them MULT khi chon muc cao (option 59/231 la nhan/cohieu -> khong nhan)
        caiTrang.itemOptions.clear();
        caiTrang.itemOptions.add(new ItemOption(59, Util.nextInt(1, 2)));
        caiTrang.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100000) * mult));
        caiTrang.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100000) * mult));
        caiTrang.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100000) * mult));
        caiTrang.itemOptions.add(new ItemOption(94, Util.nextInt(1, 1000) * mult));
        caiTrang.itemOptions.add(new ItemOption(5, Util.nextInt(1, 100) * mult));
        caiTrang.itemOptions.add(new ItemOption(107, Util.nextInt(1, 25) * mult)); // lo sao cung nhan he so
        caiTrang.itemOptions.add(new ItemOption(231, 0));
        CombineService.gI().sendEffectSuccessCombine(player);
        InventoryService.gI().sendItemBag(player);
        Service.getInstance().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
        Service.getInstance().sendThongBao(player, "Mở chỉ số x" + mult + " thành công, trừ " + cost + " Lượng Vàng");
    }
}
