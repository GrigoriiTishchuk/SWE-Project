package com.edujournal.dao;

import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest {

    private final StudentDAO studentDAO = new StudentDAO();
    private final UserDAO userDAO = new UserDAO();

    private final List<Integer> createdStudentIds =
            new ArrayList<>();

    private final List<User> createdUsers =
            new ArrayList<>();

    @AfterEach
    void cleanup() {

        for (Integer studentId : createdStudentIds) {
            studentDAO.delete(studentId);
        }

        for (User user : createdUsers) {
            userDAO.delete(user);
        }

        createdStudentIds.clear();
        createdUsers.clear();
    }

    @Test
    void saveAndFindById() {
        User user = new User();

        user.setUsername("john_test");
        user.setPasswordHash("test");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("TEST001");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );
        student.setUser(user);

        studentDAO.save(student);
        createdStudentIds.add(student.getId());

        assertNotNull(student.getId());

        Student found =
                studentDAO.findById(student.getId());

        assertNotNull(found);
        assertEquals(
                "TEST001",
                found.getStudentNumber()
        );
        assertEquals(
                "John",
                found.getFirstName()
        );
        assertEquals(
                "Doe",
                found.getLastName()
        );
        assertEquals(
                LocalDate.of(2000, 1, 1),
                found.getDateOfBirth()
        );
    }

    @Test
    void findAll() {
        List<Student> students =
                studentDAO.findAll();

        assertNotNull(students);
    }

    @Test
    void findByStudentNumber() {
        User user = new User();

        user.setUsername("jane_test");
        user.setPasswordHash("test");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("TEST002");
        student.setDateOfBirth(
                LocalDate.of(2001, 5, 10)
        );
        student.setUser(user);

        studentDAO.save(student);
        createdStudentIds.add(student.getId());

        Student found =
                studentDAO.findByStudentNumber(
                        "TEST002"
                );

        assertNotNull(found);
        assertNotNull(found.getUser());
        assertEquals(
                "TEST002",
                found.getStudentNumber()
        );
        assertEquals(
                "Jane",
                found.getFirstName()
        );
        assertEquals(
                "Smith",
                found.getLastName()
        );
    }

    @Test
    void update() {
        User user = new User();

        user.setUsername("update_test");
        user.setPasswordHash("test");
        user.setFirstName("OldName");
        user.setLastName("OldLastName");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("TEST003");
        student.setUser(user);

        studentDAO.save(student);
        createdStudentIds.add(student.getId());

        user.setFirstName("NewName");
        user.setLastName("NewLastName");

        userDAO.update(user);

        Student updated =
                studentDAO.findById(student.getId());

        assertNotNull(updated);
        assertEquals(
                "NewName",
                updated.getFirstName()
        );
        assertEquals(
                "NewLastName",
                updated.getLastName()
        );
    }

    @Test
    void delete() {
        User user = new User();

        user.setUsername("delete_test");
        user.setPasswordHash("test");
        user.setFirstName("Delete");
        user.setLastName("Test");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("TEST004");
        student.setUser(user);

        studentDAO.save(student);

        Integer id = student.getId();

        assertNotNull(
                studentDAO.findById(id)
        );

        studentDAO.delete(id);

        assertNull(
                studentDAO.findById(id)
        );
    }
}