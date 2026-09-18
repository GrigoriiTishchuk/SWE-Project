package com.edujournal.backend.utils;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Course;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

public class CourseMapper {
    private final UserDAO userDAO = new UserDAO();

    public CourseDTO toDTO(Course entity) {

        String teacherName = null;

        if (entity.getUserId() != null) {
            User teacher = userDAO.findById(entity.getUserId());
            if (teacher != null) {
                teacherName = teacher.getFirstName() + " " + teacher.getLastName();
            }
        }

        CourseDTO dto = new CourseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getUserId(),
                teacherName
        );

        // dto.setStudentsCount(...);

        return dto;
    }


    public Course toEntity(CourseDTO dto) {
        Course entity = new Course();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
