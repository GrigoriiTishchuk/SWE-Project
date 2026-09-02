package com.edujournal.ui;

import com.edujournal.Main;
import com.edujournal.ui.admin.AdminDashboardPage;
import com.edujournal.ui.teacher.TeacherDashboardPage;
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

// No real authentication yet

public class LoginPage extends HBox {

    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();

    public LoginPage() {
        getChildren().addAll(buildBanner(), buildForm());
    }

    private VBox buildBanner() {
        ImageView logo = new ImageView(new Image(
                getClass().getResourceAsStream("/images/edujournal_logo.png")));
        logo.setFitHeight(48);
        logo.setPreserveRatio(true);

        Label tagline = new Label("Teacher's Gradebook\nand Report Card System");
        tagline.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2F6FED;");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox banner = new VBox(logo, spacer, tagline);
        banner.setPadding(new Insets(32));
        banner.setPrefWidth(400);
        banner.setStyle("-fx-background-image: url('/images/background.jpg'); -fx-background-size: cover; -fx-background-position: center;");
        return banner;
    }

    private VBox buildForm() {
        emailField.setPromptText("Enter your email");
        passwordField.setPromptText("Enter your password");

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
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                forgotPassword, signIn
        );
        form.setPadding(new Insets(40));
        form.setPrefWidth(400);
        return form;
    }

    private void onSignIn() {
        // TODO: authenticate and navigate to the right dashboard by role
        Main.showPage(new TeacherDashboardPage());
    }

    private void onForgotPassword() {
        // TODO: forgot-password logic
    }
}
