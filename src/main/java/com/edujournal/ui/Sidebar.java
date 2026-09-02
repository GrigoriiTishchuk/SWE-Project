package com.edujournal.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

// left navigation sidebar for every role's pages.

public class Sidebar {

    public record NavItem(String label, boolean active, Runnable onClick) {}

    public static VBox build(String roleTitle, NavItem... items) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(20, 12, 20, 12));
        box.setPrefWidth(220);
        box.setStyle("-fx-background-color: #1B2A4A;");

        Label brand = new Label("EduJournal");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label role = new Label(roleTitle);
        role.setStyle("-fx-text-fill: #AEC2EA; -fx-font-size: 12px;");

        box.getChildren().addAll(brand, role, new Separator());

        for (NavItem item : items) {
            Button button = new Button(item.label());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setStyle(navButtonStyle(item.active()));
            button.setOnAction(e -> item.onClick().run());
            box.getChildren().add(button);
        }

        return box;
    }

    private static String navButtonStyle(boolean active) {
        String base = "-fx-alignment: CENTER_LEFT; -fx-text-fill: white; -fx-padding: 8 12 8 12;";
        return active
                ? base + " -fx-background-color: #2F6FED; -fx-background-radius: 6;"
                : base + " -fx-background-color: transparent;";
    }
}
