
package com.edujournal.backend.utils;

/**
 * Provides utility methods for finding the highest score
 * from an array of scores.
 *
 * @author EduJournal
 */
public final class BestScore {

    /**
     * Finds the highest score in the given array.
     *
     * @param scores the array of scores
     * @return the highest score
     * @throws IllegalArgumentException if the array is null or empty
     */
    public double getBestScore(final double[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException(
                    "Score array cannot be null or empty.");
        }

        double bestScore = scores[0];

        for (double score : scores) {
            if (score > bestScore) {
                bestScore = score;
            }
        }

        return bestScore;
    }
}
