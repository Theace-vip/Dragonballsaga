package boss.ListBossKhac;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossesData;
import consts.ConstPlayer;
import map.ItemMap;
import map.Zone;
import player.Player;
import server.Client;
import services.ChatGlobalService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.func.ChangeMapService;
import skill.Skill;
import utils.Util;



/**
 *
 * @author by Ts cụt cụt
 */
public class ANTROM extends Boss {

    private long lastTimeAnTrom;
    private long lastTimeChangeMap;

     public ANTROM() throws Exception {
        super( BossID.ANTROM, new BossData( 
                //            "Ăn trộm TV",
                "Ăn Trộm ",
                ConstPlayer.TRAI_DAT,
                new short[]{201, 202, 203, -1, -1, -1},
                //            new short[]{657, 658, 659, 50, -1, 5},
                1,
                new double[]{100},
                new int[]{5, 7, 0, 14},
                new int[][]{
                    {Skill.DRAGON, 7, 1000},
                    {Skill.GALICK, 7, 1000}, {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.THOI_MIEN, 3, 50000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 3, 50000}},
                new String[]{"|-1|Tới giờ làm việc, lụm lụm", "|-1|Cảm giác mình vào phải khu người nghèo :))"}, //text chat 1
                new String[]{"|-1|Ái chà vàng vàng", "|-1|Không làm vẫn có ăn :))", "|-2|Giám ăn trộm giữa ban ngày thế à", "|-2|Cút ngay không là ăn đòn"}, //text chat 2
                new String[]{"|-1|Híc lần sau ta sẽ cho ngươi phá sản",
                    "|-2|Chừa thói ăn trộm nghe chưa"}, //text chat 3
                60));

    }

    @Override
    public Zone getMapJoin() {
        int mapId = data[currentLevel].getMapJoin()[Util.nextInt(0, data[currentLevel].getMapJoin().length - 1)];
        return MapService.gI().getMap(mapId).zones.get(0);
    }

    @Override
    public void active() {
        tromTien();
        cFlag = 8;
        Service.gI().changeFlag(this, cFlag);
    }

    @Override
    public Player getPlayerAttack1() {
        if (plTarget == null || Util.canDoWithTime(lastTimeTargetPlayer, timeTargetPlayer)) {
            plTarget = zone.getPlayers().stream().filter(Player::isPl).findAny().orElse(null);
            lastTimeTargetPlayer = System.currentTimeMillis();
            timeTargetPlayer = Util.nextInt(1000, 3000);
        }
        return plTarget;
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    private void followPlayer(int dis) {
        int mX = plTarget.location.x;
        int mY = plTarget.location.y;
        int disX = location.x - mX;
        if (Math.sqrt(Math.pow(mX - location.x, 2) + Math.pow(mY - location.y, 2)) >= dis) {
            if (disX < 0) {
                location.x = mX - Util.nextInt(0, dis);
            } else {
                location.x = mX + Util.nextInt(0, dis);
            }
            location.y = mY;
            PlayerService.gI().playerMove(this, location.x, location.y);
        }
    }

    private void tromTien() {
        try {
            plTarget = getPlayerAttack1();
            if (plTarget == null) {
                return;
            }
            if (location != null && plTarget.location != null) {
                followPlayer(60);
                if (Util.canDoWithTime(lastTimeAnTrom, 1000)) {
                    if (location.x + Util.nextInt(0, 10) >= plTarget.location.x) {
                        int stolenGold = Util.nextInt(1000000, 100000000);
                        if (plTarget.inventory != null && plTarget.inventory.gold > stolenGold) {
                            plTarget.inventory.gold -= stolenGold;
                            inventory.gold += stolenGold;
                            Service.gI().sendMoney(plTarget);
                            lastTimeAnTrom = System.currentTimeMillis();
                             Service.gI().stealMoney(plTarget, - stolenGold);
                           this.chat("Á đù Hốc đc " + Util.numberToMoney(stolenGold) + " Vàng roài");
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void reward(Player plKill) {
        ChatGlobalService.gI().chat(plKill, "Vinh danh (" + plKill.name + ") vừa giết được ăn trộm và nhận được " + Util.numberToMoney(inventory.gold) + " Vàng");
        plKill.inventory.addGold(inventory.gold);
        PlayerService.gI().sendInfoHpMpMoney(plKill);
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!isDie()) {
            if (!piercing && effectSkill.isShielding) {
                return 0;
            }
            nPoint.subHP(1);
            if (isDie()) {
                setDie(plAtt);
                die(plAtt);
            }
            return 1;
        } else {
            return 0;
        }
    }

    void changePlayer() {
        try {
            if (Util.canDoWithTime(lastTimeChangeMap, 15000)) {
                Player player = Client.gI().getPlayers().stream().filter(pl -> !pl.equals(plTarget) && pl.isPl() && pl.zone.map.mapId != 51 && pl.zone.map.mapId != 5 && pl.zone.map.mapId != 21 && pl.zone.map.mapId != 22 && pl.zone.map.mapId != 23 && pl.zone.map.mapId != 113 && pl.zone.map.mapId != 129 && pl.zone.map.mapId != 52).findAny().orElse(null);
                if (player != null) {
                    ChangeMapService.gI().changeMap(this, player.zone, player.location.x, player.location.y);
                    lastTimeChangeMap = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void update() {
        super.update();
        if (Client.gI().getPlayers().isEmpty() || isDie()) {
            return;
        }
        changePlayer();
    }
}
