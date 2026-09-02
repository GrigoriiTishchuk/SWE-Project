package com.edujournal.ui.admin;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import com.edujournal.ui.common.CoursePage;
import com.edujournal.ui.common.CourseReportPage;
import com.edujournal.ui.common.OwnProfilePage;
import com.edujournal.ui.common.StudentReportPage;
import javafx.scene.layout.VBox;

// every admin page just calls AdminSidebar.build("<its own label>")

public class AdminSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new AdminDashboardPage())),
                new Sidebar.SectionHeader("MANAGEMENT"),
                new Sidebar.NavItem("Student", active.equals("Student"),
                        () -> Main.showPage(new AdminStudentPage())),
                new Sidebar.NavItem("Teacher", active.equals("Teacher"),
                        () -> Main.showPage(new AdminTeacherPage())),
                new Sidebar.NavItem("Group", active.equals("Group"),
                        () -> Main.showPage(new AdminGroupPage())),
                new Sidebar.NavItem("Course", active.equals("Course"),
                        () -> Main.showPage(new CoursePage(AdminSidebar.build("Course"), "Administrator"))),
                new Sidebar.NavItem("Own Profile", active.equals("Own Profile"),
                        () -> Main.showPage(new OwnProfilePage(AdminSidebar.build("Own Profile"), "Administrator"))),
                new Sidebar.SectionHeader("REPORTS"),
                new Sidebar.NavItem("Student's report", active.equals("Student's report"),
                        () -> Main.showPage(new StudentReportPage(AdminSidebar.build("Student's report"), "Administrator"))),
                new Sidebar.NavItem("Course's report", active.equals("Course's report"),
                        () -> Main.showPage(new CourseReportPage(AdminSidebar.build("Course's report"), "Administrator")))
        );
    }
}
