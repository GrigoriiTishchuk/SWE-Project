package com.edujournal.backend.service;

import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Enrollment;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;

import java.util.List;

public class AcademicGroupService {

    private final AcademicGroupDAO academicGroupDAO;
    private final AcademicGroupMapper agMapper = new AcademicGroupMapper();
    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

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

    public void addStudentToGroup(Integer studentId, Integer groupId) {
        academicGroupDAO.addStudentToGroup(studentId, groupId);
        List<CourseDTO> courses = courseService.findByGroupId(groupId);

        for (CourseDTO course : courses) {
            enrollmentService.createIfNotExists(
                    studentId,
                    course.getId(),
                    groupId
            );
        }
    }

    public void removeStudentFromGroup(Integer studentId, Integer groupId) {

        academicGroupDAO.removeStudentFromGroup(
                studentId,
                groupId
        );

        enrollmentService.deleteByStudentAndAcademicGroup(
                studentId,
                groupId
        );
    }
}