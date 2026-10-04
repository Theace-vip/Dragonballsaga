package npc.npc_manifest;

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.CungMenhService;
import services.TaskService;
import models.Achievement.AchievementService;

/**
 * Bo Mong (Rung Karin, map 47) - chuyen thanh NPC CUNG MENH.
 * Nang cap bang Manh Tinh Tu, dot pha moi 10 cap bang Manh + Ngoc Tinh Do.
 *
 * == CODE CU (NHIEM VU HANG NGAY / THANH TICH) - GIU LAI DE SAU NAY MO LAI ==
 * Khi muon mo lai: bo comment khoi openBaseMenu / confirmMenu ben duoi
 * (menu nhiem vu hang ngay + thanh tich + chon cap do).
 * Luu y: TaskService.checkDoneTaskTalkNpc van duoc giu nguyen o menu moi
 * de nguoi choi lam nhiem vu chinh (task 9/10 "Noi chuyen voi Bo Mong") khong bi ket.
 * == HET CODE CU ==
 */
public class BoMong extends Npc {

    public BoMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            // GIU: kiem tra nhiem vu (vo hinh) - neu co nhiem vu can talk NPC nay thi xu ly va ket thuc
            if (TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                return;
            }
            if (this.mapId == 47 || this.mapId == 84) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        CungMenhService.gI().menuText(player),
                        "Nâng cấp", "Đột phá", "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 47 || this.mapId == 84) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0 ->
                            CungMenhService.gI().nangCap(player);
                        case 1 ->
                            CungMenhService.gI().dotPha(player);
                    }
                }
            }
        }
    }

    /* ================= CODE CU - GIU LAI (mo lai khi can) =================

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (this.mapId == 47 || this.mapId == 84) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi muốn vip, có nhiều cách, nạp thẻ là nhanh nhất, còn không thì chịu khó cày hãy nghe lời thầy dạy cần cù bù siêng năng.",
                            "Nhiệm vụ\\nhàng ngày", "Nhiệm vụ\\nthành tích", "Từ chối");
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 47 || this.mapId == 84) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0 -> {
                            if (player.playerTask.sideTask.template != null) {
                                String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                        + player.playerTask.sideTask.getLevel() + ")"
                                        + "\\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                        + player.playerTask.sideTask.maxCount + " ("
                                        + player.playerTask.sideTask.getPercentProcess() + "%)\\nSố nhiệm vụ còn lại trong ngày: "
                                        + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                        npcSay, "Trả nhiệm\\nvụ", "Hủy nhiệm\\nvụ");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                        "Tôi có vài nhiệm vụ theo cấp bậc, "
                                        + "sức cậu có thể làm được cái nào?",
                                        "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                            }
                        }
                        case 1 -> {
                            AchievementService.gI().openAchievementUI(player);
                        }
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                    switch (select) {
                        case 0, 1, 2, 3, 4 -> TaskService.gI().changeSideTask(player, (byte) select);
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                    switch (select) {
                        case 0 -> TaskService.gI().paySideTask(player);
                        case 1 -> TaskService.gI().removeSideTask(player);
                    }
                }
            }
        }
    }

    ================= HET CODE CU ================= */
}
