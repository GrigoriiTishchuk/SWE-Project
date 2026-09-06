package com.edujournal.backend.service;

import com.edujournal.backend.utils.BestScore;
import com.edujournal.backend.utils.GradeCalculator;
import com.edujournal.backend.utils.GradeNormalizer;
import com.edujournal.model.AssessmentType;
import com.edujournal.model.Assessments;
import com.edujournal.model.Grades;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseGradeService {
    private final GradeNormalizer normalizer = new GradeNormalizer();
    private final BestScore bestScore = new BestScore();

    public double calculateFinalGrade(List<Assessments> assessments, List<Grades> grades) {
        // Group assessments by enum type
        Map<AssessmentType, List<Assessments>> assessmentsForType = assessments.stream().collect(Collectors.groupingBy(Assessments::getType));

        // Group grades by assessmentId
        Map<Integer, List<Grades>> gradesForAssessment = grades.stream().collect(Collectors.groupingBy(Grades::getAssessmentId));

        List<Double> typeAverages = new ArrayList<>();
        List<Double> typeWeights = new ArrayList<>();

        for (AssessmentType type : assessmentsForType.keySet()) {
            if (type.name().contains("EXAM")) {
                double average = processExamCategory(assessmentsForType.get(type), gradesForAssessment);
                typeAverages.add(average);
                typeWeights.add(assessmentsForType.get(type).get(0).getWeight());
            } else {
                double avg = calculateCategoryAverage(assessmentsForType.get(type), gradesForAssessment);
                typeAverages.add(avg);
                typeWeights.add(assessmentsForType.get(type).get(0).getWeight());
            }
        }
        return GradeCalculator.calculateWeightedAverage(typeAverages.stream().mapToDouble(Double::doubleValue).toArray(),
                typeWeights.stream().mapToDouble(Double::doubleValue).toArray()
        );
    }

    // Method to calculate the average score for a category of assessments
    private double calculateCategoryAverage(List<Assessments> assessments, Map<Integer, List<Grades>> gradesForAssessment) {
        List<Double> normalizedScores = new ArrayList<>();

        for (Assessments a : assessments) {
            List<Grades> g = gradesForAssessment.get(a.getId());
            if (g == null || g.isEmpty()) {
                throw new IllegalArgumentException("No Grades for Assessments with id " + a.getId());
            }
            for (Grades grade : g) {
                if (grade.getScore() > a.getMaxScore()) {
                    throw new IllegalArgumentException(
                            "Score " + grade.getScore() + " exceeds max score " + a.getMaxScore()
                    );
                }
                double normalizedScore = normalizer.normalizer(grade.getScore(), a.getMaxScore());
                normalizedScores.add(normalizedScore);
            }
        }
        return GradeCalculator.calculateAverage(normalizedScores.stream().mapToDouble(Double::doubleValue).toArray());
    }

    private double processExamCategory(List<Assessments> exams, Map<Integer, List<Grades>> gradesForAssessment) {
        List<Double> bestExamScores = new ArrayList<>();

        for (Assessments exam : exams) {
            List<Grades> examGrades = gradesForAssessment.getOrDefault(exam.getId(), List.of());

            if (!examGrades.isEmpty()) {
                double[] rawScores = examGrades.stream().mapToDouble(Grades::getScore).toArray();

                for (double score : rawScores) {
                    if (score > exam.getMaxScore()) {
                        throw new IllegalArgumentException(
                                "Score " + score + " exceeds max score " + exam.getMaxScore()
                        );
                    }
                }

                double bestRaw = bestScore.getBestScore(rawScores);
                double normalized = normalizer.normalizer(bestRaw, exam.getMaxScore());

                bestExamScores.add(normalized);
            }
        }

        return GradeCalculator.calculateAverage(
                bestExamScores.stream().mapToDouble(Double::doubleValue).toArray()
        );
    }
}
