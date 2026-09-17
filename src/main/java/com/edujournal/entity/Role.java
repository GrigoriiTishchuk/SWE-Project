package com.edujournal.entity;

public enum Role {
    ADMINISTRATOR("Administrator"),
    TEACHER("Teacher"),
    STUDENT("Student");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
