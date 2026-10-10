package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import static models.Combine.CombineUtil.getOptionDaPhaLe;
import static models.Combine.CombineUtil.getParamDaPhaLe;
import player.Player;
import services.InventoryService;
import services.Service;

public class EpSaoTrangBi {

    public static int getGem(long star) {
        if (star == 7) {
            return 200;
        } else if (star == 8) {
            return 300;
        } else {
            return 1000;
        }
    }

    public static boolean isTrangBiEpPhaLeHoa(Item item) {
        // [10/10/2026] Ep Sao Pha Le CHI dung cho 5 mon: Ao, Quan, Gang, Giay, Nhan (type 0-4).
        // Cai trang / phu kien / giap tap luyen (type 32) phai dung "Ep Sao Phu Kien".
        return item != null && item.isNotNullItem() && item.template.type < 5;
    }

    public static boolean isDaPhaLe(Item item) {

        if (item != null && item.isNotNullItem()) {
            return item.template.type == 31 || item.template.type == 30 || item.template.type == 12
                    || (item.template.id >= 925 && item.template.id <= 931);
        }
        return false;
    }

    public static void showInfoCombine(Player player) {
         Item luongbac = null;
            luongbac = InventoryService.gI().findItemBag(player, 1271);
            if (luongbac == null || luongbac.quantity < 10000) {

                Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần ít Nhất 10000 Lượng Bạc Trở Lên\nChi Phí Cơ Bản\nFam quái Và Boss Kiếm");
                return;
            }
        if (player.combine.itemsCombine.size() != 2) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị có lỗ sao pha lê và 1 loại ngọc để ép vào.");
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        boolean coCaiTrangHoacPhuKien = false;
        for (Item item : player.combine.itemsCombine) {
            if (isTrangBiEpPhaLeHoa(item)) {
                trangBi = item;
            } else if (item.isDaPhaLeEpSao()) {
                daPhaLe = item;
            } else if (item.canPhaLeHoa()) {
                coCaiTrangHoacPhuKien = true;
            }
        }
        if (trangBi == null || !trangBi.isNotNullItem() || daPhaLe == null || !daPhaLe.isNotNullItem()) {
            if (coCaiTrangHoacPhuKien) {
                Service.gI().sendDialogMessage(player, "Ép Sao Pha Lê chỉ dùng cho Áo, Quần, Găng, Giày, Nhẫn.\n"
                        + "Cải trang / phụ kiện / giáp tập luyện hãy dùng chức năng 'Ép Sao Phụ Kiện'.");
                return;
            }
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị có lỗ sao pha lê và 1 loại ngọc để ép vào.");
            return;
        }
        long star = trangBi.getOptionParam(102);
        long starEmpty = trangBi.getOptionParam(107);
        long cuongHoa = trangBi.getOptionParam(228);

        if (star >= starEmpty) {
            if (starEmpty <= 0) {
                Service.gI().sendDialogMessage(player, "Trang bị chưa có ô Sao Pha Lê nào.\n"
                        + "Hãy 'Pha Lê Hóa' (theo cấp VIP) để đục lỗ trước rồi mới ép sao.");
            } else {
                Service.gI().sendDialogMessage(player, "Trang bị này đã ép đầy " + starEmpty + " ô Sao Pha Lê.");
            }
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(trangBi.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK).append(star >= 7000 ? trangBi.getOptionInfoCuongHoa(daPhaLe) : trangBi.getOptionInfo(daPhaLe)).append("\n");
        text.append(player.inventory.gem < getGem(star) ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE).append("Cần " + getGem(star) + " ngọc");
        if (player.inventory.gem < getGem(star)) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(), "Còn thiếu\n" + (getGem(star) - player.inventory.gem) + " ngọc");
            return;
        }
        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(), "Nâng cấp\n" + getGem(star) + " ngọc", "Từ chối");
    }

    public static void epSaoTrangBi(Player player) {
        if (player.combine.itemsCombine.size() == 2) {
            int gem = player.combine.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
           
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combine.itemsCombine) {
                if (isTrangBiEpPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = (int) io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = (int) io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    long param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(74, 0));
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {

                        trangBi.itemOptions.add(new ItemOption(102, 1));
//                        trangBi.itemOptions.add(new ItemOption(30, 1));
                    }// ru lai di a

                    if (optionStar != null) {
                        if (!trangBi.haveOption(141) && optionStar.param == 18 && trangBi.template.type < 5) {
                            trangBi.itemOptions.add(new ItemOption(75, 1));//99s
                            trangBi.itemOptions.add(new ItemOption(141, 1));//99s

                        } else if (!trangBi.haveOption(142) && optionStar.param == 30 && trangBi.template.type < 5) {
                            trangBi.removeOption(141);
                            trangBi.itemOptions.add(new ItemOption(142, 1));//200s
                        } else if (!trangBi.haveOption(143) && optionStar.param == 45 && trangBi.template.type < 5) {
                            trangBi.removeOption(142);
                            trangBi.itemOptions.add(new ItemOption(143, 1));//300s
                        }
                        if (!trangBi.haveOption(144) && optionStar.param == 65 && trangBi.template.type < 5) {
                            trangBi.removeOption(143);
                            trangBi.itemOptions.add(new ItemOption(144, 1));//99s
                        } else if (!trangBi.haveOption(136) && optionStar.param == 99 && trangBi.template.type < 5) {
                            trangBi.removeOption(144);
                            trangBi.itemOptions.add(new ItemOption(136, 1));//200s
                        } else if (!trangBi.haveOption(137) && optionStar.param == 200 && trangBi.template.type < 5) {
                            trangBi.removeOption(136);
                            trangBi.itemOptions.add(new ItemOption(137, 1));//300s

                        } else if (!trangBi.haveOption(138) && optionStar.param == 300 && trangBi.template.type < 5) {
                            trangBi.removeOption(137);
                            trangBi.itemOptions.add(new ItemOption(138, 1));//300s
                        } else if (!trangBi.haveOption(139) && optionStar.param == 500 && trangBi.template.type < 5) {
                            trangBi.removeOption(138);
                            trangBi.itemOptions.add(new ItemOption(139, 1));//300s

                        } else if (!trangBi.haveOption(140) && optionStar.param == 700 && trangBi.template.type < 5) {
                            trangBi.removeOption(139);
                            trangBi.itemOptions.add(new ItemOption(140, 1));//300s

                        } else if (!trangBi.haveOption(145) && optionStar.param == 999 && trangBi.template.type < 5) {
                            trangBi.removeOption(140);
                            trangBi.itemOptions.add(new ItemOption(145, 1));//300s
                        }
                    }

                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    CombineService.gI().sendEffectSuccessCombine(player);
                    InventoryService.gI().subLuongBac(player, 10000);
                }
                InventoryService.gI().sendItemBag(player);
                Service.gI().sendMoney(player);
                CombineService.gI().reOpenItemCombine(player);
            }
        }
    }
}
