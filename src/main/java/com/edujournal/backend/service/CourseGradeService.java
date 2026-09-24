package com.edujournal.backend.service;

import com.edujournal.backend.utils.BestScore;
import com.edujournal.backend.utils.GradeNormalizer;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Grades;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseGradeService {
    private final GradeNormalizer normalizer = new GradeNormalizer();
    private final BestScore bestScore = new BestScore();

    public double calculateFinalGrade(List<Assessments> assessments, List<Grades> grades) {
        Map<Integer, List<Grades>> gradesByAssessment = grades.stream()
                .collect(Collectors.groupingBy(Grades::getAssessmentId));

        double weightedSum = 0;
        double totalWeight = 0;

        for (Assessments a : assessments) {
            List<Grades> assessmentGrades = gradesByAssessment.getOrDefault(a.getId(), List.of());
            if (assessmentGrades.isEmpty()) {
                throw new IllegalArgumentException("No grades for assessment: " + a.getTitle());
            }

            double[] rawScores = assessmentGrades.stream().mapToDouble(Grades::getScore).toArray();
            double score = bestScore.getBestScore(rawScores);

            if (score > a.getMaxScore()) {
                throw new IllegalArgumentException(
                        "Score " + score + " exceeds max score " + a.getMaxScore()
                );
            }

            double normalized = normalizer.normalizer(score, a.getMaxScore());
            weightedSum += normalized * a.getWeight();
            totalWeight += a.getWeight();
        }

        if (totalWeight == 0) {
            throw new IllegalArgumentException("Total weight cannot be zero");
        }

        return Math.round((weightedSum / totalWeight) * 100.0) / 100.0;
    }
}
