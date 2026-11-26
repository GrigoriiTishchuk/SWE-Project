package com.edujournal.view.common;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Builds a simple placeholder card for a chart that is not yet available.
 */
public class ChartPlaceholder {

    private ChartPlaceholder() {
    }

    /**
     * Creates a card with the supplied chart title.
     *
     * @param title title displayed above the placeholder
     * @return the chart placeholder container
     */
    public static VBox build(String title) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(16));
        box.setPrefWidth(260);
        box.getStyleClass().add("card");

        Label heading = new Label(title);
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label placeholder = new Label("[chart placeholder]");
        placeholder.setStyle("-fx-text-fill: #6B7280;");

        box.getChildren().addAll(heading, placeholder);
        return box;
    }
}
