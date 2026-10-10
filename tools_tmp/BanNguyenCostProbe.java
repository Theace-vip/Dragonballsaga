import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Kiem tra cong thuc gia Ban Nguyen + hang so chi so bang reflection (khong can Player). */
public class BanNguyenCostProbe {

    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("services.BanNguyenTinhCauService");
        Object svc = c.getMethod("gI").invoke(null);
        int loi = 0;

        int ITEM_NANG = c.getField("ITEM_NANG_CAP").getInt(null);
        int ITEM_DOT = c.getField("ITEM_DOT_PHA").getInt(null);
        double hp = c.getField("HP_PERCENT_PER_LEVEL").getDouble(null);
        double ki = c.getField("KI_PERCENT_PER_LEVEL").getDouble(null);
        double dame = c.getField("DAME_PERCENT_PER_LEVEL").getDouble(null);
        System.out.println("ITEM_NANG_CAP = " + ITEM_NANG + " (mong doi 1942)");
        System.out.println("ITEM_DOT_PHA  = " + ITEM_DOT + " (mong doi 1943)");
        System.out.println("HP/KI/DAME moi cap = " + hp + "/" + ki + "/" + dame + " (mong doi 250/250/200)");
        if (ITEM_NANG != 1942 || ITEM_DOT != 1943) {
            loi++;
        }
        if (hp != 250 || ki != 250 || dame != 200) {
            loi++;
        }

        Method manh = c.getMethod("manhCanNangCap", int.class);
        int[] mongManh = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        StringBuilder sb = new StringBuilder("manhCanNangCap(level 0..9) = ");
        for (int lv = 0; lv < 10; lv++) {
            int v = (Integer) manh.invoke(svc, lv);
            sb.append(v).append(lv < 9 ? ", " : "");
            if (v != mongManh[lv]) {
                loi++;
            }
        }
        System.out.println(sb);
        System.out.println("manhCanNangCap(99) = " + manh.invoke(svc, 99) + " (mong doi 1000 - chan tren)");
        if ((Integer) manh.invoke(svc, 99) != 1000) {
            loi++;
        }

        Method hat = c.getMethod("hatGiongCanDotPha", int.class);
        int[] mongHat = {10, 20, 40, 80, 160};
        sb = new StringBuilder("hatGiongCanDotPha(tier 0..4) = ");
        for (int t = 0; t < 5; t++) {
            int v = (Integer) hat.invoke(svc, t);
            sb.append(v).append(t < 4 ? ", " : "");
            if (v != mongHat[t]) {
                loi++;
            }
        }
        System.out.println(sb);
        System.out.println("hatGiongCanDotPha(9) = " + hat.invoke(svc, 9) + " (mong doi 160 - chan tren)");
        if ((Integer) hat.invoke(svc, 9) != 160) {
            loi++;
        }

        Method ten = c.getMethod("tenVatPham", int.class);
        System.out.println("tenVatPham(1942) = " + ten.invoke(null, 1942));
        System.out.println("tenVatPham(1943) = " + ten.invoke(null, 1943));
        System.out.println("tenVatPham(99999) = " + ten.invoke(null, 99999) + " (fallback)");

        Field vsItem = Class.forName("data.DataGame").getField("vsItem");
        System.out.println("DataGame.vsItem = " + vsItem.getByte(null) + " (mong doi 25)");
        if (vsItem.getByte(null) != 25) {
            loi++;
        }

        System.out.println(loi == 0 ? "PROBE_OK (0 loi)" : ("PROBE_FAIL (" + loi + " loi)"));
        if (loi != 0) {
            System.exit(1);
        }
    }
}
