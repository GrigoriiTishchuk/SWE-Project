package com.edujournal.ui.common;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CoursePage extends BorderPane {

    public static final String[][] COURSES = {
        {"Software Engineering Project 1", "TXK3000-112"},
        {"Software Engineering Project 2", "TXK3000-113"},
        {"WEB-Project",                    "TXK3000-105"}
    };

    private final String role;

    public CoursePage(VBox sidebar, String role) {
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build("Courses", role));

        for (String[] course : COURSES) {
            Label name = new Label(course[0]);
            name.setStyle("-fx-font-size: 14px;");
            Label code = new Label(course[1]);
            code.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

            VBox text = new VBox(2, name, code);
            HBox row = new HBox(text);
            row.setPadding(new Insets(10, 12, 10, 12));
            row.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                    + "-fx-border-radius: 6; -fx-background-radius: 6;");
            box.getChildren().add(row);
        }

        return box;
    }
}
