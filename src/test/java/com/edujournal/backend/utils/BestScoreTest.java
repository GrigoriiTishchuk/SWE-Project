package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BestScoreTest {

    @Test
    void getBestScore() {
        BestScore bestScore = new BestScore();

        double[] scores1 = {85.0, 90.5, 78.0, 92.0, 88.5};
        double[] scores2 = {0.0, 100, 3.0, 99.0, 10.5};
        double[] scores3 = {0.0};
        double[] scores4 = null;
        double[] scores5 = {};

        assertEquals(92.0, bestScore.getBestScore(scores1), 0.001);
        assertEquals(100.0, bestScore.getBestScore(scores2), 0.001);
        assertEquals(0.0, bestScore.getBestScore(scores3), 0.001);

        assertThrows(IllegalArgumentException.class, () -> bestScore.getBestScore(scores4));
        assertThrows(IllegalArgumentException.class, () -> bestScore.getBestScore(scores5));
    }
}