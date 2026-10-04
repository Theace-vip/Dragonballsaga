package npc.npc_manifest;

/**
 *

 */

import clan.Clan;
import clan.ClanMember;
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import server.Client;
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
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi đang muốn tìm mảnh vỡ và mảnh hồn bông tai Porata trong truyền thuyết, ta sẽ đưa ngươi đến đó ?\n"
                    + "|7|Điểm Nâng Cấp Bang Hôi: " + Util.FormatNumber(player.clan.capsuleClan )
                    + "\nLever Bang: " + Util.FormatNumber(player.clan.level )
                    + "\nSức Chứa Men: " + Util.FormatNumber(player.clan.maxMember )
                    + "\n|6|Mỗi Lv Bang Tăng Toàn Bang 500% Dame:"
                     + "\nTạo Bang Hội Cần 1M Điểm Fam Hoặc 1000 Cấp Tiên bang Trừ 1000 Cấp"
                    ,
                    "Khiêu chiến\nBoss", "Lên Cấp\nBang Hội", "Ok", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (select) {
                case 0 -> {
                }
                 case 1 -> {
                     Clan clan = player.clan;
                        if (clan != null) {
                            if (clan.isLeader(player)) {
                                if (clan.level > 50) {
                                    Service.gI().sendThongBao(player, "Đang ở cấp độ cao nhất.");
                                    return;
                                }
                                int capsuleCan = ClanService.gI().capsule(clan);
                                int capsuleBang = clan.capsuleClan;
                                if (capsuleBang >= capsuleCan) {
                                    clan.capsuleClan -= capsuleCan;
                                    clan.level++;
                                    clan.maxMember++;
                                    Service.gI().sendThongBao(player, "Chúc mừng bang hội của bạn đã lên cấp " + (clan.level));
                                    for (ClanMember cm : player.clan.getMembers()) {
                                        Player pl = Client.gI().getPlayer(cm.id);
                                        if (pl != null) {
                                            ClanService.gI().sendMyClan(player);
                                        }
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ capsule bang, cần " + Util.FormatNumber(capsuleCan - capsuleBang) + " capsule bang nữa.");
                                }
                            }
                        }
                         
                }
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
