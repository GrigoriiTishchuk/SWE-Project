package com.edujournal.view.student;

import com.edujournal.backend.service.StudentService;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.view.common.ChartPlaceholder;
import com.edujournal.view.common.DashboardStatCards;
import com.edujournal.view.common.TopBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentDashboardPage extends BorderPane {

    private final StudentService studentService = new StudentService();

    private final VBox studentList = new VBox(8);
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    public StudentDashboardPage() {
        setLeft(StudentSidebar.build("Dashboard"));
        setCenter(buildContent());
    }

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        content.getChildren().addAll(
                TopBar.build("Dashboard", Role.STUDENT),
                buildStatCards(),
                buildBottomRow()
        );

        return content;
    }

    private HBox buildStatCards() {
        return DashboardStatCards.build(Role.STUDENT);
    }

    private HBox buildBottomRow() {
        HBox box = new HBox(
                16,
                buildStudentOverview(),
                ChartPlaceholder.build("Personal Grade Distribution")
        );

        box.setAlignment(javafx.geometry.Pos.CENTER);

        return box;
    }

    private VBox buildStudentOverview() {

        VBox box = new VBox(12);

        box.setPadding(new Insets(16));

        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #E5E7EB;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        Label heading = new Label("Students");

        heading.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        TextField search = new TextField();
        search.setPromptText("Type student name or number");

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            showStudents(newValue);
        });

        loadStudents();

        box.getChildren().addAll(
                heading,
                search,
                studentList
        );

        return box;
    }

    private void loadStudents() {
        students.setAll(studentService.findAll());

        showStudents("");
    }

    private void showStudents(String searchText) {

        studentList.getChildren().clear();

        String search = searchText == null
                ? ""
                : searchText.trim().toLowerCase();

        for (Student student : students) {

            String firstName = student.getFirstName() == null
                    ? ""
                    : student.getFirstName();

            String lastName = student.getLastName() == null
                    ? ""
                    : student.getLastName();

            String studentNumber = student.getStudentNumber() == null
                    ? ""
                    : student.getStudentNumber();

            String fullName = firstName + " " + lastName;

            boolean matches =
                    fullName.toLowerCase().contains(search)
                            || studentNumber.toLowerCase().contains(search);

            if (matches) {
                studentList.getChildren().add(
                        studentRow(student)
                );
            }
        }
    }

    private HBox studentRow(Student student) {

        String firstName = student.getFirstName() == null
                ? ""
                : student.getFirstName();

        String lastName = student.getLastName() == null
                ? ""
                : student.getLastName();

        String studentNumber = student.getStudentNumber() == null
                ? ""
                : student.getStudentNumber();

        Label nameLabel = new Label(
                firstName + " " + lastName
        );

        nameLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        Label numberLabel = new Label(
                studentNumber
        );

        numberLabel.setStyle(
                "-fx-text-fill: #6B7280;" +
                        "-fx-font-size: 12px;"
        );

        VBox text = new VBox(
                2,
                nameLabel,
                numberLabel
        );

        HBox row = new HBox(text);

        row.setPadding(new Insets(8));

        row.setStyle(
                "-fx-background-color: #F3F4F6;" +
                        "-fx-background-radius: 6;"
        );

        return row;
    }
}