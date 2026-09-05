package com.edujournal.backend;

public class GradeCalculator {
    public static double calculateAverage(double[] grades) {
        if (grades == null || grades.length == 0) {
            throw new IllegalArgumentException("Grades array cannot be null or empty");
        }
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }

        double result = sum / grades.length;
        return Math.round(result * 100.0) / 100.0;
    }

    public static double calculateWeightedAverage(double[] grades, double[] weights) {
        if (grades == null || weights == null || grades.length != weights.length || grades.length == 0) {
            throw new IllegalArgumentException("Grades and weights arrays must be non-null and of the same length");
        }
        double weightedSum = 0;
        double totalWeight = 0;
        for (int i = 0; i < grades.length; i++) {
            if (grades[i] < 0 || grades[i] > 100) {
                throw new IllegalArgumentException("Grades should be between 0 and 100");
            }

            if (weights[i] < 0.01 || weights[i] > 1.0) {
                throw new IllegalArgumentException("Weights should be between 0.01 and 1.0");
            }

            weightedSum += grades[i] * weights[i];
            totalWeight += weights[i];
        }

        if (totalWeight == 0) {
            throw new IllegalArgumentException("Total weight cannot be zero");
        }

        double result = weightedSum / totalWeight;
        return Math.round(result * 100.0) / 100.0;
    }
}
