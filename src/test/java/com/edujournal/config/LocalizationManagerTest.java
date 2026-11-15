package com.edujournal.config;

import javafx.geometry.NodeOrientation;
import org.junit.jupiter.api.Test;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class LocalizationManagerTest {
    @Test
    void setLocaleChangesCurrentLocale() {
        Locale locale = new Locale("fi", "FI");

        LocalizationManager.setLocale(locale);

        assertEquals(locale, LocalizationManager.getCurrentLocale());
    }

    @Test
    void getReturnsLocalizedValue() {
        String value = LocalizationManager.get("save");

        assertNotNull(value);
        assertFalse(value.isBlank());
    }

    @Test
    void isRTLReturnsTrueForArabic() {
        LocalizationManager.setLocale(new Locale("ar"));

        assertTrue(LocalizationManager.isRTL());
    }

    @Test
    void isRTLReturnsFalseForEnglish() {
        LocalizationManager.setLocale(Locale.ENGLISH);

        assertFalse(LocalizationManager.isRTL());
    }

    @Test
    void getOrientationReturnsRightToLeftForArabic() {
        LocalizationManager.setLocale(new Locale("ar"));

        assertEquals(
                NodeOrientation.RIGHT_TO_LEFT,
                LocalizationManager.getOrientation()
        );
    }

    @Test
    void getOrientationReturnsLeftToRightForEnglish() {
        LocalizationManager.setLocale(Locale.ENGLISH);

        assertEquals(
                NodeOrientation.LEFT_TO_RIGHT,
                LocalizationManager.getOrientation()
        );
    }
}