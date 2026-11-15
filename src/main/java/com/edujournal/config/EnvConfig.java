package com.edujournal.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class for accessing environment variables
 * from the .env configuration file.
 */
public final class EnvConfig {

    private static final Dotenv DOTENV = Dotenv.load();

    /**
     * Prevents instantiation of this utility class.
     */
    private EnvConfig() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the value of the specified environment variable.
     *
     * @param key the environment variable name
     * @return the value associated with the specified key,
     *         or null if the key does not exist
     */
    public static String get(String key) {
        return DOTENV.get(key);
    }
}