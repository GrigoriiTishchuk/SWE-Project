package com.edujournal.view.admin;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.Sidebar;
import com.edujournal.view.common.CoursePage;
import com.edujournal.view.common.CourseReportPage;
import com.edujournal.view.common.OwnProfilePage;
import com.edujournal.view.common.StudentReportPage;
import javafx.scene.layout.VBox;
import com.edujournal.config.LocalizationManager;

// every admin page just calls AdminSidebar.build("<its own label>")

public class AdminSidebar {

    public static VBox build(String active) {
        return Sidebar.build(
                new Sidebar.NavItem(LocalizationManager.get("dashboard"), active.equals("Dashboard"),
                        () -> Main.showPage(new AdminDashboardPage())),
                new Sidebar.SectionHeader(LocalizationManager.get("management")),
                new Sidebar.NavItem(LocalizationManager.get("students"), active.equals("Students"),
                        () -> Main.showPage(new AdminStudentPage(AdminSidebar.build("Students"), Role.ADMINISTRATOR))),
                new Sidebar.NavItem(LocalizationManager.get("teachers"), active.equals("Teachers"),
                        () -> Main.showPage(new AdminTeacherPage(AdminSidebar.build("Teachers"), Role.ADMINISTRATOR))),
                new Sidebar.NavItem(LocalizationManager.get("groups"), active.equals("Groups"),
                        () -> Main.showPage(new AdminGroupPage(AdminSidebar.build("Groups"), Role.ADMINISTRATOR))),
                new Sidebar.NavItem(LocalizationManager.get("courses"), active.equals("Courses"),
                        () -> Main.showPage(new CoursePage(AdminSidebar.build("Courses"), Role.ADMINISTRATOR))),
                new Sidebar.NavItem(LocalizationManager.get("profile"), active.equals("Own Profile"),
                        () -> Main.showPage(new OwnProfilePage(AdminSidebar.build("Own Profile"), Role.ADMINISTRATOR))),
                new Sidebar.SectionHeader(LocalizationManager.get("reports")),
                new Sidebar.NavItem(LocalizationManager.get("studentReport"), active.equals("Student's report"),
                        () -> Main.showPage(new StudentReportPage(AdminSidebar.build("Student's report"), Role.ADMINISTRATOR, null))),
                new Sidebar.NavItem(LocalizationManager.get("courseReport"), active.equals("Course's report"),
                        () -> Main.showPage(new CourseReportPage(AdminSidebar.build("Course's report"), Role.ADMINISTRATOR)))
        );
    }
}
