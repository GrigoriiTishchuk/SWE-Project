package com.edujournal.view.controller;

import com.edujournal.backend.service.StudentService;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminStudentController extends BaseController<Student> {

    private final StudentService studentService = new StudentService();
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
                            student.getStudentNumber(),
                            student.getFirstName(),
                            student.getLastName()
                    ),
                    text
            );
        }

        return switch (filter) {
            case "ID" -> student.getId() != null
                    && student.getId().toString().contains(text);

            case "Student Number" -> student.getStudentNumber() != null
                    && student.getStudentNumber().toLowerCase().contains(text);

            case "First Name" -> student.getFirstName() != null
                    && student.getFirstName().toLowerCase().contains(text);

            case "Last Name" -> student.getLastName() != null
                    && student.getLastName().toLowerCase().contains(text);

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

        firstNameCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFirstName() != null
                                ? cellData.getValue().getFirstName()
                                : ""
                )
        );

        TableColumn<Student, String> lastNameCol =
                new TableColumn<>("Last Name");

        lastNameCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getLastName() != null
                                ? cellData.getValue().getLastName()
                                : ""
                )
        );

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
        // Will be implemented later
    }

    @Override
    protected void showEditDialog() {
        // Will be implemented later
    }

    @Override
    protected void showDeleteDialog() {
        // Will be implemented later
    }

    @Override
    protected void onView() {
        Student selectedStudent =
                table.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            return;
        }

        System.out.println(
                "Selected student: "
                        + selectedStudent.getStudentNumber()
                        + " - "
                        + selectedStudent.getFirstName()
                        + " "
                        + selectedStudent.getLastName()
        );
    }
}

