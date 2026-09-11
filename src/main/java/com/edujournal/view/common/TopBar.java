package com.edujournal.view.common;

import com.edujournal.Main;
import com.edujournal.view.LoginPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// name, role, year

public class TopBar {

    public static HBox build(String title, String role) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> studyingYear = new ComboBox<>();
        studyingYear.getItems().addAll("2025/2026", "2024/2025", "2023/2024");
        studyingYear.setValue("2025/2026");

        Circle avatar = new Circle(20, Color.web("#9CA3AF"));

        Label name = new Label("Name Surname");
        name.setStyle("-fx-font-weight: bold;");
        Label roleLabel = new Label(role);
        roleLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        VBox userInfo = new VBox(2, name, roleLabel);
        userInfo.setAlignment(Pos.CENTER_LEFT);
        HBox userBox = new HBox(10, avatar, userInfo);
        userBox.setAlignment(Pos.CENTER);

        Button exitBtn = new Button("⏻");
        exitBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 30px; -fx-text-fill: #6B7280; -fx-cursor: hand;");
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 30px; -fx-text-fill: #EF4444; -fx-cursor: hand;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 30px; -fx-text-fill: #6B7280; -fx-cursor: hand;"));
        exitBtn.setOnAction(e -> Main.showPage(new LoginPage()));

        HBox rightBox = new HBox(16, studyingYear, userBox, exitBtn);
        rightBox.setAlignment(Pos.CENTER);

        HBox topBar = new HBox(16, titleLabel, spacer, rightBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 8, 0));
        return topBar;
    }
}
