import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;

/**
 * CmpMirror - doi chieu anim "-1" va "-2" cua set spine: xem "-2" co phai la "-1" dao nguoc
 * (huong trai) hay khac. Neu MSE(mirror(-1), -2) nho -> chi can bake 1 huong, client lat bang trans.
 *
 * Cach dung: java -cp build/tools CmpMirror <root_BeMeoGaming> <set>
 */
public class CmpMirror {
    public static void main(String[] args) throws Exception {
        Path setDir = Paths.get(args[0], "data/spine_frames", args[1]);
        String[] bases = {"stand", "run", "attack", "dead", "skill"};
        for (String b : bases) {
            Path d1 = setDir.resolve(b + "-1"), d2 = setDir.resolve(b + "-2");
            if (!Files.isDirectory(d1) || !Files.isDirectory(d2)) continue;
            List<Path> f1 = files(d1), f2 = files(d2);
            int n = Math.min(f1.size(), f2.size());
            double same = 0, mir = 0;
            for (int i = 0; i < n; i++) {
                BufferedImage a = norm(ImageIO.read(f1.get(i).toFile()));
                BufferedImage c = norm(ImageIO.read(f2.get(i).toFile()));
                same += mse(a, c, false);
                mir += mse(a, c, true);
            }
            System.out.printf("%-8s n=%d  mse(same)=%8.1f  mse(mirror)=%8.1f  -> %s%n",
                b, n, same / n, mir / n, (mir / n < same / n * 0.6) ? "-2 = dao nguoc cua -1" : "-2 KHAC -1");
        }
    }

    static List<Path> files(Path d) throws Exception {
        List<Path> l = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(d, "f*.png")) { for (Path p : ds) l.add(p); }
        l.sort(Comparator.comparing(p -> p.getFileName().toString()));
        return l;
    }

    /** crop theo alpha roi scale ve 64x64 -> so sanh duoc du khac camera/kich thuoc */
    static BufferedImage norm(BufferedImage src) {
        int x0 = src.getWidth(), y0 = src.getHeight(), x1 = -1, y1 = -1;
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (((src.getRGB(x, y) >>> 24) & 0xFF) > 8) {
                    if (x < x0) x0 = x; if (x > x1) x1 = x;
                    if (y < y0) y0 = y; if (y > y1) y1 = y;
                }
        if (x1 < 0) return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        java.awt.Image im = src.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = out.createGraphics();
        g.drawImage(src, 0, 0, 64, 64, x0, y0, x1 + 1, y1 + 1, null);
        g.dispose();
        return out;
    }

    static double mse(BufferedImage a, BufferedImage b, boolean mirror) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) return 1e9;
        long sum = 0; long cnt = 0;
        int w = a.getWidth(), h = a.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int xa = mirror ? (w - 1 - x) : x;
                int pa = a.getRGB(xa, y), pb = b.getRGB(x, y);
                for (int k = 0; k < 4; k++) {
                    int da = ((pa >> (k * 8)) & 255) - ((pb >> (k * 8)) & 255);
                    sum += (long) da * da; cnt++;
                }
            }
        }
        return sum / (double) cnt;
    }
}
