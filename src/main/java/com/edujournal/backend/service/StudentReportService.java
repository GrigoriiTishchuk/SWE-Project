package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.StudentReportDTO;
import com.edujournal.view.teacher.GradesTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds student reports and calculates students' average grades.
 *
 * @author EduJournal
 */
public final class StudentReportService {

    /** Service for retrieving student information. */
    private final StudentService studentService = new StudentService();

    /** Service for managing student enrollments. */
    private final EnrollmentService enrollmentService = new EnrollmentService();

    /** Service for retrieving course assessments. */
    private final AssessmentsService assessmentsService = new AssessmentsService();

    /** Data access object for course operations. */
    private final CourseDAO courseDAO = new CourseDAO();

    /** Data access object for grade operations. */
    private final GradesDAO gradesDAO = new GradesDAO();

    /** Service for retrieving academic group information. */
    private final AcademicGroupService academicGroupService = new AcademicGroupService();

    /**
     * Builds a detailed report containing the student's courses, assessments, and grades.
     *
     * @param studentId the identifier of the student
     * @return a list of student report entries, or an empty list if the student does not exist
     */
    public List<StudentReportDTO> buildReport(final Integer studentId) {
        List<StudentReportDTO> report = new ArrayList<>();
        StudentDTO student = studentService.findDTOById(studentId);

        if (student == null) {
            return report;
        }

        List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);

        String groupName = "-";

        if (student.getAcademicGroupId() != null) {
            AcademicGroup group = academicGroupService.findById(student.getAcademicGroupId());

            if (group != null) {
                groupName = group.getName();
            }
        }

        for (Enrollment enrollment : enrollments) {
            Course course = courseDAO.findById(enrollment.getCourseId());

            if (course == null) {
                continue;
            }

            List<Assessments> assessments = assessmentsService.getByCourseId(course.getId());

            for (Assessments assessment : assessments) {
                Grades grade =
                        gradesDAO.findByEnrollmentAssessment(
                                enrollment.getId(), assessment.getId());

                report.add(
                        new StudentReportDTO(
                                student.getStudentId(),
                                student.getFirstName() + " " + student.getLastName(),
                                student.getStudentNumber(),
                                student.getAcademicGroupId(),
                                groupName,
                                course.getId(),
                                course.getCode(),
                                course.getName(),
                                assessment.getId(),
                                assessment.getTitle(),
                                assessment.getType(),
                                grade != null ? grade.getScore() : null));
            }
        }

        return report;
    }

    /**
     * Calculates the average final grade across all courses in which the student is enrolled.
     *
     * @param studentId the identifier of the student
     * @return the average grade formatted to one decimal place, or an em dash if no final grades
     *     are available
     */
    public String computeAverageGrade(final Integer studentId) {
        double gradeSum = 0;
        int gradeCount = 0;

        for (Enrollment enrollment : enrollmentService.findByStudentId(studentId)) {

            List<Assessments> courseAssessments =
                    assessmentsService.getByCourseId(enrollment.getCourseId());

            Map<Integer, Grades> gradeMap =
                    gradesDAO.findByEnrollment(enrollment.getId()).stream()
                            .collect(Collectors.toMap(Grades::getAssessmentId, grade -> grade));

            String result =
                    GradesTab.computeFinalGrade(
                            courseAssessments, gradeMap, courseAssessments.size());

            if (!result.equals("—")) {
                gradeSum += Integer.parseInt(result.substring(0, 1));
                gradeCount++;
            }
        }

        return gradeCount == 0 ? "—" : String.format("%.1f", gradeSum / gradeCount);
    }
}
