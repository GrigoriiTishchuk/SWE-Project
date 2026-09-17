package com.edujournal.dao;

import com.edujournal.entity.Assessments;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentsDAOTest {

    private final AssessmentsDAO assessmentsDAO = new AssessmentsDAO();

    @Test
    void findAll_shouldReturnAssessments() {

        List<Assessments> assessments = assessmentsDAO.findAll();

        assertNotNull(assessments);

        System.out.println("Assessment count: " + assessments.size());

        for (Assessments assessment : assessments) {
            System.out.println(
                    assessment.getId() + " - " +
                            assessment.getTitle()
            );
        }
    }
}