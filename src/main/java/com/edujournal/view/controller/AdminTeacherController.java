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
        loadAndShowItems();
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

            case "Phone":
                return teacher.getPhone() != null &&
                        teacher.getPhone().toLowerCase().contains(searchText);

            case "All":
            default:
                return matchesAllFields(
                        List.of(
                                String.valueOf(teacher.getId()),
                                safe(teacher.getUsername()),
                                safe(teacher.getFirstName()),
                                safe(teacher.getLastName()),
                                safe(teacher.getPhone())
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

        TableColumn<UserDTO, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<UserDTO, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));

        table.getColumns().addAll(idCol, usernameCol, firstNameCol, lastNameCol, emailCol, phoneCol);

        filterCombo.getItems().addAll("All", "ID", "Username", "First Name", "Last Name", "Phone Number");
        filterCombo.setValue("All");
    }

    // Dialogs
    @Override
    public void showAddDialog() {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Teacher");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        VBox box = new VBox(10, firstNameField, lastNameField, phoneField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "First name and last name cannot be empty.").showAndWait();
                    return null;
                }

                UserDTO dto = new UserDTO();
                dto.setFirstName(firstNameField.getText());
                dto.setLastName(lastNameField.getText());
                dto.setPhone(phoneField.getText());
                return dto;
            }
            return null;
        });

        UserDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            userService.createTeacher(result.getFirstName(), result.getLastName(), result.getPhone());
            loadAndShowItems();
        }
    }

    @Override
    protected void showEditDialog() {

        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Teacher");

        TextField firstNameField = new TextField(teacher.getFirstName());
        TextField lastNameField = new TextField(teacher.getLastName());

        VBox box = new VBox(10, firstNameField, lastNameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {

                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "First name and last name cannot be empty.").showAndWait();
                    return null;
                }

                teacher.setFirstName(firstNameField.getText().trim());
                teacher.setLastName(lastNameField.getText().trim());
                return teacher;
            }
            return null;
        });

        UserDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            User entity = userMapper.toEntity(updated);
            userService.update(entity);
            loadAndShowItems();
        }
    }

    @Override
    protected void showDeleteDialog() {
        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

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

    @Override
    protected void onView() {
        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

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

}
