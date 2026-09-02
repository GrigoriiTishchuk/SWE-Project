package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminStudentReportPage extends BorderPane {

    public AdminStudentReportPage() {
        setLeft(AdminSidebar.build("Student's report"));
        setCenter(PagePlaceholder.build("Student's report", "Administrator"));
    }
}
