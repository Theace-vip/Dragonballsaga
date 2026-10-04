import java.nio.file.*;
import java.util.*;

/**
 * Reverse format DataEffect - phan tich header + frame rects + phan con lai.
 * Usage: java ParseEff file1 [file2 ...]  hoac  java ParseEff --all thu_muc
 */
public class ParseEff {

    static String hex(byte[] d, int off, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = off; i < Math.min(off + n, d.length); i++)
            sb.append(String.format("%02x ", d[i]));
        return sb.toString();
    }

    static int u8(byte[] d, int i) { return d[i] & 0xFF; }
    static int s16le(byte[] d, int i) { return (d[i] & 0xFF) | ((int) d[i + 1] << 8); }

    static void parse(Path p) throws Exception {
        byte[] d = Files.readAllBytes(p);
        System.out.println("=== " + p.getFileName() + " (" + d.length + " bytes) ===");
        // Header byte0 = numFrame? (nhanh doan: 07 00, 09 00...)
        int nFrame = u8(d, 0);
        System.out.println("b0=" + nFrame + " b1=" + u8(d, 1) + " b2=" + u8(d, 2) + " b3=" + u8(d, 3));

        // Hypothesis: b0=numFrame, roi moi frame 5 byte [idx][x][y][w][h] HOAC [x][y][w][h][idx]
        // so sanh vi tri byte 4..4+5*n
        int o = 4;
        if (4 + 5 * nFrame <= d.length) {
            System.out.println("-- rects doc [idx][x][y][w][h] tu off 4:");
            for (int f = 0; f < nFrame && o + 5 <= d.length; f++, o += 5) {
                int idx = u8(d, o), x = u8(d, o + 1), y = u8(d, o + 2), w = u8(d, o + 3), h = u8(d, o + 4);
                System.out.printf("  f%d: idx=%d x=%d y=%d w=%d h=%d%n", f, idx, x, y, w, h);
            }
            System.out.println("  tail off=" + o + " first16: " + hex(d, o, 16));
        }

        // Gia su phan con lai la offset signed16: dump 20 short dau
        System.out.println("-- tail as s16le:");
        StringBuilder sb = new StringBuilder("  ");
        for (int i = o; i + 1 < d.length && i < o + 60; i += 2)
            sb.append(s16le(d, i)).append(' ');
        System.out.println(sb);
        System.out.println();
    }

    public static void main(String[] a) throws Exception {
        if (a.length == 0) { System.out.println("java ParseEff <file...> | --all <dir>"); return; }
        if (a[0].equals("--all")) {
            Path dir = Paths.get(a[1]);
            List<Path> fs = new ArrayList<>();
            Files.newDirectoryStream(dir).forEach(fs::add);
            fs.sort(Comparator.comparingLong(p -> p.toFile().length()));
            for (Path p : fs) parse(p);
        } else {
            for (String s : a) parse(Paths.get(s));
        }
    }
}
