package com.edujournal.ui;

import com.edujournal.Main;
import com.edujournal.ui.admin.AdminDashboardPage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Skeleton login page — matches the Figma login screen fields.
 * No real authentication yet.
 */
public class LoginPage extends HBox {

    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();

    public LoginPage() {
        VBox banner = new VBox(8);
        banner.setPadding(new Insets(40));
        banner.setPrefWidth(400);
        banner.setStyle("-fx-background-color: #EAF1FD;");

        Label brand = new Label("EduJournal");
        brand.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label tagline = new Label("Teacher's Gradebook and Report Card System");
        tagline.setWrapText(true);
        tagline.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1B2A4A;");

        banner.getChildren().addAll(brand, tagline);

        VBox form = new VBox(12);
        form.setPadding(new Insets(40));
        form.setPrefWidth(400);

        Label welcome = new Label("Welcome back");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Sign into your account");
        subtitle.setStyle("-fx-text-fill: #6B7280;");

        emailField.setPromptText("Enter your email");
        passwordField.setPromptText("Enter your password");

        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setOnAction(e -> onForgotPassword());

        Button signIn = new Button("Sign in");
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setStyle("-fx-background-color: #2F6FED; -fx-text-fill: white; -fx-padding: 10;");
        signIn.setOnAction(e -> onSignIn());

        form.getChildren().addAll(
                welcome, subtitle,
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                forgotPassword, signIn
        );

        getChildren().addAll(banner, form);
    }

    private void onSignIn() {
        // TODO: authenticate and navigate to the right dashboard by role
        Main.showPage(new AdminDashboardPage());
    }

    private void onForgotPassword() {
        // TODO: forgot-password flow
        System.out.println("Forgot password clicked");
    }
}
