package com.edujournal.ui.teacher;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class GradesTab {

    public static Node build(String course, String group) {
        Label lbl = new Label("Grades content goes here.");
        lbl.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
        lbl.setAlignment(Pos.TOP_LEFT);
        return lbl;
    }
}
