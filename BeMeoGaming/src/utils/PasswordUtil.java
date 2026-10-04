package utils;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Bam mat khau PBKDF2-HMAC-SHA256, dong format voi PHP:
 * hash_pbkdf2("sha256", $plain, $salt, 100000, 32, true)
 * Dinh dang luu: pbkdf2-sha256$<lanLap>$<saltHex16>$<hashHex32>
 */
public final class PasswordUtil {

    private static final String PREFIX = "pbkdf2-sha256$";
    private static final int ITERATIONS = 100000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordUtil() {
    }

    /** Chuoi da bam chua? (dung de phan bai voi mat khau legacy dang plaintext) */
    public static boolean isHash(String stored) {
        return stored != null && stored.startsWith(PREFIX);
    }

    public static String hash(String plain) {
        try {
            String pw = plain == null ? "" : plain;
            byte[] salt = new byte[SALT_BYTES];
            new SecureRandom().nextBytes(salt);
            byte[] dk = pbkdf2(pw.toCharArray(), salt, ITERATIONS, KEY_BITS);
            StringBuilder sb = new StringBuilder(PREFIX);
            sb.append(ITERATIONS).append('$').append(toHex(salt)).append('$').append(toHex(dk));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Khong the bam mat khau", e);
        }
    }

    public static boolean verify(String stored, String plain) {
        try {
            if (stored == null || !stored.startsWith(PREFIX)) {
                return false;
            }
            String[] p = stored.split("\\$");
            if (p.length != 4) {
                return false;
            }
            int iter = Integer.parseInt(p[1]);
            byte[] salt = fromHex(p[2]);
            byte[] expect = fromHex(p[3]);
            byte[] got = pbkdf2((plain == null ? "" : plain).toCharArray(), salt, iter, expect.length * 8);
            return constantTimeEquals(expect, got);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] plain, byte[] salt, int iter, int keyBits) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(plain, salt, iter, keyBits);
        byte[] dk = skf.generateSecret(spec).getEncoded();
        spec.clearPassword();
        return dk;
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }
        int r = 0;
        for (int i = 0; i < a.length; i++) {
            r |= a[i] ^ b[i];
        }
        return r == 0;
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int n = hex.length();
        byte[] out = new byte[n / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
}
