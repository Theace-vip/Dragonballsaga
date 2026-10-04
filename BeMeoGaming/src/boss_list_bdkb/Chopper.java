package boss_list_bdkb;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class Chopper extends Boss {

    private long st;

    public Chopper() throws Exception {
        super(BossID.Chopper, false, true, BossesData.Chopper);
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
         Service.getInstance().sendThongBao(plKill,"Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
        
       int diemSK = Util.nextInt(1, 200); //50 sd 
        plKill.point_bdkb += diemSK;
          Service.getInstance().sendThongBao(plKill,"Bạn \n" + plKill.name  + "\nNhận Được " + diemSK + " \nĐiểm bdkb");      
         
    
       
     
    
       
        if (Util.isTrue(5, 100)) { // tỉ lệ 30 k vv
            int slcarot = Util.nextInt(1, 3);
            for (int i = 0; i < slcarot; i++) {
                ItemMap carot = new ItemMap(zone, 1206, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(50, Util.nextInt(10, 150)));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(15, 100)) { // tỉ lệ 30 k vv
            int slcarot1 = Util.nextInt(2, 5);
            for (int i = 0; i < slcarot1; i++) {
                ItemMap carot = new ItemMap(zone, 1207, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(77, Util.nextInt(10, 150)));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(30, 100)) { // tỉ lệ 30 k vv
            int slcarot2 = Util.nextInt(4, 12);
            for (int i = 0; i < slcarot2; i++) {
                ItemMap carot = new ItemMap(zone, 1208, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(103, Util.nextInt(10, 150)));
                Service.getInstance().dropItemMap(this.zone, carot);
            }

        }
        if (Util.isTrue(20, 100)) { // tỉ lệ 30 k vv
            int slcarot3 = Util.nextInt(7, 15);
            for (int i = 0; i < slcarot3; i++) {
                ItemMap carot = new ItemMap(zone, 1209, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(101, Util.nextInt(10, 150)));
                Service.getInstance().dropItemMap(this.zone, carot);
            }
        }
        
           if (Util.isTrue(25, 100)) { // tỉ lệ 30 k vv
            int slcarot = Util.nextInt(1, 5);
            for (int i = 0; i < slcarot; i++) {
                ItemMap carot = new ItemMap(zone, Util.nextInt(618, 626), 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
               carot.options.add(new Item.ItemOption(50, Util.nextInt(5, 7999)));
            carot.options.add(new Item.ItemOption(77, Util.nextInt(5, 7999)));
            carot.options.add(new Item.ItemOption(103, Util.nextInt(5, 7999)));
            carot.options.add(new Item.ItemOption(101, Util.nextInt(1, 200)));
             carot.options.add(new Item.ItemOption(72, Util.nextInt(1, 16)));
            carot.options.add(new Item.ItemOption(237, 5));
             carot.options.add(new Item.ItemOption(231, 5));
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
