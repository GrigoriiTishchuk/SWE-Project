package com.edujournal.view.controller;

import com.edujournal.backend.service.CourseService;
import com.edujournal.model.CourseDTO;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CourseController extends BorderPane {

    private final CourseService courseService = new CourseService();

    private TableView<CourseDTO> table;

    public CourseController() {
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

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");

        HBox actions = new HBox(10, addBtn, editBtn, deleteBtn);
        actions.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, actions);
        layout.setPadding(new Insets(10));

        setCenter(layout);
    }

    private void loadCourses() {
        List<CourseDTO> courses = courseService.findAll();
        table.getItems().setAll(courses);
    }
}
