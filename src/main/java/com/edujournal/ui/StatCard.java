package com.edujournal.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// white boxes in dashboard

public class StatCard extends VBox {

    public StatCard(String label, String value, String iconResource) {
        setSpacing(8);
        setPadding(new Insets(16));
        setPrefWidth(160);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");

        StackPane iconBox = new StackPane(new ImageView(new Image(
                getClass().getResourceAsStream(iconResource), 40, 40, true, true)));
        iconBox.setPrefSize(64, 64);
        iconBox.setStyle("-fx-background-color: #DBEAFE; -fx-background-radius: 5;");

        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-text-fill: #6B7280;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        getChildren().addAll(nameLabel, iconBox, valueLabel);
    }
}
