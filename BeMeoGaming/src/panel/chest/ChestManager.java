package panel.chest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import item.Item;
import player.Player;
import utils.Logger;

/**
 * ChestManager - ruong (box) do admin tao tren panel "Tao Ruong".
 * Du lieu luu trong DB: panel_chest (1 dong = 1 ruong = 1 item_template moi)
 * + panel_chest_entry (noi dung: vat pham, so luong, ty le).
 *
 * Mo ruong (UseItem goi truoc switch) -> roll ty le -> nhan vat pham -> tru 1 ruong.
 *
 * MODE (cot mode trong panel_chest):
 *  - 0 (mac dinh): chon DUNG 1 mon theo trong so ty le. Rate la trong so, KHONG can tong = 100
 *    (he thong tu chuan hoa). Vd: 70/30 hay 7/3 deu duoc.
 *  - 1: moi mon roll DOC LAP theo % (rate = %, 100 = chac chan nhan). Co the nhan nhieu mon
 *    hoac khong nhan gi (van tru ruong).
 *
 * Try-catch toan bo, khong bao gio crash game loop. Tieng Viet khong dau.
 */
public class ChestManager {

    public static class Entry {
        public int id;      // id dong panel_chest_entry (de panel sua/xoa)
        public int tempId;  // vat pham nhan duoc
        public int qty;     // so luong
        public double rate; // trong so (mode0) hoac % (mode1)
        public Entry(int id, int tempId, int qty, double rate) {
            this.id = id; this.tempId = tempId; this.qty = qty; this.rate = rate;
        }
    }

    public static class Chest {
        public int itemId;                 // item_template id cua ruong
        public String name;
        public int mode;                   // 0 = chon 1 theo trong so, 1 = roll doc lap theo %
        public String note;
        public List<Entry> entries = new ArrayList<>();
    }

    private static ChestManager instance;

    public static ChestManager gI() {
        if (instance == null) {
            synchronized (ChestManager.class) {
                if (instance == null) instance = new ChestManager();
            }
        }
        return instance;
    }

    private ChestManager() {
        load();
    }

    private final Map<Integer, Chest> chests = new ConcurrentHashMap<>();

    /** Tao bang neu chua co - an toan, chay lai duoc. */
    public static void ensureTables() {
        try (Connection con = jdbc.DBConnecter.getConnectionServer(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS panel_chest ("
                    + "item_id INT NOT NULL PRIMARY KEY, "
                    + "name VARCHAR(64) NOT NULL, "
                    + "mode TINYINT NOT NULL DEFAULT 0, "
                    + "note VARCHAR(255) NOT NULL DEFAULT '', "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            st.execute("CREATE TABLE IF NOT EXISTS panel_chest_entry ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "chest_id INT NOT NULL, "
                    + "temp_id INT NOT NULL, "
                    + "qty INT NOT NULL DEFAULT 1, "
                    + "rate DOUBLE NOT NULL DEFAULT 1, "
                    + "KEY idx_chest (chest_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        } catch (Exception e) {
            try { Logger.log("ChestManager ensureTables loi: " + e); } catch (Exception ex) {}
        }
    }

    /** Doc toan bo ruong + noi dung tu DB vao RAM. Goi sau moi thay doi tu panel. */
    public synchronized void load() {
        try {
            ensureTables();
            Map<Integer, Chest> tmp = new LinkedHashMap<>();
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("SELECT item_id, name, mode, note FROM panel_chest");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Chest c = new Chest();
                    c.itemId = rs.getInt("item_id");
                    c.name = rs.getString("name");
                    c.mode = rs.getInt("mode");
                    c.note = rs.getString("note");
                    tmp.put(c.itemId, c);
                }
            }
            try (Connection con = jdbc.DBConnecter.getConnectionServer();
                 PreparedStatement ps = con.prepareStatement("SELECT id, chest_id, temp_id, qty, rate FROM panel_chest_entry ORDER BY id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Chest c = tmp.get(rs.getInt("chest_id"));
                    if (c == null) continue;
                    c.entries.add(new Entry(rs.getInt("id"), rs.getInt("temp_id"),
                            rs.getInt("qty"), rs.getDouble("rate")));
                }
            }
            chests.clear();
            chests.putAll(tmp);
        } catch (Exception e) {
            try { Logger.log("ChestManager load loi: " + e); } catch (Exception ex) {}
        }
    }

    public boolean isChest(int tempId) {
        return chests.containsKey(tempId);
    }

    public Map<Integer, Chest> all() {
        return chests;
    }

    /**
     * Mo ruong. Tra ve true = da xu ly xong (UseItem phai return, khong vao switch nua).
     * Tra ve false = khong phai ruong cua panel -> chay logic cu binh thuong.
     */
    public boolean open(Player pl, Item item, int indexBag) {
        try {
            if (pl == null || item == null || !item.isNotNullItem() || item.template == null) return false;
            Chest c = chests.get((int) item.template.id);
            if (c == null) return false;
            if (c.entries.isEmpty()) {
                services.Service.gI().sendThongBao(pl, "Ruong nay dang trong!");
                return true;
            }
            if (services.InventoryService.gI().getCountEmptyBag(pl) <= 0) {
                services.Service.gI().sendThongBao(pl, "Hanh trang ban can it nhat 1 o trong de mo ruong");
                return true;
            }

            List<Entry> hits = new ArrayList<>();
            if (c.mode == 1) {
                // Moi mon roll doc lap theo % (0-100)
                for (Entry en : c.entries) {
                    if (en.rate >= 100 || Math.random() * 100.0 < en.rate) hits.add(en);
                }
            } else {
                // Chon dung 1 mon theo trong so ty le (tu chuan hoa)
                double total = 0;
                for (Entry en : c.entries) total += Math.max(0, en.rate);
                if (total <= 0) {
                    services.Service.gI().sendThongBao(pl, "Ty le ruong chua duoc cau hinh (tong = 0)!");
                    return true;
                }
                double roll = Math.random() * total;
                double acc = 0;
                Entry pick = null;
                for (Entry en : c.entries) {
                    double w = Math.max(0, en.rate);
                    if (w <= 0) continue;
                    acc += w;
                    if (roll < acc) { pick = en; break; }
                }
                if (pick == null) {
                    for (int i = c.entries.size() - 1; i >= 0; i--) {
                        if (c.entries.get(i).rate > 0) { pick = c.entries.get(i); break; }
                    }
                }
                if (pick == null) {
                    services.Service.gI().sendThongBao(pl, "Ty le ruong chua duoc cau hinh (tong = 0)!");
                    return true;
                }
                hits.add(pick);
            }

            int soNhan = 0, thatBai = 0;
            StringBuilder sb = new StringBuilder("Ban nhan duoc:");
            for (Entry en : hits) {
                try {
                    Item gift = services.ItemService.gI().createNewItem(en.tempId, Math.max(1, en.qty));
                    if (gift == null || gift.template == null) { thatBai++; continue; }
                    services.InventoryService.gI().addItemBag(pl, gift, 999999);
                    soNhan++;
                    sb.append(" ").append(gift.template.name).append(" x").append(Math.max(1, en.qty)).append(",");
                } catch (Exception ex) { thatBai++; }
            }

            // Tru 1 ruong
            try { services.InventoryService.gI().subQuantityItemsBag(pl, item, 1); } catch (Exception ex) {}
            try { services.InventoryService.gI().sendItemBag(pl); } catch (Exception ex) {}
            try { models.Combine.CombineService.gI().sendEffectOpenItem(pl, item.template.iconID, item.template.iconID); } catch (Exception ex) {}

            if (soNhan > 0) {
                if (thatBai > 0) sb.append(" (khong du cho cho ").append(thatBai).append(" mon)");
                try { services.Service.gI().sendThongBao(pl, sb.toString()); } catch (Exception ex) {}
            } else if (c.mode == 1) {
                try { services.Service.gI().sendThongBao(pl, "Chuc ban may man lan sau! (lan nay khong trung mon nao)"); } catch (Exception ex) {}
            } else {
                try { services.Service.gI().sendThongBao(pl, "Mo ruong that bai, ban khong nhan duoc gi."); } catch (Exception ex) {}
            }
            return true;
        } catch (Exception e) {
            try { Logger.logException(ChestManager.class, e, "Mo ruong loi"); } catch (Exception ex) {}
            return true;
        }
    }
}
