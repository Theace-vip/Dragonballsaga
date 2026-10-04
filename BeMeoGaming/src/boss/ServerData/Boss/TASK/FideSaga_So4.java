package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;





public class FideSaga_So4 extends Boss {

    public FideSaga_So4() throws Exception {
        super(BossID.SO_4, BossesData.SO_4);
    }

    @Override
    public void joinMap() {
        super.joinMap();
        location.x = BossManager.gI().getBoss(BossID.SO_3).location.x + 40;
    }
}
