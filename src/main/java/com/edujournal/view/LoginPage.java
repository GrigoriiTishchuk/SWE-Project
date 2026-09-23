package com.edujournal.view;

import com.edujournal.Main;
import com.edujournal.backend.service.AuthService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.entity.Role;
import com.edujournal.view.admin.AdminDashboardPage;
import com.edujournal.view.student.StudentDashboardPage;
import com.edujournal.view.teacher.TeacherDashboardPage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginPage extends HBox {

    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = new Label();
    private final AuthService authService = new AuthService();

    public LoginPage() {
        getChildren().addAll(buildBanner(), buildForm());
    }

    private VBox buildBanner() {
        ImageView logo = new ImageView(new Image(
                getClass().getResourceAsStream("/images/edujournal_logo.png")));
        logo.setFitHeight(48);
        logo.setPreserveRatio(true);

        Label tagline = new Label("Teacher's Gradebook\nand Report Card System");
        tagline.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #2F6FED;");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox banner = new VBox(logo, spacer, tagline);
        banner.setPadding(new Insets(32));
        banner.setPrefWidth(600);
        banner.setStyle("-fx-background-image: url('/images/background.jpg'); -fx-background-size: cover; -fx-background-position: center;");
        return banner;
    }

    private VBox buildForm() {
        emailField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");

        errorLabel.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 13px;");

        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setOnAction(e -> onForgotPassword());

        Button signIn = new Button("Sign in");
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setStyle("-fx-background-color: #2F6FED; -fx-text-fill: white; -fx-padding: 10;");
        signIn.setOnAction(e -> onSignIn());

        Label welcome = new Label("Welcome back");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox form = new VBox(12,
                welcome,
                new Label("Sign into your account"),
                new Label("Username"), emailField,
                new Label("Password"), passwordField,
                errorLabel,
                forgotPassword, signIn
        );
        form.setPadding(new Insets(40));
        form.setPrefWidth(400);
        form.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return form;
    }

    private void onSignIn() {
        String username = emailField.getText();
        String password = passwordField.getText();

        if (authService.login(username, password)) {
            errorLabel.setText("");
            Role role = UserSession.getInstance().getCurrentUser().getRole();
            navigateToDashboard(role);
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    private void navigateToDashboard(Role role) {
        if (role == null) return;

        switch (role) {
            case ADMINISTRATOR:
                Main.showPage(new AdminDashboardPage());
                break;
            case TEACHER:
                Main.showPage(new TeacherDashboardPage());
                break;
            case STUDENT:
                Main.showPage(new StudentDashboardPage());
                break;
            default:
                errorLabel.setText("Unknown user role.");
                break;
        }
    }

    private void onForgotPassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reset Password");
        dialog.setHeaderText("Enter your username and new password");

        ButtonType resetButtonType = new ButtonType("Reset Password", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");

        PasswordField newPasswordInput = new PasswordField();
        newPasswordInput.setPromptText("New password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPasswordInput, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == resetButtonType) {
                String username = usernameInput.getText();
                String newPassword = newPasswordInput.getText();

                boolean success = authService.resetPassword(username, newPassword);

                Alert alert = new Alert(
                        success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR
                );
                alert.setTitle("Password Reset Result");
                alert.setHeaderText(null);
                alert.setContentText(
                        success
                                ? "Password successfully updated! You can now sign in."
                                : "User not found. Check your Username."
                );
                alert.showAndWait();
            }
        });
    }
}