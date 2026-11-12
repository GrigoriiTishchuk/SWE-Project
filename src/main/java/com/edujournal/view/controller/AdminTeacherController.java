package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.UserMapper;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.entity.User;
import com.edujournal.model.CourseDTO;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing teacher accounts. Provides functionality for viewing,
 * searching, creating, editing and deleting teachers.
 */
public class AdminTeacherController extends BaseController<UserDTO> {
    private final UserService userService = new UserService();
    private final UserMapper userMapper = new UserMapper();

    private static final String FILTER_USERNAME = "Username";
    private static final String FILTER_FIRST_NAME = "First Name";
    private static final String FILTER_LAST_NAME = "Last Name";
    private static final String FILTER_PHONE_NUMBER = "Phone Number";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PHONE = "phone";

    /**
     * Creates the teacher administration controller.
     *
     * @param role current user role
     */
    public AdminTeacherController(Role role) {
        configureColumns();
        loadAndShowItems();
    }

    /**
     * Loads all teachers.
     *
     * @return list of teacher DTOs
     */
    @Override
    protected List<UserDTO> loadAllItems() {
        return userService.findAllTeachers();
    }

    /**
     * Checks whether a teacher matches the selected filter and search criteria.
     *
     * @param teacher teacher DTO
     * @param filter selected filter
     * @param searchText search text
     * @return true if teacher matches
     */
    @Override
    protected boolean matchesFilter(UserDTO teacher, String filter, String searchText) {
        searchText = searchText.toLowerCase();

        switch (filter) {
            case "ID":
                return String.valueOf(teacher.getId()).contains(searchText);

            case FILTER_USERNAME:
                return teacher.getUsername() != null
                        && teacher.getUsername().toLowerCase().contains(searchText);

            case FILTER_FIRST_NAME:
                return teacher.getFirstName() != null
                        && teacher.getFirstName().toLowerCase().contains(searchText);

            case FILTER_LAST_NAME:
                return teacher.getLastName() != null
                        && teacher.getLastName().toLowerCase().contains(searchText);

            case "Email":
                return teacher.getEmail() != null
                        && teacher.getEmail().toLowerCase().contains(searchText);

            case FILTER_PHONE_NUMBER:
                return teacher.getPhone() != null
                        && teacher.getPhone().toLowerCase().contains(searchText);

            case "All":
            default:
                return matchesAllFields(
                        List.of(
                                String.valueOf(teacher.getId()),
                                safe(teacher.getUsername()),
                                safe(teacher.getFirstName()),
                                safe(teacher.getLastName()),
                                safe(teacher.getEmail()),
                                safe(teacher.getPhone())),
                        searchText);
        }
    }

    /** Configures table columns and filter options. */
    @Override
    protected void configureColumns() {
        TableColumn<UserDTO, String> idCol = new TableColumn<>(LocalizationManager.get("id"));
        idCol.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<UserDTO, String> usernameCol =
                new TableColumn<>(LocalizationManager.get(KEY_USERNAME));
        usernameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));

        TableColumn<UserDTO, String> firstNameCol =
                new TableColumn<>(LocalizationManager.get(KEY_FIRST_NAME));
        firstNameCol.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getFirstName()));

        TableColumn<UserDTO, String> lastNameCol =
                new TableColumn<>(LocalizationManager.get(KEY_LAST_NAME));
        lastNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));

        TableColumn<UserDTO, String> emailCol = new TableColumn<>(LocalizationManager.get("email"));
        emailCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<UserDTO, String> phoneCol =
                new TableColumn<>(LocalizationManager.get(KEY_PHONE));
        phoneCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));

        table.getColumns()
                .addAll(idCol, usernameCol, firstNameCol, lastNameCol, emailCol, phoneCol);

        filterCombo
                .getItems()
                .addAll(
                        "All",
                        "ID",
                        FILTER_USERNAME,
                        FILTER_FIRST_NAME,
                        FILTER_LAST_NAME,
                        FILTER_PHONE_NUMBER);
        filterCombo.setValue("All");

        Map<String, String> filterKeyMap =
                Map.of(
                        "All",
                        "all",
                        "ID",
                        "id",
                        FILTER_USERNAME,
                        KEY_USERNAME,
                        FILTER_FIRST_NAME,
                        KEY_FIRST_NAME,
                        FILTER_LAST_NAME,
                        KEY_LAST_NAME,
                        FILTER_PHONE_NUMBER,
                        KEY_PHONE);
        filterCombo.setCellFactory(list -> createLocalizedCell(filterKeyMap));
        filterCombo.setButtonCell(createLocalizedCell(filterKeyMap));
    }

    // Dialogs
    /** Opens a dialog for creating a new teacher. */
    @Override
    public void showAddDialog() {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("addTeacher"));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText(LocalizationManager.get(KEY_FIRST_NAME));

        TextField lastNameField = new TextField();
        lastNameField.setPromptText(LocalizationManager.get(KEY_LAST_NAME));

        TextField phoneField = new TextField();
        phoneField.setPromptText(LocalizationManager.get(KEY_PHONE));

        VBox box = new VBox(10, firstNameField, lastNameField, phoneField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn =
                new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        Button saveBtnNode = (Button) dialog.getDialogPane().lookupButton(saveBtn);
        saveBtnNode.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                        showError(LocalizationManager.get("errorNameEmpty"));
                        event.consume();
                    } else if (phoneField.getText().isBlank()) {
                        showError(LocalizationManager.get("errorPhoneEmpty"));
                        event.consume();
                    }
                });

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (firstNameField.getText().isBlank()
                        || lastNameField.getText().isBlank()
                        || phoneField.getText().isBlank()) {
                    return null;
                }

                UserDTO dto = new UserDTO();
                dto.setFirstName(firstNameField.getText().trim());
                dto.setLastName(lastNameField.getText().trim());
                dto.setPhone(phoneField.getText().trim());
                return dto;
            }
            return null;
        });

        UserDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            userService.createTeacher(
                    result.getFirstName(), result.getLastName(), result.getPhone());
            loadAndShowItems();
        }
    }

    /** Opens a dialog for editing the selected teacher. */
    @Override
    protected void showEditDialog() {

        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("editTeacher"));

        TextField firstNameField =
                new TextField(teacher.getFirstName() != null ? teacher.getFirstName() : "");
        TextField lastNameField =
                new TextField(teacher.getLastName() != null ? teacher.getLastName() : "");

        VBox box = new VBox(10, firstNameField, lastNameField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn =
                new ButtonType(LocalizationManager.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {

                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    showError(LocalizationManager.get("errorNameEmpty"));
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

    /** Opens a confirmation dialog and deletes the selected teacher if confirmed. */
    @Override
    protected void showDeleteDialog() {
        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LocalizationManager.get("deleteTeacher"));
        alert.setHeaderText(
                LocalizationManager.get("sureToDelete") + " \"" + teacher.getUsername() + "\"?");
        alert.setContentText(LocalizationManager.get("undone"));

        ButtonType yes =
                new ButtonType(LocalizationManager.get("yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType no =
                new ButtonType(LocalizationManager.get("no"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            userService.deleteUser(teacher.getId());
            loadAndShowItems();
        }
    }

    /** Displays detailed information about the selected teacher. */
    @Override
    protected void onView() {
        UserDTO teacher = table.getSelectionModel().getSelectedItem();
        if (teacher == null) return;

        CourseService courseService = new CourseService();
        List<CourseDTO> courses = courseService.findByUserId(teacher.getId());

        StringBuilder courseList = new StringBuilder();

        if (courses.isEmpty()) {
            courseList.append(LocalizationManager.get("noCourseAssigned"));
        } else {
            for (CourseDTO c : courses) {
                courseList
                        .append("• ")
                        .append(c.getName())
                        .append(" (")
                        .append(c.getCode())
                        .append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LocalizationManager.get("teacherInfo"));
        alert.setHeaderText(teacher.getFirstName() + " " + teacher.getLastName());
        alert.setContentText(
                LocalizationManager.get(KEY_USERNAME)
                        + ": "
                        + teacher.getUsername()
                        + "\n\n"
                        + LocalizationManager.get("courses")
                        + ":\n"
                        + courseList);

        alert.showAndWait();
    }
}
