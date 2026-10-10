import item.Item;
import models.Template.ItemTemplate;
import models.Combine.manifest.EpSaoTrangBi;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Kiem tra quy tac moi: 5 mon (type 0-4) -> Ep Sao Pha Le, tu o thu 6 (type >= 5, gom
 * cai trang = 5 va giap tap luyen = 32) -> Ep Sao Phu Kien / Duc Kham Lo, va MOI trang bi
 * deu Pha Le Hoa duoc theo cap VIP.
 */
public class StarRuleProbe {

    static Unsafe unsafe;
    static Method khamTargetMethod;
    static Method ducTargetMethod;

    public static void main(String[] args) throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        unsafe = (Unsafe) f.get(null);

        Class<?> kham = Class.forName("models.Combine.manifest.KhamEpDa");
        Class<?> duc = Class.forName("models.Combine.manifest.DucLoKham");
        khamTargetMethod = kham.getDeclaredMethod("isTrangBiEpSaoPhuKien", Item.class);
        khamTargetMethod.setAccessible(true);
        ducTargetMethod = duc.getDeclaredMethod("isTrangBiDucLoKham", Item.class);
        ducTargetMethod.setAccessible(true);
        Method khamStone = kham.getDeclaredMethod("isDaEpSaoPhuKien", Item.class);
        khamStone.setAccessible(true);

        // type -> ten, mong doi (epSaoPhaLe, epSaoPhuKien, phaLeHoa)
        Object[][] cases = {
                {0, 9999, "Ao", true, false, true},
                {1, 9999, "Quan", true, false, true},
                {2, 9999, "Gang", true, false, true},
                {3, 9999, "Giay", true, false, true},
                {4, 9999, "Nhan/Rada", true, false, true},
                {5, 884, "Cai trang", false, true, true},
                {32, 531, "Giap tap luyen", false, true, true},
                {82, 1810, "Chan menh", false, true, true},
                {23, 346, "Do bay", false, true, true},
                {27, 1271, "Nguyen lieu (Luong Bac)", false, false, false},
                {87, 1206, "Da kham", false, false, false},
                {30, 441, "Sao pha le", false, false, false},
                {30, 1588, "Sach Ep Premium", false, false, false},
                {12, 14, "Ngoc Rong 1 sao", false, false, false},
        };

        int fail = 0;
        System.out.printf("%-6s %-26s %-8s %-8s %-8s %s%n", "type", "ten", "eSaoPL", "eSaoPK", "phaLeHoa", "ket qua");
        for (Object[] c : cases) {
            int type = (Integer) c[0];
            int id = (Integer) c[1];
            String name = (String) c[2];
            boolean wantEp = (Boolean) c[3];
            boolean wantPhuKien = (Boolean) c[4];
            boolean wantPhaLe = (Boolean) c[5];

            Item it = newItem(type, id);
            boolean gotEp = EpSaoTrangBi.isTrangBiEpPhaLeHoa(it);
            boolean gotPhuKien = (Boolean) khamTargetMethod.invoke(null, it);
            boolean gotPhaLe = it.canPhaLeHoa();
            boolean gotDuc = (Boolean) ducTargetMethod.invoke(null, it);

            boolean ok = gotEp == wantEp && gotPhuKien == wantPhuKien && gotPhaLe == wantPhaLe;
            if (!ok) {
                fail++;
            }
            System.out.printf("%-6d %-26s %-8s %-8s %-8s %s (ducLoKham=%s)%n", type, name, gotEp, gotPhuKien,
                    gotPhaLe, ok ? "OK" : "SAI! (mong doi " + wantEp + "/" + wantPhuKien + "/" + wantPhaLe + ")", gotDuc);
        }

        // da / sao pha le khong duoc nhan nham thanh trang bi, nhung phai la nguyen lieu ep
        Item daKham = newItem(87, 1206);
        Item saoPhaLe = newItem(30, 441);
        Item sachPremium = newItem(30, 1588);
        boolean daOk = !(Boolean) khamTargetMethod.invoke(null, daKham)
                && (Boolean) khamStone.invoke(null, daKham)
                && (Boolean) khamStone.invoke(null, saoPhaLe)
                && (Boolean) khamStone.invoke(null, sachPremium)
                && !(Boolean) khamTargetMethod.invoke(null, saoPhaLe)
                && !(Boolean) khamTargetMethod.invoke(null, sachPremium);
        System.out.println((daOk ? "OK  " : "SAI!") + " Da kham / Sao pha le: la nguyen lieu ep, khong bi nhan thanh trang bi");
        if (!daOk) {
            fail++;
        }

        System.out.println(fail == 0 ? "\n==> TAT CA QUY TAC DUNG" : "\n==> CO " + fail + " TRUONG HOP SAI");
        System.exit(fail == 0 ? 0 : 1);
    }

    static Item newItem(int type, int id) throws Exception {
        Item it = new Item();
        ItemTemplate tpl = (ItemTemplate) unsafe.allocateInstance(ItemTemplate.class);
        set(ItemTemplate.class, tpl, "type", type);
        set(ItemTemplate.class, tpl, "id", id);
        set(ItemTemplate.class, tpl, "name", "probe type " + type);
        it.template = tpl;
        return it;
    }

    static void set(Class<?> cls, Object obj, String field, Object value) throws Exception {
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(field);
                f.setAccessible(true);
                Object v = value;
                Class<?> ft = f.getType();
                if (value instanceof Number n) {
                    if (ft == byte.class) {
                        v = n.byteValue();
                    } else if (ft == short.class) {
                        v = n.shortValue();
                    } else if (ft == int.class) {
                        v = n.intValue();
                    } else if (ft == long.class) {
                        v = n.longValue();
                    }
                }
                f.set(obj, v);
                return;
            } catch (NoSuchFieldException ignored) {
            }
        }
        throw new NoSuchFieldException(field);
    }
}
