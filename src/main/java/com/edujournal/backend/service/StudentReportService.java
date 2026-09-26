package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.*;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.StudentReportDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentReportService {

    private final StudentService studentService = new StudentService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final CourseDAO courseDAO = new CourseDAO();
    private final GradesDAO gradesDAO = new GradesDAO();
    private final AcademicGroupService academicGroupService = new AcademicGroupService();

    public List<StudentReportDTO> buildReport(Integer studentId) {
        List<StudentReportDTO> report = new ArrayList<>();
        StudentDTO student = studentService.findDTOById(studentId);

        if (student == null) {
            return report;
        }

        List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);

        for (Enrollment enrollment : enrollments) {
            Course course = courseDAO.findById(enrollment.getCourseId());

            if (course == null) {
                continue;
            }

            List<Assessments> assessments = assessmentsService.getByCourseId(course.getId());

            for (Assessments assessment : assessments) {
                Grades grade = gradesDAO.findByEnrollmentAssessment(enrollment.getId(),assessment.getId());
                String groupName = "-";

                if (student.getAcademicGroupId() != null) {
                    AcademicGroup group = academicGroupService.findById(student.getAcademicGroupId());

                    if (group != null) {
                        groupName = group.getName();
                    }
                }

                report.add(new StudentReportDTO(
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

                                grade != null
                                        ? grade.getScore()
                                        : null
                        )
                );
            }
        }
        return report;
    }
}