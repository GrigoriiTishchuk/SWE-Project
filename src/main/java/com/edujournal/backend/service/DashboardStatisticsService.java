package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.*;
import com.edujournal.model.GradeDistributionDTO;
import com.edujournal.view.teacher.GradesTab;

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
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final StudentService studentService = new StudentService();

    public GradeDistributionDTO getGradeDistribution(Role role, Integer userId) {
        switch (role) {

            case ADMINISTRATOR:
                return calculateDistribution(
                        getAdministratorGrades()
                );

            case TEACHER:
                return calculateDistribution(
                        getTeacherGrades(userId)
                );

            case STUDENT:

                Student student =
                        studentService.findByUserId(userId);

                return calculateDistribution(
                        getStudentGrades(student.getId())
                );

            default:
                return new GradeDistributionDTO();
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

    public List<Integer> getStudentGrades(Integer studentId) {
        System.out.println("Student ID = " + studentId);

        List<Integer> finalGrades = new ArrayList<>();

        List<Enrollment> enrollments =
                enrollmentService.findByStudentId(studentId);

        for (Enrollment enrollment : enrollments) {

            Integer courseId = enrollment.getCourseId();

            List<Assessments> assessments =
                    assessmentsService.getByCourseId(courseId);

            Map<Integer, Grades> gradeMap =
                    gradesDAO.findByEnrollment(enrollment.getId())
                            .stream()
                            .collect(Collectors.toMap(
                                    Grades::getAssessmentId,
                                    g -> g
                            ));

            if (assessments.isEmpty()) {
                continue;
            }

            if (gradeMap.isEmpty()) {
                continue;
            }

            if (gradeMap.size() < assessments.size()) {
                continue;
            }

            if (gradeMap.values().stream()
                    .anyMatch(g -> g.getScore() == null)) {
                continue;
            }

            try {
                String result =
                        GradesTab.computeFinalGrade(
                                assessments,
                                gradeMap,
                                assessments.size()
                        );

                if (!result.equals("—")) {

                    int finalGrade =
                            Integer.parseInt(
                                    result.substring(0, 1)
                            );

                    finalGrades.add(finalGrade);
                }

            } catch (Exception e) {

                System.err.println(
                        "[Statistics] Student "
                                + studentId
                                + ", enrollment "
                                + enrollment.getId()
                                + ": "
                                + e.getMessage()
                );
            }
        }

        return finalGrades;
    }

    public List<Integer> getTeacherGrades(Integer teacherUserId) {

        List<Integer> finalGrades = new ArrayList<>();

        List<Course> courses =
                courseDAO.findByUserId(teacherUserId);

        System.out.println("Teacher courses: " + courses.size());

        for (Course course : courses) {

            List<Enrollment> enrollments =
                    enrollmentDAO.findByCourseId(course.getId());

            List<Assessments> assessments =
                    assessmentsService.getByCourseId(course.getId());

            System.out.println(
                    "Course " + course.getName()
                            + ", enrollments: "
                            + enrollments.size()
            );

            for (Enrollment enrollment : enrollments) {

                Map<Integer, Grades> gradeMap =
                        gradesDAO.findByEnrollment(enrollment.getId())
                                .stream()
                                .collect(Collectors.toMap(
                                        Grades::getAssessmentId,
                                        g -> g
                                ));

                if (course.getEndDate() == null ||
                        !course.getEndDate().isBefore(LocalDate.now())) {
                    continue;
                }

                if (assessments.isEmpty()) {
                    continue;
                }

                if (gradeMap.isEmpty()) {
                    continue;
                }

                if (gradeMap.size() < assessments.size()) {
                    continue;
                }

                try {

                    String result =
                            GradesTab.computeFinalGrade(
                                    assessments,
                                    gradeMap,
                                    assessments.size()
                            );

                    if (!result.equals("—")) {

                        int finalGrade =
                                Integer.parseInt(result.substring(0, 1));

                        finalGrades.add(finalGrade);
                    }

                } catch (Exception e) {

                    System.err.println(
                            "[TeacherStatistics] "
                                    + e.getMessage()
                    );
                }
            }
        }

        return finalGrades;
    }

    private GradeDistributionDTO calculateDistribution(
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