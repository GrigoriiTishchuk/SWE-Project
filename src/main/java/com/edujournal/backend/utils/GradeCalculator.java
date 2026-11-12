
package com.edujournal.backend.utils;

/**
 * Provides utility methods for calculating average and
 * weighted average grades.
 *
 * @author EduJournal
 */
public final class GradeCalculator {

    /** Minimum allowed grade value. */
    private static final double MIN_GRADE = 0.0;

    /** Maximum allowed grade value. */
    private static final double MAX_GRADE = 100.0;

    /** Minimum allowed weight value. */
    private static final double MIN_WEIGHT = 0.0;

    /** Maximum allowed weight value. */
    private static final double MAX_WEIGHT = 1.0;

    /** Multiplier used to round results to two decimal places. */
    private static final double ROUNDING_FACTOR = 100.0;

    /**
     * Prevents instantiation of this utility class.
     */
    private GradeCalculator() {
        // Utility class
    }

    /**
     * Calculates the arithmetic average of the given grades.
     *
     * @param grades the array of grades
     * @return the average grade rounded to two decimal places
     * @throws IllegalArgumentException if the array is null, empty,
     * or contains a grade outside the allowed range
     */
    public static double calculateAverage(final double[] grades) {
        if (grades == null || grades.length == 0) {
            throw new IllegalArgumentException(
                    "Grades array cannot be null or empty.");
        }

        double sum = 0.0;

        for (double grade : grades) {
            if (grade < MIN_GRADE || grade > MAX_GRADE) {
                throw new IllegalArgumentException(
                        "Grades should be between 0 and 100.");
            }

            sum += grade;
        }

        double result = sum / grades.length;

        return Math.round(result * ROUNDING_FACTOR)
                / ROUNDING_FACTOR;
    }

    /**
     * Calculates the weighted average of the given grades.
     *
     * @param grades the array of grades
     * @param weights the array of corresponding weights
     * @return the weighted average rounded to two decimal places
     * @throws IllegalArgumentException if either array is null or empty,
     * if their lengths differ, if a grade or weight is outside
     * the allowed range, or if the total weight is zero
     */
    public static double calculateWeightedAverage(
            final double[] grades,
            final double[] weights) {

        if (grades == null || weights == null
                || grades.length != weights.length
                || grades.length == 0) {
            throw new IllegalArgumentException(
                    "Grades and weights arrays must be non-null "
                            + "and of the same length.");
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (int i = 0; i < grades.length; i++) {
            if (grades[i] < MIN_GRADE
                    || grades[i] > MAX_GRADE) {
                throw new IllegalArgumentException(
                        "Grades should be between 0 and 100.");
            }

            if (weights[i] < MIN_WEIGHT
                    || weights[i] > MAX_WEIGHT) {
                throw new IllegalArgumentException(
                        "Weights should be between 0 and 1.0.");
            }

            weightedSum += grades[i] * weights[i];
            totalWeight += weights[i];
        }

        if (totalWeight == 0.0) {
            throw new IllegalArgumentException(
                    "Total weight cannot be zero.");
        }

        double result = weightedSum / totalWeight;

        return Math.round(result * ROUNDING_FACTOR)
                / ROUNDING_FACTOR;
    }
}
