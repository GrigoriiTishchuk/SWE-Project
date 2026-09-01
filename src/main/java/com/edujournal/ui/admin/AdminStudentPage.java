package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Student — no real content yet, placeholder only.
 */
public class AdminStudentPage extends BorderPane {

    public AdminStudentPage() {
        setLeft(AdminSidebar.build("Student"));
        setCenter(PagePlaceholder.build("Student", "Administrator"));
    }
}
