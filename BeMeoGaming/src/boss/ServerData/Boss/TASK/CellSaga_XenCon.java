package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import consts.ConstPlayer;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.PlayerService;
import services.Service;
import services.TaskService;
import utils.Util;



public class CellSaga_XenCon extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public CellSaga_XenCon() throws Exception {
        super(BossID.XEN_CON_1, BossesData.XEN_CON);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(2, 100)) {
            Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.ITEMS_DTL[Util.nextInt(Manager.ITEMS_DTL.length - 1)], 1, location.x, location.y, -1));
        }
        Service.gI().dropItemMap(new ItemMap(zone, 1280, 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        if (Util.isTrue(1, 200)) {
            if (Util.isTrue(1, 200)) {
                Service.gI().dropItemMap(Util.ratioDTL(zone, Manager.NTL, 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24),-1));
            }
            for (int i = 0; i < 50; i += 10) {
                try {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(i), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(i), location.x - i, location.y, plKill.id));
                } catch (Exception e) {
                }
            }
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        this.attack();
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
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(70000, 150000);
    }
}
