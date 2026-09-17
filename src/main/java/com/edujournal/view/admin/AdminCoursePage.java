package com.edujournal.view.admin;

import com.edujournal.view.PagePlaceholder;
import com.edujournal.view.controller.CourseController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminCoursePage extends BorderPane {
    public AdminCoursePage() {
        setLeft(AdminSidebar.build("Courses"));
        VBox content = PagePlaceholder.build("Courses", "Administrator");
        content.getChildren().add(new CourseController());
        setCenter(content);
    }
}
