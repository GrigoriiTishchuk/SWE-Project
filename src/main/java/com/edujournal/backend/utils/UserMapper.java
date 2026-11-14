package com.edujournal.backend.utils;

import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;

/**
 * Maps User entities to UserDTO objects and converts UserDTO objects into User entities.
 *
 * @author EduJournal
 */
public final class UserMapper {

    /**
     * Converts a User entity into a UserDTO.
     *
     * @param user the user entity to convert
     * @return the corresponding UserDTO, or null if the entity is null
     */
    public UserDTO toDTO(final User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole());
    }

    /**
     * Converts a UserDTO into a User entity.
     *
     * @param dto the user data transfer object
     * @return the corresponding User entity, or null if the DTO is null
     */
    public User toEntity(final UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        return user;
    }
}
