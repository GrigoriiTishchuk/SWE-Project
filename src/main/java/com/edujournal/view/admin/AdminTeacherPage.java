package com.edujournal.view.admin;

import com.edujournal.entity.Role;
import com.edujournal.view.common.TopBar;
import com.edujournal.view.controller.AdminTeacherController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// placeholder only

public class AdminTeacherPage extends BorderPane {
    private final Role role;

    public AdminTeacherPage(VBox sidebar, Role role) {
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private VBox buildContent()  {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build("Teachers", role));

        AdminTeacherController controller = new AdminTeacherController(role);
        box.getChildren().add(controller);

        return box;
    }
}
