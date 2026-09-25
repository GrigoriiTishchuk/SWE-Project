package com.edujournal.security.session;

import com.edujournal.backend.utils.UserSession;
import com.edujournal.entity.Role;
import com.edujournal.model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionSecurityTest {

    private UserSession userSession;

    @BeforeEach
    void setUp() {
        userSession = UserSession.getInstance();

        userSession.cleanUserSession();
    }

    @AfterEach
    void tearDown() {
        userSession.cleanUserSession();
    }

    @Test
    void newSessionShouldNotContainUser() {
        assertNull(userSession.getCurrentUser());
    }

    @Test
    void newSessionShouldNotBeLoggedIn() {
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void settingUserShouldCreateLoggedInSession() {
        UserDTO user = createUser(Role.STUDENT);

        userSession.setCurrentUser(user);

        assertTrue(userSession.isLoggedIn());
    }

    @Test
    void settingUserShouldStoreCorrectUser() {
        UserDTO user = createUser(Role.TEACHER);

        userSession.setCurrentUser(user);

        assertSame(user, userSession.getCurrentUser());
    }

    @Test
    void sessionShouldReportCorrectRole() {
        UserDTO user = createUser(Role.TEACHER);

        userSession.setCurrentUser(user);

        assertTrue(userSession.hasRole(Role.TEACHER));
    }

    @Test
    void sessionShouldRejectIncorrectRole() {
        UserDTO user = createUser(Role.TEACHER);

        userSession.setCurrentUser(user);

        assertFalse(userSession.hasRole(Role.STUDENT));
        assertFalse(userSession.hasRole(Role.ADMINISTRATOR));
    }

    @Test
    void unauthenticatedSessionShouldNotHaveAnyRole() {
        assertFalse(userSession.hasRole(Role.ADMINISTRATOR));
        assertFalse(userSession.hasRole(Role.TEACHER));
        assertFalse(userSession.hasRole(Role.STUDENT));
    }

    @Test
    void logoutShouldClearCurrentUser() {
        UserDTO user = createUser(Role.STUDENT);

        userSession.setCurrentUser(user);

        userSession.cleanUserSession();

        assertNull(userSession.getCurrentUser());
    }

    @Test
    void logoutShouldSetLoggedInToFalse() {
        UserDTO user = createUser(Role.STUDENT);

        userSession.setCurrentUser(user);

        assertTrue(userSession.isLoggedIn());

        userSession.cleanUserSession();

        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void logoutShouldRemoveRoleAuthorization() {
        UserDTO user = createUser(Role.ADMINISTRATOR);

        userSession.setCurrentUser(user);

        assertTrue(userSession.hasRole(Role.ADMINISTRATOR));

        userSession.cleanUserSession();

        assertFalse(userSession.hasRole(Role.ADMINISTRATOR));
    }

    @Test
    void replacingUserShouldReplacePreviousSessionUser() {
        UserDTO firstUser = createUser(Role.TEACHER);
        UserDTO secondUser = createUser(Role.STUDENT);

        userSession.setCurrentUser(firstUser);
        userSession.setCurrentUser(secondUser);

        assertSame(secondUser, userSession.getCurrentUser());
        assertTrue(userSession.hasRole(Role.STUDENT));
        assertFalse(userSession.hasRole(Role.TEACHER));
    }

    @Test
    void settingNullUserShouldResultInLoggedOutSession() {
        UserDTO user = createUser(Role.TEACHER);

        userSession.setCurrentUser(user);

        userSession.setCurrentUser(null);

        assertNull(userSession.getCurrentUser());
        assertFalse(userSession.isLoggedIn());
        assertFalse(userSession.hasRole(Role.TEACHER));
    }

    @Test
    void singletonShouldReturnSameSessionInstance() {
        UserSession first = UserSession.getInstance();
        UserSession second = UserSession.getInstance();

        assertSame(first, second);
    }

    private UserDTO createUser(Role role) {
        return new UserDTO(
                1,
                "testuser",
                "Test",
                "User",
                "test@example.com",
                "123456789",
                role
        );
    }
}