package com.edujournal.backend.utils;

import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

public class CourseMapper {
    private final UserDAO userDAO;
    private final AcademicGroupDAO academicGroupDAO ;

    public CourseMapper() {
        this.userDAO = new UserDAO();
        this.academicGroupDAO = new AcademicGroupDAO();
    }

    public CourseMapper(UserDAO userDAO, AcademicGroupDAO academicGroupDAO) {
        this.userDAO = userDAO;
        this.academicGroupDAO = academicGroupDAO;
    }

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
        dto.setAcademicYear(entity.getAcademicYear());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        return dto;
    }


    public Course toEntity(CourseDTO dto) {
        Course entity = new Course();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setUserId(dto.getUserId());
        entity.setAcademicGroupId(dto.getGroupId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }
}
