package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.backend.service.StudentReportService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.config.LocalizationManager;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.model.CourseDTO;
import com.edujournal.view.StatCard;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Creates role-specific summary cards for dashboard pages.
 */
public class DashboardStatCards {
    private static final String COURSE_ICON = "/images/course_icon.png";
    private static final String GROUP_ICON = "/images/group_icon.png";
    private static final String STUDENT_ICON = "/images/student_icon.png";
    private static final String TEACHER_ICON = "/images/teacher_icon.png";
    private static final String ASSESSMENT_ICON = "/images/assessement_icon.png";
    private static final String CURRENT_COURSE_ICON = "/images/current_course_icon.png";
    private static final String AVG_GRADE_ICON = "/images/av_grade_icon.png";

    private DashboardStatCards() {
    }

    /**
     * Builds dashboard statistics for the currently logged-in user.
     *
     * @param role role used to select the applicable statistics
     * @return a row of dashboard statistic cards
     */
    public static HBox build(Role role) {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER);

        switch (role) {
            case TEACHER -> buildTeacherStats(box);
            case STUDENT -> buildStudentStats(box);
            case ADMINISTRATOR -> buildAdminStats(box);
        }

        return box;
    }

    private static void buildTeacherStats(HBox box) {
        Integer teacherId = UserSession.getInstance()
                .getCurrentUser()
                .getId();

        List<CourseDTO> teacherCourses =
                new CourseService().findByUserId(teacherId);

        AssessmentsService assessmentsService =
                new AssessmentsService();

        EnrollmentService enrollmentService =
                new EnrollmentService();

        Set<Integer> studentIds = new HashSet<>();

        int assessments = 0;

        for (CourseDTO course : teacherCourses) {
            assessments += assessmentsService
                    .getByCourseId(course.getId())
                    .size();

            for (Enrollment enrollment :
                    enrollmentService.findByCourseId(course.getId())) {
                studentIds.add(enrollment.getStudentId());
            }
        }

        int groups = (int) teacherCourses.stream()
                .map(CourseDTO::getGroupId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        box.getChildren().addAll(
                new StatCard(
                        LocalizationManager.get("courses"),
                        String.valueOf(teacherCourses.size()),
                        COURSE_ICON),

                new StatCard(
                        LocalizationManager.get("groups"),
                        String.valueOf(groups),
                        GROUP_ICON),

                new StatCard(
                        LocalizationManager.get("students"),
                        String.valueOf(studentIds.size()),
                        STUDENT_ICON),

                new StatCard(
                        LocalizationManager.get("assessments"),
                        String.valueOf(assessments),
                        ASSESSMENT_ICON)
        );
    }

    private static void buildStudentStats(HBox box) {

        Integer userId = UserSession.getInstance()
                .getCurrentUser()
                .getId();

        Student student =
                new StudentService().findByUserId(userId);

        EnrollmentService enrollmentService =
                new EnrollmentService();

        AssessmentsService assessmentsService =
                new AssessmentsService();

        CourseService courseService =
                new CourseService();

        int enrolledCourses = 0;
        int assessments = 0;
        int completed = 0;
        String avgGrade = "—";

        if (student != null) {

            List<Enrollment> enrollments =
                    enrollmentService.findByStudentId(student.getId());

            enrolledCourses = enrollments.size();

            for (Enrollment enrollment : enrollments) {

                assessments += assessmentsService
                        .getByCourseId(enrollment.getCourseId())
                        .size();

                CourseDTO course =
                        courseService.getById(enrollment.getCourseId());

                if (course != null
                        && course.getEndDate() != null
                        && course.getEndDate().isBefore(LocalDate.now())) {

                    completed++;
                }
            }

            avgGrade = new StudentReportService()
                    .computeAverageGrade(student.getId());
        }

        box.getChildren().addAll(
                new StatCard(
                        LocalizationManager.get("averageGrade"),
                        avgGrade,
                        AVG_GRADE_ICON),

                new StatCard(
                        LocalizationManager.get("assessments"),
                        String.valueOf(assessments),
                        ASSESSMENT_ICON),

                new StatCard(
                        LocalizationManager.get("currentCourses"),
                        String.valueOf(enrolledCourses),
                        CURRENT_COURSE_ICON),

                new StatCard(
                        LocalizationManager.get("completed"),
                        String.valueOf(completed),
                        COURSE_ICON)
        );
    }

    private static void buildAdminStats(HBox box) {

        int students =
                new StudentService().findAll().size();

        int teachers = (int) new UserDAO()
                .findAll()
                .stream()
                .filter(user -> user.getRole() == Role.TEACHER)
                .count();

        int courses =
                new CourseService().findAll().size();

        int groups =
                new AcademicGroupService().findAll().size();

        box.getChildren().addAll(
                new StatCard(
                        LocalizationManager.get("students"),
                        String.valueOf(students),
                        STUDENT_ICON),

                new StatCard(
                        LocalizationManager.get("teachers"),
                        String.valueOf(teachers),
                        TEACHER_ICON),

                new StatCard(
                        LocalizationManager.get("courses"),
                        String.valueOf(courses),
                        COURSE_ICON),

                new StatCard(
                        LocalizationManager.get("groups"),
                        String.valueOf(groups),
                        GROUP_ICON)
        );
    }
}
