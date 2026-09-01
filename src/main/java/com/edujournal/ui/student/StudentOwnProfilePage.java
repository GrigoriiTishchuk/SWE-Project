package com.edujournal.ui.student;

import com.edujournal.ui.PagePlaceholder;
import javafx.scene.layout.BorderPane;

/**
 * Skeleton page: student / Own profile — no real content yet, placeholder only.
 */
public class StudentOwnProfilePage extends BorderPane {

    public StudentOwnProfilePage() {
        setLeft(StudentSidebar.build("Own profile"));
        setCenter(PagePlaceholder.build("Own profile", "Student"));
    }
}
