package boss.boss_list_tainguyen;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.EffectSkillService;
import services.Service;
import utils.Util;

public class BOSSDIEMFAM extends Boss {

    private long st;

    public BOSSDIEMFAM() throws Exception {
        super(BossID.BOSSDIEMFAM, false, true, BossesData.BOSSDIEMFAM);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

      @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (piercing) {
                damage /= 100;
            }
            if (Util.isTrue(100, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (damage > 6_000_000) {
                damage = 5999999;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
    
    
    
    @Override
    public void reward(Player plKill) {
        
        
           int diemsb = Util.nextInt(1, 2); //50 sd 
        plKill.point_sb += diemsb;
         Service.getInstance().sendThongBaoBenDuoi("Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
        
       int diemfamne = Util.nextInt(1, 20000); //50 sd 
        plKill.diemfam += diemfamne;
         Service.getInstance().sendThongBaoBenDuoi( "Bạn \n" + plKill.name  + "\nNhận Được " + diemfamne + " \nĐiểm fam");         
        
            
        for (int i = 1; i < Util.nextInt(2, 5) + 1; i++) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1664, 1, this.location.x - i * 10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        }
       
      
         int slcarot = Util.nextInt(1, 3);
        for (int i = 0; i < slcarot; i++) {
            ItemMap carot = new ItemMap(zone, 1687, 1, 10 * i + this.location.x, zone.map.yPhysicInTop(this.location.x, 0), -1);
            carot.options.add(new Item.ItemOption(50, Util.nextInt(5, 400)));
            carot.options.add(new Item.ItemOption(77, Util.nextInt(1, 400)));
            carot.options.add(new Item.ItemOption(103, Util.nextInt(1, 400)));
            carot.options.add(new Item.ItemOption(101, Util.nextInt(1, 10)));
            carot.options.add(new Item.ItemOption(237, 5));
             carot.options.add(new Item.ItemOption(231, 2));
            carot.options.add(new Item.ItemOption(72,  Util.nextInt(1, 16))); //50 sd 
            Service.getInstance().dropItemMap(this.zone, carot);
        
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
