package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;



public class FideSaga_So2 extends Boss {

    Boss bTogether;

    public FideSaga_So2() throws Exception {
        super(BossID.SO_2, BossesData.SO_2);
    } 

    @Override
    public void active() {
        if (bTogether != null && bTogether.zone != null) {
            return;
        }
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
        bTogether = BossManager.gI().getBoss(BossID.SO_3);
        location.x = BossManager.gI().getBoss(BossID.SO_1).location.x + 40;
    }
}
