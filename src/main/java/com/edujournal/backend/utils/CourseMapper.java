package com.edujournal.backend.utils;

import com.edujournal.entity.Course;
import com.edujournal.model.CourseDTO;

public class CourseMapper {
    public CourseDTO toDTO(Course entity) {
        CourseDTO dto = new CourseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getUserId()
        );

        // dto.setId(entity.getId());
        // dto.setTeacherName(...);
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
