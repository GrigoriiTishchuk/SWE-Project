package com.edujournal.backend.utils;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    InputValidator inputValidator = new InputValidator();

    @org.junit.jupiter.api.Test
    void isValidName() {
        assertTrue(inputValidator.isValidName("Olenape"));
        assertFalse(inputValidator.isValidName(null));
        assertFalse(inputValidator.isValidName(""));
        assertFalse(inputValidator.isValidName("  "));
        assertTrue(inputValidator.isValidName("John Johnson"));
    }

    @org.junit.jupiter.api.Test
    void isValidEmail() {
        assertTrue(inputValidator.isValidEmail("olenape@gmail.com"));
        assertFalse(inputValidator.isValidEmail("newmail.com"));
        assertFalse(inputValidator.isValidEmail("newmail.com@"));
        assertFalse(inputValidator.isValidEmail("@newmail.com"));
        assertFalse(inputValidator.isValidEmail(null));
        assertFalse(inputValidator.isValidEmail(""));
        assertFalse(inputValidator.isValidEmail("   "));
        assertFalse(inputValidator.isValidEmail("user@@mail.com"));
        assertFalse(inputValidator.isValidEmail("user@mail"));
        assertFalse(inputValidator.isValidEmail(".user@mail.com"));
        assertFalse(inputValidator.isValidEmail("user @mail.com"));
        assertFalse(inputValidator.isValidEmail("user!mail.com"));
    }

    @org.junit.jupiter.api.Test
    void isValidPassword() {
        assertTrue(inputValidator.isValidPassword("123456789"));
        assertFalse(inputValidator.isValidPassword("12345678"));
        assertFalse(inputValidator.isValidPassword(null));
    }
}