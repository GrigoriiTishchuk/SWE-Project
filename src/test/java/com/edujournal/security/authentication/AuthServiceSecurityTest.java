package com.edujournal.security.authentication;

import com.edujournal.backend.service.AuthService;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceSecurityTest {

    private UserDAO userDAO;
    private UserMapper userMapper;
    private AuthService authService;
    private UserSession userSession;

    private User testUser;
    private String correctPassword;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userMapper = new UserMapper();
        authService = new AuthService(userDAO, userMapper);
        userSession = UserSession.getInstance();

        userSession.cleanUserSession();

        correctPassword = "SecurePass123";

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("teacher1");
        testUser.setFirstName("Test");
        testUser.setLastName("Teacher");
        testUser.setEmail("teacher@example.com");
        testUser.setPhone("123456789");
        testUser.setRole(Role.TEACHER);
        testUser.setPasswordHash(BCrypt.hashpw(correctPassword, BCrypt.gensalt()));

        when(userDAO.findByUsername("teacher1")).thenReturn(testUser);
    }

    @AfterEach
    void tearDown() {
        userSession.cleanUserSession();
    }

    @Test
    void loginWithValidCredentialsShouldSucceed() {
        boolean result = authService.login("teacher1", correctPassword);

        assertTrue(result);
        assertTrue(userSession.isLoggedIn());
    }

    @Test
    void successfulLoginShouldStoreCorrectUserInSession() {
        authService.login("teacher1", correctPassword);

        UserDTO currentUser = userSession.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(1, currentUser.getId());
        assertEquals("teacher1", currentUser.getUsername());
        assertEquals(Role.TEACHER, currentUser.getRole());
    }

    @Test
    void successfulLoginShouldNotExposePasswordHashInSessionDTO() {
        authService.login("teacher1", correctPassword);

        UserDTO currentUser = userSession.getCurrentUser();

        assertNotNull(currentUser);

        assertFalse(
                java.util.Arrays.stream(currentUser.getClass().getDeclaredFields())
                        .anyMatch(field ->
                                field.getName().toLowerCase().contains("password")
                                        || field.getName().toLowerCase().contains("hash")
                        )
        );
    }

    @Test
    void loginWithWrongPasswordShouldFail() {
        boolean result = authService.login("teacher1", "WrongPassword123");

        assertFalse(result);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginWithUnknownUsernameShouldFail() {
        when(userDAO.findByUsername("unknown")).thenReturn(null);

        boolean result = authService.login("unknown", correctPassword);

        assertFalse(result);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginWithNullUsernameShouldFailWithoutDatabaseLookup() {
        boolean result = authService.login(null, correctPassword);

        assertFalse(result);
        verifyNoInteractions(userDAO);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginWithBlankUsernameShouldFailWithoutDatabaseLookup() {
        boolean result = authService.login("   ", correctPassword);

        assertFalse(result);
        verifyNoInteractions(userDAO);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginWithNullPasswordShouldFailWithoutDatabaseLookup() {
        boolean result = authService.login("teacher1", null);

        assertFalse(result);
        verifyNoInteractions(userDAO);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginWithBlankPasswordShouldFailWithoutDatabaseLookup() {
        boolean result = authService.login("teacher1", "   ");

        assertFalse(result);
        verifyNoInteractions(userDAO);
        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void failedLoginShouldNotReplaceExistingSession() {
        UserDTO existingUser = new UserDTO(
                99,
                "existingUser",
                "Existing",
                "User",
                "existing@example.com",
                "987654321",
                Role.ADMINISTRATOR
        );

        userSession.setCurrentUser(existingUser);

        boolean result = authService.login("teacher1", "WrongPassword123");

        assertFalse(result);
        assertSame(existingUser, userSession.getCurrentUser());
    }

    @Test
    void loginShouldRejectMalformedStoredPasswordHash() {
        testUser.setPasswordHash("not-a-bcrypt-hash");

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("teacher1", correctPassword)
        );

        assertFalse(userSession.isLoggedIn());
    }

    @Test
    void loginShouldUseUsernameLookup() {
        authService.login("teacher1", correctPassword);

        verify(userDAO).findByUsername("teacher1");
    }
}