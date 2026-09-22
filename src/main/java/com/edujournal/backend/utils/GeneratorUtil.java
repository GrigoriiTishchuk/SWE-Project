package com.edujournal.backend.utils;

import java.util.UUID;

public class GeneratorUtil {
    // Username generation
    public static String generateUsername(String firstName, String lastName) {
        String fn = normalize(firstName);
        String ln = normalize(lastName);

        int maxFn = Math.min(fn.length(), 5);
        int minFn = 1;

        int minLn = 3;
        int maxLn = ln.length();

        for (int fnLen = maxFn, lnLen = minLn;
             fnLen >= minFn && lnLen <= maxLn;
             fnLen--, lnLen++) {

            String base = fn.substring(0, fnLen) + ln.substring(0, lnLen);
            return base;
        }

        return fn + ln;
    }

    private static String normalize(String s) {
        return s.toLowerCase()
                .replace("ä", "a")
                .replace("ö", "o")
                .replace("å", "a")
                .replaceAll("\\s+", "");
    }

    // Email generation
    public static String generateEmail(String username) {
        return username + "@edujournal.fi";
    }

    // Password Hash generation
    public static String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    // Student_number generation
    public static String generateStudentNumber() {
        int number = (int)(Math.random() * 1_000_0000);
        return String.format("%07d", number);
    }
}
