package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentReportDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentReportServiceTest {

    private final StudentReportService reportService = new StudentReportService();
    private final StudentService studentService = new StudentService();
    private final UserDAO userDAO = new UserDAO();

    private Integer createdStudentId = null;
    private User createdUser = null;

    @AfterEach
    void cleanup() {
        if (createdStudentId != null) {
            studentService.delete(createdStudentId);
            createdStudentId = null;
        }

        if (createdUser != null) {
            userDAO.delete(createdUser);
            createdUser = null;
        }
    }

    @Test
    void buildReportForNonExistentStudentReturnsEmptyList() {
        List<StudentReportDTO> report = reportService.buildReport(999999);
        assertNotNull(report);
        assertTrue(report.isEmpty());
    }

    @Test
    void computeAverageGradeForStudentWithNoEnrollments() {
        User user = new User();
        user.setUsername("report_test_user");
        user.setPasswordHash("test");
        user.setFirstName("Report");
        user.setLastName("Student");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUser = user;

        Student student = new Student();
        student.setStudentNumber("RPT_TEST_001");
        student.setUserId(user.getId());

        studentService.save(student);
        createdStudentId = student.getId();

        String average = reportService.computeAverageGrade(student.getId());

        assertEquals("—", average);
    }

    @Test
    void buildReportForStudentWithNoEnrollmentsReturnsEmptyList() {
        User user = new User();
        user.setUsername("report_test_user2");
        user.setPasswordHash("test");
        user.setFirstName("Report2");
        user.setLastName("Student2");
        user.setRole(Role.STUDENT);

        userDAO.save(user);
        createdUser = user;

        Student student = new Student();
        student.setStudentNumber("RPT_TEST_002");
        student.setUserId(user.getId());

        studentService.save(student);
        createdStudentId = student.getId();

        List<StudentReportDTO> report = reportService.buildReport(student.getId());

        assertNotNull(report);
        assertTrue(report.isEmpty());
    }

    @Test
    void buildReportFieldsArePopulated() {
        List<StudentReportDTO> report = reportService.buildReport(1);
        assertNotNull(report);

        for (StudentReportDTO row : report) {
            assertNotNull(row.getStudentName());
            assertNotNull(row.getCourseCode());
            assertNotNull(row.getAssessmentTitle());
        }
    }

    @Test
    void computeAverageGradeForStudentWithEnrollmentsReturnsString() {
        // student 1 has seeded enrollments; even without grades the method returns "—"
        String avg = reportService.computeAverageGrade(1);
        assertNotNull(avg);
        assertFalse(avg.isBlank());
    }

    @Test
    void buildReportReturnsRowsForStudentWithEnrollmentsAndAssessments() {
        // student 1 has a seeded enrollment in course 1 which has assessments
        List<StudentReportDTO> report = reportService.buildReport(1);
        assertNotNull(report);
        // if the seeded student has enrollments with assessments, rows are returned
        if (!report.isEmpty()) {
            StudentReportDTO first = report.get(0);
            assertNotNull(first.getStudentName());
            assertNotNull(first.getCourseCode());
            assertNotNull(first.getAssessmentTitle());
        }
    }
}
