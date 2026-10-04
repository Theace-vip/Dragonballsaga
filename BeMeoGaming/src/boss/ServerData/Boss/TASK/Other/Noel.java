package boss.ServerData.Boss.TASK.Other;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossesData;
import consts.ConstPlayer;
import map.ItemMap;
import player.Player;
import server.Client;
import services.PlayerService;
import services.Service;
import services.func.ChangeMapService;
import skill.Skill;
import utils.Util;


/**
 *
 * @author by Ts cụt cụt
 */
public class Noel extends Boss {

    private static final short[][] outfitArrs = {
        {805, 806, 807, 1760, -1, -1},
        {809, 806, 807, 1760, -1, -1},
        {808, 806, 807, 1760, -1, -1},
        {810, 806, 807, 1760, -1, -1},
        {937, 938, 839, 1760, -1, -1},
        {940, 941, 942, 1760, -1, -1},
        {943, 944, 945, 1760, -1, -1},
        {950, 951, 952, 1760, -1, -1}
    };
    private static final int TIME_CHANGE_PLAYER = 15000;
    private static final int[][] listItemRewards = {{190, 100000}, {861, 1},{1271, 10},{648, 1},{649, 1}};
    private long lastTimeGift;
    private long lastTimeChangeMap;

    public Noel() throws Exception {
        super(BossID.NOEL, new BossData(
                "Noel Phát quà", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{805, 806, 807, 52, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
                1, //dame
                new double[]{1000}, //hp
                new int[]{5}, //map join
                new int[][]{
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000000
               }},
                new String[]{"|-1|Ố hô hô hô",
                    "|-2|Lại bú đồ nè con ơi!"
                }, //text chat 1
                new String[]{"|-1|Ố hố, hô hô hô",
                    "|-1|Bú đồ nào các con ơi!",
                    "|-1|Ta chỉ ở đây 15 giây thôi đấy",
                    "|-1|Thời gian không đợi 1 ai, kể cả nyc của con cũng vậy",
                    "|-2|Thằng này,tao nhịn mày lâu lắm rồi ấy nhá",
                    "|-2|Coi thường nhau quá đấy"}, //text chat 2
                new String[]{"|-1|Ôi bạn ơi ....ơi!!!"}, //text chat 3
                10 //type appear
        ));
    }

    @Override
    public void active() {
        if (!isDie() && Client.gI().getPlayers().size() < 0) {
            return;
        }
        super.active();
        phatQua();
    }

    public Player getPlayerPhatQua() {
        if (plTarget == null || Util.canDoWithTime(lastTimeTargetPlayer, timeTargetPlayer)) {
            plTarget = zone.getRandomPlayerInMap();
            lastTimeTargetPlayer = System.currentTimeMillis();
            timeTargetPlayer = 5000;
        }
        return plTarget;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        cFlag = 8;
        Service.gI().changeFlag(this, cFlag);
        lastTimeChangeMap = System.currentTimeMillis();
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        return 0;
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

    private void phatQua() {
        try {
            plTarget = getPlayerPhatQua();
            if (plTarget == null) {
                return;
            }
            if (location != null && plTarget.location != null) {
                followPlayer(60);
                if (Util.canDoWithTime(lastTimeGift, 15000)) {
                    int[] itemGift = listItemRewards[Util.nextInt(listItemRewards.length)];
                    Service.gI().dropItemMap(new ItemMap(zone, itemGift[0], itemGift[0] == 861 ? Util.nextInt(100, 500) : itemGift[1], location.x + (Util.nextInt(-50, 50)), zone.map.yPhysicInTop(location.x, location.y), -1).setNoAutoPickUp());
                    chat("Ố hô hô hô......");
                    lastTimeGift = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void update() {
        super.update();
        if (Client.gI().getPlayers().size() < 10 || isDie()) {
            return;
        }
        if (Util.canDoWithTime(lastTimeChangeMap, TIME_CHANGE_PLAYER)) {
            try {
                plTarget = null;
                Player pl = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
                if (pl != null && pl.isPl() && !pl.equals(plTarget) && !(pl.zone.map.mapId >= 21 && pl.zone.map.mapId <= 23) && pl.zone.map.mapId != 113 && pl.zone.map.mapId != 129 && pl.zone.map.mapId != 52) {
                    plTarget = pl;
                }
                if (plTarget != null) {
                    ChangeMapService.gI().changeMap(this, plTarget.zone, plTarget.location.x, plTarget.location.y);
                }
            } catch (Exception e) {
            }
            lastTimeChangeMap = System.currentTimeMillis();
        }
    }
}
