package com.edujournal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "enrollment_id")
    private Integer enrollmentId;

    @Column(name = "assessment_id")
    private Integer assessmentId;

    private Double score;

    private String comment;

    public Grades() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }
    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
