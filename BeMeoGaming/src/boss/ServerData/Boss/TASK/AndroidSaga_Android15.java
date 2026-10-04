package boss.ServerData.Boss.TASK;


import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import services.PlayerService;
import services.Service;
import services.TaskService;
import utils.Util;

public class AndroidSaga_Android15 extends Boss {


    public AndroidSaga_Android15() throws Exception {
        super(BossID.ANDROID_15, BossesData.ANDROID_15);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{380,380,380};
        int itemId = itemRan[2];
        
          int pointssm = Util.nextInt(1, 50); //50 sd 
        plKill.point_PassFree += pointssm;
        Service.getInstance().sendThongBao(plKill, "Bạn \n" + plKill.name + "\nNhận Được " + pointssm + " \nĐiểm Sổ Xứ mệnh Free");
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected void resetBase() {
        super.resetBase();
    }

    @Override
    public void active() {
        this.attack();
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!((AndroidSaga_Android14) this.parentBoss).callApk13 && damage >= this.nPoint.hp) {
            if (this.parentBoss != null) {
                ((AndroidSaga_Android14) this.parentBoss).callApk13();
            }
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }
}
