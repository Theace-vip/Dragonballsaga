import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GenCaitrang - bake anh cai trang tu spine frames da export thanh part NRO.
 *
 * Nguyen tac (khong sua client):
 *   - Client (Char.paintCharBody) ve 3 mui: head -> leg -> body, anchor TOP_LEFT, cdir=1:
 *       x = cx + CharInfo[cf][i][1] + part.pi[pIdx].dx
 *       y = cy - CharInfo[cf][i][2] + part.pi[pIdx].dy
 *     (mGraphics nhan moi toa do voi zoomLevel -> dx/dy la logical, anh x2 = 2x logical)
 *   - Mot part frame duoc NHIEU cf dung chung -> chon 1 cf dai dien, cat mieng tu frame cua cf do.
 *   - Cat theo 2 duong cong co dinh (tinh tu day artwork, don vi: px spine):
 *       L1 = head/body, L2 = body/leg  (ty so lay theo frame stand-1 f0 de moi frame join nhau)
 *   - Goc toa do moi frame: originY = day bbox (mat dat), originX = tam trong luong vung than.
 *
 * Output:
 *   - data/icon_botnet/x{2,3,4}/<id>.png  (34 anh part + 1 icon + 1 avatar)
 *   - build/caitrang_part.txt              (slot, idx, id, dx, dy  -> de chen DB)
 *   - build/ct_preview.html                (33 pose da noi voi part moi, CharFrameSheet se ghep)
 *
 * Cach dung: java -cp build/tools GenCaitrang <root_BeMeoGaming> <Char.cs>
 */
public class GenCaitrang {

    static String SET = "g13_juitianxuannv";
    static String STAND_ANIM = "stand-1";   // ten anim dung lam chuan (bo moi co ten khac)
    static String ITEM_NAME = "Cải trang VLT 052";   // ten hien thi tren preview (CO DAU)
    static int PART_HEAD = 2143, PART_BODY = 2144, PART_LEG = 2145;

    /** cf -> {anim, frame}  (da duyet: -1 cho dung/chay, -2 cho danh) */
    static final String[][] MAP = {
        {"stand-1", "0"}, {"stand-1", "1"},                       // 0,1 dung
        {"run-1", "0"}, {"run-1", "2"}, {"run-1", "3"}, {"run-1", "4"}, {"run-1", "6"}, // 2..6 di bo
        {"run-1", "1"}, {"run-1", "5"},                            // 7,8 roi/nhay
        {"attack-2", "4"}, {"attack-2", "5"},                      // 9,10 lanh
        {"run-1", "7"},                                            // 11 nhay ngang
        {"attack-2", "3"},                                         // 12 charge tren khong
        {"skill-2", "0"}, {"skill-2", "7"}, {"skill-2", "3"},      // 13,14,15 skill dung
        {"stand-1", "0"},                                          // 16 (vep)
        {"stand-1", "3"},                                          // 17 charge dung
        {"skill-2", "19"}, {"skill-2", "6"}, {"skill-2", "8"}, {"skill-2", "11"}, // 18..21 skill bay
        {"attack-2", "3"},                                         // 22 charge ket thuc
        {"attack-2", "7"},                                         // 23 bi trung don
        {"skill-2", "5"},                                          // 24 skill roi
        {"run-1", "0"},                                            // 25 dich chuyen (vep)
        {"skill-2", "9"}, {"skill-2", "8"},                        // 26,27 skill roi
        {"skill-2", "4"},                                          // 28
        {"stand-1", "0"},                                          // 29 (vep)
        {"skill-2", "9"}, {"skill-2", "16"},                       // 30,31
        {"attack-2", "3"}                                          // 32 charge bay
    };

    /** cf dai dien cho tung part frame (xem BANG_MAP_CAI_TRANG.md) */
    static final int[] HEAD_REP = {0, 2, 25};
    static final int[] LEG_REP  = {8, 0, 2, 3, 4, 5, 6, 7, 32, 13, 9, 10, 11, 25};
    static final int[] BODY_REP = {23, 0, 15, 3, 4, 5, 6, 7, 12, 32, 13, 14, 9, 16, 19, 31, 25};

    static int ID_HEAD0 = 32601;   // head 3 -> ID_HEAD0..+2
    static int ID_LEG0  = 32604;   // leg 14 -> ID_LEG0..+13
    static int ID_BODY0 = 32618;   // body 17 -> ID_BODY0..+16
    static int ID_ICON  = 32635;
    static int ID_AVATAR = 32636;

    static final double L1F = 0.664;   // duong head/body (ty so tinh tu day artwork len)
    static final double L2F = 0.389;   // duong body/leg
    /** Chieu cao muc tieu (px x2). Dinh dang part la BYTE (-128..127 ca dx/dy, ca client lan server)
     *  nen khong giu duoc 446 px goc -> chon 150 px (f~0.336, dx/dy ~ -50..-80, duong cho). */
    static final int TARGET_H = 150;
    static final int PAD = 12;         // do mo rong mieng cat (px spine) de join khong kin moc
    static final int ALPHA = 128;      // nguong alpha tinh bbox

    static Path root;
    static int[][][] ci;               // [cf][4][3]
    static final Map<String, BufferedImage> cache = new HashMap<>();
    static final Map<String, double[]> cams = new HashMap<>();  // anim -> {camX,camY,camH}
    static double standWorldH;         // chieu cao nhan (world unit) cua pose stand
    static double L1d, L2d;            // khoang cach world tu day artwork len toi 2 duong cat
    static double scale;               // x2 px / world unit

    public static void main(String[] args) throws Exception {
        root = Paths.get(args[0]);
        // java -cp build/tools GenCaitrang <root> <Char.cs> [set] [iconHead0] [partHead] [partBody] [partLeg] [ten item]
        if (args.length > 2) SET = args[2];
        int iconBase = args.length > 3 ? Integer.parseInt(args[3]) : 32601;
        if (args.length > 4) PART_HEAD = Integer.parseInt(args[4]);
        if (args.length > 5) PART_BODY = Integer.parseInt(args[5]);
        if (args.length > 6) PART_LEG = Integer.parseInt(args[6]);
        // ten item dat trong source (UTF-8) - khong truyen qua CLI vi Git Bash lam mat dau
        ITEM_NAME = itemNameFor(SET);
        ID_HEAD0 = iconBase; ID_LEG0 = iconBase + 3; ID_BODY0 = iconBase + 17;
        ID_ICON = iconBase + 34; ID_AVATAR = iconBase + 35;
        // bo moi co kieu ten khac nhau -> tim ten anim thich hop cho tung dong MAP:
        //   g13_juitianxuannv: stand-1 (co san) | g13_dijiang: stand | g13_qiongqi_nan: stand1-0
        java.util.Set<String> avai = new java.util.HashSet<>();
        java.io.File[] ds = root.resolve("data/spine_frames/" + SET).toFile().listFiles(java.io.File::isDirectory);
        if (ds != null) for (java.io.File d : ds) avai.add(d.getName());
        for (String[] r : MAP) {
            if (avai.contains(r[0])) continue;                 // ten cu van con
            String base = r[0].replaceAll("-[12]$", "");       // stand-1 -> stand
            if (avai.contains(base + "1-0")) { r[0] = base + "1-0"; continue; }
            if (avai.contains(base)) { r[0] = base; continue; }
            for (String n : avai) if (n.startsWith(base)) { r[0] = n; break; }
        }
        STAND_ANIM = MAP[0][0];
        System.out.println("Bo: " + MAP[0][0] + ", " + MAP[2][0] + ", " + MAP[9][0] + ", " + MAP[13][0]);
        ci = parseCharInfo(new String(Files.readAllBytes(Paths.get(args[1])), "UTF-8"));
        parseCams(new String(Files.readAllBytes(root.resolve("data/spine_frames/" + SET + "/meta.json")), "UTF-8"));
        System.out.println("CharInfo: " + ci.length + " cf, " + cams.size() + " anim co cam");

        // --- 1. chuyen sang don vi THE GIOI (bo -2 duoc camera xa hon nen pixel nho hon ~60%) ---
        int baseH = baseCharHeight();
        BufferedImage stand = frame(STAND_ANIM, 0);
        int[] sb = bbox(stand);
        double[] c0 = cams.get(STAND_ANIM);
        int standPxH = sb[3] + 1 - sb[1];            // chieu cao PX GOC cua frame spine
        standWorldH = standPxH * c0[2] / 935.0;
        // Chieu cao muc tieu = TARGET_H (khong phai pixel goc 446 vi bi han che byte ±127)
        scale = (double) TARGET_H / standWorldH;
        L1d = L1F * standWorldH;
        L2d = L2F * standWorldH;
        System.out.printf("Muc tieu: %d px x2 (spine goc %d px, nhan NRO %d x2px) ; scale = %.5f x2px/world%n",
            TARGET_H, standPxH, baseH, scale);
        System.out.printf("duong cat: L1 = %.1f world, L2 = %.1f world tren day artwork%n", L1d, L2d);

        // --- 2. cat 34 mieng part ---
        StringBuilder report = new StringBuilder();
        int[] headBox = null;
        String[][] slots = {new String[HEAD_REP.length], new String[LEG_REP.length], new String[BODY_REP.length]};
        int[][] dxs = new int[3][], dys = new int[3][];
        for (int s = 0; s < 3; s++) {
            int[] reps = s == 0 ? HEAD_REP : (s == 1 ? LEG_REP : BODY_REP);
            dxs[s] = new int[reps.length];
            dys[s] = new int[reps.length];
            for (int idx = 0; idx < reps.length; idx++) {
                int repCf = reps[idx];
                Piece p = cut(repCf, s == 0 ? 0 : (s == 1 ? 1 : 2));
                int id = (s == 0 ? ID_HEAD0 : (s == 1 ? ID_LEG0 : ID_BODY0)) + idx;
                if (p == null) {
                    System.out.println("!! khong cat duoc slot=" + s + " idx=" + idx + " cf=" + repCf);
                    continue;
                }
                // toa do logical cua goc anh so voi (cx, cy)
                double lx = p.wx * scale / 2.0, ly = p.wy * scale / 2.0;
                int slotCol = (s == 0) ? 0 : (s == 1 ? 1 : 2);   // CharInfo: 0=head,1=leg,2=body
                int dx = (int) Math.round(lx - ci[repCf][slotCol][1]);
                int dy = (int) Math.round(ly + ci[repCf][slotCol][2]);
                dxs[s][idx] = dx; dys[s][idx] = dy;
                if (s == 0 && idx == 0) headBox = new int[]{p.ax, p.ay, p.aw, p.ah};
                for (int z : new int[]{2, 3, 4}) {
                    // scale = x2px/world -> phai doi tu pixel sang world truoc (ppw) moi nhan z
                    save(scale(p.img, scale * p.ppw * z / 2.0), id, z);
                }
                slots[s][idx] = "[" + id + "," + dx + "," + dy + "]";
                report.append("slot=").append(s).append(" idx=").append(idx)
                    .append(" id=").append(id).append(" cf=").append(repCf)
                    .append(" src=").append(MAP[repCf][0]).append("/f").append(MAP[repCf][1])
                    .append(" size=").append(p.img.getWidth()).append("x").append(p.img.getHeight())
                    .append(" px -> ").append(Math.round(p.ww * scale)).append("x").append(Math.round(p.wh * scale))
                    .append(" x2px  dx=").append(dx).append(" dy=").append(dy).append("\n");
            }
        }

        // --- 3. icon + avatar ---
        BufferedImage art = crop(stand, bbox(stand));
        save(fit(art, 32), ID_ICON, 2);
        save(fit(art, 48), ID_ICON, 3);
        save(fit(art, 64), ID_ICON, 4);
        int hx = headBox[0], hy = headBox[1], hw = headBox[2], hh = headBox[3];
        int[] pb = new int[]{Math.max(0, hx - hw / 3), Math.max(0, hy - hh / 4),
            Math.min(stand.getWidth() - 1, hx + hw + hw / 3),
            Math.min(stand.getHeight() - 1, hy + (int) (hh * 2.1))};
        BufferedImage portrait = crop(stand, pb);
        save(fit(portrait, 130, 126), ID_AVATAR, 2);
        save(fit(portrait, 195, 189), ID_AVATAR, 3);
        save(fit(portrait, 260, 252), ID_AVATAR, 4);

        // --- 4. ghi SQL part + preview ---
        String headData = join(slots[0]);
        String legData = join(slots[1]);
        String bodyData = join(slots[2]);
        Files.write(root.resolve("build/caitrang_part.txt"),
            ("-- INSERT part\n" +
             "INSERT INTO part (id, TYPE, DATA) VALUES\n" +
             "  (" + PART_HEAD + ", 0, '" + headData + "'),\n" +  // head
             "  (" + PART_BODY + ", 1, '" + bodyData + "'),\n" +  // body (TYPE=1)
             "  (" + PART_LEG + ", 2, '" + legData + "');\n" +    // leg  (TYPE=2)
             "-- part: head=" + headData + "\n-- leg=" + legData + "\n-- body=" + bodyData + "\n"
            ).getBytes("UTF-8"));
        // report (khong phai SQL) dan rieng de mysql < caitrang_part.txt duoc nguyen van
        Files.write(root.resolve("build/caitrang_report.txt"), report.toString().getBytes("UTF-8"));
        preview(ci, slots);
        System.out.println("Da ghi build/caitrang_part.txt va build/ct_preview.html");
        System.out.println("head n=" + slots[0].length + " leg n=" + slots[1].length + " body n=" + slots[2].length);
    }

    // ---------------------------------------------------------------- helpers

    /** ten item hien thi tren preview - CO DAU, dat truc tiep trong source */
    static String itemNameFor(String set) {
        if (set.equals("g13_dijiang")) return "Cải trang VLT 021";
        if (set.equals("g13_juitianxuannv")) return "Cải trang VLT 052";
        if (set.equals("g13_qiongqi_nan")) return "Cải trang VLT 090";
        return "Cải trang " + set;
    }

    static String join(String[] a) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) { if (i > 0) sb.append(","); sb.append(a[i]); }
        return sb.append("]").toString();
    }

    static class Piece {
        BufferedImage img;
        double wx, wy;                 // toa do THE GIOI cua goc anh so voi goc frame (am = tren mat dat)
        double ww, wh;                 // kich thuoc THE GIOI cua mieng
        double ppw;                    // px cua frame nay tren 1 world unit (= camH/935)
        int ax, ay, aw, ah;            // bbox tuyet doi trong frame (px)
    }

    /** cat mieng part thu `col` (0=head, 1=leg, 2=body theo CharInfo) tu frame dai dien */
    static Piece cut(int cf, int col) throws Exception {
        String[] af = MAP[cf];
        BufferedImage f = frame(af[0], Integer.parseInt(af[1]));
        int[] b = bbox(f);
        if (b == null) return null;
        double[] cam = cams.get(af[0]);
        double camH = cam[2], camW = camH * 704.0 / 935.0;
        double pxPerWorld = 935.0 / camH;
        double worldH = (b[3] + 1 - b[1]) * camH / 935.0;
        if (worldH < 0.75 * standWorldH || worldH > 1.25 * standWorldH)
            System.out.println("!! canh bao: " + af[0] + "/f" + af[1] + " cao " + String.format("%.0f", worldH) + " world (stand " + String.format("%.0f", standWorldH) + ")");
        double oYpx = b[3] + 1;                       // day artwork (mat dat ung nghiem)
        double oXpx = originXpx(f, b);                // tam trong luong vung than
        // duong cat tinh theo khoang cach world co dinh tu day artwork (doi vao pixel cua frame nay)
        double l1px = oYpx - L1d * pxPerWorld;
        double l2px = oYpx - L2d * pxPerWorld;
        int yA, yB;
        if (col == 0)      { yA = 0;                                  yB = (int) Math.round(l1px + PAD * pxPerWorld); }
        else if (col == 1) { yA = (int) Math.round(l2px - PAD * pxPerWorld); yB = f.getHeight() - 1; }
        else               { yA = (int) Math.round(l1px - PAD * pxPerWorld); yB = (int) Math.round(l2px + PAD * pxPerWorld); }
        yA = Math.max(0, yA); yB = Math.min(f.getHeight() - 1, yB);
        int x0 = f.getWidth(), y0 = f.getHeight(), x1 = -1, y1 = -1;
        for (int y = yA; y <= yB; y++) {
            for (int x = 0; x < f.getWidth(); x++) {
                if (((f.getRGB(x, y) >>> 24) & 0xFF) > ALPHA) {
                    if (x < x0) x0 = x; if (x > x1) x1 = x;
                    if (y < y0) y0 = y; if (y > y1) y1 = y;
                }
            }
        }
        if (x1 < 0) return null;
        Piece p = new Piece();
        p.img = crop(f, new int[]{x0, y0, x1, y1});
        p.ax = x0; p.ay = y0; p.aw = x1 - x0 + 1; p.ah = y1 - y0 + 1;
        p.wx = (x0 - oXpx) * camW / 704.0;     // world unit
        p.wy = (y0 - oYpx) * camH / 935.0;
        p.ww = (x1 - x0 + 1) * camW / 704.0;
        p.wh = (y1 - y0 + 1) * camH / 935.0;
        p.ppw = camH / 935.0;
        return p;
    }

    /** tam trong luong vung THAN de co goc X on dinh giua cac frame (tra ve pixel) */
    static double originXpx(BufferedImage f, int[] b) {
        double H = b[3] - b[1] + 1;
        int ya = (int) (b[3] + 1 - 0.70 * H), yb = (int) (b[3] + 1 - 0.35 * H);
        double sum = 0, w = 0;
        for (int y = Math.max(0, ya); y < Math.min(f.getHeight(), yb); y++)
            for (int x = 0; x < f.getWidth(); x++) {
                int a = (f.getRGB(x, y) >>> 24) & 0xFF;
                if (a > ALPHA) { sum += x * a; w += a; }
            }
        return w > 0 ? sum / w : (b[0] + b[2]) / 2.0;
    }

        static BufferedImage frame(String anim, int idx) throws Exception {
        String k = anim + "/" + idx;
        BufferedImage im = cache.get(k);
        if (im == null) {
            Path p = root.resolve("data/spine_frames/" + SET + "/" + anim + "/f" +
                String.format("%03d", idx) + ".png");
            if (!Files.exists(p)) {
                // vuot so frame cua anim (vd skill chi 18 frame nhung MAP co f19) -> lay frame cuoi
                int last = -1;
                try (DirectoryStream<Path> it = Files.newDirectoryStream(p.getParent())) {
                    for (Path q : it) {
                        String n = q.getFileName().toString();
                        if (n.matches("f\\d+\\.png")) last = Math.max(last, Integer.parseInt(n.substring(1, 4)));
                    }
                }
                if (last < 0) throw new java.io.FileNotFoundException("khong co frame nao cho " + anim);
                p = p.getParent().resolve(String.format("f%03d.png", last));
                System.out.println("!! frame " + anim + "/f" + idx + " khong co -> dung f" + last);
            }
            im = ImageIO.read(p.toFile());
            cache.put(k, im);
        }
        return im;
    }

    static int[] bbox(BufferedImage f) {
        int W = f.getWidth(), H = f.getHeight();
        int[] rc = new int[H], cc = new int[W];
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
                if (((f.getRGB(x, y) >>> 24) & 0xFF) > ALPHA) { rc[y]++; cc[x]++; }
        int x0 = -1, x1 = -1, y0 = -1, y1 = -1;
        for (int y = 0; y < H; y++) if (rc[y] >= 3) { if (y0 < 0) y0 = y; y1 = y; }
        for (int x = 0; x < W; x++) if (cc[x] >= 3) { if (x0 < 0) x0 = x; x1 = x; }
        return x1 < 0 ? null : new int[]{x0, y0, x1, y1};
    }

    static BufferedImage crop(BufferedImage f, int[] b) {
        BufferedImage o = new BufferedImage(b[2] - b[0] + 1, b[3] - b[1] + 1, BufferedImage.TYPE_INT_ARGB);
        o.getGraphics().drawImage(f, 0, 0, b[2] - b[0] + 1, b[3] - b[1] + 1, b[0], b[1], b[2] + 1, b[3] + 1, null);
        return o;
    }

    static BufferedImage scale(BufferedImage src, double s) {
        int nw = Math.max(1, (int) Math.round(src.getWidth() * s));
        int nh = Math.max(1, (int) Math.round(src.getHeight() * s));
        BufferedImage o = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, nw, nh, null);
        g.dispose();
        return o;
    }

    static BufferedImage fit(BufferedImage src, int box) { return fit(src, box, box); }

    static BufferedImage fit(BufferedImage src, int w, int h) {
        double s = Math.min(w / (double) src.getWidth(), h / (double) src.getHeight());
        BufferedImage t = scale(src, s);
        BufferedImage o = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(t, (w - t.getWidth()) / 2, (h - t.getHeight()) / 2, null);
        g.dispose();
        return o;
    }

    static void save(BufferedImage img, int id, int zoom) throws Exception {
        Path p = root.resolve("data/icon_botnet/x" + zoom + "/" + id + ".png");
        Files.createDirectories(p.getParent());
        ImageIO.write(img, "png", p.toFile());
    }

    // --- nhan NRO (render bang part mac dinh) ---
    static int[][][] basePart;

    static void loadBasePart() throws Exception {
        basePart = new int[3][][];
        for (String line : Files.readAllLines(root.resolve("build/part_default.txt"))) {
            Matcher mt = Pattern.compile("^(\\d+)\\t(\\d+)\\t(.+)$").matcher(line.trim());
            if (!mt.find()) continue;
            int type = Integer.parseInt(mt.group(2));
            List<int[]> es = new ArrayList<>();
            Matcher me = Pattern.compile("\\[(-?\\d+),(-?\\d+),(-?\\d+)\\]").matcher(mt.group(3));
            while (me.find()) es.add(new int[]{Integer.parseInt(me.group(1)), Integer.parseInt(me.group(2)), Integer.parseInt(me.group(3))});
            // TYPE: 0=head, 1=body, 2=leg  -> vi tri CharInfo: 0=head, 1=leg, 2=body
            basePart[type == 0 ? 0 : (type == 1 ? 2 : 1)] = es.toArray(new int[0][]);
        }
    }

    /** ve nhan NRO cf voi toa do CUNG Ky thuat voi ban cai trang (CX,CY,SC) de do chieu cao truc tiep */
    static BufferedImage renderBase(int cf, int CX, int CY, int W, int H) {
        BufferedImage c = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = c.createGraphics();
        int[][] slot = {ci[cf][0], ci[cf][1], ci[cf][2]};   // head, leg, body
        for (int i = 0; i < 3; i++) {
            if (slot[i][0] < 0 || slot[i][0] >= basePart[i].length) continue;
            int[] pi = basePart[i][slot[i][0]];
            try {
                BufferedImage im = ImageIO.read(root.resolve("data/icon_botnet/x2/" + pi[0] + ".png").toFile());
                g.drawImage(im, CX + 2 * (slot[i][1] + pi[1]), CY - 2 * slot[i][2] + 2 * pi[2], null);
            } catch (Exception e) {
                System.out.println("!! khong doc duoc anh nhan " + pi[0]);
            }
        }
        g.dispose();
        return c;
    }

    static int baseCharHeight() throws Exception {
        loadBasePart();
        int[] b = bbox(renderBase(0, 400, 600, 800, 800));
        if (b == null) throw new RuntimeException("khong do duoc nhan NRO");
        return b[3] - b[1] + 1;
    }

    /** doc camX/camY/camH tung anim trong meta.json de chuyen pixel <-> the gioi */
    static void parseCams(String meta) {
        Matcher ma = Pattern.compile("\"anim\"\\s*:\\s*\"([^\"]+)\"([\\s\\S]*?)(?=\"anim\"\\s*:|\\]\\s*\\}\\s*$)").matcher(meta);
        while (ma.find()) {
            Matcher mc = Pattern.compile("\"camX\"\\s*:\\s*(-?[\\d.]+)\\s*,\\s*\"camY\"\\s*:\\s*(-?[\\d.]+)\\s*,\\s*\"camH\"\\s*:\\s*(-?[\\d.]+)")
                .matcher(ma.group(2));
            if (mc.find()) cams.put(ma.group(1), new double[]{
                Double.parseDouble(mc.group(1)), Double.parseDouble(mc.group(2)), Double.parseDouble(mc.group(3))});
        }
    }

    // --- CharInfo ---
    static int[][][] parseCharInfo(String src) {
        int st = src.indexOf("CharInfo = new int[33]");
        int en = src.indexOf("\n        };", st);
        String body = src.substring(st, en < 0 ? st + 40000 : en);
        List<int[]> rows = new ArrayList<>();
        Matcher m = Pattern.compile("new\\s+int\\s*\\[4\\]\\s*\\[\\]").matcher(body);
        int[] starts = new int[34];
        int k = 0;
        while (m.find()) starts[k++] = m.start();
        for (int i = 0; i < 33; i++) {
            String seg = body.substring(starts[i], i + 1 < k ? starts[i + 1] : body.length());
            Matcher r = Pattern.compile("new\\s+int\\s*\\[3\\]\\s*\\{\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\}|new\\s+int\\s*\\[3\\](?!\\s*\\{)").matcher(seg);
            int n = 0;
            while (r.find() && n < 4) {
                if (r.group(1) != null) rows.add(new int[]{Integer.parseInt(r.group(1)), Integer.parseInt(r.group(2)), Integer.parseInt(r.group(3))});
                else rows.add(new int[]{0, 0, 0});
                n++;
            }
        }
        int[][][] out = new int[33][4][3];
        for (int i = 0; i < 33; i++)
            for (int j = 0; j < 4; j++)
                out[i][j] = rows.get(i * 4 + j);
        return out;
    }

    /** crop theo bbox roi phong chieu cao = th, giu ty le, canh giua khung W x H */
    static BufferedImage fitH(BufferedImage src, int th) {
        int[] b = bbox(src);
        if (b == null) b = new int[]{0, 0, src.getWidth() - 1, src.getHeight() - 1};
        BufferedImage c = crop(src, b);
        double s = Math.min(th / (double) c.getHeight(), 236.0 / c.getWidth());
        BufferedImage t = scale(c, s);
        BufferedImage o = new BufferedImage(240, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(t, (240 - t.getWidth()) / 2, (200 - t.getHeight()) / 2, null);
        g.dispose();
        return o;
    }

    // --- preview 33 pose voi part moi ---
    static void preview(int[][][] c, String[][] slots) throws Exception {
        int[] partCol = {0, 1, 2};
        int[][] dxs = new int[3][], dys = new int[3][];
        for (int s = 0; s < 3; s++) {
            int n = slots[s].length;
            dxs[s] = new int[n]; dys[s] = new int[n];
            for (int i = 0; i < n; i++) {
                Matcher m = Pattern.compile("\\[(\\d+),(-?\\d+),(-?\\d+)\\]").matcher(slots[s][i]);
                m.find();
                dxs[s][i] = Integer.parseInt(m.group(2));
                dys[s][i] = Integer.parseInt(m.group(3));
            }
        }
        int BW = 1000, BH = 1000, CX = BW / 2, CY = BH - 40, SC = 2;
        int CW = 320, CH = 280;   // o hien thi - 3 cot chung 1 ty le
        BufferedImage baseCv = renderBase(0, CX, CY, BW, BH);
        int[] bb = bbox(baseCv);
        int baseH = (bb == null) ? 0 : bb[3] - bb[1] + 1;
        int standPxH = bbox(frame(STAND_ANIM, 0))[3] - bbox(frame(STAND_ANIM, 0))[1] + 1;
        StringBuilder tbl = new StringBuilder(
            "<table border=1 cellpadding=4 style='border-collapse:collapse;color:#eee;font-size:12px'>" +
            "<tr><th>cf</th><th>nhan NRO (x2 px)</th><th>cai trang (px)</th><th>spine goc xuong muc tieu (px)</th><th>lech ct-spine</th></tr>" +
            "<tr><td><b>tham chieu</b></td><td>nhan NRO = " + baseH + " (khong dung de fit)</td>" +
            "<td colspan=3><b>MUC TIEU = " + TARGET_H + " px x2</b> (spine goc " + standPxH + " px bi han che boi dinh dang part byte ±127)</td></tr>");
        StringBuilder html = new StringBuilder(
            "<style>.ct h2{color:#fc6}.ct h4{margin:14px 0 2px;color:#fc6}.ct .grid{display:grid;grid-template-columns:repeat(2,1fr);gap:4px}" +
            ".ct .c{background:#111;padding:2px;border-radius:6px;text-align:center}" +
            ".ct .pair{display:flex;gap:4px;justify-content:center}" +
            ".ct .c img{image-rendering:pixelated;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px}" +
            ".ct small{color:#ab8;font-size:11px}.ct em{color:#8cf;font-style:normal;font-size:11px}" +
            ".ct table{margin:8px 0}</style><div class='ct'>" +
            "<h2>" + ITEM_NAME + " - chieu cao muc tieu " + TARGET_H + " px x2 (" + SET + ")</h2>" +
            "<p><em>3 cot cung 1 ty le, canh day chung: 1 = nhan NRO (tham chi), 2 = cai trang noi, 3 = frame spine goc quy xuong muc tieu. Lech = cai trang - spine (px)</em></p>");
        for (int cf = 0; cf < 33; cf++) {
            BufferedImage canvas = new BufferedImage(BW, BH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = canvas.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            for (int i = 0; i < 3; i++) {   // 0=head,1=leg,2=body theo thu tu ve
                int idx = c[cf][partCol[i]][0];
                if (idx < 0 || idx >= dxs[i].length || slots[i][idx] == null) continue;
                int id = (i == 0 ? ID_HEAD0 : (i == 1 ? ID_LEG0 : ID_BODY0)) + idx;
                BufferedImage im = ImageIO.read(root.resolve("data/icon_botnet/x2/" + id + ".png").toFile());
                int x = CX + SC * (c[cf][partCol[i]][1] + dxs[i][idx]);
                int y = CY - SC * c[cf][partCol[i]][2] + SC * dys[i][idx];
                g.drawImage(im, x, y, null);
            }
            g.dispose();
            int[] cb = bbox(canvas);
            int ch = (cb == null) ? 0 : cb[3] - cb[1] + 1;
            int cw = (cb == null) ? 0 : cb[2] - cb[0] + 1;
            BufferedImage baseCf = renderBase(cf, CX, CY, BW, BH);
            int[] bbb = bbox(baseCf);
            int bh = (bbb == null) ? 0 : bbb[3] - bbb[1] + 1;
            int bw = (bbb == null) ? 0 : bbb[2] - bbb[0] + 1;
            BufferedImage src = frame(MAP[cf][0], Integer.parseInt(MAP[cf][1]));
            int[] sbb = bbox(src);
            int shRaw = (sbb == null) ? 0 : sbb[3] - sbb[1] + 1;
            int swRaw = (sbb == null) ? 0 : sbb[2] - sbb[0] + 1;
            // frame -2/-15... duoc camera xa hon -> quy ve ty le pixel cua stand-1/f0 (cung don vi the gioi)
            double k = cams.get(MAP[cf][0])[2] / cams.get(STAND_ANIM)[2];
            int sh = (int) Math.round(shRaw * k), sw = (int) Math.round(swRaw * k);
            double tr = (double) TARGET_H / standPxH;                        // spine goc xuong muc tieu
            int shT = (int) Math.round(sh * tr), swT = (int) Math.round(sw * tr);
            // 1 ty le chung cho ca 3 cot trong hang -> doi chieu chieu cao truc tiep
            int maxW = Math.max(cw, Math.max(bw, swT));
            int maxH = Math.max(ch, Math.max(bh, shT));
            double s = Math.min((CW - 12) / (double) Math.max(1, maxW), (CH - 20) / (double) Math.max(1, maxH));
            tbl.append("<tr><td>cf ").append(cf).append("</td><td>").append(bh)
               .append("</td><td>").append(ch).append("</td><td>").append(shT)
               .append(" (goc ").append(shRaw).append(")</td><td>").append(ch - shT).append("</td></tr>");
            html.append("<div class='c'><div class='pair'>")
                .append("<div><img src='data:image/png;base64,").append(b64(cell(baseCf, bbb, s, CW, CH)))
                .append("'><br><small>nhan goc cf ").append(cf).append(" (h=").append(bh).append(")</small></div>")
                .append("<div><img src='data:image/png;base64,").append(b64(cell(canvas, cb, s, CW, CH)))
                .append("'><br><small>cai trang cf ").append(cf).append(" (h=").append(ch).append(")</small></div>")
                .append("<div><img src='data:image/png;base64,").append(b64(cell(src, sbb, s * k * tr, CW, CH)))
                .append("'><br><small>spine ").append(MAP[cf][0]).append("/f").append(MAP[cf][1]).append(" (h=").append(shT).append(", goc ").append(shRaw).append(")</small></div>")
                .append("</div></div>");
        }
        html.append("</div></div>");
        String out = html.toString() + tbl.toString() + "</table>";
        Files.write(root.resolve("build/ct_preview.html"), out.getBytes("UTF-8"));
        System.out.println("CHUAN spine " + STAND_ANIM + "/f0 = " + standPxH + " px ; nhan NRO = " + baseH + " x2px (tham chi) -> xem build/ct_preview.html");
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }

    /** cat bbox roi phong bang ty le s, canh duoi (chan chung 1 day) giua o W x H */
    static BufferedImage cell(BufferedImage src, int[] b, double s, int W, int H) {
        BufferedImage c = (b == null) ? src : crop(src, b);
        BufferedImage t = scale(c, s);
        BufferedImage o = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(t, (W - t.getWidth()) / 2, H - 8 - t.getHeight(), null);
        g.dispose();
        return o;
    }
}
