
package com.edujournal.backend.service;

import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

import java.util.List;

/**
 * Provides business logic for course management,
 * including course assignments and student enrollments.
 *
 * @author EduJournal
 */
public final class CourseService {

    /** Data access object for course operations. */
    private final CourseDAO courseDAO = new CourseDAO();

    /** Mapper used to convert courses into DTOs. */
    private final CourseMapper courseMapper = new CourseMapper();

    /** Data access object for user operations. */
    private final UserDAO userDAO = new UserDAO();

    /** Data access object for academic group operations. */
    private final AcademicGroupDAO academicGroupDAO =
            new AcademicGroupDAO();

    /** Service responsible for enrollment operations. */
    private final EnrollmentService enrollmentService =
            new EnrollmentService();

    /**
     * Retrieves a course by its identifier.
     *
     * @param id the course identifier
     * @return the course DTO, or null if the course does not exist
     */
    public CourseDTO getById(final Integer id) {
        Course course = courseDAO.findById(id);
        return course != null ? courseMapper.toDTO(course) : null;
    }

    /**
     * Retrieves all courses.
     *
     * @return a list of course DTOs
     */
    public List<CourseDTO> findAll() {
        return courseDAO.findAll()
                .stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    /**
     * Finds a course by its code.
     *
     * @param code the course code
     * @return the course DTO, or null if the course does not exist
     */
    public CourseDTO findByCode(final String code) {
        Course course = courseDAO.findByCode(code);
        return course != null ? courseMapper.toDTO(course) : null;
    }

    /**
     * Finds a course by its name.
     *
     * @param name the course name
     * @return the corresponding course DTO
     */
    public CourseDTO findByName(final String name) {
        Course course = courseDAO.findByName(name);
        return courseMapper.toDTO(course);
    }

    /**
     * Retrieves courses assigned to a specific user.
     *
     * @param userId the user identifier
     * @return a list of course DTOs
     */
    public List<CourseDTO> findByUserId(final Integer userId) {
        List<Course> courses = courseDAO.findByUserId(userId);
        return courses.stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves courses belonging to a specific academic group.
     *
     * @param groupId the academic group identifier
     * @return a list of course DTOs
     */
    public List<CourseDTO> findByGroupId(final Integer groupId) {
        List<Course> courses = courseDAO.findByGroupId(groupId);
        return courses.stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    /**
     * Assigns a teacher to a course after validating both entities.
     *
     * @param courseId the course identifier
     * @param teacherId the teacher's user identifier
     * @throws IllegalArgumentException if the course or user is not found,
     *         or if the user is not a teacher
     */
    public void assignTeacherToCourse(
            final Integer courseId,
            final Integer teacherId) {

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        User teacher = userDAO.findById(teacherId);
        if (teacher == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException(
                    "User is not a teacher");
        }

        course.setUserId(teacherId);
        courseDAO.update(course);
    }

    /**
     * Creates enrollments for students in the specified academic group
     * if they are not already enrolled in the course.
     *
     * @param courseId the course identifier
     * @param groupId the academic group identifier
     * @throws IllegalArgumentException if the course or group is not found
     */
    public void assignStudentsToEnrollments(
            final Integer courseId,
            final Integer groupId) {

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        AcademicGroup group = academicGroupDAO.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        List<Integer> studentIds =
                academicGroupDAO.findStudentIdsByGroupId(groupId);

        for (Integer studentId : studentIds) {
            Enrollment existing =
                    enrollmentService.findByStudentAndCourse(
                            studentId,
                            courseId
                    );

            if (existing == null) {
                Enrollment enrollment = new Enrollment();

                enrollment.setStudentId(studentId);
                enrollment.setCourseId(courseId);
                enrollment.setAcademicGroupId(groupId);
                enrollment.setStatus("ENROLLED");

                enrollmentService.save(enrollment);
            }
        }
    }

    /**
     * Saves a new course and creates enrollments for its academic group
     * when a group is assigned.
     *
     * @param course the course to save
     * @throws IllegalArgumentException if the course code already exists
     */
    public void save(final Course course) {
        if (existsByCode(course.getCode())) {
            throw new IllegalArgumentException(
                    "Course code already exists");
        }

        courseDAO.save(course);

        if (course.getAcademicGroupId() != null) {
            assignStudentsToEnrollments(
                    course.getId(),
                    course.getAcademicGroupId()
            );
        }
    }

    /**
     * Updates an existing course.
     *
     * @param course the course containing updated information
     */
    public void update(final Course course) {
        courseDAO.update(course);
    }

    /**
     * Deletes a course and its associated enrollments.
     *
     * @param id the identifier of the course to delete
     */
    public void delete(final Integer id) {
        enrollmentService.deleteByCourseId(id);
        courseDAO.delete(id);
    }

    /**
     * Checks whether a course with the specified code exists.
     *
     * @param code the course code to check
     * @return true if a course with the code exists, otherwise false
     */
    public boolean existsByCode(final String code) {
        return courseDAO.existsByCode(code);
    }
}
