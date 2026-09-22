package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.common.ChartPlaceholder;
import com.edujournal.view.common.DashboardStatCards;
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
        content.getChildren().addAll(TopBar.build("Dashboard", Role.ADMINISTRATOR, false), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildStatCards() {
        return DashboardStatCards.build(Role.ADMINISTRATOR);
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
        viewStudents.setOnAction(e -> Main.showPage(new AdminStudentPage(AdminSidebar.build("Students"), Role.ADMINISTRATOR)));

        Button viewTeachers = new Button("View all teachers");
        viewTeachers.setOnAction(e -> Main.showPage(new AdminTeacherPage(AdminSidebar.build("Teachers"), Role.ADMINISTRATOR)));

        Button viewCourses = new Button("View all courses");
        viewCourses.setOnAction(e -> Main.showPage(new CoursePage(AdminSidebar.build("Courses"), Role.ADMINISTRATOR)));

        Button viewGroups = new Button("View all groups");
        viewGroups.setOnAction(e -> Main.showPage(new AdminGroupPage(AdminSidebar.build("Academic Groups"), Role.ADMINISTRATOR)));

        HBox addButtons  = new HBox(8, addStudent, addTeacher, addCourse, addGroup);
        HBox viewButtons = new HBox(8, viewStudents, viewTeachers, viewCourses, viewGroups);

        box.getChildren().addAll(heading, search, addButtons, viewButtons);
        return box;
    }
}
