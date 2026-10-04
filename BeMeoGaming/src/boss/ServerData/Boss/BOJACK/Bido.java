package boss.ServerData.Boss.BOJACK;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.Service;
import utils.Util;


// author Ts
public class Bido extends Boss {

    public Bido() throws Exception {
        super(BossID.BIDO, BossesData.BIDO);
    }

   
    @Override
    public void reward(Player plKill) {
        if (plKill.playerTask.taskMain.id == 31 && plKill.playerTask.taskMain.index == 1) {
            if (Util.isTrue(10, 100)) {
                Service.gI().dropItemMap(new ItemMap(zone, 457, 1, location.x, location.y, plKill.id));
            }
        }
        if (Util.isTrue(5, 100)) {
            Service.gI().dropItemMap(Util.listbido(zone, Manager.ITEMS_listBido[Util.nextInt(Manager.ITEMS_listBido.length - 1)], 1, location.x, location.y, -1));
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
    public void active() {
        if (BossManager.gI().getBoss(BossID.BUJIN).zone != null || BossManager.gI().getBoss(BossID.KOGU).zone != null) {
            return;
        }
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
        location.x = BossManager.gI().getBoss(BossID.ZANGYA).location.x - 40;
    }
}
