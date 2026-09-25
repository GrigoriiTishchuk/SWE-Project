package com.edujournal.backend.service;

import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.StudentDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.*;
import com.edujournal.model.CourseDTO;

import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();
    private final CourseMapper courseMapper = new CourseMapper();
    private final UserDAO userDAO = new UserDAO();
    private final AcademicGroupDAO academicGroupDAO = new AcademicGroupDAO();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    public CourseDTO getById(Integer id) {
        Course course = courseDAO.findById(id);
        return course != null ? courseMapper.toDTO(course) : null;
    }

    public List<CourseDTO> findAll() {
        return courseDAO.findAll().stream().map(courseMapper::toDTO).toList();
    }

    public CourseDTO findByCode(String code) {
        Course course = courseDAO.findByCode(code);
        return course != null ? courseMapper.toDTO(course) : null;
    }

    public CourseDTO findByName(String name) {
        Course course = courseDAO.findByName(name);
        return courseMapper.toDTO(course);
    }

    public List<CourseDTO> findByUserId(Integer userId) {
        List<Course> courses = courseDAO.findByUserId(userId);
        return courses.stream().map(courseMapper::toDTO).toList();
    }

    public List<CourseDTO> findByGroupId(Integer groupId) {
        List<Course> courses = courseDAO.findByGroupId(groupId);
        return courses.stream().map(courseMapper::toDTO).toList();
    }

    public void assignTeacherToCourse(Integer courseId, Integer teacherId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        User teacher = userDAO.findById(teacherId);
        if (teacher == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        course.setUserId(teacherId);
        courseDAO.update(course);
    }

    public void assignStudentsToEnrollments(Integer courseId, Integer groupId) {
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

    public void save(Course course) {
        if (existsByCode(course.getCode())) {
            throw new IllegalArgumentException("Course code already exists");
        }
        courseDAO.save(course);
        if (course.getAcademicGroupId() != null) {assignStudentsToEnrollments(course.getId(), course.getAcademicGroupId());
        }
    }

    public void update(Course course) {
        courseDAO.update(course);
    }

    public void delete(Integer id) {
        enrollmentService.deleteByCourseId(id);
        courseDAO.delete(id);
    }

    public boolean existsByCode(String code) {
        return courseDAO.existsByCode(code);
    }

}
