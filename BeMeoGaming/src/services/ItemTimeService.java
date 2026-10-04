package services;

import consts.ConstPlayer;
import item.Item;

import static item.ItemTime.*;

import player.Fusion;
import player.Player;
import network.Message;

import java.io.IOException;

import models.TreasureUnderSea.TreasureUnderSea;
import models.SnakeWay.SnakeWay;
import models.DestronGas.DestronGas;
import models.RedRibbonHQ.RedRibbonHQ;
import utils.Logger;

public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    //gửi cho client
    public void sendAllItemTime(Player player) {
        ItemTimeService.gI().sendTextBanDoKhoBau(player);
        ItemTimeService.gI().sendTextDoanhTrai(player);
        ItemTimeService.gI().sendTextConDuongRanDoc(player);
        ItemTimeService.gI().sendTextKhiGasHuyDiet(player);
        ItemTimeService.gI().sendTextTimePickDoanhTrai(player);
        if (player.effectSkill.isSuper) {
            switch (player.gender) {
                case 0:
                    sendItemTime(player, 30006 + (player.playerSkill.getSkillbyId(27).point - player.numUseSkill - 1), (int) ((player.effectSkill.lastTimeUpSuper + player.effectSkill.timeSuper) - System.currentTimeMillis()) / 1000);
                    break;
                case 1:
                    sendItemTime(player, 30012 + (player.playerSkill.getSkillbyId(28).point - player.numUseSkill - 1), (int) ((player.effectSkill.lastTimeUpSuper + player.effectSkill.timeSuper) - System.currentTimeMillis()) / 1000);
                    break;
                case 2:
                    sendItemTime(player, 30000 + (player.playerSkill.getSkillbyId(29).point - player.numUseSkill - 1), (int) ((player.effectSkill.lastTimeUpSuper + player.effectSkill.timeSuper) - System.currentTimeMillis()) / 1000);
                    break;
            }
        }
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isItemTest) {
            sendItemTime(player, 21877, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeItemTest)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        if (player.itemTime.isdanbacdau) {
            sendItemTime(player, 30079, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.tgianbacdau)) / 1000));
        }
      //lIST MÁY DÒ TNSM
        if (player.itemTime.isBINHX3) {
            sendItemTime(player, 21877, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX3)) / 1000));
        }
      
        if (player.itemTime.isBINHX5) {
            sendItemTime(player, 21878, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX5)) / 1000));
        }
        
          if (player.itemTime.isBINHX7) {
            sendItemTime(player, 21879, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX7)) / 1000));
        }
           if (player.itemTime.isBINHX10) {
            sendItemTime(player, 21880, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeBINHX10)) / 1000));
        }

        // [The Diem Farm 27/09/2026] dem nguoc cac the diem farm (icon 32324/32325/32327)
        if (player.itemTime.isFamX2) {
            sendItemTime(player, 32324, (int) ((thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX2)) / 1000));
        }
        if (player.itemTime.isFamX3) {
            sendItemTime(player, 32325, (int) ((thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX3)) / 1000));
        }
        if (player.itemTime.isFamX5) {
            sendItemTime(player, 32327, (int) ((thoiGianTheFamMs() - (System.currentTimeMillis() - player.itemTime.lastTimeFamX5)) / 1000));
        }
          
           if (player.itemTime.isUpTbx2) {
               sendItemTime(player, 10087, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx2)) / 1000));
        } 
          if (player.itemTime.isUpTbx3) {
               sendItemTime(player, 10088, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx3)) / 1000));
        } 
         if (player.itemTime.isUpTbx4) {
               sendItemTime(player, 10089, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx4)) / 1000));
        }    
            if (player.itemTime.isUpTbx5) {
               sendItemTime(player, 10090, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx5)) / 1000));
        } 
          if (player.itemTime.isUpTbx6) {
               sendItemTime(player, 10091, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx6)) / 1000));
        } 
          
           if (player.itemTime.isUpTbx7) {
               sendItemTime(player, 10092, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.thoigianUpx7)) / 1000));
        } 
       //
         if (player.itemTime.isLYNUOCMIATO) {
            sendItemTime(player, 13462, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeLYNUOCMIATO)) / 1000));
        }
      
        if (player.itemTime.isLYNUOCMIATHOM) {
            sendItemTime(player, 13463, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeLYNUOCMIATHOM)) / 1000));
        }
        
          if (player.itemTime.isLYNUOCMIASAURIENG) {
            sendItemTime(player, 13464, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastLYNUOCMIASAURIENG)) / 1000));
        }
         if (player.itemTime.isMAYDOSKH) {
            sendItemTime(player, 15083, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoskh)) / 1000));
        }
        
          if (player.itemTime.isMAYDOSKHVIP) {
            sendItemTime(player, 15084, (int) ((TIME_1H - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoskhvip)) / 1000));
        }  
          //
          
           if (player.itemTime.iscuongnosieucap) {
            sendItemTime(player, 21336, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimecnsc)) / 1000));
        }
        
          if (player.itemTime.isbohuyetsieucap) {
            sendItemTime(player, 21337, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebhsc)) / 1000));
        }
         if (player.itemTime.isbokhisieucap) {
            sendItemTime(player, 21338, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimebksc)) / 1000));
        }
        
          if (player.itemTime.isgiapxensieucap) {
            sendItemTime(player, 21339, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimegxsc)) / 1000));
        }  
            if (player.itemTime.isBanhTrungDacBiet) {
            sendItemTime(player, 4125, (int) ((TIME_150p - (System.currentTimeMillis() - player.itemTime.lastTimeBanhDacBiet)) / 1000));
        }
        
          if (player.itemTime.isBanhTrungThu) {
            sendItemTime(player, 4126, (int) ((TIME_150p - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungthu)) / 1000));
        }  
            if (player.itemTime.isBanhTrung1Trung) {
            sendItemTime(player, 4042, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanh1Trung)) / 1000));
        }
        
          if (player.itemTime.isBanhTrung2Trung) {
            sendItemTime(player, 4043, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanh2Trung)) / 1000));
        }   
             if (player.itemTime.isBanhgaquay) {
            sendItemTime(player, 8132, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimegaquay)) / 1000));
        }
        
          if (player.itemTime.isBanhthapcam) {
            sendItemTime(player, 8131, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimethapcam)) / 1000));
        }   
          
          
          
          //***************************************
        
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet2) {
            sendItemTime(player, 10714, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) / 1000));
        }
        if (player.itemTime.isUseBoKhi2) {
            sendItemTime(player, 10715, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) / 1000));
        }
        if (player.itemTime.isUseGiapXen2) {
            sendItemTime(player, 10712, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) / 1000));
        }
        if (player.itemTime.isUseCuongNo2) {
            sendItemTime(player, 10716, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) / 1000));
        }

        if (player.itemTime.isUseAnDanh2) {
            sendItemTime(player, 10717, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2)) / 1000));
        }
        if (player.itemTime.isUseCMS) {
            sendItemTime(player, 5829, (int) ((TIME_CMS - (System.currentTimeMillis() - player.itemTime.lastTimeUseCMS)) / 1000));
        }
        if (player.itemTime.isUseNCD) {
            sendItemTime(player, 11173, (int) ((TIME_NCD - (System.currentTimeMillis() - player.itemTime.lastTimeUseNCD)) / 1000));
        }
        if (player.itemTime.isUseGTPT) {
            sendItemTime(player, 3778, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGTPT)) / 1000));
        }
        if (player.itemTime.isUseDK) {
            sendItemTime(player, 5072, (int) ((TIME_DK - (System.currentTimeMillis() - player.itemTime.lastTimeUseDK)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.isUseMayDo2) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) / 1000));
        }
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isEatMeal2) {
            sendItemTime(player, player.itemTime.iconMeal2, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal2)) / 1000));
        }
        if (player.itemTime.isUseTDLT) {
            sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        }
        if (player.itemTime.isUseRX) {
            sendItemTime(player, 6581, player.itemTime.timeRX / 1000);
        }
    }

    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        int minleft = 0;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = (int) io.param;
                minleft = (min * 60 - 32767) / 60;
                io.param = minleft > 0 ? minleft : 0;
                break;
            }
        }

        player.itemTime.isUseTDLT = true;

        int timeTDLTInMinutes = min - minleft;
        int timeTDLTInSeconds = timeTDLTInMinutes * 60;
        int maxTimeInSeconds = 32767;
        int timeTDLTInSecondsLimited = Math.min(timeTDLTInSeconds, maxTimeInSeconds);

        player.itemTime.timeTDLT = timeTDLTInSecondsLimited * 1000;

        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, timeTDLTInSecondsLimited);
        InventoryService.gI().sendItemBag(player);
    }

    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param += (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryService.gI().sendItemBag(player);
    }

    public void removeTextTimeSuperNew(Player player) {
        switch (player.gender) {
            case 0:
                sendItemTime(player, 30006 + (player.playerSkill.getSkillbyId(27).point - player.numUseSkill - 1), 5);
                break;
            case 1:
                sendItemTime(player, 30012 + (player.playerSkill.getSkillbyId(28).point - player.numUseSkill - 1), 5);
                break;
            case 2:
                sendItemTime(player, 30000 + (player.playerSkill.getSkillbyId(29).point - player.numUseSkill - 1), 5);
                break;
        }
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (IOException e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.lastTimeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.lastTimeOpenDoanhTrai) / 1000);
            int secondsLeft = (RedRibbonHQ.TIME_DOANH_TRAI / 1000) - secondPassed;
            if (secondsLeft < 0 || secondsLeft > 1800) {
                return;
            }
            sendTextTime(player, DOANH_TRAI, "Trại độc nhãn:", secondsLeft);
        }
    }

    public void sendTextTimePickDoanhTrai(Player player) {
        if (player.clan != null && player.clan.doanhTrai != null && player.clan.doanhTrai.isTimePicking) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.doanhTrai.lastTimePick) / 1000);
            int secondsLeft = (RedRibbonHQ.TIME_PICK_DOANH_TRAI / 1000) - secondPassed;
            if (secondsLeft < 0 || secondsLeft > 1800) {
                return;
            }
            sendTextTime(player, DOANH_TRAI, "Trại độc nhãn:", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.lastTimeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.lastTimeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (TreasureUnderSea.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            if (secondsLeft < 0 || secondsLeft > 1800) {
                return;
            }
            sendTextTime(player, BAN_DO_KHO_BAU, "Hang kho báu:", secondsLeft);
        }
    }

    public void sendTextXinbato(Player player) {
        sendTextTime(player, BAN_DO_KHO_BAU, "Tìm nước cho Xinbatô ở đảo Kame hoặc đảo Guru", 30);
    }

    public void sendTextConDuongRanDoc(Player player) {
        if (player.clan != null
                && player.clan.lastTimeOpenConDuongRanDoc != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.lastTimeOpenConDuongRanDoc) / 1000);
            int secondsLeft = (SnakeWay.TIME_CON_DUONG_RAN_DOC / 1000) - secondPassed;
            if (secondsLeft < 0 || secondsLeft > 1800) {
                return;
            }
            sendTextTime(player, CON_DUONG_RAN_DOC, "Con đường rắn độc:", secondsLeft);
        }
    }

    public void sendTextKhiGasHuyDiet(Player player) {
        if (player.clan != null
                && player.clan.lastTimeOpenKhiGasHuyDiet != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.lastTimeOpenKhiGasHuyDiet) / 1000);
            int secondsLeft = (DestronGas.TIME_KHI_GAS_HUY_DIET / 1000) - secondPassed;
            if (secondsLeft < 0 || secondsLeft > 1800) {
                return;
            }
            sendTextTime(player, KHI_GAS_HUY_DIET, "Khí gas hủy diệt:", secondsLeft);
        }
    }

    public void sendTextTimeKeoBuaBao(Player player, int time) {
        sendTextTime(player, TIME_KEO_BUA_BAO, "Thời gian : ", time);
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextBanDoKhoBau(Player player) {
        removeTextTime(player, BAN_DO_KHO_BAU);
    }

    public void removeTextConDuongRanDoc(Player player) {
        removeTextTime(player, CON_DUONG_RAN_DOC);
    }

    public void removeTextKhiGasHuyDiet(Player player) {
        removeTextTime(player, KHI_GAS_HUY_DIET);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, null, 0);
    }

    public void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text == null ? "" : text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        // gioi han kieu short cua giao thuc - neu buff the diem farm cong don vuot 32767 giay van gui duoc
        if (time < 0) {
            time = 0;
        } else if (time > 32767) {
            time = 32767;
        }
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
