package com.edujournal.backend.service;

import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private final StudentService studentService =
            new StudentService();

    private final UserDAO userDAO =
            new UserDAO();

    private final EnrollmentDAO enrollmentDAO =
            new EnrollmentDAO();

    private final GradesDAO gradesDAO =
            new GradesDAO();

    private final List<Integer> createdIds =
            new ArrayList<>();

    private final List<User> createdUsers =
            new ArrayList<>();

    private final List<Integer> createdEnrollmentIds =
            new ArrayList<>();

    private final List<Integer> createdGradeIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {

        for (Integer gradeId : createdGradeIds) {
            gradesDAO.deleteById(gradeId);
        }

        for (Integer enrollmentId : createdEnrollmentIds) {
            enrollmentDAO.delete(enrollmentId);
        }

        for (Integer id : createdIds) {
            studentService.delete(id);
        }

        for (User user : createdUsers) {
            userDAO.delete(user);
        }

        createdGradeIds.clear();
        createdEnrollmentIds.clear();
        createdIds.clear();
        createdUsers.clear();
    }

    @Test
    void saveAndFindById() {
        Student student = new Student();

        student.setStudentNumber("SERVICE001");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        createdIds.add(student.getId());

        assertNotNull(student.getId());

        Student found =
                studentService.findById(student.getId());

        assertNotNull(found);

        assertEquals(
                "SERVICE001",
                found.getStudentNumber()
        );

        assertEquals(
                LocalDate.of(2000, 1, 1),
                found.getDateOfBirth()
        );
    }

    @Test
    void findByUserId() {
        User user = new User();

        user.setUsername("service_user_test");
        user.setPasswordHash("test");
        user.setFirstName("Service");
        user.setLastName("User");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE005");
        student.setUserId(user.getId());

        studentService.save(student);
        createdIds.add(student.getId());

        Student found =
                studentService.findByUserId(user.getId());

        assertNotNull(found);

        assertEquals(
                student.getId(),
                found.getId()
        );

        assertEquals(
                user.getId(),
                found.getUserId()
        );

        assertEquals(
                "SERVICE005",
                found.getStudentNumber()
        );
    }

    @Test
    void findAll() {
        List<Student> students =
                studentService.findAll();

        assertNotNull(students);
    }

    @Test
    void findByStudentNumber() {
        Student student = new Student();

        student.setStudentNumber("SERVICE002");

        studentService.save(student);

        createdIds.add(student.getId());

        Student found =
                studentService.findByStudentNumber(
                        "SERVICE002"
                );

        assertNotNull(found);

        assertEquals(
                "SERVICE002",
                found.getStudentNumber()
        );
    }

    @Test
    void update() {
        Student student = new Student();

        student.setStudentNumber("SERVICE003");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        createdIds.add(student.getId());

        student.setStudentNumber("SERVICE003_UPDATED");
        student.setDateOfBirth(
                LocalDate.of(2001, 2, 2)
        );

        studentService.update(student);

        Student updated =
                studentService.findById(
                        student.getId()
                );

        assertNotNull(updated);

        assertEquals(
                "SERVICE003_UPDATED",
                updated.getStudentNumber()
        );

        assertEquals(
                LocalDate.of(2001, 2, 2),
                updated.getDateOfBirth()
        );
    }

    @Test
    void delete() {
        Student student = new Student();

        student.setStudentNumber("SERVICE004");
        student.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );

        studentService.save(student);

        Integer id = student.getId();

        assertNotNull(
                studentService.findById(id)
        );

        studentService.delete(id);

        assertNull(
                studentService.findById(id)
        );
    }

    @Test
    void deleteThrowsWhenGradesExist() {
        User user = new User();

        user.setUsername("grades_delete_test");
        user.setPasswordHash("test");
        user.setFirstName("Grades");
        user.setLastName("Delete");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE006");
        student.setUserId(user.getId());
        student.setAcademicGroupId(1);

        studentService.save(student);
        createdIds.add(student.getId());

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");

        enrollmentDAO.save(enrollment);
        createdEnrollmentIds.add(enrollment.getId());

        Grades grade = new Grades();

        grade.setEnrollmentId(enrollment.getId());
        grade.setAssessmentId(1);
        grade.setScore(85.0);
        grade.setComment("Test grade");

        gradesDAO.save(grade);
        createdGradeIds.add(grade.getId());

        assertThrows(
                IllegalStateException.class,
                () -> studentService.delete(student.getId())
        );
    }

    @Test
    void deleteThrowsWhenEnrolledInCourse() {
        User user = new User();

        user.setUsername("course_delete_test");
        user.setPasswordHash("test");
        user.setFirstName("Course");
        user.setLastName("Delete");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE007");
        student.setUserId(user.getId());
        student.setAcademicGroupId(1);

        studentService.save(student);
        createdIds.add(student.getId());

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(student.getId());
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ENROLLED");

        enrollmentDAO.save(enrollment);
        createdEnrollmentIds.add(enrollment.getId());

        assertThrows(
                IllegalStateException.class,
                () -> studentService.delete(student.getId())
        );
    }

    @Test
    void findDTOById() {
        User user = new User();

        user.setUsername("dto_test");
        user.setPasswordHash("test");
        user.setFirstName("DTO");
        user.setLastName("Student");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE008");
        student.setUserId(user.getId());
        student.setDateOfBirth(
                LocalDate.of(2000, 5, 10)
        );

        studentService.save(student);
        createdIds.add(student.getId());

        var dto =
                studentService.findDTOById(student.getId());

        assertNotNull(dto);

        assertEquals(
                student.getId(),
                dto.getStudentId()
        );

        assertEquals(
                user.getId(),
                dto.getUserId()
        );

        assertEquals(
                "SERVICE008",
                dto.getStudentNumber()
        );

        assertEquals(
                "dto_test",
                dto.getUsername()
        );

        assertEquals(
                "DTO",
                dto.getFirstName()
        );

        assertEquals(
                "Student",
                dto.getLastName()
        );
    }

    @Test
    void findDTOByIdReturnsNullWhenStudentDoesNotExist() {
        StudentDTO dto =
                studentService.findDTOById(999999);

        assertNull(dto);
    }

    @Test
    void findAllDTO() {
        User user = new User();

        user.setUsername("all_dto_test");
        user.setPasswordHash("test");
        user.setFirstName("All");
        user.setLastName("DTO");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE009");
        student.setUserId(user.getId());

        studentService.save(student);
        createdIds.add(student.getId());

        List<StudentDTO> dtos =
                studentService.findAllDTO();

        assertNotNull(dtos);

        StudentDTO dto = dtos.stream()
                .filter(d -> student.getId().equals(d.getStudentId()))
                .findFirst()
                .orElse(null);

        assertNotNull(dto);

        assertEquals(
                student.getId(),
                dto.getStudentId()
        );

        assertEquals(
                user.getId(),
                dto.getUserId()
        );

        assertEquals(
                "SERVICE009",
                dto.getStudentNumber()
        );

        assertEquals(
                "all_dto_test",
                dto.getUsername()
        );
    }

    @Test
    void createStudent() {
        StudentDTO dto =
                studentService.createStudent(
                        "Created",
                        "Student",
                        "0501234567"
                );

        assertNotNull(dto);

        createdIds.add(dto.getStudentId());
        createdUsers.add(userDAO.findById(dto.getUserId()));

        assertNotNull(dto.getStudentId());
        assertNotNull(dto.getUserId());
        assertNotNull(dto.getStudentNumber());

        assertEquals("Created", dto.getFirstName());
        assertEquals("Student", dto.getLastName());

        Student student = studentService.findById(dto.getStudentId());
        assertNotNull(student);

        User user = userDAO.findById(dto.getUserId());
        assertNotNull(user);
    }

    @Test
    void updateFromDTO() {
        User user = new User();

        user.setUsername("update_dto_test");
        user.setPasswordHash("test");
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUsers.add(user);

        Student student = new Student();

        student.setStudentNumber("SERVICE010");
        student.setUserId(user.getId());
        student.setAcademicGroupId(1);

        studentService.save(student);
        createdIds.add(student.getId());

        StudentDTO dto = new StudentDTO();

        dto.setStudentId(student.getId());
        dto.setUserId(user.getId());
        dto.setStudentNumber("SERVICE010_UPDATED");
        dto.setAcademicGroupId(2);
        dto.setUsername("updated_username");
        dto.setFirstName("Updated");
        dto.setLastName("Student");

        studentService.updateFromDTO(dto);

        Student updatedStudent =
                studentService.findById(student.getId());

        assertNotNull(updatedStudent);

        assertEquals(
                "SERVICE010_UPDATED",
                updatedStudent.getStudentNumber()
        );

        assertEquals(
                2,
                updatedStudent.getAcademicGroupId()
        );

        User updatedUser =
                userDAO.findById(user.getId());

        assertNotNull(updatedUser);

        assertEquals(
                "updated_username",
                updatedUser.getUsername()
        );

        assertEquals(
                "Updated",
                updatedUser.getFirstName()
        );

        assertEquals(
                "Student",
                updatedUser.getLastName()
        );
    }

    @Test
    void updateFromDTODoesNothingWhenStudentDoesNotExist() {
        StudentDTO dto = new StudentDTO();

        dto.setStudentId(999999);
        dto.setUserId(999999);
        dto.setStudentNumber("DOES_NOT_EXIST");
        dto.setAcademicGroupId(1);
        dto.setUsername("not_exist");
        dto.setFirstName("Not");
        dto.setLastName("Exist");

        assertDoesNotThrow(
                () -> studentService.updateFromDTO(dto)
        );
    }

    @Test
    void generateUniqueStudentNumber() {
        String studentNumber =
                studentService.generateUniqueStudentNumber();

        assertNotNull(studentNumber);
        assertFalse(studentNumber.isBlank());

        assertNull(
                studentService.findByStudentNumber(
                        studentNumber
                )
        );
    }
}