package com.edujournal.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A small card showing one number, e.g. "Students / 518".
 * Static only — the value is just hardcoded text, no real data.
 */
public class StatCard extends VBox {

    public StatCard(String label, String value) {
        setSpacing(4);
        setPadding(new Insets(16));
        setPrefWidth(160);
        setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-text-fill: #6B7280;");

        getChildren().addAll(nameLabel, valueLabel);
    }
}
