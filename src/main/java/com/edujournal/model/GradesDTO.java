package com.edujournal.model;

public class GradesDTO {
    private Integer enrollmentId;
    private Integer assessmentId;
    private Double score;
    private String comment;

    public GradesDTO(Integer enrollmentId, Integer assessmentId, Double score, String comment) {
        this.enrollmentId = enrollmentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.comment = comment;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public Double getScore() { return score; }

    public String getComment() {
        return comment;
    }
}
