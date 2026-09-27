package com.edujournal.model;

import com.edujournal.entity.AssessmentType;

public class StudentReportDTO {

    // Student
    private Integer studentId;
    private String studentName;
    private String studentNumber;
    private Integer academicGroupId;
    private String academicGroupName;

    // Course
    private Integer courseId;
    private String courseCode;
    private String courseName;

    // Assessment
    private Integer assessmentId;
    private String assessmentTitle;
    private AssessmentType assessmentType;

    // Grade
    private Double score;

    public StudentReportDTO(
            Integer studentId,
            String studentName,
            String studentNumber,
            Integer academicGroupId,
            String academicGroupName,
            Integer courseId,
            String courseCode,
            String courseName,
            Integer assessmentId,
            String assessmentTitle,
            AssessmentType assessmentType,
            Double score
    ) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.academicGroupId = academicGroupId;
        this.academicGroupName = academicGroupName;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.score = score;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public Integer getAcademicGroupId() {
        return academicGroupId;
    }

    public String getAcademicGroupName() {
        return academicGroupName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public Double getScore() {
        return score;
    }
}