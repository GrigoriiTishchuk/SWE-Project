package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Role;
import com.edujournal.model.CourseDTO;
import com.edujournal.view.StatCard;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

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
                        new StatCard("Courses",     String.valueOf(teacherCourses.size()), "/images/course_icon.png"),
                        new StatCard("Groups",      String.valueOf(groups),                "/images/group_icon.png"),
                        new StatCard("Students",    String.valueOf(studentIds.size()),     "/images/student_icon.png"),
                        new StatCard("Assessments", String.valueOf(assessments),           "/images/assessement_icon.png")
                );
            }
            case STUDENT -> box.getChildren().addAll(
                        new StatCard("Average Grade",     "4.2", "/images/av_grade_icon.png"),
                        new StatCard("Credits",           "15",  "/images/credits_icon.png"),
                        new StatCard("Current Courses",   "3",   "/images/current_course_icon.png"),
                        new StatCard("Completed Courses", "8",   "/images/course_icon.png")
                );
            case ADMINISTRATOR -> {
                int students = new StudentService().findAll().size();
                int teachers = (int) new UserDAO().findAll().stream()
                        .filter(u -> u.getRole() == Role.TEACHER).count();
                int courses  = new CourseService().findAll().size();
                int groups   = new AcademicGroupService().findAll().size();
                box.getChildren().addAll(
                        new StatCard("Students", String.valueOf(students), "/images/student_icon.png"),
                        new StatCard("Teachers", String.valueOf(teachers), "/images/teacher_icon.png"),
                        new StatCard("Courses",  String.valueOf(courses),  "/images/course_icon.png"),
                        new StatCard("Groups",   String.valueOf(groups),   "/images/group_icon.png")
                );
            }
        }

        return box;
    }
}
