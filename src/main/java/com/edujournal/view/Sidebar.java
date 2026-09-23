package com.edujournal.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

// left navigation sidebar for every role's pages.

public class Sidebar {

    public sealed interface Item permits NavItem, SectionHeader {}
    public record NavItem(String label, boolean active, Runnable onClick) implements Item {}
    public record SectionHeader(String label) implements Item {}

    public static VBox build(Item... items) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(20, 12, 20, 12));
        box.setPrefWidth(220);
        box.setStyle("-fx-background-color: #1b2a4a;");

        ImageView logo = new ImageView(new Image(
                Sidebar.class.getResourceAsStream("/images/edujournal_logo_white.png"), 140, 40, true, true));
        VBox.setMargin(logo, new Insets(16, 0, 10, 0));
        box.getChildren().add(logo);

        Separator divider = new Separator();
        VBox.setMargin(divider, new Insets(20, 0, 20, 0));
        box.getChildren().add(divider);

        for (Item item : items) {
            if (item instanceof SectionHeader h) {
                Label header = new Label(h.label());
                header.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px;");
                VBox.setMargin(header, new Insets(12, 0, 4, 4));
                box.getChildren().add(header);
            } else if (item instanceof NavItem n) {
                Button button = new Button(n.label());
                button.setMaxWidth(Double.MAX_VALUE);
                button.setStyle(navButtonStyle(n.active()));
                if (!n.active()) {
                    button.setOnMouseEntered(e -> button.setStyle(navButtonStyle(true)));
                    button.setOnMouseExited(e -> button.setStyle(navButtonStyle(false)));
                }
                button.setOnAction(e -> n.onClick().run());
                box.getChildren().add(button);
            }
        }

        return box;
    }

    private static String navButtonStyle(boolean active) {
        String base = "-fx-alignment: CENTER_LEFT; -fx-padding: 8 12 8 12;";
        return active
                ? base + " -fx-text-fill: white; -fx-background-color: #2F6FED; -fx-background-radius: 6;"
                : base + " -fx-text-fill: #e2e8f0; -fx-background-color: transparent;";
    }
}
