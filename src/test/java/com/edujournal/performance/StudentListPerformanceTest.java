package com.edujournal.performance;

import com.edujournal.dao.StudentDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.backend.service.StudentService;
import com.edujournal.model.StudentDTO;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class StudentListPerformanceTest {

    private StudentService studentService;

    private final String usernamePrefix = "performance_student_";

    @Setup(Level.Trial)
    public void setup() {
        UserDAO userDAO = new UserDAO();
        StudentDAO studentDAO = new StudentDAO();

        for (int i = 1; i <= 10; i++) {
            User user = new User();

            user.setUsername(usernamePrefix + i);
            user.setPasswordHash("performance-test");
            user.setFirstName("Performance");
            user.setLastName("Student");
            user.setEmail(usernamePrefix + i + "@test.com");
            user.setPhone("0000000000");
            user.setRole(Role.STUDENT);

            userDAO.save(user);

            Student student = new Student();
            student.setUserId(user.getId());
            student.setStudentNumber("PERF" + String.format("%04d", i));

            studentDAO.save(student);
        }

        studentService = new StudentService();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        StudentDAO studentDAO = new StudentDAO();
        UserDAO userDAO = new UserDAO();

        for (int i = 1; i <= 10; i++) {
            Student student =
                    studentDAO.findByStudentNumber(
                            "PERF" + String.format("%04d", i)
                    );

            if (student != null) {
                studentDAO.delete(student.getId());

                User user = userDAO.findById(student.getUserId());

                if (user != null) {
                    userDAO.delete(user);
                }
            }
        }
    }

    @Benchmark
    public void studentList(Blackhole blackhole) {
        List<StudentDTO> students = studentService.findAllDTO();

        blackhole.consume(students);
    }
}