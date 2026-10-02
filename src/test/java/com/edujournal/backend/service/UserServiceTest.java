package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService();
    private final UserDAO userDAO = new UserDAO();

    private final List<Integer> createdUserIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdUserIds) {
            try {
                User user = userDAO.findById(id);
                if (user != null) {
                    userDAO.delete(user);
                }
            } catch (Exception ignored) {
            }
        }

        createdUserIds.clear();
    }

    @Test
    void findAll() {
        List<User> users = userService.findAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void findById() {
        UserDTO dto = userService.createTeacher("FindById", "User", "0501234567");
        createdUserIds.add(dto.getId());

        User found = userService.findById(dto.getId());

        assertNotNull(found);
        assertEquals(dto.getId(), found.getId());
    }

    @Test
    void createTeacher() {
        UserDTO dto = userService.createTeacher("Test", "Teacher", "0501234567");
        createdUserIds.add(dto.getId());

        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertEquals("Test", dto.getFirstName());
        assertEquals("Teacher", dto.getLastName());

        User saved = userDAO.findById(dto.getId());
        assertNotNull(saved);
        assertEquals(Role.TEACHER, saved.getRole());
    }

    @Test
    void createStudentUser() {
        User user = userService.createStudentUser("Test", "Student", "0509876543");
        createdUserIds.add(user.getId());

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(Role.STUDENT, user.getRole());
        assertEquals("Test", user.getFirstName());
        assertEquals("Student", user.getLastName());
    }

    @Test
    void deleteUser() {
        UserDTO dto = userService.createTeacher("Delete", "Teacher", "0501111111");
        Integer id = dto.getId();
        createdUserIds.add(id);

        assertNotNull(userDAO.findById(id));

        userService.deleteUser(id);
        createdUserIds.remove(id);

        assertNull(userDAO.findById(id));
    }

    @Test
    void deleteUserThrowsForAdministrator() {
        User admin = userDAO.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMINISTRATOR)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No administrator found in the database"));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(admin.getId())
        );
    }

    @Test
    void deleteUserThrowsForNonExistentUser() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(999999)
        );
    }

    @Test
    void findAllTeachersReturnsOnlyTeachers() {
        UserDTO dto = userService.createTeacher("Only", "Teacher", "0502222222");
        createdUserIds.add(dto.getId());

        List<UserDTO> teachers = userService.findAllTeachers();

        assertNotNull(teachers);
        assertTrue(teachers.stream().anyMatch(t -> t.getId().equals(dto.getId())));
    }

    @Test
    void findAllStudentsReturnsOnlyStudents() {
        User student = userService.createStudentUser("Only", "Student", "0503333333");
        createdUserIds.add(student.getId());

        List<UserDTO> students = userService.findAllStudents();

        assertNotNull(students);
        assertTrue(students.stream().anyMatch(s -> s.getId().equals(student.getId())));
    }

    @Test
    void generateUniqueUsernameIsUnique() {
        String username = userService.generateUniqueUsername("Unique", "User");

        assertNotNull(username);
        assertFalse(username.isBlank());
        assertNull(userDAO.findByUsername(username));
    }

    @Test
    void updateChangesFields() {
        UserDTO dto = userService.createTeacher("Original", "Name", "0504444444");
        createdUserIds.add(dto.getId());

        User toUpdate = userDAO.findById(dto.getId());
        toUpdate.setFirstName("Updated");
        toUpdate.setLastName("Name");
        toUpdate.setPhone("0509999999");

        userService.update(toUpdate);

        User updated = userDAO.findById(dto.getId());
        assertEquals("Updated", updated.getFirstName());
        assertEquals("0509999999", updated.getPhone());
    }
}
