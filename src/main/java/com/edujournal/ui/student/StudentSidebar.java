package com.edujournal.ui.student;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import javafx.scene.layout.VBox;

// every student page just calls StudentSidebar.build("<its own label>"). The nav list is defined in one place. Every student page just calls that one method build

public class StudentSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                "Student",
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new StudentDashboardPage())),
                new Sidebar.NavItem("Own profile", active.equals("Own profile"),
                        () -> Main.showPage(new StudentOwnProfilePage())),
                new Sidebar.NavItem("Student's report", active.equals("Student's report"),
                        () -> Main.showPage(new StudentStudentReportPage()))
        );
    }
}
