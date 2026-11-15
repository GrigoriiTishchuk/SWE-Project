package com.edujournal.entity;

import com.edujournal.config.LocalizationManager;

/**
 * Defines the available user roles in the system.
 * Each role is associated with a localization key
 * used to display a translated role name.
 */
public enum Role {
    ADMINISTRATOR("roleAdministrator"),
    TEACHER("roleTeacher"),
    STUDENT("roleStudent");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    /**
     * Returns the localized display name of the role.
     *
     * @return localized role name
     */
    public String getDisplayName() {
        return LocalizationManager.get(key);
    }
}
