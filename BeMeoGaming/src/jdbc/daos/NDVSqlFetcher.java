package jdbc.daos;

import models.Card.OptionCard;
import models.Card.Card;
import jdbc.DBConnecter;
import jdbc.NDVResultSet;
import consts.ConstPlayer;
import data.DataGame;
import clan.Clan;
import clan.ClanMember;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import Mail.Thu;
import item.Item;
import item.ItemTime;
import npc.specialnpc.MabuEgg;
import npc.specialnpc.MagicTree;
import player.Enemy;
import player.Friend;
import player.Fusion;
import player.Pet;
import player.Player;
import player.badges.BadgesData;
import player.dailyGift.DailyGiftData;
import player.dailyGift.DailyGiftService;
import skill.Skill;
import task.Badges.BadgesTask;
import task.Badges.BadgesTaskService;
import task.TaskMain;
import server.Client;
import server.Manager;
import server.io.MySession;
import models.AntiLogin;
import services.ClanService;
import services.IntrinsicService;
import services.InventoryService;
import services.ItemService;
import services.MapService;
import services.Service;
import services.TaskService;
import utils.Logger;
import utils.SkillUtil;
import utils.TimeUtil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Template.AchievementQuest;

import utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import services.PlayerService;

public class NDVSqlFetcher {

    public static Player login(MySession session, AntiLogin al) {
        Player player = null;
        NDVResultSet rs = null;
        Player plInGame;
        try {
            rs = DBConnecter.executeQuery("select * from account where username = ?", session.uu);
            if (rs.next()) {
                if (!checkAndMigratePassword(session, al, rs.getString("password"), rs.getInt("account.id"))) {
                    return player;
                }
                session.userId = rs.getInt("account.id");
                session.isAdmin = rs.getBoolean("is_admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.actived = rs.getBoolean("active");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.luotquay = rs.getInt("account.luotquay");
                session.gold = rs.getLong("account.vang");
                session.eventPoint = rs.getInt("account.event_point");
                session.bdPlayer = rs.getDouble("account.bd_player");

                int tmp_vnd = rs.getInt("temp_vnd");
                tmp_vnd = Math.max(0, tmp_vnd);
                session.vnd = rs.getInt("vnd") + tmp_vnd;

                session.tongnap = rs.getInt("tongnap");
                session.vip = rs.getInt("vip");
                session.lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                long lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                int secondsPass1 = (int) ((System.currentTimeMillis() - lastTimeLogin) / 1000);
                long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);
                long createTime = rs.getTimestamp("create_time").getTime();
                int deltaTime = (int) ((System.currentTimeMillis() - createTime) / 1000);

                if (rs.getBoolean("ban")) {
                    dlog("[LOGIN] " + session.uu + " -> bi ban, gui OK cho client");
                    Service.gI().sendThongBaoOK(session, "Còn 8 giờ nữa open full cày chay");
                } else if (secondsPass1 < Manager.SECOND_WAIT_LOGIN) {
                    dlog("[LOGIN] " + session.uu + " -> vua login trong " + Manager.SECOND_WAIT_LOGIN + "s, gui cho doi");
                    if (secondsPass < secondsPass1) {
                        Service.gI().sendWaitToLogin(session, Manager.SECOND_WAIT_LOGIN - secondsPass);
                        return null;
                    }

                    Service.gI().sendWaitToLogin(session, Manager.SECOND_WAIT_LOGIN - secondsPass1);
                    return null;

                } else if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout && (plInGame = Client.gI().getPlayerByUser(session.userId)) != null) {
                    dlog("[LOGIN] " + session.uu + " -> TAI KHOAN DANG ONLINE (ghost=" + plInGame.name
                            + ") -> kick session ghost + KICK SESSION HIENTAI, guiLoginFail tren session da dong");
                    Client.gI().kickSession(plInGame.getSession());
                    Client.gI().kickSession(session);
                    Service.gI().sendLoginFail(session, true);
                } else {
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN) {
                        dlog("[LOGIN] " + session.uu + " -> vua logout trong " + Manager.SECOND_WAIT_LOGIN + "s, gui cho doi");
                        Service.gI().sendWaitToLogin(session, Manager.SECOND_WAIT_LOGIN - secondsPass);
                    } else {
                        rs = DBConnecter.executeQuery("select * from player where account_id = ? limit 1", session.userId);
                        if (!rs.next()) {
                            dlog("[LOGIN] " + session.uu + " -> chua co nhan vat, switchToCreateChar");
                            //-28 -4 version data game
                            DataGame.sendVersionGame(session);
                            //-31 data item background
                            DataGame.sendDataItemBG(session);
                            Service.gI().switchToCreateChar(session);
                        } else {
                            plInGame = Client.gI().getPlayerByUser(session.userId);
                            if (plInGame != null) {
                                Client.gI().kickSession(plInGame.getSession());
                            }
                            if ((player = loadPlayer(rs, false)) != null) {

                                player.isPlayer = true;
                                player.deltaTime = deltaTime;
                                player.isNewMember = !Util.isTimeDifferenceGreaterThanNDays(createTime, player.actived() ? 5 : 3);
                                DBConnecter.executeUpdate("Update account set temp_vnd = 0 where id = ?", session.userId);
                                DBConnecter.executeUpdate("UPDATE account SET vnd = ? WHERE id = ?", session.vnd, session.userId);
                                DBConnecter.executeUpdate("update account set last_time_login = '" + new Timestamp(System.currentTimeMillis()) + "', ip_address = '" + session.ipAddress + "' where id = " + session.userId);
                                if (player.Saga_VIP >= 1) {
                                    //  Service.getInstance().sendThongBaoBenDuoi("Thánh Nhân VIP " + player.vip + " " + player.name + " đã đăng nhập!");
                                    Service.getInstance().sendThongBaoAllPlayer("|2|Đại Đế VIP " + player.Saga_VIP + "\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
                                }

                                if (player.Saga_VIP == 0) {
                                    // Service.getInstance().sendThongBaoBenDuoi("Kiến Hôi VIP " + player.vip + " " + player.name + " đã đăng nhập!");
                                    Service.getInstance().sendThongBaoAllPlayer("|2|Phế Vật VIP " + player.Saga_VIP + "\n|7|[" + player.name + "]\n|2| Đã đăng nhập!");
                                }
                            }

                        }
                    }
                }
                al.reset();
            } else {
                dlog("[LOGIN] " + session.uu + " -> khong ton tai ten tai khoan");
                Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                Service.gI().sendLoginFail(session, false);
                al.wrong();
            }
        } catch (Exception e) {
            Logger.error(session.uu);
            if (player != null) {
                player.dispose();
                player = null;
            }
            Logger.logException(NDVSqlFetcher.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    /**
     * Kiem tra mat khau (PBKDF2 hash hoac plaintext legacy) va tu bam lai cho tai khoan cu.
     * Tra ve false neu sai mat khau (da gui thong bao + dem loiAntiLogin).
     */
    // Log chuan doan dang nhap
    private static void dlog(String msg) {
        Logger.warning(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: " + msg + "\n");
    }

    private static boolean checkAndMigratePassword(MySession session, AntiLogin al, String storedPass, int accId) {
        boolean ok = utils.PasswordUtil.isHash(storedPass)
                ? utils.PasswordUtil.verify(storedPass, session.pp)
                : storedPass != null && storedPass.equals(session.pp);
        if (!ok) {
            dlog("[LOGIN] " + session.uu + " -> SAI MAT KHAU");
            Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
            Service.gI().sendLoginFail(session, false);
            al.wrong();
            return false;
        }
        if (!utils.PasswordUtil.isHash(storedPass)) {
            try {
                DBConnecter.executeUpdate("update account set password = ? where id = ?",
                        utils.PasswordUtil.hash(session.pp), accId);
            } catch (Exception ex) {
                Logger.logException(NDVSqlFetcher.class, ex);
            }
        }
        return true;
    }

    public static Player loadById(long id) {
        Player player = null;
        NDVResultSet rs = null;
        try {
            rs = DBConnecter.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.next() && (player = loadPlayer(rs, true)) != null) {
                player.isOffline = true;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {
            if (player != null) {
                player.dispose();
                player = null;
            }
            Logger.logException(NDVSqlFetcher.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static Player loadPlayerByName(String name) {
        Player player = null;
        NDVResultSet rs = null;
        try {
            rs = DBConnecter.executeQuery("select * from player where name = ? limit 1", name);
            if (rs.next() && (player = loadPlayer(rs, true)) != null) {
                player.isOffline = true;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {
            if (player != null) {
                player.dispose();
                player = null;
            }
            Logger.logException(NDVSqlFetcher.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    private static Player loadPlayer(NDVResultSet rs, boolean isOffline) throws Exception {
        Player player = null;
        try {
            double plHp;
            double plMp;
            JSONArray dataArray;

            player = new Player();

            //base info
            player.id = rs.getInt("id");
            player.name = rs.getString("name");
            player.head = rs.getShort("head");
            player.gender = rs.getByte("gender");
            player.point_event = rs.getInt("point_phongtap");
            player.diemfam = rs.getInt("diemfam");
            player.sukien = rs.getInt("sukien");
            player.point_sb = rs.getInt("point_sb");
            player.point_vithu = rs.getInt("point_vithu");
            player.isbienhinh = rs.getInt("isbienhinh");
            player.SukienTamBao = rs.getInt("SukienTamBao");
            player.point_bdkb = rs.getInt("point_bdkb");
            player.point_moruong = rs.getInt("point_moruong");
            player.point_vip = rs.getInt("point_vip");
            player.point_dapdo = rs.getInt("point_dapdo");
            player.Saga_VIP = rs.getByte("Saga_VIP");
            // Cung Menh: cap + dot pha (JSON cung_menh)
            try {
                String cungMenhJson = rs.getString("cung_menh");
                player.cungMenhLevel = 0;
                player.cungMenhDotPha = 0;
                if (cungMenhJson != null && !cungMenhJson.isEmpty()) {
                    Object o = org.json.simple.JSONValue.parse(cungMenhJson);
                    if (o instanceof org.json.simple.JSONObject) {
                        org.json.simple.JSONObject jo = (org.json.simple.JSONObject) o;
                        try { player.cungMenhLevel = Integer.parseInt(String.valueOf(jo.get("level"))); } catch (Exception e) {}
                        try { player.cungMenhDotPha = Integer.parseInt(String.valueOf(jo.get("dotPha"))); } catch (Exception e) {}
                        player.cungMenhFixedOldBonus = "1".equals(String.valueOf(jo.get("fixedOld")));
                    }
                }
            } catch (Exception e) {}
            // Ban Nguyen Tinh Cau: cap do loi + tier (JSON ban_nguyen)
            try {
                String banNguyenJson = rs.getString("ban_nguyen");
                player.banNguyenLevel = 0;
                player.banNguyenTier = 0;
                if (banNguyenJson != null && !banNguyenJson.isEmpty()) {
                    Object o = org.json.simple.JSONValue.parse(banNguyenJson);
                    if (o instanceof org.json.simple.JSONObject) {
                        org.json.simple.JSONObject jo = (org.json.simple.JSONObject) o;
                        try { player.banNguyenLevel = Integer.parseInt(String.valueOf(jo.get("level"))); } catch (Exception e) {}
                        try { player.banNguyenTier = Integer.parseInt(String.valueOf(jo.get("tier"))); } catch (Exception e) {}
                    }
                }
            } catch (Exception e) {}
            player.dakethon = rs.getByte("dakethon");
            player.duockethon = rs.getByte("duockethon");
            player.NapDau = rs.getByte("NapDau");
            player.point_cauca = rs.getInt("point_cauca");
            player.tho_nguyen = rs.getLong("tho_nguyen");
            player.point_PassFree = rs.getLong("point_PassFree");
            player.point_PassVIP = rs.getLong("point_PassVIP");
            player.point_MokhoaVip = rs.getLong("point_MokhoaVip");
            player.SoXuMenhDaNhan = rs.getLong("SoXuMenhDaNhan");
             player.SoXuMenhDaNhanvip = rs.getLong("SoXuMenhDaNhanvip");

            if (player.head == -1) {
                switch (player.gender) {
                    case 0 ->
                        player.head = 64;
                    case 1 ->
                        player.head = 9;
                    case 2 ->
                        player.head = 6;
                }
            }
            player.SagaThienDao = rs.getInt("SagaThienDao");
            if (player.SagaThienDao > 2000000000) {
                player.SagaThienDao = 2000000000;
            }

            player.PhapTac_ThienDao = rs.getLong("PhapTac_ThienDao");
            player.SagaDiaDao = rs.getInt("SagaDiaDao");
            if (player.SagaThienDao > 2000000000) {
                player.SagaThienDao = 2000000000;
            }
            player.DLbTamkjll = rs.getLong("DLbTamkjll");
            player.TamkjllCapPb = rs.getInt("TamkjllCapPb");
            if (player.TamkjllCapPb > 500) {
                player.TamkjllCapPb = 500;
            }
            player.TamkjllThomo = rs.getInt("TamkjllThomo");
            player.TamkjllThomoExp = rs.getLong("TamkjllThomoExp");
            player.SagaChuyenSinh = rs.getInt("SagaChuyenSinh");
            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
            int clanId = rs.getInt("clan_id");
            if (clanId != -1) {
                try {
                    Clan clan = ClanService.gI().getClanById(clanId);
                    for (ClanMember cm : clan.getMembers()) {
                        if (cm.id == player.id) {
                            if (!isOffline) {
                                clan.addMemberOnline(player);
                            }
                            player.clan = clan;
                            player.clanMember = cm;
                            break;
                        }
                    }
                } catch (Exception e) {
                    player.clan = null;
                }
            }

            //data kim lượng
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
            if (dataArray.size() >= 4) {
                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
            } else {
                player.inventory.coupon = 0;
            }
            if (dataArray.size() >= 5) {
                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
            } else {
                player.inventory.event = 0;
            }
            dataArray.clear();

            dataArray = (JSONArray) JSONValue.parse(rs.getString("SagaTuTien"));
            player.SagaTuTien[0] = Double.parseDouble(String.valueOf(dataArray.get(0)));
            player.SagaTuTien[1] = Double.parseDouble(String.valueOf(dataArray.get(1)));
            if (player.SagaTuTien[1] > 340) {
                player.SagaTuTien[1] = 0;
            }
            player.SagaTuTien[2] = Double.parseDouble(String.valueOf(dataArray.get(2)));
            if (player.SagaTuTien[2] > 10000) {
                player.SagaTuTien[2] = 0;
            }
            dataArray.clear();

            dataArray = (JSONArray) JSONValue.parse(rs.getString("TamkjllDLDL"));
            player.TamkjllDauLaDaiLuc[0] = Long.parseLong(String.valueOf(dataArray.get(0)));
            player.TamkjllDauLaDaiLuc[1] = Long.parseLong(String.valueOf(dataArray.get(1)));
            player.TamkjllDauLaDaiLuc[2] = Long.parseLong(String.valueOf(dataArray.get(2)));
            player.TamkjllDauLaDaiLuc[3] = Long.parseLong(String.valueOf(dataArray.get(3)));
            player.TamkjllDauLaDaiLuc[4] = Long.parseLong(String.valueOf(dataArray.get(4)));
            player.TamkjllDauLaDaiLuc[5] = Long.parseLong(String.valueOf(dataArray.get(5)));
            player.TamkjllDauLaDaiLuc[6] = Long.parseLong(String.valueOf(dataArray.get(6)));
            player.TamkjllDauLaDaiLuc[7] = Long.parseLong(String.valueOf(dataArray.get(7)));
            player.TamkjllDauLaDaiLuc[8] = Long.parseLong(String.valueOf(dataArray.get(8)));
            player.TamkjllDauLaDaiLuc[9] = Long.parseLong(String.valueOf(dataArray.get(9)));
            player.TamkjllDauLaDaiLuc[10] = Long.parseLong(String.valueOf(dataArray.get(10)));
            player.TamkjllDauLaDaiLuc[11] = Long.parseLong(String.valueOf(dataArray.get(11)));
            player.TamkjllDauLaDaiLuc[12] = Long.parseLong(String.valueOf(dataArray.get(12)));
            player.TamkjllDauLaDaiLuc[13] = Long.parseLong(String.valueOf(dataArray.get(13)));
            player.TamkjllDauLaDaiLuc[14] = Long.parseLong(String.valueOf(dataArray.get(14)));
            player.TamkjllDauLaDaiLuc[15] = Long.parseLong(String.valueOf(dataArray.get(15)));
            player.TamkjllDauLaDaiLuc[16] = Long.parseLong(String.valueOf(dataArray.get(16)));
            player.TamkjllDauLaDaiLuc[17] = Long.parseLong(String.valueOf(dataArray.get(17)));
            player.TamkjllDauLaDaiLuc[18] = Long.parseLong(String.valueOf(dataArray.get(18)));
            player.TamkjllDauLaDaiLuc[19] = Long.parseLong(String.valueOf(dataArray.get(19)));
            player.TamkjllDauLaDaiLuc[20] = Long.parseLong(String.valueOf(dataArray.get(20)));
            dataArray.clear();

            dataArray = (JSONArray) JSONValue.parse(rs.getString("Tamkjll_Pet"));
            player.TamkjllPetGiong = Integer.parseInt(String.valueOf(dataArray.get(0)));
            if (player.TamkjllPetGiong >= 0 && player.TamkjllPetGiong <= 10) {
                player.TamkjllNamePet = String.valueOf(dataArray.get(1));
                player.TamkjllPetHunger = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.TamkjllPetPower = Long.parseLong(String.valueOf(dataArray.get(3)));
                player.TamkjlllastTimeThucan = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.EmBeLv = Integer.parseInt(String.valueOf(dataArray.get(5)));
                if (player.EmBeLv > 10000) {
                    player.EmBeLv = 10000;
                }
                player.EmBeEXP = Long.parseLong(String.valueOf(dataArray.get(6)));
            } else {
                player.TamkjllPetGiong = -1;
            }
            dataArray.clear();
            dataArray = (JSONArray) JSONValue.parse(rs.getString("Tamkjll_Tu_Ma"));
            if (!dataArray.isEmpty()) {
                player.Tamkjll_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.Tamkjll_Exp_Tu_Ma = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.Tamkjll_Ma_Hoa = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.TamkjllLasttimeMaHoa = Long.parseLong(String.valueOf(dataArray.get(3)));
                player.Tamkjll_Ma_cot = Integer.parseInt(String.valueOf(dataArray.get(4)));
            }
            // data kim lượng
            dataArray.clear();

            //data tọa độ
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_location"));
                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.location.lastTimeplayerMove = System.currentTimeMillis();
                if (mapId == 51 || MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId) || MapService.gI().isMapSieuThanhThuy(mapId) || MapService.gI().isMapMabu2H(mapId)) {
                    mapId = player.gender + 21;
                    player.location.x = 300;
                    player.location.y = 336;
                }
                if (MapService.gI().isMapMaBu(mapId)) {
                    if (!TimeUtil.isMabuOpen()) {
                        mapId = player.gender + 21;
                        player.location.x = 300;
                        player.location.y = 336;
                    }
                }
                if (mapId == 112) {
                    player.location.y = 408;
                } else if (mapId == 129 || mapId == 113) {
                    player.location.y = 360;
                }
                if (mapId == 49) {
                    mapId = 45;
                    player.location.x = 359;
                    player.location.y = 408;
                }

                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
            } catch (Exception e) {
                Logger.logException(NDVSqlFetcher.class, e);
            }
            dataArray.clear();

            //data chỉ số
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
            player.nPoint.power = Double.parseDouble(String.valueOf(dataArray.get(1)));
            player.nPoint.tiemNang = Double.parseDouble(String.valueOf(dataArray.get(2)));
            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
            player.nPoint.hpg = Double.parseDouble(String.valueOf(dataArray.get(5)));
            player.nPoint.mpg = Double.parseDouble(String.valueOf(dataArray.get(6)));
            player.nPoint.dameg = Double.parseDouble(String.valueOf(dataArray.get(7)));
            player.nPoint.defg = Double.parseDouble(String.valueOf(dataArray.get(8)));
            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
            dataArray.get(10); //** Năng động
            plHp = Double.parseDouble(String.valueOf(dataArray.get(11)));
            plMp = Double.parseDouble(String.valueOf(dataArray.get(12)));
            dataArray.clear();

            // Cung Menh: tru 1 lan bonus cu da cong thang vao hpg/mpg/dameg (chi so gio tinh lai tu bang cau hinh)
            try { services.CungMenhService.gI().migrateOldBonus(player); } catch (Exception ex) {}

            //data đậu thần
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_magic_tree"));
            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
            dataArray.clear();

            //Thu - fix chong mat thu offline: null-safe + xoa het han an toan
            try {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<Thu>>() {
                }.getType();
                String homthuStr = null;
                try { homthuStr = rs.getString("homthu"); } catch (Exception ex) { homthuStr = null; }
                if (homthuStr == null || homthuStr.trim().isEmpty() || homthuStr.trim().equals("null")) {
                    player.homThu = new java.util.ArrayList<>();
                } else {
                    try { player.homThu = gson.fromJson(homthuStr, listType); } catch (Exception ex) { player.homThu = new java.util.ArrayList<>(); }
                    if (player.homThu == null) player.homThu = new java.util.ArrayList<>();
                }
            } catch (Exception ex) {
                try { if (player.homThu == null) player.homThu = new java.util.ArrayList<>(); } catch (Exception ex2) {}
            }
            try {
                if (player.homThu != null) {
                    long nowMl = System.currentTimeMillis();
                    long expMl = 15L * 24 * 60 * 60 * 1000;
                    player.homThu.removeIf(m -> m == null || (nowMl - m.time) >= expMl);
                }
            } catch (Exception ex) {}

            //Phuc loi (format: [listNhan, listOnline, listDiemDanh, phutOnline, weekTimeLogin])
            try {
                String phucLoiData = rs.getString("phuc_loi");
                if (phucLoiData != null && !phucLoiData.trim().isEmpty() && !phucLoiData.trim().equals("null")) {
                    Object objPhucLoi = JSONValue.parse(phucLoiData);
                    if (objPhucLoi instanceof JSONArray) {
                        JSONArray arrPhucLoi = (JSONArray) objPhucLoi;
                        if (arrPhucLoi.size() >= 4 && arrPhucLoi.get(0) instanceof JSONArray) {
                            JSONArray arrNhan = (JSONArray) arrPhucLoi.get(0);
                            for (int i = 0; i < arrNhan.size(); i++) {
                                player.listNhan.add(Integer.parseInt(String.valueOf(arrNhan.get(i))));
                            }
                            JSONArray arrOnline = (JSONArray) arrPhucLoi.get(1);
                            for (int i = 0; i < arrOnline.size(); i++) {
                                player.listOnline.add(Integer.parseInt(String.valueOf(arrOnline.get(i))));
                            }
                            JSONArray arrDiemDanh = (JSONArray) arrPhucLoi.get(2);
                            for (int i = 0; i < arrDiemDanh.size(); i++) {
                                player.listDiemDanh.add(Integer.parseInt(String.valueOf(arrDiemDanh.get(i))));
                            }
                            player.phutOnline = Integer.parseInt(String.valueOf(arrPhucLoi.get(3)));
                            if (arrPhucLoi.size() >= 5) {
                                player.weekTimeLogin = new Date(Long.parseLong(String.valueOf(arrPhucLoi.get(4))));
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }

            //Vòng quay (Tầm Bảo): điểm quay + danh sách id mốc đã nhận (cột active_vong_quay)
            try {
                player.diem_quay = rs.getInt("diem_quay");
            } catch (Exception ex) {
                player.diem_quay = 0;
            }
            try {
                player.reset_vong_quay = rs.getInt("reset_vong_quay");
            } catch (Exception ex) {
                player.reset_vong_quay = 0;
            }
            try {
                String vongQuayData = rs.getString("active_vong_quay");
                if (vongQuayData != null && !vongQuayData.trim().isEmpty() && !vongQuayData.trim().equals("null")) {
                    Object objVongQuay = JSONValue.parse(vongQuayData);
                    if (objVongQuay instanceof JSONArray) {
                        JSONArray arrVongQuay = (JSONArray) objVongQuay;
                        for (int i = 0; i < arrVongQuay.size(); i++) {
                            player.listNhan_TamBao.add(Integer.parseInt(String.valueOf(arrVongQuay.get(i))));
                        }
                    }
                }
            } catch (Exception ex) {
            }

            //data phần thưởng sao đen
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_black_ball"));
            JSONArray dataBlackBall;
            for (int i = 0; i < dataArray.size(); i++) {
                dataBlackBall = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                try {
                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                } catch (NumberFormatException e) {
                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;
                }
                dataBlackBall.clear();
            }
            dataArray.clear();

            //data body
            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item;
                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBody.add(item);
            }
//            if (player.inventory.itemsBody.size() == 10) {
//                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
//            }
//            if (player.inventory.itemsBody.size() == 11) {
//                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
//            }
            if (player.inventory.itemsBody.size() == 16) {
                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
            }
            dataArray.clear();

            //data bag
            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item;
                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Long.parseLong(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBag.add(item);
            }
            dataArray.clear();

            //data box
            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item;
                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (item.template.id == 2132) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date currentDate = new Date(item.createTime);
                            Date startDate = formatter.parse("15/03/2024");
                            Date endDate = formatter.parse("28/03/2024");
                            if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                                System.out.println("Thu hồi cải trang rồng lộn bug.");
                                item = ItemService.gI().createItemNull();
                            }
                        } catch (ParseException e) {
                        }
                    }
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBox.add(item);
            }
            dataArray.clear();

            //data box lucky round
            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item;
                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    player.inventory.itemsBoxCrackBall.add(item);
                }
            }
            dataArray.clear();

            //data item da ban
            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_daban"));
            for (int i = 0; i < dataArray.size() && i < 20; i++) {
                Item item;
                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    // 26/06/2023 - Giảm Ngày Trong Shop
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (item.template.id == 2132) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date currentDate = new Date(item.createTime);
                            Date startDate = formatter.parse("15/03/2024");
                            Date endDate = formatter.parse("28/03/2024");
                            if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                                System.out.println("Thu hồi cải trang rồng lộn bug.");
                                item = ItemService.gI().createItemNull();
                            }
                        } catch (ParseException e) {
                        }
                    }
                    if (!ItemService.gI().isOutOfDateTime(item)) {
                        player.inventory.itemsDaBan.add(item);
                    }
                    //player.inventory.itemsDaBan.add(item);
                }
            }
            dataArray.clear();

            //data friends
            dataArray = (JSONArray) JSONValue.parse(rs.getString("friends"));
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    Friend friend = new Friend();
                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                    friend.name = String.valueOf(dataFE.get(1));
                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                    friend.power = Double.parseDouble(String.valueOf(dataFE.get(6)));
                    player.friends.add(friend);
                    dataFE.clear();
                }
                dataArray.clear();
            }

            //data enemies
            dataArray = (JSONArray) JSONValue.parse(rs.getString("enemies"));
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    Enemy enemy = new Enemy();
                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                    enemy.name = String.valueOf(dataFE.get(1));
                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                    enemy.power = Double.parseDouble(String.valueOf(dataFE.get(6)));
                    player.enemies.add(enemy);
                    dataFE.clear();
                }
                dataArray.clear();
            }

            //data nội tại
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_intrinsic"));
            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
            if (dataArray.size() > 4) {
                try {
                    player.effectSkill.isIntrinsic = Boolean.parseBoolean(String.valueOf(dataArray.get(4)));
                    player.effectSkill.skillID = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    player.effectSkill.cooldown = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    player.effectSkill.lastTimeUseSkill = Long.parseLong(String.valueOf(dataArray.get(7)));
                } catch (NumberFormatException e) {
                }
            }
            dataArray.clear();

            //tạo itemtime mới
            dataArray = (JSONArray) JSONValue.parse(rs.getString("item_time_new"));
            int timeItemTest = Integer.parseInt(String.valueOf(dataArray.get(0)));
            int timedanbacdau = Integer.parseInt(String.valueOf(dataArray.get(1)));
            int BINHX3 = Integer.parseInt(String.valueOf(dataArray.get(2)));
            int BINHX5 = Integer.parseInt(String.valueOf(dataArray.get(3)));
            int BINHX7 = Integer.parseInt(String.valueOf(dataArray.get(4)));

            int NUOCMIATO = Integer.parseInt(String.valueOf(dataArray.get(5)));
            int NUOCMIATHOM = Integer.parseInt(String.valueOf(dataArray.get(6)));
            int NUOCMIASAU = Integer.parseInt(String.valueOf(dataArray.get(7)));
            int maydoskh = Integer.parseInt(String.valueOf(dataArray.get(8)));
            int maydoskhvip = Integer.parseInt(String.valueOf(dataArray.get(9)));
            int BINHX10 = Integer.parseInt(String.valueOf(dataArray.get(10)));
            int cnsc = Integer.parseInt(String.valueOf(dataArray.get(11)));
            int bhsc = Integer.parseInt(String.valueOf(dataArray.get(12)));
            int bksc = Integer.parseInt(String.valueOf(dataArray.get(13)));
            int gxsc = Integer.parseInt(String.valueOf(dataArray.get(14)));
            int UpTbx2 = Integer.parseInt(String.valueOf(dataArray.get(15)));
            int UpTbx3 = Integer.parseInt(String.valueOf(dataArray.get(16)));
            int UpTbx4 = Integer.parseInt(String.valueOf(dataArray.get(17)));
            int UpTbx5 = Integer.parseInt(String.valueOf(dataArray.get(18)));
            int UpTbx6 = Integer.parseInt(String.valueOf(dataArray.get(19)));
            int UpTbx7 = Integer.parseInt(String.valueOf(dataArray.get(20)));
            int trungdacbiet = Integer.parseInt(String.valueOf(dataArray.get(21)));
            int trungthu = Integer.parseInt(String.valueOf(dataArray.get(22)));
             int banhtrungthu1trung = Integer.parseInt(String.valueOf(dataArray.get(23)));
            int banhtrungthu2trung = Integer.parseInt(String.valueOf(dataArray.get(24)));
             int banhtrunggaquay = Integer.parseInt(String.valueOf(dataArray.get(25)));             int banhtrungthapcam = Integer.parseInt(String.valueOf(dataArray.get(26)));

            // [The Diem Farm 27/09/2026] slot 27/28/29 - save cu chi co 27 phan tu (0-26)
            // nen phai size-guard, khong la load player cu bi IndexOutOfBounds
            int famX2 = dataArray.size() > 27 ? Integer.parseInt(String.valueOf(dataArray.get(27))) : 0;
            int famX3 = dataArray.size() > 28 ? Integer.parseInt(String.valueOf(dataArray.get(28))) : 0;
            int famX5 = dataArray.size() > 29 ? Integer.parseInt(String.valueOf(dataArray.get(29))) : 0;

            player.itemTime.lastTimeItemTest = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeItemTest);
            player.itemTime.tgianbacdau = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timedanbacdau);
            player.itemTime.lastTimeBINHX3 = System.currentTimeMillis() - (ItemTime.TIME_1H - BINHX3);
            player.itemTime.lastTimeBINHX5 = System.currentTimeMillis() - (ItemTime.TIME_1H - BINHX5);
            player.itemTime.lastTimeBINHX7 = System.currentTimeMillis() - (ItemTime.TIME_1H - BINHX7);

            //
            player.itemTime.lastTimeLYNUOCMIATO = System.currentTimeMillis() - (ItemTime.TIME_ITEM - NUOCMIATO);
            player.itemTime.lastTimeLYNUOCMIATHOM = System.currentTimeMillis() - (ItemTime.TIME_ITEM - NUOCMIATHOM);
            player.itemTime.lastLYNUOCMIASAURIENG = System.currentTimeMillis() - (ItemTime.TIME_ITEM - NUOCMIASAU);
            player.itemTime.lastTimeMaydoskh = System.currentTimeMillis() - (ItemTime.TIME_1H - maydoskh);
            player.itemTime.lastTimeMaydoskhvip = System.currentTimeMillis() - (ItemTime.TIME_1H - maydoskhvip);
            player.itemTime.lastTimeBINHX10 = System.currentTimeMillis() - (ItemTime.TIME_1H - BINHX10);

            player.itemTime.lastTimecnsc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - cnsc);
            player.itemTime.lastTimebhsc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - bhsc);
            player.itemTime.lastTimebksc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - bksc);
            player.itemTime.lastTimegxsc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - gxsc);

            player.itemTime.thoigianUpx2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx2);
            player.itemTime.thoigianUpx3 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx3);
            player.itemTime.thoigianUpx4 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx4);
            player.itemTime.thoigianUpx5 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx5);
            player.itemTime.thoigianUpx6 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx6);
            player.itemTime.thoigianUpx7 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - UpTbx7);
            player.itemTime.lastTimeBanhDacBiet = System.currentTimeMillis() - (ItemTime.TIME_150p - trungdacbiet);
            player.itemTime.lastTimeBanhTrungthu = System.currentTimeMillis() - (ItemTime.TIME_150p - trungthu);
            player.itemTime.lastTimeBanh1Trung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - banhtrungthu1trung);
            player.itemTime.lastTimeBanh2Trung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - banhtrungthu2trung);
             player.itemTime.lastTimegaquay = System.currentTimeMillis() - (ItemTime.TIME_ITEM - banhtrunggaquay);             player.itemTime.lastTimethapcam = System.currentTimeMillis() - (ItemTime.TIME_ITEM - banhtrungthapcam);

            // [The Diem Farm]
            long thoiGianTheFam = item.ItemTime.thoiGianTheFamMs();
            player.itemTime.lastTimeFamX2 = System.currentTimeMillis() - (thoiGianTheFam - famX2);
            player.itemTime.lastTimeFamX3 = System.currentTimeMillis() - (thoiGianTheFam - famX3);
            player.itemTime.lastTimeFamX5 = System.currentTimeMillis() - (thoiGianTheFam - famX5);

            player.itemTime.isItemTest = timeItemTest != 0;
            player.itemTime.isdanbacdau = timedanbacdau != 0;
            player.itemTime.isBINHX3 = BINHX3 != 0;
            player.itemTime.isBINHX5 = BINHX5 != 0;
            player.itemTime.isBINHX7 = BINHX7 != 0;

            //
            player.itemTime.isLYNUOCMIATO = NUOCMIATO != 0;
            player.itemTime.isLYNUOCMIATHOM = NUOCMIATHOM != 0;
            player.itemTime.isLYNUOCMIASAURIENG = NUOCMIASAU != 0;
            player.itemTime.isMAYDOSKH = maydoskh != 0;
            player.itemTime.isMAYDOSKHVIP = maydoskhvip != 0;
            player.itemTime.isBINHX10 = BINHX10 != 0;
            player.itemTime.iscuongnosieucap = cnsc != 0;
            player.itemTime.isbohuyetsieucap = bhsc != 0;
            player.itemTime.isbokhisieucap = bksc != 0;
            player.itemTime.isgiapxensieucap = gxsc != 0;
            player.itemTime.isBanhTrungDacBiet = trungdacbiet != 0;
            player.itemTime.isBanhTrungThu = trungthu != 0;
            player.itemTime.isBanhTrung1Trung = banhtrungthu1trung != 0;
            player.itemTime.isBanhTrung2Trung = banhtrungthu2trung != 0;
             player.itemTime.isBanhgaquay = banhtrunggaquay != 0;
            player.itemTime.isBanhthapcam = banhtrungthapcam != 0;

            // [The Diem Farm]
            player.itemTime.isFamX2 = famX2 != 0;
            player.itemTime.isFamX3 = famX3 != 0;
            player.itemTime.isFamX5 = famX5 != 0;

            player.itemTime.isUpTbx2 = UpTbx2 != 0;
            player.itemTime.isUpTbx3 = UpTbx3 != 0;
            player.itemTime.isUpTbx4 = UpTbx4 != 0;
            player.itemTime.isUpTbx5 = UpTbx5 != 0;
            player.itemTime.isUpTbx6 = UpTbx6 != 0;
            player.itemTime.isUpTbx7 = UpTbx7 != 0;

            dataArray.clear();

            //data item time
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time"));
            int timeUseTDLT = 0;
            int timeOpenPower = 0;
            int timeMayDo = 0;
            int timeMayDo2 = 0;
            int timeMeal = 0;
            int iconMeal = 0;
            int timeUseCMS = 0;
            int timeUseGTPT = 0;
            int timeUseDK = 0;
            int timeUseDuoiKhiTT = 0;
            int timeUseRX = 0;
            int timeMeal2 = 0;
            int iconMeal2 = 0;
            int timeUseNCD = 0;
            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
            int timeBoHuyet2 = Integer.parseInt(String.valueOf(dataArray.get(1)));
            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(2)));
            int timeBoKhi2 = Integer.parseInt(String.valueOf(dataArray.get(3)));
            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(4)));
            int timeGiapXen2 = Integer.parseInt(String.valueOf(dataArray.get(5)));
            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(6)));
            int timeCuongNo2 = Integer.parseInt(String.valueOf(dataArray.get(7)));
            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(8)));
            int timeAnDanh2 = Integer.parseInt(String.valueOf(dataArray.get(9)));
            if (dataArray.size() > 10) {
                timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(10)));
            }
            if (dataArray.size() > 11) {
                timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(11)));
            }
            if (dataArray.size() > 12) {
                timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(12)));
            }
            if (dataArray.size() > 13) {
                timeUseDuoiKhiTT = Integer.parseInt(String.valueOf(dataArray.get(13)));
            }
            if (dataArray.size() > 14) {
                timeMeal = Integer.parseInt(String.valueOf(dataArray.get(14)));
            }
            if (dataArray.size() > 15) {
                iconMeal = Integer.parseInt(String.valueOf(dataArray.get(15)));
            }
            if (dataArray.size() > 16) {
                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(16)));
            }
            if (dataArray.size() > 17) {
                timeUseCMS = Integer.parseInt(String.valueOf(dataArray.get(17)));
            }
            if (dataArray.size() > 18) {
                timeUseGTPT = Integer.parseInt(String.valueOf(dataArray.get(18)));
            }
            if (dataArray.size() > 19) {
                timeUseDK = Integer.parseInt(String.valueOf(dataArray.get(19)));
            }
            if (dataArray.size() > 20) {
                timeUseRX = Integer.parseInt(String.valueOf(dataArray.get(20)));
            }
            if (dataArray.size() > 21) {
                timeMeal2 = Integer.parseInt(String.valueOf(dataArray.get(21)));
            }
            if (dataArray.size() > 22) {
                iconMeal2 = Integer.parseInt(String.valueOf(dataArray.get(22)));
            }
            if (dataArray.size() > 23) {
            }
            if (dataArray.size() > 24) {
                timeUseNCD = Integer.parseInt(String.valueOf(dataArray.get(24)));
            }
            if (dataArray.size() > 25) {
            }
            if (dataArray.size() > 26) {
            }

            player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
            player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
            player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
            player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
            player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
            player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet2);
            player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi2);
            player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen2);
            player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo2);
            player.itemTime.lastTimeAnDanh2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh2);
            player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
            player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
            player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
            player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
            player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
            player.itemTime.lastTimeUseCMS = System.currentTimeMillis() - (ItemTime.TIME_CMS - timeUseCMS);
            player.itemTime.lastTimeUseGTPT = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeUseGTPT);
            player.itemTime.lastTimeUseDK = System.currentTimeMillis() - (ItemTime.TIME_DK - timeUseDK);
            player.itemTime.isDuoiKhiTT = timeUseDuoiKhiTT > 0;
            player.itemTime.lastTimeDuoiKhiTT = System.currentTimeMillis()
                    - (panel.tuning.TrungThuTuning.getLong("drop.tailSeconds", 900) * 1000L - timeUseDuoiKhiTT);
            player.itemTime.timeRX = timeUseRX * 60 * 1000;
            player.itemTime.lastTimeUseRX = System.currentTimeMillis();
            player.itemTime.lastTimeEatMeal2 = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal2);
            player.itemTime.lastTimeUseNCD = System.currentTimeMillis() - (ItemTime.TIME_NCD - timeUseNCD);

            player.itemTime.iconMeal = iconMeal;
            player.itemTime.isEatMeal = timeMeal != 0;
            player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
            player.itemTime.isUseBoKhi = timeBoKhi != 0;
            player.itemTime.isUseGiapXen = timeGiapXen != 0;
            player.itemTime.isUseCuongNo = timeCuongNo != 0;
            player.itemTime.isUseAnDanh = timeAnDanh != 0;
            player.itemTime.isUseBoHuyet2 = timeBoHuyet2 != 0;
            player.itemTime.isUseBoKhi2 = timeBoKhi2 != 0;
            player.itemTime.isUseGiapXen2 = timeGiapXen2 != 0;
            player.itemTime.isUseCuongNo2 = timeCuongNo2 != 0;
            player.itemTime.isUseAnDanh2 = timeAnDanh2 != 0;
            player.itemTime.isOpenPower = timeOpenPower != 0;
            player.itemTime.isUseMayDo = timeMayDo != 0;
            player.itemTime.isUseMayDo2 = timeMayDo2 != 0;
            player.itemTime.isUseTDLT = timeUseTDLT != 0;
            player.itemTime.isUseCMS = timeUseCMS != 0;
            player.itemTime.isUseGTPT = timeUseGTPT != 0;
            player.itemTime.isUseDK = timeUseDK != 0;
            player.itemTime.isUseRX = timeUseRX != 0;
            player.itemTime.iconMeal2 = iconMeal2;
            player.itemTime.isEatMeal2 = timeMeal2 != 0;
            player.itemTime.isUseNCD = timeUseNCD != 0;
            dataArray.clear();

//            dataArray = (JSONArray) JSONValue.parse(rs.getString("Saga_VIP"));
//            player.vip = Byte.parseByte(String.valueOf(dataArray.get(0)));
//            player.timevip = Long.parseLong(String.valueOf(dataArray.get(1)));
//            dataArray.clear();
            //data nhiệm vụ
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_task"));
            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
            if (dataArray.size() > 3) {
                taskMain.lastTime = Long.parseLong(String.valueOf(dataArray.get(3)));
            } else {
                taskMain.lastTime = System.currentTimeMillis();
            }
            player.playerTask.taskMain = taskMain;
            dataArray.clear();

            //data nhiệm vụ hàng ngày
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_side_task"));
            String format = "dd-MM-yyyy";
            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
            Date date = new Date(receivedTime);
            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.playerTask.sideTask.receivedTime = receivedTime;
            }

            //data trứng bư
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_mabu_egg"));
            if (!dataArray.isEmpty()) {
                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))), Long.parseLong(String.valueOf(dataArray.get(1))));
            }
            dataArray.clear();

            dataArray = (JSONArray) JSONValue.parse(rs.getString("diemdanh"));
            player.CheckDayOnl = Byte.parseByte(String.valueOf(dataArray.get(0).toString()));
            player.diemdanh = Long.parseLong(String.valueOf(dataArray.get(1).toString()));
            dataArray.clear();
            if (player.CheckDayOnl == 0) {
                player.CheckDayOnl = (byte) (LocalDate.now().getDayOfMonth());
            }

            //data bùa
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_charm"));
            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
            dataArray.clear();

            //data skill
            dataArray = (JSONArray) JSONValue.parse(rs.getString("skills"));
            for (int i = 0; i < dataArray.size(); i++) {
                JSONArray dataSkill = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                Skill skill;
                if (point != 0) {
                    skill = SkillUtil.createSkill(tempId, point);
                } else {
                    skill = SkillUtil.createSkillLevel0(tempId);
                }
                skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                if (dataSkill.size() > 3) {
                    skill.currLevel = Short.parseShort(String.valueOf(dataSkill.get(3)));
                }
                player.playerSkill.skills.add(skill);
            }
            if (player.gender == 0 && player.playerSkill.getSkillbyId(Skill.SUPER_KAME) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.SUPER_KAME));
            }
            if (player.gender == 1 && player.playerSkill.getSkillbyId(Skill.MA_PHONG_BA) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.MA_PHONG_BA));
            }
            if (player.gender == 2 && player.playerSkill.getSkillbyId(Skill.LIEN_HOAN_CHUONG) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.LIEN_HOAN_CHUONG));
            }
            if (player.playerSkill.getSkillbyId(Skill.SUPER_TRANFORMATION) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.SUPER_TRANFORMATION));
            }
            if (player.playerSkill.getSkillbyId(Skill.EVOLUTION) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.EVOLUTION));
            }
            if (player.playerSkill.getSkillbyId(Skill.PHAN_THAN) == null) {
                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(Skill.PHAN_THAN));
            }
            dataArray.clear();

            //data skill shortcut
            dataArray = (JSONArray) JSONValue.parse(rs.getString("skills_shortcut"));
            for (int i = 0; i < dataArray.size(); i++) {
                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
            }

            for (int i : player.playerSkill.skillShortCut) {
                if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                    break;
                }
            }
            if (player.playerSkill.skillSelect == null) {
                player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
            }
            dataArray.clear();

            //notify
            player.notify = rs.getString("notify");

            //data pet
            JSONArray petData = (JSONArray) JSONValue.parse(rs.getString("pet"));
            if (!petData.isEmpty()) {
                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(0)));
                Pet pet = new Pet(player);
                pet.id = -player.id;
                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                pet.name = String.valueOf(dataArray.get(2));
                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                if (player.fusion.typeFusion > 6) {
                    player.fusion.typeFusion /= 2;
                }
                player.fusion.lastTimeFusion = System.currentTimeMillis() - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(1)));
                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                pet.nPoint.power = Double.parseDouble(String.valueOf(dataArray.get(1)));
                pet.nPoint.tiemNang = Double.parseDouble(String.valueOf(dataArray.get(2)));
                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                pet.nPoint.hpg = Double.parseDouble(String.valueOf(dataArray.get(5)));
                pet.nPoint.mpg = Double.parseDouble(String.valueOf(dataArray.get(6)));
                pet.nPoint.dameg = Double.parseDouble(String.valueOf(dataArray.get(7)));
                pet.nPoint.defg = Double.parseDouble(String.valueOf(dataArray.get(8)));
                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                double hp = Double.parseDouble(String.valueOf(dataArray.get(10)));
                double mp = Double.parseDouble(String.valueOf(dataArray.get(11)));

                //data body
                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(2)));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))), Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (item.template.id == 2132) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                            try {
                                Date currentDate = new Date(item.createTime);
                                Date startDate = formatter.parse("15/03/2024");
                                Date endDate = formatter.parse("28/03/2024");
                                if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                                    System.out.println("Thu hồi cải trang rồng lộn bug.");
                                    item = ItemService.gI().createItemNull();
                                }
                            } catch (ParseException e) {
                            }
                        }
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    pet.inventory.itemsBody.add(item);
                }
                while (pet.inventory.itemsBody.size() < 7) {
                    Item item = ItemService.gI().createItemNull();
                    pet.inventory.itemsBody.add(item);
                }

                //data skills
                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(3)));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray skillTemp = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                    Skill skill;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    if (skillTemp.size() > 3) {
                        skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(skillTemp.get(2)));
                    }
                    if (skillTemp.size() > 3) {
                        skill.currLevel = Short.parseShort(String.valueOf(skillTemp.get(3)));
                    }
                    switch (skill.template.id) {
                        case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC ->
                            skill.coolDown = 1000;
                    }
                    pet.playerSkill.skills.add(skill);
                }
                pet.nPoint.hp = hp;
                pet.nPoint.mp = mp;
                player.pet = pet;
            }

            //Data bảo vệ tài khoản
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("baovetaikhoan"));
                player.mbv = Integer.parseInt(dataArray.get(0).toString());
                player.baovetaikhoan = Boolean.parseBoolean(dataArray.get(1).toString());
                player.mbvtime = Long.parseLong(dataArray.get(2).toString());
            } catch (Exception e) {
                player.mbv = 0;
                player.baovetaikhoan = false;
                player.mbvtime = System.currentTimeMillis();
            }

            // data rada card
            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_card"));
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject obj = (JSONObject) dataArray.get(i);
                player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()), Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()), Byte.parseByte(obj.get("level").toString()), loadOptionCard((JSONArray) JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
            }
            dataArray.clear();

            //data PK Commeson
            player.lastPkCommesonTime = rs.getLong("lasttimepkcommeson");

            //Data BDKB
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("bandokhobau"));
                player.timesPerDayBDKB = Integer.parseInt(dataArray.get(0).toString());
                player.lastTimeJoinBDKB = Long.parseLong(dataArray.get(1).toString());
            } catch (Exception e) {
                player.timesPerDayBDKB = 0;
                player.lastTimeJoinBDKB = System.currentTimeMillis();
            }

            //Data doanh trại
            player.lastTimeJoinDT = rs.getLong("doanhtrai");

            //Data CDRD
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("conduongrandoc"));
                player.joinCDRD = Boolean.parseBoolean(dataArray.get(0).toString());
                player.lastTimeJoinCDRD = Long.parseLong(dataArray.get(1).toString());
                player.talkToThuongDe = Boolean.parseBoolean(dataArray.get(2).toString());
                player.talkToThanMeo = Boolean.parseBoolean(dataArray.get(2).toString());
                if (player.clan.ConDuongRanDoc == null || player.lastTimeJoinCDRD != player.clan.lastTimeOpenConDuongRanDoc) {
                    player.joinCDRD = false;
                    player.talkToThuongDe = false;
                    player.talkToThanMeo = false;
                }
            } catch (Exception e) {
                player.joinCDRD = false;
                player.lastTimeJoinCDRD = 0;
                player.talkToThuongDe = false;
                player.talkToThanMeo = false;
            }

            // Sư phụ không tấn công
            try {
                player.doesNotAttack = rs.getBoolean("masterDoesAttack");
                player.lastTimePlayerNotAttack = System.currentTimeMillis();
            } catch (Exception e) {
                player.doesNotAttack = false;
                player.lastTimePlayerNotAttack = System.currentTimeMillis();
            }

            //data Nhận Thỏi Vàng
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("nhanthoivang"));
                player.danhanthoivang = Boolean.parseBoolean(dataArray.get(0).toString());
                player.lastRewardGoldBarTime = Long.parseLong(dataArray.get(1).toString());
            } catch (Exception e) {
                player.danhanthoivang = false;
                player.lastRewardGoldBarTime = 0;
            }

            //data Rương gỗ
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("ruonggo"));
                player.levelWoodChest = Integer.parseInt(dataArray.get(0).toString());
                player.goldChallenge = Long.parseLong(dataArray.get(1).toString());
                player.rubyChallenge = Long.parseLong(dataArray.get(2).toString());
                player.lastTimeRewardWoodChest = Long.parseLong(dataArray.get(3).toString());
                player.lastTimePKDHVT23 = Long.parseLong(dataArray.get(4).toString());
            } catch (Exception e) {
                player.levelWoodChest = 0;
                player.goldChallenge = 50000000;
                player.rubyChallenge = 100;
                player.lastTimeRewardWoodChest = System.currentTimeMillis();
                player.lastTimePKDHVT23 = 0;
            }

            //data Siêu Thần Thủy
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("sieuthanthuy"));
                player.winSTT = Boolean.parseBoolean(dataArray.get(0).toString());
                player.lastTimeWinSTT = Long.parseLong(dataArray.get(1).toString());
                player.callBossPocolo = Boolean.parseBoolean(dataArray.get(2).toString());
            } catch (Exception e) {
            }

            //data Võ đài sinh tử
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("vodaisinhtu"));
                player.haveRewardVDST = Boolean.parseBoolean(dataArray.get(0).toString());
                player.thoiVangVoDaiSinhTu = Integer.parseInt(dataArray.get(1).toString());
                player.lastTimePKVoDaiSinhTu = Long.parseLong(dataArray.get(2).toString());
                player.timePKVDST = Long.parseLong(dataArray.get(3).toString());
            } catch (Exception e) {
            }

            //Thời gian gọi rồng
            player.lastTimeShenronAppeared = rs.getLong("rongxuong");

            //Thời gian gọi rồng
            //     player.diemfam = rs.getLong("diemfam"); 
            //data item event
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_event"));
                player.itemEvent.remainingTVGSCount = Integer.parseInt(dataArray.get(0).toString());
                player.itemEvent.lastTVGSTime = Long.parseLong(dataArray.get(1).toString());
                player.itemEvent.remainingHHCount = Integer.parseInt(dataArray.get(2).toString());
                player.itemEvent.lastHHTime = Long.parseLong(dataArray.get(3).toString());
                player.itemEvent.remainingBNCount = Integer.parseInt(dataArray.get(4).toString());
                player.itemEvent.lastBNTime = Long.parseLong(dataArray.get(5).toString());
            } catch (Exception e) {
                player.itemEvent.remainingTVGSCount = 0;
                player.itemEvent.lastTVGSTime = 0;
                player.itemEvent.remainingHHCount = 0;
                player.itemEvent.lastHHTime = 0;
                player.itemEvent.remainingBNCount = 0;
                player.itemEvent.lastBNTime = 0;
            }

            //data luyện tập
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_luyentap"));
                player.levelLuyenTap = Integer.parseInt(dataArray.get(0).toString());
                player.dangKyTapTuDong = Boolean.parseBoolean(dataArray.get(1).toString());
                player.mapIdDangTapTuDong = Integer.parseInt(dataArray.get(2).toString());
                player.tnsmLuyenTap = Integer.parseInt(dataArray.get(3).toString());
                player.lastTimeOffline = Long.parseLong(dataArray.get(4).toString());
                if (dataArray.size() > 5) {
                    player.traning.setTop(Integer.parseInt(dataArray.get(5).toString()));
                    player.traning.setTime(Integer.parseInt(dataArray.get(6).toString()));
                    player.traning.setLastTime(Long.parseLong(dataArray.get(7).toString()));
                    player.traning.setLastTop(Integer.parseInt(dataArray.get(8).toString()));
                    player.traning.setLastRewardTime(Long.parseLong(dataArray.get(9).toString()));
                }
            } catch (Exception e) {
                player.levelLuyenTap = 0;
                player.dangKyTapTuDong = false;
                player.mapIdDangTapTuDong = -1;
                player.tnsmLuyenTap = 0;
                player.lastTimeOffline = System.currentTimeMillis();
            }

            //data nhiệm vụ bang hàng ngày
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_clan_task"));
                format = "dd-MM-yyyy";
                receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                date = new Date(receivedTime);
                if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                    player.playerTask.clanTask.template = TaskService.gI().getClanTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                    player.playerTask.clanTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.playerTask.clanTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.playerTask.clanTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    player.playerTask.clanTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    player.playerTask.clanTask.receivedTime = receivedTime;
                }
            } catch (Exception e) {
            }

            //data vip
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_vip"));
                player.timesPerDayCuuSat = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.lastTimeCuuSat = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nhanDeTuNangVIP = Boolean.parseBoolean(String.valueOf(dataArray.get(2)));
                player.nhanVangNangVIP = Boolean.parseBoolean(String.valueOf(dataArray.get(3)));
                if (dataArray.size() > 4) {
                    player.nhanSKHVIP = Boolean.parseBoolean(String.valueOf(dataArray.get(4)));
                }
            } catch (Exception e) {
            }

            //data super rank
            player.superRank.rank = Integer.parseInt(rs.getString("rank"));
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_super_rank"));
                player.superRank.lastTimePK = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.superRank.lastTimeReward = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.superRank.ticket = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.superRank.win = Integer.parseInt(String.valueOf(dataArray.get(3)));
                player.superRank.lose = Integer.parseInt(String.valueOf(dataArray.get(4)));
                JsonObject jsonObject = new Gson().fromJson(String.valueOf(dataArray.get(5)), JsonObject.class);
                JsonArray history = jsonObject.getAsJsonArray("history");
                JsonArray lasttime = jsonObject.getAsJsonArray("lasttime");
                for (int i = 0; i < history.size(); i++) {
                    player.superRank.history(history.get(i).getAsString(), lasttime.get(i).getAsLong());
                }
            } catch (Exception e) {
            }

            if (Util.isAfterMidnight(player.superRank.lastTimePK)) {
                if (player.superRank.ticket < 1) {
                    player.superRank.ticket++;
                }
                player.superRank.lastTimePK = System.currentTimeMillis();
            }

            //data achievement
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_achievement"));
                for (int i = 0; i < Manager.ACHIEVEMENT_TEMPLATE.size(); i++) {
                    AchievementQuest aq;
                    if (i < dataArray.size()) {
                        JSONArray data = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                        aq = new AchievementQuest(Long.parseLong(data.get(0).toString()), Boolean.parseBoolean(data.get(1).toString()));
                    } else {
                        aq = new AchievementQuest(0, false);
                    }
                    player.achievement.add(aq);
                }
                dataArray.clear();
            } catch (Exception e) {
            }

            //Giftcode
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("giftcode"));
                for (Object code : dataArray) {
                    player.giftCode.add((String) code);
                }
                dataArray.clear();
            } catch (Exception e) {
            }

            try {
                player.firstTimeLogin = rs.getTimestamp("firstTimeLogin");
            } catch (Exception e) {
            }

            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("dataBadges"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject obj = (JSONObject) dataArray.get(i);
                    int idBadges = Integer.parseInt(obj.get("idBadGes").toString());
                    long timeOfUseBadges = Long.parseLong(obj.get("timeofUseBadges").toString());
                    boolean isUse = Boolean.parseBoolean(String.valueOf(obj.get("isUse")));
                    player.dataBadges.add(new BadgesData(idBadges, timeOfUseBadges, isUse));
                }
                dataArray.clear();
            } catch (Exception ex) {
            }

            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("dataTaskBadges"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject obj = (JSONObject) dataArray.get(i);
                    BadgesTask data = new BadgesTask();
                    data.id = Integer.parseInt(obj.get("id").toString());
                    data.count = Integer.parseInt(obj.get("count").toString());
                    data.countMax = Integer.parseInt(obj.get("countMax").toString());
                    data.idBadgesReward = Integer.parseInt(obj.get("idBadgesReward").toString());
                    player.dataTaskBadges.add(data);
                }
                dataArray.clear();
            } catch (Exception ex) {
                BadgesTaskService.createAndResetTask(player);
            }

            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("dailyGift"));
                if (dataArray.size() < 2) {
                    DailyGiftService.addAndReset(player);
                } else {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONObject obj = (JSONObject) dataArray.get(i);
                        DailyGiftData data = new DailyGiftData();
                        data.id = Byte.parseByte(obj.get("id").toString());
                        data.daNhan = Boolean.parseBoolean(obj.get("daNhan").toString());
                        player.dailyGiftData.add(data);
                    }
                }
                dataArray.clear();
            } catch (Exception ex) {
                DailyGiftService.addAndReset(player);
            }
            try {
                dataArray = (JSONArray) JSONValue.parse(rs.getString("leothap"));
                if (dataArray != null && !dataArray.isEmpty()) {
                    player.tangThap = Integer.parseInt(dataArray.get(0).toString());
                    player.levelThap = Integer.parseInt(dataArray.get(1).toString());
                    player.pointThap = Integer.parseInt(dataArray.get(2).toString());
                    dataArray.clear();
                }
            } catch (Exception ex) {
            }

            PlayerService.gI().dailyLogin(player);// RESET DATA KHI QUA 12H ĐÊM
            if (player.nPoint.power >= Double.MAX_VALUE) {
                player.nPoint.power = Double.MAX_VALUE;
            }
            if (player.getSession() != null && player.getSession().actived && player.getSession().vnd < 0) {
                player.getSession().actived = false;
                player.getSession().vnd = 0;
            }
            player.nPoint.hp = plHp;
            player.nPoint.mp = plMp;
            player.iDMark.setLoadedAllDataPlayer(true);
        } catch (Exception e) {
            if (player != null) {
                player.dispose();
                player = null;
            }
            e.printStackTrace();
        }
        return player;
    }

    public static List<OptionCard> loadOptionCard(JSONArray json) {
        List<OptionCard> ops = new ArrayList<>();
        try {
            for (int i = 0; i < json.size(); i++) {
                JSONObject ob = (JSONObject) json.get(i);
                if (ob != null) {
                    ops.add(new OptionCard(Integer.parseInt(ob.get("id").toString()), Integer.parseInt(ob.get("param").toString()), Byte.parseByte(ob.get("active").toString())));
                }
            }
        } catch (NumberFormatException e) {
        }
        return ops;
    }
}
