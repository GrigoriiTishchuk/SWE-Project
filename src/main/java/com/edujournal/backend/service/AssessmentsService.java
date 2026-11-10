
package com.edujournal.backend.service;

import com.edujournal.dao.AssessmentsDAO;
import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;

import java.util.List;

/**
 * Provides business logic for managing assessments and retrieving
 * assessment information related to courses and grades.
 *
 * @author EduJournal
 */
public final class AssessmentsService {

    /** Data access object for assessment operations. */
    private final AssessmentsDAO assessmentsDAO =
            new AssessmentsDAO();

    /** Data access object for grade operations. */
    private final GradesDAO gradesDAO =
            new GradesDAO();

    /** Data access object for course operations. */
    private final CourseDAO courseDAO =
            new CourseDAO();

    /**
     * Retrieves all assessments associated with a course.
     *
     * @param courseId the ID of the course
     * @return a list of assessments belonging to the course
     */
    public List<Assessments> getByCourseId(
            final Integer courseId) {
        return assessmentsDAO.findByCourseId(courseId);
    }

    /**
     * Finds an assessment by its ID.
     *
     * @param id the ID of the assessment
     * @return the assessment with the specified ID, or null if not found
     */
    public Assessments findById(final Integer id) {
        return assessmentsDAO.findById(id);
    }

    /**
     * Retrieves assessments associated with a course name.
     *
     * @param courseName the name of the course
     * @return a list of assessments, or an empty list if the course
     *         does not exist
     */
    public List<Assessments> getByCourseName(
            final String courseName) {

        Course course =
                courseDAO.findByName(courseName);

        if (course == null) {
            return List.of();
        }

        return assessmentsDAO.findByCourseId(
                course.getId()
        );
    }

    /**
     * Saves a new assessment.
     *
     * @param assessment the assessment to save
     */
    public void save(final Assessments assessment) {
        assessmentsDAO.save(assessment);
    }

    /**
     * Retrieves a course by its name.
     *
     * @param courseName the name of the course
     * @return the matching course, or null if not found
     */
    public Course getCourseByName(final String courseName) {
        return courseDAO.findByName(courseName);
    }

    /**
     * Updates an existing assessment.
     *
     * @param assessment the assessment containing updated information
     */
    public void update(final Assessments assessment) {
        assessmentsDAO.update(assessment);
    }

    /**
     * Checks whether an assessment has any associated grades.
     *
     * @param assessmentId the ID of the assessment
     * @return true if the assessment has grades, false otherwise
     */
    public boolean hasGrades(final Integer assessmentId) {

        return !gradesDAO
                .findByAssessment(assessmentId)
                .isEmpty();
    }

    /**
     * Deletes an assessment.
     *
     * @param assessment the assessment to delete
     */
    public void delete(final Assessments assessment) {
        assessmentsDAO.delete(assessment);
    }
}
