package com.edujournal;

import com.edujournal.config.LocalizationManager;
import com.edujournal.database.DatabaseInitializer;
import com.edujournal.view.LoginPage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Supplier;

// Run via Launcher to avoid missing error

public class Main extends Application {

    private static Stage stage;
    private static Supplier<Parent> currentPageSupplier;

    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.initialize();
        stage = primaryStage;
        stage.setTitle("EduJournal");
        showPage(new LoginPage());
        stage.show();
    }

    /** Switches the whole window to a different page. */
    public static void showPage(Parent page) {
        page.setNodeOrientation(
                LocalizationManager.getOrientation()
        );

        Scene scene = new Scene(page, 1280, 720);
        scene.getRoot().setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        stage.setScene(scene);
    }

    public static void setCurrentPageSupplier(Supplier<Parent> supplier) {
        currentPageSupplier = supplier;
    }

    public static void reloadPage() {
        if (currentPageSupplier != null) showPage(currentPageSupplier.get());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
