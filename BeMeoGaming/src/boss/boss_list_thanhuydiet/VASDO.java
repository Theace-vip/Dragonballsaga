package boss.boss_list_thanhuydiet;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import item.Item.ItemOption;
import java.util.List;
import java.util.Random;
import map.ItemMap;
import player.Player;
import services.ItemService;
import services.Service;
import utils.Util;

public class VASDO extends Boss {

    private long st;

    public VASDO() throws Exception {
        super(BossID.VADOS, false, true, BossesData.VASDO);
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
         Service.getInstance().sendThongBaoBenDuoi("Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
    
        
            int[] item = new int[]{1502,1111,1404};//list item
        int[] dohuydiet = new int[]{Util.nextInt(1048, 1060)};   // list đồ huỷ diệt   // list đồ huỷ diệt
        
        int listdohuydiet = new Random().nextInt(dohuydiet.length);
        int roiitem = new Random().nextInt(item.length);
        
        
        
            
        for (int i = 1; i < 1 + 1; i++) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 987, 5, this.location.x - i * 10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        }

        if (Util.isTrue(80, 100)) { // tỉ lệ 30 k vv
            Service.gI().dropItemMap(this.zone, Util.setthiensu(zone, dohuydiet[listdohuydiet], 1,  this.location.x-30, this.location.y, plKill.id));
         
        }
       
        if (this.currentLevel == 1) {
            return;
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
