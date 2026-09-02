package com.edujournal.ui.common;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CourseReportPage extends BorderPane {

    public CourseReportPage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(PagePlaceholder.build("Course's report", role));
    }
}
