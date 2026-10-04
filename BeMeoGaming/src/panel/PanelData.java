package panel;

import boss.Boss;
import boss.BossStatus;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanelData {
    public static final long START_MILLIS = System.currentTimeMillis();
    public static volatile boolean AUTO_CLEAN_SS = false;
    private static long lastDelayMs = 10;
    private static long lastDelayCheck = 0;
    public static int playersOnline() {
        try { return server.Client.gI().getPlayers().size(); } catch (Exception e) { return 0; }
    }
    public static int sessions() {
        try { return network.SessionManager.gI().getNumSession(); } catch (Exception e) { return 0; }
    }
    public static int threads() {
        try { return Thread.activeCount(); } catch (Exception e) { return 0; }
    }
    public static int heapUsedMb() {
        try { Runtime rt = Runtime.getRuntime(); return (int)((rt.totalMemory()-rt.freeMemory())/1024/1024); } catch (Exception e) { return 0; }
    }
    public static int heapMaxMb() {
        try { Runtime rt = Runtime.getRuntime(); return (int)(rt.maxMemory()/1024/1024); } catch (Exception e) { return 4030; }
    }
    public static double cpuPct() {
        try {
            java.lang.management.OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
            if (os instanceof com.sun.management.OperatingSystemMXBean) {
                double v = ((com.sun.management.OperatingSystemMXBean)os).getProcessCpuLoad();
                if (v < 0) return 0;
                return v*100.0;
            }
        } catch (Exception e) {}
        return 0;
    }
    public static int ipCount() {
        try { return server.ServerManager.CLIENTS.size(); } catch (Exception e) { return 0; }
    }
    public static int maxPerIp() {
        try { return server.Manager.MAX_PER_IP; } catch (Exception e) { return 9999; }
    }
    public static boolean maintenanceRunning() {
        try { return server.Maintenance.isRunning; } catch (Exception e) { return false; }
    }
    public static boolean autoMaintOn() {
        try { return server.AutoMaintenance.AutoMaintenance; } catch (Exception e) { return false; }
    }
    public static String nextMaintText() {
        try {
            if (!server.AutoMaintenance.AutoMaintenance) return "Tat";
            return String.format("%02d:%02d:%02d", server.AutoMaintenance.hours, server.AutoMaintenance.mins, server.AutoMaintenance.second);
        } catch (Exception e) { return "Tat"; }
    }
    @SuppressWarnings({"rawtypes","unchecked"})
    public static int[] bossStats() {
        int alive=0,respawn=0,wait=0;
        String[] managers = {"boss.BossManager","boss.BrolyManager","boss.YardartManager","boss.FinalBossManager","boss.SkillSummonedManager","boss.OtherBossManager","boss.RedRibbonHQManager","boss.TreasureUnderSeaManager","boss.SnakeWayManager","boss.GasDestroyManager","boss.TrungThuEventManager","boss.HalloweenEventManager","boss.ChristmasEventManager","boss.HungVuongEventManager","boss.LunarNewYearEventManager"};
        try {
            for (String cls : managers) {
                try {
                    Class<?> c = Class.forName(cls);
                    java.lang.reflect.Method mGI = c.getMethod("gI");
                    Object mgr = mGI.invoke(null);
                    if (mgr==null) continue;
                    java.lang.reflect.Method mGet;
                    try { mGet = mgr.getClass().getMethod("getBosses"); } catch (NoSuchMethodException ex) { continue; }
                    Object o = mGet.invoke(mgr);
                    if (!(o instanceof List)) continue;
                    List<Boss> list = (List<Boss>)o;
                    Boss[] arr;
                    synchronized(list){ arr = list.toArray(new Boss[0]); }
                    for (Boss b : arr) {
                        if (b==null) continue;
                        try {
                            if (!b.isDie()) alive++;
                            else if (b.bossStatus==BossStatus.RESPAWN) respawn++;
                            else wait++;
                        } catch (Exception ex) { wait++; }
                    }
                } catch (Exception ex) {}
            }
        } catch (Exception e) {}
        return new int[]{alive,respawn,wait};
    }
    public static int giftcodes() {
        try { return models.GiftCode.GiftCodeManager.gI().listGiftCode.size(); } catch (Exception e) { return 0; }
    }
    public static int consign() {
        try { return models.Consign.ConsignShopManager.gI().listItem.size(); } catch (Exception e) { return 0; }
    }
    public static String uptimeText() {
        try {
            long sec=(System.currentTimeMillis()-START_MILLIS)/1000;
            long d=sec/86400; long h=(sec%86400)/3600; long m=(sec%3600)/60; long s=sec%60;
            if (d>0) return String.format("%dd %02dh %02dm %02ds",d,h,m,s);
            return String.format("%02dh %02dm %02ds",h,m,s);
        } catch (Exception e) { return "00h 00m 00s"; }
    }
    public static long dbDelayMs() {
        long now=System.currentTimeMillis();
        if (now-lastDelayCheck<5000) return lastDelayMs;
        lastDelayCheck=now;
        try {
            long st=System.nanoTime();
            try (Connection con=jdbc.DBConnecter.getConnectionServer(); Statement s=con.createStatement()) { s.execute("SELECT 1"); }
            lastDelayMs=(System.nanoTime()-st)/1000000;
            if (lastDelayMs<1) lastDelayMs=1;
        } catch (Exception e) { lastDelayMs=-1; }
        return lastDelayMs;
    }
    public static String clockText() {
        try {
            SimpleDateFormat f1=new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat f2=new SimpleDateFormat("dd/MM/yyyy");
            Date d=new Date();
            return f1.format(d)+" - "+f2.format(d);
        } catch (Exception e) { return ""; }
    }
}
