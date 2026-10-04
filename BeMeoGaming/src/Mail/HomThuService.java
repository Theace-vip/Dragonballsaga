package Mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import item.Item;
import network.Message;
import player.Player;
import server.Client;
import jdbc.daos.PlayerDAO;
import npc.NpcFactory;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;

/**
 *
 * @author RAIS
 */
public class HomThuService {

    private static HomThuService ins;

    public static HomThuService gI() {
        if (ins == null) {
            ins = new HomThuService();
        }
        return ins;
    }
    private final short maxMail = 50;
    public static final int XOA_THU = 0;
    public static final int XOA_DA_DOC = 1;
    public static final int XOA_ALL = 2;
    public static final int MAIL_HUB = 1102026;
    public static final int MAIL_DELETE_ALL = 1102027;

    public void readMsg(Message msg, Player player) {
        try {
            int type = msg.reader().readInt();
            switch (type) {
                case 1 ->
                    sendListMail(player);
                case 3 ->
                    seenMail(player, msg.reader().readInt());
                case 4 ->
                    receiveItemThu(player, msg.reader().readInt());
                case 5 ->
                    remove(player, msg.reader().readInt());
                case 6 ->
                    receiveAllItems(player);
                default -> {
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    private void ensureBox(Player player) {
        try { if (player.homThu == null) player.homThu = new ArrayList<>(); } catch (Exception e) {}
    }

    private void saveBox(Player player) {
        try { PlayerDAO.updatePlayer(player); } catch (Exception e) {}
    }

    private boolean isValidIndex(Player player, int index) {
        try { return player != null && player.homThu != null && index >= 0 && index < player.homThu.size(); } catch (Exception e) { return false; }
    }

    // Hub qua o NPC: moi thu co qua la 1 nut, them Nhan Tat Ca + Mo Hom Thu + Dong.
    // Luu List<Thu> vao PLAYERID_OBJECT de chiu duoc lech index khi box doi.
    public void openMailHub(Player player) {
        try { ensureBox(player); } catch (Exception e) {}
        try {
            if (player == null || player.homThu == null || player.homThu.isEmpty()) {
                try { Service.gI().sendThongBao(player, "Hom thu dang trong"); } catch (Exception ex) {}
                try { sendListMail(player); } catch (Exception ex) {}
                return;
            }
            java.util.List<Thu> gift = new java.util.ArrayList<>();
            try {
                for (Thu t : player.homThu) {
                    try {
                        if (t == null) continue;
                        if (Boolean.TRUE.equals(t.isNhan)) continue;
                        if (t.listItem == null || t.listItem.isEmpty()) continue;
                        gift.add(t);
                    } catch (Exception ex) {}
                }
            } catch (Exception ex) {}
            if (gift.isEmpty()) {
                try { sendListMail(player); } catch (Exception ex) {}
                return;
            }
            java.util.List<String> menus = new java.util.ArrayList<>();
            try {
                for (int i = 0; i < gift.size(); i++) {
                    Thu t = null;
                    try { t = gift.get(i); } catch (Exception ex) {}
                    String title = "Thu";
                    int n = 0;
                    try { title = t.title == null || t.title.isEmpty() ? "Thu" : t.title; } catch (Exception ex) {}
                    try { n = t.listItem == null ? 0 : t.listItem.size(); } catch (Exception ex) {}
                    try {
                        title = title.replace("\n", " ").trim();
                        if (title.length() > 28) title = title.substring(0, 28);
                    } catch (Exception ex) {}
                    menus.add("Nhan:\n" + title + " (" + n + ")");
                }
            } catch (Exception ex) {}
            menus.add("Nhan Tat Ca");
            menus.add("Mo Hom Thu");
            menus.add("Xoa Tat Ca");
            menus.add("Dong");
            String say = "";
            try { say = "|7|Hom thu: " + gift.size() + " thu co qua\n|6|Chon tung thu de nhan, hoac Nhan Tat Ca.\n|4|Tui day thi nhan dan, phan con lai giu trong thu."; } catch (Exception ex) {}
            try {
                String[] arr = menus.toArray(new String[0]);
                NpcService.gI().createMenuConMeo(player, MAIL_HUB, -1, say, arr, gift);
            } catch (Exception ex) {
                try { sendListMail(player); } catch (Exception e2) {}
            }
        } catch (Exception e) {
            try { sendListMail(player); } catch (Exception ex) {}
        }
    }

    public void handleHubConfirm(Player player, int select) {
        try { ensureBox(player); } catch (Exception e) {}
        try {
            if (player == null) return;
            Object o = null;
            try { o = NpcFactory.PLAYERID_OBJECT.get(player.id); } catch (Exception ex) {}
            java.util.List<Thu> gift = null;
            try { gift = (java.util.List<Thu>) o; } catch (Exception ex) { gift = null; }
            if (gift == null || gift.isEmpty()) {
                try { sendListMail(player); } catch (Exception ex) {}
                return;
            }
            int size = 0;
            try { size = player.homThu.size(); } catch (Exception ex) {}
            if (select < 0) return;
            if (select < gift.size()) {
                Thu target = null;
                try { target = gift.get(select); } catch (Exception ex) {}
                if (target == null) {
                    try { openMailHub(player); } catch (Exception ex) {}
                    return;
                }
                int realIdx = -1;
                try { realIdx = player.homThu.indexOf(target); } catch (Exception ex) {}
                if (realIdx < 0 || realIdx >= size) {
                    try { Service.gI().sendThongBao(player, "Thu da doi, mo lai hub"); } catch (Exception ex) {}
                    try { openMailHub(player); } catch (Exception ex) {}
                    return;
                }
                int cliIdx = 0;
                try { cliIdx = size - 1 - realIdx; } catch (Exception ex) { return; }
                try { receiveItemThu(player, cliIdx); } catch (Exception ex) {}
                return;
            }
            if (select == gift.size()) {
                try { receiveAllItems(player); } catch (Exception ex) {}
                return;
            }
            if (select == gift.size() + 1) {
                try { sendListMail(player); } catch (Exception ex) {}
                return;
            }
            if (select == gift.size() + 2) {
                // Xoa Tat Ca - hoi xac nhan truoc, xu ly bang logic removeMail(XOA_ALL) co san
                NpcService.gI().createMenuConMeo(player, MAIL_DELETE_ALL, -1,
                        "|4|XÓA TOÀN BỘ hòm thư?\n|7|Mọi thư sẽ bị xóa, bạn sẽ mất toàn bộ vật phẩm chưa nhận trong thư.",
                        "Xóa toàn bộ", "Không");
                return;
            }
            // Dong: khong lam gi
        } catch (Exception e) {}
    }

    public void remove(Player player, int index) {
        try { ensureBox(player); } catch (Exception e) {}
        try { index = player.homThu.size() - index - 1; } catch (Exception e) { return; }
        if (!isValidIndex(player, index)) {
            return;
        }
        player.selectMail = index;
        Thu selectedMail = null;
        try { selectedMail = player.homThu.get(index); } catch (Exception e) { return; }
        if (selectedMail == null) return;
        try { if (selectedMail.listItem == null) selectedMail.listItem = new ArrayList<>(); } catch (Exception e) {}
        boolean hasItems = false;
        try { hasItems = !selectedMail.listItem.isEmpty(); } catch (Exception e) {}
        boolean isNotReceived = true;
        try { isNotReceived = !Boolean.TRUE.equals(selectedMail.isNhan); } catch (Exception e) {}

        StringBuilder message = new StringBuilder("Bạn có muốn xóa thư hay không?\n");
        if (hasItems && isNotReceived) {
            message.append("Bạn sẽ bị mất vật phẩm trong thư");
        }
        NpcService.gI().createMenuConMeo(player, 1102025, -1, message.toString(), "Xóa thư", "Xóa thư\nđã đọc", "Xóa All", "Không");
    }

    public void removeMail(Player player, int option) {
        try { ensureBox(player); } catch (Exception e) {}
        if (player.homThu.isEmpty()) {
            Service.gI().sendThongBao(player, "Hòm thư của bạn đang trống");
            return;
        }
        switch (option) {
            case XOA_THU -> {
                if (player.selectMail >= 0 && player.selectMail < player.homThu.size()) {
                    player.homThu.remove(player.selectMail);
                    try { player.selectMail = -1; } catch (Exception e) {}
                    Service.gI().sendThongBao(player, "Xóa thư thành công!");
                } else {
                    Service.gI().sendThongBao(player, "Thư không hợp lệ!");
                }
            }
            case XOA_DA_DOC -> {
                // Chi xoa thu da nhan hoac (da xem + khong con item) - sua loi uu tien && ||
                player.homThu.removeIf(mail -> mail != null && (Boolean.TRUE.equals(mail.isNhan) || (Boolean.TRUE.equals(mail.isSeen) && (mail.listItem == null || mail.listItem.isEmpty()))));
                Service.gI().sendThongBao(player, "Đã xóa tất cả thư đã đọc (trừ thư đã nhận)");
            }
            case XOA_ALL -> {
                player.homThu.clear();
                Service.gI().sendThongBao(player, "Đã xóa tất cả thư");
            }

            default -> {
                sendListMail(player);
                return;
            }
        }
        try { saveBox(player); } catch (Exception e) {}
        sendListMail(player);
    }

    public void receiveItemThu(Player player, int index) {
        try { ensureBox(player); } catch (Exception e) {}
        try { index = player.homThu.size() - index - 1; } catch (Exception e) { return; }
        if (!isValidIndex(player, index)) {
            return;
        }

        Thu mail = null;
        try { mail = player.homThu.get(index); } catch (Exception e) { return; }
        if (mail == null) return;
        try { if (mail.listItem == null) mail.listItem = new ArrayList<>(); } catch (Exception e) {}
        if (mail.listItem.isEmpty()) {
            try { mail.isNhan = true; mail.isSeen = true; } catch (Exception e) {}
            Service.gI().sendThongBao(player, "Thu nay khong con vat pham");
            try { saveBox(player); } catch (Exception e) {}
            sendListMail(player);
            return;
        }

        // Nhan dan tung mon: mon nao vua tui thi lay, mon nao tran tui thi giu lai trong thu
        int total = 0;
        int got = 0;
        java.util.List<Item> keep = new java.util.ArrayList<>();
        try { total = mail.listItem.size(); } catch (Exception e) {}
        for (Item item : new java.util.ArrayList<>(mail.listItem)) {
            try {
                if (item == null || item.template == null) continue;
                boolean ok = false;
                try { ok = InventoryService.gI().addItemBag(player, item, 999999); } catch (Exception e) { ok = false; }
                if (ok) got++;
                else keep.add(item);
            } catch (Exception e) {
                try { keep.add(item); } catch (Exception ex) {}
            }
        }
        try { mail.listItem = keep; } catch (Exception e) {}
        try { mail.isSeen = true; } catch (Exception e) {}
        if (keep.isEmpty()) {
            try { mail.isNhan = true; } catch (Exception e) {}
            Service.gI().sendThongBao(player, "Nhan thanh cong " + got + "/" + total + " mon");
        } else {
            try { mail.isNhan = false; } catch (Exception e) {}
            Service.gI().sendThongBao(player, "Hanh trang day! Da nhan " + got + "/" + total + " mon, con " + keep.size() + " mon van nam trong thu. Don bot tui roi nhan tiep.");
        }

        try { saveBox(player); } catch (Exception e) {}
        try { InventoryService.gI().sendItemBag(player); } catch (Exception e) {}
        sendListMail(player);
    }

    public void receiveAllItems(Player player) {
        try { ensureBox(player); } catch (Exception e) {}
        if (player.homThu.isEmpty()) {
            Service.gI().sendThongBao(player, "Hòm thư của bạn đang trống");
            return;
        }
        int totalGot = 0;
        int totalKeep = 0;
        boolean changed = false;
        for (Thu mail : player.homThu) {
            try {
                if (mail == null) continue;
                if (mail.listItem == null) mail.listItem = new ArrayList<>();
                if (mail.listItem.isEmpty()) {
                    if (!Boolean.TRUE.equals(mail.isNhan)) { mail.isNhan = true; mail.isSeen = true; changed = true; }
                    continue;
                }
                java.util.List<Item> keep = new java.util.ArrayList<>();
                for (Item item : new java.util.ArrayList<>(mail.listItem)) {
                    try {
                        if (item == null || item.template == null) continue;
                        boolean ok = false;
                        try { ok = InventoryService.gI().addItemBag(player, item, 999999); } catch (Exception e) { ok = false; }
                        if (ok) { totalGot++; changed = true; }
                        else keep.add(item);
                    } catch (Exception e) {
                        try { keep.add(item); } catch (Exception ex) {}
                    }
                }
                try { mail.listItem = keep; } catch (Exception e) {}
                try { mail.isSeen = true; } catch (Exception e) {}
                if (keep.isEmpty()) {
                    try { mail.isNhan = true; } catch (Exception e) {}
                } else {
                    try { mail.isNhan = false; } catch (Exception e) {}
                    totalKeep += keep.size();
                }
                changed = true;
            } catch (Exception e) {}
        }
        try { InventoryService.gI().sendItemBag(player); } catch (Exception e) {}
        if (changed) { try { saveBox(player); } catch (Exception e) {} }
        if (totalKeep > 0) Service.gI().sendThongBao(player, "Da nhan " + totalGot + " mon, con " + totalKeep + " mon van nam trong thu do day tui.");
        else if (totalGot > 0) Service.gI().sendThongBao(player, "Nhan tat ca thanh cong (" + totalGot + " mon)!");
        sendListMail(player);
    }

    public boolean isMailboxFull(Player player) {
        try { if (player == null || player.homThu == null) return false; return player.homThu.size() >= maxMail; } catch (Exception e) { return false; }
    }

    public void seenMail(Player player, int select) {
        try { ensureBox(player); } catch (Exception e) {}
        try { select = player.homThu.size() - select - 1; } catch (Exception e) { return; }
        if (!isValidIndex(player, select)) {
            return;
        }

        try { player.homThu.get(select).isSeen = true; } catch (Exception e) {}
        try { saveBox(player); } catch (Exception e) {}
        sendListMail(player);
    }

    public void sendListMail(Player player) {
        try {
            if (player == null) return;
            try { ensureBox(player); } catch (Exception e) {}
            // Tạo tin nhắn gửi đi
            Message msg = new Message(-120);
            msg.writer().writeInt(1); // Loại tin nhắn
            int mailSize = 0;
            try { mailSize = player.homThu.size(); } catch (Exception e) { mailSize = 0; }
            msg.writer().writeInt(mailSize); // Số lượng thư
            msg.writer().writeShort(maxMail); // Giới hạn số thư

            // Duyệt qua danh sách thư
            for (int i = mailSize - 1; i >= 0; i--) {
                Thu mail = null;
                try { mail = player.homThu.get(i); } catch (Exception e) { continue; }
                if (mail == null) continue;
                try { if (mail.listItem == null) mail.listItem = new ArrayList<>(); } catch (Exception e) {}

                // Ghi thông tin cơ bản của thư vào Message
                msg.writer().writeUTF(mail.title != null ? mail.title : ""); // Tiêu đề
                String tm = "";
                try { tm = mail.getTime(); } catch (Exception e) { tm = ""; }
                msg.writer().writeUTF(tm == null ? "" : tm); // Thời gian
                msg.writer().writeUTF(mail.title2 != null ? mail.title2 : ""); // Tiêu đề phụ
                msg.writer().writeUTF(mail.note != null ? mail.note : ""); // Ghi chú
                msg.writer().writeUTF(mail.content != null ? mail.content : ""); // Nội dung
                msg.writer().writeBoolean(Boolean.TRUE.equals(mail.isNhan)); // Trạng thái nhận
                msg.writer().writeBoolean(Boolean.TRUE.equals(mail.isSeen)); // Trạng thái đã đọc

                // Lấy danh sách vật phẩm trong thư
                int itemSize = 0;
                try { itemSize = mail.listItem.size(); } catch (Exception e) { itemSize = 0; }
                msg.writer().writeInt(itemSize); // Số lượng vật phẩm

                // Duyệt qua danh sách vật phẩm trong thư
                for (int j = 0; j < itemSize; j++) {
                    Item item = null;
                    try { item = mail.listItem.get(j); } catch (Exception e) { continue; }
                    if (item == null || item.template == null) continue;

                    // Ghi thông tin vật phẩm
                    int icon = 0;
                    try { icon = item.template.iconID; } catch (Exception e) { icon = 0; }
                    msg.writer().writeInt(icon); // Icon ID
                    int qty = 1;
                    try { qty = item.quantity <= 0 ? 1 : item.quantity; } catch (Exception e) { qty = 1; }
                    msg.writer().writeInt(qty); // Số lượng
                }
            }

            // Gửi tin nhắn cho người chơi
            try { player.sendMessage(msg); } catch (Exception e) {}
            try { msg.cleanup(); } catch (Exception e) {}

        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi nếu có
        } catch (Exception e) {}
    }

    public void sendMail(Player player, Thu thu) {
        try {
            Message msg = new Message(-120);
            msg.writer().writeInt(2);

            player.sendMessage(msg);
        } catch (Exception e) {

        }
    }

    public void createMail(Player player, String mailName, String tieuDe, String noiDung) {
        Player rais = Client.gI().getPlayer(player.id);
        Thu thu = new Thu(mailName, tieuDe, "Được gửi bởi hệ thống", noiDung,
                System.currentTimeMillis(), false, false);
        int itemId = 0, quantity = 0;
        List<Item.ItemOption> options = new ArrayList<>();
        Item item = ItemService.gI().createNewItem((short) itemId);
        item.quantity = quantity;
        item.itemOptions = options;
        thu.listItem.add(item);
        rais.homThu.add(thu);
    }
}
