package com.edujournal.model;

public class CourseDTO {
    private String code;
    private String name;
    private Integer userId;

    // private String teacherName; // later
    // private Integer studentsCount; // later

    public CourseDTO(String code, String name, Integer userId) {
        this.code = code;
        this.name = name;
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getUserId() {
        return userId;
    }
}
