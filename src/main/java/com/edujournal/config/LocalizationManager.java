package com.edujournal.config;

import javafx.geometry.NodeOrientation;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides localization support for the application.
 * Manages the current locale, localized resource bundles,
 * text orientation, and alignment settings.
 */
public final class LocalizationManager {

    private static Locale currentLocale = new Locale("en", "US");
    private static final String BUNDLE_NAME = "MessagesBundle";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    private static final ResourceBundle FALLBACK = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Prevents instantiation of this utility class.
     */
    private LocalizationManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sets the current application locale and loads
     * the corresponding resource bundle.
     *
     * @param locale the locale to set
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Returns a localized string for the specified key.
     * Falls back to the default English bundle if the key
     * is not found in the current locale.
     *
     * @param key the resource key
     * @return the localized value
     */
    public static String get(String key) {
        if (bundle.containsKey(key)) return bundle.getString(key);
        return FALLBACK.getString(key);
    }

    /**
     * Checks whether the current language is right-to-left.
     *
     * @return true if the current locale uses RTL text direction,
     *         false otherwise
     */
    public static boolean isRTL() {
        return getCurrentLocale().getLanguage().equals("ar");
    }

    /**
     * Returns the appropriate node orientation for the current locale.
     *
     * @return LEFT_TO_RIGHT or RIGHT_TO_LEFT orientation
     */
    public static NodeOrientation getOrientation() {
        return isRTL()
                ? NodeOrientation.RIGHT_TO_LEFT
                : NodeOrientation.LEFT_TO_RIGHT;
    }

    /**
     * Returns the currently selected locale.
     *
     * @return current application locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
