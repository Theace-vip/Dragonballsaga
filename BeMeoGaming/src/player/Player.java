package player;

import consts.ConstDailyGift;
import minigame.cost.LuckyNumberCost;
import minigame.LuckyNumber.LuckyNumberService;
import npc.NonInteractiveNPC;
import models.Card.Card;
import models.Card.RadarCard;
import models.Card.RadarService;
import models.MajinBuu.MajinBuuService;
import player.badges.Badges;
import player.badges.BadgesData;
import player.dailyGift.DailyGiftData;
import player.dailyGift.DailyGiftService;
import services.*;
import skill.PlayerSkill;

import java.util.Iterator;
import java.util.List;

import Mail.Thu;
import clan.Clan;
import intrinsic.IntrinsicPlayer;
import item.Item;
import item.ItemTime;
import jdbc.daos.PlayerDAO;
import npc.specialnpc.MagicTree;
import consts.ConstPlayer;
import consts.ConstTask;
import npc.specialnpc.MabuEgg;
import mob.MobMe;
import data.DataGame;
import dragon.Functions;
import clan.ClanMember;
import consts.ConstAchievement;
import map.Zone;
import matches.IPVP;
import matches.TYPE_LOSE_PVP;
import skill.Skill;
import server.io.MySession;
import task.Badges.BadgesTask;
import task.Badges.BadgesTaskService;
import task.TaskPlayer;
import network.Message;
import server.Client;
import services.func.ChangeMapService;
import models.Combine.Combine;
import utils.Logger;
import utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import models.BlackBallWar.BlackBallWarService;
import models.The23rdMartialArtCongress.The23rdMartialArtCongressManager;
import map.ItemMap;
import map.MaBuHold;
import models.MajinBuu.MajinBuu14H;
import models.SuperDivineWater.SuperDivineWaterService;
import models.The23rdMartialArtCongress.The23rdMartialArtCongress;
import models.ShenronEvent.ShenronEvent;
import server.Maintenance;

public class Player implements Runnable {

    //-----------STAR -----------
    public boolean isBot = false;
    public int dakethon;
    public int duockethon;
    public long lastTimerun;
    public String Nametc;
    public double dametc = 0;
    public double hptc = 0;
    public long ctk = 0;
    public int SagaThienDao = 0;
    public long PhapTac_ThienDao = 0;
    public int SagaDiaDao = 0;
    public long DLbTamkjll = 0;
    public int TamkjllCapPb = 0;
    public int TamkjllThomo = 0;
    public long TamkjllThomoExp = 0;
    public long SagaChuyenSinh = 0;
    public double[] SagaTuTien = new double[]{0, 0, 0};
    public long[] TamkjllDauLaDaiLuc = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int TamkjllPetGiong = -1;
    public Tamkjll_Pet tamkjllpet;
    public Player TamkjllPlayerAttack;
    public String TamkjllNamePet;
    public int TamkjllPetHunger;
    public long TamkjllPetPower;
    public long TamkjlllastTimeThucan;
    public int EmBeLv;
    public long EmBeEXP;
    public int Tamkjll_Tu_Ma = 0;
    public long Tamkjll_Exp_Tu_Ma = 0;
    public int Tamkjll_Ma_Hoa = 0;
    public long TamkjllLasttimeMaHoa;
    public int Tamkjll_Ma_cot;
    public long ctkclan = 0;
    public boolean useCanCau;
    public long lasttimeCanCau;
    public int iconCancau;

    public boolean autonangSagaThienDao = true;
    //-----------END TAMKJLL-----------
    public long diemdanh;
    public byte CheckDayOnl;//này bữa Trường lm lun h
    public byte Saga_VIP = 0;
    // Cung Menh: cap nang cap va so lan dot pha (luu cot cung_menh)
    public int cungMenhLevel = 0;
    public int cungMenhDotPha = 0;
    // true = da tru bonus cu (cong thang vao hpg/mpg/dameg) - xem CungMenhService.migrateOldBonus
    public boolean cungMenhFixedOldBonus = false;
    public long timevip;
    public boolean isLoadingTranformation = false;
    public long lastTimeTranformation;
    public int isbienhinh;
    public int pointThap;
    public int levelThap;
    public int tangThap;
    public long lastTimeEatPea;
    public int point_event = 0;
    @Setter
    @Getter
    private MySession session;
    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public List<Integer> listNhan = new ArrayList<>();
    public int[] checkNhan;
    public int phutOnline;
    public long lastTimeOnline = System.currentTimeMillis();
    public List<Integer> listOnline = new ArrayList<>();
    public List<Integer> listDiemDanh = new ArrayList<>();
    public Date weekTimeLogin = new Date();
    // Tầm Bảo (Vòng Quay) - xem services.TamBao
    public int diem_quay;
    public List<Integer> listNhan_TamBao = new ArrayList<>();
    public int[] checkNhan_TamBao = new int[0];
    public int[] list_id_nhan = new int[TamBao.SLOTS];
    public short head;
    public int deltaTime;
    public byte typePk;
    public byte cFlag;
    public boolean haveTennisSpaceShip;
    public boolean isCopy;
    public boolean beforeDispose;
    public int mbv = 0;
    public int bktdeptrai;
    public boolean baovetaikhoan;
    public long mbvtime;
    public int timeGohome;
    public long lastUpdateGohomeTime;
    public boolean goHome;
    public long lastPkCommesonTime;
    public boolean callBossPocolo;
    public Zone zoneSieuThanhThuy;
    public boolean winSTT;
    public long lastTimeWinSTT;
    public long lastTimeUpdateSTT;
    public MajinBuu14H maBu2H;
    public boolean isMabuHold;
    public MaBuHold maBuHold;
    public int precentMabuHold;
    public boolean isPhuHoMapMabu;
    public boolean danhanthoivang;
    public long lastTimeChangeMap;
    public long lastRewardGoldBarTime;
    public int timesPerDayBDKB = 0;
    public long lastTimeJoinBDKB;
    public boolean joinCDRD;
    public long lastTimeJoinCDRD;
    public boolean talkToThuongDe;
    public boolean talkToThanMeo;
    public long timeChangeMap144;
    public long lastTimeJoinDT;
    public int typeChibi;
    public long lastTimeChibi;
    public long lastTimeUpdateChibi;
    public String captcha = "";
    public int spamcaptcha = 0;
    public long lasttimebotchat;
    public boolean doesNotAttack;
    public long lastTimePlayerNotAttack;
    public int timeNotAttack = 1800000;
    public boolean isPet;
    public boolean isClone;
    public boolean isNewPet;
    public boolean isNewPet1;
    public boolean isBoss;
    public boolean isPlayer;
    public IPVP pvp;
    public byte maxTime = 30;
    public byte type = 0;
    public boolean isOffline = false;
    public String notify = null;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public List<Player> temporaryEnemies = new ArrayList<>();
    public Date firstTimeLogin;
    public Zone zone;

    // public byte effDragon = -1;
    public byte effDragon = -1;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Clone clone;
    public Pet pet;
    public NewPet newPet;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public int playerTradeId = -1;
    public Player playerTrade;
    public Combine combine;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public FightMabu fightMabu;
    public NewSkill newSkill;
    public Satellite satellite;
    public Achievement achievement;
    public GiftCode giftCode;
    public Traning traning;
    public Badges badges;
    public Clan clan;
    public ClanMember clanMember;
    public List<Friend> friends;
    public List<Enemy> enemies;
    public boolean justRevived;
    public long lastTimeRevived;
    public long timeChangeZone;
    public long diemfam = 0;
    public long NapDau = 0;
    public int point_cauca;
    public long sukien = 0;
    public long SukienTamBao = 0;
    public long point_bdkb = 0;
    public long point_nhs;
    public long point_sb;
    public long point_vithu;
    public long point_moruong;
    public long point_vip;
    public long point_dapdo;
    public long point_PassFree;
    public long point_PassVIP;
    public long point_MokhoaVip;
    public long SoXuMenhDaNhan;
    public long SoXuMenhDaNhanvip;

    public long lastUseOptionTime;
    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public List<Card> Cards = new ArrayList<>();
    public int levelWoodChest;
    public long goldChallenge;
    public long rubyChallenge;
    public long lastTimeRewardWoodChest;
    public List<Item> itemsWoodChest = new ArrayList<>();
    public int indexWoodChest;
    public long lastTimePKDHVT23;
    public boolean lostByDeath;
    public boolean isPKDHVT;
    public long thoNguyenEndTime; // thời gian kết thúc (millis)
    public long tho_nguyen; // thời gian kết thúc (millis)
    public int xSend;
    public int ySend;
    public boolean isFly;
    public boolean notiKillBoss = true;
    //Mail
    public List<Thu> homThu;
    public int selectMail;
    // shenron event
    public long lastTimeShenronAppeared;
    public boolean isShenronAppear;
    public ShenronEvent shenronEvent;
    // vo dai sinh tu
    public long lastTimePKVoDaiSinhTu;
    public boolean haveRewardVDST;

    public int thoiVangVoDaiSinhTu;
    public long timePKVDST;
    public int binhChonHatMit;
    public int binhChonPlayer;
    public Zone zoneBinhChon;
    public ItemEvent itemEvent;
    public int levelLuyenTap;
    public boolean isThachDau;
    public int tnsmLuyenTap;
    public boolean dangKyTapTuDong;
    public long lastTimeOffline;
    public int mapIdDangTapTuDong;
    public int lastMapOffline;
    public int lastZoneOffline;
    public int lastXOffline;
    public String thongBaoTapTuDong;
    public boolean teleTapTuDong;
    public int timesPerDayCuuSat;
    public long lastTimeCuuSat;
    public boolean nhanVangNangVIP;
    public boolean nhanDeTuNangVIP;
    public boolean nhanSKHVIP;
    public long totalDamageTaken;
    public boolean thongBaoChangeMap;
    public String textThongBaoChangeMap;
    public boolean thongBaoThua;
    public String textThongBaoThua;
    public SuperRank superRank;
    public boolean canReward;
    public boolean changeMapVIP;
    public boolean haveReward;
    public int tayThong;
    public List<Item> itemsTradeWVP = new ArrayList<>();
    public boolean tradeWVP;
    private DropItem dropItem;
    public boolean isBattu = false;
    public List<BadgesData> dataBadges = new ArrayList<>();
    public List<BadgesTask> dataTaskBadges = new ArrayList<>();
    public long lastTimeChangeBadges;
    public List<DailyGiftData> dailyGiftData = new ArrayList<>();
    public int numUseSkill = 0;

    public Player() {
        lastUseOptionTime = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combine = new Combine();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        effectSkin = new EffectSkin(this);
        newSkill = new NewSkill(this);
        satellite = new Satellite();
        achievement = new Achievement(this);
        giftCode = new GiftCode();
        traning = new Traning();
        itemEvent = new ItemEvent(this);
        superRank = new SuperRank(this);
        dropItem = new DropItem(this);
        badges = new Badges();
        homThu = new ArrayList<Thu>();
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    public boolean isClientDoubleMessage() {
        return this.session != null && this.session.version == 237;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public boolean isPl() {
        return isPlayer && !isPet && !isBoss && !isNewPet && !isNewPet1 && !(this instanceof NonInteractiveNPC);
    }
    long lastTimeUpdateDB = System.currentTimeMillis();

    @Override
    public void run() {
        Functions.sleep(500);
        while (!Maintenance.isRunning && session != null && session.isConnected() && this.name != null) {
            long st = System.currentTimeMillis();
            update();
            long time = 1000 - (System.currentTimeMillis() - st);
            if (time > 0) {
                Functions.sleep(time);
            }
//            this.tho_nguyen -= 1;
//              PlayerDAO.updateThoNguyen(this);
//            if (this.tho_nguyen == 0) {
//                Service.gI().sendThongBao(this, "|2|Thọ Nguyên đã hết! Nhân vật sẽ bị khóa.");
//                PlayerDAO.lockAccount(this);
//                Client.gI().kickSession(this.getSession());
//                break; // dừng thread luôn
//            }
            // Panel > He Thong: linh khi tu luyen / giay + tho nguyen tieu hao / giay
            this.tho_nguyen -= panel.tuning.SystemTuning.getL("tutien_tho_nguyen_moi_giay");
            this.SagaTuTien[0] += panel.tuning.SystemTuning.getL("tutien_linh_khi_moi_giay");
            // HUY BO update DB moi giay: updatePlayer (60s + khi logout) da luu tho_nguyen + SagaTuTien.
            // Truoc do 1000 nguoi = 1000 UPDATE/s tren pool 2 ket noi -> login treo.
            if (this.tho_nguyen == 0) {
                Service.gI().sendThongBaoFromAdmin(this, "|2|Thọ Nguyên đã hết!\n Bạn Bị Phế Bỏ 100 Triệu Năm Tu Vi\nPhế Bỏ Linh Căn Thành Phàm Nhân");
                this.SagaTuTien[0] = 0;
                this.SagaTuTien[1] = 0;
                this.SagaTuTien[2] = 0;

                break; // dừng thread luôn
            }
            if (System.currentTimeMillis() - lastTimeUpdateDB >= 60000) {
                try {
                    this.phutOnline++;
                    lastTimeUpdateDB = System.currentTimeMillis();
                    PlayerDAO.updatePlayer(this);
//                    PlayerDAO.updateVND(this);

                } catch (Exception e) {
                    Logger.logException(Player.class, e);
                }

            }
        }
    }

    public void start() {
        new Thread(this, "Update player " + this.name).start();
    }

    public void CreatePet(String NamePet) {
        this.TamkjllNamePet = NamePet;
        this.TamkjlllastTimeThucan = System.currentTimeMillis();
        this.TamkjllPetHunger = panel.tuning.SystemTuning.getI("embe_thuc_an_dau");
        this.EmBeLv = 1;
        this.EmBeEXP = panel.tuning.SystemTuning.getL("embe_exp_dau");
        this.TamkjllPetGiong = Util.nextInt(
                panel.tuning.SystemTuning.getI("embe_giong_min"),
                panel.tuning.SystemTuning.getI("embe_giong_max"));
        this.TamkjllPetPower = Util.Tamkjllnext(10, 100000000L + ((this.TamkjllPetGiong + 1) * 100000000L));
    }

    public void update() {
        if (!this.beforeDispose) {
            try {
                if (this.zone != null || (!this.isPl() && this.zone == null)) {
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (this.isPl() && this.zone != null && this.zone.map.mapId == this.gender + 21 && (TaskService.gI().getIdTask(this) == ConstTask.TASK_0_0 || TaskService.gI().getIdTask(this) == ConstTask.TASK_0_1)) {
                        this.playerTask.taskMain.index = 2;
                        TaskService.gI().sendTaskMain(this);
                    }
                }
                if ((this.zone != null && !MapService.gI().isHome(this.zone.map.mapId)) || (!this.isPl() && this.zone == null)) {
                    if (isPl() && iDMark != null && iDMark.isBan() && Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                        return;
                    }
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkill != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (clone != null) {
                        clone.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newPet != null) {
                        newPet.update();
                    }
                    if (tamkjllpet != null) {
                        tamkjllpet.update();
                    }
//                    if (itemTime != null) {
//                        itemTime.update();
//                    }
                    if (satellite != null) {
                        satellite.update();
                    }

                    if (this.isPl()
                            && this.nPoint != null
                            && this.nPoint.power >= 0
                            && this.nPoint.power <= Long.MAX_VALUE) {
                        Service.getInstance().sendTitle(this);
                    }
                    if (this.isPl() && this.tamkjllpet == null && this.TamkjllPetGiong >= 0
                            && this.TamkjllPetGiong <= 10) {
                        PetService.Tamkjll_Pet(this);
                    } else if (this.isPl() && this.tamkjllpet != null && this.TamkjllPetGiong < 0
                            && this.TamkjllPetGiong > 10) {
                        ChangeMapService.gI().exitMap(this.tamkjllpet);
                        tamkjllpet.dispose();
                        tamkjllpet = null;
                    }

                    //Chibi
                    if (this.isPl() && !this.isDie() && this.effectSkill != null && !this.effectSkill.isChibi && Util.canDoWithTime(lastTimeChibi, 300000)) {
                        if (Util.isTrue(1, 10) && !MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
                            EffectSkillService.gI().setChibi(this, 600000);
                        }
                        lastTimeChibi = System.currentTimeMillis();
                    }
                    if (this.isPl() && !this.isDie() && this.effectSkill != null && this.effectSkill.isChibi && Util.canDoWithTime(lastTimeUpdateChibi, 1000)) {
                        if (this.typeChibi == 1) {
                            if (this.nPoint.mp < this.nPoint.mpMax) {
                                if (this.nPoint.mpMax - this.nPoint.mp < this.nPoint.mpMax / 10) {
                                    this.nPoint.mp = this.nPoint.mpMax;
                                } else {
                                    this.nPoint.mp += this.nPoint.mpMax / 10;
                                }
                            }
                            PlayerService.gI().sendInfoMp(this);
                        } else if (this.typeChibi == 3) {
                            if (this.nPoint.hp < this.nPoint.hpMax) {
                                if (this.nPoint.hpMax - this.nPoint.hp < this.nPoint.hpMax / 10) {
                                    this.nPoint.hp = this.nPoint.hpMax;
                                } else {
                                    this.nPoint.hp += this.nPoint.hpMax / 10;
                                }
                            }
                            PlayerService.gI().sendInfoHp(this);
                        }
                        lastTimeUpdateChibi = System.currentTimeMillis();
                    }

                    if (this.isPl() && this.achievement != null) {
                        this.achievement.done(ConstAchievement.HOAT_DONG_CHAM_CHI, 1000);
                    }
                    if (this.isPl()) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        if (!(hour >= 22 && hour <= 23)) {
                            if (zone.map.mapId == 126) {
                                ChangeMapService.gI().changeMapNonSpaceship(this, 19, 1000 + Util.nextInt(-100, 100), 360);
                            }

                            if (!(hour >= 18 && hour <= 20)) {
                                if (zone.map.mapId == 2130) {
                                    ChangeMapService.gI().changeMapNonSpaceship(this, 0, 1000 + Util.nextInt(-100, 100), 360);
                                    Service.getInstance().sendThongBaoFromAdmin(this, "|7|Hết Gio Vào Map 18h-20h Hàng Ngày\n"
                                            + "|6|Để Vào Map Thảo Nguyên Up Sét KH VIP\n"
                                            + "Gồm Những Sét Kích Hoạt\n"
                                            + "5 Món Goku +80% Sức Đánh\n"
                                            + "5 Món Broly +95% giảm ST\n"
                                            + "5 Món Picolo +500% Laze\n"
                                            + "5 Món Vegeta +500% HP\n"
                                            + "|7|Đặc Biệt Săn Boss Tại map Sẽ Nhận Được SKH Chỉ Số 10M");
                                }

                            }
                        }

                        updateCSMM();
                        TaskService.gI().sendUpdateCountSubTask(this);
                        autoSendBadges();
                        BadgesTaskService.updateDoneTask(this);
                        sendTextTimeDaiLyGift();
                        if (clan != null) {
                            ClanService.gI().checkDoneTaskJoinClan(clan);
                        }
                        // thuhoivp();
                    }
                    if (this.isPl() && this.effectSkill != null && this.effectSkill.isMabuHold) {
                        this.nPoint.subHP(this.nPoint.hpMax / 100);
                        if (Util.isTrue(1, 10)) {
                            Service.gI().chat(this, "Cứu tôi với");
                        }
                        PlayerService.gI().sendInfoHp(this);
                        if (this.precentMabuHold > 15) {
                            EffectSkillService.gI().removeMabuHold(this);
                        }
                        if (this.nPoint.hp <= 0) {
                            EffectSkillService.gI().removeMabuHold(this);
                            setDie();
                        }
                    }

                    if (this.zone != null && this.effectSkin != null && this.effectSkin.xHPKI > 1 && !MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
                        this.effectSkin.xHPKI = 1;
                        this.nPoint.calPoint();
                        Service.gI().point(this);
                    }

                    if (this.zone != null && this.effectSkin != null && this.effectSkin.xDame > 1 && !MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
                        this.effectSkin.xDame = 1;
                        this.nPoint.calPoint();
                        Service.gI().point(this);
                    }

                    if (this.isPl() && this.zone != null) {
                        fixBlackBallWar();
                    }

                    if (this.zone != null && this.zone.map.mapId == (21 + this.gender)) {
                        if (this.mabuEgg != null) {
                            this.mabuEgg.sendMabuEgg();
                        }
                    }

                    if (this.isPhuHoMapMabu && this.zone != null && !MapService.gI().isMapMabu2H(this.zone.map.mapId)) {
                        this.isPhuHoMapMabu = false;
                        this.nPoint.calPoint();
                        Service.gI().point(this);
                        Service.gI().Send_Info_NV(this);
                        Service.gI().Send_Caitrang(this);
                    }

                    // Change Map 144 CDRD
                    if (this.isPl() && this.clan != null && this.clan.ConDuongRanDoc != null
                            && this.joinCDRD && this.clan.ConDuongRanDoc.allMobsDead
                            && this.talkToThanMeo && this.zone.map.mapId == 47
                            && Util.canDoWithTime(timeChangeMap144, 5000)) {
                        ChangeMapService.gI().changeMapYardrat(this, this.clan.ConDuongRanDoc.getMapById(144), 300 + Util.nextInt(-100, 100), 312);
                        this.timeChangeMap144 = System.currentTimeMillis();
                    }
                    // Auto tắt cờ khi rời map Mabu
                    if (this.isPl() && this.zone != null && !MapService.gI().isMapMaBu(this.zone.map.mapId) && (this.cFlag == 9 || this.cFlag == 10)) {
                        Service.gI().changeFlag(this, 0);
                    }

                    if (this.isPl() && this.superRank != null) {
                        if (Util.isAfterMidnightPlus11(this.superRank.lastTimeReward)) {
                            this.superRank.reward();
                        }
                    }

                    if (this.isPl() && this.zone != null && MapService.gI().isMapMaBu(this.zone.map.mapId) && this.cFlag != 9 && this.cFlag != 10) {
                        Service.gI().changeFlag(this, Util.nextInt(9, 10));
                    }
                    if (dropItem != null) {
                        dropItem.update();
                    }
                    MajinBuuService.gI().update(this);
                    SuperDivineWaterService.gI().update(this);
                    if (!isBoss && this.iDMark != null && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 60000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (!isBoss && this.iDMark != null && this.iDMark.isGotoFuture1() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture1(), 5000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 215, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture1(false);
                    }
                    if (this.isPl() && location != null && location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    public long lastTimeSendTextTime;

    public void sendTextTimeDaiLyGift() {
        if (Util.canDoWithTime(lastTimeSendTextTime, 999999999)) {
            if (DailyGiftService.checkDailyGift(this, ConstDailyGift.NHAN_BUA_MIEN_PHI)) {
                //   ItemTimeService.gI().sendTextTime(this, itemTime.TEXT_NHAN_BUA_MIEN_PHI, "Chúc Các Bạn Chơi Game Vui Vẻ\nHãy Tham Gia Đua Top, Săn Boss\nSự Kiện, Nạp Game,Up Đồ,Buil Đồ", 30);
            }
            lastTimeSendTextTime = System.currentTimeMillis();
        }
    }

    public void updateCSMM() {
        minigame.LuckyNumber.LuckyNumber.players.forEach((g) -> {
            if (this.id == g.id) {
                LuckyNumberService.showNumberPlayer(this, LuckyNumberService.strNumber(this.id));
                ItemTimeService.gI().sendItemTime(this, 2295, LuckyNumberCost.timeGame);
            }
        });
    }

    public void autoSendBadges() {
        Iterator<BadgesData> iterator = dataBadges.iterator();
        while (iterator.hasNext()) {
            BadgesData data = iterator.next();
            if (System.currentTimeMillis() >= data.timeofUseBadges) {
                iterator.remove();
            } else if (data.isUse) {
                badges.idBadges = data.idBadGes;
            }
        }

        if (badges.idBadges != -1 && Util.canDoWithTime(badges.lastTimeSendBadges, 10000)) {
            Service.gI().sendBadgesPlayer(this, 5, badges.idBadges);
            badges.lastTimeSendBadges = System.currentTimeMillis();
            this.nPoint.update();
            Service.gI().point(this);
        }
    }

    public static final short[][] idOutfitGod = {
        {-1, 472, 473}, {-1, 476, 477}, {-1, 474, 475}
    };

    public static final short[][][] idOutfitHalloween = {
        {
            {545, 548, 549}, {547, 548, 549}, {546, 548, 549}
        },
        {
            {760, 761, 762}, {760, 761, 762}, {760, 761, 762}
        },
        {
            {654, 655, 656}, {654, 655, 656}, {654, 655, 656}
        },
        {
            {651, 652, 653}, {651, 652, 653}, {651, 652, 653}
        }};

    public static final short[][] idOutfitMafuba = {
        {2030, 2031, 2032}, {-1, -1, -1}, {1218, 1219, 1220}
    };

    public int getHat() {
        return -1;
    }

//    public byte getAura() {
//        if (!isPl() || this.Cards.isEmpty()) {
//            return -1;
//        }
//        if (this.effectSkill != null && this.effectSkill.isSuper) {
//            return idAuraSuper[gender][(playerSkill.getSkillbyId(gender == 0 ? 27 : gender == 1 ? 28 : 29).point - 1) - numUseSkill];
//        }
//        for (Card card : this.Cards) {
//            if (card != null //                    && (card.Id == 956 || card.Id == 1142) && card.Level > 1
//                    ) {
//                RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == card.Id).findFirst().orElse(null);
//                if (radarTemplate != null) {
//                    return (byte) radarTemplate.AuraId;
//                }
//            }
//        }
//        return -1;
//    }
    public byte getAura() {

        if (this.inventory.itemsBody.isEmpty()
                || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5); // Gốc là 8
        if (!item.isNotNullItem()) {
            return 0;
        }

        // LH: item 1929 da chuyen sang TYPE 11 (he thong Hao quang / flag_bag, msg -64),
        // khong con nam o slot 5 nua -> khong can hook o day.

        // Aura theo VIP - chinh tren panel (tab He Thong, nhom "VIP", phan "Aura theo VIP")
        int vipAura = this.Saga_VIP;
        if (vipAura >= 1 && vipAura <= 13) {
            return (byte) panel.tuning.SystemTuning.getI("vip_aura_" + vipAura);
        }
        return (byte) panel.tuning.SystemTuning.getI("vip_aura_0");
    }

    public byte getEffFront() {
        if (this.inventory == null) {
            return -1;
        }
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        long levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        long levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        long levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        long levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        long levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    private static final short[][] idOutFitSuperEarth = {
        {1436, 1437, 1438}, // level 1

        {1436, 1437, 1438}, // level 2

        {1442, 1437, 1438}, // level 3

        {1440, 1437, 1438}, // level 4

        {1439, 1437, 1438}, // level 5

        {1441, 1437, 1438}, // level 6 
    };

    private static final short[][] idOutFitSuperNamec = {
        {1430, 1431, 1432}, // level 1

        {1443, 1431, 1432}, // level 2

        {1444, 1431, 1432}, // level 3

        {1445, 1431, 1432}, // level 4

        {1446, 1431, 1432}, // level 5

        {1447, 1431, 1432}, // level 6 
    };

    private static final short[][] idOutFitSuperSaiyan = {
        {1433, 1434, 1435}, // level 1

        {1433, 1434, 1435}, // level 2

        {1448, 1434, 1435}, // level 3

        {1449, 1434, 1435}, // level 4

        {1450, 1434, 1435}, // level 5

        {1451, 1434, 1435}, // level 6 
    };

    private static final byte[][] idAuraSuper = {
        {20, 21, 22, 23, 24, 25},// Trái đất

        {26, 27, 28, 29, 30, 31},// namec

        {32, 33, 34, 35, 36, 37},// xayda
    };

    // index an toan cho trang thai Mafuba (typeBinh den tu du lieu skill, co the vuot mang)
    protected int mafubaRow() {
        int t = effectSkill != null ? effectSkill.typeBinh : 0;
        return (t < 0 || t >= idOutfitMafuba.length) ? 0 : t;
    }

    // index an toan cho trang thai Halloween (idOutfitHalloween chi co 4 dong 0..3)
    protected short[][] halloweenOutfit() {
        int idx = effectSkill != null ? effectSkill.idOutfitHalloween : 0;
        if (idx < 0 || idx >= idOutfitHalloween.length) {
            idx = 0;
        }
        return idOutfitHalloween[idx];
    }

    public short getHeadSuper() {
        switch (gender) {
            case 0:
                return idOutFitSuperEarth[(playerSkill.getSkillbyId(27).point - 1) - numUseSkill][0];
            case 1:
                return idOutFitSuperNamec[(playerSkill.getSkillbyId(28).point - 1) - numUseSkill][0];
            case 2:
                return idOutFitSuperSaiyan[(playerSkill.getSkillbyId(29).point - 1) - numUseSkill][0];
        }
        return -1;
    }

    public short getBodySuper() {
        switch (gender) {
            case 0:
                return idOutFitSuperEarth[(playerSkill.getSkillbyId(27).point - 1) - numUseSkill][1];
            case 1:
                return idOutFitSuperNamec[(playerSkill.getSkillbyId(28).point - 1) - numUseSkill][1];
            case 2:
                return idOutFitSuperSaiyan[(playerSkill.getSkillbyId(29).point - 1) - numUseSkill][1];
        }
        return -1;
    }

    public short getLegSuper() {
        switch (gender) {
            case 0:
                return idOutFitSuperEarth[(playerSkill.getSkillbyId(27).point - 1) - numUseSkill][2];
            case 1:
                return idOutFitSuperNamec[(playerSkill.getSkillbyId(28).point - 1) - numUseSkill][2];
            case 2:
                return idOutFitSuperSaiyan[(playerSkill.getSkillbyId(29).point - 1) - numUseSkill][2];
        }
        return -1;
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isSuper) {
            return getHeadSuper();
        }
        if (effectSkill != null) {
            if (effectSkill.isTranformation) {
                switch (this.gender) {
                    case 0:
                        ItemTimeService.gI().sendItemTime(this, 20958, this.effectSkill.timeTranformation / 1000);
                        return 1940;
                    case 1:
                        ItemTimeService.gI().sendItemTime(this, 20964, this.effectSkill.timeTranformation / 1000);
                        return 1957;
                    case 2:
                        ItemTimeService.gI().sendItemTime(this, 20952, this.effectSkill.timeTranformation / 1000);
                        return 1977;
                    default:
                }
            }
        }
        if (effectSkill != null) {
            if (effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20958);
                                ItemTimeService.gI().sendItemTime(this, 20959, this.effectSkill.timeTranformation / 1000);
                                return 1940;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20959);
                                ItemTimeService.gI().sendItemTime(this, 20960, this.effectSkill.timeTranformation / 1000);
                                return 1943;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20960);
                                ItemTimeService.gI().sendItemTime(this, 20961, this.effectSkill.timeTranformation / 1000);
                                return 1946;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20961);
                                ItemTimeService.gI().sendItemTime(this, 20962, this.effectSkill.timeTranformation / 1000);
                                return 1949;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20962);
                                ItemTimeService.gI().sendItemTime(this, 20963, this.effectSkill.timeTranformation / 1000);
                                return 1952;
                            default:
                        }
                    case 1:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20964);
                                ItemTimeService.gI().sendItemTime(this, 20965, this.effectSkill.timeTranformation / 1000);
                                return 1960;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20965);
                                ItemTimeService.gI().sendItemTime(this, 20966, this.effectSkill.timeTranformation / 1000);
                                return 1963;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20966);
                                ItemTimeService.gI().sendItemTime(this, 20967, this.effectSkill.timeTranformation / 1000);
                                return 1966;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20967);
                                ItemTimeService.gI().sendItemTime(this, 20968, this.effectSkill.timeTranformation / 1000);
                                return 1969;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20968);
                                ItemTimeService.gI().sendItemTime(this, 20969, this.effectSkill.timeTranformation / 1000);
                                return 1972;

                            default:
                        }

                    case 2:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20952);
                                ItemTimeService.gI().sendItemTime(this, 20953, this.effectSkill.timeTranformation / 1000);
                                return 1977;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20953);
                                ItemTimeService.gI().sendItemTime(this, 20954, this.effectSkill.timeTranformation / 1000);
                                return 1980;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20954);
                                ItemTimeService.gI().sendItemTime(this, 20955, this.effectSkill.timeTranformation / 1000);
                                return 1983;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20955);
                                ItemTimeService.gI().sendItemTime(this, 20956, this.effectSkill.timeTranformation / 1000);
                                return 1986;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20956);
                                ItemTimeService.gI().sendItemTime(this, 20957, this.effectSkill.timeTranformation / 1000);
                                return 1989;
                            default:
                        }

                    default:
                }
            }
        } else if (effectSkill != null && effectSkill.isBinh) {
            return idOutfitMafuba[mafubaRow()][0];
        }
        if (effectSkill != null && effectSkill.isStone) {
            return 454;
        }
        if (effectSkill != null && effectSkill.isHalloween) {
            return halloweenOutfit()[this.gender][0];
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            return ConstPlayer.OUTFIT_FUSION[gender][fusion.typeFusion - 1][0];
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int headId = inventory.itemsBody.get(5).template.head;
            if (headId != -1) {
                return (short) headId;
            }
        }
        return this.head;
    }

    public short getBody() {

        if (effectSkill != null && effectSkill.isSuper) {
            return getBodySuper();
        }
        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1955;
                    case 1:
                        return 1975;
                    case 2:
                        return 1992;
                    default:
                }
            }
        } else if (effectSkill != null && effectSkill.isBinh) {
            return idOutfitMafuba[mafubaRow()][1];
        }
        if (effectSkill != null && effectSkill.isStone) {
            return 455;
        }
        if (effectSkill != null && effectSkill.isHalloween) {
            return halloweenOutfit()[this.gender][1];
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (isPhuHoMapMabu && fusion != null && fusion.typeFusion == ConstPlayer.NON_FUSION) {
            return idOutfitGod[this.gender][1];
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            return ConstPlayer.OUTFIT_FUSION[gender][fusion.typeFusion - 1][1];
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {

        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1956;
                    case 1:
                        return 1976;
                    case 2:
                        return 1993;
                    default:
                }
            }
        }

        if (effectSkill != null && effectSkill.isSuper) {
            return getLegSuper();
        } else if (effectSkill != null && effectSkill.isBinh) {
            return idOutfitMafuba[mafubaRow()][2];
        }
        if (effectSkill != null && effectSkill.isStone) {
            return 456;
        }
        if (effectSkill != null && effectSkill.isHalloween) {
            return halloweenOutfit()[this.gender][2];
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (isPhuHoMapMabu && fusion != null && fusion.typeFusion == ConstPlayer.NON_FUSION) {
            return idOutfitGod[this.gender][2];
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            return ConstPlayer.OUTFIT_FUSION[gender][fusion.typeFusion - 1][2];
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.inventory.itemsBody.size() >= 11) {
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
//        if (this.isPet && this.inventory.itemsBody.size() >= 8) {
//            if (this.inventory.itemsBody.get(7).isNotNullItem()) {
//                return this.inventory.itemsBody.get(7).template.part;
//            }
//        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                try {
                    return (short) DataGame.MAP_MOUNT_NUM.get(item.template.id);
                } catch (Exception e) {
                    return -1;
                }

            }
        }
    }

    //--------------------------------------------------------------------------
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null && !plAtt.equals(this)) {
                setTemporaryEnemies(plAtt);
            }
            if (this.isBattu) {
                return 0;
            }
            if (plAtt != null && this.isPet && ((Pet) this).master.id == plAtt.id) {
                if (this.effectSkill != null && !this.effectSkill.isHalloween) {
                    EffectSkillService.gI().setIsHalloween(this, -1, 1800000);
                }
            }
            if (plAtt != null && plAtt.playerSkill.skillSelect != null && !plAtt.isBoss && MapService.gI().isMapMaBu(this.zone.map.mapId)) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC, Skill.DRAGON, Skill.DEMON, Skill.GALICK, Skill.LIEN_HOAN, Skill.KAIOKEN ->
                        damage = damage > this.nPoint.hpMax / 20 ? this.nPoint.hpMax / 20 : damage;
                }
            }
            if (plAtt != null && plAtt.isBoss) {
                this.effectSkin.isVoHinh = false;
                this.effectSkin.lastTimeVoHinh = System.currentTimeMillis();
            }
            if (plAtt != null && plAtt.effectSkill != null && plAtt.effectSkill.isBinh
                    && !Util.canDoWithTime(plAtt.effectSkill.lastTimeUpBinh, 3000)) {
                return 0;
            }
            if (plAtt != null && plAtt.isPl() && this.maBuHold != null && this.zone != null && this.zone.map.mapId == 128) {
                this.precentMabuHold++;
                damage = 1;
            }
            if (plAtt != null && this.nPoint.islinhthuydanhbac) {
                Service.gI().sendThongBao(plAtt, "Không thể tấn công! Vì người chơi này đã nạp lần đầu!");
                return 0;
            }
            if (plAtt != null && plAtt.idNRNM != -1 && (this.isBoss || this.isNewPet)) {
                return 1;
            }
            if (plAtt != null && (plAtt.idNRNM != -1 || this.idNRNM != -1) && plAtt.clan != null && this.clan != null && plAtt.clan == this.clan) {
                Service.gI().chatJustForMe(plAtt, this, "Ê cùng bang mà");
                return 0;
            }
            if (!Util.canDoWithTime(this.lastTimeRevived, 1500)) {
                return 0;
            }

            if (plAtt != null && plAtt.playerSkill.skillSelect != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC -> {
                        if (this.nPoint.voHieuChuong > 0) {
                            services.PlayerService.gI().hoiPhuc(this, 0, (int) (damage * this.nPoint.voHieuChuong / 100));
                            return 0;
                        }
                    }
                }
            }

            int tlGiap = this.nPoint.tlGiap;
            int tlNeDon = this.nPoint.tlNeDon;

            if (plAtt != null && !isMobAttack && plAtt.playerSkill.skillSelect != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC, Skill.DRAGON, Skill.DEMON, Skill.GALICK, Skill.LIEN_HOAN, Skill.KAIOKEN, Skill.QUA_CAU_KENH_KHI, Skill.MAKANKOSAPPO, Skill.DICH_CHUYEN_TUC_THOI ->
                        tlNeDon -= plAtt.nPoint.tlchinhxac;
                    default ->
                        tlNeDon = 0;
                }

                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC -> {
                        if (tlGiap - plAtt.nPoint.tlxgc >= 0) {
                            tlGiap -= plAtt.nPoint.tlxgc;
                        } else {
                            tlGiap = 0;
                        }
                    }
                    case Skill.DRAGON, Skill.DEMON, Skill.GALICK, Skill.LIEN_HOAN, Skill.KAIOKEN -> {
                        if (tlGiap - plAtt.nPoint.tlxgcc >= 0) {
                            tlGiap -= plAtt.nPoint.tlxgcc;
                        } else {
                            tlGiap = 0;
                        }
                    }
                }
            }

            if (piercing) {
                tlGiap = 0;
            }

            if (tlNeDon > 40) {
                tlNeDon = 40;
            }
            if (tlGiap > 30) {
                tlGiap = 30;
            }

            if (Util.isTrue(tlNeDon, 100)) {
                return 0;
            }

            damage -= ((damage / 100) * tlGiap);

            if (!piercing) {
                damage = this.nPoint.subDameInjureWithDeff(damage);
            }

            boolean isUseGX = false;
            if (!piercing && plAtt != null && plAtt.playerSkill.skillSelect != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC, Skill.DRAGON, Skill.DEMON, Skill.GALICK, Skill.LIEN_HOAN, Skill.KAIOKEN, Skill.QUA_CAU_KENH_KHI, Skill.MAKANKOSAPPO, Skill.DICH_CHUYEN_TUC_THOI ->
                        isUseGX = true;
                }
            }
            if ((isUseGX || isMobAttack) && this.itemTime != null) {
                if (this.itemTime.isUseGiapXen && !this.itemTime.isUseGiapXen2) {
                    damage /= 2;
                }
                if (this.itemTime.isUseGiapXen2) {
                    damage = damage / 100 * 40;
                }
            }

            if (!piercing && effectSkill.isShielding && !isMobAttack) {
                if (this.iDMark != null) {
                    this.iDMark.setDamePST(damage);
                }
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
                if (MapService.gI().isMapPhoBan(this.zone.map.mapId)) {
                    damage = 10;
                }
            }
            if (!piercing && plAtt == null && isMobAttack && (this.charms.tdBatTu > System.currentTimeMillis() || this.effectSkill != null && this.effectSkill.isHalloween) && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            if (this.zone.map.mapId == 129) {
                if (damage >= this.nPoint.hp) {
                    this.lostByDeath = true;
                    The23rdMartialArtCongress mc = The23rdMartialArtCongressManager.gI().getMC(zone);
                    if (mc != null) {
                        mc.die();
                    }
                    return 0;
                }
            }
            if (this.zone.map.mapId == 51) {
                this.totalDamageTaken += damage;
            }
            // SU KIEN TRUNG THU: boss su kien gay 2.000.000 sat thuong CO DINH,
            // ap dung SAU KHI da tinh het moi bo dieu chinh (giap, %, shield, crit...)
            if (plAtt != null && plAtt.isBoss && EventSuKien.TrungThuService.gI().isEventBossPlayer(plAtt)) {
                damage = EventSuKien.TrungThuService.gI().bossFixedDamage();
            }
            this.nPoint.subHP(damage);
            if ((plAtt != null || isMobAttack) && isDie() && !isBoss && !isNewPet && !isNewPet1) {
                if (Util.isTrue(this.nPoint.tlBom, 100)) {
                    setBom(plAtt);
                } else {
                    setDie(plAtt);
                }
            }

            return damage;
        } else {
            return 0;
        }
    }

    public void setTemporaryEnemies(Player pl) {
        if (!temporaryEnemies.contains(pl)) {
            temporaryEnemies.add(pl);
        }
    }

    protected void setBom(Player plAtt) {
        setDie(plAtt);
//        Service.gI().callClone(this);
    }

    public void kill(Player pl) {
        pl.injured(this, Double.MAX_VALUE, false, false);
        PlayerService.gI().sendInfoHpMpMoney(this);
        Service.gI().Send_Info_NV(this);
    }

    public void setDie() {
        this.setDie(null);
    }

    protected void setDie(Player plAtt) {
        TaskService.gI().checkDoneTaskKillPlayer(plAtt);
        if (this.isPl()) {
            double vangtru = this.nPoint.power / 1000000;
            if (vangtru > 32000) {
                vangtru = 32000;
            }

            int vang = (int) vangtru - Util.nextInt(10, 100);

            if (this.inventory.gold >= vang && vang >= 1) {
                this.inventory.gold -= vang;
                Service.gI().sendMoney(this);
                vang = vang * 95 / 100;
                if (vang < 10000) {
                    Service.gI().dropItemMap(this.zone, new ItemMap(zone, 189, vang, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), this.id));
                } else if (vang < 20000) {
                    Service.gI().dropItemMap(this.zone, new ItemMap(zone, 188, vang, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), this.id));
                } else {
                    Service.gI().dropItemMap(this.zone, new ItemMap(zone, 190, vang, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), this.id));
                }
            }
        }

        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        if (this.effectSkin.xDame > 1) {
            this.effectSkin.xDame = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
            this.mobMe.dispose();
            this.mobMe = null;
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isNewPet1 && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isNewPet1 && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk

        this.typePk = 0;

        if (this.pvp != null && this.zone.map.mapId != 140) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }

        BlackBallWarService.gI().dropBlackBall(this);
        NgocRongNamecService.gI().dropNamekBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session != null && this.session.isAdmin;
    }

    public void thuhoivp() {
        // Xử lý itemsBag
        Iterator<Item> bagIterator = inventory.itemsBag.iterator();
        while (bagIterator.hasNext()) {
            Item item = bagIterator.next();
            if (item.isNotNullItem() && item.itemOptions != null && !item.itemOptions.isEmpty()) {
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 209) {
                        bagIterator.remove();
                        InventoryService.gI().removeItemBag(this, item);
                        InventoryService.gI().sendItemBag(this);
                        Service.gI().sendThongBao(this, "Đã thu hồi Vật phẩm vì gây lỗi game!");
                        break;  // Dừng vòng lặp khi đã xóa phần tử
                    }
                }
            }
        }

        // Xử lý itemsBody
        Iterator<Item> bodyIterator = inventory.itemsBody.iterator();
        while (bodyIterator.hasNext()) {
            Item item = bodyIterator.next();
            if (item.isNotNullItem() && item.itemOptions != null && !item.itemOptions.isEmpty()) {
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 209) {
                        bodyIterator.remove();
                        InventoryService.gI().removeItem(inventory.itemsBody, item);
                        InventoryService.gI().sendItemBody(this);
                        Service.gI().sendThongBao(this, "Đã thu hồi Vật phẩm vì gây lỗi game!");
                        break;
                    }
                }
            }
        }

        // Xử lý itemsBox
        Iterator<Item> boxIterator = inventory.itemsBox.iterator();
        while (boxIterator.hasNext()) {
            Item item = boxIterator.next();
            if (item.isNotNullItem() && item.itemOptions != null && !item.itemOptions.isEmpty()) {
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 209) {
                        boxIterator.remove();
                        InventoryService.gI().removeItem(inventory.itemsBox, item);
                        InventoryService.gI().sendItemBox(this);
                        Service.gI().sendThongBao(this, "Đã thu hồi Vật phẩm vì gây lỗi game!");
                        break;
                    }
                }
            }
        }

        // Xử lý pet.itemsBody
        if (pet != null) {
            Iterator<Item> petBodyIterator = pet.inventory.itemsBody.iterator();
            while (petBodyIterator.hasNext()) {
                Item item = petBodyIterator.next();
                if (item.isNotNullItem() && item.itemOptions != null && !item.itemOptions.isEmpty()) {
                    for (Item.ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 209) {
                            petBodyIterator.remove();
                            InventoryService.gI().removeItem(pet.inventory.itemsBody, item);
                            InventoryService.gI().sendItemBag(pet);
                            Service.gI().sendThongBao(this, "Đã thu hồi Vật phẩm vì gây lỗi game!");
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public String LinhCanEmBe(int linhcan) {
        switch (linhcan) {
            case 10:
                return "Hỗn Độn Tiên Linh Căn";
            case 9:
                return "Ma Linh Căn";
            case 8:
                return "Thánh Linh Căn";
            case 7:
                return "Hỗn Độn Linh Căn";
            case 6:
                return "Ngũ Linh Căn";
            case 5:
                return "Tam Linh Căn";
            case 4:
                return "Song Linh Căn";
            case 3:
                return "Đơn Linh Căn";
            case 2:
                return "Ngũ Hành Linh Căn";
            case 1:
                return "Tiên Linh Căn";
            case 0:
                return "Thiên Linh Căn";
            default:
                return "Phế Vật Không Linh Căn";
        }
    }

    public String KyNangEmBe(int Kynang) {
        switch (Kynang) {
            case 10:
                return "Tìm " + ((EmBeLv + 1) * 20) + " Hồng ngọc, ngọc xanh cho chủ\n"
                        + "Chuyển " + ((EmBeLv + 1) * 4) + " Exp Thiên Đạo, Địa Đảo,Tu Tiên qua cho chủ\n"
                        + "Tăng " + ((EmBeLv + 1) * 5) + "% HP,KI,Giáp\n" + ((EmBeLv + 1) * 3)
                        + "% SD, SD chí mạng cho chủ\n"
                        + "Tìm\n" + ((EmBeLv + 1) * 20) + " HP,kI,Giáp gốc\n" + ((EmBeLv + 1) * 5)
                        + " SD gốc\ncho chủ";
            case 9:
                return "Tăng " + ((EmBeLv + 1) * 5) + "% HP,KI,Giáp\n" + ((EmBeLv + 1) * 3)
                        + "% SD, SD chí mạng cho chủ";
            case 8:
                return "Chuyển " + ((EmBeLv + 1) * 4) + " Exp Thiên Đạo, Địa Đạo, tu tiên qua cho chủ";
            case 7:
                return "Tìm\n" + ((EmBeLv + 1) * 20) + " HP,kI,Giáp gốc\n" + ((EmBeLv + 1) * 5)
                        + " SD gốc\ncho chủ";
            case 6:
                return "Tăng " + ((EmBeLv + 1) * 3) + "% SD Chí mạng cho chủ";
            case 5:
                return "Tăng " + ((EmBeLv + 1) * 3) + "% SD cho chủ";
            case 4:
                return "Tăng " + ((EmBeLv + 1) * 5) + "% HP,KI,Giáp cho chủ";
            case 3:
                return "Chuyển " + ((EmBeLv + 1) * 4) + " Exp Thiên Đạoqua cho chủ";
            case 2:
                return "Chuyển " + ((EmBeLv + 1) * 4) + " Exp Tu tiên qua cho chủ";
            case 1:
                return "Chuyển " + ((EmBeLv + 1) * 4) + " Exp Địa Đạo qua cho chủ";
            case 0:
                return "Tìm " + ((EmBeLv + 1) * 20) + " Hồng ngọc, ngọc xanh cho chủ";
            default:
                return "Phế VậT Đấu Kiếm";
        }
    }

    public boolean actived() {
        return (this.isPl() && this.session != null && this.session.actived) || (this.isPet && ((Pet) this).master.session != null && ((Pet) this).master.session.actived);
    }

//    public void sendNewPet() {
//        if (isPl() && inventory != null && inventory.itemsBody.get(7) != null) {
//            Item it = inventory.itemsBody.get(7);
//            if (it != null && it.isNotNullItem() && newPet == null) {
//                switch (it.template.id) {
//                    case 942 -> {
//                        PetService.Pet2(this, 966, 967, 968);
//                        Service.gI().point(this);
//                    }
//                    case 943 -> {
//                        PetService.Pet2(this, 969, 970, 971);
//                        Service.gI().point(this);
//                    }
//                    case 944 -> {
//                        PetService.Pet2(this, 972, 973, 974);
//                        Service.gI().point(this);
//                    }
//                    case 967 -> {
//                        PetService.Pet2(this, 1050, 1051, 1052);
//                        Service.gI().point(this);
//                    }
//                    case 968 -> {
//                        PetService.Pet2(this, 1183, 1184, 1185);
//                        Service.gI().point(this);
//                    }
//                }
//            }
//        }
//    }
    private void fixBlackBallWar() {
        int x = this.location.x;
        int y = this.location.y;
        switch (this.zone.map.mapId) {
            case 85, 86, 87, 88, 89, 90, 91 -> {
                if (this.isPl()) {
                    if (x < 24 || x > this.zone.map.mapWidth - 24 || y < 0 || y > this.zone.map.mapHeight - 24) {
                        if (MapService.gI().getWaypointPlayerIn(this) == null) {
                            Service.gI().resetPoint(this, x, this.zone.map.yPhysicInTop(this.location.x, 100));
                            this.nPoint.hp -= this.nPoint.hpMax / 10;
                            PlayerService.gI().sendInfoHp(this);
                            return;
                        }
                    }
                    int yTop = this.zone.map.yPhysicInTop(this.location.x, this.location.y);
                    if (yTop >= this.zone.map.mapHeight - 24) {
                        Service.gI().resetPoint(this, x, this.zone.map.yPhysicInTop(this.location.x, 100));
                        this.nPoint.hp -= this.nPoint.hpMax / 10;
                        PlayerService.gI().sendInfoHp(this);
                    }
                }
            }
        }
    }

    public String getTuVi() {
        double power = this.nPoint.power;
        String[] realmsList = {
            "Phàm Nhân", "Luyện Khí", "Trúc Cơ", "Kết Đan", "Nguyên Anh", "Hóa Thần",
            "Luyện Hư", "Hợp Thể", "Đại Thừa", "Độ Kiếp", "Chuẩn Thánh",
            "Bán Thánh", "Thánh Vương", "Chuẩn Đế", "Bán Bộ Đại Đế", "Đại Đế"
        };
        String[] stagesList = {
            "Nhất Trọng", "Nhị Trọng", "Tam Trọng", "Tứ Trọng", "Ngũ Trọng",
            "Lục Trọng", "Thất Trọng", "Bát Trọng", "Cửu Trọng", "Đỉnh Phong"
        };
        int totalLevels = 1 + (realmsList.length - 1) * stagesList.length;
        double minPower = 2000.0;
        double maxPower = Double.MAX_VALUE;
        double scale = Math.pow(maxPower / minPower, 1.0 / (totalLevels - 1));
        double[] powerThresholds = new double[totalLevels];
        for (int i = 0; i < totalLevels; i++) {
            powerThresholds[i] = minPower * Math.pow(scale, i);
            if (power < minPower) {
                return "Phế Vật";
            }
            if (power >= maxPower) {
                return "Hoang Cổ Thiên Đế";
            }
            for (int j = 0; j < totalLevels - 1; j++) {
                if (power >= powerThresholds[j] && power < powerThresholds[j + 1]) {
                    if (i == 0) {
                        return "Phàm Nhân";
                    } else {
                        int realmIndex = (j - 1) / stagesList.length + 1;
                        int stageIndex = (j - 1) % stagesList.length;
                        return realmsList[realmIndex] + " " + stagesList[stageIndex];
                    }
                }
            }
        }
        return "";
    }

    public String TamkjllTuviTutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return "Luyện khí Tầng 1";
            case 1:
                return "Luyện khí Tầng 2";
            case 2:
                return "Luyện khí Tầng 3";
            case 3:
                return "Luyện khí Tầng 4";
            case 4:
                return "Luyện khí Tầng 5";
            case 5:
                return "Luyện khí Tầng 6";
            case 6:
                return "Luyện khí Tầng 7";
            case 7:
                return "Luyện khí Tầng 8";
            case 8:
                return "Luyện khí Tầng 9";
            case 9:
                return "Luyện khí đỉnh phong";
            case 10:
                return "Trúc Cơ Tầng 1";
            case 11:
                return "Trúc Cơ Tầng 2";
            case 12:
                return "Trúc Cơ Tầng 3";
            case 13:
                return "Trúc Cơ Tầng 4";
            case 14:
                return "Trúc Cơ Tầng 5";
            case 15:
                return "Trúc Cơ Tầng 6";
            case 16:
                return "Trúc Cơ Tầng 7";
            case 17:
                return "Trúc Cơ Tầng 8";
            case 18:
                return "Trúc Cơ Tầng 9";
            case 19:
                return "Trúc Cơ đỉnh phong";
            case 20:
                return "Kim Đan Tầng 1";
            case 21:
                return "Kim Đan Tầng 2";
            case 22:
                return "Kim Đan Tầng 3";
            case 23:
                return "Kim Đan Tầng 4";
            case 24:
                return "Kim Đan Tầng 5";
            case 25:
                return "Kim Đan Tầng 6";
            case 26:
                return "Kim Đan Tầng 7";
            case 27:
                return "Kim Đan Tầng 8";
            case 28:
                return "Kim Đan Tầng 9";
            case 29:
                return "Kim Đan đỉnh phong";
            case 30:
                return "Nguyên Anh Tầng 1";
            case 31:
                return "Nguyên Anh Tầng 2";
            case 32:
                return "Nguyên Anh Tầng 3";
            case 33:
                return "Nguyên Anh Tầng 4";
            case 34:
                return "Nguyên Anh Tầng 5";
            case 35:
                return "Nguyên Anh Tầng 6";
            case 36:
                return "Nguyên Anh Tầng 7";
            case 37:
                return "Nguyên Anh Tầng 8";
            case 38:
                return "Nguyên Anh Tầng 9";
            case 39:
                return "Nguyên Anh đỉnh phong";
            case 40:
                return "Hóa Thần Tầng 1";
            case 41:
                return "Hóa Thần Tầng 2";
            case 42:
                return "Hóa Thần Tầng 3";
            case 43:
                return "Hóa Thần Tầng 4";
            case 44:
                return "Hóa Thần Tầng 5";
            case 45:
                return "Hóa Thần Tầng 6";
            case 46:
                return "Hóa Thần Tầng 7";
            case 47:
                return "Hóa Thần Tầng 8";
            case 48:
                return "Hóa Thần Tầng 9";
            case 49:
                return "Hóa Thần đỉnh phong";
            case 50:
                return "Luyện Hư Tầng 1";
            case 51:
                return "Luyện Hư Tầng 2";
            case 52:
                return "Luyện Hư Tầng 3";
            case 53:
                return "Luyện Hư Tầng 4";
            case 54:
                return "Luyện Hư Tầng 5";
            case 55:
                return "Luyện Hư Tầng 6";
            case 56:
                return "Luyện Hư Tầng 7";
            case 57:
                return "Luyện Hư Tầng 8";
            case 58:
                return "Luyện Hư Tầng 9";
            case 59:
                return "Luyện Hư đỉnh phong";
            case 60:
                return "Hợp Thể Tầng 1";
            case 61:
                return "Hợp Thể Tầng 2";
            case 62:
                return "Hợp Thể Tầng 3";
            case 63:
                return "Hợp Thể Tầng 4";
            case 64:
                return "Hợp Thể Tầng 5";
            case 65:
                return "Hợp Thể Tầng 6";
            case 66:
                return "Hợp Thể Tầng 7";
            case 67:
                return "Hợp Thể Tầng 8";
            case 68:
                return "Hợp Thể Tầng 9";
            case 69:
                return "Hợp Thể đỉnh phong";
            case 70:
                return "Quy Nguyên Tầng 1";
            case 71:
                return "Quy Nguyên Tầng 2";
            case 72:
                return "Quy Nguyên Tầng 3";
            case 73:
                return "Quy Nguyên Tầng 4";
            case 74:
                return "Quy Nguyên Tầng 5";
            case 75:
                return "Quy Nguyên Tầng 6";
            case 76:
                return "Quy Nguyên Tầng 7";
            case 77:
                return "Quy Nguyên Tầng 8";
            case 78:
                return "Quy Nguyên Tầng 9";
            case 79:
                return "Quy Nguyên đỉnh phong";
            case 80:
                return "Nhập Đạo Tầng 1";
            case 81:
                return "Nhập Đạo Tầng 2";
            case 82:
                return "Nhập Đạo Tầng 3";
            case 83:
                return "Nhập Đạo Tầng 4";
            case 84:
                return "Nhập Đạo Tầng 5";
            case 85:
                return "Nhập Đạo Tầng 6";
            case 86:
                return "Nhập Đạo Tầng 7";
            case 87:
                return "Nhập Đạo Tầng 8";
            case 88:
                return "Nhập Đạo Tầng 9";
            case 89:
                return "Nhập Đạo Đỉnh cao";
            case 90:
                return "Thánh Nhân Tầng 1";
            case 91:
                return "Thánh Nhân Tầng 2";
            case 92:
                return "Thánh Nhân Tầng 2.5";
            case 93:
                return "Thánh Nhân Tầng 3";
            case 94:
                return "Thánh Nhân Tầng 4";
            case 95:
                return "Thánh Nhân Tầng 5";
            case 96:
                return "Thánh Nhân Tầng 6";
            case 97:
                return "Thánh Nhân Tầng 7";
            case 98:
                return "Thánh Nhân Tầng 8";
            case 99:
                return "Thánh Nhân Tầng 9";
            case 100:
                return "Thánh Nhân Đỉnh Phong";
            case 101:
                return "Thánh Vương Tầng 1";
            case 102:
                return "Thánh Vương Tầng 2";
            case 103:
                return "Thánh Vương Tầng 3";
            case 104:
                return "Thánh Vương Tầng 4";
            case 105:
                return "Thánh Vương Tầng 5";
            case 106:
                return "Thánh Vương Tầng 6";
            case 107:
                return "Thánh Vương Tầng 7";
            case 108:
                return "Thánh Vương Tầng 8";
            case 109:
                return "Thánh Vương Tầng 9";
            case 110:
                return "Thánh Vương Đỉnh Phong";
            case 111:
                return "Thánh Hoàng Tầng 1";
            case 112:
                return "Thánh Hoàng Tầng 2";
            case 113:
                return "Thánh Hoàng Tầng 3";
            case 114:
                return "Thánh Hoàng Tầng 4";
            case 115:
                return "Thánh Hoàng Tầng 5";
            case 116:
                return "Thánh Hoàng Tầng 6";
            case 117:
                return "Thánh Hoàng Tầng 7";
            case 118:
                return "Thánh Hoàng Tầng 8";
            case 119:
                return "Thánh Hoàng Tầng 9";
            case 120:
                return "Thánh Hoàng Đỉnh Phong";
            case 121:
                return "Thánh Tôn Tầng 1";
            case 122:
                return "Thánh Tôn Tầng 2";
            case 123:
                return "Thánh Tôn Tầng 3";
            case 124:
                return "Thánh Tôn Tầng 4";
            case 125:
                return "Thánh Tôn Tầng 5";
            case 126:
                return "Thánh Tôn Tầng 6";
            case 127:
                return "Thánh Tôn Tầng 7";
            case 128:
                return "Thánh Tôn Tầng 8";
            case 129:
                return "Thánh Tôn Tầng 9";
            case 130:
                return "Thánh Tôn Đỉnh Phong";
            case 131:
                return "Hậu Thiên Cổ Thánh Tầng 1";
            case 132:
                return "Hậu Thiên Cổ Thánh Tầng 2";
            case 133:
                return "Hậu Thiên Cổ Thánh Tầng 3";
            case 134:
                return "Hậu Thiên Cổ Thánh Tầng 4";
            case 135:
                return "Hậu Thiên Cổ Thánh Tầng 5";
            case 136:
                return "Hậu Thiên Cổ Thánh Tầng 6";
            case 137:
                return "Hậu Thiên Cổ Thánh Tầng 7";
            case 138:
                return "Hậu Thiên Cổ Thánh Tầng 8";
            case 139:
                return "Hậu Thiên Cổ Thánh Tầng 9";
            case 140:
                return "Hậu Thiên Cổ Thánh Đỉnh Phong";
            case 141:
                return "Tiên Thiên Cổ Thánh Tầng 1";
            case 142:
                return "Tiên Thiên Cổ Thánh Tầng 2";
            case 143:
                return "Tiên Thiên Cổ Thánh Tầng 3";
            case 144:
                return "Tiên Thiên Cổ Thánh Tầng 4";
            case 145:
                return "Tiên Thiên Cổ Thánh Tầng 5";
            case 146:
                return "Tiên Thiên Cổ Thánh Tầng 6";
            case 147:
                return "Tiên Thiên Cổ Thánh Tầng 7";
            case 148:
                return "Tiên Thiên Cổ Thánh Tầng 8";
            case 149:
                return "Tiên Thiên Cổ Thánh Tầng 9";
            case 150:
                return "Tiên Thiên Cổ Thánh Đỉnh Phong";
            case 151:
                return "Chí Tôn Thánh Hiền Tầng 1";
            case 152:
                return "Chí Tôn Thánh Hiền Tầng 2";
            case 153:
                return "Chí Tôn Thánh Hiền Tầng 3";
            case 154:
                return "Chí Tôn Thánh Hiền Tầng 4";
            case 155:
                return "Chí Tôn Thánh Hiền Tầng 5";
            case 156:
                return "Chí Tôn Thánh Hiền Tầng 6";
            case 157:
                return "Chí Tôn Thánh Hiền Tầng 7";
            case 158:
                return "Chí Tôn Thánh Hiền Tầng 8";
            case 159:
                return "Chí Tôn Thánh Hiền Tầng 9";
            case 160:
                return "Chí Tôn Thánh Hiền Đỉnh Phong";
            case 161:
                return "Chuẩn Đế Tầng 1";
            case 162:
                return "Chuẩn Đế Tầng 2";
            case 163:
                return "Chuẩn Đế Tầng 3";
            case 164:
                return "Chuẩn Đế Tầng 4";
            case 165:
                return "Chuẩn Đế Tầng 5";
            case 166:
                return "Chuẩn Đế Tầng 6";
            case 167:
                return "Chuẩn Đế Tầng 7";
            case 168:
                return "Chuẩn Đế Tầng 8";
            case 169:
                return "Chuẩn Đế Tầng 9";
            case 170:
                return "Chuẩn Đế Đỉnh Phong";
            case 171:
                return "Đại Đế Tầng 1";
            case 172:
                return "Đại Đế Tầng 2";
            case 173:
                return "Đại Đế Tầng 3";
            case 174:
                return "Đại Đế Tầng 4";
            case 175:
                return "Đại Đế Tầng 5";
            case 176:
                return "Đại Đế Tầng 6";
            case 177:
                return "Đại Đế Tầng 7";
            case 178:
                return "Đại Đế Tầng 8";
            case 179:
                return "Đại Đế Tầng 9";
            case 180:
                return "Đại Đế Đỉnh Phong";
            case 181:
                return "Cổ Đế Tầng 1";
            case 182:
                return "Cổ Đế Tầng 2";
            case 183:
                return "Cổ Đế Tầng 3";
            case 184:
                return "Cổ Đế Tầng 4";
            case 185:
                return "Cổ Đế Tầng 5";
            case 186:
                return "Cổ Đế Tầng 6";
            case 187:
                return "Cổ Đế Tầng 7";
            case 188:
                return "Cổ Đế Tầng 8";
            case 189:
                return "Cổ Đế Tầng 9";
            case 190:
                return "Cổ Đế Đỉnh Phong";
            case 191:
                return "Hồng Trần Tiên Tầng 1";
            case 192:
                return "Hồng Trần Tiên Tầng 2";
            case 193:
                return "Hồng Trần Tiên Tầng 3";
            case 194:
                return "Hồng Trần Tiên Tầng 4";
            case 195:
                return "Hồng Trần Tiên Tầng 5";
            case 196:
                return "Hồng Trần Tiên Tầng 6";
            case 197:
                return "Hồng Trần Tiên Tầng 7";
            case 198:
                return "Hồng Trần Tiên Tầng 8";
            case 199:
                return "Hồng Trần Tiên Tầng 9";
            case 200:
                return "Hồng Trần Tiên Đỉnh Phong";
            case 201:
                return "Địa Tiên Tầng 1";
            case 202:
                return "Địa Tiên Tầng 2";
            case 203:
                return "Địa Tiên Tầng 3";
            case 204:
                return "Địa Tiên Tầng 4";
            case 205:
                return "Địa Tiên Tầng 5";
            case 206:
                return "Địa Tiên Tầng 6";
            case 207:
                return "Địa Tiên Tầng 7";
            case 208:
                return "Địa Tiên Tầng 8";
            case 209:
                return "Địa Tiên Tầng 9";
            case 210:
                return "Địa Tiên Đỉnh Phong";
            case 211:
                return "Thiên Tiên Tầng 1";
            case 212:
                return "Thiên Tiên Tầng 2";
            case 213:
                return "Thiên Tiên Tầng 3";
            case 214:
                return "Thiên Tiên Tầng 4";
            case 215:
                return "Thiên Tiên Tầng 5";
            case 216:
                return "Thiên Tiên Tầng 6";
            case 217:
                return "Thiên Tiên Tầng 7";
            case 218:
                return "Thiên Tiên Tầng 8";
            case 219:
                return "Thiên Tiên Tầng 9";
            case 220:
                return "Thiên Tiên Đỉnh Phong";
            case 221:
                return "Thái Ất Chân Tiên Tầng 1";
            case 222:
                return "Thái Ất Chân Tiên Tầng 2";
            case 223:
                return "Thái Ất Chân Tiên Tầng 3";
            case 224:
                return "Thái Ất Chân Tiên Tầng 4";
            case 225:
                return "Thái Ất Chân Tiên Tầng 5";
            case 226:
                return "Thái Ất Chân Tiên Tầng 6";
            case 227:
                return "Thái Ất Chân Tiên Tầng 7";
            case 228:
                return "Thái Ất Chân Tiên Tầng 8";
            case 229:
                return "Thái Ất Chân Tiên Tầng 9";
            case 230:
                return "Thái Ất Chân Tiên Đỉnh Phong";
            case 231:
                return "Đại Chí Huyền Tiên Tầng 1";
            case 232:
                return "Đại Chí Huyền Tiên Tầng 2";
            case 233:
                return "Đại Chí Huyền Tiên Tầng 3";
            case 234:
                return "Đại Chí Huyền Tiên Tầng 4";
            case 235:
                return "Đại Chí Huyền Tiên Tầng 5";
            case 236:
                return "Đại Chí Huyền Tiên Tầng 6";
            case 237:
                return "Đại Chí Huyền Tiên Tầng 7";
            case 238:
                return "Đại Chí Huyền Tiên Tầng 8";
            case 239:
                return "Đại Chí Huyền Tiên Tầng 9";
            case 240:
                return "Đại Chí Huyền Tiên Đỉnh Phong";
            case 241:
                return "Đại La Kim Tiên Tầng 1";
            case 242:
                return "Đại La Kim Tiên Tầng 2";
            case 243:
                return "Đại La Kim Tiên Tầng 3";
            case 244:
                return "Đại La Kim Tiên Tầng 4";
            case 245:
                return "Đại La Kim Tiên Tầng 5";
            case 246:
                return "Đại La Kim Tiên Tầng 6";
            case 247:
                return "Đại La Kim Tiên Tầng 7";
            case 248:
                return "Đại La Kim Tiên Tầng 8";
            case 249:
                return "Đại La Kim Tiên Tầng 9";
            case 250:
                return "Đại La Kim Tiên Đỉnh Phong";
            case 251:
                return "Hỗn Nguyên Kim Tiên Tầng 1";
            case 252:
                return "Hỗn Nguyên Kim Tiên Tầng 2";
            case 253:
                return "Hỗn Nguyên Kim Tiên Tầng 3";
            case 254:
                return "Hỗn Nguyên Kim Tiên Tầng 4";
            case 255:
                return "Hỗn Nguyên Kim Tiên Tầng 5";
            case 256:
                return "Hỗn Nguyên Kim Tiên Tầng 6";
            case 257:
                return "Hỗn Nguyên Kim Tiên Tầng 7";
            case 258:
                return "Hỗn Nguyên Kim Tiên Tầng 8";
            case 259:
                return "Hỗn Nguyên Kim Tiên Tầng 9";
            case 260:
                return "Hỗn Nguyên Kim Tiên Đỉnh Phong";
            case 261:
                return "Tiên Vương Tầng 1";
            case 262:
                return "Tiên Vương Tầng 2";
            case 263:
                return "Tiên Vương Tầng 3";
            case 264:
                return "Tiên Vương Tầng 4";
            case 265:
                return "Tiên Vương Tầng 5";
            case 266:
                return "Tiên Vương Tầng 6";
            case 267:
                return "Tiên Vương Tầng 7";
            case 268:
                return "Tiên Vương Tầng 8";
            case 269:
                return "Tiên Vương Tầng 9";
            case 270:
                return "Tiên Vương Đỉnh Phong";
            case 271:
                return "Tiên Quân Tầng 1";
            case 272:
                return "Tiên Quân Tầng 2";
            case 273:
                return "Tiên Quân Tầng 3";
            case 274:
                return "Tiên Quân Tầng 4";
            case 275:
                return "Tiên Quân Tầng 5";
            case 276:
                return "Tiên Quân Tầng 6";
            case 277:
                return "Tiên Quân Tầng 7";
            case 278:
                return "Tiên Quân Tầng 8";
            case 279:
                return "Tiên Quân Tầng 9";
            case 280:
                return "Tiên Quân Đỉnh Phong";
            case 281:
                return "Tiên Tôn Tầng 1";
            case 282:
                return "Tiên Tôn Tầng 2";
            case 283:
                return "Tiên Tôn Tầng 3";
            case 284:
                return "Tiên Tôn Tầng 4";
            case 285:
                return "Tiên Tôn Tầng 5";
            case 286:
                return "Tiên Tôn Tầng 6";
            case 287:
                return "Tiên Tôn Tầng 7";
            case 288:
                return "Tiên Tôn Tầng 8";
            case 289:
                return "Tiên Tôn Tầng 9";
            case 290:
                return "Tiên Tôn Đỉnh Phong";
            case 291:
                return "Tiên Đế Tầng 1";
            case 292:
                return "Tiên Đế Tầng 2";
            case 293:
                return "Tiên Đế Tầng 3";
            case 294:
                return "Tiên Đế Tầng 4";
            case 295:
                return "Tiên Đế Tầng 5";
            case 296:
                return "Tiên Đế Tầng 6";
            case 297:
                return "Tiên Đế Tầng 7";
            case 298:
                return "Tiên Đế Tầng 8";
            case 299:
                return "Tiên Đế Tầng 9";
            case 300:
                return "Tiên Đế Đỉnh Phong";
            case 301:
                return "Chúa Tể Tầng 1";
            case 302:
                return "Chúa Tể Tầng 2";
            case 303:
                return "Chúa Tể Tầng 3";
            case 304:
                return "Chúa Tể Tầng 4";
            case 305:
                return "Chúa Tể Tầng 5";
            case 306:
                return "Chúa Tể Tầng 6";
            case 307:
                return "Chúa Tể Tầng 7";
            case 308:
                return "Chúa Tể Tầng 8";
            case 309:
                return "Chúa Tể Tầng 9";
            case 310:
                return "Chúa Tể Đỉnh Phong";
            case 311:
                return "Cấm Kỵ Tầng 1";
            case 312:
                return "Cấm Kỵ Tầng 2";
            case 313:
                return "Cấm Kỵ Tầng 3";
            case 314:
                return "Cấm Kỵ Tầng 4";
            case 315:
                return "Cấm Kỵ Tầng 5";
            case 316:
                return "Cấm Kỵ Tầng 6";
            case 317:
                return "Cấm Kỵ Tầng 9";
            case 318:
                return "Cấm Kỵ Tầng 8";
            case 319:
                return "Cấm Kỵ Tầng 9";
            case 320:
                return "Cấm Kỵ Đỉnh Phong";
            case 321:
                return "Hậu Thiên Cổ Thần Tầng 1";
            case 322:
                return "Hậu Thiên Cổ Thần Tầng 2";
            case 323:
                return "Hậu Thiên Cổ Thần Tầng 3";
            case 324:
                return "Hậu Thiên Cổ Thần Tầng 4";
            case 325:
                return "Hậu Thiên Cổ Thần Tầng 5";
            case 326:
                return "Hậu Thiên Cổ Thần Tầng 6";
            case 327:
                return "Hậu Thiên Cổ Thần Tầng 7";
            case 328:
                return "Hậu Thiên Cổ Thần Tầng 8";
            case 329:
                return "Hậu Thiên Cổ Thần Tầng 9";
            case 330:
                return "Hậu Thiên Cổ Thần Đỉnh Phong";
            case 331:
                return "Tiên Thiên Cổ Thần Tôn Tầng 1";
            case 332:
                return "Tiên Thiên Cổ Thần Tôn Tầng 2";
            case 333:
                return "Tiên Thiên Cổ Thần Tôn Tầng 3";
            case 334:
                return "Tiên Thiên Cổ Thần Tôn Tầng 4";
            case 335:
                return "Tiên Thiên Cổ Thần Tôn Tầng 5";
            case 336:
                return "Tiên Thiên Cổ Thần Tôn Tầng 6";
            case 337:
                return "Tiên Thiên Cổ Thần Tôn Tầng 7";
            case 338:
                return "Tiên Thiên Cổ Thần Tôn Tầng 8";
            case 339:
                return "Tiên Thiên Cổ Thần Tôn Tầng 9";
            case 340:
                return "Tiên Thiên Cổ Thần Tôn Đỉnh Phong";

            default:
                return "Phế vật";
        }
    }

    public String linhcan(int lvtt) {
        switch (lvtt) {
            case 0:
                return "Phàm Thể";
            case 1:
                return "Linh Căn Thường";
            case 2:
                return "Nhị Linh Căn";
            case 3:
                return "Tam Linh Căn";
            case 4:
                return "Địa Linh Căn";
            case 5:
                return "Thiên Linh Căn";
            case 6:
                return "Thuần Linh Căn";
            case 7:
                return "Tiên Thiên Linh Căn";
            case 8:
                return "Thiên Chi Kiêu Tử";
            case 9:
                return "Yêu Nghiệt Thiên Phú";
            case 10:
                return "Tiên Thiên Thần Căn";
            case 11:
                return "Thần Huyết Dị Cốt";
            case 12:
                return "Hoang Cổ Thể";
            case 13:
                return "Hoang Cổ Thánh Thể";
            case 14:
                return "Thánh Thể";
            case 15:
                return "Bất Diệt Thánh Thể";
            case 16:
                return "Cổ Thần Thể";
            case 17:
                return "Cổ Đế Thể";
            case 18:
                return "Chí Tôn Chi Thể";
            case 19:
                return "Vạn Cổ Chí Tôn Thể";
            case 20:
                return "Hỗn Độn Linh Căn";
            case 21:
                return "Hỗn Độn Thể";
            case 22:
                return "Hỗn Độn Thánh Thể";
            case 23:
                return "Nguyên Thủy Hỗn Độn Thể";
            case 24:
                return "Vô Thượng Hỗn Độn Thần Thể";
            case 25:
                return "Thiên Đạo Chi Tử";
            case 26:
                return "Vô Thượng Đạo Thể";
            case 27:
                return "Vũ Trụ Chi Tâm";
            case 28:
                return "Chúa Tể Vạn Đạo";
            case 29:
                return "Vô Thượng Độc Tôn";

            default:
                return "Phế Vật Tu Luyện";
        }
    }

    public double bemeocanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return 10f;
            case 5:
            case 6:
            case 7:
            case 8:
                return 7f;
            case 9:
                return 3f;
            case 10:
            case 11:
            case 12:
            case 13:
                return 8f;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                return 2f;
            case 19:
                return 10f;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                return 15f;
            case 29:
                return 2f;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return 14f;
            case 39:
                return 5f;
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
                return 15f;
            case 49:
                return 2f;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                return 16f;
            case 59:
                return 5f;
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return 25f;
            case 69:
                return 3f;
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
                return 35f;
            case 79:
                return 2f;
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
                return 48f;
            case 89:
                return 4f;
            case 90:
                return 1f;
            case 91:
                return 3f;
            case 92:
            case 93:
                return 4f;
            case 94:
                return 3f;
            case 95:
                return 7f;
            case 96:
                return 1f;
            case 97:
            case 98:
            case 99:
                return 3f;
            case 100:
                return 1f;
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                return 11f;
            case 110:
                return 1f;
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
                return 7f;
            case 120:
                return 1f;
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
                return 3f;
            default:
                return 1f;
        }
    }

    public int TamkjllHpKiGiaptutien(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 356);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }

    public int TamkjllDametutien(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 287);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }

    public int tancongtutien(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 1000000);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }
      public int lamchodep(int lvtt) {
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 1000000);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }

    public int hpkitutien(int lvtt) {// mỗi cấp tăng 2% tnsm
        if (lvtt >= 0 && lvtt <= 340) {
            return (int) (lvtt * 2500000);
        }
        return -1; // Trả về -1 nếu lvtt ngoài phạm vi 0-9
    }

    public void move(int _toX, int _toY) {
        if (_toX != this.location.x) {
            this.location.x = _toX;
        }
        if (_toY != this.location.y) {
            this.location.y = _toY;
        }
        MapService.gI().sendPlayerMove(this);
    }

    public void dispose() {
        // dọn bố cục vòng quay Tầm Bảo đã cache của nhân vật này
        TamBao.gI().clearCache(this.id);
        if (itemsTradeWVP != null) {
            if (!itemsTradeWVP.isEmpty()) {
                for (Item item : itemsTradeWVP) {
                    InventoryService.gI().addItemBag(this, item, 999999);

                }
            }
            itemsTradeWVP.clear();
            itemsTradeWVP = null;
        }
        if (clone != null) {
            clone.dispose();
            clone = null;
        }
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newPet != null) {
            newPet.dispose();
            newPet = null;
        }
        if (tamkjllpet != null) {
            tamkjllpet.dispose();
            tamkjllpet = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combine != null) {
            combine.dispose();
            combine = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        if (superRank != null) {
            superRank.dispose();
            superRank = null;
        }
        if (dropItem != null) {
            dropItem.dispose();
            dropItem = null;
        }
        if (satellite != null) {
            satellite = null;
        }
        if (achievement != null) {
            achievement.dispose();
            achievement = null;
        }
        if (giftCode != null) {
            giftCode.dispose();
            giftCode = null;
        }
        if (traning != null) {
            traning = null;
        }
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (Cards != null) {
            Cards.clear();
            Cards = null;
        }
        if (itemsWoodChest != null) {
            itemsWoodChest.clear();
            itemsWoodChest = null;
        }
        if (friends != null) {
            friends.clear();
            friends = null;
        }
        if (enemies != null) {
            enemies.clear();
            enemies = null;
        }
        if (temporaryEnemies != null) {
            temporaryEnemies.clear();
            temporaryEnemies = null;
        }
        itemsWoodChest = null;
        Cards = null;
        itemEvent = null;
        maBu2H = null;
        maBuHold = null;
        zoneSieuThanhThuy = null;
        thongBaoTapTuDong = null;
        notify = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        newSkill = null;
        name = null;
        textThongBaoChangeMap = null;
        textThongBaoThua = null;
    }

}
