package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.utils.CourseMapper;
import com.edujournal.entity.Course;
import com.edujournal.model.CourseDTO;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CourseController extends BorderPane {

    private final CourseService courseService = new CourseService();
    private final CourseMapper courseMapper = new CourseMapper();

    private final String role;

    private TableView<CourseDTO> table;

    public CourseController(String role) {
        this.role = role;
        buildUI();
        loadCourses();
    }

    private void buildUI() {
        table = new TableView<>();

        TableColumn<CourseDTO, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCode()));

        TableColumn<CourseDTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        table.getColumns().addAll(codeCol, nameCol);

        // Double click opens information about the course
        table.setRowFactory(tv -> {
            TableRow<CourseDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    CourseDTO course = row.getItem();
                    showCourseInfoDialog(course);
                }
            });
            return row;
        });

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button addAssessmentBtn = new Button("Add Assessment");

        addBtn.setOnAction(e -> showAddCourseDialog());

        editBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showEditCourseDialog(selected);
        });

        deleteBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showDeleteDialog(selected);
        });

        addAssessmentBtn.setOnAction(e -> {
            CourseDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showAssessmentDialog(selected);
            }
        });

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10));

        // Buttons available by roles
        if (role.equals("Administrator")) {
            actions.getChildren().addAll(addBtn, editBtn, deleteBtn);
        } else if (role.equals("Teacher")) {
            actions.getChildren().add(addAssessmentBtn);
        }

        VBox layout = new VBox(10, table, actions);
        layout.setPadding(new Insets(10));

        setCenter(layout);
    }

    private void loadCourses() {
        List<CourseDTO> courses = courseService.findAll();
        table.getItems().setAll(courses);
    }

    // Dialogs

    // TODO: replace with pop-up windows
    private void showCourseInfoDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Info");
        alert.setHeaderText(course.getName());
        alert.setContentText("Code: " + course.getCode());
        alert.showAndWait();
    }

    private void showAddCourseDialog() {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Add Course");

        TextField nameField = new TextField();
        nameField.setPromptText("Course name");

        TextField codeField = new TextField();
        codeField.setPromptText("Course code");

        VBox box = new VBox(10, nameField, codeField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                CourseDTO dto = new CourseDTO();
                dto.setCode(codeField.getText());
                dto.setName(nameField.getText());
                dto.setUserId(null);
                return dto;
            }
            return null;
        });

        CourseDTO result = dialog.showAndWait().orElse(null);

        if (result != null) {
            Course entity = courseMapper.toEntity(result);
            courseService.save(entity);
            loadCourses();
        }
    }

    private void showEditCourseDialog(CourseDTO course) {
        Dialog<CourseDTO> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");

        TextField nameField = new TextField(course.getName());
        TextField codeField = new TextField(course.getCode());

        VBox box = new VBox(10, nameField, codeField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                course.setName(nameField.getText());
                course.setCode(codeField.getText());
                return course;
            }
            return null;
        });

        CourseDTO updated = dialog.showAndWait().orElse(null);

        if (updated != null) {
            Course entity = courseMapper.toEntity(updated);
            courseService.update(entity);
            loadCourses();
        }
    }

    private void showDeleteDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Are you sure you want to delete \"" + course.getName() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        if (alert.showAndWait().orElse(no) == yes) {
            courseService.delete(course.getId());
            loadCourses();
        }
    }

    // TODO: replace with pop-up windows
    private void showAssessmentDialog(CourseDTO course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Assessment");
        alert.setHeaderText("Teacher functionality");
        alert.setContentText("Here you will add assessments for: " + course.getName());
        alert.showAndWait();
    }
}
