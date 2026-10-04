package services;


import consts.ConstNpc;
import item.Item;
import player.Player;
import server.Client;
import network.Message;
import java.util.ArrayList;
import utils.Util;

public class SubMenuService {

     public static final int BAN = 500;
    public static final int BUFF_PET = 501;
    public static final int KET_HON = 502;

    private static SubMenuService i;

    private SubMenuService() {
    }

    public static SubMenuService gI() {
        if (i == null) {
            i = new SubMenuService();
        }
        return i;
    }

    public void controller(Player player, int playerTarget, int menuId) {
        Player plTarget = Client.gI().getPlayer(playerTarget);
        switch (menuId) {
  case BAN:
                if (plTarget != null) {
                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                            "Bạn có chắc chắn muốn ban " + plTarget.name, selects, plTarget);
                }
                break;
            case BUFF_PET:
                if (plTarget != null) {
                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.BUFF_PET, -1,
                            "Bạn có chắc chắn muốn phát đệ tử cho " + plTarget.name, selects, plTarget);
                }
                break;
            case KET_HON:
                if (plTarget != null) {
                    Item ctrang = null;
                    try {
                        ctrang = InventoryService.gI().findItemBody(player,
                                panel.tuning.SystemTuning.getI("kh_cai_trang_cau_hon"));
                    } catch (Exception e) {
                    }
                    if (!player.inventory.itemsBody.get(5).isNotNullItem() || ctrang == null) {
                        Service.gI().sendThongBao(player, "Yêu cầu mang cải trang Nữ Thần");
                        return;
                    }
                    if (plTarget.duockethon >= panel.tuning.SystemTuning.getI("kh_duockethon_toi_da")) {
                        Service.gI().sendThongBao(player, "Đối phương đã đạt tối đa 10 lần nhận được kết hôn");
                        return;
                    }
                    if (player.dakethon >= panel.tuning.SystemTuning.getI("kh_dakethon_toi_da")) {
                        Service.gI().sendThongBao(player, "Bạn đã đạt tối đa 20 lần Cầu hôn!!!");
                    } else {
                        String[] selects = new String[]{"Đồng ý", "Hủy"};
                        NpcService.gI().createMenuConMeo(player, ConstNpc.KETHON_PLAYER, -1,
                                "Bạn có chắc chắn muốn Kết hôn với " + plTarget.name, selects, plTarget);
                    }
                }
                break;
        }
        Service.getInstance().hideWaitDialog(player);
    }

        
//    public void showMenu(Player player) {
//        ArrayList<SubMenu> subMenusList = new ArrayList<>();
//        subMenusList.add(new SubMenu(MENU, "Chức năng khác", "Oẳn tù tì, cừu sát, mua skin,..."));
//
//        showSubMenu(player, subMenusList.toArray(SubMenu[]::new));
//    }

     public void showKetHon(Player player) {
        showSubMenu(player, new SubMenu(KET_HON, "Kết hôn", ""));
    }
    public void showSubMenu(Player player, SubMenu... subMenus) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 63);
            msg.writer().writeByte(subMenus.length);
            for (SubMenu subMenu : subMenus) {
                msg.writer().writeUTF(subMenu.caption1);
                msg.writer().writeUTF(subMenu.caption2);
                msg.writer().writeShort((short) subMenu.id);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            utils.Logger.logException(SubMenuService.class, e);
        }
    }

    public static class SubMenu {

        private int id;
        private String caption1;
        private String caption2;

        public SubMenu(int id, String caption1, String caption2) {
            this.id = id;
            this.caption1 = caption1;
            this.caption2 = caption2;
        }
    }
}
