package com.edujournal.config;

import org.junit.jupiter.api.Test;

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
    void envConfigConstructor() {
        EnvConfig config = new EnvConfig();
        assertNotNull(config);
    }

    @Test
    void jpaUtilConstructor() {
        JPAUtil util = new JPAUtil();
        assertNotNull(util);
    }
}
