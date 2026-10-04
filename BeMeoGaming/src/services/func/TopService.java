package services.func;

import Top.TOPDAME;
import Top.TOPThoMo;
import Top.TopCauCa;
import Top.TopChuyenSinh;
import Top.TopDapdo;
import Top.TopDiaDao;
import Top.TopEventManager;
import Top.TopFam;
import Top.TopPowerManager;
import Top.TopRuong;
import Top.TopSanboss;
import Top.TopSk;
import Top.TopTamBao;
import Top.TopTaskManager;
import Top.TopThienDao;
import Top.TopTutien;
import Top.TopVIPGame;
import Top.TopViThu;
import Top.TopVnd;
import consts.ConstSQL;

import java.io.IOException;

import jdbc.DBConnecter;
import player.Player;
import server.Manager;
import network.Message;
import utils.Logger;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jdbc.NDVDB;
import jdbc.daos.NDVSqlFetcher;

import matches.TOP;
import services.Service;
import services.TaskService;
import utils.NinjaUtil;
import utils.Util;

public class TopService {

    private static TopService instance;

    public static String[] topTabName = new String[]{
        "Power", "Donate", "TrainFam", "Sự Kiện", "Đập Đồ", "Mở Rương", "Săn Boss"
            ,"Thiên Đạo","Địa Đạo","Tu Tiên","Chuyển Sinh","InGameVIP","Vĩ Thú","Tầm Bảo","Câu Cá","Thợ Mỏ"
    };
    public static List<List<Player>> arrListTop = new ArrayList<>();

    static {
        for (int i = 0; i < 50; i++) {
            arrListTop.add(new ArrayList<>()); // tạo list rỗng
        }
    }

    public static TopService gI() {
        if (instance == null) {
            instance = new TopService();
        }
        return instance;
    }

    public void updateTop() {
        if (Manager.timeRealTop + (10 * 60 * 1000) < System.currentTimeMillis()) {
            updateTopNow();
        }
    }

    /** Nap lai ngay 4 top lon (khong throttle) - dung cho thread nen tu dong. */
    public void updateTopNow() {
        Manager.timeRealTop = System.currentTimeMillis();
        try (Connection con = DBConnecter.getConnectionServer()) {
            Manager.topNV = Manager.realTop(ConstSQL.TOP_NV, con);
            Manager.topDC = Manager.realTop(ConstSQL.TOP_DC, con);
            Manager.topVDST = Manager.realTop(ConstSQL.TOP_VDST, con);
            Manager.topWHIS = Manager.realTop(ConstSQL.TOP_WHIS, con);
        } catch (Exception ignored) {
            Logger.error("Lỗi đọc top\n");
        }
    }

    /**
     * Thread nen: reload toan bo bang xep hang (Manager.reloadtop + 4 top TopService)
     * moi `minutes` phut. Goi 1 lan khi server boot - muon them/sua top chi can
     * sua reloadtop(), khong phai code them scheduler.
     */
    public static void startAutoRefresh(long minutes) {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(minutes * 60_000L);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    Manager.reloadtop();
                    gI().updateTopNow();
                } catch (Exception e) {
                    Logger.logException(TopService.class, e);
                }
            }
        }, "Top-AutoRefresh");
        t.setDaemon(true);
        t.start();
    }

    public void ReceiveListTop(Message msg, Player player) throws IOException {
        byte b = msg.reader().readByte();
        switch (b) {
            case 0:
                sendTopTabName(player);
                break;
            case 1:
                byte index = msg.reader().readByte();
                sendTopList(player, index);
                break;
        }
    }

    public void sendTopList(Player pl, byte index) {
        Message msg = null;
        try {
            msg = Service.gI().messageSubCommand((byte) -97);
            msg.writer().writeByte(1);
            callbackTOP(index);
            List<Player> listT = arrListTop.get(index);
            int size = Math.min(100, listT.size());
            msg.writer().writeByte(index);
            msg.writer().writeInt(size);
            for (int i = 0; i < listT.size(); i++) {
                Player top = listT.get(i);
                msg.writer().writeByte(top.gender);
                msg.writer().writeInt((int) top.id);
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF(NinjaUtil.formatNumber2(top.nPoint.power));
                msg.writer().writeUTF(getInfoTop(index, i));
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    private void callbackTOP(int index) {
        switch (index) {
            case 0:
                TopPowerManager.getInstance().load();
                break;
            case 1:
                TopVnd.getInstance().load();
                break;
            case 2:
                TopFam.getInstance().load();
                break;
            case 3:
                TopSk.getInstance().load();
                break;
            case 4:
                TopDapdo.getInstance().load();
                break;
            case 5:
                TopRuong.getInstance().load();
                break;
            case 6:
                TopSanboss.getInstance().load();
                break;
                 case 7:
                TopThienDao.getInstance().load();
                break;

                 case 8:
                TopDiaDao.getInstance().load();
                break;
                 case 9:
                TopTutien.getInstance().load();
                break;
                  case 10:
                 TopChuyenSinh.getInstance().load();
                break;
                 case 11:
                 TopVIPGame.getInstance().load();
                break;
                  case 12:
                 TopViThu.getInstance().load();
                break;
                case 13:
                 TopTamBao.getInstance().load();
                break;
                case 14:
                 TopCauCa.getInstance().load();
                break;
                 case 15:
                 TOPThoMo.getInstance().load();
                break;
        }
    }

    private String getInfoTop(int index, int indexTop) {
        List<Player> listT = arrListTop.get(index);
        Player top = listT.get(indexTop);
        switch (index) {
            case 0:
                return "<color=black>Tu Vi:</color> " + top.getTuVi();
            case 1:
                return "<color=black>Đã Nạp:</color> " + Util.numberToMoney(top.bktdeptrai) + " Coin";
            case 2:
                return "<color=black>Đã Fam:</color> " + Util.numberToMoney(top.diemfam) + " Điểm";
            case 3:
                return "<color=black>Sự Kiện:</color> " + Util.numberToMoney(top.sukien) + " Điểm";
            case 4:
                return "<color=black>Đã Đập Đồ:</color> " + (top.point_dapdo) + " Sao";
            case 5:
                return "<color=black>Đã Mở :</color> " + (top.point_moruong) + " Rương Thần Bí";
            case 6:
                return "<color=black>Đã Săn :</color> " + (top.point_sb) + " Boss";
                case 7:
                return "<color=black>Thiên Đạo :</color> " + (top.SagaThienDao) + " Cấp";
                 case 8:
                return "<color=black>Địa Đạo :</color> " + (top.SagaDiaDao) + " Cấp";
                  case 9:
                return "<color=black>Cảnh giới :</color> " + top.TamkjllTuviTutien(Util.maxInt(top.SagaTuTien[1])) + " ";
                 case 10:
                return "<color=black>Chuyển Sinh :</color> " + (top.SagaChuyenSinh) + " Lần";
                 case 11:
                return "<color=black>InGameVIP :</color> " + (top.Saga_VIP) + " ";
                  case 12:
                return "<color=black>Đã Săn :</color> " + (top.point_vithu) + " Boss Vĩ Thú";
                  case 13:
                return "<color=black>Quay Trúng :</color> " + (top.SukienTamBao) + " Vật Phẩm Hiếm";
                  case 14:
                return "<color=black>Đã Câu Trúng :</color> " + (top.point_cauca) + " Cá Hiếm";
                 case 15:
                return "<color=black>Số Người :</color> " + (top.TamkjllThomo) + " Thợ Đào";
                
        }
        return "";
    }

    ;
    public void sendTopTabName(Player player) {
        Message msg = null;
        try {
            msg = Service.gI().messageSubCommand((byte) -97);
            msg.writer().writeByte(0);
            int size = topTabName.length;
            msg.writer().writeInt(size);
            for (int i = 0; i < size; i++) {
                msg.writer().writeUTF(topTabName[i]);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopEvent(Player player) {
        TopEventManager.getInstance().load();
        List<Player> list = TopEventManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100 Sự kiện");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Điểm: " + Util.numberFormat(top.inventory.event));
                msg.writer().writeUTF("Thằng này TOP " + (i + 1));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void TopFam(Player player) {
        TopFam.getInstance().load();
        List<Player> list = TopFam.getInstance().getList();
        if (list == null) {
            return;
        }

        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));

            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());

                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }

                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Điểm Fam: " + Util.FormatNumber(top.diemfam));
                msg.writer().writeUTF("Top Điểm Fam");
            }
            player.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopPower(Player player) {
        TopPowerManager.getInstance().load();
        List<Player> list = TopPowerManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Sức mạnh: " + Util.FormatNumber(top.nPoint.power));
                msg.writer().writeUTF("Top sức mạnh");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTopVnd(Player player) {
        TopVnd.getInstance().load();
        List<Player> list = TopVnd.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Top Nạp: " + Util.numberToMoney(top.bktdeptrai));
                msg.writer().writeUTF("...");
            }
            player.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopsucdanh(Player player) {
        TOPDAME.getInstance().load();
        List<Player> list = TOPDAME.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Dame: " + Util.numberFormat(top.nPoint.dame));
                msg.writer().writeUTF("Top Dame");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopphoban(Player player) {
        TopPowerManager.getInstance().load();
        List<Player> list = TopPowerManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("Dame: " + Util.numberFormat(top.clan.KhiGasHuyDiet.id));
                msg.writer().writeUTF("Top Dame");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void showListTopTask(Player player) {
        TopTaskManager.getInstance().load();
        List<Player> list = TopTaskManager.getInstance().getList();
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(Math.min(100, list.size()));
            for (int i = 0; i < Math.min(100, list.size()); i++) {
                Player top = list.get(i);
                Player pl = NDVSqlFetcher.loadById(top.id);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());

                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.name);
                msg.writer().writeUTF("[" + pl.playerTask.taskMain.id + "]" + pl.playerTask.taskMain.name);
                Instant instant = Instant.ofEpochMilli(pl.playerTask.taskMain.lastTime);

                // Chuyển đổi sang đối tượng ZonedDateTime theo múi giờ
                ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());

                // Định dạng ngày giờ
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                // Hiển thị kết quả
                String formattedDateTime = dateTime.format(formatter);
                msg.writer().writeUTF("Thời gian hoàn thành: " + formattedDateTime);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTop(Player player, int select) {
        // tu nạp top neu thieu; updateTop() tu gioi han refresh 10 phut/lan nen goi moi lan mo menu
        TopService.gI().updateTop();
        List<TOP> tops = new ArrayList<>();
        switch (select) {
            case 0 ->
                tops = Manager.topNV;
            case 1 ->
                tops = Manager.topDC;
            case 2 ->
                tops = Manager.topSM;
            case 3 ->
                tops = Manager.topWHIS;
        }
        if (tops == null) {
            Service.gI().sendThongBao(player, "Đang tải dữ liệu Top, vui lòng thử lại sau");
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            // client doc so dong dang BYTE -> writeInt se hien 0 dong (giong loi showListTop/showListBoss)
            int topCount = Math.min(100, tops.size());
            msg.writer().writeByte(topCount);
            for (int i = 0; i < topCount; i++) {
                TOP top = tops.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.getName());
                switch (select) {
                    case 0 -> {
                        msg.writer()
                                .writeUTF(TaskService.gI().getTaskMainById(player, top.getNv()).name.substring(0,
                                        TaskService.gI().getTaskMainById(player, top.getNv()).name.length() > 20 ? 20
                                        : TaskService.gI().getTaskMainById(player, top.getNv()).name.length())
                                        + "...");
                        msg.writer().writeUTF(
                                TaskService.gI().getTaskMainById(player, top.getNv()).subTasks.get(top.getSubnv()).name
                                + " - " + getTimeLeft(top.getLasttime()));
                    }
                    case 1 -> {
                        msg.writer().writeUTF("Chơi đồ " + top.getDicanh() + " lần");
                        msg.writer().writeUTF("Gia nhập juventus " + top.getJuventus() + " lần");
                    }
                    case 2 -> {
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                        msg.writer().writeUTF("...");
                    }
                    case 3 -> {
                        msg.writer().writeUTF("LV:" + top.getLevel() + " với "
                                + Util.FormatNumber(top.getTime() / 1000d) + " giây");
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void showListTopdame(Player player, int select) {
        List<TOP> tops = new ArrayList<>();
        switch (select) {
            case 0 ->
                tops = Manager.topSD;
            case 1 ->
                tops = Manager.topHP;
            case 2 ->
                tops = Manager.topKI;
            case 3 ->
                tops = Manager.topVDST;
        }
        Message msg = null;
        try {
            msg = new Message(-96);
            player.iDMark.setMenuType(9); // list top khong dung cho may do boss
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            // client doc so dong dang BYTE -> writeInt se hien 0 dong (giong loi showListTop/showListBoss)
            int topCount = Math.min(100, tops.size());
            msg.writer().writeByte(topCount);
            for (int i = 0; i < topCount; i++) {
                TOP top = tops.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(i + 1);
                msg.writer().writeShort(top.getHead());
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(top.getBody());
                msg.writer().writeShort(top.getLeg());
                msg.writer().writeUTF(top.getName());
                switch (select) {
                    case 0 -> {
                        msg.writer()
                                .writeUTF(TaskService.gI().getTaskMainById(player, top.getNv()).name.substring(0,
                                        TaskService.gI().getTaskMainById(player, top.getNv()).name.length() > 20 ? 20
                                        : TaskService.gI().getTaskMainById(player, top.getNv()).name.length())
                                        + "...");
                        msg.writer().writeUTF(
                                TaskService.gI().getTaskMainById(player, top.getNv()).subTasks.get(top.getSubnv()).name
                                + " - " + getTimeLeft(top.getLasttime()));
                    }
                    case 1 -> {
                        msg.writer().writeUTF("Chơi đồ " + top.getDicanh() + " lần");
                        msg.writer().writeUTF("Gia nhập juventus " + top.getJuventus() + " lần");
                    }
                    case 2 -> {
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                        msg.writer().writeUTF("...");
                    }
                    case 3 -> {
                        msg.writer().writeUTF("LV:" + top.getLevel() + " với "
                                + Util.roundToTwoDecimals(top.getTime() / 1000d) + " giây");
                        msg.writer().writeUTF(getTimeLeft(top.getLasttime()));
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static String getTimeLeft(long lastTime) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        return secondPassed > 86400 ? (secondPassed / 86400) + " ngày trước"
                : secondPassed > 3600 ? (secondPassed / 3600) + " giờ trước"
                        : secondPassed > 60 ? (secondPassed / 60) + " phút trước" : secondPassed + " giây trước";
    }

}
