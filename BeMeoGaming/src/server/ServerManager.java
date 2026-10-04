package server;

import boss.BrolyManager;
import minigame.DecisionMaker.DecisionMaker;
import minigame.LuckyNumber.LuckyNumber;
import models.Consign.ConsignShopManager;
import jdbc.daos.HistoryTransactionDAO;
import boss.BossManager;
import boss.OtherBossManager;
import boss.TreasureUnderSeaManager;
import boss.SnakeWayManager;
import boss.RedRibbonHQManager;
import boss.GasDestroyManager;
import boss.YardartManager;
import boss.ChristmasEventManager;
import boss.FinalBossManager;
import boss.HalloweenEventManager;
import boss.HungVuongEventManager;
import boss.LunarNewYearEventManager;
import boss.SkillSummonedManager;
import boss.TrungThuEventManager;
import network.inetwork.ISession;
import network.Network;
import network.SessionManager;
import server.io.MyKeyHandler;
import server.io.MySession;
import services.ClanService;
import services.NgocRongNamecService;
import utils.Logger;
import utils.TimeUtil;

import java.util.*;

import Mail.sendMail;
import models.The23rdMartialArtCongress.The23rdMartialArtCongressManager;
import models.DeathOrAliveArena.DeathOrAliveArenaManager;
import event.EventManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jdbc.DBConnecter;
import jdbc.daos.EventDAO;
import models.WorldMartialArtsTournament.WorldMartialArtsTournamentManager;
import network.MessageSendCollect;
import models.ShenronEvent.ShenronEventManager;
import models.SuperRank.SuperRankManager;
import network.inetwork.ISessionAcceptHandler;

public class ServerManager {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Local";
    public static String IP = "127.0.0.1";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static boolean isRunning;

    public void init() {
        Manager.gI();
        HistoryTransactionDAO.deleteHistory();
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
        sendMail.showForm();
        try {
            panel.PanelTrigger.start();
            panel.ControlPanel.showPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        isRunning = true;
        activeServerSocket();
        activeCommandLine();
        new Thread(NgocRongNamecService.gI(), "Update NRNM").start();
        new Thread(SuperRankManager.gI(), "Update Super Rank").start();
        new Thread(The23rdMartialArtCongressManager.gI(), "Update DHVT23").start();
        new Thread(DeathOrAliveArenaManager.gI(), "Update Võ Đài Sinh Tử").start();
        new Thread(WorldMartialArtsTournamentManager.gI(), "Update WMAT").start();
        new Thread(AutoMaintenance.gI(), "Update Bảo Trì Tự Động").start();
        new Thread(ShenronEventManager.gI(), "Update Shenron").start();
//        new Thread(UpdateManager.gI(), "Update Manager").start();
        //new Thread(RemoteServerManager.gI(), "Remote Server Manager").start();
        BossManager.gI().loadBoss();
        Manager.MAPS.forEach(map.Map::initBoss);
        EventManager.gI().init();
        new Thread(BossManager.gI(), "Update boss").start();
        new Thread(YardartManager.gI(), "Update yardart boss").start();
        new Thread(FinalBossManager.gI(), "Update final boss").start();
        new Thread(SkillSummonedManager.gI(), "Update Skill-summoned boss").start();
        new Thread(BrolyManager.gI(), "Update broly boss").start();
        new Thread(OtherBossManager.gI(), "Update other boss").start();
        new Thread(RedRibbonHQManager.gI(), "Update reb ribbon hq boss").start();
        new Thread(TreasureUnderSeaManager.gI(), "Update treasure under sea boss").start();
        new Thread(SnakeWayManager.gI(), "Update snake way boss").start();
        new Thread(GasDestroyManager.gI(), "Update gas destroy boss").start();
        new Thread(TrungThuEventManager.gI(), "Update trung thu event boss").start();
        new Thread(HalloweenEventManager.gI(), "Update halloween event boss").start();
        new Thread(ChristmasEventManager.gI(), "Update christmas event boss").start();
        new Thread(HungVuongEventManager.gI(), "Update Hung Vuong event boss").start();
        new Thread(LunarNewYearEventManager.gI(), "Update lunar new year event boss").start();
        new Thread(LuckyNumber.gI(), "Update Lucky Number").start();
        new Thread(DecisionMaker.gI(), "Update Decision Maker").start();
        SessionManager.gI().startCleanupThread();
        new Thread(() -> {
            Calendar time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, 10000);
            new Timer("Weekend Remove Account").schedule(new TimerTask() {
                @Override
                public void run() {
                    try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement("select * from account"); ResultSet rs = ps.executeQuery()) {
                        long now = System.currentTimeMillis();
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            long date = rs.getTimestamp("last_time_logout").getTime() + (1000 * 60 * 60 * 24 * 10);
                            if (date < now) {
                                // print log to file
                                try (BufferedWriter bw = new BufferedWriter(new FileWriter("delete.txt", true))) {
                                    bw.append("Delete Player:" + id + " | LastTime:" + TimeUtil.formatTime(date, "dd/MM/yyyy") + " | AffterTime:" + TimeUtil.formatTime(now, "dd/MM/yyyy"));
                                    bw.newLine();
                                    bw.append("Delete Account:" + rs.getString("username") + " | LastTime:" + TimeUtil.formatTime(date, "dd/MM/yyyy") + " | AffterTime:" + TimeUtil.formatTime(now, "dd/MM/yyyy"));
                                    bw.newLine();
                                    bw.newLine();
                                }
                                // remove account out sql
                                con.prepareStatement("delete from player where account_id = '" + id + "'").executeUpdate();
                                con.prepareStatement("delete from account where id = '" + id + "'").executeUpdate();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }, time.getTime(), 1000 * 60 * 60 * 24);
        }).start();
    }

    private void activeServerSocket() {
        try {
            Network.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
                @Override
                public void sessionInit(ISession is) {
                    if (!canConnectWithIp(is.getIP())) {
                        Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin()
                                + "m: [CONN] TU CHOI ket noi tu " + is.getIP() + "\n");
                        is.disconnect();
                        return;
                    }
                    Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin()
                            + "m: [CONN] ket noi moi tu " + is.getIP() + "\n");
                    is.setMessageHandler(Controller.gI())
                            .setSendCollect(new MessageSendCollect())
                            .setKeyHandler(new MyKeyHandler())
                            .startCollect();
                }

                @Override
                public void sessionDisconnect(ISession session) {
                    Client.gI().kickSession((MySession) session);
                }
            }).setTypeSessioClone(MySession.class)
                    .setDoSomeThingWhenClose(() -> {
                        Logger.error("SERVER CLOSE\n");
                        System.exit(0);
                    })
                    .start(PORT);
        } catch (Exception e) {
        }
    }

    // Chong connection flood: gioi han toc do ket noi moi theo IP
    private static final long RATE_WINDOW_MS = 10_000L;
    private static final int RATE_MAX_CONNECT = 15;
    private final Map<String, long[]> connRate = new HashMap<>();
    private long lastGuardLog;

    private boolean canConnectWithIp(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }
        // 1. Gioi han tong ket noi de khong het thread (server dang chay)
        try {
            if (SessionManager.gI().getNumSession() >= Manager.MAX_PLAYER + 1000) {
                guardLog("Tu choi ket noi: server day session (" + SessionManager.gI().getNumSession() + ")");
                return false;
            }
        } catch (Exception ignored) {
        }
        // 2. Toi da MAX_PER_IP ket noi cung luc cho moi IP (dem tu danh sach session dang song)
        try {
            int count = 0;
            for (ISession s : SessionManager.gI().getSessions()) {
                if (s != null && ipAddress.equals(s.getIP())) {
                    count++;
                    if (count >= Manager.MAX_PER_IP) {
                        guardLog("Tu choi ket noi: IP " + ipAddress + " vuot qua " + Manager.MAX_PER_IP
                                + " ket noi cung luc");
                        return false;
                    }
                }
            }
        } catch (Exception ignored) {
            // Danh sach session bi sua doi dong thoi -> bo qua, khong chan de tranh chan nham
        }
        // 3. Gioi han toc do ket noi moi (chong connection flood)
        long now = System.currentTimeMillis();
        if (connRate.size() > 20_000) {
            connRate.entrySet().removeIf(e -> now - e.getValue()[0] >= RATE_WINDOW_MS);
        }
        long[] win = connRate.get(ipAddress);
        if (win == null || now - win[0] >= RATE_WINDOW_MS) {
            connRate.put(ipAddress, new long[] { now, 1L });
            return true;
        }
        if (win[1] >= RATE_MAX_CONNECT) {
            guardLog("Tu choi ket noi: IP " + ipAddress + " ket noi qua nhanh (toi da " + RATE_MAX_CONNECT
                    + "/10 giay)");
            return false;
        }
        win[1]++;
        return true;
    }

    // Log chan ket noi toi da 1 lan/10 giay de tranh spam log khi bi tan cong
    private void guardLog(String msg) {
        long now = System.currentTimeMillis();
        if (now - lastGuardLog > 10_000L) {
            lastGuardLog = now;
            Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: " + msg + "\n");
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
        try {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    new Thread(() -> {
                        Maintenance.gI().start(5);
                    }).start();
                } else if (line.equals("athread")) {
                    System.out.println("Số thread hiện tại của Server Dragon Boy: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    System.out.println("Số lượng người chơi hiện tại của Server Dragon Boy: " + Client.gI().getPlayers().size());
                } else if (line.equals("shop")) {
                    Manager.gI().updateShop();
                    System.out.println("===========================DONE UPDATE SHOP===========================");
                } else if (line.equals("item")) {
                    Manager.gI().updateShop();
                    System.out.println("===========================DONE UPDATE ITEM===========================");
                } else if (line.equals("a")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                }
            }
            // stdin bi dong (server chay nen/reopen) -> ket thuc thread thoi, khong crash
        } catch (NoSuchElementException e) {
            Logger.warning("Console input dong, tat tinh nang lenh terminal.\n");
        }
        }, "Active line").start();
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    public void close() {
        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Logger.error("Lỗi save clan!\n");
        }
        try {
            ConsignShopManager.gI().save();
        } catch (Exception e) {
            Logger.error("Lỗi save shop ký gửi!\n");
        }
        Client.gI().close();
        EventDAO.save();
        Logger.success("SUCCESSFULLY MAINTENANCE!\n");
        if (AutoMaintenance.isRunning) {
            AutoMaintenance.isRunning = false;
            try {
                String batchFilePath = "open.bat";
                AutoMaintenance.runBatchFile(batchFilePath);
            } catch (IOException e) {
            }
        }
        System.exit(0);
    }
}
