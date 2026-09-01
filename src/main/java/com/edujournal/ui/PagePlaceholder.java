package com.edujournal.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Builds the placeholder center content shared by every skeleton page.
 */
public class PagePlaceholder {

    public static VBox build(String pageLabel, String roleTitle) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        Label title = new Label(pageLabel);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label placeholder = new Label("Here is the page of " + pageLabel + " (" + roleTitle + ").");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(title, placeholder);
        return box;
    }
}
