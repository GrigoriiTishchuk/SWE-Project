package com.edujournal.model;

public class Grades {
    private int id;
    private int studentId;
    private int assessmentId;
    private double score;
    private String note;

    public Grades(int id, int studentId, int assessmentId, double score, String note) {
        this.id = id;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public double getScore() {
        return score;
    }

    public String getNote() {
        return note;
    }
}
