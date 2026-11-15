package com.edujournal.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Represents a course in the system.
 * Stores course information, assigned teacher,
 * academic group, schedule, and notes.
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String name;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "academic_group_id")
    private Integer academicGroupId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Default constructor required by JPA.
     */
    public Course() {
        // Required by JPA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAcademicGroupId() {
        return academicGroupId;
    }

    public void setAcademicGroupId(Integer academicGroupId) {
        this.academicGroupId = academicGroupId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Calculates the academic year based on the course start date.
     * The academic year starts in August and ends in July
     * of the following calendar year.
     *
     * @return academic year in format YYYY/YYYY,
     *         or "2026/2027" if the start date is not set
     */
    public String getAcademicYear() {
        if (startDate == null) return "2026/2027";
        int y = startDate.getMonthValue() >= 8 ? startDate.getYear() : startDate.getYear() - 1;
        return y + "/" + (y + 1);
    }
}
