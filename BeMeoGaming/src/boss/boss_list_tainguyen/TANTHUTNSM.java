package boss.boss_list_tainguyen;



import boss.Boss;
import boss.BossesData;
import static boss.BossType.FINAL;
import player.Player;
import services.EffectSkillService;
import services.Service;
import utils.Util;

public class TANTHUTNSM extends Boss { 

    public TANTHUTNSM() throws Exception {
        super(Util.randomBossId(), BossesData.TANTHUTNSM);
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (damage >= 90000000) {
                damage = 90000000;
            }
            this.nPoint.dame = damage / Util.nextInt(500, 1000);
            this.nPoint.subHP(damage);
            double tnSm = damage * Util.nextInt(20, 50);
            if (tnSm > 9999999999l) {
                tnSm = 9999999999l - Util.nextInt(100000);
            }
            if (plAtt.nPoint.power > 12_000_000_000_000L) {
                tnSm = Util.nextInt(1000);
            }
            Service.gI().addSMTN(plAtt, (byte) 2, tnSm, true);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
