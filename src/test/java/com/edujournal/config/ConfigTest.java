package com.edujournal.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void envConfigGetReturnsValue() {
        String url = EnvConfig.get("DB_URL");
        assertNotNull(url);
        assertFalse(url.isBlank());
    }

    @Test
    void jpaUtilGetEntityManagerFactoryReturnsNonNull() {
        assertNotNull(JPAUtil.getEntityManagerFactory());
    }

    @Test
    void envConfigConstructor() throws Exception {
        Constructor<EnvConfig> constructor = EnvConfig.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );
    }
}
