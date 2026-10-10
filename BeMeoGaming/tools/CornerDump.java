import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CornerDump - in ban do ASCII vung goc anh + dem mau rieng biet, de biet nen la
 * "ban co caro ve san" (2 mau lap theo o vuong) hay la trong suot that.
 *
 * Dung: java CornerDump <anh.png> [kich thuoc vung] [buoc lay mau]
 */
public class CornerDump {
    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        BufferedImage im = ImageIO.read(f);
        int[] rect = {0, 0, 400, 200};
        int step = 8;
        System.out.printf("%s %dx%d%n", f.getName(), im.getWidth(), im.getHeight());
        System.out.println("Vung goc " + rect[2] + "x" + rect[3] + ", buoc " + step + "px:");
        for (int y = rect[1]; y < rect[1] + rect[3]; y += step) {
            StringBuilder sb = new StringBuilder();
            for (int x = rect[0]; x < rect[0] + rect[2]; x += step) {
                int rgb = im.getRGB(x, y);
                int a = rgb >>> 24, r = (rgb >> 16) & 255, g = (rgb >> 8) & 255, b = rgb & 255;
                if (a <= 8) {
                    sb.append('.');
                } else if (Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b)) > 18) {
                    sb.append('#'); // co mau -> vat the
                } else if (r < 120) {
                    sb.append('D'); // xam dam
                } else {
                    sb.append(r > 230 ? 'W' : 'g'); // W = trang, g = xam nhe
                }
            }
            System.out.println(sb);
        }
        Map<String, Integer> dem = new LinkedHashMap<>();
        for (int y = 0; y < Math.min(200, im.getHeight()); y++) {
            for (int x = 0; x < Math.min(400, im.getWidth()); x++) {
                int rgb = im.getRGB(x, y);
                if ((rgb >>> 24) <= 8) {
                    continue;
                }
                String k = String.format("#%06X", rgb & 0xFFFFFF);
                dem.merge(k, 1, Integer::sum);
            }
        }
        System.out.println("Mau rieng biet trong vung goc: " + dem.size());
        dem.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).limit(6)
                .forEach(e -> System.out.printf("  %s x%d%n", e.getKey(), e.getValue()));
    }
}
