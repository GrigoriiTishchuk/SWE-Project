package com.edujournal.backend.service;

import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.entity.Grades;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssessmentsServiceTest {

    private final AssessmentsService service =
            new AssessmentsService();

    private final List<Assessments> createdAssessments =
            new ArrayList<>();

    private final List<Grades> createdGrades =
            new ArrayList<>();

    @AfterEach
    void cleanup() {

        GradesDAO gradesDAO = new GradesDAO();

        for (Grades grade : createdGrades) {
            gradesDAO.deleteById(grade.getId());
        }

        for (Assessments assessment : createdAssessments) {
            service.delete(assessment);
        }

        createdGrades.clear();
        createdAssessments.clear();
    }

    @Test
    void getByCourseIdReturnsAssessments() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Course ID Test");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);
        createdAssessments.add(assessment);

        List<Assessments> assessments =
                service.getByCourseId(1);

        assertNotNull(assessments);

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment.getId().equals(a.getId()))
        );
    }

    @Test
    void getByCourseIdReturnsEmptyListForNonExistingCourseId() {

        List<Assessments> assessments =
                service.getByCourseId(999999);

        assertNotNull(assessments);
        assertTrue(assessments.isEmpty());
    }

    @Test
    void getByCourseNameReturnsEmptyListForNonExistingCourseName() {

        List<Assessments> assessments =
                service.getByCourseName(
                        "Non Existing Course"
                );

        assertNotNull(assessments);
        assertTrue(assessments.isEmpty());
    }

    @Test
    void findByIdReturnsAssessment() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Find By ID Test");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);
        createdAssessments.add(assessment);

        Assessments found =
                service.findById(assessment.getId());

        assertNotNull(found);
        assertEquals(
                assessment.getId(),
                found.getId()
        );
        assertEquals(
                "Find By ID Test",
                found.getTitle()
        );
    }

    @Test
    void findByIdReturnsNullForNonExistingId() {

        Assessments found =
                service.findById(1234);

        assertNull(found);
    }

    @Test
    void getCourseByName() {

        Course course =
                service.getCourseByName(
                        "Software Engineering"
                );

        assertNotNull(course);
        assertEquals(
                "Software Engineering",
                course.getName()
        );
    }

    @Test
    void getCourseByNameReturnsNullForNonExistingCourseName() {

        Course course =
                service.getCourseByName(
                        "Non Existing Course"
                );

        assertNull(course);
    }

    @Test
    void hasGradesReturnsFalseWhenAssessmentHasNoGrades() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("No Grades Test");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);
        createdAssessments.add(assessment);

        boolean hasGrades =
                service.hasGrades(assessment.getId());

        assertFalse(hasGrades);
    }

    @Test
    void hasGradesReturnsTrueWhenAssessmentHasGrades() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Has Grades Test");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);
        createdAssessments.add(assessment);

        Grades grade = new Grades();

        grade.setEnrollmentId(1);
        grade.setAssessmentId(assessment.getId());
        grade.setScore(85.0);
        grade.setComment("Good");

        GradesDAO gradesDAO = new GradesDAO();
        gradesDAO.save(grade);

        createdGrades.add(grade);

        assertTrue(
                service.hasGrades(assessment.getId())
        );
    }

    @Test
    void getByCourseNameReturnsAssessments() {

        List<Assessments> assessments =
                service.getByCourseName(
                        "Software Engineering"
                );

        assertNotNull(assessments);
        assertFalse(assessments.isEmpty());
    }

    @Test
    void save() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Test Assessment");
        assessment.setType(AssessmentType.PROJECT);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);

        createdAssessments.add(assessment);

        assertNotNull(assessment.getId());

    }

    @Test
    void update() {

        Assessments assessment =
                new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Update Test");
        assessment.setType(AssessmentType.PROJECT);
        assessment.setMaxScore(100.0);
        assessment.setWeight(10.0);

        service.save(assessment);

        createdAssessments.add(assessment);

        Integer id = assessment.getId();

        assessment.setTitle("Updated Assessment");

        service.update(assessment);

        Assessments updated =
                service.getByCourseId(1)
                        .stream()
                        .filter(a ->
                                a.getId().equals(id)
                        )
                        .findFirst()
                        .orElse(null);

        assertNotNull(updated);
        assertEquals(
                "Updated Assessment",
                updated.getTitle()
        );

    }

    @Test
    void delete() {

        Assessments assessment =
                new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Delete Test");
        assessment.setType(AssessmentType.PROJECT);
        assessment.setMaxScore(100.0);
        assessment.setWeight(10.0);

        service.save(assessment);

        Integer id = assessment.getId();

        service.delete(assessment);

        Assessments deleted =
                service.getByCourseId(1)
                        .stream()
                        .filter(a ->
                                a.getId().equals(id)
                        )
                        .findFirst()
                        .orElse(null);

        assertNull(deleted);
    }
}