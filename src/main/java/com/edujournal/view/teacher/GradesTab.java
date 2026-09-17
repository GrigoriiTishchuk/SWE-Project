package com.edujournal.view.teacher;

import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.Assessments;
import com.edujournal.entity.Grades;
import com.edujournal.entity.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class GradesTab {

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";

    public static Node build(String course, String group) {
        AssessmentsService assessmentsService = new AssessmentsService();
        GradesDAO gradesDAO = new GradesDAO();
        StudentService studentService = new StudentService();

        List<Assessments> assessments = assessmentsService.getByCourseName(course);

        if (assessments.isEmpty()) {
            Label lbl = new Label("No assessments found for \"" + course + "\".");
            lbl.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            return lbl;
        }

        // studentId -> assessmentId -> Grades entity
        Map<Integer, Map<Integer, Grades>> gradeMap = new LinkedHashMap<>();
        for (Assessments a : assessments) {
            for (Grades g : gradesDAO.findByAssessment(a.getId())) {
                gradeMap.computeIfAbsent(g.getStudentId(), k -> new LinkedHashMap<>())
                        .put(g.getAssessmentId(), g);
            }
        }

        List<Student> allStudents = studentService.findAll();
        List<Integer> rowStudentIds = new ArrayList<>();
        for (Student s : allStudents) rowStudentIds.add(s.getId());

        int rowCount = rowStudentIds.size();
        int colCount = assessments.size();

        // Display data and TextFields side by side
        String[][] data = new String[rowCount][colCount + 1];
        TextField[][] tfs = new TextField[rowCount][colCount];

        for (int r = 0; r < rowCount; r++) {
            Student student = allStudents.get(r);
            int studentId = student.getId();
            data[r][0] = student.getFirstName() + " " + student.getLastName();
            Map<Integer, Grades> studentGrades = gradeMap.get(studentId);
            for (int c = 0; c < colCount; c++) {
                Grades g = studentGrades != null ? studentGrades.get(assessments.get(c).getId()) : null;
                String val = (g != null && g.getScore() != null) ? String.valueOf(g.getScore()) : "—";
                data[r][c + 1] = val;
                tfs[r][c] = new TextField(val.equals("—") ? "" : val);
            }
        }

        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);
        table.getStylesheets().add(String.valueOf(GradesTab.class.getResource("/css/table.css")));

        TableColumn<String[], String> numCol = new TableColumn<>("#");
        numCol.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(table.getItems().indexOf(d.getValue()) + 1)));
        numCol.setMaxWidth(35);
        numCol.setMinWidth(35);

        TableColumn<String[], String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        studentCol.setPrefWidth(150);

        table.getColumns().addAll(numCol, studentCol);

        boolean[] editing = {false};

        for (int i = 0; i < colCount; i++) {
            final int col = i + 1;
            final int tfCol = i;
            TableColumn<String[], String> scoreCol = new TableColumn<>(assessments.get(i).getTitle());
            scoreCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[col]));
            scoreCol.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(String s, boolean empty) {
                    super.updateItem(s, empty);
                    int idx = getIndex();
                    if (empty || idx < 0 || idx >= rowCount) { setText(null); setGraphic(null); return; }
                    if (editing[0]) {
                        setGraphic(tfs[idx][tfCol]); setText(null);
                    } else {
                        setText(data[idx][col]); setGraphic(null);
                    }
                }
            });
            scoreCol.setPrefWidth(80);
            scoreCol.setMinWidth(80);
            table.getColumns().add(scoreCol);
        }

        if (table.getItems().isEmpty()) {
            table.setPlaceholder(new Label("No grades recorded for \"" + course + "\" yet."));
        }
        table.getItems().addAll(List.of(data));

        Button fixSave = new Button("Fix / Edit");
        fixSave.setStyle(BLUE_BTN);
        fixSave.setOnAction(e -> {
            if (editing[0]) {
                for (int r = 0; r < rowCount; r++) {
                    int studentId = rowStudentIds.get(r);
                    Map<Integer, Grades> studentGrades = gradeMap.get(studentId);
                    for (int c = 0; c < colCount; c++) {
                        String text = tfs[r][c].getText().trim();
                        if (text.isEmpty()) continue;
                        try {
                            double score = Double.parseDouble(text);
                            Grades grade = studentGrades != null
                                    ? studentGrades.get(assessments.get(c).getId()) : null;
                            if (grade != null) {
                                grade.setScore(score);
                                gradesDAO.update(grade);
                            } else {
                                Grades newGrade = new Grades();
                                newGrade.setStudentId(studentId);
                                newGrade.setAssessmentId(assessments.get(c).getId());
                                newGrade.setScore(score);
                                gradesDAO.save(newGrade);
                                gradeMap.computeIfAbsent(studentId, k -> new LinkedHashMap<>())
                                        .put(assessments.get(c).getId(), newGrade);
                            }
                            data[r][c + 1] = text;
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            editing[0] = !editing[0];
            table.refresh();
            fixSave.setText(editing[0] ? "Save Changes" : "Fix / Edit");
        });

        HBox btnRow = new HBox(fixSave);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, btnRow, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
}
