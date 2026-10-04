package panel.tuning;

import boss.BossData;
import boss.BossesData;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BossResolve - tim FIELD BossesData tu instance BossData (so sanh tham chieu).
 * Cache 1 lan luc load de initBase goi nhanh.
 */
public class BossResolve {
    private static final Map<BossData, String> BY_REF = new LinkedHashMap<>();
    private static volatile boolean loaded = false;

    private static synchronized void ensure() {
        if (loaded) return;
        loaded = true;
        try {
            for (Field f : BossesData.class.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) continue;
                if (!f.getType().getSimpleName().equals("BossData")) continue;
                try {
                    f.setAccessible(true);
                    Object o = f.get(null);
                    if (o instanceof BossData) BY_REF.put((BossData) o, f.getName());
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
    }

    public static String fieldOf(BossData d) {
        if (d == null) return null;
        ensure();
        String s = BY_REF.get(d);
        if (s != null) return s;
        // fallback: so sanh ten (phong truong hop clone)
        try {
            String n = d.getName();
            for (Field f : BossesData.class.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) continue;
                if (!f.getType().getSimpleName().equals("BossData")) continue;
                try {
                    f.setAccessible(true);
                    Object o = f.get(null);
                    if (o instanceof BossData && n != null && n.equals(((BossData) o).getName())) return f.getName();
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return null;
    }

    /** Nhom boss theo ten field de panel phan tang: Boss -> Loai -> Sua. */
    public static String groupOf(String field) {
        if (field == null) return "Khac";
        String f = field.toUpperCase();
        if (f.startsWith("KUKU") || f.startsWith("MAP_DAU_DINH") || f.startsWith("RAMBO") || f.startsWith("SO_") || f.startsWith("TIEU_DOI") || f.startsWith("FIDE") || f.startsWith("DR_KORE") || f.startsWith("ANDROID") || f.startsWith("PIC") || f.startsWith("POC") || f.startsWith("KING_KONG") || f.startsWith("XEN") || f.startsWith("SIEU_BO") || f.startsWith("BLACK_GOKU") || f.startsWith("CUMBER") || f.startsWith("MABU") || f.startsWith("SUPER_BU") || f.startsWith("BU_") || f.startsWith("KID_BU") || f.startsWith("GOKU") || f.startsWith("CADIC") || f.startsWith("DRABURA") || f.startsWith("BUI_BUI") || f.startsWith("YACON") || f.startsWith("O_DO") || f.startsWith("XINBATO") || f.startsWith("CHA_PA") || f.startsWith("PON_PUT") || f.startsWith("CHAN_XU") || f.startsWith("TAU_PAY") || f.startsWith("YAMCHA") || f.startsWith("JACKY") || f.startsWith("THIEN_XIN") || f.startsWith("LIU_LIU") || f.startsWith("BUJIN") || f.startsWith("KOGU") || f.startsWith("ZANGYA") || f.startsWith("BIDO") || f.startsWith("BOJACK") || f.startsWith("TAP_SU") || f.startsWith("TAN_BINH") || f.startsWith("CHIEN_BINH") || f.startsWith("COOLER")) return "Boss Cot Truyen / Pho Ban";
        if (f.startsWith("KHIDOT") || f.startsWith("NGUYETTHAN") || f.startsWith("NHATTTHAN") || f.startsWith("DRACULA") || f.startsWith("NGUOI_VO") || f.startsWith("BONG_BANG") || f.startsWith("VUA_QUY") || f.startsWith("THO_DAU")) return "Ngu Hanh Son (Trung Thu)";
        if (f.startsWith("MA_TROI") || f.startsWith("DOI") || f.startsWith("BI_MA")) return "Halloween";
        if (f.startsWith("CUU_VI") || f.startsWith("OBITO")) return "Nhan Gioi";
        if (f.startsWith("HAI_KHUYEN") || f.startsWith("BACH_LANG") || f.startsWith("THIET_MA") || f.startsWith("VUA_BACH")) return "Than Thu";
        if (f.startsWith("ONG_GIA_NOEL")) return "Noel";
        if (f.startsWith("THUY_TINH") || f.startsWith("SON_TINH")) return "Hung Vuong";
        if (f.startsWith("LAN_CON")) return "Tet";
        if (f.startsWith("KARIN") || f.startsWith("TAUPAYPAY") || f.startsWith("YAJIRO") || f.startsWith("MRPOPO") || f.startsWith("THUONG_DE") || f.startsWith("KHI_BUBBLES") || f.startsWith("THAN_VU") || f.startsWith("TO_SU") || f.startsWith("WHIS") || f.startsWith("GOLDEN") || f.startsWith("DEATH_BEAM")) return "Than / Training";
        if (f.startsWith("DORAEMON") || f.startsWith("XUKA") || f.startsWith("XEKO") || f.startsWith("CHAIEN") || f.startsWith("NOBITA") || f.startsWith("CAUTHANTHU") || f.startsWith("CAU_THAN")) return "Doraemon / Cau Than Thu";
        if (f.startsWith("BLACKGOKU") || f.startsWith("BLACKGOKUROSE") || f.startsWith("THANHUYDIET") || f.startsWith("THIENSU") || f.startsWith("CHAMPA") || f.startsWith("VASDO") || f.startsWith("ADMIN") || f.startsWith("VUA_COLD") || f.startsWith("LUFFY") || f.startsWith("ZORO") || f.startsWith("SANJI") || f.startsWith("BROOK") || f.startsWith("CHOPPER") || f.startsWith("NAMI") || f.startsWith("FRANKY") || f.startsWith("USOPP") || f.startsWith("ROBIN") || f.startsWith("GOHAN") || f.startsWith("TEST_DAME") || f.startsWith("BOSSDIEMFAM") || f.startsWith("TANTHUTNSM")) return "Dac Biet / Test";
        if (f.startsWith("WORLD_BOSS") || f.startsWith("WORLD")) return "Boss The Gioi (Bat Tu)";
        return "Khac";
    }
}
