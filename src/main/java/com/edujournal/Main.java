package com.edujournal;

import com.edujournal.view.LoginPage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Run via Launcher to avoid missing error

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
        Scene scene = new Scene(page, 1280, 720);
        scene.getRoot().setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
