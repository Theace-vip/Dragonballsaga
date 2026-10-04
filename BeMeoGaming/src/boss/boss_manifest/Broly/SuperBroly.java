package boss.boss_manifest.Broly;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossManager;
import consts.ConstPlayer;
import player.Player;
import services.EffectSkillService;
import services.PetService;
import skill.Skill;
import utils.Util;



public class SuperBroly extends Boss {

    private final byte gender = (byte) Util.nextInt(0, 2);

    public SuperBroly(BossData bossData) throws Exception {
        super(Util.randomBossId(), bossData);
    }

    public SuperBroly() throws Exception {
        super(BossID.SUPER_BROLY, false, true, new BossData(
                "Super Broly", // name
                ConstPlayer.XAYDA, // gender
                new short[] { 294, 295, 296, -1, -1, -1 }, // outfit {head, body, leg, bag, aura, eff}
                10000, // dame
                new double[] { 2000000000 }, // hp
                new int[] { 5, 13, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38 }, // map join
                new int[][] {
                        { Skill.TAI_TAO_NANG_LUONG, 1, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 2, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 3, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 4, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 5, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 6, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 7, 1000 },
                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 1000 }, { Skill.DRAGON, 3, 1000 },
                        { Skill.DRAGON, 4, 1000 }, { Skill.DRAGON, 5, 1000 }, { Skill.DRAGON, 6, 1000 },
                        { Skill.DRAGON, 7, 1000 },
                        { Skill.DEMON, 1, 1000 }, { Skill.DEMON, 2, 1000 }, { Skill.DEMON, 3, 1000 },
                        { Skill.DEMON, 4, 1000 }, { Skill.DEMON, 5, 1000 }, { Skill.DEMON, 6, 1000 },
                        { Skill.DEMON, 7, 1000 },
                        { Skill.GALICK, 1, 1000 }, { Skill.GALICK, 2, 1000 }, { Skill.GALICK, 3, 1000 },
                        { Skill.GALICK, 4, 1000 }, { Skill.GALICK, 5, 1000 }, { Skill.GALICK, 6, 1000 },
                        { Skill.GALICK, 7, 1000 },
                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1000 }, { Skill.KAMEJOKO, 3, 1000 },
                        { Skill.KAMEJOKO, 4, 1000 }, { Skill.KAMEJOKO, 5, 1000 }, { Skill.KAMEJOKO, 6, 1000 },
                        { Skill.KAMEJOKO, 7, 1000 },
                        { Skill.MASENKO, 1, 1000 }, { Skill.MASENKO, 2, 1000 }, { Skill.MASENKO, 3, 1000 },
                        { Skill.MASENKO, 4, 1000 }, { Skill.MASENKO, 5, 1000 }, { Skill.MASENKO, 6, 1000 },
                        { Skill.MASENKO, 7, 1000 },
                        { Skill.ANTOMIC, 1, 1000 }, { Skill.ANTOMIC, 2, 1000 }, { Skill.ANTOMIC, 3, 1000 },
                        { Skill.ANTOMIC, 4, 1000 }, { Skill.ANTOMIC, 5, 1000 }, { Skill.ANTOMIC, 6, 1000 },
                        { Skill.ANTOMIC, 7, 1000 }, }, // skill
                new String[] {}, // text chat 1
                new String[] { "|-1|Haha! ta sẽ giết hết các ngươi",
                        "|-1|Sức mạnh của ta là tuyệt đối",
                        "|-1|Vào hết đây!!!", }, // text chat 2
                new String[] { "|-1|Các ngươi giỏi lắm. Ta sẽ quay lại." }, // text chat 3
                1// type appear
        ));
    }


    @Override
    public void reward(Player plKill) {
        if (plKill.pet != null) {
            return;
        }
        if (plKill.pet == null) {
           PetService.gI().createbroly(this);
        }
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!isDie()) {
            if (!piercing && Util.isTrue(10, 100)) {
                chat("Xí hụt");
                return 0;
            }
            damage = nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                if (damage > nPoint.hpMax * 0.3) {
                    damage = nPoint.hpMax * 3 / 10;
                }
            }
            nPoint.subHP(damage);
            if (isDie()) {
                setDie(plAtt);
                die(plAtt);
              // BossManager.gI().createBossBroly(BossID.BROLY, 100000);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        dispose();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        super.joinMap();
        PetService.gI().createbroly(this);
    }
}
