package com.edujournal.view.common;

import com.edujournal.entity.Role;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class RoleBackground {

    private RoleBackground() {}

    public static StackPane wrap(Role role, Node content) {
        String img = switch (role) {
            case ADMINISTRATOR -> RoleBackground.class.getResource("/images/admin-background.png").toExternalForm();
            case TEACHER -> RoleBackground.class.getResource("/images/teacher-background.png").toExternalForm();
            case STUDENT -> RoleBackground.class.getResource("/images/student-background.png").toExternalForm();
        };

        Region bg = new Region();
        bg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bg.setStyle("-fx-background-image: url('" + img + "'); -fx-background-size: cover; -fx-background-position: center;");
        GaussianBlur blur = new GaussianBlur(2);
        blur.setInput(new ColorAdjust(0, 0, 0.2, 0));
        bg.setEffect(blur);

        StackPane center = new StackPane(bg, content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        return center;
    }
}
