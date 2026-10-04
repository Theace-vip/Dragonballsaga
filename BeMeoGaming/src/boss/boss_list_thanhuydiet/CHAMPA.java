package boss.boss_list_thanhuydiet;



import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import consts.ConstPlayer;
import item.Item;
import item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import map.ItemMap;
import player.Player;
import services.ItemService;
import services.PlayerService;
import services.Service;
import services.SkillService;
import services.func.ChangeMapService;
import utils.Util;

public class CHAMPA extends Boss {

    private long st;
      private long lastTimeMove;

    private int timeMove;

    private boolean isReward;

    private long lastTimeReward;


    public CHAMPA() throws Exception {
        super(BossID.CHAMPA, false, true, BossesData.CHAMPA);
    }

    @Override
    public void reward(Player plKill) {
            
         int diemsb = Util.nextInt(1, 2); //50 sd 
        plKill.point_sb += diemsb;
         Service.getInstance().sendThongBaoBenDuoi("Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
    
        
        
            int[] item = new int[]{1502,1111,1404};//list item
        int[] dohuydiet = new int[]{Util.nextInt(650, 662)};   // list đồ huỷ diệt   // list đồ huỷ diệt
        
        int listdohuydiet = new Random().nextInt(dohuydiet.length);
        int roiitem = new Random().nextInt(item.length);
        
        
        
            
        for (int i = 1; i < 1 + 1; i++) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 987, 1, this.location.x - i * 10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        }

        if (Util.isTrue(50, 100)) { // tỉ lệ 30 k vv
            Service.gI().dropItemMap(this.zone, Util.sethuydiet(zone, dohuydiet[listdohuydiet], 1,  this.location.x-30, this.location.y, plKill.id));
         
        }
       
        if (this.currentLevel == 1) {
            return;
        }
    }

  @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
        Service.gI().changeFlag(this, 1);
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
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
    public void active() {
        this.attack();
    }

    @Override
    public Player getPlayerAttack() {
        List<Player> plNotVoHinh = new ArrayList();
        for (Player pl : this.zone.getNotBosses()) {
            if ((pl.effectSkin == null || !pl.effectSkin.isVoHinh) && pl.cFlag != this.cFlag) {
                plNotVoHinh.add(pl);
            }
        }
        for (Player pl : this.zone.getBosses()) {
            if (!pl.equals(this) && pl.cFlag == 2) {
                plNotVoHinh.add(pl);
            }
        }
        if (!plNotVoHinh.isEmpty()) {
            return plNotVoHinh.get(Util.nextInt(0, plNotVoHinh.size() - 1));
        }

        return null;
    }

    @Override
    public void attack() {
        if (this.effectSkill.isCharging) {
            return;
        }
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    if (Util.canDoWithTime(lastTimeMove, timeMove)) {
                        Player plRand = super.getPlayerAttack();
                        if (plRand != null) {
                            this.moveToPlayer(plRand);
                            this.lastTimeMove = System.currentTimeMillis();
                            this.timeMove = Util.nextInt(5000, 30000);
                        }
                    }
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                int dis = Util.getDistance(this, pl);
                if (dis > 450) {
                    move(pl.location.x - 24, pl.location.y);
                } else if (dis > 100) {
                    int dir = (this.location.x - pl.location.x < 0 ? 1 : -1);
                    int move = Util.nextInt(50, 100);
                    move(this.location.x + (dir == 1 ? move : -move), pl.location.y);
                } else {
                    if (Util.isTrue(30, 100)) {
                        int move = Util.nextInt(50);
                        move(pl.location.x + (Util.nextInt(0, 1) == 1 ? move : -move), this.location.y);
                    }
                    if (pl.isPl()) {
                        this.nPoint.dame = pl.nPoint.hpMax / 30;
                    } else {
                        this.nPoint.dame = 100000000;
                    }
                    SkillService.gI().useSkill(this, pl, null, -1, null);
                    checkPlayerDie(pl);
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        }
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(50, 100);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    @Override
    public void moveToPlayer(Player pl) {
        if (pl.location != null) {
            moveTo(pl.location.x, pl.location.y);
        }
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.isReward = false;
        this.playerReward = null;
        this.changeStatus(BossStatus.REST);
    }
}
