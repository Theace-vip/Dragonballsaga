package npc.npc_manifest;

/**
 *
 *
 */
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.Service;
import services.func.ChangeMapService;

public class Jaco extends Npc {

    public Jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 24 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy giúp ta đánh bại bản sao\nNgươi chỉ có 5 phút để hạ hắn\nPhần thưởng cho ngươi là item cấp 5\n"
                            + "Cuồng Nộ Cấp 5 : Tăng 500% Sức Đánh\n"
                            + "Bổ Huyết Cấp 5 : Tăng 500% Sinh Lực\n"
                            + "Bổ Khí Cấp 5   : Tăng 500% Manaa\n"
                            + "Đan Tiên bang x1,2,3,4,5\n"
                            + "Tăng Cường Up EXP X1,2,3,4,5 Lần/Hít Max 2 Tỉ Cấp\n"
                            + "|7|Yêu Cầu: Đạt 1k Cấp Tiên bang Để Vào map\n",
                            "Đến\nPotaufeu", "Từ chối");
                case 139 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây.\nCậu muốn đi đâu?",
                            "Đến\nTrái Đất", "Đến\nNamếc", "Đến\nXayda", "Từ chối");
                default -> {
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (this.mapId) {
                    case 24 -> {
                        if (select == 0) {
                            if (player.SagaThienDao < 1000) {
                                Service.gI().sendThongBaoFromAdmin(player, "Yêu Cầu Đạt 1000 Tiên bang Trở Lên Để Vào");

                                return;
                            }

                            ChangeMapService.gI().goToPotaufeu(player);
                        }
                    }

                    case 139 -> {
                        switch (select) {
                            case 0 ->
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                            case 1 ->
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                            case 2 ->
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                        }
                    }
                }
            }
        }
    }
}
