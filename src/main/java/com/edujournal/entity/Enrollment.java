package com.edujournal.entity;

import jakarta.persistence.*;

/**
 * Represents a student's enrollment in a course.
 * Stores references to the student, course,
 * academic group, and enrollment status.
 */
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "academic_group_id")
    private Integer academicGroupId;

    @Column(name = "status")
    private String status;

    /**
     * Default constructor required by JPA.
     */
    public Enrollment() {
        // Required by JPA
    }

    public Integer getId() {
        return id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getAcademicGroupId() {
        return academicGroupId;
    }

    public void setAcademicGroupId(Integer academicGroupId) {
        this.academicGroupId = academicGroupId;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
