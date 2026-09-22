package com.edujournal.view.teacher;

import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.model.AssessmentsDTO;

import java.util.List;
import java.util.Optional;

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
            "-fx-background-color: white; -fx-border-color: #E5E7EB; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8;";

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 10;";

    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; " +
                    "-fx-border-color: transparent;";

    public static Node build(String course, String group) {


        AssessmentsService assessmentsService =
                new AssessmentsService();

        ObservableList<AssessmentsDTO> data =
                FXCollections.observableArrayList();

        // --- Load assessments from DB ---
            List<Assessments> assessments =
                    assessmentsService.getByCourseName(course);

            for (Assessments assessment : assessments) {

                data.add(
                        new AssessmentsDTO(
                                assessment.getId(),
                                assessment.getCourseId(),
                                assessment.getTitle(),
                                assessment.getType(),
                                assessment.getMaxScore(),
                                assessment.getWeight(),
                                assessment.getDueDate()
                        )
                );
            }


        // --- Table columns ---
        TableColumn<AssessmentsDTO, String> nameCol =
                new TableColumn<>("Name");

        nameCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        d.getValue().getTitle()
                )
        );

        TableColumn<AssessmentsDTO, String> typeCol =
                new TableColumn<>("Type");

        typeCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        d.getValue().getType().name()
                )
        );

        TableColumn<AssessmentsDTO, String> weightCol =
                new TableColumn<>("Weight");

        weightCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        String.valueOf(
                                d.getValue().getWeight()
                        )
                )
        );

        nameCol.setPrefWidth(160);
        typeCol.setPrefWidth(160);
        weightCol.setPrefWidth(60);

        // --- Table ---
        TableView<AssessmentsDTO> table =
                new TableView<>(data);

        table.getColumns().addAll(
                List.of(
                        nameCol,
                        typeCol,
                        weightCol
                )
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(320);

        table.getStylesheets().add(
                String.valueOf(
                        AssessmentsTab.class.getResource(
                                "/css/table.css"
                        )
                )
        );



            Label placeholder =
                    new Label(
                            "Data for \"" + course +
                                    "\" will be loaded from DB"
                    );

            placeholder.setWrapText(true);
            placeholder.setMaxWidth(348);
            placeholder.setAlignment(Pos.CENTER);

            placeholder.setTextAlignment(
                    javafx.scene.text.TextAlignment.CENTER
            );

            table.setPlaceholder(placeholder);


        // --- Total row ---
        HBox totalRow = new HBox();

        totalRow.setPadding(
                new Insets(6, 8, 6, 8)
        );

        totalRow.setStyle(
                "-fx-background-color: #F3F4F6; " +
                        "-fx-border-color: #E5E7EB; " +
                        "-fx-border-radius: 4;"
        );

        Label totalName =
                new Label("Total (Weighted)");

        Label totalType = new Label();
        totalType.setStyle("-fx-text-fill: #6B7280;");

        Runnable updateTotal = () -> {
            double sum = data.stream().mapToDouble(AssessmentsDTO::getWeight).sum();
            totalType.setText(String.format("%.2f", sum));
        };
        updateTotal.run();
        data.addListener((javafx.collections.ListChangeListener<AssessmentsDTO>) c -> updateTotal.run());

        Region totalSpacer =
                new Region();

        HBox.setHgrow(
                totalSpacer,
                Priority.ALWAYS
        );

        totalRow.getChildren().addAll(
                totalName,
                totalSpacer,
                totalType
        );

        VBox leftPanel =
                new VBox(
                        12,
                        table,
                        totalRow
                );

        leftPanel.setPadding(
                new Insets(16)
        );

        leftPanel.setStyle(
                PANEL_STYLE
        );

        leftPanel.setPrefWidth(450);

        // --- Form ---
        Label title =
                new Label("Add assessment");

        title.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Type the name"
        );

        nameField.setStyle(LIGHT_INPUT);
        nameField.setMaxWidth(
                Double.MAX_VALUE
        );

        ComboBox<AssessmentType> typeCombo =
                new ComboBox<>();

        typeCombo.getItems().addAll(
                AssessmentType.values()
        );

        typeCombo.setPromptText(
                "Choose the type"
        );

        typeCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        typeCombo.setStyle(LIGHT_INPUT);

        TextField weightField =
                new TextField();

        weightField.setPromptText(
                "Type the weight (e.g. 0.1)"
        );

        weightField.setStyle(LIGHT_INPUT);
        weightField.setMaxWidth(
                Double.MAX_VALUE
        );

        ComboBox<Integer> posCombo =
                new ComboBox<>();

        for (int i = 1; i <= data.size() + 1; i++) {
            posCombo.getItems().add(i);
        }

        posCombo.setPromptText(
                "Choose the position"
        );

        posCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        posCombo.setStyle(LIGHT_INPUT);

        Button saveBtn =
                new Button("Save");

        saveBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        saveBtn.setStyle(BLUE_BTN);

        Button updateBtn =
                new Button("Update");

        updateBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        updateBtn.setStyle(BLUE_BTN);

        updateBtn.setVisible(false);

        final AssessmentsDTO[] selectedAssessment =
                new AssessmentsDTO[1];

        // --- Actions column ---
        TableColumn<AssessmentsDTO, Void> actionCol =
                new TableColumn<>("Actions");

        actionCol.setPrefWidth(100);

        actionCol.setCellFactory(col ->
                new TableCell<>() {

                    private final Button editBtn =
                            new Button("✏");

                    private final Button deleteBtn =
                            new Button("🗑");

                    {
                        deleteBtn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: white; -fx-background-radius: 6;");
                        editBtn.setOnAction(e -> {

                            AssessmentsDTO selected =
                                    getTableView()
                                            .getItems()
                                            .get(getIndex());

                            selectedAssessment[0] =
                                    selected;

                            nameField.setText(
                                    selected.getTitle()
                            );

                            typeCombo.setValue(
                                    selected.getType()
                            );

                            weightField.setText(
                                    String.valueOf(
                                            selected.getWeight()
                                    )
                            );

                            title.setText(
                                    "Edit assessment"
                            );

                            saveBtn.setVisible(false);
                            updateBtn.setVisible(true);

                            System.out.println(
                                    "Editing ID: "
                                            + selected.getId()
                                            + " - "
                                            + selected.getTitle()
                            );
                        });

                        deleteBtn.setOnAction(e -> {

                            AssessmentsDTO selected =
                                    getTableView()
                                            .getItems()
                                            .get(getIndex());

                            Alert confirm =
                                    new Alert(
                                            Alert.AlertType.CONFIRMATION
                                    );

                            confirm.setTitle("Delete Assessment");
                            confirm.setHeaderText(
                                    "Delete this assessment?"
                            );

                            confirm.setContentText(
                                    selected.getTitle()
                            );

                            Optional<ButtonType> result =
                                    confirm.showAndWait();

                            if (result.isEmpty()
                                    || result.get() != ButtonType.OK) {
                                return;
                            }

                            System.out.println(
                                    "Delete ID: "
                                            + selected.getId()
                                            + " - "
                                            + selected.getTitle()
                            );

                            Assessments assessment =
                                    assessmentsService.findById(
                                            selected.getId()
                                    );

                            if (assessmentsService.hasGrades(selected.getId())) {
                                Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                                warning.setTitle("Delete Assessment");
                                warning.setHeaderText("This assessment has grades.");
                                warning.setContentText("All associated grades will also be deleted. Delete anyway?");

                                ButtonType yes = new ButtonType("Delete anyway", ButtonBar.ButtonData.OK_DONE);
                                ButtonType no = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                                warning.getButtonTypes().setAll(yes, no);

                                if (warning.showAndWait().orElse(no) != yes) return;

                                com.edujournal.dao.GradesDAO gradesDAO = new com.edujournal.dao.GradesDAO();
                                gradesDAO.findByAssessment(selected.getId())
                                        .forEach(g -> gradesDAO.deleteById(g.getId()));
                            }

                            assessmentsService.delete(assessment);

                            data.remove(selected);
                        });
                    }

                    @Override
                    protected void updateItem(
                            Void item,
                            boolean empty) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty) {
                            setGraphic(null);
                        } else {

                            HBox buttons = new HBox(
                                    5,
                                    editBtn,
                                    deleteBtn
                            );

                            setGraphic(buttons);
                        }

                    }
                }
        );

        table.getColumns().add(actionCol);

        // --- Save ---
        saveBtn.setOnAction(e -> {

            String name =
                    nameField.getText().trim();

            AssessmentType type =
                    typeCombo.getValue();

            Integer pos =
                    posCombo.getValue();

            double weight;

            try {
                weight = Double.parseDouble(
                        weightField
                                .getText()
                                .trim()
                );
            } catch (NumberFormatException ex) {
                weight = 1.0;
            }

            if (name.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter the assessment name.").showAndWait();
                return;
            }
            if (type == null) {
                new Alert(Alert.AlertType.WARNING, "Please choose the assessment type.").showAndWait();
                return;
            }
            if (pos == null) {
                new Alert(Alert.AlertType.WARNING, "Please choose the position.").showAndWait();
                return;
            }

            Assessments assessment = new Assessments();

            Course courseEntity = assessmentsService.getCourseByName(course);

            if (courseEntity == null) {
                new Alert(Alert.AlertType.ERROR, "Course not found.").showAndWait();
                return;
            }

                assessment.setCourseId(
                        courseEntity.getId()
                );

                assessment.setTitle(name);

                assessment.setType(type);

                assessment.setMaxScore(
                        100.0
                );

                assessment.setWeight(
                        weight
                );

                assessment.setDueDate(
                        null
                );

                assessmentsService.save(
                        assessment
                );

                data.add(
                        pos - 1,
                        new AssessmentsDTO(
                                assessment.getId(),
                                assessment.getCourseId(),
                                assessment.getTitle(),
                                assessment.getType(),
                                assessment.getMaxScore(),
                                assessment.getWeight(),
                                assessment.getDueDate()
                        )
                );

                nameField.clear();
                typeCombo.setValue(null);
                weightField.clear();
                posCombo.setValue(null);

                posCombo.getItems().clear();

                for (int i = 1;
                     i <= data.size() + 1;
                     i++) {

                    posCombo.getItems().add(i);
                }
        });

        // --- Update ---
        updateBtn.setOnAction(e -> {

            AssessmentsDTO selected =
                    selectedAssessment[0];

            if (selected == null) {
                return;
            }

            String name =
                    nameField.getText().trim();

            AssessmentType type =
                    typeCombo.getValue();

            double weight;

            try {
                weight = Double.parseDouble(
                        weightField
                                .getText()
                                .trim()
                );
            } catch (NumberFormatException ex) {
                weight = 1.0;
            }

            if (name.isEmpty()
                    || type == null) {
                return;
            }

            Assessments assessment =
                    assessmentsService.findById(
                            selected.getId()
                    );

            if (assessment == null) {
                return;
            }

            assessment.setTitle(name);
            assessment.setType(type);
            assessment.setWeight(weight);

            assessmentsService.update(
                    assessment
            );

            int index =
                    data.indexOf(selected);

            AssessmentsDTO updated =
                    new AssessmentsDTO(
                            assessment.getId(),
                            assessment.getCourseId(),
                            assessment.getTitle(),
                            assessment.getType(),
                            assessment.getMaxScore(),
                            assessment.getWeight(),
                            assessment.getDueDate()
                    );

            data.set(
                    index,
                    updated
            );

            nameField.clear();
            typeCombo.setValue(null);
            weightField.clear();

            selectedAssessment[0] = null;

            title.setText(
                    "Add assessment"
            );

            updateBtn.setVisible(false);
            saveBtn.setVisible(true);

            System.out.println(
                    "Updated ID: "
                            + assessment.getId()
            );
        });

        VBox rightPanel =
                new VBox(
                        20,
                        title,
                        nameField,
                        typeCombo,
                        weightField,
                        posCombo,
                        saveBtn,
                        updateBtn
                );

        rightPanel.setPadding(
                new Insets(24)
        );

        rightPanel.setStyle(
                PANEL_STYLE
        );

        rightPanel.setAlignment(
                Pos.TOP_LEFT
        );

        HBox.setHgrow(
                rightPanel,
                Priority.ALWAYS
        );

        HBox layout =
                new HBox(
                        16,
                        leftPanel,
                        rightPanel
                );

        layout.setAlignment(
                Pos.TOP_LEFT
        );
        return layout;
    }
}