package boss;

import boss.ListBossKhac.ANTROM;

import boss.ListBossKhac.GOHAN1;
import boss.ListBossKhac.GOHAN2;
import boss.ListBossKhac.GOHAN3;
import boss.ListBossKhac.GOHAN4;
import boss.ListBossKhac.GOHAN5;
import boss.ListBossKhac.GOHAN6;

import boss.ListBossKhac.TEST_DAME;

import boss.ServerData.Boss.BOJACK.Bido;
import boss.ServerData.Boss.BOJACK.Bojack;
import boss.ServerData.Boss.BOJACK.Bujin;
import boss.ServerData.Boss.BOJACK.Kogu;
import boss.ServerData.Boss.BOJACK.Zangya;
import boss.ServerData.Boss.COLD.Cooler;
import boss.ServerData.Boss.COLD.Kingcold;
import boss.ServerData.Boss.TASK.AndroidSaga_Android13;
import boss.ServerData.Boss.TASK.AndroidSaga_Android14;
import boss.ServerData.Boss.TASK.AndroidSaga_Android15;
import boss.ServerData.Boss.TASK.AndroidSaga_Android19;
import boss.ServerData.Boss.TASK.BlackSaga_Black;
import boss.ServerData.Boss.TASK.BlackSaga_BlackGokuRose;
import boss.ServerData.Boss.TASK.CellSaga_XenBoHung;
import boss.ServerData.Boss.TASK.CellSaga_XenCon;
import boss.ServerData.Boss.TASK.CellSaga_XenHoanThien;
import boss.ServerData.Boss.TASK.DRMSaga_Chaien;
import boss.ServerData.Boss.TASK.DRMSaga_Doraemon;
import boss.ServerData.Boss.TASK.DRMSaga_Nobita;
import boss.ServerData.Boss.TASK.DRMSaga_Xeko;
import boss.ServerData.Boss.TASK.DRMSaga_Xuka;
import boss.ServerData.Boss.TASK.DrKore;
import boss.ServerData.Boss.TASK.FideSaga_Fide;
import boss.ServerData.Boss.TASK.FideSaga_Kuku;
import boss.ServerData.Boss.TASK.FideSaga_MapDauDinh;
import boss.ServerData.Boss.TASK.FideSaga_Rambo;
import boss.ServerData.Boss.TASK.FideSaga_So1;
import boss.ServerData.Boss.TASK.FideSaga_So2;
import boss.ServerData.Boss.TASK.FideSaga_So3;
import boss.ServerData.Boss.TASK.FideSaga_So4;
import boss.ServerData.Boss.TASK.FideSaga_TieuDoiTruong;
import boss.ServerData.Boss.TASK.KingKong;
import boss.ServerData.Boss.TASK.Other.BaDo;
import boss.ServerData.Boss.TASK.Other.CauThanThu;
import boss.ServerData.Boss.TASK.Other.Noel;
import boss.ServerData.Boss.TASK.Other.admin;
import boss.ServerData.Boss.TASK.Pic;
import boss.ServerData.Boss.TASK.Poc;

import boss.boss_list_tainguyen.BLACKGOKU;

import boss.boss_list_tainguyen.TANTHUTNSM;

import boss.boss_list_thanhuydiet.CHAMPA;
import boss.boss_list_thanhuydiet.THANHUYDIET;
import boss.boss_list_thanhuydiet.THIENSUWHIS;
import boss.boss_list_thanhuydiet.VASDO;

import boss.boss_manifest.Nhan_Gioi.CuuVi;
import boss.boss_manifest.Nhan_Gioi.Obito;
import boss.boss_manifest.Nhan_Gioi.ObitoLucDao;

import boss.boss_manifest.GoldenFrieza.DeathBeam1;
import boss.boss_manifest.GoldenFrieza.DeathBeam2;
import boss.boss_manifest.GoldenFrieza.DeathBeam3;
import boss.boss_manifest.GoldenFrieza.DeathBeam4;
import boss.boss_manifest.GoldenFrieza.DeathBeam5;
import boss.boss_manifest.GoldenFrieza.GoldenFrieza;

import boss.boss_manifest.Broly.Broly;
import boss.boss_manifest.Broly.SuperBroly;
import boss.boss_manifest.ChristmasEvent.OngGiaNoel;
import boss.boss_manifest.TaoPaiPai.TaoPaiPai;

import boss.boss_manifest.HalloweenEvent.BiMa;
import boss.boss_manifest.HalloweenEvent.Doi;
import boss.boss_manifest.HalloweenEvent.MaTroi;
import boss.boss_manifest.TrungThuEvent.KhiDot;
import boss.boss_manifest.TrungThuEvent.NguyetThan;
import boss.boss_manifest.TrungThuEvent.NhatThan;
import boss.boss_manifest.MajinBuu12H.Mabu;
import boss.boss_manifest.MajinBuu12H.BuiBui;
import boss.boss_manifest.MajinBuu12H.BuiBui2;
import boss.boss_manifest.MajinBuu12H.Cadic;
import boss.boss_manifest.MajinBuu12H.Drabura;
import boss.boss_manifest.MajinBuu12H.Drabura2;
import boss.boss_manifest.MajinBuu12H.Drabura3;
import boss.boss_manifest.MajinBuu12H.Goku;
import boss.boss_manifest.MajinBuu12H.Yacon;
import boss.boss_manifest.MajinBuu14H.Mabu2H;
import boss.boss_manifest.MajinBuu14H.SuperBu;

import boss.boss_manifest.NamekGinyuForce.SO1_NM;
import boss.boss_manifest.NamekGinyuForce.SO2_NM;
import boss.boss_manifest.NamekGinyuForce.SO3_NM;
import boss.boss_manifest.NamekGinyuForce.SO4_NM;
import boss.boss_manifest.NamekGinyuForce.TDT_NM;

import boss.boss_manifest.Cumber.Cumber;
import boss.boss_manifest.DaiChienThanThu.BachLang;
import boss.boss_manifest.DaiChienThanThu.HaiKhuyen;
import boss.boss_manifest.DaiChienThanThu.ThietMa;
import boss.boss_manifest.DaiChienThanThu.VuaBachThu;
import boss.boss_manifest.LunarNewYearEvent.LanCon;
import boss_list_bdkb.Brook;
import boss_list_bdkb.Chopper;
import boss_list_bdkb.Franky;
import boss_list_bdkb.Luffy;
import boss_list_bdkb.Nami;
import boss_list_bdkb.Robin;
import boss_list_bdkb.Sanji;
import boss_list_bdkb.Usopp;
import boss_list_bdkb.Zoro;
import java.text.Normalizer;
import player.Player;
import server.Manager;
import network.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import map.Zone;
import server.Maintenance;
import utils.Logger;
import utils.Util;

public class BossManager implements Runnable {

    private static BossManager instance;
    public static byte ratioReward = 10;

    /** May do boss: list da gui co han 5 phut, qua han phai dung lai item */
    public static final long BOSS_TELE_LIST_LIVE_MS = 5 * 60 * 1000L;

    /** Map BossID -> ten FIELD trong BossesData (de GenericBoss lay data). */
    private static final Map<Integer, String> ID2FIELD = new HashMap<>();
    static {
        try {
            for (java.lang.reflect.Field f : BossID.class.getDeclaredFields()) {
                if (f.getType() == int.class && java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                    try { ID2FIELD.put(f.getInt(null), f.getName()); } catch (Exception e) {}
                }
            }
        } catch (Exception e) { }
    }

    public static String fieldOfBossId(int id) { return ID2FIELD.get(id); }

    public static BossManager gI() {
        if (instance == null) {
            instance = new BossManager();
        }
        return instance;
    }

    public BossManager() {
        // CopyOnWriteArrayList: doc nhieu / it ghi -> khong con ConcurrentModificationException
        // khi getBoss() duyet dong thoi boss spawn/rest xoa phia thread khac
        this.bosses = new CopyOnWriteArrayList<>();
    }

    protected final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        this.createBoss(BossID.KUKU);
        this.createBoss(BossID.MAP_DAU_DINH);
        this.createBoss(BossID.RAMBO);
        this.createBoss(BossID.SO_1);
        this.createBoss(BossID.SO_2);
        this.createBoss(BossID.SO_3);
        this.createBoss(BossID.SO_4);
        this.createBoss(BossID.TIEU_DOI_TRUONG);
        this.createBoss(BossID.FIDE);
        this.createBoss(BossID.ANDROID_19);
        this.createBoss(BossID.DR_KORE);
        this.createBoss(BossID.ANDROID_13);
        this.createBoss(BossID.ANDROID_14);
        this.createBoss(BossID.ANDROID_15);
        this.createBoss(BossID.PIC);
        this.createBoss(BossID.POC);
        this.createBoss(BossID.KING_KONG);
        this.createBoss(BossID.SO_1_NM);
        this.createBoss(BossID.SO_2_NM);
        this.createBoss(BossID.SO_3_NM);
        this.createBoss(BossID.SO_4_NM);
        this.createBoss(BossID.TIEU_DOI_TRUONG_NM);
        this.createBoss(BossID.BLACK_GOKU);
        this.createBoss(BossID.BLACKGOKUROSE);
        this.createBoss(BossID.XEN_BO_HUNG);
        this.createBoss(BossID.SIEU_BO_HUNG);

        this.createBoss(BossID.XEN_CON_1);

        this.createBoss(BossID.DORAEMON);
        this.createBoss(BossID.NOBITA);
        this.createBoss(BossID.XUKA);
        this.createBoss(BossID.XEKO);
        this.createBoss(BossID.CHAIEN);
        this.createBoss(BossID.O_DO_NEW);
        this.createBoss(BossID.NOEL);
        this.createBoss(BossID.CauThanThu, 5);
        this.createBoss(BossID.BIDO);
        this.createBoss(BossID.BOJACK);
        this.createBoss(BossID.BUJIN);
        this.createBoss(BossID.KOGU);
        this.createBoss(BossID.ZANGYA);
        this.createBoss(BossID.COOLER);
        this.createBoss(BossID.VUA_COLD);
        this.createBoss(BossID.admin);
         this.createBoss(BossID.GOHAN1);
          this.createBoss(BossID.GOHAN2);
           this.createBoss(BossID.GOHAN3);
            this.createBoss(BossID.GOHAN4);
             this.createBoss(BossID.GOHAN5);
            this.createBoss(BossID.GOHAN6);
        

        this.createBoss(BossID.BROLY);
        this.createBoss(BossID.ANTROM);

        this.createBoss(BossID.TEST_DAME);

        try { WorldBossScheduler.start(); } catch (Exception e) {}

    }

    public void createBoss(int bossID, int total) {
        for (int i = 0; i < total; i++) {
            createBoss(bossID);
        }
    }

    public Boss createBossBroly(int bossID, double hp) {
        try {
            return switch (bossID) {
                case BossID.BROLY ->
                    new Broly();
                case BossID.SUPER_BROLY ->
                    new SuperBroly();
                default ->
                    createGeneric(bossID);
            };
        } catch (Exception e) {
            Logger.logException(BossManager.class, e);
            return null;
        }
    }

    /** Tao boss bang GenericBoss neu co BossData tuong ung trong BossesData. */
    private Boss createGeneric(int id) {
        try {
            String field = ID2FIELD.get(id);
            if (field == null) return null;
            BossData d = GenericBoss.getDataByField(field);
            if (d == null) return null;
            return new GenericBoss(id, field);
        } catch (Exception e) {
            Logger.error("createGeneric " + id + ": " + e + "\n");
            return null;
        }
    }

    /**
     * Goi boss vuot quyen tu Admin Panel (bo qua lich/ dieu kien).
     * @param bossId id boss (BossID.*)
     * @param zoneId khu vuc (-1 = khu 0 cua map mac dinh cua boss, hoac 0)
     * @return Boss da tao, hoac null neu that bai
     */
    public Boss spawnBossByAdmin(int bossId, int zoneId) {
        try {
            Boss b = this.createBoss(bossId);
            if (b == null) return null;
            // tim zone de join
            Zone z = null;
            try {
                if (b.zoneFinal != null) z = b.zoneFinal;
                else if (zoneId >= 0 && !Manager.MAPS.isEmpty()) {
                    for (map.Map m : Manager.MAPS) {
                        for (Zone zz : m.zones) { if (zz.zoneId == zoneId) { z = zz; break; } }
                        if (z != null) break;
                    }
                }
            } catch (Exception e) {}
            if (z == null) {
                // mac dinh map dau tien, zone 0
                try { z = Manager.MAPS.get(0).zones.get(0); } catch (Exception e) {}
            }
            if (z != null) {
                b.zoneFinal = z;
                try { b.joinMapByZone(z); } catch (Exception e) { }
            }
            try { writeBossLog(bossId, "spawn_admin"); } catch (Exception e) {}
            return b;
        } catch (Exception e) {
            Logger.error("spawnBossByAdmin " + bossId + ": " + e + "\n");
            return null;
        }
    }

    private void writeBossLog(int bossId, String action) {
        try (java.sql.Connection con = jdbc.DBConnecter.getConnectionServer()) {
            try (java.sql.PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO panel_change_log (admin_user,module,action,detail) VALUES ('admin','boss',?,?)")) {
                ps.setString(1, action);
                ps.setString(2, String.valueOf(bossId));
                ps.executeUpdate();
            }
        } catch (Exception e) { }
    }

    public Boss createBoss(int bossID) {
        try {
            return switch (bossID) {
                case BossID.HAI_KHUYEN ->
                    new HaiKhuyen();

                case BossID.COOLER ->
                    new Cooler();
                case BossID.VUA_COLD ->
                    new Kingcold();
                case BossID.admin ->
                    new admin();
                case BossID.DORAEMON ->
                    new DRMSaga_Doraemon();
                case BossID.NOBITA ->
                    new DRMSaga_Nobita();
                case BossID.XUKA ->
                    new DRMSaga_Xuka();
                case BossID.XEKO ->
                    new DRMSaga_Xeko();
                case BossID.CHAIEN ->
                    new DRMSaga_Chaien();

                case BossID.TEST_DAME ->
                    new TEST_DAME();

                case BossID.XEN_BO_HUNG ->
                    new CellSaga_XenBoHung();

                case BossID.SIEU_BO_HUNG ->
                    new CellSaga_XenHoanThien();

                case BossID.ANTROM ->
                    new ANTROM();
                
                case BossID.GOHAN1 ->
                    new GOHAN1();
                case BossID.GOHAN2 ->
                    new GOHAN2();
                case BossID.GOHAN3 ->
                    new GOHAN3();
                case BossID.GOHAN4 ->
                    new GOHAN4();
                case BossID.GOHAN5 ->
                    new GOHAN5();
                case BossID.GOHAN6 ->
                    new GOHAN6();

                case BossID.THANHUYDIET ->
                    new THANHUYDIET();
                case BossID.Luffy ->
                    new Luffy();
                case BossID.Zoro ->
                    new Zoro();
                case BossID.Sanji ->
                    new Sanji();
                case BossID.Brook ->
                    new Brook();
                case BossID.Chopper ->
                    new Chopper();
                case BossID.Nami ->
                    new Nami();
                case BossID.Franky ->
                    new Franky();
                case BossID.Usopp ->
                    new Usopp();
                case BossID.Robin ->
                    new Robin();

                case BossID.THIENSUWHIS ->
                    new THIENSUWHIS();
                case BossID.CHAMPA ->
                    new CHAMPA();
                case BossID.VADOS ->
                    new VASDO();

                case BossID.BACH_LANG ->
                    new BachLang();
                case BossID.THIET_MA ->
                    new ThietMa();
                case BossID.VUA_BACH_THU ->
                    new VuaBachThu();
                case BossID.OBITO_LUC_DAO ->
                    new ObitoLucDao();
                case BossID.CUU_VI ->
                    new CuuVi();
                case BossID.OBITO ->
                    new Obito();
//                case BossID.TAP_SU_0 -> new TAPSU0();
//                case BossID.TAP_SU_1 -> new TAPSU1();
//                case BossID.TAP_SU_2 -> new TAPSU2();
//                case BossID.TAP_SU_3 -> new TAPSU3();
//                case BossID.TAP_SU_4 -> new TAPSU4();
//                case BossID.TAN_BINH_5 -> new TANBINH5();
//                case BossID.TAN_BINH_0 -> new TANBINH0();
//                case BossID.TAN_BINH_1 -> new TANBINH1();
//                case BossID.TAN_BINH_2 -> new TANBINH2();
//                case BossID.TAN_BINH_3 -> new TANBINH3();
//                case BossID.TAN_BINH_4 -> new TANBINH4();
//                case BossID.CHIEN_BINH_5 -> new CHIENBINH5();
//                case BossID.CHIEN_BINH_0 -> new CHIENBINH0();
//                case BossID.CHIEN_BINH_1 -> new CHIENBINH1();
//                case BossID.CHIEN_BINH_2 -> new CHIENBINH2();
//                case BossID.CHIEN_BINH_3 -> new CHIENBINH3();
//                case BossID.CHIEN_BINH_4 -> new CHIENBINH4();
//                case BossID.DOI_TRUONG_5 -> new DOITRUONG5();
                case BossID.SO_4 ->
                    new FideSaga_So4();
                case BossID.SO_3 ->
                    new FideSaga_So3();
                case BossID.SO_2 ->
                    new FideSaga_So2();
                case BossID.SO_1 ->
                    new FideSaga_So1();
                case BossID.TIEU_DOI_TRUONG ->
                    new FideSaga_TieuDoiTruong();
                case BossID.SO_4_NM ->
                    new SO4_NM();
                case BossID.SO_3_NM ->
                    new SO3_NM();
                case BossID.SO_2_NM ->
                    new SO2_NM();
                case BossID.SO_1_NM ->
                    new SO1_NM();
                case BossID.TIEU_DOI_TRUONG_NM ->
                    new TDT_NM();
                case BossID.BUJIN ->
                    new Bujin();
                case BossID.KOGU ->
                    new Kogu();
                case BossID.ZANGYA ->
                    new Zangya();
                case BossID.BIDO ->
                    new Bido();
                case BossID.BOJACK ->
                    new Bojack();

                case BossID.KUKU ->
                    new FideSaga_Kuku();
                case BossID.MAP_DAU_DINH ->
                    new FideSaga_MapDauDinh();
                case BossID.RAMBO ->
                    new FideSaga_Rambo();
                case BossID.TAU_PAY_PAY_DONG_NAM_KARIN ->
                    new TaoPaiPai();
                case BossID.DRABURA ->
                    new Drabura();
                case BossID.BUI_BUI ->
                    new BuiBui();
                case BossID.BUI_BUI_2 ->
                    new BuiBui2();
                case BossID.YA_CON ->
                    new Yacon();
                case BossID.DRABURA_2 ->
                    new Drabura2();
                case BossID.GOKU ->
                    new Goku();
                case BossID.CADIC ->
                    new Cadic();
                case BossID.MABU_12H ->
                    new Mabu();
                case BossID.DRABURA_3 ->
                    new Drabura3();
                case BossID.MABU ->
                    new Mabu2H();
                case BossID.SUPERBU ->
                    new SuperBu();
                case BossID.FIDE ->
                    new FideSaga_Fide();
                case BossID.DR_KORE ->
                    new DrKore();
                case BossID.ANDROID_19 ->
                    new AndroidSaga_Android19();
                case BossID.ANDROID_13 ->
                    new AndroidSaga_Android13();
                case BossID.ANDROID_14 ->
                    new AndroidSaga_Android14();
                case BossID.ANDROID_15 ->
                    new AndroidSaga_Android15();
                case BossID.PIC ->
                    new Pic();
                case BossID.POC ->
                    new Poc();
                case BossID.KING_KONG ->
                    new KingKong();

                case BossID.BROLY ->
                    new Broly();
                case BossID.KHIDOT ->
                    new KhiDot();
                case BossID.NGUYETTHAN ->
                    new NguyetThan();
                case BossID.NHATTHAN ->
                    new NhatThan();
                case BossID.GOLDEN_FRIEZA ->
                    new GoldenFrieza();
                case BossID.DEATH_BEAM_1 ->
                    new DeathBeam1();
                case BossID.DEATH_BEAM_2 ->
                    new DeathBeam2();
                case BossID.DEATH_BEAM_3 ->
                    new DeathBeam3();
                case BossID.DEATH_BEAM_4 ->
                    new DeathBeam4();
                case BossID.DEATH_BEAM_5 ->
                    new DeathBeam5();
                case BossID.BIMA ->
                    new BiMa();
                case BossID.MATROI ->
                    new MaTroi();
                case BossID.DOI ->
                    new Doi();
                case BossID.ONG_GIA_NOEL ->
                    new OngGiaNoel();
                case BossID.TANTHUTNSM ->
                    new TANTHUTNSM();

                case BossID.LAN_CON ->
                    new LanCon();
                case BossID.BLACK_GOKU ->
                    new BlackSaga_Black();
                case BossID.SUPER_BROLY ->
                    new SuperBroly();
                case BossID.CUMBER ->
                    new Cumber();

                case BossID.BLACKGOKU ->
                    new BLACKGOKU();
                case BossID.BLACKGOKUROSE ->
                    new BlackSaga_BlackGokuRose();

                case BossID.O_DO_NEW ->
                    new BaDo();
                case BossID.NOEL ->
                    new Noel();

                case BossID.CauThanThu ->
                    new CauThanThu();

                case BossID.XEN_CON_1 ->
                    new CellSaga_XenCon();

                case BossID.WORLD_BOSS ->
                    new boss.boss_manifest.WorldBoss.WorldBoss();

                default ->
                    createGeneric(bossID);
            };
        } catch (Exception e) {
            Logger.logException(BossManager.class, e);
            return null;
        }
    }

    public Boss getBoss(Object value) {
        for (Boss boss : bosses) {
            try {
                int idOrIndex = Integer.parseInt(String.valueOf(value));
                if (boss.id == idOrIndex || bosses.indexOf(boss) == idOrIndex) {
                    return boss;
                }
            } catch (NumberFormatException e) {
                if (convertString(boss.data[0].name).equalsIgnoreCase(convertString(String.valueOf(value))) || boss.name != null && convertString(boss.name).equalsIgnoreCase(convertString(String.valueOf(value)))) {
                    return boss;
                }
            }
        }
        return null;
    }

    public List<Boss> getBosses() {
        return this.bosses;
    }

    public static String convertString(String value) {
        try {
            return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[Đđ]", "d")).replaceAll("");
        } catch (Exception ex) {
            return Normalizer.normalize(value, Normalizer.Form.NFD);
        }
    }

    /**
     * Boss dang song tren ban do (may do chi liet ke boss con song).
     * Boss chet -> bo loc tu dong loai bo ngay; spawn lai -> tu dong xuat hien lai
     * (danh sach duoc loc lai moi lan gui, khong can add/remove tay).
     */
    public static boolean isAliveOnMap(Boss boss) {
        if (boss == null || boss.zone == null || boss.zone.map == null || boss.isDie()) {
            return false;
        }
        return switch (boss.bossStatus) {
            case JOIN_MAP, CHAT_S, ACTIVE, AFK -> true;
            default -> false;
        };
    }

    /**
     * Gui danh sach boss dang song cho client.
     * @return true neu gui thanh cong (goi xong moi tru item o UseItem)
     */
    public boolean showListBoss(Player player) {
        player.iDMark.setMenuType(3);
        List<Boss> alive = new ArrayList<>();
        for (Boss boss : this.bosses) {
            if (isAliveOnMap(boss)) {
                alive.add(boss);
            }
        }
        Message msg = null;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);

            // chi hien boss dang song
            msg.writer().writeUTF("Boss đang sống: " + alive.size());

            // client doc so dong dang BYTE -> ghi int se bi doc thanh 0 dong
            // (xem Service.showListTop: ghi int(17) = 00 00 00 11 -> byte dau = 0)
            int count = Math.min(255, alive.size());
            msg.writer().writeByte(count);

            for (int i = 0; i < count; i++) {
                Boss boss = alive.get(i);

                msg.writer().writeInt(i);
                // ca 2 int deu la vi tri dong: client chi gui 1 int ve server,
                // truoc ghi boss.id -> getBoss tra ban dau tien trung id -> nhay sai boss
                msg.writer().writeInt(i);

                // Ngoại hình
                msg.writer().writeShort(boss.data[0].getOutfit()[0]); // đầu
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1); // aura (nếu cần)
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]); // áo
                msg.writer().writeShort(boss.data[0].getOutfit()[2]); // quần

                // Tên boss
                msg.writer().writeUTF(boss.data[0].getName());

                // chi dung trang thai "on" vi chi boss con song moi vao day
                msg.writer().writeUTF("on");
                msg.writer().writeUTF("Thông Tin Boss\n"
                        + "|4|Bản Đồ : [" + boss.zone.map.mapName + "][" + boss.zone.map.mapId + "] \n"
                        + "Khu Vực: [" + boss.zone.zoneId + "]\n"
                        + "HP: " + Util.numberToMoney(boss.nPoint.hp) + "\n"
                        + "Dame: " + Util.numberToMoney(boss.nPoint.dame) + "\n"
                        + "Chí Mạng: " + Util.numberToMoney(boss.nPoint.crit));
            }

            // luu list da gui theo player: khi client bam dong lay dung boss theo vi tri
            player.iDMark.setBossTeleList(alive);
            player.iDMark.setBossTeleListTime(System.currentTimeMillis());
            player.sendMessage(msg);
            return true;
        } catch (Exception e) {
            // truoc do printStackTrace -> server loi ma khong co gi trong log
            Logger.logException(BossManager.class, e, "Loi showListBoss");
            return false;
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Lay boss tu list da gui cho client (may do boss).
     * Khong dung getBoss(_id) vi getBoss tra ve ban dau tien trung id
     * (khong loc song/chet) -> nhay sai boss khi nhieu boss cung id.
     */
    public Boss getBossFromTeleList(Player player, int index) {
        List<Boss> list = player.iDMark.getBossTeleList();
        if (list == null || System.currentTimeMillis() - player.iDMark.getBossTeleListTime() > BOSS_TELE_LIST_LIVE_MS) {
            return null;
        }
        if (index < 0 || index >= list.size()) {
            return null;
        }
        Boss boss = list.get(index);
        return isAliveOnMap(boss) ? boss : null;
    }

    public Boss getBossById(int bossId) {
        return this.bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    public boolean checkBosses(Zone zone, int BossID) {
        return this.bosses.stream()
                .filter(boss -> boss.id == BossID && boss.zone != null && boss.zone.equals(zone) && !boss.isDie())
                .findFirst().orElse(null) != null;
    }

    public Player findBossClone(Player player) {
        return player.zone.getBosses().stream().filter(boss -> boss.id < -100_000_000 && !boss.isDie()).findFirst()
                .orElse(null);
    }

    public Boss getBossById(int bossId, int mapId, int zoneId) {
        return this.bosses.stream().filter(boss -> boss.id == bossId && boss.zone != null
                && boss.zone.map.mapId == mapId && boss.zone.zoneId == zoneId && !boss.isDie()).findFirst()
                .orElse(null);
    }

    @Override
    public void run() {
        while (!Maintenance.isRunning) {
            try {
                int delay = 500;
                long st = System.currentTimeMillis();
                for (int i = this.bosses.size() - 1; i >= 0; i--) {
                    try {
                        this.bosses.get(i).update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (delay - (System.currentTimeMillis() - st) > 0) {
                    Thread.sleep(delay - (System.currentTimeMillis() - st));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
