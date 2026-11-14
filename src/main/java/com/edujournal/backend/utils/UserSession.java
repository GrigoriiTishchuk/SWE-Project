package com.edujournal.backend.utils;

import com.edujournal.entity.Role;
import com.edujournal.model.UserDTO;

/**
 * Manages the currently logged-in user's session using the Singleton design pattern.
 *
 * @author EduJournal
 */
public final class UserSession {

    /** The single instance of the UserSession class. */
    private static UserSession instance;

    /** The currently logged-in user. */
    private UserDTO currentUser;

    /** Creates a private UserSession constructor. */
    private UserSession() {}

    /**
     * Returns the singleton instance of UserSession.
     *
     * @return the single UserSession instance
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }

        return instance;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user the user to store in the session
     */
    public void setCurrentUser(final UserDTO user) {
        this.currentUser = user;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current user, or null if no user is logged in
     */
    public UserDTO getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return true if a user is logged in, otherwise false
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Checks whether the current user has the specified role.
     *
     * @param role the role to check
     * @return true if the current user has the specified role, otherwise false
     */
    public boolean hasRole(final Role role) {
        return isLoggedIn() && currentUser.getRole() == role;
    }

    /** Clears the current user session. */
    public void cleanUserSession() {
        this.currentUser = null;
    }
}
