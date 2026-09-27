package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.*;
import com.edujournal.model.GradeDistributionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edujournal.view.teacher.GradesTab.toGrade;

public class DashboardStatisticsService {
    private final CourseDAO courseDAO = new CourseDAO();
    private final GradesDAO gradesDAO = new GradesDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final CourseGradeService courseGradeService = new CourseGradeService();

    public void loadDistribution(Role role) {

        switch (role) {

            case STUDENT:
                break;

            case TEACHER:
                break;

            case ADMINISTRATOR:
                break;
        }
    }

    public List<Integer> getAdministratorGrades() {

        LocalDate today = LocalDate.now();

        List<Integer> finalGrades = new ArrayList<>();

        List<Course> completedCourses = courseDAO.findAll()
                .stream()
                .filter(course -> course.getEndDate() != null)
                .filter(course -> course.getEndDate().isBefore(today))
                .toList();

        for (Course course : completedCourses) {

            List<Enrollment> enrollments =
                    enrollmentDAO.findByCourseId(course.getId());

            List<Assessments> assessments =
                    assessmentsService.getByCourseId(course.getId());

            for (Enrollment enrollment : enrollments) {

                Map<Integer, Grades> gradeMap =
                        gradesDAO.findByEnrollment(enrollment.getId())
                                .stream()
                                .collect(Collectors.toMap(
                                        Grades::getAssessmentId,
                                        g -> g
                                ));

                // Not all assessments with grades
                if (gradeMap.size() < assessments.size()) {
                    continue;
                }

                // Empty grades
                if (gradeMap.values()
                        .stream()
                        .anyMatch(g -> g.getScore() == null)) {
                    continue;
                }

                try {

                    double percent =
                            courseGradeService.calculateFinalGrade(
                                    assessments,
                                    new ArrayList<>(gradeMap.values())
                            );

                    int finalGrade = toGrade(percent);

                    finalGrades.add(finalGrade);

                } catch (Exception e) {

                    System.err.println(
                            "[Statistics] Failed to calculate grade for enrollment "
                                    + enrollment.getId()
                    );
                }
            }
        }

        return finalGrades;
    }

    public GradeDistributionDTO calculateDistribution(
            List<Integer> grades
    ) {

        if (grades.isEmpty()) {
            return new GradeDistributionDTO();
        }

        int total = grades.size();

        long excellent =
                grades.stream().filter(g -> g == 5).count();

        long veryGood =
                grades.stream().filter(g -> g == 4).count();

        long good =
                grades.stream().filter(g -> g == 3).count();

        long satisfactory =
                grades.stream().filter(g -> g == 2).count();

        long sufficient =
                grades.stream().filter(g -> g == 1).count();

        long fail =
                grades.stream().filter(g -> g == 0).count();

        return new GradeDistributionDTO(
                excellent * 100.0 / total,
                veryGood * 100.0 / total,
                good * 100.0 / total,
                satisfactory * 100.0 / total,
                sufficient * 100.0 / total,
                fail * 100.0 / total
        );
    }
}