package com.edujournal.model;

/**
 * Data transfer object for academic groups.
 */
public class AcademicGroupDTO {
    private Integer id;
    private String name;

    /**
     * Creates an academic group DTO.
     *
     * @param id group identifier
     * @param name group name
     */
    public AcademicGroupDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Creates an empty academic group DTO.
     */
    public AcademicGroupDTO() {
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