import boss.WorldBossTracker;
import boss.boss_manifest.WorldBoss.WorldBoss;

/**
 * Probe cho tinh nang [Thong bao Top 30s] cua Boss The Gioi.
 * Kiem tra:
 *  1. Chua ai danh boss  -> noi dung thong bao RONG (=> khong gui gi).
 *  2. Co nguoi danh      -> dung noi dung se broadcast: hang + so dame (bo qua nguoi dame 0).
 *  3. Gioi han so hang theo panel (topNotifyN).
 */
public class WorldBossTopProbe {

    public static void main(String[] args) {
        WorldBossTracker.reset();

        System.out.println("== 1. Chua ai danh boss ==");
        String rong = WorldBoss.bangDameThongBao(30, 5);
        System.out.println("noi dung se gui = [" + rong + "]");
        System.out.println("=> KHONG thong bao: " + rong.isEmpty());

        System.out.println();
        System.out.println("== 2. Sau khi co nguoi danh ==");
        WorldBossTracker.addDame(11L, "theace", 1234);
        WorldBossTracker.addDame(22L, "admin", 5_000_000_000_000L);
        WorldBossTracker.addDame(33L, "top1pro", 1.5E30);
        WorldBossTracker.addDame(44L, "nguoidanhboss", 0); // dame 0 -> phai bi bo qua
        System.out.println(WorldBoss.bangDameThongBao(30, 5));

        System.out.println();
        System.out.println("== 3. Gioi han 2 hang (panel topNotifyN = 2) ==");
        System.out.println(WorldBoss.bangDameThongBao(30, 2));

        System.out.println();
        System.out.println("== so nguoi co dame: " + WorldBossTracker.size() + " (nguoi dame 0 khong tinh) ==");
    }
}
