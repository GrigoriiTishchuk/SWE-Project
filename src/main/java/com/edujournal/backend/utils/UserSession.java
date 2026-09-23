package com.edujournal.backend.utils;

import com.edujournal.entity.Role;
import com.edujournal.model.UserDTO;

// Singleton class to manage the current user session
public class UserSession {

    private static UserSession instance;
    private UserDTO currentUser;

    private UserSession() {
    }

    // thread safe singleton instance retrieval
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
    }


    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(Role role) {
        return isLoggedIn() && currentUser.getRole() == role;
    }

    public void cleanUserSession() {
        this.currentUser = null;
    }
}
