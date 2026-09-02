package com.edujournal.ui.teacher;

import com.edujournal.Main;
import com.edujournal.ui.Sidebar;
import com.edujournal.ui.common.CoursePage;
import com.edujournal.ui.common.CourseReportPage;
import com.edujournal.ui.common.OwnProfilePage;
import javafx.scene.layout.VBox;

// every teacher page just calls TeacherSidebar.build("<its own label>")

public class TeacherSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                "Teacher",
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new TeacherDashboardPage())),
                new Sidebar.NavItem("Course", active.equals("Course"),
                        () -> Main.showPage(new CoursePage(TeacherSidebar.build("Course"), "Teacher"))),
                new Sidebar.NavItem("Assessment", active.equals("Assessment"),
                        () -> Main.showPage(new TeacherAssessmentPage())),
                new Sidebar.NavItem("Own Profile", active.equals("Own Profile"),
                        () -> Main.showPage(new OwnProfilePage(TeacherSidebar.build("Own Profile"), "Teacher"))),
                new Sidebar.NavItem("Course's report", active.equals("Course's report"),
                        () -> Main.showPage(new CourseReportPage(TeacherSidebar.build("Course's report"), "Teacher")))
        );
    }
}
