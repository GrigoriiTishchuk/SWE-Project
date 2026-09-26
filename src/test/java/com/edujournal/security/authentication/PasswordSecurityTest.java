package com.edujournal.security.authentication;

import com.edujournal.backend.service.AuthService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mindrot.jbcrypt.BCrypt;

class PasswordSecurityTest {

    @Test
    void bcryptHashShouldVerifyWithCorrectPassword() {
        String password = "SecurePassword123";

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertTrue(BCrypt.checkpw(password, hash));
    }

    @Test
    void bcryptHashShouldRejectWrongPassword() {
        String password = "SecurePassword123";

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertFalse(BCrypt.checkpw("WrongPassword123", hash));
    }

    @Test
    void samePasswordShouldProduceDifferentBcryptHashes() {
        String password = "SecurePassword123";

        String hash1 = BCrypt.hashpw(password, BCrypt.gensalt());
        String hash2 = BCrypt.hashpw(password, BCrypt.gensalt());

        assertNotEquals(hash1, hash2);

        assertTrue(BCrypt.checkpw(password, hash1));
        assertTrue(BCrypt.checkpw(password, hash2));
    }

    @Test
    void resetPasswordShouldStoreBCryptHash() {
        UserDAO userDAO = mock(UserDAO.class);
        UserMapper userMapper = new UserMapper();

        AuthService authService = new AuthService(userDAO, userMapper);

        User user = new User();
        user.setId(1);
        user.setUsername("teacher1");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmail("john@example.com");
        user.setRole(Role.TEACHER);

        when(userDAO.findByUsername("teacher1")).thenReturn(user);

        String newPassword = "NewSecurePassword123";

        boolean result = authService.resetPassword(
                "teacher1",
                newPassword
        );

        assertTrue(result);

        verify(userDAO).update(user);

        String storedHash = user.getPasswordHash();

        assertNotNull(storedHash);

        assertTrue(
                isBCryptHash(storedHash),
                "Reset password must be stored using BCrypt"
        );

        assertTrue(
                BCrypt.checkpw(newPassword, storedHash),
                "Stored hash must match the new password"
        );
    }

    @Test
    void resetPasswordShouldNotStorePlaintextPassword() {
        UserDAO userDAO = mock(UserDAO.class);
        UserMapper userMapper = new UserMapper();

        AuthService authService = new AuthService(userDAO, userMapper);

        User user = new User();
        user.setId(1);
        user.setUsername("teacher1");
        user.setRole(Role.TEACHER);

        when(userDAO.findByUsername("teacher1")).thenReturn(user);

        String newPassword = "NewSecurePassword123";

        boolean result = authService.resetPassword(
                "teacher1",
                newPassword
        );

        assertTrue(result);

        assertNotEquals(
                newPassword,
                user.getPasswordHash(),
                "Plaintext password must never be stored"
        );
    }

    private boolean isBCryptHash(String hash) {
        return hash != null
                && hash.matches("^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    }
}