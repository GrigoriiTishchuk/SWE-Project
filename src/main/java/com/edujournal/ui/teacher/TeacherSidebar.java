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
                new Sidebar.NavItem("Dashboard", active.equals("Dashboard"),
                        () -> Main.showPage(new TeacherDashboardPage())),
                new Sidebar.SectionHeader("MANAGEMENT"),
                new Sidebar.NavItem("Courses", active.equals("Courses"),
                        () -> Main.showPage(new CoursePage(TeacherSidebar.build("Courses"), "Teacher"))),
                new Sidebar.NavItem("Gradebook", active.equals("Gradebook"),
                        () -> Main.showPage(new TeacherAssessmentPage())),
                new Sidebar.NavItem("Own Profile", active.equals("Own Profile"),
                        () -> Main.showPage(new OwnProfilePage(TeacherSidebar.build("Own Profile"), "Teacher"))),
                new Sidebar.SectionHeader("REPORTS"),
                new Sidebar.NavItem("Course's report", active.equals("Course's report"),
                        () -> Main.showPage(new CourseReportPage(TeacherSidebar.build("Course's report"), "Teacher")))
        );
    }
}
