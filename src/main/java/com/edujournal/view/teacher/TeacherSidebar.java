package com.edujournal.view.teacher;

import com.edujournal.Main;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.view.Sidebar;
import com.edujournal.view.common.CoursePage;
import com.edujournal.view.common.CourseReportPage;
import com.edujournal.view.common.OwnProfilePage;
import javafx.scene.layout.VBox;

// every teacher page just calls TeacherSidebar.build("<its own label>")

public class TeacherSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                new Sidebar.NavItem(LocalizationManager.get("dashboard"), active.equals("Dashboard"),
                        () -> Main.showPage(new TeacherDashboardPage())),
                new Sidebar.SectionHeader(LocalizationManager.get("management")),
                new Sidebar.NavItem(LocalizationManager.get("courses"), active.equals("Courses"),
                        () -> Main.showPage(new CoursePage(TeacherSidebar.build("Courses"), Role.TEACHER))),
                new Sidebar.NavItem(LocalizationManager.get("gradebook"), active.equals("Gradebook"),
                        () -> Main.showPage(new TeacherGradebookPage())),
                new Sidebar.NavItem(LocalizationManager.get("profile"), active.equals("Own Profile"),
                        () -> Main.showPage(new OwnProfilePage(TeacherSidebar.build("Own Profile"), Role.TEACHER))),
                new Sidebar.SectionHeader(LocalizationManager.get("reports")),
                new Sidebar.NavItem(LocalizationManager.get("courseReport"), active.equals("Course's report"),
                        () -> Main.showPage(new CourseReportPage(TeacherSidebar.build("Course's report"), Role.TEACHER)))
        );
    }
}
