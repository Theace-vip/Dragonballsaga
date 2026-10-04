package boss;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * WorldBossScheduler - goi Boss The Gioi theo lich SPAWN_TIMES moi ngay.
 * Try-catch khong crash server. Tieng Viet khong dau.
 */
public class WorldBossScheduler {

    private static boolean started = false;
    private static String lastFiredMinute = "";

    public static synchronized void start() {
        if (started) return;
        started = true;
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20000);
                    tick();
                } catch (Exception e) {}
            }
        });
        try {
            t.setDaemon(true);
            t.setName("WorldBoss-Scheduler");
            t.start();
        } catch (Exception e) {}
    }

    private static void tick() {
        try {
            panel.tuning.WorldBossTuning.load();
            if (!panel.tuning.WorldBossTuning.ON) return;
            List<String> times = panel.tuning.WorldBossTuning.spawnTimeList();
            if (times == null || times.isEmpty()) return;
            Calendar c = Calendar.getInstance();
            String cur = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            if (!times.contains(cur)) return;
            String dayMin = "";
            try {
                dayMin = new SimpleDateFormat("yyyyMMdd_HH:mm").format(new Date());
            } catch (Exception e) {
                dayMin = cur;
            }
            if (dayMin.equals(lastFiredMinute)) return;
            lastFiredMinute = dayMin;
            try {
                for (Boss b : BossManager.gI().getBosses()) {
                    try {
                        if (b != null && b.id == BossID.WORLD_BOSS && b.zone != null) return;
                    } catch (Exception e) {}
                }
            } catch (Exception e) {}
            try {
                BossManager.gI().createBoss(BossID.WORLD_BOSS);
            } catch (Exception e) {}
        } catch (Exception e) {}
    }
}
