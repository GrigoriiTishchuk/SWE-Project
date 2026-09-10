package com.edujournal.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        Map<String, Object> properties = new HashMap<>();

        properties.put(
                "jakarta.persistence.jdbc.driver",
                "org.mariadb.jdbc.Driver"
        );

        properties.put(
                "jakarta.persistence.jdbc.url",
                EnvConfig.get("DB_URL")
        );

        properties.put(
                "jakarta.persistence.jdbc.user",
                EnvConfig.get("DB_USERNAME")
        );

        properties.put(
                "jakarta.persistence.jdbc.password",
                EnvConfig.get("DB_PASSWORD")
        );

        ENTITY_MANAGER_FACTORY =
                Persistence.createEntityManagerFactory(
                        "EduJournalPU",
                        properties
                );
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }
}