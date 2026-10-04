/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author HairMod
 */
public class NinjaUtil {

    public static String getMoneys(long money) { 
        return String.valueOf(money);  
    }

    public static double powdb(double a, int b) {
        double num = 1;
        for (int i = 0; i < b; i++) {
            num *= a;
        }
        return num;
    }

    public static String formatNumber2(double a) {
       

        int solan = 1;
        int b = 1000000000;
        for (int i = 1; a >= powdb(b, i + 1); i++) {
            solan++;
        }

        String ans = "";
        while (a >= powdb(b, solan)) {
            while (a > 1000000000 * powdb(b, solan)) {
                a /= 1000000000 * powdb(b, solan);
                ans = " Tỉ X" + solan + "Tỉ" + ans;
            }
            while (a > 1000000 * powdb(b, solan)) {
                a /= 1000000 * powdb(b, solan);
                ans = " Triệu X" + solan + "Tỉ" + ans;
            }
            while (a > 1000 * powdb(b, solan)) {
                a /= 1000 * powdb(b, solan);
                ans = " Nghìn X" + solan + "Tỉ" + ans;
            }
            while (a > powdb(b, solan)) {
                a /= powdb(b, solan);
                ans = " X" + solan + "Tỉ" + ans;
            }
        }

        while (a > 1000000000) {
            a /= 1000000000;
            ans = " Tỷ" + ans;
        }
        while (a > 1000000) {
            a /= 1000000;
            ans = " Triệu" + ans;
        }
        while (a > 1000) {
            a /= 1000;
            ans = " Nghìn" + ans;
        }

        ans = String.format("%.0f", a) + ans;
        return ans;
    }
}
