package com.edujournal.backend.service;

import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.entity.AcademicGroup;

import java.util.List;

public class AcademicGroupService {

    private final AcademicGroupDAO academicGroupDAO;

    public AcademicGroupService() {
        this.academicGroupDAO = new AcademicGroupDAO();
    }

    public AcademicGroup findById(Integer id) {
        return academicGroupDAO.findById(id);
    }

    public List<AcademicGroup> findAll() {
        return academicGroupDAO.findAll();
    }

    public AcademicGroup findByName(String name) {
        return academicGroupDAO.findByName(name);
    }

    public void save(AcademicGroup academicGroup) {
        academicGroupDAO.save(academicGroup);
    }

    public void update(AcademicGroup academicGroup) {
        academicGroupDAO.update(academicGroup);
    }

    public void delete(Integer id) {
        academicGroupDAO.delete(id);
    }
}