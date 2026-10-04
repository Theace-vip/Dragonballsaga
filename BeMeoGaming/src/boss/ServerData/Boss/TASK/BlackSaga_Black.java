package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.Service;
import services.TaskService;
import utils.Util;



public class BlackSaga_Black extends Boss {

    public BlackSaga_Black() throws Exception {
        super(BossID.BLACK_GOKU, BossesData.BLACK_GOKU);
    }

    @Override
    public void reward(Player plKill) {
        if (plKill.playerTask.taskMain.id == 31 && plKill.playerTask.taskMain.index == 0) {
            if (Util.isTrue(1, 100)) {
                Service.gI().dropItemMap(new ItemMap(zone, 992, 1, location.x, location.y, plKill.id));
            }
        }
        int[] randItem = {1271, 861, 1271};
        for (int i = 0; i < 50; i += 10) {
            Service.gI().dropItemMap(new ItemMap(this.zone, randItem[Util.nextInt(randItem.length - 1)], 1, location.x + i, this.zone.map.yPhysicInTop(location.x, location.y), plKill.id));
            Service.gI().dropItemMap(new ItemMap(this.zone, randItem[Util.nextInt(randItem.length - 1)], 1, location.x - i, this.zone.map.yPhysicInTop(location.x, location.y), plKill.id));
        }
        if (Util.isTrue(3, 200)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_DTL[Util.nextInt(Manager.ITEMS_DTL.length - 1)], 1, location.x, location.y, -1));
        } else {
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_NRO_BOSS_DROP[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        }
        if (Util.isTrue(1, 5)) {
            if (Util.isTrue(1, 100)) {
                Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.NTL, 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), -1));
            }
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 5, piercing, isMobAttack);
    }
}
