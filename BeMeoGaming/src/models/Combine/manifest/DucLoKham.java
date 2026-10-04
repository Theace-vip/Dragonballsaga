/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import server.ServerNotify;
import services.InventoryService;
import services.Service;
import utils.Util;

/**
 *
 * @author bemeo
 */
public class DucLoKham {

    private static float getRatio(long star) {
        if (star == 0) {
            return 20f;
        } else if (star == 1) {
            return 10f;
        } else if (star == 2) {
            return 7f;
        } else if (star == 3) {
            return 5f;
        } else if (star == 4) {
            return 5f;
        } else if (star == 5) {
            return 1f;
        } else if (star == 6) {
            return 2f;
        } else if (star == 7) {
            return 0.2f;
        } else if (star == 8) {
            return 0.01f;
          } else if (star == 9) {
            return 0.01f;    
            
        } else {
            return 0.001f;
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
                return 2f;
        }

    }

    private static float ratioTheoVip(Player player) {
        if (player.Saga_VIP >= 1 && player.Saga_VIP <= 13) {
            return player.Saga_VIP * 1f; // Mỗi VIP tăng 1%
        }
        return 0f; // Nếu VIP ngoài phạm vi 1-8, không tăng tỷ lệ
    }

    private static String getRatioStr(long star) {
        int ratio = (int) getRatio(star);
        if (ratio < 1) {
            ratio = 1;
        }
        return String.valueOf(ratio);
    }

    public static void showInfoCombine(Player player) {
        if (player.combine.itemsCombine.size() != 3) {
            Service.gI().sendDialogMessage(player, "Thiếu nguyên liệu.");
            return;
        }
        Item coNe = null;
        Item zenNiNe = null;
        Item trangBiKham = null;

        for (Item item : player.combine.itemsCombine) {
            if (item == null || !item.isNotNullItem()) {
                continue;
            }

            if (item.template.id == 457) {
                coNe = item;
            } else if (item.template.id == 1270) {
                zenNiNe = item;
            } else if (item.isTrangBiKham()) {
                trangBiKham = item;
            }
        }

        if (coNe == null || zenNiNe == null || trangBiKham == null) {
            Service.gI().sendDialogMessage(player, "Thiếu nguyên liệu hoặc trang bị không hợp lệ");
            return;
        }
        long star = trangBiKham.getOptionParam(107);
        if (star >= CombineService.MAX_DUCKHAM) {
            Service.gI().sendDialogMessage(player, "Đã đạt số pha lê tối đa");
            return;
        }

        int required = 1;
        float totalRatio = getRatio(star) + getRatioPhaLeHoascam((int) star) + ratioTheoVip(player); // 100 + 80 + 1 = 181/ 100 :))))
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(trangBiKham.template.name).append("\n");
        text.append(ConstFont.BOLD_DARK).append(trangBiKham.getOptionInfo()).append("\n");
        text.append(ConstFont.BOLD_GREEN).append(star + 1).append(" ô Sao Pha Lê\n");
        text.append(ConstFont.BOLD_GREEN).append("Đục Tối Đa Max 30 Lỗ\n");
      //  text.append(ConstFont.BOLD_RED).append("Tỉ lệ thành công: ").append(totalRatio).append("%\n");
        text.append(ConstFont.BOLD_RED).append("Tỉ lệ thành công: Không Thể Xác Định\n");
        text.append(ConstFont.BOLD_RED).append("Tỉ lệ Thêm Theo vip: ").append(getRatioStr(star)).append("%\n");
     text.append(ConstFont.BOLD_RED).append("Điểm SK Đập Đồ : " + player.point_dapdo + " Điểm\n");
        
        text.append(coNe.quantity < required ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE)
                .append("Cần ").append(required).append(" Thỏi Vàng\n");
        text.append(zenNiNe.quantity < required ? ConstFont.BOLD_RED : ConstFont.BOLD_BLUE)
                .append("Cần ").append(required).append(" Lượng Vàng");

        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                "Nâng cấp x" + required, "Từ chối");
    }

    public static void DucLoKham(Player player, int... numm) {
        int n = (numm.length > 0) ? numm[0] : 1;
        Item coNe = null;
        Item zenNiNe = null;
        Item trangBiKham = null;
        for (Item item : player.combine.itemsCombine) {
            if (item == null || !item.isNotNullItem()) {
                continue;
            }

            if (item.template.id == 457) {
                coNe = item;
            } else if (item.template.id == 1270) {
                zenNiNe = item;
            } else if (item.isTrangBiKham()) {
                trangBiKham = item;
            }
        }

        if (coNe == null || zenNiNe == null || trangBiKham == null) {
            return;
        }
        if (trangBiKham.isHaveOption(93)) {
            return;
        }

        long star = trangBiKham.getOptionParam(107);
        if (star >= CombineService.MAX_DUCKHAM) {
            return;
        }

        int required = 1;
        boolean success = false;
        int i;

        for (i = 0; i < n; i++) {
            if (coNe.quantity < required) {
                Service.gI().sendServerMessage(player, "Thiếu Thỏi Vàng sau " + i + " lần thử.");
                break;
            }
            if (zenNiNe.quantity < required) {
                Service.gI().sendServerMessage(player, "Thiếu Lượng Vàng sau " + i + " lần thử.");
                break;
            }

            coNe.quantity -= required;
            zenNiNe.quantity -= required;

            if (Util.isTrue(getRatio(star) + ratioTheoVip(player), 100)) {///r 
                success = true;
                break;
            }
        }
        if (success) {
            trangBiKham.addOptionParam(107, 1);
            if (star > 1) {
                Service.gI().sendThongBaoAllPlayer("Chúc mừng " + player.name + " vừa Đục Khảm Phụ Kiện "
                        + "thành công " + trangBiKham.template.name + " lên " + (star + 1) + " sao pha lê");
            }
            CombineService.gI().sendEffectSuccessCombine(player);
            player.point_dapdo += 10;
        } else {
            CombineService.gI().sendEffectFailCombine(player);
           
        }
        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
    }
}
