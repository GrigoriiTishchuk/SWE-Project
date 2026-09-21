package com.edujournal.backend.utils;

import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

public class CourseMapper {
    private final UserDAO userDAO = new UserDAO();
    private final AcademicGroupDAO academicGroupDAO = new AcademicGroupDAO();

    public CourseDTO toDTO(Course entity) {

        String teacherName = null;
        String groupName = null;

        if (entity.getUserId() != null) {
            User teacher = userDAO.findById(entity.getUserId());
            if (teacher != null) {
                teacherName = teacher.getFirstName() + " " + teacher.getLastName();
            }
        }

        if (entity.getAcademicGroupId() != null) {
            AcademicGroup group = academicGroupDAO.findById(entity.getAcademicGroupId());
            if (group != null) {
                groupName = group.getName();
            }
        }

        CourseDTO dto = new CourseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getUserId(),
                teacherName,
                entity.getAcademicGroupId(),
                groupName
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
        entity.setAcademicGroupId(dto.getGroupId());
        return entity;
    }
}
