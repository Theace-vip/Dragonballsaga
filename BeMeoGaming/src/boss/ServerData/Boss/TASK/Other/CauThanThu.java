package boss.ServerData.Boss.TASK.Other;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.Service;
import utils.Util;



public class CauThanThu extends Boss {

    public CauThanThu() throws Exception {
        super(BossID.CauThanThu, BossesData.CauThanThu);
    }

    @Override
    public void reward(Player plKill) {
        if (plKill.playerTask.taskMain.id == 31 && plKill.playerTask.taskMain.index == 1) {
            if (Util.isTrue(1, 200)) {
                Service.gI().dropItemMap(new ItemMap(zone, 1270, 1, location.x, location.y, plKill.id));
            }
        }
        if (Util.isTrue(3, 200)) {
            Service.gI().dropItemMap(Util.vithu(zone, Manager.ITEMS_Vithu[Util.nextInt(Manager.ITEMS_Vithu.length - 1)], 1, location.x, location.y, -1));
            } else if (Util.isTrue(2, 100)) {
           Service.gI().dropItemMap(new ItemMap(zone, 16, 1, location.x , location.y, plKill.id));
        } else {
            if (Util.isTrue(50, 100)) {
                for (int i = 0; i < 80; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(1, 10), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 190, Util.nextInt(1, 10000000), location.x - i, location.y, plKill.id));
                }
            }
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_NRO_BOSS_DROP[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        }
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 2, piercing, isMobAttack);
    }
}
