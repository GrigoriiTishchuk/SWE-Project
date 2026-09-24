package com.edujournal.performance;

import com.edujournal.backend.service.CourseService;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.Course;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class CourseAdditionPerformanceTest {

    private CourseService courseService;
    private CourseDAO courseDAO;

    private int counter;
    private List<Integer> createdCourseIds;

    @Setup(Level.Trial)
    public void setup() {
        courseService = new CourseService();
        courseDAO = new CourseDAO();

        counter = 0;
        createdCourseIds = new ArrayList<>();
    }

    @Benchmark
    public void addCourse(Blackhole blackhole) {
        counter++;

        Course course = new Course();
        course.setName("Performance Course " + counter);
        course.setCode("PERF" + counter);
        course.setUserId(null);
        course.setAcademicGroupId(null);

        courseService.save(course);

        createdCourseIds.add(course.getId());

        blackhole.consume(course);
    }

    @TearDown(Level.Iteration)
    public void cleanup() {
        for (Integer id : createdCourseIds) {
            courseDAO.delete(id);
        }

        createdCourseIds.clear();
    }
}