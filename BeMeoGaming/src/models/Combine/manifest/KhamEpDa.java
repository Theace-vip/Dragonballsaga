package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.Service;

/**
 *
 * @author bemeo
 */
public class KhamEpDa {

    // [10/10/2026] Chi phi ep sao phu kien: tu tru trong hanh trang, khong can keo vao o
    private static final int REQUIRED_BAC = 100000; // Luong Bac (1271)
    private static final int REQUIRED_VANG = 1; // Luong Vang (1270)

    /**
     * [10/10/2026] Ep Sao Phu Kien = cac o thu 6 tro di: cai trang (type 5),
     * phu kien, giap tap luyen (type 32)... KHONG dung cho 5 mon ao/quan/gang/giay/nhan
     * (5 mon do phai dung "Ep Sao Pha Le").
     */
    private static boolean isTrangBiEpSaoPhuKien(Item item) {
        if (item == null || !item.isNotNullItem()) {
            return false;
        }
        // da kham / sao pha le / sach ep premium (type 30) / ngoc rong khong phai la trang bi
        if (item.isDaKham() || item.isDaPhaLeEpSao() || (item.template.id >= 14 && item.template.id <= 20)) {
            return false;
        }
        return item.isTrangBiKham() || item.template.type == 32;
    }

    /**
     * Nguyen lieu ep duoc: Da kham (type 87), Sao pha le va Sach Ep Premium (type 30),
     * Ngoc Rong 1-7 sao (id 14-20).
     */
    private static boolean isDaEpSaoPhuKien(Item item) {
        return item != null && item.isNotNullItem() && (item.isDaKham() || item.isDaPhaLeEpSao());
    }

    private static boolean hasOptionDa(Item da) {
        return da != null && !da.itemOptions.isEmpty();
    }

    private static StringBuilder buildPreview(Item trangBi, Item daPhaLe, Item luongBac, Item luongVang) {
        StringBuilder text = new StringBuilder();
        text.append(ConstFont.BOLD_BLUE).append(trangBi.template.name).append("\n");
        long star = trangBi.getOptionParam(102);
        text.append(ConstFont.BOLD_DARK)
                .append(star >= 7 ? trangBi.getOptionInfoCuongHoa(daPhaLe) : trangBi.getOptionInfo(daPhaLe))
                .append("\n");
        text.append((luongBac != null && luongBac.quantity >= REQUIRED_BAC) ? ConstFont.BOLD_BLUE : ConstFont.BOLD_RED)
                .append("Cần ").append(String.format("%,d", REQUIRED_BAC)).append(" Lượng Bạc")
                .append(luongBac != null ? " (có " + String.format("%,d", luongBac.quantity) + ")" : " (không có)")
                .append("\n");
        text.append((luongVang != null && luongVang.quantity >= REQUIRED_VANG) ? ConstFont.BOLD_BLUE : ConstFont.BOLD_RED)
                .append("Cần ").append(REQUIRED_VANG).append(" Lượng Vàng")
                .append(luongVang != null ? " (có " + luongVang.quantity + ")" : " (không có)");
        return text;
    }

    private static void sendNeedMore(Player player, Item luongBac, Item luongVang) {
        if (luongBac == null || luongBac.quantity < REQUIRED_BAC) {
            Service.gI().sendDialogMessage(player, "Cần ít nhất " + String.format("%,d", REQUIRED_BAC)
                    + " Lượng Bạc trong hành trang.\nĐang có: "
                    + (luongBac == null ? 0 : luongBac.quantity) + " Lượng Bạc.");
            return;
        }
        Service.gI().sendDialogMessage(player, "Cần " + REQUIRED_VANG + " Lượng Vàng trong hành trang.");
    }

    public static void showInfoCombine(Player player) {
        int size = player.combine.itemsCombine.size();
        if (size < 2 || size > 3) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị từ ô thứ 6 trở đi (cải trang, phụ kiện, giáp tập luyện)\n"
                    + "1 Đá Khảm hoặc Sao Pha Lê (Sách Ép Premium)\n"
                    + "Lượng Bạc và Lượng Vàng tự trừ trong hành trang.");
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        for (Item item : player.combine.itemsCombine) {
            if (isTrangBiEpSaoPhuKien(item)) {
                trangBi = item;
            } else if (isDaEpSaoPhuKien(item)) {
                daPhaLe = item;
            }
        }
        if (trangBi == null || !trangBi.isNotNullItem()) {
            Service.gI().sendDialogMessage(player, "Cần 1 trang bị từ ô thứ 6 trở đi (cải trang, phụ kiện, giáp tập luyện).\n"
                    + "Áo, quần, găng, giày, nhẫn hãy dùng chức năng 'Ép Sao Pha Lê'.");
            return;
        }
        if (daPhaLe == null || !daPhaLe.isNotNullItem()) {
            Service.gI().sendDialogMessage(player, "Cần 1 Đá Khảm hoặc Sao Pha Lê (Sách Ép Premium) để ép vào.");
            return;
        }
        if (!hasOptionDa(daPhaLe)) {
            Service.gI().sendDialogMessage(player, "Đá khảm / sao pha lê này không hợp lệ.");
            return;
        }
        long star = trangBi.getOptionParam(102);
        long starEmpty = trangBi.getOptionParam(107);
        if (star >= starEmpty) {
            if (starEmpty <= 0) {
                Service.gI().sendDialogMessage(player, "Trang bị này chưa có ô Sao Pha Lê nào.\n"
                        + "Hãy dùng 'Pha Lê Hóa' (theo cấp VIP) hoặc 'Đục Khảm Lỗ' để đục lỗ trước rồi mới ép sao.");
            } else {
                Service.gI().sendDialogMessage(player, "Trang bị này đã ép đầy " + starEmpty + " ô Sao Pha Lê.");
            }
            return;
        }
        Item luongBac = InventoryService.gI().findItemBag(player, 1271);
        Item luongVang = InventoryService.gI().findItemBag(player, 1270);
        StringBuilder text = buildPreview(trangBi, daPhaLe, luongBac, luongVang);
        if (luongBac == null || luongBac.quantity < REQUIRED_BAC || luongVang == null
                || luongVang.quantity < REQUIRED_VANG) {
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                    "Còn thiếu\n" + (luongBac != null && luongBac.quantity >= REQUIRED_BAC ? REQUIRED_VANG + " Lượng Vàng"
                            : String.format("%,d", REQUIRED_BAC - (luongBac == null ? 0 : luongBac.quantity)) + " Bạc"));
            return;
        }
        CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(),
                "Nâng cấp", "Từ chối");
    }

    public static void KhamEpDa(Player player) {
        int size = player.combine.itemsCombine.size();
        if (size < 2 || size > 3) {
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        for (Item item : player.combine.itemsCombine) {
            if (isTrangBiEpSaoPhuKien(item)) {
                trangBi = item;
            } else if (isDaEpSaoPhuKien(item)) {
                daPhaLe = item;
            }
        }
        Item luongBac = InventoryService.gI().findItemBag(player, 1271);
        Item luongVang = InventoryService.gI().findItemBag(player, 1270);
        if (trangBi == null || !trangBi.isNotNullItem() || daPhaLe == null || !daPhaLe.isNotNullItem()
                || !hasOptionDa(daPhaLe)
                || luongBac == null || luongBac.quantity < REQUIRED_BAC
                || luongVang == null || luongVang.quantity < REQUIRED_VANG) {
            return;
        }
        long star = trangBi.getOptionParam(102);
        long starEmpty = trangBi.getOptionParam(107);
        if (star >= starEmpty) {
            return;
        }
        long cuongHoa = trangBi.getOptionParam(228);
        trangBi.addOptionParam(74, 0);
        trangBi.addOptionParam(247, 1);
        trangBi.addOptionParam(102, 1);
        if (star >= 70000) {
            if (star == 70000) {
                trangBi.itemOptions.add(new Item.ItemOption(218, 0));
            }
            trangBi.itemOptions.add(new Item.ItemOption(daPhaLe.getOptionDaPhaLe().optionTemplate.id,
                    daPhaLe.getOptionDaPhaLe().param));
        } else {
            trangBi.addOptionParam(daPhaLe.getOptionDaPhaLe().optionTemplate.id, daPhaLe.getOptionDaPhaLe().param);
        }
        InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
        InventoryService.gI().subLuongBac(player, REQUIRED_BAC);
        InventoryService.gI().subLuongvang(player, REQUIRED_VANG);
        CombineService.gI().sendEffectSuccessCombine(player);
        InventoryService.gI().sendItemBag(player);
        Service.gI().sendMoney(player);
        CombineService.gI().reOpenItemCombine(player);
    }
}
