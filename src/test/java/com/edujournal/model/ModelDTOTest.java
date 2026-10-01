package com.edujournal.model;

import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ModelDTOTest {

    // --- StudentDTO ---

    @Test
    void studentDTO_noArgsConstructor() {
        StudentDTO dto = new StudentDTO();
        assertNull(dto.getStudentId());
        assertNull(dto.getFirstName());
    }

    @Test
    void studentDTO_allArgsConstructor() {
        StudentDTO dto = new StudentDTO(1, 2, "jdoe", "John", "Doe", "123", "j@test.com", "S001", 3);
        assertEquals(1, dto.getStudentId());
        assertEquals(2, dto.getUserId());
        assertEquals("jdoe", dto.getUsername());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123", dto.getPhone());
        assertEquals("j@test.com", dto.getEmail());
        assertEquals("S001", dto.getStudentNumber());
        assertEquals(3, dto.getAcademicGroupId());
    }

    @Test
    void studentDTO_setters() {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(5);
        dto.setUserId(6);
        dto.setUsername("user");
        dto.setFirstName("Anna");
        dto.setLastName("Smith");
        dto.setPhone("456");
        dto.setEmail("a@test.com");
        dto.setStudentNumber("S002");
        dto.setAcademicGroupId(7);

        assertEquals(5, dto.getStudentId());
        assertEquals(6, dto.getUserId());
        assertEquals("user", dto.getUsername());
        assertEquals("Anna", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("456", dto.getPhone());
        assertEquals("a@test.com", dto.getEmail());
        assertEquals("S002", dto.getStudentNumber());
        assertEquals(7, dto.getAcademicGroupId());
    }

    // --- UserDTO ---

    @Test
    void userDTO_noArgsConstructor() {
        UserDTO dto = new UserDTO();
        assertNull(dto.getId());
        assertNull(dto.getRole());
    }

    @Test
    void userDTO_allArgsConstructor() {
        UserDTO dto = new UserDTO(1, "admin", "Alice", "Brown", "a@x.com", "789", Role.ADMINISTRATOR);
        assertEquals(1, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Brown", dto.getLastName());
        assertEquals("a@x.com", dto.getEmail());
        assertEquals("789", dto.getPhone());
        assertEquals(Role.ADMINISTRATOR, dto.getRole());
    }

    @Test
    void userDTO_setters() {
        UserDTO dto = new UserDTO();
        dto.setId(2);
        dto.setUsername("teacher1");
        dto.setFirstName("Bob");
        dto.setLastName("Jones");
        dto.setEmail("b@x.com");
        dto.setPhone("000");
        dto.setRole(Role.TEACHER);

        assertEquals(2, dto.getId());
        assertEquals("teacher1", dto.getUsername());
        assertEquals("Bob", dto.getFirstName());
        assertEquals("Jones", dto.getLastName());
        assertEquals("b@x.com", dto.getEmail());
        assertEquals("000", dto.getPhone());
        assertEquals(Role.TEACHER, dto.getRole());
    }

    @Test
    void userDTO_toString() {
        UserDTO dto = new UserDTO(1, "u", "Alice", "Brown", "a@x.com", "789", Role.TEACHER);
        assertEquals("Alice Brown", dto.toString());
    }

    // --- CourseDTO ---

    @Test
    void courseDTO_noArgsConstructor() {
        CourseDTO dto = new CourseDTO();
        assertNull(dto.getId());
        assertNull(dto.getCode());
    }

    @Test
    void courseDTO_allArgsConstructor() {
        CourseDTO dto = new CourseDTO(1, "CS101", "Math", 2, "Prof A", 3, "Group A");
        assertEquals(1, dto.getId());
        assertEquals("CS101", dto.getCode());
        assertEquals("Math", dto.getName());
        assertEquals(2, dto.getUserId());
        assertEquals("Prof A", dto.getTeacherName());
        assertEquals(3, dto.getGroupId());
        assertEquals("Group A", dto.getGroupName());
    }

    @Test
    void courseDTO_setters() {
        CourseDTO dto = new CourseDTO();
        LocalDate start = LocalDate.of(2024, 9, 1);
        LocalDate end = LocalDate.of(2025, 5, 31);
        dto.setId(10);
        dto.setCode("PHY200");
        dto.setName("Physics");
        dto.setUserId(5);
        dto.setTeacherName("Prof B");
        dto.setGroupId(4);
        dto.setGroupName("Group B");
        dto.setAcademicYear("2024/2025");
        dto.setStartDate(start);
        dto.setEndDate(end);

        assertEquals(10, dto.getId());
        assertEquals("PHY200", dto.getCode());
        assertEquals("Physics", dto.getName());
        assertEquals(5, dto.getUserId());
        assertEquals("Prof B", dto.getTeacherName());
        assertEquals(4, dto.getGroupId());
        assertEquals("Group B", dto.getGroupName());
        assertEquals("2024/2025", dto.getAcademicYear());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
    }

    // --- AssessmentsDTO ---

    @Test
    void assessmentsDTO_allArgsConstructor() {
        LocalDate due = LocalDate.of(2024, 12, 1);
        AssessmentsDTO dto = new AssessmentsDTO(1, 2, "Midterm", AssessmentType.EXAM1, 100.0, 0.3, due);

        assertEquals(1, dto.getId());
        assertEquals(2, dto.getCourseId());
        assertEquals("Midterm", dto.getTitle());
        assertEquals(AssessmentType.EXAM1, dto.getType());
        assertEquals(100.0, dto.getMaxScore());
        assertEquals(0.3, dto.getWeight());
        assertEquals(due, dto.getDueDate());
    }

    // --- GradesDTO ---

    @Test
    void gradesDTO_allArgsConstructor() {
        GradesDTO dto = new GradesDTO(1, 2, 85.5, "Good work");

        assertEquals(1, dto.getEnrollmentId());
        assertEquals(2, dto.getAssessmentId());
        assertEquals(85.5, dto.getScore());
        assertEquals("Good work", dto.getComment());
    }

    // --- AcademicGroupDTO ---

    @Test
    void academicGroupDTO_noArgsConstructor() {
        AcademicGroupDTO dto = new AcademicGroupDTO();
        assertNull(dto.getId());
        assertNull(dto.getName());
    }

    @Test
    void academicGroupDTO_constructor() {
        AcademicGroupDTO dto = new AcademicGroupDTO(1, "Group A");
        assertEquals(1, dto.getId());
        assertEquals("Group A", dto.getName());
    }

    @Test
    void academicGroupDTO_setters() {
        AcademicGroupDTO dto = new AcademicGroupDTO();
        dto.setId(3);
        dto.setName("Group B");
        assertEquals(3, dto.getId());
        assertEquals("Group B", dto.getName());
    }

    // --- StudentReportDTO ---

    @Test
    void studentReportDTO_allArgsConstructor() {
        StudentReportDTO dto = new StudentReportDTO(
                1, "John Doe", "S001", 2, "Group A",
                3, "CS101", "Math", 4, "Midterm", AssessmentType.EXAM1, 90.0
        );

        assertEquals(1, dto.getStudentId());
        assertEquals("John Doe", dto.getStudentName());
        assertEquals("S001", dto.getStudentNumber());
        assertEquals(2, dto.getAcademicGroupId());
        assertEquals("Group A", dto.getAcademicGroupName());
        assertEquals(3, dto.getCourseId());
        assertEquals("CS101", dto.getCourseCode());
        assertEquals("Math", dto.getCourseName());
        assertEquals(4, dto.getAssessmentId());
        assertEquals("Midterm", dto.getAssessmentTitle());
        assertEquals(AssessmentType.EXAM1, dto.getAssessmentType());
        assertEquals(90.0, dto.getScore());
    }

    // --- GradeDistributionDTO ---

    @Test
    void gradeDistributionDTO_noArgsConstructor() {
        GradeDistributionDTO dto = new GradeDistributionDTO();
        assertEquals(0.0, dto.getExcellent());
        assertEquals(0.0, dto.getFail());
    }

    @Test
    void gradeDistributionDTO_allArgsConstructor() {
        GradeDistributionDTO dto = new GradeDistributionDTO(10, 20, 30, 15, 5, 2);
        assertEquals(10, dto.getExcellent());
        assertEquals(20, dto.getVeryGood());
        assertEquals(30, dto.getGood());
        assertEquals(15, dto.getSatisfactory());
        assertEquals(5, dto.getSufficient());
        assertEquals(2, dto.getFail());
    }

    @Test
    void gradeDistributionDTO_setters() {
        GradeDistributionDTO dto = new GradeDistributionDTO();
        dto.setExcellent(5);
        dto.setVeryGood(10);
        dto.setGood(15);
        dto.setSatisfactory(8);
        dto.setSufficient(3);
        dto.setFail(1);

        assertEquals(5, dto.getExcellent());
        assertEquals(10, dto.getVeryGood());
        assertEquals(15, dto.getGood());
        assertEquals(8, dto.getSatisfactory());
        assertEquals(3, dto.getSufficient());
        assertEquals(1, dto.getFail());
    }
}
