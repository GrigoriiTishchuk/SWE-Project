package com.edujournal.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {

    private static Locale currentLocale = new Locale("en", "US");
    private static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    private static final ResourceBundle fallback = ResourceBundle.getBundle("MessagesBundle", new Locale("en", "US"));

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);
    }

    public static String get(String key) {
        if (bundle.containsKey(key)) return bundle.getString(key);
        return fallback.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
