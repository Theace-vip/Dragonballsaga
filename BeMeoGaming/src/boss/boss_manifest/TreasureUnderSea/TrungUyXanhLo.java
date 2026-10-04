package boss.boss_manifest.TreasureUnderSea;



import consts.ConstPlayer;
import boss.*;
import static boss.BossType.PHOBANBDKB;
import map.ItemMap;
import map.Zone;
import player.Player;
import services.EffectSkillService;
import skill.Skill;
import services.Service;
import services.func.ChangeMapService;
import utils.Util;

public class TrungUyXanhLo extends Boss {

    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public TrungUyXanhLo(Zone zone, int level, double dame, double hp) throws Exception {
        super(BossID.TRUNG_UY_XANH_LO, new BossData(
                "Trung úy Xanh Lơ",
                ConstPlayer.TRAI_DAT,
                new short[]{135, 136, 137, -1, -1, -1},
                (dame),
                new double[]{2000000000},
                new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|He he he",
                    "|-1|Xem các ngươi mạnh đến đâu"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

//      @Override
//    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (piercing) {
//                damage /= 100;
//            }
//            if (Util.isTrue(100, 1000)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage);
//            if (effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                damage = 1;
//            }
//            if (damage > 900_000_000) {
//                damage = 899999999;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    }
    
    
    @Override
    public void reward(Player plKill) {
      
        
       
        
        if (Util.isTrue(30, 100)) {
            ItemMap it = new ItemMap(this.zone, Util.nextInt(17,20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void joinMap() {
        ChangeMapService.gI().changeMap(this, this.zone, 198, 456);
        this.changeStatus(BossStatus.CHAT_S);
    }

    @Override
    public void doneChatS() {
        this.changeStatus(BossStatus.AFK);
        Service.gI().setPos(this, 198, 456);
    }

    @Override
    public void afk() {
        Player pl = getPlayerAttack();
        if (pl == null || pl.isDie()) {
            return;
        }
        if (Util.getDistance(this, pl) <= 200) {
            this.changeStatus(BossStatus.ACTIVE);
        }
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.changeStatus(BossStatus.REST);
        TreasureUnderSeaManager.gI().removeBoss(this);
        this.dispose();
    }
}
