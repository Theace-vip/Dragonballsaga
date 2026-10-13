import clan.Clan;
import clan.ClanMember;

/**
 * Probe chay tren class da build (build/classes) de kiem chung:
 *  - Tran diem capsule theo ngay cua tung nguoi (Clan.diemCapsuleTheoNgay).
 *  - Danh sach boss nhiem vu (boss.BossID.isBossNhiemVu).
 *  - Gia tri tuning tren panel (clan_capsule_*, boss_hp_them).
 */
public class ClanCapsuleDailyProbe {

    public static void main(String[] args) throws Exception {
        System.out.println("== panel keys ==");
        System.out.println("  clan_capsule_hp_div     = " + panel.tuning.SystemTuning.getI("clan_capsule_hp_div"));
        System.out.println("  clan_capsule_mul        = " + panel.tuning.SystemTuning.getI("clan_capsule_mul"));
        System.out.println("  clan_capsule_vip_moi    = " + panel.tuning.SystemTuning.getI("clan_capsule_vip_moi"));
        System.out.println("  clan_capsule_ngay_toi_da= " + panel.tuning.SystemTuning.getI("clan_capsule_ngay_toi_da"));
        System.out.println("  clan_capsule_sau_tran   = " + panel.tuning.SystemTuning.getI("clan_capsule_sau_tran"));
        System.out.println("  boss_hp_them            = " + panel.tuning.SystemTuning.get("boss_hp_them"));

        System.out.println("== cong thuc cong thuc theo quai (HP mob / -) ==");
        long[] hpMobs = {10_000L, 100_000L, 1_000_000L, 100_000_000L};
        for (long hp : hpMobs) {
            long diem = (hp / 10000L) + 6; // vip 6
            System.out.println("  HP " + hp + " + VIP 6 => " + diem + " diem");
        }

        System.out.println("== tran ngay (Clan.diemCapsuleTheoNgay) ==");
        Clan clan = new Clan();
        ClanMember cm = new ClanMember();
        cm.id = 12345;
        System.out.println("  ngay hom nay = " + Clan.ngayHienTai());
        long n1 = clan.diemCapsuleTheoNgay(cm, 50);
        System.out.println("  quai 1 (50 diem)  -> nhan " + n1 + " | today=" + cm.capsuleToday + " day=" + cm.capsuleDay);
        cm.capsuleToday = 19990;
        long n2 = clan.diemCapsuleTheoNgay(cm, 50);
        System.out.println("  quai 2 (con 10)   -> nhan " + n2 + " | today=" + cm.capsuleToday);
        long n3 = clan.diemCapsuleTheoNgay(cm, 50);
        System.out.println("  quai 3 (het tran) -> nhan " + n3 + " | today=" + cm.capsuleToday);
        long n4 = clan.diemCapsuleTheoNgay(cm, 999_999);
        System.out.println("  quai 4 (diem to)  -> nhan " + n4 + " | today=" + cm.capsuleToday);
        cm.capsuleDay = 20260101;
        long n5 = clan.diemCapsuleTheoNgay(cm, 77);
        System.out.println("  sang ngay moi      -> nhan " + n5 + " | today=" + cm.capsuleToday + " day=" + cm.capsuleDay);

        System.out.println("== boss nhiem vu (mien +HP) ==");
        int[] ids = {boss.BossID.KUKU, boss.BossID.FIDE, boss.BossID.MABU_12H, boss.BossID.TRUNG_UY_TRANG,
            boss.BossID.COOLER, boss.BossID.GOLDEN_FRIEZA, boss.BossID.WORLD_BOSS, boss.BossID.CUMBER};
        String[] names = {"KUKU", "FIDE", "MABU_12H", "TRUNG_UY_TRANG", "COOLER", "GOLDEN_FRIEZA", "WORLD_BOSS", "CUMBER"};
        for (int i = 0; i < ids.length; i++) {
            System.out.println("  " + names[i] + " (" + ids[i] + ") nhiem vu = " + boss.BossID.isBossNhiemVu(ids[i]));
        }
        System.out.println("  hpThem(null) = " + panel.tuning.BossTuning.hpThem(null));

        System.out.println("== hpThem tren Player that =");
        player.Player pb = new player.Player();
        pb.isBoss = true;
        pb.id = boss.BossID.COOLER;
        System.out.println("  boss thuong COOLER   -> " + panel.tuning.BossTuning.hpThem(pb));
        pb.id = boss.BossID.KUKU;
        System.out.println("  boss nhiem vu KUKU   -> " + panel.tuning.BossTuning.hpThem(pb));
        pb.isBoss = false;
        pb.id = boss.BossID.COOLER;
        System.out.println("  player thuong        -> " + panel.tuning.BossTuning.hpThem(pb));

        System.out.println("== NPoint.calPoint (duong di that cua HP boss) =");
        try {
            player.Player b1 = new player.Player();
            b1.isBoss = true;
            b1.id = boss.BossID.COOLER;
            b1.nPoint.hpg = 1_000_000;
            b1.nPoint.calPoint();
            System.out.println("  COOLER  hpg=1tr -> hpMax=" + b1.nPoint.hpMax);
        } catch (Throwable t) {
            System.out.println("  COOLER  loi khi calPoint: " + t);
        }
        try {
            player.Player b2 = new player.Player();
            b2.isBoss = true;
            b2.id = boss.BossID.KUKU;
            b2.nPoint.hpg = 1_000_000;
            b2.nPoint.calPoint();
            System.out.println("  KUKU    hpg=1tr -> hpMax=" + b2.nPoint.hpMax);
        } catch (Throwable t) {
            System.out.println("  KUKU    loi khi calPoint: " + t);
        }
        try {
            player.Player b3 = new player.Player();
            b3.nPoint.hpg = 1_000_000;
            b3.nPoint.calPoint();
            System.out.println("  PLAYER  hpg=1tr -> hpMax=" + b3.nPoint.hpMax);
        } catch (Throwable t) {
            System.out.println("  PLAYER  loi khi calPoint: " + t);
        }
    }
}
