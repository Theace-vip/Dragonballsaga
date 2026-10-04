import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ThumbSheet - tao fragment contact-sheet spine o dang base64 thumbnail (da crop theo alpha)
 * vi preview chi phuc file HTML da dang ky (khong phuc file anh rieng).
 *
 * Doc: data/spine_frames/<set>/meta.json + <set>/<anim>/fNNN.png
 * Ghi: data/spine_frames/_sheet_frag.html  (CharFrameSheet se ghep vao _match.html)
 *
 * Cach dung: java -cp build/tools ThumbSheet <root_BeMeoGaming> <set>
 */
public class ThumbSheet {

    static final int TW = 148, TH = 156; // kich thuoc thumbnail toi da

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(args[0]);
        String set = args.length > 1 ? args[1] : "g13_juitianxuannv";
        Path setDir = root.resolve("data/spine_frames/" + set);
        String meta = new String(Files.readAllBytes(setDir.resolve("meta.json")), "UTF-8");

        int fps = 12;
        Matcher mf = Pattern.compile("\"fps\"\\s*:\\s*(\\d+)").matcher(meta);
        if (mf.find()) fps = Integer.parseInt(mf.group(1));

        // times theo tung anim: "anim": "x" ... "times": [ ... ]
        Map<String, double[]> times = new LinkedHashMap<>();
        Matcher ma = Pattern.compile("\"anim\"\\s*:\\s*\"([^\"]+)\"([\\s\\S]*?)(?=\"anim\"\\s*:|\\]\\s*\\}\\s*$)").matcher(meta);
        while (ma.find()) {
            String name = ma.group(1);
            Matcher mt = Pattern.compile("\"times\"\\s*:\\s*\\[([\\s\\S]*?)\\]").matcher(ma.group(2));
            List<Double> ts = new ArrayList<>();
            if (mt.find()) {
                Matcher mv = Pattern.compile("-?\\d+(?:\\.\\d+)?").matcher(mt.group(1));
                while (mv.find()) ts.add(Double.parseDouble(mv.group()));
            }
            double[] a = new double[ts.size()];
            for (int i = 0; i < a.length; i++) a[i] = ts.get(i);
            times.put(name, a);
        }

        List<String> anims = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(setDir)) {
            for (Path p : ds) if (Files.isDirectory(p)) anims.add(p.getFileName().toString());
        }
        Collections.sort(anims);

        StringBuilder html = new StringBuilder();
        html.append("<style>\n.spine h4{margin:14px 0 2px;color:#8cf}\n")
            .append(".spine .row{display:flex;flex-wrap:wrap;gap:3px;margin-bottom:6px}\n")
            .append(".spine .f{background:#000;text-align:center;padding:1px;border-radius:4px}\n")
            .append(".spine .f img{display:block;image-rendering:pixelated;width:").append(TW)
            .append("px;height:").append(TH).append("px;object-fit:contain;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 50%) 50%/12px 12px}\n")
            .append(".spine small{color:#8ab;font-size:10px;display:block}\n</style>")
            .append("<div class='spine'><h2>spine ").append(set)
            .append(" - fps ").append(fps).append(", 704x935 / frame (da crop alpha)</h2>");

        int total = 0;
        Map<String, BufferedImage> cache = new HashMap<>();
        for (String anim : anims) {
            List<Path> fs = new ArrayList<>();
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(setDir.resolve(anim), "f*.png")) {
                for (Path p : ds) fs.add(p);
            }
            fs.sort(Comparator.comparing(p -> p.getFileName().toString()));
            double[] ts = times.getOrDefault(anim, new double[0]);
            html.append("<h4>").append(anim).append(" (").append(fs.size()).append("f)</h4><div class='row'>");
            for (int i = 0; i < fs.size(); i++) {
                BufferedImage src = ImageIO.read(fs.get(i).toFile());
                if (src == null) continue;
                BufferedImage thumb = thumb(src);
                double t = (i < ts.length) ? ts[i] : (i / (double) fps);
                html.append("<div class='f'><img src='data:image/png;base64,").append(b64(thumb))
                    .append("'><small>").append(anim).append("<br>f").append(i)
                    .append(" @").append(String.format(Locale.US, "%.2f", t)).append("s</small></div>");
                total++;
            }
            html.append("</div>");
        }
        html.append("</div>");

        Files.write(root.resolve("data/spine_frames/_sheet_frag.html"), html.toString().getBytes("UTF-8"));
        System.out.println("ThumbSheet: " + anims.size() + " anim, " + total + " frame -> _sheet_frag.html "
            + (Files.size(root.resolve("data/spine_frames/_sheet_frag.html")) / 1024) + " KB");
    }

    /** crop theo alpha roi scale vua khung TW x TH (khong phong to) */
    static BufferedImage thumb(BufferedImage src) {
        int x0 = src.getWidth(), y0 = src.getHeight(), x1 = -1, y1 = -1;
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                if (((src.getRGB(x, y) >>> 24) & 0xFF) > 8) {
                    if (x < x0) x0 = x;
                    if (x > x1) x1 = x;
                    if (y < y0) y0 = y;
                    if (y > y1) y1 = y;
                }
            }
        }
        if (x1 < 0) { x0 = 0; y0 = 0; x1 = src.getWidth() - 1; y1 = src.getHeight() - 1; }
        int pad = 6;
        x0 = Math.max(0, x0 - pad); y0 = Math.max(0, y0 - pad);
        x1 = Math.min(src.getWidth() - 1, x1 + pad); y1 = Math.min(src.getHeight() - 1, y1 + pad);
        int cw = x1 - x0 + 1, chh = y1 - y0 + 1;
        double s = Math.min(1.0, Math.min(TW / (double) cw, TH / (double) chh));
        int nw = Math.max(1, (int) Math.round(cw * s)), nh = Math.max(1, (int) Math.round(chh * s));
        BufferedImage out = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, nw, nh, x0, y0, x1 + 1, y1 + 1, null);
        g.dispose();
        return out;
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }
}
