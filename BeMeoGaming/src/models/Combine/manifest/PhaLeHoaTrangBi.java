package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import models.Combine.CombineService;
import player.Player;
import server.ServerNotify;
import services.InventoryService;
import services.Service;
import utils.Util;

public class PhaLeHoaTrangBi {

    public static short capSaoTheoVip(Item item, Player player) {
        switch (player.Saga_VIP) {
            case 1:
                return 8;
            case 2:
                return 12;
            case 3:
                return 15;
            case 4:
                return 18;
            case 5:
                return 25;
            case 6:
                return 30;
            case 7:
                return 30;
            case 8:
                return 30;
            case 9:
                return 30;
            case 10:
                return 30;
            case 11:
                return 30;
            case 12:
                return 30;
            case 13:
                return 99;

            default:
                return 6;

        }
    }

    private static float getRatio(long star) {
        if (star == 0) {
            return 30f;
        } else if (star == 1) {
            return 20f;
        } else if (star == 2) {
            return 10f;
        } else if (star == 3) {
            return 8f;
        } else if (star == 4) {
            return 5f;
        } else if (star == 5) {
            return 2f;
        } else if (star == 6) {
            return 1f;
        } else if (star == 7) {
            return 0.5f;
        } else if (star == 8) {
            return 0.2f;
        } else {
            return 0.1f;
        }
    }

    private static float getRatioPhaLeHoascam(int star) {
        //    switch (star) {
        switch (star) {
            case 0:
                return 10f;//là giảm ở đây fa là dòng đầu nó giảm theo ấy
            case 1:
                return 9f;

            default:// 8s tro di mac dinh
                return 2.92f;
        }

    }

    private static float ratioTheoVip(Player player) {
        if (player.Saga_VIP >= 1 && player.Saga_VIP <= 13) {
            return player.Saga_VIP * 0.01f; // Mỗi VIP tăng 1%
        }
        return 1f; // Nếu VIP ngoài phạm vi 1-8, không tăng tỷ lệ
    }

    private static String getRatioStr(long star) {
        float ratio = getRatio(star);
        if (ratio < 1) {
            ratio = 1;
        }
        return String.valueOf(ratio);
    }

    private static long getGold(long star) {
        if (star == 0) {
            return 50_000_000;
        } else if (star == 1) {
            return 100_000_000;
        } else {
            return 500_000_000;
        }
    }

    private static int getGem(long star) {
        if (star == 0) {
            return 100;
        } else if (star == 1) {
            return 200;
        } else if (star == 2) {
            return 300;
        } else if (star == 3) {
            return 400;
        } else if (star == 4) {
            return 500;
        } else if (star == 5) {
            return 600;
        } else if (star == 6) {
            return 700;
        } else if (star == 7) {
            return 800;
        } else if (star == 8) {
            return 900;
        } else {
            return 1000;
        }
    }

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 1) {
            Service.gI().sendDialogMessage(player, "Trang bị không phù hợp");
            return;//clm mấy ở nhà khó làm vc lm k quen
        }
        Item item = player.combine.itemsCombine.get(0);
        if (item == null || !item.isNotNullItem()) {
            return;
        }
        if (item.isHaveOption(93)) {
            Service.gI().sendDialogMessage(player, "Trang bị có hạn sử dụng, không thể thực hiện");
            return;
        }
        if (!item.canPhaLeHoa()) {
            Service.gI().sendDialogMessage(player, "Trang bị không phù hợp");
            return;
        }
        long star = item.getOptionParam(107);
        int gem = getGem(star);
        long gold = getGold(star);
        if (star >= capSaoTheoVip(item, player)) {
            Service.gI().sendDialogMessage(player, "Đã đạt số pha lê tối đa");
            return;
        }
        float totalRatio = getRatio(star) + getRatioPhaLeHoascam((int) star) + ratioTheoVip(player); // 100 + 80 + 1 = 181/ 100 :))))
        float sosaodcdap = capSaoTheoVip(item, player);
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(item.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK).append(item.getOptionInfo()).append("\n");
        text.append(ConstFont.BOLD_GREEN).append(star + 1).append(" ô Sao Pha Lê\n");
        text.append(ConstFont.BOLD_GREEN).append("Số Sao Được Đục: " + sosaodcdap + " Lỗ: VIP Càng Cao + Đục Nhiều Lỗ\n");
       // text.append(ConstFont.BOLD_RED).append("Tỉ lệ thành công: " + totalRatio + "%\n");
       text.append(ConstFont.BOLD_RED).append("Tỉ lệ thành công: Không Xác Định\n");
        text.append(ConstFont.BOLD_RED).append("Tỉ lệ Thêm Theo vip: " + ratioTheoVip(player) + "%\n");
         text.append(ConstFont.BOLD_RED).append("Điểm SK Đập Đồ : " + player.point_dapdo + " Điểm\n");
        text.append(player.inventory.gold < gold ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE).append("Cần ").append(Util.numberToMoney(gold)).append(" vàng");
        if (player.inventory.gold < gold) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                    "Còn thiếu\n" + Util.numberToMoney(gold - player.inventory.gold) + " vàng");
            return;
        }
        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                    "Nâng cấp\n10 Lần\n" + gem + " ngọc",  "Nâng cấp\n1 Lần\n" + gem + " ngọc", "Từ chối");
    }

    public static void phaLeHoa(Player player, int... numm) {
        int n = 1;
        if (numm.length > 0) {
            n = numm[0];
        }
        if (!player.combine.itemsCombine.isEmpty()) {
            Item item = player.combine.itemsCombine.get(0);
            if (item == null || !item.isNotNullItem() || item.isHaveOption(93) || !item.canPhaLeHoa()) {
                return;
            }
            long star = item.getOptionParam(107);
            if (star >= capSaoTheoVip(item, player)) {
                return;
            }
            long gold = getGold(star);
            int gem = getGem(star);
            if (n == 1) {
                if (player.inventory.gold < gold) {
                    return;
                } else if (player.inventory.gem < gem) {
                    Service.gI().sendServerMessage(player, "Bạn không đủ ngọc, còn thiếu " + (gem - player.inventory.gem) + " ngọc nữa");
                    return;
                }
            }
            int num = 0;
            boolean success = false;
            for (int i = 0; i < n; i++) {
                num = i + 1;
                if (player.inventory.gem < gem) {
                    Service.gI().sendServerMessage(player, "Sau " + i + " lần nâng cấp thất bại, bạn không đủ ngọc để tiếp tục.");
                    break;
                }
                if (player.inventory.gold < gold) {
                    Service.gI().sendServerMessage(player, "Sau " + i + " lần nâng cấp thất bại, bạn không đủ vàng để tiếp tục.");
                    break;
                }
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                if (Util.isTrue(getRatio(star) + ratioTheoVip(player), 100)) {//là 1 sao = 80% tile gốc + vip 8 là 8 = 88/100 ok mà vừa xóa nhầm j kkiafks tại viết hàm ratio vip kahsc r mà
                    success = true;
                    break;
                }
            }
            if (success) {
                item.addOptionParam(107, 1);

                if (star > 1 && !player.isAdmin()) {
                    Service.gI().sendThongBaoBenDuoi("Chúc mừng " + player.name + " vừa pha lê hóa "
                            + "thành công " + item.template.name + " lên " + (star + 1) + " sao pha lê");
                }
                if (n > 1) {
                    Service.gI().sendServerMessage(player, "Thành công sau " + num + " lần nâng cấp.");
                }
                CombineService.gI().sendEffectSuccessCombine(player);
                player.point_dapdo +=1;
                Service.gI().sendMoney(player);
              
            } else {

                ItemOption xit = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 218 ) {
                        xit = io;
                        break;
                    }
                }
                if (xit == null) {
                     item.itemOptions.add(new ItemOption(250, 0));
                    item.itemOptions.add(new ItemOption(218, 1));
                   // item.itemOptions.add(new ItemOption(30, 1));
                } else {
                    xit.param++;
                }
                CombineService.gI().sendEffectFailCombine(player);
                
            }
            InventoryService.gI().sendItemBag(player);
            Service.gI().sendMoney(player);
            CombineService.gI().reOpenItemCombine(player);
        }
    }

}
