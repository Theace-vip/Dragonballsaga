package boss.ServerData.Boss.COLD;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.EffectSkillService;
import services.Service;
import utils.Util;



public class Cooler extends Boss {

    public Cooler() throws Exception {
        super(BossID.COOLER, BossesData.COOLER_1, BossesData.COOLER_2);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(3, 100)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_DTL[Util.nextInt(Manager.ITEMS_DTL.length - 1)], 1, location.x, location.y, -1));
        } else {
            if (Util.isTrue(80, 100)) {
                for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(1,10), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 190, Util.nextInt(1,10000000), location.x - i, location.y, plKill.id));
                }
            } else {
                for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 457, 1, location.x + i, location.y, plKill.id)); // đá hoàng kim
                    Service.gI().dropItemMap(new ItemMap(zone, 1884, 1, location.x - i, location.y, plKill.id));
                }
            }
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_NRO_BOSS_DROP_bang[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP_bang.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        }
        if (Util.isTrue(1, 5)) {
            if (Util.isTrue(1, 100)) {
                Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.NTL, 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24),-1));
            }
        }
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
            }
            damage = damage * Util.nextInt(50, 70) / 100;
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
