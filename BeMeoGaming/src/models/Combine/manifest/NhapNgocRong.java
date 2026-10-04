package models.Combine.manifest;

import consts.ConstFont;
import consts.ConstNpc;
import item.Item;
import jdbc.daos.PlayerDAO;
import models.Combine.CombineService;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

public class NhapNgocRong {

    public static void showInfoCombine(Player player) {
         Item luongbac = null;
            luongbac = InventoryService.gI().findItemBag(player, 1271);
            if (luongbac == null || luongbac.quantity < 1000) {

                Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần ít Nhất 1000 Lượng Bạc Trở Lên\nChi Phí Cơ Bản\nFam quái Và Boss Kiếm");
                return;
            }
        if (InventoryService.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendDialogMessage(player, "Hành trang đã đầy, cần một ô trống trong hành trang");
            return;
        }
        if (player.combine.itemsCombine.size() != 1) {
            Service.gI().sendDialogMessage(player, "Cần 7 viên Ngọc Rồng");
            return;
        }
        Item item = player.combine.itemsCombine.get(0);
        if (item == null || !item.isNotNullItem() || item.template.id < 14 || item.template.id > 20 || item.quantity < 7) {
            Service.gI().sendDialogMessage(player, "Cần 7 viên Ngọc Rồng");
            return;
        }
        if (item.template.id > 14 && item.template.id <= 20) {
            StringBuilder text = new StringBuilder();
            text.append(ConstFont.BOLD_BLUE).append("Con có muốn biến 7 ").append(item.template.name).append(" thành\n");
            text.append("1 viên ").append(ItemService.gI().getTemplate((short) (item.template.id - 1)).name).append("\n");
            text.append(ConstFont.BOLD_GREEN).append("Cần 7 ").append(item.template.name);
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(), "Làm phép", "Từ chối");
        } else if (item.template.id == 14) {
            Item loNuocPhep = InventoryService.gI().findItemBag(player, 1820);
            StringBuilder text = new StringBuilder();
            text.append(ConstFont.BOLD_BLUE).append("Con có muốn biến 7 Ngọc Rồng 1 sao thành\n");
            text.append("1 viên Ngọc rồng Siêu Cấp\n");
            text.append(ConstFont.BOLD_GREEN).append("Cần 7 Ngọc Rồng 1 sao\n");
            text.append(player.getSession().vnd >= 10000 ? ConstFont.BOLD_GREEN : ConstFont.BOLD_RED).append("Cần 10k Cash\n");
            text.append(loNuocPhep == null ? ConstFont.BOLD_RED : ConstFont.BOLD_GREEN).append("Cần 1 Lọ nước phép\n");
            text.append(ConstFont.BOLD_BLUE).append("Tỉ lệ thành công: 50%\n");
            text.append(ConstFont.BOLD_RED).append("Nếu dùng đá bảo vệ sẽ không bị mất 1 viên ngọc rồng 1 sao khi thất bại.");
            if (loNuocPhep == null) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                        "Còn thiếu\nLọ nước phép");
                return;
            }
            if (player.getSession().vnd < 10000) {
                CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, text.toString(),
                        "Còn thiếu\n" + Util.numberToMoney(10000 - player.getSession().vnd) + " Cash");
                return;
            }
            CombineService.gI().baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, text.toString(), "Làm phép", "Nâng cấp\ndùng đá\nbảo vệ", "Từ chối");
        }
    }

    public static void nhapNgocRong(Player player, boolean useDBV) {
        if (InventoryService.gI().getCountEmptyBag(player) == 0) {
            return;
        }
        if (player.combine.itemsCombine.size() != 1) {
            return;
        }
        Item item = player.combine.itemsCombine.get(0);
        if (item == null || !item.isNotNullItem() || item.template.id < 14 || item.template.id > 20 || item.quantity < 7) {
            return;
        }
        if (item.template.id > 14 && item.template.id <= 20 && !useDBV) {
            Item ngocRong = InventoryService.gI().findItemBag(player, (short) (item.template.id - 1));
            if (ngocRong != null && ngocRong.quantity >= 99) {
                Service.gI().sendServerMessage(player, "Hành trang đã đầy, cần một ô trống trong hành trang để nhận vật phẩm");
                return;
            }
            Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
            CombineService.gI().sendEffectCombineDB(player, nr.template.iconID);
             InventoryService.gI().subLuongBac(player, 1000);
            InventoryService.gI().addItemBag(player, nr,999999);
            InventoryService.gI().subQuantityItemsBag(player, item, 7);
            InventoryService.gI().sendItemBag(player);
            CombineService.gI().reOpenItemCombine(player);
        } else if (item.template.id == 14) {
            Item daBaoVe = InventoryService.gI().findItemBag(player, 987);
            Item daBaoVeKhoa = InventoryService.gI().findItemBag(player, 1143);
            if (useDBV && daBaoVe == null && daBaoVeKhoa == null) {
                Service.gI().sendServerMessage(player, "Cần 1 Đá bảo vệ");
                return;
            }
            Item loNuocPhep = InventoryService.gI().findItemBag(player, 1820);
            if (player.getSession().vnd < 10000 || loNuocPhep == null) {
                return;
            }
            int nrSub;
            if (Util.isTrue(20, 100)) {
                nrSub = 7;
                Item nr = ItemService.gI().createNewItem((short) 1820);
                nr.itemOptions.add(new Item.ItemOption(30, 0));
                CombineService.gI().sendEffectCombineDB(player, nr.template.iconID);
                InventoryService.gI().addItemBag(player, nr,999999);
            } else {
                nrSub = useDBV ? 0 : 1;
                CombineService.gI().sendEffectFailCombine(player);
            }
            if (useDBV) {
                InventoryService.gI().subQuantityItemsBag(player, daBaoVe == null ? daBaoVeKhoa : daBaoVe, 1);
            }
           PlayerDAO.subcash(player, 10000);
          
            InventoryService.gI().subQuantityItemsBag(player, item, nrSub);
            InventoryService.gI().subQuantityItemsBag(player, loNuocPhep, 1);
            InventoryService.gI().sendItemBag(player);
            Service.gI().sendMoney(player);
            CombineService.gI().reOpenItemCombine(player);
        }
    }

}
