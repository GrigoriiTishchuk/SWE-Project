package com.edujournal.view.admin;

import com.edujournal.entity.Role;
import com.edujournal.view.common.TopBar;
import com.edujournal.view.controller.AdminStudentController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminStudentPage extends BorderPane {

    private final Role role;

    public AdminStudentPage() {
        this.role = Role.ADMINISTRATOR;
        setLeft(AdminSidebar.build("Students"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));
        box.getChildren().add(TopBar.build("Students", role, false));
        box.getChildren().add(new AdminStudentController(role));
        return box;
    }
}
