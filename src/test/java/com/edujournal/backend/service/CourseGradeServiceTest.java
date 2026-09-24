package com.edujournal.backend.service;

import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Grades;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CourseGradeServiceTest {

    private final CourseGradeService service = new CourseGradeService();

    // Set id for testing
    private void setId(Object entity, int id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void calculateFinalGrade_RegularCategoryAverage() {
        Assessments a1 = new Assessments();
        a1.setType(AssessmentType.HOMETASK);
        a1.setMaxScore(10.0);
        a1.setWeight(1.0);
        setId(a1, 1);

        Grades g1 = new Grades();
        g1.setScore(8.0);
        g1.setAssessmentId(1);

        Grades g2 = new Grades();
        g2.setScore(6.0);
        g2.setAssessmentId(1);

        double result = service.calculateFinalGrade(List.of(a1), List.of(g1, g2));
        assertEquals(70.0, result);
    }

    @Test
    void calculateFinalGrade_missingGradesForOneAssessment() {
        Assessments a1 = new Assessments();
        a1.setType(AssessmentType.HOMETASK);
        a1.setMaxScore(10.0);
        a1.setWeight(1.0);
        setId(a1, 1);

        Assessments a2 = new Assessments();
        a2.setType(AssessmentType.HOMETASK);
        a2.setMaxScore(10.0);
        a2.setWeight(1.0);
        setId(a2, 2);

        Grades g1 = new Grades();
        g1.setScore(8.0);
        g1.setAssessmentId(1);

        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalGrade(List.of(a1, a2), List.of(g1)));
    }

    @Test
    void calculateFinalGrade_bestExamScore() {
        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(1.0);
        setId(exam, 10);

        Grades g1 = new Grades();
        g1.setScore(70.0);
        g1.setAssessmentId(10);

        Grades g2 = new Grades();
        g2.setScore(90.0);
        g2.setAssessmentId(10);

        double result = service.calculateFinalGrade(List.of(exam), List.of(g1, g2));
        assertEquals(90.0, result);
    }

    @Test
    void calculateFinalGrade_examScoreExceedsMax() {
        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(1.0);
        setId(exam, 10);

        Grades g1 = new Grades();
        g1.setScore(150.0);
        g1.setAssessmentId(10);

        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalGrade(List.of(exam), List.of(g1)));
    }

    @Test
    void calculateFinalGrade_examWithoutGrades() {
        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(1.0);
        setId(exam, 10);
        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalGrade(List.of(exam), List.of()));
    }

    @Test
    void calculateFinalGrade_mixedTypesOrder() {
        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(0.5);
        setId(exam, 1);

        Assessments ht = new Assessments();
        ht.setType(AssessmentType.HOMETASK);
        ht.setMaxScore(10.0);
        ht.setWeight(0.5);
        setId(ht, 2);

        Grades g1 = new Grades();
        g1.setScore(90.0);
        g1.setAssessmentId(1);

        Grades g2 = new Grades();
        g2.setScore(10.0);
        g2.setAssessmentId(2);

        double result = service.calculateFinalGrade(List.of(exam, ht), List.of(g1, g2));
        assertEquals(95.0, result);
    }

    @Test
    void calculateFinalGrade_applyWeightsCorrectly() {
        Assessments ht = new Assessments();
        ht.setType(AssessmentType.HOMETASK);
        ht.setMaxScore(10.0);
        ht.setWeight(0.4);
        setId(ht, 1);

        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(0.6);
        setId(exam, 2);

        Grades g1 = new Grades();
        g1.setScore(10.0);
        g1.setAssessmentId(1);

        Grades g2 = new Grades();
        g2.setScore(80.0);
        g2.setAssessmentId(2);

        double result = service.calculateFinalGrade(List.of(ht, exam), List.of(g1, g2));
        assertEquals(88.0, result);
    }

    @Test
    void calculateFinalGrade_throwIfNoGradesForAssessment() {
        Assessments a1 = new Assessments();
        a1.setType(AssessmentType.HOMETASK);
        a1.setMaxScore(10.0);
        a1.setWeight(1.0);
        setId(a1, 1);

        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalGrade(List.of(a1), List.of()));
    }

    @Test
    void calculateFinalGrade_throwIfScoreExceedsMax() {
        Assessments a1 = new Assessments();
        a1.setType(AssessmentType.HOMETASK);
        a1.setMaxScore(10.0);
        a1.setWeight(1.0);
        setId(a1, 1);

        Grades g1 = new Grades();
        g1.setScore(15.0);
        g1.setAssessmentId(1);

        assertThrows(IllegalArgumentException.class, () -> service.calculateFinalGrade(List.of(a1), List.of(g1)));
    }

    @Test
    void calculateFinalGrade_weightedAverageAcrossCategories() {
        Assessments ht1 = new Assessments();
        ht1.setType(AssessmentType.HOMETASK);
        ht1.setMaxScore(10.0);
        ht1.setWeight(0.3);
        setId(ht1, 1);

        Assessments ht2 = new Assessments();
        ht2.setType(AssessmentType.HOMETASK);
        ht2.setMaxScore(10.0);
        ht2.setWeight(0.3);
        setId(ht2, 2);

        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(0.4);
        setId(exam, 3);

        Grades g1 = new Grades();
        g1.setScore(10.0);
        g1.setAssessmentId(1);

        Grades g2 = new Grades();
        g2.setScore(5.0);
        g2.setAssessmentId(2);

        Grades g3 = new Grades();
        g3.setScore(90.0);
        g3.setAssessmentId(3);

        double result = service.calculateFinalGrade(List.of(ht1, ht2, exam), List.of(g1, g2, g3));
        assertEquals(83.57, result);
    }

    @Test
    void calculateFinalGrade_fullCourseScenario() {
        Assessments ht1 = new Assessments();
        ht1.setType(AssessmentType.HOMETASK);
        ht1.setMaxScore(10.0);
        ht1.setWeight(0.2);
        setId(ht1, 1);

        Assessments ht2 = new Assessments();
        ht2.setType(AssessmentType.HOMETASK);
        ht2.setMaxScore(20.0);
        ht2.setWeight(0.2);
        setId(ht2, 2);

        Assessments ht3 = new Assessments();
        ht3.setType(AssessmentType.HOMETASK);
        ht3.setMaxScore(15.0);
        ht3.setWeight(0.2);
        setId(ht3, 3);

        Grades g_ht1 = new Grades();
        g_ht1.setScore(9.0);
        g_ht1.setAssessmentId(1);

        Grades g_ht2 = new Grades();
        g_ht2.setScore(15.0);
        g_ht2.setAssessmentId(2);

        Grades g_ht3 = new Grades();
        g_ht3.setScore(12.0);
        g_ht3.setAssessmentId(3);
        // Homework average = (90 + 75 + 80) / 3 = 81.67%

        Assessments lab1 = new Assessments();
        lab1.setType(AssessmentType.LAB);
        lab1.setMaxScore(50.0);
        lab1.setWeight(0.3);
        setId(lab1, 4);

        Assessments lab2 = new Assessments();
        lab2.setType(AssessmentType.LAB);
        lab2.setMaxScore(50.0);
        lab2.setWeight(0.3);
        setId(lab2, 5);

        Grades g_lab1 = new Grades();
        g_lab1.setScore(40.0);
        g_lab1.setAssessmentId(4);

        Grades g_lab2 = new Grades();
        g_lab2.setScore(45.0);
        g_lab2.setAssessmentId(5);
        // Lab average = (80 + 90) / 2 = 85%

        Assessments exam = new Assessments();
        exam.setType(AssessmentType.FINALEXAM);
        exam.setMaxScore(100.0);
        exam.setWeight(0.5);
        setId(exam, 6);

        Grades g_exam1 = new Grades();
        g_exam1.setScore(70.0);

        Grades g_exam2 = new Grades();
        g_exam2.setScore(85.0);

        Grades g_exam3 = new Grades();
        g_exam3.setScore(90.0);

        g_exam1.setAssessmentId(6);
        g_exam2.setAssessmentId(6);
        g_exam3.setAssessmentId(6);
        // Exam best = 90%

        double result = service.calculateFinalGrade(
                List.of(ht1, ht2, ht3, lab1, lab2, exam),
                List.of(g_ht1, g_ht2, g_ht3, g_lab1, g_lab2, g_exam1, g_exam2, g_exam3)
        );

        // Weighted average: 81.67 * 0.2 + 85 * 0.3 + 90 * 0.5
        // weightedSum = 16.33 + 25.5 + 45 = 86.83
        // totalWeight = 0.2 + 0.3 + 0.5 = 1.0
        // final = 86.83

        assertEquals(86.83, result, 0.01);
    }
}
