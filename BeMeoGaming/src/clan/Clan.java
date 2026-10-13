package clan;

import jdbc.DBConnecter;
import models.RedRibbonHQ.RedRibbonHQ;
import services.ClanService;

import java.util.ArrayList;
import java.util.List;

import player.Player;
import server.Client;
import services.Service;
import network.Message;
import jdbc.daos.NDVSqlFetcher;
import utils.Logger;
import utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import models.TreasureUnderSea.TreasureUnderSea;
import models.SnakeWay.SnakeWay;
import models.DestronGas.DestronGas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.TimeUtil;

public class Clan {

    public static int NEXT_ID = 0;

    public int clanMessageId = 0;
    private final List<ClanMessage> clanMessages;

    public static final byte LEADER = 0;
    public static final byte DEPUTY = 1;
    public static final byte MEMBER = 2;

    /** Cap bang toi da (level duoc gui cho client dang byte). */
    public static final int LEVEL_MAX = 127;

    /** Slot thanh vien toi da (max_member luu dang byte). */
    public static final int MEMBER_MAX = 127;

    public int id;
    public int imgId;
    public String name;
    public String name2;
    public String slogan;
    public int createTime;
    public long powerPoint;
    public byte maxMember;
    public int level;
    public boolean active;
    public int capsuleClan;

    /** Co thay doi (diem capsule cong don theo quai) chua ghi xuong DB. */
    public transient volatile boolean dirtySave;

    /** Lan cuoi gui lai thong tin bang cho thanh vien online (chong spam goi tin). */
    private volatile long lastTimeSendCapsule;

    public long lastTimeOpenDoanhTrai;
    public boolean haveGoneDoanhTrai;
    public RedRibbonHQ doanhTrai;
    public Player playerOpenDoanhTrai;

    public final List<ClanMember> members;
    public final List<Player> membersInGame;

    public TreasureUnderSea BanDoKhoBau;
    public long lastTimeOpenBanDoKhoBau;
    public Player playerOpenBanDoKhoBau;

    public SnakeWay ConDuongRanDoc;
    public long lastTimeOpenConDuongRanDoc;
    public Player playerOpenConDuongRanDoc;

    public DestronGas KhiGasHuyDiet;
    public long lastTimeOpenKhiGasHuyDiet;
    public Player playerOpenKhiGasHuyDiet;
    public int timesPerDayKGHD;

    public long timeUpdateClan;

    public Clan() {
        this.id = NEXT_ID++;
        this.name = "";
        this.name2 = "";
        this.slogan = "";
        this.maxMember = 10;
        this.level = 1;
        this.createTime = (int) (System.currentTimeMillis() / 1000);
        this.members = new ArrayList<>();
        this.membersInGame = new ArrayList<>();
        this.clanMessages = new ArrayList<>();
    }

    public boolean canUpdateClan(Player player) {
        if (Util.canDoWithTime(timeUpdateClan, 60000)) {
            timeUpdateClan = System.currentTimeMillis();
            return true;
        }
        Service.gI().sendThongBao(player, "Vui lòng đợi " + TimeUtil.getTimeLeft(timeUpdateClan, 60) + " nữa");
        return false;
    }

    public ClanMember getLeader() {
        for (ClanMember cm : members) {
            if (cm.role == LEADER) {
                return cm;
            }
        }
        ClanMember cm = new ClanMember();
        cm.name = "Bang chủ";
        return cm;
    }

    public byte getRole(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id) {
                return cm.role;
            }
        }
        return -1;
    }

    public boolean isLeader(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == LEADER) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeputy(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == DEPUTY) {
                return true;
            }
        }
        return false;
    }

    public void addSMTNClan(Player plOri, double param) {
        for (int i = this.membersInGame.size() - 1; i >= 0; i--) {
            Player pl = this.membersInGame.get(i);
            if (!plOri.equals(pl) && pl != null && pl.zone != null && plOri.zone.equals(pl.zone)) {
                double tnsm = param / (Math.abs(Service.gI().getCurrLevel(pl) - Service.gI().getCurrLevel(plOri)) + 1);
                Service.gI().addSMTN(pl, (byte) 1, tnsm, false);
            }
        }
    }
    
    public void addPhapTacClan(Player plOri, long param) {
        if (plOri != null) {
            if (param > 1) {
                param = Util.nextInt(2, 5);
            }
            for (Player pl : this.membersInGame) {
                if (pl != null) {
                    if (plOri.zone != null) {
                        if (plOri.SagaThienDao >= 500 && pl.SagaThienDao >= 500) {
                            if (!plOri.equals(pl) && plOri.zone.equals(pl.zone)) {
                                pl.PhapTac_ThienDao += param;
                                pl.ctkclan = param;
                            }
                        } else {
                            if (!plOri.equals(pl) && plOri.zone.equals(pl.zone)) {
                                pl.PhapTac_ThienDao += param;
                                pl.ctkclan = param;
                            }
                        }
                    }
                }
            }
        }
    }

   public void sendMessageClan(ClanMessage cmg) {
        Message msg;
       Player ply = NDVSqlFetcher.loadById(cmg.playerId); 
        try {
            msg = new Message(-51);
            msg.writer().writeByte(cmg.type);
            msg.writer().writeInt(cmg.id);
            msg.writer().writeInt(cmg.playerId);
            if (cmg.type == 2) {
                msg.writer().writeUTF(cmg.playerName + " (" + Util.numberToMoney(cmg.playerPower) + ")");
            } else {
                msg.writer().writeUTF(cmg.playerName);
            }
            msg.writer().writeByte(cmg.role);
            msg.writer().writeInt(cmg.time);
            if (cmg.type == 0) {
                msg.writer().writeUTF(cmg.text);
                msg.writer().writeByte(cmg.color);
                msg.writer().writeByte(ply.gender);
            } else if (cmg.type == 1) {
                msg.writer().writeByte(cmg.receiveDonate);
                msg.writer().writeByte(cmg.maxDonate);
                msg.writer().writeByte(cmg.isNewMessage); 
                msg.writer().writeByte(ply.gender);
            }
            for (Player pl : this.membersInGame) {
                pl.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
        }
    }
    public void addClanMessage(ClanMessage cmg) {
        this.clanMessages.add(0, cmg);
        if (clanMessages.size() > 20) {
            for (int i = clanMessages.size() - 1; i >= 20; i--) {
                clanMessages.remove(i).dispose();
            }
        }
    }

    public ClanMessage getClanMessage(int clanMessageId) {
        for (ClanMessage cmg : this.clanMessages) {
            if (cmg.id == clanMessageId) {
                return cmg;
            }
        }
        return null;
    }

    public List<ClanMessage> getCurrClanMessages() {
        List<ClanMessage> list = new ArrayList();
        if (this.clanMessages.size() <= 20) {
            list.addAll(this.clanMessages);
        } else {
            for (int i = 0; i < 20; i++) {
                list.add(this.clanMessages.get(i));
            }
        }
        return list;
    }

    /**
     * Cong diem capsule bang khi ha quai (Mob.getItemMobReward goi).
     * - TRAN NGAY theo TUNG NGUOI (panel: clan_capsule_ngay_toi_da / clan_capsule_sau_tran).
     * - Cong don vao capsuleClan + diem cong hien ca nhan cua nguoi ha quai.
     * - Danh dau dirtySave de thread dinh ky ghi xuong DB (ca bo dem diem ngay).
     * - Gui lai thong tin bang cho toi da 1 lan / 2 giay de thanh vien thay ngay.
     */
    public synchronized void addCapsule(long points, Player gainer) {
        if (points <= 0) {
            return;
        }
        ClanMember cm = null;
        long award = points;
        if (gainer != null) {
            cm = getClanMember((int) gainer.id);
            if (cm != null) {
                award = diemCapsuleTheoNgay(cm, points);
            }
        }
        this.dirtySave = true;
        if (award <= 0) {
            return;
        }
        long total = (long) this.capsuleClan + award;
        this.capsuleClan = total > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) total;
        if (cm != null) {
            cm.memberPoint = congAnToan(cm.memberPoint, award);
            cm.clanPoint = congAnToan(cm.clanPoint, award);
        }
        long now = System.currentTimeMillis();
        if (now - lastTimeSendCapsule >= 2000L) {
            lastTimeSendCapsule = now;
            sendMyClanForAllMember();
        }
    }

    /** Khoa ngay (yyyyMMdd) - dung de reset bo dem diem capsule ngay. */
    public static int ngayHienTai() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        return c.get(java.util.Calendar.YEAR) * 10000 + (c.get(java.util.Calendar.MONTH) + 1) * 100
                + c.get(java.util.Calendar.DAY_OF_MONTH);
    }

    /** Cong so nguyen an toan (chong tran int). */
    private static int congAnToan(int a, long b) {
        long s = (long) a + b;
        if (s < 0) {
            return 0;
        }
        return s > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) s;
    }

    /**
     * Diem capsule THUC NHAN cua 1 thanh vien khi ha quai, co tran ngay:
     * - Sang ngay moi: tu dong reset bo dem ve 0.
     * - Chua het tran ngay: lay theo cong thuc (khong vuot qua phan con lai cua tran).
     * - Het tran ngay: moi quai chi con "clan_capsule_sau_tran" diem.
     */
    public synchronized long diemCapsuleTheoNgay(ClanMember cm, long diemCongThuc) {
        if (cm == null) {
            return diemCongThuc;
        }
        int ngay = ngayHienTai();
        if (cm.capsuleDay != ngay) {
            cm.capsuleDay = ngay;
            cm.capsuleToday = 0;
        }
        int tranNgay = 20000;
        int sauTran = 100;
        try {
            tranNgay = panel.tuning.SystemTuning.getI("clan_capsule_ngay_toi_da");
            sauTran = panel.tuning.SystemTuning.getI("clan_capsule_sau_tran");
        } catch (Exception e) {
        }
        if (tranNgay <= 0) {
            cm.capsuleToday = congAnToan(cm.capsuleToday, diemCongThuc);
            return diemCongThuc;
        }
        long nhan;
        if (cm.capsuleToday >= tranNgay) {
            nhan = Math.max(0, sauTran);
        } else {
            nhan = Math.min(diemCongThuc, (long) tranNgay - cm.capsuleToday);
        }
        cm.capsuleToday = congAnToan(cm.capsuleToday, nhan);
        return nhan;
    }

    public void sendMyClanForAllMember() {
        List<Player> snapshot = new ArrayList<>(this.membersInGame);
        for (Player pl : snapshot) {
            if (pl != null) {
                ClanService.gI().sendMyClan(pl);
            }
        }
    }

    public void sendFlagBagForAllMember() {
        for (Player pl : this.membersInGame) {
            if (pl != null) {
                Service.gI().sendFlagBag(pl);
            }
        }
    }

    public void addMemberOnline(Player player) {
        this.membersInGame.add(player);
    }

    public void removeMemberOnline(ClanMember cm, Player player) {
        if (player != null) {
            this.membersInGame.remove(player);
        }
        if (cm != null) {
            for (int i = this.membersInGame.size() - 1; i >= 0; i--) {
                if (this.membersInGame.get(i).id == cm.id) {
                    this.membersInGame.remove(i);
                    break;
                }
            }
        }
    }

    public Player getPlayerOnline(int playerId) {
        for (Player player : this.membersInGame) {
            if (player.id == playerId) {
                return player;
            }
        }

        Player playeroffline = NDVSqlFetcher.loadById(playerId);
        if (playeroffline != null) {
            return playeroffline;
        }

        return null;
    }

    //load db danh sách member
    public void addClanMember(ClanMember cm) {
        this.members.add(cm);
    }

    //thêm vào khi player tạo mới clan or mới vào clan
    public void addClanMember(Player player, byte role) {
        ClanMember cm = new ClanMember(player, this, role);
        this.members.add(cm);
        player.clanMember = cm;
    }

    //xóa khi member rời clan or bị kích
    public void removeClanMember(ClanMember cm) {
        this.members.remove(cm);
        cm.dispose();
    }

    public byte getCurrMembers() {
        return (byte) this.members.size();
    }

    public List<ClanMember> getMembers() {
        return this.members;
    }

    public ClanMember getClanMember(int memberId) {
        for (ClanMember cm : members) {
            if (cm.id == memberId) {
                return cm;
            }
        }
        return null;
    }

    public void reloadClanMember() {
        for (ClanMember cm : this.members) {
            Player pl = Client.gI().getPlayer(cm.id);
            if (pl != null) {
                cm.powerPoint = pl.nPoint.power;
            }
        }
    }

    public void insert() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
            dataObject.put("name", cm.name);
            dataObject.put("head", cm.head);
            dataObject.put("body", cm.body);
            dataObject.put("leg", cm.leg);
            dataObject.put("role", cm.role);
            dataObject.put("donate", cm.donate);
            dataObject.put("receive_donate", cm.receiveDonate);
            dataObject.put("member_point", cm.memberPoint);
            dataObject.put("clan_point", cm.clanPoint);
            dataObject.put("capsule_today", cm.capsuleToday);
            dataObject.put("capsule_day", cm.capsuleDay);
            dataObject.put("join_time", cm.joinTime);
            dataObject.put("ask_pea_time", cm.timeAskPea);
            dataObject.put("power", cm.powerPoint);
            dataArray.add(dataObject.toJSONString());
            dataObject.clear();
        }

        String member = dataArray.toJSONString();
        dataArray.clear();

        String top = dataArray.toJSONString();

        PreparedStatement ps = null;
        try (Connection con = DBConnecter.getConnectionServer();) {
            ps = con.prepareStatement("insert into clan (id, name, name_2, slogan, img_id, power_point, max_member, clan_point, level, members, tops) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, this.id);
            ps.setString(2, this.name);
            ps.setString(3, this.name2);
            ps.setString(4, this.slogan);
            ps.setInt(5, this.imgId);
            ps.setLong(6, this.powerPoint);
            ps.setByte(7, this.maxMember);
            ps.setInt(8, this.capsuleClan);
            ps.setInt(9, this.level);
            ps.setString(10, member);
            ps.setString(11, top);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi insert clan vào db");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }

    }

    public void update() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
            dataObject.put("name", cm.name);
            dataObject.put("head", cm.head);
            dataObject.put("body", cm.body);
            dataObject.put("leg", cm.leg);
            dataObject.put("role", cm.role);
            dataObject.put("donate", cm.donate);
            dataObject.put("receive_donate", cm.receiveDonate);
            dataObject.put("member_point", cm.memberPoint);
            dataObject.put("clan_point", cm.clanPoint);
            dataObject.put("capsule_today", cm.capsuleToday);
            dataObject.put("capsule_day", cm.capsuleDay);
            dataObject.put("join_time", cm.joinTime);
            dataObject.put("ask_pea_time", cm.timeAskPea);
            dataObject.put("power", cm.powerPoint);
            dataArray.add(dataObject.toJSONString());
            dataObject.clear();
        }

        String member = dataArray.toJSONString();

        dataArray.clear();

        PreparedStatement ps = null;
        try (Connection con = DBConnecter.getConnectionServer();) {
            ps = con.prepareStatement("update clan set slogan = ?, img_id = ?, power_point = ?, max_member = ?, clan_point = ?, "
                    + "level = ?, members = ?, name_2 = ?, tops = ? where id = ? limit 1");
            ps.setString(1, this.slogan);
            ps.setInt(2, this.imgId);
            ps.setLong(3, this.powerPoint);
            ps.setByte(4, this.maxMember);
            ps.setInt(5, this.capsuleClan);
            ps.setInt(6, this.level);
            ps.setString(7, member);
            ps.setString(8, this.name2);
            ps.setString(9, "cc");
            ps.setInt(10, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi insert clan vào db");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public void deleteDB(int id) {
        PreparedStatement ps;
        try (Connection con = DBConnecter.getConnectionServer();) {
            ps = con.prepareStatement("delete from clan where id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi delete clan");
        }
    }

}
