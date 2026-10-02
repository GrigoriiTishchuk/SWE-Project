package com.edujournal.backend.service;

import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = new AuthService();
    private final UserDAO userDAO = new UserDAO();

    private User createdUser = null;

    @AfterEach
    void cleanup() {
        authService.logout();

        if (createdUser != null) {
            userDAO.delete(createdUser);
            createdUser = null;
        }
    }

    @Test
    void loginWithValidCredentials() {
        assertTrue(authService.login("admin", "demo_password"));
        assertNotNull(UserSession.getInstance().getCurrentUser());
    }

    @Test
    void loginWithWrongPassword() {
        assertFalse(authService.login("admin", "wrong_password"));
    }

    @Test
    void loginWithNullUsername() {
        assertFalse(authService.login(null, "demo_password"));
    }

    @Test
    void loginWithBlankPassword() {
        assertFalse(authService.login("admin", ""));
    }

    @Test
    void loginWithNonExistentUser() {
        assertFalse(authService.login("no_such_user_xyz_999", "demo_password"));
    }

    @Test
    void logoutClearsSession() {
        authService.login("admin", "demo_password");
        assertNotNull(UserSession.getInstance().getCurrentUser());

        authService.logout();

        assertNull(UserSession.getInstance().getCurrentUser());
    }

    @Test
    void resetPasswordWithShortPassword() {
        assertFalse(authService.resetPassword("admin", "short"));
    }

    @Test
    void resetPasswordWithNullPassword() {
        assertFalse(authService.resetPassword("admin", null));
    }

    @Test
    void resetPasswordForNonExistentUser() {
        assertFalse(authService.resetPassword("no_such_user_xyz_999", "newpassword123"));
    }

    @Test
    void resetPasswordThenLoginWithNewPassword() {
        User user = new User();
        user.setUsername("auth_test_reset_user");
        user.setPasswordHash(BCrypt.hashpw("oldpassword1", BCrypt.gensalt()));
        user.setFirstName("Reset");
        user.setLastName("Test");
        user.setRole(Role.TEACHER);

        userDAO.save(user);
        createdUser = user;

        assertTrue(authService.resetPassword("auth_test_reset_user", "newpassword123"));
        assertTrue(authService.login("auth_test_reset_user", "newpassword123"));
    }
}
