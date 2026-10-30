package com.edujournal.view.common;

import com.edujournal.Main;
import com.edujournal.backend.service.StudentService;
import com.edujournal.config.LocalizationManager;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.model.UserDTO;
import com.edujournal.view.admin.AdminSidebar;
import com.edujournal.view.controller.StudentReportController;
import com.edujournal.view.student.StudentSidebar;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StudentReportPage extends BorderPane {
    private final Role role;

    public StudentReportPage(VBox sidebar, Role role, Integer studentId) {
        Main.setCurrentPageSupplier(() -> new StudentReportPage(buildSidebar(role), role, null));
        this.role = role;
        setLeft(sidebar);
        if (role == Role.STUDENT) {
            studentId = resolveCurrentStudentId();
        }
        setCenter(buildContent(studentId));
    }

    private static VBox buildSidebar(Role role) {
        return switch (role) {
            case STUDENT -> StudentSidebar.build("Student's report");
            default -> AdminSidebar.build("Student's report");
        };
    }

    private Integer resolveCurrentStudentId() {
        UserDTO currentUser =
                UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            return null;
        }

        StudentService studentService = new StudentService();

        Student student =
                studentService.findByUserId(currentUser.getId());

        return student != null ? student.getId() : null;
    }

    private VBox buildContent(Integer studentId) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(24));

        box.getChildren().add(TopBar.build(LocalizationManager.get("studentReport"), role, false));

        StudentReportController controller =
                new StudentReportController(role, studentId);

        box.getChildren().add(controller.buildReport());

        return box;
    }
}
