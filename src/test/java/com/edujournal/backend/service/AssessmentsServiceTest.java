package com.edujournal.backend.service;

import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
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

    @AfterEach
    void cleanup() {
        for (Assessments assessment : createdAssessments) {
            service.delete(assessment);
        }

        createdAssessments.clear();
    }

    @Test
    void testGetByCourseName() {

        List<Assessments> assessments =
                service.getByCourseName(
                        "Software Engineering"
                );

        System.out.println(
                "Assessment count: "
                        + assessments.size()
        );

        for (Assessments assessment : assessments) {
            System.out.println(
                    assessment.getId()
                            + " - "
                            + assessment.getTitle()
            );
        }

        assertNotNull(assessments);
        assertFalse(assessments.isEmpty());
    }

    @Test
    void testSave() {

        Assessments assessment = new Assessments();

        assessment.setCourseId(1);
        assessment.setTitle("Test Assessment");
        assessment.setType(AssessmentType.PROJECT);
        assessment.setMaxScore(100.0);
        assessment.setWeight(20.0);

        service.save(assessment);

        createdAssessments.add(assessment);

        assertNotNull(assessment.getId());

        System.out.println(
                "Saved assessment ID: "
                        + assessment.getId()
        );
    }

    @Test
    void testUpdate() {

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

        System.out.println(
                "Updated assessment ID: "
                        + updated.getId()
        );
    }

    @Test
    void testDelete() {

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

        System.out.println(
                "Deleted assessment ID: " + id
        );
    }
}