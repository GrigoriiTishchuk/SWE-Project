package com.edujournal.model;

import com.edujournal.entity.Course;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class AcademicGroupDTO {
    private Integer id;
    private String name;
    private Integer courseId;
    private String courseName;

    public AcademicGroupDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public AcademicGroupDTO() {}

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