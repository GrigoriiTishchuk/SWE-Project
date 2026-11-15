package com.edujournal.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Represents an assessment within a course.
 * Stores assessment details such as title, type,
 * maximum score, weight, and due date.
 */
@Entity
@Table(name = "assessments")
public class Assessments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;

    private String title;

    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    @Column(name = "max_score")
    private Double maxScore;

    private Double weight;

    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Default constructor required by JPA.
     */
    public Assessments() {
        // Required by JPA
    }

    public Integer getId() {
        return id;
    }

    public Integer getCourseId() {
        return courseId;
    }
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public AssessmentType getType() {
        return type;
    }
    public void setType(AssessmentType type) {
        this.type = type;
    }

    public Double getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
