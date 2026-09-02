package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminGroupPage extends BorderPane {

    public AdminGroupPage() {
        setLeft(AdminSidebar.build("Group"));
        setCenter(PagePlaceholder.build("Group", "Administrator"));
    }
}
