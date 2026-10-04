package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;



public class FideSaga_So1 extends Boss {

    Boss bTogether;

    public FideSaga_So1() throws Exception {
        super(BossID.SO_1, BossesData.SO_1);
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
        bTogether = BossManager.gI().getBoss(BossID.SO_2);
        location.x = BossManager.gI().getBoss(BossID.TIEU_DOI_TRUONG).location.x + 40;
    }
}
