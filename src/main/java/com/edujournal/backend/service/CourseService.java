package com.edujournal.backend.service;

import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.Course;
import com.edujournal.model.CourseDTO;

import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO =
            new CourseDAO();

    private final CourseMapper courseMapper = new CourseMapper();

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
