import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Cat frame tu sheet ImgEffect_43 (grid 3x3) ra PNG rieng cho flag_bag (icon_botnet).
 * flag_bag.icon_data = list icon id -> client tai qua icon request binh thuong.
 * Usage: java GenFlagFrames <firstIconId>
 */
public class GenFlagFrames {

    public static void main(String[] args) throws Exception {
        int first = args.length > 0 ? Integer.parseInt(args[0]) : 32339;
        int cols = 3, rows = 3, count = 8;
        int made = 0;
        for (int z = 2; z <= 4; z++) {
            File sheetFile = new File("Eff/effect/x" + z + "/img/ImgEffect_43.png");
            if (!sheetFile.isFile()) {
                System.out.println("MISS " + sheetFile);
                continue;
            }
            BufferedImage sheet = ImageIO.read(sheetFile);
            int cw = sheet.getWidth() / cols, ch = sheet.getHeight() / rows;
            File outDir = new File("data/icon_botnet/x" + z);
            outDir.mkdirs();
            for (int i = 0; i < count; i++) {
                int cx = (i % cols) * cw, cy = (i / cols) * ch;
                BufferedImage frame = sheet.getSubimage(cx, cy, cw, ch);
                File out = new File(outDir, (first + i) + ".png");
                ImageIO.write(frame, "png", out);
                made++;
            }
            System.out.println("x" + z + ": " + count + " frame " + cw + "x" + ch + " -> " + outDir.getPath());
        }
        System.out.println("DONE " + made + " file, icon id " + first + ".." + (first + count - 1));
    }
}
