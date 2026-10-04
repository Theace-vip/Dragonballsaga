package server.io;

import java.net.Socket;

import player.Player;
import server.Controller;
import data.DataGame;
import jdbc.daos.NDVSqlFetcher;
import item.Item;
import java.io.IOException;
import network.Session;
import network.Message;
import server.Client;
import server.Maintenance;
import server.Manager;
import models.AntiLogin;
import services.Service;
import utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.TimeUtil;

public class MySession extends Session {

    private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();
    public Player player;

    public byte timeWait = 100;
    public boolean sentKey;

    public static final byte[] KEYS = { 0 };
    public byte curR, curW;

    public String ipAddress;
    public boolean isAdmin;
    public int userId;
    public String uu;
    public String pp;

    public int typeClient;
    public byte zoomLevel;

    public long lastTimeLogout;
    public long lastTimeLogin;
    public boolean joinedGame;

    public long lastTimeReadMessage;

    public boolean actived;
    public boolean registeringAccount;

    public boolean check;

    public int goldBar;
    public long gold;
    public int eventPoint;
     public long diemfam;
    
    public List<Item> itemsReward;
    public String dataReward;
    public boolean is_gift_box;
    public double bdPlayer;

    public int version = 237; // mac dinh 237 (client double) - neu client khong gui lai cmd2 khi reconnect van dung format dung, khong con version=0 -> crash client
    public int vnd;
    public int tongnap;
    public int vip;
    public int luotquay;

    public boolean finishUpdate;

    public MySession(Socket socket) {
        super(socket);
        ipAddress = socket.getInetAddress().getHostAddress();
    }

    @Override
    public void sendKey() throws Exception {
        super.sendKey();
        this.startSend();
    }

    public void sendSessionKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; i++) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            this.sendMessage(msg);
            msg.cleanup();
            sentKey = true;
        } catch (IOException e) {
        }
    }

    // Log chuan doan dang nhap vao server_latest.log
    private static void logLogin(String msg) {
        Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: " + msg + "\n");
    }

    public void login(String username, String password) {
        AntiLogin al = ANTILOGIN.get(this.ipAddress);
        if (al == null) {
            al = new AntiLogin();
            ANTILOGIN.put(this.ipAddress, al);
        }
        logLogin("[LOGIN] Yeu cau login: user=" + username + " ip=" + ipAddress
                + " sessionPlayer=" + (this.player == null ? "null" : this.player.name));
        if (!al.canLogin()) {
            logLogin("[LOGIN] -> bi chan AntiLogin (sai qua nhieu lan), gui thong bao OK");
            Service.gI().sendThongBaoOK(this, al.getNotifyCannotLogin());
            return;
        }
        if (Manager.LOCAL) {
            logLogin("[LOGIN] -> bi chan Manager.LOCAL");
            Service.gI().sendThongBaoOK(this, "Server này chỉ để lưu dữ liệu\nVui lòng qua server khác");
            return;
        }
        if (Maintenance.isRunning) {
            logLogin("[LOGIN] -> bi chan dang bao tri");
            Service.gI().sendThongBaoOK(this, "Server đang trong thời gian bảo trì, vui lòng quay lại sau");
            return;
        }
        if (!this.isAdmin && Client.gI().getPlayers().size() >= Manager.MAX_PLAYER) {
            logLogin("[LOGIN] -> bi chan day may chu (MAX_PLAYER)");
            Service.gI().sendThongBaoOK(this, "Máy chủ hiện đang quá tải, "
                    + "cư dân vui lòng di chuyển sang máy chủ khác.");
            return;
        }
        if (this.player == null) {
            Player pl = null;
            try {
                long st = System.currentTimeMillis();
                this.uu = username;
                this.pp = password;
                pl = NDVSqlFetcher.login(this, al);
                    DataGame.sendSmallVersion(this);
                if (pl != null) {
                    // -77 max small
                    // -93 bgitem version
                    DataGame.sendBgItemVersion(this);

                    this.timeWait = 0;
                    this.joinedGame = true;
                    pl.nPoint.calPoint();
                    pl.nPoint.setHp(pl.nPoint.hp);
                    pl.nPoint.setMp(pl.nPoint.mp);
                    pl.zone.addPlayer(pl);
                    if (pl.pet != null) {
                        pl.pet.nPoint.calPoint();
                        pl.pet.nPoint.setHp(pl.pet.nPoint.hp);
                        pl.pet.nPoint.setMp(pl.pet.nPoint.mp);
                    }

                    pl.setSession(this);
                    Client.gI().put(pl);
                    this.player = pl;
                    // -28 -4 version data game
                    DataGame.sendVersionGame(this);
                    // -31 data item background
                    DataGame.sendDataItemBG(this);
                    Controller.gI().sendInfo(this);
                    Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin()
                            + "m: Successful login for player " + this.player.name + "[" + this.version + "]" + ": "
                            + (System.currentTimeMillis() - st) + " ms\n");
                    if (this.player.notify != null && !this.player.notify.equals("null")
                            && !this.player.notify.isEmpty() && this.player.notify.length() > 0) {
                        Service.gI().sendThongBao(this.player, this.player.notify);
                        this.player.notify = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (pl != null) {
                    pl.dispose();
                }
            }
        } else {
            // Nguon gay man hinh treo: session da co player ma client login lai
            // ma server khong tra loi gi -> client cho vo han
            logLogin("[LOGIN] -> session DA CO player " + this.player.name
                    + " -> BO QUA, khong tra loi gi (client se treo man hinh)");
        }
    }
}
