package com.edujournal.ui.teacher;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

// placeholder only

public class TeacherAssessmentPage extends BorderPane {

    public TeacherAssessmentPage() {
        setLeft(TeacherSidebar.build("Assessment"));
        setCenter(PagePlaceholder.build("Assessment", "Teacher"));
    }
}
