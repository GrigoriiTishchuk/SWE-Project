package com.edujournal.ui.teacher;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import javafx.scene.layout.VBox;

// every teacher page just calls TeacherSidebar.build("<its own label>").

public class TeacherSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                "Teacher",
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new TeacherDashboardPage())),
                new Sidebar.NavItem("Course", active.equals("Course"),
                        () -> Main.showPage(new TeacherCoursePage())),
                new Sidebar.NavItem("Assessment", active.equals("Assessment"),
                        () -> Main.showPage(new TeacherAssessmentPage())),
                new Sidebar.NavItem("Own Profile", active.equals("Own Profile"),
                        () -> Main.showPage(new TeacherOwnProfilePage())),
                new Sidebar.NavItem("Course's report", active.equals("Course's report"),
                        () -> Main.showPage(new TeacherCourseReportPage()))
        );
    }
}
