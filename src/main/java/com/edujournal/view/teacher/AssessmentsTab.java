package com.edujournal.view.teacher;

import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.config.LocalizationManager;
import com.edujournal.dao.LocalizationDAO;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Course;
import com.edujournal.model.AssessmentsDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

/**
 * Builds the teacher gradebook tab for managing course assessments.
 */
public class AssessmentsTab {

    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; " +
                    "-fx-border-color: transparent;";

    private AssessmentsTab() {}

    /**
     * Creates the assessment list and controls for a selected course and optional group.
     *
     * @param course selected course name
     * @return assessment management view
     */
    public static Node build(String course) {


        AssessmentsService assessmentsService =
                new AssessmentsService();

        String lang = LocalizationManager.getCurrentLocale().toString();
        Map<String, String> typeNames = new LocalizationDAO().getAssessmentTypeNames(lang);

        ObservableList<AssessmentsDTO> data =
                loadAssessments(course, assessmentsService);


        // --- Table columns ---
        TableColumn<AssessmentsDTO, String> nameCol =
                new TableColumn<>(LocalizationManager.get("name"));

        nameCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        d.getValue().getTitle()
                )
        );

        TableColumn<AssessmentsDTO, String> typeCol =
                new TableColumn<>(LocalizationManager.get("type"));

        typeCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        typeNames.getOrDefault(d.getValue().getType().name(), d.getValue().getType().name())
                )
        );

        TableColumn<AssessmentsDTO, String> weightCol =
                new TableColumn<>(LocalizationManager.get("weightCol"));

        weightCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        String.valueOf(d.getValue().getWeight())
                )
        );

        TableColumn<AssessmentsDTO, String> maxScoreCol =
                new TableColumn<>(LocalizationManager.get("maxPtsCol"));

        maxScoreCol.setCellValueFactory(
                d -> new SimpleStringProperty(
                        String.valueOf(d.getValue().getMaxScore())
                )
        );

        nameCol.setPrefWidth(160);
        typeCol.setPrefWidth(160);
        weightCol.setPrefWidth(60);
        maxScoreCol.setPrefWidth(70);

        // --- Table ---
        TableView<AssessmentsDTO> table =
                new TableView<>(data);

        table.getColumns().addAll(
                List.of(
                        nameCol,
                        typeCol,
                        weightCol,
                        maxScoreCol
                )
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(320);
        table.getStylesheets().add(AssessmentsTab.class.getResource("/css/table.css").toExternalForm());
        table.setPlaceholder(createPlaceholder());

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
                new Label(LocalizationManager.get("totalWeight"));

        Label totalType = new Label();
        totalType.setStyle("-fx-text-fill: #6B7280; -fx-font-weight: bold;");

        updateTotalWeight(data, totalType);

        data.addListener(
                (ListChangeListener<AssessmentsDTO>) c ->
                        updateTotalWeight(data, totalType)
        );
        Region totalSpacer = new Region();

        HBox.setHgrow(totalSpacer, Priority.ALWAYS);

        totalRow.getChildren().addAll(
                totalName,
                totalSpacer,
                totalType
        );

        VBox leftPanel = new VBox(12, table, totalRow);
        leftPanel.setPadding(new Insets(16));
        leftPanel.getStyleClass().add("card");
        leftPanel.setPrefWidth(450);

        // --- Form ---
        Label title =
                new Label(LocalizationManager.get("addAssessment"));

        title.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;"
        );

        TextField nameField = new TextField();

        nameField.setPromptText(
                LocalizationManager.get("typeName")
        );

        nameField.setStyle(LIGHT_INPUT);
        nameField.setMaxWidth(
                Double.MAX_VALUE
        );

        ComboBox<AssessmentType> typeCombo = new ComboBox<>();

        typeCombo.getItems().addAll(AssessmentType.values());
        typeCombo.setPromptText(LocalizationManager.get("chooseType"));
        typeCombo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(AssessmentType item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : typeNames.getOrDefault(item.name(), item.name()));
            }
        });
        typeCombo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(AssessmentType item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? LocalizationManager.get("chooseType") : typeNames.getOrDefault(item.name(), item.name()));
            }
        });

        typeCombo.setMaxWidth(Double.MAX_VALUE);
        typeCombo.setStyle(LIGHT_INPUT);

        TextField weightField = new TextField();

        weightField.setPromptText(LocalizationManager.get("weight"));
        weightField.setStyle(LIGHT_INPUT);
        weightField.setMaxWidth(Double.MAX_VALUE);

        typeCombo.setOnAction(e -> {
            AssessmentType sel = typeCombo.getValue();
            if (sel != null) {
                data.stream().filter(dto -> dto.getType() == sel).findFirst()
                    .ifPresent(dto -> weightField.setText(String.valueOf(dto.getWeight())));
            }
        });

        TextField maxScoreField = new TextField();
        maxScoreField.setPromptText(LocalizationManager.get("maxPoints"));
        maxScoreField.setStyle(LIGHT_INPUT);
        maxScoreField.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Integer> posCombo = new ComboBox<>();

        for (int i = 1; i <= data.size() + 1; i++) {
            posCombo.getItems().add(i);
        }

        posCombo.setPromptText(
                LocalizationManager.get("choosePosition")
        );

        posCombo.setMaxWidth(Double.MAX_VALUE);
        posCombo.setStyle(LIGHT_INPUT);

        Button saveBtn =
                new Button(LocalizationManager.get("save"));

        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.getStyleClass().add("btn-primary");

        Button updateBtn =
                new Button(LocalizationManager.get("update"));

        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.getStyleClass().add("btn-primary");
        updateBtn.setVisible(false);

        final AssessmentsDTO[] selectedAssessment =
                new AssessmentsDTO[1];

        // --- Actions column ---
        AssessmentForm form =
                new AssessmentForm(
                        nameField,
                        typeCombo,
                        weightField,
                        maxScoreField,
                        posCombo,
                        title,
                        saveBtn,
                        updateBtn,
                        selectedAssessment
                );

        table.getColumns().add(
                createActionColumn(
                        data,
                        assessmentsService,
                        form
                )
        );

        // --- Save ---
        configureSaveButton(
                saveBtn,
                assessmentsService,
                course,
                data,
                form
        );

        // --- Update ---
        configureUpdateButton(
                updateBtn,
                assessmentsService,
                data,
                form
        );

        VBox rightPanel =
                new VBox(
                        20,
                        title,
                        nameField,
                        typeCombo,
                        weightField,
                        maxScoreField,
                        posCombo,
                        saveBtn,
                        updateBtn
                );

        rightPanel.setPadding(new Insets(24));
        rightPanel.getStyleClass().add("card");
        rightPanel.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        HBox layout =
                new HBox(
                        16,
                        leftPanel,
                        rightPanel
                );

        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStylesheets().add(AssessmentsTab.class.getResource("/css/button.css").toExternalForm());
        return layout;
    }

    /**
     * Loads all assessments for the specified course and converts them
     * to table data transfer objects.
     *
     * @param course course name
     * @param assessmentsService assessments service
     * @return observable list of assessments
     */
    private static ObservableList<AssessmentsDTO> loadAssessments(
            String course,
            AssessmentsService assessmentsService) {

        ObservableList<AssessmentsDTO> data =
                FXCollections.observableArrayList();

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

        return data;
    }

    /**
     * Calculates the total weight of unique assessment types and updates
     * the summary label.
     *
     * @param data assessment table data
     * @param totalType label displaying the total weight
     */
    private static void updateTotalWeight(
            ObservableList<AssessmentsDTO> data,
            Label totalType) {

        Set<AssessmentType> seen = new HashSet<>();

        double sum = 0;

        for (AssessmentsDTO dto : data) {
            if (seen.add(dto.getType())) {
                sum += dto.getWeight();
            }
        }

        totalType.setText(
                String.valueOf(
                        Math.round(sum * 100.0) / 100.0
                )
        );
    }

    /**
     * Creates a placeholder displayed when no assessments are available.
     *
     * @return placeholder label
     */
    private static Label createPlaceholder() {

        Label placeholder = new Label(LocalizationManager.get("noContent"));

        placeholder.setWrapText(true);
        placeholder.setMaxWidth(348);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setTextAlignment(TextAlignment.CENTER);

        return placeholder;
    }

    /**
     * Validates that the total weight of assessment categories
     * does not exceed the allowed limit.
     *
     * @param data existing assessments
     * @param type assessment type being processed
     * @param weight proposed weight
     * @return true if the weight is valid; false otherwise
     */
    private static boolean validateTotalWeight(
            ObservableList<AssessmentsDTO> data,
            AssessmentType type,
            double weight) {

        Set<AssessmentType> seenTypes = new HashSet<>();

        double currentTotal = 0;

        for (AssessmentsDTO dto : data) {

            if (dto.getType() != type && seenTypes.add(dto.getType())) {
                currentTotal += dto.getWeight();
            }
        }

        if (currentTotal + weight > 1.0) {

            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("weightExceedsLimit")
                            + " "
                            + currentTotal
                            + "."
            ).showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Parses and validates the maximum score value entered by the user.
     *
     * @param maxScoreField maximum score input field
     * @return parsed maximum score or null if validation fails
     */
    private static Double parseMaxScore(TextField maxScoreField) {

        String maxScoreText = maxScoreField.getText().trim();
        if (maxScoreText.isEmpty()) {

            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("enterMaxPoints")
            ).showAndWait();

            return null;
        }

        try {

            double maxScore = Double.parseDouble(maxScoreText);

            if (maxScore <= 0) {
                new Alert(
                        Alert.AlertType.WARNING,
                        LocalizationManager.get("maxPointsPositive")
                ).showAndWait();

                return null;
            }
            return maxScore;

        } catch (NumberFormatException ex) {
            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("maxPointsInvalid")
            ).showAndWait();
            return null;
        }
    }

    /**
     * Encapsulates controls and state used by the assessment form.
     * This object is used to reduce parameter passing between helper methods.
     */
    @SuppressWarnings("java:S107")
    private static class AssessmentForm {
        TextField nameField;
        ComboBox<AssessmentType> typeCombo;
        TextField weightField;
        TextField maxScoreField;
        ComboBox<Integer> posCombo;
        Label title;
        Button saveBtn;
        Button updateBtn;
        AssessmentsDTO[] selectedAssessment;

        public AssessmentForm(
                TextField nameField,
                ComboBox<AssessmentType> typeCombo,
                TextField weightField,
                TextField maxScoreField,
                ComboBox<Integer> posCombo,
                Label title,
                Button saveBtn,
                Button updateBtn,
                AssessmentsDTO[] selectedAssessment) {

            this.nameField = nameField;
            this.typeCombo = typeCombo;
            this.weightField = weightField;
            this.maxScoreField = maxScoreField;
            this.posCombo = posCombo;
            this.title = title;
            this.saveBtn = saveBtn;
            this.updateBtn = updateBtn;
            this.selectedAssessment = selectedAssessment;
        }

        public TextField getNameField() {
            return nameField;
        }

        public ComboBox<AssessmentType> getTypeCombo() {
            return typeCombo;
        }

        public TextField getWeightField() {
            return weightField;
        }

        public TextField getMaxScoreField() {
            return maxScoreField;
        }

        public ComboBox<Integer> getPosCombo() {
            return posCombo;
        }

        public Label getTitle() {
            return title;
        }

        public Button getSaveBtn() {
            return saveBtn;
        }

        public Button getUpdateBtn() {
            return updateBtn;
        }

        public AssessmentsDTO[] getSelectedAssessment() {
            return selectedAssessment;
        }
    }

    /**
     * Creates the action column containing edit and delete buttons.
     *
     * @param data assessment table data
     * @param assessmentsService assessments service
     * @param form assessment form controls and state
     * @return configured action column
     */
    @SuppressWarnings("java:S1171")
    private static TableColumn<AssessmentsDTO, Void> createActionColumn(
            ObservableList<AssessmentsDTO> data,
            AssessmentsService assessmentsService,
            AssessmentForm form) {

        TableColumn<AssessmentsDTO, Void> actionCol =
                new TableColumn<>(
                        LocalizationManager.get("actions")
                );

        actionCol.setPrefWidth(100);

        actionCol.setCellFactory(col ->
                new TableCell<>() {

                    private final Button editBtn =
                            new Button("✏");

                    private final Button deleteBtn =
                            new Button("🗑");

                    {
                        configureActionButtons(
                                editBtn,
                                deleteBtn,
                                this,
                                data,
                                assessmentsService,
                                form
                        );
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {

                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new HBox(5, editBtn, deleteBtn));
                        }
                    }
                }
        );

        return actionCol;
    }

    /**
     * Configures the save button used to create new assessments.
     *
     * @param saveBtn save button
     * @param assessmentsService assessments service
     * @param course selected course name
     * @param data assessment table data
     * @param form assessment form controls and state
     */
    private static void configureSaveButton(
            Button saveBtn,
            AssessmentsService assessmentsService,
            String course,
            ObservableList<AssessmentsDTO> data,
            AssessmentForm form) {

        saveBtn.setOnAction(e -> {

            String name = form.getNameField().getText().trim();
            AssessmentType type = form.getTypeCombo().getValue();
            Integer pos = form.getPosCombo().getValue();

            double weight;
            try {
                weight = Double.parseDouble(
                        form.getWeightField()
                                .getText()
                                .trim()
                );
            } catch (NumberFormatException ex) {
                weight = 1.0;
            }

            if (!validateSaveInput(name, type, pos)) {
                return;
            }

            if (!validateTotalWeight(data, type, weight)) {
                return;
            }

            Assessments assessment = new Assessments();
            Course courseEntity = assessmentsService.getCourseByName(course);

            if (courseEntity == null) {

                new Alert(
                        Alert.AlertType.ERROR,
                        LocalizationManager.get(
                                "courseNotFound"
                        )
                ).showAndWait();

                return;
            }

            assessment.setCourseId(courseEntity.getId());
            assessment.setTitle(name);
            assessment.setType(type);

            Double maxScore = parseMaxScore(form.getMaxScoreField());

            if (maxScore == null) {
                return;
            }

            assessment.setMaxScore(maxScore);
            assessment.setWeight(weight);
            assessment.setDueDate(null);

            assessmentsService.save(assessment);

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

            resetForm(
                    data,
                    form.getNameField(),
                    form.getTypeCombo(),
                    form.getWeightField(),
                    form.getMaxScoreField(),
                    form.getPosCombo()
            );
        });
    }

    /**
     * Configures the update button used to edit existing assessments.
     *
     * @param updateBtn update button
     * @param assessmentsService assessments service
     * @param data assessment table data
     * @param form assessment form controls and state
     */
    private static void configureUpdateButton(
            Button updateBtn,
            AssessmentsService assessmentsService,
            ObservableList<AssessmentsDTO> data,
            AssessmentForm form) {

        updateBtn.setOnAction(e -> {

            AssessmentsDTO selected = form.getSelectedAssessment()[0];

            if (selected == null) {
                return;
            }

            String name = form.getNameField().getText().trim();
            AssessmentType type = form.getTypeCombo().getValue();

            double weight = parseWeight(form.getWeightField());

            if (name.isEmpty()
                    || type == null) {
                return;
            }

            if (!validateUpdatedWeight(
                    data,
                    selected,
                    type,
                    weight
            )) {
                return;
            }

            Double maxScore = parseMaxScore(form.getMaxScoreField());

            if (maxScore == null) {
                return;
            }

            Assessments assessment =
                    updateAssessment(
                            assessmentsService,
                            selected,
                            name,
                            type,
                            weight,
                            maxScore
                    );

            if (assessment == null) {
                return;
            }

            int index = data.indexOf(selected);
            AssessmentsDTO updated = toDTO(assessment);
            Integer newPos = form.getPosCombo().getValue();

            if (newPos != null && newPos != index + 1) {
                data.remove(index);
                data.add(
                        Math.min(
                                newPos - 1,
                                data.size()
                        ),
                        updated
                );

            } else {
                data.set(
                        index,
                        updated
                );
            }

            resetForm(
                    data,
                    form.getNameField(),
                    form.getTypeCombo(),
                    form.getWeightField(),
                    form.getMaxScoreField(),
                    form.getPosCombo()
            );

            form.getSelectedAssessment()[0] = null;

            form.getTitle().setText(
                    LocalizationManager.get(
                            "addAssessment"
                    )
            );

            updateBtn.setVisible(false);
            form.getSaveBtn().setVisible(true);
        });
    }

    /**
     * Validates the total weight when an assessment is updated.
     *
     * @param data assessment table data
     * @param selected assessment being edited
     * @param type updated assessment type
     * @param weight updated weight
     * @return true if the updated weight is valid; false otherwise
     */
    private static boolean validateUpdatedWeight(
            ObservableList<AssessmentsDTO> data,
            AssessmentsDTO selected,
            AssessmentType type,
            double weight) {

        Set<AssessmentType> seenTypes =
                new HashSet<>();

        double currentTotal = 0;

        for (AssessmentsDTO dto : data) {

            if (dto == selected) {
                continue;
            }

            if (dto.getType() != type && seenTypes.add(dto.getType())) {
                currentTotal += dto.getWeight();
            }
        }

        if (currentTotal + weight > 1.0) {
            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("weightExceedsLimit")
                            + " "
                            + currentTotal
                            + "."
            ).showAndWait();

            return false;
        }

        return true;
    }

    /**
     * Converts a persistence entity to a data transfer object.
     *
     * @param assessment assessment entity
     * @return assessment DTO
     */
    private static AssessmentsDTO toDTO(
            Assessments assessment) {

        return new AssessmentsDTO(
                assessment.getId(),
                assessment.getCourseId(),
                assessment.getTitle(),
                assessment.getType(),
                assessment.getMaxScore(),
                assessment.getWeight(),
                assessment.getDueDate()
        );
    }

    /**
     * Clears the assessment form and restores available positions.
     *
     * @param data assessment table data
     * @param nameField assessment name field
     * @param typeCombo assessment type selector
     * @param weightField weight field
     * @param maxScoreField maximum score field
     * @param posCombo position selector
     */
    private static void resetForm(
            ObservableList<AssessmentsDTO> data,
            TextField nameField,
            ComboBox<AssessmentType> typeCombo,
            TextField weightField,
            TextField maxScoreField,
            ComboBox<Integer> posCombo) {

        nameField.clear();
        typeCombo.setValue(null);
        weightField.clear();
        maxScoreField.clear();

        posCombo.getItems().clear();

        for (int i = 1; i <= data.size() + 1; i++) {
            posCombo.getItems().add(i);
        }

        posCombo.setValue(null);
    }

    /**
     * Updates an existing assessment and persists the changes.
     *
     * @param assessmentsService assessments service
     * @param selected selected assessment DTO
     * @param name updated title
     * @param type updated assessment type
     * @param weight updated weight
     * @param maxScore updated maximum score
     * @return updated assessment entity or null if not found
     */
    private static Assessments updateAssessment(
            AssessmentsService assessmentsService,
            AssessmentsDTO selected,
            String name,
            AssessmentType type,
            double weight,
            Double maxScore) {

        Assessments assessment =
                assessmentsService.findById(
                        selected.getId()
                );

        if (assessment == null) {
            return null;
        }

        assessment.setTitle(name);
        assessment.setType(type);
        assessment.setWeight(weight);
        assessment.setMaxScore(maxScore);

        assessmentsService.update(
                assessment
        );

        return assessment;
    }

    /**
     * Parses the assessment weight entered by the user.
     *
     * @param weightField weight input field
     * @return parsed weight or 1.0 if parsing fails
     */
    private static double parseWeight(
            TextField weightField) {

        try {
            return Double.parseDouble(
                    weightField.getText().trim()
            );
        } catch (NumberFormatException ex) {
            return 1.0;
        }
    }

    /**
     * Validates required input fields before creating a new assessment.
     *
     * @param name assessment title
     * @param type assessment type
     * @param pos selected position
     * @return true if all required values are provided
     */
    private static boolean validateSaveInput(
            String name,
            AssessmentType type,
            Integer pos) {

        if (name.isEmpty()) {

            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("enterAssessmentName")
            ).showAndWait();

            return false;
        }

        if (type == null) {

            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("chooseAssessmentType")
            ).showAndWait();

            return false;
        }

        if (pos == null) {

            new Alert(
                    Alert.AlertType.WARNING,
                    LocalizationManager.get("choosePosition")
            ).showAndWait();

            return false;
        }

        return true;
    }

    /**
     * Loads assessment data into the form for editing.
     *
     * @param selected selected assessment
     * @param data assessment table data
     * @param form assessment form controls and state
     */
    private static void handleEditAssessment(
            AssessmentsDTO selected,
            ObservableList<AssessmentsDTO> data,
            AssessmentForm form) {

        form.getSelectedAssessment()[0] = selected;

        form.getNameField().setText(selected.getTitle());
        form.getTypeCombo().setValue(selected.getType());
        form.getWeightField().setText(String.valueOf(selected.getWeight()));
        form.getMaxScoreField().setText(String.valueOf(selected.getMaxScore()));
        form.getPosCombo().getItems().clear();

        for (int i = 1; i <= data.size(); i++) {
            form.getPosCombo()
                    .getItems()
                    .add(i);
        }

        form.getPosCombo().setValue(data.indexOf(selected) + 1);

        form.getTitle().setText(
                LocalizationManager.get("edit")
                        + " "
                        + LocalizationManager
                        .get("assessment")
                        .toLowerCase()
        );

        form.getSaveBtn().setVisible(false);
        form.getUpdateBtn().setVisible(true);
    }

    /**
     * Deletes an assessment and optionally removes related grades.
     *
     * @param selected selected assessment
     * @param assessmentsService assessments service
     * @param data assessment table data
     */
    private static void handleDeleteAssessment(
            AssessmentsDTO selected,
            AssessmentsService assessmentsService,
            ObservableList<AssessmentsDTO> data) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle(LocalizationManager.get(
                        "deleteAssessmentTitle"));

        confirm.setHeaderText(LocalizationManager.get(
                        "deleteAssessmentQuestion"));

        confirm.setContentText(selected.getTitle());

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        Assessments assessment =
                assessmentsService.findById(
                        selected.getId()
                );

        if (assessment == null) {
            return;
        }

        if (assessmentsService.hasGrades(selected.getId())) {

            Alert warning = new Alert(Alert.AlertType.CONFIRMATION);

            warning.setTitle(
                    LocalizationManager.get(
                            "deleteAssessmentTitle"
                    )
            );

            warning.setHeaderText(
                    LocalizationManager.get(
                            "assessmentHasGrades"
                    )
            );

            warning.setContentText(
                    LocalizationManager.get(
                            "deleteGradesConfirm"
                    )
            );

            ButtonType yes =
                    new ButtonType(
                            LocalizationManager.get(
                                    "deleteAnyway"
                            ),
                            ButtonBar.ButtonData.OK_DONE
                    );

            ButtonType no =
                    new ButtonType(
                            LocalizationManager.get(
                                    "cancel"
                            ),
                            ButtonBar.ButtonData.CANCEL_CLOSE
                    );

            warning.getButtonTypes()
                    .setAll(yes, no);

            if (warning.showAndWait()
                    .orElse(no) != yes) {
                return;
            }

            com.edujournal.dao.GradesDAO gradesDAO =
                    new com.edujournal.dao.GradesDAO();

            gradesDAO.findByAssessment(
                            selected.getId()
                    )
                    .forEach(
                            g -> gradesDAO.deleteById(
                                    g.getId()
                            )
                    );
        }

        assessmentsService.delete(
                assessment
        );

        data.remove(selected);
    }

    private static void configureActionButtons(
            Button editBtn,
            Button deleteBtn,
            TableCell<AssessmentsDTO, Void> cell,
            ObservableList<AssessmentsDTO> data,
            AssessmentsService assessmentsService,
            AssessmentForm form) {

        deleteBtn.setStyle(
                "-fx-background-color: #DC2626; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-radius: 6;"
        );

        editBtn.setOnAction(e ->
                handleEditAssessment(
                        cell.getTableView()
                                .getItems()
                                .get(cell.getIndex()),
                        data,
                        form
                )
        );

        deleteBtn.setOnAction(e ->
                handleDeleteAssessment(
                        cell.getTableView()
                                .getItems()
                                .get(cell.getIndex()),
                        assessmentsService,
                        data
                )
        );
    }
}