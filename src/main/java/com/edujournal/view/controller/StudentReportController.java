package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.StudentReportService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.AssessmentType;
import com.edujournal.entity.Role;
import com.edujournal.model.AcademicGroupDTO;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.StudentReportDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentReportController {

    private final Role role;
    private final Integer studentId;

    private final StudentService studentService = new StudentService();
    private final StudentReportService reportService = new StudentReportService();
    private final AcademicGroupService groupService = new AcademicGroupService();

    public StudentReportController(Role role, Integer studentId) {
        this.role = role;
        this.studentId = studentId;
    }

    public VBox buildReport() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(10));

        if (studentId == null) {
            root.getChildren().add(new Label("Select a student from Student Management to generate a report."));
            return root;
        }

        StudentDTO student = studentService.findDTOById(studentId);

        if (student == null) {
            root.getChildren().add(new Label("Student not found."));
            return root;
        }

        root.getChildren().add(buildStudentCard(student));

        List<StudentReportDTO> report = reportService.buildReport(studentId);

        if (report.isEmpty()) {
            root.getChildren().add(new Label("No assessment records found."));
            return root;
        }

        addCourseSections(root, report);
        root.getChildren().add(buildOverallGradeSection());
        return root;
    }

    private VBox buildStudentCard(StudentDTO student) {
        String groupName = findGroupName(student.getAcademicGroupId());

        VBox card = new VBox(5);
        card.getChildren().addAll(
                new Label(student.getFirstName() + " " + student.getLastName()),
                new Label("Group: " + groupName),
                new Label("Student Number: " + student.getStudentNumber())
        );
        return card;
    }

    private String findGroupName(Integer groupId) {
        if (groupId == null) {
            return "-";
        }
        AcademicGroup group = groupService.findById(groupId);
        return group != null ? group.getName() : "-";
    }

    private void addCourseSections(VBox root, List<StudentReportDTO> report) {
        Map<Integer, List<StudentReportDTO>> courses = report.stream().collect(
                Collectors.groupingBy(
                        StudentReportDTO::getCourseId,
                        LinkedHashMap::new,
                        Collectors.toList()
                )
        );
        for (List<StudentReportDTO> records : courses.values()) {
            if (records.isEmpty()) {
                continue;
            }
            StudentReportDTO first = records.get(0);
            Label courseTitle = new Label(first.getCourseName());

            root.getChildren().add(courseTitle);
            root.getChildren().add(createCourseTable(records));
        }
    }

    private TableView<StudentReportDTO> createCourseTable(List<StudentReportDTO> records) {
        TableView<StudentReportDTO> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StudentReportDTO, String> assessmentColumn = new TableColumn<>("Assessment");
        assessmentColumn.setCellValueFactory(data ->
                        new SimpleStringProperty(data.getValue().getAssessmentTitle())
        );

        TableColumn<StudentReportDTO, Double>  scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(data.getValue().getScore())
        );

        table.getColumns().addAll(assessmentColumn, scoreColumn);
        table.setItems(FXCollections.observableArrayList(records));

        return table;
    }

    private VBox buildOverallGradeSection() {
        VBox box = new VBox(5);
        box.getChildren().addAll(
                new Label("Overall Grade"),
                new Label("TODO")
// TODO calculate overall grade/GPA
        );

        return box;
    }
}