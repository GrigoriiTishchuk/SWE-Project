package com.edujournal.performance;

import com.edujournal.backend.service.AuthService;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class LoginPerformanceTest {

    private AuthService authService;

    private final String studentUsername = "performance_student";
    private final String teacherUsername = "performance_teacher";
    private final String adminUsername = "performance_admin";

    private final String password = "Test123!";

    @Setup(Level.Trial)
    public void setup() {
        UserDAO userDAO = new UserDAO();

        createUser(userDAO, studentUsername, Role.STUDENT);
        createUser(userDAO, teacherUsername, Role.TEACHER);
        createUser(userDAO, adminUsername, Role.ADMINISTRATOR);

        authService = new AuthService();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        UserDAO userDAO = new UserDAO();

        deleteUser(userDAO, studentUsername);
        deleteUser(userDAO, teacherUsername);
        deleteUser(userDAO, adminUsername);
    }

    private void deleteUser(UserDAO userDAO, String username) {
        User user = userDAO.findByUsername(username);

        if (user != null) {
            userDAO.delete(user);
        }
    }

    private void createUser(UserDAO userDAO, String username, Role role) {
        User user = new User();

        user.setUsername(username);
        user.setPasswordHash(
                org.mindrot.jbcrypt.BCrypt.hashpw(
                        password,
                        org.mindrot.jbcrypt.BCrypt.gensalt()
                )
        );
        user.setFirstName("Performance");
        user.setLastName("Test");
        user.setRole(role);

        userDAO.save(user);
    }

    @Benchmark
    public void studentLogin() {
        authService.login(studentUsername, password);
    }

    @Benchmark
    public void teacherLogin() {
        authService.login(teacherUsername, password);
    }

    @Benchmark
    public void adminLogin() {
        authService.login(adminUsername, password);
    }
}