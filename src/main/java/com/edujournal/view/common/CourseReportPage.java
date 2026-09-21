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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CourseReportPage extends BorderPane {

    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssessmentsService assessmentsService = new AssessmentsService();
    private final AcademicGroupService academicGroupService = new AcademicGroupService();

    public CourseReportPage(VBox sidebar, Role role) {
        setLeft(sidebar);
        setCenter(buildContent(role));
    }

    private VBox buildContent(Role role) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));
        box.getChildren().addAll(
                TopBar.build("Course's report", role),
                buildTable()
        );
        return box;
    }

    private TableView<Course> buildTable() {
        TableView<Course> table = new TableView<>();

        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        TableColumn<Course, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Course, String> groupCol = new TableColumn<>("Group");
        groupCol.setCellValueFactory(c -> {
            Integer groupId = c.getValue().getAcademicGroupId();
            AcademicGroup group = groupId != null ? academicGroupService.findById(groupId) : null;
            return new SimpleStringProperty(group != null ? group.getName() : "—");
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

        table.getColumns().addAll(codeCol, nameCol, groupCol, studentsCol, assessmentsCol);
        table.getItems().addAll(courseDAO.findAll());

        return table;
    }
}
