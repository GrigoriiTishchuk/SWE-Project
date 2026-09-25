package com.edujournal.model;

import java.time.LocalDate;

public class CourseDTO {
    private Integer id;
    private String code;
    private String name;
    private Integer userId;
    private String teacherName;
    private Integer groupId;
    private String groupName;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;

    // private Integer studentsCount;

    public CourseDTO(Integer id, String code, String name, Integer userId, String teacherName, Integer groupId, String groupName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.userId = userId;
        this.teacherName = teacherName;
        this.groupId = groupId;
        this.groupName = groupName;
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

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) { this.groupId = groupId;}

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
