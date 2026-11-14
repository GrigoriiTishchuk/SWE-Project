package com.edujournal.backend.utils;

import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;

/**
 * Maps Course entities to DTOs and DTOs to entities. Retrieves additional teacher and academic
 * group information when converting a course to a DTO.
 *
 * @author EduJournal
 */
public final class CourseMapper {

    /** Data access object for retrieving user information. */
    private final UserDAO userDAO;

    /** Data access object for retrieving academic groups. */
    private final AcademicGroupDAO academicGroupDAO;

    /** Creates a CourseMapper with default DAO instances. */
    public CourseMapper() {
        this.userDAO = new UserDAO();
        this.academicGroupDAO = new AcademicGroupDAO();
    }

    /**
     * Creates a CourseMapper with the provided DAO instances.
     *
     * @param userDao the user data access object
     * @param groupDao the academic group data access object
     */
    public CourseMapper(final UserDAO userDao, final AcademicGroupDAO groupDao) {
        this.userDAO = userDao;
        this.academicGroupDAO = groupDao;
    }

    /**
     * Converts a Course entity into a CourseDTO.
     *
     * @param entity the course entity to convert
     * @return the corresponding course DTO
     */
    public CourseDTO toDTO(final Course entity) {

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

        CourseDTO dto =
                new CourseDTO(
                        entity.getId(),
                        entity.getCode(),
                        entity.getName(),
                        entity.getUserId(),
                        teacherName,
                        entity.getAcademicGroupId(),
                        groupName);

        dto.setAcademicYear(entity.getAcademicYear());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        return dto;
    }

    /**
     * Converts a CourseDTO into a Course entity.
     *
     * @param dto the course DTO to convert
     * @return the corresponding course entity
     */
    public Course toEntity(final CourseDTO dto) {
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
