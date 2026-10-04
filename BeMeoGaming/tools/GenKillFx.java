import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GenKillFx - dua frame PNG (data/spine_frames/Full_FX) vao pipeline DataEffect cua server.
 *
 * Client (EffectData.readData / readEffect, nhan msg -66) doc:
 *   u8 N, N x (s8 id, u8 x, u8 y, u8 w, u8 h)     // rect trong sheet (u8 -> sheet logical <= 256)
 *   u16be H, H x (s8 parts, parts x (s16be dx, s16be dy, s8 sprite))
 *   u16be count, count x u16be frameIdx, u16 0x3232
 * DataEffect GIONG NHAU o ca x1..x4; client nhan zoomLevel va mGraphics.drawRegion
 * tu nhan x zoomLevel -> ImgEffect moi zoom = sheet x zoom (x1..x4).
 * Tick client = 50/s (FixedUpdate 0.02s, Main.count khong reset) -> seq 50 entry/s.
 *
 * Ids 110..121: trong khoang 101..199 (client dung readData2 = format giong readData),
 * khong trung builtin client (100..109), khong trung id server hien co.
 *
 * Cach dung:  javac -d build/tools tools/GenKillFx.java
 *             java -cp build/tools GenKillFx <root_BeMeoGaming>
 * Output: Eff/effect/x{1..4}/data/DataEffect_{id} + Eff/effect/x{z}/img/ImgEffect_{id}.png
 *         + data/spine_frames/_gen_killfx.html (preview anchor)
 */
public class GenKillFx {

    static final int[] IDS = {110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121};
    static final int T = 50;       // tick client (s)
    static final int SHEET = 256;  // gioi han logical (rect u8)
    static final int MAX_KEEP = 16;

    static class Anim {
        String name;
        double dur;
        int count;
        double[] times;
        double camX, camY, camH;
        int w, h;
    }

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(args.length > 0 ? args[0] : ".");
        Path src = root.resolve("data/spine_frames/Full_FX");
        Path outRoot = root.resolve("Eff/effect");
        String meta = new String(Files.readAllBytes(src.resolve("meta.json")), "UTF-8");
        List<Anim> anims = parseMeta(meta);
        System.out.println("Tim thay " + anims.size() + " anim");
        int used = Math.min(anims.size(), IDS.length);
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset='utf-8'><title>GenKillFx</title><style>")
            .append("body{background:#1e2530;color:#eee;font-family:sans-serif;margin:14px}")
            .append(".row{display:flex;gap:14px;margin-bottom:18px;flex-wrap:wrap}")
            .append(".cell{text-align:center;background:#111;padding:8px;border-radius:8px}")
            .append(".cell img{image-rendering:pixelated;background:")
            .append("repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px}")
            .append("h2{font-size:14px;margin:4px 0}small{color:#8ab}</style></head><body>");
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < used; i++) {
            process(src, outRoot, anims.get(i), IDS[i], html, rows);
        }
        html.append("</body></html>");
        Files.createDirectories(root.resolve("data/spine_frames"));
        Files.write(root.resolve("data/spine_frames/_gen_killfx.html"), html.toString().getBytes("UTF-8"));
        System.out.println();
        System.out.println("ID   | ANIM                | FRM | GRID     | CELL     | DX,DY    | SEQ | DUR");
        for (String r : rows) System.out.println(r);
        System.out.println("Da ghi data/spine_frames/_gen_killfx.html");
    }

    // ---------- meta.json (regex, khong can JSON lib) ----------
    static List<Anim> parseMeta(String meta) {
        List<Anim> out = new ArrayList<>();
        Matcher m = Pattern.compile("\"anim\"\\s*:\\s*\"([^\"]+)\"").matcher(meta);
        List<int[]> spans = new ArrayList<>();
        List<String> names = new ArrayList<>();
        while (m.find()) { spans.add(new int[]{m.start(), m.end()}); names.add(m.group(1)); }
        Matcher mFps = Pattern.compile("\"fps\"\\s*:\\s*(\\d+)").matcher(meta);
        double fps = mFps.find() ? Double.parseDouble(mFps.group(1)) : 12;
        for (int i = 0; i < spans.size(); i++) {
            int from = spans.get(i)[1];
            int to = i + 1 < spans.size() ? spans.get(i + 1)[0] : meta.length();
            String seg = meta.substring(from, Math.min(to, from + 4000));
            Anim a = new Anim();
            a.name = names.get(i);
            a.dur = num(seg, "\"dur\"", 0);
            a.count = (int) num(seg, "\"count\"", 0);
            Matcher mt = Pattern.compile("\"times\"\\s*:\\s*\\[([^\\]]*)\\]").matcher(seg);
            if (mt.find()) {
                String[] parts = mt.group(1).split(",");
                double[] t = new double[parts.length];
                for (int k = 0; k < parts.length; k++) t[k] = Double.parseDouble(parts[k].trim());
                a.times = t;
            }
            Matcher mc = Pattern.compile("\"cam\"\\s*:\\s*\\{([^}]*)\\}").matcher(seg);
            if (mc.find()) {
                String c = mc.group(1);
                a.camX = num(c, "\"camX\"", 0);
                a.camY = num(c, "\"camY\"", 0);
                a.camH = num(c, "\"camH\"", 800);
                a.w = (int) num(c, "\"w\"", 704);
                a.h = (int) num(c, "\"h\"", 890);
            }
            if (a.times == null && a.count > 0) {
                a.times = new double[a.count];
                for (int k = 0; k < a.count; k++) a.times[k] = k / fps;
            }
            out.add(a);
        }
        return out;
    }

    static double num(String s, String key, double def) {
        Matcher m = Pattern.compile(Pattern.quote(key) + "\\s*:\\s*(-?[\\d.eE+]+)").matcher(s);
        return m.find() ? Double.parseDouble(m.group(1)) : def;
    }

    static String sanitize(String n) { return n.replaceAll("[^\\w.-]", "_"); }

    // ---------- xu ly 1 anim ----------
    static void process(Path src, Path outRoot, Anim a, int id, StringBuilder html, List<String> rows)
            throws Exception {
        Path dir = src.resolve(sanitize(a.name));
        List<Path> pngs = new ArrayList<>();
        if (Files.isDirectory(dir)) {
            Files.newDirectoryStream(dir, p -> p.toString().endsWith(".png"))
                .forEach(pngs::add);
        }
        pngs.sort(Comparator.comparing(p -> p.getFileName().toString()));
        int n = pngs.size();
        if (n == 0) { rows.add(id + "   | " + a.name + " | KHONG CO FRAME"); return; }

        // doc frame + bbox union
        BufferedImage[] frames = new BufferedImage[n];
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
        for (int i = 0; i < n; i++) {
            frames[i] = ImageIO.read(pngs.get(i).toFile());
            BufferedImage f = frames[i];
            int W = f.getWidth(), H = f.getHeight();
            int[] px = f.getRGB(0, 0, W, H, null, 0, W);
            for (int y = 0; y < H; y++) {
                int row = y * W;
                for (int x = 0; x < W; x++) {
                    if (((px[row + x] >>> 24) & 0xFF) > 16) {
                        if (x < minX) minX = x;
                        if (x > maxX) maxX = x;
                        if (y < minY) minY = y;
                        if (y > maxY) maxY = y;
                    }
                }
            }
        }
        if (maxX < 0) { rows.add(id + "   | " + a.name + " | FRAME RONG"); return; }
        int cropX = Math.max(0, minX - 1), cropY = Math.max(0, minY - 1);
        int cropW = Math.min(frames[0].getWidth(), maxX + 2) - cropX;
        int cropH = Math.min(frames[0].getHeight(), maxY + 2) - cropY;

        // chon frame giu (deu, co cuoi)
        int keep = Math.min(n, MAX_KEEP);
        int[] idx = new int[keep];
        for (int j = 0; j < keep; j++) {
            idx[j] = keep == 1 ? 0 : (int) Math.round(j * (n - 1) / (double) (keep - 1));
            if (j > 0 && idx[j] <= idx[j - 1]) idx[j] = idx[j - 1] + 1;
        }
        idx[keep - 1] = n - 1;

        // grid toi uu trong 256x256
        double bestS = -1; int bestCols = 1, bestRows = 1;
        for (int cols = 1; cols <= keep; cols++) {
            int r = (keep + cols - 1) / cols;
            double s = Math.min(SHEET / (double) cols / cropW, SHEET / (double) r / cropH);
            if (s > bestS + 1e-9) { bestS = s; bestCols = cols; bestRows = r; }
        }
        int cols = bestCols, rowsN = bestRows;
        double scale = Math.min(SHEET / (double) (cropW * cols), SHEET / (double) (cropH * rowsN));
        int fw = Math.max(1, (int) Math.floor(cropW * scale));
        int fh = Math.max(1, (int) Math.floor(cropH * scale));
        double sx = fw / (double) cropW, sy = fh / (double) cropH;
        int sheetW = cols * fw, sheetH = rowsN * fh;

        // pack sheet
        BufferedImage sheet = new BufferedImage(sheetW, sheetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sg = sheet.createGraphics();
        sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        sg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage peak = null; long peakPx = -1;
        for (int j = 0; j < keep; j++) {
            BufferedImage f = frames[idx[j]];
            int col = j % cols, row = j / cols;
            sg.drawImage(f.getSubimage(cropX, cropY, cropW, cropH), col * fw, row * fh, fw, fh, null);
            long p = countPx(f, cropX, cropY, cropW, cropH);
            if (p > peakPx) { peakPx = p; peak = f; }
        }
        sg.dispose();

        // anchor = splice origin (0,0) dua tren camera fit (camX,camY = center bbox)
        double sCanvas = a.h / (double) a.camH;
        double ax = a.w / 2.0 - a.camX * sCanvas;
        double ay = a.h / 2.0 - a.camY * sCanvas;
        double acx = (ax - cropX) * sx, acy = (ay - cropY) * sy;
        boolean fallback = acx < -0.4 * fw || acx > 1.4 * fw || acy < -0.4 * fh || acy > 1.4 * fh;
        if (fallback) { acx = fw / 2.0; acy = fh / 2.0; }
        int dx = -(int) Math.round(acx), dy = -(int) Math.round(acy);

        // seq: tick 50/s theo thoi gian goc
        int[] seqList = new int[600]; int seqN = 0;
        for (int j = 0; j < keep; j++) {
            double t0 = at(a.times, idx[j], 0);
            double t1 = (j + 1 < keep) ? at(a.times, idx[j + 1], a.dur) : Math.max(a.dur, t0 + 0.001);
            int r0 = (int) Math.round(t0 * T), r1 = (int) Math.round(t1 * T);
            int reps = Math.max(1, r1 - r0);
            for (int r = 0; r < reps && seqN < seqList.length; r++) seqList[seqN++] = j;
        }
        seqList[seqN++] = keep - 1; // pad: frame cuoi hien du 1 tick truoc khi remove

        byte[] data = buildData(cols, fw, fh, keep, dx, dy, seqList, seqN);

        for (int z = 1; z <= 4; z++) {
            Path dataDir = outRoot.resolve("x" + z + "/data");
            Path imgDir = outRoot.resolve("x" + z + "/img");
            Files.createDirectories(dataDir);
            Files.createDirectories(imgDir);
            Files.write(dataDir.resolve("DataEffect_" + id), data);
            BufferedImage img = z == 1 ? sheet : scale(sheetW * z, sheetH * z, sheet);
            ImageIO.write(img, "png", imgDir.resolve("ImgEffect_" + id + ".png").toFile());
        }

        // preview: frame nhieu pixel nhat + cross tai anchor
        BufferedImage mock = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D mg = mock.createGraphics();
        mg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        mg.drawImage(peak.getSubimage(cropX, cropY, cropW, cropH), 0, 0, fw, fh, null);
        mg.setColor(Color.RED);
        mg.drawLine(0, (int) acy, fw, (int) acy);
        mg.drawLine((int) acx, 0, (int) acx, fh);
        mg.dispose();
        html.append("<div class='row'><div class='cell'><h2>").append(id).append(" ").append(a.name)
            .append("</h2><img src='data:image/png;base64,").append(b64(mock))
            .append("' style='width:").append(fw * 2).append("px'><br><small>anchor (cross do) + frame peak</small></div>")
            .append("<div class='cell'><h2>sheet ").append(sheetW).append("x").append(sheetH)
            .append("</h2><img src='data:image/png;base64,").append(b64(sheet))
            .append("' style='width:").append(Math.min(sheetW * 2, 520)).append("px'><br><small>")
            .append(keep).append(" frame, grid ").append(cols).append("x").append(rowsN)
            .append("</small></div></div>");

        rows.add(String.format("%-4d | %-19s | %3d | %dx%d     | %3dx%-3d  | %4d,%-4d | %3d | %.2fs%s",
            id, a.name, keep, cols, rowsN, fw, fh, dx, dy, seqN, a.dur, fallback ? "  ANCHOR-FALLBACK" : ""));
    }

    static double at(double[] t, int i, double def) {
        return (t != null && i < t.length) ? t[i] : def;
    }

    static long countPx(BufferedImage f, int x0, int y0, int w, int h) {
        long n = 0;
        for (int y = y0; y < y0 + h; y++)
            for (int x = x0; x < x0 + w; x++)
                if (((f.getRGB(x, y) >>> 24) & 0xFF) > 16) n++;
        return n;
    }

    static BufferedImage scale(int w, int h, BufferedImage src) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    // ---------- binary DataEffect ----------
    static byte[] buildData(int cols, int fw, int fh, int keep, int dx, int dy, int[] seq, int seqN)
            throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        w8(o, keep);
        for (int i = 0; i < keep; i++) {
            w8(o, i);
            w8(o, (i % cols) * fw);
            w8(o, (i / cols) * fh);
            w8(o, fw);
            w8(o, fh);
        }
        w8(o, 0);
        w8(o, keep);
        for (int i = 0; i < keep; i++) {
            w8(o, 1);
            w16(o, dx);
            w16(o, dy);
            w8(o, i);
        }
        w16(o, seqN);
        for (int i = 0; i < seqN; i++) w16(o, seq[i]);
        w16(o, 0x3232);
        return o.toByteArray();
    }

    static void w8(ByteArrayOutputStream o, int v) { o.write(v & 0xFF); }

    static void w16(ByteArrayOutputStream o, int v) {
        o.write((v >> 8) & 0xFF);
        o.write(v & 0xFF);
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }
}
