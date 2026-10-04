package boss.boss_list_tainguyen;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import java.util.List;
import map.ItemMap;
import player.Player;
import services.ItemService;
import services.Service;
import utils.Util;

public class BLACKGOKUROSE extends Boss {

    private long st;

    public BLACKGOKUROSE() throws Exception {
        super(BossID.BLACKGOKUROSE, false, true, BossesData.BLACKGOKUROSE);
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

        int diemsb = Util.nextInt(1, 2); //50 sd 
        plKill.point_sb += diemsb;
        Service.getInstance().sendThongBao(plKill, "Bạn \n" + plKill.name + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");
        
          int pointssm = Util.nextInt(1, 50); //50 sd 
        plKill.point_PassFree += pointssm;
        Service.getInstance().sendThongBao(plKill, "Bạn \n" + plKill.name + "\nNhận Được " + pointssm + " \nĐiểm Sổ Xứ mệnh Free");

        if (Util.isTrue(5, 100)) { // tỉ lệ 30 k vv
            int slcarot = Util.nextInt(1, 3);
            for (int i = 0; i < slcarot; i++) {
                ItemMap carot = new ItemMap(zone, 1190, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(50, 100));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(20, 100)) { // tỉ lệ 30 k vv
            int slcarot1 = Util.nextInt(2, 5);
            for (int i = 0; i < slcarot1; i++) {
                ItemMap carot = new ItemMap(zone, 1191, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(77, 100));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(50, 100)) { // tỉ lệ 30 k vv
            int slcarot2 = Util.nextInt(4, 12);
            for (int i = 0; i < slcarot2; i++) {
                ItemMap carot = new ItemMap(zone, 1192, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(103, 100));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(25, 100)) { // tỉ lệ 30 k vv
            int slcarot3 = Util.nextInt(7, 15);
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, 1193, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(101, 100));
                Service.getInstance().dropItemMap(this.zone, carot);
            }
        }
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
