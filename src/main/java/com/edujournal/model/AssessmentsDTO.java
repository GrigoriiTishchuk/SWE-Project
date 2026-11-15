package com.edujournal.model;

import com.edujournal.entity.AssessmentType;

import java.time.LocalDate;

/**
 * Data transfer object for assessments.
 */
public class AssessmentsDTO {

    private final Integer id;
    private final Integer courseId;
    private final String title;
    private final AssessmentType type;
    private final Double maxScore;
    private final Double weight;
    private final LocalDate dueDate;

    /**
     * Creates an assessment DTO.
     *
     * @param id assessment identifier
     * @param courseId course identifier
     * @param title assessment title
     * @param type assessment type
     * @param maxScore maximum score
     * @param weight assessment weight
     * @param dueDate assessment due date
     */
    public AssessmentsDTO(
            Integer id,
            Integer courseId,
            String title,
            AssessmentType type,
            Double maxScore,
            Double weight,
            LocalDate dueDate) {

        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.maxScore = maxScore;
        this.weight = weight;
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
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