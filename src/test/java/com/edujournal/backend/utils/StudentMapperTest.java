package com.edujournal.backend.utils;

import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.StudentDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private final StudentMapper mapper = new StudentMapper();

    @Test
    void toDTOReturnsNullWhenUserIsNull() {
        Student student = new Student();
        student.setStudentNumber("TEST001");

        assertNull(mapper.toDTO(null, student));
    }

    @Test
    void toDTOReturnsNullWhenStudentIsNull() {
        User user = new User();
        user.setUsername("test_user");

        assertNull(mapper.toDTO(user, null));
    }

    @Test
    void toDTOMapsFieldsCorrectly() {
        User user = new User();
        user.setUsername("mapper_user");
        user.setFirstName("Map");
        user.setLastName("Per");
        user.setEmail("map@test.fi");
        user.setPhone("0501234567");

        Student student = new Student();
        student.setStudentNumber("MAP001");
        student.setAcademicGroupId(2);

        StudentDTO dto = mapper.toDTO(user, student);

        assertNotNull(dto);
        assertEquals("mapper_user", dto.getUsername());
        assertEquals("Map", dto.getFirstName());
        assertEquals("Per", dto.getLastName());
        assertEquals("map@test.fi", dto.getEmail());
        assertEquals("0501234567", dto.getPhone());
        assertEquals("MAP001", dto.getStudentNumber());
        assertEquals(2, dto.getAcademicGroupId());
    }

    @Test
    void toEntityReturnsNullWhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntityMapsFieldsCorrectly() {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(5);
        dto.setStudentNumber("ENT001");
        dto.setAcademicGroupId(3);

        Student student = mapper.toEntity(dto);

        assertNotNull(student);
        assertEquals(5, student.getId());
        assertEquals("ENT001", student.getStudentNumber());
        assertEquals(3, student.getAcademicGroupId());
    }
}
