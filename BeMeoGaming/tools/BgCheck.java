import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** Doi mau nen: in 8 mau xuat hien nhieu nhat + mau 4 goc de biet anh co nen don sac khong. */
public class BgCheck {
    public static void main(String[] args) throws Exception {
        BufferedImage im = ImageIO.read(new File(args[0]));
        int W = im.getWidth(), H = im.getHeight();
        Map<Integer, Integer> dem = new HashMap<>();
        for (int y = 0; y < H; y += 2) {
            for (int x = 0; x < W; x += 2) {
                int rgb = im.getRGB(x, y) & 0xFFFFFF;
                dem.merge(rgb, 1, Integer::sum);
            }
        }
        long tong = (long) ((W / 2) * (H / 2));
        System.out.printf("%s %dx%d%n", new File(args[0]).getName(), W, H);
        dem.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(8)
                .forEach(e -> System.out.printf("  #%06X  %d px  (%.1f%%)%n", e.getKey(), e.getValue(),
                        100.0 * e.getValue() / tong));
        System.out.printf("  goc TL=#%06X TR=#%06X BL=#%06X BR=#%06X%n",
                im.getRGB(0, 0) & 0xFFFFFF, im.getRGB(W - 1, 0) & 0xFFFFFF,
                im.getRGB(0, H - 1) & 0xFFFFFF, im.getRGB(W - 1, H - 1) & 0xFFFFFF);
    }
}
