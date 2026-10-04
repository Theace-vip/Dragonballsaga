package npc.npc_manifest;



import boss.BossManager;
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import utils.Util;

public class Potage extends Npc {

    public Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 140) {
                Player BossClone = BossManager.gI().findBossClone(player);
                if (BossClone != null) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đang có 1 nhân bản của " + BossClone.name + " hãy chờ kết quả trận đấu",
                            "OK");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy giúp ta đánh bại bản sao\nNgươi chỉ có 5 phút để hạ hắn\nPhần thưởng cho ngươi là item cấp 5\n"
                            + "Cuồng Nộ Cấp 5 : Tăng 500% Sức Đánh\n"
                            + "Bổ Huyết Cấp 5 : Tăng 500% Sinh Lực\n"
                            + "Bổ Khí Cấp 5   : Tăng 500% Manaa\n"
                            + "Đan Tiên bang x1,2,3,4,5\n"
                            + "Tăng Cường Up EXP X1,2,3,4,5 Lần/Hít Max 2 Tỉ Cấp\n"
                            + "|7|Yêu Cầu: Đạt 1k Cấp Tiên bang Để Vào map\n",
                            "Hướng\ndẫn\nthêm", "OK", "Từ chối");
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 140) {
                if (player.iDMark.isBaseMenu()) {
                    Player BossClone = BossManager.gI().findBossClone(player);
                    if (BossClone == null) {
                        switch (select) {
                            case 0 ->
                                NpcService.gI().createTutorial(player, tempId, this.avartar, "Thứ bị phong ấn tại đây là vũ khí có tên Commesonđược tạo ra nhằm bảo vệ cho hành tinh PotaufeuTuy nhiên nó đã tàn phá mọi thứ trong quá khứ\nKhiến cư dân Potaufeu niêm phong nó với cái giáphải trả là mạng sống của họTa, Potage là người duy nhất sống sótvà ta đã bảo vệ phong ấn hơn một trăm năm.\nTuy nhiên bọn xâm lượt Gryll đã đến và giải thoát CommesonHãy giúp ta tiêu diệt bản sao do Commeson tạo ravà niêm phong Commeson một lần và mãi mãi");
                            case 1 -> {
                                if (!Util.isAfterMidnight(player.lastPkCommesonTime) && !player.isAdmin()) {
                                    Service.gI().sendThongBao(player, "Hãy chờ đến ngày mai");
                                } else {
                                    Service.gI().callNhanBan(player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
