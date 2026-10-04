package services;

import item.Item;
import item.Item.ItemOption;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jdbc.DBConnecter;
import jdbc.daos.PlayerDAO;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import player.Player;
import utils.Logger;
import utils.Util;

/**
 * Phúc Lợi - port từ bản src (nro/services/PhucLoi.java).
 *
 * - Client mở bảng -> cmd 103; bấm nhận 1 mốc -> cmd 105 (kèm id mốc).
 * - Dữ liệu: bảng phuc_loi (tab) + phuc_loi_tab (mốc), xem Sql/phuc_loi.sql.
 *
 * @author Hoàng Việt - 0857853150
 */
public class PhucLoi {

    public static final int CMD_SEND = 103;
    public static final int CMD_ACTIVE = 105;

    /** Các tab, nạp từ bảng phuc_loi. */
    public static final List<PhucLoiManager> TABS = new ArrayList<>();

    /** Các mốc thưởng, nạp từ bảng phuc_loi_tab, xếp theo id. */
    public static final List<PhucLoi> TEMPLATES = new ArrayList<>();

    private int id;
    private int tab_id;
    private String name;
    private int max_count;
    private byte active;
    private final List<Item> items = new ArrayList<>();

    private static PhucLoi i;

    public static PhucLoi gI() {
        if (i == null) {
            i = new PhucLoi();
        }
        return i;
    }

    // ------------------------------------------------------------------ gửi bảng

    public void sendPhucLoi(Player pl) {
        checkNhan(pl);
        try {
            Message msg = new Message(CMD_SEND);
            msg.writer().writeByte(1);
            msg.writer().writeByte(TABS.size());
            for (PhucLoiManager tab : TABS) {
                msg.writer().writeUTF(tab.tab_name);
                // client cấp mảng mốc theo số này, nên gửi số mốc thật của tab (không dùng cột max_tab)
                msg.writer().writeInt(countTemplates(tab.id_tab));
                msg.writer().writeInt(tab.id_tab);
                msg.writer().writeUTF(tab.info_phucloi);
                msg.writer().writeInt(tab.action);
                msg.writer().writeUTF(tab.tichLuy);
                msg.writer().writeByte(TEMPLATES.size());
                for (int k = 0; k < TEMPLATES.size(); k++) {
                    PhucLoi t = TEMPLATES.get(k);
                    msg.writer().writeInt(pl.checkNhan[k]);
                    msg.writer().writeInt(t.tab_id);
                    if (t.tab_id != tab.id_tab) {
                        continue;
                    }
                    msg.writer().writeInt(t.tab_id);
                    msg.writer().writeInt(t.id);
                    msg.writer().writeUTF(tenHienMua(t));
                    msg.writer().writeInt(t.max_count);
                    msg.writer().writeByte(t.active);
                    msg.writer().writeInt(count(pl, t));
                    msg.writer().writeInt(t.items.size());
                    for (Item item : t.items) {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());
                        List<ItemOption> options = item.getDisplayOptions();
                        msg.writer().writeByte(options.size());
                        for (ItemOption o : options) {
                            msg.writer().writeByte(o.optionTemplate.id);
                            msg.writer().writeInt((int) o.param);
                        }
                    }
                }
            }
            pl.sendMessage(msg);
        } catch (IOException e) {
            Logger.error("Gửi bảng Phúc Lợi lỗi: " + e.getMessage());
        }
    }

    /** Đánh dấu mốc người chơi đã nhận, dựa vào dữ liệu đã lưu của nhân vật. */
    private void checkNhan(Player pl) {
        pl.checkNhan = new int[TEMPLATES.size()];
        for (int k = 0; k < TEMPLATES.size(); k++) {
            int idMoc = TEMPLATES.get(k).id;
            if (pl.listNhan.contains(idMoc) || pl.listOnline.contains(idMoc) || pl.listDiemDanh.contains(idMoc)) {
                pl.checkNhan[k] = 1;
            }
        }
    }

    /** Tab chua moc nay (null neu khong tim thay). */
    private PhucLoiManager tabOf(PhucLoi t) {
        for (PhucLoiManager tab : TABS) {
            if (tab.id_tab == t.tab_id) {
                return tab;
            }
        }
        return null;
    }

    /** So tien te hien co: >0 = tong item trong han trang, -1 = vi Ngoc Xanh, -2 = vi Hong Ngoc. */
    private int countTien(Player pl, int currencyItem) {
        if (currencyItem == -1) {
            return pl.inventory.gem;
        }
        if (currencyItem == -2) {
            return pl.inventory.ruby;
        }
        int total = 0;
        for (Item it : pl.inventory.itemsBag) {
            if (it != null && it.isNotNullItem() && it.template.id == currencyItem) {
                total += it.quantity;
            }
        }
        return total;
    }

    /** Tru item tien te trong han trang, tru dan qua cac stack. Tra ve false neu thieu. */
    private boolean truTienBag(Player pl, int tempId, int soLuong) {
        List<Item> bag = pl.inventory.itemsBag;
        for (int i = 0; i < bag.size() && soLuong > 0; i++) {
            Item it = bag.get(i);
            if (it == null || !it.isNotNullItem() || it.template.id != tempId) {
                continue;
            }
            int tru = Math.min(it.quantity, soLuong);
            it.quantity -= tru;
            soLuong -= tru;
            if (it.quantity <= 0) {
                InventoryService.gI().removeItemBag(pl, it);
            }
        }
        return soLuong <= 0;
    }

    /** Tien te cua tab: 0 = Coin, -1 = Ngoc Xanh (vi), -2 = Hong Ngoc (vi), >0 = ten item trong han trang. */
    private static String tenTien(int currencyItem) {
        if (currencyItem == 0) {
            return "Coin";
        }
        if (currencyItem == -1) {
            return "Ngọc Xanh";
        }
        if (currencyItem == -2) {
            return "Hồng Ngọc";
        }
        try {
            for (models.Template.ItemTemplate t : server.Manager.ITEM_TEMPLATES) {
                if (t != null && (int) t.id == currencyItem) {
                    return t.name;
                }
            }
        } catch (Exception e) {
        }
        return "#" + currencyItem;
    }

    /**
     * Shop (active=1, tab >= 3): gan gia vao ten moc de user thay so tien phai tra
     * truoc khi bam Mua (client chi ve nut "Mua", khong ve gia).
     * Gia = max_count, tien te = currency cua tab (0 = Coin, -1/-2 = vi ngoc, >0 = item).
     */
    private String tenHienMua(PhucLoi t) {
        if (t.active != 1 || t.tab_id < 3) {
            return t.name;
        }
        PhucLoiManager tab = tabOf(t);
        int currency = tab == null ? 0 : tab.currencyItem;
        return t.name + " (Giá: " + Util.numberToMoney(t.max_count) + " " + tenTien(currency) + ")";
    }

    /**
     * Số để so với max_count: tab 0 = phút online, tab 1 = thứ trong tuần, tab 2 = coin nạp,
     * tab khai báo currency_item = so luong item do trong han trang, con lai = coin dang co.
     */
    private int count(Player pl, PhucLoi t) {
        PhucLoiManager tab = tabOf(t);
        if (tab != null && tab.currencyItem != 0) {
            return countTien(pl, tab.currencyItem);
        }
        switch (t.tab_id) {
            case 0:
                return pl.phutOnline;
            case 1:
                return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            case 2:
                return pl.getSession().tongnap;
            default:
                // tab id >= 3: moi tab Shop coin (gom ca 5 tab Shop tao moi tu panel/SQL)
                return pl.getSession().vnd;
        }
    }

    // ------------------------------------------------------------------ nhận thưởng

    /**
     * Client bấm nhận 1 mốc, gửi về id của mốc (xem client: PhucLoi.cs -> ActivePhucLoi).
     * id do client gui la id DB (sendPhucLoi gui t.id), KHONG phai vi tri trong TEMPLATES
     * -> phai tim theo id. Truoc day lay TEMPLATES.get(idMoc) (lay theo vi tri) nen khi
     * id thieu/gap (vd xoa moc id=14) bi lech 1 dong: mua SET Than Long nhan duoc 1 Thoi
     * Vang (dong id=16), mua Thẻ x2 nhan duoc Thẻ x3 (dong id=21), moc id cuoi bi bo qua.
     */
    public void claimReward(Player pl, int idMoc) {
        int viTri = -1;
        for (int k = 0; k < TEMPLATES.size(); k++) {
            if (TEMPLATES.get(k).id == idMoc) {
                viTri = k;
                break;
            }
        }
        if (viTri < 0) {
            return;
        }
        if (pl.checkNhan.length != TEMPLATES.size()) {
            checkNhan(pl);
        }
        PhucLoi t = TEMPLATES.get(viTri);
        if (t.active == 0 || t.active == 2) {
            claimFree(pl, t, viTri);
        } else if (t.tab_id >= 3) {
            claimCoin(pl, t);
        }
    }

    /** Mốc miễn phí: online, điểm danh hoặc mốc thường. */
    private void claimFree(Player pl, PhucLoi t, int viTri) {
        if (pl.checkNhan[viTri] == 1) {
            Service.gI().sendThongBao(pl, "|7|Bạn đã nhận rồi mà !!!");
            return;
        }
        int count = count(pl, t);
        // tab điểm danh so bằng đúng, các tab khác chỉ cần đủ
        if (t.tab_id == 1 ? count != t.max_count : count < t.max_count) {
            Service.gI().sendThongBao(pl, "|7|Không đủ điều kiện Nhận thưởng");
            return;
        }
        if (!conChoTrong(pl, t)) {
            Service.gI().sendThongBao(pl, "|7|Hành trang không đủ chổ trống");
            return;
        }
        if (t.tab_id == 1) {
            pl.listDiemDanh.add(t.id);
        } else if (t.tab_id == 0) {
            pl.listOnline.add(t.id);
        } else {
            pl.listNhan.add(t.id);
        }
        nhanItem(pl, t);
    }

    /**
     * Moc mua bang tien te: tru tien moi lan mua, mua lai khong gioi han.
     * Tien te = currency_item cua tab (0 = Coin, >0 = item trong han trang).
     */
    private void claimCoin(Player pl, PhucLoi t) {
        PhucLoiManager tab = tabOf(t);
        int currency = tab == null ? 0 : tab.currencyItem;
        String tien = tenTien(currency);
        int count = count(pl, t);
        if (count < t.max_count) {
            Service.gI().sendThongBao(pl, "|7|Còn thiếu " + (t.max_count - count) + " " + tien);
            return;
        }
        if (!conChoTrong(pl, t)) {
            Service.gI().sendThongBao(pl, "|7|Hành trang không đủ chổ trống");
            return;
        }
        boolean coHang = false;
        for (Item item : t.items) {
            if (item != null && item.isNotNullItem() && item.quantity > 0) {
                coHang = true;
                break;
            }
        }
        if (!coHang) {
            Service.gI().sendThongBao(pl, "|7|Mốc quà này đang hết hàng (số lượng = 0)");
            return;
        }
        if (currency == -1 || currency == -2) {
            // vi tien Ngoc Xanh / Hong Ngoc
            int vi = currency == -1 ? pl.inventory.gem : pl.inventory.ruby;
            if (vi < t.max_count) {
                Service.gI().sendThongBao(pl, "|7|Không đủ " + tien);
                return;
            }
            if (currency == -1) {
                pl.inventory.gem -= t.max_count;
            } else {
                pl.inventory.ruby -= t.max_count;
            }
            Service.gI().sendMoney(pl);
        } else if (currency > 0) {
            // tra bang item trong han trang (tru dan qua cac stack)
            if (!truTienBag(pl, currency, t.max_count)) {
                Service.gI().sendThongBao(pl, "|7|Không đủ " + tien);
                return;
            }
        } else {
            // tra bang Coin
            if (!PlayerDAO.checkVnd(pl, t.max_count)) {
                Service.gI().sendThongBao(pl, "|7|Không đủ Coin");
                return;
            }
            PlayerDAO.subVnd(pl, t.max_count);
        }
        nhanItem(pl, t);
    }

    /** Giống bản src: phải còn NHIỀU ô trống hơn số item của mốc, tức là dư 1 ô. */
    private boolean conChoTrong(Player pl, PhucLoi t) {
        return t.items.size() < InventoryService.gI().getCountEmptyBag(pl);
    }

    private void nhanItem(Player pl, PhucLoi t) {
        for (Item item : t.items) {
            if (item == null || !item.isNotNullItem() || item.quantity <= 0) {
                continue;
            }
            // PHAI BAN BAN SAO: addItemBag se set quantity=0 tren item truyen vao.
            // Neu truyen truc tiep template cua moc thi moc bi xoa so luong
            // -> lan sau nhan duoc 0 ma van bi tru tien.
            Item give = ItemService.gI().copyItem(item);
            // addItemBag se dat quantity = 0 len item sau khi nap vao túi/ví
            // -> phay lay so luong TRUOC khi goi, khong hien x0 o thong bao
            int soLuongNhan = give.quantity;
            InventoryService.gI().addItemBag(pl, give, -333);
            Service.gI().sendThongBao(pl, "|2|Đã nhận x" + soLuongNhan + " " + give.template.name + "\n");
        }
        InventoryService.gI().sendItemBag(pl);
        sendPhucLoi(pl);
    }

    // ------------------------------------------------------------------ nạp dữ liệu

    /** Nạp lại cả 2 bảng, dùng khi admin đổi cấu hình mà không muốn restart server. */
    public void reload() {
        loadTabs();
        loadTemplates();
    }

    private int countTemplates(int idTab) {
        int count = 0;
        for (PhucLoi t : TEMPLATES) {
            if (t.tab_id == idTab) {
                count++;
            }
        }
        return count;
    }

    private void loadTabs() {
        TABS.clear();
        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("select * from phuc_loi order by id");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhucLoiManager tab = new PhucLoiManager();
                tab.tab_name = rs.getString("name");
                tab.max_tab = rs.getInt("max_tab");
                tab.id_tab = rs.getInt("id_tab");
                tab.info_phucloi = rs.getString("info_phucloi");
                tab.action = rs.getInt("action");
                tab.tichLuy = rs.getString("tich_luy");
                try {
                    tab.currencyItem = rs.getInt("currency_item");
                } catch (Exception e) {
                    tab.currencyItem = 0; // chua ALTER bang thi tra tien bang Coin
                }
                TABS.add(tab);
            }
            Logger.success("Load phuc_loi thành công (" + TABS.size() + ")");
        } catch (SQLException e) {
            Logger.logException(PhucLoi.class, e, "Lỗi load bảng phuc_loi");
        }
    }

    private void loadTemplates() {
        TEMPLATES.clear();
        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement("select * from phuc_loi_tab order by id");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhucLoi t = new PhucLoi();
                t.id = rs.getInt("id");
                t.tab_id = rs.getInt("tab_id");
                t.name = rs.getString("name");
                t.max_count = rs.getInt("max_count");
                t.active = rs.getByte("active");
                try {
                    readItems(t, rs.getString("list_item"));
                    TEMPLATES.add(t);
                } catch (Exception e) {
                    // 1 dòng lỗi không làm mất cả bảng
                    Logger.error("Mốc Phúc Lợi id=" + t.id + " sai dữ liệu: " + e.getMessage());
                }
            }
            if (TEMPLATES.size() > 127) {
                Logger.error("phuc_loi_tab co " + TEMPLATES.size() + " moc, vuot qua 127 - client doc duoi dang byte se loi giao dien Phuc Loi!");
            }
            Logger.success("Load phuc_loi_tab thành công (" + TEMPLATES.size() + ")");
        } catch (SQLException e) {
            Logger.logException(PhucLoi.class, e, "Lỗi load bảng phuc_loi_tab");
        }
    }

    /** Cột list_item là mảng JSON: [{"id":15,"quantity":1,"options":[{"id":0,"param":10}]}] */
    private void readItems(PhucLoi t, String json) {
        for (Object element : (JSONArray) JSONValue.parse(json)) {
            JSONObject data = (JSONObject) JSONValue.parse(String.valueOf(element));
            Item item = ItemService.gI().createNewItem(Integer.parseInt(String.valueOf(data.get("id"))),
                    Integer.parseInt(String.valueOf(data.get("quantity"))));
            for (Object elementOption : (JSONArray) data.get("options")) {
                JSONObject option = (JSONObject) elementOption;
                int optionId = Integer.parseInt(String.valueOf(option.get("id")));
                long optionParam = Long.parseLong(String.valueOf(option.get("param")));
                item.itemOptions.add(new ItemOption(optionId, optionParam));
            }
            t.items.add(item);
        }
    }

}
