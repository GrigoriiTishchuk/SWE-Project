package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class CourseReportPage extends BorderPane {

    private static final String[] HEADERS = {"Code", "Name", "Group", "Students", "Assessments"};

    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final AcademicGroupService academicGroupService = new AcademicGroupService();

    private TableView<Course> table;

    public CourseReportPage(VBox sidebar, Role role) {
        setLeft(sidebar);
        setCenter(buildContent(role));
    }

    private VBox buildContent(Role role) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        table = buildTable();

        MenuButton exportBtn = new MenuButton("Export");
        MenuItem csvItem = new MenuItem("Export CSV");
        MenuItem pdfItem = new MenuItem("Export PDF");
        csvItem.setOnAction(e -> ExportUtil.exportCsv(getScene().getWindow(), "courses_report", HEADERS, buildRows()));
        pdfItem.setOnAction(e -> ExportUtil.exportPdf(getScene().getWindow(), "courses_report", "Courses Report", null, HEADERS, buildRows()));
        exportBtn.getItems().addAll(csvItem, pdfItem);

        HBox toolbar = new HBox(exportBtn);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        box.getChildren().addAll(TopBar.build("Course's report", role, true), toolbar, table);
        return box;
    }

    private TableView<Course> buildTable() {
        TableView<Course> t = new TableView<>();

        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        TableColumn<Course, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Course, String> groupCol = new TableColumn<>("Group");
        groupCol.setCellValueFactory(c -> {
            Integer groupId = c.getValue().getAcademicGroupId();
            AcademicGroup group = groupId != null ? academicGroupService.findById(groupId) : null;
            return new SimpleStringProperty(group != null ? group.getName() : "-");
        });

        TableColumn<Course, String> studentsCol = new TableColumn<>("Students");
        studentsCol.setCellValueFactory(c -> {
            int count = enrollmentService.findByCourseId(c.getValue().getId()).size();
            return new SimpleStringProperty(String.valueOf(count));
        });

        TableColumn<Course, String> assessmentsCol = new TableColumn<>("Assessments");
        assessmentsCol.setCellValueFactory(c -> {
            int count = assessmentsService.getByCourseId(c.getValue().getId()).size();
            return new SimpleStringProperty(String.valueOf(count));
        });

        t.getColumns().addAll(List.of(codeCol, nameCol, groupCol, studentsCol, assessmentsCol));
        t.getItems().addAll(courseDAO.findAll());
        return t;
    }

    private List<String[]> buildRows() {
        List<String[]> rows = new ArrayList<>();
        for (Course c : table.getItems()) {
            Integer groupId = c.getAcademicGroupId();
            AcademicGroup group = groupId != null ? academicGroupService.findById(groupId) : null;
            rows.add(new String[]{
                c.getCode(),
                c.getName(),
                group != null ? group.getName() : "-",
                String.valueOf(enrollmentService.findByCourseId(c.getId()).size()),
                String.valueOf(assessmentsService.getByCourseId(c.getId()).size())
            });
        }
        return rows;
    }

}
