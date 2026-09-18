package com.edujournal.dao;

import com.edujournal.entity.Enrollment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentDAOTest {

    private final EnrollmentDAO enrollmentDAO =
            new EnrollmentDAO();

    @Test
    void saveAndFindById() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        assertNotNull(enrollment.getId());

        Enrollment found =
                enrollmentDAO.findById(enrollment.getId());

        assertNotNull(found);
        assertEquals(1, found.getStudentId());
        assertEquals(1, found.getCourseId());
        assertEquals(1, found.getAcademicGroupId());
        assertEquals("ACTIVE", found.getStatus());
    }

    @Test
    void findAll() {
        List<Enrollment> enrollments =
                enrollmentDAO.findAll();

        assertNotNull(enrollments);
    }

    @Test
    void findByStudentId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        List<Enrollment> results =
                enrollmentDAO.findByStudentId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void findByCourseId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(2);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        List<Enrollment> results =
                enrollmentDAO.findByCourseId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void findByAcademicGroupId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(3);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        List<Enrollment> results =
                enrollmentDAO.findByAcademicGroupId(1);

        assertFalse(results.isEmpty());

        assertTrue(
                results.stream()
                        .anyMatch(e ->
                                e.getId().equals(enrollment.getId())
                        )
        );
    }

    @Test
    void update() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(4);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        enrollment.setStatus("INACTIVE");

        enrollmentDAO.update(enrollment);

        Enrollment updated =
                enrollmentDAO.findById(enrollment.getId());

        assertNotNull(updated);
        assertEquals("INACTIVE", updated.getStatus());
    }

    @Test
    void delete() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(4);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentDAO.save(enrollment);

        Integer id = enrollment.getId();

        assertNotNull(enrollmentDAO.findById(id));

        enrollmentDAO.delete(id);

        assertNull(enrollmentDAO.findById(id));
    }
}
