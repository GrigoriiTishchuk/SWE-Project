package com.edujournal.backend.utils;

import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;

public class AcademicGroupMapper {
    public AcademicGroupDTO toDTO(AcademicGroup group) {
        if (group == null) {
            return null;
        }

        return new AcademicGroupDTO(
                group.getId(),
                group.getName()
        );
    }

    public AcademicGroup toEntity(AcademicGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        AcademicGroup group = new AcademicGroup();
        group.setId(dto.getId());
        group.setName(dto.getName());

        return group;
    }
}
