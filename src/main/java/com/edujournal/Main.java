package com.edujournal;

import com.edujournal.ui.LoginPage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point. No FXML — pages are plain Java classes.
 * Run via Launcher (not this class directly) to avoid IntelliJ's
 * "JavaFX runtime components are missing" error.
 */
public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("EduJournal");
        showPage(new LoginPage());
        stage.show();
    }

    /** Switches the whole window to a different page. */
    public static void showPage(Parent page) {
        Scene scene = new Scene(page, 900, 560);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
