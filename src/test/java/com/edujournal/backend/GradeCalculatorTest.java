package com.edujournal.backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    @Test
    void calculateAverage() {
        double[] grades1 = {90, 80, 70, 60};
        double[] grades2 = {100, 60, 80, 100};
        double[] grades3 = {};

        assertEquals(75.0, GradeCalculator.calculateAverage(grades1), 0.001);
        assertEquals(85.0, GradeCalculator.calculateAverage(grades2), 0.001);
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateAverage(grades3));
    }

    @Test
    void calculateWeightedAverage() {
        double[] grades1 = {90, 80, 70, 60};
        double[] weights1 = {0.4, 0.3, 0.2, 0.1};
        double[] grades2 = {100, 60, 80, 100};
        double[] weights2 = {0.5, 0.2, 0.2, 0.1};
        double[] grades3 = {};
        double[] weights3 = {};
        double[] grades4 = {90, 80};
        double[] weights4 = {0.4, 0.3};
        double[] grades5 = {100, -60};
        double[] weights5 = {-0.5, 0.2};
        double[] grades6 = {101, 80};
        double[] weights6 = {0.0, 0.0};
        double[] weights7 = {0.2, 1.01};

        // Correct calculations
        assertEquals(80.0, GradeCalculator.calculateWeightedAverage(grades1, weights1), 0.001);
        assertEquals(88.0, GradeCalculator.calculateWeightedAverage(grades2, weights2), 0.001);

        // Exceptions
        // Empty arrays
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades3, weights3));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades1, weights3));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades3, weights1));
        // Mismatched lengths
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades1, weights4));
        // Grades or weights are out of range
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades5, weights4));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades4, weights5));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades6, weights4));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades6, weights7));
        // Total weight is zero
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades4, weights6));
        // Grades or weights are null
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(null, weights1));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(grades1, null));
    }
}