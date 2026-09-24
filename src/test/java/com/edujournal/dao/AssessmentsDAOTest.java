package com.edujournal.dao;

import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Grades;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentsDAOTest {

    private final AssessmentsDAO assessmentsDAO =
            new AssessmentsDAO();

    private final List<Assessments> createdAssessments =
            new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Assessments assessment : createdAssessments) {
            assessmentsDAO.delete(assessment);
        }

        createdAssessments.clear();
    }

    @Test
    void findByIdReturnsNullForNonExistingId() {

        Assessments found =
                assessmentsDAO.findById(999999);

        assertNull(found);
    }

    @Test
    void findAllReturnsSavedAssessments() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Find All Test Assessment");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        List<Assessments> assessments =
                assessmentsDAO.findAll();

        assertNotNull(assessments);

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment.getId().equals(a.getId()))
        );
    }

    @Test
    void findAllWithMultipleAssessments() {

        Assessments assessment1 = new Assessments();
        assessment1.setCourseId(1);
        assessment1.setTitle("Assessment A");
        assessment1.setType(AssessmentType.EXAM1);
        assessment1.setMaxScore(100.0);
        assessment1.setWeight(20.0);

        Assessments assessment2 = new Assessments();
        assessment2.setCourseId(1);
        assessment2.setTitle("Assessment B");
        assessment2.setType(AssessmentType.PROJECT);
        assessment2.setMaxScore(50.0);
        assessment2.setWeight(10.0);

        Assessments assessment3 = new Assessments();
        assessment3.setCourseId(1);
        assessment3.setTitle("Assessment C");
        assessment3.setType(AssessmentType.LAB);
        assessment3.setMaxScore(50.0);
        assessment3.setWeight(10.0);

        assessmentsDAO.save(assessment1);
        assessmentsDAO.save(assessment2);
        assessmentsDAO.save(assessment3);

        createdAssessments.add(assessment1);
        createdAssessments.add(assessment2);
        createdAssessments.add(assessment3);

        List<Assessments> assessments =
                assessmentsDAO.findAll();

        assertTrue(
                assessments.stream()
                        .anyMatch(a -> assessment1.getId().equals(a.getId()))
        );

        assertTrue(
                assessments.stream()
                        .anyMatch(a -> assessment2.getId().equals(a.getId()))
        );

        assertTrue(
                assessments.stream()
                        .anyMatch(a -> assessment3.getId().equals(a.getId()))
        );
    }

    @Test
    void findByCourseId() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Course Assessment");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        List<Assessments> assessments =
                assessmentsDAO.findByCourseId(1);

        assertNotNull(assessments);

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment.getId().equals(a.getId()))
        );
    }

    @Test
    void findByCourseIdReturnsEmptyListForNonExistingCourseId() {

        List<Assessments> assessments =
                assessmentsDAO.findByCourseId(1234);

        assertNotNull(assessments);
        assertTrue(assessments.isEmpty());
    }

    @Test
    void findByCourseIdWithMultipleAssessments() {

        Assessments assessment1 = new Assessments();
        assessment1.setCourseId(1);
        assessment1.setTitle("Course Assessment A");
        assessment1.setType(AssessmentType.EXAM1);
        assessment1.setMaxScore(100.0);
        assessment1.setWeight(20.0);

        Assessments assessment2 = new Assessments();
        assessment2.setCourseId(1);
        assessment2.setTitle("Course Assessment B");
        assessment2.setType(AssessmentType.PROJECT);
        assessment2.setMaxScore(50.0);
        assessment2.setWeight(10.0);

        assessmentsDAO.save(assessment1);
        assessmentsDAO.save(assessment2);

        createdAssessments.add(assessment1);
        createdAssessments.add(assessment2);

        List<Assessments> assessments =
                assessmentsDAO.findByCourseId(1);

        assertNotNull(assessments);

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment1.getId().equals(a.getId()))
        );

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment2.getId().equals(a.getId()))
        );
    }

    @Test
    void findByCourseIdDoesNotReturnOtherCoursesAssessments() {

        Assessments assessment1 = new Assessments();
        assessment1.setCourseId(1);
        assessment1.setTitle("Course 1 Assessment");
        assessment1.setType(AssessmentType.EXAM1);
        assessment1.setMaxScore(100.0);
        assessment1.setWeight(20.0);

        Assessments assessment2 = new Assessments();
        assessment2.setCourseId(2);
        assessment2.setTitle("Course 2 Assessment");
        assessment2.setType(AssessmentType.PROJECT);
        assessment2.setMaxScore(50.0);
        assessment2.setWeight(10.0);

        assessmentsDAO.save(assessment1);
        assessmentsDAO.save(assessment2);

        createdAssessments.add(assessment1);
        createdAssessments.add(assessment2);

        List<Assessments> assessments =
                assessmentsDAO.findByCourseId(1);

        assertTrue(
                assessments.stream()
                        .anyMatch(a ->
                                assessment1.getId().equals(a.getId()))
        );

        assertTrue(
                assessments.stream()
                        .noneMatch(a ->
                                assessment2.getId().equals(a.getId()))
        );
    }

    @Test
    void saveAndFindById() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Test Assessment");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assertNotNull(assessment.getId());

        Assessments found =
                assessmentsDAO.findById(assessment.getId());

        assertNotNull(found);
        assertEquals("Test Assessment", found.getTitle());
        assertEquals(AssessmentType.EXAM1, found.getType());
    }

    @Test
    void saveWithNullTitle() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle(null);
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assertThrows(Exception.class, () ->
                assessmentsDAO.save(assessment)
        );
    }

    @Test
    void saveWithEmptyTitle() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assertNotNull(assessment.getId());
    }

    @Test
    void saveWithLongTitle() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("A".repeat(101));
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assertThrows(Exception.class, () ->
                assessmentsDAO.save(assessment)
        );
    }

    @Test
    void saveWithNullCourseId() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(null);
        assessment.setTitle("Test Assessment");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assertThrows(Exception.class, () ->
                assessmentsDAO.save(assessment)
        );
    }

    @Test
    void saveWithInvalidCourseId() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1234);
        assessment.setTitle("Invalid Course Assessment");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assertThrows(Exception.class, () ->
                assessmentsDAO.save(assessment)
        );
    }

    @Test
    void update() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Old Assessment");
        assessment.setType(AssessmentType.PROJECT);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assessment.setTitle("Updated Assessment");

        assessmentsDAO.update(assessment);

        Assessments updated =
                assessmentsDAO.findById(assessment.getId());

        assertNotNull(updated);
        assertEquals(
                "Updated Assessment",
                updated.getTitle()
        );
    }

    @Test
    void updateWithNullTitle() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Assessment Before Update");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assessment.setTitle(null);

        assertThrows(Exception.class, () ->
                assessmentsDAO.update(assessment)
        );
    }

    @Test
    void updateWithEmptyTitle() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Assessment Before Update");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assessment.setTitle("");

        assessmentsDAO.update(assessment);

        Assessments updated =
                assessmentsDAO.findById(assessment.getId());

        assertNotNull(updated);
        assertEquals("", updated.getTitle());
    }

    @Test
    void updateWithInvalidCourseId() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Assessment Before Update");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);
        createdAssessments.add(assessment);

        assessment.setCourseId(1234);

        assertThrows(Exception.class, () ->
                assessmentsDAO.update(assessment)
        );
    }

    @Test
    void delete() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Assessment To Delete");
        assessment.setType(AssessmentType.LAB);
        assessment.setMaxScore(50.0);
        assessment.setWeight(10.0);

        assessmentsDAO.save(assessment);

        assertNotNull(assessment.getId());

        Assessments found =
                assessmentsDAO.findById(assessment.getId());

        assertNotNull(found);

        assessmentsDAO.delete(assessment);

        Assessments deleted =
                assessmentsDAO.findById(assessment.getId());

        assertNull(deleted);
    }

    @Test
    void deleteWithConstraintViolation() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Assessment With Grade");
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        assessmentsDAO.save(assessment);

        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(assessment.getId());
        grade.setScore(80.0);

        GradesDAO gradesDAO = new GradesDAO();
        gradesDAO.save(grade);

        assertThrows(Exception.class, () ->
                assessmentsDAO.delete(assessment)
        );

        gradesDAO.deleteById(grade.getId());
        assessmentsDAO.delete(assessment);
    }

}