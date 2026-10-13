package boss.boss_manifest.Broly;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossManager;
import consts.ConstPlayer;
import map.Zone;
import player.Player;
import services.PlayerService;
import services.Service;
import skill.Skill;
import utils.SkillUtil;
import utils.Util;


public class Broly extends Boss {

    private long lastTimeDamaged;
    private long lastTimeHP;
    private int timeHP;

    public Broly() throws Exception {
        this(1500, 150);
    }

    public Broly(double hp, double dame) throws Exception {
        super(Util.randomBossId(), new BossData(
                "Broly", //name
                ConstPlayer.XAYDA, //gender
                new short[]{291, 292, 293, -1, -1, -1},
                dame, //dame
                new double[]{10000}, //hp
                new int[]{33,34,35,18,19,20,4,6,28}, //map join
                new int[][]{
                    {Skill.DRAGON, 7, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                    {Skill.ANTOMIC, 7, 500}}, //skill
                new String[]{
                    "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                    "|-2|Tới đây đi!"
                }, //text chat 1
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không..thể..nào!!",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, //text chat 2
                new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
                1 //second rest
        ));
    }

    /** [FIX lag 11/10/2026] So Super Broly song cung luc - chan xoan so luong boss. */
    private static final int MAX_SUPER_BROLY = 20;

    @Override
    public void update() {
        // [FIX lag 11/10/2026] 16.6M la nguong HP GOC cua Broly (hoiPhuc nang dan len).
        // Tu khi cong them HP boss (panel boss_hp_them = 1E18), hp luc nao cung >= 16.6M nen
        // Broly tu sat + sinh Super Broly MOI TICK -> hang nghin boss + ~1700 thread/giay -> lag.
        // Tru di phan HP cong them de so sanh dung voi HP goc.
        if (nPoint.hp - hpThem() >= 16_600_000) {
            Zone zoneTemp = zone;
            setDie(null);
            die(null);
            Service.gI().charDie(this);
            if (soSuperBroly() < MAX_SUPER_BROLY) {
                BossManager.gI().createBoss(BossID.SUPER_BROLY);
            }
        }
        super.update();
    }

    /** [FIX lag 11/10/2026] HP cong them tu panel (boss_hp_them) - de so sanh voi nguong HP goc. */
    private double hpThem() {
        try {
            return panel.tuning.BossTuning.hpThem(this);
        } catch (Exception e) {
            return 0;
        }
    }

    /** [FIX lag 11/10/2026] Dem so Super Broly dang song (tranh sinh vo han). */
    private int soSuperBroly() {
        int c = 0;
        try {
            for (Boss b : BossManager.gI().getBosses()) {
                if (b != null && b.id == BossID.SUPER_BROLY) {
                    c++;
                }
            }
        } catch (Exception e) {
        }
        return c;
    }

    private void hoiPhuc() {
        if (!Util.canDoWithTime(lastTimeHP, timeHP)) {
            return;
        }
        nPoint.dameg += nPoint.dame * 10 / 100;
        nPoint.hpg += 160000;
        nPoint.critg++;
        nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, nPoint.hp, nPoint.mp);
        chat(2, "Mọi người cẩn thận sức mạnh hắn ta tăng đột biến..");
        chat("Graaaaaa...");
        lastTimeHP = System.currentTimeMillis();
        timeHP = Util.nextInt(2000, 5000);
        // [FIX lag 11/10/2026] tran HP goc 16M, phai cong lai phan HP boss cong them
        if (nPoint.hp - hpThem() > 16000000) {
            nPoint.hp = 16000000 + hpThem();
        }
        Player pl = zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        Service.gI().sendThongBao(pl, "Tên broly hắn lại tăng sức mạnh rồi!");
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        hoiPhuc();
        if (!piercing && Util.isTrue(10, 100)) {
            chat("Xí hụt");
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeDamaged >= 10000) {
            nPoint.hpMax = Math.min(nPoint.hp + (nPoint.hpMax * 8 / 10), nPoint.hpMax);
            byte skillId = Skill.TAI_TAO_NANG_LUONG;
            if (skillId != 0) {
                playerSkill.skills.add(SkillUtil.createSkill(skillId, 7));
                chat("Cảm giác rất tốt khi được hồi phục lại năng lượng :)");
            }
            lastTimeDamaged = currentTime;
        }
        damage = Math.min(damage, nPoint.hpMax * 5 / 100);
        nPoint.subHP(damage);
        if (isDie()) {
            // [FIX lag 11/10/2026] nguong HP goc: tru phan HP boss cong them (1E18)
            if (nPoint.hpMax - hpThem() < 16_600_000) {
                nPoint.addHp(nPoint.hpMax * 20 / 100);
                Service.gI().Send_Info_NV(this);
                active();
                return 0;
            } else {
                setDie(plAtt);
                die(plAtt);
            }
        }
        return damage;
    }
}
