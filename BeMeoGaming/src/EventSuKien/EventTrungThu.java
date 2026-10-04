package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import shop.ShopService;
import utils.Util;

public class EventTrungThu extends Npc {

    public EventTrungThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (EventSuKien.TrungThuService.gI().isNpcMap(this.mapId)) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        baseMenu(player),
                        "Thể lệ", "Làm bánh", "Đổi điểm\nTrung thu");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (EventSuKien.TrungThuService.gI().isNpcMap(this.mapId)) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                    theLeMenu(player),
                                    "Đã hiểu");

                            break;
                        case 1:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.EventTrungThuLB, -1,
                                    nauBanhMenu(player),
                                    "Bánh\nHạt sen", "Bánh\nĐậu xanh", "Bánh\nThập cẩm");

                            break;

                        case 2:
//                            if (player.Saga_VIP < 99) {
//                                Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì Thay quà");
//                                return;
//
//                            }
                            
                            
                            String[] exBtns = doiDiemButtons();
                            if (exBtns.length == 0) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.huongdan, -1,
                                        "|7|Chua co moc doi diem nao\nAdmin them moc o panel su kien Trung Thu.",
                                        "Da hieu");
                            } else {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.EventTrungThuDD, -1,
                                        doiDiemMenu(player), exBtns);
                            }

                            break;

                        case 3:
                            ShopService.gI().opendShop(player, "Trungthu", true);
                            break;

                    }
                }
            }
        }
    }

    /** Menu chinh - hien dung du lieu ma nut "Lam banh" se tru (doc tu panel). */
    private String baseMenu(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("|7|SỰ KIỆN TRUNG THU");
        sb.append("\n\n|2|NGUYÊN LIỆU NẤU BÁNH");
        String[] names = {"Bánh Hạt sen", "Bánh Đậu xanh", "Bánh Thập cẩm"};
        for (int i = 0; i < 3; i++) {
            EventSuKien.TrungThuService.Recipe r = EventSuKien.TrungThuService.gI().getRecipe(i);
            if (r == null) continue;
            sb.append("\n|1|- ").append(names[i]).append("  (+").append(r.points).append(" Điểm)");
            StringBuilder mat = new StringBuilder();
            for (int[] m : r.mats) {
                if (mat.length() > 0) mat.append(" + ");
                mat.append(Util.format(m[1])).append(" ").append(nameOf(m[0]));
            }
            for (String line : wrapPlus(mat.toString(), 40)) {
                sb.append("\n|0|   ").append(line);
            }
        }
        sb.append("\n\n|7|Phí nấu : ").append(Util.format(EventSuKien.TrungThuService.gI().cookFeeVnd()))
                .append(" VND nạp / lần");
        sb.append("\n|7|Nấu xong nhận ngay Điểm Sự Kiện, không nhận bánh");
        sb.append("\n\n|1|Điểm sự kiện : ").append(Util.format(player.sukien)).append(" Điểm");
        return sb.toString();
    }

    /** Cat chuoi "a + b + c" thanh cac dong co do dai toi da, cat tai dau "+". */
    private static java.util.List<String> wrapPlus(String s, int max) {
        java.util.List<String> out = new java.util.ArrayList<>();
        String[] parts = s.split(" \\+ ");
        StringBuilder cur = new StringBuilder();
        for (String p : parts) {
            int add = (cur.length() == 0 ? 0 : 3) + p.length();
            if (cur.length() > 0 && cur.length() + add > max) {
                out.add(cur.toString());
                cur = new StringBuilder();
            }
            if (cur.length() > 0) cur.append(" + ");
            cur.append(p);
        }
        if (cur.length() > 0) out.add(cur.toString());
        return out;
    }

    /** Menu "Lam banh" - trinh dung dung du lieu ma nut bam se tru. */
    private String nauBanhMenu(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("|7|SỰ KIỆN TRUNG THU");
        sb.append("\n\n|2|Bạn muốn làm bánh gì?");
        String[] names = {"Bánh Hạt sen", "Bánh Đậu xanh", "Bánh Thập cẩm"};
        for (int i = 0; i < 3; i++) {
            EventSuKien.TrungThuService.Recipe r = EventSuKien.TrungThuService.gI().getRecipe(i);
            if (r == null) continue;
            sb.append("\n|1|- ").append(names[i]).append("  (+").append(r.points).append(" Điểm)");
            StringBuilder mat = new StringBuilder();
            for (int[] m : r.mats) {
                if (mat.length() > 0) mat.append(" + ");
                mat.append(Util.format(m[1])).append(" ").append(nameOf(m[0]));
            }
            for (String line : wrapPlus(mat.toString(), 40)) {
                sb.append("\n|0|   ").append(line);
            }
        }
        sb.append("\n\n|7|Mỗi lần trừ đúng nguyên liệu + ")
                .append(Util.format(EventSuKien.TrungThuService.gI().cookFeeVnd()))
                .append(" VND nạp");
        sb.append("\n|7|Nhận ngay điểm, không nhận bánh");
        sb.append("\n\n|1|Điểm sự kiện : ").append(Util.format(player.sukien)).append(" Điểm");
        return sb.toString();
    }

    /** Thong le: (1) tim nguyen lieu  (2) ket qua khi nau banh  (3) qua doi duoc. */
    private String theLeMenu(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("|7|SỰ KIỆN TRUNG THU\n");

        sb.append("\n|2|1. CÁCH TÌM NGUYÊN LIỆU");
        sb.append("\n|4|- Hạt sen : Đánh các quái Bên Cooler");
        sb.append("\n|4|- Đậu xanh : Đánh các quái dưới đất");
        sb.append("\n|4|- Bột nếp : Đánh quái Sên ở Tương lai");
        sb.append("\n|4|- Mồi lửa : Giết Boss Thỏ Đại Ca (5p xuất hiện 1 lần)");
        java.util.Set<Integer> ing = new java.util.HashSet<>();
        for (int i = 0; i < 8; i++) {
            EventSuKien.TrungThuService.Recipe rr = EventSuKien.TrungThuService.gI().getRecipe(i);
            if (rr == null) continue;
            for (int[] m : rr.mats) ing.add(m[0]);
        }
        ing.add(751);  // La dong
        ing.add(889);  // Dau xanh su kien
        for (int i = 0; i < 32; i++) {
            String raw = panel.tuning.TrungThuTuning.get("drop." + i);
            if (raw.isEmpty()) continue;
            String[] p = raw.split("\\|");
            if (p.length < 3) continue;
            try {
                int id = Integer.parseInt(p[0].trim());
                if (!ing.contains(id)) continue; // chi liet ke nguyen lieu
                sb.append("\n|4|- ").append(nameOf(id))
                        .append(" : rớt từ quái ").append(p[2].trim()).append("%");
            } catch (Exception ignore) {
            }
        }

        sb.append("\n\n|2|2. KẾT QUẢ KHI NẤU BÁNH");
        sb.append("\n|5|Nấu xong nhận NGAY phần thưởng sau (không nhận bánh):");
        String[] names = {"Bánh Hạt sen", "Bánh Đậu xanh", "Bánh Thập cẩm"};
        for (int i = 0; i < 3; i++) {
            EventSuKien.TrungThuService.Recipe r = EventSuKien.TrungThuService.gI().getRecipe(i);
            if (r == null) continue;
            sb.append("\n|-1|- ").append(names[i]).append(" : +")
                    .append(r.points).append(" Điểm Sự Kiện");
        }
        sb.append("\n|7|(Phí ").append(Util.format(EventSuKien.TrungThuService.gI().cookFeeVnd()))
                .append(" VND nạp / lần)");

        sb.append("\n\n|2|3. ĐỔI ĐIỂM NHẬN QUÀ");
        java.util.List<String> shown = new java.util.ArrayList<>();
        for (EventSuKien.TrungThuService.Exchange e : EventSuKien.TrungThuService.gI().allExchanges()) {
            for (int[] r : e.rewards) {
                String n = Util.format(r[1]) + " " + nameOf(r[0]);
                if (!shown.contains(n)) shown.add(n);
            }
        }
        for (String n : shown) {
            sb.append("\n|-1|- ").append(n);
        }
        for (int i = 0; i < 32; i++) {
            String raw = panel.tuning.TrungThuTuning.get("box.reward." + i);
            if (raw.isEmpty()) continue;
            String[] p = raw.split("\\|");
            if (p.length < 2) continue;
            try {
                String n = Util.format(Integer.parseInt(p[1].trim())) + " " + nameOf(Integer.parseInt(p[0].trim()));
                if (!shown.contains(n)) { shown.add(n); sb.append("\n|-1|- ").append(n); }
            } catch (Exception ignore) {
            }
        }
        sb.append("\n|4|(Rương Trung Thu có ")
                .append(EventSuKien.TrungThuService.gI().costumeRate())
                .append("% ra cải trang)");

        sb.append("\n\n|7|Đuôi Khỉ : +").append(EventSuKien.TrungThuService.gI().tailBonusPercent())
                .append("% rơi vật phẩm Sự Kiện trong ")
                .append(EventSuKien.TrungThuService.gI().tailDurationMs() / 60000).append(" phút");
        sb.append("\n|7|Boss hồi sinh sau ")
                .append(EventSuKien.TrungThuService.gI().bossRestSeconds())
                .append(" giây, gây ").append(Util.format(EventSuKien.TrungThuService.gI().bossFixedDamage()))
                .append(" sát thương cố định");
        sb.append("\n\n|1|Điểm sự kiện : ").append(Util.format(player.sukien)).append(" Điểm");
        return sb.toString();
    }

    /**
     * Tieu de menu doi diem - doc toan bo tu panel TrungThuTuning.
     * Don vi: "k" = x1000 (7k = 7000, 5k = 5000) - khong nhat 77.
     */
    private String doiDiemMenu(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("|7|TÍCH ĐIỂM SỰ KIỆN TRUNG THU\n");
        sb.append("|1|Trừ điểm TRƯỚC rồi mới nhận quà\n");
        java.util.List<EventSuKien.TrungThuService.Exchange> list = EventSuKien.TrungThuService.gI().allExchanges();
        for (int i = 0; i < list.size() && i < 8; i++) {
            EventSuKien.TrungThuService.Exchange e = list.get(i);
            sb.append("|2|Mốc ").append(Util.format(e.need)).append(" Điểm\n");
            StringBuilder got = new StringBuilder();
            for (int[] r : e.rewards) {
                if (got.length() > 0) got.append(" + ");
                got.append(Util.format(r[1])).append(" ").append(nameOf(r[0]));
            }
            sb.append("|4|Nhận ").append(got).append("\n");
        }
        sb.append("|7|Điểm sự kiện : ").append(Util.format(player.sukien)).append(" Điểm");
        sb.append("\n|1|Điểm Đổi Trung thu : ").append(Util.format(player.point_vip)).append(" Điểm");
        return sb.toString();
    }

    /** Nut chon moc - lay tu config, gioi han 8 nut de menu khong tran. */
    private String[] doiDiemButtons() {
        java.util.List<EventSuKien.TrungThuService.Exchange> list = EventSuKien.TrungThuService.gI().allExchanges();
        int n = Math.min(list.size(), 8);
        String[] out = new String[n];
        for (int i = 0; i < n; i++) {
            int need = list.get(i).need;
            String s = Util.format(need);
            if (need >= 1000 && need % 1000 == 0) {
                s = (need / 1000) + "k"; // k = x1000
            }
            out[i] = s + " Điểm";
        }
        return out;
    }

    private static String nameOf(int id) {
        try {
            models.Template.ItemTemplate t = services.ItemService.gI().getTemplate(id);
            return t == null ? ("vật phẩm " + id) : t.name.trim();
        } catch (Exception e) {
            return "vật phẩm " + id;
        }
    }
}
