package com.edujournal.view.controller;

import com.edujournal.backend.service.StudentService;
import com.edujournal.entity.Role;
import com.edujournal.model.StudentDTO;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StudentReportController {

    private final Role role;
    private final Integer studentId;

    private final StudentService studentService =
            new StudentService();

    public StudentReportController(Role role, Integer studentId) {
        this.role = role;
        this.studentId = studentId;
    }

    public VBox buildReport() {

        VBox box = new VBox(12);

        if (studentId == null) {
            Label message = new Label(
                    "Select a student from Student Management " +
                            "to generate a report."
            );

            box.getChildren().add(message);
            return box;
        }

        StudentDTO student =
                studentService.findDTOById(studentId);

        if (student == null) {
            box.getChildren().add(
                    new Label("Student not found.")
            );
            return box;
        }

        // TODO:
        // 1. Student info card
        // 2. Assessments
        // 3. Grades
        // 4. Summary

        return box;
    }
}