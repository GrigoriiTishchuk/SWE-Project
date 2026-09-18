package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class AdminTeacherController extends BaseController<UserDTO> {
    private final UserService userService = new UserService();
    private final UserMapper userMapper = new UserMapper();

    private final Role role;

    public AdminTeacherController(Role role) {
        this.role = role;
        configureColumns();
        buildUI();
        loadAndShowItems();
    }

    private HBox buildFilterBar() {
        searchField.setPromptText("Search teachers...");
        searchField.setPrefWidth(250);

        Button clearButton = new Button("Clear");
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.getChildren().addAll(
                new Label("Search:"),
                searchField,
                new Label("Filter by:"),
                filterCombo,
                clearButton
        );

        clearButton.setOnAction(event -> {
            searchField.clear();
            filterCombo.setValue("All");
        });

        return filterBar;
    }

    private void buildUI() {
        // Double click opens information about the teacher
        table.setRowFactory(tv -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    UserDTO teacher = row.getItem();
                    showUserInfoDialog(teacher);
                }
            });
            return row;
        });

        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");

        addBtn.setOnAction(e -> showAddTeacherDialog());

        deleteBtn.setOnAction(e -> {
            UserDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showDeleteTeacherDialog(selected);
        });

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10));
        actions.getChildren().addAll(addBtn, deleteBtn);

        HBox filterBar = buildFilterBar();

        VBox layout = new VBox(10, filterBar, table, actions);
        layout.setPadding(new Insets(10));

        setCenter(layout);
    }

    @Override
    protected List<UserDTO> loadAllItems() {
        return userService.findAllTeachers();
    }

    @Override
    protected boolean matchesFilter(UserDTO teacher, String filter, String searchText) {
        searchText = searchText.toLowerCase();

        switch (filter) {
            case "ID":
                return String.valueOf(teacher.getId()).contains(searchText);

            case "Username":
                return teacher.getUsername() != null &&
                        teacher.getUsername().toLowerCase().contains(searchText);

            case "First Name":
                return teacher.getFirstName() != null &&
                        teacher.getFirstName().toLowerCase().contains(searchText);

            case "Last Name":
                return teacher.getLastName() != null &&
                        teacher.getLastName().toLowerCase().contains(searchText);

            case "All":
            default:
                return matchesAllFields(
                        List.of(
                                String.valueOf(teacher.getId()),
                                safe(teacher.getUsername()),
                                safe(teacher.getFirstName()),
                                safe(teacher.getLastName())
                        ),
                        searchText
                );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    protected void configureColumns() {
        TableColumn<UserDTO, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<UserDTO, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));

        TableColumn<UserDTO, String> firstNameCol = new TableColumn<>("First name");
        firstNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));

        TableColumn<UserDTO, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));

        table.getColumns().addAll(idCol, usernameCol, firstNameCol, lastNameCol);

        filterCombo.getItems().addAll("All", "ID", "Username", "First Name", "Last Name");
        filterCombo.setValue("All");
    }

    // Dialogs
    private void showUserInfoDialog(UserDTO teacher) {
        CourseService courseService = new CourseService();
        List<CourseDTO> courses = courseService.findByUserId(teacher.getId());

        StringBuilder courseList = new StringBuilder();

        if (courses.isEmpty()) {
            courseList.append("No courses assigned.");
        } else {
            for (CourseDTO c : courses) {
                courseList.append("• ")
                        .append(c.getName())
                        .append(" (")
                        .append(c.getCode())
                        .append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Teacher Info");

        alert.setHeaderText(teacher.getFirstName() + " " + teacher.getLastName());

        alert.setContentText(
                "Username: " + teacher.getUsername() + "\n\n" +
                        "Courses:\n" + courseList
        );

        alert.showAndWait();
    }

    private void showAddTeacherDialog() {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Teacher");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        VBox box = new VBox(10, firstNameField, lastNameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                UserDTO dto = new UserDTO();
                dto.setFirstName(firstNameField.getText());
                dto.setLastName(lastNameField.getText());
                return dto;
            }
            return null;
        });

        UserDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            userService.createTeacher(result.getFirstName(), result.getLastName());
            loadAndShowItems();
        }
    }

    private void showDeleteTeacherDialog(UserDTO teacher) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Teacher");
        alert.setHeaderText("Are you sure you want to delete \"" + teacher.getUsername() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            userService.deleteUser(teacher.getId());
            loadAndShowItems();
        }
    }
}
