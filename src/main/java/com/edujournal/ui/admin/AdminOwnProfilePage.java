package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminOwnProfilePage extends BorderPane {

    public AdminOwnProfilePage() {
        setLeft(AdminSidebar.build("Own Profile"));
        setCenter(PagePlaceholder.build("Own Profile", "Administrator"));
    }
}
