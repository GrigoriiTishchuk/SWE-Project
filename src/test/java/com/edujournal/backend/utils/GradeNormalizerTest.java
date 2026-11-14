package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeNormalizerTest {
    @Test
    void normalizeTest() {
        assertEquals(80.0, GradeNormalizer.normalizer(4, 5), 0.001);
        assertEquals(0.0, GradeNormalizer.normalizer(0, 100), 0.001);
        assertEquals(90.0, GradeNormalizer.normalizer(90, 100), 0.001);
        assertEquals(100.0, GradeNormalizer.normalizer(10, 10), 0.001);

        // Round
        assertEquals(66.666, GradeNormalizer.normalizer(2, 3), 0.001);

        // Big numbers
        assertEquals(50.0, GradeNormalizer.normalizer(500, 1000), 0.001);

        // Exceptions
        assertThrows(IllegalArgumentException.class, () -> GradeNormalizer.normalizer(0, -1));
        assertThrows(IllegalArgumentException.class, () -> GradeNormalizer.normalizer(1, 0));
    }
}