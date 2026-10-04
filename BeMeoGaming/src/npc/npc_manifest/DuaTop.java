package npc.npc_manifest;

import consts.ConstNpc;
import matches.TOP;
import npc.Npc;
import player.Player;
import server.Manager;
import services.Service;
import services.TaskService;

import java.util.List;

/**
 * NPC "Đua top" - dat tren map 5 (Đảo Kamê), ben trai NPC Kaio Shin.
 * Copy tu NPC cadich (Bảng Xep Hang), mo rong thanh 3 trang - toan bo 16 bang xep hang dang co.
 * Du lieu top duoc nap lai boi thread nen TopService.startAutoRefresh(5) (moi 5 phut).
 */
public class DuaTop extends Npc {

    private static final int MENU_PAGE_2 = 1103001;
    private static final int MENU_PAGE_3 = 1103002;
    private static final int MENU_PAGE_4 = 1103003;

    public DuaTop(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    private String say() {
        return "|7|•❅────✧❅✦❅✧────❅•\n"
                + "|4|BẢNG XẾP HẠNG SERVER\n"
                + "|7|Dữ liệu tự làm mới mỗi 5 phút\n"
                + "•❅────✧❅✦❅✧────❅•";
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, say(),
                        "TOP\nNạp Tiền",
                        "TOP\nSự Kiện",
                        "TOP\nTrainFam",
                        "TOP\nPhòng Tập",
                        "TOP\nVĩ Thú",
                        "TOP\nSăn Boss",
                        "TOP\nLeo Tháp",
                        "Trang 2 ▶");
            }
        }
    }

    private void openPage2(Player player) {
        this.createOtherMenu(player, MENU_PAGE_2, say(),
                "TOP\nSức Mạnh",
                "TOP\nTiên Bang",
                "TOP\nNhập Ma",
                "TOP\nTiêu Bư",
                "TOP\nBản Đồ KB",
                "TOP\nMở Rương",
                "◀ Trang 1",
                "Trang 3 ▶");
    }

    private void openPage3(Player player) {
        this.createOtherMenu(player, MENU_PAGE_3, say(),
                "TOP\nĐập Đồ",
                "TOP\nTầm Bảo",
                "TOP\nCâu Cá",
                "◀ Trang 2",
                "Trang 4 ▶");
    }

    private void openPage4(Player player) {
        this.createOtherMenu(player, MENU_PAGE_4, say(),
                "TOP\nThiên Đạo",
                "TOP\nĐịa Đạo",
                "TOP\nTu Tiên",
                "TOP\nChuyển Sinh",
                "◀ Trang 3");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (!canOpenNpc(player)) {
            return;
        }
        int menu = player.iDMark.getIndexMenu();
        if (menu == ConstNpc.BASE_MENU) {
            switch (select) {
                case 0 -> show(player, Manager.TopNap);
                case 1 -> show(player, Manager.Topsk);
                case 2 -> show(player, Manager.TopNGUHANH);
                case 3 -> show(player, Manager.TopPhongtap);
                case 4 -> show(player, Manager.Topvithu);
                case 5 -> show(player, Manager.Topsanboss);
                case 6 -> show(player, Manager.topLeoThap);
                case 7 -> openPage2(player);
                default -> {
                }
            }
        } else if (menu == MENU_PAGE_2) {
            switch (select) {
                case 0 -> show(player, Manager.Topsucmanh);
                case 1 -> show(player, Manager.TopTienBang);
                case 2 -> show(player, Manager.TopNhapMa);
                case 3 -> show(player, Manager.Topsanbu);
                case 4 -> show(player, Manager.Topbdkb);
                case 5 -> show(player, Manager.Topmoruong);
                case 6 -> openBaseMenu(player);
                case 7 -> openPage3(player);
                default -> {
                }
            }
        } else if (menu == MENU_PAGE_3) {
            switch (select) {
                case 0 -> show(player, Manager.Topdapdo);
                case 1 -> show(player, Manager.TopTambao);
                case 2 -> show(player, Manager.TopCauCa);
                case 3 -> openPage2(player);
                case 4 -> openPage4(player);
                default -> {
                }
            }
        } else if (menu == MENU_PAGE_4) {
            switch (select) {
                case 0 -> show(player, Manager.TopThienDao);
                case 1 -> show(player, Manager.TopDiaDao);
                case 2 -> show(player, Manager.TopTutien);
                case 3 -> show(player, Manager.TopChuyenSinh);
                case 4 -> openPage3(player);
                default -> {
                }
            }
        }
    }

    private void show(Player player, List<TOP> tops) {
        if (tops == null || tops.isEmpty()) {
            Service.gI().sendThongBao(player, "Bảng xếp hạng đang được cập nhật, bạn thử lại sau ít phút");
            return;
        }
        Service.gI().showListTop(player, tops);
    }
}
