package com.edujournal.view.common;

import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StudentReportPage extends BorderPane {

    public StudentReportPage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(PagePlaceholder.build("Student's report", role));
    }
}
