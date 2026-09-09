package com.edujournal.view.admin;

import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminStudentPage extends BorderPane {

    public AdminStudentPage() {
        setLeft(AdminSidebar.build("Students"));
        setCenter(PagePlaceholder.build("Students", "Administrator"));
    }
}
