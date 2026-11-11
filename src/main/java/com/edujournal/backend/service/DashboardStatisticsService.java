
package com.edujournal.backend.service;

import com.edujournal.dao.CourseDAO;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.model.GradeDistributionDTO;
import com.edujournal.view.teacher.GradesTab;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Provides grade statistics for administrators, teachers, and students.
 *
 * @author EduJournal
 */
public final class DashboardStatisticsService {

    /** Logger used to record statistics-related messages. */
    private static final Logger LOGGER =
            Logger.getLogger(DashboardStatisticsService.class.getName());

    /** Default time zone used for date calculations. */
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    /** Maximum grade value. */
    private static final int EXCELLENT_GRADE = 5;

    /** Very good grade value. */
    private static final int VERY_GOOD_GRADE = 4;

    /** Good grade value. */
    private static final int GOOD_GRADE = 3;

    /** Satisfactory grade value. */
    private static final int SATISFACTORY_GRADE = 2;

    /** Sufficient grade value. */
    private static final int SUFFICIENT_GRADE = 1;

    /** Failing grade value. */
    private static final int FAIL_GRADE = 0;

    /** Percentage multiplier used for grade distribution. */
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    /** Data access object for course operations. */
    private final CourseDAO courseDAO = new CourseDAO();

    /** Data access object for grade operations. */
    private final GradesDAO gradesDAO = new GradesDAO();

    /** Data access object for enrollment operations. */
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    /** Service responsible for assessment operations. */
    private final AssessmentsService assessmentsService =
            new AssessmentsService();

    /** Service responsible for final course grade calculations. */
    private final CourseGradeService courseGradeService =
            new CourseGradeService();

    /** Service responsible for enrollment operations. */
    private final EnrollmentService enrollmentService =
            new EnrollmentService();

    /** Service responsible for student operations. */
    private final StudentService studentService =
            new StudentService();

    /**
     * Retrieves grade distribution according to the user's role.
     *
     * @param role the role of the user
     * @param userId the user's identifier
     * @return the grade distribution for the specified role
     */
    public GradeDistributionDTO getGradeDistribution(
            final Role role,
            final Integer userId) {

        switch (role) {
            case ADMINISTRATOR:
                return calculateDistribution(
                        getAdministratorGrades());

            case TEACHER:
                return calculateDistribution(
                        getTeacherGrades(userId));

            case STUDENT:
                Student student = studentService.findByUserId(userId);
                return calculateDistribution(
                        getStudentGrades(student.getId()));

            default:
                return new GradeDistributionDTO();
        }
    }

    /**
     * Retrieves final grades for all completed courses.
     *
     * @return a list of administrator-visible final grades
     */
    public List<Integer> getAdministratorGrades() {
        LocalDate today = LocalDate.now(SYSTEM_ZONE);
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
                Integer finalGrade = calculateAdministratorGrade(
                        enrollment, assessments);

                if (finalGrade != null) {
                    finalGrades.add(finalGrade);
                }
            }
        }

        return finalGrades;
    }

    /**
     * Calculates a final grade for an administrator's enrollment.
     *
     * @param enrollment the enrollment to process
     * @param assessments the course assessments
     * @return the calculated grade, or null if grades are incomplete
     */
    private Integer calculateAdministratorGrade(
            final Enrollment enrollment,
            final List<Assessments> assessments) {

        Map<Integer, Grades> gradeMap =
                getGradeMap(enrollment.getId());

        if (!hasCompleteGrades(assessments, gradeMap)) {
            return null;
        }

        try {
            double percent = courseGradeService.calculateFinalGrade(
                    assessments,
                    new ArrayList<>(gradeMap.values()));

            return GradesTab.toGrade(percent);
        } catch (Exception exception) {
            LOGGER.log(
                    Level.WARNING,
                    () -> "Failed to calculate grade for enrollment "
                            + enrollment.getId());
            LOGGER.log(Level.WARNING, "Grade calculation exception", exception);
            return null;
        }
    }

    /**
     * Retrieves final grades for a specific student.
     *
     * @param studentId the student's identifier
     * @return a list of the student's final grades
     */
    public List<Integer> getStudentGrades(final Integer studentId) {
        LOGGER.log(Level.INFO, "Student ID = {0}", studentId);

        List<Integer> finalGrades = new ArrayList<>();

        List<Enrollment> enrollments =
                enrollmentService.findByStudentId(studentId);

        for (Enrollment enrollment : enrollments) {
            Integer courseId = enrollment.getCourseId();

            List<Assessments> assessments =
                    assessmentsService.getByCourseId(courseId);

            Map<Integer, Grades> gradeMap =
                    getGradeMap(enrollment.getId());

            if (assessments.isEmpty()
                    || !hasCompleteGrades(assessments, gradeMap)) {
                continue;
            }

            Integer finalGrade = calculateStudentGrade(
                    assessments, gradeMap, studentId, enrollment);

            if (finalGrade != null) {
                finalGrades.add(finalGrade);
            }
        }

        return finalGrades;
    }

    /**
     * Calculates a student's final grade for an enrollment.
     *
     * @param assessments the course assessments
     * @param gradeMap grades indexed by assessment identifier
     * @param studentId the student's identifier
     * @param enrollment the enrollment being processed
     * @return the calculated grade, or null if calculation fails
     */
    private Integer calculateStudentGrade(
            final List<Assessments> assessments,
            final Map<Integer, Grades> gradeMap,
            final Integer studentId,
            final Enrollment enrollment) {

        try {
            String result = GradesTab.computeFinalGrade(
                    assessments,
                    gradeMap,
                    assessments.size());

            if (result != null && !result.equals("—")) {
                return Integer.parseInt(result.substring(0, 1));
            }
        } catch (Exception exception) {
            LOGGER.log(
                    Level.WARNING,
                    () -> "Failed to calculate grade for student "
                            + studentId
                            + ", enrollment "
                            + enrollment.getId());
            LOGGER.log(Level.WARNING, "Grade calculation exception", exception);
        }

        return null;
    }

    /**
     * Retrieves final grades for courses assigned to a teacher.
     *
     * @param teacherUserId the teacher's user identifier
     * @return a list of the teacher's students' final grades
     */
    public List<Integer> getTeacherGrades(final Integer teacherUserId) {
        List<Integer> finalGrades = new ArrayList<>();

        List<Course> courses = courseDAO.findByUserId(teacherUserId);

        LOGGER.log(
                Level.INFO,
                "Teacher courses: {0}",
                courses.size());

        for (Course course : courses) {
            finalGrades.addAll(calculateTeacherCourseGrades(course));
        }

        return finalGrades;
    }

    /**
     * Calculates final grades for all eligible enrollments in a course.
     *
     * @param course the course to process
     * @return a list of calculated final grades
     */
    private List<Integer> calculateTeacherCourseGrades(
            final Course course) {

        List<Integer> courseGrades = new ArrayList<>();

        List<Enrollment> enrollments =
                enrollmentDAO.findByCourseId(course.getId());

        List<Assessments> assessments =
                assessmentsService.getByCourseId(course.getId());

        LOGGER.log(
                Level.INFO,
                "Course {0}, enrollments: {1}",
                new Object[] {course.getName(), enrollments.size()});

        LocalDate today = LocalDate.now(SYSTEM_ZONE);

        if (course.getEndDate() == null
                || !course.getEndDate().isBefore(today)
                || assessments.isEmpty()) {
            return courseGrades;
        }

        for (Enrollment enrollment : enrollments) {
            Integer finalGrade = calculateTeacherEnrollmentGrade(
                    enrollment, assessments);

            if (finalGrade != null) {
                courseGrades.add(finalGrade);
            }
        }

        return courseGrades;
    }

    /**
     * Calculates a final grade for a teacher's enrollment.
     *
     * @param enrollment the enrollment to process
     * @param assessments the course assessments
     * @return the calculated grade, or null if grades are incomplete
     */
    private Integer calculateTeacherEnrollmentGrade(
            final Enrollment enrollment,
            final List<Assessments> assessments) {

        Map<Integer, Grades> gradeMap =
                getGradeMap(enrollment.getId());

        if (!hasCompleteGrades(assessments, gradeMap)) {
            return null;
        }

        try {
            String result = GradesTab.computeFinalGrade(
                    assessments,
                    gradeMap,
                    assessments.size());

            if (result != null && !result.equals("—")) {
                return Integer.parseInt(result.substring(0, 1));
            }
        } catch (Exception exception) {
            LOGGER.log(
                    Level.WARNING,
                    "Failed to calculate teacher statistics",
                    exception);
        }

        return null;
    }

    /**
     * Retrieves grades for an enrollment indexed by assessment identifier.
     *
     * @param enrollmentId the enrollment identifier
     * @return a map of assessment identifiers and their grades
     */
    private Map<Integer, Grades> getGradeMap(
            final Integer enrollmentId) {

        return gradesDAO.findByEnrollment(enrollmentId)
                .stream()
                .collect(Collectors.toMap(
                        Grades::getAssessmentId,
                        grade -> grade));
    }

    /**
     * Checks whether all assessments have non-null grades.
     *
     * @param assessments the assessments to check
     * @param gradeMap the grades indexed by assessment identifier
     * @return true if all assessments have grades, otherwise false
     */
    private boolean hasCompleteGrades(
            final List<Assessments> assessments,
            final Map<Integer, Grades> gradeMap) {

        return !assessments.isEmpty()
                && !gradeMap.isEmpty()
                && gradeMap.size() >= assessments.size()
                && gradeMap.values()
                .stream()
                .noneMatch(grade -> grade.getScore() == null);
    }

    /**
     * Calculates the percentage distribution of final grades.
     *
     * @param grades the final grades to analyze
     * @return the calculated grade distribution
     */
    private GradeDistributionDTO calculateDistribution(
            final List<Integer> grades) {

        if (grades.isEmpty()) {
            return new GradeDistributionDTO();
        }

        int total = grades.size();

        long excellent = countGrade(grades, EXCELLENT_GRADE);
        long veryGood = countGrade(grades, VERY_GOOD_GRADE);
        long good = countGrade(grades, GOOD_GRADE);
        long satisfactory = countGrade(grades, SATISFACTORY_GRADE);
        long sufficient = countGrade(grades, SUFFICIENT_GRADE);
        long fail = countGrade(grades, FAIL_GRADE);

        return new GradeDistributionDTO(
                percentage(excellent, total),
                percentage(veryGood, total),
                percentage(good, total),
                percentage(satisfactory, total),
                percentage(sufficient, total),
                percentage(fail, total));
    }

    /**
     * Counts occurrences of a specific grade.
     *
     * @param grades the grades to examine
     * @param targetGrade the grade value to count
     * @return the number of occurrences
     */
    private long countGrade(
            final List<Integer> grades,
            final int targetGrade) {

        return grades.stream()
                .filter(grade -> grade == targetGrade)
                .count();
    }

    /**
     * Converts a grade count into a percentage.
     *
     * @param count the number of grades
     * @param total the total number of grades
     * @return the percentage value
     */
    private double percentage(final long count, final int total) {
        return count * PERCENTAGE_MULTIPLIER / total;
    }
}
