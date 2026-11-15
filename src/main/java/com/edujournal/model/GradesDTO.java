package com.edujournal.model;

/**
 * Data transfer object for grades.
 */
public class GradesDTO {
    private final Integer enrollmentId;
    private final Integer assessmentId;
    private final Double score;
    private final String comment;

    /**
     * Creates a grade DTO.
     *
     * @param enrollmentId enrollment identifier
     * @param assessmentId assessment identifier
     * @param score achieved score
     * @param comment grade comment
     */
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
