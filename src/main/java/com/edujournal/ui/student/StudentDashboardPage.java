package com.edujournal.ui.student;

import com.edujournal.ui.StatCard;
import com.edujournal.ui.common.ChartPlaceholder;
import com.edujournal.ui.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Everything is hardcoded

public class StudentDashboardPage extends BorderPane {

    public StudentDashboardPage() {
        setLeft(StudentSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Dashboard", "Student"), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildStatCards() {
        return new HBox(16,
                new StatCard("Average Grade",     "4.83", "/images/av_grade_icon.png"),
                new StatCard("Credits",           "183",  "/images/credits_icon.png"),
                new StatCard("Current Courses",   "4",    "/images/current_course_icon.png"),
                new StatCard("Completed Courses", "23",   "/images/course_icon.png")
        );
    }

    private HBox buildBottomRow() {
        return new HBox(16, buildCourseOverview(), ChartPlaceholder.build("Personal Grade Distribution"));
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

        box.getChildren().addAll(
                heading, search,
                courseRow("Software Engineering Project 1", "TXK3000-112"),
                courseRow("Software Engineering Project 2", "TXK3000-113"),
                courseRow("WEB-Project", "TXK3000-105")
        );
        return box;
    }

    private HBox courseRow(String name, String code) {
        VBox text = new VBox(2, new Label(name), new Label(code));
        HBox row = new HBox(text);
        row.setPadding(new Insets(8));
        row.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 6;");
        return row;
    }
}
