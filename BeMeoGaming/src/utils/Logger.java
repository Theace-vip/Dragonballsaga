/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {

    public static final String RESET = "\u001b[0m";
    public static final String RED = "\u001b[4;31m";
    public static final String GREEN = "\u001b[0;32m";
    public static final String PURPLE = "\u001b[0;35m";
    public static final String BLUE = "\u001b[0;34m";
    public static final String YELLOW = "\u001b[33m";

    public static void log(String text) {
        System.out.print(text);
    }

    public static void logln(String text) {
        System.out.println(text);
    }

    public static void log(String color, String text) {
        System.out.print(color + text + RESET);
    }

    public static void success(String text) {
        System.out.print(GREEN + text + RESET);
    }

    public static void warning(String text) {
        System.out.print(BLUE + text + RESET);
    }

    public static void error(String text) {
        System.out.print(RED + text + RESET);
    }

    public static void logException(Class clazz, Exception ex, String... log) {
        try {
            if (log != null && log.length > 0) {
                Logger.log(PURPLE, log[0] + "\n");
            }
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            String nameMethod = stackTraceElements[1].getMethodName();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String detail = sw.toString();
            String[] arr = detail.split("\n");
            Logger.warning("C\u00f3 l\u1ed7i t\u1ea1i class: ");
            Logger.error(clazz.getName());
            Logger.warning(" - t\u1ea1i ph\u01b0\u01a1ng th\u1ee9c: ");
            Logger.error(nameMethod + "\n");
            Logger.warning("Chi ti\u1ebft l\u1ed7i:\n");
            for (String str : arr) {
                Logger.error(str + "\n");
            }
            Logger.log("--------------------------------------------------------\n");
        } catch (Exception exception) {
            // empty catch block
        }
    }
}
