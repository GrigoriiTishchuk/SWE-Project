package com.edujournal.ui.admin;

import com.edujournal.ui.StatCard;
import com.edujournal.ui.common.TopBar;
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
        return new HBox(16,
                new StatCard("Students", "518", "/images/student_icon.png"),
                new StatCard("Teachers", "12",  "/images/teacher_icon.png"),
                new StatCard("Courses",  "32",  "/images/course_icon.png"),
                new StatCard("Groups",   "166", "/images/group_icon.png")
        );
    }

    private HBox buildBottomRow() {
        return new HBox(16, buildQuickActions(), buildChartPlaceholder());
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

        HBox addButtons = new HBox(8,
                new Button("Add student"),
                new Button("Add teacher"),
                new Button("Add course"),
                new Button("Add group")
        );
        HBox viewButtons = new HBox(8,
                new Button("View all students"),
                new Button("View all teachers"),
                new Button("View all courses"),
                new Button("View all groups")
        );

        box.getChildren().addAll(heading, search, addButtons, viewButtons);
        return box;
    }

    private VBox buildChartPlaceholder() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(16));
        box.setPrefWidth(260);
        box.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label heading = new Label("Average Grade");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label placeholder = new Label("[chart placeholder]");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(heading, placeholder);
        return box;
    }
}
