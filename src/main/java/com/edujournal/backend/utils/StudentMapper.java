
package com.edujournal.backend.utils;

import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentDTO;

/**
 * Maps User and Student entities to StudentDTO objects
 * and converts StudentDTO objects into Student entities.
 *
 * @author EduJournal
 */
public final class StudentMapper {

    /**
     * Converts a User and Student entity into a StudentDTO.
     *
     * @param user the user entity
     * @param student the student entity
     * @return the corresponding StudentDTO, or null if either
     * entity is null
     */
    public StudentDTO toDTO(
            final User user,
            final Student student) {

        if (user == null || student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();

        dto.setStudentId(student.getId());
        dto.setUserId(user.getId());

        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        dto.setStudentNumber(student.getStudentNumber());
        dto.setAcademicGroupId(student.getAcademicGroupId());

        return dto;
    }

    /**
     * Converts a StudentDTO into a Student entity.
     *
     * @param dto the student data transfer object
     * @return the corresponding Student entity, or null if
     * the DTO is null
     */
    public Student toEntity(final StudentDTO dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();

        student.setId(dto.getStudentId());
        student.setStudentNumber(dto.getStudentNumber());
        student.setAcademicGroupId(dto.getAcademicGroupId());

        return student;
    }
}
