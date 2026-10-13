package npc.npc_manifest;

/**

 */

import clan.Clan;
import clan.ClanMember;
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.ClanService;
import services.Service;
import utils.Util;

public class GiuMaDauBo extends Npc {

    public GiuMaDauBo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (player.clan == null) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Ngươi chưa có bang hội. Hãy tạo bang hội rồi quay lại đây để lên cấp bang.",
                        "Ok", "Đóng");
                return;
            }
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi đang muốn tìm mảnh vỡ và mảnh hồn bông tai Porata trong truyền thuyết, ta sẽ đưa ngươi đến đó ?\n"
                    + "|7|Điểm Nâng Cấp Bang Hôi: " + Util.FormatNumber(player.clan.capsuleClan)
                    + "\nLever Bang: " + Util.FormatNumber(player.clan.level)
                    + "\nSức Chứa Men: " + Util.FormatNumber(player.clan.maxMember)
                    + "\n|2|Điểm Capsule hôm nay: " + diemHomNay(player)
                    + "\n|6|Mỗi Lv Bang Tăng Toàn Bang 200% Dame:"
                    + "\nTạo Bang Hội Cần 1M Điểm Fam Hoặc 1000 Cấp Tiên bang Trừ 1000 Cấp",
                    "Khiêu chiến\nBoss", "Lên Cấp\nBang Hội", "Ok", "Đóng");
        }
    }

    /** Diem capsule bang ma nguoi choi kiem duoc trong ngay / tran ngay (panel He Thong). */
    private String diemHomNay(Player player) {
        try {
            ClanMember cm = player.clan.getClanMember((int) player.id);
            if (cm == null) {
                cm = player.clanMember;
            }
            if (cm == null) {
                return "0";
            }
            // sang ngay moi thi bo dem coi nhu 0
            int diem = cm.capsuleDay == Clan.ngayHienTai() ? cm.capsuleToday : 0;
            int tran = panel.tuning.SystemTuning.getI("clan_capsule_ngay_toi_da");
            if (tran <= 0) {
                return Util.FormatNumber(diem);
            }
            return Util.FormatNumber(diem) + "/" + Util.FormatNumber(tran);
        } catch (Exception e) {
            return "0";
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.clan == null) {
                return;
            }
            switch (select) {
                case 1 -> ClanService.gI().upgradeClan(player);
                case 2 -> {
                    player.type = 5;
                    player.maxTime = 5;
                    Service.gI().Transport(player);
                }
                default -> {
                }
            }
        }
    }
}
