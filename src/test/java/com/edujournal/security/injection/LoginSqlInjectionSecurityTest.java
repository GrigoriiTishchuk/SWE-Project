package com.edujournal.security.injection;

import com.edujournal.dao.UserDAO;
import com.edujournal.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginSqlInjectionSecurityTest {

    @Test
    void sqlInjectionUsernameShouldNotReturnAnotherUser() {

        UserDAO userDAO = new UserDAO();

        String maliciousUsername = "' OR '1'='1";

        User result = userDAO.findByUsername(maliciousUsername);

        assertNull(result);
    }

    @Test
    void commentBasedSqlInjectionShouldNotReturnAnotherUser() {

        UserDAO userDAO = new UserDAO();

        String maliciousUsername = "admin' --";

        User result = userDAO.findByUsername(maliciousUsername);

        assertNull(result);
    }

    @Test
    void unionBasedSqlInjectionShouldNotReturnAnotherUser() {

        UserDAO userDAO = new UserDAO();

        String maliciousUsername =
                "' UNION SELECT 1, 'admin', 'password' --";

        User result = userDAO.findByUsername(maliciousUsername);

        assertNull(result);
    }
}