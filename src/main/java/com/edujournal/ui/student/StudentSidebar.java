package com.edujournal.ui.student;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import com.edujournal.ui.common.OwnProfilePage;
import com.edujournal.ui.common.StudentReportPage;
import javafx.scene.layout.VBox;

// every student page just calls StudentSidebar.build("<its own label>")

public class StudentSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new StudentDashboardPage())),
                new Sidebar.SectionHeader("MANAGEMENT"),
                new Sidebar.NavItem("Own profile", active.equals("Own profile"),
                        () -> Main.showPage(new OwnProfilePage(StudentSidebar.build("Own profile"), "Student"))),
                new Sidebar.SectionHeader("REPORTS"),
                new Sidebar.NavItem("Student's report", active.equals("Student's report"),
                        () -> Main.showPage(new StudentReportPage(StudentSidebar.build("Student's report"), "Student")))
        );
    }
}
