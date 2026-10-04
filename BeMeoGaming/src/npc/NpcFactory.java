package npc;

import EventSuKien.EventBanGoi;
import EventSuKien.EventBikip;
import EventSuKien.EventCauCa;
import EventSuKien.EventChuyenSinh;
import EventSuKien.EventKetHon;
import EventSuKien.EventSoXuMenh;
import EventSuKien.EventThoMo;
import EventSuKien.EventTrungThu;
import boss.BossID;
import models.Consign.ConsignShopService;
import services.ClanService;
import services.Service;
import services.ItemService;
import services.NgocRongNamecService;
import services.IntrinsicService;
import services.InventoryService;
import services.NpcService;
import services.PetService;
import services.PlayerService;
import services.FriendAndEnemyService;
import consts.ConstNpc;
import boss.BossManager;
import clan.Clan;

import java.util.HashMap;
import java.util.Random;

import Mail.HomThuService;
import boss.FinalBossManager;
import boss.LunarNewYearEventManager;
import boss.OtherBossManager;
import boss.RedRibbonHQManager;
import boss.SkillSummonedManager;
import boss.TreasureUnderSeaManager;
import consts.ConstPlayer;
import services.func.ChangeMapService;
import services.func.SummonDragon;

import static services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static services.func.SummonDragon.SHENRON_SAY;

import player.Player;
import item.Item;
import item.Item.ItemOption;
import jdbc.daos.PlayerDAO;
import matches.PVPService;
import server.Client;
import server.Maintenance;
import server.Manager;
import services.func.Input;
import utils.Logger;
import utils.Util;
import models.SuperDivineWater.SuperDivineWaterService;
import models.ShenronEvent.ShenronEventService;
import npc.npc_manifest.*;
import player.Tamkjll_Pet;
import static services.func.SummonDragon.ICE_SHENRON_SAY;
import static services.func.SummonDragon.ICE_SHENRON_WISHES;
import services.func.SummonDragonNamek;
import skill.Skill;
import utils.SkillUtil;

public class NpcFactory {

    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<>();

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            return switch (tempId) {
                case ConstNpc.NPCBiKiep ->
                    new EventBikip(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAO_LU_NE ->
                    new DaoLu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BERRY ->
                    new EventThoMo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPCCauCa ->
                    new EventCauCa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TuTienNe ->
                    new EventChuyenSinh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPCSoXuMenh ->
                    new EventSoXuMenh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPCBanGoi ->
                    new EventBanGoi(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.Check ->
                    new Check(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SKMabu ->
                    new SKMabu(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.NPC_KetHonne ->
                    new EventKetHon(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.EventTrungThu ->
                    new EventTrungThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH -> // noi banh Trung Thu (chi dat tren map lang, thay NPC 92)
                    new EventTrungThu(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.shopbdkb ->
                    new Luffyshop(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.vaymuon ->
                    new vaymuon(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.BERRY ->
//                    new Berry(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPCTanThu ->
                    new NPCTanThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.cadich ->
                    new cadich(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DuaTop ->
                    new DuaTop(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NARUTO ->
                    new Naruto(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH ->
                    new GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI ->
                    new TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE ->
                    new Potage(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO ->
                    new MrPoPo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME ->
                    new QuyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU ->
                    new TruongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA ->
                    new VuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI ->
                    new KyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN ->
                    new OngGohan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.hatsu ->
                    new hatsu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.kaioshin ->
                    new kaioshin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.facebook ->
                    new facebook(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SUKIEN ->
                    new SUKIEN365(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.ONG_MOORI ->
                    new OngMoori(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_PARAGUS ->
                    new OngParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA ->
                    new Bulma(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE ->
                    new Dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE ->
                    new Appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF ->
                    new DrDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO ->
                    new Cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI ->
                    new Cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA ->
                    new Santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON ->
                    new Uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT ->
                    new BaHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO ->
                    new RuongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN ->
                    new DauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK ->
                    new Calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO ->
                    new Jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE ->
                    new ThuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS ->
                    new Vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU ->
                    new ThanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT ->
                    new Kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN ->
                    new Osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BABIDAY ->
                    new Babiday(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG ->
                    new LyTieuNuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH ->
                    new LinhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG ->
                    new QuaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG ->
                    new QuocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL ->
                    new BulmaTuongLai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA ->
                    new RongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S ->
                    new Rong1Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_2S ->
                    new Rong2Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_3S ->
                    new Rong3Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_4S ->
                    new Rong4Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_5S ->
                    new Rong5Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_6S ->
                    new Rong6Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_7S ->
                    new Rong7Sao(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAI_THIEN_SU ->
                    new DaiThienSu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS ->
                    new Whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL ->
                    new Bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG ->
                    new BoMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN ->
                    new Karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ ->
                    new GokuSSJ(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_2 ->
                    new GokuSSJ2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION ->
                    new Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN ->
                    new DocNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO ->
                    new GiuMaDauBo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO ->
                    new ToSuKaio(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BARDOCK ->
                    new Bardock(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG ->
                    new DuongTang(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGO_KHONG ->
                    new NGO_KHONG(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TORI_BOT ->
                    new ToriBot(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LEO_THAP ->
                    new LeoThap(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HO_TONG ->
                    new HoTong(mapId, status, cx, cy, tempId, avatar);
                default ->
                    new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                            }
                        }
                    };
            };
        } catch (Exception e) {
            Logger.logException(NpcFactory.class,
                    e, "Lỗi load npc");
            return null;
        }
    }

    public static void createNpcRongThieng() {
        new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:
                        break;
                    case ConstNpc.SHOW_SHENRON_NAMEK_CONFIRM:
                        SummonDragonNamek.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                    case ConstNpc.SHENRON_NAMEK_CONFIRM:
                        if (select == 0) {
                            SummonDragonNamek.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragonNamek.gI().sendWhishesNamec(player);
                        }
                        break;
                    case ConstNpc.SHOW_SHENRON_EVENT_CONFIRM:
                        if (player.shenronEvent != null) {
                            player.shenronEvent.showConfirmShenron((byte) select);
                        }
                        break;
                    case ConstNpc.SHENRON_EVENT_CONFIRM:
                        if (player.shenronEvent != null) {
                            if (select == 0) {
                                player.shenronEvent.confirmWish();
                            } else if (select == 1) {
                                player.shenronEvent.sendWhishesShenron();
                            }
                        }
                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    case ConstNpc.ICE_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.ICE_SHENRON
                                && select == ICE_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY,
                                    ICE_SHENRON_WISHES);
                            break;
                        }

                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void MenuBH(Player player, String hieuung) {
        long dk = player.SagaThienDao + 1;
        if (player.SagaThienDao >= 700) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaThienDao >= 500) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaThienDao >= 300) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaThienDao >= 100) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaThienDao >= 70) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaThienDao >= 30) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUng, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Thiên Đạo: " + player.SagaThienDao + " Cấp\n"
                    + "|5|Thiên Đạo: " + player.PhapTac_ThienDao + " Pháp Tắc\n"
                    + "|7|" + "Hoàn Thành: " + player.PhapTac_ThienDao * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuung,
                    "hiệu ứng\nChủ Động", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        }
    }

    public static void MenuDiaDao(Player player, String hieuungNe) {
        long dk = player.SagaDiaDao + 1;
        if (player.SagaDiaDao >= 1) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUngdia, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Địa Đạo: " + player.SagaDiaDao + " Cấp\n"
                    + "|5|Lục Đạo: " + player.DLbTamkjll + " Luân Hồi\n"
                    + "|7|" + "Hoàn Thành: " + player.DLbTamkjll * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuungNe,
                    "hiệu ứng\nCơ Bản", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        } else if (player.SagaDiaDao >= 2) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaHieuUngdia, -1, "|7|Chúc Bạn Chơi Game Vui Vẻ\n"
                    + "|5|Địa Đạo: " + player.SagaDiaDao + " Cấp\n"
                    + "|5|Lục Đạo: " + player.DLbTamkjll + " Luân Hồi\n"
                    + "|7|" + "Hoàn Thành: " + player.DLbTamkjll * 100L / (500000L * dk) + "% Lên Cấp\n"
                    + "|4|" + hieuungNe,
                    "hiệu ứng\nCơ Bản", "hiệu ứng\nHỗ Trợ", "hiệu ứng\nPhụ Trợ");
        }
    }

    public static void createNpcConMeo() {
        new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case 1102025 -> {
                        switch (select) {
                            case 0 -> {
                                HomThuService.gI().removeMail(player, HomThuService.XOA_THU);
                            }
                            case 1 -> {
                                HomThuService.gI().removeMail(player, HomThuService.XOA_DA_DOC);
                            }
                            case 2 -> {
                                HomThuService.gI().removeMail(player, HomThuService.XOA_ALL);
                            }
                            default -> {
                                HomThuService.gI().sendListMail(player);
                            }
                        }
                    }
                    case 1102026 -> {
                        try { HomThuService.gI().handleHubConfirm(player, select); } catch (Exception e) {}
                    }
                    case HomThuService.MAIL_DELETE_ALL -> {
                        if (select == 0) {
                            HomThuService.gI().removeMail(player, HomThuService.XOA_ALL);
                        } else {
                            try { HomThuService.gI().openMailHub(player); } catch (Exception e) {}
                        }
                    }
                    case ConstNpc.IGNORE_MENU -> {
                    }
                    case ConstNpc.SUMMON_SHENRON_EVENT -> {
                        if (select == 0) {
                            ShenronEventService.gI().summonShenron(player);
                        }
                    }
                    case ConstNpc.MAKE_MATCH_PVP -> {
                        if (Maintenance.isRunning) {
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                    }
                    case ConstNpc.MAKE_FRIEND -> {
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                try {
                                    FriendAndEnemyService.gI().acceptMakeFriend(player,
                                            Integer.parseInt(String.valueOf(playerId)));
                                } catch (NumberFormatException e) {
                                }
                            }
                        }
                    }
                    case ConstNpc.REVENGE -> {
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                    }
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON -> {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                    }
                    case ConstNpc.SUMMON_DRAGON_SIEU_CAP -> {
                        switch (select) {
                            case 0:

                                NpcService.gI().createMenuConMeo(player, ConstNpc.SUMMON_DRAGON_SIEU_CAP_MENU, -1,
                                        "Ta sẽ ban cho ngươi một điều ước, ngươi có 5 phút, hãy chọn đi:"
                                        + "\n1) Cải trang Gohan Siêu Nhân. (Hạn dùng  60 ngày)"
                                        + "\n2) Cải trang Biden Siêu Nhân. (Hạn dùng 60 ngày)"
                                        + "\n3) Cải trang bản cô nương Siêu Nhân. (Hạn dùng 60 ngày)"
                                        + "\n4) Rương sao pha lê",
                                        "Điều ước 1", "Điều ước 2", "Điều ước 3", "Điều ước 4");
                                break;
                        }
                    }
                    case ConstNpc.SUMMON_SHENRON -> {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                    }
                    case ConstNpc.MENU_OPTION_USE_ITEM726 -> {
                        if (select == 0) {
                            SuperDivineWaterService.gI().joinMapThanhThuy(player);
                        }
                    }
                    case ConstNpc.MENU_SIEU_THAN_THUY -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMap(player, 46, -1, Util.nextInt(300, 400), 408);
                        }
                    }
                    case ConstNpc.TAP_TU_DONG_CONFIRM -> {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.lastMapOffline, player.lastZoneOffline, player.lastXOffline);
                        }
                    }

                    case ConstNpc.SUMMON_BLACK_SHENRON -> {
                        if (select
                                == 0) {
                            SummonDragon.gI().summonBlackShenron(player);
                        }
                    }

                    case ConstNpc.SUMMON_ICE_SHENRON -> {
                        if (select
                                == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                    }

                    case ConstNpc.INTRINSIC -> {
                        switch (select) {
                            case 0 ->
                                IntrinsicService.gI().showAllIntrinsic(player);
                            //  case 1 -> IntrinsicService.gI().showConfirmOpen(player);
                            case 1 ->
                                IntrinsicService.gI().showConfirmOpenVip(player);
                            default -> {
                            }
                        }
                    }
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC -> {
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                    }
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP -> {
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                    }
                    case ConstNpc.CONFIRM_LEAVE_CLAN -> {
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                    }
                    case ConstNpc.CONFIRM_NHUONG_PC -> {
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                    }

                    case ConstNpc.BAN_PLAYER -> {
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                    }
                    case ConstNpc.BUFF_PET -> {
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                    }
                    case ConstNpc.OTT -> {
                        if (select < 3) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            player.iDMark.setOtt(select);
                            String[] selects = new String[]{"Kéo", "Búa", "Bao", "Hủy"};
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.OTT_ACCEPT, -1,
                                    player.name + " muốn chơi oẳn tù tì với bạn mức cược 5tr.", selects, player);
                        }
                    }
                    case ConstNpc.OTT_ACCEPT -> {
                        if (select < 3) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            int slp1 = pl.iDMark.getOtt();
                            int slp2 = select;
                            if (slp1 == -1 || slp2 == -1) {
                                return;
                            }
                            pl.iDMark.setOtt(-1);
                            String[] selects = new String[]{"Kéo", "Búa", "Bao"};
                            Service.gI().chat(pl, selects[slp1]);
                            Service.gI().chat(player, selects[slp2]);
                            Service.gI().sendEffAllPlayer(pl, 1000 + slp1, 1, 2, 1);
                            Service.gI().sendEffAllPlayer(player, 1000 + slp2, 1, 2, 1);
                            if (slp1 == slp2) {
                                Service.gI().sendThongBao(pl, "Hòa!");
                                Service.gI().sendThongBao(player, "Hòa!");
                            } else if (slp1 == 0 && slp2 == 2 || slp1 == 1 && slp2 == 0 || slp1 == 2 && slp2 == 1) {
                                Service.gI().sendThongBao(pl, "Thắng!");
                                Service.gI().sendThongBao(player, "Thua!");
                                pl.inventory.gold += 4800000;
                                player.inventory.gold -= 5000000;
                                Service.gI().sendMoney(pl);
                                Service.gI().sendMoney(player);
                            } else {
                                Service.gI().sendThongBao(pl, "Thua!");
                                Service.gI().sendThongBao(player, "Thắng!");
                                pl.inventory.gold -= 5000000;
                                player.inventory.gold += 4800000;
                                Service.gI().sendMoney(pl);
                                Service.gI().sendMoney(player);
                            }
                        }
                    }
                    case ConstNpc.Tamkjllmenu -> {
                        switch (select) {
                            case 0:
                                if (player.Nametc == null) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đánh 1 Hít Vô Ai Đi Cu");
                                    return;
                                }
                                String huthaydame;
                                if (player.dametc <= 0) {
                                    huthaydame = "Hụt";
                                } else {
                                    huthaydame = Util.FormatNumber(player.dametc);
                                }
                                String expke = "";
                                if (player.ctkclan > 0) {
                                    expke = "\n|1|Exp Tiên Bang Vừa nhận ké: " + player.ctkclan;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + player.Nametc
                                        + "\n|7|Hp Mục tiêu còn: " + Util.FormatNumber(player.hptc)
                                        + "\n|3|Sát thương lên mục tiêu: " + huthaydame
                                        + "\n|1|Exp Tiên Bang Nhận: " + player.ctk
                                        + expke,
                                        "Sát Thương Mục tiêu");
                                break;
                            case 1:
                                int gender = player.gender;
                                String hieuung = "Hành tinh: " + gender;
                                MenuBH(player, hieuung);
                                break;

                            case 2:
                                long exptm = player.TamkjllThomo;
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "|4|\nCần Mở Thiên Phú Để Tu Tiên\n"
                                        + "|4|\nExp Tu tiên: " + Util.getFormatNumber(player.SagaTuTien[0]),
                                        "Sát Thương \nVừa Đấm", "Hiệu ứng \nTiên Bang",
                                        "Thông Tin Exp"
                                );
                                break;
                        }
                    }
                    case ConstNpc.Checkthongtin -> {
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "|7|Hp: " + Util.FormatNumber(player.nPoint.hpMax)
                                        //    + Util.FormatNumber(player.nPoint.hpMax)
                                        + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mpMax)
                                        //   + Util.FormatNumber(player.nPoint.mpMax)
                                        + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                        + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                        + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                        + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                        + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                        + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                        + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                        "Thông Tin\nBản Thân", "Thông Tin\nĐệ Tử", "Đồ Sát");
                                break;

                            case 1:
                                if (player.pet != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.Checkthongtin, -1,
                                            "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                            + "|7|Hp: " + Util.FormatNumber(player.nPoint.hpMax)
                                            //    + Util.FormatNumber(player.nPoint.hpMax)
                                            + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mpMax)
                                            //   + Util.FormatNumber(player.nPoint.mpMax)
                                            + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                            + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                            + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                            + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                            + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                            + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                            + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                            "Thông Tin\nBản Thân", "Thông Tin\nĐệ Tử");
                                }
                                break;

                        }
                    }
                    case ConstNpc.SagaHieuUng -> {
                        switch (select) {
                            case 0: {
                                String hieuung = "-Hiệu ứng Đang Có:";
                                hieuung += "\n+Tỉ lệ Rơi Cs Gốc: " + (player.SagaThienDao + 1L) * panel.tuning.SystemTuning.getL("thiendao_roi_cs_goc_moi_cap");
                                hieuung += "\n+Giảm ST của Boss: Từ " + (player.SagaThienDao + panel.tuning.SystemTuning.getL("thiendao_giam_st_boss"));

                                if (player.SagaThienDao >= 1) {
                                    hieuung += "\n+HP,KI,SD: " + player.SagaThienDao + "% " + "/Cấp";
                                }
                                if (player.SagaChuyenSinh >= 1) {
                                    hieuung += "\nChuyển sinh +HP,KI,SD,Giáp: " + player.SagaChuyenSinh * 30L + "% " + "/Cấp";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 1: {
                                String hieuung = "-Hiệu ứng Thêm Từ cấp 30 Cộng Dồn:";

                                if (player.gender == 0) {
                                    hieuung += "\n+Kaioken: " + player.SagaThienDao + "% SD.";
                                    hieuung += "\n+Qckk: " + player.SagaThienDao + "% Tung Ra.";
                                    hieuung += "\n+Tăng: " + player.SagaThienDao + "% DCTT Đòn kế.";
                                    hieuung += "\n+Tăng: " + player.SagaThienDao / 90L + "s Thời gian choáng.";
                                    hieuung += "\n+Tăng: " + Util.getFormatNumber(player.SagaThienDao / 100L)
                                            + "S Thời gian Thôi miên.";
                                    hieuung += "\n+Tăng Tăng thời gian khiên năng lượng lên: " + player.SagaThienDao / 50L
                                            + "s";
                                }
                                if (player.gender == 1) {
                                    hieuung += "\n+Liên hoàn: " + player.SagaThienDao + "% SD.";
                                    hieuung += "\n+laZe: " + player.SagaThienDao * 28L + "% SD.";
                                    hieuung += "\n+Tăng: " + player.SagaThienDao + "% SD Chim.";
                                    hieuung += "\n+Tăng STCM liên hoàn " + player.SagaThienDao + "%";

                                }
                                if (player.gender == 2) {
                                    hieuung += "\n+Khỉ: " + player.SagaThienDao * 30L + "% HP.";
                                    hieuung += "\n+Khỉ: " + player.SagaThienDao + "% SD.";
                                    hieuung += "\n+Nổ Bom: " + player.SagaThienDao * 65L + "% ST.";
                                    hieuung += "\n+Tăng: " + player.SagaThienDao / 30L + "s Khi Khỉ.";
                                    hieuung += "\n+Tăng Giáp: " + player.SagaThienDao / 30L + "% Hp Khi Khỉ.";
                                    hieuung += "\n+Tăng Choáng: " + player.SagaThienDao / 50L + "% Khi Trói.";
                                    hieuung += "\n+Tăng HP: " + player.SagaThienDao / 90L + "% Khí Huýt Sáo"
                                            + "s";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }

                            case 2: {
                                String hieuung = "-Hiệu ứng Phụ Từ Cấp 30 Cộng Dồn:";
                                hieuung += "\n+Tăng Nhận EXP [Địa Đạo] Theo Thiên Đạo";
                                hieuung += "\n+Tăng Tỉ lệ chuyển sinh ở đệ";
                                hieuung += "\n+Up nhận ngọc xanh, hồng ngọc,Vàng";
                                hieuung += "\nĐame Chuẩn Lên Boss:" + player.SagaThienDao + " %.";
                                hieuung += "\n+Tăng Tiến Độ Tu Tiên";
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 3: {
                                String hieuung = "-Hiệu ứng Trên cấp 100:";
                                hieuung += "\nĐánh boss +Dame: " + player.TamkjllCapPb * 106L + "%.";
                                hieuung += "\n+Tăng tỉ lệ Exp Thiên Đạo up ra";
                                hieuung += "\n+Tăng thời gian khiên năng lượng lên: " + player.SagaThienDao / 90L
                                        + "s";
                                if (player.gender == 0) {
                                    hieuung += "\n+Tăng: " + player.SagaThienDao / 90L + "s Thời gian choáng.";
                                    hieuung += "\n+Tăng: " + Util.getFormatNumber(player.SagaThienDao / 100L)
                                            + "s Thời gian Thôi miên.";
                                }
                                if (player.gender == 1) {
                                    hieuung += "\n+Giảm Nữa thời gian hồi chiêu liên hoàn";
                                    hieuung += "\n+Tăng Sát thương chí mạng liên hoàn " + player.SagaThienDao + "%";
                                }
                                if (player.gender == 2) {
                                    hieuung += "\n+Tăng: " + player.SagaThienDao / 85L + "s Khi Khỉ.";
                                    hieuung += "\n+Tăng Giáp theo: " + player.SagaThienDao / 60L + "% Hp Khi Khỉ.";
                                }
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 4: {
                                String hieuung = "-Hiệu ứng Trên cấp 300:";
                                hieuung += "\n+Tăng tỉ lệ up Nhập Ma theo Tiên Bang";
                                hieuung += "\n+Tăng tỉ lệ số vàng up ra khi ở núi khỉ vàng";
                                hieuung += "\n+Tăng Exp Khai Thác ở map núi khỉ vàng khi up (hơi bị nhanh nhớ) tốc độ theo Tiên Bang";
                                hieuung += "\n+Tăng Exp tu tiên khi nhận được";

                                MenuBH(player, hieuung);
                                break;
                            }
                            case 5: {
                                String hieuung = "-Hiệu ứng Trên cấp 500:";
                                hieuung += "\n+Khi đổi đệ mới có tỉ lệ nhận đệ có skill 1 là liên hoàn.";
                                hieuung += "\n+Tăng tốc độ up của đệ.";
                                hieuung += "\n+Tăng " + player.SagaThienDao / 15L + "% Hp cho đệ.";
                                hieuung += "\n+Tăng " + player.SagaThienDao / 10L + "% Mp cho đệ.";
                                hieuung += "\n+Tăng " + player.SagaThienDao / 25L + "% Dame cho đệ.";
                                hieuung += "\n+Tăng " + player.SagaThienDao / 15L + "% Giáp cho đệ.";
                                hieuung += "\n+Khi chí mạng Tăng " + player.SagaThienDao / 20L
                                        + "% Sát thương đánh ra cho đệ.";
                                hieuung += "\n+Tăng Tỉ lệ chuyển sinh ở đệ";
                                MenuBH(player, hieuung);
                                break;
                            }
                            case 6: {
                                String hieuung = "-Hiệu ứng Trên cấp 700:";
                                hieuung += "\n+Giảm 90% thời gian các chiêu đấm trừ Liên hoàn.";
                                hieuung += "\n+Giảm " + player.SagaThienDao / 130 + "% Hp bịp.";
                                hieuung += "\n+Tăng tốc độ trưởng thành Chiến Thần theo Tiên Bang";
                                MenuBH(player, hieuung);
                                break;
                            }
                        }
                    }

                    case ConstNpc.SagaHieuUngdia -> {
                        switch (select) {
                            case 0: {
                                String hieuungNe = "-Hiệu ứng Cơ Bản:";
                                long tongSD = (player.SagaDiaDao) * 1000_000L;
                                hieuungNe += "\nTăng Sức Đánh: " + tongSD + "( 500k/Cấp)";
                                long tongSDDeTu = (player.SagaDiaDao) * 42_000L;
                                hieuungNe += "\nTăng Sức Đánh Đệ Tử: " + tongSDDeTu;

                                if (player.SagaDiaDao >= 1) {
                                    hieuungNe += "\n+HP,KI: " + player.SagaDiaDao + "% " + "/Cấp";
                                }
                                MenuDiaDao(player, hieuungNe);
                                break;
                            }
                            case 1: {
                                String hieuungNe = "-Hiệu ứng Thêm Từ cấp 30 Cộng Dồn:";
                                if (player.gender == 0) {
                                    hieuungNe += "\n+Kamejoko Bộc Phá: " + player.SagaDiaDao + "% SD.";
                                    hieuungNe += "\n+Kamejoko Super: " + player.SagaDiaDao * 280L + "% SD.";
                                }
                                if (player.gender == 1) {
                                    hieuungNe += "\n+Mesenco Siêu Max: " + player.SagaDiaDao * 80L + "% SD.";
                                    hieuungNe += "\n+Ma Phong Ba Pro: " + player.SagaDiaDao * 580L + "% SD.";
                                }
                                if (player.gender == 2) {
                                    hieuungNe += "\n+Antomic Xuyên Thấu: " + player.SagaDiaDao * 30L + "% SD.";
                                    hieuungNe += "\n+Liên Hoàn Chưởng: " + player.SagaDiaDao * 500L + "% SD.";

                                }
                                MenuDiaDao(player, hieuungNe);
                                break;
                            }

                            case 2: {
                                String hieuungNe = "-Hiệu ứng Phụ Từ Cấp 30 Cộng Dồn:";
                                hieuungNe += "\n+Tăng Tỉ Lệ Nhận May Mắn Trong Cuộc Sống";
                                hieuungNe += "\n+Tăng Tỉ lệ Kiếm Tiền Trong Tương Lai";
                                hieuungNe += "\n+Tăng Tỉ Lệ Win Trong Tài Xỉu";
                                hieuungNe += "\n+Tăng Độ Nói Phét Trong Game.";
                                hieuungNe += "\n+Tăng Tiến Độ Nạp Game Cho ad";
                                MenuDiaDao(player, hieuungNe);
                                break;

                            }
                        }
                    }
                    case ConstNpc.SagaChuyenSinh -> {
                        switch (select) {
                            case 0: {
                                if (player.SagaTuTien[1] < panel.tuning.SystemTuning.getI("cs_tutien_yeucau")) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã Đạt Trúc Cơ Đỉnh Phong Để Mở Khóa Cs.");
                                    return;
                                }
                                int coin = 0;
                                if (player.getSession().vnd < coin) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần ít nhất "
                                            + Util.FormatNumber(coin)
                                            + " 2k Coin");
                                    return;
                                }
                                Item luongbac = null;
                                luongbac = InventoryService.gI().findItemBag(player, 1271);
                                if (luongbac == null || luongbac.quantity < panel.tuning.SystemTuning.getI("cs_luong_bac")) {

                                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần ít Nhất 50000 Lượng Bạc Trở Lên");
                                    return;
                                }

                                long vangth = panel.tuning.SystemTuning.getL("cs_vang");
                                if (player.nPoint.power < panel.tuning.SystemTuning.getL("cs_sm_yeucau")) {
                                    Service.getInstance()
                                            .sendThongBaoFromAdmin(player, "Bạn cần: 150k Tỷ Sức Mạnh Để Chuyển Sinh "
                                            );
                                    return;
                                }
                                if (player.inventory.gold < vangth) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần: " + Util.FormatNumber(vangth - player.inventory.gold) + " vàng nx để thực hiện.");
                                    return;
                                }
                                PlayerDAO.subcash(player, coin);
                                InventoryService.gI().subLuongBac(player, panel.tuning.SystemTuning.getI("cs_luong_bac_tru"));
                                player.inventory.gold -= vangth;
                                if (Util.isTrue(panel.tuning.SystemTuning.getI("cs_tile_thanh_cong"), 100)) {
                                    player.nPoint.power = 0;
                                    player.nPoint.tiemNang = 0;
                                    player.SagaChuyenSinh++;
                                    player.nPoint.hpg += panel.tuning.SystemTuning.getL("cs_hp_goc");
                                    player.nPoint.mpg += panel.tuning.SystemTuning.getL("cs_ki_goc");
                                    player.nPoint.dameg += panel.tuning.SystemTuning.getL("cs_sd_goc");
                                    player.nPoint.defg += panel.tuning.SystemTuning.getL("cs_giap_goc");
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Bạn Đã Chuyển Sinh Thành Công!\nBạn được:\n500k Hp Ki gốc\n50k dame gốc\n1000 giáp gốc\n"
                                            + "+52% HP KI SD Vào Hiệu Ứng Thiên Đạo /1 Cấp\n"
                                            + "\nBạn Chuyển Sinh Được: " + Util.cap(player.SagaChuyenSinh) + "  Cấp Thành Côngღ\n"
                                            + "ღVui Lòng Thoát Game Vào Lại Để Không Lỗi Tài Khoảnღ");
                                } else {
                                    player.nPoint.power = 0;
                                    player.nPoint.tiemNang = 0;
                                    Service.gI().point(player);

                                    Service.getInstance().sendThongBao(player,
                                            "Bạn Chuyển Sinh  Thất bại!");
                                }

                                break;
                            }

                            case 1: {
                                if (player.pet == null) {
                                    return;
                                }
                                Item luongbac = null;
                                luongbac = InventoryService.gI().findItemBag(player, 1271);
                                if (luongbac == null || luongbac.quantity < panel.tuning.SystemTuning.getI("cs_luong_bac")) {

                                    Service.gI().sendThongBaoFromAdmin(player, "Bạn Cần ít Nhất 50000 Lượng Bạc Trở Lên");
                                    return;
                                }
                                if (player.SagaTuTien[1] < panel.tuning.SystemTuning.getI("cspet_tutien_yeucau")) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Đã Đạt Chuẩn Đế Cảnh Đâu mà Chuyển Sinh.");
                                    return;
                                }
                                long DiemFam = panel.tuning.SystemTuning.getL("cspet_diem_fam");
                                if (player.diemfam < DiemFam + (player.SagaChuyenSinh * 2)) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần ít nhất "
                                            + Util.FormatNumber(DiemFam + (player.SagaChuyenSinh * 2))
                                            + " Điểm Fam");
                                    return;
                                }
                                long vangth = panel.tuning.SystemTuning.getL("cspet_vang");
                                if (player.nPoint.power < Long.MAX_VALUE) {
                                    Service.getInstance()
                                            .sendThongBaoFromAdmin(player, "Bạn cần: 9 Tỷ Tỷ Sức Mạnh Để Chuyển Sinh "
                                            );
                                    return;
                                }
                                if (player.inventory.gold < vangth) {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn cần: " + Util.FormatNumber(vangth - player.inventory.gold) + " vàng nx để thực hiện.");
                                    return;
                                }

                                player.diemfam -= DiemFam;
                                player.inventory.gold -= vangth;
                                Service.getInstance().sendMoney(player);
                                InventoryService.gI().subLuongBac(player, panel.tuning.SystemTuning.getI("cspet_luong_bac"));
                                if (Util.isTrue(panel.tuning.SystemTuning.getI("cspet_tile_thanh_cong")
                                        + (player.SagaThienDao >= 5000 ? panel.tuning.SystemTuning.getI("cspet_bonus_thiendo") : 0f), 50)) {
                                    player.pet.nPoint.power = 0;
                                    player.pet.nPoint.tiemNang = 0;
                                    player.pet.SagaChuyenSinh++;
                                    player.pet.nPoint.hpg += panel.tuning.SystemTuning.getL("cspet_hp_goc");
                                    player.pet.nPoint.mpg += panel.tuning.SystemTuning.getL("cspet_ki_goc");
                                    player.pet.nPoint.dameg += panel.tuning.SystemTuning.getL("cspet_sd_goc");
                                    player.pet.nPoint.defg += panel.tuning.SystemTuning.getL("cspet_giap_goc");
                                    Service.getInstance().point(player.pet);
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Bạn Đã Chuyển Sinh cho đệ thành công! đệ được:\n50k Hp Ki gốc\n1k dame gốc\n100 giáp gốc");
                                } else {
                                    player.nPoint.power = 0;
                                    player.nPoint.tiemNang = 0;
                                    Service.gI().point(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn Chuyển Sinh cho đệ Thất bại!");
                                }
                                break;
                            }
                        }
                    }

                    case ConstNpc.Saga_EmBe -> {
                        if (player.TamkjllPetGiong != -1) {
                            switch (select) {
                                case 0:
                                    long chiPhiAn = panel.tuning.SystemTuning.getL("embe_cho_an_linh_khi");
                                    if (player.SagaTuTien[0] < chiPhiAn) {
                                        Service.gI().sendThongBaoOK(player,
                                                "Mày Không Đủ " + Util.getFormatNumber(chiPhiAn)
                                                + " Linh Khí Cho EmBe ăn");
                                        return;
                                    }
                                    player.SagaTuTien[0] -= chiPhiAn;
                                    player.TamkjllPetHunger += Util.nextInt(
                                            panel.tuning.SystemTuning.getI("embe_cho_an_min"),
                                            panel.tuning.SystemTuning.getI("embe_cho_an_max"));
                                    if (player.TamkjllPetHunger > panel.tuning.SystemTuning.getI("embe_thuc_an_toi_da")) {
                                        player.TamkjllPetGiong = -1;
                                        Service.gI().sendThongBaoOK(player,
                                                "Mày Cho ăn No Qua Em Bé Bạo Tử Mà Chết Rồi.");
                                    } else {
                                        Service.gI().sendThongBaoOK(player,
                                                "Thức Ăn Em Bé: " + player.TamkjllPetHunger
                                                + "%\nBạn đã Cho Em Bé ăn hãy thoát game vào lại hoặc đợi 15 phút để load\nLưu ý: khi cho quá "
                                                + panel.tuning.SystemTuning.getI("embe_thuc_an_toi_da")
                                                + "% Em Bé sẽ no quá mà chết");
                                    }
                                    break;
                                case 1:
                                    player.tamkjllpet.changeStatus(Tamkjll_Pet.FOLLOW);
                                    break;
                                case 2:
                                    player.tamkjllpet.changeStatus(Tamkjll_Pet.ATTACK_PLAYER);
                                    player.tamkjllpet.effectSkill.removeSkillEffectWhenDie();
                                    Service.gI().sendThongBaoOK(player, "Đã Xóa Trạng Thái Bất Lợi Cho Em Bé");
                                    break;
                                case 3:
                                    player.tamkjllpet.changeStatus(Tamkjll_Pet.ATTACK_MOB);
                                    break;
                                case 4:
                                    player.tamkjllpet.changeStatus(Tamkjll_Pet.GOHOME);
                                    break;
                                case 5:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                            "|7|Kỹ Năng Em Bé " + player.TamkjllNamePet + "ღ\n"
                                            + "|6|Kỹ Năng: " + player.KyNangEmBe(player.TamkjllPetGiong),
                                            "Đã Hiểu"
                                    );
                                    break;

                            }
                        } else {
                            Service.gI().sendThongBaoOK(player, "Bạn Chưa Kết Hôn Sinh Bé Có Nit Dùng A");
                        }
                    }

                    case ConstNpc.MENU_ADMIN -> {
                        switch (select) {
                            case 0 -> {
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryService.gI().addItemBag(player, item, 999999);
                                }
                                InventoryService.gI().sendItemBag(player);
                            }
                            case 1 -> {
                                PetService.gI().createNormalPet(player, player.gender);
                            }
                            case 2 -> {
                                if (player.isAdmin()) {
                                    System.out.println(player.name + " Đang bảo trì game!");
                                    Maintenance.gI().start(30);
                                }
                            }
                            case 3 ->
                                Input.gI().createFormFindPlayer(player);
                            case 4 ->
                                BossManager.gI().showListBoss(player);
                            case 5 ->
                                BossManager.gI().createBoss(BossID.SUPER_BROLY);
                        }
                    }

                    case ConstNpc.maydoboss -> {
                        switch (select) {
                            case 0 ->
                                OtherBossManager.gI().showListBoss(player);
                            case 1 ->
                                FinalBossManager.gI().showListBoss(player);
                            case 2 ->
                                LunarNewYearEventManager.gI().showListBoss(player);
                            case 3 ->
                                RedRibbonHQManager.gI().showListBoss(player);
                            case 4 ->
                                TreasureUnderSeaManager.gI().showListBoss(player);
                            case 5 ->
                                SkillSummonedManager.gI().showListBoss(player);

                        }
                    }

                    case ConstNpc.SUKIENXUYENKO -> {
                        switch (select) {
                            case 0 ->
                                ChangeMapService.gI().goTokhongian(player);
                        }
                    }
                    case ConstNpc.LUYENHON -> {
                        switch (select) {
                            case 0: {
                                if (InventoryService.gI().getCountEmptyBag(player) > 5) {
                                    Item diqua1 = InventoryService.gI().findItemBag(player, 1611);
                                    Item diqua2 = InventoryService.gI().findItemBag(player, 1612);
                                    Item diqua3 = InventoryService.gI().findItemBag(player, 1613);
                                    Item diqua4 = InventoryService.gI().findItemBag(player, 1614);
                                    Item diqua5 = InventoryService.gI().findItemBag(player, 1615);
                                    Item diqua6 = InventoryService.gI().findItemBag(player, 1616);
                                    Item zenni = InventoryService.gI().findItemBag(player, 457);
                                    Item fancung = InventoryService.gI().findItemBag(player, 1027);

                                    if (diqua1 != null && diqua2 != null && diqua3 != null && diqua4 != null
                                            && diqua5 != null && diqua6 != null && zenni != null && fancung != null
                                            && diqua1.quantity >= 999 && diqua2.quantity >= 999 && diqua3.quantity >= 999
                                            && diqua4.quantity >= 999 && diqua5.quantity >= 999 && diqua6.quantity >= 999
                                            && zenni.quantity >= 50000 && fancung.quantity >= 999) {

                                        Item ramdomhondetu = ItemService.gI().createNewItem((short) Util.nextInt(1639, 1644), 1);

                                        Item hopqua = ItemService.gI().createNewItem((short) 1645, 1);

                                        ramdomhondetu.itemOptions.add(new ItemOption(72, 1));
                                        hopqua.itemOptions.add(new ItemOption(72, 1));

                                        if (Util.isTrue(98, 100)) {
                                            InventoryService.gI().addItemBag(player, hopqua, 99999999);
                                            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Được Hộp quà.+ Điểm Sự kiện.");
                                        } else {
                                            InventoryService.gI().addItemBag(player, ramdomhondetu, 99999999);
                                            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Được Hồn Đệ  Gohan.+ Điểm Sự kiện.");
                                        }
                                        InventoryService.gI().subQuantityItemsBag(player, diqua1, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua2, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua3, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua4, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua5, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua6, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, zenni, 50000);
                                        InventoryService.gI().subQuantityItemsBag(player, fancung, 999);

                                        InventoryService.gI().sendItemBag(player);
                                        Service.gI().sendMoney(player);
                                        player.sukien += 50;
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Không Đủ 6 loại Dị quả\nHoặc 50k Zenni + 999 Fancung");
                                    }
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Hãy chừa 5 ô trống để mở.");
                                }
                                break;
                            }

                            case 1: {
                                if (InventoryService.gI().getCountEmptyBag(player) > 5) {
                                    Item diqua1 = InventoryService.gI().findItemBag(player, 1611);
                                    Item diqua2 = InventoryService.gI().findItemBag(player, 1612);
                                    Item diqua3 = InventoryService.gI().findItemBag(player, 1613);
                                    Item diqua4 = InventoryService.gI().findItemBag(player, 1614);
                                    Item diqua5 = InventoryService.gI().findItemBag(player, 1615);
                                    Item diqua6 = InventoryService.gI().findItemBag(player, 1616);
                                    Item zenni = InventoryService.gI().findItemBag(player, 457);
                                    Item fancung = InventoryService.gI().findItemBag(player, 1150);

                                    if (diqua1 != null && diqua2 != null && diqua3 != null && diqua4 != null
                                            && diqua5 != null && diqua6 != null && zenni != null && fancung != null
                                            && diqua1.quantity >= 999 && diqua2.quantity >= 999 && diqua3.quantity >= 999
                                            && diqua4.quantity >= 999 && diqua5.quantity >= 999 && diqua6.quantity >= 999
                                            && zenni.quantity >= 50000 && fancung.quantity >= 2000000) {

                                        Item ruonggohanvip = ItemService.gI().createNewItem((short) 1646, 1);
                                        Item hopquapet = ItemService.gI().createNewItem((short) 1698, 1);
                                        ruonggohanvip.itemOptions.add(new ItemOption(72, 1));
                                        hopquapet.itemOptions.add(new ItemOption(72, 1));

                                        if (Util.isTrue(95, 100)) {
                                            InventoryService.gI().addItemBag(player, ruonggohanvip, 99999999);
                                            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Được Hộp quà CaiTrang + Điểm Sự kiện.");
                                        } else {
                                            InventoryService.gI().addItemBag(player, hopquapet, 99999999);
                                            player.sukien += Util.nextInt(50, 500);
                                            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Được Hop qua pet.+ Điểm Sự kiện.");
                                        }
                                        InventoryService.gI().subQuantityItemsBag(player, diqua1, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua2, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua3, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua4, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua5, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, diqua6, 999);
                                        InventoryService.gI().subQuantityItemsBag(player, zenni, 50000);
                                        InventoryService.gI().subQuantityItemsBag(player, fancung, 2000000);

                                        InventoryService.gI().sendItemBag(player);
                                        Service.gI().sendMoney(player);
                                    } else {
                                        Service.getInstance().sendThongBaoFromAdmin(player, "Không Đủ 6 loại Dị quả\nHoặc 50k Zenni + 2M cỏ 4 lá.");
                                    }
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Hãy chừa 5 ô trống để mở.");
                                }
                                break;
                            }

                            case 2: {
                                // Code case 2 nếu có
                                break;
                            }

                            default: {
                                this.npcChat(player, "Lựa chọn không hợp lệ!");
                                break;
                            }
                        }
                    }

                    case ConstNpc.checkthongtin -> {

                        switch (select) {

                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                        "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                        + "|7|Hp: " + Util.FormatNumber(player.nPoint.hpMax)
                                        //    + Util.FormatNumber(player.nPoint.hpMax)
                                        + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mpMax)
                                        //   + Util.FormatNumber(player.nPoint.mpMax)
                                        + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                        + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                        + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                        + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                        + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                        + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                        + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                        "Ok"
                                );
                                break;

                            case 1:
                                if (player.pet != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                            "|7|Công Ty Trách Nhiệm Hữu Hạn 1 Thành Viên: @Admin\n"
                                            + "|7|Hp: " + Util.FormatNumber(player.nPoint.hpMax)
                                            //    + Util.FormatNumber(player.nPoint.hpMax)
                                            + "\n|2|Ki: " + Util.FormatNumber(player.nPoint.mpMax)
                                            //   + Util.FormatNumber(player.nPoint.mpMax)
                                            + "\n|4|Dame: " + Util.FormatNumber(player.nPoint.dame)
                                            + "\n|8|Giáp: " + Util.FormatNumber(player.nPoint.def)
                                            + "\n-Tiềm năng: " + Util.FormatNumber(player.nPoint.tiemNang)
                                            + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                            + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                            + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                            + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                            "OK");
                                }
                                break;

                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.PKALL, -1,
                                        "Xin Chào Kiếm Sĩ Số 1 Võ Lâm\n"
                                        + "|8|Minh Chủ Lệnh\n"
                                        + "|7|Quyền Đại Khai Sát Gioi Võ Lâm\n",
                                        "Bật Tắt\n Đồ Sát"
                                );
                                break;

                        }
                    }

                    case ConstNpc.PKALL -> {
                        if (player.typePk == ConstPlayer.NON_PK) {
                            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                            Service.getInstance().sendThongBao(player, "|7|Bật tàn sát");
                        } else {
                            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                            Service.getInstance().sendThongBao(player, "|7|Tắt tàn sát");
                        }
                    }

                    case ConstNpc.ghepmanhtrung -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1160);
                            if (manhTrung != null && manhTrung.quantity >= 999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1158, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1159, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(72, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(72, 1));
                                if (Util.isTrue(90, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x999 Mảnh Trứng Để Ghép.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.gheptrungmabu -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1212);
                            if (manhTrung != null && manhTrung.quantity >= 999) {

                                Item KIDBU = ItemService.gI().createNewItem((short) 1691, 1);
                                Item KIDFIDE = ItemService.gI().createNewItem((short) 1300, 1);
                                Item KIDUUB = ItemService.gI().createNewItem((short) 1301, 1);
                                Item KIDXEN = ItemService.gI().createNewItem((short) 1302, 1);

                                KIDBU.itemOptions.add(new Item.ItemOption(72, 1));
                                KIDFIDE.itemOptions.add(new Item.ItemOption(72, 1));
                                KIDUUB.itemOptions.add(new Item.ItemOption(72, 1));
                                KIDXEN.itemOptions.add(new Item.ItemOption(72, 1));

                                if (Util.isTrue(60, 100)) {
                                    InventoryService.gI().addItemBag(player, KIDXEN, 999999);

                                }
                                if (Util.isTrue(20, 100)) {
                                    InventoryService.gI().addItemBag(player, KIDFIDE, 999999);
                                }
                                if (Util.isTrue(10, 100)) {
                                    InventoryService.gI().addItemBag(player, KIDUUB, 999999);

                                }
                                if (Util.isTrue(5, 100)) {
                                    InventoryService.gI().addItemBag(player, KIDBU, 999999);

                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x999 Mảnh Trứng Để Ghép.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.ghepsachchiendau -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1169);
                            if (manhTrung != null && manhTrung.quantity >= 5000) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1170, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1211, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 71), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(59, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(149, 152), 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(153, 157), 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 71), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(59, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10000)));
                                trungVip.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 10000)));
                                trungVip.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 10000)));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(149, 152), 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(153, 157), 1));

                                if (Util.isTrue(90, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 5000);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x5000 Mảnh Sách Chiến Đấu.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.ghepmatgiatoc -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1189);
                            if (manhTrung != null && manhTrung.quantity >= 5000) {
                                Item trungThuong = ItemService.gI().createNewItem((short) Util.nextInt(1179, 1188), 1);
                                Item trungVip = ItemService.gI().createNewItem((short) Util.nextInt(1179, 1188), 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 71), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(59, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 10000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(149, 152), 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(153, 157), 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 71), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(59, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 20000)));
                                trungVip.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 20000)));
                                trungVip.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 20000)));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(149, 152), 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(153, 157), 1));
                                if (Util.isTrue(80, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 5000);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x5000 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPAO -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1295);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 650, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1048, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 69), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(158, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(47, Util.nextInt(1, 5000000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(75, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(57, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(249, 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(251, 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 70), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(158, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(47, Util.nextInt(10000, 10000000)));
                                trungVip.itemOptions.add(new Item.ItemOption(75, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(57, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(249, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(251, 1));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPQUAN -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1296);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 651, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1051, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 69), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(158, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(1, 10000000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(75, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(57, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(249, 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(251, 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 70), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(158, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(6, Util.nextInt(300000, 20000000)));
                                trungVip.itemOptions.add(new Item.ItemOption(75, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(57, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(249, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(251, 1));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPGANG -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1297);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 657, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1054, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 69), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(158, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(1, 100000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(75, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(57, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(249, 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(251, 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 70), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(158, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(0, Util.nextInt(1000, 200000)));
                                trungVip.itemOptions.add(new Item.ItemOption(75, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(57, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(249, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(251, 1));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPGIAY -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1298);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 658, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1057, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 69), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(158, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(1, 10000000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(75, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(57, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(249, 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(251, 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 70), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(158, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(7, Util.nextInt(300000, 20000000)));
                                trungVip.itemOptions.add(new Item.ItemOption(75, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(57, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(249, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(251, 1));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPNHAN -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1299);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 656, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1060, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 69), 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(158, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 10)));
                                trungThuong.itemOptions.add(new Item.ItemOption(75, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(57, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(60, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(249, 1));
                                trungThuong.itemOptions.add(new Item.ItemOption(61, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(251, 1));

                                trungVip.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 70), 0));
                                trungVip.itemOptions.add(new Item.ItemOption(158, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 20)));
                                trungVip.itemOptions.add(new Item.ItemOption(75, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(57, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(60, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(249, 1));
                                trungVip.itemOptions.add(new Item.ItemOption(61, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(251, 1));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPAOfree -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1066);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1679, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1679, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(47, Util.nextInt(1, 50000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(55, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(30, 8888));

                                trungVip.itemOptions.add(new Item.ItemOption(47, Util.nextInt(10, 10000)));
                                trungVip.itemOptions.add(new Item.ItemOption(55, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(30, 0));

                                if (Util.isTrue(50, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPQUANfree -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1067);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1680, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1680, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(1, 500000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(55, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(30, 8888));

                                trungVip.itemOptions.add(new Item.ItemOption(6, Util.nextInt(10, 500000)));
                                trungVip.itemOptions.add(new Item.ItemOption(55, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(30, 0));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPGANGfree -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1070);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1681, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1681, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(10000, 50000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(55, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(30, 8888));

                                trungVip.itemOptions.add(new Item.ItemOption(0, Util.nextInt(10000, 50000)));
                                trungVip.itemOptions.add(new Item.ItemOption(55, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(30, 0));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPGIAYfree -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1068);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1682, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1682, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(10000, 500000)));
                                trungThuong.itemOptions.add(new Item.ItemOption(55, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(30, 8888));

                                trungVip.itemOptions.add(new Item.ItemOption(7, Util.nextInt(10000, 500000)));
                                trungVip.itemOptions.add(new Item.ItemOption(55, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(30, 0));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.GHEPNHANfree -> {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item manhTrung = InventoryService.gI().findItemBag(player, 1069);
                            if (manhTrung != null && manhTrung.quantity >= 9999) {
                                Item trungThuong = ItemService.gI().createNewItem((short) 1683, 1);
                                Item trungVip = ItemService.gI().createNewItem((short) 1683, 1);
                                trungThuong.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 12)));
                                trungThuong.itemOptions.add(new Item.ItemOption(55, 0));
                                trungThuong.itemOptions.add(new Item.ItemOption(30, 8888));

                                trungVip.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 12)));
                                trungVip.itemOptions.add(new Item.ItemOption(55, 0));
                                trungVip.itemOptions.add(new Item.ItemOption(30, 0));

                                if (Util.isTrue(95, 100)) {
                                    InventoryService.gI().addItemBag(player, trungThuong, 999999);

                                } else {
                                    InventoryService.gI().addItemBag(player, trungVip, 999999);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, manhTrung, 9999);
                                InventoryService.gI().sendItemBag(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần x9999 Mảnh .");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy chừa 1 ô trống để mở.");
                        }
                    }

                    case ConstNpc.doitrangbi -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 555, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 556, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 562, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(1, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 563, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 559, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(10, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 560, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 566, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(100, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 567, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 557, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 558, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 564, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(100, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 565, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 561, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 650, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 651, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 657, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(1, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 658, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 654, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 655, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 661, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(100, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 662, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 652, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 653, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 659, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(100, 50000)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 660, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(10000, 500000)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 656, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 15)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.NHANGEM -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 100000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 600000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 500000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(10, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 600000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (600000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 800000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 800000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (800000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 1500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (1500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 5000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 2000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (2000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 3800000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 10000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 3800000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (3800000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.NHANRUBY -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 100000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 6000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 500000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(10, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 6000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (6000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 8000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 8000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (8000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 15000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 15000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (15000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 20000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 5000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 38000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 10000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 38000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (38000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }
                    case ConstNpc.NHANVANG -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 100000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 6000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 500000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(10, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 6000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (6000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 8000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 8000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (8000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 15000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 15000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (15000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 20000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 5000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 38000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 10000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 38000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (38000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.NHANSAOPHA -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 10000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 441, 1);
                                    goiqua.itemOptions.add(new ItemOption(95, 10));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (10000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 20000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 442, 1);
                                    goiqua.itemOptions.add(new ItemOption(96, 10));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 20000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 30000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 443, 1);
                                    goiqua.itemOptions.add(new ItemOption(98, 20));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 30000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (30000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 40000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 444, 1);
                                    goiqua.itemOptions.add(new ItemOption(99, 20));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 40000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (40000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 445, 1);
                                    goiqua.itemOptions.add(new ItemOption(101, 20));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                        }
                    }

                    case ConstNpc.NHANDANOITAI -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 100000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 6000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 500000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(10, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 6000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (6000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 8000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 8000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (8000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 15000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 15000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (15000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 20000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 5000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 38000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 10000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 38000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (38000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.HDTANTHU -> {
                        switch (select) {
                            case 0:
                                Service.getInstance().sendThongBaoFromAdmin(player,
                                        "|7|Hưỡng Dẫn Tân Thủ\n"
                                        + "|4|NPC Tân Thủ: UP điểm Fam ALL quái Ramdom 1-50 Điểm Để Đổi\n"
                                        + "NHIỆM VỤ: Hoàn Thành 1 Nhiệm Vụ +200K hp ki+ 100k sd(KO phải Gốc)\n"
                                        + "TIÊN BANG: up EXP Từ Fam quái/hít of săn boss - Đủ EXP Tự Lên Cấp\n"
                                        + "Nhập Ma: Cứ Di chuyển là lên EXP Đủ tự động lên cấp : 500k SD/Cấp\n"
                                        + "ĐỒ SKH: Dùng Máy dò skh Up all Map Rơi ALL đồ skh vip free\n"
                                        + "SKH VIP: Dùng Máy dò skhvip Up all map rơi Đồ skh chỉ số vip free\n"
                                        + "VIP: Up all quái nhận điểm vip of nâng bằng vnd + dame hpki/cấp vip\n"
                                        + "TUTIÊN:CHát tutien để đến map tu tiên up exp tu tiên , ra đảo kame độ kiếp\n"
                                        + "Tu Tiên Tăng 99% HPKISD/ Cảnh giới Max 999 Cảnh giới");

                                break;

                            case 1:
                                Service.getInstance().sendThongBaoFromAdmin(player,
                                        "|7|Hưỡng Dẫn Đập Đồ\n"
                                        + "|4|Full Đục: 18sao ALL Trang Bị- Phụ Kiện\n"
                                        + "Trang Bị Free Đục: 18Sao Cần Vàng + Ngọc \n"
                                        + "Phụ Kiện Đục: Free 18sao Cần Zenni+ Cỏ 4 Lá\n"
                                        + "Gỡ Sao: Gỡ Sao Hoàn Lại 1 Lỗ + Giu Nguyên chỉ số cũ\n"
                                        + "Tại Bà Hạt Mít Ngoài Đảo Kame");

                                break;

                            case 2:
                                if (player.diemfam >= 8000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 8000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (8000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 15000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(100, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 15000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (15000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 20000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 5000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 20000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 38000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1825, 10000000);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 15000)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);
                                    player.diemfam -= 38000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (38000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.doicaitrang -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(282, 292), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(405, 433), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(675, 681), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(729, 732), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 350000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(883, 885), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 350000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (350000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1246, 1250), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(93, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.doiphukien -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(467, 471), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 99)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(800, 805), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 199)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(675, 681), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 299)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(814, 817), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 399)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 350000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(994, 999), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 499)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(90, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 350000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (350000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(954, 955), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 999)));
                                    goiqua.itemOptions.add(new ItemOption(237, Util.nextInt(1, 2)));
                                    goiqua.itemOptions.add(new ItemOption(72, Util.nextInt(1, 6)));
                                    if (Util.isTrue(93, 100)) {
                                        goiqua.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                    }
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.doitainguyen -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 10000);
                                    // goiqua.itemOptions.add(new ItemOption(73, 0));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 14, 10);
                                    // goiqua.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 15, 10);
                                    //   goiqua1.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 16, 10);
                                    //  goiqua2.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 17, 10);
                                    //  goiqua3.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 18, 10);
                                    //  goiqua4.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua5 = ItemService.gI().createNewItem((short) 19, 10);
                                    //  goiqua5.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua5);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua6 = ItemService.gI().createNewItem((short) 20, 10);
                                    //   goiqua6.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua6);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 1500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 77, 500000);
                                    goiqua.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (1500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 2500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 861, 500000);
                                    goiqua.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 2500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (2500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 3500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), 9999);
                                    goiqua.itemOptions.add(new ItemOption(73, Util.nextInt(1, 6)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 3500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (3500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1150, 9999);
                                    //    goiqua.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (5000000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }
                    case ConstNpc.NapDau -> {
                        switch (select) {
                            case 0:
                                if (player.NapDau >= 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|Bạn đã Nhận 1 Trong 5\n quà Nạp Đầu Rồi!!!!");
                                    return;
                                }
                                if (player.getSession().tongnap >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1868, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(47, 100000));
                                    goiqua.itemOptions.add(new ItemOption(75, 0));
                                    goiqua.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1869, 1);
                                    goiqua1.itemOptions.add(new ItemOption(158, 0));
                                    goiqua1.itemOptions.add(new ItemOption(6, 5000000));
                                    goiqua1.itemOptions.add(new ItemOption(75, 0));
                                    goiqua1.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1870, 1);
                                    goiqua2.itemOptions.add(new ItemOption(158, 0));
                                    goiqua2.itemOptions.add(new ItemOption(0, 100000));
                                    goiqua2.itemOptions.add(new ItemOption(75, 0));
                                    goiqua2.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1871, 1);
                                    goiqua3.itemOptions.add(new ItemOption(158, 0));
                                    goiqua3.itemOptions.add(new ItemOption(7, 5000000));
                                    goiqua3.itemOptions.add(new ItemOption(75, 0));
                                    goiqua3.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1872, 1);
                                    goiqua4.itemOptions.add(new ItemOption(158, 0));
                                    goiqua4.itemOptions.add(new ItemOption(14, 5));
                                    goiqua4.itemOptions.add(new ItemOption(75, 0));
                                    goiqua4.itemOptions.add(new ItemOption(66, 0));

                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    InventoryService.gI().addItemMail(player, goiqua4);

                                    HomThuService.gI().sendListMail(player);
                                    player.NapDau += 1;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Đã Nạp Được Nghìn Nào Đéo Đâu\nCày Chay Thì Cày Đi");
                                }
                                break;

                            case 1:
                                if (player.NapDau >= 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|Bạn đã Nhận 1 Trong 5\n quà Nạp Đầu Rồi!!!!");
                                    return;
                                }
                                if (player.getSession().tongnap >= 200000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1868, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(47, 200000));
                                    goiqua.itemOptions.add(new ItemOption(75, 0));
                                    goiqua.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1869, 1);
                                    goiqua1.itemOptions.add(new ItemOption(158, 0));
                                    goiqua1.itemOptions.add(new ItemOption(6, 10000000));
                                    goiqua1.itemOptions.add(new ItemOption(75, 0));
                                    goiqua1.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1870, 1);
                                    goiqua2.itemOptions.add(new ItemOption(158, 0));
                                    goiqua2.itemOptions.add(new ItemOption(0, 200000));
                                    goiqua2.itemOptions.add(new ItemOption(75, 0));
                                    goiqua2.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1871, 1);
                                    goiqua3.itemOptions.add(new ItemOption(158, 0));
                                    goiqua3.itemOptions.add(new ItemOption(7, 10000000));
                                    goiqua3.itemOptions.add(new ItemOption(75, 0));
                                    goiqua3.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1872, 1);
                                    goiqua4.itemOptions.add(new ItemOption(158, 0));
                                    goiqua4.itemOptions.add(new ItemOption(14, 6));
                                    goiqua4.itemOptions.add(new ItemOption(75, 0));
                                    goiqua4.itemOptions.add(new ItemOption(66, 0));

                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);
                                    player.NapDau += 1;

                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Có Tiền Đéo Đâu");
                                }
                                break;

                            case 2:
                                if (player.NapDau >= 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|Bạn đã Nhận 1 Trong 5\n quà Nạp Đầu Rồi!!!!");
                                    return;
                                }
                                if (player.getSession().tongnap >= 500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1868, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(47, 500000));
                                    goiqua.itemOptions.add(new ItemOption(75, 0));
                                    goiqua.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1869, 1);
                                    goiqua1.itemOptions.add(new ItemOption(158, 0));
                                    goiqua1.itemOptions.add(new ItemOption(6, 25000000));
                                    goiqua1.itemOptions.add(new ItemOption(75, 0));
                                    goiqua1.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1870, 1);
                                    goiqua2.itemOptions.add(new ItemOption(158, 0));
                                    goiqua2.itemOptions.add(new ItemOption(0, 500000));
                                    goiqua2.itemOptions.add(new ItemOption(75, 0));
                                    goiqua2.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1871, 1);
                                    goiqua3.itemOptions.add(new ItemOption(158, 0));
                                    goiqua3.itemOptions.add(new ItemOption(7, 25000000));
                                    goiqua3.itemOptions.add(new ItemOption(75, 0));
                                    goiqua3.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1872, 1);
                                    goiqua4.itemOptions.add(new ItemOption(158, 0));
                                    goiqua4.itemOptions.add(new ItemOption(14, 8));
                                    goiqua4.itemOptions.add(new ItemOption(75, 0));
                                    goiqua4.itemOptions.add(new ItemOption(66, 0));

                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    InventoryService.gI().addItemMail(player, goiqua4);

                                    HomThuService.gI().sendListMail(player);
                                    player.NapDau += 1;

                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Đã Nạp Được Nghìn Nào Đéo Đâu\nCày Chay Thì Cày Đi");
                                }
                                break;
                            case 3:
                                if (player.NapDau >= 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|Bạn đã Nhận 1 Trong 5\n quà Nạp Đầu Rồi!!!!");
                                    return;
                                }
                                if (player.getSession().tongnap >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1868, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(47, 1000000));
                                    goiqua.itemOptions.add(new ItemOption(75, 0));
                                    goiqua.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1869, 1);
                                    goiqua1.itemOptions.add(new ItemOption(158, 0));
                                    goiqua1.itemOptions.add(new ItemOption(6, 59999999));
                                    goiqua1.itemOptions.add(new ItemOption(75, 0));
                                    goiqua1.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1870, 1);
                                    goiqua2.itemOptions.add(new ItemOption(158, 0));
                                    goiqua2.itemOptions.add(new ItemOption(0, 988888));
                                    goiqua2.itemOptions.add(new ItemOption(75, 0));
                                    goiqua2.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1871, 1);
                                    goiqua3.itemOptions.add(new ItemOption(158, 0));
                                    goiqua3.itemOptions.add(new ItemOption(7, 59999999));
                                    goiqua3.itemOptions.add(new ItemOption(75, 0));
                                    goiqua3.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1872, 1);
                                    goiqua4.itemOptions.add(new ItemOption(158, 0));
                                    goiqua4.itemOptions.add(new ItemOption(14, 10));
                                    goiqua4.itemOptions.add(new ItemOption(75, 0));
                                    goiqua4.itemOptions.add(new ItemOption(66, 0));

                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);
                                    player.NapDau += 1;

                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Đã Nạp Được Nghìn Nào Đéo Đâu\nCày Chay Thì Cày Đi");
                                }
                                break;
                            case 4:
                                if (player.NapDau >= 1) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|4|Bạn đã Nhận 1 Trong 5\n quà Nạp Đầu Rồi!!!!");
                                    return;
                                }
                                if (player.getSession().tongnap >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1868, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 50));
                                    goiqua.itemOptions.add(new ItemOption(47, 2000000));
                                    goiqua.itemOptions.add(new ItemOption(75, 0));
                                    goiqua.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1869, 1);
                                    goiqua1.itemOptions.add(new ItemOption(158, 50));
                                    goiqua1.itemOptions.add(new ItemOption(6, 188888888));
                                    goiqua1.itemOptions.add(new ItemOption(75, 0));
                                    goiqua1.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1870, 1);
                                    goiqua2.itemOptions.add(new ItemOption(158, 50));
                                    goiqua2.itemOptions.add(new ItemOption(0, 1800000));
                                    goiqua2.itemOptions.add(new ItemOption(75, 0));
                                    goiqua2.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1871, 1);
                                    goiqua3.itemOptions.add(new ItemOption(158, 50));
                                    goiqua3.itemOptions.add(new ItemOption(7, 188888888));
                                    goiqua3.itemOptions.add(new ItemOption(75, 0));
                                    goiqua3.itemOptions.add(new ItemOption(66, 0));

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1872, 1);
                                    goiqua4.itemOptions.add(new ItemOption(158, 50));
                                    goiqua4.itemOptions.add(new ItemOption(14, 11));
                                    goiqua4.itemOptions.add(new ItemOption(75, 0));
                                    goiqua4.itemOptions.add(new ItemOption(66, 0));

                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);
                                    player.NapDau += 1;

                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng ");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Đã Nạp Được Nghìn Nào Đéo Đâu\nCày Chay Thì Cày Đi");
                                }
                                break;
                        }
                    }

                    case ConstNpc.doisaophale -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 150000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 150000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (150000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 250000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 100)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 250000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (250000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 350000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(14, 1));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 350000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (350000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(5, 1));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (500000 - player.diemfam) + " Điểm Nữa");
                                }
                                break;
                        }
                    }
                    case ConstNpc.EventTrungThuDD -> {
                        // Doi diem su kien: TRU diem truoc -> moi phat qua (qua/rong do cua hang cai chinh tren panel)
                        EventSuKien.TrungThuService.gI().exchange(player, select);
                    }

                    case ConstNpc.DoiTanThu -> {
                        switch (select) {
                            case 0:
                                if (player.diemfam >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(220, 224), 100000);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 100k");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 5000k Điểm Fam");
                                }
                                break;

                            case 1:
                                if (player.diemfam >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 674, 100000);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua1.itemOptions.add(new ItemOption(50, Util.nextInt(1, 9999)));
                                    goiqua1.itemOptions.add(new ItemOption(77, Util.nextInt(1, 9999)));
                                    goiqua1.itemOptions.add(new ItemOption(103, Util.nextInt(1, 9999)));
                                    goiqua1.itemOptions.add(new ItemOption(245, Util.nextInt(1, 10)));
                                    goiqua1.itemOptions.add(new ItemOption(231, Util.nextInt(1, 9999)));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 10000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 100k");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 10M Điểm Fam");
                                }
                                break;

                            case 2:
                                if (player.diemfam >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1190, 10);
                                    goiqua.itemOptions.add(new ItemOption(50, 100));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1191, 10);
                                    goiqua1.itemOptions.add(new ItemOption(77, 100));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1192, 10);
                                    goiqua2.itemOptions.add(new ItemOption(103, 100));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1193, 10);
                                    goiqua3.itemOptions.add(new ItemOption(101, 100));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 5000k Điểm Fam");
                                }
                                break;
                            case 3:
                                if (player.diemfam >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 10000000;
                                    player.inventory.gem += 10000000;
                                    player.inventory.ruby += 10000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 10M Điểm Fam");
                                }
                                break;
                            case 4:
                                if (player.diemfam >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(1351, 1367), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(101, Util.nextInt(1, 1000)));
                                    goiqua.itemOptions.add(new ItemOption(107, Util.nextInt(1, 8)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 100k");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 5M Điểm Fam");
                                }
                                break;

                            case 5:
                                if (player.diemfam >= 7000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) Util.nextInt(968, 978), 1);
                                    goiqua.itemOptions.add(new ItemOption(50, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(77, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(103, Util.nextInt(1, 9999)));
                                    goiqua.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                    goiqua.itemOptions.add(new ItemOption(107, Util.nextInt(1, 8)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.diemfam -= 7000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 100k");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có điểm , Lười Vừa Thôi\n Cần 7M Điểm Fam");
                                }
                                break;

                        }
                    }

                    case ConstNpc.goidapdo -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 1000000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 50000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 50000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 50000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được ramdom cải trang  ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1174);
                                    sethuyetma.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(47, 100000000));
                                    sethuyetma.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1175);
                                    sethuyetma1.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 100000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1176);
                                    sethuyetma2.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 10000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1177);
                                    sethuyetma3.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 100000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1178);
                                    sethuyetma4.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 25));
                                    sethuyetma4.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    player.inventory.ruby += 100000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100tr Ruby ");
                                    player.inventory.gem += 100000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100tr Ngọc Xanh");

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 2000000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 100000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 1500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được ramdom cải trang  ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1174);
                                    sethuyetma.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(47, 200000000));
                                    sethuyetma.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1175);
                                    sethuyetma1.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 200000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1176);
                                    sethuyetma2.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 20000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1177);
                                    sethuyetma3.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 200000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1178);
                                    sethuyetma4.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 35));
                                    sethuyetma4.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    player.inventory.ruby += 200000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200tr Ruby ");
                                    player.inventory.gem += 200000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200tr Ngọc Xanh");

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 7000000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 500000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 500000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 500000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 1500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được ramdom cải trang  ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1174);
                                    sethuyetma.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(47, 500000000));
                                    sethuyetma.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1175);
                                    sethuyetma1.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 500000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1176);
                                    sethuyetma2.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 50000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1177);
                                    sethuyetma3.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 500000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1178);
                                    sethuyetma4.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 35));
                                    sethuyetma4.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    player.inventory.ruby += 500000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ruby ");
                                    player.inventory.gem += 500000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ngọc Xanh");

                                    player.point_PassVIP += 500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500k Điểm ");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 457, 15000000);
                                    goiqua.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item caitrangthoitrang = ItemService.gI().createNewItem((short) Util.nextInt(1573, 1578));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(50, 800000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(77, 800000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(103, 800000));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(78, 8500));
                                    caitrangthoitrang.itemOptions.add(new ItemOption(30, 1));
                                    InventoryService.gI().addItemBag(player, caitrangthoitrang, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được ramdom cải trang  ");

                                    Item sethuyetma = ItemService.gI().createNewItem((short) 1174);
                                    sethuyetma.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(47, 800000000));
                                    sethuyetma.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma1 = ItemService.gI().createNewItem((short) 1175);
                                    sethuyetma1.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(6, 800000000));
                                    sethuyetma1.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma1.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma2 = ItemService.gI().createNewItem((short) 1176);
                                    sethuyetma2.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(0, 80000000));
                                    sethuyetma2.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma2.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma2, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma3 = ItemService.gI().createNewItem((short) 1177);
                                    sethuyetma3.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(7, 800000000));
                                    sethuyetma3.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma3.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma3, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    Item sethuyetma4 = ItemService.gI().createNewItem((short) 1178);
                                    sethuyetma4.itemOptions.add(new ItemOption(158, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(14, 50));
                                    sethuyetma4.itemOptions.add(new ItemOption(75, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(66, 0));
                                    sethuyetma4.itemOptions.add(new ItemOption(35, 10));
                                    InventoryService.gI().addItemBag(player, sethuyetma4, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được sét Hắc Ám ");

                                    player.inventory.ruby += 500000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ruby ");
                                    player.inventory.gem += 500000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 500tr Ngọc Xanh");

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1m Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }

                    case ConstNpc.goichuyensinh -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1360, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(101, 100000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1855, 20);
                                    goiqua1.itemOptions.add(new ItemOption(158, 0));
                                    goiqua1.itemOptions.add(new ItemOption(101, 100000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.SagaThienDao += 99;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 20 cấp Địa Đạo");

                                    player.SagaDiaDao += 500;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 20 cấp Thiên Đạo");

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1360, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(101, 200000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1855, 50);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.SagaThienDao += 250;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 250 cấp Thiên Đạo");

                                    player.SagaDiaDao += 1200;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1200 cấp Địa Đạo");

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1360, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(101, 700000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1855, 120);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.SagaThienDao += 600;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 600 cấp Thiên Đạo");

                                    player.SagaDiaDao += 3000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 3000 cấp Địa Đạo");

                                    player.point_PassVIP += 700000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 700k Điểm ");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1360, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(101, 1000000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1855, 300);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.SagaThienDao += 1500;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1500 cấp Thiên Đạo");

                                    player.SagaDiaDao += 7000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 7000 cấp Địa Đạo");

                                    player.point_PassVIP += 1200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1m2 Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }

                    case ConstNpc.goichanmenh -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(50, 1000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 674, 1000000);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.diemfam += 5000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 5m điểm fam");

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(50, 1000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 674, 3000000);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.diemfam += 15000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 15m điểm fam");

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(50, 1000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 674, 8000000);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.diemfam += 50000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 50m điểm fam");

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1m Điểm xứ mệnh");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua.itemOptions.add(new ItemOption(158, 0));
                                    goiqua.itemOptions.add(new ItemOption(50, 1000));
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 674, 15000000);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.diemfam += 150000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 150m điểm fam");

                                    player.point_PassVIP += 1500000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1m5 Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }
                    case ConstNpc.chualanh -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1010, 10000000);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1763, 100);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1854, 10);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1010, 20000000);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1763, 200);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1854, 20);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1010, 70000000);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1763, 700);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1854, 70);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1000k Điểm ");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1010, 150000000);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1763, 1500);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1854, 150);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1000k Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }

                    case ConstNpc.goidetu -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1639, 1);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1748, 1);
                                    goiqua1.itemOptions.add(new ItemOption(50, 100000));
                                    goiqua1.itemOptions.add(new ItemOption(77, 100000));
                                    goiqua1.itemOptions.add(new ItemOption(103, 100000));
                                    goiqua1.itemOptions.add(new ItemOption(117, 1000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1640, 1);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1748, 1);
                                    goiqua1.itemOptions.add(new ItemOption(50, 200000));
                                    goiqua1.itemOptions.add(new ItemOption(77, 200000));
                                    goiqua1.itemOptions.add(new ItemOption(103, 200000));
                                    goiqua1.itemOptions.add(new ItemOption(117, 2000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1641, 1);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1748, 1);
                                    goiqua1.itemOptions.add(new ItemOption(50, 700000));
                                    goiqua1.itemOptions.add(new ItemOption(77, 700000));
                                    goiqua1.itemOptions.add(new ItemOption(103, 700000));
                                    goiqua1.itemOptions.add(new ItemOption(117, 7000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 700000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 700k Điểm ");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1642, 1);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1748, 1);
                                    goiqua1.itemOptions.add(new ItemOption(50, 1000000));
                                    goiqua1.itemOptions.add(new ItemOption(77, 1000000));
                                    goiqua1.itemOptions.add(new ItemOption(103, 1000000));
                                    goiqua1.itemOptions.add(new ItemOption(117, 10000));
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1000k Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }
                    case ConstNpc.goitambao -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 1000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1873, 999);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1874, 300);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 100k Điểm ");

                                    PlayerDAO.subcash(player, 1000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 2000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1873, 1999);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1874, 600);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 200000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 200k Điểm ");

                                    PlayerDAO.subcash(player, 2000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 2:
                                if (player.getSession().vnd >= 5000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1873, 5999);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1874, 800);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 700000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 700k Điểm ");

                                    PlayerDAO.subcash(player, 5000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                            case 3:
                                if (player.getSession().vnd >= 10000000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1873, 15999);
                                    InventoryService.gI().addItemBag(player, goiqua, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1874, 3000);
                                    InventoryService.gI().addItemBag(player, goiqua1, 2000000000000000l);
                                    InventoryService.gI().sendItemBag(player);

                                    player.point_PassVIP += 1000000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 1000k Điểm ");

                                    PlayerDAO.subcash(player, 10000000);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Đã Hết Tiền");
                                }
                                break;

                        }
                    }

                    case ConstNpc.ThoMo -> {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 132, 506, 360);
                                break;

                            case 1:
                                if (player.point_vip >= 100000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1558, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 100000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (100000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;

                            case 2:
                                if (player.point_vip >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1557, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 50000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.point_vip >= 30000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1556, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 30000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (30000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.point_vip >= 20000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1555, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1225, 500);
                                    goiqua1.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 20000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.point_vip >= 5000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1554, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 5000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (5000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.doihanhtinhde -> {
                        switch (select) {
                            case 0: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }

                                    player.pet.gender = 0;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Trái Đất thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không Đủ 100k Điểm Fam");
                                }
                                break;

                            }
                            case 1: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }

                                    player.pet.gender = 2;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Namec thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không Đủ 100k Điểm Fam");
                                }
                                break;
                            }

                            case 2: {
                                if (player.diemfam >= 100000) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn Chưa có đệ!");
                                        break;
                                    }
                                    player.pet.gender = 1;
                                    player.diemfam -= 100000;
                                    ChangeMapService.gI().exitMap(player.pet);
                                    Service.gI().sendThongBaoFromAdmin(player, "Đổi hành tinh đệ Xayda thành công!");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không Đủ 100k Điểm Fam");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBao(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                    }

                    case ConstNpc.doiskill2detu -> {
                        switch (select) {
                            case 0: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.Saga_VIP >= 7) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(0, SkillUtil.createSkill(Skill.LIEN_HOAN, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill  Liên Hoàn!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm! Hoặc Đạt Vip 7 Để Đổi Liên Hoàn ");
                                }
                                break;
                            }

                            case 1: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 2000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.KAMEJOKO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 Kamejoko!");

                                    // Trừ điểm farm
                                    player.diemfam -= 2000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 2000k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 2: {
                                if (player.diemfam >= 2000000 && player.nPoint.power >= 10000000000000000000d) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.ANTOMIC, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 ATOMIC!");

                                    player.diemfam -= 2000000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 2000k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 3: {
                                if (player.diemfam >= 2000000 && player.nPoint.power >= 10000000000000000000d) {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }
                                    player.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.MASENKO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 2 MASENKO!");

                                    player.diemfam -= 2000000;
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 2000k Điểm Farm hoặc chưa Mở Skil 2!");
                                }
                                break;
                            }

                            case 4: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 THAI_DUONG_HA_SAN!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            case 5: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.TROI, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 Trói!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            case 6: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 3 TAI_TAO_NANG_LUONG!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 3!");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBaoFromAdmin(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                    }

                    case ConstNpc.doiskill3detu -> {
                        switch (select) {

                            case 0: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.DE_TRUNG, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 DE_TRUNG!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 1: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 QUA_CAU_KENH_KHI!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 2: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.HUYT_SAO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 4 HUYT_SAO!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 4!");
                                }
                                break;
                            }

                            case 3: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.SUPER_KAME, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 SUPER_KAME!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 4: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.MA_PHONG_BA, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 MAFUBA!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 5: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG, 9));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 5 SUPER_ANTOMIC!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 5!");
                                }
                                break;
                            }

                            case 6: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.BIEN_KHI, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 BIEN_KHI!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            case 7: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.MAKANKOSAPPO, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 MAKANKOSAPPO!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            case 8: {
                                // Kiểm tra nếu cả hai điều kiện đều cần đúng (&&)
                                if (player.diemfam >= 5000000 && player.nPoint.power >= 10000000000000000000d) {

                                    if (player.pet == null) {
                                        Service.gI().sendThongBaoFromAdmin(player, "Bạn chưa có đệ!");
                                        return;
                                    }

                                    // Mở skill
                                    player.pet.playerSkill.skills.set(5, SkillUtil.createSkill(Skill.SOCOLA, 7));
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Đã Mở Skill 6 SOCOLA!");

                                    // Trừ điểm farm
                                    player.diemfam -= 5000000;

                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Không đủ 5000k Điểm Farm hoặc chưa Mở Skil 6!");
                                }
                                break;
                            }

                            default:
                                Service.gI().sendThongBaoFromAdmin(player, "Lựa chọn không hợp lệ!");
                                break;
                        }
                    }

                    case ConstNpc.KETHON_PLAYER -> {
                        if (select == 0) {
                            Item NhanKetHon = null;
                            try {
                                NhanKetHon = InventoryService.gI().findItemBag(player, 1213);
                            } catch (Exception e) {
                            }
                            if (NhanKetHon == null || NhanKetHon.quantity <= 0) {
                                Service.getInstance().sendThongBao(player, "Bạn không có Nhẫn cầu hôn");
                            } else {
                                player.dakethon++;
                                ((Player) PLAYERID_OBJECT.get(player.id)).duockethon++;
                                Service.getInstance().sendThongBaoFromAdmin((Player) PLAYERID_OBJECT.get(player.id), "Bạn đã được " + player.name + " kết hôn thành công");
                                Service.getInstance().sendThongBaoFromAdmin(player, "Bạn đã Kết hôn " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                                Service.getInstance().sendThongBaoAllPlayer("Bạn [" + player.name + "]\nđã Kết hôn Với " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " \nthành công");
                                InventoryService.gI().subQuantityItemsBag(player, NhanKetHon, 1);
                                InventoryService.gI().sendItemBag(player);
                            }
                        }
                    }

                    case ConstNpc.EventTrungThuLB -> {
                        // Nau banh Trung Thu:
                        // + tru nguyen lieu TRUOC, roi tru tien nap (VND nap thuc te),
                        // + moi cong diem sukien (dua top su kien) - KHONG nhan loai banh nua.
                        // + chi so cong diem/nguyen lieu/phi do admin chinh tren panel.
                        EventSuKien.TrungThuService.gI().cook(player, select);
                    }

                    case ConstNpc.EMbe -> {
                        switch (select) {

                            case 0: {

                                Input.gI().TAOPET(player);
                            }
                            break;
                        }
                    }

                    case ConstNpc.TuTienNPC -> {
                        final short[] IDThanKhi = {1759, 1760, 1761, 1762, 1763, 1854};
                        final int[] Ti_Le_Than_Khi = {3, 5, 7, 13, 15, 35};

                        switch (select) {
                            case 0: {
                                if (player.SagaTuTien[2] == 0
                                       ) {

                                    int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 2)));
                                    player.SagaTuTien[2] = tp;

                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "|6|Vì đạo hữu vừa bị hết Thọ Nguyên nên lần Tẩy Luyện này được MIỄN PHÍ.\n"
                                            + "|4|Thiên Phú hiện tại\n|7|[" + player.linhcan(Util.maxInt(player.SagaTuTien[2])) + "].");
                                    return;
                                }

                                if (player.SagaTuTien[0] < panel.tuning.SystemTuning.getL("tutien_chi_phi_tay_luyen")) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "|6|\nChào Mừng Chủ Nhân\nYêu Cầu Chủ Nhân Cần 500M Linh Khí Để Tẩy Luyện");
                                    return;
                                }
                                if (player.SagaTuTien[1] < 10) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "|8|\nChào Mừng Chủ Nhân\nYêu Cầu Chủ Nhân Cần Đạt Trúc Cơ\nĐể Tẩy Luyện");
                                    return;
                                }

                                if (player.SagaTuTien[2] >= 29) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Mày Đã Là Thiên Kiêu Yêu Nghiệt Rồi");
                                    return;
                                }
                                player.SagaTuTien[0] -= panel.tuning.SystemTuning.getL("tutien_chi_phi_tay_luyen");
                                if (Util.isTrue(3f, 100)) {
                                    player.SagaTuTien[2]++;
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "\n|6|Chúc Mừng Đạo Hữu :\n [" + player.name + "]\n Đã Tẩy Luyện Thiên Phú Từ "
                                            + (player.SagaTuTien[2] - 1) + " lên: "
                                            + player.SagaTuTien[2] + " xx.");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Tẩy Luyện Thiên Phú Thất Bại.\nBạn Bị Trừ 500M Linh Khí");
                                }
                                break;
                            }

                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                        "|7|Cảnh Giới Tu Tiên\n"
                                                + "Cảnh giới Hiện\n"
                                        + "|1|" + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + "\n"
                                        + "|7|️ Sinh Lực Tăng: "
                                        + player.TamkjllHpKiGiaptutien(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "|1|MaNa Tăng: "
                                        + player.TamkjllHpKiGiaptutien(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "|6|Giáp Tăng: "
                                        + player.TamkjllHpKiGiaptutien(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "️\n|7|Dame Tăng: "
                                        + player.TamkjllDametutien(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "|6|Tấn Công +: "
                                        + player.tancongtutien(Util.maxInt(player.SagaTuTien[1])) + "\n"
                                        + "|6|Sinh Lực & Mana +: "
                                        + player.hpkitutien(Util.maxInt(player.SagaTuTien[1])) + "\n"
                                        + "|8|Sát Thương Liên Hoàn: "
                                        + player.lamchodep(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "|8|Sát Thương Kaioken: "
                                        + player.lamchodep(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                        + "|7|Sát Thương Biến Khỉ: "
                                        + player.lamchodep(Util.maxInt(player.SagaTuTien[1])) + "%\n"
                                      ,
                                        "Đóng"
                                );
                                break;

                            case 2: {
                                double Ti_Le_Goc = player.bemeocanhgioi(Util.bemeo(player.SagaTuTien[1]));
                                int Bonus = 0;
                                int bestIdx = -1;
                                for (int i = 0; i < IDThanKhi.length; i++) {
                                    Item it = InventoryService.gI().findItemBag(player, IDThanKhi[i]);
                                    if (it != null && it.quantity > 0) {
                                        if (Ti_Le_Than_Khi[i] > Bonus) {
                                            Bonus = Ti_Le_Than_Khi[i];
                                            bestIdx = i;
                                        }
                                    }
                                }

                                double tileFinal = Ti_Le_Goc + Bonus;
                                if (tileFinal > 100) {
                                    tileFinal = 100;
                                }

                                if (player.SagaTuTien[0] < panel.tuning.SystemTuning.getL("tutien_chi_phi_do_kiep")) {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "|6|Chào Mừng Chủ Nhân\n"
                                            + "Yêu Cầu 50M Linh Khí Để Độ Kiếp\n"
                                            + "Tỉ lệ: Gốc " + (int) Ti_Le_Goc + "% "
                                            + (Bonus > 0 ? ("+ " + Bonus + "%") : "")
                                            + " → " + (int) tileFinal + "%");
                                    break;
                                }
                                if (player.SagaTuTien[1] >= panel.tuning.SystemTuning.getI("tutien_canh_gioi_toi_da")) {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Mày Đã Đỉnh Cao Của Cày Chay rồi");
                                    break;
                                }
                                player.SagaTuTien[0] -= panel.tuning.SystemTuning.getL("tutien_chi_phi_do_kiep");
                                if (bestIdx != -1) {
                                    Item chosen = InventoryService.gI().findItemBag(player, IDThanKhi[bestIdx]);
                                    if (chosen != null) {
                                        InventoryService.gI().subQuantityItemsBag(player, chosen, 1);
                                        InventoryService.gI().sendItemBag(player);
                                    }
                                }
                                if (Util.isTrue2(tileFinal, 100)) {
                                    player.SagaTuTien[1]++;
                                    player.tho_nguyen += 259200;
                                    Service.getInstance().sendThongBaoAllPlayer(
                                            "|8|Chúc Mừng Bạn :\n [" + player.name + "]\n Đã Được Độ Kiếp\n "
                                            + "Từ Cảnh Giới " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]) - 1) + " lên: "
                                            + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " !\nThọ Nguyên Tăng Vọt");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Độ Kiếp Thất Bại \n"
                                            + "Tỉ lệ: Gốc " + (int) Ti_Le_Goc + "% "
                                            + (Bonus > 0 ? ("+ " + Bonus + "%") : "")
                                            + " → " + (int) tileFinal + "%\nLần Sau Cố Gắng Hơn");
                                }
                                break;
                            }

                            case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 408);
                                break;

                        }
                    }

                    case ConstNpc.doiten -> {
                        switch (select) {

                            case 0: {

                                Input.gI().createFormChangeNameByItem(player);
                            }
                            break;
                        }
                    }
                    case ConstNpc.kickVipPass -> {
                        switch (select) {
                            case 0:
                                if (player.point_MokhoaVip == 1) {
                                    Service.gI().sendThongBaoFromAdmin(player, "Đã Mua Mùa Này Rồi");
                                    return;
                                }

                                if (player.getSession().vnd >= 500000 && player.getSession().tongnap >= 500000) {
                                    PlayerDAO.subcash(player, 500000);
                                    player.point_MokhoaVip = 1;
                                    player.point_PassVIP += 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Đã Mua Gói Premium Sổ Xứ Mệnh\nTặng 100k Điểm VIP");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có Tiền , Nghèo Vừa Thôi\nTổng Cần 500K Cash Và Đã Nạp DC Trên 500k Cash\nChú Định Bú Free à");
                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 50000 && player.getSession().tongnap >= 50000) {
                                    PlayerDAO.subcash(player, 50000);
                                    player.point_PassVIP += 50000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Đã Thêm 50k Điểm");
                                } else {
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Anh Zai Không có Tiền , Nghèo Vừa Thôi\nTổng Cần 50k Cash Và Đã Nạp DC Trên 50k Cash\nChú Định Bú Free Up coin à");
                                }
                                break;

                            case 2:
                                if (player.point_vip >= 50000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1557, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 50000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (50000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                            case 3:
                                if (player.point_vip >= 30000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1556, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 30000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (30000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                            case 4:
                                if (player.point_vip >= 20000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1555, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1225, 500);
                                    goiqua1.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 20000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (20000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;

                            case 5:
                                if (player.point_vip >= 5000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1554, 10);
                                    goiqua.itemOptions.add(new ItemOption(30, Util.nextInt(1, 10)));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    player.point_vip -= 5000;
                                    player.sukien += 5;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có điểm , Lười Vừa Thôi" + "\nCần " + (5000 - player.point_vip) + " Điểm Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.mohanhtrang -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 0) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1174, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, Util.nextInt(1, 1000)));
                                    goiqua.itemOptions.add(new ItemOption(55, 0));
                                    goiqua.itemOptions.add(new ItemOption(35, 0));
                                    goiqua.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1175, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, Util.nextInt(1, 200000)));
                                    goiqua1.itemOptions.add(new ItemOption(55, 0));
                                    goiqua1.itemOptions.add(new ItemOption(35, 0));
                                    goiqua1.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1176, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, Util.nextInt(1, 5000)));
                                    goiqua2.itemOptions.add(new ItemOption(55, 0));
                                    goiqua2.itemOptions.add(new ItemOption(35, 0));
                                    goiqua2.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1177, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, Util.nextInt(1, 200000)));
                                    goiqua3.itemOptions.add(new ItemOption(55, 0));
                                    goiqua3.itemOptions.add(new ItemOption(35, 0));
                                    goiqua3.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1178, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, Util.nextInt(1, 12)));
                                    goiqua4.itemOptions.add(new ItemOption(55, 0));
                                    goiqua4.itemOptions.add(new ItemOption(35, 0));
                                    goiqua4.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua5 = ItemService.gI().createNewItem((short) 1292, 1);
                                    goiqua5.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua5.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua5.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua5.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua5.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua5);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua6 = ItemService.gI().createNewItem((short) 1170, 1);
                                    goiqua6.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua6.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua6.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua6.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua6.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua6);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua7 = ItemService.gI().createNewItem((short) 1275, 1);
                                    goiqua7.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua7.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua7.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua7.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua7.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua7);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua8 = ItemService.gI().createNewItem((short) 1166, 1);
                                    goiqua8.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua8.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua8.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua8.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua8.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua8);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua9 = ItemService.gI().createNewItem((short) 1041, 1);
                                    goiqua9.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua9.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua9.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua9.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua9.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua9);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua10 = ItemService.gI().createNewItem((short) 1462, 1);
                                    goiqua10.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua10.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua10.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua10.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua10.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua10);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua11 = ItemService.gI().createNewItem((short) 1008, 1);
                                    goiqua11.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua11.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua11.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua11.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua11.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua11);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua12 = ItemService.gI().createNewItem((short) 1561, 1);
                                    goiqua12.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua12.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua12.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua12.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua12.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua12);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua13 = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua13.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua13.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua13.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua13.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua13.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua13);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua14 = ItemService.gI().createNewItem((short) 1172, 1);
                                    goiqua14.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    goiqua14.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    goiqua14.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    goiqua14.itemOptions.add(new ItemOption(210, Util.nextInt(1, 3)));
                                    goiqua14.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua14);
                                    HomThuService.gI().sendListMail(player);

                                    PlayerDAO.subcash(player, 0);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + goiqua.template.name + "\nSố Lượng 1");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có Tiền , Nghèo Vừa Thôi" + "\nCần " + (0 - player.getSession().vnd) + " Coin Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.moruongdo -> {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 500000) {
                                    Item goiqua = ItemService.gI().createNewItem((short) 1174, 1);
                                    goiqua.itemOptions.add(new ItemOption(47, 7000));
                                    goiqua.itemOptions.add(new ItemOption(54, 0));
                                    goiqua.itemOptions.add(new ItemOption(34, 0));
                                    goiqua.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua1 = ItemService.gI().createNewItem((short) 1175, 1);
                                    goiqua1.itemOptions.add(new ItemOption(6, 500000));
                                    goiqua1.itemOptions.add(new ItemOption(54, 0));
                                    goiqua1.itemOptions.add(new ItemOption(34, 0));
                                    goiqua1.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua1);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua2 = ItemService.gI().createNewItem((short) 1176, 1);
                                    goiqua2.itemOptions.add(new ItemOption(0, 9500));
                                    goiqua2.itemOptions.add(new ItemOption(54, 0));
                                    goiqua2.itemOptions.add(new ItemOption(34, 0));
                                    goiqua2.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua2);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua3 = ItemService.gI().createNewItem((short) 1177, 1);
                                    goiqua3.itemOptions.add(new ItemOption(7, 500000));
                                    goiqua3.itemOptions.add(new ItemOption(54, 0));
                                    goiqua3.itemOptions.add(new ItemOption(34, 0));
                                    goiqua3.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua3);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua4 = ItemService.gI().createNewItem((short) 1178, 1);
                                    goiqua4.itemOptions.add(new ItemOption(14, 20));
                                    goiqua4.itemOptions.add(new ItemOption(54, 0));
                                    goiqua4.itemOptions.add(new ItemOption(34, 0));
                                    goiqua4.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua4);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua5 = ItemService.gI().createNewItem((short) 1292, 1);
                                    goiqua5.itemOptions.add(new ItemOption(50, 30));
                                    goiqua5.itemOptions.add(new ItemOption(77, 35));
                                    goiqua5.itemOptions.add(new ItemOption(103, 35));
                                    goiqua5.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua5.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua5);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua6 = ItemService.gI().createNewItem((short) 1170, 1);
                                    goiqua6.itemOptions.add(new ItemOption(50, 30));
                                    goiqua6.itemOptions.add(new ItemOption(77, 35));
                                    goiqua6.itemOptions.add(new ItemOption(103, 35));
                                    goiqua6.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua6.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua6);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua7 = ItemService.gI().createNewItem((short) 1275, 1);
                                    goiqua7.itemOptions.add(new ItemOption(50, 30));
                                    goiqua7.itemOptions.add(new ItemOption(77, 35));
                                    goiqua7.itemOptions.add(new ItemOption(103, 35));
                                    goiqua7.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua7.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua7);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua8 = ItemService.gI().createNewItem((short) 1166, 1);
                                    goiqua8.itemOptions.add(new ItemOption(50, 30));
                                    goiqua8.itemOptions.add(new ItemOption(77, 35));
                                    goiqua8.itemOptions.add(new ItemOption(103, 35));
                                    goiqua8.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua8.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua8);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua9 = ItemService.gI().createNewItem((short) 1041, 1);
                                    goiqua9.itemOptions.add(new ItemOption(50, 30));
                                    goiqua9.itemOptions.add(new ItemOption(77, 35));
                                    goiqua9.itemOptions.add(new ItemOption(103, 35));
                                    goiqua9.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua9.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua9);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua10 = ItemService.gI().createNewItem((short) 1462, 1);
                                    goiqua10.itemOptions.add(new ItemOption(50, 30));
                                    goiqua10.itemOptions.add(new ItemOption(77, 35));
                                    goiqua10.itemOptions.add(new ItemOption(103, 35));
                                    goiqua10.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua10.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua10);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua11 = ItemService.gI().createNewItem((short) 1008, 1);
                                    goiqua11.itemOptions.add(new ItemOption(50, 30));
                                    goiqua11.itemOptions.add(new ItemOption(77, 35));
                                    goiqua11.itemOptions.add(new ItemOption(103, 35));
                                    goiqua11.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua11.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua11);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua12 = ItemService.gI().createNewItem((short) 1561, 1);
                                    goiqua12.itemOptions.add(new ItemOption(50, 30));
                                    goiqua12.itemOptions.add(new ItemOption(77, 35));
                                    goiqua12.itemOptions.add(new ItemOption(103, 35));
                                    goiqua12.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua12.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua12);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua13 = ItemService.gI().createNewItem((short) 1810, 1);
                                    goiqua13.itemOptions.add(new ItemOption(50, 30));
                                    goiqua13.itemOptions.add(new ItemOption(77, 35));
                                    goiqua13.itemOptions.add(new ItemOption(103, 35));
                                    goiqua13.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua13.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua13);
                                    HomThuService.gI().sendListMail(player);

                                    Item goiqua14 = ItemService.gI().createNewItem((short) 1172, 1);
                                    goiqua14.itemOptions.add(new ItemOption(50, 30));
                                    goiqua14.itemOptions.add(new ItemOption(77, 35));
                                    goiqua14.itemOptions.add(new ItemOption(103, 35));
                                    goiqua14.itemOptions.add(new ItemOption(237, Util.nextInt(1, 3)));
                                    goiqua14.itemOptions.add(new ItemOption(30, 0));
                                    InventoryService.gI().addItemMail(player, goiqua14);
                                    HomThuService.gI().sendListMail(player);
                                    PlayerDAO.subcash(player, 500000);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Anh Zai Không có Tiền , Nghèo Vừa Thôi" + "\nCần " + (500000 - player.getSession().vnd) + " vnđ Nữa");
                                }
                                break;
                        }
                    }

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN -> {
                        switch (select) {
                            case 0 -> {
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                            }
                        }
                    }

                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND -> {
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                    }

                    case ConstNpc.MENU_FIND_PLAYER -> {
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0 -> {
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                }
                                case 1 -> {
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                }
                                case 2 ->
                                    Input.gI().createFormChangeName(player, p);
                                case 3 -> {
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                }
                                case 4 -> {
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                }
                            }
                        }
                    }
                    case ConstNpc.SUMMON_DRAGON_SIEU_CAP_MENU -> {
                        switch (select) {
                            case 0, 1, 2:
                                int idtemp = 989 + select;
                                Item it = ItemService.gI().createNewItem((short) idtemp);
                                it.addOptionParam(50, 23);
                                it.addOptionParam(77, 20);
                                it.addOptionParam(103, 20);
                                it.addOptionParam(94, 15);
                                it.addOptionParam(192, 10);
                                it.addOptionParam(108, 15);
                                it.addOptionParam(148, 20);
                                it.addOptionParam(195, 2);
                                it.addOptionParam(154, 1);
                                it.addOptionParam(93, 60);

                                if (!InventoryService.gI().addItemBag(player, it, 999999)) {
                                    Service.gI().sendThongBao(player, "Không đủ ô trống trong hành trang!");
                                    return;
                                }
                                break;
                            case 3:
                                it = ItemService.gI().createNewItem((short) 1019);
                                it.addOptionParam(30, 1);
                                if (!InventoryService.gI().addItemBag(player, it, 999999)) {
                                    Service.gI().sendThongBao(player, "Không đủ ô trống trong hành trang!");
                                    return;
                                }

                                break;
                        }
                        InventoryService.gI().subQuantityItemsBag(player, InventoryService.gI().findItemBag(player, 1018), 1);
                        InventoryService.gI().sendItemBag(player);
                    }

                    case ConstNpc.CONFIRM_TELE_NAMEC -> {
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                    }

                    case ConstNpc.MA_BAO_VE -> {
                        if (select == 0) {
                            if (player.mbv == 0) {
                                if (player.inventory.ruby >= 5000) {
                                    player.inventory.ruby -= 5000;
                                    Service.gI().sendMoney(player);
                                    player.mbv = player.iDMark.getMbv();
                                    player.baovetaikhoan = true;
                                    Service.gI().sendThongBao(player, "Kích hoạt thành công, tài khoản đang được bảo vệ");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ tiền để kích hoạt bảo vệ tài khoản");
                                }
                            } else {
                                if (player.baovetaikhoan) {
                                    player.baovetaikhoan = false;
                                    Service.gI().sendThongBao(player, "Chức năng bảo vệ tài khoản đang tắt");
                                } else {
                                    player.baovetaikhoan = true;
                                    Service.gI().sendThongBao(player, "Tài khoản đang được bảo vệ");
                                }
                            }
                        } else if (select == 1) {
                            Service.gI().mabaove(player, 1234567);
                        }
                    }

                    case ConstNpc.UP_TOP_ITEM -> {
                        if (select == 0) {
                            if (player.inventory.gold < 5000000) {
                                Service.gI().sendThongBao(player, "Bạn không có đủ vàng!");
                                return;
                            }
                            player.inventory.gold -= 5000000;
                            Service.gI().sendMoney(player);
                            int iditem = player.iDMark.getIdItemUpTop();
                            ConsignShopService.gI().getItemBuy(player, iditem).lasttime = System.currentTimeMillis();
                            Service.gI().sendThongBao(player, "Up top thành công!");
                            ConsignShopService.gI().openShopKyGui(player);
                        }
                    }
                    case ConstNpc.RUONG_GO -> {
                        int i = player.indexWoodChest;
                        if (i < 0) {
                            return;
                        }
                        Item itemWoodChest = player.itemsWoodChest.get(i);
                        player.indexWoodChest--;
                        String info = "|1|" + itemWoodChest.template.name;
                        String info2 = "\n|2|";
                        if (!itemWoodChest.itemOptions.isEmpty()) {
                            for (Item.ItemOption io : itemWoodChest.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 73) {
                                    info2 += io.getOptionString() + "\n";
                                }
                            }
                        }
                        info = (info2.length() > "\n|2|".length() ? (info + info2).trim() : info.trim()) + "\n|0|" + itemWoodChest.template.description;
                        NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n"
                                + info.trim(), "OK" + (i > 0 ? " [" + i + "]" : ""));
                    }
                    case ConstNpc.HOP_QUA_THAN_LINH -> {
                        Item aotl_td = ItemService.gI().createNewItem((short) 555);
                        Item aotl_nm = ItemService.gI().createNewItem((short) 557);
                        Item aotl_xd = ItemService.gI().createNewItem((short) 559);

                        aotl_td.itemOptions.add(new Item.ItemOption(47, 800 + new Random().nextInt(2000)));

                        aotl_nm.itemOptions.add(new Item.ItemOption(47, 900 + new Random().nextInt(1000)));

                        aotl_xd.itemOptions.add(new Item.ItemOption(47, 950 + new Random().nextInt(2000)));

                        aotl_td.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        aotl_nm.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        aotl_xd.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ

                        aotl_td.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        aotl_nm.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        aotl_xd.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ

                        Item quantl_td = ItemService.gI().createNewItem((short) 556);
                        Item quantl_nm = ItemService.gI().createNewItem((short) 558);
                        Item quantl_xd = ItemService.gI().createNewItem((short) 560);

                        quantl_td.itemOptions.add(new Item.ItemOption(22, 47 + new Random().nextInt(5)));
                        quantl_td.itemOptions.add(new Item.ItemOption(27, (47 + new Random().nextInt(5)) * 1000 * 15 / 100));

                        quantl_nm.itemOptions.add(new Item.ItemOption(22, 45 + new Random().nextInt(5)));
                        quantl_nm.itemOptions.add(new Item.ItemOption(27, (45 + new Random().nextInt(5)) * 1000 * 15 / 100));

                        quantl_xd.itemOptions.add(new Item.ItemOption(22, 42 + new Random().nextInt(8)));
                        quantl_xd.itemOptions.add(new Item.ItemOption(27, (42 + new Random().nextInt(8)) * 1000 * 15 / 100));

                        quantl_td.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        quantl_nm.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        quantl_xd.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ

                        quantl_td.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        quantl_nm.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        quantl_xd.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ

                        Item gangtl_td = ItemService.gI().createNewItem((short) 562);
                        Item gangtl_nm = ItemService.gI().createNewItem((short) 564);
                        Item gangtl_xd = ItemService.gI().createNewItem((short) 566);

                        gangtl_td.itemOptions.add(new Item.ItemOption(0, 3500 + new Random().nextInt(5000)));
                        gangtl_nm.itemOptions.add(new Item.ItemOption(0, 3300 + new Random().nextInt(5000)));
                        gangtl_xd.itemOptions.add(new Item.ItemOption(0, 3500 + new Random().nextInt(5000)));

                        gangtl_td.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        gangtl_nm.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        gangtl_xd.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ

                        gangtl_td.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        gangtl_nm.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        gangtl_xd.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ

                        Item giaytl_td = ItemService.gI().createNewItem((short) 563);
                        Item giaytl_nm = ItemService.gI().createNewItem((short) 565);
                        Item giaytl_xd = ItemService.gI().createNewItem((short) 567);

                        giaytl_td.itemOptions.add(new Item.ItemOption(23, 42 + new Random().nextInt(5)));
                        giaytl_nm.itemOptions.add(new Item.ItemOption(23, 47 + new Random().nextInt(5)));
                        giaytl_xd.itemOptions.add(new Item.ItemOption(23, 45 + new Random().nextInt(4)));

                        giaytl_td.itemOptions.add(new Item.ItemOption(28, (42 + new Random().nextInt(5)) * 1000 * 15 / 100));
                        giaytl_nm.itemOptions.add(new Item.ItemOption(28, (47 + new Random().nextInt(5)) * 1000 * 15 / 100));
                        giaytl_xd.itemOptions.add(new Item.ItemOption(28, (45 + new Random().nextInt(4)) * 1000 * 15 / 100));

                        giaytl_td.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        giaytl_nm.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
                        giaytl_xd.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ

                        giaytl_td.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        giaytl_nm.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        giaytl_xd.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ

                        Item nhan = ItemService.gI().createNewItem((short) 561);

                        nhan.itemOptions.add(new Item.ItemOption(14, 14 + new Random().nextInt(4)));
                        nhan.itemOptions.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ

                        nhan.itemOptions.add(new Item.ItemOption(30, 1)); // ycsm 18 tỉ
                        Item HopQuaThanLinh = InventoryService.gI().findItemBag(player, 1228);
                        switch (select) {
                            case 0:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.gI().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }
                                InventoryService.gI().addItemBag(player, aotl_td, 999999);
                                InventoryService.gI().addItemBag(player, quantl_td, 999999);
                                InventoryService.gI().addItemBag(player, gangtl_td, 999999);
                                InventoryService.gI().addItemBag(player, giaytl_td, 999999);
                                InventoryService.gI().addItemBag(player, nhan, 999999);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                InventoryService.gI().sendItemBag(player);
                                Service.gI().sendThongBao(player, "Bạn nhận được 1 set thần linh trái đất");
                                break;
                            case 1:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.gI().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }
                                InventoryService.gI().addItemBag(player, aotl_nm, 999999);
                                InventoryService.gI().addItemBag(player, quantl_nm, 999999);
                                InventoryService.gI().addItemBag(player, gangtl_nm, 999999);
                                InventoryService.gI().addItemBag(player, giaytl_nm, 999999);
                                InventoryService.gI().addItemBag(player, nhan, 999999);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                Service.gI().sendThongBao(player, "Bạn nhận được 1 set thần linh namek");
                                InventoryService.gI().sendItemBag(player);
                                break;
                            case 2:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.gI().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }
                                InventoryService.gI().addItemBag(player, aotl_xd, 999999);
                                InventoryService.gI().addItemBag(player, quantl_xd, 999999);
                                InventoryService.gI().addItemBag(player, gangtl_xd, 999999);
                                InventoryService.gI().addItemBag(player, giaytl_xd, 999999);
                                InventoryService.gI().addItemBag(player, nhan, 999999);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                InventoryService.gI().sendItemBag(player);

                                Service.gI().sendThongBao(player, "Bạn nhận được 1 set thần linh xayda");
                                break;
                        }
                    }
                    case ConstNpc.MENU_XUONG_TANG_DUOI -> {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX && player.zone.map.mapId != 120) {
                            ChangeMapService.gI().changeMap(player, player.zone.map.mapIdNextMabu((short) player.zone.map.mapId), -1, -1, 100);
                        }
                    }
                }
            }
        };
    }
}
