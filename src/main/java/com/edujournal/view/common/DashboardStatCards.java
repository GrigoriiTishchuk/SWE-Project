package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.dao.AssessmentsDAO;
import com.edujournal.dao.UserDAO;
import com.edujournal.entity.Role;
import com.edujournal.view.StatCard;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class DashboardStatCards {

    public static HBox build(Role role) {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER);

        switch (role) {
            case TEACHER -> {
                int courses     = new CourseService().findAll().size();
                int groups      = new AcademicGroupService().findAll().size();
                int students    = new StudentService().findAll().size();
                int assessments = new AssessmentsDAO().findAll().size();
                box.getChildren().addAll(
                        new StatCard("Courses",     String.valueOf(courses),     "/images/course_icon.png"),
                        new StatCard("Groups",      String.valueOf(groups),      "/images/group_icon.png"),
                        new StatCard("Students",    String.valueOf(students),    "/images/student_icon.png"),
                        new StatCard("Assessments", String.valueOf(assessments), "/images/assessement_icon.png")
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
