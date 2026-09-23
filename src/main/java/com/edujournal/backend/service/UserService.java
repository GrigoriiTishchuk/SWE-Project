package com.edujournal.backend.service;

import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;

import java.util.List;
import java.util.UUID;

public class UserService {
    UserDAO userDAO = new UserDAO();
    UserMapper userMapper = new UserMapper();

    public User findById(int id) {
        return userDAO.findById(id);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public void update(User user) {
        User existing = userDAO.findById(user.getId());
        if (existing == null) return;

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setUsername(user.getUsername());

        userDAO.update(existing);
    }

    public void deleteUser(Integer userId) {
        User user = userDAO.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new IllegalArgumentException("Administrator's deleting is not available");
        }

        userDAO.delete(user);
    }

    public UserDTO createTeacher(String firstName, String lastName) {
        String username = generateUniqueUsername(firstName, lastName);

        // Generate temporary password
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // Password hashing
        String hashedPassword = Integer.toHexString(tempPassword.hashCode());

        // Create entity
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(Role.TEACHER);
        user.setPasswordHash(hashedPassword);

        userDAO.save(user);

        return userMapper.toDTO(user);
    }

    public User createStudentUser(String firstName, String lastName, String phone) {
        String username = generateUniqueUsername(firstName, lastName);
        String email = GeneratorUtil.generateEmail(username);
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = Integer.toHexString(tempPassword.hashCode());

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

    public List<UserDTO> findAllTeachers() {

        List<User> teachers = userDAO.findByRole(Role.TEACHER);

        return teachers.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public String generateUniqueUsername(String firstName, String lastName) {

        List<String> candidates = GeneratorUtil.generateUsernameCandidates(firstName, lastName);

        for (String candidate : candidates) {
            if (userDAO.findByUsername(candidate) == null) {
                return candidate;
            }
        }

        String base = candidates.isEmpty()
                ? (GeneratorUtil.normalize(firstName) + GeneratorUtil.normalize(lastName))
                : candidates.get(0).substring(0, 7);

        int counter = 1;
        String username;

        do {
            username = base + counter;
            counter++;
        } while (userDAO.findByUsername(username) != null);

        return username;
    }
}
