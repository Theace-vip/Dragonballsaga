package models.Combine.manifest;

import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

public class NangCapBongTai {

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            Item bongtai = null;
            Item manhvobt = null;
            for (Item item : player.combine.itemsCombine) {
                if (checkbongtai(item)) {
                    bongtai = item;
                } else if (item.template.id == 933) {
                    manhvobt = item;
                }
            }

            if (bongtai != null && manhvobt != null) {
                int level = 0;
                for (Item.ItemOption io : bongtai.itemOptions) {
                    if (io.optionTemplate.id == 243) {
                        level = (int) io.param;
                        break;
                    }
                }
                if (level < 4) {
                    int lvbt = lvbt(bongtai);
                    int countmvbt = getcountmvbtnangbt();
                    player.combine.goldCombine = getGoldnangbt();
                    player.combine.gemCombine = getGoldnangbt();
                    player.combine.ratioCombine = getRationangbt(lvbt);

                    String npcSay = "Bông tai Porata Cấp: " + lvbt + " \n|2|";
                    for (Item.ItemOption io : bongtai.itemOptions) {
                        npcSay += io.getOptionString() + "\n";
                    }
                    npcSay += "|7|Tỉ lệ thành công: " + player.combine.ratioCombine + "%" + "\n";
                    if (manhvobt.quantity >= countmvbt) {
                        if (player.combine.goldCombine <= player.diemfam) {
                            if (player.combine.gemCombine <= player.diemfam) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combine.goldCombine)
                                        + " điểm fam";
                                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(
                                        player.combine.gemCombine - player.diemfam) + " điểm fam";
                                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu "
                                    + Util.numberToMoney(player.combine.goldCombine - player.diemfam)
                                    + " điểm fam";
                            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        npcSay += "Còn thiếu " + Util.numberToMoney(countmvbt - manhvobt.quantity)
                                + " Mảnh vỡ bông tai";
                        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Đã đạt cấp tối đa! Nâng con cặc :)))", "Đóng");
                }
            } else {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 Bông tai Porata 2 hoặc SS và Mảnh vỡ bông tai", "Đóng");
            }
        } else {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 Bông tai Porata cấp 2 hoặc SS và Mảnh vỡ bông tai", "Đóng");
        }
    }

    public static void nangCapBongTai(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            int gold = player.combine.goldCombine;
            if (player.diemfam < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ Điểm Fam");
                return;
            }
            int gem = player.combine.gemCombine;
            if (player.diemfam< gem) {
                Service.getInstance().sendThongBao(player, "Không đủ Điểm Fam");
                return;
            }
            Item bongtai = null;
            Item manhvobt = null;
            for (Item item : player.combine.itemsCombine) {
                if (checkbongtai(item)) {
                    bongtai = item;
                } else if (item.template.id == 933) {
                    manhvobt = item;
                }
            }
            if (bongtai != null && manhvobt != null) {
                int level = 0;
                for (Item.ItemOption io : bongtai.itemOptions) {
                    if (io.optionTemplate.id == 243) {
                        level = (int) io.param;
                        break;
                    }
                }
                if (level < 4) {
                    int lvbt = lvbt(bongtai);
                    int countmvbt = getcountmvbtnangbt();
                    if (countmvbt > manhvobt.quantity) {
                        Service.getInstance().sendThongBao(player, "Không đủ Mảnh vỡ bông tai");
                        return;
                    }
                    player.diemfam -= gold;
                    player.diemfam -= gem;

                    if (Util.isTrue(player.combine.ratioCombine, 100)) {
                        bongtai.template = ItemService.gI().getTemplate(getidbtsaukhilencap(lvbt));
                        bongtai.itemOptions.clear();
                        bongtai.itemOptions.add(new Item.ItemOption(243, lvbt + 1));

                        CombineService.gI().sendEffectSuccessCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 9999);
                    } else {
                        CombineService.gI().sendEffectFailCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 999);
                    }
                    InventoryService.gI().sendItemBag(player);
                    Service.getInstance().sendMoney(player);
                    CombineService.gI().reOpenItemCombine(player);
                }
            }
        }
    }

    private static boolean checkbongtai(Item item) {
        return     item.template.id == 454||

                item.template.id == 921
                || item.template.id == 1668
                || item.template.id == 1669
                || item.template.id == 1670;
    }

    private static int getGoldnangbt() {
        return 5000;
    }

    private static int getgemdnangbt() {
        return 1;
    }

    private static int getcountmvbtnangbt() {
        return 9999;
    }

    private static float getRationangbt(int lvbt) { // tile dap do chi hat mit
        return switch (lvbt) {
            case 0 ->
                7f;
            case 1 ->
                5f;
            case 2 ->
                2f;
            case 3 ->
                1f;
            case 4 ->
                0.1f;    
            default ->
                0;

        };
    }

    private static int lvbt(Item bongtai) {
        return switch (bongtai.template.id) {
            case 454 ->
                0;
            case 921 ->
                1;
            case 1668 ->
                2;
            case 1669 ->
                3;
            case 1670 ->
                4;
            default ->
                0;
        };
    }

    private static short getidbtsaukhilencap(int lvbtcu) {
        return switch (lvbtcu) {
              case 0 ->
                921;
            case 1 ->
                1668;
            case 2 ->
                1669;
            case 3 ->
                1670;
            default ->
                0;
        };
    }
}
