package com.edujournal.performance;

import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class StudentDataPerformanceTest {

    private EnrollmentService enrollmentService;
    private GradesDAO gradesDAO;

    @Setup(Level.Trial)
    public void setup() {
        enrollmentService = new EnrollmentService();
        gradesDAO = new GradesDAO();
    }

    @Benchmark
    public void loadStudentData(Blackhole blackhole) {

        List<Enrollment> enrollments =
                enrollmentService.findByStudentId(1);

        blackhole.consume(enrollments);

        for (Enrollment enrollment : enrollments) {

            List<Grades> grades =
                    gradesDAO.findByEnrollment(enrollment.getId());

            blackhole.consume(grades);
        }
    }
}