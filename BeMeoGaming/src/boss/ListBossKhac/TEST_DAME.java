package boss.ListBossKhac;

import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import item.Item.ItemOption;
import java.util.List;
import map.ItemMap;
import player.Player;
import services.ItemService;
import services.Service;
import services.TaskService;
import utils.Util;

public class TEST_DAME extends Boss {

    private long st;

    public TEST_DAME() throws Exception {
        super(BossID.TEST_DAME, false, true, BossesData.TEST_DAME);
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

        int diemsb = Util.nextInt(1, 10); //50 sd 
        plKill.point_sb += diemsb;
        Service.getInstance().sendThongBao(plKill, "Bạn \n" + plKill.name + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");

         
    }

    

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
         // Service.gI().sendThongBaoAllPlayer("|7|Alo-Alo-Alo\n|4|Tứ Hoàng Tử Kaido \nĐã Xuất Chiến\nTiêu Diệt Nhận Đá Khảm Nào");
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
