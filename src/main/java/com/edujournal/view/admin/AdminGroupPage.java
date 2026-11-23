package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.common.*;
import com.edujournal.view.controller.AdminGroupController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.edujournal.config.LocalizationManager;

/**
 * Displays the academic-group management screen for administrators.
 */
public class AdminGroupPage extends BorderPane {
    private final Role role;

    /**
     * Creates the academic-group page.
     *
     * @param sidebar sidebar displayed beside the page content
     * @param role role used to configure group management
     */
    public AdminGroupPage(VBox sidebar, Role role) {
        Main.setCurrentPageSupplier(() -> new AdminGroupPage(AdminSidebar.build("Groups"), Role.ADMINISTRATOR));
        this.role = role;
        setLeft(sidebar);
        setCenter(RoleBackground.wrap(Role.ADMINISTRATOR, buildContent()));
    }

    private VBox buildContent()  {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build(LocalizationManager.get("academicGroups"), role, false));

        AdminGroupController controller = new AdminGroupController(role);

        box.getChildren().add(controller);

        return box;
    }
}
