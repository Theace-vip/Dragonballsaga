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
import services.ItemService;
import services.Service;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class NangCapCanh {

    public static void showCombine(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            Item bongTai1 = null;
            Item manhVo1 = null;
            int star = 0;
            for (Item item : player.combine.itemsCombine) {
                if (item.template.id == 1332) {
                    manhVo1 = item;
                } else if (item.template.id >= 1240 && item.template.id <= 1245) {
                    bongTai1 = item;
                    star = item.template.id - 1240;
                }
            }
            if (bongTai1 != null && bongTai1.template.id == 1245) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cánh đã đạt cấp tối đa", "Đóng");
                return;
            }

            player.combine.DiemNangcap = CombineService.gI().getDiemNangcapcanh(star);
            player.combine.DaNangcap = CombineService.gI().dnsdapdo(star);
            player.combine.TileNangcap = CombineService.gI().getTiLeNangcapCanh(star);
            if (bongTai1 != null && manhVo1 != null && (bongTai1.template.id >= 1240 && bongTai1.template.id < 1245)) {
                String npcSay = bongTai1.template.name + "\n|2|";
                for (ItemOption io : bongTai1.itemOptions) {
                    npcSay += io.getOptionString() + "\n";
                }
                npcSay += "|7|Tỉ lệ thành công: " + player.combine.TileNangcap + "%" + "\n";
                if (player.combine.DiemNangcap <= player.point_sb) {
                    npcSay += "|1|Cần " + Util.numberToMoney(player.combine.DiemNangcap) + " Điểm Săn Boss";
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combine.DaNangcap + " Hàn Thiết");
                } else {
                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combine.DiemNangcap - player.point_sb) + " Điểm Săn Boss";
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần Cánh Lv1 và Thỏi Hàn Thiết", "Đóng");
            }
        } else {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần Cánh Lv1 và Thỏi Hàn Thiết", "Đóng");
        }
    }

    public static void NangCapCanh222(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            int diem = player.combine.DiemNangcap;
            int coin = player.combine.CoinNangcap;
            if (player.point_sb < diem) {
                Service.getInstance().sendThongBao(player, "Không đủ Điểm Săn Boss Để Thực Hiện");
                return;
            }

            Item chanmenh1 = null;
            Item dahoangkim1 = null;
            int capbac = 0;
            for (Item item : player.combine.itemsCombine) {
                if (item.template.id == 1332) {
                    dahoangkim1 = item;
                } else if (item.template.id >= 1240 && item.template.id < 1245) {
                    chanmenh1 = item;
                    capbac = item.template.id - 1239;
                }
            }
            int soluongda = player.combine.DaNangcap;
            if (dahoangkim1 != null && dahoangkim1.quantity >= soluongda) {
                if (chanmenh1 != null && (chanmenh1.template.id >= 1240 && chanmenh1.template.id < 1245)) {
                    player.point_sb -= diem;
                    if (Util.isTrue(player.combine.TileNangcap, 2)) {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim1, soluongda);
                        CombineService.gI().sendEffectSuccessCombine(player);
                        switch (capbac) {
                            case 1:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 100));
                                chanmenh1.itemOptions.add(new ItemOption(77, 100));
                                chanmenh1.itemOptions.add(new ItemOption(103, 100));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 2:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 200));
                                chanmenh1.itemOptions.add(new ItemOption(77, 200));
                                chanmenh1.itemOptions.add(new ItemOption(103, 200));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 3:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 300));
                                chanmenh1.itemOptions.add(new ItemOption(77, 300));
                                chanmenh1.itemOptions.add(new ItemOption(103, 300));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 4:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 400));
                                chanmenh1.itemOptions.add(new ItemOption(77, 400));
                                chanmenh1.itemOptions.add(new ItemOption(103, 400));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 5:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 500));
                                chanmenh1.itemOptions.add(new ItemOption(77, 500));
                                chanmenh1.itemOptions.add(new ItemOption(103, 500));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 6:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 600));
                                chanmenh1.itemOptions.add(new ItemOption(77, 600));
                                chanmenh1.itemOptions.add(new ItemOption(103, 600));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;
                            case 7:
                                chanmenh1.template = ItemService.gI().getTemplate(chanmenh1.template.id + 1);
                                chanmenh1.itemOptions.clear();
                                chanmenh1.itemOptions.add(new ItemOption(59, 100));
                                chanmenh1.itemOptions.add(new ItemOption(50, 700));
                                chanmenh1.itemOptions.add(new ItemOption(77, 700));
                                chanmenh1.itemOptions.add(new ItemOption(103, 700));
                                chanmenh1.itemOptions.add(new ItemOption(60, 1));
                                chanmenh1.itemOptions.add(new ItemOption(249, 1));
                                chanmenh1.itemOptions.add(new ItemOption(61, 1));
                                chanmenh1.itemOptions.add(new ItemOption(251, 1));
                                chanmenh1.itemOptions.add(new ItemOption(242, 10));
                                break;

                            default:
                                break;
                        }
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim1, soluongda);
                        CombineService.gI().sendEffectFailCombine(player);
                    }
                    InventoryService.gI().sendItemBag(player);
                    Service.getInstance().sendMoney(player);
                    CombineService.gI().reOpenItemCombine(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ Đá ngũ sắc để thực hiện");
            }
        }
    }

}
