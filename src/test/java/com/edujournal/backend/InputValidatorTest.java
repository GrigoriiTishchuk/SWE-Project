package com.edujournal.backend;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    InputValidator inputValidator = new InputValidator();

    @org.junit.jupiter.api.Test
    void isValidName() {
        assertTrue(inputValidator.isValidName("Olenape"));
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
    }

    @org.junit.jupiter.api.Test
    void isValidPassword() {
        assertTrue(inputValidator.isValidPassword("123456789"));
        assertFalse(inputValidator.isValidPassword("12345678"));
    }
}