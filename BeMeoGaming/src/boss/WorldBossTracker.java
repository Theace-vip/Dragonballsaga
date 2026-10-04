package boss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import jdbc.daos.WorldBossDameDAO;
import jdbc.daos.WorldBossRewardDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import panel.GiftItemModel;

/**
 * WorldBossTracker V2 - luu dame runtime + persist DB theo mua giai.
 * Try-catch khong crash server. Tieng Viet khong dau.
 */
public class WorldBossTracker {

    /** Gioi han tich luy top dame cua 1 nguoi - ~1E305 (khoa hoc 1.00E305), vuot qua 33 ty ty lan doi. */
    public static final double DAME_CAP = 1.0E305;

    public static class Entry {
        public long playerId;
        public String name;
        public double dame;
        public Entry(long id, String n, double d) { playerId = id; name = n; dame = d; }
    }

    private static final ConcurrentHashMap<Long, Entry> MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Double> FLUSHED = new ConcurrentHashMap<>();
    public static volatile String season = "";
    public static volatile long startTime = 0;
    public static volatile long endTime = 0;
    public static volatile long lastFlush = 0;

    public static synchronized void start(long durationMin) {
        try {
            season = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        } catch (Exception e) {
            season = String.valueOf(System.currentTimeMillis());
        }
        MAP.clear();
        FLUSHED.clear();
        startTime = System.currentTimeMillis();
        endTime = startTime + Math.max(1, durationMin) * 60000L;
        lastFlush = startTime;
        try { WorldBossDameDAO.ensureTable(); } catch (Exception e) {}
        try { WorldBossRewardDAO.ensureTable(); } catch (Exception e) {}
    }

    public static synchronized void start(String seasonId, long durationMin) {
        try {
            season = seasonId;
        } catch (Exception e) {}
        MAP.clear();
        FLUSHED.clear();
        startTime = System.currentTimeMillis();
        endTime = startTime + Math.max(1, durationMin) * 60000L;
        lastFlush = startTime;
        try { WorldBossDameDAO.ensureTable(); } catch (Exception e) {}
        try { WorldBossRewardDAO.ensureTable(); } catch (Exception e) {}
    }

    public static void reset() {
        try { MAP.clear(); } catch (Exception e) {}
        try { FLUSHED.clear(); } catch (Exception e) {}
        startTime = 0;
        endTime = 0;
        lastFlush = 0;
    }

    public static String getSeason() {
        return season == null ? "" : season;
    }

    public static void addDame(long playerId, String name, double dame) {
        if (dame <= 0) return;
        try {
            MAP.compute(playerId, (k, old) -> {
                if (old == null) return new Entry(playerId, name == null ? ("ID:" + playerId) : name, Math.min(dame, DAME_CAP));
                old.dame = Math.min(old.dame + dame, DAME_CAP);
                if (name != null && !name.isEmpty()) old.name = name;
                return old;
            });
        } catch (Exception e) {}
        try {
            long now = System.currentTimeMillis();
            if (now - lastFlush > 30000L) {
                flushDelta();
            }
        } catch (Exception e) {}
    }

    private static synchronized void flushDelta() {
        try {
            if (season == null || season.isEmpty()) return;
            for (Entry e : MAP.values()) {
                try {
                    if (e == null) continue;
                    double sent = 0;
                    try { sent = FLUSHED.getOrDefault(e.playerId, 0d); } catch (Exception ex) {}
                    double cur = Math.min(e.dame, DAME_CAP);
                    double delta = cur - sent;
                    if (delta > 0) {
                        WorldBossDameDAO.upsert(season, e.playerId, e.name, delta);
                        try { FLUSHED.put(e.playerId, cur); } catch (Exception ex) {}
                    }
                } catch (Exception ex) {}
            }
            lastFlush = System.currentTimeMillis();
        } catch (Exception e) {}
    }

    public static synchronized void flushNow() {
        try {
            if (season == null || season.isEmpty()) return;
            for (Entry e : MAP.values()) {
                try {
                    if (e == null) continue;
                    double sent = 0;
                    try { sent = FLUSHED.getOrDefault(e.playerId, 0d); } catch (Exception ex) {}
                    double cur = Math.min(e.dame, DAME_CAP);
                    double delta = cur - sent;
                    if (delta > 0) {
                        WorldBossDameDAO.upsert(season, e.playerId, e.name, delta);
                        try { FLUSHED.put(e.playerId, cur); } catch (Exception ex) {}
                    }
                } catch (Exception ex) {}
            }
            lastFlush = System.currentTimeMillis();
        } catch (Exception e) {}
    }

    public static List<Entry> top(int n) {
        List<Entry> list = new ArrayList<>(MAP.values());
        try {
            list.sort((a, b) -> Double.compare(b.dame, a.dame));
        } catch (Exception e) {}
        if (list.size() > n) return list.subList(0, n);
        return list;
    }

    public static List<Entry> top10() { return top(10); }

    /** Dinh dang so dame rat lon: < 1E15 dung so tien (k/Tr/Tỷ), >= 1E15 dung khoa hoc (1.00E305). */
    public static String fmtDame(double d) {
        try {
            if (Double.isNaN(d) || Double.isInfinite(d)) return "MAX";
            if (d >= 1.0E15) return String.format(java.util.Locale.US, "%.2E", Math.min(d, DAME_CAP));
            return utils.Util.numberToMoney(d);
        } catch (Exception e) { return String.valueOf(d); }
    }
    public static int size() { try { return MAP.size(); } catch (Exception e) { return 0; } }

    public static long millisLeft() {
        if (endTime <= 0) return -1;
        return endTime - System.currentTimeMillis();
    }

    public static String topText() {
        List<Entry> t = top10();
        if (t.isEmpty()) return "(chua co ai danh)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.size(); i++) {
            Entry e = t.get(i);
            sb.append(i + 1).append(". ").append(e.name).append(" - ").append(fmtDame(e.dame));
            if (i < t.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public static List<int[]> parseReward(String s) {
        List<int[]> out = new ArrayList<>();
        if (s == null || s.trim().isEmpty()) return out;
        for (String part : s.split("[,;\\s]+")) {
            part = part.trim();
            if (part.isEmpty()) continue;
            try {
                String[] kv = part.split(":");
                int temp = Integer.parseInt(kv[0].trim());
                int qty = kv.length > 1 ? Integer.parseInt(kv[1].trim()) : 1;
                if (qty <= 0) qty = 1;
                out.add(new int[]{temp, qty});
            } catch (Exception e) {}
        }
        return out;
    }

    public static List<GiftItemModel> parseGiftJson(String json) {
        List<GiftItemModel> out = new ArrayList<>();
        try {
            if (json == null || json.trim().isEmpty()) return out;
            Object o = JSONValue.parse(json.trim());
            if (o instanceof JSONArray) {
                JSONArray arr = (JSONArray) o;
                for (Object oo : arr) {
                    try {
                        if (oo instanceof JSONObject) {
                            GiftItemModel m = GiftItemModel.fromJson((JSONObject) oo);
                            if (m != null) out.add(m);
                        }
                    } catch (Exception ex) {}
                }
            } else if (o instanceof JSONObject) {
                try {
                    GiftItemModel m = GiftItemModel.fromJson((JSONObject) o);
                    if (m != null) out.add(m);
                } catch (Exception ex) {}
            }
        } catch (Exception e) {}
        return out;
    }

    public static List<GiftItemModel> buildItems(String cfgLegacy, String jsonCfg) {
        try {
            if (jsonCfg != null && !jsonCfg.trim().isEmpty()) {
                List<GiftItemModel> js = parseGiftJson(jsonCfg);
                if (!js.isEmpty()) return js;
            }
        } catch (Exception e) {}
        List<GiftItemModel> out = new ArrayList<>();
        try {
            List<int[]> legacy = parseReward(cfgLegacy);
            for (int[] tq : legacy) {
                try {
                    GiftItemModel m = new GiftItemModel();
                    m.tempId = tq[0];
                    m.quantity = tq[1];
                    out.add(m);
                } catch (Exception ex) {}
            }
        } catch (Exception e) {}
        return out;
    }

    public static String rewardAll() {
        StringBuilder log = new StringBuilder();
        try {
            panel.tuning.WorldBossTuning.load();
            try { WorldBossDameDAO.ensureTable(); } catch (Exception e) {}
            try { WorldBossRewardDAO.ensureTable(); } catch (Exception e) {}
            try { flushNow(); } catch (Exception e) {}
            if (season == null || season.isEmpty()) {
                try {
                    season = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
                } catch (Exception e) {
                    season = String.valueOf(System.currentTimeMillis());
                }
            }
            List<Entry> t = top10();
            if (t.isEmpty()) {
                log.append("Khong co ai danh boss dot ").append(season).append("\n");
                try { WorldBossRewardDAO.markRewarded(season, "[]"); } catch (Exception e) {}
                return log.toString();
            }
            try {
                if (WorldBossRewardDAO.alreadyRewarded(season)) {
                    return "Dot " + season + " da trao truoc do.\n";
                }
            } catch (Exception e) {}
            Set<Long> topIds = new HashSet<>();
            for (Entry e : t) try { topIds.add(e.playerId); } catch (Exception ex) {}
            for (int i = 0; i < t.size(); i++) {
                Entry e = t.get(i);
                String rank;
                String cfgLegacy;
                String cfgJson;
                if (i == 0) {
                    cfgLegacy = panel.tuning.WorldBossTuning.TOP1;
                    cfgJson = panel.tuning.WorldBossTuning.TOP1_JSON;
                    rank = "Top 1";
                } else if (i <= 2) {
                    cfgLegacy = panel.tuning.WorldBossTuning.TOP23;
                    cfgJson = panel.tuning.WorldBossTuning.TOP23_JSON;
                    rank = "Top " + (i + 1);
                } else {
                    cfgLegacy = panel.tuning.WorldBossTuning.TOP410;
                    cfgJson = panel.tuning.WorldBossTuning.TOP410_JSON;
                    rank = "Top " + (i + 1);
                }
                List<GiftItemModel> items = buildItems(cfgLegacy, cfgJson);
                boolean ok = false;
                try { ok = giveGift(e, items, rank); } catch (Exception ex) {}
                log.append(rank).append(" ").append(e.name).append(ok ? " OK" : " OFFLINE/that bai").append("\n");
                try {
                    player.Player p = server.Client.gI().getPlayer(e.playerId);
                    if (p != null) services.Service.gI().sendThongBao(p, "Boss The Gioi: ban dat " + rank + "!");
                } catch (Exception ex) {}
            }
            int consolCount = 0;
            try {
                List<GiftItemModel> consolItems = buildItems(panel.tuning.WorldBossTuning.CONSOL, panel.tuning.WorldBossTuning.CONSOL_JSON);
                for (player.Player p : server.Client.gI().getPlayers()) {
                    try {
                        if (p == null || p.isBoss) continue;
                        if (topIds.contains(p.id)) continue;
                        Entry e = MAP.get(p.id);
                        if (e == null) continue;
                        if (e.dame < panel.tuning.WorldBossTuning.MIN_DAME) continue;
                        if (giveGift(e, consolItems, "An ui")) consolCount++;
                    } catch (Exception ex) {}
                }
            } catch (Exception e) {}
            log.append("An ui: ").append(consolCount).append(" nguoi\n");
            try {
                JSONArray arr = new JSONArray();
                for (int i = 0; i < t.size(); i++) {
                    try {
                        Entry e = t.get(i);
                        JSONObject o = new JSONObject();
                        o.put("rank", i + 1);
                        o.put("id", e.playerId);
                        o.put("name", e.name);
                        o.put("dame", Math.min(e.dame, DAME_CAP));
                        arr.add(o);
                    } catch (Exception ex) {}
                }
                String topJson = arr.toJSONString();
                WorldBossRewardDAO.markRewarded(season, topJson);
            } catch (Exception e) {
                try { WorldBossRewardDAO.markRewarded(season, "[]"); } catch (Exception ex) {}
            }
            try { services.Service.gI().sendThongBaoAllPlayer("Boss The Gioi da ket thuc! Top 1: " + (t.isEmpty() ? "khong co" : t.get(0).name)); } catch (Exception e) {}
        } catch (Exception e) {
            try { log.append("Loi: ").append(e.getMessage()); } catch (Exception ex) {}
        }
        return log.toString();
    }

    private static List<item.Item> buildItemObjects(List<GiftItemModel> models) {
        List<item.Item> out = new ArrayList<>();
        if (models == null) return out;
        for (GiftItemModel m : models) {
            try {
                if (m == null) continue;
                if (m.tempId == -1 || m.tempId == -2 || m.tempId == -3) continue;
                item.Item it = services.ItemService.gI().createNewItem(m.tempId, m.quantity <= 0 ? 1 : m.quantity);
                if (it == null || it.template == null) continue;
                it.quantity = m.quantity <= 0 ? 1 : m.quantity;
                try {
                    if (m.options != null) {
                        for (GiftItemModel.Opt o : m.options) {
                            try {
                                if (o == null) continue;
                                it.itemOptions.add(new item.Item.ItemOption(o.id, o.param));
                            } catch (Exception ex) {}
                        }
                    }
                } catch (Exception ex) {}
                out.add(it);
            } catch (Exception ex) {}
        }
        return out;
    }

    private static void applyCurrency(player.Player p, List<GiftItemModel> models) {
        if (p == null || models == null) return;
        for (GiftItemModel m : models) {
            try {
                if (m == null) continue;
                int q = m.quantity <= 0 ? 1 : m.quantity;
                if (m.tempId == -1) {
                    try { p.inventory.gold = Math.min(p.inventory.gold + (long) q, 2000000000L); } catch (Exception ex) {}
                } else if (m.tempId == -2) {
                    try { p.inventory.gem = Math.min(p.inventory.gem + q, 200000000); } catch (Exception ex) {}
                } else if (m.tempId == -3) {
                    try { p.inventory.ruby = Math.min(p.inventory.ruby + q, 200000000); } catch (Exception ex) {}
                }
            } catch (Exception ex) {}
        }
    }

    private static String currencyText(List<GiftItemModel> models) {
        try {
            StringBuilder sb = new StringBuilder();
            for (GiftItemModel m : models) {
                try {
                    if (m == null) continue;
                    int q = m.quantity <= 0 ? 1 : m.quantity;
                    if (m.tempId == -1) { if (sb.length() > 0) sb.append(", "); sb.append(q).append(" Vang"); }
                    else if (m.tempId == -2) { if (sb.length() > 0) sb.append(", "); sb.append(q).append(" Ngoc"); }
                    else if (m.tempId == -3) { if (sb.length() > 0) sb.append(", "); sb.append(q).append(" Ngoc khoa"); }
                } catch (Exception ex) {}
            }
            return sb.toString();
        } catch (Exception e) { return ""; }
    }

    private static String itemText(List<item.Item> objs, List<GiftItemModel> models) {
        try {
            StringBuilder sb = new StringBuilder();
            for (item.Item it : objs) {
                try {
                    if (it == null || it.template == null) continue;
                    if (sb.length() > 0) sb.append("; ");
                    sb.append(it.template.name).append(" x").append(it.quantity);
                    try {
                        if (it.itemOptions != null && !it.itemOptions.isEmpty()) {
                            sb.append(" [");
                            for (int i = 0; i < it.itemOptions.size(); i++) {
                                if (i > 0) sb.append(", ");
                                item.Item.ItemOption o = it.itemOptions.get(i);
                                sb.append("#").append(o.optionTemplate.id).append(" +").append(o.param);
                            }
                            sb.append("]");
                        }
                    } catch (Exception ex) {}
                } catch (Exception ex) {}
            }
            return sb.toString();
        } catch (Exception e) { return ""; }
    }

    private static Mail.Thu buildMail(Entry e, List<GiftItemModel> items, List<item.Item> objs, String rank) {
        String cur = "";
        String det = "";
        try { cur = currencyText(items); } catch (Exception ex) {}
        try { det = itemText(objs, items); } catch (Exception ex) {}
        StringBuilder content = new StringBuilder();
        try {
            content.append("Chuc mung ").append(e.name).append(" dat ").append(rank == null ? "" : rank);
            content.append(" Boss The Gioi dot ").append(season == null ? "" : season).append("!");
            if (cur != null && !cur.isEmpty()) content.append("\nTien kem theo: ").append(cur).append(" (da cong truc tiep).");
            if (det != null && !det.isEmpty()) content.append("\nVat pham trong thu: ").append(det).append(".");
            else if (cur != null && !cur.isEmpty()) content.append("\nMo thu de xem chi tiet.");
            else content.append("\nMo thu de nhan qua.");
        } catch (Exception ex) {}
        Mail.Thu thu = new Mail.Thu("Qua Boss The Gioi", rank == null ? "" : rank, "He thong",
                content.toString(), System.currentTimeMillis(), false, false);
        try { thu.listItem = objs == null ? new ArrayList<>() : objs; } catch (Exception ex) {}
        return thu;
    }

    private static boolean giveGift(Entry e, List<GiftItemModel> items, String rank) {
        if (e == null) return false;
        if (items == null) items = new ArrayList<>();
        try {
            player.Player p = null;
            try { p = server.Client.gI().getPlayer(e.playerId); } catch (Exception ex) {}
            if (p != null) {
                try {
                    // Vang/Ngoc cong truc tiep (khong phai item nen khong the nam trong thu).
                    // Vat pham nam im trong thu - chi hien khi bam NPC Nhan Thu -> nhan dan theo o trong.
                    try { applyCurrency(p, items); } catch (Exception ex) {}
                    List<item.Item> objs = new ArrayList<>();
                    try { objs = buildItemObjects(items); } catch (Exception ex) {}
                    if (p.homThu == null) { try { p.homThu = new ArrayList<>(); } catch (Exception ex) {} }
                    Mail.Thu thu = null;
                    try { thu = buildMail(e, items, objs, rank); } catch (Exception ex) {}
                    if (thu != null) {
                        try {
                            while (p.homThu.size() >= 50) p.homThu.remove(0);
                        } catch (Exception ex) {}
                        try { p.homThu.add(thu); } catch (Exception ex) {}
                    }
                    try { jdbc.daos.PlayerDAO.updatePlayer(p); } catch (Exception ex) {}
                    // Khong goi sendListMail ngay: de thu nam im, chi mo khi bam NPC Nhan Thu.
                    // Khong goi sendItemBag vi chua nhan item nao vao tui.
                    try { services.Service.gI().sendThongBao(p, "Boss The Gioi: ban dat " + rank + "! Gap NPC Nhan Thu de mo thu nhan qua."); } catch (Exception ex) {}
                    try { services.Service.gI().sendMoney(p); } catch (Exception ex) {}
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        } catch (Exception ex) {}
        try {
            player.Player off = null;
            try { off = jdbc.daos.NDVSqlFetcher.loadById(e.playerId); } catch (Exception ex) {}
            if (off == null) {
                try { off = jdbc.daos.NDVSqlFetcher.loadPlayerByName(e.name); } catch (Exception ex) {}
            }
            if (off == null) return false;
            try {
                applyCurrency(off, items);
                List<item.Item> objs = buildItemObjects(items);
                Mail.Thu thu = buildMail(e, items, objs, rank);
                try {
                    if (off.homThu == null) off.homThu = new ArrayList<>();
                } catch (Exception ex) {}
                try {
                    while (off.homThu.size() >= 50) off.homThu.remove(0);
                } catch (Exception ex) {}
                try { off.homThu.add(thu); } catch (Exception ex) {}
                try { jdbc.daos.PlayerDAO.updatePlayer(off); } catch (Exception ex) {}
                return true;
            } catch (Exception ex) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
