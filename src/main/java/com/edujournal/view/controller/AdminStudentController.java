package com.edujournal.view.controller;

import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.model.UserDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class AdminStudentController extends BaseController<Student> {

    private final StudentService studentService = new StudentService();
    private final UserService userService = new UserService();
    private final Role role;

    public AdminStudentController(Role role) {
        this.role = role;
        configureColumns();
        loadAndShowItems();
    }

    @Override
    protected List<Student> loadAllItems() {
        return studentService.findAll();
    }

    @Override
    protected boolean matchesFilter(
            Student student,
            String filter,
            String searchText
    ) {
        if (searchText == null || searchText.isBlank()) {
            return true;
        }

        String text = searchText.toLowerCase();

        if (filter == null || filter.equals("All")) {
            return matchesAllFields(
                    List.of(
                            student.getId() != null
                                    ? student.getId().toString()
                                    : null,
                            student.getStudentNumber()
                    ),
                    text
            );
        }

        return switch (filter) {
            case "ID" -> student.getId() != null
                    && student.getId().toString().contains(text);
            case "Student Number" -> student.getStudentNumber() != null
                    && student.getStudentNumber().toLowerCase().contains(text);
            case "First Name" -> {
                User user = student.getUserId() != null ? userService.findById(student.getUserId()) : null;
                yield user != null && user.getFirstName() != null
                        && user.getFirstName().toLowerCase().contains(text);
            }
            case "Last Name" -> {
                User user = student.getUserId() != null ? userService.findById(student.getUserId()) : null;
                yield user != null && user.getLastName() != null
                        && user.getLastName().toLowerCase().contains(text);
            }
            default -> true;
        };
    }

    @Override
    protected void configureColumns() {

        TableColumn<Student, String> idCol =
                new TableColumn<>("ID");

        idCol.setCellValueFactory(
                cellData -> cellData.getValue()
                        .getId() == null
                        ? null
                        : new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getId().toString()
                )
        );

        TableColumn<Student, String> studentNumberCol =
                new TableColumn<>("Student Number");

        studentNumberCol.setCellValueFactory(
                new PropertyValueFactory<>("studentNumber")
        );

        TableColumn<Student, String> firstNameCol =
                new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUserId() != null
                    ? userService.findById(cellData.getValue().getUserId()) : null;
            return new SimpleStringProperty(user != null ? user.getFirstName() : "");
        });

        TableColumn<Student, String> lastNameCol =
                new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUserId() != null
                    ? userService.findById(cellData.getValue().getUserId()) : null;
            return new SimpleStringProperty(user != null ? user.getLastName() : "");
        });


        table.getColumns().addAll(
                idCol,
                studentNumberCol,
                firstNameCol,
                lastNameCol
        );

        filterCombo.getItems().addAll(
                "All",
                "ID",
                "Student Number",
                "First Name",
                "Last Name"
        );

        filterCombo.setValue("All");
    }

    @Override
    protected void showAddDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField studentNumberField = new TextField();
        studentNumberField.setPromptText("Student Number");

        VBox box = new VBox(10, firstNameField, lastNameField, studentNumberField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "First name and last name cannot be empty.").showAndWait();
                    return null;
                }
                if (studentNumberField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "Student number cannot be empty.").showAndWait();
                    return null;
                }
                if (studentService.findByStudentNumber(studentNumberField.getText().trim()) != null) {
                    new Alert(Alert.AlertType.ERROR, "Student number already exists.").showAndWait();
                    return null;
                }
                Student s = new Student();
                s.setStudentNumber(studentNumberField.getText().trim());
                return s;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> {
            UserDTO user = userService.createStudent(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim()
            );
            student.setUserId(user.getId());
            studentService.save(student);
            loadAndShowItems();
        });
    }

    @Override
    protected void showEditDialog() {
        Student student = table.getSelectionModel().getSelectedItem();
        if (student == null) return;

        User user = student.getUserId() != null ? userService.findById(student.getUserId()) : null;

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");

        TextField firstNameField = new TextField(user != null ? user.getFirstName() : "");
        TextField lastNameField = new TextField(user != null ? user.getLastName() : "");
        TextField studentNumberField = new TextField(student.getStudentNumber());

        VBox box = new VBox(10, firstNameField, lastNameField, studentNumberField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "First name and last name cannot be empty.").showAndWait();
                    return null;
                }
                if (studentNumberField.getText().isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "Student number cannot be empty.").showAndWait();
                    return null;
                }
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(ok -> {
            if (user != null) {
                user.setFirstName(firstNameField.getText().trim());
                user.setLastName(lastNameField.getText().trim());
                userService.update(user);
            }
            student.setStudentNumber(studentNumberField.getText().trim());
            studentService.update(student);
            loadAndShowItems();
        });
    }

    @Override
    protected void showDeleteDialog() {
        Student student = table.getSelectionModel().getSelectedItem();
        if (student == null) return;

        User user = student.getUserId() != null ? userService.findById(student.getUserId()) : null;
        String name = user != null ? user.getFirstName() + " " + user.getLastName() : student.getStudentNumber();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Are you sure you want to delete \"" + name + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            studentService.delete(student.getId());
            if (student.getUserId() != null) userService.deleteUser(student.getUserId());
            loadAndShowItems();
        }
    }

    @Override
    protected void onView() {
        Student student = table.getSelectionModel().getSelectedItem();
        if (student == null) return;

        User user = student.getUserId() != null ? userService.findById(student.getUserId()) : null;
        String name = user != null ? user.getFirstName() + " " + user.getLastName() : "—";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Info");
        alert.setHeaderText(name);
        alert.setContentText(
                "Student Number: " + student.getStudentNumber() + "\n" +
                (user != null ? "Username: " + user.getUsername() : "")
        );
        alert.showAndWait();
    }
}

