package com.edujournal.ui.teacher;

import com.edujournal.model.Assessments;
import com.edujournal.model.AssessmentType;
import com.edujournal.ui.common.CoursePage;
import com.edujournal.ui.common.TopBar;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TeacherAssessmentPage extends BorderPane {

    private static final String ACTIVE_TAB_STYLE =
            "-fx-text-fill: #2563EB; -fx-font-size: 14px; -fx-cursor: hand;";
    private static final String INACTIVE_TAB_STYLE =
            "-fx-text-fill: #6B7280; -fx-font-size: 14px; -fx-cursor: hand;";
    private static final String ACTIVE_UNDERLINE =
            "-fx-background-color: #2563EB; -fx-min-height: 2; -fx-max-height: 2;";
    private static final String INACTIVE_UNDERLINE =
            "-fx-background-color: transparent; -fx-min-height: 2; -fx-max-height: 2;";
    private static final String PANEL_STYLE =
            "-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 10;";
    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; -fx-border-color: transparent;";

    private final String[] TAB_NAMES = {"Grades", "Assessments", "Notes"};
    private final Label[] tabLabels = new Label[TAB_NAMES.length];
    private final Region[] tabUnderlines = new Region[TAB_NAMES.length];
    private final StackPane contentArea = new StackPane();
    private int activeTab = 0;

    private ComboBox<String> courseCombo;
    private ComboBox<String> groupCombo;

    public TeacherAssessmentPage() {
        this(0);
    }

    public TeacherAssessmentPage(int initialTab) {
        activeTab = initialTab;
        setLeft(TeacherSidebar.build("Gradebook"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(
                TopBar.build("Gradebook", "Teacher"),
                buildFilterRow(),
                buildTabs(),
                contentArea
        );
        updateTabContent();
        return content;
    }

    private HBox buildFilterRow() {
        courseCombo = new ComboBox<>();
        courseCombo.setPromptText("Choose the course");
        courseCombo.setPrefWidth(220);
        for (String[] c : CoursePage.COURSES)
            courseCombo.getItems().add(c[0]);

        groupCombo = new ComboBox<>();
        groupCombo.setPromptText("Group");
        groupCombo.setPrefWidth(120);
        groupCombo.getItems().addAll("TVT25K-O", "Group 2", "Group 3");

        courseCombo.setOnAction(e -> updateTabContent());
        groupCombo.setOnAction(e -> updateTabContent());

        HBox row = new HBox(12, courseCombo, groupCombo);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox buildTabs() {
        HBox tabRow = new HBox(0);
        tabRow.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < TAB_NAMES.length; i++) {
            final int idx = i;

            Label label = new Label(TAB_NAMES[i]);
            label.setStyle(i == activeTab ? ACTIVE_TAB_STYLE : INACTIVE_TAB_STYLE);
            label.setPadding(new Insets(0, 0, 8, 0));
            label.setOnMouseClicked(e -> selectTab(idx));

            Region underline = new Region();
            underline.setStyle(i == activeTab ? ACTIVE_UNDERLINE : INACTIVE_UNDERLINE);
            underline.setPrefWidth(80);

            VBox tabCell = new VBox(0, label, underline);
            tabCell.setPadding(new Insets(0, 24, 0, 0));

            tabLabels[i] = label;
            tabUnderlines[i] = underline;
            tabRow.getChildren().add(tabCell);
        }

        Separator sep = new Separator();
        sep.setMaxWidth(Double.MAX_VALUE);

        return new VBox(0, tabRow, sep);
    }

    private void selectTab(int idx) {
        activeTab = idx;
        for (int i = 0; i < TAB_NAMES.length; i++) {
            tabLabels[i].setStyle(i == idx ? ACTIVE_TAB_STYLE : INACTIVE_TAB_STYLE);
            tabUnderlines[i].setStyle(i == idx ? ACTIVE_UNDERLINE : INACTIVE_UNDERLINE);
        }
        updateTabContent();
    }

    private void updateTabContent() {
        contentArea.getChildren().clear();

        boolean courseSelected = courseCombo.getValue() != null;
        boolean groupSelected  = groupCombo.getValue() != null;

        if (!courseSelected || !groupSelected) {
            String action = TAB_NAMES[activeTab].toLowerCase();
            Label hint = new Label("Choose the course name and the group to view / add / delete " + action + ".");
            hint.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            StackPane.setAlignment(hint, Pos.TOP_LEFT);
            contentArea.getChildren().add(hint);
            return;
        }

        Node tabContent = switch (activeTab) {
            case 1  -> buildAssessmentsTab();
            default -> buildPlaceholder(TAB_NAMES[activeTab]);
        };
        StackPane.setAlignment(tabContent, Pos.TOP_LEFT);
        contentArea.getChildren().add(tabContent);
    }

    private Node buildAssessmentsTab() {
        boolean isFirst = courseCombo.getValue().equals(CoursePage.COURSES[0][0])
                && groupCombo.getValue().equals("TVT25K-O");

        ObservableList<Assessments> data = isFirst
                ? FXCollections.observableArrayList(
                        new Assessments(1, 1, "As1",   AssessmentType.INCLASSTASK, 100, 1.0, null),
                        new Assessments(2, 1, "As2",   AssessmentType.INCLASSTASK, 100, 1.0, null),
                        new Assessments(3, 1, "As3",   AssessmentType.HOMETASK,    100, 1.0, null),
                        new Assessments(4, 1, "Exam1", AssessmentType.EXAM1,       100, 1.0, null),
                        new Assessments(5, 1, "As4",   AssessmentType.INCLASSTASK, 100, 1.0, null),
                        new Assessments(6, 1, "As5",   AssessmentType.HOMETASK,    100, 1.0, null),
                        new Assessments(7, 1, "As6",   AssessmentType.INCLASSTASK, 100, 1.0, null),
                        new Assessments(8, 1, "Exam2", AssessmentType.EXAM2,       100, 1.0, null))
                : FXCollections.observableArrayList();

        TableColumn<Assessments, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));

        TableColumn<Assessments, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(typeLabel(d.getValue().getType())));

        TableView<Assessments> table = new TableView<>(data);
        table.getColumns().addAll(nameCol, typeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(320);
        table.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

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

        if (!isFirst) {
            Label placeholder = new Label("Data for \"" + courseCombo.getValue() + "\" will be loaded from DB");
            placeholder.setWrapText(true);
            placeholder.setMaxWidth(348);
            placeholder.setAlignment(javafx.geometry.Pos.CENTER);
            placeholder.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            table.setPlaceholder(placeholder);
        }

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

        ComboBox<Integer> posCombo = new ComboBox<>();
        for (int i = 1; i <= data.size() + 1; i++) posCombo.getItems().add(i);
        posCombo.setPromptText("Choose the position");
        posCombo.setMaxWidth(Double.MAX_VALUE);
        posCombo.setStyle(LIGHT_INPUT);

        Button saveBtn = new Button("Save");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle(BLUE_BTN);
        saveBtn.setOnAction(e -> {
            String name      = nameField.getText().trim();
            AssessmentType type = typeCombo.getValue();
            Integer pos      = posCombo.getValue();
            if (!name.isEmpty() && type != null && pos != null) {
                int nextId = data.size() + 1;
                data.add(pos - 1, new Assessments(nextId, 1, name, type, 100, 1.0, null));
                nameField.clear();
                typeCombo.setValue(null);
                posCombo.setValue(null);
                posCombo.getItems().add(data.size() + 1);
            }
        });

        VBox rightPanel = new VBox(20, title, nameField, typeCombo, posCombo, saveBtn);
        rightPanel.setPadding(new Insets(24));
        rightPanel.setStyle(PANEL_STYLE);
        rightPanel.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox layout = new HBox(16, leftPanel, rightPanel);
        layout.setAlignment(Pos.TOP_LEFT);
        return layout;
    }

    private String typeLabel(AssessmentType type) {
        return switch (type) {
            case INCLASSTASK -> "Assignment (inclass)";
            case HOMETASK    -> "Assignment (hometask)";
            default          -> type.name();
        };
    }

    private Node buildPlaceholder(String tab) {
        Label lbl = new Label(tab + " content goes here.");
        lbl.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
        return lbl;
    }
}
