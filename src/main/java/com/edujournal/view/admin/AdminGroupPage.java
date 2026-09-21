package com.edujournal.view.admin;

import com.edujournal.entity.Role;
import com.edujournal.view.PagePlaceholder;
import com.edujournal.view.common.TopBar;
import com.edujournal.view.controller.AdminGroupController;
import com.edujournal.view.controller.AdminTeacherController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminGroupPage extends BorderPane {
    private final Role role;

    public AdminGroupPage(VBox sidebar, Role role) {
        this.role = role;
        setLeft(sidebar);
        setCenter(buildContent());
    }

    private VBox buildContent()  {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build("Academic Groups", role));

        AdminGroupController controller = new AdminGroupController();
        box.getChildren().add(controller);

        return box;
    }
}
