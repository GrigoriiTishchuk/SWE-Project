package com.edujournal.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {

    private static Locale currentLocale = new Locale("ar", "SA");
    private static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(
                "MessagesBundle",
                locale
        );
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
