package com.edujournal.view;

import com.edujournal.Main;
import com.edujournal.backend.service.AuthService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.backend.utils.InputValidator;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.view.admin.AdminDashboardPage;
import com.edujournal.view.student.StudentDashboardPage;
import com.edujournal.view.teacher.TeacherDashboardPage;
import javafx.geometry.Insets;
import java.util.Locale;
/*
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
*/

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginPage extends StackPane {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = new Label();
    private final AuthService authService = new AuthService();

    public LoginPage() {
        Main.setCurrentPageSupplier(LoginPage::new);

        HBox main = new HBox(buildBanner(), buildForm());

        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("English", "Türkçe", "Русский", "العربية");
        languageSelector.setValue(localeToLanguage(LocalizationManager.getCurrentLocale()));
        languageSelector.valueProperty().addListener((obs, old, val) -> {
            Locale locale = switch (val) {
                case "Русский" -> new Locale("ru", "RU");
                case "Türkçe"  -> new Locale("tr", "TR");
                case "العربية"  -> new Locale("ar", "SA");
                default        -> new Locale("en", "US");
            };
            LocalizationManager.setLocale(locale);
            Main.reloadPage();
        });

        StackPane.setAlignment(languageSelector, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(languageSelector, new Insets(10, 16, 0, 0));

        getChildren().addAll(main, languageSelector);
    }

    private StackPane buildBanner() {
        ImageView bg = new ImageView(new Image(
                getClass().getResourceAsStream("/images/background.jpg")));
        bg.setPreserveRatio(false);
        GaussianBlur blur = new GaussianBlur(8);
        blur.setInput(new ColorAdjust(0, 0, 0.55, 0));
        bg.setEffect(blur);

        ImageView logo = new ImageView(new Image(
                getClass().getResourceAsStream("/images/edujournal_logo.png")));
        logo.setFitHeight(48);
        VBox.setMargin(logo, new Insets(0, 0, 35, 0));
        logo.setPreserveRatio(true);

        Label tagline = new Label(LocalizationManager.get("projectTitle"));
        tagline.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #0d06b6;");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox.setMargin(tagline, new Insets(0, 0, 25, 0));
        VBox content = new VBox(logo, spacer, tagline);
        content.setPadding(new Insets(32));

        StackPane banner = new StackPane(bg, content);
        banner.setPrefWidth(600);
        bg.fitWidthProperty().bind(banner.widthProperty());
        bg.fitHeightProperty().bind(banner.heightProperty());
        StackPane.setAlignment(content, javafx.geometry.Pos.TOP_LEFT);
        return banner;
    }

    private VBox buildForm() {
        usernameField.setPromptText(LocalizationManager.get("enterUsername"));
        passwordField.setPromptText(LocalizationManager.get("enterPassword"));

        errorLabel.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 13px;");

        Hyperlink forgotPassword = new Hyperlink(LocalizationManager.get("forgotPassword"));
        forgotPassword.setOnAction(e -> onForgotPassword());

        Button signIn = new Button(LocalizationManager.get("signIn"));
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setStyle("-fx-background-color: #2F6FED; -fx-text-fill: white; -fx-padding: 10;");
        signIn.setOnAction(e -> onSignIn());

        Label welcome = new Label(LocalizationManager.get("welcome"));
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox form = new VBox(12,
                welcome,
                new Label(LocalizationManager.get("signInto")),
                new Label(LocalizationManager.get("username")), usernameField,
                new Label(LocalizationManager.get("password")), passwordField,
                errorLabel,
                forgotPassword, signIn
        );
        form.setPadding(new Insets(40, 40, 40, 70));
        form.setPrefWidth(400);
        form.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return form;
    }

    private static String localeToLanguage(Locale locale) {
        return switch (locale.getLanguage()) {
            case "ru" -> "Русский";
            case "tr" -> "Türkçe";
            case "ar" -> "العربية";
            default   -> "English";
        };
    }

    private void onSignIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.login(username, password)) {
            errorLabel.setText("");
            Role role = UserSession.getInstance().getCurrentUser().getRole();
            navigateToDashboard(role);
        } else {
            errorLabel.setText(LocalizationManager.get("invalidNamePassword"));
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
                errorLabel.setText(LocalizationManager.get("unknownRole"));
                break;
        }
    }

    private void onForgotPassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(LocalizationManager.get("resetPassword"));
        dialog.setHeaderText(LocalizationManager.get("resetPasswordHeader"));

        ButtonType resetButtonType = new ButtonType(LocalizationManager.get("resetPassword"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField usernameInput = new TextField();
        usernameInput.setPromptText(LocalizationManager.get("username"));

        PasswordField newPasswordInput = new PasswordField();
        newPasswordInput.setPromptText(LocalizationManager.get("newPasswordPrompt"));

        grid.add(new Label(LocalizationManager.get("username") + ":"), 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(new Label(LocalizationManager.get("newPasswordLabel")), 0, 1);
        grid.add(newPasswordInput, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == resetButtonType) {
                String username = usernameInput.getText();
                String newPassword = newPasswordInput.getText();

                if (!InputValidator.isValidName(username)) {
                    showResultAlert(false, LocalizationManager.get("usernameEmpty"));
                    return;
                }

                if (!InputValidator.isValidPassword(newPassword)) {
                    showResultAlert(false, LocalizationManager.get("passwordMinLength"));
                    return;
                }

                boolean success = authService.resetPassword(username, newPassword);
                showResultAlert(success, success
                        ? LocalizationManager.get("passwordResetSuccess")
                        : LocalizationManager.get("userNotFound"));
            }
        });
    }

    private void showResultAlert(boolean isSuccess, String message) {
        Alert alert = new Alert(isSuccess ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(LocalizationManager.get("passwordResetResult"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}