package com.edujournal.backend.service;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;
import com.edujournal.backend.utils.UserSession;

public class AuthService {

    private final UserDAO userDao;

    public AuthService() {
        this.userDao = new UserDAO();
    }

    public AuthService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null
                || username.isBlank() || password.isBlank()) {
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

    public void logout() {
        UserSession.getInstance().cleanUserSession();
    }
}