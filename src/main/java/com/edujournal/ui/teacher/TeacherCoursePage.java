package com.edujournal.ui.teacher;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: teacher / Course — no real content yet, placeholder only.
 */
public class TeacherCoursePage extends BorderPane {

    public TeacherCoursePage() {
        setLeft(TeacherSidebar.build("Course"));
        setCenter(PagePlaceholder.build("Course", "Teacher"));
    }
}
