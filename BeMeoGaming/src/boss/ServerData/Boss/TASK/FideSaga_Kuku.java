package boss.ServerData.Boss.TASK;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import utils.Util;

public class FideSaga_Kuku extends Boss {

    private long st;

    public FideSaga_Kuku() throws Exception {
        super(BossID.KUKU, true, true, BossesData.KUKU);
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
//        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
//            st = System.currentTimeMillis();
//        }
    }
}
