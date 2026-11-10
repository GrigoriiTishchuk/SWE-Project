
package com.edujournal.backend.service;

import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides business logic for managing users, including
 * creating teachers and students and retrieving user records.
 *
 * @author EduJournal
 */
public final class UserService {

    /** Logger used for recording user service events. */
    private static final Logger LOGGER =
            Logger.getLogger(UserService.class.getName());

    /** Length of generated temporary passwords. */
    private static final int TEMP_PASSWORD_LENGTH = 8;

    /** Length of the username candidate prefix. */
    private static final int USERNAME_PREFIX_LENGTH = 7;

    /** Data access object for user operations. */
    private final UserDAO userDAO = new UserDAO();

    /** Mapper used to convert users into DTOs. */
    private final UserMapper userMapper = new UserMapper();

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return the user, or null if no user is found
     */
    public User findById(final int id) {
        return userDAO.findById(id);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        return userDAO.findAll();
    }

    /**
     * Saves a new user.
     *
     * @param user the user to save
     */
    public void save(final User user) {
        userDAO.save(user);
    }

    /**
     * Updates the editable details of an existing user.
     *
     * @param user the user containing the updated information
     */
    public void update(final User user) {
        User existing = userDAO.findById(user.getId());

        if (existing == null) {
            return;
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());

        userDAO.update(existing);
    }

    /**
     * Deletes a user unless the user has the administrator role.
     *
     * @param userId the ID of the user to delete
     * @throws IllegalArgumentException if the user does not exist
     *         or is an administrator
     */
    public void deleteUser(final Integer userId) {
        User user = userDAO.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new IllegalArgumentException(
                    "Administrator's deleting is not available");
        }

        userDAO.delete(user);
    }

    /**
     * Creates a teacher account with generated credentials.
     *
     * @param firstName the teacher's first name
     * @param lastName the teacher's last name
     * @param phone the teacher's phone number
     * @return the created teacher as a DTO
     */
    public UserDTO createTeacher(
            final String firstName,
            final String lastName,
            final String phone) {

        String username = generateUniqueUsername(
                firstName, lastName, "teacher");

        String email = GeneratorUtil.generateEmail(username);

        String tempPassword = UUID.randomUUID()
                .toString()
                .substring(0, TEMP_PASSWORD_LENGTH);

        LOGGER.log(
                Level.INFO,
                "Temporary credentials generated for teacher: {0}",
                username);

        String hashedPassword = BCrypt.hashpw(
                tempPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(Role.TEACHER);
        user.setPasswordHash(hashedPassword);

        userDAO.save(user);

        return userMapper.toDTO(user);
    }

    /**
     * Creates a student account with generated credentials.
     *
     * @param firstName the student's first name
     * @param lastName the student's last name
     * @param phone the student's phone number
     * @return the created student
     */
    public User createStudentUser(
            final String firstName,
            final String lastName,
            final String phone) {

        String username = generateUniqueUsername(
                firstName, lastName, "student");

        String email = GeneratorUtil.generateEmail(username);

        String tempPassword = UUID.randomUUID()
                .toString()
                .substring(0, TEMP_PASSWORD_LENGTH);

        LOGGER.log(
                Level.INFO,
                "Temporary credentials generated for student: {0}",
                username);

        String hashedPassword = BCrypt.hashpw(
                tempPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(Role.STUDENT);
        user.setPasswordHash(hashedPassword);

        userDAO.save(user);
        return user;
    }

    /**
     * Retrieves all users with the teacher role as DTOs.
     *
     * @return a list of teacher DTOs
     */
    public List<UserDTO> findAllTeachers() {
        List<User> teachers = userDAO.findByRole(Role.TEACHER);

        return teachers.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves all users with the student role as DTOs.
     *
     * @return a list of student DTOs
     */
    public List<UserDTO> findAllStudents() {
        List<User> students = userDAO.findByRole(Role.STUDENT);

        return students.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    /**
     * Generates a unique username based on the user's name.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param fallbackPrefix the prefix used when no name is available
     * @return a username that is not currently in use
     */
    public String generateUniqueUsername(
            final String firstName,
            final String lastName,
            final String fallbackPrefix) {

        List<String> candidates =
                GeneratorUtil.generateUsernameCandidates(
                        firstName, lastName);

        for (String candidate : candidates) {
            if (userDAO.findByUsername(candidate) == null) {
                return candidate;
            }
        }

        String combined = GeneratorUtil.normalize(firstName)
                + GeneratorUtil.normalize(lastName);

        String base;

        if (candidates.isEmpty()) {
            if (combined.isEmpty()) {
                base = fallbackPrefix;
            } else {
                base = combined;
            }
        } else {
            base = candidates.get(0)
                    .substring(0, USERNAME_PREFIX_LENGTH);
        }

        int counter = 1;
        String username;

        do {
            username = base + counter;
            counter++;
        } while (userDAO.findByUsername(username) != null);

        return username;
    }
}
