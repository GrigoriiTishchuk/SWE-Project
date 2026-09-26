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
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setOnAction(e -> {
            STORE.put(key, area.getText());
            saved.setVisible(true);
        });

        area.textProperty().addListener((obs, old, val) -> saved.setVisible(false));

        HBox btnRow = new HBox(8, saved, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, area, btnRow);
        vbox.getStylesheets().add(NotesTab.class.getResource("/css/button.css").toExternalForm());
        VBox.setVgrow(area, Priority.ALWAYS);
        return vbox;
    }
}
