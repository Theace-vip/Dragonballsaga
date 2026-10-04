package boss.ServerData.Boss.TASK;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;


public class FideSaga_TieuDoiTruong extends Boss {

    Boss bTogether;

    public FideSaga_TieuDoiTruong() throws Exception {
        super(BossID.TIEU_DOI_TRUONG, BossesData.TIEU_DOI_TRUONG);
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
        bTogether = BossManager.gI().getBoss(BossID.SO_1);
    }
}
