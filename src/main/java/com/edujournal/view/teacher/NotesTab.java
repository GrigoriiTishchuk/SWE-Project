package com.edujournal.view.teacher;

import com.edujournal.config.LocalizationManager;
import com.edujournal.dao.AcademicGroupDAO;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Course;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;

public class NotesTab {

    public static Node build(String course, String group) {
        CourseDAO courseDAO = new CourseDAO();
        AcademicGroupDAO groupDAO = new AcademicGroupDAO();
        AcademicGroup groupEntity = group != null && !group.isBlank() ? groupDAO.findByName(group) : null;
        Integer groupId = groupEntity != null ? groupEntity.getId() : null;
        Course courseEntity = groupId != null
                ? courseDAO.findByNameAndGroupId(course, groupId)
                : null;

        if (courseEntity == null) {
            String msg = (group == null || group.isBlank())
                    ? LocalizationManager.get("selectGroup")
                    : LocalizationManager.get("notesNotAvailable");
            Label placeholder = new Label(msg);
            placeholder.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            return new VBox(placeholder);
        }

        String existingContent = courseEntity.getNotes() != null ? courseEntity.getNotes() : "";

        TextArea area = new TextArea(existingContent);
        area.setPromptText(MessageFormat.format(LocalizationManager.get("notes.prompt"), course, group));
        area.setWrapText(true);

        Label saved = new Label("Saved.");
        saved.setStyle("-fx-text-fill: #6B7280;");
        saved.setVisible(false);

        Button saveBtn = new Button(LocalizationManager.get("save"));
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setOnAction(e -> {
            if (courseEntity == null) return;
            courseEntity.setNotes(area.getText());
            courseDAO.update(courseEntity);
            saved.setVisible(true);
        });

        area.textProperty().addListener((obs, old, val) -> saved.setVisible(false));

        HBox btnRow = new HBox(8, saved, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(8, area, btnRow);
        vbox.getStylesheets().add(NotesTab.class.getResource("/css/button.css").toExternalForm());
        VBox.setVgrow(area, Priority.ALWAYS);
        return vbox;
    }
}
