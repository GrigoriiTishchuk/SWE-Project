package com.edujournal.view.common;

import com.edujournal.Main;
import com.edujournal.backend.service.AcademicGroupService;
import com.edujournal.backend.service.AuthService;
import com.edujournal.config.LocalizationManager;
import com.edujournal.backend.service.StudentService;
import com.edujournal.backend.service.UserService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.LocalizationDAO;
import com.edujournal.entity.AcademicGroup;
import com.edujournal.model.UserDTO;
import com.edujournal.entity.Role;
import com.edujournal.entity.Student;
import com.edujournal.entity.User;
import com.edujournal.view.admin.AdminSidebar;
import com.edujournal.view.student.StudentSidebar;
import com.edujournal.view.teacher.TeacherSidebar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays and edits the current user's profile details.
 */
public class OwnProfilePage extends BorderPane {

    private static final String BLUE_BTN =
            "-fx-background-color: #1a3a6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";
    private static final String RED_BTN =
            "-fx-background-color: #DC2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16;";
    private static final String LIGHT_INPUT =
            "-fx-background-color: #DBEAFE; -fx-background-radius: 6; -fx-border-color: transparent;";
    private static final String READONLY_INPUT =
            "-fx-background-color: #F3F4F6; -fx-background-radius: 6; -fx-border-color: transparent; -fx-text-fill: #374151;";

    public OwnProfilePage(VBox sidebar, Role role) {
        Main.setCurrentPageSupplier(() -> new OwnProfilePage(buildSidebar(role), role));
        setLeft(sidebar);
        ScrollPane scroll = new ScrollPane(buildContent(role));
        scroll.setFitToWidth(true);
        scroll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        setCenter(RoleBackground.wrap(role, scroll));
    }

    private static VBox buildSidebar(Role role) {
        return switch (role) {
            case ADMINISTRATOR -> AdminSidebar.build("Own Profile");
            case TEACHER -> TeacherSidebar.build("Own Profile");
            default -> StudentSidebar.build("Own profile");
        };
    }

    private VBox buildContent(Role role) {
        UserDTO sessionUser = UserSession.getInstance().getCurrentUser();

        // Build User from session — avoids a DB fetch just to display the form
        User user = new User();
        user.setId(sessionUser.getId());
        user.setFirstName(sessionUser.getFirstName());
        user.setLastName(sessionUser.getLastName());
        user.setUsername(sessionUser.getUsername());
        user.setEmail(sessionUser.getEmail());
        user.setPhone(sessionUser.getPhone());
        user.setRole(sessionUser.getRole());

        Student student = null;
        if (role == Role.STUDENT) {
            student = new StudentService().findByUserId(sessionUser.getId());
        }

        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox changePasswordCard = buildChangePasswordCard(sessionUser.getUsername());
        changePasswordCard.setVisible(false);
        changePasswordCard.setManaged(false);

        HBox body = new HBox(16, buildAvatarCard(role, nameLabel), buildFormCard(user, student, role, nameLabel, changePasswordCard));
        body.setAlignment(Pos.TOP_LEFT);

        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(TopBar.build(LocalizationManager.get("profile"), role, false), body,
                changePasswordCard);
        return content;
    }

    private VBox buildAvatarCard(Role role, Label nameLabel) {
        Circle avatar = new Circle(40, Color.web("#9CA3AF"));

        String badgeLang = LocalizationManager.getCurrentLocale().toString();
        Label badge = new Label(new LocalizationDAO().getRoleLabel(role.name(), badgeLang));
        badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1a3a6b; "
                + "-fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 12;");

        VBox card = new VBox(14, avatar, nameLabel, badge);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28));
        card.getStyleClass().add("card");
        card.setPrefWidth(200);
        return card;
    }

    private VBox buildFormCard(User user, Student student, Role role, Label nameLabel, VBox changePasswordCard) {
        Label title = new Label(LocalizationManager.get("personalInfo"));
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
                row(LocalizationManager.get("firstName"), firstName),
                row(LocalizationManager.get("lastName"),  lastName),
                row(LocalizationManager.get("username"),  username),
                row(LocalizationManager.get("email"),     email),
                row(LocalizationManager.get("phone"),     phone)
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
                    row(LocalizationManager.get("studentNumber"), studentNumber),
                    row(LocalizationManager.get("dateOfBirth"),   dobField),
                    row(LocalizationManager.get("group"),         groupField)
            );
        }

        form.getChildren().add(row(LocalizationManager.get("role"), roleField));

        boolean[] editing = {false};
        java.util.Map<TextField, String> originals = new java.util.HashMap<>();

        Button editSaveBtn = new Button(LocalizationManager.get("edit"));
        editSaveBtn.setStyle(BLUE_BTN);

        Button cancelBtn = new Button(LocalizationManager.get("cancel"));
        cancelBtn.setStyle(BLUE_BTN);
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);

        final Student finalStudent = student;
        final TextField finalDob = dobField;

        cancelBtn.setOnAction(e -> {
            for (TextField tf : editableFields) {
                tf.setText(originals.getOrDefault(tf, tf.getText()));
                tf.setEditable(false);
                tf.setStyle(READONLY_INPUT);
            }
            editSaveBtn.setText(LocalizationManager.get("edit"));
            cancelBtn.setVisible(false);
            cancelBtn.setManaged(false);
            editing[0] = false;
        });

        editSaveBtn.setOnAction(e -> {
            if (!editing[0]) {
                for (TextField tf : editableFields) {
                    originals.put(tf, tf.getText());
                    tf.setEditable(true);
                    tf.setStyle(LIGHT_INPUT);
                }
                editSaveBtn.setText(LocalizationManager.get("saveChanges"));
                cancelBtn.setVisible(true);
                cancelBtn.setManaged(true);
            } else {
                LocalDate dateOfBirth = null;
                if (role == Role.STUDENT && finalStudent != null && finalDob != null) {
                    String dob = finalDob.getText().trim();
                    if (!dob.isEmpty()) {
                        try {
                            dateOfBirth = LocalDate.parse(dob);
                        } catch (DateTimeParseException ex) {
                            new Alert(Alert.AlertType.WARNING,
                                    "Invalid date of birth. Use yyyy-MM-dd.").showAndWait();
                            return;
                        }
                    }
                }

                user.setFirstName(firstName.getText().trim());
                user.setLastName(lastName.getText().trim());
                user.setUsername(username.getText().trim());
                user.setEmail(email.getText().trim());
                user.setPhone(phone.getText().trim());
                new UserService().update(user);

                UserDTO session = UserSession.getInstance().getCurrentUser();
                session.setFirstName(user.getFirstName());
                session.setLastName(user.getLastName());
                session.setUsername(user.getUsername());
                session.setEmail(user.getEmail());
                session.setPhone(user.getPhone());
                if (role == Role.STUDENT && finalStudent != null && finalDob != null) {
                    if (dateOfBirth != null) {
                        finalStudent.setDateOfBirth(dateOfBirth);
                    }
                    new StudentService().update(finalStudent);
                }

                nameLabel.setText(user.getFirstName() + " " + user.getLastName());

                for (TextField tf : editableFields) {
                    tf.setEditable(false);
                    tf.setStyle(READONLY_INPUT);
                }
                editSaveBtn.setText(LocalizationManager.get("edit"));
                cancelBtn.setVisible(false);
                cancelBtn.setManaged(false);
            }
            editing[0] = !editing[0];
        });

        Button changePasswordBtn = new Button(LocalizationManager.get("changePassword"));
        changePasswordBtn.setStyle(RED_BTN);
        changePasswordBtn.setOnAction(e -> {
            boolean showing = changePasswordCard.isVisible();
            changePasswordCard.setVisible(!showing);
            changePasswordCard.setManaged(!showing);
        });

        HBox btnRow = new HBox(8, editSaveBtn, cancelBtn, changePasswordBtn);
        form.getChildren().add(btnRow);
        form.setPadding(new Insets(24));
        form.getStyleClass().add("card");
        HBox.setHgrow(form, Priority.ALWAYS);
        return form;
    }

    private VBox buildChangePasswordCard(String username) {
        Label title = new Label(LocalizationManager.get("changePassword"));
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText(LocalizationManager.get("currentPassword"));
        currentPasswordField.setMaxWidth(Double.MAX_VALUE);
        currentPasswordField.setStyle(LIGHT_INPUT);

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText(LocalizationManager.get("newPasswordPrompt"));
        newPasswordField.setMaxWidth(Double.MAX_VALUE);
        newPasswordField.setStyle(LIGHT_INPUT);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText(LocalizationManager.get("confirmPassword"));
        confirmPasswordField.setMaxWidth(Double.MAX_VALUE);
        confirmPasswordField.setStyle(LIGHT_INPUT);

        Button saveBtn = new Button(LocalizationManager.get("saveChanges"));
        saveBtn.setStyle(BLUE_BTN);

        AuthService authService = new AuthService();

        saveBtn.setOnAction(e -> {
            String current = currentPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirm = confirmPasswordField.getText();

            if (!authService.verifyPassword(username, current)) {
                new Alert(Alert.AlertType.WARNING, LocalizationManager.get("incorrectPassword")).showAndWait();
                return;
            }
            if (newPass.length() < 9) {
                new Alert(Alert.AlertType.WARNING, LocalizationManager.get("passwordMinLength")).showAndWait();
                return;
            }
            if (!newPass.equals(confirm)) {
                new Alert(Alert.AlertType.WARNING, LocalizationManager.get("passwordsDoNotMatch")).showAndWait();
                return;
            }

            boolean success = authService.resetPassword(username, newPass);
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, LocalizationManager.get("passwordResetSuccess")).showAndWait();
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, LocalizationManager.get("userNotFound")).showAndWait();
            }
        });

        VBox card = new VBox(12, title,
                row(LocalizationManager.get("currentPassword"), currentPasswordField),
                row(LocalizationManager.get("newPasswordLabel"), newPasswordField),
                row(LocalizationManager.get("confirmPassword"), confirmPasswordField),
                saveBtn);
        card.setPadding(new Insets(24));
        card.getStyleClass().add("card");
        return card;
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
