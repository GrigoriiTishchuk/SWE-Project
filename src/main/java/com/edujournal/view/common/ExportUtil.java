package com.edujournal.view.common;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Provides file export actions for tabular data.
 */
public class ExportUtil {

    private ExportUtil() {
    }

    /**
     * Opens a save dialog and writes the supplied data as a UTF-8 CSV file.
     *
     * @param window owner window for the save dialog
     * @param fileName suggested file name without an extension
     * @param headers column headings
     * @param rows table rows, with each row containing one value per column
     */
    public static void exportCsv(Window window, String fileName, String[] headers, List<String[]> rows) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV");
        chooser.setInitialFileName(fileName + ".csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File file = chooser.showSaveDialog(window);
        if (file == null) return;

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(csvRow(headers));
            writer.newLine();
            for (String[] row : rows) {
                writer.write(csvRow(row));
                writer.newLine();
            }
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to save CSV: " + ex.getMessage()).showAndWait();
        }
    }

    /**
     * Opens a save dialog and writes the supplied data as a landscape PDF table.
     *
     * @param window owner window for the save dialog
     * @param fileName suggested file name without an extension
     * @param title report title
     * @param subtitle optional report subtitle
     * @param headers column headings
     * @param rows table rows, with each row containing one value per column
     */
    public static void exportPdf(Window window, String fileName, String title, String subtitle, String[] headers, List<String[]> rows) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save PDF");
        chooser.setInitialFileName(fileName + ".pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File file = chooser.showSaveDialog(window);
        if (file == null) return;

        Document doc = new Document(PageSize.A4.rotate());
        try (FileOutputStream output = new FileOutputStream(file)) {
            PdfWriter.getInstance(doc, output);
            doc.open();

            try {
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                doc.add(new Paragraph(title, titleFont));
                if (subtitle != null && !subtitle.isBlank()) {
                    Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
                    doc.add(new Paragraph(subtitle, subtitleFont));
                }
                doc.add(Chunk.NEWLINE);

                PdfPTable pdfTable = new PdfPTable(headers.length);
                pdfTable.setWidthPercentage(100);

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.NORMAL, Color.WHITE);
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                    cell.setBackgroundColor(new Color(63, 131, 248));
                    cell.setPadding(6);
                    pdfTable.addCell(cell);
                }

                Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
                boolean shade = false;
                for (String[] row : rows) {
                    Color bg = shade ? new Color(243, 244, 246) : Color.WHITE;
                    for (String val : row) {
                        PdfPCell cell = new PdfPCell(new Phrase(val == null ? "" : val, cellFont));
                        cell.setBackgroundColor(bg);
                        cell.setPadding(5);
                        pdfTable.addCell(cell);
                    }
                    shade = !shade;
                }

                doc.add(pdfTable);
            } finally {
                if (doc.isOpen()) {
                    doc.close();
                }
            }
        } catch (DocumentException | IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to save PDF: " + ex.getMessage()).showAndWait();
        }
    }

    private static String csvRow(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(',');
            String val = fields[i] == null ? "" : fields[i];
            if (val.contains(",") || val.contains("\"") || val.contains("\n") || val.contains("\r")) {
                sb.append('"').append(val.replace("\"", "\"\"")).append('"');
            } else {
                sb.append(val);
            }
        }
        return sb.toString();
    }
}
