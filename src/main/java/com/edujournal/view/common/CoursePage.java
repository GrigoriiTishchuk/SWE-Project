package com.edujournal.view.common;

import com.edujournal.entity.Role;
import com.edujournal.view.controller.CourseController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CoursePage extends BorderPane {
    private final Role role;

    public CoursePage(VBox sidebar, Role role) {
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build("Courses", role));

        CourseController controller = new CourseController(role);
        box.getChildren().add(controller);



        return box;
    }
}
