package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.backend.utils.InputValidator;

public class AuthService {

    private final UserDAO userDao;

    public AuthService() {
        this.userDao = new UserDAO();
    }

    public AuthService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean login(String username, String password) {
        if (!InputValidator.isValidName(username) || password == null || password.isBlank()) {
            return false;
        }

        // findByUsername returns User (or null - could be null if user not found)
        User user = userDao.findByUsername(username);

        if (user != null) {
            // В сущности User геттер называется getPasswordHash()
            if (checkPassword(password, user.getPasswordHash())) {
                UserSession.getInstance().setCurrentUser(user);
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
            // Use update() method instead of save() to avoid creating a new user and having EntityExistsException or DuplicateKey.
            return true;
        }

        return false;
    }

    public void logout() {
        UserSession.getInstance().cleanUserSession();
    }
}