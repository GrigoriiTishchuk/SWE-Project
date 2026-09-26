package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.common.ChartPlaceholder;
import com.edujournal.view.common.DashboardStatCards;
import com.edujournal.view.common.CoursePage;
import com.edujournal.view.common.TopBar;
import com.edujournal.view.controller.AdminGroupController;
import com.edujournal.view.controller.AdminStudentController;
import com.edujournal.view.controller.AdminTeacherController;
import com.edujournal.view.controller.CourseController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

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
        box.getStylesheets().add(AdminDashboardPage.class.getResource("/css/button.css").toExternalForm());

        Label heading = new Label("Quick Actions");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField search = new TextField();
        search.setPromptText("Type the name");

        Button addStudent  = new Button("Add student");
        addStudent.setOnAction(e -> {
            AdminStudentController sController = new AdminStudentController(Role.ADMINISTRATOR);
            sController.showAddDialog();
            Main.showPage(new AdminStudentPage(AdminSidebar.build("Students"), Role.ADMINISTRATOR));
        });

        Button addTeacher  = new Button("Add teacher");
        addTeacher.setOnAction(e -> {
            AdminTeacherController tController = new AdminTeacherController(Role.ADMINISTRATOR);
            tController.showAddDialog();
            Main.showPage(new AdminTeacherPage(AdminSidebar.build("Teachers"), Role.ADMINISTRATOR));
        });

        Button addCourse   = new Button("Add course");
        addCourse.setOnAction(e -> {
            CourseController cController = new CourseController(Role.ADMINISTRATOR);
            cController.showAddDialog();
            Main.showPage(new CoursePage(AdminSidebar.build("Courses"), Role.ADMINISTRATOR));
        });

        Button addGroup    = new Button("Add group");
        addGroup.setOnAction(e -> {
            AdminGroupController gController = new AdminGroupController(Role.ADMINISTRATOR);
            gController.showAddDialog();
            Main.showPage(new AdminGroupPage(AdminSidebar.build("Groups"), Role.ADMINISTRATOR));
        });

        Button viewStudents = new Button("View all students");
        viewStudents.setOnAction(e -> Main.showPage(new AdminStudentPage(AdminSidebar.build("Students"), Role.ADMINISTRATOR)));

        Button viewTeachers = new Button("View all teachers");
        viewTeachers.setOnAction(e -> Main.showPage(new AdminTeacherPage(AdminSidebar.build("Teachers"), Role.ADMINISTRATOR)));

        Button viewCourses = new Button("View all courses");
        viewCourses.setOnAction(e -> Main.showPage(new CoursePage(AdminSidebar.build("Courses"), Role.ADMINISTRATOR)));

        Button viewGroups = new Button("View all groups");
        viewGroups.setOnAction(e -> Main.showPage(new AdminGroupPage(AdminSidebar.build("Academic Groups"), Role.ADMINISTRATOR)));

        List.of(addStudent, addTeacher, addCourse, addGroup, viewStudents, viewTeachers, viewCourses, viewGroups)
                .forEach(b -> b.getStyleClass().add("btn-primary"));

        HBox row1 = new HBox(8, addStudent, addTeacher);
        HBox row2 = new HBox(8, addCourse, addGroup);
        HBox row3 = new HBox(8, viewStudents, viewTeachers);
        HBox row4 = new HBox(8, viewCourses, viewGroups);

        List<Button> all = List.of(addStudent, addTeacher, addCourse, addGroup,
                viewStudents, viewTeachers, viewCourses, viewGroups);

        search.textProperty().addListener((obs, old, text) -> {
            if (text.isEmpty()) box.setMinWidth(0);
            else if (old.isEmpty()) box.setMinWidth(box.getWidth());
            String q = text.toLowerCase();
            all.forEach(b -> { b.setVisible(b.getText().toLowerCase().contains(q)); b.setManaged(b.isVisible()); });
        });

        box.getChildren().addAll(heading, search, row1, row2, row3, row4);
        return box;
    }
}
