package com.edujournal.ui.teacher;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: teacher / Own Profile — no real content yet, placeholder only.
 */
public class TeacherOwnProfilePage extends BorderPane {

    public TeacherOwnProfilePage() {
        setLeft(TeacherSidebar.build("Own Profile"));
        setCenter(PagePlaceholder.build("Own Profile", "Teacher"));
    }
}
