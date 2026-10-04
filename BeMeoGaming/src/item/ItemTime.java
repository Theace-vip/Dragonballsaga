package item;

import player.NPoint;
import player.Player;
import services.Service;
import utils.Util;
import services.ItemTimeService;

public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte CON_DUONG_RAN_DOC = 2;
    public static final byte KHI_GAS_HUY_DIET = 3;
    public static final byte TIME_KEO_BUA_BAO = 4;
    public static final byte TEXT_NHAN_BUA_MIEN_PHI = 5;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_150p = 9000000;
    public static final int TIME_OPEN_POWER = 8640000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_CMS = 3600000;
    public static final int TIME_DK = 1800000;
    public static final int TIME_RK = 3600000;
    public static final int TIME_1H = 3600000;

    public static final int TIME_NCD = 1800000;
    public static final int TIME_GIANGHOA = 30000;
    public static final int TIME_TD = 1800000;

    private Player player;

    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public boolean isUseAnDanh2;
    public boolean isdanbacdau;

    //List Máy dò TNSM
    public boolean isBINHX3;
    public boolean isBINHX5;
    public boolean isBINHX7;
    public boolean isBINHX10;

    // [The Diem Farm 27/09/2026] the x2/x3/x5 diem farm - item 1916/1917/1918
    public boolean isFamX2;
    public boolean isFamX3;
    public boolean isFamX5;
    public long lastTimeFamX2;
    public long lastTimeFamX3;
    public long lastTimeFamX5;

    //List Máy dò TNSM
    public boolean isBanhTrungDacBiet;
    public boolean isBanhTrungThu;
    public boolean isBanhTrung1Trung;
    public boolean isBanhTrung2Trung;
    public boolean isBanhgaquay;
    public boolean isBanhthapcam;

    //
    public boolean isLYNUOCMIATO;
    public boolean isLYNUOCMIATHOM;
    public boolean isLYNUOCMIASAURIENG;
    public boolean isMAYDOSKH;
    public boolean isMAYDOSKHVIP;
    // cuongno sieucap
    public boolean iscuongnosieucap;
    public boolean isbohuyetsieucap;
    public boolean isbokhisieucap;
    public boolean isgiapxensieucap;

    //LAS-TIME TNSM
    public long lastTimeBINHX3;
    public long lastTimeBINHX5;
    public long lastTimeBINHX7;
    public long lastTimeBINHX10;

    //LAS-TIME TNSM
    public long lastTimeBanhDacBiet;
    public long lastTimeBanhTrungthu;
    public long lastTimeBanh1Trung;
    public long lastTimeBanh2Trung;
    public long lastTimegaquay;
    public long lastTimethapcam;

    //LAS-TIME cnsc
    public long lastTimecnsc;
    public long lastTimebhsc;
    public long lastTimebksc;
    public long lastTimegxsc;
    //
    public long lastTimeLYNUOCMIATO;
    public long lastTimeLYNUOCMIATHOM;
    public long lastLYNUOCMIASAURIENG;
    public long lastTimeMaydoskh;
    public long lastTimeMaydoskhvip;

    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    public long tgianbacdau;

    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;
    public long lastTimeAnDanh2;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;
    public boolean isUseMayDo2;
    public long lastTimeUseMayDo2;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isUseRX;
    public long lastTimeUseRX;
    public int timeRX;

    public boolean isUseCMS;
    public long lastTimeUseCMS;

    public boolean isUseNCD;
    public long lastTimeUseNCD;

    public boolean isUseGTPT;
    public long lastTimeUseGTPT;

    public boolean isUseDK;
    public long lastTimeUseDK;

    // Buff Duoi khic (579/1045): +ty le roi vat pham su kien Trung Thu (mac dinh 15 phut)
    public boolean isDuoiKhiTT;
    public long lastTimeDuoiKhiTT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isEatMeal2;
    public long lastTimeEatMeal2;
    public int iconMeal2;

    public boolean isItemTest;
    public long lastTimeItemTest;

    //star item time tamkjll 
    public boolean isXexpTamkjll1_5;
    public boolean isXexpTamkjll2;
    public boolean isXexpTamkjll3;
    public boolean isXexpTamkjll4;
    public boolean isXexpTamkjll5;
    public boolean isXexpTamkjll6;

    public long lastTimeexpTamkjll1_5;
    public long lastTimeexpTamkjll2;
    public long lastTimeexpTamkjll3;
    public long lastTimeexpTamkjll4;
    public long lastTimeexpTamkjll5;
    public long lastTimeexpTamkjll6;

    public boolean isUpTbx2;
    public boolean isUpTbx3;
    public boolean isUpTbx4;
    public boolean isUpTbx5;
    public boolean isUpTbx6;
    public boolean isUpTbx7;

    public long thoigianUpx2;
    public long thoigianUpx3;
    public long thoigianUpx4;
    public long thoigianUpx5;
    public long thoigianUpx6;
    public long thoigianUpx7;

    public ItemTime(Player player) {
        this.player = player;
    }

    /**
     * Thoi luong buff cua the diem farm (ms). Doc tren panel - tab He Thong, nhom
     * "Diem Farm", phan "The diem farm" (key farm_card_thoi_gian, don vi giay).
     */
    public static long thoiGianTheFamMs() {
        try {
            int giay = panel.tuning.SystemTuning.getI("farm_card_thoi_gian");
            if (giay <= 0) {
                giay = 3600;
            }
            return giay * 1000L;
        } catch (Throwable e) {
            return 3600000L;
        }
    }

    /**
     * He so diem farm hien tai - CONG DON cac the dang bat:
     * chi 1 the thi lay dung he so the; tu 2 the tro len thi tong he so - 1
     * (vd: X2 + X3 -> 2 + 3 - 1 = x4, X2 + X3 + X5 -> 9); khong bat the nao -> 1.
     */
    public int heSoDiemFarm() {
        int tong = 0;
        int soThe = 0;
        if (isFamX5) {
            tong += 5;
            soThe++;
        }
        if (isFamX3) {
            tong += 3;
            soThe++;
        }
        if (isFamX2) {
            tong += 2;
            soThe++;
        }
        if (soThe == 0) {
            return 1;
        }
        if (soThe == 1) {
            return tong;
        }
        return tong - 1;
    }

    public void update() {
        if (isItemTest) {
            if (Util.canDoWithTime(lastTimeItemTest, TIME_1H)) {
                isItemTest = false;
                Service.gI().point(player);
            }
        }
        //List TNSM
        if (isBINHX3) {
            if (Util.canDoWithTime(lastTimeBINHX3, TIME_1H)) {
                isBINHX3 = false;
                Service.gI().point(player);
            }
        }

        if (isBINHX5) {
            if (Util.canDoWithTime(lastTimeBINHX5, TIME_1H)) {
                isBINHX5 = false;
                Service.gI().point(player);
            }
        }

        if (isBINHX7) {
            if (Util.canDoWithTime(lastTimeBINHX7, TIME_1H)) {
                isBINHX7 = false;
                Service.gI().point(player);
            }
        }
        if (isBINHX10) {
            if (Util.canDoWithTime(lastTimeBINHX10, TIME_1H)) {
                isBINHX10 = false;
                Service.gI().point(player);
            }
        }

        // [The Diem Farm] het gio tu tat
        long thoiGianThe = thoiGianTheFamMs();
        if (isFamX2 && Util.canDoWithTime(lastTimeFamX2, thoiGianThe)) {
            isFamX2 = false;
            Service.gI().point(player);
        }
        if (isFamX3 && Util.canDoWithTime(lastTimeFamX3, thoiGianThe)) {
            isFamX3 = false;
            Service.gI().point(player);
        }
        if (isFamX5 && Util.canDoWithTime(lastTimeFamX5, thoiGianThe)) {
            isFamX5 = false;
            Service.gI().point(player);
        }

//
        if (isLYNUOCMIATO) {
            if (Util.canDoWithTime(lastTimeLYNUOCMIATO, TIME_ITEM)) {
                isLYNUOCMIATO = false;
                Service.gI().point(player);
            }
        }

        if (isLYNUOCMIATHOM) {
            if (Util.canDoWithTime(lastTimeLYNUOCMIATHOM, TIME_ITEM)) {
                isLYNUOCMIATHOM = false;
                Service.gI().point(player);
            }
        }

        if (isLYNUOCMIASAURIENG) {
            if (Util.canDoWithTime(lastLYNUOCMIASAURIENG, TIME_ITEM)) {
                isLYNUOCMIASAURIENG = false;
                Service.gI().point(player);
            }
        }
        if (isBanhTrungDacBiet) {
            if (Util.canDoWithTime(lastTimeBanhDacBiet, TIME_150p)) {
                isBanhTrungDacBiet = false;
                Service.gI().point(player);
            }
        }
        if (isBanhTrungThu) {
            if (Util.canDoWithTime(lastTimeBanhTrungthu, TIME_150p)) {
                isBanhTrungThu = false;
                Service.gI().point(player);
            }
        }
        if (isBanhTrung1Trung) {
            if (Util.canDoWithTime(lastTimeBanh1Trung, TIME_ITEM)) {
                isBanhTrung1Trung = false;
                Service.gI().point(player);
            }
        }
        if (isBanhTrung2Trung) {
            if (Util.canDoWithTime(lastTimeBanh2Trung, TIME_ITEM)) {
                isBanhTrung2Trung = false;
                Service.gI().point(player);
            }
        }
         if (isBanhgaquay) {
            if (Util.canDoWithTime(lastTimegaquay, TIME_ITEM)) {
                isBanhgaquay = false;
                Service.gI().point(player);
            }
        }
        if (isBanhthapcam) {
            if (Util.canDoWithTime(lastTimethapcam, TIME_ITEM)) {
                isBanhthapcam = false;
                Service.gI().point(player);
            }
        }
        
        
        

        if (isMAYDOSKH) {
            if (Util.canDoWithTime(lastTimeMaydoskh, TIME_1H)) {
                isMAYDOSKH = false;
                Service.gI().point(player);
            }
        }

        if (isMAYDOSKHVIP) {
            if (Util.canDoWithTime(lastTimeMaydoskhvip, TIME_1H)) {
                isMAYDOSKHVIP = false;
                Service.gI().point(player);
            }
        }

        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (isEatMeal2) {
            if (Util.canDoWithTime(lastTimeEatMeal2, TIME_EAT_MEAL)) {
                isEatMeal2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
//                Service.gI().Send_Info_NV(this.player);
            }
        }

        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }

        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }

        //
        if (isUpTbx2) {
            if (Util.canDoWithTime(thoigianUpx2, TIME_ITEM)) {
                isUpTbx2 = false;
                Service.gI().point(player);
            }
        }
        if (isUpTbx3) {
            if (Util.canDoWithTime(thoigianUpx3, TIME_ITEM)) {
                isUpTbx3 = false;
                Service.gI().point(player);
            }
        }
        if (isUpTbx4) {
            if (Util.canDoWithTime(thoigianUpx4, TIME_ITEM)) {
                isUpTbx4 = false;
                Service.gI().point(player);
            }
        }
        if (isUpTbx5) {
            if (Util.canDoWithTime(thoigianUpx5, TIME_ITEM)) {
                isUpTbx5 = false;
                Service.gI().point(player);
            }
        }
        if (isUpTbx6) {
            if (Util.canDoWithTime(thoigianUpx6, TIME_ITEM)) {
                isUpTbx6 = false;
                Service.gI().point(player);
            }
        }
        if (isUpTbx7) {
            if (Util.canDoWithTime(thoigianUpx7, TIME_ITEM)) {
                isUpTbx7 = false;
                Service.gI().point(player);
            }
        }

        //
        if (iscuongnosieucap) {
            if (Util.canDoWithTime(lastTimecnsc, TIME_ITEM)) {
                iscuongnosieucap = false;
                Service.gI().point(player);
            }
        }

        if (isbohuyetsieucap) {
            if (Util.canDoWithTime(lastTimebhsc, TIME_ITEM)) {
                isbohuyetsieucap = false;
                Service.gI().point(player);
            }
        }

        if (isbokhisieucap) {
            if (Util.canDoWithTime(lastTimebksc, TIME_ITEM)) {
                isbokhisieucap = false;
            }
        }
        if (isgiapxensieucap) {
            if (Util.canDoWithTime(lastTimegxsc, TIME_ITEM)) {
                isgiapxensieucap = false;
                Service.gI().point(player);
            }
        }

        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }

        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                Service.gI().point(player);
//                Service.gI().Send_Info_NV(this.player);
            }
        }

        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                Service.gI().point(player);
            }
        }
        if (isdanbacdau) {
            if (Util.canDoWithTime(tgianbacdau, TIME_ITEM)) {
                isdanbacdau = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh2) {
            if (Util.canDoWithTime(lastTimeAnDanh2, TIME_ITEM)) {
                isUseAnDanh2 = false;
            }
        }
        if (isUseCMS) {
            if (Util.canDoWithTime(lastTimeUseCMS, TIME_CMS)) {
                isUseCMS = false;
            }
        }
        if (isUseGTPT) {
            if (Util.canDoWithTime(lastTimeUseGTPT, TIME_ITEM)) {
                isUseGTPT = false;
            }
        }
        if (isUseDK) {
            if (Util.canDoWithTime(lastTimeUseDK, TIME_DK)) {
                isUseDK = false;
            }
        }
        if (isDuoiKhiTT) {
            if (Util.canDoWithTime(lastTimeDuoiKhiTT, panel.tuning.TrungThuTuning.isOn()
                    ? panel.tuning.TrungThuTuning.getLong("drop.tailSeconds", 900) * 1000L : 900000L)) {
                isDuoiKhiTT = false;
                services.ItemTimeService.gI().removeItemTime(player, 5072);
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                player.nPoint.initPowerLimit();
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
        if (isUseRX) {
            if (Util.canDoWithTime(lastTimeUseRX, timeRX)) {
                isUseRX = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
