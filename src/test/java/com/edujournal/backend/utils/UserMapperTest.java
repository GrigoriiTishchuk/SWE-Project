package com.edujournal.backend.utils;

import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    UserMapper umapper = new UserMapper();

    @Test
    public void toDTOTest() {
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.TEACHER);

        UserMapper mapper = new UserMapper();
        UserDTO dto = mapper.toDTO(user);

        assertEquals(1, dto.getId());
        assertEquals("john", dto.getUsername());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(Role.TEACHER, dto.getRole());
    }

    @Test
    public void toEntityTest() {
        UserDTO dto = new UserDTO(
                1,
                "john",
                "John",
                "Doe",
                "john@edujournal.fi",
                "0501112233",
                Role.TEACHER
        );

        UserMapper mapper = new UserMapper();
        User user = mapper.toEntity(dto);

        assertEquals(1, user.getId());
        assertEquals("john", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@edujournal.fi", user.getEmail());
        assertEquals("0501112233", user.getPhone());
        assertEquals(Role.TEACHER, user.getRole());
    }

    @Test
    public void toDTONullTest() {
        UserMapper mapper = new UserMapper();
        assertNull(mapper.toDTO(null));
    }

    @Test
    public void toEntityNullTest() {
        UserMapper mapper = new UserMapper();
        assertNull(mapper.toEntity(null));
    }
}