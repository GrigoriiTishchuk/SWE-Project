
package com.edujournal.backend.utils;

/**
 * Provides utility methods for validating names, email addresses,
 * and passwords.
 *
 * @author EduJournal
 */
public final class InputValidator {

    /** Minimum required password length. */
    private static final int MIN_PASSWORD_LENGTH = 9;

    /**
     * Creates an InputValidator instance.
     */
    public InputValidator() {
        // Public constructor for compatibility with existing code.
    }

    /**
     * Checks whether a name is non-null and not empty.
     *
     * @param name the name to validate
     * @return true if the name is valid, otherwise false
     */
    public static boolean isValidName(final String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Checks whether an email address has a valid basic format.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, otherwise false
     */
    public static boolean isValidEmail(final String email) {
        if (email == null) {
            return false;
        }

        String trimmedEmail = email.trim();

        if (trimmedEmail.isEmpty()
                || trimmedEmail.contains(" ")) {
            return false;
        }

        int atIndex = trimmedEmail.indexOf('@');

        if (atIndex <= 0
                || atIndex != trimmedEmail.lastIndexOf('@')) {
            return false;
        }

        String local = trimmedEmail.substring(0, atIndex);
        String domain = trimmedEmail.substring(atIndex + 1);

        if (local.startsWith(".") || !domain.contains(".")) {
            return false;
        }

        return trimmedEmail.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Checks whether a password meets the minimum length requirement.
     *
     * @param password the password to validate
     * @return true if the password contains at least the required
     * number of characters, otherwise false
     */
    public static boolean isValidPassword(final String password) {
        return password != null
                && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
