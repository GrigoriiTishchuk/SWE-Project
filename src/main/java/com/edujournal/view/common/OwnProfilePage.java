package com.edujournal.view.common;

import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnProfilePage extends BorderPane {

    private static final String PANEL =
            "-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;";
    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";
    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; -fx-border-color: transparent;";
    private static final String READONLY_INPUT =
            "-fx-background-color: #F3F4F6; -fx-background-radius: 6; -fx-border-color: transparent; -fx-text-fill: #374151;";

    public OwnProfilePage(VBox sidebar, Role role) {
        setLeft(sidebar);
        ScrollPane scroll = new ScrollPane(buildContent(role));
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        setCenter(scroll);
    }

    private VBox buildContent(Role role) {
        Integer userId = UserSession.getInstance().getCurrentUser().getId();
        User user = new UserService().findById(userId);

        Student student = null;
        if (role == Role.STUDENT) {
            student = new StudentService().findByUserId(userId);
        }

        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox body = new HBox(16, buildAvatarCard(role, nameLabel), buildFormCard(user, student, role, nameLabel));
        body.setAlignment(Pos.TOP_LEFT);

        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build("Own Profile", role, false), body);
        return content;
    }

    private VBox buildAvatarCard(Role role, Label nameLabel) {
        Circle avatar = new Circle(40, Color.web("#9CA3AF"));

        Label badge = new Label(role.getDisplayName());
        badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1a3a6b; "
                + "-fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 12;");

        VBox card = new VBox(14, avatar, nameLabel, badge);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28));
        card.setStyle(PANEL);
        card.setPrefWidth(200);
        return card;
    }

    private VBox buildFormCard(User user, Student student, Role role, Label nameLabel) {
        Label title = new Label("Personal Information");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField firstName  = createField(user.getFirstName());
        TextField lastName   = createField(user.getLastName());
        TextField username   = createField(user.getUsername());
        TextField email      = createField(user.getEmail());
        TextField phone      = createField(user.getPhone());
        TextField roleField  = createField(role.getDisplayName());
        roleField.setEditable(false);

        List<TextField> editableFields = new ArrayList<>(List.of(email, phone));
        if (role == Role.ADMINISTRATOR) {
            editableFields.add(0, lastName);
            editableFields.add(0, firstName);
        }

        VBox form = new VBox(12, title,
                row("First Name", firstName),
                row("Last Name",  lastName),
                row("Username",   username),
                row("Email",      email),
                row("Phone",      phone)
        );

        TextField dobField = null;
        if (role == Role.STUDENT && student != null) {
            TextField studentNumber = createField(student.getStudentNumber());

            dobField = createField(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "");
            editableFields.add(dobField);

            String groupName = "-";
            if (student.getAcademicGroupId() != null) {
                AcademicGroup group = new AcademicGroupService().findById(student.getAcademicGroupId());
                if (group != null) groupName = group.getName();
            }
            TextField groupField = createField(groupName);

            form.getChildren().addAll(
                    row("Student Number", studentNumber),
                    row("Date of Birth",  dobField),
                    row("Group",          groupField)
            );
        }

        form.getChildren().add(row("Role", roleField));

        boolean[] editing = {false};
        Button editSaveBtn = new Button("Edit");
        editSaveBtn.setStyle(BLUE_BTN);

        final Student finalStudent = student;
        final TextField finalDob = dobField;

        editSaveBtn.setOnAction(e -> {
            if (!editing[0]) {
                for (TextField tf : editableFields) {
                    tf.setEditable(true);
                    tf.setStyle(LIGHT_INPUT);
                }
                editSaveBtn.setText("Save Changes");
            } else {
                user.setFirstName(firstName.getText().trim());
                user.setLastName(lastName.getText().trim());
                user.setUsername(username.getText().trim());
                user.setEmail(email.getText().trim());
                user.setPhone(phone.getText().trim());
                new UserService().update(user);

                if (role == Role.STUDENT && finalStudent != null && finalDob != null) {
                    String dob = finalDob.getText().trim();
                    if (!dob.isEmpty()) {
                        try { finalStudent.setDateOfBirth(LocalDate.parse(dob)); } catch (Exception ignored) {}
                    }
                    new StudentService().update(finalStudent);
                }

                nameLabel.setText(user.getFirstName() + " " + user.getLastName());

                for (TextField tf : editableFields) {
                    tf.setEditable(false);
                    tf.setStyle(READONLY_INPUT);
                }
                editSaveBtn.setText("Edit");
            }
            editing[0] = !editing[0];
        });

        form.getChildren().add(editSaveBtn);
        form.setPadding(new Insets(24));
        form.setStyle(PANEL);
        HBox.setHgrow(form, Priority.ALWAYS);
        return form;
    }

    private TextField createField(String value) {
        TextField tf = new TextField(value != null ? value : "");
        tf.setEditable(false);
        tf.setStyle(READONLY_INPUT);
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private VBox row(String label, TextField tf) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");
        return new VBox(4, lbl, tf);
    }
}
