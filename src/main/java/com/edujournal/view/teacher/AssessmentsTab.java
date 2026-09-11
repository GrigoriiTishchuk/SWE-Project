package com.edujournal.view.teacher;

import com.edujournal.entity.Assessments;
import com.edujournal.entity.AssessmentType;
import com.edujournal.model.AssessmentsDTO;
import com.edujournal.view.common.CoursePage;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AssessmentsTab {

    private static final String PANEL_STYLE =
            "-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 10;";
    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; -fx-border-color: transparent;";

    public static Node build(String course, String group) {
        boolean isFirst = course.equals(CoursePage.COURSES[0][0]) && group.equals("TVT25K-O");

        ObservableList<AssessmentsDTO> data = isFirst
                ? FXCollections.observableArrayList(
                        new AssessmentsDTO(1, "As1",   AssessmentType.INCLASSTASK, 100.0, 0.1, null),
                        new AssessmentsDTO(1, "As2",   AssessmentType.INCLASSTASK, 100.0, 0.1, null),
                        new AssessmentsDTO(1, "As3",   AssessmentType.HOMETASK,    100.0, 0.1, null),
                        new AssessmentsDTO(1, "Exam1", AssessmentType.EXAM1,       100.0, 0.2, null),
                        new AssessmentsDTO(1, "As4",   AssessmentType.INCLASSTASK, 100.0, 0.1, null),
                        new AssessmentsDTO(1, "As5",   AssessmentType.HOMETASK,    100.0, 0.1, null),
                        new AssessmentsDTO(1, "As6",   AssessmentType.INCLASSTASK, 100.0, 0.1, null),
                        new AssessmentsDTO(1, "Exam2", AssessmentType.EXAM2,       100.0, 0.2, null))
                : FXCollections.observableArrayList();

        // --- Table ---
        TableColumn<AssessmentsDTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));

        TableColumn<AssessmentsDTO, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(typeLabel(d.getValue().getType())));

        TableColumn<AssessmentsDTO, String> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getWeight())));

        TableView<AssessmentsDTO> table = new TableView<>(data);
        table.getColumns().addAll(List.of(nameCol, typeCol, weightCol));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(320);
        table.getStylesheets().add(String.valueOf(AssessmentsTab.class.getResource("/css/table.css")));

        if (!isFirst) {
            Label placeholder = new Label("Data for \"" + course + "\" will be loaded from DB");
            placeholder.setWrapText(true);
            placeholder.setMaxWidth(348);
            placeholder.setAlignment(Pos.CENTER);
            placeholder.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            table.setPlaceholder(placeholder);
        }

        // --- Total row ---
        HBox totalRow = new HBox();
        totalRow.setPadding(new Insets(6, 8, 6, 8));
        totalRow.setStyle("-fx-background-color: #F3F4F6; -fx-border-color: #E5E7EB; -fx-border-radius: 4;");
        Label totalName = new Label("Total (Weighted)");
        Label totalType = new Label("100%");
        totalType.setStyle("-fx-text-fill: #6B7280;");
        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);
        totalRow.getChildren().addAll(totalName, totalSpacer, totalType);

        Button fixSave = new Button("Fix / Save");
        fixSave.setMaxWidth(Double.MAX_VALUE);
        fixSave.setStyle(BLUE_BTN);

        VBox leftPanel = new VBox(12, table, totalRow, fixSave);
        leftPanel.setPadding(new Insets(16));
        leftPanel.setStyle(PANEL_STYLE);
        leftPanel.setPrefWidth(380);

        // --- Add form ---
        Label title = new Label("Add assessment");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Type the name");
        nameField.setStyle(LIGHT_INPUT);
        nameField.setMaxWidth(Double.MAX_VALUE);

        ComboBox<AssessmentType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(AssessmentType.values());
        typeCombo.setPromptText("Choose the type");
        typeCombo.setMaxWidth(Double.MAX_VALUE);
        typeCombo.setStyle(LIGHT_INPUT);

        TextField weightField = new TextField();
        weightField.setPromptText("Type the weight (e.g. 0.1)");
        weightField.setStyle(LIGHT_INPUT);
        weightField.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Integer> posCombo = new ComboBox<>();
        for (int i = 1; i <= data.size() + 1; i++) posCombo.getItems().add(i);
        posCombo.setPromptText("Choose the position");
        posCombo.setMaxWidth(Double.MAX_VALUE);
        posCombo.setStyle(LIGHT_INPUT);

        Button saveBtn = new Button("Save");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle(BLUE_BTN);
        saveBtn.setOnAction(e -> {
            String name         = nameField.getText().trim();
            AssessmentType type = typeCombo.getValue();
            Integer pos         = posCombo.getValue();
            double weight;
            try {
                weight = Double.parseDouble(weightField.getText().trim());
            } catch (NumberFormatException ex) {
                weight = 1.0;
            }
            if (!name.isEmpty() && type != null && pos != null) {
                data.add(pos - 1, new AssessmentsDTO(1, name, type, 100.0, weight, null));
                nameField.clear();
                typeCombo.setValue(null);
                weightField.clear();
                posCombo.setValue(null);
                posCombo.getItems().add(data.size() + 1);
            }
        });

        VBox rightPanel = new VBox(20, title, nameField, typeCombo, weightField, posCombo, saveBtn);
        rightPanel.setPadding(new Insets(24));
        rightPanel.setStyle(PANEL_STYLE);
        rightPanel.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox layout = new HBox(16, leftPanel, rightPanel);
        layout.setAlignment(Pos.TOP_LEFT);
        return layout;
    }

    private static String typeLabel(AssessmentType type) {
        return switch (type) {
            case INCLASSTASK -> "Assignment (inclass)";
            case HOMETASK    -> "Assignment (hometask)";
            default          -> type.name();
        };
    }
}
