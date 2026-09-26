package com.edujournal.view.student;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.model.CourseDTO;
import com.edujournal.view.common.ChartPlaceholder;
import com.edujournal.view.common.DashboardStatCards;
import com.edujournal.view.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class StudentDashboardPage extends BorderPane {

    public StudentDashboardPage() {
        setLeft(StudentSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Dashboard", Role.STUDENT, false), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildStatCards() {
        return DashboardStatCards.build(Role.STUDENT);
    }

    private HBox buildBottomRow() {
        HBox box = new HBox(16, buildCourseOverview(), ChartPlaceholder.build("Personal Grade Distribution"));
        box.setAlignment(javafx.geometry.Pos.CENTER);
        return box;
    }

    private VBox buildCourseOverview() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label heading = new Label("Course Overview");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField search = new TextField();
        search.setPromptText("Type the name of course");

        List<CourseDTO> courses = loadStudentCourses();

        VBox courseList = new VBox(8);
        courses.forEach(c -> courseList.getChildren().add(courseRow(c)));

        search.textProperty().addListener((obs, old, text) -> {
            String q = text.toLowerCase();
            courseList.getChildren().clear();
            courses.stream()
                    .filter(c -> c.getName().toLowerCase().contains(q) || c.getCode().toLowerCase().contains(q))
                    .forEach(c -> courseList.getChildren().add(courseRow(c)));
        });

        box.getChildren().addAll(heading, search, courseList);
        return box;
    }

    private List<CourseDTO> loadStudentCourses() {
        Integer userId = UserSession.getInstance().getCurrentUser().getId();
        Student student = new StudentService().findByUserId(userId);
        if (student == null) return List.of();

        EnrollmentService enrollmentService = new EnrollmentService();
        CourseService courseService = new CourseService();

        return enrollmentService.findByStudentId(student.getId()).stream()
                .map(e -> courseService.getById(e.getCourseId()))
                .filter(c -> c != null)
                .toList();
    }

    private HBox courseRow(CourseDTO course) {
        Label icon = new Label("📚");
        icon.setStyle("-fx-font-size: 22px;");

        Label name = new Label(course.getName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        Label code = new Label(course.getCode());
        code.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13px;");

        VBox text = new VBox(2, name, code);

        HBox row = new HBox(10, icon, text);
        row.setPadding(new Insets(8));
        row.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6;");
        return row;
    }
}
