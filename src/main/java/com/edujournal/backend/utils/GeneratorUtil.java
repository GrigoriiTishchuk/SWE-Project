package com.edujournal.backend.utils;

import java.security.SecureRandom;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides utility methods for generating usernames, email addresses, temporary passwords, password
 * hashes, and student numbers.
 *
 * @author EduJournal
 */
public final class GeneratorUtil {

    /** Maximum number of first-name characters used in usernames. */
    private static final int MAX_FIRST_NAME_LENGTH = 5;

    /** Minimum number of last-name characters used in usernames. */
    private static final int MIN_LAST_NAME_LENGTH = 3;

    /** Required length of generated usernames. */
    private static final int USERNAME_LENGTH = 8;

    /** Required length of generated temporary passwords. */
    private static final int TEMP_PASSWORD_LENGTH = 8;

    /** Upper bound for generating seven-digit student numbers. */
    private static final int STUDENT_NUMBER_BOUND = 10_000_000;

    /** Cryptographically secure random number generator. */
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Prevents instantiation of this utility class. */
    private GeneratorUtil() {
        throw new IllegalStateException("Utility class cannot be instantiated.");
    }

    /**
     * Generates possible usernames using parts of a first name and a last name.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return a list of username candidates
     */
    public static List<String> generateUsernameCandidates(
            final String firstName, final String lastName) {

        String fn = normalize(firstName);
        String ln = normalize(lastName);

        int maxFn = Math.min(fn.length(), MAX_FIRST_NAME_LENGTH);
        int minFn = 1;

        int minLn = MIN_LAST_NAME_LENGTH;
        int maxLn = ln.length();

        List<String> candidates = new ArrayList<>();

        for (int fnLen = maxFn, lnLen = minLn; fnLen >= minFn && lnLen <= maxLn; fnLen--, lnLen++) {

            String base = fn.substring(0, fnLen) + ln.substring(0, lnLen);

            if (base.length() == USERNAME_LENGTH) {
                candidates.add(base);
            }
        }

        return candidates;
    }

    /**
     * Normalizes a string by converting it to lowercase, replacing selected special characters, and
     * removing unsupported characters.
     *
     * @param value the string to normalize
     * @return the normalized string, or an empty string if null
     */
    public static String normalize(final String value) {
        if (value == null) {
            return "";
        }

        return value.toLowerCase()
                .replace("ä", "a")
                .replace("ö", "o")
                .replace("å", "a")
                .replaceAll("[^a-z0-9]", "");
    }

    /**
     * Generates an email address for the given username.
     *
     * @param username the username to use
     * @return the generated email address
     */
    public static String generateEmail(final String username) {
        return username + "@edujournal.fi";
    }

    /**
     * Generates a temporary password using a random UUID.
     *
     * @return an eight-character temporary password
     */
    public static String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, TEMP_PASSWORD_LENGTH);
    }

    /**
     * Hashes a password using BCrypt.
     *
     * @param password the password to hash
     * @return the BCrypt password hash
     */
    public static String hashPassword(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Generates a seven-digit student number.
     *
     * @return the generated student number
     */
    public static String generateStudentNumber() {
        int number = RANDOM.nextInt(STUDENT_NUMBER_BOUND);
        return String.format("%07d", number);
    }
}
