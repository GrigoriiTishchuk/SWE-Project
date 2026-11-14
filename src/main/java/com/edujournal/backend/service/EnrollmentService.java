package com.edujournal.backend.service;

import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.entity.Enrollment;

import java.util.List;

/**
 * Provides business logic for managing student course enrollments.
 *
 * @author EduJournal
 */
public final class EnrollmentService {

    /** Data access object for enrollment operations. */
    private final EnrollmentDAO enrollmentDAO;

    /** Creates an enrollment service with a new DAO instance. */
    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    /**
     * Finds an enrollment by its unique identifier.
     *
     * @param id the enrollment identifier
     * @return the matching enrollment, or null if not found
     */
    public Enrollment findById(final Integer id) {
        return enrollmentDAO.findById(id);
    }

    /**
     * Retrieves all enrollments.
     *
     * @return a list of all enrollments
     */
    public List<Enrollment> findAll() {
        return enrollmentDAO.findAll();
    }

    /**
     * Finds all enrollments belonging to a student.
     *
     * @param studentId the student identifier
     * @return the student's enrollments
     */
    public List<Enrollment> findByStudentId(final Integer studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    /**
     * Finds all enrollments associated with a course.
     *
     * @param courseId the course identifier
     * @return the course's enrollments
     */
    public List<Enrollment> findByCourseId(final Integer courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    /**
     * Finds all enrollments belonging to an academic group.
     *
     * @param academicGroupId the academic group identifier
     * @return the academic group's enrollments
     */
    public List<Enrollment> findByAcademicGroupId(final Integer academicGroupId) {
        return enrollmentDAO.findByAcademicGroupId(academicGroupId);
    }

    /**
     * Finds an enrollment by student and course identifiers.
     *
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return the matching enrollment, or null if not found
     */
    public Enrollment findByStudentAndCourse(final Integer studentId, final Integer courseId) {
        return enrollmentDAO.findByStudentAndCourse(studentId, courseId);
    }

    /**
     * Creates an enrollment if one does not already exist for the specified student and course.
     *
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @param academicGroupId the academic group identifier
     */
    public void createIfNotExists(
            final Integer studentId, final Integer courseId, final Integer academicGroupId) {

        Enrollment existing = findByStudentAndCourse(studentId, courseId);

        if (existing == null) {
            Enrollment enrollment = new Enrollment();

            enrollment.setStudentId(studentId);
            enrollment.setCourseId(courseId);
            enrollment.setAcademicGroupId(academicGroupId);
            enrollment.setStatus("ENROLLED");

            save(enrollment);
        }
    }

    /**
     * Saves a new enrollment.
     *
     * @param enrollment the enrollment to save
     */
    public void save(final Enrollment enrollment) {
        enrollmentDAO.save(enrollment);
    }

    /**
     * Updates an existing enrollment.
     *
     * @param enrollment the enrollment to update
     */
    public void update(final Enrollment enrollment) {
        enrollmentDAO.update(enrollment);
    }

    /**
     * Deletes an enrollment by its unique identifier.
     *
     * @param id the enrollment identifier
     */
    public void delete(final Integer id) {
        enrollmentDAO.delete(id);
    }

    /**
     * Deletes enrollments for a student within an academic group.
     *
     * @param studentId the student identifier
     * @param academicGroupId the academic group identifier
     */
    public void deleteByStudentAndAcademicGroup(
            final Integer studentId, final Integer academicGroupId) {

        enrollmentDAO.deleteByStudentAndAcademicGroup(studentId, academicGroupId);
    }

    /**
     * Deletes all enrollments associated with a course.
     *
     * @param courseId the course identifier
     */
    public void deleteByCourseId(final Integer courseId) {
        enrollmentDAO.deleteByCourseId(courseId);
    }
}
