package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.AcademicGroupMapper;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminGroupController extends BaseController<AcademicGroupDTO> {

    private final AcademicGroupService groupService = new AcademicGroupService();
    private final AcademicGroupMapper groupMapper = new AcademicGroupMapper();
    private final CourseService courseService = new CourseService();

    private AcademicGroupDTO selectedGroup;

    public AdminGroupController(Role role) {
        enableCardsView(this::buildGroupCard);
        viewBtn.setVisible(false);
        configureColumns();
        loadAndShowItems();
    }

    @Override
    protected void configureColumns() {
        TableColumn<AcademicGroupDTO, String> nameCol =
                new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        table.getColumns().add(nameCol);

        filterCombo.getItems().addAll("All", "Name");
        filterCombo.setValue("All");
    }

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

        Button addStudentBtn = new Button("Add");
        addStudentBtn.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white;");
        addStudentBtn.setOnAction(e -> showAddStudentDialog(group));

        Button removeStudentBtn = new Button("Remove");
        removeStudentBtn.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white;");
        removeStudentBtn.setOnAction(e -> showRemoveStudentDialog(group));

        Button showStudentsBtn = new Button("Show students");
        showStudentsBtn.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white;");
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
            String teacherName = (c.getTeacherName() != null && !c.getTeacherName().isBlank())
                    ? c.getTeacherName()
                    : "No teacher";

            Label courseLabel = new Label("• " + c.getName() + " (" + c.getCode() + ") - " + teacherName);
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
                    node.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
                }
            }
            card.setStyle("-fx-border-color: #4A90E2; -fx-padding: 10; -fx-background-color: #E8F0FE;");
        });

        card.getChildren().addAll(header, coursesBox);

        return card;
    }

    @Override
    protected List<AcademicGroupDTO> loadAllItems() {
        return groupService.findAllDTO();
    }

    @Override
    protected boolean matchesFilter(AcademicGroupDTO item, String filter, String text) {

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(List.of(item.getName()), text);
        }

        if (filter.equals("Name")) {
            return item.getName() != null &&
                    item.getName().toLowerCase().contains(text);
        }
        return true;
    }

    @Override
    public void showAddDialog() {
        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Academic Group");

        TextField nameField = new TextField();
        nameField.setPromptText("Academic group name");

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (groupService.existsByName(nameField.getText())) {
                    new Alert(Alert.AlertType.ERROR,
                            "Academic group already exists.").showAndWait();
                    return null;
                }

                if (nameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Group name cannot be empty.").showAndWait();
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

    @Override
    protected void showEditDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Dialog<AcademicGroupDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Academic Group");

        TextField nameField = new TextField(group.getName());

        VBox box = new VBox(10, nameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                String newName = nameField.getText().trim();

                if (newName.equalsIgnoreCase(group.getName())) {
                } else {
                    if (groupService.existsByName(newName)) {
                        new Alert(Alert.AlertType.ERROR,
                                "Academic group already exists.").showAndWait();
                        return null;
                    }
                }

                if (nameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Group name cannot be empty.").showAndWait();
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

    @Override
    protected void showDeleteDialog() {
        AcademicGroupDTO group = selectedGroup;
        if (group == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Group");
        alert.setHeaderText("Are you sure you want to delete \"" + group.getName() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            groupService.delete(group.getId());

            loadAndShowItems();
            clearSelection();
        }
    }

    private void showAddStudentDialog(AcademicGroupDTO group) {

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Add student to group: " + group.getName());

        List<StudentDTO> availableStudents = new StudentService().findAllDTO()
                .stream()
                .filter(s -> s.getAcademicGroupId() == null)
                .toList();

        if (availableStudents.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "No available students to add.").showAndWait();
            return;
        }

        ComboBox<StudentDTO> studentCombo = new ComboBox<>();
        studentCombo.setItems(FXCollections.observableArrayList(availableStudents));
        studentCombo.setEditable(false);
        studentCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(StudentDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName()
                            + " (" + item.getStudentNumber() + ")");
                }
            }
        });

        studentCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StudentDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName()
                            + " (" + item.getStudentNumber() + ")");
                }
            }
        });

        VBox box = new VBox(10, studentCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == addBtn) {
                StudentDTO selected = studentCombo.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    new Alert(Alert.AlertType.ERROR, "Select a student").showAndWait();
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

    private void showGroupStudentsWindow(AcademicGroupDTO group) {

        Stage stage = new Stage();
        stage.setTitle("Students in group: " + group.getName());

        List<StudentDTO> students = new StudentService().findAllDTO()
                .stream()
                .filter(s -> group.getId().equals(s.getAcademicGroupId()))
                .toList();

        TableView<StudentDTO> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(students));

        TableColumn<StudentDTO, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));

        TableColumn<StudentDTO, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));

        TableColumn<StudentDTO, String> numberCol = new TableColumn<>("Student Number");
        numberCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentNumber()));

        TableColumn<StudentDTO, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<StudentDTO, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        table.getColumns().addAll(firstNameCol, lastNameCol, numberCol, emailCol, phoneCol);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox root = new VBox(table);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void showRemoveStudentDialog(AcademicGroupDTO group) {

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Remove student from group: " + group.getName());

        List<StudentDTO> groupStudents = new StudentService().findAllDTO()
                .stream()
                .filter(s -> group.getId().equals(s.getAcademicGroupId()))
                .toList();

        if (groupStudents.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "This group has no students.").showAndWait();
            return;
        }

        ComboBox<StudentDTO> studentCombo = new ComboBox<>();
        studentCombo.setItems(FXCollections.observableArrayList(groupStudents));
        studentCombo.setEditable(false);

        studentCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(StudentDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName()
                            + " (" + item.getStudentNumber() + ")");
                }
            }
        });

        studentCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StudentDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName()
                            + " (" + item.getStudentNumber() + ")");
                }
            }
        });

        VBox box = new VBox(10, new Label("Select student to remove:"), studentCombo);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType removeBtn = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == removeBtn) {
                StudentDTO selected = studentCombo.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    new Alert(Alert.AlertType.ERROR, "Select a student").showAndWait();
                    return null;
                }
                return selected.getStudentId();
            }
            return null;
        });

        Integer studentId = dialog.showAndWait().orElse(null);

        if (studentId != null) {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm removal");
            confirm.setHeaderText("Remove student from group?");
            confirm.setContentText("This action cannot be undone.");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                groupService.removeStudentFromGroup(studentId, group.getId());
                loadAndShowItems();
            }
        }
    }

    @Override
    protected void onView() {}

    private void clearSelection() {
        selectedGroup = null;
    }
}
