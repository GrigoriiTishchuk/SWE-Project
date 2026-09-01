package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Student's report — no real content yet, placeholder only.
 */
public class AdminStudentReportPage extends BorderPane {

    public AdminStudentReportPage() {
        setLeft(AdminSidebar.build("Student's report"));
        setCenter(PagePlaceholder.build("Student's report", "Administrator"));
    }
}
