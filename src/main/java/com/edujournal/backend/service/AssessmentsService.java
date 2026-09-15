package com.edujournal.backend.service;

import com.edujournal.dao.AssessmentsDAO;
import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;

import java.util.List;

public class AssessmentsService {

    private final AssessmentsDAO assessmentsDAO =
            new AssessmentsDAO();

    private final GradesDAO gradesDAO =
            new GradesDAO();

    private final CourseDAO courseDAO =
            new CourseDAO();

    public List<Assessments> getByCourseId(Integer courseId) {
        return assessmentsDAO.findByCourseId(courseId);
    }

    public Assessments findById(Integer id) {
        return assessmentsDAO.findById(id);
    }

    public List<Assessments> getByCourseName(String courseName) {

        Course course =
                courseDAO.findByName(courseName);

        if (course == null) {
            return List.of();
        }

        return assessmentsDAO.findByCourseId(
                course.getId()
        );
    }

    public void save(Assessments assessment) {
        assessmentsDAO.save(assessment);
    }

    public Course getCourseByName(String courseName) {
        return courseDAO.findByName(courseName);
    }

    public void update(Assessments assessment) {
        assessmentsDAO.update(assessment);
    }

    public boolean hasGrades(Integer assessmentId) {

        return !gradesDAO
                .findByAssessment(assessmentId)
                .isEmpty();
    }

    public void delete(Assessments assessment) {
        assessmentsDAO.delete(assessment);
    }
}