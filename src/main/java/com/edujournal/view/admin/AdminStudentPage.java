package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.common.TopBar;
import com.edujournal.view.controller.AdminStudentController;
import com.edujournal.view.controller.AdminTeacherController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import com.edujournal.config.LocalizationManager;

public class AdminStudentPage extends BorderPane {

    private final Role role;

    public AdminStudentPage(VBox sidebar, Role role) {
        Main.setCurrentPageSupplier(() -> new AdminStudentPage(AdminSidebar.build("Students"), Role.ADMINISTRATOR));
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build(LocalizationManager.get("students"), role, false));

        AdminStudentController controller = new AdminStudentController(role);
        box.getChildren().add(controller);

        return box;
    }
}
