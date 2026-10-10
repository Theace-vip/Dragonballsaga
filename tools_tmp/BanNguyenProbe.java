import player.NPoint;
import player.Player;
import services.BanNguyenTinhCauService;

/**
 * Kiem tra cong thuc Ban Nguyen Tinh Cau (khong can client / DB).
 * Moi truong hop dung 1 nhan vat rieng de khong ghi de lan nhau.
 */
public class BanNguyenProbe {

    static int fail = 0;

    public static void main(String[] args) {
        BanNguyenTinhCauService s = BanNguyenTinhCauService.gI();

        System.out.println("--- 1. % dac trung theo Tier 3 ---");
        check("Trai Dat tier 3 -> Mien thuong 12%", 12, s.percentDacTrung(pc(0, 3, 0)));
        check("Namek tier 3 -> Phan sat thuong 15%", 15, s.percentDacTrung(pc(1, 3, 0)));
        check("Xayda tier 3 -> Sat thuong chuan 12%", 12, s.percentDacTrung(pc(2, 3, 0)));
        check("Tier 0 -> khong co gi", 0, s.percentDacTrung(pc(2, 0, 0)));

        System.out.println("--- 2. Sat thuong chuan (Xayda bo qua giap) ---");
        check("Xayda T5: 100 sau giap + 20% cua 200 goc = 140", 140,
                s.congSatThuongChuan(pc(2, 5, 0), 100, 200));
        check("Trai Dat khong co chuan -> giu 100", 100, s.congSatThuongChuan(pc(0, 5, 0), 100, 200));
        check("Namek khong co chuan -> giu 100", 100, s.congSatThuongChuan(pc(1, 5, 0), 100, 200));
        check("Xayda tier 1 -> +4% cua 200 = 108", 108, s.congSatThuongChuan(pc(2, 1, 0), 100, 200));

        System.out.println("--- 3. Noi tai Zenkai (Xayda T5) ---");
        Player z = pc(2, 5, 0);
        z.nPoint.hpMax = 1000;
        z.nPoint.hp = 200;
        check("HP 20% -> +0%", 0, s.zenkaiPercent(z));
        z.nPoint.hp = 100;
        check("HP 10% -> +150%", 150, s.zenkaiPercent(z));
        z.nPoint.hp = 0;
        check("HP 0% -> +300% (toi da)", 300, s.zenkaiPercent(z));
        z.nPoint.hp = 100;
        check("applyZenkai: 1000 sat thuong -> 2500", 2500, s.applyZenkai(z, 1000));
        z.nPoint.hp = 1000;
        check("HP day -> khong cong", 1000, s.applyZenkai(z, 1000));
        Player namekT5 = pc(1, 5, 0);
        namekT5.nPoint.hpMax = 1000;
        namekT5.nPoint.hp = 50;
        check("Namek khong co Zenkai -> 0", 0, s.zenkaiPercent(namekT5));
        Player xaydaT4 = pc(2, 4, 0);
        xaydaT4.nPoint.hpMax = 1000;
        xaydaT4.nPoint.hp = 50;
        check("Xayda chua len Tier 5 -> 0", 0, s.zenkaiPercent(xaydaT4));

        System.out.println("--- 4. Mien thuong Trai Dat ---");
        Player td4 = pc(0, 4, 0);
        td4.nPoint.hpMax = 100000;
        td4.nPoint.hp = 100000;
        check("Trai Dat T4: 1000 sat thuong -> 840 (giam 16%)", 840, s.truSatThuongNhan(td4, 1000, false));
        Player none = pc(0, 0, 0);
        none.nPoint.hpMax = 100;
        none.nPoint.hp = 100;
        check("Chua mo khoa -> khong giam sat thuong", 500, s.truSatThuongNhan(none, 500, false));
        Player xayda = pc(2, 5, 0);
        xayda.nPoint.hpMax = 100000;
        xayda.nPoint.hp = 100000;
        check("Xayda khong co mien thuong", 500, s.truSatThuongNhan(xayda, 500, false));

        System.out.println("--- 5. Cong % chi so theo Cap do Loi ---");
        Player lv = pc(0, 2, 5);
        testApplyBonus(s, lv, 625, 625, 500, "25 cap loi (2 tier + 5)");
        Player lv2 = pc(0, 5, 10);
        testApplyBonus(s, lv2, 1500, 1500, 1200, "60 cap loi (5 tier + 10)");
        Player empty = pc(0, 0, 0);
        testApplyBonus(s, empty, 0, 0, 0, "chua nang cap");

        System.out.println(fail == 0 ? "\n==> TAT CA CONG THUC OK" : "\n==> CO " + fail + " TRUONG HOP SAI");
        System.exit(fail == 0 ? 0 : 1);
    }

    static void testApplyBonus(BanNguyenTinhCauService s, Player p, long hp, long ki, long dame, String ten) {
        NPoint np = p.nPoint;
        s.applyBonus(p, np);
        check(ten + " -> +" + hp + "% HP", hp, np.tlHp.stream().mapToLong(Long::longValue).sum());
        check(ten + " -> +" + ki + "% KI", ki, np.tlMp.stream().mapToLong(Long::longValue).sum());
        check(ten + " -> +" + dame + "% SD", dame, np.tlDame.stream().mapToLong(Long::longValue).sum());
    }

    /** Tao nhan vat rieng cho tung truong hop. */
    static Player pc(int gender, int tier, int level) {
        Player p = new Player();
        p.isPlayer = true;
        p.gender = (byte) gender;
        p.banNguyenTier = tier;
        p.banNguyenLevel = level;
        p.nPoint = new NPoint(p);
        return p;
    }

    static void check(String name, double expected, double actual) {
        boolean ok = Math.abs(expected - actual) < 0.001;
        if (!ok) {
            fail++;
        }
        System.out.println((ok ? "OK   " : "SAI! ") + name + " (mong doi " + expected + " / thuc te " + actual + ")");
    }
}
