package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    @Test
    void constructorShouldThrowException() throws Exception {
        Constructor<InputValidator> constructor =
                InputValidator.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        InvocationTargetException exception =
                assertThrows(
                        InvocationTargetException.class,
                        constructor::newInstance
                );

        assertInstanceOf(
                IllegalStateException.class,
                exception.getCause()
        );

        assertEquals(
                "Utility class cannot be instantiated.",
                exception.getCause().getMessage()
        );
    }

    @org.junit.jupiter.api.Test
    void isValidName() {
        assertTrue(InputValidator.isValidName("Olenape"));
        assertFalse(InputValidator.isValidName(null));
        assertFalse(InputValidator.isValidName(""));
        assertFalse(InputValidator.isValidName("  "));
        assertTrue(InputValidator.isValidName("John Johnson"));
    }

    @org.junit.jupiter.api.Test
    void isValidEmail() {
        assertTrue(InputValidator.isValidEmail("olenape@gmail.com"));
        assertFalse(InputValidator.isValidEmail("newmail.com"));
        assertFalse(InputValidator.isValidEmail("newmail.com@"));
        assertFalse(InputValidator.isValidEmail("@newmail.com"));
        assertFalse(InputValidator.isValidEmail(null));
        assertFalse(InputValidator.isValidEmail(""));
        assertFalse(InputValidator.isValidEmail("   "));
        assertFalse(InputValidator.isValidEmail("user@@mail.com"));
        assertFalse(InputValidator.isValidEmail("user@mail"));
        assertFalse(InputValidator.isValidEmail(".user@mail.com"));
        assertFalse(InputValidator.isValidEmail("user @mail.com"));
        assertFalse(InputValidator.isValidEmail("user!mail.com"));
    }

    @org.junit.jupiter.api.Test
    void isValidPassword() {
        assertTrue(InputValidator.isValidPassword("123456789"));
        assertFalse(InputValidator.isValidPassword("12345678"));
        assertFalse(InputValidator.isValidPassword(null));
    }
}