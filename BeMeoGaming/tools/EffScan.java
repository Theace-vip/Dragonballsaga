import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;

/**
 * Quet toan bo DataEffect de reverse format:
 * - Section1 (DA XAC MINH): byte0=N, N*5 byte [idx][x][y][w][h] tu offset 1
 * - Section2/3: chua xac dinh - scan tu 1+5N, tim quy luat chung qua nhieu file
 * Usage: java EffScan <dir_data> <dir_img>
 */
public class EffScan {

    static int u8(byte[] d, int i) { return d[i] & 0xFF; }

    public static void main(String[] a) throws Exception {
        Path dataDir = Paths.get(a[0]);
        Path imgDir = Paths.get(a[1]);
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dataDir, "DataEffect_*")) {
            for (Path p : ds) files.add(p);
        }
        files.sort(Comparator.comparingLong(p -> p.toFile().length()));

        int rectOk = 0, rectBad = 0, s2HeaderOk = 0;
        for (Path p : files) {
            String name = p.getFileName().toString().replace("DataEffect_", "");
            byte[] d = Files.readAllBytes(p);
            // section1
            int n = u8(d, 0);
            int s2off = 1 + 5 * n;
            StringBuilder bad = new StringBuilder();
            // bounds check vs image
            int iw = -1, ih = -1;
            Path img = imgDir.resolve("ImgEffect_" + name + ".png");
            if (Files.exists(img)) {
                BufferedImage bi = ImageIO.read(img.toFile());
                iw = bi.getWidth(); ih = bi.getHeight();
            }
            boolean ok = true;
            if (iw > 0) {
                for (int f = 0; f < n; f++) {
                    int o = 1 + f * 5;
                    int x = u8(d, o + 1), y = u8(d, o + 2), w = u8(d, o + 3), h = u8(d, o + 4);
                    if (x + w > iw || y + h > ih) {
                        ok = false;
                        bad.append(String.format(" f%d=(%d,%d,%d,%d)", f, x, y, w, h));
                    }
                }
            }
            if (ok) rectOk++; else { rectBad++; }

            // section2 header check
            int h0 = s2off < d.length ? u8(d, s2off) : -1;
            int h1 = s2off + 1 < d.length ? u8(d, s2off + 1) : -1;
            boolean s2ok = (h0 == 0);
            if (s2ok) s2HeaderOk++;

            // tail 12 byte
            StringBuilder tail = new StringBuilder();
            int ts = Math.max(0, d.length - 14);
            for (int i = ts; i < d.length; i++) tail.append(String.format("%02x ", d[i]));

            System.out.printf("%-6s N=%d len=%-4d img=%dx%d s2off=%-3d hdr=%02x %02x %-4s tail: %s%s%n",
                    name, n, d.length, iw, ih, s2off, h0, h1, s2ok ? "OK" : "??", tail, bad);
        }
        System.out.println("---");
        System.out.println("rect bounds: OK=" + rectOk + " BAD=" + rectBad);
        System.out.println("s2 header byte0==0: " + s2HeaderOk + "/" + files.size());
    }
}
