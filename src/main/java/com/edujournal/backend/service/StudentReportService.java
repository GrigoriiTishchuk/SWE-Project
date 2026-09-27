package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.*;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.StudentReportDTO;
import com.edujournal.view.teacher.GradesTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                Grades grade = gradesDAO.findByEnrollmentAssessment(enrollment.getId(), assessment.getId());

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

    public String computeAverageGrade(Integer studentId) {
        double gradeSum = 0;
        int gradeCount = 0;
        for (Enrollment e : enrollmentService.findByStudentId(studentId)) {
            List<Assessments> courseAssessments = assessmentsService.getByCourseId(e.getCourseId());
            Map<Integer, Grades> gradeMap = gradesDAO.findByEnrollment(e.getId()).stream()
                    .collect(Collectors.toMap(Grades::getAssessmentId, g -> g));
            String result = GradesTab.computeFinalGrade(courseAssessments, gradeMap, courseAssessments.size());
            if (!result.equals("—")) {
                gradeSum += Integer.parseInt(result.substring(0, 1));
                gradeCount++;
            }
        }
        return gradeCount == 0 ? "—" : String.format("%.1f", gradeSum / gradeCount);
    }
}