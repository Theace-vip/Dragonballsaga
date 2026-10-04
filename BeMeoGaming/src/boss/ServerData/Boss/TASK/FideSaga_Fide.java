package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import map.ItemMap;
import player.Player;
import services.Service;
import services.TaskService;
import utils.Util;



public class FideSaga_Fide extends Boss {

    public FideSaga_Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap carot = new ItemMap(zone, Util.nextInt(18, 20), 1,  + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
         Service.getInstance().dropItemMap(this.zone, carot);
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
}
