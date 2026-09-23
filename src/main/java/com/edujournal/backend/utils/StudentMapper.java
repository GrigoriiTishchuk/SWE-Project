package com.edujournal.backend.utils;

import com.edujournal.entity.User;
import com.edujournal.entity.Student;
import com.edujournal.model.StudentDTO;

public class StudentMapper {

    public StudentDTO toDTO(User user, Student student) {
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

    public Student toEntity(StudentDTO dto) {
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
