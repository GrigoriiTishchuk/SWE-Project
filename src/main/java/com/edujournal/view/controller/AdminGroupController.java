package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Role;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.StudentDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing academic groups. Provides functionality for creating,
 * editing, deleting groups and managing group membership.
 */
public class AdminGroupController extends BaseController<AcademicGroupDTO> {

    private final AcademicGroupService groupService = new AcademicGroupService();
    private final AcademicGroupMapper groupMapper = new AcademicGroupMapper();
    private final CourseService courseService = new CourseService();

    private AcademicGroupDTO selectedGroup;

    /**
     * Creates the academic group administration controller.
     *
     * @param role current user role
     */
    public AdminGroupController(Role role) {
        enableCardsView(this::buildGroupCard);
        viewBtn.setVisible(false);
        configureColumns();
        loadAndShowItems();
    }

    /** Configures group columns and filter options. */
    @Override
    protected void configureColumns() {
        TableColumn<AcademicGroupDTO, String> nameCol =
                new TableColumn<>(LocalizationManager.get("name"));
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        table.getColumns().add(nameCol);

        filterCombo.getItems().addAll("All", "Name");
        filterCombo.setValue("All");

        Map<String, String> filterKeyMap = Map.of("All", "all", "Name", "name");
        filterCombo.setCellFactory(list -> createLocalizedCell(filterKeyMap));
        filterCombo.setButtonCell(createLocalizedCell(filterKeyMap));
    }

    /**
     * Creates a card representation of an academic group including assigned courses and management
     * actions.
     *
     * @param group academic group to display
     * @return configured group card
     */
    private VBox buildGroupCard(AcademicGroupDTO group) {
        VBox card = new VBox();
        card.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        card.setSpacing(8);
        card.setMinHeight(80);
        card.setPrefHeight(80);
        card.setMaxHeight(80);

        Label nameLabel = new Label(group.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label arrow = new Label("▶");
        arrow.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button addStudentBtn = new Button(LocalizationManager.get("add"));
        addStudentBtn.getStyleClass().add("btn-primary");
        addStudentBtn.setOnAction(e -> showAddStudentDialog(group));

        Button removeStudentBtn = new Button(LocalizationManager.get("remove"));
        removeStudentBtn.getStyleClass().add("btn-delete");
        removeStudentBtn.setOnAction(e -> showRemoveStudentDialog(group));

        Button showStudentsBtn = new Button(LocalizationManager.get("showStudents"));
        showStudentsBtn.getStyleClass().add("btn-secondary");
        showStudentsBtn.setOnAction(e -> showGroupStudentsWindow(group));

        HBox header = new HBox(nameLabel, arrow);
        header.setSpacing(10);
        header.setStyle("-fx-alignment: center-left;");
        header.setPadding(new Insets(0, 0, 5, 0));

        HBox leftHeader = new HBox(nameLabel, arrow);
        leftHeader.setSpacing(10);

        HBox rightHeader = new HBox(addStudentBtn, removeStudentBtn, showStudentsBtn);
        rightHeader.setSpacing(10);

        header.getChildren().addAll(leftHeader, rightHeader);
        header.setSpacing(10);
        header.setFillHeight(true);
        header.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(leftHeader, javafx.scene.layout.Priority.ALWAYS);

        VBox coursesBox = new VBox();
        coursesBox.setSpacing(5);
        coursesBox.setPadding(new Insets(0, 0, 0, 20));
        coursesBox.setVisible(false);

        List<CourseDTO> courses = courseService.findByGroupId(group.getId());

        for (CourseDTO c : courses) {
            String teacherName =
                    (c.getTeacherName() != null && !c.getTeacherName().isBlank())
                            ? c.getTeacherName()
                            : LocalizationManager.get("noTeacher");

            Label courseLabel =
                    new Label("• " + c.getName() + " (" + c.getCode() + ") - " + teacherName);
            coursesBox.getChildren().add(courseLabel);
        }

        header.setOnMouseClicked(e -> {
            boolean expanded = coursesBox.isVisible();
            coursesBox.setVisible(!expanded);
            coursesBox.setManaged(!expanded);
            arrow.setText(expanded ? "▶" : "▼");

            if (!expanded) {
                card.setMinHeight(Region.USE_COMPUTED_SIZE);
                card.setPrefHeight(Region.USE_COMPUTED_SIZE);
                card.setMaxHeight(Region.USE_COMPUTED_SIZE);
            } else {
                card.setMinHeight(80);
                card.setPrefHeight(80);
                card.setMaxHeight(80);
            }
        });

        card.setOnMouseClicked(e -> {
            selectedGroup = group;

            VBox parent = (VBox) card.getParent();
            if (parent != null) {
                for (Node node : parent.getChildren()) {
                    node.setStyle(
                            "-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
                }
            }
            card.setStyle(
                    "-fx-border-color: #4A90E2; -fx-padding: 10; -fx-background-color: #E8F0FE;");
        });

        card.getChildren().addAll(header, coursesBox);

        return card;
    }

    /**
     * Loads all academic groups.
     *
     * @return list of academic groups
     */
    @Override
    protected List<AcademicGroupDTO> loadAllItems() {
        return groupService.findAllDTO();
    }

    /**
     * Checks whether a group matches the current filter and search text.
     *
     * @param item group to evaluate
     * @param filter selected filter
     * @param text search text
     * @return true if the group matches
     */
    @Override
    protected boolean matchesFilter(AcademicGroupDTO item, String filter, String text) {

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(List.of(item.getName()), text);
        }

        if (filter.equals("Name")) {
            return item.getName() != null && item.getName().toLowerCase().contains(text);
        }
        return true;
    }

    /** Opens a dialog for creating a new academic group. */
    @Override
    public void showAddDialog() {
        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("addGroup"));

        TextField nameField = new TextField();
        nameField.setPromptText(LocalizationManager.get("name"));

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn =
                new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (groupService.existsByName(nameField.getText())) {
                    showError(LocalizationManager.get("groupExists"));
                    return null;
                }
                if (nameField.getText().isBlank()) {
                    showError(LocalizationManager.get("groupNameNotEmpty"));
                    return null;
                }

                AcademicGroupDTO dto = new AcademicGroupDTO();
                dto.setName(nameField.getText());
                return dto;
            }
            return null;
        });

        AcademicGroupDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            AcademicGroup entity = groupMapper.toEntity(result);
            groupService.save(entity);
            loadAndShowItems();
        }
    }

    /** Opens a dialog for editing the selected academic group. */
    @Override
    protected void showEditDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("editGroup"));

        TextField nameField = new TextField(group.getName() != null ? group.getName() : "");

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn =
                new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                String newName = nameField.getText().trim();

                if (!newName.equalsIgnoreCase(group.getName())
                        && groupService.existsByName(newName)) {
                    showError(LocalizationManager.get("groupExists"));
                    return null;
                }

                if (nameField.getText().isBlank()) {
                    showError(LocalizationManager.get("groupNameNotEmpty"));
                    return null;
                }

                group.setName(nameField.getText());
                return group;
            }
            return null;
        });

        AcademicGroupDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            AcademicGroup entity = groupMapper.toEntity(updated);
            groupService.update(entity);
            loadAndShowItems();
            clearSelection();
        }
    }

    /** Opens a confirmation dialog and deletes the selected academic group if confirmed. */
    @Override
    protected void showDeleteDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LocalizationManager.get("deleteGroup"));
        alert.setHeaderText(
                LocalizationManager.get("sureToDelete") + " \"" + group.getName() + "\"?");
        alert.setContentText(LocalizationManager.get("undone"));

        ButtonType yes =
                new ButtonType(LocalizationManager.get("yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType no =
                new ButtonType(LocalizationManager.get("no"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            groupService.delete(group.getId());

            loadAndShowItems();
            clearSelection();
        }
    }

    /**
     * Displays a dialog for assigning a student to the specified group.
     *
     * @param group target academic group
     */
    private void showAddStudentDialog(AcademicGroupDTO group) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("addStudentGroup") + ": " + group.getName());

        List<StudentDTO> availableStudents =
                new StudentService()
                        .findAllDTO().stream().filter(s -> s.getAcademicGroupId() == null).toList();

        if (availableStudents.isEmpty()) {
            showInfo(LocalizationManager.get("noAvailableStudent"));
            return;
        }

        ComboBox<StudentDTO> studentCombo = createStudentComboBox(availableStudents);

        VBox box = new VBox(10, studentCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType addBtn =
                new ButtonType(LocalizationManager.get("add"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == addBtn) {
                StudentDTO selected = studentCombo.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    showError(LocalizationManager.get("selectStudent"));
                    return null;
                }

                return selected.getStudentId();
            }
            return null;
        });

        Integer studentId = dialog.showAndWait().orElse(null);

        if (studentId != null) {
            groupService.addStudentToGroup(studentId, group.getId());
            loadAndShowItems();
        }
    }

    /**
     * Creates and configures a student selection ComboBox.
     *
     * @param students available students
     * @return configured student ComboBox
     */
    private ComboBox<StudentDTO> createStudentComboBox(List<StudentDTO> students) {
        ComboBox<StudentDTO> studentCombo = new ComboBox<>();
        studentCombo.setItems(FXCollections.observableArrayList(students));
        studentCombo.setEditable(false);

        studentCombo.setCellFactory(cb ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(StudentDTO item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(
                                    item.getFirstName()
                                            + " "
                                            + item.getLastName()
                                            + " ("
                                            + item.getStudentNumber()
                                            + ")");
                        }
                    }
                });

        studentCombo.setButtonCell(
                new ListCell<>() {
                    @Override
                    protected void updateItem(StudentDTO item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(
                                    item.getFirstName()
                                            + " "
                                            + item.getLastName()
                                            + " ("
                                            + item.getStudentNumber()
                                            + ")");
                        }
                    }
                });

        return studentCombo;
    }

    /**
     * Displays a window containing all students assigned to the group.
     *
     * @param group academic group to display
     */
    private void showGroupStudentsWindow(AcademicGroupDTO group) {
        Stage stage = new Stage();
        stage.setTitle(LocalizationManager.get("studentsGroup") + ": " + group.getName());

        List<StudentDTO> students =
                new StudentService()
                        .findAllDTO().stream()
                                .filter(s -> group.getId().equals(s.getAcademicGroupId()))
                                .toList();

        TableView<StudentDTO> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(students));

        TableColumn<StudentDTO, String> firstNameCol =
                new TableColumn<>(LocalizationManager.get("firstName"));
        firstNameCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getFirstName()));

        TableColumn<StudentDTO, String> lastNameCol =
                new TableColumn<>(LocalizationManager.get("lastName"));
        lastNameCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getLastName()));

        TableColumn<StudentDTO, String> numberCol =
                new TableColumn<>(LocalizationManager.get("studentNumberCol"));
        numberCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getStudentNumber()));

        TableColumn<StudentDTO, String> emailCol =
                new TableColumn<>(LocalizationManager.get("email"));
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<StudentDTO, String> phoneCol =
                new TableColumn<>(LocalizationManager.get("phone"));
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        table.getColumns().addAll(firstNameCol, lastNameCol, numberCol, emailCol, phoneCol);
        table.getStylesheets()
                .add(AdminGroupController.class.getResource("/css/table.css").toExternalForm());

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox root = new VBox(table);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * Displays a dialog for removing a student from the specified group.
     *
     * @param group target academic group
     */
    private void showRemoveStudentDialog(AcademicGroupDTO group) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("removeStudentFromGroup") + ": " + group.getName());

        List<StudentDTO> groupStudents =
                new StudentService()
                        .findAllDTO().stream()
                                .filter(s -> group.getId().equals(s.getAcademicGroupId()))
                                .toList();

        if (groupStudents.isEmpty()) {
            showInfo(LocalizationManager.get("noStudents"));
            return;
        }

        ComboBox<StudentDTO> studentCombo = createStudentComboBox(groupStudents);

        VBox box =
                new VBox(
                        10,
                        new Label(LocalizationManager.get("selectStudentToRemove")),
                        studentCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType removeBtn =
                new ButtonType(LocalizationManager.get("remove"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == removeBtn) {
                StudentDTO selected = studentCombo.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    showError(LocalizationManager.get("selectStudent"));
                    return null;
                }
                return selected.getStudentId();
            }
            return null;
        });

        Integer studentId = dialog.showAndWait().orElse(null);

        if (studentId != null) {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle(LocalizationManager.get("confirmRemoval"));
            confirm.setHeaderText(LocalizationManager.get("removeStudentFromGroup") + "?");
            confirm.setContentText(LocalizationManager.get("undone"));

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                groupService.removeStudentFromGroup(studentId, group.getId());
                loadAndShowItems();
            }
        }
    }

    /** Not used in card view mode. */
    @Override
    protected void onView() {
        // Not used
    }

    /** Clears the currently selected group. */
    private void clearSelection() {
        selectedGroup = null;
    }
}
