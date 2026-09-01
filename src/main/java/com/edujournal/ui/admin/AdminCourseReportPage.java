package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Course's report — no real content yet, placeholder only.
 */
public class AdminCourseReportPage extends BorderPane {

    public AdminCourseReportPage() {
        setLeft(AdminSidebar.build("Course's report"));
        setCenter(PagePlaceholder.build("Course's report", "Administrator"));
    }
}
