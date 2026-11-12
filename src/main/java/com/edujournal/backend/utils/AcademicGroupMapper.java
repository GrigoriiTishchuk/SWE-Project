
package com.edujournal.backend.utils;

import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;

/**
 * Maps AcademicGroup entities to DTOs and DTOs to entities.
 *
 * @author EduJournal
 */
public final class AcademicGroupMapper {

    /**
     * Converts an AcademicGroup entity into an AcademicGroupDTO.
     *
     * @param group the academic group entity
     * @return the corresponding DTO, or null if the entity is null
     */
    public AcademicGroupDTO toDTO(final AcademicGroup group) {
        if (group == null) {
            return null;
        }

        return new AcademicGroupDTO(
                group.getId(),
                group.getName()
        );
    }

    /**
     * Converts an AcademicGroupDTO into an AcademicGroup entity.
     *
     * @param dto the academic group DTO
     * @return the corresponding entity, or null if the DTO is null
     */
    public AcademicGroup toEntity(final AcademicGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        AcademicGroup group = new AcademicGroup();
        group.setId(dto.getId());
        group.setName(dto.getName());

        return group;
    }
}
