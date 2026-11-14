package com.edujournal.backend.service;

import com.edujournal.config.LocalizationManager;
import com.edujournal.dao.UserDAO;
import com.edujournal.model.UserDTO;
import com.edujournal.entity.User;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.backend.utils.InputValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;

/**
 * Provides authentication services, including login, password reset, logout, and user locale
 * configuration.
 *
 * @author EduJournal
 */
public final class AuthService {

    /** Data access object for user operations. */
    private final UserDAO userDao;

    /** Maps user entities to data transfer objects. */
    private final UserMapper userMapper;

    /** Creates an authentication service with default dependencies. */
    public AuthService() {
        this.userDao = new UserDAO();
        this.userMapper = new UserMapper();
    }

    /**
     * Creates an authentication service with custom dependencies.
     *
     * @param dao the user data access object
     * @param mapper the user entity mapper
     */
    public AuthService(final UserDAO dao, final UserMapper mapper) {
        this.userDao = dao;
        this.userMapper = mapper;
    }

    /**
     * Authenticates a user using their username and password. Configures the user session and
     * locale after successful login.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return true if authentication succeeds, false otherwise
     */
    public boolean login(final String username, final String password) {

        if (!InputValidator.isValidName(username) || password == null || password.isBlank()) {
            return false;
        }

        User user = userDao.findByUsername(username);

        if (user != null && checkPassword(password, user.getPasswordHash())) {
            UserDTO userDto = userMapper.toDTO(user);
            UserSession.getInstance().setCurrentUser(userDto);
            applyUserLocale(user.getPreferredLocale());
            return true;
        }

        return false;
    }

    /**
     * Checks whether a raw password matches its stored BCrypt hash.
     *
     * @param rawPassword the password provided by the user
     * @param storedHash the stored password hash
     * @return true if the password matches, false otherwise
     */
    private boolean checkPassword(final String rawPassword, final String storedHash) {
        return rawPassword != null && BCrypt.checkpw(rawPassword, storedHash);
    }

    /**
     * Applies the user's preferred locale to the application.
     *
     * @param localeTag the preferred locale identifier
     */
    private void applyUserLocale(final String localeTag) {
        if (localeTag == null || localeTag.isBlank()) {
            return;
        }

        String[] parts = localeTag.split("_");

        Locale locale = parts.length == 2 ? new Locale(parts[0], parts[1]) : new Locale(parts[0]);

        LocalizationManager.setLocale(locale);
    }

    /**
     * Resets the password of an existing user. The new password is stored as a BCrypt hash.
     *
     * @param username the username of the account
     * @param newPassword the new password to set
     * @return true if the password is successfully updated, false otherwise
     */
    public boolean resetPassword(final String username, final String newPassword) {

        if (!InputValidator.isValidName(username) || !InputValidator.isValidPassword(newPassword)) {
            return false;
        }

        User user = userDao.findByUsername(username);

        if (user != null) {
            user.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userDao.update(user);
            return true;
        }

        return false;
    }

    /** Logs out the current user by clearing the active session. */
    public void logout() {
        UserSession.getInstance().cleanUserSession();
    }
}
