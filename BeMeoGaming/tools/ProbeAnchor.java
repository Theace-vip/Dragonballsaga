import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * ProbeAnchor - do neo toa do cua frame spine da export:
 *   - bbox alpha o pixel screen
 *   - doi sang toa do the gioi bang camX/camY/camH trong meta.json
 *   - in center X / bottom Y the gioi de xac dinh goc (cx,cy) on dinh
 *
 * Cach dung: java -cp build/tools ProbeAnchor <root_BeMeoGaming> <set>
 */
public class ProbeAnchor {
    public static void main(String[] args) throws Exception {
        Path setDir = Paths.get(args[0], "data/spine_frames", args[1]);
        if (args.length > 2 && "map".equals(args[2])) {
            asciiMap(Paths.get(setDir.resolve(args[3]).toString()));
            return;
        }
        String meta = new String(Files.readAllBytes(setDir.resolve("meta.json")), "UTF-8");

        // map anim -> cam
        Map<String, double[]> cams = new HashMap<>();
        Matcher ma = Pattern.compile("\"anim\"\\s*:\\s*\"([^\"]+)\"([\\s\\S]*?)(?=\"anim\"\\s*:|\\]\\s*\\}\\s*$)").matcher(meta);
        while (ma.find()) {
            String name = ma.group(1);
            Matcher mc = Pattern.compile("\"camX\"\\s*:\\s*(-?[\\d.]+)\\s*,\\s*\"camY\"\\s*:\\s*(-?[\\d.]+)\\s*,\\s*\"camH\"\\s*:\\s*(-?[\\d.]+)").matcher(ma.group(2));
            if (mc.find()) {
                cams.put(name, new double[]{Double.parseDouble(mc.group(1)), Double.parseDouble(mc.group(2)), Double.parseDouble(mc.group(3))});
            }
        }
        double w = 704, h = 935;
        System.out.printf("%-12s %-4s | %5s %5s %5s %5s | %8s %8s | %8s %8s%n",
            "anim", "f", "x0", "y0", "x1", "y1", "cxWorld", "cyWorld", "botWorld", "ctrXw");
        List<String> anims = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(setDir)) {
            for (Path p : ds) if (Files.isDirectory(p)) anims.add(p.getFileName().toString());
        }
        Collections.sort(anims);
        for (String anim : anims) {
            double[] c = cams.get(anim);
            if (c == null) continue;
            double camW = c[2] * w / h;
            List<Path> fs = new ArrayList<>();
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(setDir.resolve(anim), "f*.png")) {
                for (Path p : ds) fs.add(p);
            }
            fs.sort(Comparator.comparing(p -> p.getFileName().toString()));
            for (int i = 0; i < fs.size(); i++) {
                if (i % 4 != 0 && !"stand-1".equals(anim)) continue;
                BufferedImage src = ImageIO.read(fs.get(i).toFile());
                int W = src.getWidth(), H = src.getHeight();
                int[] rowCount = new int[H], colCount = new int[W];
                for (int y = 0; y < H; y++)
                    for (int x = 0; x < W; x++)
                        if (((src.getRGB(x, y) >>> 24) & 0xFF) > 128) { rowCount[y]++; colCount[x]++; }
                int x0 = -1, x1 = -1, y0 = -1, y1 = -1;
                for (int y = 0; y < H; y++) if (rowCount[y] >= 3) { if (y0 < 0) y0 = y; y1 = y; }
                for (int x = 0; x < W; x++) if (colCount[x] >= 3) { if (x0 < 0) x0 = x; x1 = x; }
                if (x1 < 0) continue;
                double sx0 = c[0] + x0 * camW / w, sx1 = c[0] + (x1 + 1) * camW / w;
                double sy0 = c[1] + y0 * c[2] / h, sy1 = c[1] + (y1 + 1) * c[2] / h;
                System.out.printf("%-12s %-4d | %5d %5d %5d %5d | %8.1f %8.1f | %8.1f %8.1f  h=%d%n",
                    anim, i, x0, y0, x1, y1, (sx0 + sx1) / 2, (sy0 + sy1) / 2, sy1, (sx0 + sx1) / 2, y1 - y0 + 1);
            }
        }
    }

    static void asciiMap(Path f) throws Exception {
        BufferedImage src = ImageIO.read(f.toFile());
        int W = src.getWidth(), H = src.getHeight();
        int CW = 8, CH = 12;
        System.out.println(f + "  " + W + "x" + H + "  (1 ky tu = 8x12 px; '#'>=40px alpha>128, '+'>=8, '.'<8)");
        StringBuilder sb = new StringBuilder("    ");
        for (int cx = 0; cx < W / CW; cx++) sb.append(cx % 10);
        sb.append('\n');
        for (int cy = 0; cy < H / CH; cy++) {
            sb.append(String.format("%3d ", cy * CH));
            for (int cx = 0; cx < W / CW; cx++) {
                int n = 0, n2 = 0;
                for (int y = cy * CH; y < Math.min(H, (cy + 1) * CH); y++)
                    for (int x = cx * CW; x < Math.min(W, (cx + 1) * CW); x++) {
                        int a = (src.getRGB(x, y) >>> 24) & 0xFF;
                        if (a > 128) n++;
                        if (a > 8) n2++;
                    }
                sb.append(n >= 40 ? '#' : (n2 >= 8 ? '+' : '.'));
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }
}
