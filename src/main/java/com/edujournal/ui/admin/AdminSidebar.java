package com.edujournal.ui.admin;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import javafx.scene.layout.VBox;

// every admin page just calls AdminSidebar.build("<its own label>"), instead of each page writing out all 8 nav links itself, just call one method and pass their own name

public class AdminSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                "Administrator",
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new AdminDashboardPage())),
                new Sidebar.NavItem("Student", active.equals("Student"),
                        () -> Main.showPage(new AdminStudentPage())),
                new Sidebar.NavItem("Teacher", active.equals("Teacher"),
                        () -> Main.showPage(new AdminTeacherPage())),
                new Sidebar.NavItem("Group", active.equals("Group"),
                        () -> Main.showPage(new AdminGroupPage())),
                new Sidebar.NavItem("Course", active.equals("Course"),
                        () -> Main.showPage(new AdminCoursePage())),
                new Sidebar.NavItem("Own Profile", active.equals("Own Profile"),
                        () -> Main.showPage(new AdminOwnProfilePage())),
                new Sidebar.NavItem("Student's report", active.equals("Student's report"),
                        () -> Main.showPage(new AdminStudentReportPage())),
                new Sidebar.NavItem("Course's report", active.equals("Course's report"),
                        () -> Main.showPage(new AdminCourseReportPage()))
        );
    }
}
