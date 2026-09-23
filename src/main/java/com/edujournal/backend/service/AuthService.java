package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.model.UserDTO;
import com.edujournal.entity.User;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.backend.utils.InputValidator;

public class AuthService {

    private final UserDAO userDao;
    private final UserMapper userMapper;

    public AuthService() {
        this.userDao = new UserDAO();
        this.userMapper = new UserMapper();
    }

    public AuthService(UserDAO userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    public boolean login(String username, String password) {
        if (!InputValidator.isValidName(username) || password == null || password.isBlank()) {
            return false;
        }

        // findByUsername returns Entity
        User user = userDao.findByUsername(username);

        if (user != null) {
            if (checkPassword(password, user.getPasswordHash())) {
                // Convert Entity in DTO and send it to UserSession
                UserDTO userDto = userMapper.toDTO(user);
                UserSession.getInstance().setCurrentUser(userDto);
                return true;
            }
        }

        return false;
    }

    // check input password against stored password hash (in this case, just a simple comparison)
    private boolean checkPassword(String rawPassword, String storedPassword) {
        return rawPassword != null && rawPassword.equals(storedPassword);
    }

    public boolean resetPassword(String username, String newPassword) {
        if (!InputValidator.isValidName(username) || !InputValidator.isValidPassword(newPassword)) {
            return false;
        }

        User user = userDao.findByUsername(username);

        if (user != null) {
            user.setPasswordHash(newPassword);
            userDao.update(user); // Update existing user in the database with the new password, user becomes Detached.
            return true;
        }

        return false;
    }

    public void logout() {
        UserSession.getInstance().cleanUserSession();
    }
}