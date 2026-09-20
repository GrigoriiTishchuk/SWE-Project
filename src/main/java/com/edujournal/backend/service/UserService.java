package com.edujournal.backend.service;

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

    public UserDTO createTeacher(String firstName, String lastName) {
        String username = generateUsername(firstName, lastName);

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

    public List<UserDTO> findAllTeachers() {

        List<User> teachers = userDAO.findByRole(Role.TEACHER);

        return teachers.stream()
                .map(userMapper::toDTO)
                .toList();
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

    private String generateUsername(String firstName, String lastName) {

        String fn = normalize(firstName);
        String ln = normalize(lastName);

        int maxFn = Math.min(fn.length(), 5);
        int minFn = 1;

        int minLn = 3;
        int maxLn = ln.length();

        // Search for the unique username
        for (int fnLen = maxFn, lnLen = minLn;
             fnLen >= minFn && lnLen <= maxLn;
             fnLen--, lnLen++) {

            String base = fn.substring(0, fnLen) + ln.substring(0, lnLen);

            String username = base;
            int counter = 1;

            while (userDAO.findByUsername(username) != null) {
                username = base + counter;
                counter++;
            }

            return username;
        }

        // fallback
        return fn + ln;
    }

    private String normalize(String s) {
        return s
                .toLowerCase()
                .replace("ä", "a")
                .replace("ö", "o")
                .replace("å", "a")
                .replaceAll("\\s+", "");
    }

    public void update(User user) {
        userDAO.update(user);
    }
}
