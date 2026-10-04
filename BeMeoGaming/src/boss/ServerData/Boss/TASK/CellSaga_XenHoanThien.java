package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import consts.ConstPlayer;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.EffectSkillService;
import services.PlayerService;
import services.Service;
import services.TaskService;
import utils.Util;



public class CellSaga_XenHoanThien extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public CellSaga_XenHoanThien() throws Exception {
        super(BossID.SIEU_BO_HUNG, BossesData.SIEU_BO_HUNG_1, BossesData.SIEU_BO_HUNG_2, BossesData.SIEU_BO_HUNG_3);
    }

    @Override
    public void reward(Player plKill) {
        for (int i = 0; i < 100; i += 10) {
            Service.gI().dropItemMap(new ItemMap(this.zone, 1271, 1, location.x + i, this.zone.map.yPhysicInTop(location.x, location.y), plKill.id));
            Service.gI().dropItemMap(new ItemMap(this.zone, 190, 100000, location.x - i, this.zone.map.yPhysicInTop(location.x, location.y), plKill.id));
        }
        if (Util.isTrue(1, 300)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.NTL, 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), -1));
        } else if (Util.isTrue(20, 100)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_DTL[Util.nextInt(Manager.ITEMS_DTL.length - 1)], 1, location.x, location.y, -1));
        } else {
            if (Util.isTrue(10, 100)) {
                for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 674, Util.nextInt(1, 10), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 1884, Util.nextInt(1, 10), location.x - i, location.y, plKill.id));
                }
            }
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_NRO_BOSS_DROP[Util.nextInt(Manager.ITEMS_NRO_BOSS_DROP.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }

    private void hapThu() {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
        this.nPoint.critg++;
        this.nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp * 2 / 100, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000);
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
