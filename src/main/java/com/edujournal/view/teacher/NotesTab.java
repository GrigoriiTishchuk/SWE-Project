package com.edujournal.view.teacher;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class NotesTab {

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";

    private static final Map<String, String> STORE = new HashMap<>();

    public static Node build(String course, String group) {
        String key = course + "|" + group;

        TextArea area = new TextArea(STORE.getOrDefault(key, ""));
        area.setPromptText("Type notes for " + course + " / " + group + "...");
        area.setWrapText(true);

        Label saved = new Label("Saved.");
        saved.setStyle("-fx-text-fill: #6B7280;");
        saved.setVisible(false);

        Button saveBtn = new Button("Save");
        saveBtn.setStyle(BLUE_BTN);
        saveBtn.setOnAction(e -> {
            STORE.put(key, area.getText());
            saved.setVisible(true);
        });

        area.textProperty().addListener((obs, old, val) -> saved.setVisible(false));

        HBox btnRow = new HBox(8, saved, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, area, btnRow);
        VBox.setVgrow(area, Priority.ALWAYS);
        return vbox;
    }
}
