package com.edujournal.ui.student;

import com.edujournal.ui.StatCard;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Student dashboard — static layout matching the Figma design.
 * Everything is hardcoded/placeholder, nothing is connected to real data yet.
 */
public class StudentDashboardPage extends BorderPane {

    public StudentDashboardPage() {
        setLeft(StudentSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox statCards = new HBox(16,
                new StatCard("Average Grade", "4.83"),
                new StatCard("Credits", "183"),
                new StatCard("Current Courses", "4"),
                new StatCard("Completed courses", "23")
        );

        VBox courseOverview = buildCourseOverview();
        VBox chartPlaceholder = buildChartPlaceholder("Personal Grade Distribution");

        HBox bottomRow = new HBox(16, courseOverview, chartPlaceholder);

        content.getChildren().addAll(title, statCards, bottomRow);
        return content;
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

    private VBox buildChartPlaceholder(String label) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(16));
        box.setPrefWidth(260);
        box.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label heading = new Label(label);
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label placeholder = new Label("[chart placeholder]");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(heading, placeholder);
        return box;
    }
}
