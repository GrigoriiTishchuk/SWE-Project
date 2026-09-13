package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    @Test
    void calculateAverage() {
        assertEquals(75.0, GradeCalculator.calculateAverage(new double[]{90, 80, 70, 60}), 0.001);
        assertEquals(85.0, GradeCalculator.calculateAverage(new double[]{100, 60, 80, 100}), 0.001);
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateAverage(new double[]{}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateAverage(null));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateAverage(new double[]{120}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateAverage(new double[]{-5}));

    }

    @Test
    void calculateWeightedAverage() {
        // Correct calculations
        assertEquals(80.0, GradeCalculator.calculateWeightedAverage(new double[]{90, 80, 70, 60}, new double[]{0.4, 0.3, 0.2, 0.1}), 0.001);
        assertEquals(88.0, GradeCalculator.calculateWeightedAverage(new double[]{100, 60, 80, 100}, new double[]{0.5, 0.2, 0.2, 0.1}), 0.001);

        // Exceptions
        // Empty arrays
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{}, new double[]{}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(null, new double[]{0.4, 0.3, 0.2, 0.1}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{90, 80, 70, 60}, null));

        // Mismatched lengths
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{90, 80, 70, 60}, new double[]{0.4, 0.3}));

        // invalid grades
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{100, -5}, new double[]{0.5, 0.5}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{101, 80}, new double[]{0.5, 0.5}));

        // invalid weights
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{90, 80}, new double[]{-0.1, 0.5}));
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{90, 80}, new double[]{0.5, 1.5}));

        // total weight = 0
        assertThrows(IllegalArgumentException.class, () -> GradeCalculator.calculateWeightedAverage(new double[]{90, 80}, new double[]{0.0, 0.0}));

        // valid case with zero weight
        assertEquals(90.0, GradeCalculator.calculateWeightedAverage(new double[]{90, 80}, new double[]{1.0, 0.0}), 0.001);
    }
}