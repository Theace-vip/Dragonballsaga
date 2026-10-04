package boss;

import boss.BossData;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import player.Player;
import utils.Util;

/**
 * GenericBoss - boss "chung" dung cho moi BossID co BossData trong BossesData.
 * Muc dich: admin co the goi BAT KY boss nao (ke ca cac con chua co class rieng)
 * tu panel ma khong loi "khong ho tro".
 *  - fieldName: ten FIELD static trong BossesData (vi du "KUKU", "XENCON1")
 *  - Tu dong lay BossData tuong ung bang reflection.
 */
public class GenericBoss extends Boss {

    private static final long serialVersionUID = 1L;

    /** Lay BossData tu BossesData theo ten field (viet hoa). */
    public static BossData getDataByField(String fieldName) {
        try {
            Field f = BossesData.class.getDeclaredField(fieldName);
            if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(BossData.class)) {
                f.setAccessible(true);
                Object o = f.get(null);
                if (o instanceof BossData) return (BossData) o;
            }
        } catch (Exception e) { }
        return null;
    }

    public GenericBoss(int id, String fieldName) throws Exception {
        this(id, fieldName, BossType.PHOBAN);
    }

    public GenericBoss(int id, String fieldName, BossType bossType) throws Exception {
        super(bossType, id, getDataByField(fieldName));
    }

    @Override
    public void reward(Player plKill) {
        // drop mac dinh rong; admin cau hinh qua panel_boss_override (Boss.die)
    }
}
