package com.edujournal.performance;

import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Grades;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class GradesAdditionPerformanceTest {

    private GradesDAO gradesDAO;

    private int counter;
    private List<Integer> createdGradeIds;

    @Setup(Level.Trial)
    public void setup() {
        gradesDAO = new GradesDAO();

        counter = 0;
        createdGradeIds = new ArrayList<>();
    }

    @Benchmark
    public void addGrade(Blackhole blackhole) {
        counter++;

        Grades grade = new Grades();
        grade.setEnrollmentId(1);
        grade.setAssessmentId(1);
        grade.setScore(70.0 + (counter % 30));
        grade.setComment("Performance test");

        gradesDAO.save(grade);

        createdGradeIds.add(grade.getId());

        blackhole.consume(grade);
    }

    @TearDown(Level.Iteration)
    public void cleanup() {
        for (Integer id : createdGradeIds) {
            gradesDAO.deleteById(id);
        }

        createdGradeIds.clear();
    }
}