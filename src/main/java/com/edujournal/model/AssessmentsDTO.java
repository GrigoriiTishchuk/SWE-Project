package com.edujournal.model;

import com.edujournal.entity.AssessmentType;

import java.time.LocalDate;

public class AssessmentsDTO {
    private Integer courseId;
    private String title;
    private AssessmentType type;
    private Double maxScore;
    private Double weight;
    private LocalDate dueDate;

    public AssessmentsDTO(Integer courseId, String title, AssessmentType type, Double maxScore, Double weight, LocalDate dueDate) {
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.maxScore = maxScore;
        this.weight = weight;
        this.dueDate = dueDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public AssessmentType getType() {
        return type;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public Double getWeight() {
        return weight;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

}

