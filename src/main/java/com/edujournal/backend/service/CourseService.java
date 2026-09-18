package com.edujournal.backend.service;

import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();
    private final CourseMapper courseMapper = new CourseMapper();
    private final UserDAO userDAO = new UserDAO();

    public CourseDTO getById(Integer id) {
        Course course = courseDAO.findById(id);
        return courseMapper.toDTO(course);
    }

    public List<CourseDTO> findAll() {
        return courseDAO.findAll().stream().map(courseMapper::toDTO).toList();
    }

    public CourseDTO findByCode(String code) {
        Course course = courseDAO.findByCode(code);
        return courseMapper.toDTO(course);
    }

    public CourseDTO findByName(String name) {
        Course course = courseDAO.findByName(name);
        return courseMapper.toDTO(course);
    }

    public List<CourseDTO> findByUserId(Integer userId) {
        List<Course> courses = courseDAO.findByUserId(userId);
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

    public void save(Course course) {
        courseDAO.save(course);
    }

    public void update(Course course) {
        courseDAO.update(course);
    }

    public void delete(Integer id) {
        courseDAO.delete(id);
    }
}
