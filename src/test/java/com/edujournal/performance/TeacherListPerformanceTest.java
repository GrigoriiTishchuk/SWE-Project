package com.edujournal.performance;

import com.edujournal.backend.service.UserService;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class TeacherListPerformanceTest {

    private UserService userService;

    private final String teacherPrefix = "performance_teacher_";
    private final String passwordHash = "performance-test";

    @Setup(Level.Trial)
    public void setup() {
        UserDAO userDAO = new UserDAO();

        for (int i = 1; i <= 10; i++) {
            createTeacher(userDAO, teacherPrefix + i);
        }

        userService = new UserService();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        UserDAO userDAO = new UserDAO();

        for (int i = 1; i <= 10; i++) {
            String username = teacherPrefix + i;

            User teacher = userDAO.findByUsername(username);

            if (teacher != null) {
                userDAO.delete(teacher);
            }
        }
    }

    private void createTeacher(UserDAO userDAO, String username) {
        User teacher = new User();

        teacher.setUsername(username);
        teacher.setPasswordHash(passwordHash);
        teacher.setFirstName("Performance");
        teacher.setLastName("Teacher");
        teacher.setEmail(username + "@test.com");
        teacher.setPhone("0000000000");
        teacher.setRole(Role.TEACHER);

        userDAO.save(teacher);
    }

    @Benchmark
    public void teacherList(Blackhole blackhole) {
        List<UserDTO> teachers = userService.findAllTeachers();

        blackhole.consume(teachers);
    }
}