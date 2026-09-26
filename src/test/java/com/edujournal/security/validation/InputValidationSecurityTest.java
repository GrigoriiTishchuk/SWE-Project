package com.edujournal.security.validation;

import com.edujournal.backend.utils.InputValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidationSecurityTest {

    @Test
    void nullNameShouldBeRejected() {
        assertFalse(InputValidator.isValidName(null));
    }

    @Test
    void emptyNameShouldBeRejected() {
        assertFalse(InputValidator.isValidName(""));
    }

    @Test
    void whitespaceOnlyNameShouldBeRejected() {
        assertFalse(InputValidator.isValidName("     "));
    }

    @Test
    void normalNameShouldBeAccepted() {
        assertTrue(InputValidator.isValidName("Ali"));
    }

    @Test
    void nullPasswordShouldBeRejected() {
        assertFalse(InputValidator.isValidPassword(null));
    }

    @Test
    void passwordShorterThanNineCharactersShouldBeRejected() {
        assertFalse(InputValidator.isValidPassword("12345678"));
    }

    @Test
    void nineCharacterPasswordShouldBeAccepted() {
        assertTrue(InputValidator.isValidPassword("123456789"));
    }

    @Test
    void longPasswordShouldBeAccepted() {
        assertTrue(
                InputValidator.isValidPassword("SecurePassword123!")
        );
    }

}
