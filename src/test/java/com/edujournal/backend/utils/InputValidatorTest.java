package com.edujournal.backend.utils;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

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