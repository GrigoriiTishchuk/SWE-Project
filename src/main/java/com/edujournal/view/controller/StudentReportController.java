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
import com.edujournal.config.LocalizationManager;
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

/**
 * Builds a student progress report that contains general
 * student information and assessment results grouped by course.
 */
public class StudentReportController {

    private final Role role;
    private final Integer studentId;

    private final StudentService studentService = new StudentService();
    private final StudentReportService reportService = new StudentReportService();
    private final AcademicGroupService groupService = new AcademicGroupService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final GradesDAO gradesDAO = new GradesDAO();

    /**
     * Creates a student report controller.
     *
     * @param role current user role
     * @param studentId selected student identifier
     */
    public StudentReportController(Role role, Integer studentId) {
        this.role = role;
        this.studentId = studentId;
    }

    /**
     * Builds the complete student report view.
     *
     * @return report content
     */
    public VBox buildReport() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(10));

        if (studentId == null) {
            root.getChildren().add(new Label(LocalizationManager.get("selectStudentForReport")));
            return root;
        }

        StudentDTO student = studentService.findDTOById(studentId);

        if (student == null) {
            root.getChildren().add(new Label(LocalizationManager.get("studentNotFound")));
            return root;
        }

        root.getChildren().add(buildStudentCard(student, reportService.computeAverageGrade(studentId)));

        List<StudentReportDTO> report = reportService.buildReport(studentId);

        if (report.isEmpty()) {
            root.getChildren().add(new Label(LocalizationManager.get("assessmentNotFound")));
            return root;
        }

        addCourseSections(root, report);
        return root;
    }

    /**
     * Creates a summary card containing student information and average grade.
     *
     * @param student student information
     * @param avgGrade calculated average grade
     * @return student summary card
     */
    private VBox buildStudentCard(StudentDTO student, String avgGrade) {
        String groupName = findGroupName(student.getAcademicGroupId());

        VBox card = new VBox(5);
        card.getChildren().addAll(
                new Label(student.getFirstName() + " " + student.getLastName()),
                new Label(LocalizationManager.get("group") + " " + groupName),
                new Label(LocalizationManager.get("studentNumber") + " " + student.getStudentNumber()),
                new Label(LocalizationManager.get("averageGrade2") + " " + avgGrade)
        );
        return card;
    }

    /**
     * Finds the academic group name by group identifier.
     *
     * @param groupId academic group identifier
     * @return group name or placeholder value
     */
    private String findGroupName(Integer groupId) {
        if (groupId == null) {
            return "-";
        }
        AcademicGroup group = groupService.findById(groupId);
        return group != null ? group.getName() : "-";
    }

    /**
     * Adds course sections and assessment tables to the report view.
     *
     * @param root report container
     * @param report report records
     */
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

    /**
     * Creates a table displaying assessment scores and final grade for a course.
     *
     * @param records course assessment records
     * @param courseId course identifier
     * @return configured table container
     */
    private ScrollPane createCourseTable(List<StudentReportDTO> records, Integer courseId) {
        TableView<List<String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<List<String>, String> labelColumn = new TableColumn<>(LocalizationManager.get("assessment"));
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
        scoreRow.add(LocalizationManager.get("score"));
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

        TableColumn<List<String>, String> finalGradeCol = new TableColumn<>(LocalizationManager.get("finalGrade"));
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
}