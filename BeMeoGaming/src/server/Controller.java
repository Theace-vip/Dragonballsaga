package server;

import DanhSachBoss.ListBossesService;
import boss.Boss;
import boss.BossManager;
import consts.*;
import models.Consign.ConsignShopService;
import player.dailyGift.DailyGiftService;
import services.ClanService;
import services.ChatGlobalService;
import services.SubMenuService;
import services.Service;
import services.IntrinsicService;
import services.InventoryService;
import services.FlagBagService;
import services.ItemTimeService;
import services.SkillService;
import services.NpcService;
import services.TaskService;
import services.ItemMapService;
import services.PlayerService;
import services.FriendAndEnemyService;
import jdbc.DBConnecter;
import jdbc.NDVResultSet;
import utils.Util;
import data.DataGame;
import server.io.MySession;

import java.io.IOException;
import java.io.EOFException;

import Mail.HomThuService;
import services.func.ChangeMapService;
import services.func.UseItem;
import services.func.Input;
import data.ItemData;
import jdbc.daos.NDVSqlFetcher;
import jdbc.daos.PlayerDAO;
import jdbc.daos.SuperRankDAO;
import models.Card.Card;
import models.Card.RadarService;
import npc.NpcManager;
import player.Player;
import matches.PVPService;
import models.Achievement.AchievementService;
import shop.ShopService;
import network.inetwork.IMessageHandler;
import network.Message;
import network.inetwork.ISession;
import models.BlackBallWar.BlackBallWarService;
import models.SuperRank.SuperRankService;
import models.Training.TrainingService;
import services.MapService;
import models.Combine.CombineService;
import services.PhucLoi;
import services.TamBao;
import services.func.LuckyRound;
import services.func.TopService;
import services.func.TransactionService;
import skill.Skill;
import utils.Logger;

public class Controller implements IMessageHandler {

    private int errors;
    private long lastErrorLogMs;
    public static byte typeInfo;
    private static Controller instance;

    public static Controller gI() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    @Override
    public void onMessage(ISession s, Message _msg) {
        long st = System.currentTimeMillis();
        MySession _session = (MySession) s;
        Player player = null;
        try {
            player = _session.player;
            byte cmd = _msg.command;
            switch (cmd) {
                case PhucLoi.CMD_SEND:
                    if (player != null) {
                        PhucLoi.gI().sendPhucLoi(player);
                    }
                    break;
                case PhucLoi.CMD_ACTIVE:
                    if (player != null) {
                        PhucLoi.gI().claimReward(player, _msg.reader().readInt());
                    }
                    break;
                case -120:
                    HomThuService.gI().readMsg(_msg, player);
                    break;
                case TamBao.CMD_SEND:
                    if (player != null) {
                        TamBao.gI().sendMocTamBao(player);
                    }
                    break;
                case TamBao.CMD_ACTIVE:
                    if (player != null) {
                        byte tamBaoAction = _msg.reader().readByte();
                        if (tamBaoAction == TamBao.ACTION_CLAIM) {
                            TamBao.gI().nhanMocThuong(player, _msg.reader().readInt());
                        } else if (tamBaoAction == TamBao.ACTION_SPIN) {
                            TamBao.gI().quayTamBao(player, _msg.reader().readInt());
                        }
                    }
                    break;
                case -100:
                    if (player == null) {
                        return;
                    }
                    if (TransactionService.gI().check(player)) {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                        return;
                    }
                    if (player.baovetaikhoan) {
                        Service.gI().sendThongBao(player, "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                        return;
                    }
                    byte action = _msg.reader().readByte();
                    switch (action) {
                        case 0:
                            // ký gửi
                            short idItem = _msg.reader().readShort();
                            byte moneyType = _msg.reader().readByte();
                            int money = _msg.reader().readInt();
                            int quantity;
                            if (player.getSession().version >= 222) {
                                quantity = _msg.reader().readInt();
                            } else {
                                quantity = _msg.reader().readByte();
                            }
                            if (quantity > 0) {
                                ConsignShopService.gI().KiGui(player, idItem, money, moneyType, quantity);
                            }
                            break;
                        case 1:
                        case 2: // hủy ký gửi
                            // nhận tiền
                            idItem = _msg.reader().readShort();
                            ConsignShopService.gI().claimOrDel(player, action, idItem);
                            break;
                        case 3:
                            // buy item
                            idItem = _msg.reader().readShort();
                            _msg.reader().readByte();
                            _msg.reader().readInt();
                            ConsignShopService.gI().buyItem(player, idItem);
                            break;
                        case 4:
                            // next page
                            moneyType = _msg.reader().readByte();
                            money = _msg.reader().readByte();
                            ConsignShopService.gI().openShopKyGui(player, moneyType, money);
                            break;
                        case 5:
                            // up top
                            idItem = _msg.reader().readShort();
                            ConsignShopService.gI().upItemToTop(player, idItem);
                            break;
                        default:
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            break;
                        // hủy ký gửi
                    }
                    break;

                case 127:
                    if (player != null) {
                        byte actionRadar = _msg.reader().readByte();
                        switch (actionRadar) {
                            case 0:
                                RadarService.gI().sendRadar(player, player.Cards);
                                break;
                            case 1:
                                short idC = _msg.reader().readShort();
                                Card card = player.Cards.stream().filter(r -> r != null && r.Id == idC).findFirst()
                                        .orElse(null);
                                if (card != null) {
                                    if (card.Level == 0) {
                                        return;
                                    }
                                    if (card.Used == 0) {
                                        if (player.Cards.stream().anyMatch(c -> c != null && c.Used == 1)) {
                                            Service.gI().sendThongBao(player, "Số thẻ sử dụng đã đạt tối đa");
                                            return;
                                        }
                                        card.Used = 1;
                                    } else {
                                        card.Used = 0;
                                    }
                                    RadarService.gI().Radar1(player, idC, card.Used);
                                    Service.gI().point(player);
                                }
                                break;
                        }
                    }
                    break;
                case -105:
                    if (player != null) {
                        if (player.type == 0 && player.maxTime == 30) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 102, -1, Util.nextInt(60, 200));
                            player.iDMark.setGotoFuture(false);
                        } else if (player.type == 1 && player.maxTime == 5) {
                            if (player.iDMark != null && player.iDMark.isGoToBDKB()) {
                                ChangeMapService.gI().changeMap(player, MapService.gI().getMapCanJoin(player, 135, -1),
                                        35, 35);
                                player.iDMark.setGoToBDKB(false);
                            }
                        } else if (player.type == 2 && player.maxTime == 5) {
                            if (MapService.gI().isMapHanhTinhThucVat(player.zone.map.mapId)) {
                                ChangeMapService.gI().changeMap(player, 80, -1, -1, 5);
                            } else {
                                ChangeMapService.gI().changeMap(player, 160, -1, -1, 5);
                            }
                        } else if (player.type == 3 && player.maxTime == 5) {
                            ChangeMapService.gI().changeMap(player, player.iDMark.getZoneKhiGasHuyDiet(),
                                    player.iDMark.getXMapKhiGasHuyDiet(), player.iDMark.getYMapKhiGasHuyDiet());
                            player.iDMark.setZoneKhiGasHuyDiet(null);
                        } else if (player.type == 4 && player.maxTime == 5) {
                            if (player.iDMark != null && player.iDMark.isGoToKGHD()) {
                                ChangeMapService.gI().changeMap(player, MapService.gI().getMapCanJoin(player, 149, -1),
                                        100 + (Util.nextInt(-10, 10)), 336);
                                player.iDMark.setGoToKGHD(false);
                            }
                        } else if (player.type == 5 && player.maxTime == 5) {
                            ChangeMapService.gI().changeMap(player, MapService.gI().getMapCanJoin(player, 156, -1),
                                    100 + (Util.nextInt(-10, 10)), 336);
                        }
                    } else if (player != null) {
                        if (player.type == 6 && player.maxTime == 300) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 215, -1, Util.nextInt(60, 200));
                            player.iDMark.setGotoFuture1(false);
                        }
                    }
                    break;
                case 42:
                    // Client cmd 42 is for personal information, not account credentials.
                    break;
                case -127:
                    if (player != null) {
                        LuckyRound.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125:
                    if (_session.registeringAccount) {
                        if (player == null) {
                            Service.gI().registerAccount(_session, _msg);
                        } else {
                            _session.registeringAccount = false;
                        }
                    } else if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112:
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;
                case -34:
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1:
                                player.magicTree.openMenuTree();
                                break;
                            case 2:
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18:
                    if (player != null) {
                        player.changeMapVIP = true;
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72:
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59:
                    if (player != null) {
                        if (player.baovetaikhoan) {
                            Service.gI().sendThongBao(player,
                                    "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                            return;
                        }
                        PVPService.gI().controllerThachDau(player, _msg);
                    }
                    break;
                case -86:
                    if (player != null) {
                        TransactionService.gI().controller(player, _msg);
                    }
                    break;
                case -107:
                    if (player != null) {
                        Service.gI().showInfoPet(player);
                    }
                    break;
                case -109:
                    if (player != null && player.pet != null) {
                        Service.getInstance().InfoPetGoc(player);
                    }
                    break;
                case -108:
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;
                case 6: // buy item
                    if (player != null && !Maintenance.isRunning) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        if (player.baovetaikhoan) {
                            Service.gI().sendThongBao(player,
                                    "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                            return;
                        }
                        byte typeBuy = _msg.reader().readByte();
                        int tempId = _msg.reader().readShort();
                        // int quantity = 0;
                        // try {
                        // quantity = _msg.reader().readShort();
                        // } catch (Exception e) {
                        // }
                        ShopService.gI().takeItem(player, typeBuy, tempId);
                    }
                    break;
                case 7: // sell item
                    if (player != null && !Maintenance.isRunning) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        if (player.baovetaikhoan) {
                            Service.gI().sendThongBao(player,
                                    "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                            return;
                        }
                        action = _msg.reader().readByte();
                        if (action == 0) {
                            ShopService.gI().showConfirmSellItem(player, _msg.reader().readByte(),
                                    _msg.reader().readShort());
                        } else {
                            ShopService.gI().sellItem(player, _msg.reader().readByte(),
                                    _msg.reader().readShort());
                        }
                    }
                    break;
                case 29:
                    if (player != null) {
                        ChangeMapService.gI().openZoneUI(player);
                    }
                    break;
                case 21:
                    if (player != null) {
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case -71:
                    if (player != null) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case -79:
                    if (player != null) {
                        Service.gI().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113:
                    if (player != null) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                player.playerSkill.skillShortCut[i] = _msg.reader().readByte();
                            } catch (IOException e) {
                                player.playerSkill.skillShortCut[i] = -1;
                            }
                        }
                        player.playerSkill.sendSkillShortCut();
                    }
                    break;
                case -101:
                    login2(_session, _msg);
                    break;
                case -103:
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        switch (act) {
                            case 0 ->
                                Service.gI().openFlagUI(player);
                            case 1 ->
                                Service.gI().chooseFlag(player, _msg.reader().readByte());
                        }
                    }
                    break;
                case -7:
                    if (player != null) {
                        if (player.isDie()) {
                            Service.gI().charDie(player);
                            return;
                        }
                        if (player.effectSkill.isHaveEffectSkill()) {
                            return;
                        }
                        int toX = player.location.x;
                        int toY = player.location.y;
                        try {
                            byte b = _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            try {
                                toY = _msg.reader().readShort();
                            } catch (IOException ex) {
                            }
                            if (player.zone != null && MapService.gI().isMapBlackBallWar(player.zone.map.mapId)
                                    && Util.getDistance(player.location.x, player.location.y, toX, toY) > 500) {
                                return;
                            }
                            if (b == 1) {
                                AchievementService.gI().checkDoneTaskFly(player, player.location.x - toX);
                            }
                        } catch (IOException e) {
                        }
                        PlayerService.gI().playerMove(player, toX, toY);
                    }
                    break;
                case -74:
                    byte type = _msg.reader().readByte();
                    if (type == 1) {
                        DataGame.sendSizeRes(_session);
                    } else if (type == 2) {
                        DataGame.sendRes(_session);
                    }
                    break;
                case -81:
                    if (player != null) {
                        try {
                            _msg.reader().readByte();
                            int[] indexItem = new int[_msg.reader().readByte()];
                            for (int i = 0; i < indexItem.length; i++) {
                                indexItem[i] = _msg.reader().readByte();
                            }
                            CombineService.gI().showInfoCombine(player, indexItem);
                        } catch (IOException e) {
                        }
                    }
                    break;
                case -87:
                    DataGame.updateData(_session);
                    break;
                case -67:
                    int id = _msg.reader().readInt();
                    DataGame.sendIcon(_session, id);
                    break;
                case 66:
                    DataGame.sendImageByName(_session, _msg.reader().readUTF());
                    break;
                case -66:
                    int effId = _msg.reader().readShort();
                    int idT = effId;

                    if (effId == 25) {
                        idT = 50; // id eff rong muon thay doi ( hien tai la rong xuong)
                    }
                    if (effId == 25 && (player.zone.map.mapId == 1 || player.zone.map.mapId == 8 || player.zone.map.mapId == 15)) {
                        idT = 51; // id eff rong muon thay doi ( hien tai la rong xuong) 
                    }
                    if (effId == 25 && MapService.gI().isMapCold(player.zone.map)) {
                        idT = 59;
                    }
                    if (effId == 25 && player.zone.map.mapId == 5) {
                        idT = 50;
                    }
                    DataGame.effData(_session, effId, idT);

                    break;
//                 test
                case -62:
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte() & 0xFF);
                    }
                    break;
                case -63:
                    if (player != null) {
                        byte fbid = _msg.reader().readByte();
                        int fbidz = fbid & 0xFF; // Chuyển sang byte không dấu
                        FlagBagService.gI().sendIconEffectFlag(player, fbidz);
                    }
                    break;
                case -32:
                    int bgId = _msg.reader().readShort();
                    DataGame.sendItemBGTemplate(_session, bgId);
                    break;
                case 22:
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33:
                case -23:
                    if (player != null) {
                        ChangeMapService.gI().changeMapWaypoint(player);
                        Service.gI().hideWaitDialog(player);
                    }
                    break;
                case -45:
                    if (player != null) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        byte status = _msg.readByte();
                        SkillService.gI().useSkill(player, null, null, status, _msg);
                    }
                    break;
                case -46:
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51:
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54:
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
                    }
                    break;
                case -49:
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;
                case -50:
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56:
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47:
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55:
                    if (player != null) {
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57:
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
                case -40:
                    if (player != null) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        UseItem.gI().getItem(_session, _msg);
                    }
                    break;
                case -41:
                    Service.gI().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43:
                    if (player != null) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        if (player.baovetaikhoan) {
                            Service.gI().sendThongBao(player,
                                    "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                            return;
                        }
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91:
                    if (player != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE -> {
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                            }
                            case ConstMap.CHANGE_BLACK_BALL -> {
                                BlackBallWarService.gI().changeMap(player, _msg.reader().readByte());
                            }
                        }
                    }
                    break;
                case -39:
                    if (player != null) {
                        ChangeMapService.gI().finishLoadMap(player);
                    }
                    break;
                case 11:
                    byte modId = _msg.reader().readByte();
                    DataGame.requestMobTemplate(_session, modId);
                    break;
                case 44:
                    if (player != null) {
                        if (TransactionService.gI().check(player)) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện");
                            return;
                        }
                        Command.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case 32:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.gI().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        if (npcId != 54) {
                            MenuController.gI().openMenuNPC(_session, npcId, player);
                        } else {
                            if (player.TamkjllPetGiong != -1) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Saga_EmBe, -1,
                                        "|7|Đạo Lữ SSS\n"
                                        + "|7|Name Đạo Lữ: " + player.TamkjllNamePet + " \n"
                                        + "|7|LV Đạo Lữ: (" + (player.EmBeEXP * 100 / (3000000L + player.EmBeLv * 1500000L))
                                        + "%)-LV: "
                                        + player.EmBeLv + "\n"
                                        + "|4|Linh Hồn: " + player.LinhCanEmBe(player.TamkjllPetGiong) + "\n"
                                        + "Thức ăn: " + player.TamkjllPetHunger + "%\n"
                                        + "Sức mạnh: " + Util.getFormatNumber(player.TamkjllPetPower) + "\n"
                                        + "cần 15 phút để load hoặc thoát game ra vào lại\n"
                                        + "Hãy Cho Đạo Lữ Ăn Liên Tục Không Sẽ Chết Đói Đó\n"
                                        + "Chết Đạo Lữ Sẽ Bỏ Nhà Gia Đi\n"
                                        + "|6|Chủ Nhân Hãy Ra Lệnh Cho Ta\n",
                                        "Đi Theo", "PK Người", "Pk quái",
                                        "Về Nhà", "Kĩ Năng\nĐạo Lữ");
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn Chưa Có Đạo Lữ Để Dùng Tính Năng Này");
                            }
                        }
                    }
                    break;
                case 34:
                    if (player != null) {
                        int selectSkill = _msg.reader().readShort();
                        SkillService.gI().selectSkill(player, selectSkill);
                        if (selectSkill == Skill.SUPER_TRANFORMATION) {
                            SkillService.gI().useSkillTranformation(player);
                        } else if (selectSkill == Skill.EVOLUTION) {
                            SkillService.gI().useSkillEvolution(player);
                        }
                    }
                    break;
                case 54:
                    if (player != null) {
                        int mobId = _msg.reader().readByte();
                        int masterId = -1;
                        boolean isMobMe = mobId == -1;
                        if (isMobMe) {
                            masterId = _msg.reader().readInt();
                        }
                        // _msg.reader().readByte();
                        Service.gI().attackMob(player, mobId, isMobMe, masterId);
                    }
                    break;
                case -60:
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        // _msg.reader().readByte();
                        Service.gI().attackPlayer(player, playerId);
                    }
                    break;
                case -27:
                    _session.sendKey();
                    DataGame.sendVersionRes(_session);
                    break;
                case -111:
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20:
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        ItemMapService.gI().pickItem(player, itemMapId, false);
                    }
                    break;
                case -28:
                    messageNotMap(_session, _msg);
                    break;
                case -29:
                    messageNotLogin(_session, _msg);
                    break;
                case -30:
                    messageSubCommand(_session, _msg);
                    break;
                case -15: // về nhà
                    if (player != null) {
                        int mapId = MapService.gI().isMapMaBu(player.zone.map.mapId) ? 114 : player.gender + 21;
                        ChangeMapService.gI().changeMapBySpaceShip(player, mapId, 0, -1);
                    }
                    break;
                case -16: // hồi sinh
                    if (player != null && !player.isPKDHVT) {
                        PlayerService.gI().hoiSinh(player);
                    }
                    break;
                case -104:
                    if (player != null) {
                        Service.gI().mabaove(player, _msg.reader().readInt());
                    }
                    break;
                case -118:
                    if (player != null) {
                        int _id = _msg.readInt();
                        int menuType = player.iDMark.getMenuType();
                        switch (menuType) {
                            case 0, 1, 2 -> {
                                SuperRankService.gI().competing(player, _id);
                            }
                            default -> {
                                // chi may do boss (menuType=3) moi dich chuyen;
                                // cac list top/online gui menuType=9 -> bo qua
                                if (menuType != 3) {
                                    return;
                                }
                                if (player.Saga_VIP < 0) {
                                    Service.gI().sendThongBao(player, "Dịch Cái Lông Lợn À");
                                    return;
                                }
                                // lay boss theo vi tri dong da gui, khong goi getBoss nua (getBoss tra sai boss)
                                Boss boss = BossManager.gI().getBossFromTeleList(player, _id);
                                if (boss == null) {
                                    Service.gI().sendThongBao(player,
                                            "Danh sách máy dò đã hết hạn hoặc boss đã rời khỏi bản đồ, bạn dùng lại máy dò nhé!");
                                    return;
                                }
                                if (MapService.gI().isMapPhoBan(boss.zone.map.mapId)
                                        && !MapService.gI().isMapPhoBan(player.zone.map.mapId)) {
                                    Service.gI().sendThongBao(player, "Không thể tele vào bản đồ Phó Bản từ ngoài!");
                                    return;
                                }
                                // check truoc de khong bi im lang khi bi chan (nhiem vu chua mo map, map 122/123/124...)
                                if (ChangeMapService.gI().checkMapCanJoinByYardart(player, boss.zone) == null
                                        || ChangeMapService.gI().checkMapCanJoin(player, boss.zone) == null) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn chưa thể đến khu vực này, hãy làm nhiệm vụ để mở map nhé!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapYardrat(player, boss.zone, boss.location.x, boss.location.y);
                            }
                        }
                    }
                    break;

                case -38: // finish update
                    if (player != null) {
                        finishUpdate(player);
                    }
                    break;
                case 126: // androidPack2
                    break;
                case -78: // checkMMove
                    _msg.reader().readInt(); // second
                    break;
                case -114: // RequestPean
                    break;
                case 27:
                    // short menuid
                    break;
                case -76:
                    AchievementService.gI().confirmAchievement(player, _msg.reader().readByte());
                    break;
                default:
                    Logger.log(Logger.YELLOW, "CMD: " + cmd + "\n");
                    break;
            }
        } catch (Exception e) {
            // reset bo dem sau 60s: van de chan log tran, nhung khong cham chan log toi vien
            if (System.currentTimeMillis() - lastErrorLogMs > 60_000L) {
                errors = 0;
            }
            if (errors < 5) {
                errors++;
                lastErrorLogMs = System.currentTimeMillis();
                Logger.logException(Controller.class, e);
                if (player != null) {
                    Logger.warning("Player: " + player.name + "\n");
                }
                Logger.warning("Lỗi function: 'onMessage'\n");
                Logger.warning("Lỗi controller message command: " + _msg.command + "\n");
            }
        } finally {
            _msg.cleanup();
            _msg.dispose();
            long timeDo = System.currentTimeMillis() - st;
            if (timeDo > 1000) {
                //   Logger.warning(_msg.command + " - TimeOut: " + timeDo + " ms\n");
            }
        }
    }

    public void messageNotLogin(MySession session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0:
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        break;
                    case 2:
                        Service.gI().setClientType(session, msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.warning("[LOGIN] loi doc message dang nhap -> disconnect session\n");
                Logger.logException(Controller.class, e);
                session.disconnect();
            }
        }
    }

    public void messageNotMap(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte cmd = _msg.reader().readByte();
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    case 6:
                        DataGame.updateMap(_session);
                        break;
                    case 7:
                        DataGame.updateSkill(_session);
                        break;
                    case 8:
                        ItemData.updateItem(_session);
                        break;
                    case 10:
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    case 13:
                        // client ok
                        if (player != null && player.isPl()) {
                            player.nPoint.initPowerLimit();
                            if (player.pet != null) {
                                player.pet.nPoint.initPowerLimit();
                            }
                            Service.gI().player(player);
                            Service.gI().Send_Caitrang(player);
                            // -64 my flag bag
                            Service.gI().sendFlagBag(player);
                            // -113 skill shortcut
                            player.playerSkill.sendSkillShortCut();
                            // item time
                            ItemTimeService.gI().sendAllItemTime(player);
                            // send current task
                            TaskService.gI().sendInfoCurrentTask(player);
                            if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
                                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
                                    Service.gI().sendThongBao(player, "Nhiệm vụ của bạn là\nHãy di chuyển nhân vật");
                                    String npcSay = "Chào mừng " + player.name + " đến với thế giới Chiến Binh Rồng\n";
                                    npcSay += "Mình là "
                                            + (player.gender == 0 ? "Puaru" : player.gender == 1 ? "Piano" : "Icarus")
                                            + " sẽ đồng hành cũng bạn trên thế giới này\n";
                                    npcSay += "Để di chuyển, hãy chạm 1 lần vào nơi muốn đến";
                                    NpcService.gI().createTutorial(player, -1, npcSay);
                                }
                            } else {
                                // -70 thông báo bigmessage
                                sendThongBaoServer(player);
                            }

                            Service.gI().sendChibi(player);
                            player.zone.mapInfo(player);
                            if (player.getSession().version >= 231) {
                                for (Skill skill : player.playerSkill.skills) {
                                    if (skill.currLevel <= 0 || skill.template.type != 4) {
                                        continue;
                                    }
                                    SkillService.gI().sendCurrLevelSpecial(player, skill);
                                }
                            }
                            Service.gI().sendTimeSkill(player);
                            TrainingService.gI().tnsmLuyenTapUp(player);
                            InventoryService.gI().sendEffectBody(player);
                            if (player.getSession() != null && player.getSession().vnd > 0) {
                                AchievementService.gI().checkDoneTask(player, ConstAchievement.LAN_DAU_NAP_NGOC);
                            }
                            if (DailyGiftService.checkDailyGift(player, ConstDailyGift.NHAN_NGOC_MIEN_PHI)) {
                                Service.gI().sendThongBao(player,
                                        "Hôm nay bạn sẽ nhận được từ 1 đến 2 viên ngọc khi tiêu diệt 1 con quái");
                            }
                            if (player.SagaThienDao >= 1) {
                                ServerNotify.gI().notify("Người Chơi : " + player.name + ", Thiên đaok : "
                                        + player.SagaThienDao + " Đã Vào Game");
                            }
//                            if (player.Tamkjlltutien[1] >= 1) {
//                                ServerNotify.gI().notify("Đạo Hữu : [" + player.name + "] Cảnh giới : "
//                                        + player.TamkjllTuviTutien(Util.maxInt(player.Tamkjlltutien[1])) + " Đã Vào Game");
//                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    public void messageSubCommand(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte command = _msg.reader().readByte();
                switch (command) {
                    case -99:
                        byte typePet = _msg.reader().readByte();
                        long pointPet = _msg.reader().readLong();
                        if (player != null && player.pet != null && player.pet.nPoint != null) {
                            player.nPoint.increasePointPet(typePet, pointPet);
                        }
                        break;
                    case -97:
                        TopService.gI().ReceiveListTop(_msg, player);
                        break;
                    case 98:
                        ListBossesService.gI().ReceiveMessage(player, _msg);
                        break;
                    case 99:
                        typeInfo = _msg.reader().readByte();
                        byte typeSend = _msg.reader().readByte();
                        int playerID = _msg.reader().readInt();
                        Player pl = NDVSqlFetcher.loadById(playerID);

                        switch (typeSend) {
                            case 0:
                                Service.gI().sendCharInfoByID(_session, pl);
                                break;
                            case 1:
                                Service.gI().sendCharBodyInfo(_session, pl);
                                break;
                            case 2:
                                Service.gI().sendCharMainInfo(_session, pl);
                                break;
                        }

                        break;
                    case 16: {
                        // Client (NRO) gửi writeByte(type) + writeShort(số điểm). Không đọc readLong
                        // vì sẽ cạn dữ liệu gây EOFException khi cộng điểm tiềm năng.
                        byte type = _msg.reader().readByte();
                        long point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null && player.iDMark != null) {
                            // dem toc do yeu cau (acs): cho phep cong nhanh toi da 100 req/s, qua do bo qua
                            long now = System.currentTimeMillis();
                            if (now - player.iDMark.getAcsWindowTime() > 1000L) {
                                player.iDMark.setAcsWindowTime(now);
                                player.iDMark.setAcsCount(0);
                            }
                            int rate = player.iDMark.getAcsCount() + 1;
                            player.iDMark.setAcsCount(rate);
                            boolean le = type < 0 || type > 4 || point <= 0;
                            if (rate == 10 || rate == 101 || (le && rate <= 2)) {
                                Logger.log("[ACS] " + player.name + " ver="
                                        + (player.getSession() == null ? -1 : player.getSession().version)
                                        + " type=" + type + " point=" + point + " rate=" + rate + "/s" + (le ? " LE" : ""));
                            }
                            if (rate > 100) {
                                break;
                            }
                            player.nPoint.increasePoint(type, point);
                        }
                        break;
                    }
                    case 64:
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    public void createChar(MySession session, Message msg) {
        if (!Maintenance.isRunning) {
            NDVResultSet rs = null;
            boolean created = false;
            try {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() >= 5 && name.length() <= 10) {
                    rs = DBConnecter.executeQuery("select * from player where name = ?", name);
                    if (rs.next()) {
                        Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        if (Util.haveSpecialCharacter(name)) {
                            Service.gI().sendThongBaoOK(session, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerDAO.createNewPlayer(session.userId, name.toLowerCase(), (byte) gender,
                                        hair);

                            }
                        }
                    }
                } else {
                    Service.gI().sendThongBaoOK(session,
                            "Tên nhân vật chỉ đồng ý các ký tự a-z, 0-9 và chiều dài từ 5 đến 10 ký tự");
                }
            } catch (Exception e) {
                Logger.logException(Controller.class, e);
            } finally {
                if (rs != null) {
                    rs.dispose();
                }
            }
            if (created) {
                session.login(session.uu, session.pp);
            }
        }
    }

    public void login2(MySession session, Message msg) {
        if (session.player != null) {
            return;
        }
        session.registeringAccount = false;
        try {
            String username = msg.reader().readUTF();
            byte action = msg.reader().readByte();
            if (username.isEmpty() && action == 1) {
                Service.gI().switchToRegisterScr(session);
            }
        } catch (IOException e) {
            Logger.logException(Controller.class, e);
        }
    }

    public void sendInfo(MySession session) {
        try {

            Player player = session.player;

            // Phúc Lợi (cmd 103)
            PhucLoi.gI().sendPhucLoi(player);

            // Tầm Bảo - Vòng Quay (cmd 106)
            TamBao.gI().sendTamBao(player);
            TamBao.gI().sendMocTamBao(player);
            TamBao.gI().sendWonSlots(player);

            // -82 set tile map
            DataGame.sendTileSetInfo(session);

            // 112 my info intrinsic
            IntrinsicService.gI().sendInfoIntrinsic(player);

            // -42 my point
            Service.gI().point(player);

            // 40 task
            TaskService.gI().sendTaskMain(player);

            // -22 reset all
            Service.gI().clearMap(player);

            // -53 my clan
            ClanService.gI().sendMyClan(player);

            // -69 max statima
            PlayerService.gI().sendMaxStamina(player);

            // -68 cur statima
            PlayerService.gI().sendCurrentStamina(player);

            // -97 năng động
            Service.gI().sendNangDong(player);
            // -107 have pet
            Service.gI().sendHavePet(player);
            // -119 top rank
            Service.gI().sendTopRank(player);
            if (player.superRank != null && player.superRank.rank < 1) {
                player.superRank.rank = SuperRankDAO.getHighestRank() + 1;
                SuperRankDAO.updateRank(player);
            }
            if (player.inventory.itemsBody.get(12).isNotNullItem()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(12).template.id);
                    } catch (Exception e) {
                    }
                }).start();
            }
            // -50 thông tin bảng thông báo
            ServerNotify.gI().sendNotifyTab(player);
            // check activation set
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            // send can auto play
            ItemTimeService.gI().sendCanAutoPlay(player);
            player.start();
        } catch (Exception e) {
        }
    }

    public void finishUpdate(Player player) {
        if (player.getSession() != null) {
            player.getSession().finishUpdate = true;
        }
    }

    private void sendThongBaoServer(Player player) {
        String s = "|7|Xin Chào Bạn Trẻ\n"
                + "|6|Hãy Online Thật Nhiều Mỗi Ngày Nha Nhận quà\n"
                + "Hãy Truy Cập Phúc Lợi Để Xem Hoạt Động Free\n"
                + "|7|VIP Hiện Tại " + player.Saga_VIP
                + "\nĐã Online Được " + player.phutOnline + " Phút"
                + "\nĐã Điểm Danh " + player.listDiemDanh.size() + " Ngày";

        Service.gI().sendThongBaoFromAdmin(player, s);

    }
}
