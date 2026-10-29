package com.edujournal.view.common;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.view.admin.AdminSidebar;
import com.edujournal.view.controller.CourseController;
import com.edujournal.view.teacher.TeacherSidebar;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CoursePage extends BorderPane {
    private final Role role;

    public CoursePage(VBox sidebar, Role role) {
        Main.setCurrentPageSupplier(() -> new CoursePage(buildSidebar(role), role));
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private static VBox buildSidebar(Role role) {
        return switch (role) {
            case ADMINISTRATOR -> AdminSidebar.build("Courses");
            default -> TeacherSidebar.build("Courses");
        };
    }

    private VBox buildContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        CourseController controller = new CourseController(role);

        box.getChildren().add(TopBar.build(LocalizationManager.get("courses"), role, true, controller::setYear));
        box.getChildren().add(controller);

        return box;
    }
}
