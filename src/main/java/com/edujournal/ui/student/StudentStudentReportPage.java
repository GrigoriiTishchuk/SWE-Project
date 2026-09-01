package com.edujournal.ui.student;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: student / Student's report — no real content yet, placeholder only.
 */
public class StudentStudentReportPage extends BorderPane {

    public StudentStudentReportPage() {
        setLeft(StudentSidebar.build("Student's report"));
        setCenter(PagePlaceholder.build("Student's report", "Student"));
    }
}
