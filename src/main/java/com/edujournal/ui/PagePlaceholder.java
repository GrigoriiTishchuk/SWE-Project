package com.edujournal.ui;

import com.edujournal.ui.common.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PagePlaceholder {

    public static VBox build(String pageLabel, String roleTitle) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        Label placeholder = new Label("Here is the page of " + pageLabel + " (" + roleTitle + ").");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(TopBar.build(pageLabel, roleTitle), placeholder);
        return box;
    }
}
