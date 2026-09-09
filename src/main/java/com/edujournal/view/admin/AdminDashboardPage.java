package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.view.StatCard;
import com.edujournal.view.common.ChartPlaceholder;
import com.edujournal.view.common.CoursePage;
import com.edujournal.view.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Everything is hardcoded

public class AdminDashboardPage extends BorderPane {

    public AdminDashboardPage() {
        setLeft(AdminSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Dashboard", "Administrator"), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildStatCards() {
        HBox box = new HBox(16,
                new StatCard("Students", "518", "/images/student_icon.png"),
                new StatCard("Teachers", "12",  "/images/teacher_icon.png"),
                new StatCard("Courses",  "32",  "/images/course_icon.png"),
                new StatCard("Groups",   "166", "/images/group_icon.png")
        );
        box.setAlignment(javafx.geometry.Pos.CENTER);
        return box;
    }

    private HBox buildBottomRow() {
        HBox box = new HBox(16, buildQuickActions(), ChartPlaceholder.build("Average Grade"));
        box.setAlignment(javafx.geometry.Pos.CENTER);
        return box;
    }

    private VBox buildQuickActions() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label heading = new Label("Quick Actions");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField search = new TextField();
        search.setPromptText("Type the name");

        Button addStudent  = new Button("Add student");
        Button addTeacher  = new Button("Add teacher");
        Button addCourse   = new Button("Add course");
        Button addGroup    = new Button("Add group");

        Button viewStudents = new Button("View all students");
        viewStudents.setOnAction(e -> Main.showPage(new AdminStudentPage()));

        Button viewTeachers = new Button("View all teachers");
        viewTeachers.setOnAction(e -> Main.showPage(new AdminTeacherPage()));

        Button viewCourses = new Button("View all courses");
        viewCourses.setOnAction(e -> Main.showPage(new CoursePage(AdminSidebar.build("Courses"), "Administrator")));

        Button viewGroups = new Button("View all groups");
        viewGroups.setOnAction(e -> Main.showPage(new AdminGroupPage()));

        HBox addButtons  = new HBox(8, addStudent, addTeacher, addCourse, addGroup);
        HBox viewButtons = new HBox(8, viewStudents, viewTeachers, viewCourses, viewGroups);

        box.getChildren().addAll(heading, search, addButtons, viewButtons);
        return box;
    }
}
