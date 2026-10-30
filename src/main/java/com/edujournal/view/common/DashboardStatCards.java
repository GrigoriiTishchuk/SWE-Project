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
import com.edujournal.model.CourseDTO;
import com.edujournal.view.StatCard;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DashboardStatCards {

    public static HBox build(Role role) {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER);

        switch (role) {
            case TEACHER -> {
                Integer teacherId = UserSession.getInstance().getCurrentUser().getId();
                List<CourseDTO> teacherCourses = new CourseService().findByUserId(teacherId);
                AssessmentsService assessmentsService = new AssessmentsService();
                EnrollmentService enrollmentService = new EnrollmentService();
                Set<Integer> studentIds = new HashSet<>();
                int assessments = 0;
                for (CourseDTO c : teacherCourses) {
                    assessments += assessmentsService.getByCourseId(c.getId()).size();
                    for (Enrollment e : enrollmentService.findByCourseId(c.getId()))
                        studentIds.add(e.getStudentId());
                }
                int groups = (int) teacherCourses.stream()
                        .map(CourseDTO::getGroupId).filter(Objects::nonNull).distinct().count();
                box.getChildren().addAll(
                        new StatCard(LocalizationManager.get("courses"),     String.valueOf(teacherCourses.size()), "/images/course_icon.png"),
                        new StatCard(LocalizationManager.get("groups"),      String.valueOf(groups),                "/images/group_icon.png"),
                        new StatCard(LocalizationManager.get("students"),    String.valueOf(studentIds.size()),     "/images/student_icon.png"),
                        new StatCard(LocalizationManager.get("assessments"), String.valueOf(assessments),           "/images/assessement_icon.png")
                );
            }
            case STUDENT -> {
                Integer userId = UserSession.getInstance().getCurrentUser().getId();
                com.edujournal.entity.Student student = new StudentService().findByUserId(userId);
                EnrollmentService enrollmentService = new EnrollmentService();
                AssessmentsService assessmentsService = new AssessmentsService();
                CourseService courseService = new CourseService();

                int enrolledCourses = 0;
                int assessments = 0;
                int completed = 0;
                String avgGrade = "—";

                if (student != null) {
                    List<Enrollment> enrollments = enrollmentService.findByStudentId(student.getId());
                    enrolledCourses = enrollments.size();
                    for (Enrollment e : enrollments) {
                        assessments += assessmentsService.getByCourseId(e.getCourseId()).size();
                        CourseDTO course = courseService.getById(e.getCourseId());
                        if (course != null && course.getEndDate() != null && course.getEndDate().isBefore(LocalDate.now())) {
                            completed++;
                        }
                    }
                    avgGrade = new StudentReportService().computeAverageGrade(student.getId());
                }

                box.getChildren().addAll(
                        new StatCard(LocalizationManager.get("averageGrade"),   avgGrade,                      "/images/av_grade_icon.png"),
                        new StatCard(LocalizationManager.get("assessments"),    String.valueOf(assessments),    "/images/assessement_icon.png"),
                        new StatCard(LocalizationManager.get("currentCourses"), String.valueOf(enrolledCourses),"/images/current_course_icon.png"),
                        new StatCard(LocalizationManager.get("completed"),      String.valueOf(completed),      "/images/course_icon.png")
                );
            }
            case ADMINISTRATOR -> {
                int students = new StudentService().findAll().size();
                int teachers = (int) new UserDAO().findAll().stream()
                        .filter(u -> u.getRole() == Role.TEACHER).count();
                int courses  = new CourseService().findAll().size();
                int groups   = new AcademicGroupService().findAll().size();
                box.getChildren().addAll(
                        new StatCard(LocalizationManager.get("students"), String.valueOf(students), "/images/student_icon.png"),
                        new StatCard(LocalizationManager.get("teachers"), String.valueOf(teachers), "/images/teacher_icon.png"),
                        new StatCard(LocalizationManager.get("courses"),  String.valueOf(courses),  "/images/course_icon.png"),
                        new StatCard(LocalizationManager.get("groups"),   String.valueOf(groups),   "/images/group_icon.png")
                );
            }
        }

        return box;
    }

}
