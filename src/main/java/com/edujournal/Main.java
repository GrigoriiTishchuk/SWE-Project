package com.edujournal;

import com.edujournal.config.LocalizationManager;
import com.edujournal.database.DatabaseInitializer;
import com.edujournal.view.LoginPage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Supplier;

/**
 * Main JavaFX application entry point.
 * Responsible for application startup,
 * database initialization, and page navigation.
 */
public class Main extends Application {

    private static Stage stage;
    private static Supplier<Parent> currentPageSupplier;

    /**
     * Initializes the application, prepares the database,
     * and displays the login page.
     *
     * @param primaryStage main application stage
     */
    @SuppressWarnings("java:S2696")
    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.initialize();
        stage = primaryStage;
        stage.setTitle("EduJournal");
        showPage(new LoginPage());
        stage.show();
    }

    /**
     * Switches the application to the specified page.
     *
     * @param page page to display
     */
    public static void showPage(Parent page) {
        page.setNodeOrientation(
                LocalizationManager.getOrientation()
        );

        Scene scene = new Scene(page, 1280, 720);
        scene.getRoot().setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-base-color: #111827;");
        scene.getStylesheets().add(Main.class.getResource("/css/components.css").toExternalForm());
        stage.setScene(scene);
    }

    /**
     * Sets the supplier used to recreate the current page.
     * This is mainly used when reloading the UI after
     * localization changes.
     *
     * @param supplier page supplier
     */
    public static void setCurrentPageSupplier(Supplier<Parent> supplier) {
        currentPageSupplier = supplier;
    }

    /**
     * Reloads the currently displayed page.
     */
    public static void reloadPage() {
        if (currentPageSupplier != null) showPage(currentPageSupplier.get());
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
