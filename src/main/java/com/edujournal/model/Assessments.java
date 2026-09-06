package com.edujournal.model;

import java.time.LocalDate;

public class Assessments {
    private int id;
    private int courseId;
    private String title;
    private AssessmentType type;
    private double maxScore;
    private double weight;
    private LocalDate dueDate;

    public Assessments(int id, int courseId, String title, AssessmentType type, double maxScore, double weight, LocalDate dueDate) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.maxScore = maxScore;
        this.weight = weight;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public AssessmentType getType() {
        return type;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public double getWeight() {
        return weight;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
