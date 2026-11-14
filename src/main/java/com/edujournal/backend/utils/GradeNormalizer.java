package com.edujournal.backend.utils;

/**
 * Provides functionality for normalizing grades based on their maximum possible score.
 *
 * @author EduJournal
 */
public final class GradeNormalizer {

    /** Maximum normalized grade value. */
    private static final double NORMALIZED_MAX_SCORE = 100.0;

    /** Prevents instantiation of this utility class. */
    private GradeNormalizer() {
        throw new IllegalStateException("Utility class cannot be instantiated.");
    }

    /**
     * Normalizes a score to a percentage based on the maximum score.
     *
     * @param score the score to normalize
     * @param maxScore the maximum possible score
     * @return the normalized score as a percentage
     * @throws IllegalArgumentException if the maximum score is zero or less
     */
    public static double normalizer(final double score, final double maxScore) {

        if (maxScore <= 0) {
            throw new IllegalArgumentException("Max score cannot be zero or less.");
        }

        return (score / maxScore) * NORMALIZED_MAX_SCORE;
    }
}
