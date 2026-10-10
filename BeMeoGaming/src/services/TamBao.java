package services;

import item.Item;
import item.Item.ItemOption;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jdbc.DBConnecter;
import jdbc.daos.PlayerDAO;
import models.Template;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import player.Player;
import utils.Logger;
import utils.Util;

/**
 * Tầm Bảo - Vòng Quay (port từ bản src nro/services/TamBao.java).
 *
 * <p>
 * Client: cmd {@link #CMD_SEND} khi mở vòng quay, cmd {@link #CMD_ACTIVE} khi quay x1/x10 hoặc
 * nhận mốc thưởng. Giao thức giữ nguyên byte-by-byte so với bản src.
 *
 * <p>
 * Dữ liệu nạp từ SQL:
 * <ul>
 * <li>{@code tambao_items} - vật phẩm của vòng quay theo từng loại chìa (key_item_id), tỉ lệ
 * {@code tile_trung_thuong} tính theo %, option dạng compact {@code "30-1,77-50"}.</li>
 * <li>{@code moc_vong_quay} - mốc thưởng theo điểm quay (max_value = số điểm cần).</li>
 * <li>{@code history_tambao} - log từng lượt quay.</li>
 * </ul>
 *
 * <p>
 * Trạng thái nhân vật: cột {@code player.diem_quay} (số điểm) và {@code player.active_vong_quay}
 * (JSON mảng id mốc đã nhận), lưu riêng bằng {@link PlayerDAO#saveVongQuay(Player)}.
 *
 * <p>
 * Câu lệnh tạo bảng + dữ liệu mẫu: {@code Sql/vong_quay_tambao.sql}.
 *
 * @author Hoàng Việt - 0857853150
 */
public class TamBao {

    // ==================== GIAO THỨC CLIENT ====================
    /** Client mở vòng quay / xin lại danh sách mốc thưởng. */
    public static final int CMD_SEND = 106;

    /** Client quay hoặc nhận thưởng. */
    public static final int CMD_ACTIVE = 107;

    /** CMD_ACTIVE kèm byte này + id mốc = nhận thưởng. */
    public static final byte ACTION_CLAIM = 0;

    /** CMD_ACTIVE kèm byte này + số lượt = quay x1 / x10. */
    public static final byte ACTION_SPIN = 1;

    /** Số ô của vòng quay - client vẽ đúng 14 ô xếp thành vòng quanh khung. */
    public static final int SLOTS = 14;

    private static final byte SEND_POOL = 0;
    private static final byte SEND_MOC = 1;
    private static final byte SEND_WON_SLOTS = 2;

    /**
     * Client chỉ hiện icon "Vòng quay" và update/paint khi đọc được START == 1
     * (Scripts/Game1/BagVip/IconChucNang.cs) - đổi thành 0 nếu muốn đóng chức năng.
     */
    private static final byte START = 1;

    // ==================== CẤU HÌNH QUAY ====================
    /** type 9 = vàng: số lượng random và tô màu riêng trong thông báo. */
    private static final int TYPE_GOLD = 9;

    /** Vật phẩm này tô màu riêng trong thông báo (giống bản src). */
    private static final int SPECIAL_ITEM_ID = 457;

    private static final int GOLD_MIN = 1_000_000;
    private static final int GOLD_MAX = 20_000_000;

    /** Chìa quay dùng khi túi không có chìa nào đủ số lượt. */
    private static final int FALLBACK_KEY = 1874; // Key vang - vat pham dung de quay Tam bao

    /** Vật phẩm bù vào ô trống khi bảng tambao_items chưa đủ {@link #SLOTS} dòng. */
    private static final int[] FALLBACK_ITEM_IDS = {220, 221, 222, 223, 224, 15, 17, 18, 19, 20, 381, 382, 383, 384, 385};

    private static final double MIN_PERCENT = 0.0;
    private static final double MAX_PERCENT = 100.0;

    /** Option "Đã Khóa" - không tăng theo hệ số reset. */
    private static final int OPTION_LOCK = 30;

    /** Mốc cuối (2000 điểm): nhận xong thì reset vòng quay về đầu. */
    private static final int MOC_RESET_VALUE = 2000;

    /** Mốc 10-900: mỗi lần reset tăng 1.5 lần số lượng quà. */
    private static final double MOC_QTY_SCALE = 1.5;

    /** Mốc 1000-1700: mỗi lần reset cộng thêm 2000% vào các dòng chỉ số. */
    private static final int MOC_OPTION_SCALE = 2000;

    /** Mốc 2000: cứ 3 lần reset thì +1 Hộp quà. */
    private static final int MOC_BOX_RESETS = 3;

    // ==================== MÀU CHỮ THÔNG BÁO ====================
    private static final String COLOR_SUCCESS = "|2|";
    private static final String COLOR_GOLD = "|5|";
    private static final String COLOR_NORMAL = "|6|";
    private static final String COLOR_ERROR = "|7|";
    private static final String COLOR_SPECIAL = "|8|";

    // ==================== DỮ LIỆU NẠP TỪ SQL ====================
    /** Mốc thưởng theo điểm quay (bảng moc_vong_quay). */
    private final List<TamBaoItem> mocList = new ArrayList<>();

    /** Vật phẩm của từng loại chìa (bảng tambao_items, theo key_item_id). */
    private final Map<Integer, List<Item>> pools = new HashMap<>();

    /** Tỉ lệ trúng (%) của từng vật phẩm, song song với {@link #pools} - cho phép số thập phân (0.5%). */
    private final Map<Integer, List<Double>> poolRates = new HashMap<>();

    /** Bố cục 14 ô đã gửi cho client của lần mở vòng quay gần nhất, theo id nhân vật. */
    private final Map<Long, SpinPool> lastView = new ConcurrentHashMap<>();

    /** key_item_id mặc định - là loại chìa đầu tiên nạp được từ SQL. */
    private int defaultKeyId = -1;

    private static TamBao instance;

    public static TamBao gI() {
        if (instance == null) {
            instance = new TamBao();
        }
        return instance;
    }

    private TamBao() {
    }

    /** 14 ô đang hiện trên vòng quay kèm tỉ lệ tương ứng từng ô. */
    private static class SpinPool {

        final int keyId;
        final List<Item> items;
        final List<Double> rates;

        SpinPool(int keyId, List<Item> items, List<Double> rates) {
            this.keyId = keyId;
            this.items = items;
            this.rates = rates;
        }
    }

    // =========================================================
    // THÔNG TIN DỮ LIỆU (dùng cho panel admin)
    // =========================================================

    /** key_item_id đang dùng để quay. */
    public int getCurrentKeyId() {
        return defaultKeyId != -1 ? defaultKeyId : FALLBACK_KEY;
    }

    /** Tổng số vật phẩm đã nạp từ tambao_items. */
    public int getItemCount() {
        int count = 0;
        for (List<Item> pool : pools.values()) {
            count += pool.size();
        }
        return count;
    }

    /** Số loại chìa (pool) đã nạp từ tambao_items. */
    public int getPoolCount() {
        return pools.size();
    }

    /** Số mốc thưởng đã nạp từ moc_vong_quay. */
    public int getMocCount() {
        return mocList.size();
    }

    /** Số nhân vật đang được giữ bố cục vòng quay trong RAM. */
    public int getCachedViewCount() {
        return lastView.size();
    }

    // =========================================================
    // NẠP DỮ LIỆU
    // =========================================================

    /** Nạp lại pool vật phẩm và mốc thưởng (dùng khi admin sửa SQL mà không restart server). */
    public void reload() {
        loadPools();
        loadMocs();
    }

    /** Xoá bố cục vòng quay đã cache của một nhân vật (gọi khi nhân vật dispose). */
    public void clearCache(long playerId) {
        lastView.remove(playerId);
    }

    private void loadPools() {
        pools.clear();
        poolRates.clear();
        defaultKeyId = -1;

        final String sql = "SELECT id, key_item_id, item_id, quantity, item_options, tile_trung_thuong,"
                + " start_at, end_at, enabled FROM tambao_items"
                + " WHERE enabled = 1"
                + " AND (start_at IS NULL OR start_at <= NOW())"
                + " AND (end_at IS NULL OR end_at >= NOW())"
                + " ORDER BY id ASC";

        int rows = 0;
        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Integer templateId = readInt(rs, "item_id");
                Integer quantity = readInt(rs, "quantity");
                if (templateId == null || quantity == null) {
                    continue; // dòng cấu hình thiếu vật phẩm
                }
                rows++;

                int keyId = rs.getInt("key_item_id");
                Item item = createItemWithOptions(templateId, Math.max(1, quantity), rs.getString("item_options"));
                pools.computeIfAbsent(keyId, key -> new ArrayList<>()).add(item);
                poolRates.computeIfAbsent(keyId, key -> new ArrayList<>()).add(toPercent(rs.getDouble("tile_trung_thuong")));
            }
            if (!pools.isEmpty()) {
                // mặc định dùng pool "Key vàng" (1874); không có pool đó thì lấy key nhỏ nhất cho ổn định
                defaultKeyId = pools.containsKey(FALLBACK_KEY) ? FALLBACK_KEY : Collections.min(pools.keySet());
            }
            Logger.success("Load tambao_items thành công: " + rows + " dòng, " + pools.size() + " pool");
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Lỗi load bảng tambao_items (Tầm Bảo)");
        }
    }

    private void loadMocs() {
        mocList.clear();
        final String sql = "SELECT * FROM moc_vong_quay ORDER BY id";
        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TamBaoItem moc = new TamBaoItem();
                moc.id_moc = rs.getInt("id");
                moc.max_value = rs.getInt("max_value");
                moc.quantity = Math.max(1, rs.getInt("quantity"));
                moc.template = ItemService.gI().getTemplate(rs.getInt("item_id"));
                if (moc.template == null) {
                    Logger.error("Mốc Vòng Quay id=" + moc.id_moc + " trỏ tới vật phẩm không tồn tại");
                    continue;
                }
                try {
                    readOptions(moc, rs.getString("item_options"));
                    mocList.add(moc);
                } catch (Exception e) {
                    // một mốc sai dữ liệu không làm mất cả bảng
                    Logger.error("Mốc Vòng Quay id=" + moc.id_moc + " sai dữ liệu: " + e.getMessage());
                }
            }
            if (mocList.size() > 255) {
                Logger.error("moc_vong_quay có " + mocList.size() + " mốc, quá 255 - client đọc 1 byte sẽ hiển thị sai!");
            }
            Logger.success("Load moc_vong_quay thành công (" + mocList.size() + ")");
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Lỗi load bảng moc_vong_quay (Tầm Bảo)");
        }
    }

    /** Đọc cột số cho phép NULL - trả về null nếu cột trống. */
    private static Integer readInt(ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (Exception e) {
            return null;
        }
    }

    private Item createItemWithOptions(int templateId, int quantity, String compactOptions) {
        Item item = ItemService.gI().createNewItem(templateId, quantity);
        if (compactOptions != null && !compactOptions.trim().isEmpty()) {
            // "30-1,77-50" -> option (30,1) và (77,50)
            for (String option : compactOptions.split(",")) {
                String[] pair = option.trim().split("-");
                if (pair.length < 2) {
                    continue;
                }
                try {
                    item.itemOptions.add(new ItemOption(Integer.parseInt(pair[0].trim()),
                            Long.parseLong(pair[1].trim())));
                } catch (NumberFormatException e) {
                    Logger.error("Option Tầm Bảo sai định dạng: " + option);
                }
            }
        }
        item.info = item.getInfo();
        return item;
    }

    /** Option của mốc lưu dạng JSON: [{"id":30,"param":1}] hoặc [[30,1]] hoặc [30,1]. */
    private void readOptions(Item target, String json) {
        if (json == null || json.trim().isEmpty()) {
            return;
        }
        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return;
        }
        for (Object entry : (JSONArray) parsed) {
            readOption(entry, target);
        }
    }

    private void readOption(Object entry, Item target) {
        if (entry instanceof JSONObject) {
            JSONObject obj = (JSONObject) entry;
            target.itemOptions.add(new ItemOption(toInt(obj.get("id")), toInt(obj.get("param"))));
        } else if (entry instanceof JSONArray) {
            JSONArray pair = (JSONArray) entry;
            if (pair.size() >= 2) {
                target.itemOptions.add(new ItemOption(toInt(pair.get(0)), toInt(pair.get(1))));
            }
        } else if (entry instanceof String) {
            Object nested = JSONValue.parse((String) entry);
            if (nested != null && nested != entry) {
                readOption(nested, target);
            }
        }
    }

    private static int toInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // =========================================================
    // GỬI DỮ LIỆU CHO CLIENT
    // =========================================================

    /** Gửi 14 ô vật phẩm của vòng quay (cmd 106, status 0). */
    public void sendTamBao(Player pl) {
        if (pl == null) {
            return;
        }
        SpinPool pool = spinPoolOf(pl, getCurrentKeyId());
        logSentPool(pl, pool);

        Message msg = new Message(CMD_SEND);
        try {
            DataOutputStream out = msg.writer();
            out.writeByte(SEND_POOL);
            out.writeByte(START);
            out.writeShort(pool.keyId);
            out.writeShort(keyIconId(pool.keyId));
            out.writeByte(SLOTS);
            for (Item item : pool.items) {
                out.writeByte(0); // active_vip
                writeItemInfo(out, item);
                writeOptions(out, item);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Gửi vật phẩm vòng quay Tầm Bảo lỗi");
        } finally {
            msg.cleanup();
        }
    }

    /** Gửi điểm quay + danh sách mốc thưởng (cmd 106, status 1). */
    public void sendMocTamBao(Player pl) {
        if (pl == null) {
            return;
        }
        markReceived(pl);

        Message msg = new Message(CMD_SEND);
        try {
            DataOutputStream out = msg.writer();
            out.writeByte(SEND_MOC);
            out.writeInt(pl.diem_quay);
            out.writeByte(mocList.size());
            for (int i = 0; i < mocList.size(); i++) {
                TamBaoItem moc = mocList.get(i);
                out.writeInt(pl.checkNhan_TamBao[i]); // 1 = đã nhận
                writeItemInfo(out, moc);
                out.writeInt(moc.id_moc);
                out.writeInt(moc.max_value);
                writeOptions(out, moc);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Gửi mốc Tầm Bảo lỗi");
        } finally {
            msg.cleanup();
        }
    }

    /** Gửi danh sách ô vừa trúng để client vẽ khung sáng (cmd 106, status 2). */
    public void sendWonSlots(Player pl) {
        if (pl == null || pl.list_id_nhan == null) {
            return;
        }
        Message msg = new Message(CMD_SEND);
        try {
            DataOutputStream out = msg.writer();
            out.writeByte(SEND_WON_SLOTS);
            out.writeByte(pl.list_id_nhan.length);
            for (int won : pl.list_id_nhan) {
                out.writeInt(won);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Gửi ô trúng Tầm Bảo lỗi");
        } finally {
            msg.cleanup();
        }
    }

    /** Log 14 o vua gui cho client - dung de doi chieu khi client hien thi sai. */
    private void logSentPool(Player pl, SpinPool pool) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pool.items.size(); i++) {
            Item item = pool.items.get(i);
            sb.append(item.template.id).append('x').append(item.quantity)
                    .append('(').append(pool.rates.get(i)).append("%) ");
        }
        Logger.success("Gui vong quay Tam Bao cho " + pl.name + ": chia=" + pool.keyId
                + ", " + pool.items.size() + " o -> " + sb.toString().trim());
    }

    private void writeItemInfo(DataOutputStream out, Item item) throws IOException {
        out.writeShort(item.template.id);
        out.writeInt(item.quantity);
        out.writeUTF(item.getInfo());
        out.writeUTF(item.getContent());
    }

    private void writeOptions(DataOutputStream out, Item item) throws IOException {
        List<ItemOption> options = item.getDisplayOptions();
        out.writeByte(options.size());
        for (ItemOption option : options) {
            out.writeByte(option.optionTemplate.id);
            out.writeInt((int) option.param);
        }
    }

    private int keyIconId(int keyId) {
        Template.ItemTemplate template = ItemService.gI().getTemplate(keyId);
        return template == null ? 0 : template.iconID;
    }

    private String keyName(int keyId) {
        Template.ItemTemplate template = ItemService.gI().getTemplate(keyId);
        return template == null ? "chìa quay" : template.name;
    }

    // =========================================================
    // QUAY
    // =========================================================

    /** Quay x1 / x10 bằng loại chìa đang dùng. */
    public void quayTamBao(Player pl, int soLan) {
        if (pl == null || soLan <= 0) {
            return;
        }
        int poolKeyId = getCurrentKeyId();
        Item key = findUsableKey(pl, soLan, poolKeyId);
        if (key == null) {
            Service.gI().sendThongBao(pl, COLOR_ERROR + "Không đủ " + keyName(poolKeyId));
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < soLan) {
            Service.gI().sendThongBao(pl, COLOR_ERROR + "Hành trang cần ít nhất " + soLan + " chổ trống");
            return;
        }

        SpinPool pool = spinPoolOf(pl, key.template.id);
        int[] wonSlots = new int[SLOTS];
        List<String> received = new ArrayList<>(soLan);
        for (int turn = 0; turn < soLan; turn++) {
            int slot = pickSlot(pool.rates);
            Item prize = createPrize(pool.items.get(slot));
            // phải mô tả trước khi addItemBag: hàm này đưa item vào túi và set quantity = 0 lên item truyền vào
            received.add(describePrize(prize));
            wonSlots[slot] = 1;
            grant(pl, prize);
        }

        pl.list_id_nhan = wonSlots;
        pl.diem_quay += soLan;
        // đếm lượt quay cho bảng xếp hạng "Top Tầm Bảo" (server.Manager.queryTopTambao)
        pl.SukienTamBao += soLan;
        InventoryService.gI().subQuantityItemsBag(pl, key, soLan);
        PlayerDAO.saveVongQuay(pl);

        Service.gI().sendThongBaoFromAdmin(pl, COLOR_ERROR + "Nhận được\n" + String.join("\n", received));
        InventoryService.gI().sendItemBag(pl);
        sendMocTamBao(pl);
        sendWonSlots(pl);
    }

    /**
     * Chìa dùng cho lượt quay này: chìa của pool đang mở, nếu không đủ thì dùng chìa dự phòng.
     * Trả về null khi túi không có loại chìa nào đủ số lượt.
     */
    private Item findUsableKey(Player pl, int soLan, int poolKeyId) {
        Item key = findKey(pl, poolKeyId, soLan);
        if (key != null || poolKeyId == FALLBACK_KEY) {
            return key;
        }
        return findKey(pl, FALLBACK_KEY, soLan);
    }

    private Item findKey(Player pl, int keyId, int soLan) {
        Item key = InventoryService.gI().findItemBag(pl, keyId);
        return key != null && key.template != null && key.quantity >= soLan ? key : null;
    }

    /** Bố cục 14 ô đang hiện trên client; tạo mới nếu chưa có hoặc đổi loại chìa. */
    private SpinPool spinPoolOf(Player pl, int keyId) {
        SpinPool pool = lastView.get(pl.id);
        if (pool == null || pool.keyId != keyId || pool.items.size() != SLOTS) {
            pool = buildSpinPool(keyId);
            lastView.put(pl.id, pool);
        }
        return pool;
    }

    /**
     * Dựng đủ {@link #SLOTS} ô cho loại chìa: lấy vật phẩm từ SQL (nhiều hơn 14 thì random 14),
     * bù vật phẩm dự phòng cho đủ ô (tỉ lệ 0%) rồi trộn thứ tự để mỗi lần mở là một bố cục khác.
     */
    private SpinPool buildSpinPool(int keyId) {
        List<Item> sourceItems = pools.getOrDefault(keyId, Collections.emptyList());
        List<Double> sourceRates = poolRates.getOrDefault(keyId, Collections.emptyList());

        List<Item> items = new ArrayList<>(SLOTS);
        List<Double> rates = new ArrayList<>(SLOTS);

        List<Integer> order = shuffledIndexes(sourceItems.size());
        for (int i = 0; i < Math.min(SLOTS, order.size()); i++) {
            int index = order.get(i);
            items.add(sourceItems.get(index));
            rates.add(toPercent(index < sourceRates.size() ? sourceRates.get(index) : 0.0));
        }

        List<Integer> fallbackIds = new ArrayList<>();
        for (int id : FALLBACK_ITEM_IDS) {
            fallbackIds.add(id);
        }
        Collections.shuffle(fallbackIds);
        while (items.size() < SLOTS) {
            int slot = items.size();
            items.add(ItemService.gI().createNewItem(fallbackIds.get(slot % fallbackIds.size()), 1));
            rates.add(MIN_PERCENT);
        }

        List<Item> shuffledItems = new ArrayList<>(SLOTS);
        List<Double> shuffledRates = new ArrayList<>(SLOTS);
        for (int index : shuffledIndexes(SLOTS)) {
            shuffledItems.add(items.get(index));
            shuffledRates.add(rates.get(index));
        }
        return new SpinPool(keyId, shuffledItems, shuffledRates);
    }

    private List<Integer> shuffledIndexes(int size) {
        List<Integer> indexes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        return indexes;
    }

    /**
     * Quay theo tỉ lệ: ô có tỉ lệ > 0 nhận đúng phần trăm của nó. Tổng > 100 thì chuẩn hoá về 100,
     * tổng < 100 thì chia phần dư cho các ô 0% (thường là ô bù dự phòng), tất cả đều 0 thì chia đều.
     */
    private int pickSlot(List<Double> rates) {
        int size = rates.size();
        if (size == 0) {
            return 0;
        }

        double[] weights = new double[size];
        double totalPercent = 0;
        int zeroSlots = 0;
        for (int i = 0; i < size; i++) {
            double rate = toPercent(rates.get(i));
            weights[i] = rate;
            totalPercent += rate;
            if (rate == 0) {
                zeroSlots++;
            }
        }

        if (totalPercent == 0) {
            Arrays.fill(weights, 100.0 / size);
        } else if (totalPercent < MAX_PERCENT && zeroSlots > 0) {
            spreadRemainder(weights, (MAX_PERCENT - totalPercent) / zeroSlots);
        } else if (totalPercent != MAX_PERCENT) {
            normalize(weights, totalPercent);
        }

        double total = 0;
        for (double weight : weights) {
            total += weight;
        }
        double random = Math.random() * total;
        double accumulated = 0;
        for (int i = 0; i < size; i++) {
            accumulated += weights[i];
            if (random < accumulated) {
                return i;
            }
        }
        return size - 1;
    }

    /** Chia phần trăm dư cho các ô chưa có tỉ lệ (ô bù dự phòng). */
    private void spreadRemainder(double[] weights, double bonus) {
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] == 0) {
                weights[i] = bonus;
            }
        }
    }

    /** Đưa tổng tỉ lệ về đúng 100%. */
    private void normalize(double[] weights, double totalPercent) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] / totalPercent * MAX_PERCENT;
        }
    }

    /** Bản sao vật phẩm kèm số lượng: vàng thì random số lượng, các loại khác giữ nguyên. */
    private Item createPrize(Item base) {
        int quantity = base.template.type == TYPE_GOLD ? Util.nextInt(GOLD_MIN, GOLD_MAX) : base.quantity;
        Item prize = ItemService.gI().createNewItem(base.template.id, quantity);
        for (ItemOption option : base.itemOptions) {
            prize.itemOptions.add(new ItemOption(option.optionTemplate.id, option.param));
        }
        prize.info = prize.getInfo();
        return prize;
    }

    private String describePrize(Item prize) {
        return colorOf(prize) + "x" + Util.format(prize.quantity) + " " + prize.template.name;
    }

    private String colorOf(Item item) {
        if (item.template.type == TYPE_GOLD) {
            return COLOR_GOLD;
        }
        if (item.template.id == SPECIAL_ITEM_ID) {
            return COLOR_SPECIAL;
        }
        return COLOR_NORMAL;
    }

    /** Đưa vật phẩm vào túi và ghi log quay. */
    private void grant(Player pl, Item prize) {
        String history = itemToJson(prize);
        InventoryService.gI().addItemBag(pl, prize, -333);
        insertHistory(pl.id, history);
    }

    // =========================================================
    // MỐC THƯỞNG
    // =========================================================

    /** Nhận thưởng một mốc; id mốc là id trong bảng moc_vong_quay mà client nhận được ở cmd 106. */
    public void nhanMocThuong(Player pl, int idMoc) {
        if (pl == null) {
            return;
        }
        int index = indexOfMoc(idMoc);
        if (index == -1) {
            return;
        }
        TamBaoItem moc = mocList.get(index);
        markReceived(pl);
        if (pl.checkNhan_TamBao[index] == 1) {
            Service.gI().sendThongBao(pl, COLOR_ERROR + "Bạn đã nhận rồi mà !!!");
            return;
        }
        if (pl.diem_quay < moc.max_value) {
            Service.gI().sendThongBao(pl, COLOR_ERROR + "Không đủ điều kiện Nhận thưởng");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
            Service.gI().sendThongBao(pl, COLOR_ERROR + "Hành trang không đủ chổ trống");
            return;
        }

        // mốc cuối: nhận xong thì reset vòng quay, quà các vòng sau tăng theo số lần đã reset
        final int resetCount = Math.max(0, pl.reset_vong_quay);
        final boolean lastMoc = moc.max_value >= MOC_RESET_VALUE;
        if (lastMoc) {
            pl.listNhan_TamBao.clear();
            pl.diem_quay = 0;
            pl.reset_vong_quay = resetCount + 1;
        } else {
            pl.listNhan_TamBao.add(moc.id_moc);
        }
        PlayerDAO.saveVongQuay(pl);

        // phải dùng bản sao: addItemBag set quantity = 0 trên item truyền vào
        Item reward = ItemService.gI().copyItem(moc);
        reward.quantity = scaledQuantity(reward.quantity, moc.max_value, resetCount);
        applyResetScale(reward, moc.max_value, resetCount);
        int quantity = reward.quantity;
        InventoryService.gI().addItemBag(pl, reward, -333);
        Service.gI().sendThongBao(pl, COLOR_SUCCESS + "Đã nhận x" + quantity + " " + moc.template.name + "\n");
        if (lastMoc) {
            Service.gI().sendThongBaoFromAdmin(pl, COLOR_GOLD + "Vòng quay Tầm Bảo đã RESET lần " + pl.reset_vong_quay
                    + "!\nQuà mốc 10-900 x" + MOC_QTY_SCALE + ", mốc 1000-1700 +" + MOC_OPTION_SCALE
                    + "% chỉ số, mốc 2000 +1 hộp mỗi " + MOC_BOX_RESETS + " lần reset.");
        }
        InventoryService.gI().sendItemBag(pl);
        sendMocTamBao(pl);
    }

    /**
     * Số lượng quà của mốc sau khi tính hệ số reset: mốc 10-900 x1.5 mỗi lần reset,
     * mốc 2000 +1 mỗi {@link #MOC_BOX_RESETS} lần reset.
     */
    private int scaledQuantity(int baseQuantity, int maxValue, int resetCount) {
        if (resetCount <= 0) {
            return baseQuantity;
        }
        if (maxValue <= 900) {
            return Math.max(1, (int) Math.round(baseQuantity * Math.pow(MOC_QTY_SCALE, resetCount)));
        }
        if (maxValue >= MOC_RESET_VALUE) {
            return baseQuantity + resetCount / MOC_BOX_RESETS;
        }
        return baseQuantity;
    }

    /** Mốc 1000-1700: mỗi lần reset cộng thêm {@link #MOC_OPTION_SCALE} vào các dòng chỉ số (bỏ qua option khoá). */
    private void applyResetScale(Item reward, int maxValue, int resetCount) {
        if (resetCount <= 0 || maxValue < 1000 || maxValue >= MOC_RESET_VALUE) {
            return;
        }
        long bonus = (long) MOC_OPTION_SCALE * resetCount;
        for (ItemOption option : reward.itemOptions) {
            if (option.optionTemplate != null && option.optionTemplate.id != OPTION_LOCK) {
                option.param += bonus;
            }
        }
    }

    private int indexOfMoc(int idMoc) {
        for (int i = 0; i < mocList.size(); i++) {
            if (mocList.get(i).id_moc == idMoc) {
                return i;
            }
        }
        return -1;
    }

    /** Cập nhật mảng đánh dấu mốc đã nhận theo dữ liệu đã lưu của nhân vật. */
    private void markReceived(Player pl) {
        if (pl.listNhan_TamBao == null) {
            pl.listNhan_TamBao = new ArrayList<>();
        }
        pl.checkNhan_TamBao = new int[mocList.size()];
        for (int i = 0; i < mocList.size(); i++) {
            pl.checkNhan_TamBao[i] = daNhanMoc(pl, i, mocList.get(i)) ? 1 : 0;
        }
    }

    /** Mốc đã nhận chưa - chấp nhận cả dữ liệu cũ lưu theo vị trí mốc thay vì id mốc. */
    private boolean daNhanMoc(Player pl, int index, TamBaoItem moc) {
        for (Integer received : pl.listNhan_TamBao) {
            if (received != null && (received == moc.id_moc || received == index)) {
                return true;
            }
        }
        return false;
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private static double toPercent(Double rate) {
        if (rate == null) {
            return MIN_PERCENT;
        }
        return Math.max(MIN_PERCENT, Math.min(MAX_PERCENT, rate));
    }

    /** Ghi log quay vào bảng history_tambao; lỗi ở đây không được làm hỏng lượt quay. */
    private void insertHistory(long playerId, String itemJson) {
        final String sql = "INSERT INTO history_tambao (id_player, item) VALUES (?, ?)";
        try (Connection con = DBConnecter.getConnectionServer();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, playerId);
            ps.setString(2, itemJson);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.logException(TamBao.class, e, "Không ghi được history_tambao (bỏ qua lượt này)");
        }
    }

    private String itemToJson(Item item) {
        JSONObject json = new JSONObject();
        json.put("id", (int) item.template.id);
        json.put("quantity", item.quantity);
        JSONArray options = new JSONArray();
        for (ItemOption option : item.itemOptions) {
            JSONObject jsonOption = new JSONObject();
            jsonOption.put("id", option.optionTemplate.id);
            jsonOption.put("param", option.param);
            options.add(jsonOption);
        }
        json.put("options", options);
        return json.toJSONString();
    }
}
