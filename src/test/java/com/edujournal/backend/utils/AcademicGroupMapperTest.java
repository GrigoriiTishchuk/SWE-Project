package com.edujournal.backend.utils;

import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcademicGroupMapperTest {
    private final AcademicGroupMapper mapper = new AcademicGroupMapper();

    @Test
    void toDTOTest() {
        AcademicGroup group = new AcademicGroup();
        group.setId(3);
        group.setName("Group A");

        AcademicGroupDTO dto = mapper.toDTO(group);

        assertEquals(3, dto.getId());
        assertEquals("Group A", dto.getName());
    }

    @Test
    void toEntityTest() {
        AcademicGroupDTO dto = new AcademicGroupDTO(3, "Group A");

        AcademicGroup group = mapper.toEntity(dto);

        assertEquals(3, group.getId());
        assertEquals("Group A", group.getName());
    }

    @Test
    void toDTONullTest() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void toEntityNullTest() {
        assertNull(mapper.toEntity(null));
    }
}