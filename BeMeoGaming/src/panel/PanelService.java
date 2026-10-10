package panel;

import boss.Boss;
import boss.BossData;
import boss.BossesData;
import boss.BossID;
import boss.BossStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PanelService - lop API trung tam cho Admin Panel (dot 2).
 * Moi chuc nang panel deu goi qua day de sau nay de tai su dung.
 * Dung JDBC truc tiep (DBConnecter.getConnectionServer) de tranh phu thuoc NDVResultSet.
 */
public class PanelService {

    // ============ ACCOUNT ============
    public static List<Map<String, Object>> listAccounts(String search, int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        String sql = "SELECT id,username,password,ban,is_admin,vnd,tongnap,vip,active,last_time_login,last_time_logout,ip_address FROM account "
                + (search != null && !search.trim().isEmpty() ? "WHERE username LIKE ? OR id = ? " : "")
                + "ORDER BY id DESC LIMIT " + Math.max(1, Math.min(2000, limit));
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(1, "%" + search.trim() + "%");
                int idTry = -1;
                try { idTry = Integer.parseInt(search.trim()); } catch (Exception e) {}
                ps.setInt(2, idTry);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("username", rs.getString("username"));
                    m.put("password", rs.getString("password"));
                    m.put("ban", rs.getInt("ban"));
                    m.put("is_admin", rs.getInt("is_admin"));
                    m.put("vnd", rs.getLong("vnd"));
                    m.put("tongnap", rs.getLong("tongnap"));
                    m.put("vip", rs.getInt("vip"));
                    m.put("active", rs.getInt("active"));
                    m.put("last_login", rs.getString("last_time_login"));
                    m.put("ip", rs.getString("ip_address"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String banAccount(int id, boolean ban) {
        try {
            jdbc.DBConnecter.executeUpdate("update account set ban = ? where id = ?", ban ? 1 : 0, id);
            audit("banAccount", "id=" + id + " ban=" + ban);
            return "OK";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String setAdmin(int id, boolean admin) {
        try {
            jdbc.DBConnecter.executeUpdate("update account set is_admin = ? where id = ?", admin ? 1 : 0, id);
            audit("setAdmin", "id=" + id + " admin=" + admin);
            return "OK";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String resetPass(int id, String pass) {
        try {
            if (pass == null || pass.length() < 6 || pass.length() > 100) {
                return "Loi: mat khau can 6-100 ky tu";
            }
            jdbc.DBConnecter.executeUpdate("update account set password = ? where id = ?", utils.PasswordUtil.hash(pass), id);
            audit("resetPass", "id=" + id);
            return "OK - da doi mat khau cho id " + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** Tao tai khoan moi tu panel (username + password). Mat khau bam PBKDF2 cung format dang ky in-game. */
    public static String createAccount(String username, String password) {
        if (username == null || username.trim().isEmpty()) return "Loi: chua nhap username";
        username = username.trim();
        if (username.length() < 3 || username.length() > 20) return "Loi: username can 3-20 ky tu";
        if (password == null || password.length() < 6 || password.length() > 100) return "Loi: mat khau can 6-100 ky tu";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO account (username, password, ip_address, admin, mabaove, anh_web, gioithieu, gmail) "
                                + "VALUES (?, ?, ?, 0, 0, '', 0, '')")) {
            ps.setString(1, username);
            ps.setString(2, utils.PasswordUtil.hash(password));
            ps.setString(3, "panel");
            ps.executeUpdate();
            audit("createAccount", "username=" + username);
            return "OK - da tao tai khoan " + username;
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1062) return "Loi: username '" + username + "' da ton tai";
            return "Loi: " + e.getMessage();
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** Doi ten dang nhap (username) cho account da chon. */
    public static String renameAccount(int id, String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) return "Loi: chua nhap username moi";
        newUsername = newUsername.trim();
        if (newUsername.length() < 3 || newUsername.length() > 20) return "Loi: username can 3-20 ky tu";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(
                        "update account set username = ?, update_time = CURRENT_TIMESTAMP where id = ?")) {
            ps.setString(1, newUsername);
            ps.setInt(2, id);
            int n = ps.executeUpdate();
            if (n == 0) return "Loi: khong tim thay id " + id;
            audit("renameAccount", "id=" + id + " -> " + newUsername);
            return "OK - id " + id + " da doi ten thanh " + newUsername;
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1062) return "Loi: username '" + newUsername + "' da ton tai";
            return "Loi: " + e.getMessage();
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String buffVnd(int id, long vndAdd, long tongnapAdd, int vipSet) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("update account set vnd = vnd + ?, tongnap = tongnap + ?" + (vipSet >= 0 ? ", vip = ?" : "") + " where id = ?")) {
            ps.setLong(1, vndAdd);
            ps.setLong(2, tongnapAdd);
            if (vipSet >= 0) { ps.setInt(3, vipSet); ps.setInt(4, id); }
            else ps.setInt(3, id);
            ps.executeUpdate();
            audit("buffVnd", "id=" + id + " vnd+=" + vndAdd + " tongnap+=" + tongnapAdd + " vip=" + vipSet);
            return "OK";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ NAP TIEN THEO TUNG PHAN ============
    /**
     * Cac phan tien rieng le (admin sua tung phan mot):
     * - vnd: so du Cash (nguoi choi dung mua trong game)
     * - temp_vnd: so du nap khi OFFLINE, tu + vnd o lan login (NDVSqlFetcher)
     * - tongnap: tong nap - mo mo qua nap dau (50k/200k/500k/1tr/2tr), moc phuc loi tab 2,
     *   top nap, event, achievement
     * - danap: da nap - bang xep hang top nap tren WEB (top-nap.php)
     * - active: mo thanh vien 20k (web tu dong mo khi nap > 20k neu _AutoMember)
     * - vip: VIP tai khoan (account.vip -> session.vip, NPoint.t2278)
     * - napdau: player.NapDau - qua nap dau da nhan (0 = cho phep nhan lai)
     * - sagavip: player.Saga_VIP - VIP in-game (khau truoc, aura, mua VIP)
     */
    public static String[][] napParts() {
        return new String[][]{
            {"vnd", "Số dư Cash (account.vnd)"},
            {"temp_vnd", "Số dư chờ nạp (account.temp_vnd)"},
            {"tongnap", "Tổng nạp (account.tongnap)"},
            {"danap", "Đã nạp - Top web (account.danap)"},
            {"active", "Mở thành viên (account.active)"},
            {"vip", "VIP tài khoản (account.vip)"},
            {"napdau", "Quà nạp đầu đã nhận (player.NapDau)"},
            {"sagavip", "VIP in-game (player.Saga_VIP)"}
        };
    }

    /** Doc hien trang tat ca phan tien cua 1 account (key khop voi napParts). */
    public static Map<String, Object> napStatus(int accId) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("found", false);
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT a.vnd, a.temp_vnd, a.tongnap, a.danap, a.active, a.vip, "
                        + "p.NapDau, p.Saga_VIP FROM account a "
                        + "LEFT JOIN player p ON p.account_id = a.id WHERE a.id = ? LIMIT 1")) {
            ps.setInt(1, accId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    out.put("found", true);
                    out.put("vnd", rs.getLong("vnd"));
                    out.put("temp_vnd", rs.getLong("temp_vnd"));
                    out.put("tongnap", rs.getLong("tongnap"));
                    out.put("danap", rs.getLong("danap"));
                    out.put("active", rs.getLong("active"));
                    out.put("vip", rs.getLong("vip"));
                    out.put("napdau", rs.getLong("NapDau"));
                    out.put("sagavip", rs.getLong("Saga_VIP"));
                }
            }
        } catch (Exception e) {
            out.put("error", e.getMessage());
        }
        try {
            out.put("online", server.Client.gI().getPlayerByUser(accId) != null);
        } catch (Exception e) {
            out.put("online", false);
        }
        return out;
    }

    private static player.Player onlineByAcc(int accId) {
        try {
            return server.Client.gI().getPlayerByUser(accId);
        } catch (Exception e) {
            return null;
        }
    }

    private static String fmtSo(long n) {
        return String.format("%,d", n).replace(',', '.');
    }

    /**
     * Cong (add=true) hoac dat dung gia tri (add=false) mot phan tien.
     * Neu player dang online thi dong bo luon session/player va gui tien ve client.
     */
    public static String napApply(int accId, String part, long value, boolean add) {
        try {
            switch (part) {
                case "vnd":
                    jdbc.DBConnecter.executeUpdate(add ? "update account set vnd = vnd + ? where id = ?"
                            : "update account set vnd = ? where id = ?", value, accId);
                    break;
                case "temp_vnd":
                    jdbc.DBConnecter.executeUpdate(add ? "update account set temp_vnd = temp_vnd + ? where id = ?"
                            : "update account set temp_vnd = ? where id = ?", value, accId);
                    break;
                case "tongnap":
                    jdbc.DBConnecter.executeUpdate(add ? "update account set tongnap = tongnap + ? where id = ?"
                            : "update account set tongnap = ? where id = ?", value, accId);
                    break;
                case "danap":
                    jdbc.DBConnecter.executeUpdate(add ? "update account set danap = danap + ? where id = ?"
                            : "update account set danap = ? where id = ?", value, accId);
                    break;
                case "active":
                    value = Math.max(0, value);
                    jdbc.DBConnecter.executeUpdate(add ? "update account set active = active + ? where id = ?"
                            : "update account set active = ? where id = ?", value, accId);
                    break;
                case "vip":
                    value = Math.max(0, value);
                    jdbc.DBConnecter.executeUpdate(add ? "update account set vip = vip + ? where id = ?"
                            : "update account set vip = ? where id = ?", value, accId);
                    break;
                case "napdau":
                    value = Math.max(0, value);
                    jdbc.DBConnecter.executeUpdate(add ? "update player set NapDau = NapDau + ? where account_id = ?"
                            : "update player set NapDau = ? where account_id = ?", value, accId);
                    break;
                case "sagavip":
                    value = Math.max(0, Math.min(127, value)); // player.Saga_VIP la byte
                    jdbc.DBConnecter.executeUpdate(add ? "update player set Saga_VIP = Saga_VIP + ? where account_id = ?"
                            : "update player set Saga_VIP = ? where account_id = ?", value, accId);
                    break;
                default:
                    return "Loi: phan tien khong ton tai: " + part;
            }

            player.Player p = onlineByAcc(accId);
            String onlineNote = "";
            if (p != null && p.getSession() != null) {
                switch (part) {
                    case "vnd":
                        p.getSession().vnd = add ? p.getSession().vnd + (int) value : (int) value;
                        break;
                    case "tongnap":
                        p.getSession().tongnap = add ? p.getSession().tongnap + (int) value : (int) value;
                        break;
                    case "vip":
                        p.getSession().vip = add ? p.getSession().vip + (int) value : (int) value;
                        break;
                    case "active":
                        p.getSession().actived = add ? (p.getSession().actived || value != 0) : value != 0;
                        break;
                    case "napdau":
                        p.NapDau = add ? p.NapDau + value : value;
                        break;
                    case "sagavip":
                        p.Saga_VIP = (byte) (add ? Math.max(0, Math.min(127, p.Saga_VIP + value)) : value);
                        break;
                    default:
                        break; // temp_vnd / danap: chi ghi DB, khong co session
                }
                try {
                    services.Service.getInstance().sendMoney(p);
                } catch (Exception e) {
                }
                onlineNote = " | player ONLINE: da dong bo trong RAM";
            }
            audit("napApply", "id=" + accId + " " + part + (add ? "+=" : "=") + value);
            return "OK - " + part + (add ? " + " : " = ") + fmtSo(value) + onlineNote;
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    /**
     * Nap day du 1 lan (giong nap that cua web): + so du, + tong nap, + danap,
     * mo thanh vien, dat lai qua nap dau.
     * Luu y: moc nap (phuc loi tab 2), qua nap dau, top nap, event deu doc tu
     * tongnap -> chi can + tongnap la cac moc tu hien lech, khong can sua them cho.
     */
    public static String napFull(int accId, long amount, boolean vndNgay, boolean tongnap,
            boolean danap, boolean active, boolean resetNapdau) {
        if (amount <= 0) {
            return "Loi: so tien nap phai > 0";
        }
        try {
            StringBuilder ch = new StringBuilder();
            if (vndNgay) {
                jdbc.DBConnecter.executeUpdate("update account set vnd = vnd + ? where id = ?", amount, accId);
                ch.append("so_du+").append(fmtSo(amount));
            } else {
                jdbc.DBConnecter.executeUpdate("update account set temp_vnd = temp_vnd + ? where id = ?", amount, accId);
                ch.append("cho_nap+").append(fmtSo(amount));
            }
            if (tongnap) {
                jdbc.DBConnecter.executeUpdate("update account set tongnap = tongnap + ? where id = ?", amount, accId);
                ch.append(", tongnap+").append(fmtSo(amount));
            }
            if (danap) {
                jdbc.DBConnecter.executeUpdate("update account set danap = danap + ? where id = ?", amount, accId);
                ch.append(", danap+").append(fmtSo(amount));
            }
            if (active) {
                jdbc.DBConnecter.executeUpdate("update account set active = 1 where id = ? and active = 0", accId);
                ch.append(", active=1");
            }
            if (resetNapdau) {
                jdbc.DBConnecter.executeUpdate("update player set NapDau = 0 where account_id = ?", accId);
                ch.append(", napdau=0 (nhan lai qua nap dau)");
            }

            player.Player p = onlineByAcc(accId);
            String note;
            if (p != null && p.getSession() != null) {
                if (vndNgay) {
                    p.getSession().vnd += (int) amount;
                }
                if (tongnap) {
                    p.getSession().tongnap += (int) amount;
                }
                if (active) {
                    p.getSession().actived = true;
                }
                if (resetNapdau) {
                    p.NapDau = 0;
                }
                try {
                    services.Service.getInstance().sendMoney(p);
                } catch (Exception e) {
                }
                note = vndNgay ? "player ONLINE: da cong trong RAM" : "player ONLINE: tien vao cho nap, cham len so du o login sau";
            } else {
                note = "player OFFLINE: " + (vndNgay ? "da vao so du" : "tien vao cho nap, tu + so du o login");
            }
            audit("napFull", "id=" + accId + " amount=" + amount + " " + ch);
            return "OK - nap " + fmtSo(amount) + ": " + ch + " | " + note;
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    public static String deleteAccount(int id) {
        try {
            jdbc.DBConnecter.executeUpdate("delete from account where id = ?", id);
            audit("deleteAccount", "id=" + id);
            return "OK (da xoa account - player giu lai de an toan)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static List<Map<String, Object>> topNap(int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("SELECT id,username,vnd,tongnap,vip FROM account ORDER BY tongnap DESC LIMIT " + limit);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("username", rs.getString("username"));
                m.put("vnd", rs.getLong("vnd"));
                m.put("tongnap", rs.getLong("tongnap"));
                m.put("vip", rs.getInt("vip"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ============ PLAYER ONLINE ============
    public static List<player.Player> onlinePlayers() {
        try { return new ArrayList<>(server.Client.gI().getPlayers()); }
        catch (Exception e) { return new ArrayList<>(); }
    }

    public static player.Player findOnline(String name) {
        try { return server.Client.gI().getPlayer(name); } catch (Exception e) { return null; }
    }

    public static String kickPlayer(String name) {
        try {
            player.Player p = server.Client.gI().getPlayer(name);
            if (p == null) return "Player offline";
            server.Client.gI().kickSession((server.io.MySession) p.getSession());
            audit("kickPlayer", name);
            return "OK da kick " + name;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String kickAll() {
        try { server.Client.gI().kickAll(); audit("kickAll", "kick tat ca player online"); return "OK"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String notifyPlayer(String name, String text) {
        try {
            player.Player p = server.Client.gI().getPlayer(name);
            if (p == null) return "Player offline";
            services.Service.gI().sendThongBaoFromAdmin(p, text);
            jdbc.daos.PlayerDAO.updatePlayer(p);
            return "OK";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String broadcast(String text) {
        try { services.Service.gI().sendThongBaoAllPlayer(text); return "OK"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String buffPlayer(String name, double powerAdd, double tnAdd, long goldAdd, int gemAdd, int rubyAdd) {
        try {
            player.Player p = server.Client.gI().getPlayer(name);
            if (p == null) return "Player offline - chi buff duoc online (offline thi sua DB player.data_point/data_inventory)";
            if (powerAdd != 0) p.nPoint.power += powerAdd;
            if (tnAdd != 0) p.nPoint.tiemNang += tnAdd;
            if (goldAdd != 0) p.inventory.gold = Math.min(p.inventory.gold + goldAdd, 2000000000L);
            if (gemAdd != 0) p.inventory.gem = Math.min(p.inventory.gem + gemAdd, 200000000);
            if (rubyAdd != 0) p.inventory.ruby = Math.min(p.inventory.ruby + rubyAdd, 200000000);
            services.Service.gI().point(p);
            services.InventoryService.gI().sendItemBag(p);
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("buffPlayer", name + " power+=" + (long) powerAdd + " tn+=" + (long) tnAdd + " gold+=" + goldAdd + " gem+=" + gemAdd + " ruby+=" + rubyAdd);
            return "OK";
        } catch (Exception e) { e.printStackTrace(); return "Loi: " + e.getMessage(); }
    }

    public static String giveItem(String name, int tempId, int qty, String optStr) {
        return giveItem(name, tempId, qty, optStr, "bag");
    }

    /**
     * Tang vat pham vao vi tri cu the (bag / box) - ho tro ca player OFFLINE.
     * Neu online: dung dich vu game + gui goi tin; neu offline: sua list truc tiep roi ghi DB.
     */
    public static String giveItem(String name, int tempId, int qty, String optStr, String where) {
        try {
            boolean[] on = new boolean[1];
            player.Player p = getPlayerForEdit(name, on);
            if (p == null) return "Khong tim thay player (chua co nhan vat?): " + name;
            if (qty <= 0) qty = 1;
            item.Item item = services.ItemService.gI().createNewItem((short) tempId, qty);
            if (item.template == null || item.template.id != tempId) return "temp_id khong ton tai: " + tempId;
            if (optStr != null && !optStr.trim().isEmpty()) {
                for (String pair : optStr.split(",")) {
                    String[] kv = pair.trim().split(":");
                    if (kv.length == 2) {
                        try { item.itemOptions.add(new item.Item.ItemOption(Integer.parseInt(kv[0].trim()), Long.parseLong(kv[1].trim()))); } catch (Exception ex) {}
                    }
                }
            }
            boolean vaoBox = "box".equals(where);
            boolean ok;
            if (on[0]) {
                if (vaoBox) {
                    ok = services.InventoryService.gI().addItemBox(p, item);
                    try { services.InventoryService.gI().sendItemBox(p); } catch (Exception e) { }
                } else {
                    if (services.InventoryService.gI().getCountEmptyBag(p) <= 0) return "Het o hanh trang";
                    ok = services.InventoryService.gI().addItemBag(p, item, 999999);
                    try { services.InventoryService.gI().sendItemBag(p); } catch (Exception e) { }
                }
            } else {
                if (vaoBox) ok = services.InventoryService.gI().addItemList(p.inventory.itemsBox, item);
                else ok = services.InventoryService.gI().addItemList(p.inventory.itemsBag, item);
            }
            if (!ok) return "Khong them duoc (hanh trang / ruong day?)";
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("giveItem", name + " temp=" + tempId + " x" + qty + " vao " + where
                    + (optStr != null && !optStr.trim().isEmpty() ? " opt[" + optStr + "]" : "")
                    + (on[0] ? " (online)" : " (offline)"));
            return "OK da tang " + item.template.name + " x" + qty + " vao " + (vaoBox ? "RUONG" : "HANH TRANG");
        } catch (Exception e) { e.printStackTrace(); return "Loi: " + e.getMessage(); }
    }

    // ================= LICH SU GIAO DICH (chi doc DB) =================
    private static String trunc(String s, int n) {
        if (s == null) return "";
        return s.length() <= n ? s : s.substring(0, n) + "...";
    }

    private static int lim(int limit) {
        return Math.max(1, Math.min(5000, limit));
    }

    /** Trade 2 player (history_transaction): time, p1, p2, i1, i2 */
    public static List<Map<String, Object>> listTrades(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        String sql = "SELECT time_tran, player_1, player_2, item_player_1, item_player_2 FROM history_transaction "
                + (like.isEmpty() ? "" : "WHERE player_1 LIKE ? OR player_2 LIKE ? ")
                + "ORDER BY id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) { ps.setString(1, "%" + like + "%"); ps.setString(2, "%" + like + "%"); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("time_tran"));
                    m.put("p1", rs.getString("player_1"));
                    m.put("p2", rs.getString("player_2"));
                    m.put("i1", trunc(rs.getString("item_player_1"), 150));
                    m.put("i2", trunc(rs.getString("item_player_2"), 150));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /**
     * Nap the (napthe): time, user, ingame (ten nhan vat), telco, amount, status, serial, code.
     * Ten ingame lay tu account.username -> player.name (join theo TRIM de chiu duoc du lieu cu co khoang trang).
     */
    public static List<Map<String, Object>> listNapThe(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        // Du lieu cu co the dinh \r\n trong user_nap -> cat sach de hien thi va join dung account
        String userExpr = "TRIM(REPLACE(REPLACE(n.user_nap, '\\r', ''), '\\n', ''))";
        String sql = "SELECT n.created_at, " + userExpr + " AS user_nap, p.name AS ingame, n.telco, n.amount, n.status, n.serial, n.code "
                + "FROM napthe n "
                + "LEFT JOIN account a ON a.username = " + userExpr + " "
                + "LEFT JOIN player p ON p.account_id = a.id "
                + (like.isEmpty() ? "" : "WHERE " + userExpr + " LIKE ? OR n.serial LIKE ? OR n.code LIKE ? OR p.name LIKE ? ")
                + "ORDER BY n.id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) { ps.setString(1, "%" + like + "%"); ps.setString(2, "%" + like + "%"); ps.setString(3, "%" + like + "%"); ps.setString(4, "%" + like + "%"); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("created_at"));
                    m.put("user", rs.getString("user_nap"));
                    String ingame = rs.getString("ingame");
                    m.put("ingame", ingame == null ? "" : ingame);
                    m.put("telco", rs.getString("telco"));
                    m.put("amount", rs.getString("amount"));
                    m.put("status", rs.getString("status"));
                    m.put("serial", rs.getString("serial"));
                    m.put("code", rs.getString("code"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ============ TY LE NAP (su kien x2 / x3 / ... / x50) ============
    /** Tran ty le nap toi da (lan). */
    public static final int NAP_RATE_MAX = 50;

    private static volatile boolean napRateTableReady = false;

    private static void ensureNapRateTable() {
        if (napRateTableReady) return;
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS panel_nap_rate ("
                        + "id TINYINT NOT NULL PRIMARY KEY, "
                        + "rate INT NOT NULL DEFAULT 1, "
                        + "enabled TINYINT(1) NOT NULL DEFAULT 0, "
                        + "until_at DATETIME NULL, "
                        + "note VARCHAR(255) NULL, "
                        + "updated_by VARCHAR(64) NULL, "
                        + "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)")) {
            ps.execute();
            napRateTableReady = true;
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Ty le nap hien tai: rate (1..50), enabled (0/1), until_at, note, updated_by, updated_at. */
    public static Map<String, Object> getNapRate() {
        ensureNapRateTable();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("rate", 1);
        m.put("enabled", 0);
        m.put("until_at", null);
        m.put("note", "");
        m.put("updated_by", "");
        m.put("updated_at", "");
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("SELECT rate, enabled, until_at, note, updated_by, updated_at FROM panel_nap_rate WHERE id = 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                m.put("rate", rs.getInt("rate"));
                m.put("enabled", rs.getInt("enabled"));
                m.put("until_at", rs.getString("until_at"));
                m.put("note", rs.getString("note"));
                m.put("updated_by", rs.getString("updated_by"));
                m.put("updated_at", rs.getString("updated_at"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return m;
    }

    /**
     * Luu ty le nap. Tran toi da x50 (NAP_RATE_MAX).
     * until: 'yyyy-MM-dd HH:mm' hoac rong = khong gioi han thoi gian.
     */
    public static String setNapRate(int rate, boolean enabled, String until, String note, String actor) {
        if (rate < 1) rate = 1;
        if (rate > NAP_RATE_MAX) rate = NAP_RATE_MAX;
        ensureNapRateTable();
        String untilSql = (until == null || until.trim().isEmpty()) ? null : until.trim();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("INSERT INTO panel_nap_rate (id, rate, enabled, until_at, note, updated_by) VALUES (1, ?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE rate = VALUES(rate), enabled = VALUES(enabled), until_at = VALUES(until_at), note = VALUES(note), updated_by = VALUES(updated_by)")) {
            ps.setInt(1, rate);
            ps.setInt(2, enabled ? 1 : 0);
            if (untilSql == null) ps.setNull(3, java.sql.Types.TIMESTAMP); else ps.setString(3, untilSql);
            ps.setString(4, note == null ? "" : note);
            ps.setString(5, actor == null || actor.trim().isEmpty() ? "panel" : actor.trim());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return "Loi luu ty le nap: " + e.getMessage(); }
        audit("nap_rate", "rate=x" + rate + " enabled=" + (enabled ? 1 : 0) + (untilSql == null ? "" : " until=" + untilSql) + (note == null || note.isEmpty() ? "" : " note=" + note));
        return "Da luu ty le nap: x" + rate + (enabled ? " (DANG BAT)" : " (TAT)") + (untilSql == null ? "" : ", ket thuc " + untilSql)
                + ". Web nap (nrokura.site) doc truc tiep tu bang panel_nap_rate -> ap dung ngay cho chuyen khoan.";
    }

    /** Nap bang bank (history_bank): time, user, vnd, cash, code, desc */
    public static List<Map<String, Object>> listBankTx(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        String sql = "SELECT created_at, username, amount_vnd, amount_cash, code, description FROM history_bank "
                + (like.isEmpty() ? "" : "WHERE username LIKE ? OR code LIKE ? ")
                + "ORDER BY id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) { ps.setString(1, "%" + like + "%"); ps.setString(2, "%" + like + "%"); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("created_at"));
                    m.put("user", rs.getString("username"));
                    m.put("vnd", rs.getString("amount_vnd"));
                    m.put("cash", rs.getString("amount_cash"));
                    m.put("code", rs.getString("code"));
                    m.put("desc", trunc(rs.getString("description"), 200));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Don nap MoMo/ZaloPay (table order): time, acc, orderid, type, amount, status, transid */
    public static List<Map<String, Object>> listOrderTx(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        String sql = "SELECT created_at, account_id, orderId, orderType, amount, status, transId FROM `order` "
                + (like.isEmpty() ? "" : "WHERE orderId LIKE ? OR account_id = ? ")
                + "ORDER BY id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) {
                ps.setString(1, "%" + like + "%");
                int idTry = -1;
                try { idTry = Integer.parseInt(like); } catch (Exception e) {}
                ps.setInt(2, idTry);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("created_at"));
                    m.put("acc", rs.getString("account_id"));
                    m.put("orderid", rs.getString("orderId"));
                    m.put("type", rs.getString("orderType"));
                    m.put("amount", rs.getString("amount"));
                    m.put("status", rs.getString("status"));
                    m.put("transid", trunc(rs.getString("transId"), 80));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** MoMo trans (momo_trans): time, user, txid, amount, content */
    public static List<Map<String, Object>> listMomoTx(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        String sql = "SELECT created_at, username, transaction_id, amount, content FROM momo_trans "
                + (like.isEmpty() ? "" : "WHERE username LIKE ? OR transaction_id LIKE ? ")
                + "ORDER BY id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) { ps.setString(1, "%" + like + "%"); ps.setString(2, "%" + like + "%"); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("created_at"));
                    m.put("user", rs.getString("username"));
                    m.put("txid", rs.getString("transaction_id"));
                    m.put("amount", rs.getString("amount"));
                    m.put("content", trunc(rs.getString("content"), 150));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Web shop (web_shop_history): time, user, item, amount, price, total */
    public static List<Map<String, Object>> listWebShopTx(String filter, int limit) {
        String like = filter == null ? "" : filter.trim();
        String sql = "SELECT created_at, username, item_name, amount, price, total_price FROM web_shop_history "
                + (like.isEmpty() ? "" : "WHERE username LIKE ? OR item_name LIKE ? ")
                + "ORDER BY id DESC LIMIT " + lim(limit);
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (!like.isEmpty()) { ps.setString(1, "%" + like + "%"); ps.setString(2, "%" + like + "%"); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("time", rs.getString("created_at"));
                    m.put("user", rs.getString("username"));
                    m.put("item", rs.getString("item_name"));
                    m.put("amount", rs.getString("amount"));
                    m.put("price", rs.getString("price"));
                    m.put("total", rs.getString("total_price"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String findItemTemplate(String key) {
        try {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                boolean match = false;
                try {
                    if (String.valueOf(t.id).equals(key.trim())) match = true;
                    if (t.name != null && boss.BossManager.convertString(t.name.toLowerCase()).contains(boss.BossManager.convertString(key.toLowerCase()))) match = true;
                } catch (Exception e) {}
                if (match) {
                    sb.append(t.id).append(" | ").append(t.name).append(" | type=").append(t.type).append("\n");
                    if (++count >= 30) break;
                }
            }
            return sb.length() == 0 ? "Khong tim thay" : sb.toString();
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ BOSS ============
    public static List<Boss> allBosses() {
        List<Boss> out = new ArrayList<>();
        String[] managers = {"boss.BossManager","boss.BrolyManager","boss.FinalBossManager","boss.SkillSummonedManager","boss.OtherBossManager","boss.RedRibbonHQManager","boss.TreasureUnderSeaManager","boss.SnakeWayManager","boss.GasDestroyManager","boss.TrungThuEventManager","boss.HalloweenEventManager","boss.ChristmasEventManager","boss.HungVuongEventManager","boss.LunarNewYearEventManager"};
        for (String cls : managers) {
            try {
                Class<?> c = Class.forName(cls);
                Object mgr = c.getMethod("gI").invoke(null);
                if (mgr == null) continue;
                Object o;
                try { o = mgr.getClass().getMethod("getBosses").invoke(mgr); } catch (NoSuchMethodException ex) { continue; }
                if (o instanceof List) {
                    List<Boss> list = (List<Boss>) o;
                    Boss[] arr;
                    synchronized (list) { arr = list.toArray(new Boss[0]); }
                    for (Boss b : arr) if (b != null) out.add(b);
                }
            } catch (Exception e) {}
        }
        return out;
    }

    public static String bossInfo(Boss b) {
        try {
            String name = "Boss#" + b.id;
            try {
                java.lang.reflect.Field f = Boss.class.getDeclaredField("data");
                f.setAccessible(true);
                Object arr = f.get(b);
                if (arr instanceof boss.BossData[]) {
                    boss.BossData[] data = (boss.BossData[]) arr;
                    if (data.length > 0 && data[0] != null) name = data[0].getName();
                }
            } catch (Exception ex) {}
            String map = (b.zone != null && b.zone.map != null) ? (b.zone.map.mapName + "[" + b.zone.map.mapId + "]-khu" + b.zone.zoneId) : "chua xuat hien";
            return name + " | hp=" + (long) b.nPoint.hp + " | status=" + b.bossStatus + " | " + map;
        } catch (Exception e) { return "Boss#" + b.id; }
    }

    public static String killBoss(Boss b) {
        try { b.die(null); return "OK da kill " + bossInfo(b); }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String removeBossFromMap(Boss b) {
        try { b.leaveMap(); return "OK da duoi khoi map"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String summonBoss(int bossId) {
        try {
            Boss b = boss.BossManager.gI().createBoss(bossId);
            if (b == null) return "BossID khong ho tro: " + bossId + " (xem boss.BossID)";
            return "OK da goi boss id=" + bossId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT 4: TIM BOSS SPAWN O DAU (live) ============
    /** Tim boss dang online theo ten/field/ID/map - tra ve list map de ve bang. */
    public static List<Map<String, Object>> searchBossSpawn(String q) {
        List<Map<String, Object>> out = new ArrayList<>();
        String query = q == null ? "" : q.trim().toLowerCase();
        try {
            for (Boss b : allBosses()) {
                if (b == null) continue;
                String field = "";
                String name = "Boss#" + b.id;
                try {
                    java.lang.reflect.Field f = Boss.class.getDeclaredField("data");
                    f.setAccessible(true);
                    Object arr = f.get(b);
                    if (arr instanceof boss.BossData[]) {
                        boss.BossData[] data = (boss.BossData[]) arr;
                        if (data.length > 0 && data[0] != null) {
                            name = data[0].getName();
                            String ff = panel.tuning.BossResolve.fieldOf(data[0]);
                            if (ff != null) field = ff;
                        }
                    }
                } catch (Exception ex) {}
                String mapStr = "chua xuat hien (dang nghi)";
                String mapNameOnly = "";
                int mapId = -1, zoneId = -1, countInMap = 0;
                try {
                    if (b.zone != null && b.zone.map != null) {
                        mapId = b.zone.map.mapId;
                        zoneId = b.zone.zoneId;
                        mapNameOnly = b.zone.map.mapName;
                        mapStr = b.zone.map.mapName + " [" + mapId + "] - Khu " + zoneId;
                        try { countInMap = b.zone.getNumOfPlayers(); } catch (Exception ex) {}
                    }
                } catch (Exception ex) {}
                String status = "";
                try { status = String.valueOf(b.bossStatus); } catch (Exception ex) {}
                long hp = 0, hpMax = 0;
                try { hp = (long) b.nPoint.hp; hpMax = (long) b.nPoint.hpMax; } catch (Exception ex) {}
                if (!query.isEmpty()) {
                    String hay = (name + " " + field + " " + b.id + " " + mapNameOnly + " " + mapId).toLowerCase();
                    boolean hit = hay.contains(query);
                    if (!hit) {
                        boolean numOnly = true;
                        for (char c : query.toCharArray()) if (!Character.isDigit(c) && c != '-' && c != ' ') { numOnly = false; break; }
                        if (numOnly) {
                            try { if (mapId == Integer.parseInt(query.trim())) hit = true; } catch (Exception ex) {}
                        }
                    }
                    if (!hit) continue;
                }
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("name", name);
                m.put("field", field);
                m.put("bossId", b.id);
                m.put("nhom", panel.tuning.BossResolve.groupOf(field.isEmpty() ? null : field));
                m.put("map", mapStr);
                m.put("mapId", mapId);
                m.put("zone", zoneId);
                m.put("nguoiTrongKhu", countInMap);
                m.put("status", status);
                m.put("hp", hp);
                m.put("hpMax", hpMax);
                out.add(m);
            }
        } catch (Exception e) {}
        out.sort((a, b2) -> String.valueOf(a.get("name")).compareTo(String.valueOf(b2.get("name"))));
        return out;
    }

    // ============ DOT 4: WORLD BOSS (bat tu, top dame) ============
    public static List<Map<String, Object>> listAllMaps() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            if (server.Manager.MAPS != null && !server.Manager.MAPS.isEmpty()) {
                for (map.Map mp : server.Manager.MAPS) {
                    try {
                        if (mp == null) continue;
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("id", mp.mapId);
                        m.put("name", mp.mapName);
                        out.add(m);
                    } catch (Exception ex) {}
                }
                if (!out.isEmpty()) return out;
            }
        } catch (Exception e) {}
        try {
            if (server.Manager.MAP_TEMPLATES != null) {
                for (models.Template.MapTemplate t : server.Manager.MAP_TEMPLATES) {
                    try {
                        if (t == null) continue;
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("id", t.id);
                        m.put("name", t.name);
                        out.add(m);
                    } catch (Exception ex) {}
                }
            }
        } catch (Exception e) {}
        return out;
    }

    public static String mapNamePublic(int mapId) {
        try { return worldBossMapName(mapId); } catch (Exception e) { return "map " + mapId; }
    }

    public static Map<String, Object> worldBossState() {
        Map<String, Object> m = new LinkedHashMap<>();
        try {
            panel.tuning.WorldBossTuning.load();
            m.put("on", panel.tuning.WorldBossTuning.ON);
            m.put("mapId", panel.tuning.WorldBossTuning.MAP_ID);
            m.put("mapName", worldBossMapName(panel.tuning.WorldBossTuning.MAP_ID));
            m.put("durationMin", panel.tuning.WorldBossTuning.DURATION_MIN);
            m.put("hp", panel.tuning.WorldBossTuning.HP);
            m.put("dame", panel.tuning.WorldBossTuning.DAME);
            m.put("top1", panel.tuning.WorldBossTuning.TOP1);
            m.put("top23", panel.tuning.WorldBossTuning.TOP23);
            m.put("top410", panel.tuning.WorldBossTuning.TOP410);
            m.put("consol", panel.tuning.WorldBossTuning.CONSOL);
            m.put("minDame", panel.tuning.WorldBossTuning.MIN_DAME);
            try { m.put("spawnTimes", panel.tuning.WorldBossTuning.SPAWN_TIMES); } catch (Exception ex) {}
            try { m.put("lifeMin", panel.tuning.WorldBossTuning.LIFE_MIN); } catch (Exception ex) {}
            try { m.put("zoneSec", panel.tuning.WorldBossTuning.ZONE_SEC); } catch (Exception ex) {}
            try { m.put("startZone", panel.tuning.WorldBossTuning.START_ZONE); } catch (Exception ex) {}
            try { m.put("top1Json", panel.tuning.WorldBossTuning.TOP1_JSON); } catch (Exception ex) {}
            try { m.put("top23Json", panel.tuning.WorldBossTuning.TOP23_JSON); } catch (Exception ex) {}
            try { m.put("top410Json", panel.tuning.WorldBossTuning.TOP410_JSON); } catch (Exception ex) {}
            try { m.put("consolJson", panel.tuning.WorldBossTuning.CONSOL_JSON); } catch (Exception ex) {}
            try { m.put("season", boss.WorldBossTracker.getSeason()); } catch (Exception ex) {}
            m.put("bossId", boss.BossID.WORLD_BOSS);
            // live: boss dang map nao
            String live = "chua goi (offline)";
            try {
                for (Boss b : allBosses()) {
                    if (b != null && b.id == boss.BossID.WORLD_BOSS) {
                        if (b.zone != null && b.zone.map != null) live = b.zone.map.mapName + " [" + b.zone.map.mapId + "] - Khu " + b.zone.zoneId + " | hp=" + (long) b.nPoint.hp + "/" + (long) b.nPoint.hpMax;
                        else live = "da goi nhung dang nghi/doi map";
                        break;
                    }
                }
            } catch (Exception e) {}
            m.put("live", live);
            m.put("luotDanh", boss.WorldBossTracker.size());
            long left = boss.WorldBossTracker.millisLeft();
            m.put("conLai", left < 0 ? "chua bat dau" : (left <= 0 ? "het gio - sap thuong" : (left / 60000) + " phut " + ((left / 1000) % 60) + " giay"));
            m.put("top", boss.WorldBossTracker.topText());
        } catch (Exception e) { m.put("err", e.getMessage()); }
        return m;
    }

    private static String worldBossMapName(int mapId) {
        String n = mapName(mapId);
        return n == null || n.equals("?") ? ("map " + mapId) : n;
    }

    public static String saveWorldBossV2(boolean on, int mapId, int durationMin, double hp, double dame, String top1, String top23, String top410, String consol, double minDame, String spawnTimes, int lifeMin, int zoneSec, int startZone, String top1Json, String top23Json, String top410Json, String consolJson) {
        try {
            panel.tuning.WorldBossTuning.load();
            panel.tuning.WorldBossTuning.ON = on;
            panel.tuning.WorldBossTuning.MAP_ID = mapId;
            panel.tuning.WorldBossTuning.DURATION_MIN = Math.max(1, durationMin);
            if (hp > 0) panel.tuning.WorldBossTuning.HP = hp;
            if (dame >= 0) panel.tuning.WorldBossTuning.DAME = dame;
            panel.tuning.WorldBossTuning.TOP1 = top1 == null ? "" : top1.trim();
            panel.tuning.WorldBossTuning.TOP23 = top23 == null ? "" : top23.trim();
            panel.tuning.WorldBossTuning.TOP410 = top410 == null ? "" : top410.trim();
            panel.tuning.WorldBossTuning.CONSOL = consol == null ? "" : consol.trim();
            if (minDame >= 0) panel.tuning.WorldBossTuning.MIN_DAME = minDame;
            if (spawnTimes != null) panel.tuning.WorldBossTuning.SPAWN_TIMES = spawnTimes.trim();
            if (lifeMin > 0) panel.tuning.WorldBossTuning.LIFE_MIN = lifeMin;
            if (zoneSec >= 5) panel.tuning.WorldBossTuning.ZONE_SEC = zoneSec;
            panel.tuning.WorldBossTuning.START_ZONE = startZone;
            if (top1Json != null) panel.tuning.WorldBossTuning.TOP1_JSON = top1Json.trim();
            if (top23Json != null) panel.tuning.WorldBossTuning.TOP23_JSON = top23Json.trim();
            if (top410Json != null) panel.tuning.WorldBossTuning.TOP410_JSON = top410Json.trim();
            if (consolJson != null) panel.tuning.WorldBossTuning.CONSOL_JSON = consolJson.trim();
            panel.tuning.WorldBossTuning.save();
            return "OK da luu Boss The Gioi V2 (map=" + mapId + " " + worldBossMapName(mapId) + ", life=" + panel.tuning.WorldBossTuning.LIFE_MIN + " phut). Bam 'Goi Boss The Gioi' de ap dung.";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String saveWorldBoss(boolean on, int mapId, int durationMin, double hp, double dame, String top1, String top23, String top410, String consol, double minDame) {
        try {
            panel.tuning.WorldBossTuning.load();
            return saveWorldBossV2(on, mapId, durationMin, hp, dame, top1, top23, top410, consol, minDame, panel.tuning.WorldBossTuning.SPAWN_TIMES, panel.tuning.WorldBossTuning.LIFE_MIN, panel.tuning.WorldBossTuning.ZONE_SEC, panel.tuning.WorldBossTuning.START_ZONE, panel.tuning.WorldBossTuning.TOP1_JSON, panel.tuning.WorldBossTuning.TOP23_JSON, panel.tuning.WorldBossTuning.TOP410_JSON, panel.tuning.WorldBossTuning.CONSOL_JSON);
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String summonWorldBoss() {
        try {
            panel.tuning.WorldBossTuning.load();
            for (Boss b : allBosses()) {
                if (b != null && b.id == boss.BossID.WORLD_BOSS && b.zone != null) {
                    return "Boss The Gioi dang online: " + bossInfo(b) + " (muon doi map: Duoi khoi map roi goi lai)";
                }
            }
            Boss b = boss.BossManager.gI().createBoss(boss.BossID.WORLD_BOSS);
            if (b == null) return "Loi: khong tao duoc Boss The Gioi (kiem tra BossID.WORLD_BOSS)";
            int lifeMin = 60;
            try { lifeMin = Math.max(1, (int) panel.tuning.WorldBossTuning.LIFE_MIN); } catch (Exception ex) {}
            boss.WorldBossTracker.reset();
            boss.WorldBossTracker.start(lifeMin);
            try { services.Service.gI().sendThongBaoAllPlayer("Boss The Gioi da xuat hien tai " + worldBossMapName(panel.tuning.WorldBossTuning.MAP_ID) + "! Hay den tham chien - Top 1 nhan qua dac biet!"); } catch (Exception e) {}
            return "OK da goi Boss The Gioi tai " + worldBossMapName(panel.tuning.WorldBossTuning.MAP_ID) + " (song " + lifeMin + " phut)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** Giet Boss The Gioi NGAY (khong trao thuong): thoat map + xoa khoi BossManager -> khong tu hoi sinh. */
    public static String killWorldBossNow() {
        try {
            int n = 0;
            String viTri = "";
            for (Boss b : allBosses()) {
                if (b == null || b.id != boss.BossID.WORLD_BOSS) continue;
                try {
                    if (b instanceof boss.boss_manifest.WorldBoss.WorldBoss wb) {
                        viTri = wb.adminKill();
                    } else {
                        viTri = bossInfo(b);
                        b.leaveMap();
                    }
                    try { boss.BossManager.gI().removeBoss(b); } catch (Exception ex) {}
                    n++;
                } catch (Exception ex) {}
            }
            if (n == 0) return "Boss The Gioi khong co tren ban do - khong can giet.";
            return "OK da giet Boss The Gioi tai " + viTri + " - boss KHONG tu hoi sinh.\n"
                    + "Goi lai: bam 'Goi Boss The Gioi' hoac cho den gio SPAWN_TIMES.";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String rewardWorldBossNow() {
        try {
            String log = boss.WorldBossTracker.rewardAll();
            // Phat thuong xong -> giet boss ngay, boss khong dung tren ban do va khong hoi sinh.
            String giet = killWorldBossNow();
            try { boss.WorldBossTracker.reset(); } catch (Exception e) {}
            return "OK da trao thuong dot nay:\n" + log + "\n" + giet;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String resetWorldBossTop() {
        try {
            boss.WorldBossTracker.reset();
            return "OK da xoa bang Top dame (bat dau dot moi). Boss van online.";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ EVENT ============
    public static Map<String, Boolean> eventStates() {
        Map<String, Boolean> m = new LinkedHashMap<>();
        try {
            Class<?> c = Class.forName("event.EventManager");
            for (String f : new String[]{"LUNNAR_NEW_YEAR","INTERNATIONAL_WOMANS_DAY","CHRISTMAS","HALLOWEEN","HUNG_VUONG","TRUNG_THU","TOP_UP","GIAI_CUU_NHAN_GIOI","DAI_CHIEN_THAN_THU"}) {
                try { m.put(f, c.getField(f).getBoolean(null)); } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return m;
    }

    // ============ CATALOG: BOSS (full chi so BossesData + goi y dame ao) ============
    public static List<Map<String, Object>> listBossCatalog() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            java.lang.reflect.Field[] fs = BossesData.class.getDeclaredFields();
            for (java.lang.reflect.Field f : fs) {
                if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                if (!f.getType().getSimpleName().equals("BossData")) continue;
                try {
                    f.setAccessible(true);
                    BossData d = (BossData) f.get(null);
                    if (d == null) continue;
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("field", f.getName());
                    m.put("name", d.getName());
                    m.put("dame", d.getDame());
                    double[] hp = d.getHp();
                    long hpMax = 0;
                    if (hp != null) for (double h : hp) hpMax = Math.max(hpMax, (long) Math.min(h, 9.0e18));
                    m.put("hpMax", hpMax);
                    m.put("hpLevels", hp == null ? 0 : hp.length);
                    int[] maps = d.getMapJoin();
                    StringBuilder ms = new StringBuilder();
                    if (maps != null) for (int i = 0; i < maps.length; i++) {
                        if (i > 0) ms.append(",");
                        ms.append(maps[i]).append(":").append(mapName(maps[i]));
                    }
                    m.put("maps", ms.toString());
                    m.put("rest", d.getSecondsRest());
                    int[][] sk = d.getSkillTemp();
                    m.put("skills", sk == null ? 0 : sk.length);
                    m.put("bossId", resolveBossId(f.getName()));
                    m.put("goiY", goiYDameAo(d.getDame(), hpMax));
                    out.add(m);
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        out.sort((a, b) -> String.valueOf(a.get("field")).compareTo(String.valueOf(b.get("field"))));
        return out;
    }

    private static String resolveBossId(String field) {
        try {
            java.lang.reflect.Field f = BossID.class.getDeclaredField(field);
            return String.valueOf(f.getInt(null));
        } catch (Exception e) {}
        // thu ten gan dung: bo dau gach duoi
        try {
            String compact = field.replace("_", "");
            for (java.lang.reflect.Field f : BossID.class.getDeclaredFields()) {
                if (f.getName().replace("_", "").equalsIgnoreCase(compact)) return String.valueOf(f.getInt(null));
            }
        } catch (Exception e) {}
        return "N/A (random/clone)";
    }

    private static String mapName(int mapId) {
        try {
            if (server.Manager.MAPS != null) {
                for (map.Map mp : server.Manager.MAPS) {
                    if (mp != null && mp.mapId == mapId) return mp.mapName;
                }
            }
        } catch (Exception e) {}
        try {
            if (server.Manager.MAP_TEMPLATES != null) {
                for (models.Template.MapTemplate t : server.Manager.MAP_TEMPLATES) {
                    if (t != null && t.id == mapId) return t.name;
                }
            }
        } catch (Exception e) {}
        return "?";
    }

    // Goi y dame ao: server muon x10-x30 ty max nhung phai leo tu tu
    private static String goiYDameAo(double dame, long hp) {
        if (dame < 100000) return "Tan thu: giu <100k, HP boss <1 ty";
        if (dame < 1000000) return "So cap: 100k-1tr, HP 1-10 ty";
        if (dame < 10000000) return "Trung cap: 1-10tr, HP 10-100 ty";
        if (dame < 100000000) return "Cao cap: 10-100tr, HP 100-500 ty";
        if (dame < 1000000000) return "Sie cap: 0.1-1 ty, HP 0.5-2k ty";
        if (dame < 10000000000L) return "Endgame: 1-10 ty, HP 2k-10k ty";
        return "Dame ao max: 10-30 ty (chi 2-3 boss cuoi, HP 10k-30k ty)";
    }

    // ============ CATALOG: EVENTS (trang thai + boss + npc + cach tinh diem + do roi) ============
    public static List<Map<String, Object>> listEventCatalog() {
        List<Map<String, Object>> out = new ArrayList<>();
        Map<String, Boolean> st = eventStates();
        out.add(evRow("HALLOWEEN", st, "BIMA, MATROI (MATROI), DOI x10 moi loai", "Khong NPC rieng",
            "Farm: Hat sen(1711)/Dau xanh?/Bot nep? tu mob thuong; doi Banh 1706-1708", "Diem: player.diemfam (xem EventTet/EventTrungThu)", "818 Capsule Halloween, 899-902 keo/banh, cai trang Noel 824-827"));
        out.add(evRow("HUNG_VUONG", st, "THUY_TINH(-355) x10", "Khong NPC rieng",
            "Farm mob ra nguyen lieu Hung Vuong; nau banh nhu EventTet", "Diem: diemfam + top diemfam", "753 Banh chung, 752 Banh tet, 748-751 thit/nep/la dong"));
        out.add(evRow("LUNNAR_NEW_YEAR (Tet)", st, "LAN_CON(-371) x10", "NPC map 0 id 49 (LunarNewYear)",
            "Farm: 748 Thit heo, 749 Thung nep, 750 Thung dau xanh, 751 La dong, 1338-1341 bot/dau/mo lua/hat sen; nau Banh chung 753 / Banh tet 752 (99 combo + 2 ty vang)", "Diem: player.diemfam, menu Top Tet", "753/752 banh, 1335/1337 banh trung thu doi diem"));
        out.add(evRow("TRUNG_THU", st, "KHIDOT(-344) x10, NGUYETTHAN(-345) x10", "EventTrungThu NPC",
            "Farm mob ra 1338-1341; lam Banh dau xanh 80+bot nep 99+mo lua 80 / Banh hat sen 99+...; Banh thap cam 1337", "Diem: diemfam + doi qua Trung Thu", "465/466/472/473 banh trung thu, 737/1512 capsule/hop, 1706-1708 banh, 1711 hat sen"));
        out.add(evRow("CHRISTMAS (Noel)", st, "ONG_GIA_NOEL(-353) x30", "Khong NPC rieng",
            "Farm mob ra 533 Keo giang sinh; doi cai trang Noel", "Diem: diemfam su kien Noel", "386-394 Nen Noel, 533 Keo, 824-827/922-924/937 cai trang Noel"));
        out.add(evRow("GIAI_CUU_NHAN_GIOI (NhanGia)", st, "CUU_VI(-6000) x3", "NPC map 20 id 78",
            "Danh Cuu Vi + Obito(-6001)/Obito Luc Dao(-6002)", "Diem: diem su kien nhan gioi", "Do roi boss NhanGia (xem reward class)"));
        out.add(evRow("DAI_CHIEN_THAN_THU", st, "THIET_MA, HAI_KHUYEN, BACH_LANG, VUA_BACH_THU", "NPC map 5 id 71",
            "Danh 4 than thu theo thu tu", "Diem: diem than thu", "Do roi than thu (xem reward class)"));
        out.add(evRow("INTERNATIONAL_WOMANS_DAY (8/3)", st, "Khong boss (EventDAO.loadInternationalWomensDayEvent)", "Khong NPC rieng",
            "Tang hoa/qua 8-3 qua EventDAO", "Diem: event 8-3 trong DB event", "Item 8-3 (xem shop/DAO)"));
        out.add(evRow("TOP_UP (Dua top nap)", st, "Khong boss", "Khong NPC",
            "Nap the -> VND/tongnap/VIP (xem DONATE)", "Diem: tongnap account", "Qua dua top nap (giftcode/shop)"));
        return out;
    }

    private static Map<String, Object> evRow(String key, Map<String, Boolean> st, String boss, String npc, String farm, String diem, String items) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("key", key);
        m.put("on", st.getOrDefault(key, false));
        m.put("boss", boss);
        m.put("npc", npc);
        m.put("farm", farm);
        m.put("diem", diem);
        m.put("items", items);
        return m;
    }

    // ============ CATALOG: CONG THUC (ty le, dame, crit, TNSM, giam tru) ============
    public static List<Map<String, Object>> listFormulaCatalog() {
        List<Map<String, Object>> out = new ArrayList<>();
        out.add(fRow("Dame co ban", "NPoint.setDame()", "dame = dameg + dameAdd + % cong don (tlDame/set/skill/pet/hop the); calPercent(p,%) = p*%/100", "src/player/NPoint.java:1453-1560"));
        out.add(fRow("Dame ao (server)", "Goi y tien trinh", "Tan thu <100k -> so cap 1tr -> trung 10tr -> cao 100tr -> sieu 1 ty -> max 10-30 ty (chi boss cuoi). HP boss gap 100-1000 lan dame. Leo bang % tlDame theo moc power, khong tang dameg dot ngot.", "Panel cot Goi y + BossesData"));
        out.add(fRow("Chi mang (crit)", "NPoint.setCrit() + setIsCrit()", "crit = critg + critAdd; Khii: crit=110; Detu: setmabu5 -> crit=25; roll: Util.isTrue(crit,100)", "src/player/NPoint.java:1845-,1974-1981"));
        out.add(fRow("Sat thuong chi mang", "NPoint tlSDCM", "Banh Trung Thu/Hoa15: tlSDCM += tlSDCM*100/100 (x2); dameCrit = dame * (100+tlSDCM)/100", "src/player/NPoint.java:setCrit"));
        out.add(fRow("Tru phong thu", "NPoint.subDameInjureWithDeff()", "dameNhan = dame - def; neu <0 thi =1 (dame toi thieu 1)", "src/player/NPoint.java:2365"));
        out.add(fRow("Ne don / Giam ST", "NPoint injured()", "roll tlNeDon/1000 -> ne; tlSubSD/tlGiap/tlGiamst tru % dame nhan", "src/player/NPoint.java + src/boss/Boss.java:injured"));
        out.add(fRow("HP/MP max", "NPoint calPercent", "hpMax/mpMax cong don % tu tlHp/tlMp/do/set; addHp/addMp gioi han tran hpMax/mpMax", "src/player/NPoint.java:858,1025,1449"));
        out.add(fRow("TNSM cong", "NPoint tiemNangUp/powerUp", "tiemNang += tn * heSo (x1/x2/x3/x4 theo nv/noi tai); bonus +% intrinsic.param1; pet huong tlTNSMPet; power += power (check TaskPower)", "src/player/NPoint.java:2194-2313,2401"));
        out.add(fRow("Diem su kien", "EventSuKien/EventTet", "player.diemfam; nau banh 99-combo + 2 ty vang -> banh 753/752/1335/1337 (x9999); top theo diemfam", "src/EventSuKien/EventTet.java:44-240"));
        out.add(fRow("Reward boss", "Boss.die()/reward()", "Base Boss.reward chi check TaskKillBoss; do roi nam o class con override reward() (vd Broly, Doraemon...). Boss khong override = KHONG roi do.", "src/boss/Boss.java:626-641"));
        out.add(fRow("Respawn boss", "Boss.leaveMap()/REST", "Het level -> REST secondsRest (BossesData REST_x, hien =10); Event boss spawn so luong theo createBoss(id, n)", "src/boss/Boss.java + event_manifest"));
        return out;
    }

    private static Map<String, Object> fRow(String ten, String ham, String congThuc, String file) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ten", ten);
        m.put("ham", ham);
        m.put("congThuc", congThuc);
        m.put("file", file);
        return m;
    }

    public static String setEvent(String field, boolean on) {
        try {
            Class<?> c = Class.forName("event.EventManager");
            c.getField(field).setBoolean(null, on);
            return "OK " + field + "=" + on + " (can restart server de load lai event init)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String reloadTamBao() { try { services.TamBao.gI().reload(); return "OK da reload Vong Quay (Tam Bao) - " + tamBaoInfo(); } catch (Exception e) { return "Loi: " + e.getMessage(); } }

    public static String tamBaoInfo() {
        try {
            services.TamBao tamBao = services.TamBao.gI();
            return "Chia=" + tamBao.getCurrentKeyId() + " | vat pham=" + tamBao.getItemCount()
                    + " | pool=" + tamBao.getPoolCount() + " | moc=" + tamBao.getMocCount()
                    + " | cache=" + tamBao.getCachedViewCount();
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    // ============ TAMBAO (VONG QUAY) - CRUD CHO PANEL ============

    /** Danh sach vat pham vong quay (tambao_items) kem ten vat pham + ten chia. */
    public static List<Map<String, Object>> listTamBaoItems() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT i.id, i.key_item_id, i.item_id, i.quantity, i.item_options, i.tile_trung_thuong, i.des, i.start_at, i.end_at, i.enabled,"
                     + " t.NAME AS item_name, k.NAME AS key_name FROM tambao_items i"
                     + " LEFT JOIN item_template t ON t.id = i.item_id"
                     + " LEFT JOIN item_template k ON k.id = i.key_item_id ORDER BY i.key_item_id, i.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("key_item_id", rs.getInt("key_item_id"));
                m.put("item_id", rs.getInt("item_id"));
                m.put("quantity", rs.getInt("quantity"));
                m.put("item_options", rs.getString("item_options"));
                m.put("rate", rs.getDouble("tile_trung_thuong"));
                m.put("des", rs.getString("des"));
                m.put("start_at", rs.getString("start_at"));
                m.put("end_at", rs.getString("end_at"));
                m.put("enabled", rs.getInt("enabled"));
                m.put("item_name", rs.getString("item_name"));
                m.put("key_name", rs.getString("key_name"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Danh sach moc thuong (moc_vong_quay) kem ten vat pham. */
    public static List<Map<String, Object>> listTamBaoMocs() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT m.id, m.item_id, m.quantity, m.max_value, m.item_options, t.NAME AS item_name"
                     + " FROM moc_vong_quay m LEFT JOIN item_template t ON t.id = m.item_id ORDER BY m.max_value, m.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("item_id", rs.getInt("item_id"));
                m.put("quantity", rs.getInt("quantity"));
                m.put("max_value", rs.getInt("max_value"));
                m.put("item_options", rs.getString("item_options"));
                m.put("item_name", rs.getString("item_name"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Log quay gan nhat (history_tambao), join ten nhan vat. */
    public static List<Map<String, Object>> listTamBaoHistory(int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT h.id, h.id_player, p.name, h.item, h.created_at FROM history_tambao h"
                     + " LEFT JOIN player p ON p.id = h.id_player ORDER BY h.id DESC LIMIT ?")) {
            ps.setInt(1, Math.max(1, Math.min(500, limit)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("id_player", rs.getLong("id_player"));
                    m.put("name", rs.getString("name"));
                    m.put("item", rs.getString("item"));
                    m.put("created_at", rs.getString("created_at"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Kiem tra item_template co ton tai - dung chung cho cac form TamBao. */
    private static String checkTamBaoItem(int itemId) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT NAME FROM item_template WHERE id = ?")) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "Loi: item_template id=" + itemId + " khong ton tai";
            }
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return null;
    }

    /** Them 1 dong vat pham vong quay. Option compact vd "30-1,77-50". Sau do tu reload TamBao. */
    public static String addTamBaoItem(int keyId, int itemId, int qty, String options, double rate, String des, String startAt, String endAt, boolean enabled) {
        if (qty < 1) return "Loi: SL phai >= 1";
        if (rate < 0 || rate > 100) return "Loi: Ty le phai trong khoang 0..100";
        String bad = checkTamBaoItem(itemId); if (bad != null) return bad;
        bad = checkTamBaoItem(keyId); if (bad != null) return bad.replace("item_template", "item_template (chia)");
        final String sql = "INSERT INTO tambao_items(key_item_id,item_id,quantity,item_options,tile_trung_thuong,des,start_at,end_at,enabled) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, keyId); ps.setInt(2, itemId); ps.setInt(3, qty);
            ps.setString(4, options == null ? "" : options.trim());
            ps.setDouble(5, rate); ps.setString(6, des);
            ps.setString(7, emptyToNull(startAt)); ps.setString(8, emptyToNull(endAt));
            ps.setInt(9, enabled ? 1 : 0);
            ps.executeUpdate();
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    /** Sua 1 dong vat pham vong quay theo id. */
    public static String updateTamBaoItem(int id, int keyId, int itemId, int qty, String options, double rate, String des, String startAt, String endAt, boolean enabled) {
        if (qty < 1) return "Loi: SL phai >= 1";
        if (rate < 0 || rate > 100) return "Loi: Ty le phai trong khoang 0..100";
        String bad = checkTamBaoItem(itemId); if (bad != null) return bad;
        final String sql = "UPDATE tambao_items SET key_item_id=?, item_id=?, quantity=?, item_options=?, tile_trung_thuong=?, des=?, start_at=?, end_at=?, enabled=? WHERE id=?";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, keyId); ps.setInt(2, itemId); ps.setInt(3, qty);
            ps.setString(4, options == null ? "" : options.trim());
            ps.setDouble(5, rate); ps.setString(6, des);
            ps.setString(7, emptyToNull(startAt)); ps.setString(8, emptyToNull(endAt));
            ps.setInt(9, enabled ? 1 : 0); ps.setInt(10, id);
            if (ps.executeUpdate() == 0) return "Loi: khong tim thay id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    public static String deleteTamBaoItem(int id) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("DELETE FROM tambao_items WHERE id=?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) return "Loi: khong tim thay id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    /** Them moc thuong. Option JSON vd [{"id":30,"param":1}]. */
    public static String addTamBaoMoc(int itemId, int qty, int maxValue, String options) {
        if (qty < 1) return "Loi: SL phai >= 1";
        if (maxValue < 0) return "Loi: Diem quay can phai >= 0";
        String bad = checkTamBaoItem(itemId); if (bad != null) return bad;
        String json = validateMocOptions(options);
        if (json != null && json.startsWith("Loi")) return json;
        final String sql = "INSERT INTO moc_vong_quay(item_id,quantity,max_value,item_options) VALUES(?,?,?,?)";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId); ps.setInt(2, qty); ps.setInt(3, maxValue); ps.setString(4, json);
            ps.executeUpdate();
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    public static String updateTamBaoMoc(int id, int itemId, int qty, int maxValue, String options) {
        if (qty < 1) return "Loi: SL phai >= 1";
        if (maxValue < 0) return "Loi: Diem quay can phai >= 0";
        String bad = checkTamBaoItem(itemId); if (bad != null) return bad;
        String json = validateMocOptions(options);
        if (json != null && json.startsWith("Loi")) return json;
        final String sql = "UPDATE moc_vong_quay SET item_id=?, quantity=?, max_value=?, item_options=? WHERE id=?";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId); ps.setInt(2, qty); ps.setInt(3, maxValue); ps.setString(4, json); ps.setInt(5, id);
            if (ps.executeUpdate() == 0) return "Loi: khong tim thay id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    public static String deleteTamBaoMoc(int id) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("DELETE FROM moc_vong_quay WHERE id=?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) return "Loi: khong tim thay id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return reloadTamBao();
    }

    /** Option moc: rong -> []; hop le phai parse duoc la JSON array; tra ve JSON string da chuan hoa. */
    private static String validateMocOptions(String options) {
        String s = options == null ? "" : options.trim();
        if (s.isEmpty()) return "[]";
        try {
            Object o = org.json.simple.JSONValue.parse(s);
            if (o instanceof org.json.simple.JSONArray) return ((org.json.simple.JSONArray) o).toJSONString();
        } catch (Exception e) { /* thong bao duoi */ }
        return "Loi: Option phai la JSON array vd [{\"id\":30,\"param\":1}]";
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    // ============ GIFTCODE ============
    public static String reloadGiftcode() {
        try { models.GiftCode.GiftCodeService.gI().updateGiftCode(); return "OK (" + models.GiftCode.GiftCodeManager.gI().listGiftCode.size() + " codes)"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String createGiftcode(String code, int count, int expiredDays, String detailJson) {
        try {
            Timestamp exp = new Timestamp(System.currentTimeMillis() + (long) expiredDays * 86400000L);
            jdbc.DBConnecter.executeUpdate("insert into giftcode(code,count_left,detail,expired) values(?,?,?,?)", code, count, detailJson, exp);
            models.GiftCode.GiftCodeService.gI().updateGiftCode();
            audit("createGiftcode", code + " count=" + count + " hetHan=" + expiredDays + "ngay");
            return "OK da tao " + code;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteGiftcode(int id) {
        try { jdbc.DBConnecter.executeUpdate("delete from giftcode where id = ?", id); models.GiftCode.GiftCodeService.gI().updateGiftCode(); audit("deleteGiftcode", "id=" + id); return "OK"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String addGiftCount(int id, int delta) {
        try { jdbc.DBConnecter.executeUpdate("update giftcode set count_left = count_left + ? where id = ?", delta, id); models.GiftCode.GiftCodeService.gI().updateGiftCode(); audit("addGiftCount", "id=" + id + " +" + delta); return "OK"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ GIFTCODE INLINE (sua truc tiep tai o, khong go JSON) ============
    public static List<Map<String, Object>> listGiftcodeFull() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT id,code,count_left,expired,detail FROM giftcode ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                int id = rs.getInt("id");
                String detail = rs.getString("detail");
                List<GiftItemModel> items = parseGiftDetail(detail);
                enrichGiftNames(items);
                m.put("id", id);
                m.put("code", rs.getString("code"));
                m.put("count", rs.getInt("count_left"));
                m.put("expired", rs.getTimestamp("expired"));
                m.put("expired_str", String.valueOf(rs.getTimestamp("expired")));
                m.put("n_item", items.size());
                m.put("summary", giftSummaryVN(items));
                m.put("detail", detail);
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static List<GiftItemModel> parseGiftDetail(String json) {
        List<GiftItemModel> out = new ArrayList<>();
        try {
            Object o = org.json.simple.JSONValue.parse(json);
            if (o instanceof org.json.simple.JSONArray) {
                for (Object oo : (org.json.simple.JSONArray) o) {
                    if (oo instanceof org.json.simple.JSONObject) out.add(GiftItemModel.fromJson((org.json.simple.JSONObject) oo));
                }
            }
        } catch (Exception e) {}
        enrichGiftNames(out);
        return out;
    }

    public static void enrichGiftNames(List<GiftItemModel> list) {
        try {
            java.util.Map<Integer, String> map = new java.util.HashMap<>();
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                if (t != null) map.put((int) t.id, t.name);
            }
            for (GiftItemModel m : list) {
                String sp = GiftItemModel.specialName(m.tempId);
                if (sp != null) { m.itemName = sp; continue; }
                String n = map.get(m.tempId);
                if (n != null) m.itemName = n;
                else if (m.itemName == null) m.itemName = "";
            }
        } catch (Exception e) {}
    }

    public static String buildGiftDetailJson(List<GiftItemModel> list) {
        org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
        for (GiftItemModel m : list) arr.add(m.toJson());
        return arr.toJSONString();
    }

    public static List<String> validateGiftItems(List<GiftItemModel> list) {
        List<String> errs = new ArrayList<>();
        java.util.Set<Integer> validTemp = new java.util.HashSet<>();
        java.util.Set<Integer> validOpt = new java.util.HashSet<>();
        try {
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) if (t != null) validTemp.add((int) t.id);
            for (models.Template.ItemOptionTemplate t : server.Manager.ITEM_OPTION_TEMPLATES) validOpt.add(t.id);
        } catch (Exception e) {}
        for (int i = 0; i < list.size(); i++) {
            GiftItemModel m = list.get(i);
            String prefix = "Dong " + (i + 1) + " (temp " + m.tempId + "): ";
            if (m.tempId != -1 && m.tempId != -2 && m.tempId != -3 && !validTemp.contains(m.tempId)) errs.add(prefix + "temp_id khong ton tai (chon lai tu danh sach, hoac -1=Vang -2=Ngoc -3=Ngoc khoa)");
            if (m.quantity <= 0) errs.add(prefix + "so luong phai > 0");
            for (GiftItemModel.Opt op : m.options) {
                if (!validOpt.contains(op.id)) { errs.add(prefix + "option id " + op.id + " khong ton tai"); continue; }
                if (op.param < 0) errs.add(prefix + "option " + op.id + " param am");
            }
        }
        return errs;
    }

    public static String giftItemSummaryVN(GiftItemModel m) {
        StringBuilder sb = new StringBuilder();
        String nm = m.itemName == null || m.itemName.isEmpty() ? ("#" + m.tempId) : m.itemName;
        sb.append(nm).append(" x").append(m.quantity);
        if (!m.options.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < m.options.size(); i++) {
                if (i > 0) sb.append(", ");
                GiftItemModel.Opt op = m.options.get(i);
                sb.append(shortOptionNamePublic(op.id)).append(" +").append(op.param);
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public static String giftSummaryVN(List<GiftItemModel> list) {
        if (list.isEmpty()) return "(rong)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append("; ");
            sb.append(giftItemSummaryVN(list.get(i)));
        }
        String s = sb.toString();
        return s.length() > 160 ? s.substring(0, 160) + "..." : s;
    }

    public static String shortOptionNamePublic(int id) {
        return shortOptionName(id);
    }

    public static String updateGiftcodeFull(int id, String code, int countLeft, String expiredStr, List<GiftItemModel> items) {
        List<String> errs = validateGiftItems(items);
        if (!errs.isEmpty()) {
            StringBuilder sb = new StringBuilder("Chua luu, con loi:\n");
            for (String e : errs) sb.append("- ").append(e).append("\n");
            return sb.toString();
        }
        try {
            String json = buildGiftDetailJson(items);
            if (expiredStr != null && !expiredStr.trim().isEmpty()) {
                try {
                    Timestamp ts = Timestamp.valueOf(expiredStr.trim());
                    jdbc.DBConnecter.executeUpdate("update giftcode set code=?, count_left=?, expired=?, detail=? where id=?", code, countLeft, ts, json, id);
                } catch (Exception ex) {
                    jdbc.DBConnecter.executeUpdate("update giftcode set code=?, count_left=?, detail=? where id=?", code, countLeft, json, id);
                }
            } else {
                jdbc.DBConnecter.executeUpdate("update giftcode set code=?, count_left=?, detail=? where id=?", code, countLeft, json, id);
            }
            models.GiftCode.GiftCodeService.gI().updateGiftCode();
            return "OK da luu code " + code + " (" + items.size() + " mon)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String createGiftcodeFromModels(String code, int count, int expiredDays, List<GiftItemModel> items) {
        List<String> errs = validateGiftItems(items);
        if (!errs.isEmpty()) {
            StringBuilder sb = new StringBuilder("Chua tao, con loi:\n");
            for (String e : errs) sb.append("- ").append(e).append("\n");
            return sb.toString();
        }
        return createGiftcode(code, count, expiredDays, buildGiftDetailJson(items));
    }

    // ============ SHOP ============
    public static String reloadShop() {
        try { server.Manager.gI().updateShop(); return "OK (" + server.Manager.SHOPS.size() + " shops)"; }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static List<Map<String, Object>> listShopTabs() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("SELECT t.id,t.shop_id,s.tag_name,s.type_shop,n.NAME AS npc_name,t.tab_name,t.tab_index,CHAR_LENGTH(t.items) AS len FROM tab_shop t LEFT JOIN shop s ON s.id=t.shop_id LEFT JOIN npc_template n ON n.id=s.npc_id ORDER BY t.shop_id,t.tab_index");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("tab_id", rs.getInt("id"));
                m.put("shop_id", rs.getInt("shop_id"));
                m.put("shop", rs.getString("tag_name"));
                m.put("type_shop", rs.getInt("type_shop"));
                m.put("type_vn", shopTypeName(rs.getInt("type_shop")));
                m.put("npc", rs.getString("npc_name"));
                m.put("tab", rs.getString("tab_name").replace("<>", "/"));
                m.put("idx", rs.getInt("tab_index"));
                m.put("len", rs.getInt("len"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ============ SHOP: NPC -> SHOP map (tra tu code + DB) ============
    public static String shopTypeName(int t) {
        if (t == 0) return "Thuong (Vang/Ngoc/Ruby/Coupon)";
        if (t == 3) return "Dac biet (doi do - Cua Hang dac biet)";
        return "Loai " + t;
    }

    public static List<Map<String, Object>> listNpcShops() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT s.id AS shop_id,s.npc_id,n.NAME AS npc_name,s.tag_name,s.type_shop,COUNT(t.id) AS tabs FROM shop s LEFT JOIN npc_template n ON n.id=s.npc_id LEFT JOIN tab_shop t ON t.shop_id=s.id GROUP BY s.id,s.npc_id,n.NAME,s.tag_name,s.type_shop ORDER BY s.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("shop_id", rs.getInt("shop_id"));
                m.put("npc_id", rs.getInt("npc_id"));
                m.put("npc", rs.getString("npc_name"));
                m.put("shop", rs.getString("tag_name"));
                m.put("type_shop", rs.getInt("type_shop"));
                m.put("type_vn", shopTypeName(rs.getInt("type_shop")));
                m.put("tabs", rs.getInt("tabs"));
                m.put("status", rs.getInt("tabs") == 0 ? "TAT (0 tab)" : "BAT");
                m.put("opener", shopOpenerInfo(rs.getString("tag_name")));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String shopStatsLine() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS n, SUM(CASE WHEN type_shop=0 THEN 1 ELSE 0 END) AS n0, SUM(CASE WHEN type_shop=3 THEN 1 ELSE 0 END) AS n3 FROM shop");
             ResultSet rs = ps.executeQuery()) {
            int n = 0, n0 = 0, n3 = 0;
            if (rs.next()) { n = rs.getInt(1); n0 = rs.getInt(2); n3 = rs.getInt(3); }
            int off = 0;
            try (PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) FROM shop s WHERE NOT EXISTS (SELECT 1 FROM tab_shop t WHERE t.shop_id=s.id)")) {
                try (ResultSet rs2 = ps2.executeQuery()) { if (rs2.next()) off = rs2.getInt(1); }
            }
            return "Tong " + n + " shop (Thuong=" + n0 + ", Dac biet=" + n3 + ", Tat=" + off + " do 0 tab). Tat = SANTA_PHUKIEN, SANTA_HAN_SU_DUNG, SANTA_DANH_HIEU.";
        } catch (Exception e) { return ""; }
    }

    public static String shopOpenerInfo(String tag) {
        if (tag == null) return "";
        switch (tag) {
            case "BUNMA": return "Bulma.java (lang) + Osin?/Bulma NPC 7";
            case "DENDE": return "Dende.java (NPC 8)";
            case "APPULE": return "Appule.java (NPC 9)";
            case "URON": return "Uron.java (NPC 16)";
            case "BILL": return "Bill.java (NPC 55, map 48)";
            case "BUNMA_FUTURE": return "BulmaTuongLai.java (NPC 37)";
            case "BUA_1H": case "BUA_8H": case "BUA_1M": return "BaHatMit.java (NPC 21)";
            case "SANTA": case "SANTA_HEAD": case "SANTA_MO_RONG_HANH_TRANG": case "SANTA_HAN_SU_DUNG": case "SANTA_DANH_HIEU": case "SANTA_PHUKIEN": return "Santa.java (NPC 39)";
            case "CUAHANG": return "Osin.java map 154 'Cua hang' = Cua Hang dac biet trong anh (NPC 39 Santa goc, hien Osin giu)";
            case "NGOKHONGSHOP": return "NGO_KHONG.java (NPC 48)";
            case "CUAHANGHOTONG": return "HoTong.java (NPC 89 Duong Tang)";
            case "SUKIEN": return "GohanDaiChien.java";
            case "Trungthu": return "EventTrungThu.java (NPC 92)";
            case "FREE": case "FREE2": case "VIP": case "CAN_CAU": case "DOI_CA": return "EventCauCa.java + Nang Dan AFK (NPC 95)";
            case "NAP": return "Nang Dan AFK (NPC 95)";
            default: return "ShopService.opendShop '" + tag + "'";
        }
    }

    public static String specItemName(int tempId) {
        if (tempId <= 0) return "Mac dinh (theo type_sell)";
        try {
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                if (t != null && (int) t.id == tempId) return t.name + " (#" + tempId + ")";
            }
        } catch (Exception e) {}
        // preset quen thuoc
        if (tempId == 1270) return "Luong Vang (#1270) - tien Cua Hang dac biet";
        if (tempId == 457) return "Thoi Vang (#457)";
        if (tempId == 861) return "Hong ngoc (#861)";
        if (tempId == 1027) return "Xu Tan Thu (#1027)";
        if (tempId == 542) return "Qua Hong Dao Chin (#542)";
        return "Item #" + tempId;
    }

    public static String getTabItems(int tabId) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("SELECT items FROM tab_shop WHERE id=?")) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("items");
            }
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
        return "[]";
    }

    public static String setTabItems(int tabId, String itemsJson) {
        try {
            // validate JSON
            Object o = org.json.simple.JSONValue.parse(itemsJson);
            if (o == null) return "JSON khong hop le";
            jdbc.DBConnecter.executeUpdate("update tab_shop set items = ? where id = ?", itemsJson, tabId);
            server.Manager.gI().updateShop();
            return "OK da luu + reload shop";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ CONSIGN (don rac) ============
    public static String clearConsign() {
        try {
            models.Consign.ConsignShopManager.gI().listItem.clear();
            models.Consign.ConsignShopManager.gI().save();
            jdbc.DBConnecter.executeUpdate("TRUNCATE shop_ky_gui");
            return "OK da xoa sach ky gui";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteConsign(int id) {
        try {
            models.Consign.ConsignShopManager.gI().listItem.removeIf(it -> it != null && it.id == id);
            models.Consign.ConsignShopManager.gI().save();
            jdbc.DBConnecter.executeUpdate("delete from shop_ky_gui where id = ?", id);
            return "OK";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ SERVER CONFIG ============
    public static String setMaxPlayer(int v) { try { server.Manager.MAX_PLAYER = v; return "OK"; } catch (Exception e) { return "Loi: " + e.getMessage(); } }
    public static String setMaxPerIp(int v) { try { server.Manager.MAX_PER_IP = v; return "OK"; } catch (Exception e) { return "Loi: " + e.getMessage(); } }

    // ============ SHOP EDITOR: tien te / loai ============
    public static String typeSellName(byte t) {
        if (t == 0) return "Vang";
        if (t == 1) return "Ngoc";
        if (t == 3) return "Ruby";
        if (t == 4) return "Coupon/DiemSK";
        return "Loại " + t;
    }
    public static byte parseTypeSell(String s) {
        if (s == null) return 0;
        s = s.trim().toLowerCase();
        if (s.startsWith("1") || s.startsWith("ngoc")) return 1;
        if (s.startsWith("3") || s.startsWith("ruby")) return 3;
        if (s.startsWith("4") || s.startsWith("coupon") || s.startsWith("diem")) return 4;
        return 0;
    }
    public static String itemTypeNameVN(byte type) {
        switch (type) {
            case 0: return "Ao";
            case 1: return "Quan";
            case 2: return "Gang";
            case 3: return "Giay";
            case 4: return "Rada";
            case 5: return "Cai trang/Phu kien";
            case 6: return "Ngoc Rong";
            case 7: return "Sach/Skill";
            case 8: return "Vat pham";
            case 9: return "Vang/Ngoc item";
            case 11: return "Bua";
            case 12: return "Sao/Phao";
            case 13: return "Thuc an";
            case 14: return "Pet/De tu";
            case 21: return "Linh tinh";
            case 23: return "Thu cuoi/Mount";
            case 27: return "Su kien";
            case 28: return "Nang cap";
            case 32: return "Trang bi dac biet";
            default: return "Loai " + type;
        }
    }
    public static String genderName(byte g) {
        if (g == 0) return "Trai Dat";
        if (g == 1) return "Namek";
        if (g == 2) return "Xayda";
        return "Chung";
    }

    // ============ SHOP EDITOR: bang phu ============
    public static void ensureHelperTables() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps1 = con.prepareStatement("CREATE TABLE IF NOT EXISTS panel_option_dict (option_id INT PRIMARY KEY, ten_viet VARCHAR(255) NOT NULL DEFAULT '', goi_y_param VARCHAR(255) NOT NULL DEFAULT '', cach_dung VARCHAR(512) NOT NULL DEFAULT '', min_param BIGINT NOT NULL DEFAULT 0, max_param BIGINT NOT NULL DEFAULT 999999999, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
             PreparedStatement ps2 = con.prepareStatement("CREATE TABLE IF NOT EXISTS panel_history (id INT AUTO_INCREMENT PRIMARY KEY, tab_id INT NOT NULL DEFAULT 0, actor VARCHAR(64) NOT NULL DEFAULT 'admin', action VARCHAR(32) NOT NULL DEFAULT 'save', summary TEXT, items_backup MEDIUMTEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")) {
            try { ps1.execute(); } catch (Exception e) {}
            try { ps2.execute(); } catch (Exception e) {}
            // them cot cach_dung cho bang da tao truoc (CREATE IF NOT EXISTS khong add cot moi)
            // - da co cot thi loi 1060 duplicate column -> bo qua
            try (PreparedStatement ps3 = con.prepareStatement("ALTER TABLE panel_option_dict ADD COLUMN cach_dung VARCHAR(512) NOT NULL DEFAULT ''")) {
                try { ps3.execute(); } catch (Exception e) {}
            } catch (Exception e) {}
        } catch (Exception e) {}
    }

    public static List<Map<String, Object>> listOptionDict() {
        ensureHelperTables();
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT o.id AS oid, o.name AS oname, d.ten_viet, d.goi_y_param, d.cach_dung, d.min_param, d.max_param FROM item_option_template o LEFT JOIN panel_option_dict d ON d.option_id=o.id ORDER BY o.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("oid"));
                m.put("name", rs.getString("oname"));
                m.put("ten_viet", rs.getString("ten_viet"));
                m.put("goi_y", rs.getString("goi_y_param"));
                m.put("cach_dung", rs.getString("cach_dung"));
                m.put("min", rs.getLong("min_param"));
                m.put("max", rs.getLong("max_param"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String saveOptionDict(int optionId, String tenViet, String goiY, long min, long max) {
        return saveOptionDict(optionId, tenViet, goiY, min, max, null);
    }

    public static String saveOptionDict(int optionId, String tenViet, String goiY, long min, long max, String cachDung) {
        ensureHelperTables();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("INSERT INTO panel_option_dict(option_id,ten_viet,goi_y_param,cach_dung,min_param,max_param) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE ten_viet=VALUES(ten_viet),goi_y_param=VALUES(goi_y_param),cach_dung=VALUES(cach_dung),min_param=VALUES(min_param),max_param=VALUES(max_param)")) {
            ps.setInt(1, optionId);
            ps.setString(2, tenViet == null ? "" : tenViet);
            ps.setString(3, goiY == null ? "" : goiY);
            ps.setString(4, cachDung == null ? "" : cachDung);
            ps.setLong(5, min);
            ps.setLong(6, max);
            ps.executeUpdate();
            return "OK da luu tu dien option " + optionId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ TU DIEN OPTION: tao option MOI tu panel ============
    // LUU Y man hinh (Controller.createItem): client gan option id = VI TRI mang
    // va dem so option bang 1 byte (readUnsignedByte) -> id phai ke tiep 0..N
    // (chen giau lam SAI ten toan bo option sau do), tong toi da 255.
    public static String createOptionTemplate(int id, String name, int type, String tenViet, String goiY, long min, long max, String cachDung) {
        ensureHelperTables();
        if (name == null || name.trim().isEmpty()) return "Loi: ten option khong duoc rong";
        if (min > max) return "Loi: min lon hon max";
        int next = -1;
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT MAX(id) FROM item_option_template");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) next = rs.getInt(1) + 1;
        } catch (Exception e) { return "Loi doc item_option_template: " + e.getMessage(); }
        if (id != next) return "Loi: id phai la " + next + " (ke tiep). Client gan id = vi tri mang 0..N -> chen giua/nho hon se lam SAI ten toan bo option sau do.";
        if (next > 254) return "Loi: da day 255 option (client dem 1 byte). Can nang giao thuc writeShort + patch Controller.cs truoc khi them tu 255 tro len.";
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO item_option_template (id, NAME, type) VALUES (?,?,?)")) {
                ps.setInt(1, id); ps.setString(2, name.trim()); ps.setInt(3, type);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO panel_option_dict(option_id,ten_viet,goi_y_param,cach_dung,min_param,max_param) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE ten_viet=VALUES(ten_viet),goi_y_param=VALUES(goi_y_param),cach_dung=VALUES(cach_dung),min_param=VALUES(min_param),max_param=VALUES(max_param)")) {
                ps.setInt(1, id);
                ps.setString(2, tenViet == null ? "" : tenViet);
                ps.setString(3, goiY == null ? "" : goiY);
                ps.setString(4, cachDung == null ? "" : cachDung);
                ps.setLong(5, min); ps.setLong(6, max);
                ps.executeUpdate();
            }
        } catch (Exception e) { return "Loi INSERT: " + e.getMessage(); }
        // append vao bo nho ngay (khong clear -> khong race voi thread dang login)
        try {
            boolean has = false;
            for (models.Template.ItemOptionTemplate t : server.Manager.ITEM_OPTION_TEMPLATES) if (t.id == id) { has = true; break; }
            if (!has) server.Manager.ITEM_OPTION_TEMPLATES.add(new models.Template.ItemOptionTemplate(id, name.trim(), type));
        } catch (Exception e) {}
        return "OK da tao option " + id + " - " + name + ". LUU Y: client dang online can DANG NHAP LAI de nhan ds option moi.";
    }

    public static String optionDisplay(int id) {
        String base = null;
        try {
            for (models.Template.ItemOptionTemplate t : server.Manager.ITEM_OPTION_TEMPLATES) {
                if (t.id == id) { base = t.name; break; }
            }
        } catch (Exception e) {}
        String vn = null, hint = null;
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT ten_viet,goi_y_param FROM panel_option_dict WHERE option_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { vn = rs.getString(1); hint = rs.getString(2); }
            }
        } catch (Exception e) {}
        String label = "#" + id + (base != null ? " " + base : "");
        if (vn != null && !vn.trim().isEmpty()) label += " | " + vn.trim();
        if (hint != null && !hint.trim().isEmpty()) label += " (" + hint.trim() + ")";
        return label;
    }

    // ============ SHOP EDITOR: template full / tim kiem ============
    public static models.Template.ItemTemplate getTemplateById(int tempId) {
        try { return services.ItemService.gI().getTemplate((short) tempId); } catch (Exception e) { return null; }
    }

    public static List<Map<String, Object>> listItemTemplatesFull(String search, int typeFilter, int genderFilter, int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        String key = search == null ? "" : search.trim().toLowerCase();
        String keyNoAccent = key;
        try { keyNoAccent = boss.BossManager.convertString(key); } catch (Exception e) {}
        int count = 0;
        int lim = Math.max(50, Math.min(100000, limit)); // [07/10/2026] tang cap de Thu Vien Vat Pham hien TOAN BO item_template (truoc do bi chan 500)
        try {
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                if (t == null) continue;
                if (typeFilter >= 0 && t.type != typeFilter) continue;
                if (genderFilter >= 0 && t.gender != genderFilter) continue;
                if (!key.isEmpty()) {
                    boolean match = false;
                    try {
                        if (String.valueOf(t.id).equals(key)) match = true;
                        String nm = t.name == null ? "" : t.name.toLowerCase();
                        if (nm.contains(key)) match = true;
                        else {
                            String nm2 = boss.BossManager.convertString(nm);
                            if (nm2.contains(keyNoAccent)) match = true;
                        }
                    } catch (Exception e) {}
                    if (!match) continue;
                }
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", (int) t.id);
                m.put("name", t.name);
                m.put("type", (int) t.type);
                m.put("type_vn", itemTypeNameVN(t.type));
                m.put("gender", (int) t.gender);
                m.put("gender_vn", genderName(t.gender));
                m.put("icon", (int) t.iconID);
                m.put("part", (int) t.part);
                m.put("head", t.head); m.put("body", t.body); m.put("leg", t.leg);
                m.put("gold", t.gold); m.put("gem", t.gem);
                m.put("desc", t.description);
                out.add(m);
                if (++count >= lim) break;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ============ SHOP EDITOR: parse/build/validate ============
    public static List<ShopItemModel> parseShopItems(int tabId) {
        List<ShopItemModel> out = new ArrayList<>();
        String json = getTabItems(tabId);
        try {
            Object o = org.json.simple.JSONValue.parse(json);
            if (o instanceof org.json.simple.JSONArray) {
                for (Object oo : (org.json.simple.JSONArray) o) {
                    if (oo instanceof org.json.simple.JSONObject) {
                        out.add(ShopItemModel.fromJson((org.json.simple.JSONObject) oo));
                    }
                }
            }
        } catch (Exception e) {}
        enrichNames(out);
        return out;
    }

    public static void enrichNames(List<ShopItemModel> list) {
        try {
            java.util.Map<Integer, String> map = new java.util.HashMap<>();
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                if (t != null) map.put((int) t.id, t.name);
            }
            for (ShopItemModel m : list) {
                String n = map.get(m.tempId);
                if (n != null) m.itemName = n;
                else if (m.itemName == null) m.itemName = "";
            }
        } catch (Exception e) {}
    }

    public static String buildShopItemsJson(List<ShopItemModel> list) {
        org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
        for (ShopItemModel m : list) arr.add(m.toJson());
        return arr.toJSONString();
    }

    public static List<String> validateShopItems(List<ShopItemModel> list) {
        List<String> errs = new ArrayList<>();
        java.util.Set<Integer> validTemp = new java.util.HashSet<>();
        java.util.Set<Integer> validOpt = new java.util.HashSet<>();
        try {
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) if (t != null) validTemp.add((int) t.id);
            for (models.Template.ItemOptionTemplate t : server.Manager.ITEM_OPTION_TEMPLATES) validOpt.add(t.id);
        } catch (Exception e) {}
        java.util.Map<Integer, long[]> dictRange = new java.util.HashMap<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT option_id,min_param,max_param FROM panel_option_dict");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) dictRange.put(rs.getInt(1), new long[]{rs.getLong(2), rs.getLong(3)});
        } catch (Exception e) {}
        for (int i = 0; i < list.size(); i++) {
            ShopItemModel m = list.get(i);
            String prefix = "Dong " + (i + 1) + " (temp " + m.tempId + "): ";
            if (!validTemp.contains(m.tempId)) errs.add(prefix + "temp_id khong ton tai");
            if (m.cost < 0) errs.add(prefix + "gia am");
            if (m.typeSell != 0 && m.typeSell != 1 && m.typeSell != 3 && m.typeSell != 4) errs.add(prefix + "type_sell la (" + m.typeSell + "), chi nhan 0/1/3/4");
            for (ShopItemModel.Opt op : m.options) {
                if (!validOpt.contains(op.id)) { errs.add(prefix + "option id " + op.id + " khong ton tai"); continue; }
                long[] rg = dictRange.get(op.id);
                if (rg != null && (op.param < rg[0] || op.param > rg[1])) errs.add(prefix + "option " + op.id + " param " + op.param + " ngoai [" + rg[0] + "-" + rg[1] + "]");
                if (op.param < 0) errs.add(prefix + "option " + op.id + " param am");
            }
        }
        return errs;
    }

    public static String itemSummaryVN(ShopItemModel m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.itemName == null || m.itemName.isEmpty() ? ("#" + m.tempId) : m.itemName);
        if (m.itemSpec > 0) {
            sb.append(" x1 - ").append(m.cost).append(" x ").append(specItemName(m.itemSpec));
        } else {
            sb.append(" x1 - ").append(m.cost).append(" ").append(typeSellName(m.typeSell));
        }
        if (!m.options.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < m.options.size(); i++) {
                if (i > 0) sb.append(", ");
                ShopItemModel.Opt op = m.options.get(i);
                String vn = shortOptionName(op.id);
                sb.append(vn).append(" +").append(op.param);
            }
            sb.append("]");
        }
        return sb.toString();
    }

    private static String shortOptionName(int id) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT ten_viet FROM panel_option_dict WHERE option_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { String s = rs.getString(1); if (s != null && !s.trim().isEmpty()) return s.trim(); }
            }
        } catch (Exception e) {}
        try {
            for (models.Template.ItemOptionTemplate t : server.Manager.ITEM_OPTION_TEMPLATES) {
                if (t.id == id) return t.name;
            }
        } catch (Exception e) {}
        return "Opt" + id;
    }

    public static String previewShopItems(List<ShopItemModel> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("XEM TRUOC ").append(list.size()).append(" mon:\n");
        for (int i = 0; i < list.size(); i++) sb.append(i + 1).append(". ").append(itemSummaryVN(list.get(i))).append("\n");
        List<String> errs = validateShopItems(list);
        if (!errs.isEmpty()) {
            sb.append("\nCANH BAO (").append(errs.size()).append("):\n");
            for (String e : errs) sb.append("- ").append(e).append("\n");
        } else sb.append("\nHop le, san sang Xuat ban.\n");
        return sb.toString();
    }

    // ============ SHOP EDITOR: backup/undo/history/publish ============
    public static String backupTab(int tabId) {
        try {
            String json = getTabItems(tabId);
            java.io.File dir = new java.io.File("log/panel_backup");
            dir.mkdirs();
            String fn = "log/panel_backup/tab_" + tabId + "_" + System.currentTimeMillis() + ".json";
            try (java.io.FileWriter fw = new java.io.FileWriter(fn)) { fw.write(json == null ? "[]" : json); }
            ensureHelperTables();
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO panel_history(tab_id,actor,action,summary,items_backup) VALUES(?,'admin','backup',?,?)")) {
                ps.setInt(1, tabId);
                ps.setString(2, "Backup truoc khi sua (" + fn + ")");
                ps.setString(3, json);
                ps.executeUpdate();
            } catch (Exception e) {}
            return fn;
        } catch (Exception e) { return "Loi backup: " + e.getMessage(); }
    }

    public static String publishTabModels(int tabId, List<ShopItemModel> list, String actor) {
        List<String> errs = validateShopItems(list);
        if (!errs.isEmpty()) {
            StringBuilder sb = new StringBuilder("Chua the Xuat ban, con loi:\n");
            for (String e : errs) sb.append("- ").append(e).append("\n");
            return sb.toString();
        }
        String oldJson = getTabItems(tabId);
        String fn = backupTab(tabId);
        String json = buildShopItemsJson(list);
        String r = setTabItems(tabId, json);
        ensureHelperTables();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("INSERT INTO panel_history(tab_id,actor,action,summary,items_backup) VALUES(?,?,?, ?,?)")) {
            ps.setInt(1, tabId);
            ps.setString(2, actor == null ? "admin" : actor);
            ps.setString(3, "publish");
            ps.setString(4, "Xuat ban " + list.size() + " mon. Backup: " + fn);
            ps.setString(5, oldJson);
            ps.executeUpdate();
        } catch (Exception e) {}
        return r + " (backup: " + fn + ")";
    }

    public static String undoTab(int tabId) {
        ensureHelperTables();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT items_backup FROM panel_history WHERE tab_id=? AND items_backup IS NOT NULL AND LENGTH(items_backup)>0 ORDER BY id DESC LIMIT 1")) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString(1);
                    jdbc.DBConnecter.executeUpdate("update tab_shop set items = ? where id = ?", json, tabId);
                    server.Manager.gI().updateShop();
                    return "OK da hoan tac ve ban backup gan nhat";
                }
            }
            return "Chua co backup de hoan tac";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static List<Map<String, Object>> tabHistory(int tabId, int limit) {
        ensureHelperTables();
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT id,actor,action,summary,created_at FROM panel_history WHERE tab_id=? ORDER BY id DESC LIMIT " + Math.max(1, Math.min(200, limit)))) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt(1)); m.put("actor", rs.getString(2));
                    m.put("action", rs.getString(3)); m.put("summary", rs.getString(4));
                    m.put("time", String.valueOf(rs.getTimestamp(5)));
                    out.add(m);
                }
            }
        } catch (Exception e) {}
        return out;
    }

    public static String cloneTab(int tabId, String newTabName) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT shop_id,tab_index,items FROM tab_shop WHERE id=?")) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "Khong thay tab " + tabId;
                int shopId = rs.getInt(1);
                String items = rs.getString(3);
                int maxIdx = 0;
                try (PreparedStatement ps2 = con.prepareStatement("SELECT COALESCE(MAX(tab_index),0) FROM tab_shop WHERE shop_id=?")) {
                    ps2.setInt(1, shopId);
                    try (ResultSet rs2 = ps2.executeQuery()) { if (rs2.next()) maxIdx = rs2.getInt(1); }
                }
                jdbc.DBConnecter.executeUpdate("insert into tab_shop(shop_id,tab_name,tab_index,items) values(?,?,?,?)", shopId, (newTabName == null || newTabName.isEmpty() ? "Copy" : newTabName), maxIdx + 1, items);
                server.Manager.gI().updateShop();
                return "OK da nhan ban tab " + tabId;
            }
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ SHOP EDITOR: CSV ============
    public static String exportTabCsv(int tabId) {
        List<ShopItemModel> list = parseShopItems(tabId);
        return PanelCsv.exportTab(list);
    }

    public static String importTabCsv(int tabId, String csv, boolean publish) {
        PanelCsv.ImportResult r = PanelCsv.importCsv(csv);
        enrichNames(r.items);
        List<String> errs = validateShopItems(r.items);
        StringBuilder sb = new StringBuilder();
        sb.append("Doc duoc ").append(r.items.size()).append(" dong. Loi dong: ").append(r.errors.size()).append(". Loi validate: ").append(errs.size()).append("\n");
        for (String e : r.errors) sb.append("[CSV] ").append(e).append("\n");
        for (String e : errs) sb.append("[Check] ").append(e).append("\n");
        if (!publish) { sb.append("\n--- XEM TRUOC ---\n").append(previewShopItems(r.items)); return sb.toString(); }
        if (!r.errors.isEmpty() || !errs.isEmpty()) return sb.toString() + "\nChua Xuat ban vi con loi. Sua CSV roi thu lai.";
        String pr = publishTabModels(tabId, r.items, "admin-csv");
        return sb.toString() + "\n" + pr;
    }

    // ============ THU VIEN: dang ban o dau ============
    public static List<String> whereUsed(int tempId) {
        List<String> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT t.id,s.tag_name,t.tab_name,t.items FROM tab_shop t LEFT JOIN shop s ON s.id=t.shop_id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String items = rs.getString(4);
                    if (items != null && items.contains("\"temp_id\":" + tempId)) {
                        out.add("Shop " + rs.getString(2) + " / Tab " + rs.getString(3) + " (tab_id=" + rs.getInt(1) + ")");
                    } else if (items != null && items.contains(String.valueOf(tempId))) {
                        try {
                            Object o = org.json.simple.JSONValue.parse(items);
                            if (o instanceof org.json.simple.JSONArray) {
                                for (Object oo : (org.json.simple.JSONArray) o) {
                                    if (oo instanceof org.json.simple.JSONObject) {
                                        Object tid = ((org.json.simple.JSONObject) oo).get("temp_id");
                                        if (tid != null && String.valueOf(tid).equals(String.valueOf(tempId))) {
                                            out.add("Shop " + rs.getString(2) + " / Tab " + rs.getString(3) + " (tab_id=" + rs.getInt(1) + ")");
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        } catch (Exception e) {}
        try {
            for (models.GiftCode.GiftCode g : models.GiftCode.GiftCodeManager.gI().listGiftCode) {
                try {
                    if (g.detail != null && g.detail.containsKey(tempId)) {
                        out.add("Giftcode " + g.code);
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        if (out.isEmpty()) out.add("Chua ban o dau / chua co trong giftcode");
        return out;
    }

    // ============ TAO DO MUC 1 / NPC ============
    public static String createItemTemplateReuse(int copyFromTempId, String newName, int typeOverride, int genderOverride, int iconOverride, int partOverride) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            int maxId = 0;
            try (PreparedStatement ps = con.prepareStatement("SELECT COALESCE(MAX(id),0) FROM item_template");
                 ResultSet rs = ps.executeQuery()) { if (rs.next()) maxId = rs.getInt(1); }
            int newId = maxId + 1;
            if (copyFromTempId >= 0) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO item_template(id,TYPE,gender,NAME,description,level,icon_id,part,is_up_to_up,power_require,gold,gem,head,body,leg,is_up_to_up_over_99,can_trade,ruby) SELECT ?,IF(?, -1, TYPE),IF(?,-1,gender),IF(?,'',NAME),description,level,IF(?,-1,icon_id),IF(?,-9999,part),is_up_to_up,power_require,gold,gem,head,body,leg,is_up_to_up_over_99,can_trade,ruby FROM item_template WHERE id=?")) {
                    ps.setInt(1, newId);
                    ps.setInt(2, typeOverride); ps.setInt(3, genderOverride); ps.setString(4, newName == null ? "" : newName);
                    ps.setInt(5, iconOverride); ps.setInt(6, partOverride); ps.setInt(7, copyFromTempId);
                    int n = ps.executeUpdate();
                    if (n <= 0) return "Khong thay mau temp " + copyFromTempId;
                }
            } else {
                jdbc.DBConnecter.executeUpdate("insert into item_template(id,TYPE,gender,NAME,description,level,icon_id,part,is_up_to_up,power_require,gold,gem,head,body,leg,ruby) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    newId, typeOverride < 0 ? 27 : typeOverride, genderOverride < 0 ? 3 : genderOverride,
                    newName == null || newName.isEmpty() ? ("Do moi " + newId) : newName, "", 0,
                    iconOverride < 0 ? 0 : iconOverride, partOverride <= -9999 ? -1 : partOverride, 0, 0, 0, 0, -1, -1, -1, 0);
            }
            try { server.Manager.ITEM_TEMPLATES.clear(); } catch (Exception e) {}
            return "OK da tao item_template id=" + newId + " (tai dung anh cu, can reload server de thay; neu client cu thi dung ngay vi icon/part cu)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String exportNewItemSpec(String tenDo, int iconCanVe, int partCanVe, String ghiChu) {
        StringBuilder sb = new StringBuilder();
        sb.append("SPEC DO MOI CHO DEV CLIENT:\n");
        sb.append("- Ten: ").append(tenDo).append("\n");
        sb.append("- Icon can ve: ").append(iconCanVe).append(" (them vao data/res, tang smallimage_version)\n");
        sb.append("- Part can ve: ").append(partCanVe).append("\n");
        sb.append("- Ghi chu: ").append(ghiChu).append("\n");
        sb.append("- Buoc dev: ve icon -> dong goi res x1-x4 -> tang version -> build client -> moi insert item_template live.\n");
        sb.append("Panel KHONG tu publish muc 2 de tranh crash client cu.\n");
        try {
            java.io.File dir = new java.io.File("log/panel_backup");
            dir.mkdirs();
            String fn = "log/panel_backup/spec_" + System.currentTimeMillis() + ".txt";
            try (java.io.FileWriter fw = new java.io.FileWriter(fn)) { fw.write(sb.toString()); }
            sb.append("Da luu: ").append(fn);
        } catch (Exception e) {}
        return sb.toString();
    }

    // ============ RUONG (BOX) - TAO/COPY/SUA TU PANEL ============
    private static void ensureChestTables() {
        try { panel.chest.ChestManager.ensureTables(); } catch (Exception e) {}
    }

    /** Danh sach ruong hien co (1 ruong = 1 item_template moi). */
    public static List<Map<String, Object>> listChests() {
        ensureChestTables();
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT c.item_id, c.name, c.mode, c.note, t.NAME AS item_name, COUNT(e.id) AS n "
                     + "FROM panel_chest c "
                     + "LEFT JOIN panel_chest_entry e ON e.chest_id = c.item_id "
                     + "LEFT JOIN item_template t ON t.id = c.item_id "
                     + "GROUP BY c.item_id, c.name, c.mode, c.note, t.NAME ORDER BY c.item_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("item_id", rs.getInt("item_id"));
                m.put("name", rs.getString("name"));
                m.put("mode", rs.getInt("mode"));
                m.put("note", rs.getString("note"));
                m.put("item_name", rs.getString("item_name"));
                m.put("n", rs.getInt("n"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Noi dung 1 ruong: cac mon + so luong + ty le. */
    public static List<Map<String, Object>> listChestEntries(int chestId) {
        ensureChestTables();
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT e.id, e.temp_id, e.qty, e.rate, t.NAME AS item_name "
                     + "FROM panel_chest_entry e LEFT JOIN item_template t ON t.id = e.temp_id "
                     + "WHERE e.chest_id = ? ORDER BY e.id")) {
            ps.setInt(1, chestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("temp_id", rs.getInt("temp_id"));
                    m.put("qty", rs.getInt("qty"));
                    m.put("rate", rs.getDouble("rate"));
                    m.put("item_name", rs.getString("item_name"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Tao ruong MOI: item_template moi (copy mau) + 1 dong panel_chest rong. */
    public static String createChest(String name, int sourceTempId) {
        StringBuilder err = new StringBuilder();
        int newId = newChestItem(name, sourceTempId, err);
        if (newId < 0) return err.toString();
        return "OK da tao ruong \"" + name.trim() + "\" - item_template id=" + newId
                + " (moi ruong = item MOI luu vao DB, copy hinh/loai tu item mau " + sourceTempId + "). Them noi dung o bang ben phai.";
    }

    private static int newChestItem(String name, int sourceTempId, StringBuilder err) {
        name = name == null ? "" : name.trim();
        if (name.isEmpty()) { err.append("Loi: ten ruong khong duoc rong"); return -1; }
        if (name.length() > 64) name = name.substring(0, 64);
        if (sourceTempId <= 0) sourceTempId = 1413;
        try {
            ensureChestTables();
            try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
                boolean srcOk = false;
                try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM item_template WHERE id = ?")) {
                    ps.setInt(1, sourceTempId);
                    try (ResultSet rs = ps.executeQuery()) { srcOk = rs.next(); }
                }
                if (!srcOk) { err.append("Loi: khong thay item mau id=" + sourceTempId); return -1; }
                int newId;
                synchronized (PanelService.class) {
                    int maxId = 0;
                    try (PreparedStatement ps = con.prepareStatement("SELECT COALESCE(MAX(id),0) FROM item_template");
                         ResultSet rs = ps.executeQuery()) { if (rs.next()) maxId = rs.getInt(1); }
                    newId = maxId + 1;
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO item_template(id,TYPE,gender,NAME,description,level,icon_id,part,is_up_to_up,"
                            + "power_require,gold,gem,head,body,leg,is_up_to_up_over_99,can_trade,comment,ruby) "
                            + "SELECT ?,TYPE,gender,?,description,level,icon_id,part,is_up_to_up,power_require,gold,gem,"
                            + "head,body,leg,is_up_to_up_over_99,can_trade,comment,ruby FROM item_template WHERE id=?")) {
                        ps.setInt(1, newId);
                        ps.setString(2, name);
                        ps.setInt(3, sourceTempId);
                        if (ps.executeUpdate() <= 0) { err.append("Loi: tao item_template that bai"); return -1; }
                    }
                    try (PreparedStatement ps = con.prepareStatement("INSERT INTO panel_chest(item_id,name,mode,note) VALUES(?,?,0,'')")) {
                        ps.setInt(1, newId);
                        ps.setString(2, name);
                        ps.executeUpdate();
                    }
                }
                appendTemplateLive(newId, con);
                try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
                return newId;
            }
        } catch (Exception e) { err.append("Loi: " + e.getMessage()); return -1; }
    }

    /** Them item_template vao list dang chay (khong can restart). Chi add khi list dang 0..max. */
    private static void appendTemplateLive(int newId, Connection con) {
        try {
            java.util.List<models.Template.ItemTemplate> list = server.Manager.ITEM_TEMPLATES;
            if (list.size() != newId) return; // list khong dang theo id -> can restart server
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM item_template WHERE id = ?")) {
                ps.setInt(1, newId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return;
                    models.Template.ItemTemplate t = new models.Template.ItemTemplate();
                    t.id = rs.getShort("id");
                    t.type = rs.getByte("type");
                    t.gender = rs.getByte("gender");
                    t.name = rs.getString("name");
                    t.description = rs.getString("description");
                    t.level = rs.getByte("level");
                    t.iconID = rs.getShort("icon_id");
                    t.part = rs.getShort("part");
                    t.isUpToUp = rs.getBoolean("is_up_to_up");
                    t.strRequire = rs.getInt("power_require");
                    t.gold = rs.getInt("gold");
                    t.gem = rs.getInt("gem");
                    t.head = rs.getInt("head");
                    t.body = rs.getInt("body");
                    t.leg = rs.getInt("leg");
                    list.add(t);
                }
            }
        } catch (Exception e) {}
    }

    /** Copy 1 ruong -> ruong MOI (item template moi + sao chep noi dung). */
    public static String copyChest(int srcChestId, String newName) {
        try {
            ensureChestTables();
            boolean srcOk = false;
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("SELECT 1 FROM panel_chest WHERE item_id = ?")) {
                ps.setInt(1, srcChestId);
                try (ResultSet rs = ps.executeQuery()) { srcOk = rs.next(); }
            }
            if (!srcOk) return "Loi: khong thay ruong id=" + srcChestId;
            StringBuilder err = new StringBuilder();
            int newId = newChestItem(newName, srcChestId, err);
            if (newId < 0) return err.toString();
            int n = 0;
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement(
                         "INSERT INTO panel_chest_entry(chest_id,temp_id,qty,rate) "
                         + "SELECT ?,temp_id,qty,rate FROM panel_chest_entry WHERE chest_id = ?")) {
                ps.setInt(1, newId);
                ps.setInt(2, srcChestId);
                n = ps.executeUpdate();
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da copy ruong " + srcChestId + " -> ruong MOI id=" + newId + " (" + n + " mon). Chon ruong moi de sua noi dung.";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String addChestEntry(int chestId, int tempId, int qty, double rate) {
        try {
            ensureChestTables();
            if (qty < 1) qty = 1;
            if (rate <= 0) return "Loi: ty le phai > 0";
            boolean chestOk = false, itemOk = false;
            try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
                try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM panel_chest WHERE item_id = ?")) {
                    ps.setInt(1, chestId);
                    try (ResultSet rs = ps.executeQuery()) { chestOk = rs.next(); }
                }
                if (!chestOk) return "Loi: khong thay ruong id=" + chestId;
                try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM item_template WHERE id = ?")) {
                    ps.setInt(1, tempId);
                    try (ResultSet rs = ps.executeQuery()) { itemOk = rs.next(); }
                }
                if (!itemOk) return "Loi: khong thay vat pham temp_id=" + tempId;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO panel_chest_entry(chest_id,temp_id,qty,rate) VALUES(?,?,?,?)")) {
                    ps.setInt(1, chestId); ps.setInt(2, tempId); ps.setInt(3, qty); ps.setDouble(4, rate);
                    ps.executeUpdate();
                }
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da them temp " + tempId + " x" + qty + " (ty le " + rate + ") vao ruong " + chestId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String updateChestEntry(int entryId, int qty, double rate) {
        try {
            if (qty < 1) qty = 1;
            if (rate <= 0) return "Loi: ty le phai > 0";
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("UPDATE panel_chest_entry SET qty = ?, rate = ? WHERE id = ?")) {
                ps.setInt(1, qty); ps.setDouble(2, rate); ps.setInt(3, entryId);
                if (ps.executeUpdate() <= 0) return "Loi: khong thay dong id=" + entryId;
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da sua dong " + entryId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteChestEntry(int entryId) {
        try {
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM panel_chest_entry WHERE id = ?")) {
                ps.setInt(1, entryId);
                if (ps.executeUpdate() <= 0) return "Loi: khong thay dong id=" + entryId;
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da xoa mon khoi ruong";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String setChestMode(int chestId, int mode) {
        try {
            if (mode != 0 && mode != 1) mode = 0;
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("UPDATE panel_chest SET mode = ? WHERE item_id = ?")) {
                ps.setInt(1, mode); ps.setInt(2, chestId);
                if (ps.executeUpdate() <= 0) return "Loi: khong thay ruong id=" + chestId;
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da dat mode " + mode + " cho ruong " + chestId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteChest(int chestId) {
        try {
            ensureChestTables();
            try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM panel_chest_entry WHERE chest_id = ?")) {
                    ps.setInt(1, chestId); ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM panel_chest WHERE item_id = ?")) {
                    ps.setInt(1, chestId);
                    if (ps.executeUpdate() <= 0) return "Loi: khong thay ruong id=" + chestId;
                }
            }
            try { panel.chest.ChestManager.gI().load(); } catch (Exception e) {}
            return "OK da xoa ruong " + chestId + " khoi danh sach. LUU Y: item_template id=" + chestId
                    + " van GIU (neu player da so huu van dung duoc nhung khong mo duoc nua).";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static List<Map<String, Object>> listNpc(int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT id,NAME,head,body,leg,avatar FROM npc_template ORDER BY id LIMIT " + Math.max(10, Math.min(2000, limit)));
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt(1)); m.put("name", rs.getString(2));
                m.put("head", rs.getInt(3)); m.put("body", rs.getInt(4)); m.put("leg", rs.getInt(5)); m.put("avatar", rs.getInt(6));
                out.add(m);
            }
        } catch (Exception e) {}
        return out;
    }

    public static String createNpcDraft(String name, int head, int body, int leg, int avatar) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            int maxId = 0;
            try (PreparedStatement ps = con.prepareStatement("SELECT COALESCE(MAX(id),0) FROM npc_template");
                 ResultSet rs = ps.executeQuery()) { if (rs.next()) maxId = rs.getInt(1); }
            int newId = maxId + 1;
            jdbc.DBConnecter.executeUpdate("insert into npc_template(id,NAME,head,body,leg,avatar) values(?,?,?,?,?,?)", newId, name, head, body, leg, avatar);
            return "OK da tao npc_template id=" + newId + " (buoc 2: tao shop cho npc nay, buoc 3: dat vao map - can restart)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String createShopForNpc(int npcId, String tagName, int typeShop) {
        try {
            jdbc.DBConnecter.executeUpdate("insert into shop(npc_id,tag_name,type_shop) values(?,?,?)", npcId, tagName, typeShop);
            int shopId = 0;
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("SELECT id FROM shop WHERE tag_name=? ORDER BY id DESC LIMIT 1")) {
                ps.setString(1, tagName);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) shopId = rs.getInt(1); }
            }
            if (shopId > 0) jdbc.DBConnecter.executeUpdate("insert into tab_shop(shop_id,tab_name,tab_index,items) values(?,'Tab 1',0,'[]')", shopId);
            server.Manager.gI().updateShop();
            return "OK da tao shop " + tagName + " (id=" + shopId + ") + 1 tab rong";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT 3: BOSS OVERRIDE (admin sua tren panel, khong can code) ============
    public static Map<String, Object> bossEditData(String field) {
        Map<String, Object> m = new LinkedHashMap<>();
        try {
            java.lang.reflect.Field f = BossesData.class.getDeclaredField(field);
            f.setAccessible(true);
            BossData d = (BossData) f.get(null);
            if (d == null) return m;
            m.put("field", field);
            m.put("name", d.getName());
            m.put("dameGoc", d.getDame());
            double[] hp = d.getHp();
            StringBuilder hps = new StringBuilder();
            if (hp != null) for (int i = 0; i < hp.length; i++) { if (i > 0) hps.append(","); hps.append((long) Math.min(hp[i], 9.0e18)); }
            m.put("hpGoc", hps.toString());
            m.put("hpLevels", hp == null ? 0 : hp.length);
            int[] maps = d.getMapJoin();
            StringBuilder ms = new StringBuilder();
            if (maps != null) for (int i = 0; i < maps.length; i++) { if (i > 0) ms.append(","); ms.append(maps[i]); }
            m.put("mapsGoc", ms.toString());
            m.put("mapsName", mapsNameStr(maps));
            m.put("restGoc", d.getSecondsRest());
            m.put("bossId", resolveBossId(field));
            m.put("nhom", panel.tuning.BossResolve.groupOf(field));
            panel.tuning.BossTuning.BossOv o = panel.tuning.BossTuning.get(field);
            m.put("dameOv", o.dame);
            m.put("hpOv", o.hp);
            m.put("restOv", o.rest);
            m.put("mapsOv", o.maps);
            m.put("drop", panel.tuning.BossTuning.toJson(o.drops));
            m.put("dropQty", "");
            m.put("dropRate", "");
            m.put("note", o.note);
            m.put("goiY", goiYDameAo(d.getDame(), hpMaxOf(d)));
            m.put("reward", bossRewardText(field));
        } catch (Exception e) {}
        return m;
    }

    private static long hpMaxOf(BossData d) {
        long mx = 0;
        try { for (double h : d.getHp()) mx = Math.max(mx, (long) Math.min(h, 9.0e18)); } catch (Exception e) {}
        return mx;
    }

    private static String mapsNameStr(int[] maps) {
        if (maps == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maps.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(maps[i]).append(":").append(mapName(maps[i]));
        }
        return sb.toString();
    }

    /** Do roi goc: doc ham reward() cua class boss (ItemMap temp) de admin biet. */
    public static String bossRewardText(String field) {
        try {
            String compact = field.replace("_", "").toLowerCase();
            java.io.File root = new java.io.File("src/boss/boss_manifest");
            List<java.io.File> files = new ArrayList<>();
            collectJava(root, files);
            for (java.io.File f : files) {
                try {
                    String t = new String(java.nio.file.Files.readAllBytes(f.toPath()), "UTF-8");
                    if (t.contains("BossesData." + field)) {
                        // tim cac so ItemMap(..., temp, qty
                        StringBuilder sb = new StringBuilder();
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("ItemMap\\s*\\([^,]+,\\s*(\\d+)\\s*,\\s*(\\d+)").matcher(t);
                        while (m.find()) sb.append("temp ").append(m.group(1)).append(" x").append(m.group(2)).append("; ");
                        String cls = f.getName();
                        if (sb.length() == 0) return cls + ": reward mac dinh (nhiem vu/khong roi them)";
                        return cls + ": " + sb.toString();
                    }
                } catch (Exception e) {}
            }
            // fallback: tim theo ten gan dung
            for (java.io.File f : files) {
                try {
                    String t = new String(java.nio.file.Files.readAllBytes(f.toPath()), "UTF-8");
                    String low = f.getName().replace("_", "").replace(".java", "").toLowerCase();
                    if (compact.contains(low) || low.contains(compact.replace(" ", ""))) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("ItemMap\\s*\\([^,]+,\\s*(\\d+)\\s*,\\s*(\\d+)").matcher(t);
                        StringBuilder sb = new StringBuilder();
                        while (m.find()) sb.append("temp ").append(m.group(1)).append(" x").append(m.group(2)).append("; ");
                        if (sb.length() > 0) return f.getName() + " (doan): " + sb.toString();
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return "Base Boss.reward (nhiem vu) - boss khong override = KHONG roi do";
    }

    private static void collectJava(java.io.File dir, List<java.io.File> out) {
        if (dir == null || !dir.exists()) return;
        java.io.File[] fs = dir.listFiles();
        if (fs == null) return;
        for (java.io.File f : fs) {
            if (f.isDirectory()) collectJava(f, out);
            else if (f.getName().endsWith(".java")) out.add(f);
        }
    }

    public static String saveBossEdit(String field, double dame, String hp, int rest, String maps, int drop, int qty, int rate, String note) {
        try {
            String dropJson = "";
            if (drop > 0) {
                org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
                org.json.simple.JSONObject o = new org.json.simple.JSONObject();
                o.put("temp_id", drop); o.put("qty", qty > 0 ? qty : 1);
                o.put("rate", rate > 0 ? rate : 100); o.put("options", new org.json.simple.JSONArray());
                arr.add(o);
                dropJson = arr.toJSONString();
            }
            return panel.tuning.BossTuning.set(field, dame, hp, rest, maps, dropJson, note);
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String resetBossEdit(String field) {
        try { return panel.tuning.BossTuning.reset(field); }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT 3: EVENT TUNING ============
    public static List<Map<String, Object>> listEventTuning() {
        List<Map<String, Object>> out = new ArrayList<>();
        Map<String, panel.tuning.EventTuning.Ev> all = panel.tuning.EventTuning.all();
        Map<String, Boolean> st = eventStates();
        for (Map.Entry<String, panel.tuning.EventTuning.Ev> e : all.entrySet()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("key", e.getKey());
            m.put("onFlag", st.getOrDefault(e.getKey(), e.getValue().on));
            m.put("on", e.getValue().on);
            m.put("drop", e.getValue().dropRate);
            m.put("point", e.getValue().pointRate);
            m.put("farm", e.getValue().farm);
            m.put("note", e.getValue().note);
            out.add(m);
        }
        return out;
    }

    public static String saveEventTuning(String key, boolean on, double drop, double point, String farm, String note) {
        try {
            // dong bo bat/tat Trung Thu voi NPC noi banh (bat = hien NPC, tat = an NPC)
            if ("TRUNG_THU".equals(key)) {
                panel.tuning.TrungThuTuning.setRaw("on", on ? "1" : "0");
            }
            return panel.tuning.EventTuning.set(key, on, drop, point, farm, note);
        }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ SU KIEN TRUNG THU (phan tang: ty le -> qua ruong -> chi so) ============
    public static List<Map<String, Object>> listTrungThuTuning() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (panel.tuning.TrungThuTuning.Row r : panel.tuning.TrungThuTuning.rows()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("layer", r.layer);
            m.put("key", r.key);
            m.put("value", r.value);
            m.put("note", r.note);
            out.add(m);
        }
        return out;
    }

    public static boolean isTrungThuOn() {
        try { return panel.tuning.TrungThuTuning.isOn(); } catch (Exception e) { return false; }
    }

    /**
     * Luu 1 khoa Trung Thu. Tro ve "" neu OK, nguoc lai la loi (khong luu).
     * Dong bo bat/tat vao EventManager.TRUNG_THU + EventTuning.
     */
    public static String saveTrungThuTuning(String key, String value) {
        try {
            String r = panel.tuning.TrungThuTuning.set(key, value);
            if (r.startsWith("OK")) {
                boolean on = panel.tuning.TrungThuTuning.isOn();
                event.EventManager.TRUNG_THU = on;
                panel.tuning.EventTuning.Ev ev = panel.tuning.EventTuning.all().get("TRUNG_THU");
                if (ev != null) {
                    panel.tuning.EventTuning.set("TRUNG_THU", on, ev.dropRate, ev.pointRate, ev.farm, ev.note);
                }
            }
            return r;
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    public static String saveTrungThuTuningAll(Map<String, String> vals) {
        StringBuilder sb = new StringBuilder();
        int ok = 0;
        if (vals != null) {
            for (Map.Entry<String, String> e : vals.entrySet()) {
                String r = saveTrungThuTuning(e.getKey(), e.getValue());
                if (r.startsWith("OK")) ok++;
                else sb.append("\n").append(r);
            }
        }
        return "Da luu " + ok + "/" + (vals == null ? 0 : vals.size()) + " khoa Trung Thu" + sb.toString();
    }

    // ---------- CRUD: MOC DOI DIEM (them / sua / xoa moc + sua qua trong moc) ----------
    /**
     * @return tung moc: {index, need, sukien, rewards (List<int[]>)}
     * rewards[i] = {itemId, soLuong, optionId, thamSoOption}
     */
    public static List<Map<String, Object>> listTrungThuExchange() {
        List<Map<String, Object>> out = new ArrayList<>();
        java.util.List<EventSuKien.TrungThuService.Exchange> list =
                EventSuKien.TrungThuService.gI().allExchanges();
        for (int i = 0; i < list.size(); i++) {
            EventSuKien.TrungThuService.Exchange e = list.get(i);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("index", i);
            m.put("need", e.need);
            m.put("sukien", e.sukienBonus);
            m.put("rewards", e.rewards);
            out.add(m);
        }
        return out;
    }

    /** Ghi lai toan bo danh sach moc (xoa cu, ghi moi 0..n-1). */
    public static String saveTrungThuExchange(List<Map<String, Object>> rows) {
        try {
            panel.tuning.TrungThuTuning.removePrefix("exchange.");
            int n = 0;
            if (rows != null) {
                for (Map<String, Object> m : rows) {
                    long need = toLong(m.get("need"));
                    long sukien = toLong(m.get("sukien"));
                    @SuppressWarnings("unchecked")
                    java.util.List<int[]> rewards = (java.util.List<int[]>) m.get("rewards");
                    StringBuilder sb = new StringBuilder();
                    sb.append(need).append("|").append(sukien).append("|");
                    boolean first = true;
                    if (rewards != null) {
                        for (int[] r : rewards) {
                            if (r == null || r[0] <= 0) continue;
                            if (!first) sb.append(",");
                            sb.append(r[0]).append(":").append(Math.max(1, r[1]))
                              .append(":").append(r[2]).append(":").append(r[3]);
                            first = false;
                        }
                    }
                    if (first) continue; // moc khong co qua -> bo qua
                    panel.tuning.TrungThuTuning.putQuiet("exchange." + n, sb.toString());
                    n++;
                }
            }
            panel.tuning.TrungThuTuning.save();
            return "Da luu " + n + " moc doi diem Trung Thu";
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    // ---------- CRUD: RUONG TRUNG THU 1914 ----------
    /** rewards[i] = {itemId, soLuong, optionId, thamSoOption} */
    public static Map<String, Object> listTrungThuBox() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("costumeRate", panel.tuning.TrungThuTuning.getInt("box.costumeRate", 5));
        out.put("costumeRange", panel.tuning.TrungThuTuning.get("box.costumeRange"));
        out.put("itemId", panel.tuning.TrungThuTuning.getInt("box.id", 1914));
        List<int[]> rewards = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            String raw = panel.tuning.TrungThuTuning.get("box.reward." + i);
            if (raw.isEmpty()) break;
            String[] p = raw.split("\\|");
            if (p.length < 4) continue;
            try {
                rewards.add(new int[]{
                    Integer.parseInt(p[0].trim()),
                    Integer.parseInt(p[1].trim()),
                    Integer.parseInt(p[2].trim()),
                    (int) panel.tuning.TrungThuTuning.parseRate(p[3])});
            } catch (Exception ignore) {
            }
        }
        out.put("rewards", rewards);
        return out;
    }

    public static String saveTrungThuBox(int costumeRate, String costumeRange, List<int[]> rewards) {
        try {
            panel.tuning.TrungThuTuning.putQuiet("box.costumeRate", String.valueOf(costumeRate));
            if (costumeRange != null && !costumeRange.trim().isEmpty()) {
                panel.tuning.TrungThuTuning.putQuiet("box.costumeRange", costumeRange.trim());
            }
            panel.tuning.TrungThuTuning.removePrefix("box.reward.");
            int n = 0;
            if (rewards != null) {
                for (int[] r : rewards) {
                    if (r == null || r[0] <= 0) continue;
                    panel.tuning.TrungThuTuning.putQuiet("box.reward." + n,
                            r[0] + "|" + Math.max(1, r[1]) + "|" + r[2] + "|" + r[3]);
                    n++;
                }
            }
            panel.tuning.TrungThuTuning.save();
            return "Da luu ruong Trung Thu: " + n + " qua, cai trang " + costumeRate + "%";
        } catch (Exception e) {
            return "Loi: " + e.getMessage();
        }
    }

    private static long toLong(Object o) {
        if (o == null) return 0;
        if (o instanceof Number) return ((Number) o).longValue();
        try {
            return (long) panel.tuning.TrungThuTuning.parseRate(String.valueOf(o));
        } catch (Exception e) {
            return 0;
        }
    }

    // ============ DOT 3: GAME TUNING (cong thuc chinh truc tiep) ============
    public static List<Map<String, Object>> listGameTuning() {
        List<Map<String, Object>> out = new ArrayList<>();
        panel.tuning.GameTuning.load();
        out.add(tRow("DAME_GLOBAL", panel.tuning.GameTuning.DAME_GLOBAL, "Nhan dame toan server (1.0 = giu goc). Tang tu tu 1.0 -> 1.2 -> 1.5, khong dot len 10."));
        out.add(tRow("HP_GLOBAL", panel.tuning.GameTuning.HP_GLOBAL, "Nhan HP toan server."));
        out.add(tRow("MP_GLOBAL", panel.tuning.GameTuning.MP_GLOBAL, "Nhan MP toan server."));
        out.add(tRow("DEF_GLOBAL", panel.tuning.GameTuning.DEF_GLOBAL, "Nhan giap toan server."));
        out.add(tRow("CRIT_ADD", panel.tuning.GameTuning.CRIT_ADD, "Cong them % chi mang (0 = giu goc)."));
        out.add(tRow("CRIT_CAP", panel.tuning.GameTuning.CRIT_CAP, "Tran chi mang (mac dinh 110)."));
        out.add(tRow("SDCM_GLOBAL", panel.tuning.GameTuning.SDCM_GLOBAL, "Nhan % sat thuong chi mang."));
        out.add(tRow("TNSM_GLOBAL", panel.tuning.GameTuning.TNSM_GLOBAL, "Nhan TNSM (hien de san, se moc vao tiemNangUp dot sau)."));
        out.add(tRow("POWER_GLOBAL", panel.tuning.GameTuning.POWER_GLOBAL, "Nhan power (hien de san)."));
        out.add(tRow("BOSS_DAME_SCALE", panel.tuning.GameTuning.BOSS_DAME_SCALE, "Nhan rieng dame boss (1.0 = giu goc)."));
        out.add(tRow("BOSS_HP_SCALE", panel.tuning.GameTuning.BOSS_HP_SCALE, "Nhan rieng HP boss."));
        out.add(tRow("DROP_RATE", panel.tuning.GameTuning.DROP_RATE, "Nhan ti le roi do chung (de san cho dot farm)."));
        out.add(tRow("EVENT_POINT_RATE", panel.tuning.GameTuning.EVENT_POINT_RATE, "Nhan diem su kien chung (de san)."));
        out.add(tRow("DAME_SOFT_CAP", panel.tuning.GameTuning.DAME_SOFT_CAP, "Nguong dame ao (30 ty). Vuot nguong chi +20% -> leo tu tu, khong dot bien."));
        out.add(tRow("HP_SOFT_CAP", panel.tuning.GameTuning.HP_SOFT_CAP, "Nguong HP (30k ty). Vuot nguong chi +20%."));
        out.add(tRow("NE_DON_RATE", panel.tuning.GameTuning.NE_DON_RATE, "Nhan ti le ne don (de san)."));
        out.add(tRow("SUB_SD_RATE", panel.tuning.GameTuning.SUB_SD_RATE, "Nhan giam dame nhan (de san)."));
        out.add(tRow("HUT_HP_RATE", panel.tuning.GameTuning.HUT_HP_RATE, "Nhan hut HP (de san)."));
        out.add(tRow("EXP_RATE", panel.tuning.GameTuning.EXP_RATE, "Nhan EXP (de san)."));
        out.add(tRow("GOLD_RATE", panel.tuning.GameTuning.GOLD_RATE, "Nhan vang (de san)."));
        return out;
    }

    private static Map<String, Object> tRow(String k, double v, String note) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("key", k); m.put("value", v); m.put("note", note);
        return m;
    }

    public static String saveGameTuning(Map<String, Double> vals) {
        try {
            for (Map.Entry<String, Double> e : vals.entrySet()) {
                double v = e.getValue();
                switch (e.getKey()) {
                    case "DAME_GLOBAL": panel.tuning.GameTuning.DAME_GLOBAL = v; break;
                    case "HP_GLOBAL": panel.tuning.GameTuning.HP_GLOBAL = v; break;
                    case "MP_GLOBAL": panel.tuning.GameTuning.MP_GLOBAL = v; break;
                    case "DEF_GLOBAL": panel.tuning.GameTuning.DEF_GLOBAL = v; break;
                    case "CRIT_ADD": panel.tuning.GameTuning.CRIT_ADD = v; break;
                    case "CRIT_CAP": panel.tuning.GameTuning.CRIT_CAP = v; break;
                    case "SDCM_GLOBAL": panel.tuning.GameTuning.SDCM_GLOBAL = v; break;
                    case "TNSM_GLOBAL": panel.tuning.GameTuning.TNSM_GLOBAL = v; break;
                    case "POWER_GLOBAL": panel.tuning.GameTuning.POWER_GLOBAL = v; break;
                    case "BOSS_DAME_SCALE": panel.tuning.GameTuning.BOSS_DAME_SCALE = v; break;
                    case "BOSS_HP_SCALE": panel.tuning.GameTuning.BOSS_HP_SCALE = v; break;
                    case "DROP_RATE": panel.tuning.GameTuning.DROP_RATE = v; break;
                    case "EVENT_POINT_RATE": panel.tuning.GameTuning.EVENT_POINT_RATE = v; break;
                    case "DAME_SOFT_CAP": panel.tuning.GameTuning.DAME_SOFT_CAP = v; break;
                    case "HP_SOFT_CAP": panel.tuning.GameTuning.HP_SOFT_CAP = v; break;
                    case "NE_DON_RATE": panel.tuning.GameTuning.NE_DON_RATE = v; break;
                    case "SUB_SD_RATE": panel.tuning.GameTuning.SUB_SD_RATE = v; break;
                    case "HUT_HP_RATE": panel.tuning.GameTuning.HUT_HP_RATE = v; break;
                    case "EXP_RATE": panel.tuning.GameTuning.EXP_RATE = v; break;
                    case "GOLD_RATE": panel.tuning.GameTuning.GOLD_RATE = v; break;
                }
            }
            panel.tuning.GameTuning.save();
            return "OK da luu cong thuc (co tac dung ngay cho lan tinh chi so sau, khong can build lai code)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT: CHI SO PHAN TANG (StatRate hien thi / thuc te) ============
    public static List<Map<String, Object>> listStatRate() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            panel.tuning.StatRateTuning.load();
            Map<String, Double> disp = panel.tuning.StatRateTuning.allDisplay();
            Map<String, Double> real = panel.tuning.StatRateTuning.allReal();
            java.util.Set<String> keys = new java.util.TreeSet<>();
            try { keys.addAll(disp.keySet()); } catch (Exception e) {}
            try { keys.addAll(real.keySet()); } catch (Exception e) {}
            for (String k : keys) {
                try {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("key", k);
                    m.put("display", disp.getOrDefault(k, 0.0));
                    m.put("real", real.getOrDefault(k, 0.0));
                    m.put("note", statRateNote(k));
                    out.add(m);
                } catch (Exception e) {}
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    private static String statRateNote(String k) {
        try {
            if (k == null) return "";
            if (k.startsWith("star")) return "Sao cuong hoa (display=so hien, real=so thuc).";
            if (k.startsWith("vipTnsmAll")) return "TNSM bonus cho MOI VIP (%).";
            if (k.startsWith("vipTnsm")) return "Tang TNSM (%) theo cap VIP (7-13 mac dinh 0).";
            if (k.matches("vip\\d+")) return "Tang HP/KI/SD/Dame (%) theo cap VIP (7-13 mac dinh 0).";
            if (k.startsWith("set")) return "Set do.";
            if (k.startsWith("opt")) return "Option factor: display=so hien, real=so thuc (real()=REAL*GLOBAL_FACTOR).";
            if (k.startsWith("pet")) return "Pet.";
            if (k.startsWith("lyruou")) return "Ly ruou.";
            if (k.contains("thiendo")) return "Thien dao (he so %).";
            if (k.contains("chuyensinh")) return "Chuyen sinh.";
            if (k.contains("capPb")) return "Cap Phong Ba.";
            if (k.contains("kethon")) return "Da/Duoc Ke Thon.";
            if (k.contains("clan")) return "Clan per level.";
            if (k.contains("cuongno") || k.contains("saoDen") || k.contains("bohuyet") || k.contains("bokhi") || k.contains("banhga")) return "Vat pham buff.";
            if (k.contains("bachho") || k.contains("chutuoc") || k.contains("thanhlong")) return "Linh thu per level.";
            return "real() = REAL * GLOBAL_FACTOR.";
        } catch (Exception e) { return ""; }
    }

    public static String saveStatRate(Map<String, double[]> vals) {
        try {
            if (vals != null) {
                for (Map.Entry<String, double[]> e : vals.entrySet()) {
                    try {
                        double[] v = e.getValue();
                        if (v == null || v.length < 2) continue;
                        panel.tuning.StatRateTuning.setPair(e.getKey(), v[0], v[1]);
                    } catch (Exception ex) {}
                }
            }
            panel.tuning.StatRateTuning.save();
            return "OK da luu chi so phan tang (co tac dung ngay)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT: OUTPUT (dame/hp/ki dau ra + he so chung) ============
    public static List<Map<String, Object>> listOutput() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            panel.tuning.OutputTuning.load();
            panel.tuning.StatRateTuning.load();
            out.add(tRow("PLAYER_OUT_RATE", panel.tuning.OutputTuning.PLAYER_OUT_RATE, "Nhan dame dau ra cua nguoi choi (1.0 = giu goc). Chi ap khi ben tan cong la player."));
            out.add(tRow("HP_OUT_RATE", panel.tuning.OutputTuning.HP_OUT_RATE, "Nhan HP dau ra."));
            out.add(tRow("KI_OUT_RATE", panel.tuning.OutputTuning.KI_OUT_RATE, "Nhan KI dau ra."));
            out.add(tRow("GLOBAL_FACTOR", panel.tuning.StatRateTuning.GLOBAL_FACTOR, "He so chung nhan vao real() = REAL * GLOBAL_FACTOR."));
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String saveOutput(Map<String, Double> vals) {
        try {
            if (vals != null) {
                for (Map.Entry<String, Double> e : vals.entrySet()) {
                    double v = e.getValue();
                    switch (e.getKey()) {
                        case "PLAYER_OUT_RATE": panel.tuning.OutputTuning.PLAYER_OUT_RATE = v; break;
                        case "HP_OUT_RATE": panel.tuning.OutputTuning.HP_OUT_RATE = v; break;
                        case "KI_OUT_RATE": panel.tuning.OutputTuning.KI_OUT_RATE = v; break;
                        case "GLOBAL_FACTOR": panel.tuning.StatRateTuning.GLOBAL_FACTOR = v; break;
                    }
                }
            }
            panel.tuning.OutputTuning.save();
            panel.tuning.StatRateTuning.save();
            return "OK da luu output (ap dung nong, khong can restart)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ DOT: CRIT / GIAP / NE ============
    public static List<Map<String, Object>> listCrit() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            panel.tuning.CritTuning.load();
            out.add(tRow("CRIT_MULT", panel.tuning.CritTuning.CRIT_MULT, "He so nhan dame khi chi mang (mac dinh 2.0)."));
            out.add(tRow("SDCM_ADD", panel.tuning.CritTuning.SDCM_ADD, "Cong them % sat thuong chi mang."));
            out.add(tRow("VARIANCE_PCT", panel.tuning.CritTuning.VARIANCE_PCT, "Bien thien dame % (+/-)."));
            out.add(tRow("CRIT_CAP", panel.tuning.CritTuning.CRIT_CAP, "Tran chi mang."));
            out.add(tRow("CRIT_ADD", panel.tuning.CritTuning.CRIT_ADD, "Cong them % chi mang."));
            out.add(tRow("THIENDAO70_ON", panel.tuning.CritTuning.THIENDAO70_ON ? 1 : 0, "1=bat, 0=tat thien dao 70."));
            out.add(tRow("THIENDAO100_LH_ON", panel.tuning.CritTuning.THIENDAO100_LH_ON ? 1 : 0, "1=bat, 0=tat thien dao 100 LH."));
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static String saveCrit(Map<String, Double> vals) {
        try {
            if (vals != null) {
                for (Map.Entry<String, Double> e : vals.entrySet()) {
                    double v = e.getValue();
                    switch (e.getKey()) {
                        case "CRIT_MULT": panel.tuning.CritTuning.CRIT_MULT = v; break;
                        case "SDCM_ADD": panel.tuning.CritTuning.SDCM_ADD = v; break;
                        case "VARIANCE_PCT": panel.tuning.CritTuning.VARIANCE_PCT = v; break;
                        case "CRIT_CAP": panel.tuning.CritTuning.CRIT_CAP = v; break;
                        case "CRIT_ADD": panel.tuning.CritTuning.CRIT_ADD = v; break;
                        case "THIENDAO70_ON": panel.tuning.CritTuning.THIENDAO70_ON = v >= 0.5; break;
                        case "THIENDAO100_LH_ON": panel.tuning.CritTuning.THIENDAO100_LH_ON = v >= 0.5; break;
                    }
                }
            }
            panel.tuning.CritTuning.save();
            return "OK da luu crit/giap/ne (co tac dung ngay)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ PANEL DOT 3: MAP / MOB ============
    public static List<Map<String, Object>> listMapMobOverrides(int mapId) {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM panel_map_mob" + (mapId >= 0 ? " WHERE map_id=?" : " ORDER BY map_id, mob_index"))) {
            if (mapId >= 0) ps.setInt(1, mapId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("map_id", rs.getInt("map_id"));
                    m.put("mob_index", rs.getInt("mob_index"));
                    m.put("mob_temp", rs.getInt("mob_temp"));
                    m.put("level", rs.getInt("level"));
                    m.put("hp", rs.getDouble("hp"));
                    m.put("sd", rs.getInt("sd"));
                    m.put("x", rs.getInt("x"));
                    m.put("y", rs.getInt("y"));
                    m.put("drop_json", rs.getString("drop_json"));
                    m.put("is_new", rs.getInt("is_new"));
                    m.put("note", rs.getString("note"));
                    out.add(m);
                }
            }
        } catch (Exception e) {}
        return out;
    }

    public static String saveMapMobOverride(int id, int mapId, int mobIndex, int mobTemp, int level, double hp, int sd, int x, int y, String dropJson, int isNew, String note) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            if (id <= 0) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO panel_map_mob(map_id,mob_index,mob_temp,level,hp,sd,x,y,drop_json,is_new,note) VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {
                    ps.setInt(1, mapId); ps.setInt(2, mobIndex); ps.setInt(3, mobTemp); ps.setInt(4, level);
                    ps.setDouble(5, hp); ps.setInt(6, sd); ps.setInt(7, x); ps.setInt(8, y);
                    ps.setString(9, dropJson); ps.setInt(10, isNew); ps.setString(11, note);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE panel_map_mob SET map_id=?,mob_index=?,mob_temp=?,level=?,hp=?,sd=?,x=?,y=?,drop_json=?,is_new=?,note=? WHERE id=?")) {
                    ps.setInt(1, mapId); ps.setInt(2, mobIndex); ps.setInt(3, mobTemp); ps.setInt(4, level);
                    ps.setDouble(5, hp); ps.setInt(6, sd); ps.setInt(7, x); ps.setInt(8, y);
                    ps.setString(9, dropJson); ps.setInt(10, isNew); ps.setString(11, note); ps.setInt(12, id);
                    ps.executeUpdate();
                }
            }
            mob.MobOverride.gI().reload();
            return "OK da luu mob map (ap dung nong: goi reload zone hoac restart de ap dung HP/SD moi)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteMapMobOverride(int id) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("DELETE FROM panel_map_mob WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
            mob.MobOverride.gI().reload();
            return "OK da xoa";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /**
     * Danh sach quai THAT cua map: quai goc lay tu map_template (Manager da parse JSON),
     * merge voi override trong panel_map_mob (khop map + mob_temp + mob_index nhu game dung).
     * Cot mo rong: mob_name (ten template), has_ov=1 neu dong do da co override.
     * Dong goc co id=0 (chua co override - Luu se tao moi).
     */
    public static List<Map<String, Object>> listMapMobs(int mapId) {
        List<Map<String, Object>> out = new ArrayList<>();
        // 1) quai goc cua map tu map_template
        try {
            if (server.Manager.MAP_TEMPLATES != null) {
                for (models.Template.MapTemplate t : server.Manager.MAP_TEMPLATES) {
                    if (t == null || t.id != mapId) continue;
                    if (t.mobTemp != null) {
                        for (int j = 0; j < t.mobTemp.length; j++) {
                            Map<String, Object> m = new LinkedHashMap<>();
                            m.put("id", 0);
                            m.put("map_id", mapId);
                            m.put("mob_index", j);
                            m.put("mob_temp", (int) t.mobTemp[j]);
                            m.put("level", t.mobLevel == null ? 0 : (int) t.mobLevel[j]);
                            m.put("hp", t.mobHp == null ? 0d : t.mobHp[j]);
                            m.put("sd", -1);
                            m.put("x", t.mobX == null ? 0 : (int) t.mobX[j]);
                            m.put("y", t.mobY == null ? 0 : (int) t.mobY[j]);
                            m.put("drop_json", "");
                            m.put("is_new", 0);
                            m.put("note", "");
                            m.put("mob_name", mobTemplateName((int) t.mobTemp[j]));
                            m.put("has_ov", 0);
                            out.add(m);
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {}
        // 2) merge override (khop dung 3 khoa ma MobOverride.findOverride dung)
        try {
            for (Map<String, Object> ov : listMapMobOverrides(mapId)) {
                int ovId = ((Number) ov.get("id")).intValue();
                int temp = ((Number) ov.get("mob_temp")).intValue();
                int idx = ((Number) ov.get("mob_index")).intValue();
                int isNew = ((Number) ov.getOrDefault("is_new", 0)).intValue();
                ov.put("mob_name", mobTemplateName(temp));
                ov.put("has_ov", 1);
                boolean merged = false;
                if (isNew == 0) {
                    for (Map<String, Object> row : out) {
                        if (((Number) row.getOrDefault("has_ov", 0)).intValue() != 0) continue;
                        if (((Number) row.get("mob_index")).intValue() != idx) continue;
                        if (((Number) row.get("mob_temp")).intValue() != temp) continue;
                        row.put("id", ovId);
                        row.put("level", ov.get("level"));
                        row.put("hp", ov.get("hp"));
                        row.put("sd", ov.get("sd"));
                        row.put("x", ov.get("x"));
                        row.put("y", ov.get("y"));
                        row.put("drop_json", ov.get("drop_json"));
                        row.put("note", ov.get("note"));
                        merged = true;
                        break;
                    }
                }
                if (!merged) out.add(ov);
            }
        } catch (Exception e) {}
        return out;
    }

    public static String mobTemplateName(int tempId) {
        try {
            models.Template.MobTemplate t = server.Manager.getMobTemplateByTemp(tempId);
            if (t != null && t.name != null && !t.name.trim().isEmpty()) return t.name;
        } catch (Exception e) {}
        return "#" + tempId;
    }

    // ============ PANEL DOT 3: BOSS ============
    public static List<Map<String, Object>> listBossOverrides() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            panel.tuning.BossTuning.load();
            for (Map.Entry<String, panel.tuning.BossTuning.BossOv> e : panel.tuning.BossTuning.all().entrySet()) {
                panel.tuning.BossTuning.BossOv o = e.getValue();
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("field", e.getKey());
                m.put("dame", o.dame);
                m.put("hp", o.hp);
                m.put("rest", o.rest);
                m.put("maps", o.maps);
                m.put("drop_json", panel.tuning.BossTuning.toJson(o.drops));
                m.put("note", o.note);
                out.add(m);
            }
        } catch (Exception e) {}
        return out;
    }

    public static String saveBossOverride(String field, double dame, String hp, int rest, String maps, String dropJson, String note) {
        try {
            String r = panel.tuning.BossTuning.set(field, dame, hp, rest, maps, dropJson, note);
            return r + " (ap dung khi goi lai boss)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String resetBossOverride(String field) {
        try { return panel.tuning.BossTuning.reset(field); }
        catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String spawnBossByAdmin(int bossId, int zoneId) {
        try {
            Boss b = boss.BossManager.gI().spawnBossByAdmin(bossId, zoneId);
            if (b == null) return "That bai: BossID khong ho tro hoac loi (xem boss.BossID)";
            return "OK da goi boss id=" + bossId + " tai khu " + zoneId;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ PANEL DOT 3: SHOP ============
    public static List<Map<String, Object>> listShops() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT s.id,s.npc_id,s.tag_name,s.type_shop,(SELECT COUNT(*) FROM tab_shop t WHERE t.shop_id=s.id) tabs FROM shop s ORDER BY s.npc_id, s.id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("npc_id", rs.getInt("npc_id"));
                    m.put("tag_name", rs.getString("tag_name"));
                    m.put("type_shop", rs.getByte("type_shop"));
                    m.put("tabs", rs.getInt("tabs"));
                    out.add(m);
                }
            }
        } catch (Exception e) {}
        return out;
    }

    public static List<Map<String, Object>> listCurrency() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM panel_shop_currency ORDER BY type_sell")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("type_sell", rs.getByte("type_sell"));
                    m.put("name", rs.getString("name"));
                    m.put("icon", rs.getInt("icon"));
                    out.add(m);
                }
            }
        } catch (Exception e) {}
        return out;
    }

    // ============ PHUC LOI (tab quà online / điểm danh / nạp / coin) ============

    /** Danh sách tab: bảng phuc_loi (id, name, max_tab, id_tab, info_phucloi, action, tich_luy, currency_item). */
    public static List<Map<String, Object>> listPhucLoiTabs() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT id, name, max_tab, id_tab, info_phucloi, action, tich_luy, currency_item FROM phuc_loi ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("name", rs.getString("name"));
                m.put("max_tab", rs.getInt("max_tab"));
                m.put("id_tab", rs.getInt("id_tab"));
                m.put("info_phucloi", rs.getString("info_phucloi"));
                m.put("action", rs.getInt("action"));
                m.put("tich_luy", rs.getString("tich_luy"));
                try { m.put("currency_item", rs.getInt("currency_item")); } catch (Exception e) { m.put("currency_item", 0); }
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Danh sách mốc: bảng phuc_loi_tab (id, tab_id, name, max_count, active, list_item JSON). */
    public static List<Map<String, Object>> listPhucLoiMocs(int tabId) {
        List<Map<String, Object>> out = new ArrayList<>();
        String sql = "SELECT id, tab_id, name, max_count, active, list_item FROM phuc_loi_tab" + (tabId < 0 ? "" : " WHERE tab_id=?") + " ORDER BY id";
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (tabId >= 0) ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("tab_id", rs.getInt("tab_id"));
                    m.put("name", rs.getString("name"));
                    m.put("max_count", rs.getInt("max_count"));
                    m.put("active", rs.getInt("active"));
                    String json = rs.getString("list_item");
                    m.put("list_item", json);
                    m.put("items", parsePhucLoiItems(json));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Parse list_item JSON của phuc_loi_tab: [{"id":15,"quantity":5,"options":[{"id":50,"param":10}]}]. */
    public static List<GiftItemModel> parsePhucLoiItems(String json) {
        List<GiftItemModel> out = new ArrayList<>();
        try {
            Object o = org.json.simple.JSONValue.parse(json == null ? "[]" : json);
            if (o instanceof org.json.simple.JSONArray) {
                for (Object oo : (org.json.simple.JSONArray) o) {
                    if (!(oo instanceof org.json.simple.JSONObject)) continue;
                    org.json.simple.JSONObject jo = (org.json.simple.JSONObject) oo;
                    GiftItemModel m = new GiftItemModel();
                    m.tempId = Integer.parseInt(String.valueOf(jo.get("id")));
                    Object q = jo.get("quantity");
                    try { m.quantity = Integer.parseInt(String.valueOf(q)); } catch (Exception e) { m.quantity = 1; }
                    Object arr = jo.get("options");
                    if (arr instanceof org.json.simple.JSONArray) {
                        for (Object oo2 : (org.json.simple.JSONArray) arr) {
                            if (oo2 instanceof org.json.simple.JSONObject) {
                                org.json.simple.JSONObject op = (org.json.simple.JSONObject) oo2;
                                m.options.add(new GiftItemModel.Opt(
                                        Integer.parseInt(String.valueOf(op.get("id"))),
                                        Long.parseLong(String.valueOf(op.get("param")))));
                            }
                        }
                    }
                    out.add(m);
                }
            }
        } catch (Exception e) {}
        enrichGiftNames(out);
        return out;
    }

    /** Sinh list_item JSON cho phuc_loi_tab (key: id/quantity/options). */
    public static String buildPhucLoiItemsJson(List<GiftItemModel> list) {
        org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
        for (GiftItemModel m : list) {
            org.json.simple.JSONObject o = new org.json.simple.JSONObject();
            o.put("id", m.tempId);
            o.put("quantity", m.quantity);
            org.json.simple.JSONArray ops = new org.json.simple.JSONArray();
            for (GiftItemModel.Opt op : m.options) {
                org.json.simple.JSONObject jo = new org.json.simple.JSONObject();
                jo.put("id", op.id);
                jo.put("param", op.param);
                ops.add(jo);
            }
            o.put("options", ops);
            arr.add(o);
        }
        return arr.toJSONString();
    }

    // ================= SET 5 MON (bang set_config) =================

    /** Danh sach set dang cau hinh (option id + bonus). */
    public static List<Map<String, Object>> listSetConfigs() {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM set_config ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("name", rs.getString("name"));
                m.put("option_id", rs.getInt("option_id"));
                m.put("active", rs.getInt("active"));
                m.put("hp_base", rs.getLong("hp_base"));
                m.put("hp_pct", rs.getInt("hp_pct"));
                m.put("ki_base", rs.getLong("ki_base"));
                m.put("ki_pct", rs.getInt("ki_pct"));
                m.put("dame_base", rs.getLong("dame_base"));
                m.put("dame_pct", rs.getInt("dame_pct"));
                m.put("def_base", rs.getLong("def_base"));
                m.put("def_pct", rs.getInt("def_pct"));
                m.put("crit_add", rs.getInt("crit_add"));
                m.put("crit_dmg_pct", rs.getInt("crit_dmg_pct"));
                m.put("skill_dmg", rs.getString("skill_dmg"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Them/sua set (id<=0 = them moi), tu load lai config ngay sau khi luu. */
    public static String saveSetConfig(int id, String name, int optionId, int active,
            long hpBase, int hpPct, long kiBase, int kiPct, long dameBase, int damePct,
            long defBase, int defPct, int critAdd, int critDmgPct, String skillDmg) {
        try {
            if (id <= 0) {
                jdbc.DBConnecter.executeUpdate("INSERT INTO set_config (name, option_id, active, hp_base, hp_pct, ki_base, ki_pct, dame_base, dame_pct, def_base, def_pct, crit_add, crit_dmg_pct, skill_dmg) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        name, optionId, active, hpBase, hpPct, kiBase, kiPct, dameBase, damePct, defBase, defPct, critAdd, critDmgPct, skillDmg);
            } else {
                jdbc.DBConnecter.executeUpdate("UPDATE set_config SET name=?, option_id=?, active=?, hp_base=?, hp_pct=?, ki_base=?, ki_pct=?, dame_base=?, dame_pct=?, def_base=?, def_pct=?, crit_add=?, crit_dmg_pct=?, skill_dmg=? WHERE id=?",
                        name, optionId, active, hpBase, hpPct, kiBase, kiPct, dameBase, damePct, defBase, defPct, critAdd, critDmgPct, skillDmg, id);
            }
            services.SetConfigService.load();
            return "OK da luu set '" + name + "' (server tu nap lai, chi cong khi du 5/5 mon dau)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteSetConfig(int id) {
        try {
            jdbc.DBConnecter.executeUpdate("DELETE FROM set_config WHERE id=?", id);
            services.SetConfigService.load();
            return "OK da xoa set id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String savePhucLoiTab(int id, String name, int maxTab, int idTab, String info, int action, String tichLuy, int currencyItem) {
        try {
            jdbc.DBConnecter.executeUpdate("UPDATE phuc_loi SET name=?, max_tab=?, id_tab=?, info_phucloi=?, action=?, tich_luy=?, currency_item=? WHERE id=?",
                    name, maxTab, idTab, info, action, tichLuy, currencyItem, id);
            services.PhucLoi.gI().reload();
            return "OK da luu tab id=" + id + " (server tu nap lai)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String addPhucLoiTab(int id, String name, int maxTab, int idTab, String info, int action, String tichLuy, int currencyItem) {
        try {
            jdbc.DBConnecter.executeUpdate("INSERT INTO phuc_loi (id, name, max_tab, id_tab, info_phucloi, action, tich_luy, currency_item) VALUES (?,?,?,?,?,?,?,?)",
                    id, name, maxTab, idTab, info, action, tichLuy, currencyItem);
            services.PhucLoi.gI().reload();
            return "OK da them tab id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deletePhucLoiTab(int id) {
        try {
            jdbc.DBConnecter.executeUpdate("DELETE FROM phuc_loi_tab WHERE tab_id=(SELECT id_tab FROM (SELECT id_tab FROM phuc_loi WHERE id=?) x)", id);
            jdbc.DBConnecter.executeUpdate("DELETE FROM phuc_loi WHERE id=?", id);
            services.PhucLoi.gI().reload();
            return "OK da xoa tab id=" + id + " (kem cac moc con)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String savePhucLoiMoc(int id, int tabId, String name, int maxCount, int active, List<GiftItemModel> items) {
        try {
            String json = buildPhucLoiItemsJson(items);
            int n = jdbc.DBConnecter.executeUpdate("UPDATE phuc_loi_tab SET tab_id=?, name=?, max_count=?, active=?, list_item=? WHERE id=?",
                    tabId, name, maxCount, active, json, id);
            if (n == 0) {
                jdbc.DBConnecter.executeUpdate("INSERT INTO phuc_loi_tab (id, tab_id, name, max_count, active, list_item) VALUES (?,?,?,?,?,?)",
                        id, tabId, name, maxCount, active, json);
            }
            services.PhucLoi.gI().reload();
            return "OK da luu moc id=" + id + " (" + items.size() + " mon, server tu nap lai)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deletePhucLoiMoc(int id) {
        try {
            jdbc.DBConnecter.executeUpdate("DELETE FROM phuc_loi_tab WHERE id=?", id);
            services.PhucLoi.gI().reload();
            return "OK da xoa moc id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** ID ke tiep cho moc moi: max(id) + 1 tren toan bo bang, tranh trung voi moc o tab khac. */
    public static int nextPhucLoiMocId() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id),-1)+1 FROM phuc_loi_tab")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // ============ HE THONG (Thien Dao / Dia Dao / Chuyen Sinh / Tu Tien) ============
    /**
     * Danh sach thong so he thong (tang 3): nhom, phan, ma, y nghia, gia tri dang dung, mac dinh.
     * Panel chia 2 tang: chon NHOM -> xem tung PHAN trong nhom.
     */
    public static List<Map<String, Object>> listSystemTuning() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            for (String k : panel.tuning.SystemTuning.keys()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("group", panel.tuning.SystemTuning.group(k));
                m.put("section", panel.tuning.SystemTuning.section(k));
                m.put("key", k);
                m.put("note", panel.tuning.SystemTuning.label(k));
                m.put("value", panel.tuning.SystemTuning.get(k));
                m.put("def", panel.tuning.SystemTuning.def(k));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Danh sach ten nhom he thong (theo thu tu dang ky trong SystemTuning) - tang 1 cua panel. */
    public static List<String> listSystemTuningGroups() {
        List<String> out = new ArrayList<>();
        try {
            for (Map<String, Object> m : listSystemTuning()) {
                String g = String.valueOf(m.get("group"));
                if (!out.contains(g)) out.add(g);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Luu thong so he thong roi ap dung ngay (khong can restart). */
    public static String saveSystemTuning(Map<String, Double> vals) {
        try {
            int n = 0;
            for (Map.Entry<String, Double> e : vals.entrySet()) {
                if (e.getKey() == null || e.getValue() == null) continue;
                panel.tuning.SystemTuning.set(e.getKey(), e.getValue());
                n++;
            }
            return "OK da luu " + n + " thong so he thong (ap dung ngay)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String reloadSystemTuning() {
        try {
            panel.tuning.SystemTuning.load();
            return "OK da nap lai thong so he thong tu file";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ CUNG MENH (NPC Bo Mong - Rung Karin) ============
    /** Cau hinh hien tai (bang cung_menh_config, dong id=1). */
    public static Map<String, Object> cungMenhConfig() {
        Map<String, Object> m = new LinkedHashMap<>();
        services.CungMenhService.Config c = services.CungMenhService.gI().config();
        m.put("max_level", c.maxLevel);
        m.put("hp_percent", c.hpPercent);
        m.put("ki_percent", c.kiPercent);
        m.put("dame_percent", c.damePercent);
        m.put("hp_flat", c.hpFlat);
        m.put("ki_flat", c.kiFlat);
        m.put("dame_flat", c.dameFlat);
        m.put("manh_base", c.manhBase);
        m.put("manh_step", c.manhStep);
        m.put("manh_extra", c.manhExtra);
        m.put("dot_pha_every", c.dotPhaEvery);
        m.put("dot_pha_manh_base", c.dotPhaManhBase);
        m.put("dot_pha_manh_step", c.dotPhaManhStep);
        m.put("dot_pha_ngoc_base", c.dotPhaNgocBase);
        m.put("dot_pha_ngoc_step", c.dotPhaNgocStep);
        return m;
    }

    /** Luu cau hinh Cung Menh roi cho server nap lai ngay. */
    public static String saveCungMenhConfig(int maxLevel, double hpPercent, double kiPercent, double damePercent,
            long hpFlat, long kiFlat, long dameFlat, int manhBase, int manhStep, int manhExtra,
            int dotPhaEvery, int dotPhaManhBase, int dotPhaManhStep, int dotPhaNgocBase, int dotPhaNgocStep) {
        try {
            services.CungMenhService.gI().reload(); // dam bao bang da ton tai
            String sql = "INSERT INTO cung_menh_config (id,max_level,hp_percent,ki_percent,dame_percent,hp_flat,ki_flat,dame_flat,manh_base,manh_step,manh_extra,dot_pha_every,dot_pha_manh_base,dot_pha_manh_step,dot_pha_ngoc_base,dot_pha_ngoc_step) "
                    + "VALUES (1,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE max_level=VALUES(max_level),hp_percent=VALUES(hp_percent),ki_percent=VALUES(ki_percent),dame_percent=VALUES(dame_percent),hp_flat=VALUES(hp_flat),ki_flat=VALUES(ki_flat),dame_flat=VALUES(dame_flat),manh_base=VALUES(manh_base),manh_step=VALUES(manh_step),manh_extra=VALUES(manh_extra),dot_pha_every=VALUES(dot_pha_every),dot_pha_manh_base=VALUES(dot_pha_manh_base),dot_pha_manh_step=VALUES(dot_pha_manh_step),dot_pha_ngoc_base=VALUES(dot_pha_ngoc_base),dot_pha_ngoc_step=VALUES(dot_pha_ngoc_step)";
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Math.max(1, maxLevel));
                ps.setDouble(2, hpPercent);
                ps.setDouble(3, kiPercent);
                ps.setDouble(4, damePercent);
                ps.setLong(5, hpFlat);
                ps.setLong(6, kiFlat);
                ps.setLong(7, dameFlat);
                ps.setInt(8, Math.max(1, manhBase));
                ps.setInt(9, Math.max(1, manhStep));
                ps.setInt(10, manhExtra);
                ps.setInt(11, Math.max(1, dotPhaEvery));
                ps.setInt(12, Math.max(0, dotPhaManhBase));
                ps.setInt(13, dotPhaManhStep);
                ps.setInt(14, Math.max(0, dotPhaNgocBase));
                ps.setInt(15, dotPhaNgocStep);
                ps.executeUpdate();
            }
            services.CungMenhService.gI().reload();
            return "OK da luu cau hinh Cung Menh (toi da " + maxLevel + " cap) - server tu nap lai";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** Danh sach bac: bang cung_menh_option (moc cap -> option duoc cong khi dat cap do). */
    public static List<Map<String, Object>> listCungMenhBac() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            for (services.CungMenhService.Bac b : services.CungMenhService.gI().listBac()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", b.id);
                m.put("level", b.level);
                m.put("option_id", b.optionId);
                m.put("param", b.param);
                m.put("note", b.note);
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    /** Them (id<=0) hoac sua 1 bac. */
    public static String saveCungMenhBac(int id, int level, int optionId, long param, String note) {
        try {
            services.CungMenhService.gI().reload();
            if (level < 1) return "Loi: moc cap phai >= 1";
            if (id > 0) {
                jdbc.DBConnecter.executeUpdate("UPDATE cung_menh_option SET level=?, option_id=?, param=?, note=? WHERE id=?",
                        level, optionId, param, note == null ? "" : note, id);
            } else {
                jdbc.DBConnecter.executeUpdate("INSERT INTO cung_menh_option (level, option_id, param, note) VALUES (?,?,?,?)",
                        level, optionId, param, note == null ? "" : note);
            }
            services.CungMenhService.gI().reload();
            return "OK da luu bac cap " + level + " -> option #" + optionId + " (" + param + ")";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deleteCungMenhBac(int id) {
        try {
            jdbc.DBConnecter.executeUpdate("DELETE FROM cung_menh_option WHERE id=?", id);
            services.CungMenhService.gI().reload();
            return "OK da xoa bac id=" + id;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    /** Nhac server nap lai cau hinh Cung Menh tu DB (dung khi sua bang tay ngoai panel). */
    public static String reloadCungMenh() {
        try {
            services.CungMenhService.gI().reload();
            services.CungMenhService.Config c = services.CungMenhService.gI().config();
            return "OK da reload Cung Menh (toi da " + c.maxLevel + " cap, " + services.CungMenhService.gI().listBac().size() + " bac option)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String createShop(int npcId, String tagName, String name, int typeShop, String itemsJson) {
        try {
            int id = shop.ShopService.createShopFromPanel(npcId, tagName, name, typeShop, itemsJson);
            if (id <= 0) return "That bai tao shop (tag_name trung hoac loi)";
            return "OK da tao shop id=" + id + " (npc " + npcId + ", tag=" + tagName + ")";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String addItemToShop(int shopId, int tabIndex, String itemsJson) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
             PreparedStatement ps = con.prepareStatement("SELECT items FROM tab_shop WHERE shop_id=? AND tab_index=?")) {
            ps.setInt(1, shopId); ps.setInt(2, tabIndex);
            StringBuilder merged = new StringBuilder("[");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String cur = rs.getString("items");
                    if (cur != null && cur.trim().startsWith("[")) merged.append(cur.trim().substring(1));
                }
            }
            String add = itemsJson == null ? "[]" : itemsJson.trim();
            if (add.startsWith("[")) add = add.substring(1);
            if (add.endsWith("]")) add = add.substring(0, add.length() - 1);
            if (merged.length() > 1 && add.length() > 0) merged.append(",");
            merged.append(add);
            merged.append("]");
            try (PreparedStatement ps2 = con.prepareStatement("UPDATE tab_shop SET items=? WHERE shop_id=? AND tab_index=?")) {
                ps2.setString(1, merged.toString()); ps2.setInt(2, shopId); ps2.setInt(3, tabIndex); ps2.executeUpdate();
            }
            shop.ShopService.reloadShops();
            return "OK da them vat pham vao shop";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    // ============ NHAT KY ADMIN (AUDIT) ============
    public static volatile String AUDIT_ACTOR = "panel";
    private static volatile boolean auditTableReady = false;

    private static void ensureAuditTable() {
        if (auditTableReady) return;
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS panel_audit (id INT AUTO_INCREMENT PRIMARY KEY, actor VARCHAR(64) NOT NULL DEFAULT 'panel', action VARCHAR(64) NOT NULL, detail TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")) {
            ps.execute();
            auditTableReady = true;
        } catch (Exception e) { }
    }

    public static void audit(String action, String detail) {
        try {
            ensureAuditTable();
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO panel_audit(actor, action, detail) VALUES(?, ?, ?)")) {
                ps.setString(1, AUDIT_ACTOR == null || AUDIT_ACTOR.trim().isEmpty() ? "panel" : AUDIT_ACTOR.trim());
                ps.setString(2, action);
                ps.setString(3, detail);
                ps.executeUpdate();
            }
        } catch (Exception e) { }
    }

    public static List<Map<String, Object>> listAudit(String search, int limit) {
        ensureAuditTable();
        List<Map<String, Object>> out = new ArrayList<>();
        String sql = "SELECT id, actor, action, detail, created_at FROM panel_audit "
                + (search != null && !search.trim().isEmpty() ? "WHERE action LIKE ? OR detail LIKE ? OR actor LIKE ? " : "")
                + "ORDER BY id DESC LIMIT " + Math.max(1, Math.min(5000, limit));
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(1, "%" + search.trim() + "%");
                ps.setString(2, "%" + search.trim() + "%");
                ps.setString(3, "%" + search.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("time", rs.getString("created_at"));
                    m.put("actor", rs.getString("actor"));
                    m.put("action", rs.getString("action"));
                    m.put("detail", rs.getString("detail"));
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static List<Map<String, Object>> listLoginHistory(int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("SELECT username, last_time_login, last_time_logout, ip_address, server_login FROM account ORDER BY last_time_login DESC LIMIT " + Math.max(1, Math.min(2000, limit)));
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("username", rs.getString("username"));
                m.put("login", rs.getString("last_time_login"));
                m.put("logout", rs.getString("last_time_logout"));
                m.put("ip", rs.getString("ip_address"));
                m.put("server", rs.getInt("server_login"));
                out.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ============ QUAN LY NHAN VAT (online + offline, khong can SQL/JSON) ============
    private static player.Player getPlayerForEdit(String name, boolean[] wasOnline) {
        if (name == null) return null;
        name = name.trim();
        if (name.isEmpty()) return null;
        player.Player p = server.Client.gI().getPlayer(name);
        if (p != null) { wasOnline[0] = true; return p; }
        wasOnline[0] = false;
        return jdbc.daos.NDVSqlFetcher.loadPlayerByName(name);
    }

    private static String genderName(int g) {
        return g == 0 ? "Trai Dat" : g == 1 ? "Namec" : "Xayda";
    }

    public static List<Map<String, Object>> searchPlayers(String q, int limit) {
        List<Map<String, Object>> out = new ArrayList<>();
        String sql = "SELECT p.name, p.gender, p.power, p.data_location, p.account_id, a.username FROM player p LEFT JOIN account a ON a.id = p.account_id "
                + (q != null && !q.trim().isEmpty() ? "WHERE p.name LIKE ? OR a.username LIKE ? " : "")
                + "ORDER BY p.id DESC LIMIT " + Math.max(1, Math.min(2000, limit));
        try (Connection con = jdbc.DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (q != null && !q.trim().isEmpty()) {
                ps.setString(1, "%" + q.trim() + "%");
                ps.setString(2, "%" + q.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("name", name);
                    m.put("username", rs.getString("username"));
                    int gender = rs.getInt("gender");
                    m.put("gender", genderName(gender));
                    m.put("power", rs.getString("power"));
                    int mapId = -1;
                    try {
                        String loc = rs.getString("data_location");
                        if (loc != null) mapId = Integer.parseInt(loc.replace("[", "").trim().split(",")[0].trim());
                    } catch (Exception e) { }
                    m.put("map", mapId);
                    m.put("map_name", mapId >= 0 ? mapNamePublic(mapId) : "?");
                    m.put("online", server.Client.gI().getPlayer(name) != null);
                    out.add(m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    public static Map<String, Object> viewPlayer(String name) {
        boolean[] on = new boolean[1];
        player.Player p = getPlayerForEdit(name, on);
        if (p == null) return null;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("online", on[0]);
        m.put("name", p.name);
        m.put("power", (long) p.nPoint.power);
        m.put("tiemNang", (long) p.nPoint.tiemNang);
        m.put("gold", p.inventory.gold);
        m.put("gem", (long) p.inventory.gem);
        m.put("ruby", (long) p.inventory.ruby);
        m.put("coupon", (long) p.inventory.coupon);
        List<Map<String, Object>> body = new ArrayList<>();
        List<Map<String, Object>> bag = new ArrayList<>();
        collectItems(p.inventory.itemsBody, body);
        collectItems(p.inventory.itemsBag, bag);
        m.put("body", body);
        m.put("bag", bag);
        List<Map<String, Object>> box = new ArrayList<>();
        List<Map<String, Object>> lucky = new ArrayList<>();
        List<Map<String, Object>> daban = new ArrayList<>();
        collectItems(p.inventory.itemsBox, box);
        collectItems(p.inventory.itemsBoxCrackBall, lucky);
        collectItems(p.inventory.itemsDaBan, daban);
        m.put("box", box);
        m.put("lucky", lucky);
        m.put("daban", daban);
        List<Map<String, Object>> skills = new ArrayList<>();
        if (p.playerSkill != null) {
            for (skill.Skill s : p.playerSkill.skills) {
                if (s == null || s.template == null) continue;
                Map<String, Object> sm = new LinkedHashMap<>();
                sm.put("id", (int) s.template.id);
                models.Template.SkillTemplate tp = utils.SkillUtil.findSkillTemplate(s.template.id);
                sm.put("name", tp != null ? tp.name : "?");
                sm.put("point", s.point);
                sm.put("max", tp != null ? tp.maxPoint : 0);
                skills.add(sm);
            }
        }
        m.put("skills", skills);
        return m;
    }

    private static void collectItems(java.util.List<item.Item> items, List<Map<String, Object>> out) {
        if (items == null) return;
        for (int i = 0; i < items.size(); i++) {
            item.Item it = items.get(i);
            if (it == null || it.template == null || !it.isNotNullItem()) continue;
            Map<String, Object> im = new LinkedHashMap<>();
            im.put("slot", i);
            im.put("id", (int) it.template.id);
            im.put("name", it.template.name);
            im.put("qty", it.quantity);
            StringBuilder sb = new StringBuilder();
            if (it.itemOptions != null) {
                for (item.Item.ItemOption o : it.itemOptions) {
                    if (o == null || o.optionTemplate == null) continue;
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(optionDisplay(o.optionTemplate.id)).append("(").append(o.param).append(")");
                }
            }
            im.put("opts", sb.toString());
            try {
                im.put("time", it.createTime <= 0 ? "-" : new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(it.createTime)));
            } catch (Exception e) { im.put("time", "-"); }
            out.add(im);
        }
    }

    public static String saveBasic(String name, double power, double tiemNang, long gold, int gem, int ruby, int coupon) {
        boolean[] on = new boolean[1];
        player.Player p = getPlayerForEdit(name, on);
        if (p == null) return "Khong tim thay player: " + name;
        try {
            p.nPoint.power = Math.max(0, power);
            p.nPoint.tiemNang = Math.max(0, tiemNang);
            p.inventory.gold = Math.max(0, Math.min(gold, 2000000000L));
            p.inventory.gem = Math.max(0, Math.min(gem, 200000000));
            p.inventory.ruby = Math.max(0, Math.min(ruby, 200000000));
            p.inventory.coupon = Math.max(0, coupon);
            try { p.nPoint.calPoint(); } catch (Exception e) { }
            if (on[0]) {
                services.Service.gI().point(p);
                services.PlayerService.gI().sendInfoHpMpMoney(p);
            }
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("saveBasic", name + " power=" + (long) power + " tn=" + (long) tiemNang + " gold=" + gold + " gem=" + gem + " ruby=" + ruby + (on[0] ? " (online)" : " (offline)"));
            return on[0] ? "OK - da luu va gui cho client (online)" : "OK - da luu vao DB (offline)";
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String addPlayerItem(String name, int tempId, int qty, String optStr) {
        boolean[] on = new boolean[1];
        player.Player p = getPlayerForEdit(name, on);
        if (p == null) return "Khong tim thay player: " + name;
        if (qty <= 0) qty = 1;
        item.Item it = services.ItemService.gI().createNewItem((short) tempId, qty);
        if (it == null || it.template == null || it.template.id != tempId) return "temp_id khong ton tai: " + tempId;
        if (optStr != null && !optStr.trim().isEmpty()) {
            for (String pair : optStr.split(",")) {
                String[] kv = pair.trim().split(":");
                if (kv.length == 2) {
                    try { it.itemOptions.add(new item.Item.ItemOption(Integer.parseInt(kv[0].trim()), Long.parseLong(kv[1].trim()))); } catch (Exception ex) { }
                }
            }
        }
        try {
            if (on[0]) {
                if (services.InventoryService.gI().getCountEmptyBag(p) <= 0) return "Het o hanh trang";
                services.InventoryService.gI().addItemBag(p, it, 999999);
                services.InventoryService.gI().sendItemBag(p);
            } else {
                int idx = -1;
                java.util.List<item.Item> bag = p.inventory.itemsBag;
                for (int i = 0; i < bag.size(); i++) {
                    item.Item e = bag.get(i);
                    if (e == null || e.template == null || !e.isNotNullItem()) { idx = i; break; }
                }
                if (idx < 0) return "Het o hanh trang";
                bag.set(idx, it);
            }
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("addPlayerItem", name + " tang " + it.template.name + " x" + qty + (optStr != null && !optStr.trim().isEmpty() ? " opt[" + optStr + "]" : "") + (on[0] ? " (online)" : " (offline)"));
            return "OK da tang " + it.template.name + " x" + qty + (on[0] ? " (online)" : " (offline, luu DB)");
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String removePlayerItem(String name, String where, int slot) {
        boolean[] on = new boolean[1];
        player.Player p = getPlayerForEdit(name, on);
        if (p == null) return "Khong tim thay player: " + name;
        try {
            java.util.List<item.Item> list;
            if ("body".equals(where)) list = p.inventory.itemsBody;
            else if ("box".equals(where)) list = p.inventory.itemsBox;
            else if ("lucky".equals(where)) list = p.inventory.itemsBoxCrackBall;
            else if ("daban".equals(where)) list = p.inventory.itemsDaBan;
            else list = p.inventory.itemsBag;
            if (slot < 0 || slot >= list.size()) return "Slot khong hop le";
            item.Item cur = list.get(slot);
            if (cur == null || cur.template == null || !cur.isNotNullItem()) return "Slot trong";
            String itemName = cur.template.name;
            list.set(slot, services.ItemService.gI().createItemNull());
            if ("body".equals(where)) {
                try { p.nPoint.calPoint(); } catch (Exception e) { }
            }
            if (on[0]) {
                try {
                    if ("body".equals(where)) services.InventoryService.gI().sendItemBody(p);
                    else if ("box".equals(where)) services.InventoryService.gI().sendItemBox(p);
                    else if ("bag".equals(where)) services.InventoryService.gI().sendItemBag(p);
                } catch (Exception e) { }
            }
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("removePlayerItem", name + " xoa " + itemName + " (" + where + " slot " + slot + ")" + (on[0] ? " (online)" : " (offline)"));
            String relag = (on[0] && ("lucky".equals(where) || "daban".equals(where)))
                    ? " - player online dang xem vi tri nay, cho vao lai de thay doi" : "";
            return "OK da xoa " + itemName + relag;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String setPlayerSkillPoint(String name, int tempId, int point) {
        boolean[] on = new boolean[1];
        player.Player p = getPlayerForEdit(name, on);
        if (p == null) return "Khong tim thay player: " + name;
        if (p.playerSkill == null || p.playerSkill.getSkillbyId(tempId) == null) return "Player khong co skill id " + tempId;
        try {
            models.Template.SkillTemplate tp = utils.SkillUtil.findSkillTemplate(tempId);
            int max = tp != null ? tp.maxPoint : 9;
            if (max < 1) max = 1;
            int cap = Math.max(0, Math.min(point, max));
            skill.Skill ns;
            if (cap <= 0) {
                ns = utils.SkillUtil.createSkillLevel0(tempId);
            } else {
                ns = utils.SkillUtil.createSkill(tempId, cap);
                if (ns == null) return "Cap khong hop le: " + cap;
                ns.lastTimeUseThisSkill = p.playerSkill.getSkillbyId(tempId).lastTimeUseThisSkill;
            }
            utils.SkillUtil.setSkill(p, ns);
            if (p.playerSkill.skillSelect != null && p.playerSkill.skillSelect.template.id == tempId) {
                p.playerSkill.skillSelect = p.playerSkill.getSkillbyId(tempId);
            }
            if (on[0]) {
                try { services.Service.gI().sendSkill(p); } catch (Exception e) { }
            }
            jdbc.daos.PlayerDAO.updatePlayer(p);
            audit("setSkill", name + " skill " + tempId + " -> cap " + cap + "/" + max + (on[0] ? " (online)" : " (offline)"));
            return "OK " + (tp != null ? tp.name : String.valueOf(tempId)) + " cap " + cap + "/" + max + (on[0] ? " (online)" : " (offline)");
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

    public static String deletePlayerRow(String name) {
        name = name == null ? "" : name.trim();
        if (name.isEmpty()) return "Nhap ten player";
        if (server.Client.gI().getPlayer(name) != null) return "Player dang online - kick truoc khi xoa";
        try {
            jdbc.DBConnecter.executeUpdate("DELETE FROM player WHERE name = ?", name);
            audit("deletePlayer", name);
            return "OK da xoa nhan vat " + name;
        } catch (Exception e) { return "Loi: " + e.getMessage(); }
    }

}
