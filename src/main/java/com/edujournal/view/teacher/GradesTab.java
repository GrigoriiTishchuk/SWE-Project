package com.edujournal.view.teacher;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.CourseGradeService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.dao.EnrollmentDAO;
import com.edujournal.dao.GradesDAO;
import com.edujournal.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.edujournal.view.common.ExportUtil;

import java.util.*;

public class GradesTab {

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";

    public static Node build(String course, String group) {
        AssessmentsService assessmentsService = new AssessmentsService();
        GradesDAO gradesDAO = new GradesDAO();
        StudentService studentService = new StudentService();
        UserService userService = new UserService();

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
                gradeMap.computeIfAbsent(g.getEnrollmentId(), k -> new LinkedHashMap<>())
                        .put(g.getAssessmentId(), g);
            }
        }

        // Load enrollments for this course/group
        List<Enrollment> enrollments;
        if (group != null && !group.isEmpty()) {
            AcademicGroup academicGroup = new AcademicGroupService().findByName(group);
            Course dbCourse = new AssessmentsService().getCourseByName(course);
            if (academicGroup != null && dbCourse != null) {
                enrollments = new EnrollmentDAO().findByCourseAndGroup(dbCourse.getId(), academicGroup.getId());
            } else {
                enrollments = new ArrayList<>();
            }
        } else {
        Course dbCourse = new AssessmentsService().getCourseByName(course);
        enrollments = new EnrollmentDAO().findByCourseId(dbCourse.getId());
        }

        List<Integer> rowEnrollmentIds = new ArrayList<>();
        for (Enrollment e : enrollments) rowEnrollmentIds.add(e.getId());

        int rowCount = rowEnrollmentIds.size();
        int colCount = assessments.size();

        // Display data and TextFields side by side
        // data columns: [0]=name, [1..colCount]=scores, [colCount+1]=final grade
        String[][] data = new String[rowCount][colCount + 2];
        TextField[][] tfs = new TextField[rowCount][colCount];

        for (int r = 0; r < rowCount; r++) {
            Enrollment enrollment = enrollments.get(r);
            int enrollmentId = enrollment.getId();

            Student student = studentService.findById(enrollment.getStudentId());
            User user = userService.findById(student.getUserId());

            data[r][0] = user.getFirstName() + " " + user.getLastName();
            Map<Integer, Grades> studentGrades = gradeMap.get(enrollmentId);

            for (int c = 0; c < colCount; c++) {
                Grades g = studentGrades != null ? studentGrades.get(assessments.get(c).getId()) : null;
                String val = (g != null && g.getScore() != null) ? String.valueOf(g.getScore()) : "—";
                data[r][c + 1] = val;
                tfs[r][c] = new TextField(val.equals("—") ? "" : val);
            }
            data[r][colCount + 1] = computeFinalGrade(assessments, studentGrades, colCount);
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

        TableColumn<String[], String> finalGradeCol = new TableColumn<>("Final Grade");
        finalGradeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[colCount + 1]));
        finalGradeCol.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : s);
                setStyle(empty ? "" : "-fx-background-color: #DBEAFE; -fx-font-weight: bold; -fx-text-fill: #1a3a6b;");
            }
        });
        finalGradeCol.setPrefWidth(110);
        finalGradeCol.setMinWidth(110);
        table.getColumns().add(finalGradeCol);

        if (table.getItems().isEmpty()) {
            table.setPlaceholder(new Label("No grades recorded for \"" + course + "\" yet."));
        }
        table.getItems().addAll(List.of(data));

        String[] exportHeaders = new String[3 + colCount];
        exportHeaders[0] = "#";
        exportHeaders[1] = "Student";
        for (int i = 0; i < colCount; i++) exportHeaders[2 + i] = assessments.get(i).getTitle();
        exportHeaders[2 + colCount] = "Final Grade";

        Button csvBtn = new Button("Export CSV");
        csvBtn.setStyle(BLUE_BTN);
        csvBtn.setOnAction(e -> {
            List<String[]> rows = new ArrayList<>();
            for (int r = 0; r < rowCount; r++) {
                String[] row = new String[3 + colCount];
                row[0] = String.valueOf(r + 1);
                row[1] = data[r][0];
                for (int c = 0; c < colCount; c++) row[2 + c] = data[r][c + 1];
                row[2 + colCount] = data[r][colCount + 1];
                rows.add(row);
            }
            ExportUtil.exportCsv(csvBtn.getScene().getWindow(), "gradebook", exportHeaders, rows);
        });

        Button pdfBtn = new Button("Export PDF");
        pdfBtn.setStyle(BLUE_BTN);
        pdfBtn.setOnAction(e -> {
            List<String[]> rows = new ArrayList<>();
            for (int r = 0; r < rowCount; r++) {
                String[] row = new String[3 + colCount];
                row[0] = String.valueOf(r + 1);
                row[1] = data[r][0];
                for (int c = 0; c < colCount; c++) row[2 + c] = data[r][c + 1];
                row[2 + colCount] = data[r][colCount + 1];
                rows.add(row);
            }
            String subtitle = "Course: " + course + (group != null && !group.isEmpty() ? "   Group: " + group : "");
            ExportUtil.exportPdf(pdfBtn.getScene().getWindow(), "gradebook", "Gradebook", subtitle, exportHeaders, rows);
        });

        Button fixSave = new Button("Fix / Edit");
        fixSave.setStyle(BLUE_BTN);

        fixSave.setOnAction(e -> {
            if (editing[0]) {
                for (int r = 0; r < rowCount; r++) {
                    int enrollmentId = rowEnrollmentIds.get(r);
                    Map<Integer, Grades> studentGrades = gradeMap.get(enrollmentId);
                    for (int c = 0; c < colCount; c++) {
                        String text = tfs[r][c].getText().trim();
                        if (text.isEmpty()) continue;
                        try {
                            double score = Double.parseDouble(text);
                            double maxScore = assessments.get(c).getMaxScore();
                            if (score > maxScore) {
                                new Alert(Alert.AlertType.WARNING,
                                        "\"" + assessments.get(c).getTitle() + "\": score " + score +
                                        " exceeds max score " + maxScore + ".").showAndWait();
                                continue;
                            }
                            Grades grade = studentGrades != null
                                    ? studentGrades.get(assessments.get(c).getId()) : null;
                            if (grade != null) {
                                grade.setScore(score);
                                gradesDAO.update(grade);
                            } else {
                                Grades newGrade = new Grades();
                                newGrade.setEnrollmentId(enrollmentId);
                                newGrade.setAssessmentId(assessments.get(c).getId());
                                newGrade.setScore(score);
                                gradesDAO.save(newGrade);
                                gradeMap.computeIfAbsent(enrollmentId, k -> new LinkedHashMap<>())
                                        .put(assessments.get(c).getId(), newGrade);
                            }
                            data[r][c + 1] = text;
                        } catch (NumberFormatException ignored) {}
                    }
                    data[r][colCount + 1] = computeFinalGrade(assessments, gradeMap.get(enrollmentId), colCount);
                }
            }
            editing[0] = !editing[0];
            table.refresh();
            fixSave.setText(editing[0] ? "Save Changes" : "Fix / Edit");
        });

        HBox btnRow = new HBox(8, csvBtn, pdfBtn, fixSave);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, btnRow, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }

    public static String computeFinalGrade(List<Assessments> assessments, Map<Integer, Grades> studentGrades, int colCount) {
        if (studentGrades == null || studentGrades.size() < colCount
                || studentGrades.values().stream().anyMatch(g -> g.getScore() == null)) {
            return "—";
        }
        try {
            double percent = new CourseGradeService().calculateFinalGrade(
                    assessments, new ArrayList<>(studentGrades.values()));
            return String.format("%d (%.1f%%)", toGrade(percent), percent);
        } catch (Exception e) {
            System.err.println("[FinalGrade] " + e.getMessage());
            return "—";
        }
    }

    private static int toGrade(double percent) {
        if (percent >= 83) return 5;
        if (percent >= 72) return 4;
        if (percent >= 62) return 3;
        if (percent >= 50) return 2;
        if (percent >= 40) return 1;
        return 0;
    }
}
