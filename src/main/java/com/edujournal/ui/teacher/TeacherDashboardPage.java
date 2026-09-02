package com.edujournal.ui.teacher;

import com.edujournal.ui.StatCard;
import com.edujournal.ui.common.ChartPlaceholder;
import com.edujournal.ui.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Everything is hardcoded

public class TeacherDashboardPage extends BorderPane {

    public TeacherDashboardPage() {
        setLeft(TeacherSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Dashboard", "Teacher"), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildStatCards() {
        return new HBox(16,
                new StatCard("Courses",     "18",  "/images/course_icon.png"),
                new StatCard("Groups",      "18",  "/images/group_icon.png"),
                new StatCard("Students",    "515", "/images/student_icon.png"),
                new StatCard("Assessments", "202", "/images/assessement_icon.png")
        );
    }

    private HBox buildBottomRow() {
        return new HBox(16, buildQuickActions(), ChartPlaceholder.build("My Courses Average Grade"));
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

        HBox row1 = new HBox(8, new Button("Add assessment"), new Button("View all courses"));
        HBox row2 = new HBox(8, new Button("Add grade"),      new Button("View all groups"));
        HBox row3 = new HBox(8, new Button("Generate Group Report"));

        box.getChildren().addAll(heading, search, row1, row2, row3);
        return box;
    }
}
