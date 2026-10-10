import player.Player;

/** Kiem tra duong load that: doc nhan vat tu DB (select * from player) -> cot ban_nguyen moi. */
public class LoadProbe {
    public static void main(String[] args) throws Exception {
        Player p = jdbc.daos.NDVSqlFetcher.loadById(1);
        if (p == null) {
            System.out.println("KHONG LOAD DUOC");
            System.exit(1);
        }
        System.out.println("Load DB OK: name=" + p.name + ", gender=" + p.gender
                + ", banNguyenLevel=" + p.banNguyenLevel + ", banNguyenTier=" + p.banNguyenTier
                + ", hpMax=" + (long) p.nPoint.hpMax + ", dame=" + (long) p.nPoint.dame);
        boolean ok = p.banNguyenLevel == 0 && p.banNguyenTier == 0;
        System.out.println(ok ? "==> Cot ban_nguyen doc dung (mac dinh 0/0)"
                : "==> Gia tri bat thuong: " + p.banNguyenLevel + "/" + p.banNguyenTier);
        System.exit(ok ? 0 : 1);
    }
}
