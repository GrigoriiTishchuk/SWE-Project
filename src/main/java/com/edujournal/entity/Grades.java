package com.edujournal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "assessment_id")
    private Integer assessmentId;

    private Double score;

    private String note;

    public Grades() {
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

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
