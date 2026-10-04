package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import jdbc.daos.PlayerDAO;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.Service;
import utils.Util;

public class NangChiSoBongTai {

    private static final int GOLD_MOCS_BONG_TAI = 0;
    private static final int GEM_MOCS_BONG_TAI = 85000;
    private static final int RATIO_NANG_CAP = 20;

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combine.itemsCombine) {
                switch (item.template.id) {
                    case 454, 921, 1668, 1669, 1670 ->
                        bongTai = item;
                    case 934 ->
                        manhHon = item;
                    case 935 ->
                        daXanhLam = item;
                    default -> {
                    }
                }
            }
            if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 9999) {

                player.combine.goldCombine = GOLD_MOCS_BONG_TAI;
                player.combine.gemCombine = GEM_MOCS_BONG_TAI;
                player.combine.ratioCombine = RATIO_NANG_CAP;

                String npcSay = "Bông tai Porata cấp "
                        + (bongTai.template.id == 1624 ? bongTai.template.id == 1625 ? "ProMax" : "ProMax" : "ProMax")
                        + " \n|2|";
                for (Item.ItemOption io : bongTai.itemOptions) {
                    npcSay += io.getOptionString() + "\n";
                }
                npcSay += "|7|Tỉ lệ thành công: " + player.combine.ratioCombine + "%" + "\n";
                if (player.combine.goldCombine <= player.getSession().vnd) {
                    if (player.combine.gemCombine <= player.diemfam) {
                        npcSay += "|1|Cần " + Util.numberToMoney(player.combine.goldCombine) + " Coin + Fam";
                        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần ");
                    } else {
                        npcSay += "Còn thiếu "
                                + Util.numberToMoney(player.combine.gemCombine - player.diemfam)
                                + " Coin + Fam";
                        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    npcSay += "Còn thiếu "
                            + Util.numberToMoney(player.combine.goldCombine - player.getSession().vnd)
                            + " Coin";
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 Bông tai Porata cấp ss hoặc sss, X9999 Mảnh hồn bông tai và 1000 Đá xanh lam", "Đóng");
            }
        } else {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 Bông tai Porata, X9999 Mảnh hồn bông tai và 1000 Đá xanh lam", "Đóng");
        }
    }

    public static void nangChiSoBongTai(Player player) {
        if (player.combine.itemsCombine.size() == 3) {
            int gold = player.combine.goldCombine;
            int gem = player.combine.gemCombine;

            if (player.getSession().vnd < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ 5K Coin Để Mở Chỉ Số 100%");
                return;
            }
            if (player.diemfam < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ điểm Fam Để Thực Hiện");
                return;
            }

            Item bongTai = null, manhHon = null, daXanhLam = null;
            for (Item item : player.combine.itemsCombine) {
                switch (item.template.id) {
                   case 454, 921, 1668, 1669, 1670 ->
                        bongTai = item;
                    case 934 ->
                        manhHon = item;
                    case 935 ->
                        daXanhLam = item;
                }
            }

            if (bongTai != null && daXanhLam != null && manhHon != null && manhHon.quantity >= 9999) {
                // Trừ tài nguyên
                 PlayerDAO.subcash(player, 0);
                player.diemfam-= gem;
                InventoryService.gI().subQuantityItemsBag(player, manhHon, 9999);
                InventoryService.gI().subQuantityItemsBag(player, daXanhLam, 1000);

                // Kiểm tra tỷ lệ thành công
                if (Util.isTrue(player.combine.ratioCombine, 50)) {
                    // Chỉ xóa chỉ số khi thành công
                    bongTai.itemOptions.clear();

                    // Danh sách chỉ số ngẫu nhiên
                    int[][] options = {
                        {50, 5, 1000}, {77, 5, 1000}, {103, 5, 1000}, {108, 5, 50}, {94, 5, 1000},
                        {14, 2, 30}, {80, 1, 5}, {108, 1, 30}, {88, 1, 5000}, {81, 1, 5}, {5, 1, 20}, {101, 1, 5000},
                        {100, 1, 10},   {15, 1, 50}, {16, 1, 50},
                        {17, 1, 50}, {18, 1, 50}, {78, 1, 1000}, {0, 1, 100000},{6, 1, 50000000},{7, 1, 50000000}
                    };

                    // Thêm chỉ số ngẫu nhiên
                    int[] option = options[Util.nextInt(0, options.length)];
                     bongTai.itemOptions.add(new Item.ItemOption(243, 1));
                    bongTai.itemOptions.add(new Item.ItemOption(option[0], Util.nextInt(option[1], option[2])));

                    CombineService.gI().sendEffectSuccessCombine(player);
                } else {
                    CombineService.gI().sendEffectFailCombine(player);
                    Service.getInstance().sendThongBao(player, "Hợp thành thất bại! Chỉ số cũ được giữ nguyên.");
                }

                // Cập nhật lại thông tin người chơi
                InventoryService.gI().sendItemBag(player);
                Service.getInstance().sendMoney(player);
                CombineService.gI().reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu để thực hiện!");
            }
        }
    }
}
