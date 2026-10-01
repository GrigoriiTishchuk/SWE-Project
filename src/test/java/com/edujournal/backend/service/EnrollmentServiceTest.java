package com.edujournal.backend.service;

import com.edujournal.entity.Enrollment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceTest {

    private final EnrollmentService enrollmentService =
            new EnrollmentService();

    private final List<Integer> createdIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (Integer id : createdIds) {
            enrollmentService.delete(id);
        }

        createdIds.clear();
    }

    @Test
    void saveAndFindById() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(1);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentService.save(enrollment);

        createdIds.add(enrollment.getId());

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

        createdIds.add(enrollment.getId());

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

        createdIds.add(enrollment.getId());

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

        createdIds.add(enrollment.getId());

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

        createdIds.add(enrollment.getId());

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

        assertNotNull(
                enrollmentService.findById(id)
        );

        enrollmentService.delete(id);

        assertNull(
                enrollmentService.findById(id)
        );
    }

    @Test
    void findByStudentAndCourse() {
        Enrollment found =
                enrollmentService.findByStudentAndCourse(1, 1);

        assertNotNull(found);
        assertEquals(1, found.getStudentId());
        assertEquals(1, found.getCourseId());
    }

    @Test
    void findByStudentAndCourseReturnsNullForNonExistingCombination() {
        Enrollment found =
                enrollmentService.findByStudentAndCourse(
                        999999,
                        999999
                );

        assertNull(found);
    }

    @Test
    void createIfNotExists() {
        enrollmentService.createIfNotExists(
                1,
                2,
                2
        );

        Enrollment found =
                enrollmentService.findByStudentAndCourse(1, 2);

        assertNotNull(found);
        assertEquals(1, found.getStudentId());
        assertEquals(2, found.getCourseId());
        assertEquals(2, found.getAcademicGroupId());
        assertEquals("ENROLLED", found.getStatus());

        createdIds.add(found.getId());
    }

    @Test
    void deleteByStudentAndAcademicGroup() {
        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(2);
        enrollment.setCourseId(1);
        enrollment.setAcademicGroupId(1);
        enrollment.setStatus("ACTIVE");

        enrollmentService.save(enrollment);

        Integer id = enrollment.getId();
        createdIds.add(id);

        assertNotNull(
                enrollmentService.findById(id)
        );

        enrollmentService.deleteByStudentAndAcademicGroup(
                2,
                1
        );

        assertNull(
                enrollmentService.findById(id)
        );
    }

    @Test
    void deleteByCourseId() {
        enrollmentService.deleteByCourseId(999999);

        assertTrue(
                enrollmentService.findByCourseId(999999).isEmpty()
        );
    }
}