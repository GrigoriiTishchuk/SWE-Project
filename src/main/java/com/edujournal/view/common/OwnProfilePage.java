package com.edujournal.view.common;

import com.edujournal.view.PagePlaceholder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class OwnProfilePage extends BorderPane {

    public OwnProfilePage(VBox sidebar, String role) {
        setLeft(sidebar);
        setCenter(PagePlaceholder.build("Own Profile", role));
    }
}
