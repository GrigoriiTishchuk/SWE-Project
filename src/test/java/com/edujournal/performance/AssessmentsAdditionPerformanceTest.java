package com.edujournal.performance;

import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.dao.AssessmentsDAO;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AssessmentsAdditionPerformanceTest {

    private AssessmentsService assessmentsService;
    private AssessmentsDAO assessmentsDAO;

    private int counter;
    private List<Integer> createdAssessmentIds;

    @Setup(Level.Trial)
    public void setup() {
        assessmentsService = new AssessmentsService();
        assessmentsDAO = new AssessmentsDAO();

        counter = 0;
        createdAssessmentIds = new ArrayList<>();
    }

    @Benchmark
    public void addAssessment(Blackhole blackhole) {
        counter++;

        Assessments assessment = new Assessments();
        assessment.setCourseId(1);
        assessment.setTitle("Performance Assessment " + counter);
        assessment.setType(AssessmentType.EXAM1);
        assessment.setMaxScore(100.0);
        assessment.setWeight(25.0);
        assessment.setDueDate(LocalDate.now().plusDays(30));

        assessmentsService.save(assessment);

        createdAssessmentIds.add(assessment.getId());

        blackhole.consume(assessment);
    }

    @TearDown(Level.Iteration)
    public void cleanup() {
        for (Integer id : createdAssessmentIds) {
            Assessments assessment = assessmentsDAO.findById(id);

            if (assessment != null) {
                assessmentsDAO.delete(assessment);
            }
        }

        createdAssessmentIds.clear();
    }
}