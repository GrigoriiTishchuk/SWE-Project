package com.edujournal.model;

import com.edujournal.entity.AssessmentType;

/**
 * Data transfer object for student performance reports.
 * Combines student, academic group, course,
 * assessment, and grade information.
 */
public class StudentReportDTO {

    // Student
    private final Integer studentId;
    private final String studentName;
    private final String studentNumber;
    private final Integer academicGroupId;
    private final String academicGroupName;

    // Course
    private final Integer courseId;
    private final String courseCode;
    private final String courseName;

    // Assessment
    private final Integer assessmentId;
    private final String assessmentTitle;
    private final AssessmentType assessmentType;

    // Grade
    private final Double score;

    /**
     * Creates a student report DTO.
     *
     * @param studentId student identifier
     * @param studentName student name
     * @param studentNumber student number
     * @param academicGroupId academic group identifier
     * @param academicGroupName academic group name
     * @param courseId course identifier
     * @param courseCode course code
     * @param courseName course name
     * @param assessmentId assessment identifier
     * @param assessmentTitle assessment title
     * @param assessmentType assessment type
     * @param score student score
     */
    @SuppressWarnings("java:S107") // DTO intentionally contains many report fields
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