package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AssessmentsService;
import com.edujournal.backend.service.EnrollmentService;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.awt.Color;
import java.io.*;
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
        csvItem.setOnAction(e -> exportCsv());
        pdfItem.setOnAction(e -> exportPdf());
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

        t.getColumns().addAll(codeCol, nameCol, groupCol, studentsCol, assessmentsCol);
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

    private void exportCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV");
        chooser.setInitialFileName("courses_report.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file == null) return;

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            pw.println(csvRow(HEADERS));
            for (String[] row : buildRows()) {
                pw.println(csvRow(row));
            }
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to save CSV: " + ex.getMessage()).showAndWait();
        }
    }

    private String csvRow(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(',');
            String val = fields[i] == null ? "" : fields[i];
            if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
                sb.append('"').append(val.replace("\"", "\"\"")).append('"');
            } else {
                sb.append(val);
            }
        }
        return sb.toString();
    }

    private void exportPdf() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save PDF");
        chooser.setInitialFileName("courses_report.pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file == null) return;

        try {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            doc.add(new Paragraph("Courses Report", titleFont));
            doc.add(Chunk.NEWLINE);

            PdfPTable pdfTable = new PdfPTable(HEADERS.length);
            pdfTable.setWidthPercentage(100);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.NORMAL, Color.WHITE);
            for (String h : HEADERS) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new Color(63, 131, 248));
                cell.setPadding(6);
                pdfTable.addCell(cell);
            }

            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            boolean shade = false;
            for (String[] row : buildRows()) {
                Color bg = shade ? new Color(243, 244, 246) : Color.WHITE;
                for (String val : row) {
                    PdfPCell cell = new PdfPCell(new Phrase(val, cellFont));
                    cell.setBackgroundColor(bg);
                    cell.setPadding(5);
                    pdfTable.addCell(cell);
                }
                shade = !shade;
            }

            doc.add(pdfTable);
            doc.close();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to save PDF: " + ex.getMessage()).showAndWait();
        }
    }
}
