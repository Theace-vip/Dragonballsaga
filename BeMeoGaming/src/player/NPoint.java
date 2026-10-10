package player;

import models.Card.Card;
import models.Card.OptionCard;
import consts.ConstPlayer;
import consts.ConstRatio;
import intrinsic.Intrinsic;
import item.Item;
import item.Item.ItemOption;
import player.badges.BagesTemplate;
import skill.Skill;
import server.Manager;
import services.EffectSkillService;
import services.ItemService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.TaskService;
import utils.Logger;
import utils.SkillUtil;
import utils.Util;
import java.util.ArrayList;
import java.util.List;
import jdbc.daos.EventDAO;
import lombok.Setter;
import mob.Mob;
import power.PowerLimit;
import power.PowerLimitManager;
import utils.TimeUtil;
import panel.tuning.GameTuning;
import panel.tuning.StatRateTuning;
import panel.tuning.CritTuning;
import panel.tuning.OutputTuning;

public class NPoint {

    public static final byte MAX_LIMIT = 112;
    // Helper doc tuning an toan, loi -> fallback so cu, khong crash game.
    private static double rate(String key, double fb) {
        try { return StatRateTuning.real(key, fb); } catch (Exception e) { return fb; }
    }
    /** % HP/KI/SD/Dame mac dinh theo cap VIP (vi tri = cap VIP; 7-13 hien tai chua co quyen loi, mac dinh 0). */
    private static final double[] VIP_STAT_DEF = {0, 50, 100, 300, 500, 1000, 2000, 0, 0, 0, 0, 0, 0, 0};
    /** % TNSM mac dinh theo cap VIP. */
    private static final double[] VIP_TNSM_DEF = {0, 100, 100, 150, 250, 350, 550, 0, 0, 0, 0, 0, 0, 0};
    private static double critMult() {
        try { return CritTuning.CRIT_MULT; } catch (Exception e) { return 2.0; }
    }
    private static double critCap() {
        try { return CritTuning.CRIT_CAP; } catch (Exception e) { return 110.0; }
    }
    private static double critAdd() {
        try { return CritTuning.CRIT_ADD; } catch (Exception e) { return 0.0; }
    }
    private static double sdcmAdd() {
        try { return CritTuning.SDCM_ADD; } catch (Exception e) { return 0.0; }
    }
    private static double variancePct() {
        try { return CritTuning.VARIANCE_PCT; } catch (Exception e) { return 5.0; }
    }
    private static boolean td70On() {
        try { return CritTuning.THIENDAO70_ON; } catch (Exception e) { return true; }
    }
    private static boolean td100LhOn() {
        try { return CritTuning.THIENDAO100_LH_ON; } catch (Exception e) { return true; }
    }
    @Setter
    private Player player;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }
    public boolean isCrit;
    public boolean isCrit100;
    public boolean isCritTele;
    public boolean TamkjllLucky;
    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public double dameAfter;
    private PowerLimit powerLimit;
    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;
    public byte limitPower;
    public double power;
    public double tiemNang;
    public double hp, hpMax, hpg;
    public double mp, mpMax, mpg;
    public double dame, dameg;
    public double def, defg;
    public int crit, critg;
    public byte speed = 15;
    public boolean teleport;

    public boolean khangTDHS;

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    /**
     * Chỉ số cộng thêm
     */
    public double hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd, dameBoss;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Long> tlDameCrit;
    public int tlSDCM;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Long> tlHp, tlMp;
    public int tlGiamst;
    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Long> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Long> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public double hpHoi, mpHoi, mpHoiCute;
    public double hpHoi1, mpHoi1, mpHoiCute1;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public long tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Long> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    public short tlBom;

    public short tlGiap;

    public short tlxgcc;

    public short tlxgc;

    public short tlchinhxac;

    public short tlTNSMPet;
    public short xChuong;

    public short setltdb;
    public short setTinhAn;
    public short setNhatAn;
    public short setNguyetAn;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public long tlSexyDame;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;
    public boolean islinhthuydanhbac;
    public boolean isTinhAn;
    public boolean isNhatAn;
    public boolean isNguyetAn;
    public boolean isTanHinh;
    public boolean isHoaDa;
    public boolean isLamCham;
    public boolean isDoSPL;
    public boolean isThoBulma;

    public short tlHpGiamODo;

    public boolean isGogeta;

    public int tlSpeed;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            tlHutHp += RewardBlackBall.R3S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R4S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            tlDameCrit.add((long) RewardBlackBall.R5S_1);
            tlSDCM += RewardBlackBall.R5S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlNeDon += RewardBlackBall.R7S_1;
        }
        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIATHOM) {
            tlNeDon += 40;

        }

        Card card = player.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
        if (card != null) {
            for (OptionCard io : card.Options) {
                if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                    switch (io.id) {
                        case 0: // Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: // HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// vô hiệu chưởng
                            this.voHieuChuong += io.param;
                            break;
                        case 5: // +#% sức đánh chí mạng
                            this.tlDameCrit.add((long) io.param);
                            this.tlSDCM += io.param;
                            break;
                        case 6: // HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: // KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: // Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 16: // Speed
                        case 114:
                        case 148:
                            this.tlSpeed += io.param;
                            break;
                        case 18: // Chinh xac
                            this.tlchinhxac += io.param;
                            break;
                        case 19: // Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add((long) io.param);
                            break;
                        case 22: // HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: // MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: // +# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: // +# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: // dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 34:
                            this.setTinhAn += 1;
                            break;
                        case 35:
                            this.setNguyetAn += 1;
                            break;
                        case 36:
                            this.setNhatAn += 1;
                            break;
                        case 47: // Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: // HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: // Tấn công+#%
                        case 50: // Sức đánh +#%
                            try { this.tlDame.add((long) (io.param * rate("opt49_factor", 10) / 100.0)); } catch (Exception e) { this.tlDame.add((long) (io.param * 0.1)); }
                            break;

                        case 239: // Sức đánh+#%
                            this.tlDame.add((long) io.param);
                            break;
                        case 240: // Sức đánh+#%
                            this.tlDame.add((long) io.param);
                            break;
//                        case 42: // Sức đánh+#%
//                            this.tlDame.add((long) io.param);
//                            break;
//                        case 43: // HP+#%
//                            this.tlHp.add((long) io.param);
//                            break;
//                        case 44: // KI +#%
//                            this.tlMp.add((long) io.param);
//                            break;
//                        case 45: // Giáp+#
//                            this.defAdd += io.param;
//                            break;
//                        case 46: // +#% sức đánh chí mạng
//                            this.tlDameCrit.add((long) io.param);
//                            this.tlSDCM += io.param;
//                            break;
//
//                        case 249: // Sức đánh+#%
//                            this.tlDame.add((long) io.param);
//                            break;
//                        case 250: // HP+#%
//                            this.tlHp.add((long) io.param);
//                            break;
//                        case 251: // KI +#%
//                            this.tlMp.add((long) io.param);
//                            break;
//
//                        case 252: // Giáp #%
//                            this.defAdd += io.param;
//                            break;

                        case 77: // HP+#%
                            try { this.tlHp.add((long) (io.param * rate("opt77_factor", 10) / 100.0)); } catch (Exception e) { this.tlHp.add((long) (io.param * 0.1)); } // giam con 30%
                            this.tlHp.add((long) io.param);
                            break;
                        case 80: // HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: // MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: // Cộng #% exp khi đánh quái
                            this.tlTNSM.add((long) io.param);
                            break;
                        case 94: // Giáp #%
                            this.tlGiap += io.param;
                            break;
                        case 95: // Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: // Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: // Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 98: // Xuyen giap chuong
                            this.tlxgc += io.param;
                            break;
                        case 99: // Xuyen giap can chien
                            this.tlxgcc += io.param;
                            break;
                        case 100: // +#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: // +#% TN,SM
                            this.tlTNSM.add((long) io.param);
                            break;
                        case 103: // KI +#%
                            try { this.tlMp.add((long) (io.param * rate("opt103_factor", 10) / 100.0)); } catch (Exception e) { this.tlMp.add((long) (io.param * 0.1)); }; // giảm còn 30%
                            //  this.tlMp.add((long) io.param);
                            break;
                        case 104: // Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: // Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: // Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 108: // #% Né đòn
                            this.tlNeDon += io.param;
                            break;
                        case 109: // Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;
                        case 116: // Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;
                        case 117: // Đẹp +#% SĐ cho mình và người xung quanh
                            if (io.param > this.tlSexyDame) {
                                this.tlSexyDame = io.param;
                            }
                            break;
                        case 147: // +#% sức đánh
                            this.tlDame.add((long) io.param);
                            break;
//                        case 156: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
//                            this.tlSubSD += 50;
//                            this.tlTNSM.add((long) io.param);
//                            this.tlGold += io.param;
//                            break;
                        case 162: // Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: // Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                        case 153: // % phát nổ sau khi chết
                            this.tlBom += io.param;
                            break;
                    }
                }
            }
        }

        // Bông tai cấp 2
        if (this.player.fusion.typeFusion > ConstPlayer.HOP_THE_PORATA) {
            this.player.inventory.itemsBag.stream().filter(it -> it.isNotNullItem()
                    && (it.template.id == 1670
                    || it.template.id == 1669
                    || it.template.id == 1668
                    || it.template.id == 921))
                    .findAny().ifPresent(bt -> {
                        for (ItemOption io : bt.itemOptions) {
                            addOption(io);
                            //    System.err.println("Bt: " + bt.template.id + " Opt: " + io.optionTemplate.id + " Param: " + io.param);
                        }
                    });
        }

        if (BagesTemplate.sendListItemOption(player) != null) {
            for (ItemOption io : BagesTemplate.sendListItemOption(player)) {
                addOption(io);
            }
        }

        this.player.setClothes.worldcup = 0;

        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                switch (item.template.id) {
                    //   case 966:
                    ///    case 982:
                    //     case 983:
                    //   case 883:
                    case 904:
                        player.setClothes.worldcup++;
                        break;
                }

                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    addOption(io);
                }
            }
        }
        // CUNG MENH: cong chi so theo cau hinh admin chinh trong panel (bao cap / % moi cap / option moi bac).
        // Tinh lai moi lan calPoint nen admin sua cau hinh la nguoi choi nhan dung ngay.
        try {
            services.CungMenhService.gI().applyBonus(this.player, this);
        } catch (Exception e) {
        }
        // BAN NGUYEN TINH CAU: % HP/KI/SD theo Cap do Loi + Tier tien hoa
        try {
            services.BanNguyenTinhCauService.gI().applyBonus(this.player, this);
        } catch (Exception e) {
        }
        setDameTrainArmor();
        setBasePoint();
        setOutfitFusion();
        setSpeed();
    }

    /** Cong 1 option tu Cung Menh - dung chung bang option cua vat pham. */
    public void addCungMenhOption(int optionId, long param) {
        try {
            ItemOption io = new ItemOption(optionId, param);
            if (io.optionTemplate != null) {
                addOption(io);
            }
        } catch (Exception e) {
        }
    }

    private void addOption(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: // Tấn công +#
                this.dameAdd += io.param;
                break;
            case 2: // HP, KI+#000
                this.hpAdd += io.param * 1000;
                this.mpAdd += io.param * 1000;
                break;
            case 3:// vô hiệu chưởng
                this.voHieuChuong += io.param;
                break;
            case 5: // +#% sức đánh chí mạng
                this.tlDameCrit.add(io.param);
                this.tlSDCM += io.param;
                break;
//            case 42: // Sức đánh+#%
//                this.tlDame.add((long) io.param);
//                break;
//            case 43: // HP+#%
//                this.tlHp.add((long) io.param);
//                break;
//            case 44: // KI +#%
//                this.tlMp.add((long) io.param);
//                break;
//            case 45: // Giáp+#
//                this.defAdd += io.param;
//                break;
//            case 46: // +#% sức đánh chí mạng
//                this.tlDameCrit.add((long) io.param);
//                this.tlSDCM += io.param;
//                break;

            case 6: // HP+#
                this.hpAdd += io.param;
                break;
            case 7: // KI+#
                this.mpAdd += io.param;
                break;
            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: // Chí mạng+#%
                this.critAdd += io.param;
                break;
            case 16: // Speed
            case 114:
            case 148:
                this.tlSpeed += io.param;
                break;
            case 18: // Chinh xac
                this.tlchinhxac += io.param;
                break;
            case 19: // Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(io.param);
                break;
            case 22: // HP+#K
                this.hpAdd += io.param * 1000;
                break;
            case 23: // MP+#K
                this.mpAdd += io.param * 1000;
                break;
            case 24: // Làm chậm
                this.isLamCham = true;
                break;
            case 25: // Tàn hình
                this.isTanHinh = true;
                break;
            case 26: // Hóa đá
                this.isHoaDa = true;
                break;
            case 27: // +# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: // +# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: // dịch chuyển tức thời
                this.teleport = true;
                break;
            case 34:
                this.setTinhAn += 1;
                break;
            case 35:
                this.setNguyetAn += 1;
                break;
            case 36:
                this.setNhatAn += 1;
                break;
            case 47: // Giáp+#
                this.defAdd += io.param;
                break;
            case 48: // HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 239: // Sức đánh+#%
                this.tlDame.add((long) io.param);
                break;
            case 240: // Sức đánh+#%
                this.tlDame.add((long) io.param);
                break;
            case 49: // Tan cong+#%
            case 50: // Suc danh+#%
                try { this.tlDame.add((long) (io.param * rate("opt49_factor", 10) / 100.0)); } catch (Exception e) { this.tlDame.add((long) (io.param * 0.1)); }
                break;
//
//            case 249: // Sức đánh+#%
//                this.tlDame.add((long) io.param);
//                break;
//            case 250: // HP+#%
//                this.tlHp.add((long) io.param);
//                break;
//            case 251: // KI +#%
//                this.tlMp.add((long) io.param);
//                break;
//
//            case 252: // Giáp #%
//                this.defAdd += io.param;
//                break;
            case 77: // HP+#%
                //     this.tlHp.add(io.param);
                try { this.tlHp.add((long) (io.param * rate("opt77_factor", 10) / 100.0)); } catch (Exception e) { this.tlHp.add((long) (io.param * 0.1)); }
                break;
            case 80: // HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: // MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: // Cộng #% exp khi đánh quái
                this.tlTNSM.add(io.param);
                break;
            case 94: // Giáp #%
                this.tlGiap += io.param;
                break;
            case 95: // Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: // Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: // Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 98: // Xuyen giap chuong
                this.tlxgc += io.param;
                break;
            case 99: // Xuyen giap can chien
                this.tlxgcc += io.param;
                break;
            case 100: // +#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: // +#% TN,SM
                this.tlTNSM.add(io.param);
                break;
            case 103: // KI +#%
                //    this.tlMp.add(io.param);
                try { this.tlMp.add((long) (io.param * rate("opt103_factor", 10) / 100.0)); } catch (Exception e) { this.tlMp.add((long) (io.param * 0.1)); }; // giảm còn 30%
                break;
            case 104: // Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: // Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: // Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 108: // #% Né đòn
                this.tlNeDon += io.param;
                break;
            case 109: // Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 110: // Do spl
                this.isDoSPL = true;
                break;
            case 116: // Kháng thái dương hạ san
                this.khangTDHS = true;
                break;
            case 117: // Đẹp +#% SĐ cho mình và người xung quanh
                if (io.param > this.tlSexyDame) {
                    this.tlSexyDame = io.param;
                }
                break;
            case 147: // +#% sức đánh
                this.tlDame.add(io.param);
                break;
//            case 156: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
//                this.tlSubSD += 50;
//                this.tlTNSM.add(io.param);
//                this.tlGold += io.param;
//                break;
            case 162: // Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 159: // x chưởng
                this.xChuong = (short) io.param;
                break;
            case 160: // TNSM PET;
                this.tlTNSMPet += io.param;
                break;
            case 173: // Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 204:
                this.dameBoss += io.param;
                break;
            case 211:
                this.setltdb += 1;
                break;
            case 153: // % phát nổ sau khi chết
                this.tlBom += io.param;
                break;
        }
    }

    private void setSpeed() {
        if (player.isPl()) {
            speed = (byte) (6 + 6 * (tlSpeed / 100));
        }
    }

    private void setOutfitFusion() {
        if (this.player.inventory.itemsBody.size() < 6 || this.player.pet == null
                || this.player.pet.inventory.itemsBody.size() < 6) {
            return;
        }
        Item skin = this.player.inventory.itemsBody.get(5);
        Item pskin = this.player.pet.inventory.itemsBody.get(5);
        if (skin.isNotNullItem() && pskin.isNotNullItem()) {
            this.isGogeta = skin.template.id == 2133 && pskin.template.id == 2134
                    || skin.template.id == 2134 && pskin.template.id == 2133;
        } else {
            this.isGogeta = false;
        }
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.player.inventory.trainArmor == null) {
                        gtl = this.player.inventory.itemsBag.stream()
                                .filter(item -> item.isNotNullItem() && item.template.type == 32
                                && item.itemOptions != null
                                && item.itemOptions.stream()
                                        .filter(io -> io.optionTemplate.id == 9 && io.param > 0).findFirst()
                                        .orElse(null) != null)
                                .findFirst().orElse(null);
                        if (gtl == null) {
                            return;
                        }
                        this.player.inventory.trainArmor = gtl;
                    }
                    this.wearingTrainArmor = false;
                    for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                        if (io.optionTemplate.id == 9 && io.param > 0) {
                            this.tlDame.add((long) ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n" + e + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setLtdb();
        setThoBulma();
        setTinhNhatNguyetAn();
    }

    private void setLtdb() {
        this.islinhthuydanhbac = this.setltdb >= 5;
    }

    private void setThoBulma() {
        this.isThoBulma = (this.player.inventory != null && this.player.inventory.itemsBody != null
                && this.player.inventory.itemsBody.size() >= 5 && this.player.inventory.itemsBody.get(5).isNotNullItem()
                && this.player.inventory.itemsBody.get(5).template.id == 584);
    }

    private void setTinhNhatNguyetAn() {
        this.isTinhAn = this.setTinhAn >= 5;
        this.isNhatAn = this.setNhatAn >= 5;
        this.isNguyetAn = this.setNguyetAn >= 5;
    }

    private void setHpHoi() {
        this.hpHoi = this.hpMax / 100;
        this.hpHoi += this.hpHoiAdd;

        // Kiểm tra giá trị tlHpHoi không vượt quá giới hạn
        if (this.tlHpHoi > 100) {
            this.tlHpHoi = 100;
        } else if (this.tlHpHoi < 0) {
            this.tlHpHoi = 0;
        }

        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);

        // Kiểm tra giá trị tlHpHoiBanThanVaDongDoi không vượt quá giới hạn
        if (this.tlHpHoiBanThanVaDongDoi > 100) {
            this.tlHpHoiBanThanVaDongDoi = 100;
        } else if (this.tlHpHoiBanThanVaDongDoi < 0) {
            this.tlHpHoiBanThanVaDongDoi = 0;
        }

        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = this.mpMax / 100;
        this.mpHoi += this.mpHoiAdd;

        // Kiểm tra giá trị tlMpHoi không vượt quá giới hạn
        if (this.tlMpHoi > 100) {
            this.tlMpHoi = 100;
        } else if (this.tlMpHoi < 0) {
            this.tlMpHoi = 0;
        }

        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);

        // Kiểm tra giá trị tlMpHoiBanThanVaDongDoi không vượt quá giới hạn
        if (this.tlMpHoiBanThanVaDongDoi > 100) {
            this.tlMpHoiBanThanVaDongDoi = 100;
        } else if (this.tlMpHoiBanThanVaDongDoi < 0) {
            this.tlMpHoiBanThanVaDongDoi = 0;
        }

        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        if (this.player.isPl() && this.player.SagaTuTien[2] >= 1) {
            this.hpMax += this.hpMax * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.SagaTuTien[1]))
                    / 100d;
        }
        if (this.player.isPl() && this.player.SagaTuTien[1] >= 1) {
            this.hpMax += 2_500_000 * this.player.SagaTuTien[1];
        }

        //đồ
        for (Long tl : this.tlHp) {
            if (tl == null) {
                Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi!");
                continue;
            }
            this.hpMax += calPercent(this.hpMax, tl);
        }

        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIASAURIENG) {
            try { this.hpMax += (this.hpMax * rate("lyruou_hp", 150) / 100L); } catch (Exception e) { this.hpMax += (this.hpMax * 150 / 100L); }

        }
        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIASAURIENG) {
            try { this.dame += (this.dame * rate("lyruou_dame", 150) / 100L); } catch (Exception e) { this.dame += (this.dame * 150 / 100L); }

        }
        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIASAURIENG) {
            try { this.mpMax += (this.mpMax * rate("lyruou_ki", 150) / 100L); } catch (Exception e) { this.mpMax += (this.mpMax * 150 / 100L); }

        }
        if (player.setClothes.setvegeta == 5) {
            try { this.hpMax += (this.hpMax * rate("setvegeta", 500) / 100L); } catch (Exception e) { this.hpMax += (this.hpMax * 500L / 100L); }
            //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }

        //hp
        if (player.setClothes.setkichhoat18sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star18", 50) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 50d / 100d); }
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star30", 100) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 100d / 100d); }
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star45", 150) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 150d / 100d); }
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star65", 250) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 250d / 100d); }
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star99", 500) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 500d / 100d); }
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star200", 1200) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 1200d / 100d); }
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star300", 2500) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 2500d / 100d); }
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star500", 5000) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 5000d / 100d); }
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star700", 8000) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 8000d / 100d); }
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            try { this.hpMax += (this.hpMax * rate("star999", 20000) / 100d); } catch (Exception e) { this.hpMax += (this.hpMax * 20000d / 100d); }
        }

        // SET 5 mon (set_config): Chu Tuoc / Moc - chi cong khi du 5/5 mon dau
        services.SetConfigService.applyHp(this, this.player);

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 11
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { hpMax += (this.hpMax * rate("pet11_hp", 100) / 100L); } catch (Exception e) { hpMax += (this.hpMax * 100 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 12
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { hpMax += (this.hpMax * rate("pet12_hp", 50) / 100L); } catch (Exception e) { hpMax += (this.hpMax * 50 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 13
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { hpMax += (this.hpMax * rate("pet13_hp", 70) / 100L); } catch (Exception e) { hpMax += (this.hpMax * 70 / 100L); }// MP black
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 14
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { hpMax += (this.hpMax * rate("pet14_hp", 30) / 100L); } catch (Exception e) { hpMax += (this.hpMax * 30 / 100L); }// MP black
        }

        // Xử lý set nappa
        if (this.player.setClothes.nappa == 5) {
            try { this.hpMax += (this.hpMax * rate("setnappa", 80) / 100L); } catch (Exception e) { this.hpMax += (this.hpMax * 80L / 100L); }
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.hpMax += ((long) this.hpMax * (percent * 10) / 100);
            }
        }

        if (this.player.itemTime != null && this.player.itemTime.isdanbacdau) {
            this.hpMax += 10000000;

        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrung2Trung) {
            this.hpMax += 100000000000D;

        }

        int vipHpLv = player.Saga_VIP;
        if (vipHpLv >= 1 && vipHpLv < VIP_STAT_DEF.length) {
            this.hpMax += (hpMax * rate("vip" + vipHpLv, VIP_STAT_DEF[vipHpLv]) / 100D);
        }

        if (this.player.itemTime != null && this.player.itemTime.isbohuyetsieucap) {
            try { this.hpMax += (hpMax * rate("bohuyet_sc", 200) / 100L); } catch (Exception e) { this.hpMax += (hpMax * 200L / 100L); }

        }

        // Xử lý set worldcup
        // Xử lý set nhật ấn
        if (this.isNhatAn) {
            try { hpMax += (hpMax * rate("nhatAn", 5) / 100L); } catch (Exception e) { hpMax += (hpMax * 5L / 100L); }
        }

        // Xử lý ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            hpMax += (hpMax * RewardBlackBall.R2S_1 / 100L);
        }

        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                if (this.player.isPl() && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_30")) {
                    percent += this.player.SagaThienDao * 3d;
                }
                this.hpMax += calPercent(this.hpMax, percent);
            }
        }
        if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.hpMax += this.hpMax * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            try { this.hpMax += this.hpMax * (this.player.SagaThienDao * rate("thiendo_hp", 1)) / 100d; } catch (Exception e) { this.hpMax += this.hpMax * (this.player.SagaThienDao * 1d) / 100d; }

            try { hpMax += hpMax * (this.player.SagaChuyenSinh * rate("chuyensinh_hp", 2)) / 100d; } catch (Exception e) { hpMax += hpMax * (this.player.SagaChuyenSinh * 2d) / 100d; }
            if (player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_70")) {
                try { hpMax += hpMax * (this.player.TamkjllCapPb * rate("capPb_hp", 20)) / 100d; } catch (Exception e) { hpMax += hpMax * (this.player.TamkjllCapPb * 20d) / 100d; }
            }
            hpMax += 500000 * player.playerTask.taskMain.id;
            hpMax += 5000000 * player.TamkjllDauLaDaiLuc[0];
            hpMax += 10000000 * player.TamkjllDauLaDaiLuc[1];
            hpMax += 20000000 * player.TamkjllDauLaDaiLuc[2];
            hpMax += 40000000 * player.TamkjllDauLaDaiLuc[3];
            hpMax += 60000000 * player.TamkjllDauLaDaiLuc[4];
            hpMax += 80000000 * player.TamkjllDauLaDaiLuc[5];
            hpMax += 100000000 * player.TamkjllDauLaDaiLuc[6];
            if (this.player.TamkjllDauLaDaiLuc[17] == 1) {
                hpMax += player.TamkjllDauLaDaiLuc[18] * 3569d;
            }
            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
                    && (this.player.TamkjllPetGiong == 4 || this.player.TamkjllPetGiong == 9
                    || this.player.TamkjllPetGiong == 10)) {
                hpMax += hpMax * ((this.player.EmBeLv + 1)
                        * panel.tuning.SystemTuning.get("embe_hp_moi_lv")) / 100d;
            }
        }
        if (this.player.isPet && ((Pet) this.player).master.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_500")) {
            hpMax += hpMax * (((Pet) this.player).master.SagaThienDao / 10d) / 100d;
        }

        if (this.player.dakethon > 0) {
            try { this.hpMax += hpMax * (this.player.dakethon * rate("dakethon_hp", 10000) / 100d); } catch (Exception e) { this.hpMax += hpMax * (this.player.dakethon * 10000d / 100d); }
        }
        if (this.player.duockethon > 0) {
            try { this.hpMax += hpMax * (this.player.duockethon * rate("duockethon_hp", 1000) / 100d); } catch (Exception e) { this.hpMax += hpMax * (this.player.duockethon * 1000d / 100d); }
        }
        // Xử lý pet pic
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { hpMax += (hpMax * rate("pet3_hp", 20) / 100L); } catch (Exception e) { hpMax += (hpMax * 20 / 100L); }
        }

        // Xử lý pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { hpMax += (hpMax * rate("pet1_hp", 25) / 100L); } catch (Exception e) { hpMax += (hpMax * 25 / 100L); }
        }

        // Xử lý pet berus
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { hpMax += (hpMax * rate("pet2_hp", 30) / 100L); } catch (Exception e) { hpMax += (hpMax * 30 / 100L); }
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { hpMax += (hpMax * rate("pet4_hp", 40) / 100L); } catch (Exception e) { hpMax += (hpMax * 40 / 100L); }
        }

        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            hpMax *= this.player.effectSkin.xHPKI;
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            this.hpMax += (this.hpMax * 10 / 100L);
        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            this.hpMax += 1_000_000;
        }

        // Xử lý +hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }

        if (this.player.itemTime != null && this.player.itemTime.isItemTest) {
            this.hpMax *= 2;
        }

        // Xử lý bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet && !this.player.itemTime.isUseBoHuyet2) {
            this.hpMax *= 2;
        }

        // Xử lý item sieu cap
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            this.hpMax *= 2.2;
        }

        // Xử lý huýt sáo
        if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += (this.hpMax * this.player.effectSkill.tiLeHPHuytSao / 100L);
            }
        }

        // Xử lý chibi
        if (this.player.effectSkill != null && this.player.effectSkill.isChibi && this.player.typeChibi == 3) {
            this.hpMax *= 2;
        }

        // Xử lý map lạnh
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            this.hpMax /= 2;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseHP())) {
            this.hpMax += this.hpMax / 10;
        }
        // PANEL dot 3: nhan he so HP + soft-cap (admin chinh tren panel, khong can code)
        try {
            if (this.player.isBoss) hpMax = hpMax * GameTuning.HP_GLOBAL * GameTuning.BOSS_HP_SCALE;
            else hpMax = hpMax * GameTuning.HP_GLOBAL;
            hpMax = GameTuning.softCapHp(hpMax);
        } catch (Exception e) {}

        this.hpMax = hpMax;
    }

    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        if (this.player.isPl() && this.player.SagaTuTien[2] >= 1) {
            mpMax += mpMax * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.SagaTuTien[1]))
                    / 100d;
        }
        if (this.player.isPl() && this.player.SagaTuTien[1] >= 1) {
            this.mpMax += 2_500_000 * this.player.SagaTuTien[1];
        }

        if (player.setClothes.setpicolo == 5) {
            try { this.mpMax += (this.mpMax * rate("setpicolo", 50) / 100L); } catch (Exception e) { this.mpMax += (this.mpMax * 50 / 100L); }
            //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }

        // Áp dụng các yếu tố ảnh hưởng đến mpMax
        for (Long tl : this.tlMp) {
            mpMax += (mpMax * tl / 100L);
        }
        if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                mpMax += mpMax * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            mpMax += 500000 * player.playerTask.taskMain.id;
           try { this.mpMax += this.mpMax * (this.player.SagaThienDao * rate("thiendo_ki", 1)) / 100d; } catch (Exception e) { this.mpMax += this.mpMax * (this.player.SagaThienDao * 1d) / 100d; }

            try { mpMax += mpMax * (this.player.SagaChuyenSinh * rate("chuyensinh_ki", 10)) / 100d; } catch (Exception e) { mpMax += mpMax * (this.player.SagaChuyenSinh * 10d) / 100d; }
            if (player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_30")) {
                try { mpMax += mpMax * (this.player.TamkjllCapPb * rate("capPb_ki", 30)) / 100d; } catch (Exception e) { mpMax += mpMax * (this.player.TamkjllCapPb * 30d) / 100d; }
            }
            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
                    && (this.player.TamkjllPetGiong == 4 || this.player.TamkjllPetGiong == 9
                    || this.player.TamkjllPetGiong == 10)) {
                mpMax += mpMax * ((this.player.EmBeLv + 1d)
                        * panel.tuning.SystemTuning.get("embe_hp_moi_lv")) / 100;
            }
        }
        if (this.player.isPet && ((Pet) this.player).master.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_500")) {
            mpMax += mpMax * (((Pet) this.player).master.SagaThienDao / 5d) / 100d;
        }
        int vipMpLv = player.Saga_VIP;
        if (vipMpLv >= 1 && vipMpLv < VIP_STAT_DEF.length) {
            this.mpMax += (mpMax * rate("vip" + vipMpLv, VIP_STAT_DEF[vipMpLv]) / 100D);
        }

        // SET 5 mon (set_config): Thanh Long / Tho - chi cong khi du 5/5 mon dau
        services.SetConfigService.applyKi(this, this.player);

        // Xử lý set picolo
        if (this.player.setClothes.picolo == 5) {
            mpMax *= 2;
        }

        if (this.player.itemTime != null && this.player.itemTime.isbokhisieucap) {
            try { this.mpMax += (mpMax * rate("bokhi_sc", 200) / 100L); } catch (Exception e) { this.mpMax += (mpMax * 200L / 100L); }

        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrung2Trung) {
            this.mpMax += 100000000000D;

        }

        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIASAURIENG) {
            try { this.mpMax += (this.mpMax * rate("lyruou_ki", 150) / 100L); } catch (Exception e) { this.mpMax += (this.mpMax * 150 / 100L); }

        }
        // Xử lý set nguyệt ấn
        if (this.isNguyetAn) {
            try { mpMax += (mpMax * rate("nguyetAn", 5) / 100L); } catch (Exception e) { mpMax += (mpMax * 5L / 100L); }
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
                this.mpMax += ((long) this.mpMax * (percent * 10) / 100);
            }

        }

        //ki
        if (player.setClothes.setkichhoat18sao == 5) {
            try { this.mpMax += (mpMax * rate("star18", 50) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 50d / 100d); }
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            try { this.mpMax += (mpMax * rate("star30", 100) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 100d / 100d); }
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            try { this.mpMax += (mpMax * rate("star45", 150) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 150d / 100d); }
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            try { this.mpMax += (mpMax * rate("star65", 250) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 250d / 100d); }
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            try { this.mpMax += (mpMax * rate("star99", 500) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 500d / 100d); }
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            try { this.mpMax += (mpMax * rate("star200", 1200) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 1200d / 100d); }
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            try { this.mpMax += (mpMax * rate("star300", 2500) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 2500d / 100d); }
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            try { this.mpMax += (mpMax * rate("star500", 5000) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 5000d / 100d); }
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            try { this.mpMax += (mpMax * rate("star700", 8000) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 8000d / 100d); }
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            try { this.mpMax += (mpMax * rate("star999", 20000) / 100d); } catch (Exception e) { this.mpMax += (mpMax * 20000d / 100d); }
        }

        // Xử lý ngọc rồng đen 6 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            mpMax += (mpMax * RewardBlackBall.R6S_1 / 100L);
        }

        // Xử lý set worldcup
        if (this.player.setClothes.worldcup == 2) {
            mpMax += (this.mpMax * 10 / 100L);
        }

        // Xử lý pet pic
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { mpMax += (this.mpMax * rate("pet3_ki", 20) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 20 / 100L); }
        }

        // Xử lý pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { mpMax += (this.mpMax * rate("pet1_ki", 25) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 25 / 100L); }
        }

        // Xử lý pet br
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { mpMax += (this.mpMax * rate("pet2_ki", 30) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 30 / 100L); }// MP berus
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { mpMax += (this.mpMax * rate("pet4_ki", 40) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 40 / 100L); }// MP black
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 11
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { mpMax += (this.mpMax * rate("pet11_ki", 100) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 100 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 12
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { mpMax += (this.mpMax * rate("pet12_ki", 50) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 50 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 13
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { mpMax += (this.mpMax * rate("pet13_ki", 70) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 70 / 100L); }// MP black
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 14
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { mpMax += (this.mpMax * rate("pet14_ki", 300) / 100L); } catch (Exception e) { mpMax += (this.mpMax * 300 / 100L); }// MP black
        }

        if (this.player.dakethon > 0) {
            try { this.mpMax += mpMax * (this.player.dakethon * rate("dakethon_ki", 1000) / 100d); } catch (Exception e) { this.mpMax += mpMax * (this.player.dakethon * 1000d / 100d); }
        }
        if (this.player.duockethon > 0) {
            try { this.mpMax += mpMax * (this.player.duockethon * rate("duockethon_ki", 100) / 100d); } catch (Exception e) { this.mpMax += mpMax * (this.player.duockethon * 100d / 100d); }
        }
        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            mpMax *= this.player.effectSkin.xHPKI;
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            mpMax += (mpMax * 10 / 100L);

        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            mpMax += 1_000_000;

        }

        // Xử lý rồng xương
        if (player.itemTime != null && player.itemTime.isUseRX) {
            mpMax += (mpMax * 10L / 100L);
        }

        // Xử lý hợp thể
        if (this.player.fusion.typeFusion != 0) {
            mpMax += this.player.pet.nPoint.mpMax;
        }

        // Xử lý bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi && !this.player.itemTime.isUseBoKhi2) {
            mpMax *= 2;
        }

        // Xử lý item sieu cap
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            mpMax *= 2.2;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseMP())) {
            mpMax += mpMax / 10;
        }
        try { mpMax = mpMax * GameTuning.MP_GLOBAL; } catch (Exception e) {}

        this.mpMax = mpMax;
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public double getHP() {
        return this.hp <= this.hpMax ? this.hp : this.hpMax;
    }

    public void setHP(double hp) {
        if (hp > 0) {
            this.hp = (hp <= this.hpMax ? hp : this.hpMax);
        } else {
            player.setDie();
        }
    }

    public double getMP() {
        return this.mp <= this.mpMax ? this.mp : this.mpMax;
    }

    public void setMP(double mp) {
        if (mp > 0) {
            this.mp = (mp <= this.mpMax ? mp : this.mpMax);
        } else {
            this.mp = 0;
        }
    }

    public double calPercent(double param, double percent) {
        return param * percent / 100;
    }

    private void setDame() {
        // Tính toán giới hạn dame
        this.dame = this.dameg;
        this.dame += this.dameAdd;

        if (this.player.isPl() && this.player.SagaTuTien[2] >= 1) {
            dame += dame * this.player.TamkjllDametutien(Util.maxInt(this.player.SagaTuTien[1])) / 100d;
        }
        if (this.player.isPl() && this.player.SagaTuTien[1] >= 1) {
            this.dame += 1000000 * this.player.SagaTuTien[1];
        }

        // Áp dụng các yếu tố ảnh hưởng đến dame
        for (Long tl : this.tlDame) {
            this.dame += calPercent(this.dame, tl);
        }

        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIASAURIENG) {
            try { this.dame += (this.dame * rate("lyruou_dame", 150) / 100L); } catch (Exception e) { this.dame += (this.dame * 150 / 100L); }

        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 11
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { dame += (this.dame * rate("pet11_dame", 1000) / 100L); } catch (Exception e) { dame += (this.dame * 1000 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 12
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { dame += (this.dame * rate("pet12_dame", 500) / 100L); } catch (Exception e) { dame += (this.dame * 500 / 100L); }// MP black
        }
        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 13
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { dame += (this.dame * rate("pet13_dame", 700) / 100L); } catch (Exception e) { dame += (this.dame * 700 / 100L); }// MP black
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 14
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_S
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SS
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA_SSS)) {
            try { dame += (this.dame * rate("pet14_dame", 300) / 100L); } catch (Exception e) { dame += (this.dame * 300 / 100L); }// MP black
        }

        if (player.setClothes.setgoku == 5) {
            try { this.dame += (this.dame * rate("setgoku", 80) / 100L); } catch (Exception e) { this.dame += (this.dame * 80l / 100L); }
            //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }
        if (player.clan != null && player.clan.level >= 1) {
            try { this.dame += (this.dame * (player.clan.level * rate("clan_per_lv", 30)) / 100L); } catch (Exception e) { this.dame += (this.dame * (player.clan.level * 30L) / 100L); }
        }

        if (player.setClothes.setsaga == 5) {
            try { this.dame += (this.dame * rate("setsaga", 30) / 100L); } catch (Exception e) { this.dame += (this.dame * 30L / 100L); }
            //   Service.getInstance().sendThongBao(player, "Full Sét Saga +150% Sức Đánh");
        }
        // for (Integer tl : this.tlSDDep) {
        // dame += (dame * tl / 100L);
        // }
        // Xử lý pet pic
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { dame += (dame * rate("pet3_dame", 20) / 100L); } catch (Exception e) { dame += (dame / 5); }
        }
//        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
//            if (!this.player.isPet || (this.player.isPet
//                    && ((Pet) this.player).status != Pet.FUSION)) {
//                int percent = SkillUtil.getPercentDameMonkey((byte) player.isbienhinh);
//                this.dame += ((long) this.dame * (percent * 10) / 100);
//            }
//        }
//        

        // Xử lý pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { dame += (dame * rate("pet1_dame", 25) / 100L); } catch (Exception e) { dame += (dame / 4); }
        }

        // Xử lý pet br
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { dame += (dame * rate("pet2_dame", 30) / 100L); } catch (Exception e) { dame += (dame * 3 / 10L); }
        }

        // Xử lý pet black
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            try { dame += (dame * rate("pet4_dame", 40) / 100L); } catch (Exception e) { dame += (dame * 2 / 5L); }
        }

        // Xử lý set tinh ấn
        if (this.isTinhAn) {
            try { dame += (dame * rate("tinhAn", 5) / 100L); } catch (Exception e) { dame += (dame * 5L / 100L); }
        }

        int vipDameLv = player.Saga_VIP;
        if (vipDameLv >= 1 && vipDameLv < VIP_STAT_DEF.length) {
            this.dame += (dame * rate("vip" + vipDameLv, VIP_STAT_DEF[vipDameLv]) / 100D);
        }

        if (this.player.itemTime != null && this.player.itemTime.isdanbacdau) {
            this.dame += 5000000;

        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhgaquay) {
            try { this.dame += (dame * rate("banhgaquay", 1000) / 100d); } catch (Exception e) { this.dame += (dame * 1000d / 100d); }

        }

        if (this.player.itemTime != null && this.player.itemTime.iscuongnosieucap) {
            try { this.dame += (dame * rate("cuongno_sc", 200) / 100L); } catch (Exception e) { this.dame += (dame * 200L / 100L); }

        }

        //cuồng nộ 2
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            try { this.dame += calPercent(dame, rate("cuongno2", 120)); } catch (Exception e) { this.dame += calPercent(dame, 120); }
        }

        // Xử lý ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            dame += (dame * RewardBlackBall.R1S_2 / 100L);
        }

        // Xử lý set worldcup
        if (this.player.setClothes.worldcup == 2) {
            dame += (dame / 10L);
        }

        // Xử lý gogeta
        if (this.isGogeta) {
            dame += (dame / 10L);
        }

        // Phù map mabu
        if (this.player.isPhuHoMapMabu) {
            dame += 10_000;
        }

        // Xử lý phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            dame *= this.player.effectSkin.xDame;
        }

        // Xử lý hợp thể
        if (this.player.fusion.typeFusion != 0) {
            dame += this.player.pet.nPoint.dame;
        }

        // Xử lý khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                if (this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_70") && this.player.isPl()) {
                    percent += this.player.SagaThienDao;
                }
                dame += (dame * percent / 100L);
            }
        }
        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (player.isbienhinh == 1) {
                dame += (dame * 10 / 100L);
            } else if (player.isbienhinh == 2) {
                dame += (dame * 20 / 100L);
            } else if (player.isbienhinh == 3) {
                dame += (dame * 30 / 100L);
            } else if (player.isbienhinh == 4) {
                dame += (dame * 40 / 100L);
            } else if (player.isbienhinh == 5) {
                dame += (dame * 50 / 100L);
            } else if (player.isbienhinh == 6) {
                dame += (dame * 60 / 100L);
            }
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (player.isbienhinh == 1) {
                hpMax += (hpMax * 10 / 100L);
            } else if (player.isbienhinh == 2) {
                hpMax += (hpMax * 20 / 100L);
            } else if (player.isbienhinh == 3) {
                hpMax += (hpMax * 30 / 100L);
            } else if (player.isbienhinh == 4) {
                hpMax += (hpMax * 40 / 100L);
            } else if (player.isbienhinh == 5) {
                hpMax += (hpMax * 50 / 100L);
            } else if (player.isbienhinh == 6) {
                hpMax += (hpMax * 60 / 100L);
            }
        }

        if (this.player.effectSkill.isTranformation || this.player.effectSkill.isEvolution) {
            if (player.isbienhinh == 1) {
                mpMax += (mpMax * 10 / 100L);
            } else if (player.isbienhinh == 2) {
                mpMax += (mpMax * 20 / 100L);
            } else if (player.isbienhinh == 3) {
                mpMax += (mpMax * 30 / 100L);
            } else if (player.isbienhinh == 4) {
                mpMax += (mpMax * 40 / 100L);
            } else if (player.isbienhinh == 5) {
                mpMax += (mpMax * 50 / 100L);
            } else if (player.isbienhinh == 6) {
                mpMax += (mpMax * 60 / 100L);
            }
        }

        if (player.setClothes.setkichhoat18sao == 5) {
            try { this.dame += (this.dame * rate("star18", 50) / 100d); } catch (Exception e) { this.dame += (this.dame * 50d / 100d); }
        }
        if (player.setClothes.setkichhoat30sao == 5) {
            try { this.dame += (this.dame * rate("star30", 100) / 100d); } catch (Exception e) { this.dame += (this.dame * 100d / 100d); }
        }

        if (player.setClothes.setkichhoat45sao == 5) {
            try { this.dame += (this.dame * rate("star45", 150) / 100d); } catch (Exception e) { this.dame += (this.dame * 150d / 100d); }
        }
        if (player.setClothes.setkichhoat65sao == 5) {
            try { this.dame += (this.dame * rate("star65", 250) / 100d); } catch (Exception e) { this.dame += (this.dame * 250d / 100d); }
        }
        if (player.setClothes.setkichhoat99sao == 5) {
            try { this.dame += (this.dame * rate("star99", 500) / 100d); } catch (Exception e) { this.dame += (this.dame * 500d / 100d); }
        }
        if (player.setClothes.setkichhoat200sao == 5) {
            try { this.dame += (this.dame * rate("star200", 1200) / 100d); } catch (Exception e) { this.dame += (this.dame * 1200d / 100d); }
        }
        if (player.setClothes.setkichhoat300sao == 5) {
            try { this.dame += (this.dame * rate("star300", 2500) / 100d); } catch (Exception e) { this.dame += (this.dame * 2500d / 100d); }
        }
        if (player.setClothes.setkichhoat500sao == 5) {
            try { this.dame += (this.dame * rate("star500", 5000) / 100d); } catch (Exception e) { this.dame += (this.dame * 5000d / 100d); }
        }
        if (player.setClothes.setkichhoat700sao == 5) {
            try { this.dame += (this.dame * rate("star700", 8000) / 100d); } catch (Exception e) { this.dame += (this.dame * 8000d / 100d); }
        }
        if (player.setClothes.setkichhoat999sao == 5) {
            try { this.dame += (this.dame * rate("star999", 20000) / 100d); } catch (Exception e) { this.dame += (this.dame * 20000d / 100d); }
        }
        //??????????????????????????????????????????????????????

        // SET 5 mon (set_config): Bach Ho / Kim - chi cong khi du 5/5 mon dau
        services.SetConfigService.applyDame(this, this.player);

        if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.dame += this.dame * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            dame += this.player.SagaDiaDao * 1000000d;
           try { dame += dame * (this.player.SagaThienDao * rate("thiendo_dame", 1)) / 100d; } catch (Exception e) { dame += dame * (this.player.SagaThienDao * 1d) / 100d; }
            try { dame += this.dame * (this.player.SagaChuyenSinh * rate("chuyensinh_dame", 20)) / 100d; } catch (Exception e) { dame += this.dame * (this.player.SagaChuyenSinh * 20d) / 100d; }
            if (player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_100")) {
                try { dame += dame * (this.player.TamkjllCapPb * rate("capPb_dame", 3)) / 100d; } catch (Exception e) { dame += dame * (this.player.TamkjllCapPb * 3d) / 100d; }
            }
            if (this.player.TamkjllDauLaDaiLuc[15] == 1) {
                this.dame += this.player.TamkjllDauLaDaiLuc[16] * 2369d;
            }
            dame += 200000 * player.playerTask.taskMain.id;
            dame += 5000000 * player.TamkjllDauLaDaiLuc[0];
            dame += 10000000 * player.TamkjllDauLaDaiLuc[1];
            dame += 20000000 * player.TamkjllDauLaDaiLuc[2];
            dame += 40000000 * player.TamkjllDauLaDaiLuc[3];
            dame += 60000000 * player.TamkjllDauLaDaiLuc[4];
            dame += 80000000 * player.TamkjllDauLaDaiLuc[5];
            dame += 100000000 * player.TamkjllDauLaDaiLuc[6];
            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
                    && (this.player.TamkjllPetGiong == 5 || this.player.TamkjllPetGiong == 9
                    || this.player.TamkjllPetGiong == 10)) {
                dame += dame * ((this.player.EmBeLv + 1)
                        * panel.tuning.SystemTuning.get("embe_dame_moi_lv")) / 100L;

            }
        }
        if (this.player.isPet && ((Pet) this.player).master.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_500")) {
            dame += dame * (((Pet) this.player).master.SagaThienDao / 15) / 100d;
        }
        if (this.player.dakethon > 0) {
            try { this.dame += dame * (this.player.dakethon * rate("dakethon_dame", 1000) / 100d); } catch (Exception e) { this.dame += dame * (this.player.dakethon * 1000d / 100d); }
        }
        if (this.player.duockethon > 0) {
            try { this.dame += dame * (this.player.duockethon * rate("duockethon_dame", 100) / 100d); } catch (Exception e) { this.dame += dame * (this.player.duockethon * 100d / 100d); }
        }

        // Sức đánh đẹp
        dame += (dame * tlSexyDame / 100L);

        // Xử lý giảm dame
        dame -= (dame * tlSubSD / 100L);

        // Xử lý map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            dame /= 7;
        }

        if (!this.player.isBoss && !this.player.isNewPet
                && TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreaseDame())) {
            dame += dame / 10;
        }
        if (this.player.effectSkill.isSocola) {
            this.dame -= calPercent(this.dame, 50);
            //  Service.getInstance().sendThongBao(player, "Bạn Bị giảm 20% Sức Đánh\nTrong 30s\nVì Bị Trúng Chiêu sô cô la... ");
        }
        // PANEL dot 3: dame ao leo tu tu - nhan global/boss scale + soft-cap (admin chinh tren panel)
        try {
            if (this.player.isBoss) dame = dame * GameTuning.DAME_GLOBAL * GameTuning.BOSS_DAME_SCALE;
            else dame = dame * GameTuning.DAME_GLOBAL;
            dame = GameTuning.softCapDame(dame);
        } catch (Exception e) {}

        this.dame = dame;
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        if (this.player.isPl() && this.player.SagaTuTien[2] >= 1) {
            this.def += this.def * this.player.TamkjllHpKiGiaptutien(Util.maxInt(this.player.SagaTuTien[1]))
                    / 100d;
        }

        if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.def += this.def * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
            if (this.player.effectSkill.isMonkey && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_100")) {
                this.def += this.def * (this.player.SagaThienDao / 10d) / 100d;
            }
            this.def += this.def * (this.player.SagaChuyenSinh * 2d) / 100d;
            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
                    && (this.player.TamkjllPetGiong == 4 || this.player.TamkjllPetGiong == 9
                    || this.player.TamkjllPetGiong == 10)) {
                this.def = this.def * ((this.player.EmBeLv + 1)
                        * panel.tuning.SystemTuning.get("embe_hp_moi_lv")) / 100;
            }
        }
        if (player.setClothes.setbroly == 5) {
            this.def += (this.def * 25L / 100L);
            //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }
        if (this.player.isPet && ((Pet) this.player).master.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_500")) {
            this.def += this.def * (((Pet) this.player).master.SagaThienDao / 15d) / 100d;
        }
        if (this.player.itemTime != null && this.player.itemTime.isgiapxensieucap) {
            this.def += (def * 50L / 100L);

        }

        // SET 5 mon (set_config): Huyen Vu / Thuy - chi cong khi du 5/5 mon dau
        services.SetConfigService.applyDef(this, this.player);
        // PANEL dot 3: giap toan cuc (admin chinh tren panel)
        try { this.def = this.def * GameTuning.DEF_GLOBAL; } catch (Exception e) {}

    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        // biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }

        if (this.player.itemTime != null && this.player.itemTime.isLYNUOCMIATO) {
            this.crit = 40;

        }

        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungDacBiet) {
            this.crit = 50;
        }
        if (this.player.itemTime != null && this.player.itemTime.isBanhTrungThu) {
            this.tlSDCM += (tlSDCM * 100L / 100L);
        }

        if (player.setClothes.setmabu == 5) {
            this.crit = 25;
            //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
        }
        // SET 5 mon (set_config): Hoa + STCM, cac set + chi mang / + STCM khi du 5/5 mon dau
        services.SetConfigService.applyCrit(this, this.player);
        // PANEL dot 3: crit cong them + tran (admin chinh tren panel)
        try {
            try { this.crit += (int) (GameTuning.CRIT_ADD + critAdd()); } catch (Exception e) { this.crit += (int) GameTuning.CRIT_ADD; }
            try { double _cap = Math.min(GameTuning.CRIT_CAP, critCap()); this.crit = (int) Math.min(this.crit, _cap); } catch (Exception e) { if (this.crit > GameTuning.CRIT_CAP) this.crit = (int) GameTuning.CRIT_CAP; }
            try { this.tlSDCM = (int) (this.tlSDCM * GameTuning.SDCM_GLOBAL + sdcmAdd()); } catch (Exception e) { this.tlSDCM = (int) (this.tlSDCM * GameTuning.SDCM_GLOBAL); }
        } catch (Exception e) {}

    }

    private void resetPoint() {
        this.dameBoss = 0;
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlGiamst = 0;

        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlSDCM = 0;
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlBom = 0;
        this.tlGiap = 0;
        this.tlxgcc = 0;
        this.tlxgc = 0;
        this.tlchinhxac = 0;
        this.tlTNSMPet = 0;
        this.xChuong = 0;
        this.setltdb = 0;
        this.setTinhAn = 0;
        this.setNhatAn = 0;
        this.setNguyetAn = 0;
        this.tlSexyDame = 0;
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.tlSpeed = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
        this.isTanHinh = false;
        this.isHoaDa = false;
        this.isLamCham = false;
        this.isDoSPL = false;
        this.isThoBulma = false;
    }

    public void addHp(double hp) {
        if (hp > 0) {
            double potentialHp = this.hp + hp;
            if (potentialHp > this.hpMax) {
                this.hp = this.hpMax;
            } else {
                this.hp = potentialHp;
            }
        }
    }

    public void addMp(double mp) {
        double potentialMp = this.mp + mp;

        if (potentialMp > this.mpMax) {
            this.mp = this.mpMax;
        } else if (potentialMp < 0) {
            this.mp = 0;
        } else {
            this.mp = potentialMp;
        }
    }

    public void setHp(double hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(double mp) {
        if (mp < 0) {
            this.mp = 0;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25 && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        double dameAttack = dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        double percentDameSkill = 0;
        double percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        if (skillSelect.template.id != Skill.DICH_CHUYEN_TUC_THOI && isCritTele) {
            isCrit = true;
            isCritTele = false;
        }
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame += 100;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame += 100;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.isPl() && this.player.SagaThienDao >= 1000) {
                    percentXDame += this.player.SagaThienDao * 0d;
                }
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame += 80;
                }
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }

                percentDameSkill = skillSelect.damage;
                if (this.player.isPl() && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_30")) {
                    percentXDame += this.player.SagaThienDao;
                }
                if (this.player.setClothes.thienXinHang == 5) {
                    percentXDame += 80;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                isCrit = true;
                isCritTele = true;
                dameAttack = Util.nextDouble(dameAttack - (dameAttack / 100 * 5), dameAttack + (dameAttack / 100 * 5));
                if (this.player.isPl() && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_30")) {
                    percentXDame += this.player.SagaThienDao * 1d; // Chuyển sang double là cóp mấy này qua th hả
                }
                break;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                double dameSkill = this.mpMax / 100 * percentDameSkill;
                dameSkill += dameSkill * services.SetConfigService.skillPct(this.player, skillSelect.template.id) / 100d;
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long hpmob = 0;
                long hppl = 0;

                for (Mob mob : this.player.zone.mobs) {
                    if (!mob.isDie() && Util.getDistance(this.player, mob) <= SkillUtil
                            .getRangeQCKK(this.player.playerSkill.skillSelect.point)) {
                        hpmob += mob.point.hp;
                    }
                }

                for (Player pl : this.player.zone.getHumanoids()) {
                    if (!pl.isDie() && this.player.id != pl.id && Util.getDistance(this.player, pl) <= SkillUtil
                            .getRangeQCKK(this.player.playerSkill.skillSelect.point)) {
                        hppl += pl.nPoint.hp;
                    }
                }

                double dameqckk = (hpmob / 10) + (hppl / 10) + this.dame * 10;

                if (this.player.setClothes.kirin == 5) {
                    dameqckk += 100;
                }
                dameqckk += dameqckk * services.SetConfigService.skillPct(this.player, skillSelect.template.id) / 100d;

                dameqckk = dameqckk + (Util.nextLong(-5, 5) * dameqckk / 100);

                return dameqckk;
            case Skill.DE_TRUNG:
                if (player.setClothes.pikkoroDaimao == 5) {
                    dameAttack += 100;
                }
                dameAttack += dameAttack * services.SetConfigService.skillPct(this.player, skillSelect.template.id) / 100d;
                return dameAttack;
        }

        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }

        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }

        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);
        if (this.player.effectSkill != null && this.player.effectSkill.isDameBuff && tlSexyDame == 0) {
            double tiLeDame = this.player.effectSkill.tileDameBuff;
            dameAttack += (dameAttack * tiLeDame / 100L);
        }
        if (isAttackMob) {
            for (double tl : this.tlDameAttMob) {
                dameAttack += (dameAttack / 100 * tl);
            }
            if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                dameAttack *= 2;
            }
        }

        dameAfter = 0;

        if (isCrit) {
            try { dameAttack *= critMult(); } catch (Exception e) { dameAttack *= 2; }
            try { dameAttack += (dameAttack * (tlSDCM + sdcmAdd()) / 100); } catch (Exception e) { dameAttack += (dameAttack * tlSDCM / 100); }
            if (td70On() && this.player.isPl() && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_70")) {
                dameAttack += dameAttack * this.player.SagaThienDao / 100d;
            }
            if (td100LhOn() && this.player.isPl() && player.playerSkill.skillSelect.skillId == Skill.LIEN_HOAN
                    && this.player.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_100")) {
                dameAttack += dameAttack * this.player.SagaThienDao / 100d;
            }
            if (this.player.isPet && ((Pet) this.player).master.SagaThienDao >= panel.tuning.SystemTuning.getI("thiendao_moc_500")) {
                dameAttack += dameAttack * (((Pet) this.player).master.SagaThienDao / 20d) / 100d;
            }
            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
                    && (this.player.TamkjllPetGiong == 6 || this.player.TamkjllPetGiong == 9
                    || this.player.TamkjllPetGiong == 10)) {
                dameAttack += dameAttack * ((this.player.EmBeLv + 1d)
                        * panel.tuning.SystemTuning.get("embe_cm_moi_lv")) / 100d;
            }
        }

        percentXDame += services.SetConfigService.skillPct(this.player, skillSelect.template.id);
        dameAttack += (dameAttack * percentXDame / 100);

        double tempDameAttack; try { tempDameAttack = (dameAttack * variancePct() / 100); } catch (Exception e) { tempDameAttack = (dameAttack / 20L); }
        if (tempDameAttack <= 0) {
            tempDameAttack = 1;
        }
        dameAttack += (Util.getOne(-1, 1) * (tempDameAttack) + 1);

        if (player.effectSkin != null && player.effectSkin.isXChuong
                && (player.playerSkill.skillSelect.template.id == Skill.KAMEJOKO
                || player.playerSkill.skillSelect.template.id == Skill.ANTOMIC
                || player.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
            dameAttack *= xChuong;
            player.effectSkin.isXDame = true;
            player.effectSkin.isXChuong = false;
            player.effectSkin.lastTimeXChuong = System.currentTimeMillis();
        }

        // BAN NGUYEN TINH CAU: Xayda Tier 5 - noi tai Zenkai (HP cang thap sat thuong cang cao)
        dameAttack = services.BanNguyenTinhCauService.gI().applyZenkai(player, dameAttack);

        return dameAttack;
    }

    public double getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100; // Trả về 100% nếu hpMax bằng 0
        }
        return (double) this.hp * 100 / this.hpMax; // Chuyển đổi sang double để tính toán chính xác
    }

    public double getCurrPercentMP() {
        // Trả về kết quả dưới dạng double để tránh mất thông tin
        return (double) this.mp * 100 / this.mpMax;
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp <= 0) {
            this.hp = 0;
            this.setHp(0);
        }
    }

    public void subMP(double sub) {
        this.mp -= sub;
        if (this.mp <= 0) {
            this.mp = 0;
        }
    }

    public double calSucManhTiemNang(double tiemNang) {
        if (player.zone.map.type == 3) {
            return 0;
        }
        if (power < getPowerLimit()) {
            for (double tl : this.tlTNSM) {
                tiemNang += ((double) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((double) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((double) tiemNang * 5 / 100);
                }
            }
            double tn = tiemNang;
            if (this.player.Saga_VIP != 0) {
                int vipTnLv = this.player.Saga_VIP;
                if (vipTnLv >= 1 && vipTnLv < VIP_TNSM_DEF.length) {
                    tiemNang += ((double) tiemNang * rate("vipTnsm" + vipTnLv, VIP_TNSM_DEF[vipTnLv]) / 100);
                }
            }
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.effectSkill.isChibi && this.player.typeChibi == 2) {
                tiemNang += tn * 2;
            }
            if (this.player.itemTime != null && this.player.itemTime.isBINHX3) {
                tiemNang += tn * 1;
            }
            if (this.player.itemTime != null && this.player.itemTime.isBINHX5) {
                tiemNang += tn * 2;
            }
            if (this.player.itemTime != null && this.player.itemTime.isBINHX7) {
                tiemNang += tn * 3;

            }
            if (this.player.itemTime != null && this.player.itemTime.isBINHX10) {
                tiemNang += tn * 4;

            }
            if (this.player.itemTime != null && this.player.itemTime.isBanhthapcam) {
                tiemNang += ((double) tiemNang * 1000 / 100);

            }

            if (player.setClothes.setphuho == 5) {
                tiemNang += ((double) tiemNang * 888 / 100);
                //    Service.getInstance().sendThongBao(player, "Đã tăng 100% sức đánh");
            }
           

            if (player.Saga_VIP > 0) {
                tiemNang += ((long) tiemNang * rate("vipTnsmAll", 1) / 100);
            }

            if (this.player.getSession() != null && this.player.getSession().vip > 0
                    || this.player.isPet && ((Pet) this.player).master.getSession() != null
                    && ((Pet) this.player).master.getSession().vip > 0) {
                tiemNang += tn * 3;
            }
//            if (this.player.itemTime != null && this.player.itemTime.isUseDK) {
//                tiemNang += tn * 2;
//            }
            if (this.player.satellite != null && this.player.satellite.isIntelligent) {
                tiemNang += tn / 5;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((double) tiemNang * this.intrinsic.param1 / 100);
            }
            if (this.power >= 60000000000L) {
                tiemNang -= ((long) tiemNang * 80 / 100);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
                if (((Pet) this.player).master.nPoint != null && ((Pet) this.player).master.nPoint.tlTNSMPet > 0) {
                    tiemNang += tn / 100 * (((Pet) this.player).master.nPoint.tlTNSMPet + 100);
                }
            }
            if (TimeUtil.checkTime(EventDAO.getRemainingTimeToIncreasePotentialAndPower())) {
                tiemNang *= 2;
            }
            if (MapService.gI().isMapNguHanhSon(this.player.zone.map.mapId)) {
                tiemNang *= 2;
            }
            if (MapService.gI().isMapPhongTap(this.player.zone.map.mapId)) {
                tiemNang *= 2;
            }
            if (MapService.gI().isMapThaoNguyen(this.player.zone.map.mapId)) {
                tiemNang *= 2;
            }

            if (MapService.gI().isMapBanDoKhoBau(this.player.zone.map.mapId)) {
                tiemNang *= 3;
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 0;
        }
        return tiemNang;
    }

    public double calSubTNSM(double tiemNang) {
        if (power >= 1) {
            tiemNang /= 3;
        } else if (power == Double.MAX_VALUE) {
            tiemNang = 0;

        }
        return tiemNang;
    }

    public long getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public long getTiLeHutMp() {
        return this.tlHutMp;
    }

    public double subDameInjureWithDeff(double dame) {
        double def = this.def;
        dame -= def;
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public double getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public double getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    // **************************************************************************
    // POWER - TIEM NANG
    public void powerUp(double power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(double tiemNang) {
        this.tiemNang += tiemNang;
    }

    // dedupe thong bao loi cong diem: khi acs loi lien tuc se spam toast -> client phai xu ly tin nhan qua nhieu -> crash may yeu
    private boolean canThongBaoTiemNang() {
        if (player == null || player.iDMark == null) {
            return true;
        }
        long now = System.currentTimeMillis();
        if (now - player.iDMark.getLastTimeThongBaoTiemNang() < 1500L) {
            return false;
        }
        player.iDMark.setLastTimeThongBaoTiemNang(now);
        return true;
    }

    private boolean doUseTiemNang(double tiemNang) {
        if (this.tiemNang < tiemNang) {
            if (canThongBaoTiemNang()) {
                Service.gI().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            }
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    public void increasePoint(byte type, double point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        if ((type == 0 || type == 1) && point < 20) {
            if (canThongBaoTiemNang()) {
                Service.gI().sendThongBao(player, "Giá trị phải lớn hơn 20");
            }
            return;
        }
        boolean updatePoint = false;
        double tiemNangUse = 0;
        if (type == 0) {
            tiemNangUse = point / 20 * (2 * (hpg + 1000) + (point - 20)) / 2;
            if ((this.hpg + point) <= powerLimit.getHp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "HP của bạn đã đạt mức tối đa");
                    Service.gI().sendMoney(player);
                }
                return;
            }
        }
        if (type == 1) {
            tiemNangUse = point / 20 * (2 * (mpg + 1000) + (point - 20)) / 2;
            if ((this.mpg + point) <= powerLimit.getMp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "KI của bạn đã đạt mức tối đa");
                    Service.gI().sendMoney(player);
                }
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * dameg + 99) / 2 * 100;
            if ((this.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Sức đánh của bạn đã đạt mức tối đa");
                    Service.gI().sendMoney(player);
                }
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Giáp của bạn đã đạt mức tối đa");
                    Service.gI().sendMoney(player);
                }
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Chí mạng của bạn đã đạt mức tối đa");
                    Service.gI().sendMoney(player);
                }
                return;
            }
        }
        if (updatePoint) {
            Service.gI().point(player);
        }
    }

    public void increasePointPet(byte type, long point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        if ((type == 0 || type == 1) && point < 20) {
            if (canThongBaoTiemNang()) {
                Service.gI().sendThongBao(player, "Giá trị phải lớn hơn 20");
            }
            return;
        }
        boolean updatePoint = false;
        double tiemNangUse = 0;
        if (type == 0) {
            tiemNangUse = point / 20 * (2 * (player.pet.nPoint.hpg + 1000) + (point - 20)) / 2;
            if ((player.pet.nPoint.hpg + point) <= powerLimit.getHp()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.hpg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "HP của đệ đã đạt mức tối đa");
                }
                return;
            }
        }
        if (type == 1) {
            tiemNangUse = point / 20 * (2 * (player.pet.nPoint.mpg + 1000) + (point - 20)) / 2;
            if ((player.pet.nPoint.mpg + point) <= powerLimit.getMp()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.mpg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "KI của đệ đã đạt mức tối đa");
                }
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * player.pet.nPoint.dameg + 99) / 2 * 100;
            if ((player.pet.nPoint.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.dameg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Sức đánh của đệ đã đạt mức tối đa");
                }
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (player.pet.nPoint.defg + 5) / 2 * 100000;
            if ((player.pet.nPoint.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.defg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Giáp của đệ đã đạt mức tối đa");
                }
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < player.pet.nPoint.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((player.pet.nPoint.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.critg += point;
                    updatePoint = true;
                }
            } else {
                if (canThongBaoTiemNang()) {
                    Service.gI().sendThongBao(player, "Chí mạng của đệ đã đạt mức tối đa");
                }
                return;
            }
        }
        if (updatePoint) {
            Service.gI().showInfoPet(player);
        }
    }

    // public void increasePoint(byte type, short point) {
    // if (point <= 0 || point > 100) {
    // return;
    // }
    // long tiemNangUse;
    // if (type == 0) {
    // int pointHp = point * 20;
    // tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
    // if ((this.hpg + pointHp) <= getHpMpLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // hpg += pointHp;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 1) {
    // int pointMp = point * 20;
    // tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
    // if ((this.mpg + pointMp) <= getHpMpLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // mpg += pointMp;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 2) {
    // TaskService.gI().checkDoneTaskNangCS(player);
    // tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
    // if ((this.dameg + point) <= getDameLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // dameg += point;
    // }
    // TaskService.gI().checkDoneTaskNangCS(player);
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 3) {
    // tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
    // if ((this.defg + point) <= getDefLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // defg += point;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // if (type == 4) {
    // tiemNangUse = 50000000L;
    // for (int i = 0; i < this.critg; i++) {
    // tiemNangUse *= 5L;
    // }
    // if ((this.critg + point) <= getCritLimit()) {
    // if (doUseTiemNang(tiemNangUse)) {
    // critg += point;
    // }
    // } else {
    // Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
    // return;
    // }
    // }
    // Service.gI().point(player);
    // }
    private boolean doUseTiemNangPet(double tiemNang) {
        if (player.pet.nPoint.tiemNang < tiemNang) {
            if (canThongBaoTiemNang()) {
                Service.gI().sendThongBaoOK(player, "Đệ không đủ tiềm năng");
            }
            return false;
        }
        if (player.pet.nPoint.tiemNang >= tiemNang && player.pet.nPoint.tiemNang - tiemNang >= 0) {
            player.pet.nPoint.tiemNang -= tiemNang;
            return true;
        }
        return false;
    }

    public double getFullTN() {
        double tnhp = 0, tnki = 0, tnsd = 0, tng = 0, tncm = 0;

        if (hpg > 0) {
            tnhp = (((hpg / 20L) * (50L + (50L + (hpg / 20L) - 1L)) / 2L) * 20L);
        }
        if (mpg > 0) {
            tnki = (((mpg / 20L) * (50L + (50L + (mpg / 20L) - 1L)) / 2L) * 20L);
        }
        if (dameg > 0) {
            tnsd = ((dameg * (dameg - 1L) * 100L) / 2L);
        }
        if (defg > 0) {
            tng = ((defg * (500000L + (500000L + (defg - 1L) * 100000L))) / 2L);
        }
        if (critg > 0) {
            tncm = ((50L * (((long) Math.pow(5L, critg) - 1L)) / (5L - 1L) * 1000000L));
        }
        return tnhp + tnki + tnsd + tng + tncm;
    }

    // --------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiPhuc1;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    double hpRecovered = hpMax / 100 * tiLeHoiPhuc;
                    double mpRecovered = mpMax / 100 * tiLeHoiPhuc;

                    PlayerService.gI().hoiPhuc(player, hpRecovered, mpRecovered);

                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.gI().chat(player, "Phục hồi năng lượng " + Util.FormatNumber(getCurrPercentHP()) + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
//            if (this.player.effectSkill.isThoiMien) {
//                // Kiểm tra xem người chơi có đang chiến đấu với boss không
//                if (!player.isBoss) { // Nếu không phải boss
//                    this.hpMax -= calPercent(this.hpMax, 30);
//                    this.mpMax -= calPercent(this.mpMax, 30);
//                    Service.getInstance().sendThongBao(player, "Bạn Bị Thôi Miên\nTrong 30s\ngiảm hp ki 30% ");
//                } else {
//                    Service.getInstance().sendThongBao(player, "Bạn đang chiến đấu với boss, không bị ảnh hưởng bởi thôi miên.");
//                }
//            }
//            if (this.player.effectSkill.isStun) {
//                // Kiểm tra xem người chơi có đang chiến đấu với boss không
//                if (!player.isBoss) { // Nếu không phải boss
//                    this.def -= calPercent(this.def, 60);
//                    Service.getInstance().sendThongBao(player, "Bạn Bị TDHS\nTrong 30s\ngiảm giáp 60% ");
//                } else {
//                    Service.getInstance().sendThongBao(player, "Bạn đang chiến đấu với boss, không bị ảnh hưởng bởi thôi miên.");
//                }
//            }

//            if (this.player.effectSkill.useTroi) {
//                this.hpMax += calPercent(this.hpMax, 100);
//                Service.getInstance().sendThongBao(player, "Bạn Trói\nTrong 30s\nTăng 1000% hp.Và Giap ");
//            }
//
//            if (this.player.effectSkill.anTroi) {
//                this.hpHoi1 += calPercent(hp, 1);
//                Service.getInstance().sendThongBao(player, "Bạn Bị Trói\nTrong 30s\nHồi Liên Tục HP 1%/s ");
//            }
            if (Util.canDoWithTime(lastTimeHoiPhuc1, 1000)) {
                PlayerService.gI().hoiPhuc1(this.player, hpHoi1, mpHoi1);
                this.lastTimeHoiPhuc1 = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 60000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }

            if (Util.canDoWithTime(lastTimeHoiStamina, 30000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();

                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlTNSM = null;
    }
}
