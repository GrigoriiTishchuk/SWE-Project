package com.edujournal.entity;

import com.edujournal.config.LocalizationManager;

public enum Role {
    ADMINISTRATOR("roleAdministrator"),
    TEACHER("roleTeacher"),
    STUDENT("roleStudent");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return LocalizationManager.get(key);
    }
}
