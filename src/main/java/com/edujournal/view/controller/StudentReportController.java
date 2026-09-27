package com.edujournal.view.controller;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.backend.service.StudentReportService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Enrollment;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Role;
import com.edujournal.model.StudentDTO;
import com.edujournal.model.StudentReportDTO;
import com.edujournal.view.teacher.GradesTab;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
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
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final GradesDAO gradesDAO = new GradesDAO();

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

        root.getChildren().add(buildStudentCard(student, reportService.computeAverageGrade(studentId)));

        List<StudentReportDTO> report = reportService.buildReport(studentId);

        if (report.isEmpty()) {
            root.getChildren().add(new Label("No assessment records found."));
            return root;
        }

        addCourseSections(root, report);
        //root.getChildren().add(buildOverallGradeSection());
        return root;
    }

    private VBox buildStudentCard(StudentDTO student, String avgGrade) {
        String groupName = findGroupName(student.getAcademicGroupId());

        VBox card = new VBox(5);
        card.getChildren().addAll(
                new Label(student.getFirstName() + " " + student.getLastName()),
                new Label("Group: " + groupName),
                new Label("Student Number: " + student.getStudentNumber()),
                new Label("Average Grade: " + avgGrade)
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
            root.getChildren().add(createCourseTable(records, first.getCourseId()));
        }
    }

    private ScrollPane createCourseTable(List<StudentReportDTO> records, Integer courseId) {
        TableView<List<String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<List<String>, String> labelColumn = new TableColumn<>("Assessment");
        labelColumn.setPrefWidth(140);
        labelColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(0))
        );
        table.getColumns().add(labelColumn);


        for (int i = 0; i < records.size(); i++) {
            StudentReportDTO dto = records.get(i);
            final int columnIndex = i + 1;
            TableColumn<List<String>, String> column = new TableColumn<>(dto.getAssessmentTitle());
            column.setPrefWidth(180);
            column.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().get(columnIndex))
            );
            table.getColumns().add(column);
        }

        List<String> scoreRow = new ArrayList<>();
        scoreRow.add("Score");
        for (StudentReportDTO dto : records) {
            scoreRow.add(dto.getScore() != null ? dto.getScore().toString() : "-");
        }

        Enrollment enrollment = enrollmentService.findByStudentId(studentId).stream()
                .filter(e -> e.getCourseId().equals(courseId)).findFirst().orElse(null);
        List<Assessments> assessments = assessmentsService.getByCourseId(courseId);
        Map<Integer, Grades> gradeMap = enrollment == null ? Map.of() :
                gradesDAO.findByEnrollment(enrollment.getId()).stream()
                        .collect(Collectors.toMap(Grades::getAssessmentId, g -> g));
        String finalGrade = GradesTab.computeFinalGrade(assessments, gradeMap, assessments.size());

        TableColumn<List<String>, String> finalGradeCol = new TableColumn<>("Final Grade");
        finalGradeCol.setPrefWidth(120);
        finalGradeCol.setCellValueFactory(data -> new SimpleStringProperty(finalGrade));
        table.getColumns().add(finalGradeCol);

        table.setItems(FXCollections.observableArrayList(List.of(scoreRow)));

        table.setFixedCellSize(30);
        table.setPrefHeight(60);
        table.setMaxHeight(60);
        table.setSelectionModel(null);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
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