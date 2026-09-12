package com.edujournal.view.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OwnProfilePage extends BorderPane {

    private static final String PANEL =
            "-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";
    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; -fx-border-color: transparent;";
    private static final String READONLY_INPUT =
            "-fx-background-color: #F3F4F6; -fx-background-radius: 6; -fx-border-color: transparent; -fx-text-fill: #374151;";

    public OwnProfilePage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(buildContent(role));
    }

    private VBox buildContent(String role) {
        HBox body = new HBox(16, buildAvatarCard(role), buildFormCard(role));
        body.setAlignment(Pos.TOP_LEFT);

        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Own Profile", role), body);
        return content;
    }

    private VBox buildAvatarCard(String role) {
        Circle avatar = new Circle(40, Color.web("#9CA3AF"));

        Label name = new Label("Name Surname");
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label badge = new Label(role);
        badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1a3a6b; "
                + "-fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 12;");

        VBox card = new VBox(14, avatar, name, badge);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28));
        card.setStyle(PANEL);
        card.setPrefWidth(200);
        return card;
    }

    private VBox buildFormCard(String role) {
        Label title = new Label("Personal Information");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox form = new VBox(12, title,
                row("First Name", "Name", false),
                row("Last Name", "Surname", false),
                row("Email", "name.surname@metropolia.fi", false),
                row("Phone", "+358 00 000 0000", false)
        );

        if (role.equals("Student")) {
            form.getChildren().addAll(
                    row("Student ID", "e.g. 2300001", false),
                    row("Group", "e.g. TVT25K", false)
            );
        } else if (role.equals("Teacher")) {
            form.getChildren().add(row("Department", "e.g. ICT", false));
        }

        form.getChildren().add(row("Role", role, true));

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(BLUE_BTN);
        form.getChildren().add(saveBtn);

        form.setPadding(new Insets(24));
        form.setStyle(PANEL);
        HBox.setHgrow(form, Priority.ALWAYS);
        return form;
    }

    private VBox row(String label, String prompt, boolean readOnly) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");

        TextField tf = new TextField(readOnly ? prompt : "");
        tf.setPromptText(readOnly ? "" : prompt);
        tf.setEditable(!readOnly);
        tf.setStyle(readOnly ? READONLY_INPUT : LIGHT_INPUT);
        tf.setMaxWidth(Double.MAX_VALUE);

        return new VBox(4, lbl, tf);
    }
}
