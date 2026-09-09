package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminTeacherPage extends BorderPane {

    public AdminTeacherPage() {
        setLeft(AdminSidebar.build("Teachers"));
        setCenter(PagePlaceholder.build("Teachers", "Administrator"));
    }
}
