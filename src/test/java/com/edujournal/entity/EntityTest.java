package com.edujournal.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void assessmentsGettersAndSetters() {
        Assessments a = new Assessments();
        a.setCourseId(1);
        a.setTitle("Final Exam");
        a.setType(AssessmentType.EXAM1);
        a.setMaxScore(100.0);
        a.setWeight(50.0);
        a.setDueDate(LocalDate.of(2026, 12, 31));

        assertNull(a.getId());
        assertEquals(1, a.getCourseId());
        assertEquals("Final Exam", a.getTitle());
        assertEquals(AssessmentType.EXAM1, a.getType());
        assertEquals(100.0, a.getMaxScore());
        assertEquals(50.0, a.getWeight());
        assertEquals(LocalDate.of(2026, 12, 31), a.getDueDate());
    }

    @Test
    void userGettersAndSetters() {
        User u = new User();
        u.setId(42);
        u.setUsername("john_doe");
        u.setPasswordHash("hashed_pw");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setRole(Role.TEACHER);
        u.setPhone("+358 40 123 4567");
        u.setEmail("john@example.com");
        u.setPhoto("avatar.png");

        assertEquals(42, u.getId());
        assertEquals("john_doe", u.getUsername());
        assertEquals("hashed_pw", u.getPasswordHash());
        assertEquals("John", u.getFirstName());
        assertEquals("Doe", u.getLastName());
        assertEquals(Role.TEACHER, u.getRole());
        assertEquals("+358 40 123 4567", u.getPhone());
        assertEquals("john@example.com", u.getEmail());
        assertEquals("avatar.png", u.getPhoto());
    }

    @Test
    void courseGetAcademicYearNullStartDate() {
        Course c = new Course();
        assertEquals("2026/2027", c.getAcademicYear());
    }

    @Test
    void courseGetAcademicYearMonthOnOrAfterAugust() {
        Course c = new Course();
        c.setStartDate(LocalDate.of(2024, 9, 1));
        assertEquals("2024/2025", c.getAcademicYear());
    }

    @Test
    void courseGetAcademicYearMonthBeforeAugust() {
        Course c = new Course();
        c.setStartDate(LocalDate.of(2025, 3, 15));
        assertEquals("2024/2025", c.getAcademicYear());
    }

    @Test
    void studentGettersAndSetters() {
        Student s = new Student();
        s.setId(5);
        s.setStudentNumber("S12345");
        s.setDateOfBirth(LocalDate.of(2000, 6, 15));
        s.setUserId(10);
        s.setAcademicGroupId(3);

        assertEquals(5, s.getId());
        assertEquals("S12345", s.getStudentNumber());
        assertEquals(LocalDate.of(2000, 6, 15), s.getDateOfBirth());
        assertEquals(10, s.getUserId());
        assertEquals(3, s.getAcademicGroupId());
    }

    @Test
    void gradesGettersAndSetters() {
        Grades g = new Grades();
        g.setEnrollmentId(1);
        g.setAssessmentId(2);
        g.setScore(85.0);
        g.setComment("Well done");

        assertNull(g.getId());
        assertEquals(1, g.getEnrollmentId());
        assertEquals(2, g.getAssessmentId());
        assertEquals(85.0, g.getScore());
        assertEquals("Well done", g.getComment());
    }

    @Test
    void roleDisplayNames() {
        assertEquals("Administrator", Role.ADMINISTRATOR.getDisplayName());
        assertEquals("Teacher", Role.TEACHER.getDisplayName());
        assertEquals("Student", Role.STUDENT.getDisplayName());
    }

    @Test
    void academicGroupGettersAndSetters() {
        AcademicGroup ag = new AcademicGroup();
        ag.setId(7);
        ag.setName("Group Alpha");

        assertEquals(7, ag.getId());
        assertEquals("Group Alpha", ag.getName());
    }
}
