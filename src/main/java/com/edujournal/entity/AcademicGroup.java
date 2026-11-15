package com.edujournal.entity;

import jakarta.persistence.*;

/**
 * Represents an academic group in the system.
 */
@Entity
@Table(name = "academic_groups")
public class AcademicGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /**
     * Default constructor required by JPA.
     */
    public AcademicGroup() {
        // Required by JPA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}