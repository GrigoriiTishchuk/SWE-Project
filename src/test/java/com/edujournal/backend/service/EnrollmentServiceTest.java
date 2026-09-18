package com.edujournal.backend.service;

import com.edujournal.entity.Enrollment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceTest {

    private final EnrollmentService enrollmentService =
            new EnrollmentService();

    @Test
    void saveAndFindById() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentService.save(enrollment);

        assertNotNull(enrollment.getId());

        Enrollment found =
                enrollmentService.findById(enrollment.getId());

        assertNotNull(found);
        assertEquals(1, found.getStudentId());
        assertEquals(1, found.getCourseId());
        assertEquals(1, found.getAcademicGroupId());
        assertEquals("ACTIVE", found.getStatus());
    }

    @Test
    void findAll() {
        List<Enrollment> enrollments =
                enrollmentService.findAll();

        assertNotNull(enrollments);
    }

    @Test
    void findByStudentId() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentService.save(enrollment);

        List<Enrollment> results =
                enrollmentService.findByStudentId(1);

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

        enrollmentService.save(enrollment);

        List<Enrollment> results =
                enrollmentService.findByCourseId(1);

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

        enrollmentService.save(enrollment);

        List<Enrollment> results =
                enrollmentService.findByAcademicGroupId(1);

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

        enrollmentService.save(enrollment);

        enrollment.setStatus("INACTIVE");

        enrollmentService.update(enrollment);

        Enrollment updated =
                enrollmentService.findById(enrollment.getId());

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

        enrollmentService.save(enrollment);

        Integer id = enrollment.getId();

        assertNotNull(enrollmentService.findById(id));

        enrollmentService.delete(id);

        assertNull(enrollmentService.findById(id));
    }
}