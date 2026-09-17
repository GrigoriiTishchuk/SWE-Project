package com.edujournal.view;

import com.edujournal.entity.Role;
import com.edujournal.view.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PagePlaceholder {

    public static VBox build(String pageLabel, Role role) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        Label placeholder = new Label("Here is the page of " + pageLabel + " (" + role.getDisplayName() + ").");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(TopBar.build(pageLabel, role), placeholder);
        return box;
    }
}
