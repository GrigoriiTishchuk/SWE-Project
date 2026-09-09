package com.edujournal.view.common;

import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CourseReportPage extends BorderPane {

    public CourseReportPage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(PagePlaceholder.build("Course's report", role));
    }
}
