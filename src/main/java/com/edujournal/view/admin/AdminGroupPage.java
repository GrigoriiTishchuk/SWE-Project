package com.edujournal.view.admin;

import com.edujournal.entity.Role;
import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class AdminGroupPage extends BorderPane {

    public AdminGroupPage() {
        setLeft(AdminSidebar.build("Groups"));
        setCenter(PagePlaceholder.build("Groups", Role.ADMINISTRATOR));
    }
}
