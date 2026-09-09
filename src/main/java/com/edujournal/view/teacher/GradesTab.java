package com.edujournal.view.teacher;

import com.edujournal.view.common.CoursePage;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class GradesTab {

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";

    private static final String[] HEADERS = {
        "As1", "As2", "As3", "Exam1", "As4", "As5", "As6", "Exam2"
    };

    // rows: name, scores[8], total, finalGrade
    private static final String[][] ROWS = {
        {"First One",     "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Second Two",    "0",  "25",  "0",   "39",  "50",  "30",  "35",  "40", "22", "1"},
        {"Third Three",   "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Fourth Four",   "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Fifth Five",    "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Sixth Six",     "100","100", "100", "100", "95",  "100", "100", "99", "99", "5"},
        {"Seventh Seven", "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Eighth Eight",  "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
        {"Ninth Nine",    "89", "80",  "95",  "70",  "100", "98",  "80",  "95", "87", "4"},
    };

    public static Node build(String course, String group) {
        boolean isFirst = course.equals(CoursePage.COURSES[0][0]) && group.equals("TVT25K-O");

        // Copy rows so edits don't mutate the static array
        String[][] data = isFirst ? new String[ROWS.length][] : new String[0][];
        for (int i = 0; i < data.length; i++) data[i] = ROWS[i].clone();

        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);
        table.getStylesheets().add(String.valueOf(GradesTab.class.getResource("/css/table.css")));

        if (!isFirst) {
            Label placeholder = new Label("Data for \"" + course + "\" / " + group + " will be loaded from DB.");
            placeholder.setWrapText(true);
            placeholder.setMaxWidth(348);
            placeholder.setAlignment(Pos.CENTER);
            placeholder.setTextAlignment(TextAlignment.CENTER);
            table.setPlaceholder(placeholder);
        }

        // # column
        TableColumn<String[], String> numCol = new TableColumn<>("#");
        numCol.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(table.getItems().indexOf(d.getValue()) + 1)));
        numCol.setEditable(false);
        numCol.setMaxWidth(35);
        numCol.setMinWidth(35);

        // Student column
        TableColumn<String[], String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        studentCol.setEditable(false);
        studentCol.setPrefWidth(130);

        table.getColumns().addAll(numCol, studentCol);

        // Score columns
        for (int i = 0; i < HEADERS.length; i++) {
            final int col = i + 1; // data[0] = name, data[1..8] = scores
            TableColumn<String[], String> scoreCol = new TableColumn<>(HEADERS[i]);
            scoreCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[col]));
            scoreCol.setCellFactory(TextFieldTableCell.forTableColumn());
            scoreCol.setOnEditCommit(e -> e.getRowValue()[col] = e.getNewValue());
            table.getColumns().add(scoreCol);
        }

        // Total (Weighted) column
        TableColumn<String[], String> totalCol = new TableColumn<>("Total\n(Weighted)");
        totalCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[9]));
        totalCol.setEditable(false);

        // Final Grade column
        TableColumn<String[], String> gradeCol = new TableColumn<>("Final\nGrade");
        gradeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[10]));
        gradeCol.setEditable(false);

        table.getColumns().addAll(totalCol, gradeCol);
        table.getItems().addAll(List.of(data));

        boolean[] editing = {false};
        Button fixSave = new Button("Fix / Save Changes");
        fixSave.setStyle(BLUE_BTN);
        fixSave.setOnAction(e -> {
            editing[0] = !editing[0];
            table.setEditable(editing[0]);
            fixSave.setText(editing[0] ? "Save Changes" : "Fix / Save Changes");
        });

        HBox btnRow = new HBox(fixSave);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, btnRow, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
}
