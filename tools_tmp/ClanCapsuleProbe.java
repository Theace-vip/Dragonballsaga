import clan.Clan;
import services.ClanService;
import panel.tuning.SystemTuning;
import panel.tuning.StatRateTuning;

/**
 * Probe kiem chung cong thuc diem capsule bang + chi so moi cap bang
 * chay tren chinh class da build (khong ket noi DB, khong dung server).
 */
public class ClanCapsuleProbe {

    public static void main(String[] args) throws Exception {
        System.out.println("== Panel nhom 'Bang Hoi' ==");
        int div = SystemTuning.getI("clan_capsule_hp_div");
        int mul = SystemTuning.getI("clan_capsule_mul");
        int vipMoi = SystemTuning.getI("clan_capsule_vip_moi");
        int boss = SystemTuning.getI("clan_capsule_boss");
        System.out.println("hp_div=" + div + " mul=" + mul + " vip_moi=" + vipMoi + " boss=" + boss);

        System.out.println("== clan_per_lv (dame moi cap bang) ==");
        System.out.println("real=" + StatRateTuning.real("clan_per_lv", 200)
                + " display=" + StatRateTuning.DISPLAY.get("clan_per_lv")
                + " globalFactor=" + StatRateTuning.GLOBAL_FACTOR);

        System.out.println("== Cong thuc diem capsule theo quai: (hp/div)*mul + vip*vip_moi ==");
        long[] hps = {1000, 5000, 20000, 2_000_000};
        int[] vips = {0, 6, 13};
        for (long hp : hps) {
            for (int vip : vips) {
                long pts = (hp / div) * mul + (long) vip * vipMoi;
                System.out.println("  hp=" + hp + " vip=" + vip + " -> " + pts + " capsule");
            }
        }

        System.out.println("== Chi phi nang cap bang (khong gioi han cap) ==");
        ClanService cs = ClanService.gI();
        for (int lv : new int[]{1, 2, 3, 10, 11, 12, 50, 126, 127}) {
            System.out.println("  cap " + lv + " -> " + cs.capsuleCost(lv) + " capsule de len cap " + (lv + 1));
        }

        System.out.println("== addCapsule (khong DB, khong broadcast) ==");
        Clan clan = new Clan();
        clan.capsuleClan = 500;
        clan.addCapsule(1500, null);
        System.out.println("  capsuleClan=" + clan.capsuleClan + " dirtySave=" + clan.dirtySave);
        clan.addCapsule(0, null);
        System.out.println("  cong 0 diem: capsuleClan=" + clan.capsuleClan);
        clan.capsuleClan = Integer.MAX_VALUE - 10;
        clan.addCapsule(1_000_000, null);
        System.out.println("  chong tran: capsuleClan=" + clan.capsuleClan);

        System.out.println("== Gioi han cap bang / slot thanh vien ==");
        System.out.println("  LEVEL_MAX=" + Clan.LEVEL_MAX + " MEMBER_MAX=" + Clan.MEMBER_MAX);
    }
}
