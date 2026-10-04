import java.nio.file.*;
import java.util.*;

/**
 * Reverse DataEffect - lan 2.
 * Da xac minh:
 *   section1 = [u8 N][N x (idx,x,y,w,h)]
 *   section3 = [u16 BE count][count x u16 BE frameIdx][u16 0x3232]
 *   section2 = [00][H] + H record (grammar chua xac dinh) - DP partition.
 * Tim boundary: moi vi tri T -> count xac dinh boi (len - T - 4)/2, kiem tra values.
 * Usage: java EffDP <dir_data>
 */
public class EffDP {

    static int u8(byte[] d, int i) { return d[i] & 0xFF; }
    static int be16(byte[] d, int i) { return ((d[i] & 0xFF) << 8) | (d[i + 1] & 0xFF); }

    /** Tim toan bo vi tri bat dau section3 hop le: [count][count x u16 BE][32 32] dung EOF.
     *  Tra ve list {T, count}. */
    static List<int[]> findSection3(byte[] d, int minT) {
        List<int[]> res = new ArrayList<>();
        int len = d.length;
        for (int T = minT; T <= len - 6; T++) {
            int remain = len - T;
            // can 2 (count) + 2*count (values) + 2 (terminator) = remain  -> count = (remain - 4)/2
            if ((remain - 4) < 0 || (remain - 4) % 2 != 0) continue;
            int count = (remain - 4) / 2;
            if (count < 1 || count > 4096) continue;
            int cnt = be16(d, T);
            if (cnt != count) continue;
            // terminator: uu tien 32 32, nhung chap nhan bat ky 2 byte cuoi (se danh dau)
            // values: tang dan, buoc 0 hoac +1, run length >= 1
            int prev = -1;
            boolean ok = true;
            for (int i = T + 2; i + 1 < len - 2; i += 2) {
                int v = be16(d, i);
                if (v > 255) { ok = false; break; }
                if (prev < 0) prev = v;
                else if (v == prev || v == prev + 1) prev = v;
                else { ok = false; break; }
            }
            if (ok) res.add(new int[]{T, count});
        }
        return res;
    }

    /**
     * DP partition body[from..to) thanh dung exactRecords record.
     * mode 0: G1(k) = [c][c x k]
     * mode 1: G2(k) = [c][c x k][idx]
     * mode 2: G3(k) = [idx][c][c x k]
     * mode 3: G4(s) = fixed size s
     * allowTr0: bo qua chuoi 0 cuoi body truoc khi partition.
     */
    static boolean canPartition(byte[] d, int from, int to, int mode, int p, int exactRecords) {
        if (exactRecords <= 0 || to <= from) return false;
        boolean[][] dp = new boolean[to + 2][exactRecords + 1];
        dp[from][0] = true;
        for (int i = from; i <= to; i++) {
            for (int n = 0; n < exactRecords; n++) {
                if (!dp[i][n]) continue;
                int need = -1;
                if (mode == 3) need = p;
                else if (i < to) {
                    if (mode == 2) {
                        // [idx][c][c x k]
                        if (i + 1 < to) {
                            int c = u8(d, i + 1);
                            need = 2 + c * p;
                        }
                    } else {
                        int c = u8(d, i);
                        need = 1 + c * p + (mode == 1 ? 1 : 0);
                    }
                }
                if (need > 0 && i + need <= to) dp[i + need][n + 1] = true;
            }
        }
        return dp[to][exactRecords];
    }

    public static void main(String[] a) throws Exception {
        Path dir = Paths.get(a[0]);
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "DataEffect_*")) {
            for (Path p : ds) files.add(p);
        }
        files.sort(Comparator.comparingLong(p -> p.toFile().length()));

        Map<String, Set<String>> score = new TreeMap<>();
        Map<String, List<String>> detail = new HashMap<>();
        Set<String> matchedAny = new HashSet<>();
        int noBoundary = 0;

        for (Path p : files) {
            byte[] d = Files.readAllBytes(p);
            String name = p.getFileName().toString().replace("DataEffect_", "");
            int n = u8(d, 0);
            int s2off = 1 + 5 * n;
            if (s2off + 4 >= d.length) continue;

            List<int[]> tails = findSection3(d, s2off + 2);
            if (tails.isEmpty()) {
                noBoundary++;
                score.computeIfAbsent("NO_S3", k -> new HashSet<>()).add(name);
                detail.computeIfAbsent("NO_S3", k -> new ArrayList<>())
                        .add(name + "/N" + n + "/len" + d.length + "/s2off" + s2off);
                continue;
            }
            for (int[] t : tails) {
                int T = t[0], count3 = t[1];
                // body section2: co header [00][H] tai s2off -> from = s2off+2, H = byte s2off+1
                // khong header: from = s2off
                boolean hdrOk = (u8(d, s2off) == 0);
                int H = u8(d, s2off + 1);
                int[][] tries = hdrOk
                        ? new int[][]{{s2off + 2, H}, {s2off, -1}, {s2off + 1, -1}}
                        : new int[][]{{s2off, -1}};
                for (int[] tr : tries) {
                    int from = tr[0], exact = tr[1];
                    if (from >= T) continue;
                    for (int allowTr = 0; allowTr <= 1; allowTr++) {
                        int to = T;
                        if (allowTr == 1) {
                            while (to > from && d[to - 1] == 0) to--;
                            // it nhat 1 byte0 bi bo qua moi y nghia
                        }
                        if (to <= from) continue;
                        if (exact > 0) {
                            for (int mode = 0; mode <= 3; mode++) {
                                int[] ps = (mode == 3)
                                        ? new int[]{1,2,3,4,5,6,7,8,10,12,16,20,26}
                                        : new int[]{1,2,3,4,5,6,7,8,10,12};
                                for (int pp : ps) {
                                    if (canPartition(d, from, to, mode, pp, exact)) {
                                        matchedAny.add(name);
                                        String key = "G" + (mode + 1) + "(" + pp + ")"
                                                + (allowTr == 1 ? "+tr0" : "")
                                                + (from == s2off + 2 ? "+hdr" : "-nohdr");
                                        if (score.computeIfAbsent(key, k -> new HashSet<>()).add(name))
                                            detail.computeIfAbsent(key, k -> new ArrayList<>())
                                                    .add(name + "/H" + exact + "/T" + T + "/s3cnt" + count3
                                                            + "/body" + (to - from));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<Map.Entry<String, Set<String>>> lst = new ArrayList<>(score.entrySet());
        lst.sort((x, y) -> y.getValue().size() - x.getValue().size());
        int total = files.size();
        System.out.println("=== Tong file: " + total + ", NO_S3: " + noBoundary + " ===");
        for (Map.Entry<String, Set<String>> e : lst) {
            System.out.printf("%-22s %d/%d%n", e.getKey(), e.getValue().size(), total);
        }
        System.out.println("\n=== Chi tiet grammar tot nhat ===");
        // file khong match grammar nao
        Set<String> all = new TreeSet<>();
        for (Path p : files) all.add(p.getFileName().toString().replace("DataEffect_", ""));
        Set<String> unmatched = new TreeSet<>(all);
        unmatched.removeAll(matchedAny);
        System.out.println("Khong match grammar nao (" + unmatched.size() + "/" + all.size() + "): " + unmatched);

        for (Map.Entry<String, Set<String>> e : lst) {
            if (e.getKey().contains("+hdr")) {
                System.out.println("-- " + e.getKey() + " -> " + e.getValue().size());
                List<String> det = detail.getOrDefault(e.getKey(), Collections.emptyList());
                for (String s : det) System.out.println("   " + s);
                break;
            }
        }
        if (detail.containsKey("NO_S3")) {
            System.out.println("-- NO_S3 file:");
            for (String s : detail.get("NO_S3")) System.out.println("   " + s);
        }
    }
}
