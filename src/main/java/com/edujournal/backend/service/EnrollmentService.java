package com.edujournal.backend.service;

import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;

import java.util.List;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public Enrollment findById(Integer id) {
        return enrollmentDAO.findById(id);
    }

    public List<Enrollment> findAll() {
        return enrollmentDAO.findAll();
    }

    public List<Enrollment> findByStudentId(Integer studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    public List<Enrollment> findByCourseId(Integer courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    public List<Enrollment> findByAcademicGroupId(Integer academicGroupId) {
        return enrollmentDAO.findByAcademicGroupId(academicGroupId);
    }

    public Enrollment findByStudentAndCourse(Integer studentId, Integer courseId) {
        return enrollmentDAO.findByStudentAndCourse(studentId, courseId);
    }

    public void createIfNotExists(
            Integer studentId,
            Integer courseId,
            Integer academicGroupId) {

        Enrollment existing =
                findByStudentAndCourse(studentId, courseId);

        if (existing == null) {
            Enrollment enrollment = new Enrollment();

            enrollment.setStudentId(studentId);
            enrollment.setCourseId(courseId);
            enrollment.setAcademicGroupId(academicGroupId);
            enrollment.setStatus("ENROLLED");

            save(enrollment);
        }
    }

    public void save(Enrollment enrollment) {
        enrollmentDAO.save(enrollment);
    }

    public void update(Enrollment enrollment) {
        enrollmentDAO.update(enrollment);
    }

    public void delete(Integer id) {
        enrollmentDAO.delete(id);
    }

    public void deleteByStudentAndAcademicGroup(
            Integer studentId,
            Integer academicGroupId) {

        enrollmentDAO.deleteByStudentAndAcademicGroup(
                studentId,
                academicGroupId
        );
    }

    public void deleteByCourseId(Integer courseId) {
        enrollmentDAO.deleteByCourseId(courseId);
    }
}
