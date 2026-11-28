package com.edujournal.view;

import com.edujournal.Main;
import com.edujournal.backend.service.AuthService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.config.LocalizationManager;
import com.edujournal.entity.Role;
import com.edujournal.view.admin.AdminDashboardPage;
import com.edujournal.view.student.StudentDashboardPage;
import com.edujournal.view.teacher.TeacherDashboardPage;
import javafx.geometry.Insets;
import java.util.Locale;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

/**
 * Login page that handles:
 * <ul>
 *     <li>User authentication</li>
 *     <li>Language selection</li>
 *     <li>Navigation to role-specific dashboards</li>
 * </ul>
 */
public class LoginPage extends StackPane {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = new Label();
    private final AuthService authService = new AuthService();

    private static final String ENGLISH = "English";
    private static final String RUSSIAN = "Русский";
    private static final String TURKISH = "Türkçe";
    private static final String ARABIC = "العربية";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    /**
     * Creates the login page with a banner,
     * authentication form and language selector.
     */
    public LoginPage() {
        Main.setCurrentPageSupplier(LoginPage::new);

        HBox main = new HBox(buildBanner(), buildForm());

        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll(ENGLISH, TURKISH, RUSSIAN, ARABIC);
        languageSelector.setValue(localeToLanguage(LocalizationManager.getCurrentLocale()));
        languageSelector.valueProperty().addListener((obs, old, val) -> {
            Locale locale = switch (val) {
                case RUSSIAN -> new Locale("ru", "RU");
                case TURKISH  -> new Locale("tr", "TR");
                case ARABIC  -> new Locale("ar", "SA");
                default        -> new Locale("en", "US");
            };
            LocalizationManager.setLocale(locale);
            Main.reloadPage();
        });

        StackPane.setAlignment(languageSelector, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(languageSelector, new Insets(10, 16, 0, 0));

        getChildren().addAll(main, languageSelector);
    }

    /**
     * Builds the promotional banner displayed
     * on the left side of the login page.
     *
     * @return configured banner pane
     */
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

    /**
     * Builds the authentication form.
     *
     * @return configured login form
     */
    private VBox buildForm() {
        usernameField.setPromptText(LocalizationManager.get("enterUsername"));
        passwordField.setPromptText(LocalizationManager.get("enterPassword"));

        errorLabel.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 13px;");

        Hyperlink forgotPassword = new Hyperlink(LocalizationManager.get("forgotPassword"));
        forgotPassword.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(LocalizationManager.get("forgotPassword"));
            alert.setHeaderText(null);
            alert.setContentText(LocalizationManager.get("forgotPasswordContact"));
            alert.showAndWait();
        });

        Button signIn = new Button(LocalizationManager.get("signIn"));
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setStyle("-fx-background-color: #2F6FED; -fx-text-fill: white; -fx-padding: 10;");
        signIn.setOnAction(e -> onSignIn());

        Label welcome = new Label(LocalizationManager.get("welcome"));
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox form = new VBox(12,
                welcome,
                new Label(LocalizationManager.get("signInto")),
                new Label(LocalizationManager.get(KEY_USERNAME)), usernameField,
                new Label(LocalizationManager.get(KEY_PASSWORD)), passwordField,
                errorLabel,
                forgotPassword, signIn
        );
        form.setPadding(new Insets(40, 40, 40, 70));
        form.setPrefWidth(400);
        form.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return form;
    }

    /**
     * Converts a locale into a language name
     * displayed in the language selector.
     *
     * @param locale current locale
     * @return display name of the language
     */
    private static String localeToLanguage(Locale locale) {
        return switch (locale.getLanguage()) {
            case "ru" -> RUSSIAN;
            case "tr" -> TURKISH;
            case "ar" -> ARABIC;
            default   -> ENGLISH;
        };
    }

    /**
     * Attempts to authenticate the user and
     * redirects to the corresponding dashboard.
     */
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

    /**
     * Opens the dashboard that matches
     * the authenticated user's role.
     *
     * @param role authenticated user role
     */
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

}