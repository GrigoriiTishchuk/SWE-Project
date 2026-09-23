package com.edujournal.dao;

import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
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
    void findAll_shouldReturnAssessments() {

        List<Assessments> assessments =
                assessmentsDAO.findAll();

        assertNotNull(assessments);
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
}