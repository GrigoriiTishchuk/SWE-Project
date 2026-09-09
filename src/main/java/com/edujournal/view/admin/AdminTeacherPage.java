package com.edujournal.view.admin;

import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminTeacherPage extends BorderPane {

    public AdminTeacherPage() {
        setLeft(AdminSidebar.build("Teachers"));
        setCenter(PagePlaceholder.build("Teachers", "Administrator"));
    }
}
