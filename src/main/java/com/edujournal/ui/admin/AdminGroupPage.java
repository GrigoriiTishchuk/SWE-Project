package com.edujournal.ui.admin;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: admin / Group — no real content yet, placeholder only.
 */
public class AdminGroupPage extends BorderPane {

    public AdminGroupPage() {
        setLeft(AdminSidebar.build("Group"));
        setCenter(PagePlaceholder.build("Group", "Administrator"));
    }
}
