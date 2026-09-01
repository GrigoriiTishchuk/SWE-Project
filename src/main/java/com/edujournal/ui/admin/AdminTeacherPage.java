package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Teacher — no real content yet, placeholder only.
 */
public class AdminTeacherPage extends BorderPane {

    public AdminTeacherPage() {
        setLeft(AdminSidebar.build("Teacher"));
        setCenter(PagePlaceholder.build("Teacher", "Administrator"));
    }
}
