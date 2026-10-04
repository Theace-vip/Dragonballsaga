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
public class NangCapChanMenh {

    public static void showCombine(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            Item bongTai = null;
            Item manhVo = null;
            int star = 0;
            for (Item item : player.combine.itemsCombine) {
                if (item.template.id == 674) {
                    manhVo = item;
                } else if (item.template.id >= 1810 && item.template.id <= 1818) {
                    bongTai = item;
                    star = item.template.id - 1810;
                }
            }
            if (bongTai != null && bongTai.template.id == 1818) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cánh đã đạt cấp tối đa", "Đóng");
                return;
            }
            player.combine.DiemNangcap = CombineService.gI().getDiemNangcapChanmenh(star);
            player.combine.DaNangcap = CombineService.gI().dnsdapdo(star);
            player.combine.TileNangcap = CombineService.gI().getTiLeNangcapChanmenh(star);
            if (bongTai != null && manhVo != null && (bongTai.template.id >= 1810 && bongTai.template.id < 1818)) {
                String npcSay = bongTai.template.name + "\n|2|";
                for (ItemOption io : bongTai.itemOptions) {
                    npcSay += io.getOptionString() + "\n";
                }
                npcSay += "|7|Tỉ lệ thành công: " + player.combine.TileNangcap + "%" + "\n";
                if (player.combine.DiemNangcap <= player.diemfam) {
                    npcSay += "|1|Cần " + Util.numberToMoney(player.combine.DiemNangcap) + " Điểm TrainFam";
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combine.DaNangcap + " Đá Ngũ sắc");
                } else {
                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combine.DiemNangcap - player.diemfam) + " Điểm TrainFam";
                    CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần Chân Mệnh Lv1 và Đá Ngũ sắc", "Đóng");
            }
        } else {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần Chân Mệnh Lv1 và Đá Ngũ sắc", "Đóng");
        }
    }

    public static void NangCapChanMenh(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            int diem = player.combine.DiemNangcap;
            if (player.diemfam < diem) {
                Service.getInstance().sendThongBao(player, "Không đủ Điểm TrainFam để thực hiện");
                return;
            }
            Item chanmenh = null;
            Item dahoangkim = null;
            int capbac = 0;
            for (Item item : player.combine.itemsCombine) {
                if (item.template.id == 674) {
                    dahoangkim = item;
                } else if (item.template.id >= 1810 && item.template.id < 1818) {
                    chanmenh = item;
                    capbac = item.template.id - 1809;
                }
            }
            int soluongda = player.combine.DaNangcap;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda) {
                if (chanmenh != null && (chanmenh.template.id >= 1810 && chanmenh.template.id < 1818)) {
                    player.diemfam -= diem;
                    if (Util.isTrue(player.combine.TileNangcap, 1)) {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        CombineService.gI().sendEffectSuccessCombine(player);
                        switch (capbac) {
                            case 1:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 1000));
                                chanmenh.itemOptions.add(new ItemOption(50, 1000));
                                chanmenh.itemOptions.add(new ItemOption(77, 1000));
                                chanmenh.itemOptions.add(new ItemOption(103, 100));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 2:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 2000));
                                chanmenh.itemOptions.add(new ItemOption(50, 2000));
                                chanmenh.itemOptions.add(new ItemOption(77, 2000));
                                chanmenh.itemOptions.add(new ItemOption(103, 2000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 3:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 3000));
                                chanmenh.itemOptions.add(new ItemOption(77, 3000));
                                chanmenh.itemOptions.add(new ItemOption(103, 3000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 4:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 4000));
                                chanmenh.itemOptions.add(new ItemOption(77, 4000));
                                chanmenh.itemOptions.add(new ItemOption(103, 4000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 5:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 5000));
                                chanmenh.itemOptions.add(new ItemOption(77, 5000));
                                chanmenh.itemOptions.add(new ItemOption(103, 5000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 6:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 6000));
                                chanmenh.itemOptions.add(new ItemOption(77, 6000));
                                chanmenh.itemOptions.add(new ItemOption(103, 6000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 7:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 7000));
                                chanmenh.itemOptions.add(new ItemOption(77, 7000));
                                chanmenh.itemOptions.add(new ItemOption(103, 7000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 8:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 8000));
                                chanmenh.itemOptions.add(new ItemOption(77, 8000));
                                chanmenh.itemOptions.add(new ItemOption(103, 8000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;
                            case 9:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new ItemOption(59, 100));
                                chanmenh.itemOptions.add(new ItemOption(50, 9000));
                                chanmenh.itemOptions.add(new ItemOption(77, 9000));
                                chanmenh.itemOptions.add(new ItemOption(103, 9000));
                                chanmenh.itemOptions.add(new ItemOption(245, 10));
                                break;

                            default:
                                break;
                        }
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
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
