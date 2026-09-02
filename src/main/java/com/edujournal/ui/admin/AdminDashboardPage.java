package com.edujournal.ui.admin;

import com.edujournal.ui.StatCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// Everything is hardcoded

public class AdminDashboardPage extends BorderPane {

    public AdminDashboardPage() {
        setLeft(AdminSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        content.getChildren().addAll(buildTopBar(), buildStatCards(), buildBottomRow());
        return content;
    }

    private HBox buildTopBar() {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> studyingYear = new ComboBox<>();
        studyingYear.getItems().addAll("2025/2026", "2024/2025", "2023/2024");
        studyingYear.setValue("2025/2026");

        Circle avatar = new Circle(20, Color.web("#9CA3AF"));

        Label name = new Label("Name Surname");
        name.setStyle("-fx-font-weight: bold;");
        Label role = new Label("Administrator");
        role.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        VBox userInfo = new VBox(2, name, role);
        HBox userBox = new HBox(10, avatar, userInfo);
        userBox.setAlignment(Pos.CENTER_LEFT);

        HBox topBar = new HBox(16, title, spacer, studyingYear, userBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
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
