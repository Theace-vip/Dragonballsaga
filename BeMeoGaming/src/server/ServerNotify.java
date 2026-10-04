package server;

import player.Player;
import network.Message;
import services.Service;
import utils.Util;
import java.util.ArrayList;
import java.util.List;

public class ServerNotify extends Thread {

    private long lastNotifyTime;

    private final List<String> notifies;

    private int indexNotify;

    private final String notify[] = {"Dành cho người chơi trên 18 tuổi. Chơi quá 180 phút một ngày sẽ ảnh hưởng đến sức khỏe.",
         "Trò chơi không có bản quyền chính thức, hãy cân nhắc kỹ trước khi tham gia.",
         "Ngọc Rồng Online - Trang Chủ: https://ngocrongonline.com/"};

    private static ServerNotify instance;

    private ServerNotify() {
        this.notifies = new ArrayList<>();
        this.start();
    }

    public static ServerNotify gI() {
        if (instance == null) {
            instance = new ServerNotify();
        }
        return instance;
    }

    @Override
    public void run() {
        while (!Maintenance.isRunning) {
            try {
//                while (!notifies.isEmpty()) {
//                    sendThongBaoBenDuoi(notifies.remove(0));
//                }
                if (!notifies.isEmpty()) {
                    sendChatVip(notifies.remove(0));
                }
//                if (Util.canDoWithTime(this.lastNotifyTime, 360000)) {
//                    sendChatVip(notify[indexNotify]);
//                    this.lastNotifyTime = System.currentTimeMillis();
//                    indexNotify++;
//                    if (indexNotify >= notify.length) {
//                        indexNotify = 0;
//                    }
//                }
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void sendChatVip(String text) {
        Message msg;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            Service.gI().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void notify(String text) {
        this.notifies.add(text);
    }

    public void sendNotifyTab(Player player) {
        Message msg;
        try {
            msg = new Message(51);
            msg.writer().writeByte(Manager.NOTIFY.size());
            for (int i = 0; i < Manager.NOTIFY.size(); i++) {
                String[] arr = Manager.NOTIFY.get(i).split("<>");
                msg.writer().writeShort(i);
                msg.writer().writeUTF(arr[0]);
                msg.writer().writeUTF(arr[1]);
            }
            if (player.TamkjllPetGiong != -1) {
                String ttpet = "Name: " + player.TamkjllNamePet;
                ttpet += "\nLevel(" + (player.EmBeEXP * 100 / (3000000L + player.EmBeLv * 1500000L))
                        + "%): "
                        + player.EmBeLv;
                ttpet += "\nGiống: " + player.LinhCanEmBe(player.TamkjllPetGiong);
                ttpet += "\nThức ăn: " + player.TamkjllPetHunger + "%";
                ttpet += "\nSức mạnh: " + Util.getFormatNumber(player.TamkjllPetPower);
                ttpet += "\nKĩ năng: " + player.KyNangEmBe(player.TamkjllPetGiong);
                ttpet += "\n\ncần 15 phút để load hoặc thoát game ra vào lại";
                msg.writer().writeShort(2);
                msg.writer().writeUTF("Thông tin Pet");
                msg.writer().writeUTF(ttpet);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ignored) {
        }
    }
}
