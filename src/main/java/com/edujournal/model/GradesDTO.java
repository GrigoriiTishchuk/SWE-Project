package com.edujournal.model;

public class GradesDTO {
    private Integer studentId;
    private Integer assessmentId;
    private Double score;
    private String comment;

    public GradesDTO(Integer studentId, Integer assessmentId, Double score, String comment) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.comment = comment;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public Double getScore() { return score; }

    public String getComment() {
        return comment;
    }
}
