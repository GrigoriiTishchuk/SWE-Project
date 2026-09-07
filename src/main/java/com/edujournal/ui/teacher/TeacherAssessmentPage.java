package com.edujournal.ui.teacher;

import com.edujournal.ui.common.TopBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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

    private final String[] TAB_NAMES = {"Grades", "Assessments", "Notes"};
    private final Label[] tabLabels = new Label[TAB_NAMES.length];
    private final Region[] tabUnderlines = new Region[TAB_NAMES.length];
    private final StackPane contentArea = new StackPane();
    private int activeTab = 0; // Grades selected by default

    private ComboBox<String> courseCombo;
    private ComboBox<String> groupCombo;

    public TeacherAssessmentPage() {
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
        courseCombo.getItems().addAll("Course A", "Course B", "Course C");

        groupCombo = new ComboBox<>();
        groupCombo.setPromptText("Group");
        groupCombo.setPrefWidth(120);
        groupCombo.getItems().addAll("Group 1", "Group 2", "Group 3");

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
            HBox.setHgrow(underline, Priority.NEVER);
            underline.setPrefWidth(label.getPrefWidth());

            VBox tabCell = new VBox(0, label, underline);
            tabCell.setPadding(new Insets(0, 24, 0, 0));

            tabLabels[i] = label;
            tabUnderlines[i] = underline;
            tabRow.getChildren().add(tabCell);
        }

        Separator sep = new Separator();
        sep.setMaxWidth(Double.MAX_VALUE);

        VBox tabs = new VBox(0, tabRow, sep);
        return tabs;
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
        boolean groupSelected = groupCombo.getValue() != null;

        if (!courseSelected || !groupSelected) {
            String action = TAB_NAMES[activeTab].toLowerCase();
            Label hint = new Label(
                    "Choose the course name and the group to view / add / delete " + action + ".");
            hint.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            StackPane.setAlignment(hint, Pos.TOP_LEFT);
            contentArea.getChildren().add(hint);
        } else {
            Label placeholder = new Label(TAB_NAMES[activeTab] + " content goes here.");
            placeholder.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            contentArea.getChildren().add(placeholder);
        }
    }
}
