package com.edujournal.ui.teacher;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: teacher / Course's report — no real content yet, placeholder only.
 */
public class TeacherCourseReportPage extends BorderPane {

    public TeacherCourseReportPage() {
        setLeft(TeacherSidebar.build("Course's report"));
        setCenter(PagePlaceholder.build("Course's report", "Teacher"));
    }
}
