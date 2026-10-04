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



public class DRMSaga_Nobita extends Boss {

    public DRMSaga_Nobita() throws Exception {
        super(BossID.NOBITA, BossesData.NOBITA);
    }
     @Override
    public void reward(Player plKill) {
         for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(1, 10), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 861, Util.nextInt(1, 10), location.x - i, location.y, plKill.id));
        }
        if (Util.isTrue(3, 100)) {
            Service.gI().dropItemMap(Util.linhthu(zone, Manager.ITEMS_LinhThu[Util.nextInt(Manager.ITEMS_LinhThu.length - 1)], 1, location.x, location.y, -1));
        } else {
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_NRO_BOSS_DROP[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
}
