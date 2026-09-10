package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeNormalizerTest {
    @Test
    void normalizeTest() {
        GradeNormalizer norm = new GradeNormalizer();

        assertEquals(80.0, norm.normalizer(4, 5), 0.001);
        assertEquals(0.0, norm.normalizer(0, 100), 0.001);
        assertEquals(90.0, norm.normalizer(90, 100), 0.001);
        assertEquals(100.0, norm.normalizer(10, 10), 0.001);

        // Round
        assertEquals(66.666, norm.normalizer(2, 3), 0.001);

        // Big numbers
        assertEquals(50.0, norm.normalizer(500, 1000), 0.001);

        // Exceptions
        assertThrows(IllegalArgumentException.class, () -> norm.normalizer(0, -1));
        assertThrows(IllegalArgumentException.class, () -> norm.normalizer(1, 0));
    }
}