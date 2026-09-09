package com.edujournal.backend.utils;

public class GradeNormalizer {
    public double normalizer(double score, double maxScore) {
        if (maxScore <= 0) {
            throw new IllegalArgumentException("Max score cannot be zero or less.");
        }
        return (score / maxScore) * 100;
    }
}
