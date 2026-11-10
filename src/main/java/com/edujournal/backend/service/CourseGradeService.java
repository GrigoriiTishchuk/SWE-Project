
package com.edujournal.backend.service;

import com.edujournal.backend.utils.BestScore;
import com.edujournal.backend.utils.GradeCalculator;
import com.edujournal.backend.utils.GradeNormalizer;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Grades;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides services for calculating final course grades based on
 * assessment categories, scores, and category weights.
 *
 * @author EduJournal
 */
public final class CourseGradeService {

    /** Normalizes raw grades to a common scale. */
    private final GradeNormalizer normalizer =
            new GradeNormalizer();

    /** Finds the highest score among multiple assessment attempts. */
    private final BestScore bestScore = new BestScore();

    /**
     * Calculates the final weighted grade for a course.
     *
     * @param assessments the assessments belonging to the course
     * @param grades the grades associated with those assessments
     * @return the calculated weighted final grade
     */
    public double calculateFinalGrade(
            final List<Assessments> assessments,
            final List<Grades> grades) {

        // Group assessments by enum type.
        Map<AssessmentType, List<Assessments>> assessmentsForType =
                assessments.stream()
                        .collect(Collectors.groupingBy(
                                Assessments::getType));

        // Group grades by assessment ID.
        Map<Integer, List<Grades>> gradesForAssessment =
                grades.stream()
                        .collect(Collectors.groupingBy(
                                Grades::getAssessmentId));

        List<Double> typeAverages = new ArrayList<>();
        List<Double> typeWeights = new ArrayList<>();

        for (Map.Entry<AssessmentType, List<Assessments>> entry
                : assessmentsForType.entrySet()) {

            AssessmentType type = entry.getKey();
            List<Assessments> typeAssessments = entry.getValue();

            if (type.name().contains("EXAM")) {
                typeAverages.add(
                        processExamCategory(
                                typeAssessments, gradesForAssessment));
            } else {
                typeAverages.add(
                        calculateTypeAverage(
                                typeAssessments, gradesForAssessment));
            }

            typeWeights.add(typeAssessments.get(0).getWeight());
        }

        return GradeCalculator.calculateWeightedAverage(
                typeAverages.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray(),
                typeWeights.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray()
        );
    }

    /**
     * Calculates the average normalized score for an assessment category.
     *
     * @param assessments the assessments in the category
     * @param gradesForAssessment grades grouped by assessment ID
     * @return the average normalized score
     * @throws IllegalArgumentException if an assessment has no grades
     *         or a grade exceeds the maximum score
     */
    private double calculateTypeAverage(
            final List<Assessments> assessments,
            final Map<Integer, List<Grades>> gradesForAssessment) {

        List<Double> normalizedScores = new ArrayList<>();

        for (Assessments assessment : assessments) {
            // Get an empty list if no grades are available.
            List<Grades> assessmentGrades =
                    gradesForAssessment.getOrDefault(
                            assessment.getId(), List.of());

            if (assessmentGrades.isEmpty()) {
                throw new IllegalArgumentException(
                        "No Grades for Assessments with id "
                                + assessment.getId());
            }

            for (Grades grade : assessmentGrades) {
                if (grade.getScore() > assessment.getMaxScore()) {
                    throw new IllegalArgumentException(
                            "Score " + grade.getScore()
                                    + " exceeds max score "
                                    + assessment.getMaxScore());
                }

                double normalizedScore = normalizer.normalizer(
                        grade.getScore(), assessment.getMaxScore());

                normalizedScores.add(normalizedScore);
            }
        }

        return GradeCalculator.calculateAverage(
                normalizedScores.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray());
    }

    /**
     * Processes an exam category by selecting the best score
     * for each exam and returning the best normalized exam score.
     *
     * @param exams the exams in the category
     * @param gradesForAssessment grades grouped by assessment ID
     * @return the best normalized exam score
     * @throws IllegalArgumentException if a grade exceeds an exam's
     *         maximum score
     */
    private double processExamCategory(
            final List<Assessments> exams,
            final Map<Integer, List<Grades>> gradesForAssessment) {

        List<Double> bestExamScores = new ArrayList<>();

        for (Assessments exam : exams) {
            List<Grades> examGrades =
                    gradesForAssessment.getOrDefault(
                            exam.getId(), List.of());

            if (!examGrades.isEmpty()) {
                double[] rawScores = examGrades.stream()
                        .mapToDouble(Grades::getScore)
                        .toArray();

                for (double score : rawScores) {
                    if (score > exam.getMaxScore()) {
                        throw new IllegalArgumentException(
                                "Score " + score
                                        + " exceeds max score "
                                        + exam.getMaxScore());
                    }
                }

                double bestRaw = bestScore.getBestScore(rawScores);

                double normalized = normalizer.normalizer(
                        bestRaw, exam.getMaxScore());

                bestExamScores.add(normalized);
            }
        }

        return bestScore.getBestScore(
                bestExamScores.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray()
        );
    }
}
