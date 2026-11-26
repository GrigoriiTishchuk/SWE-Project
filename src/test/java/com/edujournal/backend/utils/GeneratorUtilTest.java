package com.edujournal.backend.utils;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorUtilTest {

    @Test
    void normalizeShouldReturnEmptyStringWhenNull() {
        assertEquals("", GeneratorUtil.normalize(null));
    }

    @Test
    void normalizeShouldReplaceSpecialCharacters() {
        String result = GeneratorUtil.normalize("ÄÖÅ-Test_123");

        assertEquals("aoatest123", result);
    }

    @Test
    void generateTempPasswordShouldReturnEightCharacters() {
        String password = GeneratorUtil.generateTempPassword();

        assertNotNull(password);
        assertEquals(8, password.length());
    }

    @Test
    void hashPasswordShouldGenerateValidHash() {
        String password = "TestPassword123";

        String hash = GeneratorUtil.hashPassword(password);

        assertNotNull(hash);
        assertNotEquals(password, hash);
        assertTrue(BCrypt.checkpw(password, hash));
    }

    @Test
    void constructorShouldThrowException() throws Exception {
        Constructor<GeneratorUtil> constructor =
                GeneratorUtil.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        InvocationTargetException exception =
                assertThrows(
                        InvocationTargetException.class,
                        constructor::newInstance
                );

        assertInstanceOf(
                IllegalStateException.class,
                exception.getCause()
        );

        assertEquals(
                "Utility class cannot be instantiated.",
                exception.getCause().getMessage()
        );
    }
}