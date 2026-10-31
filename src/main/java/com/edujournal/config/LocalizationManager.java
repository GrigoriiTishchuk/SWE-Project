package com.edujournal.config;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;

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

    public static boolean isRTL() {
        return getCurrentLocale().getLanguage().equals("ar");
    }

    public static NodeOrientation getOrientation() {
        return isRTL()
                ? NodeOrientation.RIGHT_TO_LEFT
                : NodeOrientation.LEFT_TO_RIGHT;
    }

    public static Pos getStartAlignment() {
        return isRTL()
                ? Pos.CENTER_RIGHT
                : Pos.CENTER_LEFT;
    }

    public static Pos getEndAlignment() {
        return isRTL()
                ? Pos.CENTER_LEFT
                : Pos.CENTER_RIGHT;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
