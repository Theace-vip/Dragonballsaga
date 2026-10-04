/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boss.ServerData.Boss.COLD;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.Service;
import utils.Util;


public class Kingcold extends Boss {

    public Kingcold() throws Exception {
        super(BossID.VUA_COLD, BossesData.VUA_COLD);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(3, 100)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_DTL[Util.nextInt(Manager.ITEMS_DTL.length - 1)], 1, location.x, location.y, -1));
        } else if (Util.isTrue(10, 100)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_NRO_BOSS_DROP[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP.length - 1)], 1, location.x, location.y, -1));
        } else {
            if (Util.isTrue(80, 100)) {
                for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(1,20), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 190, Util.nextInt(1,2000000), location.x - i, location.y, plKill.id));
                }
            } else {
                for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 674, 100, location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 674, 100, location.x - i, location.y, plKill.id));
                }
            }
        }
    }
}
