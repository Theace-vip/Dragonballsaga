package boss.ListBossKhac;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossType;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import server.Manager;
import services.EffectSkillService;
import services.Service;
import services.TaskService;
import utils.Util;

public class GOHAN3 extends Boss {

    private long st;

    public GOHAN3() throws Exception {
        super(BossID.GOHAN3, false, true, BossesData.GOHAN3);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

  
    
    
    
  
     @Override
    public void reward(Player plKill) {
         for (int i = 0; i < 50; i += 10) {
                    Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(1, 10), location.x + i, location.y, plKill.id));
                    Service.gI().dropItemMap(new ItemMap(zone, 1873, 1, location.x - i, location.y, plKill.id));
        }
        if (Util.isTrue(5, 100)) {
            Service.gI().dropItemMap(Util.linhthu(zone, Manager.ITEMS_gohan[Util.nextInt(Manager.ITEMS_gohan.length - 1)], 1, location.x, location.y, -1));
        } else {
            Service.gI().dropItemMap(new ItemMap(zone, Manager.ITEMS_gohan_dokiep[Util.nextInt(Manager.ITEMS_gohan_dokiep.length - 1)], 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    
    }

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }

    @Override
    public void doneChatS() {
        this.changeStatus(BossStatus.JOIN_MAP);
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.leaveMapNew();
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

  
  
}
