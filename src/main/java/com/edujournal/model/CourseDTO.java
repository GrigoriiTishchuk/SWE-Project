package com.edujournal.model;

public class CourseDTO {
    private Integer id;
    private String code;
    private String name;
    private Integer userId;

    // private String teacherName; // later
    // private Integer studentsCount; // later

    public CourseDTO(Integer id, String code, String name, Integer userId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.userId = userId;
    }

    public CourseDTO() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id;}

    public String getCode() {
        return code;
    }
    public void setCode(String code) { this.code = code;}

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name;}

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) { this.userId = userId;}
}
