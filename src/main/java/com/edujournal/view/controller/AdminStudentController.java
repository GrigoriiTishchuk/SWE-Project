package com.edujournal.view.controller;

import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.GeneratorUtil;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.model.StudentDTO;
import com.edujournal.entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class AdminStudentController extends BaseController<StudentDTO> {

    private final StudentService studentService = new StudentService();
    private final UserService userService = new UserService();

    public AdminStudentController(Role role) {
        configureColumns();
        loadAndShowItems();
    }

    @Override
    protected List<StudentDTO> loadAllItems() {
        return studentService.findAllDTO();
    }

    @Override
    protected boolean matchesFilter(StudentDTO dto, String filter, String text) {
        if (text == null || text.isBlank()) return true;
        text = text.toLowerCase();

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(
                    List.of(
                            dto.getStudentId() != null ? dto.getStudentId().toString() : null,
                            dto.getStudentNumber(),
                            dto.getUsername(),
                            dto.getFirstName(),
                            dto.getLastName()
                    ),
                    text
            );
        }

        return switch (filter) {
            case "ID" -> dto.getStudentId() != null &&
                    dto.getStudentId().toString().contains(text);

            case "Student Number" -> dto.getStudentNumber() != null &&
                    dto.getStudentNumber().toLowerCase().contains(text);

            case "Username" -> dto.getLastName() != null &&
                    dto.getUsername().toLowerCase().contains(text);

            case "First Name" -> dto.getFirstName() != null &&
                    dto.getFirstName().toLowerCase().contains(text);

            case "Last Name" -> dto.getLastName() != null &&
                    dto.getLastName().toLowerCase().contains(text);

            default -> true;
        };
    }

    @Override
    protected void configureColumns() {

        TableColumn<StudentDTO, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getStudentId() != null
                                ? cell.getValue().getStudentId().toString()
                                : ""
                )
        );

        TableColumn<StudentDTO, String> studentNumberCol =
                new TableColumn<>("Student Number");
        studentNumberCol.setCellValueFactory(
                new PropertyValueFactory<>("studentNumber")
        );

        TableColumn<StudentDTO, String> usernameCol =
                new TableColumn<>("Username");
        usernameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getUsername())
        );

        TableColumn<StudentDTO, String> firstNameCol =
                new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getFirstName())
        );

        TableColumn<StudentDTO, String> lastNameCol =
                new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getLastName())
        );

        table.getColumns().addAll(
                idCol,
                studentNumberCol,
                usernameCol,
                firstNameCol,
                lastNameCol
        );

        filterCombo.getItems().addAll(
                "All",
                "ID",
                "Student Number",
                "Username",
                "First Name",
                "Last Name"
        );

        filterCombo.setValue("All");
    }

    @Override
    protected void showAddDialog() {
        Dialog<StudentDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Student");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField phoneField = new TextField();

        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        phoneField.setPromptText("Phone Number");

        VBox box = new VBox(10, firstNameField, lastNameField, phoneField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (firstNameField.getText().isBlank() ||
                        lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "First name and last name cannot be empty.").showAndWait();
                    return null;
                }

                if (phoneField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Phone cannot be empty.").showAndWait();
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

        dialog.showAndWait().ifPresent(dto -> {
            StudentDTO created = studentService.createStudent(
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getPhone()
            );

            loadAndShowItems();
        });
    }

    @Override
    protected void showEditDialog() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");

        TextField firstNameField = new TextField(dto.getFirstName());
        TextField lastNameField = new TextField(dto.getLastName());
        TextField phoneField = new TextField(dto.getPhone());

        VBox box = new VBox(10, firstNameField, lastNameField, phoneField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                if (firstNameField.getText().isBlank() ||
                        lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "First name and last name cannot be empty.").showAndWait();
                    return null;
                }
                if (phoneField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR,
                            "Phone cannot be empty.").showAndWait();
                    return null;
                }
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(ok -> {
            dto.setFirstName(firstNameField.getText().trim());
            dto.setLastName(lastNameField.getText().trim());
            dto.setPhone(phoneField.getText().trim());

            studentService.updateFromDTO(dto);
            loadAndShowItems();
        });
    }

    @Override
    protected void showDeleteDialog() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Are you sure you want to delete \"" +
                dto.getFirstName() + " " + dto.getLastName() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
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

    @Override
    protected void onView() {
        StudentDTO dto = table.getSelectionModel().getSelectedItem();
        if (dto == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Info");
        alert.setHeaderText(dto.getFirstName() + " " + dto.getLastName());
        alert.setContentText(
                "Student Number: " + dto.getStudentNumber() + "\n" +
                        "Username: " + dto.getUsername() + "\n" +
                        "Email: " + dto.getEmail() + "\n" +
                        "Phone: " + dto.getPhone()
        );
        alert.showAndWait();
    }
}
