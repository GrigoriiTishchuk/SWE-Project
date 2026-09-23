package com.edujournal.backend.service;

import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.AcademicGroupDTO;

import java.util.List;

public class AcademicGroupService {

    private final AcademicGroupDAO academicGroupDAO;
    private final AcademicGroupMapper agMapper = new AcademicGroupMapper();

    public AcademicGroupService() {
        this.academicGroupDAO = new AcademicGroupDAO();
    }

    public AcademicGroup findById(Integer id) {
        return academicGroupDAO.findById(id);
    }

    public List<AcademicGroup> findAll() { return academicGroupDAO.findAll(); }

    public List<AcademicGroupDTO> findAllDTO() {
        return academicGroupDAO.findAll().stream().map(agMapper::toDTO).toList();
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

    public boolean existsByName(String name) {
        return academicGroupDAO.findByName(name) != null;
    }

}