package com.edujournal.backend.utils;

public class BestScore {
    private GradeNormalizer normalizer = new GradeNormalizer();

    public double getBestScore(double[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("Score array cannot be null or empty.");
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
