import java.nio.file.*;
import java.util.*;

/**
 * Codec DataEffect - parse + write, roundtrip verify.
 * Format da reverse:
 *   section1: u8 N, N x (u8 idx, u8 x, u8 y, u8 w, u8 h)   // rect trong sheet
 *   section2: u8 0x00, u8 H, H record: u8 c, c x (s16be dx, s16be dy, u8 spriteIdx)
 *   section3: u16be count, count x u16be frameIdx (tang dan), u16 0x3232
 * Usage: java DataEffectCodec <dir>       // roundtrip toan bo
 *        java DataEffectCodec <file>      // dump 1 file
 */
public class DataEffectCodec {

    public static class Rect {
        public int idx, x, y, w, h;
        public Rect(int idx, int x, int y, int w, int h) { this.idx = idx; this.x = x; this.y = y; this.w = w; this.h = h; }
    }
    public static class Part {
        public int dx, dy, sprite;
        public Part(int dx, int dy, int sprite) { this.dx = dx; this.dy = dy; this.sprite = sprite; }
    }
    public static class Data {
        public Rect[] rects;
        public Part[][] frames; // H frame, moi frame c part
        public int[] seq;       // section3 sequence
    }

    static int u8(byte[] d, int i) { return d[i] & 0xFF; }
    static int s16be(byte[] d, int i) { int v = ((d[i] & 0xFF) << 8) | (d[i + 1] & 0xFF); return (short) v; }
    static int u16be(byte[] d, int i) { return ((d[i] & 0xFF) << 8) | (d[i + 1] & 0xFF); }

    /** Parse, nem IllegalArgumentException neu khong doc het EOF. */
    public static Data parse(byte[] d) {
        int off = 0;
        int n = u8(d, off); off++;
        Rect[] rects = new Rect[n];
        for (int i = 0; i < n; i++) {
            rects[i] = new Rect(u8(d, off), u8(d, off + 1), u8(d, off + 2), u8(d, off + 3), u8(d, off + 4));
            off += 5;
        }
        if (off + 2 > d.length) throw new IllegalArgumentException("section2 truncated");
        if (d[off] != 0) throw new IllegalArgumentException("section2 header byte != 0 at " + off);
        int H = u8(d, off + 1); off += 2;
        Part[][] frames = new Part[H][];
        for (int f = 0; f < H; f++) {
            if (off >= d.length) throw new IllegalArgumentException("section2 frame truncated");
            int c = u8(d, off); off++;
            frames[f] = new Part[c];
            for (int p = 0; p < c; p++) {
                if (off + 5 > d.length) throw new IllegalArgumentException("section2 part truncated");
                frames[f][p] = new Part(s16be(d, off), s16be(d, off + 2), u8(d, off + 4));
                off += 5;
            }
        }
        if (off + 2 > d.length) throw new IllegalArgumentException("section3 truncated");
        int count = u16be(d, off); off += 2;
        if (off + 2 * count + 2 > d.length) throw new IllegalArgumentException("section3 seq truncated");
        int[] seq = new int[count];
        for (int i = 0; i < count; i++) { seq[i] = u16be(d, off); off += 2; }
        int term = u16be(d, off); off += 2;
        if (term != 0x3232) throw new IllegalArgumentException(String.format("terminator != 3232: %04x", term));
        if (off != d.length) throw new IllegalArgumentException("trailing bytes: " + (d.length - off));
        Data out = new Data();
        out.rects = rects; out.frames = frames; out.seq = seq;
        return out;
    }

    public static byte[] write(Data d) {
        int len = 1 + 5 * d.rects.length
                + 2 + d.frames.length + 5 * Arrays.stream(d.frames).mapToInt(f -> f.length).sum()
                + 2 + 2 * d.seq.length + 2;
        byte[] b = new byte[len];
        int off = 0;
        b[off++] = (byte) d.rects.length;
        for (Rect r : d.rects) {
            b[off++] = (byte) r.idx; b[off++] = (byte) r.x; b[off++] = (byte) r.y;
            b[off++] = (byte) r.w; b[off++] = (byte) r.h;
        }
        b[off++] = 0;
        b[off++] = (byte) d.frames.length;
        for (Part[] fr : d.frames) {
            b[off++] = (byte) fr.length;
            for (Part p : fr) {
                b[off++] = (byte) ((p.dx >> 8) & 0xFF); b[off++] = (byte) (p.dx & 0xFF);
                b[off++] = (byte) ((p.dy >> 8) & 0xFF); b[off++] = (byte) (p.dy & 0xFF);
                b[off++] = (byte) p.sprite;
            }
        }
        b[off++] = (byte) ((d.seq.length >> 8) & 0xFF); b[off++] = (byte) (d.seq.length & 0xFF);
        for (int v : d.seq) {
            b[off++] = (byte) ((v >> 8) & 0xFF); b[off++] = (byte) (v & 0xFF);
        }
        b[off++] = 0x32; b[off++] = 0x32;
        if (off != len) throw new IllegalStateException("len mismatch " + off + " vs " + len);
        return b;
    }

    static void dump(Data d) {
        System.out.println("  rects (N=" + d.rects.length + "):");
        for (Rect r : d.rects) System.out.printf("    idx%d: (%d,%d) %dx%d%n", r.idx, r.x, r.y, r.w, r.h);
        System.out.println("  frames (H=" + d.frames.length + "):");
        for (int f = 0; f < d.frames.length; f++) {
            StringBuilder sb = new StringBuilder("    f" + f + ":");
            for (Part p : d.frames[f]) sb.append(String.format(" [d(%d,%d)spr%d]", p.dx, p.dy, p.sprite));
            System.out.println(sb);
        }
        StringBuilder sb = new StringBuilder("  seq (" + d.seq.length + "):");
        for (int v : d.seq) sb.append(' ').append(v);
        System.out.println(sb);
    }

    public static void main(String[] a) throws Exception {
        if (a.length == 1 && !Files.isDirectory(Paths.get(a[0]))) {
            dump(parse(Files.readAllBytes(Paths.get(a[0]))));
            return;
        }
        Path dir = Paths.get(a[0]);
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "DataEffect_*")) {
            for (Path p : ds) files.add(p);
        }
        files.sort(Comparator.comparingLong(p -> p.toFile().length()));
        int ok = 0, rt = 0;
        List<String> failParse = new ArrayList<>(), failRt = new ArrayList<>();
        for (Path p : files) {
            byte[] d = Files.readAllBytes(p);
            String name = p.getFileName().toString().replace("DataEffect_", "");
            Data parsed;
            try {
                parsed = parse(d);
                ok++;
            } catch (Exception e) {
                failParse.add(name + "(" + e.getMessage() + ")");
                continue;
            }
            byte[] re = write(parsed);
            if (Arrays.equals(d, re)) rt++;
            else {
                failRt.add(name);
                // tim khac biet dau tien
                for (int i = 0; i < Math.min(d.length, re.length); i++) {
                    if (d[i] != re[i]) { failRt.set(failRt.size() - 1, name + "@off" + i); break; }
                }
            }
        }
        System.out.println("Tong: " + files.size() + " | parse OK: " + ok + " | roundtrip byte-identical: " + rt);
        System.out.println("Parse FAIL (" + failParse.size() + "): " + failParse);
        System.out.println("Roundtrip FAIL (" + failRt.size() + "): " + failRt);
    }
}
