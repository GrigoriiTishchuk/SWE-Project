package com.edujournal.view.student;

import com.edujournal.Main;
import com.edujournal.entity.Role;
import com.edujournal.view.Sidebar;
import com.edujournal.view.common.OwnProfilePage;
import com.edujournal.view.common.StudentReportPage;
import javafx.scene.layout.VBox;
import com.edujournal.config.LocalizationManager;

/**
 * Builds the student navigation menu.
 */
public class StudentSidebar {

    /**
     * Creates the student sidebar and marks the active destination.
     *
     * @param active label identifying the selected navigation destination
     * @return configured student sidebar
     */
    public static VBox build(String active) {
        return Sidebar.build(
                new Sidebar.NavItem(LocalizationManager.get("dashboard"), active.equals("Dashboard"),
                        () -> Main.showPage(new StudentDashboardPage())),
                new Sidebar.SectionHeader(LocalizationManager.get("management")),
                new Sidebar.NavItem(LocalizationManager.get("profile"), active.equals("Own profile"),
                        () -> Main.showPage(new OwnProfilePage(StudentSidebar.build("Own profile"), Role.STUDENT))),
                new Sidebar.SectionHeader(LocalizationManager.get("reports")),
                new Sidebar.NavItem(LocalizationManager.get("studentReport"), active.equals("Student's report"),
                        () -> Main.showPage(new StudentReportPage(StudentSidebar.build("Student's report"), Role.STUDENT, null)))
        );
    }
}
