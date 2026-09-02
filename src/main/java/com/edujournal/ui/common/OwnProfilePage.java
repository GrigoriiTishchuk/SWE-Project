package com.edujournal.ui.common;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class OwnProfilePage extends BorderPane {

    public OwnProfilePage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(PagePlaceholder.build("Own Profile", role));
    }
}
