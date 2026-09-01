package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Course — no real content yet, placeholder only.
 */
public class AdminCoursePage extends BorderPane {

    public AdminCoursePage() {
        setLeft(AdminSidebar.build("Course"));
        setCenter(PagePlaceholder.build("Course", "Administrator"));
    }
}
