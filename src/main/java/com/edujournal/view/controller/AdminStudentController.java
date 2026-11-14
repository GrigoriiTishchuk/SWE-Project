package com.edujournal.view.controller;

import com.edujournal.Main;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.model.StudentDTO;
import com.edujournal.view.admin.AdminSidebar;
import com.edujournal.view.common.StudentReportPage;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing students. Provides functionality for viewing, searching,
 * creating, editing and deleting student records.
 */
public class AdminStudentController extends BaseController<StudentDTO> {

    private final StudentService studentService = new StudentService();
    private final UserService userService = new UserService();

    private static final String FILTER_STUDENT_NUMBER = "Student Number";
    private static final String FILTER_USERNAME = "Username";
    private static final String FILTER_FIRST_NAME = "First Name";
    private static final String FILTER_LAST_NAME = "Last Name";
    private static final String FILTER_PHONE_NUMBER = "Phone Number";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_FIRST_NAME = "firstName";

    /**
     * Creates the student administration controller and initializes table configuration, report
     * actions and selection handling.
     *
     * @param role current user role
     */
    public AdminStudentController(Role role) {
        configureColumns();
        loadAndShowItems();
        reportBtn.setVisible(true);
        reportBtn.setManaged(true);
        reportBtn.setDisable(true);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldStudent, newStudent) -> {
                    reportBtn.setDisable(newStudent == null);
                });
        reportBtn.setOnAction(e -> openStudentReport());
    }

    /**
     * Loads all students as DTO objects.
     *
     * @return list of students
     */
    @Override
    protected List<StudentDTO> loadAllItems() {
        return studentService.findAllDTO();
    }

    /**
     * Checks whether a student matches the selected filter and search text.
     *
     * @param dto student record
     * @param filter selected filter
     * @param text search text
     * @return true if the student matches
     */
    @Override
    protected boolean matchesFilter(StudentDTO dto, String filter, String text) {
        if (text == null || text.isBlank()) return true;
        text = text.toLowerCase();

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(
                    List.of(
                            dto.getStudentId() != null ? dto.getStudentId().toString() : null,
                            safe(dto.getStudentNumber()),
                            safe(dto.getUsername()),
                            safe(dto.getFirstName()),
                            safe(dto.getLastName()),
                            safe(dto.getEmail()),
                            safe(dto.getPhone())),
                    text);
        }

        return switch (filter) {
            case "ID" -> dto.getStudentId() != null && dto.getStudentId().toString().contains(text);

            case FILTER_STUDENT_NUMBER ->
                    dto.getStudentNumber() != null
                            && dto.getStudentNumber().toLowerCase().contains(text);

            case FILTER_USERNAME ->
                    dto.getUsername() != null && dto.getUsername().toLowerCase().contains(text);

            case FILTER_FIRST_NAME ->
                    dto.getFirstName() != null && dto.getFirstName().toLowerCase().contains(text);

            case FILTER_LAST_NAME ->
                    dto.getLastName() != null && dto.getLastName().toLowerCase().contains(text);

            case FILTER_PHONE_NUMBER ->
                    dto.getPhone() != null && dto.getPhone().toLowerCase().contains(text);

            default -> true;
        };
    }

    /** Configures table columns and filter options. */
    @Override
    protected void configureColumns() {

        TableColumn<StudentDTO, String> idCol = new TableColumn<>(LocalizationManager.get("id"));
        idCol.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStudentId() != null
                                ? cell.getValue().getStudentId().toString()
                                : ""));

        TableColumn<StudentDTO, String> studentNumberCol =
                new TableColumn<>(LocalizationManager.get("studentNumberCol"));
        studentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));

        TableColumn<StudentDTO, String> usernameCol =
                new TableColumn<>(LocalizationManager.get(KEY_USERNAME));
        usernameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getUsername()));

        TableColumn<StudentDTO, String> firstNameCol =
                new TableColumn<>(LocalizationManager.get(KEY_FIRST_NAME));
        firstNameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getFirstName()));

        TableColumn<StudentDTO, String> lastNameCol =
                new TableColumn<>(LocalizationManager.get(KEY_LAST_NAME));
        lastNameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getLastName()));

        TableColumn<StudentDTO, String> emailCol =
                new TableColumn<>(LocalizationManager.get(KEY_EMAIL));
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<StudentDTO, String> phoneCol =
                new TableColumn<>(LocalizationManager.get(KEY_PHONE));
        phoneCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));

        table.getColumns()
                .addAll(
                        idCol,
                        studentNumberCol,
                        usernameCol,
                        firstNameCol,
                        lastNameCol,
                        emailCol,
                        phoneCol);

        filterCombo
                .getItems()
                .addAll(
                        "All",
                        "ID",
                        FILTER_STUDENT_NUMBER,
                        FILTER_USERNAME,
                        FILTER_FIRST_NAME,
                        FILTER_LAST_NAME,
                        "Email",
                        FILTER_PHONE_NUMBER);
        filterCombo.setValue("All");

        Map<String, String> filterKeyMap =
                Map.of(
                        "All",
                        "all",
                        "ID",
                        "id",
                        FILTER_STUDENT_NUMBER,
                        "studentNumberCol",
                        FILTER_USERNAME,
                        KEY_USERNAME,
                        FILTER_FIRST_NAME,
                        KEY_FIRST_NAME,
                        FILTER_LAST_NAME,
                        KEY_LAST_NAME,
                        "Email",
                        KEY_EMAIL,
                        FILTER_PHONE_NUMBER,
                        KEY_PHONE);
        filterCombo.setCellFactory(list -> createLocalizedCell(filterKeyMap));
        filterCombo.setButtonCell(createLocalizedCell(filterKeyMap));
    }

    /** Opens the report page for the selected student. */
    private void openStudentReport() {
        StudentDTO selectedStudent = table.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            return;
        }

        Main.showPage(
                new StudentReportPage(
                        AdminSidebar.build("Student's report"),
                        Role.ADMINISTRATOR,
                        selectedStudent.getStudentId()));
    }

    /** Opens a dialog for creating a new student. */
    @Override
    public void showAddDialog() {
        Dialog<StudentDTO> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("addStudent"));

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField phoneField = new TextField();

        firstNameField.setPromptText(LocalizationManager.get(KEY_FIRST_NAME));
        lastNameField.setPromptText(LocalizationManager.get(KEY_LAST_NAME));
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
                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    return null;
                }

                if (phoneField.getText().isBlank()) {
                    return null;
                }

                StudentDTO dto = new StudentDTO();
                dto.setFirstName(firstNameField.getText().trim());
                dto.setLastName(lastNameField.getText().trim());
                dto.setPhone(phoneField.getText().trim());
                return dto;
            }
            return null;
        });

        dialog.showAndWait()
                .ifPresent(dto -> {
                    studentService.createStudent(
                            dto.getFirstName(), dto.getLastName(), dto.getPhone());

                    loadAndShowItems();
                });
    }

    /** Opens a dialog for editing the selected student. */
    @Override
    protected void showEditDialog() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("editStudent"));

        TextField firstNameField =
                new TextField(dto.getFirstName() != null ? dto.getFirstName() : "");
        TextField lastNameField = new TextField(dto.getLastName() != null ? dto.getLastName() : "");
        TextField phoneField = new TextField(dto.getPhone() != null ? dto.getPhone() : "");

        VBox box = new VBox(10, firstNameField, lastNameField, phoneField);
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
                if (phoneField.getText().isBlank()) {
                    showError(LocalizationManager.get("errorPhoneEmpty"));
                    return null;
                }
                return true;
            }
            return null;
        });

        dialog.showAndWait()
                .ifPresent(ok -> {
                    dto.setFirstName(firstNameField.getText().trim());
                    dto.setLastName(lastNameField.getText().trim());
                    dto.setPhone(phoneField.getText().trim());

                    studentService.updateFromDTO(dto);
                    loadAndShowItems();
                });
    }

    /** Opens a confirmation dialog and deletes the selected student if confirmed. */
    @Override
    protected void showDeleteDialog() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LocalizationManager.get("deleteStudent"));
        alert.setHeaderText(
                LocalizationManager.get("sureToDelete")
                        + " \""
                        + dto.getFirstName()
                        + " "
                        + dto.getLastName()
                        + "\"?");
        alert.setContentText(LocalizationManager.get("undone"));

        ButtonType yes =
                new ButtonType(LocalizationManager.get("yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType no =
                new ButtonType(LocalizationManager.get("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            try {
                studentService.delete(dto.getStudentId());
                userService.deleteUser(dto.getUserId());
                loadAndShowItems();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
    }

    /** Displays detailed information about the selected student. */
    @Override
    protected void onView() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LocalizationManager.get("studentInfo"));
        alert.setHeaderText(dto.getFirstName() + " " + dto.getLastName());
        alert.setContentText(
                LocalizationManager.get("studentNumber")
                        + ": "
                        + dto.getStudentNumber()
                        + "\n"
                        + LocalizationManager.get(KEY_USERNAME)
                        + ": "
                        + dto.getUsername()
                        + "\n"
                        + LocalizationManager.get(KEY_EMAIL)
                        + ": "
                        + dto.getEmail()
                        + "\n"
                        + LocalizationManager.get(KEY_PHONE)
                        + ": "
                        + dto.getPhone());
        alert.showAndWait();
    }
}
