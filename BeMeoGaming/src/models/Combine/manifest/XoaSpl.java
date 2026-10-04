/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author Administrator
 */
public class XoaSpl {

    private static final long COST = 5000000000l;

    public static void showCombine(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combine.itemsCombine.size() == 2) {
                Item daPhapSu = null;
                Item itemBongToi = null;
                for (Item item_ : player.combine.itemsCombine) {
                    // id để pháp sư
                    if (item_.template.id == 1010) {
                        daPhapSu = item_;
                    } else if (item_.haveOption(102)) {
                        itemBongToi = item_;
                    }

                }
                if (daPhapSu == null) {
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu Hỏa Băng", "Đóng");
                    return;
                }
                if (itemBongToi == null) {
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị để xóa spl", "Đóng");
                    return;
                }
                player.combine.ratioCombineDaPS = 50;
                String npcSay = "|2|Hiện tại " + itemBongToi.template.name + "\n|0|";
                for (ItemOption io : itemBongToi.itemOptions) {
                    if (io.optionTemplate.id != 72) {
                        npcSay += io.getOptionString() + "\n";
                    }
                }
                npcSay += "|2|Sau khi nâng cấp sẽ xóa một spl nhưng vẫn giữ nguyên chỉ số\n|7|"
                        + "\n|7|Tỉ lệ thành công: " + player.combine.ratioCombineDaPS + "%\n"
                        + "Cần " + Util.numberToMoney(COST) + " vàng";

                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
            } else {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể xóa lỗ spl", "Đóng");
            }
        } else {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
        }
    }

    public static void XoaSpl(Player player) {
        if (player.combine.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        Item itemBongToi = null;
        for (Item item_ : player.combine.itemsCombine) {
            // id để pháp sư
            if (item_.haveOption(102)) {
                itemBongToi = item_;
            }

        }

        if (itemBongToi == null) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị có thế xóa dòng");
            return;
        }
        for (ItemOption itopt : itemBongToi.itemOptions) {
            if (itopt.param < 1 && itopt.optionTemplate.id == 102) {
                Service.getInstance().sendThongBao(player, "Số sao đã đạt tối thiểu");
                return;
            }
        }
        // id đá ps
        if (player.combine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1010).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Hỏa Băng");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 1) {

            if (player.inventory.gold < COST) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để Nâng trang bị...");
                return;
            }

            player.inventory.gold -= COST;
            Item daPhapSu = player.combine.itemsCombine.stream().filter(item -> item.template.id == 1010).findFirst().get();
            if (daPhapSu.quantity < 100000) {
                Service.getInstance().sendThongBao(player, "Con còn thiếu " + (100000 - daPhapSu.quantity) + " Hỏa Băng để nâng cấp");
                return;
            }

            if (daPhapSu == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá Kim Cương");
                return;
            }
            if (itemBongToi == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị");
                return;
            }

            if (Util.isTrue(50, 100)) {
                CombineService.gI().sendEffectSuccessCombine(player);
                CombineService.gI().sendEffectSuccessCombine(player);
                for (ItemOption itopt : itemBongToi.itemOptions) {
                    if (itopt.optionTemplate.id == 102 && itopt.param > 0) {
                        itopt.param -= 1;
                        break;
                    }
                }

                Service.getInstance().sendThongBao(player, "Bạn đã nâng cấp trang bị thành công");
            } else {
                CombineService.gI().sendEffectFailCombine(player);
            }
            InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 100000);
            InventoryService.gI().sendItemBag(player);
            Service.getInstance().sendMoney(player);

            CombineService.gI().reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

}
