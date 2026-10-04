/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package utils;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil {
    public static String randomText(int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1).filter(i -> !(i > 57 && i < 65 || i > 90 && i < 97)).limit(length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }
      public static String convertString(String value) {
        try {
            return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[Đđ]", "d")).replaceAll("");
        } catch (Exception ex) {
            return Normalizer.normalize(value, Normalizer.Form.NFD);
        }
    }
}

